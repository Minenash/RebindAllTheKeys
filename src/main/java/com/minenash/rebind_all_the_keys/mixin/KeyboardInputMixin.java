package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.StickyKeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Unique
    private static boolean movedPriorCheck = false;

    @Inject(method = "tick", at = @At(value = "INVOKE", ordinal = 5, target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
    private void persistentSneak(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        boolean moved = pressingForward || pressingBack || pressingLeft || pressingRight;

        if (RebindAllTheKeys.persistentSneak.getValue() && !moved && movedPriorCheck)
            ((StickyKeyBinding)MinecraftClient.getInstance().options.sneakKey).untoggle();

        movedPriorCheck = moved;
    }

}
