package org.amateras_smp.amatweaks;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmaTweaks implements ClientModInitializer {
    public static Logger LOGGER;
    @Override
    public void onInitializeClient() {
        LOGGER = LoggerFactory.getLogger(Reference.MOD_NAME);
        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());
        InitHandler.registerCommands();
        LOGGER.info("ama-tweaks (v{}) has initialized!", Reference.MOD_VERSION);
    }
}
