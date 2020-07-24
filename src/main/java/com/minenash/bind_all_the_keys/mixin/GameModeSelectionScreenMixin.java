package com.minenash.bind_all_the_keys.mixin;

import com.minenash.bind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Environment(EnvType.CLIENT)
@Mixin(GameModeSelectionScreen.class)
public class GameModeSelectionScreenMixin {



    @ModifyConstant(method = "checkForClose", constant = @Constant(intValue = GLFW.GLFW_KEY_F3 /*292*/))
    public int remapDebugKey(int _key) {
        return KeyBindingHelper.getBoundKeyOf(RebindAllTheKeys.DEBUG_KEY).getCode();
    }

    @ModifyConstant(method = "keyPressed", constant = @Constant(intValue = GLFW.GLFW_KEY_F4 /*293*/))
    public int remapGamemodeSwitcherKey(int _key) {
        return KeyBindingHelper.getBoundKeyOf(RebindAllTheKeys.GAMEMODE_SWITCHER).getCode();
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/GameModeSelectionScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/StringRenderable;III)V"))
    public StringRenderable getGamemodeSwitcherKeyString(StringRenderable _in) {

        if (RebindAllTheKeys.gamemodeSwitcherSelectText == null)
            RebindAllTheKeys.updateGamemodeSwitcherSelectText();
        return RebindAllTheKeys.gamemodeSwitcherSelectText;
    }

}
