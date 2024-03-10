package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
        return in + 24+24+24;
    }

    @Inject(method = "init", at = @At(value = "INVOKE", ordinal = 6, target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addMacCommandToControl(CallbackInfo info, int i, int j, int k) {
        this.addDrawableChild(RebindAllTheKeys.doubleTapSprint.createWidget(gameOptions, i, k, 150));
        this.addDrawableChild(RebindAllTheKeys.doubleTapFly.createWidget(gameOptions, j, k, 150));
//        this.addDrawableChild(RebindAllTheKeys.persistentSprint.createWidget(gameOptions, i, k+24, 150));
//        this.addDrawableChild(RebindAllTheKeys.persistentSneak.createWidget(gameOptions, j, k+24, 150));
                if (MinecraftClient.IS_SYSTEM_MAC)
        this.addDrawableChild(RebindAllTheKeys.macCommandToControl.createWidget(gameOptions, i, k+24, 150));
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"), index = 0)
    public Element addPersistentSprintMode(Element oldSneak) {
        ClickableWidget old = (ClickableWidget) oldSneak;

        var option = gameOptions.getSprintToggled();
        return ButtonWidget.builder(getSprintModeText(option), button -> {
            // Hold(false),false → Hold(false),true, Toggle(true),true
            if (option.getValue()) {
                option.setValue(false);
                RebindAllTheKeys.persistentSprint.setValue(false);
            }
            else if (RebindAllTheKeys.persistentSprint.getValue()) {
                option.setValue(true);
            }
            else {
                RebindAllTheKeys.persistentSprint.setValue(true);
            }

            button.setMessage(getSprintModeText(option));
            System.out.println("Now: " + option.getValue() + ", " + RebindAllTheKeys.persistentSprint);
        }).dimensions(old.getX(), old.getY(), old.getWidth(), old.getHeight()).build();
    }

    @ModifyArg(method = "init", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/client/gui/screen/option/ControlsOptionsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"), index = 0)
    public Element addPersistentSneakMode(Element oldSneak) {
        ClickableWidget old = (ClickableWidget) oldSneak;

        var option = gameOptions.getSneakToggled();
        return ButtonWidget.builder(getSneaktModeText(option),button -> {
            // Hold(false),false → Toggle(true),true, Toggle(true),false
            if (!option.getValue()) {
                option.setValue(true);
                RebindAllTheKeys.persistentSneak.setValue(true);
            }
            else if (RebindAllTheKeys.persistentSneak.getValue()) {
                RebindAllTheKeys.persistentSneak.setValue(false);
            }
            else {
                option.setValue(false);
                RebindAllTheKeys.persistentSneak.setValue(false);
            }
            button.setMessage(getSneaktModeText(option));
            System.out.println("Now: " + option.getValue() + ", " + RebindAllTheKeys.persistentSneak);
        }).dimensions(old.getX(), old.getY(), old.getWidth(), old.getHeight()).build();
    }

    @Unique
    private static Text getSprintModeText(SimpleOption<Boolean> option) {
        return Text.translatable("key.sprint").append(": ").append(Text.translatable(option.getValue() ? "options.key.toggle" : RebindAllTheKeys.persistentSprint.getValue() ? "rebind_all_the_keys.key.persistent" : "options.key.hold"));
    }

    @Unique
    private static Text getSneaktModeText(SimpleOption<Boolean> option) {
        return Text.translatable("key.sneak").append(": ").append(Text.translatable(option.getValue() ? RebindAllTheKeys.persistentSneak.getValue() ? "rebind_all_the_keys.key.persistent" : "options.key.toggle" : "options.key.hold"));
    }


}
