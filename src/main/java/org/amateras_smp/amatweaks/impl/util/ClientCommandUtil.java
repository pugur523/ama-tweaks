package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.AmaTweaks;
import org.amateras_smp.amatweaks.config.Configs;
import java.util.ArrayList;
import java.util.HashMap;

//#if MC < 11900
//$$ import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
    // The pairs of (alias, fullCommand).
    private static final HashMap<CommandMeta, CommandMeta> commandAliasMap = new HashMap<>();

    public static ArrayList<String> initAndGetCommands() {
        ArrayList<String> commandsList = new ArrayList<>();
        for (String entry : Configs.Lists.CUSTOM_COMMAND_ALIASES_MAP.getStrings()) {
            if (!entry.contains(";")) continue;
            entry = entry.strip();
            String[] splitted = entry.split("\s*;\s*");
            AmaTweaks.LOGGER.debug(splitted[0], " ", splitted[1]);
            CommandMeta aliasMeta = getCommandMeta(splitted[0].strip());
            CommandMeta fullCommandMeta = getCommandMeta(splitted[1].strip());
            commandsList.add(aliasMeta.command);
            commandAliasMap.put(aliasMeta, fullCommandMeta);
        }
        return commandsList;
    }

    public static boolean executeCommand(String input) {
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
        CommandMeta meta = getCommandMeta(input);
        for (CommandMeta meta2 : commandAliasMap.keySet()) {
            if (meta.command.equals(meta2.command)) {
                if (meta.arguments.isBlank() || meta2.arguments.isBlank() || !meta2.arguments.contains("*")) return meta2.asString();
                if (isWildcardMatch(meta.arguments, meta2.arguments)) return meta2.command + " " + meta.arguments;
            }
        }
        return "";
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
        int firstSpaceIndex = commandWithArguments.indexOf(' ');
        int firstTabIndex = commandWithArguments.indexOf('\t');
        int firstSemicolonIndex = commandWithArguments.indexOf(';');

        return (firstSpaceIndex == -1) ? firstTabIndex :
                (firstTabIndex == -1) ? firstSpaceIndex :
                        (firstSemicolonIndex == -1) ? firstSemicolonIndex :
                                Math.min(firstSemicolonIndex, (Math.min(firstSpaceIndex, firstTabIndex)));
    }

    private static class CommandMeta {
        String command;
        String arguments;
        public CommandMeta(String command, String arguments) {
            this.command = command;
            this.arguments = arguments;
        }

        public String asString() {
            return (this.command + " " + this.arguments).strip();
        }
    }

    private static CommandMeta getCommandMeta(String s) {
        int endIndex = getCommandEndIndex(s);
        if (endIndex == s.length()) {
            return new CommandMeta(s, "");
        }
        String command = s.substring(0, endIndex);
        String arguments = s.substring(endIndex);
        return new CommandMeta(command, arguments);
    }
}
