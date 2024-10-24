package org.amateras_smp.amatweaks.mixins.disables.disableNarratorHotkey;

import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.NarratorManager;
import org.amateras_smp.amatweaks.config.Configs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NarratorManager.class)
public class MixinNarratorManager {
    @Inject(method = "getNarratorMode", at = @At("HEAD"), cancellable = true)
    private void getNarrator(CallbackInfoReturnable<NarratorMode> ci) {
        if (Configs.Disable.DISABLE_NARRATOR_HOTKEY.getBooleanValue()) {
            ci.setReturnValue(NarratorMode.OFF);
        }
    }

    @Inject(method = "onModeChange", at = @At("HEAD"), cancellable = true)
    private void onModeChange(CallbackInfo ci) {
        if (Configs.Disable.DISABLE_NARRATOR_HOTKEY.getBooleanValue()) {
            ci.cancel();
        }
    }
}
