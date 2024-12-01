package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;

public class InteractionCache {

    public static final LimitedQueue<BlockInteraction> blockInteractionCache = new LimitedQueue<>(10);
    public static final LimitedQueue<EntityInteraction> entityInteractionCache = new LimitedQueue<>(10);

    public static void resize() {
        blockInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
        entityInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
    }

    public static void onBlockInteraction(ClientPlayerEntity player, BlockHitResult result) {
        String name = player.clientWorld.getBlockState(result.getBlockPos()).getBlock().getName().toString();
        BlockInteraction interaction = new BlockInteraction(name, result.getBlockPos());
        blockInteractionCache.add(interaction);
    }

    public static void onEntityInteraction(Entity entity) {
        String name = entity.getName().toString();
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

        BlockInteraction(String blockName, BlockPos pos) {
            this.blockName = blockName;
            this.pos = pos;
        }

        public String toString() {
            return this.blockName + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
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
