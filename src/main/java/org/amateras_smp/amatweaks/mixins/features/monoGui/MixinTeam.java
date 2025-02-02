// Copyright (c) 2025 The Ama-Tweaks Authors
// This file is part of the Ama-Tweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.monoGui;

import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Team.class)
public class MixinTeam {
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true)
    private void onGetColor(CallbackInfoReturnable<Formatting> cir) {
        if (FeatureToggle.TWEAK_MONO_TEAM_COLOR.getBooleanValue()) {
            cir.setReturnValue(Formatting.RESET);
        }
    }
}
