package org.amateras_smp.amatweaks.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.util.ModIds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.tweaks.PlacementOnPortalSides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(ModIds.tweakeroo))
@Mixin(PlacementTweaks.class)
public class MixinPlacementTweaks {
    @Inject(method = "tryPlaceBlock", at = @At("HEAD"), cancellable = true)
    private static void tryPlaceBlock(ClientPlayerInteractionManager controller, ClientPlayerEntity player, ClientWorld world, BlockPos posIn, Direction sideIn, Direction sideRotatedIn, float playerYaw, Vec3d hitVec, Hand hand, PositionUtils.HitPart hitPart, boolean isFirstClick, CallbackInfoReturnable<ActionResult> cir){
        if(!FeatureToggle.DISABLE_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return;

        if (!isFirstClick) {
            return;
        }

        BlockHitResult hitResult = new BlockHitResult(hitVec, sideIn, posIn, false);

        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        ItemPlacementContext ctx = new ItemPlacementContext(itemUsageContext);

        if (PlacementOnPortalSides.restriction(ctx.getWorld(), ctx, hitResult)) {
            cir.setReturnValue(ActionResult.CONSUME);
            cir.cancel();
        }
    }

    @Inject(method = "onUsingTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;"), cancellable = true)
    private static void onUsingTick(CallbackInfo ci, @Local(ordinal = 0) MinecraftClient mc, @Local(ordinal = 0) ClientPlayerEntity player, @Local(ordinal = 0) Hand hand) {
        HitResult hitResult = mc.crosshairTarget;

        if (hitResult == null) {
            return;
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitResult2 = (BlockHitResult) hitResult;
            ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hitResult2));
            if (PlacementOnPortalSides.restriction(ctx.getWorld(), ctx, hitResult2)) {
                ci.cancel();
            }
        }
    }

}
