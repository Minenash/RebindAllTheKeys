package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.DROP_STACK_MODIFIER;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Redirect(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"))
    private boolean handleInputEvents() {
        return DROP_STACK_MODIFIER.isDefault() ? HandledScreen.hasControlDown() : RebindAllTheKeys.isKeybindPressed(DROP_STACK_MODIFIER);
    }
}