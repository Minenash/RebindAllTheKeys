package com.minenash.rebind_all_the_keys.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(InputUtil.class)
public class InputKeyMixin {

    @Inject(method = "isKeyPressed", at = @At("HEAD"), cancellable = true)
    private static void silenceError(long handle, int key, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (key < -1) {
            callbackInfo.setReturnValue(false);
            callbackInfo.cancel();
        }
    }
}
