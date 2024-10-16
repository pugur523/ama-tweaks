package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.util.InventoryUtil;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Configs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Generic
    {
        public static final ConfigBoolean MOD_ENABLED = new ConfigBoolean ("modEnabled",  true, "the boolean value of if this mod is enabled");
        public static final ConfigInteger FOOD_SWITCHABLE_SLOT = new ConfigInteger ("foodSwitchableSlot", 0, "slot to switch food by auto eat food tweak");
        public static final ConfigDouble AUTO_EAT_THRESHOLD = new ConfigDouble("autoEatThreshold", 0.9, 0, 1.0, "hunger level threshold for auto eat food tweak");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                MOD_ENABLED,
                FOOD_SWITCHABLE_SLOT,
                AUTO_EAT_THRESHOLD
        );
    }

    public static class Lists
    {
        public static final ConfigStringList HOTBAR_RESTOCK_LIST = new ConfigStringList("hotbarRestockList", ImmutableList.of("minecraft:firework_rocket", "minecraft:golden_carrot", "minecraft:experience_bottle"), "item list to restock with tweakHotbarRestock");
        public static final ItemRestriction HOTBAR_RESTOCK_ITEMS = new ItemRestriction();
        public static final ImmutableList<IConfigBase> LISTS = ImmutableList.of(
                HOTBAR_RESTOCK_LIST
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
                ConfigUtils.readConfigBase(root, "Lists", Configs.Lists.LISTS);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            }
        }

        // TODO 1.19.3+
        //CreativeExtraItems.setCreativeExtraItems(Lists.CREATIVE_EXTRA_ITEMS.getStrings());
        InventoryUtil.setFoodSwitchSlot(Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue());

        onConfigLoaded();
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Generic", Configs.Generic.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.LISTS);
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
