package org.amateras_smp.amatweaks.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.impl.features.InteractionCache;

//#if MC >= 11900
import net.minecraft.text.Text;
//#else
//$$ import net.minecraft.text.LiteralText;
//#endif


public class LookBackInteractionCommand {
    public static Command<FabricClientCommandSource> command = LookBackInteractionCommand::callback;


    public static int callback(CommandContext<FabricClientCommandSource> context) {
        InteractionCache.printInteraction();
        StringBuilder message = new StringBuilder();
        if (!InteractionCache.blockInteractionCache.isEmpty()) {
            message.append("=== Block Interactions ===\n");
            for (var b : InteractionCache.blockInteractionCache) {
                message.append(b.toString()).append("\n");
            }
        }
        if (!InteractionCache.itemInteractionCache.isEmpty()) {
            message.append("=== Item Interactions ===\n");
            for (var i : InteractionCache.itemInteractionCache) {
                message.append(i.toString()).append("\n");
            }
        }
        if (!InteractionCache.entityInteractionCache.isEmpty()) {
            message.append("=== Entity Interactions ===\n");
            for (var e : InteractionCache.entityInteractionCache) {
                message.append(e.toString()).append("\n");
            }
        }

        //#if MC >= 11900
        context.getSource().sendFeedback(Text.literal(message.toString()));
        //#else
        //$$ context.getSource().sendFeedback(new LiteralText(message.toString()));
        //#endif

        return Command.SINGLE_SUCCESS;
    }
}
