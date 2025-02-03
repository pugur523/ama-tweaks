// Copyright (c) 2025 The Ama-Tweaks Authors
// This file is part of the Ama-Tweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.mixins.features.preventbreakportal;

import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BellBlock.class)
public interface IMixinBellBlock {
    @Invoker("isPointOnBell")
    boolean ModIsPointOnBell(BlockState state, Direction side, double y);
}
