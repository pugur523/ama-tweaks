package org.amateras_smp.amatweaks.impl.tweaks;

import fi.dy.masa.itemscroller.util.InventoryUtils;
import me.fallenbreath.tweakermore.impl.features.autoContainerProcess.processors.ProcessResult;
import me.fallenbreath.tweakermore.util.RegistryUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.util.container.IContainerProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HotbarRestock implements IContainerProcessor {
    @Override
    public FeatureToggle getConfig() {
        return FeatureToggle.TWEAK_HOTBAR_RESTOCK;
    }

    public ProcessResult process(ClientPlayerEntity player, HandledScreen<?> containerScreen, List<Slot> allSlots, List<Slot> playerInvSlots, List<Slot> containerInvSlots) {
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

        HashMap<Item, Integer> restockableMap = new HashMap<>();
        List<Slot> shouldRestockSlots = new ArrayList<>();

        for (Slot containerSlot : containerInvSlots) {
            String itemId = RegistryUtil.getItemId(containerSlot.getStack().getItem());
            if (isHotbarRestockListContains(itemId)) {
                restockableMap.put(containerSlot.getStack().getItem(), restockableMap.getOrDefault(containerSlot.getStack().getItem(), 0) + containerSlot.getStack().getCount());
            }
        }
        if (restockableMap.isEmpty()) return new ProcessResult(false, false);

        for (Slot playerSlot : playerInvSlots) {
            if (restockableMap.getOrDefault(playerSlot.getStack().getItem(), 0) > 0) {
                if (playerSlot.getStack().getCount() < playerSlot.getStack().getMaxCount()) {
                    shouldRestockSlots.add(playerSlot);
                }
            }
        }

        if (shouldRestockSlots.isEmpty()) return new ProcessResult(false, false);

        // first restock
        HashMap<Item, Integer> remainingRestockableMap = executeRestock(containerScreen, shouldRestockSlots, containerInvSlots, restockableMap);

        // これは、restockできるのにインベントリにそれが存在しないがためにリストックされなかったものを2度目のrestockで空スロットに補充しようという魂胆で試したが上手くいかなかった痕跡
        //  if (remainingRestockableMap.isEmpty()) {
        //      return new ProcessResult(true, true);
        //  }
        //
        //  shouldRestockSlots.clear();
        //  for (Slot slot : playerInvSlots) {
        //      if (!slot.hasStack()) shouldRestockSlots.add(slot);
        //  }
        //
        //  // second restock : restocks items which not in player inventory
        //  executeRestock(containerScreen, shouldRestockSlots, containerInvSlots, remainingRestockableMap);

        return new ProcessResult(true, true);
    }

    private boolean isHotbarRestockListContains(String itemId) {
        return Configs.Lists.HOTBAR_RESTOCK_LIST.getStrings().stream().anyMatch(target -> target.equals(itemId));
    }

    private HashMap<Item, Integer> executeRestock(HandledScreen<?> containerScreen, List<Slot> shouldRestockSlots, List<Slot> containerSlots, HashMap<Item, Integer> restockableMap) {
        for (Slot playerSlot : shouldRestockSlots) {
            ItemStack playerStack = playerSlot.getStack().copy();
            int requireRestockAmount = playerStack.getMaxCount() - playerStack.getCount();
            int remainingRestockAmount = requireRestockAmount;
            for (int idx = containerSlots.size() - 1; idx >= 0; idx--) {
                Slot containerSlot = containerSlots.get(idx);
            // for (Slot containerSlot : containerSlots) {
                ItemStack restockStack = containerSlot.getStack().copy();
                if (restockStack.getItem() == playerStack.getItem()) {
                    int restockAmount = Math.min(remainingRestockAmount, restockStack.getCount());
                    moveToPlayerInventory(containerScreen, containerSlot, playerSlot, restockAmount);
                    remainingRestockAmount -= restockAmount;
                }
                if (remainingRestockAmount <= 0) {
                    System.out.println("restocked item. remaining amount : "  + remainingRestockAmount);
                    System.out.println("restocked " + restockStack.getItem().getTranslationKey() + " " + requireRestockAmount);
                    break;
                }
            }
            // remove as restocked item
            restockableMap.remove(playerStack.getItem());
        }
        return restockableMap;
    }

    private void moveToPlayerInventory(HandledScreen<?> containerScreen, Slot containerSlot, Slot playerSlot, int moveAmount) {
        System.out.println("moveAmount : " + moveAmount);
        if (moveAmount == containerSlot.getStack().getCount())
        {
            InventoryUtils.shiftClickSlot(containerScreen, containerSlot.id);
            return;
        }
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.id);
        for (int i = 0; i < moveAmount; i++) {
            System.out.print(i);
            InventoryUtils.rightClickSlot(containerScreen, playerSlot.id);
        }
        InventoryUtils.leftClickSlot(containerScreen, containerSlot.id);
    }
}
