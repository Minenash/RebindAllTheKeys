package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow @Final protected MinecraftClient client;

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    private void disableDoubleTapSprint(ClientPlayerEntity instance, boolean sprinting) {
        if (RebindAllTheKeys.doubleTapSprint.getValue() || client.options.sprintKey.isPressed())
            ((ClientPlayerEntity)(Object)this).setSprinting(true);
    }
}
