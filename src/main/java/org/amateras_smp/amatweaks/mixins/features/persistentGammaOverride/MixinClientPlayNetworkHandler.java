package org.amateras_smp.amatweaks.mixins.features.persistentGammaOverride;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(Reference.ModIds.tweakeroo))
@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin(CallbackInfo ci) {
        if (FeatureToggle.TWEAK_PERSISTENT_GAMMA_OVERRIDE.getBooleanValue()) {
            fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_GAMMA_OVERRIDE.onValueChanged();
        }
    }
}
