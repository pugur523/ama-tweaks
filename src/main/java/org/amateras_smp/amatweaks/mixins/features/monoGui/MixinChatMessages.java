package org.amateras_smp.amatweaks.mixins.features.monoGui;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.client.util.TextCollector;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.amateras_smp.amatweaks.config.FeatureToggle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ChatMessages.class)
public abstract class MixinChatMessages {

    @Inject(method = "breakRenderedChatMessageLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/StringVisitable;visit(Lnet/minecraft/text/StringVisitable$StyledVisitor;Lnet/minecraft/text/Style;)Ljava/util/Optional;", shift = At.Shift.AFTER))
    private static void onBreakRenderedChatMessageLines(StringVisitable message, int width, TextRenderer textRenderer, CallbackInfoReturnable<List<OrderedText>> cir, @Local TextCollector textCollector) {
        if (!FeatureToggle.TWEAK_MONO_CHAT.getBooleanValue()) return;
        textCollector.clear();
        message.visit((style, message2) -> {
            textCollector.add(StringVisitable.styled(message2, style.withColor(Formatting.WHITE)));
            return Optional.empty();
        }, Style.EMPTY);
    }
}