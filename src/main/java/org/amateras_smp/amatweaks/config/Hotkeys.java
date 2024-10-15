package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui","LEFT_ALT,K","Open GUI Config Screen (powered by malilib)");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI
    );
}
