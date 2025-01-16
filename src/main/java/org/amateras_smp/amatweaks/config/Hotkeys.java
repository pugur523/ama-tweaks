package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;

import java.util.List;

public class Hotkeys {
    public static final ConfigHotkey OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui","LEFT_ALT,A","Open Config GUI Screen");
    public static final ConfigHotkey REFRESH_MATERIAL_LIST = new ConfigHotkey("refreshMaterialList", "", "Refresh litematica's schematic material list");
    public static final ConfigHotkey SEE_INTERACTION_HISTORY = new ConfigHotkey("seeInteractionHistory", "", "Prints interaction history");

    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_CONFIG_GUI,
            REFRESH_MATERIAL_LIST,
            SEE_INTERACTION_HISTORY
    );
}
