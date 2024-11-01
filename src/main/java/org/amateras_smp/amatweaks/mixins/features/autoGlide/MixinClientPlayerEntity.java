package org.amateras_smp.amatweaks.mixins.features.autoGlide;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.AutoFireworkGlide;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Shadow private boolean falling;

    @Shadow @Final protected MinecraftClient client;

    @Unique
    private int tickCount = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_AUTO_FIREWORK_GLIDE.getBooleanValue()) return;
        if (falling) {
            tickCount++;
            int interval = Configs.Generic.AUTO_FIREWORK_USE_INTERVAL.getIntegerValue();
            if (tickCount % interval == 0 && tickCount != 0) {
                AutoFireworkGlide.autoUseFirework(client, client.getNetworkHandler());
            }
        } else {
            tickCount = 0;
        }
    }
}
