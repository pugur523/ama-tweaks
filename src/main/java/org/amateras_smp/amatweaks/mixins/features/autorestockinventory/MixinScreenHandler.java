// Copyright (c) 2025 The Ama-Tweaks Authors
// This file is part of the Ama-Tweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.autorestockinventory;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.screen.ScreenHandler;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.impl.util.container.ContainerProcessManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = {@Condition(Reference.ModIds.itemscroller), @Condition(Reference.ModIds.tweakermore)})

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler {
    @Inject(method = "updateSlotStacks", at = @At("TAIL"))
    private void autoContainerProcess(CallbackInfo ci) {
        ContainerProcessManager.process((ScreenHandler)(Object)this);
    }
}

