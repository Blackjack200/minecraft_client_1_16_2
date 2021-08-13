package net.minecraft.client;

import java.util.function.Predicate;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.CommonComponents;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.client.renderer.GpuWarnlistManager;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.chat.NarratorChatListener;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;

public abstract class Option {
    public static final ProgressOption BIOME_BLEND_RADIUS;
    public static final ProgressOption CHAT_HEIGHT_FOCUSED;
    public static final ProgressOption CHAT_HEIGHT_UNFOCUSED;
    public static final ProgressOption CHAT_OPACITY;
    public static final ProgressOption CHAT_SCALE;
    public static final ProgressOption CHAT_WIDTH;
    public static final ProgressOption CHAT_LINE_SPACING;
    public static final ProgressOption CHAT_DELAY;
    public static final ProgressOption FOV;
    private static final Component ACCESSIBILITY_TOOLTIP_FOV_EFFECT;
    public static final ProgressOption FOV_EFFECTS_SCALE;
    private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT;
    public static final ProgressOption SCREEN_EFFECTS_SCALE;
    public static final ProgressOption FRAMERATE_LIMIT;
    public static final ProgressOption GAMMA;
    public static final ProgressOption MIPMAP_LEVELS;
    public static final ProgressOption MOUSE_WHEEL_SENSITIVITY;
    public static final BooleanOption RAW_MOUSE_INPUT;
    public static final ProgressOption RENDER_DISTANCE;
    public static final ProgressOption ENTITY_DISTANCE_SCALING;
    public static final ProgressOption SENSITIVITY;
    public static final ProgressOption TEXT_BACKGROUND_OPACITY;
    public static final CycleOption AMBIENT_OCCLUSION;
    public static final CycleOption ATTACK_INDICATOR;
    public static final CycleOption CHAT_VISIBILITY;
    private static final Component GRAPHICS_TOOLTIP_FAST;
    private static final Component GRAPHICS_TOOLTIP_FABULOUS;
    private static final Component GRAPHICS_TOOLTIP_FANCY;
    public static final CycleOption GRAPHICS;
    public static final CycleOption GUI_SCALE;
    public static final CycleOption MAIN_HAND;
    public static final CycleOption NARRATOR;
    public static final CycleOption PARTICLES;
    public static final CycleOption RENDER_CLOUDS;
    public static final CycleOption TEXT_BACKGROUND;
    public static final BooleanOption AUTO_JUMP;
    public static final BooleanOption AUTO_SUGGESTIONS;
    public static final BooleanOption CHAT_COLOR;
    public static final BooleanOption CHAT_LINKS;
    public static final BooleanOption CHAT_LINKS_PROMPT;
    public static final BooleanOption DISCRETE_MOUSE_SCROLL;
    public static final BooleanOption ENABLE_VSYNC;
    public static final BooleanOption ENTITY_SHADOWS;
    public static final BooleanOption FORCE_UNICODE_FONT;
    public static final BooleanOption INVERT_MOUSE;
    public static final BooleanOption REALMS_NOTIFICATIONS;
    public static final BooleanOption REDUCED_DEBUG_INFO;
    public static final BooleanOption SHOW_SUBTITLES;
    public static final BooleanOption SNOOPER_ENABLED;
    public static final CycleOption TOGGLE_CROUCH;
    public static final CycleOption TOGGLE_SPRINT;
    public static final BooleanOption TOUCHSCREEN;
    public static final BooleanOption USE_FULLSCREEN;
    public static final BooleanOption VIEW_BOBBING;
    private final Component caption;
    private Optional<List<FormattedCharSequence>> toolTip;
    
    public Option(final String string) {
        this.toolTip = (Optional<List<FormattedCharSequence>>)Optional.empty();
        this.caption = new TranslatableComponent(string);
    }
    
    public abstract AbstractWidget createButton(final Options dka, final int integer2, final int integer3, final int integer4);
    
    protected Component getCaption() {
        return this.caption;
    }
    
