package com.minenash.rebind_all_the_keys;

import com.minenash.rebind_all_the_keys.mixin.GameOptionsAccessor;
import com.minenash.rebind_all_the_keys.mixin.GameOptionsMixin;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class RebindAllTheKeys implements ClientModInitializer {

	public static boolean isAmecsInstalled = false;
	public static final SimpleOption<Boolean> macCommandToControl = SimpleOption.ofBoolean("rebind_all_the_keys.controls.cmdToCtrl", false);
	public static final SimpleOption<Boolean> doubleTapSprint = SimpleOption.ofBoolean("rebind_all_the_keys.controls.doubleTapSprint", false);

	public static final Map<Integer, Integer> DEBUG_REBINDS = new HashMap<>();

	public static final KeyBinding DEBUG_KEY = debugKeybind("debug_key", GLFW.GLFW_KEY_F3);
	public static final KeyBinding RELOAD_CHUNKS = debugKeybind("reload_chunks", -GLFW.GLFW_KEY_A);
	public static final KeyBinding SHOW_HITBOXES = debugKeybind("show_hitboxes", -GLFW.GLFW_KEY_B);
	public static final KeyBinding COPY_LOCATION = debugKeybind("copy_location", -GLFW.GLFW_KEY_C);
	public static final KeyBinding CLEAR_CHAT = debugKeybind("clear_chat", -GLFW.GLFW_KEY_D);
	public static final KeyBinding CYCLE_RENDER_DISTANCE = debugKeybind("cycle_render_distance", -GLFW.GLFW_KEY_F);
	public static final KeyBinding SHOW_CHUNK_BOUNDARIES = debugKeybind("show_chunk_boundaries", -GLFW.GLFW_KEY_G);
	public static final KeyBinding ADVANCE_TOOLTIPS = debugKeybind("advance_tooltips", -GLFW.GLFW_KEY_H);
	public static final KeyBinding COPY_DATA_TO_CLIPBOARD = debugKeybind("copy_data_to_clipboard", -GLFW.GLFW_KEY_I);
	public static final KeyBinding SWAP_GAMEMODE = debugKeybind("swap_gamemode", -GLFW.GLFW_KEY_N);
	public static final KeyBinding PAUSE_ON_LOST_FOCUS = debugKeybind("pause_on_lost_focus", -GLFW.GLFW_KEY_P);
	public static final KeyBinding SHOW_DEBUG_BINDINGS = debugKeybind("show_debug_bindings", -GLFW.GLFW_KEY_Q);
	public static final KeyBinding RELOAD_RESOURCES = debugKeybind("reload_resources", -GLFW.GLFW_KEY_T);
	public static final KeyBinding GAMEMODE_SWITCHER = debugKeybind("gamemode_switcher", -GLFW.GLFW_KEY_F4);
	public static final KeyBinding INTENTIONAL_CRASH = debugKeybind("intentional_crash", -GLFW.GLFW_KEY_C);

	public static final KeyBinding PROFILER = debugKeybind("profiler", -GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final KeyBinding TPS = debugKeybind("tps", -GLFW.GLFW_KEY_LEFT_ALT);

	public static final KeyBinding QUIT_ALIAS = miscKeybind("quit", GLFW.GLFW_KEY_UNKNOWN);
	public static final KeyBinding TOGGLE_HUD = miscKeybind("toggle_hud", GLFW.GLFW_KEY_F1);
	public static final KeyBinding TOGGLE_NARRATOR_OVERRIDE = miscKeybind("toggle_narrator_override", GLFW.GLFW_KEY_UNKNOWN);
	public static final KeyBinding TOGGLE_AUTO_JUMP = miscKeybind("toggle_auto_jump", GLFW.GLFW_KEY_UNKNOWN);
	public static final KeyBinding REFRESH_SERVER_LIST = miscKeybind("refresh_server_list", GLFW.GLFW_KEY_F5);

	public static final KeyBinding HOTBAR_NEXT_OVERRIDE = keybind("hotbar_next_override", GLFW.GLFW_KEY_UNKNOWN, KeyBinding.INVENTORY_CATEGORY);
	public static final KeyBinding HOTBAR_PREVIOUS_OVERRIDE = keybind("hotbar_previous_override", GLFW.GLFW_KEY_UNKNOWN, KeyBinding.INVENTORY_CATEGORY);
	public static final KeyBinding QUICK_MOVE = keybind("quick_move", GLFW.GLFW_KEY_LEFT_SHIFT, KeyBinding.INVENTORY_CATEGORY);

	public static Text gamemodeSwitcherSelectText = null;

	private static KeyBinding debugKeybind(String key, int defaultKey) {
		return keybind(key, defaultKey, "rebind_all_the_keys.keybind_group.debug");
	}

	private static KeyBinding miscKeybind(String key, int defaultKey) {
		return keybind(key, defaultKey, KeyBinding.MISC_CATEGORY);
	}

	private static KeyBinding keybind(String key, int defaultKey, String category) {
		KeyBinding binding = new KeyBinding("rebind_all_the_keys.keybind." + key, InputUtil.Type.KEYSYM, defaultKey, category);
		KeyBindingHelper.registerKeyBinding(binding);
		return binding;
	}

	@Override
	public void onInitializeClient() {
		isAmecsInstalled = FabricLoader.getInstance().isModLoaded("amecs");

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (TOGGLE_AUTO_JUMP.wasPressed()) {
				boolean value = !client.options.getAutoJump().getValue();
				client.options.getAutoJump().setValue(value);
				client.player.sendMessage(Text.translatable("rebind_all_the_keys.keybind.toggle_auto_jump.msg." + value), true);
			}

			while (HOTBAR_NEXT_OVERRIDE.wasPressed())
				client.player.getInventory().scrollInHotbar(-1);

			while (HOTBAR_PREVIOUS_OVERRIDE.wasPressed())
				client.player.getInventory().scrollInHotbar(1);

			if (isKeybindPressed(DEBUG_KEY) && isKeybindPressed(CYCLE_RENDER_DISTANCE)) {
				SimpleOption<Integer> option = ((GameOptionsAccessor) client.options).getViewDistance();
				int newValue = option.getValue() + (Screen.hasShiftDown() ? -1 : 1);
				int max = client.is64Bit() && Runtime.getRuntime().maxMemory() >= 1000000000L ? 32 : 16;
				if (newValue < 2) newValue = 2;
				if (newValue > max) newValue = max;

				client.player.sendMessage(Text.literal("§e§l[Debug]:§f Set Render Distance to " + newValue));
				option.setValue(newValue);
			}

		});

	}

	public static void updateDebugKeybinds() {
		DEBUG_REBINDS.clear();
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(RELOAD_CHUNKS).getCode(), GLFW.GLFW_KEY_A);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(SHOW_HITBOXES).getCode(), GLFW.GLFW_KEY_B);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(COPY_LOCATION).getCode(), GLFW.GLFW_KEY_C);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(CLEAR_CHAT).getCode(), GLFW.GLFW_KEY_D);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(CYCLE_RENDER_DISTANCE).getCode(), GLFW.GLFW_KEY_F);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(SHOW_CHUNK_BOUNDARIES).getCode(), GLFW.GLFW_KEY_G);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(ADVANCE_TOOLTIPS).getCode(), GLFW.GLFW_KEY_H);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(COPY_DATA_TO_CLIPBOARD).getCode(), GLFW.GLFW_KEY_I);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(SWAP_GAMEMODE).getCode(), GLFW.GLFW_KEY_N);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(PAUSE_ON_LOST_FOCUS).getCode(), GLFW.GLFW_KEY_P);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(SHOW_DEBUG_BINDINGS).getCode(), GLFW.GLFW_KEY_Q);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(RELOAD_RESOURCES).getCode(), GLFW.GLFW_KEY_T);
		DEBUG_REBINDS.put(-KeyBindingHelper.getBoundKeyOf(GAMEMODE_SWITCHER).getCode(), GLFW.GLFW_KEY_F4);

		if (gamemodeSwitcherSelectText != null)
			updateGamemodeSwitcherSelectText();

	}

	public static void updateGamemodeSwitcherSelectText() {
		gamemodeSwitcherSelectText = Text.translatable("debug.gamemodes.select_next",
				Text.literal("[").formatted(Formatting.AQUA).append(RebindAllTheKeys.GAMEMODE_SWITCHER.getBoundKeyLocalizedText()).append("]"));
	}

	public static String getDebugKeybindString(KeyBinding key) {
		String debugString = DEBUG_KEY.getBoundKeyLocalizedText().getString();

		if (debugString.length() == 1)
			debugString = debugString.toUpperCase();

		if (key == null)
			return debugString;

		String keyString = key.getBoundKeyLocalizedText().getString();
		return debugString + " + " + (keyString.length() == 1 ? keyString.toUpperCase() : keyString);
	}

	public static int getKeyCode(KeyBinding key) {
		return Math.abs(KeyBindingHelper.getBoundKeyOf(key).getCode());
	}

	public static boolean isKeybindPressed(KeyBinding key) {
		return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), getKeyCode(key));
	}
}
