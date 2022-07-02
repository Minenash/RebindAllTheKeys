package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @ModifyConstant(method = "keyPressed", constant = @Constant(intValue = GLFW.GLFW_KEY_F5 /*294*/))
    public int remapDebugKey(int _key) {
        return RebindAllTheKeys.getKeyCode( RebindAllTheKeys.REFRESH_SERVER_LIST );
    }

}
