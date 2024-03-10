package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("ConstantValue")
    @Inject(method = "setSprinting", at = @At("TAIL"))
    private void persistentSprint(boolean sprinting, CallbackInfo ci) {
        if (((Object)this instanceof ClientPlayerEntity) && !sprinting) {
            RebindAllTheKeys.dontDisableSprint = false;
        }
    }

}
