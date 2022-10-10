package com.minenash.rebind_all_the_keys.mixin;

import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

    @Accessor("KEYS_BY_ID")
    public static Map<String, KeyBinding> getKeysById() {
        throw new AssertionError();
    }

    @Accessor("KEY_TO_BINDINGS")
    public static Map<InputUtil.Key, KeyBinding> getKeyToBindings() {
        throw new AssertionError();
    }

    @Accessor
    int getTimesPressed();

    @Accessor
    void setTimesPressed(int timesPressed);

    @Accessor
    InputUtil.Key getBoundKey();
}
