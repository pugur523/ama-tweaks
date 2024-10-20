package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;

//#if MC >= 12006
//$$ import net.minecraft.component.ComponentMap;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif

import java.util.Objects;

public class InventoryUtil {
    private static int beforeSlot;
    private static boolean flag = false;

    public static void autoEat() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return;
        if (mc.getNetworkHandler() == null || mc.interactionManager == null) return;

        HitResult hit = mc.crosshairTarget;
        if (hit == null) return;
        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hitBlock = (BlockHitResult) hit;
            BlockPos hitBlockPos = hitBlock.getBlockPos();

            // is it can right-click?
            ActionResult tempResult = player.clientWorld.getBlockState(hitBlockPos).onUse(player.getWorld(), player, player.getActiveHand(), hitBlock);
            if (!tempResult.isAccepted()) return;

            //#if MC >= 11802
            if (mc.currentScreen != null || player.getWorld().getBlockEntity(hitBlockPos) != null) return;
            //#else
            //$$ if (mc.currentScreen != null || player.world.getBlockEntity(hitBlockPos) != null) return;
            //#endif
        } else {
            // hit target is entity or something, just end this func
            return;
        }

        // devide by 2 because of foodSaturationLevel
        if ((double) player.getHungerManager().getFoodLevel() / 10 / 2 < Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue()) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                //#if MC >= 12006
                //$$ if (stack.getItem().getComponents().contains(DataComponentTypes.FOOD)) {
                //#else
                if (stack.getItem().isFood()) {
                //#endif
                    tryToSwap(i);
                    KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), true);
                    flag = true;
                    return;
                }
            }
        }

        if (flag) {
            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), false);
            player.getInventory().selectedSlot = beforeSlot;
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(beforeSlot));
            flag = false;
        }
    }

    public static void tryToSwap(int slot) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        //#if MC >= 11802
        if (player == null || player.getWorld() == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;
        //#else
        //$$ if (player == null || player.world == null || mc.getNetworkHandler() == null || mc.interactionManager == null) return;
        //#endif
        PlayerInventory inventory = player.getInventory();
        ScreenHandler container = player.playerScreenHandler;
        if (slot >= 0 && slot!= inventory.selectedSlot && player.currentScreenHandler == player.playerScreenHandler) {
            beforeSlot = inventory.selectedSlot;
            if (PlayerInventory.isValidHotbarIndex(slot)) {
                inventory.selectedSlot = slot;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(inventory.selectedSlot));
            } else {
                if (inventory.selectedSlot != Configs.Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue()) {
                    inventory.selectedSlot = Configs.Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue();
                    Objects.requireNonNull(mc.getNetworkHandler()).sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
                }
                mc.interactionManager.clickSlot(container.syncId, slot, Configs.Generic.FOOD_SWITCHABLE_SLOT.getIntegerValue(), SlotActionType.SWAP, mc.player);
            }
        }
    }
}
