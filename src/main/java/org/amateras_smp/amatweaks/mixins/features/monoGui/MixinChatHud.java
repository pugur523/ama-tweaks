package org.amateras_smp.amatweaks.mixins.features.monoGui;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 12006
//$$ import net.minecraft.client.gui.hud.ChatHudLine;
//#endif

@Mixin(ChatHud.class)
public class MixinChatHud {
    //#if MC >= 12006
    //$$ @ModifyVariable(method = "addVisibleMessage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    //$$ private ChatHudLine onAddMessage(ChatHudLine message) {
    //$$     if (FeatureToggle.TWEAK_MONO_GUI.getBooleanValue()) {
    //$$         return new ChatHudLine(message.creationTick(), removeFormatting(message.content().copy()), message.signature(), message.indicator());
    //$$     }
    //$$     return message;
    //$$ }
    //#else
    //#if MC >= 11900
    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    //#else
    //$$ @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    //#endif
    private Text onAddMessage(Text message) {
        if (FeatureToggle.TWEAK_MONO_GUI.getBooleanValue()) {
            System.out.println("debug print");
            return removeFormatting(message.copy());
            // return message.copy().fillStyle(Style.EMPTY);
        }
        return message;
    }
    //#endif

    @Unique
    private MutableText removeFormatting(MutableText text) {
        text.setStyle(Style.EMPTY);
        for (Text sibling : text.getSiblings()) {
            if (sibling instanceof MutableText mutableSibling) {
                text.append(removeFormatting(mutableSibling));
            }
        }
        return text;
    }
}
