package com.minenash.rebind_all_the_keys.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(InputUtil.Type.class)
public interface InputUtilTypeAccessor {

    @Accessor
    Int2ObjectMap<InputUtil.Key> getMap();
}
