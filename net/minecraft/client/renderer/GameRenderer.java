package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import java.util.Locale;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Screenshot;
import net.minecraft.CrashReportCategory;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.world.effect.MobEffects;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.entity.player.Player;
import com.mojang.math.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.util.Mth;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.function.Predicate;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.Entity;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.gui.MapRenderer;
import java.util.Random;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class GameRenderer implements ResourceManagerReloadListener, AutoCloseable {
    private static final ResourceLocation NAUSEA_LOCATION;
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final ResourceManager resourceManager;
    private final Random random;
    private float renderDistance;
    public final ItemInHandRenderer itemInHandRenderer;
    private final MapRenderer mapRenderer;
    private final RenderBuffers renderBuffers;
    private int tick;
    private float fov;
    private float oldFov;
    private float darkenWorldAmount;
    private float darkenWorldAmountO;
    private boolean renderHand;
    private boolean renderBlockOutline;
    private long lastScreenshotAttempt;
    private long lastActiveTime;
    private final LightTexture lightTexture;
    private final OverlayTexture overlayTexture;
    private boolean panoramicMode;
    private float zoom;
    private float zoomX;
    private float zoomY;
    @Nullable
    private ItemStack itemActivationItem;
    private int itemActivationTicks;
    private float itemActivationOffX;
    private float itemActivationOffY;
    @Nullable
    private PostChain postEffect;
    private static final ResourceLocation[] EFFECTS;
    public static final int EFFECT_NONE;
    private int effectIndex;
    private boolean effectActive;
    private final Camera mainCamera;
    
    public GameRenderer(final Minecraft djw, final ResourceManager acf, final RenderBuffers eae) {
        this.random = new Random();
        this.renderHand = true;
        this.renderBlockOutline = true;
        this.lastActiveTime = Util.getMillis();
        this.overlayTexture = new OverlayTexture();
        this.zoom = 1.0f;
        this.effectIndex = GameRenderer.EFFECT_NONE;
        this.mainCamera = new Camera();
        this.minecraft = djw;
        this.resourceManager = acf;
        this.itemInHandRenderer = djw.getItemInHandRenderer();
        this.mapRenderer = new MapRenderer(djw.getTextureManager());
        this.lightTexture = new LightTexture(this, djw);
        this.renderBuffers = eae;
        this.postEffect = null;
    }
    
    public void close() {
        this.lightTexture.close();
        this.mapRenderer.close();
        this.overlayTexture.close();
        this.shutdownEffect();
    }
    
    public void shutdownEffect() {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        this.postEffect = null;
        this.effectIndex = GameRenderer.EFFECT_NONE;
    }
    
    public void togglePostEffect() {
        this.effectActive = !this.effectActive;
    }
    
    public void checkEntityPostEffect(@Nullable final Entity apx) {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        this.postEffect = null;
        if (apx instanceof Creeper) {
            this.loadEffect(new ResourceLocation("shaders/post/creeper.json"));
        }
        else if (apx instanceof Spider) {
            this.loadEffect(new ResourceLocation("shaders/post/spider.json"));
        }
        else if (apx instanceof EnderMan) {
            this.loadEffect(new ResourceLocation("shaders/post/invert.json"));
        }
    }
    
    private void loadEffect(final ResourceLocation vk) {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        try {
            (this.postEffect = new PostChain(this.minecraft.getTextureManager(), this.resourceManager, this.minecraft.getMainRenderTarget(), vk)).resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            this.effectActive = true;
        }
        catch (IOException iOException3) {
            GameRenderer.LOGGER.warn("Failed to load shader: {}", vk, iOException3);
            this.effectIndex = GameRenderer.EFFECT_NONE;
            this.effectActive = false;
        }
        catch (JsonSyntaxException jsonSyntaxException3) {
            GameRenderer.LOGGER.warn("Failed to parse shader: {}", vk, jsonSyntaxException3);
            this.effectIndex = GameRenderer.EFFECT_NONE;
            this.effectActive = false;
        }
    }
    
    public void onResourceManagerReload(final ResourceManager acf) {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        this.postEffect = null;
        if (this.effectIndex == GameRenderer.EFFECT_NONE) {
            this.checkEntityPostEffect(this.minecraft.getCameraEntity());
        }
        else {
            this.loadEffect(GameRenderer.EFFECTS[this.effectIndex]);
        }
    }
    
    public void tick() {
        this.tickFov();
        this.lightTexture.tick();
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(this.minecraft.player);
        }
        this.mainCamera.tick();
        ++this.tick;
        this.itemInHandRenderer.tick();
        this.minecraft.levelRenderer.tickRain(this.mainCamera);
        this.darkenWorldAmountO = this.darkenWorldAmount;
        if (this.minecraft.gui.getBossOverlay().shouldDarkenScreen()) {
            this.darkenWorldAmount += 0.05f;
            if (this.darkenWorldAmount > 1.0f) {
                this.darkenWorldAmount = 1.0f;
            }
        }
        else if (this.darkenWorldAmount > 0.0f) {
            this.darkenWorldAmount -= 0.0125f;
        }
        if (this.itemActivationTicks > 0) {
            --this.itemActivationTicks;
            if (this.itemActivationTicks == 0) {
                this.itemActivationItem = null;
            }
        }
    }
    
    @Nullable
    public PostChain currentEffect() {
        return this.postEffect;
    }
    
    public void resize(final int integer1, final int integer2) {
        if (this.postEffect != null) {
            this.postEffect.resize(integer1, integer2);
        }
        this.minecraft.levelRenderer.resize(integer1, integer2);
    }
    
    public void pick(final float float1) {
        final Entity apx3 = this.minecraft.getCameraEntity();
        if (apx3 == null) {
            return;
        }
        if (this.minecraft.level == null) {
            return;
        }
        this.minecraft.getProfiler().push("pick");
        this.minecraft.crosshairPickEntity = null;
        double double4 = this.minecraft.gameMode.getPickRange();
        this.minecraft.hitResult = apx3.pick(double4, float1, false);
        final Vec3 dck6 = apx3.getEyePosition(float1);
        boolean boolean7 = false;
        final int integer8 = 3;
        double double5 = double4;
        if (this.minecraft.gameMode.hasFarPickRange()) {
            double5 = (double4 = 6.0);
        }
        else {
            if (double5 > 3.0) {
                boolean7 = true;
            }
            double4 = double5;
        }
        double5 *= double5;
        if (this.minecraft.hitResult != null) {
            double5 = this.minecraft.hitResult.getLocation().distanceToSqr(dck6);
        }
        final Vec3 dck7 = apx3.getViewVector(1.0f);
        final Vec3 dck8 = dck6.add(dck7.x * double4, dck7.y * double4, dck7.z * double4);
        final float float2 = 1.0f;
        final AABB dcf14 = apx3.getBoundingBox().expandTowards(dck7.scale(double4)).inflate(1.0, 1.0, 1.0);
        final EntityHitResult dch15 = ProjectileUtil.getEntityHitResult(apx3, dck6, dck8, dcf14, (Predicate<Entity>)(apx -> !apx.isSpectator() && apx.isPickable()), double5);
        if (dch15 != null) {
            final Entity apx4 = dch15.getEntity();
            final Vec3 dck9 = dch15.getLocation();
            final double double6 = dck6.distanceToSqr(dck9);
            if (boolean7 && double6 > 9.0) {
                this.minecraft.hitResult = BlockHitResult.miss(dck9, Direction.getNearest(dck7.x, dck7.y, dck7.z), new BlockPos(dck9));
            }
            else if (double6 < double5 || this.minecraft.hitResult == null) {
                this.minecraft.hitResult = dch15;
                if (apx4 instanceof LivingEntity || apx4 instanceof ItemFrame) {
                    this.minecraft.crosshairPickEntity = apx4;
                }
            }
        }
        this.minecraft.getProfiler().pop();
    }
    
    private void tickFov() {
        float float2 = 1.0f;
        if (this.minecraft.getCameraEntity() instanceof AbstractClientPlayer) {
            final AbstractClientPlayer dzb3 = (AbstractClientPlayer)this.minecraft.getCameraEntity();
            float2 = dzb3.getFieldOfViewModifier();
        }
        this.oldFov = this.fov;
        this.fov += (float2 - this.fov) * 0.5f;
        if (this.fov > 1.5f) {
            this.fov = 1.5f;
        }
        if (this.fov < 0.1f) {
            this.fov = 0.1f;
        }
    }
    
    private double getFov(final Camera djh, final float float2, final boolean boolean3) {
        if (this.panoramicMode) {
            return 90.0;
        }
        double double5 = 70.0;
        if (boolean3) {
            double5 = this.minecraft.options.fov;
            double5 *= Mth.lerp(float2, this.oldFov, this.fov);
        }
        if (djh.getEntity() instanceof LivingEntity && ((LivingEntity)djh.getEntity()).isDeadOrDying()) {
            final float float3 = Math.min(((LivingEntity)djh.getEntity()).deathTime + float2, 20.0f);
            double5 /= (1.0f - 500.0f / (float3 + 500.0f)) * 2.0f + 1.0f;
        }
        final FluidState cuu7 = djh.getFluidInCamera();
        if (!cuu7.isEmpty()) {
            double5 = double5 * 60.0 / 70.0;
        }
        return double5;
    }
    
    private void bobHurt(final PoseStack dfj, final float float2) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
            final LivingEntity aqj4 = (LivingEntity)this.minecraft.getCameraEntity();
            float float3 = aqj4.hurtTime - float2;
            if (aqj4.isDeadOrDying()) {
                final float float4 = Math.min(aqj4.deathTime + float2, 20.0f);
                dfj.mulPose(Vector3f.ZP.rotationDegrees(40.0f - 8000.0f / (float4 + 200.0f)));
            }
            if (float3 < 0.0f) {
                return;
            }
            float3 /= aqj4.hurtDuration;
            float3 = Mth.sin(float3 * float3 * float3 * float3 * 3.1415927f);
            final float float4 = aqj4.hurtDir;
            dfj.mulPose(Vector3f.YP.rotationDegrees(-float4));
            dfj.mulPose(Vector3f.ZP.rotationDegrees(-float3 * 14.0f));
            dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
        }
    }
    
    private void bobView(final PoseStack dfj, final float float2) {
        if (!(this.minecraft.getCameraEntity() instanceof Player)) {
            return;
        }
        final Player bft4 = (Player)this.minecraft.getCameraEntity();
        final float float3 = bft4.walkDist - bft4.walkDistO;
        final float float4 = -(bft4.walkDist + float3 * float2);
        final float float5 = Mth.lerp(float2, bft4.oBob, bft4.bob);
        dfj.translate(Mth.sin(float4 * 3.1415927f) * float5 * 0.5f, -Math.abs(Mth.cos(float4 * 3.1415927f) * float5), 0.0);
        dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(float4 * 3.1415927f) * float5 * 3.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(Math.abs(Mth.cos(float4 * 3.1415927f - 0.2f) * float5) * 5.0f));
    }
    
    private void renderItemInHand(final PoseStack dfj, final Camera djh, final float float3) {
        if (this.panoramicMode) {
            return;
        }
        this.resetProjectionMatrix(this.getProjectionMatrix(djh, float3, false));
        final PoseStack.Pose a5 = dfj.last();
        a5.pose().setIdentity();
        a5.normal().setIdentity();
        dfj.pushPose();
        this.bobHurt(dfj, float3);
        if (this.minecraft.options.bobView) {
            this.bobView(dfj, float3);
        }
        final boolean boolean6 = this.minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity)this.minecraft.getCameraEntity()).isSleeping();
        if (this.minecraft.options.getCameraType().isFirstPerson() && !boolean6 && !this.minecraft.options.hideGui && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            this.lightTexture.turnOnLightLayer();
            this.itemInHandRenderer.renderHandsWithItems(float3, dfj, this.renderBuffers.bufferSource(), this.minecraft.player, this.minecraft.getEntityRenderDispatcher().<LocalPlayer>getPackedLightCoords(this.minecraft.player, float3));
            this.lightTexture.turnOffLightLayer();
        }
        dfj.popPose();
        if (this.minecraft.options.getCameraType().isFirstPerson() && !boolean6) {
            ScreenEffectRenderer.renderScreenEffect(this.minecraft, dfj);
            this.bobHurt(dfj, float3);
        }
        if (this.minecraft.options.bobView) {
            this.bobView(dfj, float3);
        }
    }
    
    public void resetProjectionMatrix(final Matrix4f b) {
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(b);
        RenderSystem.matrixMode(5888);
    }
    
    public Matrix4f getProjectionMatrix(final Camera djh, final float float2, final boolean boolean3) {
        final PoseStack dfj5 = new PoseStack();
        dfj5.last().pose().setIdentity();
        if (this.zoom != 1.0f) {
            dfj5.translate(this.zoomX, -this.zoomY, 0.0);
            dfj5.scale(this.zoom, this.zoom, 1.0f);
        }
        dfj5.last().pose().multiply(Matrix4f.perspective(this.getFov(djh, float2, boolean3), this.minecraft.getWindow().getWidth() / (float)this.minecraft.getWindow().getHeight(), 0.05f, this.renderDistance * 4.0f));
        return dfj5.last().pose();
    }
    
    public static float getNightVisionScale(final LivingEntity aqj, final float float2) {
        final int integer3 = aqj.getEffect(MobEffects.NIGHT_VISION).getDuration();
        if (integer3 > 200) {
            return 1.0f;
        }
        return 0.7f + Mth.sin((integer3 - float2) * 3.1415927f * 0.2f) * 0.3f;
    }
    
    public void render(final float float1, final long long2, final boolean boolean3) {
        if (this.minecraft.isWindowActive() || !this.minecraft.options.pauseOnLostFocus || (this.minecraft.options.touchscreen && this.minecraft.mouseHandler.isRightPressed())) {
            this.lastActiveTime = Util.getMillis();
        }
        else if (Util.getMillis() - this.lastActiveTime > 500L) {
            this.minecraft.pauseGame(false);
        }
        if (this.minecraft.noRender) {
            return;
        }
        final int integer6 = (int)(this.minecraft.mouseHandler.xpos() * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth());
        final int integer7 = (int)(this.minecraft.mouseHandler.ypos() * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight());
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        if (boolean3 && this.minecraft.level != null) {
            this.minecraft.getProfiler().push("level");
            this.renderLevel(float1, long2, new PoseStack());
            if (this.minecraft.hasSingleplayerServer() && this.lastScreenshotAttempt < Util.getMillis() - 1000L) {
                this.lastScreenshotAttempt = Util.getMillis();
                if (!this.minecraft.getSingleplayerServer().hasWorldScreenshot()) {
                    this.takeAutoScreenshot();
                }
            }
            this.minecraft.levelRenderer.doEntityOutline();
            if (this.postEffect != null && this.effectActive) {
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.disableAlphaTest();
                RenderSystem.enableTexture();
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                this.postEffect.process(float1);
                RenderSystem.popMatrix();
            }
            this.minecraft.getMainRenderTarget().bindWrite(true);
        }
        final Window dew8 = this.minecraft.getWindow();
        RenderSystem.clear(256, Minecraft.ON_OSX);
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0, dew8.getWidth() / dew8.getGuiScale(), dew8.getHeight() / dew8.getGuiScale(), 0.0, 1000.0, 3000.0);
        RenderSystem.matrixMode(5888);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0f, 0.0f, -2000.0f);
        Lighting.setupFor3DItems();
        final PoseStack dfj9 = new PoseStack();
        if (boolean3 && this.minecraft.level != null) {
            this.minecraft.getProfiler().popPush("gui");
            if (this.minecraft.player != null) {
                final float float2 = Mth.lerp(float1, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime);
                if (float2 > 0.0f && this.minecraft.player.hasEffect(MobEffects.CONFUSION) && this.minecraft.options.screenEffectScale < 1.0f) {
                    this.renderConfusionOverlay(float2 * (1.0f - this.minecraft.options.screenEffectScale));
                }
            }
            if (!this.minecraft.options.hideGui || this.minecraft.screen != null) {
                RenderSystem.defaultAlphaFunc();
                this.renderItemActivationAnimation(this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), float1);
                this.minecraft.gui.render(dfj9, float1);
                RenderSystem.clear(256, Minecraft.ON_OSX);
            }
            this.minecraft.getProfiler().pop();
        }
        if (this.minecraft.overlay != null) {
            try {
                this.minecraft.overlay.render(dfj9, integer6, integer7, this.minecraft.getDeltaFrameTime());
                return;
            }
            catch (Throwable throwable10) {
                final CrashReport l11 = CrashReport.forThrowable(throwable10, "Rendering overlay");
                final CrashReportCategory m12 = l11.addCategory("Overlay render details");
                m12.setDetail("Overlay name", (CrashReportDetail<String>)(() -> this.minecraft.overlay.getClass().getCanonicalName()));
                throw new ReportedException(l11);
            }
        }
        if (this.minecraft.screen != null) {
            try {
                this.minecraft.screen.render(dfj9, integer6, integer7, this.minecraft.getDeltaFrameTime());
            }
            catch (Throwable throwable10) {
                final CrashReport l11 = CrashReport.forThrowable(throwable10, "Rendering screen");
                final CrashReportCategory m12 = l11.addCategory("Screen render details");
                m12.setDetail("Screen name", (CrashReportDetail<String>)(() -> this.minecraft.screen.getClass().getCanonicalName()));
                m12.setDetail("Mouse location", (CrashReportDetail<String>)(() -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%f, %f)", new Object[] { integer6, integer7, this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos() })));
                m12.setDetail("Screen size", (CrashReportDetail<String>)(() -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f", new Object[] { this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), this.minecraft.getWindow().getGuiScale() })));
                throw new ReportedException(l11);
            }
        }
    }
    
    private void takeAutoScreenshot() {
        if (this.minecraft.levelRenderer.countRenderedChunks() > 10 && this.minecraft.levelRenderer.hasRenderedAllChunks() && !this.minecraft.getSingleplayerServer().hasWorldScreenshot()) {
            final NativeImage deq2 = Screenshot.takeScreenshot(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), this.minecraft.getMainRenderTarget());
            Util.ioPool().execute(() -> {
                int integer3 = deq2.getWidth();
                int integer4 = deq2.getHeight();
                int integer5 = 0;
                int integer6 = 0;
                if (integer3 > integer4) {
                    integer5 = (integer3 - integer4) / 2;
                    integer3 = integer4;
                }
                else {
                    integer6 = (integer4 - integer3) / 2;
                    integer4 = integer3;
                }
                try (final NativeImage deq2 = new NativeImage(64, 64, false)) {
                    deq2.resizeSubRectTo(integer5, integer6, integer3, integer4, deq2);
                    deq2.writeToFile(this.minecraft.getSingleplayerServer().getWorldScreenshotFile());
                }
                catch (IOException iOException7) {
                    GameRenderer.LOGGER.warn("Couldn't save auto screenshot", (Throwable)iOException7);
                }
                finally {
                    deq2.close();
                }
            });
        }
    }
    
    private boolean shouldRenderBlockOutline() {
        if (!this.renderBlockOutline) {
            return false;
        }
        final Entity apx2 = this.minecraft.getCameraEntity();
        boolean boolean3 = apx2 instanceof Player && !this.minecraft.options.hideGui;
        if (boolean3 && !((Player)apx2).abilities.mayBuild) {
            final ItemStack bly4 = ((LivingEntity)apx2).getMainHandItem();
            final HitResult dci5 = this.minecraft.hitResult;
            if (dci5 != null && dci5.getType() == HitResult.Type.BLOCK) {
                final BlockPos fx6 = ((BlockHitResult)dci5).getBlockPos();
                final BlockState cee7 = this.minecraft.level.getBlockState(fx6);
                if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
                    boolean3 = (cee7.getMenuProvider(this.minecraft.level, fx6) != null);
                }
                else {
                    final BlockInWorld cei8 = new BlockInWorld(this.minecraft.level, fx6, false);
                    boolean3 = (!bly4.isEmpty() && (bly4.hasAdventureModeBreakTagForBlock(this.minecraft.level.getTagManager(), cei8) || bly4.hasAdventureModePlaceTagForBlock(this.minecraft.level.getTagManager(), cei8)));
                }
            }
        }
        return boolean3;
    }
    
    public void renderLevel(final float float1, final long long2, final PoseStack dfj) {
        this.lightTexture.updateLightTexture(float1);
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(this.minecraft.player);
        }
        this.pick(float1);
        this.minecraft.getProfiler().push("center");
        final boolean boolean6 = this.shouldRenderBlockOutline();
        this.minecraft.getProfiler().popPush("camera");
        final Camera djh7 = this.mainCamera;
        this.renderDistance = (float)(this.minecraft.options.renderDistance * 16);
        final PoseStack dfj2 = new PoseStack();
        dfj2.last().pose().multiply(this.getProjectionMatrix(djh7, float1, true));
        this.bobHurt(dfj2, float1);
        if (this.minecraft.options.bobView) {
            this.bobView(dfj2, float1);
        }
        final float float2 = Mth.lerp(float1, this.minecraft.player.oPortalTime, this.minecraft.player.portalTime) * (this.minecraft.options.screenEffectScale * this.minecraft.options.screenEffectScale);
        if (float2 > 0.0f) {
            final int integer10 = this.minecraft.player.hasEffect(MobEffects.CONFUSION) ? 7 : 20;
            float float3 = 5.0f / (float2 * float2 + 5.0f) - float2 * 0.04f;
            float3 *= float3;
            final Vector3f g12 = new Vector3f(0.0f, Mth.SQRT_OF_TWO / 2.0f, Mth.SQRT_OF_TWO / 2.0f);
            dfj2.mulPose(g12.rotationDegrees((this.tick + float1) * integer10));
            dfj2.scale(1.0f / float3, 1.0f, 1.0f);
            final float float4 = -(this.tick + float1) * integer10;
            dfj2.mulPose(g12.rotationDegrees(float4));
        }
        final Matrix4f b10 = dfj2.last().pose();
        this.resetProjectionMatrix(b10);
        djh7.setup(this.minecraft.level, (this.minecraft.getCameraEntity() == null) ? this.minecraft.player : this.minecraft.getCameraEntity(), !this.minecraft.options.getCameraType().isFirstPerson(), this.minecraft.options.getCameraType().isMirrored(), float1);
        dfj.mulPose(Vector3f.XP.rotationDegrees(djh7.getXRot()));
        dfj.mulPose(Vector3f.YP.rotationDegrees(djh7.getYRot() + 180.0f));
        this.minecraft.levelRenderer.renderLevel(dfj, float1, long2, boolean6, djh7, this, this.lightTexture, b10);
        this.minecraft.getProfiler().popPush("hand");
        if (this.renderHand) {
            RenderSystem.clear(256, Minecraft.ON_OSX);
            this.renderItemInHand(dfj, djh7, float1);
        }
        this.minecraft.getProfiler().pop();
    }
    
    public void resetData() {
        this.itemActivationItem = null;
        this.mapRenderer.resetData();
        this.mainCamera.reset();
    }
    
    public MapRenderer getMapRenderer() {
        return this.mapRenderer;
    }
    
    public void displayItemActivation(final ItemStack bly) {
        this.itemActivationItem = bly;
        this.itemActivationTicks = 40;
        this.itemActivationOffX = this.random.nextFloat() * 2.0f - 1.0f;
        this.itemActivationOffY = this.random.nextFloat() * 2.0f - 1.0f;
    }
    
    private void renderItemActivationAnimation(final int integer1, final int integer2, final float float3) {
        if (this.itemActivationItem == null || this.itemActivationTicks <= 0) {
            return;
        }
        final int integer3 = 40 - this.itemActivationTicks;
        final float float4 = (integer3 + float3) / 40.0f;
        final float float5 = float4 * float4;
        final float float6 = float4 * float5;
        final float float7 = 10.25f * float6 * float5 - 24.95f * float5 * float5 + 25.5f * float6 - 13.8f * float5 + 4.0f * float4;
        final float float8 = float7 * 3.1415927f;
        final float float9 = this.itemActivationOffX * (integer1 / 4);
        final float float10 = this.itemActivationOffY * (integer2 / 4);
        RenderSystem.enableAlphaTest();
        RenderSystem.pushMatrix();
        RenderSystem.pushLightingAttributes();
        RenderSystem.enableDepthTest();
        RenderSystem.disableCull();
        final PoseStack dfj13 = new PoseStack();
        dfj13.pushPose();
        dfj13.translate(integer1 / 2 + float9 * Mth.abs(Mth.sin(float8 * 2.0f)), integer2 / 2 + float10 * Mth.abs(Mth.sin(float8 * 2.0f)), -50.0);
        final float float11 = 50.0f + 175.0f * Mth.sin(float8);
        dfj13.scale(float11, -float11, float11);
        dfj13.mulPose(Vector3f.YP.rotationDegrees(900.0f * Mth.abs(Mth.sin(float8))));
        dfj13.mulPose(Vector3f.XP.rotationDegrees(6.0f * Mth.cos(float4 * 8.0f)));
        dfj13.mulPose(Vector3f.ZP.rotationDegrees(6.0f * Mth.cos(float4 * 8.0f)));
        final MultiBufferSource.BufferSource a15 = this.renderBuffers.bufferSource();
        this.minecraft.getItemRenderer().renderStatic(this.itemActivationItem, ItemTransforms.TransformType.FIXED, 15728880, OverlayTexture.NO_OVERLAY, dfj13, a15);
        dfj13.popPose();
        a15.endBatch();
        RenderSystem.popAttributes();
        RenderSystem.popMatrix();
        RenderSystem.enableCull();
        RenderSystem.disableDepthTest();
    }
    
    private void renderConfusionOverlay(final float float1) {
        final int integer3 = this.minecraft.getWindow().getGuiScaledWidth();
        final int integer4 = this.minecraft.getWindow().getGuiScaledHeight();
        final double double5 = Mth.lerp(float1, 2.0, 1.0);
        final float float2 = 0.2f * float1;
        final float float3 = 0.4f * float1;
        final float float4 = 0.2f * float1;
        final double double6 = integer3 * double5;
        final double double7 = integer4 * double5;
        final double double8 = (integer3 - double6) / 2.0;
        final double double9 = (integer4 - double7) / 2.0;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.color4f(float2, float3, float4, 1.0f);
        this.minecraft.getTextureManager().bind(GameRenderer.NAUSEA_LOCATION);
        final Tesselator dfl18 = Tesselator.getInstance();
        final BufferBuilder dfe19 = dfl18.getBuilder();
        dfe19.begin(7, DefaultVertexFormat.POSITION_TEX);
        dfe19.vertex(double8, double9 + double7, -90.0).uv(0.0f, 1.0f).endVertex();
        dfe19.vertex(double8 + double6, double9 + double7, -90.0).uv(1.0f, 1.0f).endVertex();
        dfe19.vertex(double8 + double6, double9, -90.0).uv(1.0f, 0.0f).endVertex();
        dfe19.vertex(double8, double9, -90.0).uv(0.0f, 0.0f).endVertex();
        dfl18.end();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }
    
    public float getDarkenWorldAmount(final float float1) {
        return Mth.lerp(float1, this.darkenWorldAmountO, this.darkenWorldAmount);
    }
    
    public float getRenderDistance() {
        return this.renderDistance;
    }
    
    public Camera getMainCamera() {
        return this.mainCamera;
    }
    
    public LightTexture lightTexture() {
        return this.lightTexture;
    }
    
    public OverlayTexture overlayTexture() {
        return this.overlayTexture;
    }
    
    static {
        NAUSEA_LOCATION = new ResourceLocation("textures/misc/nausea.png");
        LOGGER = LogManager.getLogger();
        EFFECTS = new ResourceLocation[] { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };
        EFFECT_NONE = GameRenderer.EFFECTS.length;
    }
}
