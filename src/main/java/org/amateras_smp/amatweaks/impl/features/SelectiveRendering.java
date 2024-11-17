package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;

public class SelectiveRendering {
    public static final BlockRestriction BLOCKS_LIST = new BlockRestriction();

    public static boolean isAllowed(BlockState state) {
        return BLOCKS_LIST.isAllowed(state.getBlock());
    }

    public static boolean isAllowed(Entity entity) {
        System.out.println(entity.getType().getName());
        return true;
    }
}
