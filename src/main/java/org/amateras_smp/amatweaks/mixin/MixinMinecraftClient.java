package org.amateras_smp.amatweaks.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.util.InventoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Shadow @Final public GameOptions options;
    @Unique private boolean flag = false;
    @Unique

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onProcessKeybindsPre(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen == null)
        {
            if (FeatureToggle.TWEAK_HOLD_FORWARD.getBooleanValue())
            {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.forwardKey.getBoundKeyTranslationKey()), true);
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (Configs.Generic.MOD_ENABLED.getBooleanValue() && FeatureToggle.TWEAK_AUTO_EAT.getBooleanValue()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerEntity player = mc.player;
            if (player == null) return;
            if (mc.getNetworkHandler() == null || mc.interactionManager == null) return;

            HitResult hit = mc.crosshairTarget;
            if (hit == null) return;
            BlockHitResult hitBlock = (BlockHitResult) hit;
            BlockPos hitBlockPos = hitBlock.getBlockPos();
            if (mc.currentScreen != null || player.getWorld().getBlockEntity(hitBlockPos) != null) return;

            double hungerThreshold = Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue();
            // devide by 2 because of foodSaturationLevel
            if ((double) player.getHungerManager().getFoodLevel() / 10 / 2 < hungerThreshold) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    if (stack.getItem().isFood()) {
                        InventoryUtil.tryToSwap(mc, i);
                        KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.useKey.getBoundKeyTranslationKey()), true);
                        flag = true;
                        return;
                    }
                }
            }
            if (flag) {
                KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(this.options.useKey.getBoundKeyTranslationKey()), false);
                player.getInventory().selectedSlot = InventoryUtil.beforeSlot;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
                flag = false;
            }
        }
    }
}