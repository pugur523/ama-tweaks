// This file includes code from taichi-tweaks, distributed under the MIT license.

// https://github.com/TaichiServer/taichi-tweaks

// Modified by pugur on 2024/10/16

// The MIT License (MIT)

// Copyright (c) 2024 topi_banana

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package org.amateras_smp.amatweaks.tweaks;

import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.mixin.IBellBlockMixin;
import org.amateras_smp.amatweaks.util.BlockTypeEquals;

import static net.minecraft.block.NetherPortalBlock.AXIS;

public class PlacementOnPortalSides {
    public static boolean restriction(World world, ItemPlacementContext ctx, BlockHitResult hitResult) {
        if(!FeatureToggle.DISABLE_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return false;

        if (ctx == null) {
            return false;
        }

        ItemStack itemStack = ctx.getStack();
        if (itemStack.isEmpty()) {
            return false;
        }

        BlockState blockState = world.getBlockState(hitResult.getBlockPos());
        if (blockState.isOf(Blocks.SCAFFOLDING) && itemStack.getItem() instanceof ScaffoldingItem scaffolding) {
            ctx = scaffolding.getPlacementContext(ctx);
            if (ctx == null) {
                return false;
            }
        }
        BlockPos blockPos2 = ctx.getBlockPos();

        while (true) {
            if (checkNeighbors(world, blockPos2.north(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos2.south(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos2.east(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos2.west(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos2.up(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos2.down(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos())
            ) {
                return true;
            }

            /*
            if (itemStack.getItem() instanceof TallBlockItem || itemStack.isOf(Items.PITCHER_PLANT)) {
                if (blockPos2.equals(hitResult.getBlockPos().offset(hitResult.getSide()))) {
                    blockPos2 = blockPos2.up();
                } else {
                    break;
                }
            } else */
            if (itemStack.getItem() instanceof BedItem) {
                if (blockPos2.equals(hitResult.getBlockPos().offset(hitResult.getSide()))) {
                    // TODO in 1.20.1 use ctx.getHorizontalPlayerFacing();
                    Direction direction = ctx.getPlayerFacing();
                    blockPos2 = blockPos2.offset(direction);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return false;
    }


    public static boolean checkNeighbors(World world, BlockPos blockPos, Direction.Axis axis, ItemPlacementContext ctx, BlockHitResult hitResult, BlockPos origin) {
        BlockState blockState = world.getBlockState(blockPos);
        ItemStack itemStack = ctx.getStack();
        if (blockState.isOf(Blocks.NETHER_PORTAL)) {
            if (Direction.Axis.Y == axis || blockState.get(AXIS) == axis) {
                if (!itemStack.isOf(Items.SCAFFOLDING) && ctx.canReplaceExisting()) {
                    return false;
                }
                if (!ctx.canPlace()) {
                    return false;
                }
                if (!canUse(ctx, origin, hitResult)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean canUse(ItemPlacementContext context, BlockPos origin, BlockHitResult hitResult) {
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(origin);
        Block block = blockState.getBlock();

        if (BlockTypeEquals.isSneakingInteractionCancel(blockState)) {
            if (context.shouldCancelInteraction()) {
                return true;
            } else {
                return false;
            }
        } else if (blockState.isOf(Blocks.BELL)) {
            if ((!canRing((BellBlock) block, blockState, hitResult, origin))) {
                return true;
            } else if (context.shouldCancelInteraction()){
                return true;
            } else {
                return false;
            }
        }

        return true;
    }


    public static boolean canRing(BellBlock bell, BlockState blockState, BlockHitResult hitResult, BlockPos blockPos) {
        return ((IBellBlockMixin) bell).ModIsPointOnBell(blockState, hitResult.getSide(), hitResult.getPos().y - (double)blockPos.getY());
    }
}
