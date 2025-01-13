package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.ClientCommandUtil;

public class LiteralCommandAliases {
    public static Command<FabricClientCommandSource> command = LiteralCommandAliases::callback;

    private static int callback(CommandContext<FabricClientCommandSource> context) {
        if (!Configs.Generic.CUSTOM_COMMAND_ALIASES.getBooleanValue()) return 0;
        String aliasInput = context.getInput();
        aliasInput = aliasInput.strip();
        String fullCommand = ClientCommandUtil.getCommandFromCustomAlias(aliasInput);
        if (fullCommand.isBlank()) return 0;
        ClientCommandUtil.executeCommand(fullCommand);
        return Command.SINGLE_SUCCESS;
    }
}
