package org.amateras_smp.amatweaks.mixins.autoEat;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.InventoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        if (FeatureToggle.TWEAK_AUTO_EAT.getBooleanValue()) {
            InventoryUtil.autoEat();
        }
    }
}
