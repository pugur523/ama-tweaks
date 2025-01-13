package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.impl.util.ClientCommandUtil;
import java.util.ArrayList;

public class LiteralCommandAliases {
    public static Command<FabricClientCommandSource> command = LiteralCommandAliases::callback;

    public static int callback(CommandContext<FabricClientCommandSource> context) {
        String aliasCommand = context.getInput();
        ArrayList<String> splitCommands = ClientCommandUtil.splitCommandAndArguments(aliasCommand);
        String fullCommand = ClientCommandUtil.getCommandFromCustomAlias(splitCommands.get(0), splitCommands.get(1));
        if (fullCommand.isBlank()) return 0;
        ClientCommandUtil.executeCommand(fullCommand);
        return Command.SINGLE_SUCCESS;
    }
}
