package org.amateras_smp.amatweaks.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import fi.dy.masa.malilib.util.PositionUtils;
import fi.dy.masa.tweakeroo.config.Configs;
import fi.dy.masa.tweakeroo.config.Hotkeys;
import fi.dy.masa.tweakeroo.tweaks.PlacementTweaks;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.impl.features.schematicProPlace.ProPlaceImpl;
import me.fallenbreath.tweakermore.util.BlockUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.tweaks.PlacementOnPortalSides;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(Reference.ModIds.tweakeroo))
@Mixin(PlacementTweaks.class)
public class MixinPlacementTweaks {
    @Shadow(
            remap = false
    )
    private static boolean firstWasRotation;
    @Shadow
    private static ItemStack[] stackBeforeUse;

    public MixinPlacementTweaks() {
    }

    @Shadow
    private static boolean isFacingValidFor(Direction facing, ItemStack stack) {
        return false;
    }

    @Unique
    private static BlockHitResult getFinalHitResult(ClientPlayerEntity player, ClientWorld world, BlockPos posIn, Direction sideIn, Vec3d hitVecIn, Hand hand) {
        BlockHitResult hitResult = new BlockHitResult(hitVecIn, sideIn, posIn, false);
        ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hitResult));
        BlockState state = world.getBlockState(posIn);
        ItemStack stackOriginal;
        if (!stackBeforeUse[hand.ordinal()].isEmpty() && !fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_HOTBAR_SLOT_CYCLE.getBooleanValue() && !fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_HOTBAR_SLOT_RANDOMIZER.getBooleanValue()) {
            stackOriginal = stackBeforeUse[hand.ordinal()];
        } else {
            stackOriginal = player.getStackInHand(hand).copy();
        }

        if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_PLACEMENT_RESTRICTION.getBooleanValue() && !state.canReplace(ctx) && BlockUtil.isReplaceable(state)) {
            posIn = posIn.offset(sideIn.getOpposite());
        }

        int afterClickerClickCount = MathHelper.clamp(fi.dy.masa.tweakeroo.config.Configs.Generic.AFTER_CLICKER_CLICK_COUNT.getIntegerValue(), 0, 32);
        boolean flexible = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_FLEXIBLE_BLOCK_PLACEMENT.getBooleanValue();
        boolean rotationHeld = fi.dy.masa.tweakeroo.config.Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ROTATION.getKeybind().isKeybindHeld();
        boolean rememberFlexible = fi.dy.masa.tweakeroo.config.Configs.Generic.REMEMBER_FLEXIBLE.getBooleanValue();
        boolean rotation = rotationHeld || rememberFlexible && firstWasRotation;
        boolean accurate = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_ACCURATE_BLOCK_PLACEMENT.getBooleanValue();
        boolean keys = fi.dy.masa.tweakeroo.config.Hotkeys.ACCURATE_BLOCK_PLACEMENT_IN.getKeybind().isKeybindHeld() || fi.dy.masa.tweakeroo.config.Hotkeys.ACCURATE_BLOCK_PLACEMENT_REVERSE.getKeybind().isKeybindHeld();
        accurate = accurate && keys;
        double y;
        //#if MC >= 12100
        //$$ if (flexible && rotation && !accurate && fi.dy.masa.tweakeroo.config.Configs.Generic.ACCURATE_PLACEMENT_PROTOCOL.getBooleanValue() && isFacingValidFor(sideIn, stackOriginal)) {
        //#else
        if (flexible && rotation && !accurate && fi.dy.masa.tweakeroo.config.Configs.Generic.CARPET_ACCURATE_PLACEMENT_PROTOCOL.getBooleanValue() && isFacingValidFor(sideIn, stackOriginal)) {
        //#endif
            Direction facing = sideIn.getOpposite();
            y = (double)(posIn.getX() + 2 + facing.getId() * 2);
            if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_AFTER_CLICKER.getBooleanValue()) {
                y += (double)(afterClickerClickCount * 16);
            }

            hitVecIn = new Vec3d(y, hitVecIn.y, hitVecIn.z);
        }

        if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_Y_MIRROR.getBooleanValue() && fi.dy.masa.tweakeroo.config.Hotkeys.PLACEMENT_Y_MIRROR.getKeybind().isKeybindHeld()) {
            y = 1.0 - hitVecIn.y + (double)(2 * posIn.getY());
            hitVecIn = new Vec3d(hitVecIn.x, y, hitVecIn.z);
            if (sideIn.getAxis() == Direction.Axis.Y) {
                posIn = posIn.offset(sideIn);
                sideIn = sideIn.getOpposite();
            }
        }

        return new BlockHitResult(hitVecIn, sideIn, posIn, false);
    }

    /*
    @Inject(method = "tryPlaceBlock", at = @At("HEAD"), cancellable = true)
    private static void tryPlaceBlock(ClientPlayerInteractionManager controller, ClientPlayerEntity player, ClientWorld world, BlockPos posIn, Direction sideIn, Direction sideRotatedIn, float playerYaw, Vec3d hitVec, Hand hand, PositionUtils.HitPart hitPart, boolean isFirstClick, CallbackInfoReturnable<ActionResult> cir){
        if(!FeatureToggle.DISABLE_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return;

        if (!isFirstClick) {
            return;
        }

        BlockHitResult hitResult = new BlockHitResult(hitVec, sideIn, posIn, false);

        ItemUsageContext itemUsageContext = new ItemUsageContext(player, hand, hitResult);
        ItemPlacementContext ctx = new ItemPlacementContext(itemUsageContext);

        boolean flexible = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_FLEXIBLE_BLOCK_PLACEMENT.getBooleanValue();
        boolean rotationHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ROTATION.getKeybind().isKeybindHeld();
        boolean offsetHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_OFFSET.getKeybind().isKeybindHeld();
        boolean adjacentHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ADJACENT.getKeybind().isKeybindHeld();
        if (!flexible || (!rotationHeld && !offsetHeld && !adjacentHeld)) {
            if (PlacementOnPortalSides.restriction(ctx.getWorld(), ctx, hitResult)) {
                cir.setReturnValue(ActionResult.PASS);
            }
        }
    }

    @Inject(method = "handleFlexibleBlockPlacement", at = @At("HEAD"), cancellable = true)
    private static void handleFlexibleBlockPlacement(ClientPlayerInteractionManager controller, ClientPlayerEntity player, ClientWorld world, BlockPos pos, Direction side, float playerYaw, Vec3d hitVec, Hand hand, PositionUtils.HitPart hitPart, CallbackInfoReturnable<ActionResult> cir) {
        if(!FeatureToggle.DISABLE_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return;
        BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
        ItemPlacementContext ctx = new ItemPlacementContext(player, hand, player.getStackInHand(hand), hitResult);
        boolean flexible = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_FLEXIBLE_BLOCK_PLACEMENT.getBooleanValue();
        boolean accurate = fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_ACCURATE_BLOCK_PLACEMENT.getBooleanValue();
        boolean rotationHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ROTATION.getKeybind().isKeybindHeld();
        boolean offsetHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_OFFSET.getKeybind().isKeybindHeld();
        boolean adjacentHeld = Hotkeys.FLEXIBLE_BLOCK_PLACEMENT_ADJACENT.getKeybind().isKeybindHeld();

        if (flexible && rotationHeld && !accurate && Configs.Generic.CARPET_ACCURATE_PLACEMENT_PROTOCOL.getBooleanValue() && PlacementOnPortalSides.isFacingValidFor(side, ctx.getStack())) {
            Direction facing = side.getOpposite();
            double x = pos.getX() + 2 + facing.getId() * 2;
            if (fi.dy.masa.tweakeroo.config.FeatureToggle.TWEAK_AFTER_CLICKER.getBooleanValue()) {
                int afterClickerClickCount = MathHelper.clamp(Configs.Generic.AFTER_CLICKER_CLICK_COUNT.getIntegerValue(), 0, 32);
                x += afterClickerClickCount * 16;
            }

            hitVec = new Vec3d(x, hitVec.y, hitVec.z);
        }

        if (flexible && (rotationHeld || offsetHeld || adjacentHeld)) {
            System.out.println(ctx.getBlockPos());
            System.out.println(hitResult.getBlockPos());
            System.out.println(world);
            if (PlacementOnPortalSides.restrictionFlexbile(world, ctx, hitResult)) {
                System.out.println("test101");

                cir.setReturnValue(ActionResult.PASS);
                cir.cancel();
            }
        }
        System.out.println("test100");
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
    */

    @Inject(
            method = "processRightClickBlockWrapper",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onProcessRightClickBlockWrapper(ClientPlayerInteractionManager controller, ClientPlayerEntity player, ClientWorld world, BlockPos posIn, Direction sideIn, Vec3d hitVecIn, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(!FeatureToggle.DISABLE_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return;
        BlockHitResult hitResult = getFinalHitResult(player, world, posIn, sideIn, hitVecIn, hand);
        ItemPlacementContext ctx = new ItemPlacementContext(new ItemUsageContext(player, hand, hitResult));

        System.out.println(hitResult.getBlockPos().getX() + " " + hitResult.getBlockPos().getY() + " " + hitResult.getBlockPos().getZ());
        System.out.println(hitResult.getBlockPos().getX() + " " + hitResult.getBlockPos().getY() + " " + hitResult.getBlockPos().getZ());

        if (PlacementOnPortalSides.restriction(world, ctx, hitResult)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
