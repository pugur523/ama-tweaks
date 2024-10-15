package org.amateras_smp.amatweaks.event;

import com.google.common.collect.ImmutableList;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.config.Hotkeys;
import fi.dy.masa.malilib.hotkeys.*;

public class InputHandler implements IKeybindProvider, IKeyboardInputHandler, IMouseInputHandler
{
    private static final InputHandler INSTANCE = new InputHandler();

    private InputHandler()
    {
        super();
    }

    public static InputHandler getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager)
    {
        for (FeatureToggle toggle : FeatureToggle.values())
        {
            manager.addKeybindToMap(toggle.getKeybind());
        }

        for (IHotkey hotkey : Hotkeys.HOTKEY_LIST)
        {
            manager.addKeybindToMap(hotkey.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager)
    {
        manager.addHotkeysForCategory(Reference.MOD_NAME, "kindatweaks.hotkeys.category.generic_hotkeys", Hotkeys.HOTKEY_LIST);
        manager.addHotkeysForCategory(Reference.MOD_NAME, "kindatweaks.hotkeys.category.tweak_toggle_hotkeys", ImmutableList.copyOf(FeatureToggle.values()));
    }
}
