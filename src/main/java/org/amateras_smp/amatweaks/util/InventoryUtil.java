package org.amateras_smp.amatweaks.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import java.util.Objects;

public class InventoryUtil {
    public static int beforeSlot;
    private static int FOOD_SWITCH_SLOT;
    public static void setFoodSwitchSlot(int slot) {
        if (slot > 0 && slot <= 9) {
            FOOD_SWITCH_SLOT = slot - 1;
        }
    }

    public static void tryToSwap(MinecraftClient mc, int slot) {
        PlayerEntity player = mc.player;
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
