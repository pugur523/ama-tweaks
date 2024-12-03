package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.impl.features.InteractionHistory;

//#if MC >= 11900
import net.minecraft.text.Text;
//#else
//$$ import net.minecraft.text.LiteralText;
//#endif


public class HistoryCommand {
    public static Command<FabricClientCommandSource> command = HistoryCommand::callback;


    public static int callback(CommandContext<FabricClientCommandSource> context) {
        InteractionHistory.printInteraction();
        StringBuilder message = new StringBuilder();
        if (!InteractionHistory.blockInteractionHistory.isEmpty()) {
            message.append("=== Block Interactions ===\n");
            for (var b : InteractionHistory.blockInteractionHistory) {
                message.append(b.toString()).append("\n");
            }
        }
        if (!InteractionHistory.entityInteractionHistory.isEmpty()) {
            message.append("=== Entity Interactions ===\n");
            for (var e : InteractionHistory.entityInteractionHistory) {
                message.append(e.toString()).append("\n");
            }
        }
        if (!message.isEmpty() && message.charAt(message.length() - 1) == '\n') {
            message.deleteCharAt(message.length() - 1);
        }

        //#if MC >= 11900
        context.getSource().sendFeedback(Text.literal(message.toString()));
        //#else
        //$$ context.getSource().sendFeedback(new LiteralText(message.toString()));
        //#endif

        return Command.SINGLE_SUCCESS;
    }
}
