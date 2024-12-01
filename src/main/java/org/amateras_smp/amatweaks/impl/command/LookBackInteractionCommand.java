package org.amateras_smp.amatweaks.impl.command;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.impl.features.InteractionCache;

public class LookBackInteractionCommand {
    public static Command<FabricClientCommandSource> command = context -> callback();


    public static int callback() {
        InteractionCache.printInteraction();
        return Command.SINGLE_SUCCESS;
    }
}
