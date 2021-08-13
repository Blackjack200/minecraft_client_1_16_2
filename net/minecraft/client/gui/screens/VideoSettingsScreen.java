package net.minecraft.client.gui.screens;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.ChatFormatting;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.FullscreenResolutionProgressOption;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.GpuWarnlistManager;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.Option;
import net.minecraft.network.chat.Component;

public class VideoSettingsScreen extends OptionsSubScreen {
    private static final Component FABULOUS;
    private static final Component WARNING_MESSAGE;
    private static final Component WARNING_TITLE;
    private static final Component BUTTON_ACCEPT;
    private static final Component BUTTON_CANCEL;
    private static final Component NEW_LINE;
    private static final Option[] OPTIONS;
    private OptionsList list;
    private final GpuWarnlistManager gpuWarnlistManager;
    private final int oldMipmaps;
    
    public VideoSettingsScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("options.videoTitle"));
        (this.gpuWarnlistManager = doq.minecraft.getGpuWarnlistManager()).resetWarnings();
        if (dka.graphicsMode == GraphicsStatus.FABULOUS) {
            this.gpuWarnlistManager.dismissWarning();
        }
        this.oldMipmaps = dka.mipmapLevels;
    }
    
    @Override
    protected void init() {
        (this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25)).addBig(new FullscreenResolutionProgressOption(this.minecraft.getWindow()));
        this.list.addBig(Option.BIOME_BLEND_RADIUS);
        this.list.addSmall(VideoSettingsScreen.OPTIONS);
        this.children.add(this.list);
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE, dlg -> {
            this.minecraft.options.save();
            this.minecraft.getWindow().changeFullscreenVideoMode();
            this.minecraft.setScreen(this.lastScreen);
        }));
    }
    
    @Override
    public void removed() {
        if (this.options.mipmapLevels != this.oldMipmaps) {
            this.minecraft.updateMaxMipLevel(this.options.mipmapLevels);
            this.minecraft.delayTextureReload();
        }
        super.removed();
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        final int integer2 = this.options.guiScale;
        if (super.mouseClicked(double1, double2, integer)) {
            if (this.options.guiScale != integer2) {
                this.minecraft.resizeDisplay();
            }
            if (this.gpuWarnlistManager.isShowingWarning()) {
                final List<FormattedText> list8 = (List<FormattedText>)Lists.newArrayList((Object[])new FormattedText[] { VideoSettingsScreen.WARNING_MESSAGE, VideoSettingsScreen.NEW_LINE });
                final String string9 = this.gpuWarnlistManager.getRendererWarnings();
                if (string9 != null) {
                    list8.add(VideoSettingsScreen.NEW_LINE);
                    list8.add(new TranslatableComponent("options.graphics.warning.renderer", new Object[] { string9 }).withStyle(ChatFormatting.GRAY));
                }
                final String string10 = this.gpuWarnlistManager.getVendorWarnings();
                if (string10 != null) {
                    list8.add(VideoSettingsScreen.NEW_LINE);
                    list8.add(new TranslatableComponent("options.graphics.warning.vendor", new Object[] { string10 }).withStyle(ChatFormatting.GRAY));
                }
                final String string11 = this.gpuWarnlistManager.getVersionWarnings();
                if (string11 != null) {
                    list8.add(VideoSettingsScreen.NEW_LINE);
                    list8.add(new TranslatableComponent("options.graphics.warning.version", new Object[] { string11 }).withStyle(ChatFormatting.GRAY));
                }
                this.minecraft.setScreen(new PopupScreen(VideoSettingsScreen.WARNING_TITLE, list8, (ImmutableList<PopupScreen.ButtonOption>)ImmutableList.of(new PopupScreen.ButtonOption(VideoSettingsScreen.BUTTON_ACCEPT, dlg -> {
                    this.options.graphicsMode = GraphicsStatus.FABULOUS;
                    Minecraft.getInstance().levelRenderer.allChanged();
                    this.gpuWarnlistManager.dismissWarning();
                    this.minecraft.setScreen(this);
                    return;
                }), new PopupScreen.ButtonOption(VideoSettingsScreen.BUTTON_CANCEL, dlg -> {
                    this.gpuWarnlistManager.dismissWarningAndSkipFabulous();
                    this.minecraft.setScreen(this);
                    return;
                }))));
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseReleased(final double double1, final double double2, final int integer) {
        final int integer2 = this.options.guiScale;
        if (super.mouseReleased(double1, double2, integer)) {
            return true;
        }
        if (this.list.mouseReleased(double1, double2, integer)) {
            if (this.options.guiScale != integer2) {
                this.minecraft.resizeDisplay();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.list.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 5, 16777215);
        super.render(dfj, integer2, integer3, float4);
        final List<FormattedCharSequence> list6 = OptionsSubScreen.tooltipAt(this.list, integer2, integer3);
        if (list6 != null) {
            this.renderTooltip(dfj, list6, integer2, integer3);
        }
    }
    
    static {
        FABULOUS = new TranslatableComponent("options.graphics.fabulous").withStyle(ChatFormatting.ITALIC);
        WARNING_MESSAGE = new TranslatableComponent("options.graphics.warning.message", new Object[] { VideoSettingsScreen.FABULOUS, VideoSettingsScreen.FABULOUS });
        WARNING_TITLE = new TranslatableComponent("options.graphics.warning.title").withStyle(ChatFormatting.RED);
        BUTTON_ACCEPT = new TranslatableComponent("options.graphics.warning.accept");
        BUTTON_CANCEL = new TranslatableComponent("options.graphics.warning.cancel");
        NEW_LINE = new TextComponent("\n");
        OPTIONS = new Option[] { Option.GRAPHICS, Option.RENDER_DISTANCE, Option.AMBIENT_OCCLUSION, Option.FRAMERATE_LIMIT, Option.ENABLE_VSYNC, Option.VIEW_BOBBING, Option.GUI_SCALE, Option.ATTACK_INDICATOR, Option.GAMMA, Option.RENDER_CLOUDS, Option.USE_FULLSCREEN, Option.PARTICLES, Option.MIPMAP_LEVELS, Option.ENTITY_SHADOWS, Option.SCREEN_EFFECTS_SCALE, Option.ENTITY_DISTANCE_SCALING, Option.FOV_EFFECTS_SCALE };
    }
}