    public void setTooltip(final List<FormattedCharSequence> list) {
        this.toolTip = (Optional<List<FormattedCharSequence>>)Optional.of(list);
    }
    
    public Optional<List<FormattedCharSequence>> getTooltip() {
        return this.toolTip;
    }
    
    protected Component pixelValueLabel(final int integer) {
        return new TranslatableComponent("options.pixel_value", new Object[] { this.getCaption(), integer });
    }
    
    protected Component percentValueLabel(final double double1) {
        return new TranslatableComponent("options.percent_value", new Object[] { this.getCaption(), (int)(double1 * 100.0) });
    }
    
    protected Component percentAddValueLabel(final int integer) {
        return new TranslatableComponent("options.percent_add_value", new Object[] { this.getCaption(), integer });
    }
    
    protected Component genericValueLabel(final Component nr) {
        return new TranslatableComponent("options.generic_value", new Object[] { this.getCaption(), nr });
    }
    
    protected Component genericValueLabel(final int integer) {
        return this.genericValueLabel(new TextComponent(Integer.toString(integer)));
    }
    
    static {
        BIOME_BLEND_RADIUS = new ProgressOption("options.biomeBlendRadius", 0.0, 7.0, 1.0f, (Function<Options, Double>)(dka -> dka.biomeBlendRadius), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.biomeBlendRadius = Mth.clamp((int)(double)double2, 0, 7);
            Minecraft.getInstance().levelRenderer.allChanged();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            final int integer5 = (int)double3 * 2 + 1;
            return dkc.genericValueLabel(new TranslatableComponent(new StringBuilder().append("options.biomeBlendRadius.").append(integer5).toString()));
        }));
        CHAT_HEIGHT_FOCUSED = new ProgressOption("options.chat.height.focused", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatHeightFocused), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.chatHeightFocused = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            return dkc.pixelValueLabel(ChatComponent.getHeight(double3));
        }));
        CHAT_HEIGHT_UNFOCUSED = new ProgressOption("options.chat.height.unfocused", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatHeightUnfocused), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.chatHeightUnfocused = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            return dkc.pixelValueLabel(ChatComponent.getHeight(double3));
        }));
        CHAT_OPACITY = new ProgressOption("options.chat.opacity", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatOpacity), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.chatOpacity = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            return dkc.percentValueLabel(double3 * 0.9 + 0.1);
        }));
        CHAT_SCALE = new ProgressOption("options.chat.scale", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatScale), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.chatScale = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            if (double3 == 0.0) {
                return CommonComponents.optionStatus(dkc.getCaption(), false);
            }
            return dkc.percentValueLabel(double3);
        }));
        CHAT_WIDTH = new ProgressOption("options.chat.width", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatWidth), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.chatWidth = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            return dkc.pixelValueLabel(ChatComponent.getWidth(double3));
        }));
        CHAT_LINE_SPACING = new ProgressOption("options.chat.line_spacing", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.chatLineSpacing), (BiConsumer<Options, Double>)((dka, double2) -> dka.chatLineSpacing = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> dkc.percentValueLabel(dkc.toPct(dkc.get(dka)))));
        CHAT_DELAY = new ProgressOption("options.chat.delay_instant", 0.0, 6.0, 0.1f, (Function<Options, Double>)(dka -> dka.chatDelay), (BiConsumer<Options, Double>)((dka, double2) -> dka.chatDelay = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            if (double3 <= 0.0) {
                return new TranslatableComponent("options.chat.delay_none");
            }
            return new TranslatableComponent("options.chat.delay", new Object[] { String.format("%.1f", new Object[] { double3 }) });
        }));
        FOV = new ProgressOption("options.fov", 30.0, 110.0, 1.0f, (Function<Options, Double>)(dka -> dka.fov), (BiConsumer<Options, Double>)((dka, double2) -> dka.fov = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            if (double3 == 70.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.fov.min"));
            }
            if (double3 == dkc.getMaxValue()) {
                return dkc.genericValueLabel(new TranslatableComponent("options.fov.max"));
            }
            return dkc.genericValueLabel((int)double3);
        }));
        ACCESSIBILITY_TOOLTIP_FOV_EFFECT = new TranslatableComponent("options.fovEffectScale.tooltip");
        FOV_EFFECTS_SCALE = new ProgressOption("options.fovEffectScale", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> Math.pow((double)dka.fovEffectScale, 2.0)), (BiConsumer<Options, Double>)((dka, double2) -> dka.fovEffectScale = Mth.sqrt(double2)), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            dkc.setTooltip(Minecraft.getInstance().font.split(Option.ACCESSIBILITY_TOOLTIP_FOV_EFFECT, 200));
            final double double3 = dkc.toPct(dkc.get(dka));
            if (double3 == 0.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.fovEffectScale.off"));
            }
            return dkc.percentValueLabel(double3);
        }));
        ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT = new TranslatableComponent("options.screenEffectScale.tooltip");
        SCREEN_EFFECTS_SCALE = new ProgressOption("options.screenEffectScale", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.screenEffectScale), (BiConsumer<Options, Double>)((dka, double2) -> dka.screenEffectScale = double2.floatValue()), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            dkc.setTooltip(Minecraft.getInstance().font.split(Option.ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT, 200));
            final double double3 = dkc.toPct(dkc.get(dka));
            if (double3 == 0.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.screenEffectScale.off"));
            }
            return dkc.percentValueLabel(double3);
        }));
        FRAMERATE_LIMIT = new ProgressOption("options.framerateLimit", 10.0, 260.0, 10.0f, (Function<Options, Double>)(dka -> dka.framerateLimit), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.framerateLimit = (int)(double)double2;
            Minecraft.getInstance().getWindow().setFramerateLimit(dka.framerateLimit);
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            if (double3 == dkc.getMaxValue()) {
                return dkc.genericValueLabel(new TranslatableComponent("options.framerateLimit.max"));
            }
            return dkc.genericValueLabel(new TranslatableComponent("options.framerate", new Object[] { (int)double3 }));
        }));
        GAMMA = new ProgressOption("options.gamma", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.gamma), (BiConsumer<Options, Double>)((dka, double2) -> dka.gamma = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            if (double3 == 0.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.gamma.min"));
            }
            if (double3 == 1.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.gamma.max"));
            }
            return dkc.percentAddValueLabel((int)(double3 * 100.0));
        }));
        MIPMAP_LEVELS = new ProgressOption("options.mipmapLevels", 0.0, 4.0, 1.0f, (Function<Options, Double>)(dka -> dka.mipmapLevels), (BiConsumer<Options, Double>)((dka, double2) -> dka.mipmapLevels = (int)(double)double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            if (double3 == 0.0) {
                return CommonComponents.optionStatus(dkc.getCaption(), false);
            }
            return dkc.genericValueLabel((int)double3);
        }));
        MOUSE_WHEEL_SENSITIVITY = new LogaritmicProgressOption("options.mouseWheelSensitivity", 0.01, 10.0, 0.01f, (Function<Options, Double>)(dka -> dka.mouseWheelSensitivity), (BiConsumer<Options, Double>)((dka, double2) -> dka.mouseWheelSensitivity = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            return dkc.genericValueLabel(new TextComponent(String.format("%.2f", new Object[] { dkc.toValue(double3) })));
        }));
        RAW_MOUSE_INPUT = new BooleanOption("options.rawMouseInput", (Predicate<Options>)(dka -> dka.rawMouseInput), (BiConsumer<Options, Boolean>)((dka, boolean2) -> {
            dka.rawMouseInput = boolean2;
            final Window dew3 = Minecraft.getInstance().getWindow();
            if (dew3 != null) {
                dew3.updateRawMouseInput(boolean2);
            }
        }));
        RENDER_DISTANCE = new ProgressOption("options.renderDistance", 2.0, 16.0, 1.0f, (Function<Options, Double>)(dka -> dka.renderDistance), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.renderDistance = (int)(double)double2;
            Minecraft.getInstance().levelRenderer.needsUpdate();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            return dkc.genericValueLabel(new TranslatableComponent("options.chunks", new Object[] { (int)double3 }));
        }));
        ENTITY_DISTANCE_SCALING = new ProgressOption("options.entityDistanceScaling", 0.5, 5.0, 0.25f, (Function<Options, Double>)(dka -> dka.entityDistanceScaling), (BiConsumer<Options, Double>)((dka, double2) -> dka.entityDistanceScaling = (float)(double)double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.get(dka);
            return dkc.percentValueLabel(double3);
        }));
        SENSITIVITY = new ProgressOption("options.sensitivity", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.sensitivity), (BiConsumer<Options, Double>)((dka, double2) -> dka.sensitivity = double2), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> {
            final double double3 = dkc.toPct(dkc.get(dka));
            if (double3 == 0.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.sensitivity.min"));
            }
            if (double3 == 1.0) {
                return dkc.genericValueLabel(new TranslatableComponent("options.sensitivity.max"));
            }
            return dkc.percentValueLabel(2.0 * double3);
        }));
        TEXT_BACKGROUND_OPACITY = new ProgressOption("options.accessibility.text_background_opacity", 0.0, 1.0, 0.0f, (Function<Options, Double>)(dka -> dka.textBackgroundOpacity), (BiConsumer<Options, Double>)((dka, double2) -> {
            dka.textBackgroundOpacity = double2;
            Minecraft.getInstance().gui.getChat().rescaleChat();
        }), (BiFunction<Options, ProgressOption, Component>)((dka, dkc) -> dkc.percentValueLabel(dkc.toPct(dkc.get(dka)))));
        AMBIENT_OCCLUSION = new CycleOption("options.ao", (BiConsumer<Options, Integer>)((dka, integer) -> {
            dka.ambientOcclusion = AmbientOcclusionStatus.byId(dka.ambientOcclusion.getId() + integer);
            Minecraft.getInstance().levelRenderer.allChanged();
        }), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.ambientOcclusion.getKey()))));
        ATTACK_INDICATOR = new CycleOption("options.attackIndicator", (BiConsumer<Options, Integer>)((dka, integer) -> dka.attackIndicator = AttackIndicatorStatus.byId(dka.attackIndicator.getId() + integer)), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.attackIndicator.getKey()))));
        CHAT_VISIBILITY = new CycleOption("options.chat.visibility", (BiConsumer<Options, Integer>)((dka, integer) -> dka.chatVisibility = ChatVisiblity.byId((dka.chatVisibility.getId() + integer) % 3)), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.chatVisibility.getKey()))));
        GRAPHICS_TOOLTIP_FAST = new TranslatableComponent("options.graphics.fast.tooltip");
        GRAPHICS_TOOLTIP_FABULOUS = new TranslatableComponent("options.graphics.fabulous.tooltip", new Object[] { new TranslatableComponent("options.graphics.fabulous").withStyle(ChatFormatting.ITALIC) });
        GRAPHICS_TOOLTIP_FANCY = new TranslatableComponent("options.graphics.fancy.tooltip");
        GRAPHICS = new CycleOption("options.graphics", (BiConsumer<Options, Integer>)((dka, integer) -> {
            final Minecraft djw3 = Minecraft.getInstance();
            final GpuWarnlistManager dzs4 = djw3.getGpuWarnlistManager();
            if (dka.graphicsMode == GraphicsStatus.FANCY && dzs4.willShowWarning()) {
                dzs4.showWarning();
                return;
            }
            dka.graphicsMode = dka.graphicsMode.cycleNext();
            if (dka.graphicsMode == GraphicsStatus.FABULOUS && (!GlStateManager.supportsFramebufferBlit() || dzs4.isSkippingFabulous())) {
                dka.graphicsMode = GraphicsStatus.FAST;
            }
            djw3.levelRenderer.allChanged();
        }), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> {
            switch (dka.graphicsMode) {
                case FAST: {
                    djm.setTooltip(Minecraft.getInstance().font.split(Option.GRAPHICS_TOOLTIP_FAST, 200));
                    break;
                }
                case FANCY: {
                    djm.setTooltip(Minecraft.getInstance().font.split(Option.GRAPHICS_TOOLTIP_FANCY, 200));
                    break;
                }
                case FABULOUS: {
                    djm.setTooltip(Minecraft.getInstance().font.split(Option.GRAPHICS_TOOLTIP_FABULOUS, 200));
                    break;
                }
            }
            final MutableComponent nx3 = new TranslatableComponent(dka.graphicsMode.getKey());
            if (dka.graphicsMode == GraphicsStatus.FABULOUS) {
                return djm.genericValueLabel(nx3.withStyle(ChatFormatting.ITALIC));
            }
            return djm.genericValueLabel(nx3);
        }));
        GUI_SCALE = new CycleOption("options.guiScale", (BiConsumer<Options, Integer>)((dka, integer) -> dka.guiScale = Integer.remainderUnsigned(dka.guiScale + integer, Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode()) + 1)), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> {
            if (dka.guiScale == 0) {
                return djm.genericValueLabel(new TranslatableComponent("options.guiScale.auto"));
            }
            return djm.genericValueLabel(dka.guiScale);
        }));
        MAIN_HAND = new CycleOption("options.mainHand", (BiConsumer<Options, Integer>)((dka, integer) -> dka.mainHand = dka.mainHand.getOpposite()), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(dka.mainHand.getName())));
        NARRATOR = new CycleOption("options.narrator", (BiConsumer<Options, Integer>)((dka, integer) -> {
            if (NarratorChatListener.INSTANCE.isActive()) {
                dka.narratorStatus = NarratorStatus.byId(dka.narratorStatus.getId() + integer);
            }
            else {
                dka.narratorStatus = NarratorStatus.OFF;
            }
            NarratorChatListener.INSTANCE.updateNarratorStatus(dka.narratorStatus);
        }), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> {
            if (NarratorChatListener.INSTANCE.isActive()) {
                return djm.genericValueLabel(dka.narratorStatus.getName());
            }
            return djm.genericValueLabel(new TranslatableComponent("options.narrator.notavailable"));
        }));
        PARTICLES = new CycleOption("options.particles", (BiConsumer<Options, Integer>)((dka, integer) -> dka.particles = ParticleStatus.byId(dka.particles.getId() + integer)), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.particles.getKey()))));
        RENDER_CLOUDS = new CycleOption("options.renderClouds", (BiConsumer<Options, Integer>)((dka, integer) -> {
            dka.renderClouds = CloudStatus.byId(dka.renderClouds.getId() + integer);
            if (Minecraft.useShaderTransparency()) {
                final RenderTarget ded3 = Minecraft.getInstance().levelRenderer.getCloudsTarget();
                if (ded3 != null) {
                    ded3.clear(Minecraft.ON_OSX);
                }
            }
        }), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.renderClouds.getKey()))));
        TEXT_BACKGROUND = new CycleOption("options.accessibility.text_background", (BiConsumer<Options, Integer>)((dka, integer) -> dka.backgroundForChatOnly = !dka.backgroundForChatOnly), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.backgroundForChatOnly ? "options.accessibility.text_background.chat" : "options.accessibility.text_background.everywhere"))));
        AUTO_JUMP = new BooleanOption("options.autoJump", (Predicate<Options>)(dka -> dka.autoJump), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.autoJump = boolean2));
        AUTO_SUGGESTIONS = new BooleanOption("options.autoSuggestCommands", (Predicate<Options>)(dka -> dka.autoSuggestions), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.autoSuggestions = boolean2));
        CHAT_COLOR = new BooleanOption("options.chat.color", (Predicate<Options>)(dka -> dka.chatColors), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.chatColors = boolean2));
        CHAT_LINKS = new BooleanOption("options.chat.links", (Predicate<Options>)(dka -> dka.chatLinks), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.chatLinks = boolean2));
        CHAT_LINKS_PROMPT = new BooleanOption("options.chat.links.prompt", (Predicate<Options>)(dka -> dka.chatLinksPrompt), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.chatLinksPrompt = boolean2));
        DISCRETE_MOUSE_SCROLL = new BooleanOption("options.discrete_mouse_scroll", (Predicate<Options>)(dka -> dka.discreteMouseScroll), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.discreteMouseScroll = boolean2));
        ENABLE_VSYNC = new BooleanOption("options.vsync", (Predicate<Options>)(dka -> dka.enableVsync), (BiConsumer<Options, Boolean>)((dka, boolean2) -> {
            dka.enableVsync = boolean2;
            if (Minecraft.getInstance().getWindow() != null) {
                Minecraft.getInstance().getWindow().updateVsync(dka.enableVsync);
            }
        }));
        ENTITY_SHADOWS = new BooleanOption("options.entityShadows", (Predicate<Options>)(dka -> dka.entityShadows), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.entityShadows = boolean2));
        FORCE_UNICODE_FONT = new BooleanOption("options.forceUnicodeFont", (Predicate<Options>)(dka -> dka.forceUnicodeFont), (BiConsumer<Options, Boolean>)((dka, boolean2) -> {
            dka.forceUnicodeFont = boolean2;
            final Minecraft djw3 = Minecraft.getInstance();
            if (djw3.getWindow() != null) {
                djw3.selectMainFont(boolean2);
            }
        }));
        INVERT_MOUSE = new BooleanOption("options.invertMouse", (Predicate<Options>)(dka -> dka.invertYMouse), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.invertYMouse = boolean2));
        REALMS_NOTIFICATIONS = new BooleanOption("options.realmsNotifications", (Predicate<Options>)(dka -> dka.realmsNotifications), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.realmsNotifications = boolean2));
        REDUCED_DEBUG_INFO = new BooleanOption("options.reducedDebugInfo", (Predicate<Options>)(dka -> dka.reducedDebugInfo), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.reducedDebugInfo = boolean2));
        SHOW_SUBTITLES = new BooleanOption("options.showSubtitles", (Predicate<Options>)(dka -> dka.showSubtitles), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.showSubtitles = boolean2));
        SNOOPER_ENABLED = new BooleanOption("options.snooper", (Predicate<Options>)(dka -> {
            if (dka.snooperEnabled) {}
            return false;
        }), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.snooperEnabled = boolean2));
        TOGGLE_CROUCH = new CycleOption("key.sneak", (BiConsumer<Options, Integer>)((dka, integer) -> dka.toggleCrouch = !dka.toggleCrouch), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.toggleCrouch ? "options.key.toggle" : "options.key.hold"))));
        TOGGLE_SPRINT = new CycleOption("key.sprint", (BiConsumer<Options, Integer>)((dka, integer) -> dka.toggleSprint = !dka.toggleSprint), (BiFunction<Options, CycleOption, Component>)((dka, djm) -> djm.genericValueLabel(new TranslatableComponent(dka.toggleSprint ? "options.key.toggle" : "options.key.hold"))));
        TOUCHSCREEN = new BooleanOption("options.touchscreen", (Predicate<Options>)(dka -> dka.touchscreen), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.touchscreen = boolean2));
        USE_FULLSCREEN = new BooleanOption("options.fullscreen", (Predicate<Options>)(dka -> dka.fullscreen), (BiConsumer<Options, Boolean>)((dka, boolean2) -> {
            dka.fullscreen = boolean2;
            final Minecraft djw3 = Minecraft.getInstance();
            if (djw3.getWindow() != null && djw3.getWindow().isFullscreen() != dka.fullscreen) {
                djw3.getWindow().toggleFullScreen();
                dka.fullscreen = djw3.getWindow().isFullscreen();
            }
        }));
        VIEW_BOBBING = new BooleanOption("options.viewBobbing", (Predicate<Options>)(dka -> dka.bobView), (BiConsumer<Options, Boolean>)((dka, boolean2) -> dka.bobView = boolean2));
    }
}
