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

public class Callbacks {

    public static void init() {
        MinecraftClient mc = MinecraftClient.getInstance();
        IHotkeyCallback callbackGeneric = new KeyCallbackHotkeysGeneric();
        Hotkeys.OPEN_CONFIG_GUI.getKeybind().setCallback(callbackGeneric);
        FeatureToggle.TWEAK_HOLD_FORWARD.setValueChangeCallback(new FeatureCallbackHold(mc.options.forwardKey));

        FeatureToggle.TWEAK_AUTO_EAT.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_EAT));
        FeatureToggle.TWEAK_HOLD_FORWARD.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_EAT));
        FeatureToggle.TWEAK_AUTO_RESTOCK_HOTBAR.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_AUTO_RESTOCK_HOTBAR));
        FeatureToggle.TWEAK_SAFE_STEP_PROTECTION.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_SAFE_STEP_PROTECTION));

        
        // FeatureToggle.TWEAK_.getKeybind().setCallback(KeyCallbackAdjustableFeature.createCallback(FeatureToggle.TWEAK_));
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
