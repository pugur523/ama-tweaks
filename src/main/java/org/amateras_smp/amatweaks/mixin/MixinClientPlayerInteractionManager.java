package org.amateras_smp.amatweaks.mixin;

import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.amateras_smp.amatweaks.tweaks.SafeStepProtection;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void handleBreakingRestriction(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (Configs.Generic.MOD_ENABLED.getBooleanValue() && FeatureToggle.TWEAK_SAFE_STEP_PROTECTION.getBooleanValue() && !SafeStepProtection.isPositionAllowedByBreakingRestriction(pos)) {
            cir.setReturnValue(false);
        }
    }
}
