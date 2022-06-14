package com.minenash.rebind_all_the_keys;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class RebindAllTheKeys implements ClientModInitializer {

	public static boolean isAmecsInstalled = false;

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

	//public static final KeyBinding PROFILER = debugKeybind("profiler", GLFW.GLFW_KEY_LEFT_SHIFT);
	//public static final KeyBinding TPS = debugKeybind("tps", GLFW.GLFW_KEY_LEFT_ALT);

	public static final KeyBinding TOGGLE_HUD = miscKeybind("toggle_hud", GLFW.GLFW_KEY_F1);
	public static final KeyBinding TOGGLE_NARRATOR_OVERRIDE = miscKeybind("toggle_narrator_override", GLFW.GLFW_KEY_UNKNOWN);
	public static final KeyBinding TOGGLE_AUTO_JUMP = miscKeybind("toggle_auto_jump", GLFW.GLFW_KEY_UNKNOWN);

	public static Text gamemodeSwitcherSelectText = null;

	private static KeyBinding debugKeybind(String key, int defaultKey) {
		return new KeyBinding("rebind_all_the_keys.keybind." + key, InputUtil.Type.KEYSYM, defaultKey, "rebind_all_the_keys.keybind_group.debug");
	}

	private static KeyBinding miscKeybind(String key, int defaultKey) {
		return new KeyBinding("rebind_all_the_keys.keybind." + key, InputUtil.Type.KEYSYM, defaultKey, "key.categories.misc");
	}


	@Override
	public void onInitializeClient() {
		isAmecsInstalled = FabricLoader.getInstance().isModLoaded("amecs");

		KeyBindingHelper.registerKeyBinding(DEBUG_KEY);
		KeyBindingHelper.registerKeyBinding(RELOAD_CHUNKS);
		KeyBindingHelper.registerKeyBinding(SHOW_HITBOXES);
		KeyBindingHelper.registerKeyBinding(COPY_LOCATION);
		KeyBindingHelper.registerKeyBinding(CLEAR_CHAT);
		KeyBindingHelper.registerKeyBinding(CYCLE_RENDER_DISTANCE);
		KeyBindingHelper.registerKeyBinding(SHOW_CHUNK_BOUNDARIES);
		KeyBindingHelper.registerKeyBinding(ADVANCE_TOOLTIPS);
		KeyBindingHelper.registerKeyBinding(COPY_DATA_TO_CLIPBOARD);
		KeyBindingHelper.registerKeyBinding(SWAP_GAMEMODE);
		KeyBindingHelper.registerKeyBinding(PAUSE_ON_LOST_FOCUS);
		KeyBindingHelper.registerKeyBinding(SHOW_DEBUG_BINDINGS);
		KeyBindingHelper.registerKeyBinding(RELOAD_RESOURCES);
		KeyBindingHelper.registerKeyBinding(GAMEMODE_SWITCHER);

		//KeyBindingHelper.registerKeyBinding(PROFILER);
		//KeyBindingHelper.registerKeyBinding(TPS);

		KeyBindingHelper.registerKeyBinding(INTENTIONAL_CRASH);
		KeyBindingHelper.registerKeyBinding(TOGGLE_NARRATOR_OVERRIDE);
		KeyBindingHelper.registerKeyBinding(TOGGLE_HUD);
		KeyBindingHelper.registerKeyBinding(TOGGLE_AUTO_JUMP);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (TOGGLE_AUTO_JUMP.wasPressed()) {
				boolean value = !client.options.getAutoJump().getValue();
				client.options.getAutoJump().setValue(value);
				client.player.sendMessage(Text.translatable("rebind_all_the_keys.keybind.toggle_auto_jump.msg." + value), true);
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
}
