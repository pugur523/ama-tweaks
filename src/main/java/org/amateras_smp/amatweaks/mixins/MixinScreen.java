package org.amateras_smp.amatweaks.mixins;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.tweakermore.util.ModIds;
import net.minecraft.client.gui.screen.Screen;
import org.amateras_smp.amatweaks.impl.util.container.AutoProcessableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Restriction(require = @Condition(ModIds.itemscroller))
@Mixin(Screen.class)
public abstract class MixinScreen implements AutoProcessableScreen
{
    @Unique
    private boolean shouldProcessFlag = false;

    @Override
    public void setShouldProcess$AMT(boolean value)
    {
        this.shouldProcessFlag = value;
    }

    @Override
    public boolean shouldProcess$AMT()
    {
        return this.shouldProcessFlag;
    }
}
