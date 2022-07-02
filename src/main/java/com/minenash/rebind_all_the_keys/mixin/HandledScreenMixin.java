package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Redirect(method = "mouseReleased", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;hasShiftDown()Z"))
    public boolean rebindShift() {
        return RebindAllTheKeys.QUICK_MOVE.isDefault() ? HandledScreen.hasShiftDown() : RebindAllTheKeys.isKeybindPressed(RebindAllTheKeys.QUICK_MOVE);
    }

    @Redirect(method = "mouseReleased", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/util/InputUtil;isKeyPressed(JI)Z"))
    public boolean rebindShift2(long _handle, int _code) {
        return RebindAllTheKeys.QUICK_MOVE.isDefault() ? HandledScreen.hasShiftDown() : RebindAllTheKeys.isKeybindPressed(RebindAllTheKeys.QUICK_MOVE);
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/util/InputUtil;isKeyPressed(JI)Z"))
    public boolean rebindShift3(long _handle, int _code) {
        return RebindAllTheKeys.QUICK_MOVE.isDefault() ? HandledScreen.hasShiftDown() : RebindAllTheKeys.isKeybindPressed(RebindAllTheKeys.QUICK_MOVE);
    }

}
