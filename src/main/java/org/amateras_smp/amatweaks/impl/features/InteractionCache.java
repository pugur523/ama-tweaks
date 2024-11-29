package org.amateras_smp.amatweaks.impl.features;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;

public class InteractionCache {

    private static final InteractionCache instance = new InteractionCache();
    private LimitedQueue<BlockInteraction> blockInteractionCache = new LimitedQueue<>(60);
    private LimitedQueue<EntityInteraction> entityInteractionCache;

    public static InteractionCache getInstance() {
        return instance;
    }

    public void onBlockInteraction(ClientPlayerEntity player, BlockHitResult result) {
        String name = player.clientWorld.getBlockState(result.getBlockPos()).getBlock().getName().toString();
        BlockInteraction interaction = new BlockInteraction(name, result.getBlockPos());
        blockInteractionCache.add(interaction);
    }

    public void onEntityInteraction(ClientPlayerEntity player) {

    }

    private static class BlockInteraction {
        public String blockName;
        public BlockPos pos;

        BlockInteraction(String blockName, BlockPos pos) {
            this.blockName = blockName;
            this.pos = pos;
        }
    }

    private static class EntityInteraction {
        public String entityName;
        public BlockPos pos;

        EntityInteraction(String entityName, BlockPos pos) {
            this.entityName = entityName;
            this.pos = pos;
        }
    }
}
