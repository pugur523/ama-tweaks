package org.amateras_smp.amatweaks.mixins.features.preventBreakingAdjacentPortal;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.InfoUtils;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.amateras_smp.amatweaks.impl.features.PreventBreakingAdjacentPortal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
        if (!FeatureToggle.TWEAK_PREVENT_BREAKING_ADJACENT_PORTAL.getBooleanValue()) return;
        if (!PreventBreakingAdjacentPortal.restriction(pos)) return;
        String preRed = GuiBase.TXT_RED;
        String rst = GuiBase.TXT_RST;
        String message = preRed + "breaking restricted by tweakPreventBreakingAdjacentToPortal" + rst;
        InfoUtils.printActionbarMessage(message);

        cir.setReturnValue(false);
    }
}
