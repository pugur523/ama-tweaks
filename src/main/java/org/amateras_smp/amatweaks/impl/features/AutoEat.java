// Copyright (c) 2025 The Ama-Tweaks Authors
// This file is part of the Ama-Tweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
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
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;

import java.util.Objects;

//#if MC >= 12006
//$$ import net.minecraft.component.ComponentMap;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif


public class AutoEat {
    private static int beforeSlot;
    private static boolean eating = false;

    public static void autoEat(MinecraftClient mc, ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler) {
        if ((double) player.getHungerManager().getFoodLevel() / 20 <= Configs.Generic.AUTO_EAT_THRESHOLD.getDoubleValue() && player.getHungerManager().isNotFull()) {
            if (shouldAutoEat(mc)) {
                HitResult hit = mc.crosshairTarget;
                if (hit == null) return;
                if (hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult hitBlock = (BlockHitResult) hit;
                    BlockPos hitBlockPos = hitBlock.getBlockPos();

                    // make sure hitBlock can't be interacted.
                    if (BlockTypeEquals.isSneakingInteractionCancel(player.getWorld().getBlockState(hitBlockPos))) {
                        return;
                    }
                } else if (hit.getType() == HitResult.Type.ENTITY) {
                    // hit target is entity, so just end this function.
                    return;
                }

                // divide by 2 because of foodSaturationLevel
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    //#if MC >= 12006
                    //$$ if (stack.getItem().getComponents().contains(DataComponentTypes.FOOD)) {
                    //#else
                    if (stack.getItem().isFood()) {
                        //#endif
                        tryToSwap(i);
                        if (eating) {
                            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), true);
                        }
                        eating = true;
                        return;
                    }
                }
            }
        } else {
            autoEatCheck(mc, player, networkHandler);
        }

    }

    private static boolean shouldAutoEat(MinecraftClient mc) {
        if (mc.interactionManager == null) return false;
        if (mc.interactionManager.getCurrentGameMode().isCreative()) return false;
        if (!Configs.Generic.CANCEL_AUTO_EAT_WHILE_DOING_ACTION.getBooleanValue()) return true;
        return !mc.options.useKey.isPressed() && !mc.options.attackKey.isPressed();
    }

    public static void autoEatCheck(MinecraftClient mc, ClientPlayerEntity player, ClientPlayNetworkHandler networkHandler) {
        if (eating) {
            player.getInventory().selectedSlot = beforeSlot;
            networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(beforeSlot));
            eating = false;
            KeyBinding.setKeyPressed(InputUtil.fromTranslationKey(mc.options.useKey.getBoundKeyTranslationKey()), false);
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
