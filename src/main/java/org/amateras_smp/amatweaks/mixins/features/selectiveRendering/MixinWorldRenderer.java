package org.amateras_smp.amatweaks.mixins.features.selectiveRendering;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.SelectiveRendering;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldRenderer.class, priority = 1100)
public class MixinWorldRenderer {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void onRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if(!FeatureToggle.TWEAK_SELECTIVE_ENTITY_RENDERING.getBooleanValue()) return;

        UsageRestriction.ListType type = (UsageRestriction.ListType) Configs.Lists.SELECTIVE_ENTITY_RENDERING_LIST_TYPE.getOptionListValue();
        if(type == UsageRestriction.ListType.NONE) return;

        String targetEntity = (entity.getType().toString()).replace("entity.minecraft.", "");

        if(type == UsageRestriction.ListType.BLACKLIST) {
            if(Configs.Lists.SELECTIVE_ENTITY_RENDERING_BLACKLIST.getStrings().contains(targetEntity)) {
                ci.cancel();
            }
        } else if (type == UsageRestriction.ListType.WHITELIST) {
            if(!Configs.Lists.SELECTIVE_ENTITY_RENDERING_WHITELIST.getStrings().contains(targetEntity)) {
                ci.cancel();
            }
        }
    }


    // conflicts with sodium
    @Redirect(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;upload()V"))
    private void onRenderBlock(GlUniform instance, @Local BlockPos blockPos) {
        // if (client.world == null) return;
        if (!FeatureToggle.TWEAK_SELECTIVE_BLOCK_RENDERING.getBooleanValue()) {
            instance.upload();
            return;
        }
        if (SelectiveRendering.isAllowed(client.world.getBlockState(blockPos))) {
            instance.upload();
        }
    }

}
