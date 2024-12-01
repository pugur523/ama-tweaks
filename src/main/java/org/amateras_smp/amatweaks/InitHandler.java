package org.amateras_smp.amatweaks;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.amateras_smp.amatweaks.config.Callbacks;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.event.InputHandler;
import org.amateras_smp.amatweaks.impl.command.LookBackInteractionCommand;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class InitHandler implements IInitializationHandler
{
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        Callbacks.init();
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("lookBack")
                .executes(LookBackInteractionCommand.command)));
    }
}
