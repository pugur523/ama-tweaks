package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;

public class InteractionCache {

    private static final LimitedQueue<BlockInteraction> blockInteractionCache = new LimitedQueue<>(10);
    private static final LimitedQueue<EntityInteraction> entityInteractionCache = new LimitedQueue<>(10);

    public static void resize() {
        blockInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
        entityInteractionCache.setMaxSize(Configs.Generic.INTERACTION_CACHE_COUNT.getIntegerValue());
    }

    public static void onBlockInteraction(ClientPlayerEntity player, BlockHitResult result) {
        String name = player.clientWorld.getBlockState(result.getBlockPos()).getBlock().getName().toString();
        BlockInteraction interaction = new BlockInteraction(name, result.getBlockPos());
        blockInteractionCache.add(interaction);
    }

    public static void onEntityInteraction(ClientPlayerEntity player) {

    }

    public static BlockInteraction getBlockInteraction(int index) {
        return blockInteractionCache.get(index);
    }

    public static EntityInteraction getEntityInteraction(int index) {
        return entityInteractionCache.get(index);
    }

    public static class BlockInteraction {
        public String blockName;
        public BlockPos pos;

        BlockInteraction(String blockName, BlockPos pos) {
            this.blockName = blockName;
            this.pos = pos;
        }
    }

    public static class EntityInteraction {
        public String entityName;
        public BlockPos pos;

        EntityInteraction(String entityName, BlockPos pos) {
            this.entityName = entityName;
            this.pos = pos;
        }
    }
}
