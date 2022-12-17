package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.QUIT_ALIAS;

@Mixin(Screen.class)
public class ScreenMixin {

    @Redirect(method = "hasControlDown", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;IS_SYSTEM_MAC:Z"))
    private static boolean swapCommand() {
        return MinecraftClient.IS_SYSTEM_MAC;
    }

    int key;
    @Inject(method = "keyPressed", at = @At("HEAD"))
    public void getKey(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.key = keyCode;
    }

    @ModifyConstant(method = "keyPressed", constant = @Constant(intValue = GLFW.GLFW_KEY_ESCAPE /*256*/))
    public int remapQuitKey(int _key) {
        if (key == 256)
            return key;
        return RebindAllTheKeys.getKeyCode(QUIT_ALIAS);
    }
}
