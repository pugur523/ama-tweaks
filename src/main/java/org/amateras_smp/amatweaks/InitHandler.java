package org.amateras_smp.amatweaks;

import com.mojang.brigadier.Command;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.config.Callbacks;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.event.InputHandler;
import org.amateras_smp.amatweaks.command.HistoryCommand;

//#if MC >= 11900
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
//#else
//$$ import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
//$$ import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
//#endif

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

    private static void registerCommand(String name, Command<FabricClientCommandSource> command) {
        //#if MC >= 11900
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal(name)
                .executes(command)));
        //#else
        //$$ ClientCommandManager.DISPATCHER.register(literal(name)
        //$$    .executes(command));
        //#endif
    }

    public static void registerCommands() {
        registerCommand("history", HistoryCommand.command);
        registerCommand("clearinteraction", HistoryCommand.clearCommand);
    }
}
