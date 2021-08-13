package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.util.task.ResettingWorldTask;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.SwitchSlotTask;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsLabel;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.Logger;

public class RealmsResetWorldScreen extends RealmsScreenWithCallback {
    private static final Logger LOGGER;
    private final Screen lastScreen;
    private final RealmsServer serverData;
    private RealmsLabel titleLabel;
    private RealmsLabel subtitleLabel;
    private Component title;
    private Component subtitle;
    private Component buttonTitle;
    private int subtitleColor;
    private static final ResourceLocation SLOT_FRAME_LOCATION;
    private static final ResourceLocation UPLOAD_LOCATION;
    private static final ResourceLocation ADVENTURE_MAP_LOCATION;
    private static final ResourceLocation SURVIVAL_SPAWN_LOCATION;
    private static final ResourceLocation NEW_WORLD_LOCATION;
    private static final ResourceLocation EXPERIENCE_LOCATION;
    private static final ResourceLocation INSPIRATION_LOCATION;
    private WorldTemplatePaginatedList templates;
    private WorldTemplatePaginatedList adventuremaps;
    private WorldTemplatePaginatedList experiences;
    private WorldTemplatePaginatedList inspirations;
    public int slot;
    private ResetType typeToReset;
    private ResetWorldInfo worldInfoToReset;
    private WorldTemplate worldTemplateToReset;
    @Nullable
    private Component resetTitle;
    private final Runnable resetWorldRunnable;
    private final Runnable callback;
    
    public RealmsResetWorldScreen(final Screen doq, final RealmsServer dgn, final Runnable runnable3, final Runnable runnable4) {
        this.title = new TranslatableComponent("mco.reset.world.title");
        this.subtitle = new TranslatableComponent("mco.reset.world.warning");
        this.buttonTitle = CommonComponents.GUI_CANCEL;
        this.subtitleColor = 16711680;
        this.slot = -1;
        this.typeToReset = ResetType.NONE;
        this.lastScreen = doq;
        this.serverData = dgn;
        this.resetWorldRunnable = runnable3;
        this.callback = runnable4;
    }
    
    public RealmsResetWorldScreen(final Screen doq, final RealmsServer dgn, final Component nr3, final Component nr4, final int integer, final Component nr6, final Runnable runnable7, final Runnable runnable8) {
        this(doq, dgn, runnable7, runnable8);
        this.title = nr3;
        this.subtitle = nr4;
        this.subtitleColor = integer;
        this.buttonTitle = nr6;
    }
    
    public void setSlot(final int integer) {
        this.slot = integer;
    }
    
    public void setResetTitle(final Component nr) {
        this.resetTitle = nr;
    }
    
