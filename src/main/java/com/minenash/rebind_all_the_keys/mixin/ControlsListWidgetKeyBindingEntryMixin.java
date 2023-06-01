package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Environment(EnvType.CLIENT)
@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class ControlsListWidgetKeyBindingEntryMixin {

    @Shadow @Final private KeyBinding binding;

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;equals(Lnet/minecraft/client/option/KeyBinding;)Z"))
    public boolean ignoreConflictIfDebug(KeyBinding a, KeyBinding b) {
        if (a == RebindAllTheKeys.INTENTIONAL_CRASH || b == RebindAllTheKeys.INTENTIONAL_CRASH)
            return false;
        if (a != RebindAllTheKeys.DEBUG_KEY && b != RebindAllTheKeys.DEBUG_KEY
                && a.getCategory().equals("rebind_all_the_keys.keybind_group.debug") !=  b.getCategory().equals("rebind_all_the_keys.keybind_group.debug"))
            return false;
        return a.equals(b);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/client/gui/widget/ButtonWidget;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
    public void addHeldSuffix(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (binding == RebindAllTheKeys.INTENTIONAL_CRASH && !binding.isUnbound()) {
            String msg = button.getMessage().getString();
            if (!(msg.contains(">") && msg.contains("<"))
               && !msg.contains(I18n.translate("rebind_all_the_keys.keybind.intentional_crash.button.held")))
                button.setMessage(button.getMessage().copy().append(Text.translatable("rebind_all_the_keys.keybind.intentional_crash.button.held")));
        }
        button.renderButton(matrices, mouseX, mouseY, delta);

    }


}
