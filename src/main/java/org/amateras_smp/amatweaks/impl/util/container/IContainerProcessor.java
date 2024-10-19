package org.amateras_smp.amatweaks.impl.util.container;

import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.screen.slot.Slot;
import org.amateras_smp.amatweaks.config.FeatureToggle;

import java.util.List;

public interface IContainerProcessor {
    default boolean isEnabled()
    {
        FeatureToggle config = this.getConfig();
        return config.getBooleanValue();
    }

    FeatureToggle getConfig();

    ProcessResult process(ClientPlayerEntity player, HandledScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots);
}
