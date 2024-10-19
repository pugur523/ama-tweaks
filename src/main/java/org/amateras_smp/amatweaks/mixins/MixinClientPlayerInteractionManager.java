package org.amateras_smp.amatweaks.mixins;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.tweaks.PlacementOnPortalSides;
import org.amateras_smp.amatweaks.impl.tweaks.SafeStepProtection;
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
        if (Configs.Generic.MOD_ENABLED.getBooleanValue() && FeatureToggle.TWEAK_SAFE_STEP_PROTECTION.getBooleanValue() && !SafeStepProtection.isPositionAllowedByBreakingRestriction(pos)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "interactBlock",
            at = @At("HEAD"),
            cancellable = true
    )
    //#if MC < 11904
    //$$ private void onInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir)
    //#else
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir)
    //#endif
    {
        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        ItemPlacementContext ctx = new ItemPlacementContext(itemUsageContext);

        if (PlacementOnPortalSides.restriction(ctx.getWorld(), ctx, hitResult)) {
            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }
    }
}
