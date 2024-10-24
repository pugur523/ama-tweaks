package org.amateras_smp.amatweaks.mixins.disables.narratorHotkey;

import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.NarratorManager;
import org.amateras_smp.amatweaks.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NarratorManager.class)
public class MixinNarratorManager {
    //#if MC >= 11900
    @Inject(method = "getNarratorMode", at = @At("HEAD"), cancellable = true)
    //#else
    //$$ @Inject(method = "getNarratorOption", at = @At("HEAD"), cancellable = true)
    //#endif
    private void getNarrator(CallbackInfoReturnable<NarratorMode> ci) {
        if (Configs.Disable.DISABLE_NARRATOR_HOTKEY.getBooleanValue()) {
            ci.setReturnValue(NarratorMode.OFF);
        }
    }

    //#if MC >= 11900
    @Inject(method = "onModeChange", at = @At("HEAD"), cancellable = true)
    //#else
    //$$ @Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
    //#endif
    private void onModeChange(CallbackInfo ci) {
        if (Configs.Disable.DISABLE_NARRATOR_HOTKEY.getBooleanValue()) {
            ci.cancel();
        }
    }
}
