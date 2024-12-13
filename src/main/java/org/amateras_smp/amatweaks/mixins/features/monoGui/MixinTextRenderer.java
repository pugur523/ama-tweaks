package org.amateras_smp.amatweaks.mixins.features.monoGui;

import net.minecraft.client.font.TextRenderer.Drawer;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Drawer.class)
public class MixinTextRenderer {
    @ModifyVariable(method = "accept(ILnet/minecraft/text/Style;I)Z", at = @At("HEAD"), argsOnly = true)
    private Style onAccept(Style style){
        return Style.EMPTY;
    }
}
