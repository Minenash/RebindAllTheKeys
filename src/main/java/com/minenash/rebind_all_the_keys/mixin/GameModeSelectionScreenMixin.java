package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(GameModeSelectionScreen.class)
public class GameModeSelectionScreenMixin {



    @ModifyConstant(method = "checkForClose", constant = @Constant(intValue = GLFW.GLFW_KEY_F3 /*292*/))
    public int remapDebugKey(int _key) {
        return RebindAllTheKeys.getKeyCode(RebindAllTheKeys.DEBUG_KEY);
    }

    @ModifyConstant(method = "keyPressed", constant = @Constant(intValue = GLFW.GLFW_KEY_F4 /*293*/))
    public int remapGamemodeSwitcherKey(int _key) {
        return RebindAllTheKeys.getKeyCode(RebindAllTheKeys.GAMEMODE_SWITCHER);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V"))
    public Text getGamemodeSwitcherKeyString(Text _in) {
        if (RebindAllTheKeys.gamemodeSwitcherSelectText == null)
            RebindAllTheKeys.updateGamemodeSwitcherSelectText();
        return RebindAllTheKeys.gamemodeSwitcherSelectText;
    }

}
