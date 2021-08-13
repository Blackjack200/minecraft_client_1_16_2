package net.minecraft.client;

import com.mojang.blaze3d.platform.VideoMode;
import java.util.Optional;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.Window;

public class FullscreenResolutionProgressOption extends ProgressOption {
    public FullscreenResolutionProgressOption(final Window dew) {
        this(dew, dew.findBestMonitor());
    }
    
    private FullscreenResolutionProgressOption(final Window dew, @Nullable final Monitor deo) {
        super("options.fullscreen.resolution", -1.0, (deo != null) ? ((double)(deo.getModeCount() - 1)) : -1.0, 1.0f, (Function<Options, Double>)(dka -> {
            if (deo == null) {
                return -1.0;
            }
            final Optional<VideoMode> optional4 = dew.getPreferredFullscreenVideoMode();
            return (Double)optional4.map(dev -> deo.getVideoModeIndex(dev)).orElse((-1.0));
        }), (BiConsumer<Options, Double>)((dka, double4) -> {
            if (deo == null) {
                return;
            }
            if (double4 == -1.0) {
                dew.setPreferredFullscreenVideoMode((Optional<VideoMode>)Optional.empty());
            }
            else {
                dew.setPreferredFullscreenVideoMode((Optional<VideoMode>)Optional.of(deo.getMode(double4.intValue())));
            }
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            if (deo == null) {
                return new TranslatableComponent("options.fullscreen.unavailable");
            }
            final double double4 = dkc.get(dka);
            if (double4 == -1.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.fullscreen.current"));
            }
            return dkc.genericValueLabel(new TextComponent(deo.getMode((int)double4).toString()));
        }));
    }
}
