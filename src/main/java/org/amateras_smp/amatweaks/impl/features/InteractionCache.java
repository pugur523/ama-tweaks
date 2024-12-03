package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;
import org.jetbrains.annotations.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class InteractionCache {

    public static final LimitedQueue<BlockInteraction> blockInteractionCache = new LimitedQueue<>(10);
    public static final LimitedQueue<ItemInteraction> itemInteractionCache = new LimitedQueue<>(10);
    public static final LimitedQueue<EntityInteraction> entityInteractionCache = new LimitedQueue<>(10);

    public static void resize() {
        blockInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
        itemInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
        entityInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
    }

    public static void onBlockInteraction(Block block, BlockPos pos) {
        String name = StringUtils.translate(block.getTranslationKey());
        BlockInteraction interaction = new BlockInteraction(name, pos);
        blockInteractionCache.add(interaction);
    }

    public static void onItemInteraction(Item item) {
        String name = StringUtils.translate(item.getTranslationKey());
        ItemInteraction interaction = new ItemInteraction(name);
        itemInteractionCache.add(interaction);
    }

    public static void onEntityInteraction(Entity entity) {
        String name = StringUtils.translate(entity.getType().getTranslationKey());
        EntityInteraction interaction = new EntityInteraction(name, entity.getPos());
        entityInteractionCache.add(interaction);
    }

    public static void printInteraction() {
        for (BlockInteraction b : blockInteractionCache) {
            System.out.println(b.toString());
        }
        for (ItemInteraction i : itemInteractionCache) {
            System.out.println(i.toString());
        }
        for (EntityInteraction e : entityInteractionCache) {
            System.out.println(e.toString());
        }
    }

    public static class BlockInteraction {
        public String blockName;
        public BlockPos pos;

        BlockInteraction(String blockName, @Nullable BlockPos pos) {
            this.blockName = blockName;
            this.pos = pos;
        }

        public String toString() {
            return this.blockName + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        }
    }

    public static class ItemInteraction {
        public String itemName;

        ItemInteraction(String itemName) {
            this.itemName = itemName;
        }

        public String toString() {
            return this.itemName;
        }
    }

    public static class EntityInteraction {
        public String entityName;
        public Vec3d pos;

        EntityInteraction(String entityName, Vec3d pos) {
            this.entityName = entityName;
            this.pos = pos;
        }

        public String toString() {
            return this.entityName + " (" + roundDouble(pos.getX()) + ", " + roundDouble(pos.getY()) + ", " + roundDouble(pos.getZ()) + ")";
        }

        private double roundDouble(double d) {
            BigDecimal bd = new BigDecimal(d);
            BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
            return rounded.doubleValue();
        }
    }
}
