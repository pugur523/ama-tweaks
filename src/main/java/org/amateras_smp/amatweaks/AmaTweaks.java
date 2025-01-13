package org.amateras_smp.amatweaks;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class AmaTweaks implements ClientModInitializer {
    public static Logger LOGGER;
    @Override
    public void onInitializeClient() {
        LOGGER = LogManager.getLogger(Reference.MOD_NAME);

        Configurator.setLevel(LOGGER, Level.DEBUG);
        LOGGER.debug("[AmaTweaks] debug log is enabled");

        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        InitHandler.registerCommands();
        LOGGER.info("ama-tweaks (v{}) has initialized!", Reference.MOD_VERSION);
    }
}
