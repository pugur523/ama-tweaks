package org.amateras_smp.amatweaks.tweaks;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import me.fallenbreath.tweakermore.util.RegistryUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.util.container.IContainerProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class HotbarRestock implements IContainerProcessor {
    @Override
    public FeatureToggle getConfig() {
        return FeatureToggle.TWEAK_HOTBAR_RESTOCK;
    }

    @Override
    public ProcessResult process(ClientPlayerEntity player, HandledScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots) {
        boolean result = false;
        MinecraftClient mc = MinecraftClient.getInstance();
        HitResult hit = mc.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) return new ProcessResult(false, false);
        BlockHitResult hitBlock = (BlockHitResult) hit;
        BlockPos hitBlockPos = hitBlock.getBlockPos();
        World clientWorld = mc.world;

        if(mc.player == null || clientWorld == null) return new ProcessResult(false, false);
        BlockEntity container = clientWorld.getBlockEntity(hitBlockPos);
        if (container == null) return new ProcessResult(false, false);
        if (container instanceof EnderChestBlockEntity) {
            return new ProcessResult(false, false);
        }

        // HashMap<Item, Integer> restockedItems = new HashMap<>();
        HashSet<Item> restockedItems = new HashSet<>();
        for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
            ItemStack clientStack = player.getInventory().getStack(i).copy();
            String clientItemId = RegistryUtil.getItemId(clientStack.getItem());
            if (Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings().stream().noneMatch(target -> target.equals(clientItemId))) continue;
            if (clientStack.getMaxCount() == clientStack.getCount()) {
                // restockedItems.add(player.getInventory().getStack(i).getItem());
                continue;
            }

            for (Slot containerSlot : containerInvSlots) {
                ItemStack containerStack = containerSlot.getStack().copy();
                if (Objects.equals(RegistryUtil.getItemId(containerStack.getItem()), clientItemId) && !restockedItems.contains(containerStack.getItem())) {
                    // update amount of clientStack
                    int missingAmount = Math.min(clientStack.getMaxCount() - player.getInventory().getStack(i).getCount(), containerStack.getCount());
                    int remainAmount = clientStack.getMaxCount() - missingAmount;
                    System.out.println(missingAmount);
                    System.out.println(remainAmount);

                    InventoryUtils.leftClickSlot(containerScreen, containerSlot.getIndex());
                    for (int k = 0; k < remainAmount; k++) {
                        InventoryUtils.rightClickSlot(containerScreen, containerSlot.getIndex());
                    }
                    InventoryUtils.leftClickSlot(containerScreen, i);
                    if (player.getInventory().getStack(i).getCount() == player.getInventory().getStack(i).getMaxCount()) {
                        restockedItems.add(player.getInventory().getStack(i).getItem());
                        continue;
                    }
                    result = true;
                }
            }

        }

        for (int i = 0; i < PlayerInventory.getHotbarSize(); i++) {
            String itemId = RegistryUtil.getItemId(player.getInventory().getStack(i).getItem());
            if (Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings().stream().noneMatch(target -> target.equals(itemId))) continue;
            restockedItems.add(player.getInventory().getStack(i).getItem());
        }
        for (Slot containerSlot : containerInvSlots) {
            ItemStack containerStack = containerSlot.getStack().copy();
            if (Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings().
                    stream().anyMatch(target -> target.equals(RegistryUtil.getItemId(containerStack.getItem())) && !restockedItems.contains(containerStack.getItem()))) {
                int clickSlot = getClickSlot(containerStack, PlayerInventory.getHotbarSize());
                if (clickSlot < 0) continue;
                InventoryUtils.leftClickSlot(containerScreen, containerSlot.getIndex());
                InventoryUtils.leftClickSlot(containerScreen, clickSlot);
                result = true;
            }
        }
        return new ProcessResult(true, result);
    }

    public int getClickSlot(ItemStack itemStack, int maxSlotIdx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return -2;
        for (int i = 0; i < maxSlotIdx; i++) {
            if (mc.player.getInventory().getStack(i).isEmpty() || InventoryUtils.areStacksEqual(itemStack, mc.player.getInventory().getStack(i))) return i;
        }
        return -1;
    }
}
