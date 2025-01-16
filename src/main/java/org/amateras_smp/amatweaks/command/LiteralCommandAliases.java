package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.Formatting;
import org.amateras_smp.amatweaks.AmaTweaks;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.ClientCommandUtil;
import org.amateras_smp.amatweaks.impl.util.TextUtil;

public class LiteralCommandAliases {
    public static Command<FabricClientCommandSource> command = LiteralCommandAliases::callback;

    private static int callback(CommandContext<FabricClientCommandSource> context) {
        if (!Configs.Generic.CUSTOM_COMMAND_ALIASES.getBooleanValue()) {
            context.getSource().sendFeedback(TextUtil.withFormat("customCommandAliases is disabled.", Formatting.RED));
            return Command.SINGLE_SUCCESS;
        }
        String aliasInput = context.getInput();
        aliasInput = aliasInput.strip();
        AmaTweaks.LOGGER.debug(aliasInput);
        String fullCommand = ClientCommandUtil.getCommandFromCustomAlias(aliasInput);
        if (fullCommand.isBlank()) {
            context.getSource().sendFeedback(TextUtil.withFormat("Cannot get command from alias \"" + aliasInput + "\", please restart the client.", Formatting.RED));
            return Command.SINGLE_SUCCESS;
        }
        ClientCommandUtil.executeCommand(fullCommand);
        return Command.SINGLE_SUCCESS;
    }
}
