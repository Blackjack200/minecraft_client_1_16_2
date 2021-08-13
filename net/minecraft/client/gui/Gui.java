package net.minecraft.client.gui;

import java.util.UUID;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.border.WorldBorder;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.Score;
import com.google.common.collect.Iterables;
import java.util.stream.Collectors;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import java.util.Iterator;
import net.minecraft.client.resources.MobEffectTextureManager;
import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.Camera;
import net.minecraft.client.Options;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.AttackIndicatorStatus;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.FastColor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.level.GameType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.chat.OverlayChatListener;
import net.minecraft.client.gui.chat.StandardChatListener;
import net.minecraft.client.gui.chat.NarratorChatListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.chat.ChatListener;
import java.util.List;
import net.minecraft.network.chat.ChatType;
import java.util.Map;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.Minecraft;
import java.util.Random;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class Gui extends GuiComponent {
    private static final ResourceLocation VIGNETTE_LOCATION;
    private static final ResourceLocation WIDGETS_LOCATION;
    private static final ResourceLocation PUMPKIN_BLUR_LOCATION;
    private static final Component DEMO_EXPIRED_TEXT;
    private final Random random;
    private final Minecraft minecraft;
    private final ItemRenderer itemRenderer;
    private final ChatComponent chat;
    private int tickCount;
    @Nullable
    private Component overlayMessageString;
    private int overlayMessageTime;
    private boolean animateOverlayMessageColor;
    public float vignetteBrightness;
    private int toolHighlightTimer;
    private ItemStack lastToolHighlight;
    private final DebugScreenOverlay debugScreen;
    private final SubtitleOverlay subtitleOverlay;
    private final SpectatorGui spectatorGui;
    private final PlayerTabOverlay tabList;
    private final BossHealthOverlay bossOverlay;
    private int titleTime;
    @Nullable
    private Component title;
    @Nullable
    private Component subtitle;
    private int titleFadeInTime;
    private int titleStayTime;
    private int titleFadeOutTime;
    private int lastHealth;
    private int displayHealth;
    private long lastHealthTime;
    private long healthBlinkTime;
    private int screenWidth;
    private int screenHeight;
    private final Map<ChatType, List<ChatListener>> chatListeners;
    
    public Gui(final Minecraft djw) {
        this.random = new Random();
        this.vignetteBrightness = 1.0f;
        this.lastToolHighlight = ItemStack.EMPTY;
        this.chatListeners = (Map<ChatType, List<ChatListener>>)Maps.newHashMap();
        this.minecraft = djw;
        this.itemRenderer = djw.getItemRenderer();
        this.debugScreen = new DebugScreenOverlay(djw);
        this.spectatorGui = new SpectatorGui(djw);
        this.chat = new ChatComponent(djw);
        this.tabList = new PlayerTabOverlay(djw, this);
        this.bossOverlay = new BossHealthOverlay(djw);
        this.subtitleOverlay = new SubtitleOverlay(djw);
        for (final ChatType no6 : ChatType.values()) {
            this.chatListeners.put(no6, Lists.newArrayList());
        }
        final ChatListener dkv3 = NarratorChatListener.INSTANCE;
        ((List)this.chatListeners.get(ChatType.CHAT)).add(new StandardChatListener(djw));
        ((List)this.chatListeners.get(ChatType.CHAT)).add(dkv3);
        ((List)this.chatListeners.get(ChatType.SYSTEM)).add(new StandardChatListener(djw));
        ((List)this.chatListeners.get(ChatType.SYSTEM)).add(dkv3);
        ((List)this.chatListeners.get(ChatType.GAME_INFO)).add(new OverlayChatListener(djw));
        this.resetTitleTimes();
    }
    
    public void resetTitleTimes() {
        this.titleFadeInTime = 10;
        this.titleStayTime = 70;
        this.titleFadeOutTime = 20;
    }
    
    public void render(final PoseStack dfj, final float float2) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        final Font dkr4 = this.getFont();
        RenderSystem.enableBlend();
        if (Minecraft.useFancyGraphics()) {
            this.renderVignette(this.minecraft.getCameraEntity());
        }
        else {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
        }
        final ItemStack bly5 = this.minecraft.player.inventory.getArmor(3);
        if (this.minecraft.options.getCameraType().isFirstPerson() && bly5.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
            this.renderPumpkin();
        }
        final float float3 = Mth.lerp(float2, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime);
        if (float3 > 0.0f && !this.minecraft.player.hasEffect(MobEffects.CONFUSION)) {
            this.renderPortalOverlay(float3);
        }
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            this.spectatorGui.renderHotbar(dfj, float2);
        }
        else if (!this.minecraft.options.hideGui) {
            this.renderHotbar(float2, dfj);
        }
        if (!this.minecraft.options.hideGui) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.getTextureManager().bind(Gui.GUI_ICONS_LOCATION);
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            this.renderCrosshair(dfj);
            RenderSystem.defaultBlendFunc();
            this.minecraft.getProfiler().push("bossHealth");
            this.bossOverlay.render(dfj);
            this.minecraft.getProfiler().pop();
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.getTextureManager().bind(Gui.GUI_ICONS_LOCATION);
            if (this.minecraft.gameMode.canHurtPlayer()) {
                this.renderPlayerHealth(dfj);
            }
            this.renderVehicleHealth(dfj);
            RenderSystem.disableBlend();
            final int integer7 = this.screenWidth / 2 - 91;
            if (this.minecraft.player.isRidingJumpable()) {
                this.renderJumpMeter(dfj, integer7);
            }
            else if (this.minecraft.gameMode.hasExperience()) {
                this.renderExperienceBar(dfj, integer7);
            }
            if (this.minecraft.options.heldItemTooltips && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                this.renderSelectedItemName(dfj);
            }
            else if (this.minecraft.player.isSpectator()) {
                this.spectatorGui.renderTooltip(dfj);
            }
        }
        if (this.minecraft.player.getSleepTimer() > 0) {
            this.minecraft.getProfiler().push("sleep");
            RenderSystem.disableDepthTest();
            RenderSystem.disableAlphaTest();
            final float float4 = (float)this.minecraft.player.getSleepTimer();
            float float5 = float4 / 100.0f;
            if (float5 > 1.0f) {
                float5 = 1.0f - (float4 - 100.0f) / 10.0f;
            }
            final int integer8 = (int)(220.0f * float5) << 24 | 0x101020;
            GuiComponent.fill(dfj, 0, 0, this.screenWidth, this.screenHeight, integer8);
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
            this.minecraft.getProfiler().pop();
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        if (this.minecraft.isDemo()) {
            this.renderDemoOverlay(dfj);
        }
        this.renderEffects(dfj);
        if (this.minecraft.options.renderDebug) {
            this.debugScreen.render(dfj);
        }
        if (!this.minecraft.options.hideGui) {
            if (this.overlayMessageString != null && this.overlayMessageTime > 0) {
                this.minecraft.getProfiler().push("overlayMessage");
                final float float4 = this.overlayMessageTime - float2;
                int integer9 = (int)(float4 * 255.0f / 20.0f);
                if (integer9 > 255) {
                    integer9 = 255;
                }
                if (integer9 > 8) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)(this.screenWidth / 2), (float)(this.screenHeight - 68), 0.0f);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int integer8 = 16777215;
                    if (this.animateOverlayMessageColor) {
                        integer8 = (Mth.hsvToRgb(float4 / 50.0f, 0.7f, 0.6f) & 0xFFFFFF);
                    }
                    final int integer10 = integer9 << 24 & 0xFF000000;
                    final int integer11 = dkr4.width(this.overlayMessageString);
                    this.drawBackdrop(dfj, dkr4, -4, integer11, 0xFFFFFF | integer10);
                    dkr4.draw(dfj, this.overlayMessageString, (float)(-integer11 / 2), -4.0f, integer8 | integer10);
                    RenderSystem.disableBlend();
                    RenderSystem.popMatrix();
                }
                this.minecraft.getProfiler().pop();
            }
            if (this.title != null && this.titleTime > 0) {
                this.minecraft.getProfiler().push("titleAndSubtitle");
                final float float4 = this.titleTime - float2;
                int integer9 = 255;
                if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
                    final float float6 = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime - float4;
                    integer9 = (int)(float6 * 255.0f / this.titleFadeInTime);
                }
                if (this.titleTime <= this.titleFadeOutTime) {
                    integer9 = (int)(float4 * 255.0f / this.titleFadeOutTime);
                }
                integer9 = Mth.clamp(integer9, 0, 255);
                if (integer9 > 8) {
                    RenderSystem.pushMatrix();
                    RenderSystem.translatef((float)(this.screenWidth / 2), (float)(this.screenHeight / 2), 0.0f);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.pushMatrix();
                    RenderSystem.scalef(4.0f, 4.0f, 4.0f);
                    final int integer8 = integer9 << 24 & 0xFF000000;
                    final int integer10 = dkr4.width(this.title);
                    this.drawBackdrop(dfj, dkr4, -10, integer10, 0xFFFFFF | integer8);
                    dkr4.drawShadow(dfj, this.title, (float)(-integer10 / 2), -10.0f, 0xFFFFFF | integer8);
                    RenderSystem.popMatrix();
                    if (this.subtitle != null) {
                        RenderSystem.pushMatrix();
                        RenderSystem.scalef(2.0f, 2.0f, 2.0f);
                        final int integer11 = dkr4.width(this.subtitle);
                        this.drawBackdrop(dfj, dkr4, 5, integer11, 0xFFFFFF | integer8);
                        dkr4.drawShadow(dfj, this.subtitle, (float)(-integer11 / 2), 5.0f, 0xFFFFFF | integer8);
                        RenderSystem.popMatrix();
                    }
                    RenderSystem.disableBlend();
                    RenderSystem.popMatrix();
                }
                this.minecraft.getProfiler().pop();
            }
            this.subtitleOverlay.render(dfj);
            final Scoreboard ddk7 = this.minecraft.level.getScoreboard();
            Objective ddh8 = null;
            final PlayerTeam ddi9 = ddk7.getPlayersTeam(this.minecraft.player.getScoreboardName());
            if (ddi9 != null) {
                final int integer10 = ddi9.getColor().getId();
                if (integer10 >= 0) {
                    ddh8 = ddk7.getDisplayObjective(3 + integer10);
                }
            }
            Objective ddh9 = (ddh8 != null) ? ddh8 : ddk7.getDisplayObjective(1);
            if (ddh9 != null) {
                this.displayScoreboardSidebar(dfj, ddh9);
            }
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableAlphaTest();
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0f, (float)(this.screenHeight - 48), 0.0f);
            this.minecraft.getProfiler().push("chat");
            this.chat.render(dfj, this.tickCount);
            this.minecraft.getProfiler().pop();
            RenderSystem.popMatrix();
            ddh9 = ddk7.getDisplayObjective(0);
            if (this.minecraft.options.keyPlayerList.isDown() && (!this.minecraft.isLocalServer() || this.minecraft.player.connection.getOnlinePlayers().size() > 1 || ddh9 != null)) {
                this.tabList.setVisible(true);
                this.tabList.render(dfj, this.screenWidth, ddk7, ddh9);
            }
            else {
                this.tabList.setVisible(false);
            }
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableAlphaTest();
    }
    
    private void drawBackdrop(final PoseStack dfj, final Font dkr, final int integer3, final int integer4, final int integer5) {
        final int integer6 = this.minecraft.options.getBackgroundColor(0.0f);
        if (integer6 != 0) {
            final int integer7 = -integer4 / 2;
            final int integer8 = integer7 - 2;
            final int integer9 = integer3 - 2;
            final int integer10 = integer7 + integer4 + 2;
            dkr.getClass();
            GuiComponent.fill(dfj, integer8, integer9, integer10, integer3 + 9 + 2, FastColor.ARGB32.multiply(integer6, integer5));
        }
    }
    
    private void renderCrosshair(final PoseStack dfj) {
        final Options dka3 = this.minecraft.options;
        if (!dka3.getCameraType().isFirstPerson()) {
            return;
        }
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR && !this.canRenderCrosshairForSpectator(this.minecraft.hitResult)) {
            return;
        }
        if (dka3.renderDebug && !dka3.hideGui && !this.minecraft.player.isReducedDebugInfo() && !dka3.reducedDebugInfo) {
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(this.screenWidth / 2), (float)(this.screenHeight / 2), (float)this.getBlitOffset());
            final Camera djh4 = this.minecraft.gameRenderer.getMainCamera();
            RenderSystem.rotatef(djh4.getXRot(), -1.0f, 0.0f, 0.0f);
            RenderSystem.rotatef(djh4.getYRot(), 0.0f, 1.0f, 0.0f);
            RenderSystem.scalef(-1.0f, -1.0f, -1.0f);
            RenderSystem.renderCrosshair(10);
            RenderSystem.popMatrix();
        }
        else {
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            final int integer4 = 15;
            this.blit(dfj, (this.screenWidth - 15) / 2, (this.screenHeight - 15) / 2, 0, 0, 15, 15);
            if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.CROSSHAIR) {
                final float float5 = this.minecraft.player.getAttackStrengthScale(0.0f);
                boolean boolean6 = false;
                if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && float5 >= 1.0f) {
                    boolean6 = (this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0f);
                    boolean6 &= this.minecraft.crosshairPickEntity.isAlive();
                }
                final int integer5 = this.screenHeight / 2 - 7 + 16;
                final int integer6 = this.screenWidth / 2 - 8;
                if (boolean6) {
                    this.blit(dfj, integer6, integer5, 68, 94, 16, 16);
                }
                else if (float5 < 1.0f) {
                    final int integer7 = (int)(float5 * 17.0f);
                    this.blit(dfj, integer6, integer5, 36, 94, 16, 4);
                    this.blit(dfj, integer6, integer5, 52, 94, integer7, 4);
                }
            }
        }
    }
    
    private boolean canRenderCrosshairForSpectator(final HitResult dci) {
        if (dci == null) {
            return false;
        }
        if (dci.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult)dci).getEntity() instanceof MenuProvider;
        }
        if (dci.getType() == HitResult.Type.BLOCK) {
            final BlockPos fx3 = ((BlockHitResult)dci).getBlockPos();
            final Level bru4 = this.minecraft.level;
            return bru4.getBlockState(fx3).getMenuProvider(bru4, fx3) != null;
        }
        return false;
    }
    
    protected void renderEffects(final PoseStack dfj) {
        final Collection<MobEffectInstance> collection3 = this.minecraft.player.getActiveEffects();
        if (collection3.isEmpty()) {
            return;
        }
        RenderSystem.enableBlend();
        int integer4 = 0;
        int integer5 = 0;
        final MobEffectTextureManager ekh6 = this.minecraft.getMobEffectTextures();
        final List<Runnable> list7 = (List<Runnable>)Lists.newArrayListWithExpectedSize(collection3.size());
        this.minecraft.getTextureManager().bind(AbstractContainerScreen.INVENTORY_LOCATION);
        for (final MobEffectInstance apr9 : Ordering.natural().reverse().sortedCopy((Iterable)collection3)) {
            final MobEffect app10 = apr9.getEffect();
            if (apr9.showIcon()) {
                int integer6 = this.screenWidth;
                int integer7 = 1;
                if (this.minecraft.isDemo()) {
                    integer7 += 15;
                }
                if (app10.isBeneficial()) {
                    ++integer4;
                    integer6 -= 25 * integer4;
                }
                else {
                    ++integer5;
                    integer6 -= 25 * integer5;
                    integer7 += 26;
                }
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                float float13 = 1.0f;
                if (apr9.isAmbient()) {
                    this.blit(dfj, integer6, integer7, 165, 166, 24, 24);
                }
                else {
                    this.blit(dfj, integer6, integer7, 141, 166, 24, 24);
                    if (apr9.getDuration() <= 200) {
                        final int integer8 = 10 - apr9.getDuration() / 20;
                        float13 = Mth.clamp(apr9.getDuration() / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos(apr9.getDuration() * 3.1415927f / 5.0f) * Mth.clamp(integer8 / 10.0f * 0.25f, 0.0f, 0.25f);
                    }
                }
                final TextureAtlasSprite eju14 = ekh6.get(app10);
                final int integer9 = integer6;
                final int integer10 = integer7;
                final float float14 = float13;
                list7.add((() -> {
                    this.minecraft.getTextureManager().bind(eju14.atlas().location());
                    RenderSystem.color4f(1.0f, 1.0f, 1.0f, float14);
                    GuiComponent.blit(dfj, integer9 + 3, integer10 + 3, this.getBlitOffset(), 18, 18, eju14);
                }));
            }
        }
        list7.forEach(Runnable::run);
    }
    
    protected void renderHotbar(final float float1, final PoseStack dfj) {
        final Player bft4 = this.getCameraPlayer();
        if (bft4 == null) {
            return;
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(Gui.WIDGETS_LOCATION);
        final ItemStack bly5 = bft4.getOffhandItem();
        final HumanoidArm aqf6 = bft4.getMainArm().getOpposite();
        final int integer7 = this.screenWidth / 2;
        final int integer8 = this.getBlitOffset();
        final int integer9 = 182;
        final int integer10 = 91;
        this.setBlitOffset(-90);
        this.blit(dfj, integer7 - 91, this.screenHeight - 22, 0, 0, 182, 22);
        this.blit(dfj, integer7 - 91 - 1 + bft4.inventory.selected * 20, this.screenHeight - 22 - 1, 0, 22, 24, 22);
        if (!bly5.isEmpty()) {
            if (aqf6 == HumanoidArm.LEFT) {
                this.blit(dfj, integer7 - 91 - 29, this.screenHeight - 23, 24, 22, 29, 24);
            }
            else {
                this.blit(dfj, integer7 + 91, this.screenHeight - 23, 53, 22, 29, 24);
            }
        }
        this.setBlitOffset(integer8);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        for (int integer11 = 0; integer11 < 9; ++integer11) {
            final int integer12 = integer7 - 90 + integer11 * 20 + 2;
            final int integer13 = this.screenHeight - 16 - 3;
            this.renderSlot(integer12, integer13, float1, bft4, bft4.inventory.items.get(integer11));
        }
        if (!bly5.isEmpty()) {
            final int integer11 = this.screenHeight - 16 - 3;
            if (aqf6 == HumanoidArm.LEFT) {
                this.renderSlot(integer7 - 91 - 26, integer11, float1, bft4, bly5);
            }
            else {
                this.renderSlot(integer7 + 91 + 10, integer11, float1, bft4, bly5);
            }
        }
        if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.HOTBAR) {
            final float float2 = this.minecraft.player.getAttackStrengthScale(0.0f);
            if (float2 < 1.0f) {
                final int integer12 = this.screenHeight - 20;
                int integer13 = integer7 + 91 + 6;
                if (aqf6 == HumanoidArm.RIGHT) {
                    integer13 = integer7 - 91 - 22;
                }
                this.minecraft.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
                final int integer14 = (int)(float2 * 19.0f);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                this.blit(dfj, integer13, integer12, 0, 94, 18, 18);
                this.blit(dfj, integer13, integer12 + 18 - integer14, 18, 112 - integer14, 18, integer14);
            }
        }
        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();
    }
    
    public void renderJumpMeter(final PoseStack dfj, final int integer) {
        this.minecraft.getProfiler().push("jumpBar");
        this.minecraft.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
        final float float4 = this.minecraft.player.getJumpRidingScale();
        final int integer2 = 182;
        final int integer3 = (int)(float4 * 183.0f);
        final int integer4 = this.screenHeight - 32 + 3;
        this.blit(dfj, integer, integer4, 0, 84, 182, 5);
        if (integer3 > 0) {
            this.blit(dfj, integer, integer4, 0, 89, integer3, 5);
        }
        this.minecraft.getProfiler().pop();
    }
    
    public void renderExperienceBar(final PoseStack dfj, final int integer) {
        this.minecraft.getProfiler().push("expBar");
        this.minecraft.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
        final int integer2 = this.minecraft.player.getXpNeededForNextLevel();
        if (integer2 > 0) {
            final int integer3 = 182;
            final int integer4 = (int)(this.minecraft.player.experienceProgress * 183.0f);
            final int integer5 = this.screenHeight - 32 + 3;
            this.blit(dfj, integer, integer5, 0, 64, 182, 5);
            if (integer4 > 0) {
                this.blit(dfj, integer, integer5, 0, 69, integer4, 5);
            }
        }
        this.minecraft.getProfiler().pop();
        if (this.minecraft.player.experienceLevel > 0) {
            this.minecraft.getProfiler().push("expLevel");
            final String string5 = new StringBuilder().append("").append(this.minecraft.player.experienceLevel).toString();
            final int integer4 = (this.screenWidth - this.getFont().width(string5)) / 2;
            final int integer5 = this.screenHeight - 31 - 4;
            this.getFont().draw(dfj, string5, (float)(integer4 + 1), (float)integer5, 0);
            this.getFont().draw(dfj, string5, (float)(integer4 - 1), (float)integer5, 0);
            this.getFont().draw(dfj, string5, (float)integer4, (float)(integer5 + 1), 0);
            this.getFont().draw(dfj, string5, (float)integer4, (float)(integer5 - 1), 0);
            this.getFont().draw(dfj, string5, (float)integer4, (float)integer5, 8453920);
            this.minecraft.getProfiler().pop();
        }
    }
    
    public void renderSelectedItemName(final PoseStack dfj) {
        this.minecraft.getProfiler().push("selectedItemName");
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            final MutableComponent nx3 = new TextComponent("").append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color);
            if (this.lastToolHighlight.hasCustomHoverName()) {
                nx3.withStyle(ChatFormatting.ITALIC);
            }
            final int integer4 = this.getFont().width(nx3);
            final int integer5 = (this.screenWidth - integer4) / 2;
            int integer6 = this.screenHeight - 59;
            if (!this.minecraft.gameMode.canHurtPlayer()) {
                integer6 += 14;
            }
            int integer7 = (int)(this.toolHighlightTimer * 256.0f / 10.0f);
            if (integer7 > 255) {
                integer7 = 255;
            }
            if (integer7 > 0) {
                RenderSystem.pushMatrix();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                final int integer8 = integer5 - 2;
                final int integer9 = integer6 - 2;
                final int integer10 = integer5 + integer4 + 2;
                final int n = integer6;
                this.getFont().getClass();
                GuiComponent.fill(dfj, integer8, integer9, integer10, n + 9 + 2, this.minecraft.options.getBackgroundColor(0));
                this.getFont().drawShadow(dfj, nx3, (float)integer5, (float)integer6, 16777215 + (integer7 << 24));
                RenderSystem.disableBlend();
                RenderSystem.popMatrix();
            }
        }
        this.minecraft.getProfiler().pop();
    }
    
    public void renderDemoOverlay(final PoseStack dfj) {
        this.minecraft.getProfiler().push("demo");
        Component nr3;
        if (this.minecraft.level.getGameTime() >= 120500L) {
            nr3 = Gui.DEMO_EXPIRED_TEXT;
        }
        else {
            nr3 = new TranslatableComponent("demo.remainingTime", new Object[] { StringUtil.formatTickDuration((int)(120500L - this.minecraft.level.getGameTime())) });
        }
        final int integer4 = this.getFont().width(nr3);
        this.getFont().drawShadow(dfj, nr3, (float)(this.screenWidth - integer4 - 10), 5.0f, 16777215);
        this.minecraft.getProfiler().pop();
    }
    
    private void displayScoreboardSidebar(final PoseStack dfj, final Objective ddh) {
        final Scoreboard ddk4 = ddh.getScoreboard();
        Collection<Score> collection5 = ddk4.getPlayerScores(ddh);
        final List<Score> list6 = (List<Score>)collection5.stream().filter(ddj -> ddj.getOwner() != null && !ddj.getOwner().startsWith("#")).collect(Collectors.toList());
        if (list6.size() > 15) {
            collection5 = (Collection<Score>)Lists.newArrayList(Iterables.skip((Iterable)list6, collection5.size() - 15));
        }
        else {
            collection5 = (Collection<Score>)list6;
        }
        final List<Pair<Score, Component>> list7 = (List<Pair<Score, Component>>)Lists.newArrayListWithCapacity(collection5.size());
        final Component nr8 = ddh.getDisplayName();
        int integer10;
        final int integer9 = integer10 = this.getFont().width(nr8);
        final int integer11 = this.getFont().width(": ");
        for (final Score ddj13 : collection5) {
            final PlayerTeam ddi14 = ddk4.getPlayersTeam(ddj13.getOwner());
            final Component nr9 = PlayerTeam.formatNameForTeam(ddi14, new TextComponent(ddj13.getOwner()));
            list7.add(Pair.of((Object)ddj13, (Object)nr9));
            integer10 = Math.max(integer10, this.getFont().width(nr9) + integer11 + this.getFont().width(Integer.toString(ddj13.getScore())));
        }
        final int size = collection5.size();
        this.getFont().getClass();
        final int integer12 = size * 9;
        final int integer13 = this.screenHeight / 2 + integer12 / 3;
        final int integer14 = 3;
        final int integer15 = this.screenWidth - integer10 - 3;
        int integer16 = 0;
        final int integer17 = this.minecraft.options.getBackgroundColor(0.3f);
        final int integer18 = this.minecraft.options.getBackgroundColor(0.4f);
        for (final Pair<Score, Component> pair20 : list7) {
            ++integer16;
            final Score ddj14 = (Score)pair20.getFirst();
            final Component nr10 = (Component)pair20.getSecond();
            final String string23 = new StringBuilder().append(ChatFormatting.RED).append("").append(ddj14.getScore()).toString();
            final int integer19 = integer15;
            final int n = integer13;
            final int n2 = integer16;
            this.getFont().getClass();
            final int integer20 = n - n2 * 9;
            final int integer21 = this.screenWidth - 3 + 2;
            final int integer22 = integer19 - 2;
            final int integer23 = integer20;
            final int integer24 = integer21;
            final int n3 = integer20;
            this.getFont().getClass();
            GuiComponent.fill(dfj, integer22, integer23, integer24, n3 + 9, integer17);
            this.getFont().draw(dfj, nr10, (float)integer19, (float)integer20, -1);
            this.getFont().draw(dfj, string23, (float)(integer21 - this.getFont().width(string23)), (float)integer20, -1);
            if (integer16 == collection5.size()) {
                final int integer25 = integer19 - 2;
                final int n4 = integer20;
                this.getFont().getClass();
                GuiComponent.fill(dfj, integer25, n4 - 9 - 1, integer21, integer20 - 1, integer18);
                GuiComponent.fill(dfj, integer19 - 2, integer20 - 1, integer21, integer20, integer17);
                final Font font = this.getFont();
                final Component nr11 = nr8;
                final float float3 = (float)(integer19 + integer10 / 2 - integer9 / 2);
                final int n5 = integer20;
                this.getFont().getClass();
                font.draw(dfj, nr11, float3, (float)(n5 - 9), -1);
            }
        }
    }
    
    private Player getCameraPlayer() {
        if (!(this.minecraft.getCameraEntity() instanceof Player)) {
            return null;
        }
        return (Player)this.minecraft.getCameraEntity();
    }
    
    private LivingEntity getPlayerVehicleWithHealth() {
        final Player bft2 = this.getCameraPlayer();
        if (bft2 != null) {
            final Entity apx3 = bft2.getVehicle();
            if (apx3 == null) {
                return null;
            }
            if (apx3 instanceof LivingEntity) {
                return (LivingEntity)apx3;
            }
        }
        return null;
    }
    
    private int getVehicleMaxHearts(final LivingEntity aqj) {
        if (aqj == null || !aqj.showVehicleHealth()) {
            return 0;
        }
        final float float3 = aqj.getMaxHealth();
        int integer4 = (int)(float3 + 0.5f) / 2;
        if (integer4 > 30) {
            integer4 = 30;
        }
        return integer4;
    }
    
    private int getVisibleVehicleHeartRows(final int integer) {
        return (int)Math.ceil(integer / 10.0);
    }
    
    private void renderPlayerHealth(final PoseStack dfj) {
        final Player bft3 = this.getCameraPlayer();
        if (bft3 == null) {
            return;
        }
        final int integer4 = Mth.ceil(bft3.getHealth());
        final boolean boolean5 = this.healthBlinkTime > this.tickCount && (this.healthBlinkTime - this.tickCount) / 3L % 2L == 1L;
        final long long6 = Util.getMillis();
        if (integer4 < this.lastHealth && bft3.invulnerableTime > 0) {
            this.lastHealthTime = long6;
            this.healthBlinkTime = this.tickCount + 20;
        }
        else if (integer4 > this.lastHealth && bft3.invulnerableTime > 0) {
            this.lastHealthTime = long6;
            this.healthBlinkTime = this.tickCount + 10;
        }
        if (long6 - this.lastHealthTime > 1000L) {
            this.lastHealth = integer4;
            this.displayHealth = integer4;
            this.lastHealthTime = long6;
        }
        this.lastHealth = integer4;
        final int integer5 = this.displayHealth;
        this.random.setSeed((long)(this.tickCount * 312871));
        final FoodData bhv9 = bft3.getFoodData();
        final int integer6 = bhv9.getFoodLevel();
        final int integer7 = this.screenWidth / 2 - 91;
        final int integer8 = this.screenWidth / 2 + 91;
        final int integer9 = this.screenHeight - 39;
        final float float14 = (float)bft3.getAttributeValue(Attributes.MAX_HEALTH);
        final int integer10 = Mth.ceil(bft3.getAbsorptionAmount());
        final int integer11 = Mth.ceil((float14 + integer10) / 2.0f / 10.0f);
        final int integer12 = Math.max(10 - (integer11 - 2), 3);
        final int integer13 = integer9 - (integer11 - 1) * integer12 - 10;
        int integer14 = integer9 - 10;
        int integer15 = integer10;
        final int integer16 = bft3.getArmorValue();
        int integer17 = -1;
        if (bft3.hasEffect(MobEffects.REGENERATION)) {
            integer17 = this.tickCount % Mth.ceil(float14 + 5.0f);
        }
        this.minecraft.getProfiler().push("armor");
        for (int integer18 = 0; integer18 < 10; ++integer18) {
            if (integer16 > 0) {
                final int integer19 = integer7 + integer18 * 8;
                if (integer18 * 2 + 1 < integer16) {
                    this.blit(dfj, integer19, integer13, 34, 9, 9, 9);
                }
                if (integer18 * 2 + 1 == integer16) {
                    this.blit(dfj, integer19, integer13, 25, 9, 9, 9);
                }
                if (integer18 * 2 + 1 > integer16) {
                    this.blit(dfj, integer19, integer13, 16, 9, 9, 9);
                }
            }
        }
        this.minecraft.getProfiler().popPush("health");
        for (int integer18 = Mth.ceil((float14 + integer10) / 2.0f) - 1; integer18 >= 0; --integer18) {
            int integer19 = 16;
            if (bft3.hasEffect(MobEffects.POISON)) {
                integer19 += 36;
            }
            else if (bft3.hasEffect(MobEffects.WITHER)) {
                integer19 += 72;
            }
            int integer20 = 0;
            if (boolean5) {
                integer20 = 1;
            }
            final int integer21 = Mth.ceil((integer18 + 1) / 10.0f) - 1;
            final int integer22 = integer7 + integer18 % 10 * 8;
            int integer23 = integer9 - integer21 * integer12;
            if (integer4 <= 4) {
                integer23 += this.random.nextInt(2);
            }
            if (integer15 <= 0 && integer18 == integer17) {
                integer23 -= 2;
            }
            int integer24 = 0;
            if (bft3.level.getLevelData().isHardcore()) {
                integer24 = 5;
            }
            this.blit(dfj, integer22, integer23, 16 + integer20 * 9, 9 * integer24, 9, 9);
            if (boolean5) {
                if (integer18 * 2 + 1 < integer5) {
                    this.blit(dfj, integer22, integer23, integer19 + 54, 9 * integer24, 9, 9);
                }
                if (integer18 * 2 + 1 == integer5) {
                    this.blit(dfj, integer22, integer23, integer19 + 63, 9 * integer24, 9, 9);
                }
            }
            if (integer15 > 0) {
                if (integer15 == integer10 && integer10 % 2 == 1) {
                    this.blit(dfj, integer22, integer23, integer19 + 153, 9 * integer24, 9, 9);
                    --integer15;
                }
                else {
                    this.blit(dfj, integer22, integer23, integer19 + 144, 9 * integer24, 9, 9);
                    integer15 -= 2;
                }
            }
            else {
                if (integer18 * 2 + 1 < integer4) {
                    this.blit(dfj, integer22, integer23, integer19 + 36, 9 * integer24, 9, 9);
                }
                if (integer18 * 2 + 1 == integer4) {
                    this.blit(dfj, integer22, integer23, integer19 + 45, 9 * integer24, 9, 9);
                }
            }
        }
        final LivingEntity aqj23 = this.getPlayerVehicleWithHealth();
        int integer19 = this.getVehicleMaxHearts(aqj23);
        if (integer19 == 0) {
            this.minecraft.getProfiler().popPush("food");
            for (int integer20 = 0; integer20 < 10; ++integer20) {
                int integer21 = integer9;
                int integer22 = 16;
                int integer23 = 0;
                if (bft3.hasEffect(MobEffects.HUNGER)) {
                    integer22 += 36;
                    integer23 = 13;
                }
                if (bft3.getFoodData().getSaturationLevel() <= 0.0f && this.tickCount % (integer6 * 3 + 1) == 0) {
                    integer21 += this.random.nextInt(3) - 1;
                }
                final int integer24 = integer8 - integer20 * 8 - 9;
                this.blit(dfj, integer24, integer21, 16 + integer23 * 9, 27, 9, 9);
                if (integer20 * 2 + 1 < integer6) {
                    this.blit(dfj, integer24, integer21, integer22 + 36, 27, 9, 9);
                }
                if (integer20 * 2 + 1 == integer6) {
                    this.blit(dfj, integer24, integer21, integer22 + 45, 27, 9, 9);
                }
            }
            integer14 -= 10;
        }
        this.minecraft.getProfiler().popPush("air");
        int integer20 = bft3.getAirSupply();
        int integer21 = bft3.getMaxAirSupply();
        if (bft3.isEyeInFluid(FluidTags.WATER) || integer20 < integer21) {
            final int integer22 = this.getVisibleVehicleHeartRows(integer19) - 1;
            integer14 -= integer22 * 10;
            for (int integer23 = Mth.ceil((integer20 - 2) * 10.0 / integer21), integer24 = Mth.ceil(integer20 * 10.0 / integer21) - integer23, integer25 = 0; integer25 < integer23 + integer24; ++integer25) {
                if (integer25 < integer23) {
                    this.blit(dfj, integer8 - integer25 * 8 - 9, integer14, 16, 18, 9, 9);
                }
                else {
                    this.blit(dfj, integer8 - integer25 * 8 - 9, integer14, 25, 18, 9, 9);
                }
            }
        }
        this.minecraft.getProfiler().pop();
    }
    
    private void renderVehicleHealth(final PoseStack dfj) {
        final LivingEntity aqj3 = this.getPlayerVehicleWithHealth();
        if (aqj3 == null) {
            return;
        }
        int integer4 = this.getVehicleMaxHearts(aqj3);
        if (integer4 == 0) {
            return;
        }
        final int integer5 = (int)Math.ceil((double)aqj3.getHealth());
        this.minecraft.getProfiler().popPush("mountHealth");
        final int integer6 = this.screenHeight - 39;
        final int integer7 = this.screenWidth / 2 + 91;
        int integer8 = integer6;
        int integer9 = 0;
        final boolean boolean10 = false;
        while (integer4 > 0) {
            final int integer10 = Math.min(integer4, 10);
            integer4 -= integer10;
            for (int integer11 = 0; integer11 < integer10; ++integer11) {
                final int integer12 = 52;
                final int integer13 = 0;
                final int integer14 = integer7 - integer11 * 8 - 9;
                this.blit(dfj, integer14, integer8, 52 + integer13 * 9, 9, 9, 9);
                if (integer11 * 2 + 1 + integer9 < integer5) {
                    this.blit(dfj, integer14, integer8, 88, 9, 9, 9);
                }
                if (integer11 * 2 + 1 + integer9 == integer5) {
                    this.blit(dfj, integer14, integer8, 97, 9, 9, 9);
                }
            }
            integer8 -= 10;
            integer9 += 20;
        }
    }
    
    private void renderPumpkin() {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableAlphaTest();
        this.minecraft.getTextureManager().bind(Gui.PUMPKIN_BLUR_LOCATION);
        final Tesselator dfl2 = Tesselator.getInstance();
        final BufferBuilder dfe3 = dfl2.getBuilder();
        dfe3.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe3.vertex(0.0, this.screenHeight, -90.0).uv(0.0f, 1.0f).endVertex();
        dfe3.vertex(this.screenWidth, this.screenHeight, -90.0).uv(1.0f, 1.0f).endVertex();
        dfe3.vertex(this.screenWidth, 0.0, -90.0).uv(1.0f, 0.0f).endVertex();
        dfe3.vertex(0.0, 0.0, -90.0).uv(0.0f, 0.0f).endVertex();
        dfl2.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void updateVignetteBrightness(final Entity apx) {
        if (apx == null) {
            return;
        }
        final float float3 = Mth.clamp(1.0f - apx.getBrightness(), 0.0f, 1.0f);
        this.vignetteBrightness += (float)((float3 - this.vignetteBrightness) * 0.01);
    }
    
    private void renderVignette(final Entity apx) {
        final WorldBorder cfr3 = this.minecraft.level.getWorldBorder();
        float float4 = (float)cfr3.getDistanceToBorder(apx);
        final double double5 = Math.min(cfr3.getLerpSpeed() * cfr3.getWarningTime() * 1000.0, Math.abs(cfr3.getLerpTarget() - cfr3.getSize()));
        final double double6 = Math.max((double)cfr3.getWarningBlocks(), double5);
        if (float4 < double6) {
            float4 = 1.0f - (float)(float4 / double6);
        }
        else {
            float4 = 0.0f;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        if (float4 > 0.0f) {
            RenderSystem.color4f(0.0f, float4, float4, 1.0f);
        }
        else {
            RenderSystem.color4f(this.vignetteBrightness, this.vignetteBrightness, this.vignetteBrightness, 1.0f);
        }
        this.minecraft.getTextureManager().bind(Gui.VIGNETTE_LOCATION);
        final Tesselator dfl9 = Tesselator.getInstance();
        final BufferBuilder dfe10 = dfl9.getBuilder();
        dfe10.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe10.vertex(0.0, this.screenHeight, -90.0).uv(0.0f, 1.0f).endVertex();
        dfe10.vertex(this.screenWidth, this.screenHeight, -90.0).uv(1.0f, 1.0f).endVertex();
        dfe10.vertex(this.screenWidth, 0.0, -90.0).uv(1.0f, 0.0f).endVertex();
        dfe10.vertex(0.0, 0.0, -90.0).uv(0.0f, 0.0f).endVertex();
        dfl9.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
    }
    
    private void renderPortalOverlay(float float1) {
        if (float1 < 1.0f) {
            float1 *= float1;
            float1 *= float1;
            float1 = float1 * 0.8f + 0.2f;
        }
        RenderSystem.disableAlphaTest();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, float1);
        this.minecraft.getTextureManager().bind(TextureAtlas.LOCATION_BLOCKS);
        final TextureAtlasSprite eju3 = this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
        final float float2 = eju3.getU0();
        final float float3 = eju3.getV0();
        final float float4 = eju3.getU1();
        final float float5 = eju3.getV1();
        final Tesselator dfl8 = Tesselator.getInstance();
        final BufferBuilder dfe9 = dfl8.getBuilder();
        dfe9.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe9.vertex(0.0, this.screenHeight, -90.0).uv(float2, float5).endVertex();
        dfe9.vertex(this.screenWidth, this.screenHeight, -90.0).uv(float4, float5).endVertex();
        dfe9.vertex(this.screenWidth, 0.0, -90.0).uv(float4, float3).endVertex();
        dfe9.vertex(0.0, 0.0, -90.0).uv(float2, float3).endVertex();
        dfl8.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void renderSlot(final int integer1, final int integer2, final float float3, final Player bft, final ItemStack bly) {
        if (bly.isEmpty()) {
            return;
        }
        final float float4 = bly.getPopTime() - float3;
        if (float4 > 0.0f) {
            RenderSystem.pushMatrix();
            final float float5 = 1.0f + float4 / 5.0f;
            RenderSystem.translatef((float)(integer1 + 8), (float)(integer2 + 12), 0.0f);
            RenderSystem.scalef(1.0f / float5, (float5 + 1.0f) / 2.0f, 1.0f);
            RenderSystem.translatef((float)(-(integer1 + 8)), (float)(-(integer2 + 12)), 0.0f);
        }
        this.itemRenderer.renderAndDecorateItem(bft, bly, integer1, integer2);
        if (float4 > 0.0f) {
            RenderSystem.popMatrix();
        }
        this.itemRenderer.renderGuiItemDecorations(this.minecraft.font, bly, integer1, integer2);
    }
    
    public void tick() {
        if (this.overlayMessageTime > 0) {
            --this.overlayMessageTime;
        }
        if (this.titleTime > 0) {
            --this.titleTime;
            if (this.titleTime <= 0) {
                this.title = null;
                this.subtitle = null;
            }
        }
        ++this.tickCount;
        final Entity apx2 = this.minecraft.getCameraEntity();
        if (apx2 != null) {
            this.updateVignetteBrightness(apx2);
        }
        if (this.minecraft.player != null) {
            final ItemStack bly3 = this.minecraft.player.inventory.getSelected();
            if (bly3.isEmpty()) {
                this.toolHighlightTimer = 0;
            }
            else if (this.lastToolHighlight.isEmpty() || bly3.getItem() != this.lastToolHighlight.getItem() || !bly3.getHoverName().equals(this.lastToolHighlight.getHoverName())) {
                this.toolHighlightTimer = 40;
            }
            else if (this.toolHighlightTimer > 0) {
                --this.toolHighlightTimer;
            }
            this.lastToolHighlight = bly3;
        }
    }
    
    public void setNowPlaying(final Component nr) {
        this.setOverlayMessage(new TranslatableComponent("record.nowPlaying", new Object[] { nr }), true);
    }
    
    public void setOverlayMessage(final Component nr, final boolean boolean2) {
        this.overlayMessageString = nr;
        this.overlayMessageTime = 60;
        this.animateOverlayMessageColor = boolean2;
    }
    
    public void setTitles(@Nullable final Component nr1, @Nullable final Component nr2, final int integer3, final int integer4, final int integer5) {
        if (nr1 == null && nr2 == null && integer3 < 0 && integer4 < 0 && integer5 < 0) {
            this.title = null;
            this.subtitle = null;
            this.titleTime = 0;
            return;
        }
        if (nr1 != null) {
            this.title = nr1;
            this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
            return;
        }
        if (nr2 != null) {
            this.subtitle = nr2;
            return;
        }
        if (integer3 >= 0) {
            this.titleFadeInTime = integer3;
        }
        if (integer4 >= 0) {
            this.titleStayTime = integer4;
        }
        if (integer5 >= 0) {
            this.titleFadeOutTime = integer5;
        }
        if (this.titleTime > 0) {
            this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
        }
    }
    
    public void handleChat(final ChatType no, final Component nr, final UUID uUID) {
        for (final ChatListener dkv6 : (List)this.chatListeners.get(no)) {
            dkv6.handle(no, nr, uUID);
        }
    }
    
    public ChatComponent getChat() {
        return this.chat;
    }
    
    public int getGuiTicks() {
        return this.tickCount;
    }
    
    public Font getFont() {
        return this.minecraft.font;
    }
    
    public SpectatorGui getSpectatorGui() {
        return this.spectatorGui;
    }
    
    public PlayerTabOverlay getTabList() {
        return this.tabList;
    }
    
    public void onDisconnected() {
        this.tabList.reset();
        this.bossOverlay.reset();
        this.minecraft.getToasts().clear();
    }
    
    public BossHealthOverlay getBossOverlay() {
        return this.bossOverlay;
    }
    
    public void clearCache() {
        this.debugScreen.clearChunkCache();
    }
    
    static {
        VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");
        WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
        PUMPKIN_BLUR_LOCATION = new ResourceLocation("textures/misc/pumpkinblur.png");
        DEMO_EXPIRED_TEXT = new TranslatableComponent("demo.demoExpired");
    }
}
