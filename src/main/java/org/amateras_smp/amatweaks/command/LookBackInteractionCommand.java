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
        //#if MC >= 11900
        context.getSource().sendFeedback(Text.literal(InteractionCache.blockInteractionCache.toString()));
        context.getSource().sendFeedback(Text.literal(InteractionCache.entityInteractionCache.toString()));
        //#else
        //$$ context.getSource().sendFeedback(new LiteralText(InteractionCache.blockInteractionCache.toString()));
        //$$ context.getSource().sendFeedback(new LiteralText(InteractionCache.entityInteractionCache.toString()));
        //#endif

        return Command.SINGLE_SUCCESS;
    }
}
