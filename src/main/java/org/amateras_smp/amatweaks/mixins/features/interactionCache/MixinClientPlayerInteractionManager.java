package org.amateras_smp.amatweaks.mixins.features.interactionCache;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.InteractionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(method = "interactBlock", at = @At("HEAD"))
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!FeatureToggle.TWEAK_INTERACTION_CACHE.getBooleanValue()) return;
        InteractionCache.onBlockInteraction(player, player.getWorld().getBlockState(hitResult.getBlockPos()).getBlock().asItem(), hitResult.getBlockPos());
    }

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onInteractEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!FeatureToggle.TWEAK_INTERACTION_CACHE.getBooleanValue()) return;
        InteractionCache.onEntityInteraction(target);
    }

    @Inject(method = "interactItem", at = @At("HEAD"))
    private void onInteractItem(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!FeatureToggle.TWEAK_INTERACTION_CACHE.getBooleanValue()) return;
        InteractionCache.onBlockInteraction(player, player.getStackInHand(hand).getItem(), );
    }
}
