package com.minenash.bind_all_the_keys.mixin;

import com.minenash.bind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Inject(method = "updateKeysByCode", at = @At("TAIL"))
    private static void generateDebugRebinds(CallbackInfo _info) {
        RebindAllTheKeys.updateDebugKeybinds();
    }



}
