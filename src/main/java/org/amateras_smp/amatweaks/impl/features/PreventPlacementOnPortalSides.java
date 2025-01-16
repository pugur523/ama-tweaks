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

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
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
import org.amateras_smp.amatweaks.impl.util.BlockTypeEquals;
import org.amateras_smp.amatweaks.mixins.features.preventPlacementOnPortalSides.IMixinBellBlock;

import static net.minecraft.block.NetherPortalBlock.AXIS;

public class PreventPlacementOnPortalSides {
    public static boolean restriction(World world, ItemPlacementContext ctx, BlockHitResult hitResult) {
        if (!FeatureToggle.TWEAK_PREVENT_PLACEMENT_ON_PORTAL_SIDES.getBooleanValue()) return false;
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
        BlockPos blockPos = ctx.getBlockPos();
        boolean firstTime = true;

        while (true) {
            if (checkNeighbors(world, blockPos.north(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos.south(), Direction.Axis.Z, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos.east(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos.west(), Direction.Axis.X, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos.up(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos()) ||
                    checkNeighbors(world, blockPos.down(), Direction.Axis.Y, ctx, hitResult, hitResult.getBlockPos())
            ) {
                String preRed = GuiBase.TXT_RED;
                String rst = GuiBase.TXT_RST;
                String message = preRed + "placement restricted by tweakPreventPlacementOnPortalSides" + rst;
                InfoUtils.printActionbarMessage(message);
                return true;
            }


            //#if MC >= 12000
            if (itemStack.getItem() instanceof TallBlockItem || itemStack.isOf(Items.PITCHER_PLANT)) {
            //#else
            //$$ if (itemStack.getItem() instanceof TallBlockItem) {
            //#endif
                if (blockPos.equals(hitResult.getBlockPos().offset(hitResult.getSide()))) {
                    blockPos = blockPos.up();
                } else if (firstTime) {
                    blockPos = blockPos.up();
                } else {
                    break;
                }
                firstTime = false;
            } else
            if (itemStack.getItem() instanceof BedItem) {
                if (blockPos.equals(hitResult.getBlockPos().offset(hitResult.getSide()))) {
                    //#if MC > 11900
                    Direction direction = ctx.getHorizontalPlayerFacing();
                    //#else
                    //$$ Direction direction = ctx.getPlayerFacing();
                    //#endif
                    blockPos = blockPos.offset(direction);
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
        if (blockState.isOf(Blocks.NETHER_PORTAL)) {
            if (Direction.Axis.Y == axis || blockState.get(AXIS) == axis) {
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
        return ((IMixinBellBlock) bell).ModIsPointOnBell(blockState, hitResult.getSide(), hitResult.getPos().y - (double)blockPos.getY());
    }
}
