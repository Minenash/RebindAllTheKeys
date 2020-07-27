package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public class DebugHudMixin {

    @ModifyConstant(method = "renderLeftText", constant = @Constant(stringValue = "For help: press F3 + Q"))
    public String forHelp(String _in) {
        return "For help press: " + RebindAllTheKeys.getDebugKeybindString(RebindAllTheKeys.SHOW_DEBUG_BINDINGS);
    }

}
