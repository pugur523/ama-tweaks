package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;

//#if MC < 11900
import com.mojang.brigadier.exceptions.CommandSyntaxException;
//#endif

public class ClientCommandUtil {
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
}
