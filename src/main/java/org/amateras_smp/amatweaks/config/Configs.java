package org.amateras_smp.amatweaks.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.restrictions.ItemRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.features.PreventBreakingAdjacentPortal;

import java.io.File;

public class Configs implements IConfigHandler
{
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static class Generic
    {
        public static final ConfigDouble AUTO_EAT_THRESHOLD = new ConfigDouble("autoEatThreshold", 1.0, 0, 1.0, "hunger level threshold for tweakAutoEat.");
        public static final ConfigInteger AUTO_FIREWORK_USE_INTERVAL = new ConfigInteger("autoFireworkUseInterval", 60, 1, 1000, "the interval game tick for automatically use firework rocket with tweakAutoFireworkGlide");
        public static final ConfigDouble AUTO_GLIDE_SPEED_THRESHOLD = new ConfigDouble("autoGlideSpeedThreshold", 15.0, 0, 1000, "the speed threshold for tweakAutoFireworkGlide to use firework rocket");
        public static final ConfigInteger FIREWORK_SWITCHABLE_SLOT = new ConfigInteger ("fireworkSwitchableSlot", 0, 0, 8, "slot to switch firework rocket by tweakAutoFireworkGlide. starts from 0.");
        public static final ConfigInteger FOOD_SWITCHABLE_SLOT = new ConfigInteger ("foodSwitchableSlot", 0, 0, 8, "slot to switch food by tweakAutoEat. starts from 0.");

        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                AUTO_EAT_THRESHOLD,
                AUTO_FIREWORK_USE_INTERVAL,
                AUTO_GLIDE_SPEED_THRESHOLD,
                FIREWORK_SWITCHABLE_SLOT,
                FOOD_SWITCHABLE_SLOT
        );
    }

    public static class Lists
    {
        public static final ConfigStringList HOTBAR_RESTOCK_LIST = new ConfigStringList("hotbarRestockList", ImmutableList.of("minecraft:firework_rocket", "minecraft:golden_carrot", "minecraft:experience_bottle"), "The items to restock with tweakAutoRestockHotbar.");
        public static final ItemRestriction HOTBAR_RESTOCK_ITEMS = new ItemRestriction();

        public static final ConfigStringList PICK_REDIRECT_MAP = new ConfigStringList("pickRedirectMap", ImmutableList.of("minecraft:farmland, minecraft:dirt", "minecraft:dirt_path, minecraft:dirt", "minecraft:water, minecraft:ice"), "replacement reference of litematica block pick");

        public static final ConfigOptionList PORTAL_BREAKING_RESTRICTION_LIST_TYPE = new ConfigOptionList("portalBreakingRestrictionListType", UsageRestriction.ListType.WHITELIST, "The type of the list used for breaking adjacent to portal restriction effects.");
        public static final ConfigStringList PORTAL_BREAKING_RESTRICTION_BLACKLIST = new ConfigStringList("portalBreakingRestrictionBlackList", ImmutableList.of(""), "The items that will be restricted by tweakPreventBreakingAdjacentPortal.");
        public static final ConfigStringList PORTAL_BREAKING_RESTRICTION_WHITELIST = new ConfigStringList("portalBreakingRestrictionWhiteList", ImmutableList.of("minecraft:obsidian"), "The items that will not be restricted by tweakPreventBreakingAdjacentPortal.");


        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                HOTBAR_RESTOCK_LIST,
                PICK_REDIRECT_MAP,
                PORTAL_BREAKING_RESTRICTION_LIST_TYPE,
                PORTAL_BREAKING_RESTRICTION_BLACKLIST,
                PORTAL_BREAKING_RESTRICTION_WHITELIST
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
        Lists.HOTBAR_RESTOCK_ITEMS.setListContents(ImmutableList.of(""), Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings());

        PreventBreakingAdjacentPortal.PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.setListType((UsageRestriction.ListType) Lists.PORTAL_BREAKING_RESTRICTION_LIST_TYPE.getOptionListValue());
        PreventBreakingAdjacentPortal.PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.setListContents(
                Lists.PORTAL_BREAKING_RESTRICTION_BLACKLIST.getStrings(),
                Lists.PORTAL_BREAKING_RESTRICTION_WHITELIST.getStrings());
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);
                // ConfigUtils.readConfigBase(root, "Fixes", Fixes.OPTIONS);
                ConfigUtils.readConfigBase(root, "Lists", Lists.OPTIONS);
                ConfigUtils.readHotkeys(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
                ConfigUtils.readHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
                ConfigUtils.readConfigBase(root, "Disables", Disable.OPTIONS);
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
            // ConfigUtils.writeConfigBase(root, "Fixes", Configs.Fixes.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Lists", Configs.Lists.OPTIONS);
            ConfigUtils.writeHotkeys(root, "GenericHotkeys", Hotkeys.HOTKEY_LIST);
            ConfigUtils.writeHotkeyToggleOptions(root, "TweakHotkeys", "TweakToggles", FeatureToggle.VALUES);
            ConfigUtils.writeConfigBase(root, "Disables", Disable.OPTIONS);

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
