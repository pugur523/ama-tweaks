package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;
import org.jetbrains.annotations.Nullable;

public class InteractionCache {

    public static final LimitedQueue<BlockInteraction> blockInteractionCache = new LimitedQueue<>(10);
    public static final LimitedQueue<EntityInteraction> entityInteractionCache = new LimitedQueue<>(10);

    public static void resize() {
        blockInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
        entityInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
    }

    public static void onBlockInteraction(Item item, @Nullable BlockPos pos) {
        String name = StringUtils.translate(item.getTranslationKey());
        BlockInteraction interaction = new BlockInteraction(name, pos);
        blockInteractionCache.add(interaction);
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
            if (pos == null) {
                return this.blockName;
            } else {
                return this.blockName + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
            }
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
            return this.entityName + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        }
    }
}
