package com.minenash.rebind_all_the_keys.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
@Mixin(InputUtil.Key.class)
public class InputUtilKeyMixin {

    @Shadow @Final private InputUtil.Type type;

    @Redirect(method = "method_27444", at = @At(value = "INVOKE", target = "Ljava/util/function/BiFunction;apply(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static Object translateNegativeKeys(BiFunction<Integer, String, Text> textTranslator, Object code_in, Object translationKey, InputUtil.Type type) {
        int code = (int)code_in == -1 ? -1 : Math.abs((int)code_in);
        InputUtil.Key key = ((InputUtilTypeAccessor)(Object)type).getMap().get(code);

        return textTranslator.apply(code, key == null ? (String)translationKey : key.getTranslationKey());
    }

}
