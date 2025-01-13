package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.config.Configs;
import java.util.ArrayList;
//#if MC < 11900
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
    public static ArrayList<String> getAliases() {
        ArrayList<String> ret = new ArrayList<>();
        for (String aliasString : Configs.Lists.CUSTOM_COMMAND_ALIASES_MAP.getStrings()) {
            ret.add(getCommand(aliasString));
        }
        return ret;
    }

    public static boolean executeCommand(String input) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return false;
        //#if MC >= 11900
        return MinecraftClient.getInstance().getNetworkHandler().sendCommand(input);
        //#else
        //$$ try {
        //$$     MinecraftClient.getInstance().getNetworkHandler().getCommandDispatcher().execute(input, MinecraftClient.getInstance().getNetworkHandler().getCommandSource());
        //$$ } catch (CommandSyntaxException e) {
        //$$     throw new RuntimeException(e);
        //$$ }
        //$$ return true;
        //#endif
    }

    public static String getCommandFromCustomAlias(String command, String arguments) {
        if (!Configs.Generic.CUSTOM_COMMAND_ALIASES.getBooleanValue()) return "";
        for (String aliasString : Configs.Lists.CUSTOM_COMMAND_ALIASES_MAP.getStrings()) {
            String firstCommand = getCommand(aliasString);
            if (firstCommand.equals(command)) {
                ArrayList<String> parsedAlias = parseCommandAlias(aliasString);
                if (aliasString.contains("*")) {
                    if (isWildcardMatch(parsedAlias.get(1), parsedAlias.get(3))) {
                        return parsedAlias.get(0) + " " + arguments;
                    }
                } else if (parsedAlias.get(1).equals(arguments)) {
                    return parsedAlias.get(0) + " " + parsedAlias.get(1);
                }
            }
        }
        return "";
    }

    private static ArrayList<String> parseCommandAlias(String aliasString) {
        int i = 0;
        for (; i < aliasString.length(); i++) {
            char c = aliasString.charAt(i);
            if (c == ';') {
                break;
            }
        }
        String first = aliasString.substring(0, i);
        String second = aliasString.substring(i + 1);

        ArrayList<String> splitFirst = splitCommandAndArguments(first);
        ArrayList<String> splitSecond = splitCommandAndArguments(second);

        splitFirst.addAll(splitSecond);
        return splitFirst;
    }

    public static ArrayList<String> splitCommandAndArguments(String rawInput) {
        int start = getCommandStartIndex(rawInput);
        int end = getCommandEndIndex(rawInput, start);
        ArrayList<String> ret = new ArrayList<>();
        ret.add(rawInput.substring(start, end));
        ret.add(rawInput.substring(end + 1));
        return ret;
    }

    private static boolean isWildcardMatch(String str, String pattern) {
        int m = str.length();
        int n = pattern.length();

        boolean[][] dp = new boolean[m + 1][n + 1];

        dp[0][0] = true;

        for (int j = 1; j <= n; j++) {
            if (pattern.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1];
            } else {
                break;
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

    private static String getCommand(String commandWithArguments) {
        if (commandWithArguments == null || commandWithArguments.isBlank()) {
            return "";
        }
        int start = getCommandStartIndex(commandWithArguments);
        int end = getCommandEndIndex(commandWithArguments, start);
        return commandWithArguments.substring(start, end);
    }

    private static int getCommandStartIndex(String commandWithArguments) {
        return searchBlank(commandWithArguments, 0, commandWithArguments.length());
    }

    private static int getCommandEndIndex(String commandWithArguments, int startIdx) {
        return searchBlank(commandWithArguments, startIdx, commandWithArguments.length());
    }

    private static int searchBlank(String input, int start, int end) {
        if (input == null || input.isBlank()) return 0;
        int i = start;
        for (; i < end; i++) {
            boolean isBlank = input.charAt(i) == ' ' || input.charAt(i) == '\t';
            if (isBlank) break;
        }
        return i;
    }
}
