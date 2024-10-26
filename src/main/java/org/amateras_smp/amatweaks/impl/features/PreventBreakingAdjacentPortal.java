package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PreventBreakingAdjacentPortal {
    public static final BlockRestriction PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION = new BlockRestriction();

    public static boolean restriction(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;
        if (player == null) return false;
        if (!isThereAdjacentPortal(player.clientWorld, pos)) return false;
        if (player.clientWorld.getBlockState(pos).isOf(Blocks.NETHER_PORTAL)) return false;

        Block block = player.clientWorld.getBlockState(pos).getBlock();
        return !PREVENT_BREAKING_ADJACENT_PORTAL_RESTRICTION.isAllowed(block);
    }

    private static boolean isThereAdjacentPortal(ClientWorld world, BlockPos pos) {
        return  world.getBlockState(pos.offset(Direction.WEST)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.WEST)).get(NetherPortalBlock.AXIS) == Direction.Axis.X ||
                world.getBlockState(pos.offset(Direction.EAST)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.EAST)).get(NetherPortalBlock.AXIS) == Direction.Axis.X ||
                world.getBlockState(pos.offset(Direction.NORTH)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.NORTH)).get(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
                world.getBlockState(pos.offset(Direction.SOUTH)).isOf(Blocks.NETHER_PORTAL) && world.getBlockState(pos.offset(Direction.SOUTH)).get(NetherPortalBlock.AXIS) == Direction.Axis.Z ||
                world.getBlockState(pos.offset(Direction.DOWN)).isOf(Blocks.NETHER_PORTAL) ||
                world.getBlockState(pos.offset(Direction.UP)).isOf(Blocks.NETHER_PORTAL);
    }
}
