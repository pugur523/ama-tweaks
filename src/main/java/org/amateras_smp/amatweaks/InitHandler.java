package org.amateras_smp.amatweaks;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.amateras_smp.amatweaks.command.LiteralCommandAliases;
import org.amateras_smp.amatweaks.config.Callbacks;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.event.InputHandler;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import org.amateras_smp.amatweaks.command.HistoryCommand;
import org.amateras_smp.amatweaks.impl.util.ClientCommandUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

//#if MC >= 11900
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
//#else
//$$ import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
//#endif

public class InitHandler implements IInitializationHandler {
    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfigHandler(Reference.MOD_ID, new Configs());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerKeyboardInputHandler(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        Callbacks.init();
    }

    public static void initLogLevel(boolean shouldEnableDebug) {
        Configurator.setLevel(AmaTweaks.LOGGER, shouldEnableDebug ? Level.DEBUG : Level.ERROR);
        if (shouldEnableDebug) AmaTweaks.LOGGER.debug("debug print is enabled");
        else AmaTweaks.LOGGER.info("debug print is disabled");
    }

    private static void registerCommand(String name, Command<FabricClientCommandSource> command) {
        //#if MC >= 11900
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal(name)
        //#else
        //$$ ClientCommandManager.DISPATCHER.register(literal(name)
        //#endif
                .executes(command)
                .then(argument("arguments", StringArgumentType.greedyString())
                        .executes(command)))
        //#if MC >= 11900
        )
        //#endif
        ;
    }

    public static void registerCommandsOnClientLoad() {
        registerCommand("history", HistoryCommand.command);
        registerCommand("clearinteraction", HistoryCommand.clearCommand);
    }

    public static void registerDynamicCommands() {
        if (!Configs.Generic.CUSTOM_COMMAND_ALIASES.getBooleanValue()) return;
        for (String alias : ClientCommandUtil.initAndGetCommands()) {
            AmaTweaks.LOGGER.debug(alias);
            registerCommand(alias, LiteralCommandAliases.command);
        }
    }
}
