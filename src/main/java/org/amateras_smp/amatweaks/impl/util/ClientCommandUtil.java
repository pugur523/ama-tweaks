package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.AmaTweaks;
import org.amateras_smp.amatweaks.config.Configs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//#if MC < 11900
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
    // Pairs of (alias, fullCommand).
    private static final HashMap<CommandMeta, CommandMeta> commandAliasMap = new HashMap<>();

    public static ArrayList<String> initAndGetCommands() {
        ArrayList<String> commandsList = new ArrayList<>();
        for (String entry : Configs.Lists.CUSTOM_COMMAND_ALIASES_MAP.getStrings()) {
            if (!entry.contains(";")) continue;
            entry = entry.strip();
            String[] splitted = entry.split("\s*;\s*");
            CommandMeta aliasMeta = getCommandMeta(splitted[0].strip());
            CommandMeta fullCommandMeta = getCommandMeta(splitted[1].strip());
            commandsList.add(aliasMeta.command);
            commandAliasMap.put(aliasMeta, fullCommandMeta);

            AmaTweaks.LOGGER.debug(aliasMeta);
            AmaTweaks.LOGGER.debug(fullCommandMeta);
        }
        return commandsList;
    }

    public static boolean executeCommand(String input) {
        AmaTweaks.LOGGER.debug("Executing client command : \"{}\"", input);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) return false;
        //#if MC >= 11900
        return client.getNetworkHandler().sendCommand(input);
        //#else
        //$$ try {
        //$$     client.getNetworkHandler().getCommandDispatcher()
        //$$         .execute(input, client.getNetworkHandler().getCommandSource());
        //$$ } catch (CommandSyntaxException e) {
        //$$     throw new RuntimeException(e);
        //$$ }
        //$$ return true;
        //#endif
    }

    public static String getCommandFromCustomAlias(String input) {
        CommandMeta inputMeta = getCommandMeta(input);
        for (CommandMeta aliasMeta : commandAliasMap.keySet()) {
            if (inputMeta.command.equals(aliasMeta.command)) {
                CommandMeta fullCommandMeta = commandAliasMap.get(aliasMeta);
                if (fullCommandMeta == null) continue;

                if (inputMeta.arguments.isBlank() || aliasMeta.arguments.isBlank()) return fullCommandMeta.asString();
                if (isWildcardMatch(inputMeta.arguments, aliasMeta.arguments)) {
                    // applyWildCard(fullCommandMeta, inputMeta, aliasMeta);
                    // return fullCommandMeta.asString();
                    fullCommandMeta.arguments = inputMeta.arguments;
                    return fullCommandMeta.asString();
                }
            }
        }
        return "";
    }

    public static void applyWildCard(CommandMeta source, CommandMeta input, CommandMeta alias) {
        AmaTweaks.LOGGER.debug("applying wildcard");
        String args = source.arguments;
        List<String> matchResult = matchPattern(input.arguments, alias.arguments);

        AmaTweaks.LOGGER.debug(matchResult);

        if (matchResult == null || matchResult.size() != countWildcards(alias.arguments)) {
            return;
        }
        int argsIndex = 0;
        int resultIndex = 0;
        StringBuilder result = new StringBuilder();

        while (argsIndex < args.length()) {
            char argChar = args.charAt(argsIndex);

            if (argChar == '*' || argChar == '?') {
                result.append(matchResult.get(resultIndex));
                resultIndex++;
            } else {
                result.append(argChar);
            }

            argsIndex++;
        }
        AmaTweaks.LOGGER.debug(result.toString());
        source.arguments = result.toString();
    }

    private static List<String> matchPattern(String input, String pattern) {
        List<String> result = new ArrayList<>();

        int patternIndex = 0;
        int otherIndex = 0;

        while (patternIndex < pattern.length() && otherIndex < input.length()) {
            char patternChar = pattern.charAt(patternIndex);
            char otherChar = input.charAt(otherIndex);

            if (patternChar == '?') {
                result.add(String.valueOf(otherChar));
                patternIndex++;
                otherIndex++;
            } else if (patternChar == '*') {
                if (patternIndex == pattern.length() - 1) {
                    result.add(input.substring(otherIndex));
                    break;
                } else {
                    char nextPatternChar = pattern.charAt(patternIndex + 1);
                    while (otherIndex < input.length() && input.charAt(otherIndex) != nextPatternChar) {
                        result.add(String.valueOf(input.charAt(otherIndex)));
                        otherIndex++;
                    }
                    patternIndex++;
                }
            } else if (patternChar == otherChar) {
                result.add(String.valueOf(otherChar));
                patternIndex++;
                otherIndex++;
            } else {
                return null;
            }
        }

        while (patternIndex < pattern.length() && pattern.charAt(patternIndex) == '*') {
            patternIndex++;
        }

        if (patternIndex == pattern.length() && otherIndex == input.length()) {
            return result;
        }

        return null;
    }

    private static int countWildcards(String pattern) {
        int count = 0;
        for (char c : pattern.toCharArray()) {
            if (c == '*' || c == '?') {
                count++;
            }
        }
        return count;
    }

    private static boolean isWildcardMatch(String str, String pattern) {
        int m = str.length();
        int n = pattern.length();

        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;

        for (int j = 1; j <= n; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1];
            }
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char currentStr = str.charAt(i - 1);
                char currentPattern = pattern.charAt(j - 1);

                if (currentPattern == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else if (currentPattern == currentStr) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }

        return dp[m][n];
    }

    private static int getCommandEndIndex(String commandWithArguments) {
        if (commandWithArguments == null || commandWithArguments.isBlank()) {
            return -1;
        }
        int firstSemicolonIndex = commandWithArguments.indexOf(';');
        int firstSpaceIndex = commandWithArguments.indexOf(' ');
        int firstTabIndex = commandWithArguments.indexOf('\t');

        if (firstSemicolonIndex == -1) firstSemicolonIndex = Integer.MAX_VALUE;
        if (firstSpaceIndex == -1) firstSpaceIndex = Integer.MAX_VALUE;
        if (firstTabIndex == -1) firstTabIndex = Integer.MAX_VALUE;

        int ret = Math.min(firstSemicolonIndex, (Math.min(firstSpaceIndex, firstTabIndex)));
        if (ret == Integer.MAX_VALUE) ret = -1;
        return ret;
    }

    private static CommandMeta getCommandMeta(String s) {
        int endIndex = getCommandEndIndex(s);
        if (endIndex == s.length() || endIndex < 0) {
            return new CommandMeta(s, "");
        }
        String command = s.substring(0, endIndex).strip();
        String arguments = s.substring(endIndex).strip();
        return new CommandMeta(command, arguments);
    }
}
