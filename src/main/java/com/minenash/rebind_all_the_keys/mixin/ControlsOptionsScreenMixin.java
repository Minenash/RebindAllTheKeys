package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public class ControlsOptionsScreenMixin extends GameOptionsScreen{

    public ControlsOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", ordinal = 5, target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    <T extends net.minecraft.client.gui.Element & net.minecraft.client.gui.Drawable & net.minecraft.client.gui.Selectable> T moveButton(ControlsOptionsScreen instance, T element) {
        ((ButtonWidget)element).y += MinecraftClient.IS_SYSTEM_MAC ? 48 : 24;
        return addDrawableChild(element);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void addMacCommandToControl(CallbackInfo info) {
        if (MinecraftClient.IS_SYSTEM_MAC)
            this.addDrawableChild(RebindAllTheKeys.macCommandToControl.createButton(gameOptions, width / 2 - 155, height / 6 + 36+24, 150));
        this.addDrawableChild(RebindAllTheKeys.doubleTapSprint.createButton(gameOptions, width / 2 + 5, height / 6 +36, 150));
    }
}
