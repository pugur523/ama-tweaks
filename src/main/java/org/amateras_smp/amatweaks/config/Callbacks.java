package org.amateras_smp.amatweaks.config;

import org.amateras_smp.amatweaks.gui.GuiConfigs;
import org.amateras_smp.amatweaks.util.InventoryUtil;
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
        Configs.Generic.FOOD_SWITCHABLE_SLOT.setValueChangeCallback((cfg) -> InventoryUtil.setFoodSwitchSlot(cfg.getIntegerValue()));
    }

    private static class KeyCallbackHotkeysGeneric implements IHotkeyCallback
    {

        public KeyCallbackHotkeysGeneric()
        {
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if (key == Hotkeys.OPEN_CONFIG_GUI.getKeybind())
            {
                GuiBase.openGui(new GuiConfigs());
                return true;
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

}
