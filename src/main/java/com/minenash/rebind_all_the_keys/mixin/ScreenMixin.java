package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.*;

@Mixin(Screen.class)
public class ScreenMixin {

    @Redirect(method = "hasControlDown", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;IS_SYSTEM_MAC:Z"))
    private static boolean swapCommand() {
        return MinecraftClient.IS_SYSTEM_MAC;
    }

    @ModifyConstant(method = "keyPressed", constant = @Constant(intValue = GLFW.GLFW_KEY_ESCAPE /*256*/))
    public int remapQuitKey(int _key) {
	return RebindAllTheKeys.getKeyCode(QUIT);
    }
}
