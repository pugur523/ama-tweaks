package org.amateras_smp.amatweaks.mixins.features.hotbarRestock;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import org.amateras_smp.amatweaks.Reference;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.container.AutoProcessableScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(Reference.ModIds.itemscroller))
@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Inject(
            //#if MC >= 11600
            method = "onOpenScreen",
            //#else
            //$$ method = "onOpenContainer",
            //#endif
            at = @At("TAIL")
    )
    private void onOpenScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen != null) {
            ((AutoProcessableScreen) screen).setShouldProcess$AMT(!Configs.Generic.AUTO_RESTOCK_ONLY_ALLOW_SHULKER_BOX.getBooleanValue() || packet.getScreenHandlerType() == ScreenHandlerType.SHULKER_BOX);
        }
    }
}
