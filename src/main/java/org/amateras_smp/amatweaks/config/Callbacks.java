package org.amateras_smp.amatweaks.config;

import fi.dy.masa.litematica.data.DataManager;
import fi.dy.masa.litematica.materials.MaterialListBase;
import org.amateras_smp.amatweaks.gui.GuiConfigs;
import fi.dy.masa.malilib.hotkeys.KeyCallbackAdjustable;
import fi.dy.masa.malilib.util.InfoUtils;
import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.amateras_smp.amatweaks.impl.features.PreventBreakingAdjacentPortal;
import org.amateras_smp.amatweaks.impl.features.SelectiveRendering;

public class Callbacks {

    public static void init() {
        MinecraftClient mc = MinecraftClient.getInstance();
        IHotkeyCallback callbackGeneric = new KeyCallbackHotkeysGeneric();

        Hotkeys.OPEN_CONFIG_GUI.getKeybind().setCallback(callbackGeneric);
        Hotkeys.REFRESH_MATERIAL_LIST.getKeybind().setCallback(callbackGeneric);

        FeatureToggle.TWEAK_HOLD_BACK.setValueChangeCallback(new FeatureCallbackHold(mc.options.backKey));
        FeatureToggle.TWEAK_HOLD_FORWARD.setValueChangeCallback(new FeatureCallbackHold(mc.options.forwardKey));
        FeatureToggle.TWEAK_HOLD_LEFT.setValueChangeCallback(new FeatureCallbackHold(mc.options.leftKey));
        FeatureToggle.TWEAK_HOLD_RIGHT.setValueChangeCallback(new FeatureCallbackHold(mc.options.rightKey));

        FeatureToggle.TWEAK_AUTO_EAT.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_EAT));
        FeatureToggle.TWEAK_AUTO_FIREWORK_GLIDE.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_FIREWORK_GLIDE));
        FeatureToggle.TWEAK_AUTO_RESTOCK_HOTBAR.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_RESTOCK_HOTBAR));
        FeatureToggle.TWEAK_COMPACT_SCOREBOARD.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_COMPACT_SCOREBOARD));
        FeatureToggle.TWEAK_HOLD_BACK.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_HOLD_BACK));
        FeatureToggle.TWEAK_HOLD_FORWARD.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_HOLD_FORWARD));
        FeatureToggle.TWEAK_HOLD_LEFT.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_HOLD_LEFT));
        FeatureToggle.TWEAK_HOLD_RIGHT.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_HOLD_RIGHT));
        FeatureToggle.TWEAK_INTERACTION_CACHE.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_INTERACTION_CACHE));
        FeatureToggle.TWEAK_PICK_BLOCK_REDIRECT.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_PICK_BLOCK_REDIRECT));
        FeatureToggle.TWEAK_PERSISTENT_GAMMA_OVERRIDE.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_PERSISTENT_GAMMA_OVERRIDE));
        FeatureToggle.TWEAK_PREVENT_BREAKING_ADJACENT_PORTAL.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_PREVENT_BREAKING_ADJACENT_PORTAL));
        FeatureToggle.TWEAK_PREVENT_PLACEMENT_ON_PORTAL_SIDES.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_PREVENT_PLACEMENT_ON_PORTAL_SIDES));
        FeatureToggle.TWEAK_SAFE_STEP_PROTECTION.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_SAFE_STEP_PROTECTION));
        FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING));
        FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING));
        // FeatureToggle.TWEAK_.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_));

        Configs.Lists.PORTAL_BREAKING_RESTRICTION_BLACKLIST.setValueChangeCallback((cfg) -> PreventBreakingAdjacentPortal.buildLists());
        Configs.Lists.PORTAL_BREAKING_RESTRICTION_WHITELIST.setValueChangeCallback((cfg) -> PreventBreakingAdjacentPortal.buildLists());
        Configs.Lists.PORTAL_BREAKING_RESTRICTION_LIST_TYPE.setValueChangeCallback((cfg) -> PreventBreakingAdjacentPortal.buildLists());

        Configs.Lists.SELECTIVE_BLOCK_RENDERING_BLACKLIST.setValueChangeCallback((cfg) -> SelectiveRendering.buildLists());
        Configs.Lists.SELECTIVE_BLOCK_RENDERING_WHITELIST.setValueChangeCallback((cfg) -> SelectiveRendering.buildLists());
        Configs.Lists.SELECTIVE_BLOCK_RENDERING_LIST_TYPE.setValueChangeCallback((cfg) -> SelectiveRendering.buildLists());
    }

    private static class KeyCallbackHotkeysGeneric implements IHotkeyCallback
    {

        public KeyCallbackHotkeysGeneric()
        {
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if (key == Hotkeys.OPEN_CONFIG_GUI.getKeybind()) {
                GuiBase.openGui(new GuiConfigs());
                return true;
            } else if (key == Hotkeys.REFRESH_MATERIAL_LIST.getKeybind()) {
                try {
                    Class.forName("fi.dy.masa.litematica.Litematica");
                    MaterialListBase materialList = DataManager.getMaterialList();
                    if (materialList != null) {
                        materialList.reCreateMaterialList();
                        return true;
                    } else {
                        return false;
                    }
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }

            return false;
        }
    }

    public static class FeatureCallbackHold implements IValueChangeCallback<IConfigBoolean>
    {
        private final KeyBinding keyBind;

        public FeatureCallbackHold(KeyBinding keyBind)
        {
            this.keyBind = keyBind;
        }

        @Override
        public void onValueChanged(IConfigBoolean config)
        {
            if (config.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()), true);
                KeyBinding.onKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()));
            }
            else
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.keyBind.getBoundKeyTranslationKey()), false);
            }
        }
    }

    private record KeyCallbackAdjustableFeature(IConfigBoolean config) implements IHotkeyCallback {
            private static IHotkeyCallback createCallback(IConfigBoolean config) {
                return new KeyCallbackAdjustable(config, new KeyCallbackAdjustableFeature(config));
            }

        @Override
            public boolean onKeyAction(KeyAction action, IKeybind key) {
                this.config.toggleBooleanValue();

                boolean enabled = this.config.getBooleanValue();
                String strStatus = enabled ? "ON" : "OFF";
                String preGreen = GuiBase.TXT_GREEN;
                String preRed = GuiBase.TXT_RED;
                String rst = GuiBase.TXT_RST;
                String prettyName = this.config.getPrettyName();
                strStatus = (enabled ? preGreen : preRed) + strStatus + rst;

                InfoUtils.printActionbarMessage("Toggled %s %s", prettyName, strStatus);
                return true;
            }
        }

}
