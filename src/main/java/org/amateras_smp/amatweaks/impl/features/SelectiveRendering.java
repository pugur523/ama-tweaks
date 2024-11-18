package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.restrictions.BlockRestriction;
import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.client.MinecraftClient;
import org.amateras_smp.amatweaks.config.Configs;

public class SelectiveRendering {
    public static BlockRestriction BLOCKS_LIST = new BlockRestriction();

    public static void buildLists() {
        BLOCKS_LIST.setListType((UsageRestriction.ListType) Configs.Lists.SELECTIVE_BLOCK_RENDERING_LIST_TYPE.getOptionListValue());
        BLOCKS_LIST.setListContents(
                Configs.Lists.SELECTIVE_BLOCK_RENDERING_BLACKLIST.getStrings(),
                Configs.Lists.SELECTIVE_BLOCK_RENDERING_WHITELIST.getStrings()
        );

        // reloads all the world rendering so that the settings change will be applied.
        if (Configs.Generic.REFRESH_WORLD_RENDERER_ON_RENDER_BLOCKS_CHANGED.getBooleanValue()) {
            MinecraftClient.getInstance().worldRenderer.reload();
        }
    }
}
