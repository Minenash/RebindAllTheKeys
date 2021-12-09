package com.minenash.rebind_all_the_keys.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin {

    @Redirect(method = "keyPressed", at = @At(value = "INVOKE", ordinal = 1,
            target = "Lnet/minecraft/client/option/GameOptions;setKeyCode(Lnet/minecraft/client/option/KeyBinding;Lnet/minecraft/client/util/InputUtil$Key;)V"))
    public void setKeyCode(GameOptions options, KeyBinding binding, InputUtil.Key _key, int keyCode, int scanCode) {
        boolean setNegative = binding.getCategory().equals("rebind_all_the_keys.keybind_group.debug") && !binding.getTranslationKey().equals("rebind_all_the_keys.keybind.debug_key");
        options.setKeyCode(binding, InputUtil.fromKeyCode(setNegative ? -keyCode : keyCode, scanCode));
    }

}
