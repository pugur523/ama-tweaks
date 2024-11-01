package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.InventoryUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import java.util.Objects;

//#if MC < 12006
import net.minecraft.nbt.NbtCompound;
//#else
//$$ import net.minecraft.component.ComponentMap;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif

public class AutoFireworkGlide {
    public static void autoUseFirework(MinecraftClient mc, ClientPlayNetworkHandler networkHandler) {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

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

        if (player.getInventory().getStack(player.getInventory().selectedSlot).isOf(Items.FIREWORK_ROCKET)) {
            use(mc, player);
            return;
        }

        int maxFlightLevelInInventory = 0;
        int targetSlot = -1;
        for (int i = 0; i < PlayerInventory.MAIN_SIZE; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isOf(Items.FIREWORK_ROCKET)) {
                //#if MC >= 12006
                //$$ ComponentMap component = stack.getComponents();
                //$$ if (component.contains(DataComponentTypes.FIREWORKS)) {
                //$$ if (component.get(DataComponentTypes.FIREWORKS) == null) return;
                //$$ int flightLevel = Objects.requireNonNull(component.get(DataComponentTypes.FIREWORKS)).flightDuration();
                //#else
                NbtCompound nbt = stack.getNbt();
                if (nbt != null && nbt.contains("Fireworks")) {
                    int flightLevel = nbt.getCompound("Fireworks").getInt("Flight");
                //#endif
                    if (flightLevel > maxFlightLevelInInventory) {
                        maxFlightLevelInInventory = flightLevel;
                        targetSlot = i;
                    }
                }
            }
        }
        if (targetSlot == -1) return;
        int originSlot = player.getInventory().selectedSlot;
        if (originSlot != targetSlot) {
            if (targetSlot != Configs.Generic.FIREWORK_SWITCHABLE_SLOT.getIntegerValue()) {
                InventoryUtils.swapSlots(player.playerScreenHandler, targetSlot, Configs.Generic.FIREWORK_SWITCHABLE_SLOT.getIntegerValue());
            }
            player.getInventory().selectedSlot = Configs.Generic.FIREWORK_SWITCHABLE_SLOT.getIntegerValue();
            networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(player.getInventory().selectedSlot));
        }
        use(mc, player);
    }

    private static void use(MinecraftClient mc, ClientPlayerEntity player) {
        if (mc.interactionManager == null) return;
        //#if MC >= 11900
        mc.interactionManager.interactItem(player, Hand.MAIN_HAND);
        //#else
        //$$ mc.interactionManager.interactItem(player, player.clientWorld, Hand.MAIN_HAND);
        //#endif
    }
}
