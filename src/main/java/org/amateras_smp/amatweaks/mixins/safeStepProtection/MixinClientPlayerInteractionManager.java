package org.amateras_smp.amatweaks.mixins.safeStepProtection;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.SafeStepProtection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC < 11904
//$$ import net.minecraft.client.world.ClientWorld;
//#endif

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void handleBreakingRestriction(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (FeatureToggle.TWEAK_SAFE_STEP_PROTECTION.getBooleanValue() && !SafeStepProtection.isPositionAllowedByBreakingRestriction(pos)) {
            cir.setReturnValue(false);
        }
    }
}
