package com.minenash.rebind_all_the_keys.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static com.minenash.rebind_all_the_keys.RebindAllTheKeys.*;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow private boolean renderingChartVisible;
    @Shadow private boolean renderingAndTickChartsVisible;
    @Shadow private boolean packetSizeAndPingChartsVisible;

    @ModifyConstant(method = "drawLeftText", constant = @Constant(stringValue = "For help: press F3 + Q"))
    public String forHelp(String _in) {
        return "For help press: " + getDebugKeybindString(SHOW_DEBUG_BINDINGS);
    }

    @Redirect(method = "drawLeftText", at = @At(value = "INVOKE", ordinal = 1, target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public boolean debugCharts(List<Object> list, Object e) {
        return list.add("Debug charts: [" + getDebugKeybindString(CHART_PIE) + "] Profiler "
                + (renderingChartVisible ? "visible" : "hidden")
                + "; [" +  getDebugKeybindString(CHART_FPS_TPS) + "] "
                + (client.getServer() != null ? "FPS + TPS " : "FPS ") + (renderingAndTickChartsVisible ? "visible" : "hidden")
                + "; [" + getDebugKeybindString(CHART_BANDWIDTH_PING) + "] "
                + (!this.client.isInSingleplayer() ? "Bandwidth + Ping" : "Ping")
                + (packetSizeAndPingChartsVisible ? " visible" : " hidden"));
    }

}