    public void init() {
        this.<Button>addButton(new Button(this.width / 2 - 40, RealmsScreen.row(14) - 10, 80, 20, this.buttonTitle, dlg -> this.minecraft.setScreen(this.lastScreen)));
        new Thread("Realms-reset-world-fetcher") {
            public void run() {
                final RealmsClient dfy2 = RealmsClient.create();
                try {
                    final WorldTemplatePaginatedList dhc3 = dfy2.fetchWorldTemplates(1, 10, RealmsServer.WorldType.NORMAL);
                    final WorldTemplatePaginatedList dhc4 = dfy2.fetchWorldTemplates(1, 10, RealmsServer.WorldType.ADVENTUREMAP);
                    final WorldTemplatePaginatedList dhc5 = dfy2.fetchWorldTemplates(1, 10, RealmsServer.WorldType.EXPERIENCE);
                    final WorldTemplatePaginatedList dhc6 = dfy2.fetchWorldTemplates(1, 10, RealmsServer.WorldType.INSPIRATION);
                    RealmsResetWorldScreen.this.minecraft.execute(() -> {
                        RealmsResetWorldScreen.this.templates = dhc3;
                        RealmsResetWorldScreen.this.adventuremaps = dhc4;
                        RealmsResetWorldScreen.this.experiences = dhc5;
                        RealmsResetWorldScreen.this.inspirations = dhc6;
                    });
                }
                catch (RealmsServiceException dhf3) {
                    RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world", (Throwable)dhf3);
                }
            }
        }.start();
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(this.title, this.width / 2, 7, 16777215));
        this.subtitleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(this.subtitle, this.width / 2, 22, this.subtitleColor));
        this.<FrameButton>addButton(new FrameButton(this.frame(1), RealmsScreen.row(0) + 10, new TranslatableComponent("mco.reset.world.generate"), RealmsResetWorldScreen.NEW_WORLD_LOCATION, dlg -> this.minecraft.setScreen(new RealmsResetNormalWorldScreen(this, this.title))));
        final RealmsSelectFileToUploadScreen realmsSelectFileToUploadScreen;
        final Screen doq3;
        this.<FrameButton>addButton(new FrameButton(this.frame(2), RealmsScreen.row(0) + 10, new TranslatableComponent("mco.reset.world.upload"), RealmsResetWorldScreen.UPLOAD_LOCATION, dlg -> {
            new RealmsSelectFileToUploadScreen(this.serverData.id, (this.slot != -1) ? this.slot : this.serverData.activeSlot, this, this.callback);
            doq3 = realmsSelectFileToUploadScreen;
            this.minecraft.setScreen(doq3);
            return;
        }));
        final RealmsSelectWorldTemplateScreen dif3;
        this.<FrameButton>addButton(new FrameButton(this.frame(3), RealmsScreen.row(0) + 10, new TranslatableComponent("mco.reset.world.template"), RealmsResetWorldScreen.SURVIVAL_SPAWN_LOCATION, dlg -> {
            dif3 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.NORMAL, this.templates);
            dif3.setTitle(new TranslatableComponent("mco.reset.world.template"));
            this.minecraft.setScreen(dif3);
            return;
        }));
        final RealmsSelectWorldTemplateScreen dif4;
        this.<FrameButton>addButton(new FrameButton(this.frame(1), RealmsScreen.row(6) + 20, new TranslatableComponent("mco.reset.world.adventure"), RealmsResetWorldScreen.ADVENTURE_MAP_LOCATION, dlg -> {
            dif4 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.ADVENTUREMAP, this.adventuremaps);
            dif4.setTitle(new TranslatableComponent("mco.reset.world.adventure"));
            this.minecraft.setScreen(dif4);
            return;
        }));
        final RealmsSelectWorldTemplateScreen dif5;
        this.<FrameButton>addButton(new FrameButton(this.frame(2), RealmsScreen.row(6) + 20, new TranslatableComponent("mco.reset.world.experience"), RealmsResetWorldScreen.EXPERIENCE_LOCATION, dlg -> {
            dif5 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.EXPERIENCE, this.experiences);
            dif5.setTitle(new TranslatableComponent("mco.reset.world.experience"));
            this.minecraft.setScreen(dif5);
            return;
        }));
        final RealmsSelectWorldTemplateScreen dif6;
        this.<FrameButton>addButton(new FrameButton(this.frame(3), RealmsScreen.row(6) + 20, new TranslatableComponent("mco.reset.world.inspiration"), RealmsResetWorldScreen.INSPIRATION_LOCATION, dlg -> {
            dif6 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.INSPIRATION, this.inspirations);
            dif6.setTitle(new TranslatableComponent("mco.reset.world.inspiration"));
            this.minecraft.setScreen(dif6);
            return;
        }));
        this.narrateLabels();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private int frame(final int integer) {
        return this.width / 2 - 130 + (integer - 1) * 100;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.titleLabel.render(this, dfj);
        this.subtitleLabel.render(this, dfj);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private void drawFrame(final PoseStack dfj, final int integer2, final int integer3, final Component nr, final ResourceLocation vk, final boolean boolean6, final boolean boolean7) {
        this.minecraft.getTextureManager().bind(vk);
        if (boolean6) {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        GuiComponent.blit(dfj, integer2 + 2, integer3 + 14, 0.0f, 0.0f, 56, 56, 56, 56);
        this.minecraft.getTextureManager().bind(RealmsResetWorldScreen.SLOT_FRAME_LOCATION);
        if (boolean6) {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        GuiComponent.blit(dfj, integer2, integer3 + 12, 0.0f, 0.0f, 60, 60, 60, 60);
        final int integer4 = boolean6 ? 10526880 : 16777215;
        GuiComponent.drawCenteredString(dfj, this.font, nr, integer2 + 30, integer3, integer4);
    }
    
    @Override
    protected void callback(@Nullable final WorldTemplate dhb) {
        if (dhb == null) {
            return;
        }
        if (this.slot == -1) {
            this.resetWorldWithTemplate(dhb);
        }
        else {
            switch (dhb.type) {
                case WORLD_TEMPLATE: {
                    this.typeToReset = ResetType.SURVIVAL_SPAWN;
                    break;
                }
                case ADVENTUREMAP: {
                    this.typeToReset = ResetType.ADVENTURE;
                    break;
                }
                case EXPERIENCE: {
                    this.typeToReset = ResetType.EXPERIENCE;
                    break;
                }
                case INSPIRATION: {
                    this.typeToReset = ResetType.INSPIRATION;
                    break;
                }
            }
            this.worldTemplateToReset = dhb;
            this.switchSlot();
        }
    }
    
    private void switchSlot() {
        this.switchSlot(() -> {
            switch (this.typeToReset) {
                case ADVENTURE:
                case SURVIVAL_SPAWN:
                case EXPERIENCE:
                case INSPIRATION: {
                    if (this.worldTemplateToReset != null) {
                        this.resetWorldWithTemplate(this.worldTemplateToReset);
                        break;
                    }
                    break;
                }
                case GENERATE: {
                    if (this.worldInfoToReset != null) {
                        this.triggerResetWorld(this.worldInfoToReset);
                        break;
                    }
                    break;
                }
            }
        });
    }
    
    public void switchSlot(final Runnable runnable) {
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new SwitchSlotTask(this.serverData.id, this.slot, runnable)));
    }
    
    public void resetWorldWithTemplate(final WorldTemplate dhb) {
        this.resetWorld(null, dhb, -1, true);
    }
    
    private void triggerResetWorld(final ResetWorldInfo c) {
        this.resetWorld(c.seed, null, c.levelType, c.generateStructures);
    }
    
    private void resetWorld(@Nullable final String string, @Nullable final WorldTemplate dhb, final int integer, final boolean boolean4) {
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new ResettingWorldTask(string, dhb, integer, boolean4, this.serverData.id, this.resetTitle, this.resetWorldRunnable)));
    }
    
    public void resetWorld(final ResetWorldInfo c) {
        if (this.slot == -1) {
            this.triggerResetWorld(c);
        }
        else {
            this.typeToReset = ResetType.GENERATE;
            this.worldInfoToReset = c;
            this.switchSlot();
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SLOT_FRAME_LOCATION = new ResourceLocation("realms", "textures/gui/realms/slot_frame.png");
        UPLOAD_LOCATION = new ResourceLocation("realms", "textures/gui/realms/upload.png");
        ADVENTURE_MAP_LOCATION = new ResourceLocation("realms", "textures/gui/realms/adventure.png");
        SURVIVAL_SPAWN_LOCATION = new ResourceLocation("realms", "textures/gui/realms/survival_spawn.png");
        NEW_WORLD_LOCATION = new ResourceLocation("realms", "textures/gui/realms/new_world.png");
        EXPERIENCE_LOCATION = new ResourceLocation("realms", "textures/gui/realms/experience.png");
        INSPIRATION_LOCATION = new ResourceLocation("realms", "textures/gui/realms/inspiration.png");
    }
    
    enum ResetType {
        NONE, 
        GENERATE, 
        UPLOAD, 
        ADVENTURE, 
        SURVIVAL_SPAWN, 
        EXPERIENCE, 
        INSPIRATION;
    }
    
    public static class ResetWorldInfo {
        private final String seed;
        private final int levelType;
        private final boolean generateStructures;
        
        public ResetWorldInfo(final String string, final int integer, final boolean boolean3) {
            this.seed = string;
            this.levelType = integer;
            this.generateStructures = boolean3;
        }
    }
    
    class FrameButton extends Button {
        private final ResourceLocation image;
        
        public FrameButton(final int integer2, final int integer3, final Component nr, final ResourceLocation vk, final OnPress a) {
            super(integer2, integer3, 60, 72, nr, a);
            this.image = vk;
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RealmsResetWorldScreen.this.drawFrame(dfj, this.x, this.y, this.getMessage(), this.image, this.isHovered(), this.isMouseOver(integer2, integer3));
        }
    }
}
