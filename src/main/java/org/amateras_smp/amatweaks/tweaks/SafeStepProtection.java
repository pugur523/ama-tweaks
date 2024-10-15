package org.amateras_smp.amatweaks.tweaks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class SafeStepProtection {
    public static boolean isPositionAllowedByBreakingRestriction(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;

        if (player != null) {
            boolean isMovingBack = mc.options.backKey.isPressed();
            boolean isMoving = mc.player.getVelocity().x != 0 || mc.player.getVelocity().z != 0;

            boolean isPosBelowPlayer = pos.getY() < player.getY();

            return (!isMoving || isMovingBack) || !isPosBelowPlayer;
        }

        return true;
    }
}
