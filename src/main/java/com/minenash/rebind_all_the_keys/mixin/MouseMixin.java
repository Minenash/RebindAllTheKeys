package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.hud.SpectatorHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.swing.text.JTextComponent;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.*;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
    public void blockHotbarScroll(PlayerInventory playerInventory, double scrollAmount) {
        if (scrollAmount < 0 && RebindAllTheKeys.HOTBAR_NEXT_OVERRIDE.isUnbound()
           || scrollAmount > 0 && RebindAllTheKeys.HOTBAR_PREVIOUS_OVERRIDE.isUnbound())
            playerInventory.scrollInHotbar(scrollAmount);
    }

    @Redirect(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SpectatorHud;cycleSlot(I)V"))
    public void blockSpectatorHotbarScroll(SpectatorHud instance, int offset) {
        if (offset > 0 && RebindAllTheKeys.HOTBAR_NEXT_OVERRIDE.isUnbound()
                || offset < 0 && RebindAllTheKeys.HOTBAR_PREVIOUS_OVERRIDE.isUnbound())
            MinecraftClient.getInstance().inGameHud.getSpectatorHud().cycleSlot(-offset);
    }

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getOverlay()Lnet/minecraft/client/gui/screen/Overlay;"))
    public void activateScrollKeybinds(long window, double horizontal, double vertical, CallbackInfo ci) {
        InputUtil.Key key = Math.abs(vertical) > Math.abs(horizontal) ? vertical > 0 ? SCROLL_UP : SCROLL_DOWN : horizontal > 0 ? SCROLL_LEFT : SCROLL_RIGHT;
        KeyBinding.setKeyPressed(key, true);
        KeyBinding.onKeyPressed(key);

    }

}
