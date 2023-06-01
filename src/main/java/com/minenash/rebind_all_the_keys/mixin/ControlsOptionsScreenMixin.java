package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ControlsOptionsScreen.class)
public class ControlsOptionsScreenMixin extends GameOptionsScreen{

    public ControlsOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;dimensions(IIII)Lnet/minecraft/client/gui/widget/ButtonWidget$Builder;"), index = 1)
    public int moveDoneButton(int in) {
        return in + 24;
    }

    @Inject(method = "init", at = @At(value = "INVOKE", ordinal = 6, target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addMacCommandToControl(CallbackInfo info, int i, int j, int k) {
        if (MinecraftClient.IS_SYSTEM_MAC)
            this.addDrawableChild(RebindAllTheKeys.macCommandToControl.createWidget(gameOptions, j, k, 150));
        this.addDrawableChild(RebindAllTheKeys.doubleTapSprint.createWidget(gameOptions, i, k, 150));
    }
}
