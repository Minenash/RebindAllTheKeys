package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public class MouseMixin {

    @Redirect(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
    public void blockHotbarScroll(PlayerInventory playerInventory, double scrollAmount) {
        if (scrollAmount < 0 && RebindAllTheKeys.HOTBAR_NEXT_OVERRIDE.isUnbound()
           || scrollAmount > 0 && RebindAllTheKeys.HOTBAR_PREVIOUS_OVERRIDE.isUnbound())
            playerInventory.scrollInHotbar(scrollAmount);
    }

}
