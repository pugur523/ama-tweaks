package org.amateras_smp.amatweaks.mixins.features.autoEat;

import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.AutoEat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow
    static MinecraftClient instance;

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Recorder;endTick()V"))
    private void onTick(CallbackInfo ci) {
        if (FeatureToggle.TWEAK_AUTO_EAT.getBooleanValue()) {
            if (instance.player != null && instance.player.networkHandler != null && instance.interactionManager != null) {
                if (instance.player.isFallFlying()) {
                    if (Configs.Generic.GLIDING_AUTO_EAT_DISABLED.getBooleanValue()) {
                        AutoEat.autoEatCheck(instance, instance.player, instance.player.networkHandler);
                        return;
                    }
                }
                AutoEat.autoEat(instance, instance.player, instance.player.networkHandler);
            }
        }
    }
}
