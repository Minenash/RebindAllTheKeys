package com.minenash.rebind_all_the_keys.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import com.minenash.rebind_all_the_keys.mixin.KeyBindingAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(KeyBinding.class)
public class KeyBindingMixin /* implements Comparable<KeyBinding> */ {
    private static final Map<InputUtil.Key, List<KeyBinding>> KEY_TO_MULTI_BINDINGS = Maps.newHashMap();

    @Inject(method = "onKeyPressed", at = @At("RETURN"))
    private static void onKeyPressedMultiBindings(InputUtil.Key key, CallbackInfo _info) {
        List<KeyBinding> bindings = KEY_TO_MULTI_BINDINGS.get(key);
        if (bindings == null) {
            return;
        }

        KeyBinding vanilla = KeyBindingAccessor.getKeyToBindings().get(key);
        for (KeyBinding binding : bindings) {
            if (binding == vanilla) {
                continue;
            }
            KeyBindingAccessor accessor = (KeyBindingAccessor) binding;
            accessor.setTimesPressed(accessor.getTimesPressed() + 1);
        }
    }

    @Inject(method = "setKeyPressed", at = @At("RETURN"))
    private static void setKeyPressedMultiBindings(InputUtil.Key key, boolean pressed, CallbackInfo _info) {
        List<KeyBinding> bindings = KEY_TO_MULTI_BINDINGS.get(key);
        if (bindings == null) {
            return;
        }

        KeyBinding vanilla = KeyBindingAccessor.getKeyToBindings().get(key);
        for (KeyBinding binding : bindings) {
            if (binding == vanilla) {
                continue;
            }
            binding.setPressed(pressed);
        }
    }

    @Inject(method = "updateKeysByCode", at = @At("RETURN"))
    private static void updateKeysByCodeMultiBindings(CallbackInfo _info) {
        KEY_TO_MULTI_BINDINGS.clear();

        for (KeyBinding binding : KeyBindingAccessor.getKeysById().values()) {
            InputUtil.Key boundKey = ((KeyBindingAccessor) binding).getBoundKey();
            KEY_TO_MULTI_BINDINGS.putIfAbsent(boundKey, new ArrayList<>());
            KEY_TO_MULTI_BINDINGS.get(boundKey).add(binding);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At("RETURN"))
    private void init(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo _info) {
        KeyBinding self = (KeyBinding) (Object) this;
        InputUtil.Key boundKey = ((KeyBindingAccessor) self).getBoundKey();
        KEY_TO_MULTI_BINDINGS.putIfAbsent(boundKey, new ArrayList<>());
        KEY_TO_MULTI_BINDINGS.get(boundKey).add(self);
    }

    @Inject(method = "updateKeysByCode", at = @At("TAIL"))
    private static void generateDebugRebinds(CallbackInfo _info) {
        RebindAllTheKeys.updateDebugKeybinds();
    }
}
