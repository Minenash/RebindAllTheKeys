package com.minenash.rebind_all_the_keys.mixin;

import com.minenash.rebind_all_the_keys.RebindAllTheKeys;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.*;

@Environment(EnvType.CLIENT)
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

	@Shadow @Final private MinecraftClient client;

	@Shadow protected abstract void debugLog(String string, Object... objects);

	@ModifyArg(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;processF3(I)Z"))
	public int remapDebugKeys(int key) {
		return DEBUG_REBINDS.getOrDefault(key,0);
	}

	@ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_F3 /*292*/))
	public int remapDebugKey(int _key) {
		return RebindAllTheKeys.getKeyCode(DEBUG_KEY);
	}

	@ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_F1 /*290*/))
	public int remapToggleHudKey(int _key) {
		return RebindAllTheKeys.getKeyCode(TOGGLE_HUD);
	}

	@ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_C /*67*/))
	public int remapIntentionalCrashKey(int _key) {
		return RebindAllTheKeys.getKeyCode(INTENTIONAL_CRASH);
	}

	int key;
	@Inject(method = "onKey", at = @At("HEAD"))
	public void getKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		this.key = key;
	}

	@ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_ESCAPE /*256*/))
	public int remapQuitKey(int _key) {
		if (key == 256)
			return key;
		return RebindAllTheKeys.getKeyCode(QUIT_ALIAS);
	}

	@ModifyConstant(method = "onKey", constant = @Constant(intValue = GLFW.GLFW_KEY_B /*66*/))
	public int remapToggleNarratorCrashKey(int key) {
		if (TOGGLE_NARRATOR_OVERRIDE.isUnbound())
			return key;
		return RebindAllTheKeys.getKeyCode(TOGGLE_NARRATOR_OVERRIDE);
	}

	@Redirect(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasControlDown()Z"))
	public boolean removeCtrlRequirementIfToggleNarratorOverrideIsUsed() {
		return Screen.hasControlDown() || !RebindAllTheKeys.TOGGLE_NARRATOR_OVERRIDE.isUnbound();
	}

	@Redirect(method = "pollDebugCrash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;debugLog(Ljava/lang/String;[Ljava/lang/Object;)V"))
	public void showActualIntentionalCrashKeybind(Keyboard keyboard, String key, Object[] objects) {

		this.client.inGameHud.getChatHud().addMessage((Text.literal(""))
				.append((Text.translatable("debug.prefix")).formatted(Formatting.YELLOW, Formatting.BOLD))
				.append(" ").append(I18n.translate(key).replace("F3 + C", RebindAllTheKeys.getDebugKeybindString(INTENTIONAL_CRASH))));
	}

	@Redirect(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasShiftDown()Z"))
	public boolean remapProfilerModifier() {
		return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), RebindAllTheKeys.getKeyCode(PROFILER));
	}

	@Redirect(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;hasAltDown()Z"))
	public boolean remapTpsModifier() {
		return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), RebindAllTheKeys.getKeyCode(RebindAllTheKeys.TPS));
	}

	@Inject(method = "processF3", at = @At("HEAD"), cancellable = true)
	public void showDebugKeybinds(int key, CallbackInfoReturnable<Boolean> info) {
		if (key != 81)
			return;
		this.debugLog("debug.help.message");

		ChatHud chatHud = this.client.inGameHud.getChatHud();
		chatHud.addMessage(changeBinding("debug.reload_chunks.help", "A", RELOAD_CHUNKS));
		chatHud.addMessage(changeBinding("debug.show_hitboxes.help", "B", SHOW_HITBOXES));
		chatHud.addMessage(changeBinding("debug.clear_chat.help", "D", CLEAR_CHAT));
		chatHud.addMessage(Text.literal(getDebugKeybindString(CYCLE_RENDER_DISTANCE) + " = Cycle render distance, hold shift to cycle downwards"));
		chatHud.addMessage(changeBinding("debug.chunk_boundaries.help", "G", SHOW_CHUNK_BOUNDARIES));
		chatHud.addMessage(changeBinding("debug.advanced_tooltips.help", "H", ADVANCE_TOOLTIPS));
		chatHud.addMessage(changeBinding("debug.inspect.help", "I", COPY_DATA_TO_CLIPBOARD));
		chatHud.addMessage(changeBinding("debug.profiling.help", "L", START_STOP_PROFILING));
		chatHud.addMessage(changeBinding("debug.creative_spectator.help", "N", SWAP_GAMEMODE));
		chatHud.addMessage(changeBinding("debug.pause_focus.help", "P", PAUSE_ON_LOST_FOCUS));
		chatHud.addMessage(changeBinding("debug.help.help", "Q", SHOW_DEBUG_BINDINGS));
		chatHud.addMessage(changeBinding("debug.reload_resourcepacks.help", "T", RELOAD_RESOURCES));
		chatHud.addMessage(changeBinding("debug.gamemodes.help", "F4", GAMEMODE_SWITCHER));

		chatHud.addMessage(Text.literal(I18n.translate("debug.pause.help").replace("F3", RebindAllTheKeys.getDebugKeybindString(null))));

		String msg = I18n.translate("debug.copy_location.help");
		chatHud.addMessage(Text.literal(msg
				.substring(0, msg.indexOf(','))
				.replaceFirst("F3 \\+ C", RebindAllTheKeys.getDebugKeybindString(COPY_LOCATION))
		));
		chatHud.addMessage(Text.literal(msg
				.substring(msg.indexOf("F3", msg.indexOf(',')))
				.replaceFirst("F3 \\+ C", RebindAllTheKeys.getDebugKeybindString(INTENTIONAL_CRASH) + " (held) =")
		));

		info.setReturnValue(true);
		info.cancel();
	}

	private Text changeBinding(String key, String old, KeyBinding binding) {
		return Text.literal(I18n.translate(key).replace("F3 + " + old, RebindAllTheKeys.getDebugKeybindString(binding)));
	}

}
