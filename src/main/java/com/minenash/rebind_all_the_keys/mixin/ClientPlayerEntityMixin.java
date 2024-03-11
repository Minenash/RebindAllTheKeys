package com.minenash.rebind_all_the_keys.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    @Shadow @Final protected MinecraftClient client;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    private void disableDoubleTapSprint(ClientPlayerEntity instance, boolean sprinting) {
        if (RebindAllTheKeys.doubleTapSprint.getValue() || client.options.sprintKey.isPressed())
            ((ClientPlayerEntity)(Object)this).setSprinting(true);
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;canSprint()Z"))
    private boolean cancelSprintIfNotPersistent(boolean original) {
        return (RebindAllTheKeys.dontDisableSprint || RebindAllTheKeys.persistentSprint.getValue() || client.options.sprintKey.isPressed()) && original;
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    public void setDontDisableSprint(CallbackInfo ci) {
        RebindAllTheKeys.dontDisableSprint = true;
    }

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "FIELD", ordinal = 2, target = "Lnet/minecraft/client/input/Input;jumping:Z"))
    public boolean disableDoubleTapFlyAndProcessJumpButton(boolean original) {

        boolean flying = getAbilities().flying;
        while (RebindAllTheKeys.FLY.wasPressed()) {
            getAbilities().flying = !getAbilities().flying;
        }
        boolean newFlying = getAbilities().flying;
        if (newFlying != flying) {
            bl8 = true;
            this.sendAbilitiesUpdate();
            if (newFlying)
                setPosition(getX(), getY() + 0.25, getZ());
        }

        return RebindAllTheKeys.doubleTapFly.getValue() && original;
    }

    @Unique
    private static boolean bl8 = false;

    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "FIELD", ordinal = 3, target = "Lnet/minecraft/client/input/Input;jumping:Z"))
    public boolean correctBl8(boolean original) {
        return original && !bl8;
    }
}
