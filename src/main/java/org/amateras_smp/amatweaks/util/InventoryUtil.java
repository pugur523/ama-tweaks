package org.amateras_smp.amatweaks.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;

import java.util.Objects;

public class InventoryUtil {
    private static int beforeSlot;
    private static int FOOD_SWITCH_SLOT;
    private static boolean flag = false;
    public static void setFoodSwitchSlot(int slot) {
        if (slot > 0 && slot <= 9) {
            FOOD_SWITCH_SLOT = slot - 1;
        }
    }

    public static void autoEat() {
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
                    tryToSwap(mc, i);
                    KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), true);
                    flag = true;
                    return;
                }
            }
        }

        if (flag) {
            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), false);
            player.getInventory().selectedSlot = beforeSlot;
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
            flag = false;
        }
    }

    public static void tryToSwap(MinecraftClient mc, int slot) {
        ClientPlayerEntity player = mc.player;
        if (player == null || player.getWorld() == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;
        PlayerInventory inventory = player.getInventory();
        ScreenHandler container = player.playerScreenHandler;
        if (slot >= 0 && slot!= inventory.selectedSlot && player.currentScreenHandler == player.playerScreenHandler) {
            beforeSlot = inventory.selectedSlot;
            if (PlayerInventory.isValidHotbarIndex(slot)) {
                inventory.selectedSlot = slot;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(inventory.selectedSlot));
            } else {
                if (inventory.selectedSlot != FOOD_SWITCH_SLOT) {
                    inventory.selectedSlot = FOOD_SWITCH_SLOT;
                    Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
                }
                mc.interactionManager.clickSlot(container.syncId, slot, FOOD_SWITCH_SLOT, SlotActionType.SWAP, mc.player);
            }
        }
    }
}
