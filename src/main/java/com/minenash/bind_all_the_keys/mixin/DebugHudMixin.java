package com.minenash.bind_all_the_keys.mixin;

import com.minenash.bind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public class DebugHudMixin {

    @ModifyConstant(method = "renderLeftText", constant = @Constant(stringValue = "For help: press F3 + Q"))
    public String forHelp(String _in) {
        return "For help press: " + RebindAllTheKeys.getDebugKeybindString(RebindAllTheKeys.SHOW_DEBUG_BINDINGS);
    }

    @Redirect(method = "renderLeftText", at = @At(value = "INVOKE", ordinal = 1, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public boolean profilerAndTpsModifiers(List list, Object _old) {
        list.add("Open Screen w/ Profiler: " + RebindAllTheKeys.getDebugKeybindString(RebindAllTheKeys.PROFILER));
        list.add("Open Screen w/ Tps: " + RebindAllTheKeys.getDebugKeybindString(RebindAllTheKeys.TPS));
        return true;
    }
}
