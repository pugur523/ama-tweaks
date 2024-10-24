package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import org.amateras_smp.amatweaks.Reference;

import java.io.File;

public class Configs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Generic
    {
        public static final ConfigInteger FOOD_SWITCHABLE_SLOT = new ConfigInteger ("foodSwitchableSlot", 0, 0, 8, "slot to switch food by auto eat food tweak. start from 0.");
        public static final ConfigDouble AUTO_EAT_THRESHOLD = new ConfigDouble("autoEatThreshold", 1.0, 0, 1.0, "hunger level threshold for auto eat food tweak.");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                FOOD_SWITCHABLE_SLOT,
                AUTO_EAT_THRESHOLD
        );
    }

    public static class Lists
    {
        public static final ConfigStringList HOTBAR_RESTOCK_LIST = new ConfigStringList("hotbarRestockList", ImmutableList.of("minecraft:firework_rocket", "minecraft:golden_carrot", "minecraft:experience_bottle"), "item list to restock with tweakHotbarRestock");
        public static final ItemRestriction HOTBAR_RESTOCK_ITEMS = new ItemRestriction();

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                HOTBAR_RESTOCK_LIST
        );
    }

    public static class Disable
    {
        public static final ConfigBooleanHotkeyed DISABLE_NARRATOR = new ConfigBooleanHotkeyed("disableNarratorHotkey", false, "", "Disables the hotkey of the narrator");

        public static final ImmutableList<IHotkeyTogglable> OPTIONS = ImmutableList.of(
                DISABLE_NARRATOR
        );
    }

    public static void onConfigLoaded() {
        Configs.Lists.HOTBAR_RESTOCK_ITEMS.setListContents(ImmutableList.of(""), Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings());
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", Configs.Generic.OPTIONS);
                ConfigUtils.readConfigBase(root, "Lists", Configs.Lists.OPTIONS);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            }
        }



        onConfigLoaded();
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
            ConfigUtils.writeHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
