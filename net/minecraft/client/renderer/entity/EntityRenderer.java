package net.minecraft.client.renderer.entity;

import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.LightLayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public abstract class EntityRenderer<T extends Entity> {
    protected final EntityRenderDispatcher entityRenderDispatcher;
    protected float shadowRadius;
    protected float shadowStrength;
    
    protected EntityRenderer(final EntityRenderDispatcher eel) {
        this.shadowStrength = 1.0f;
        this.entityRenderDispatcher = eel;
    }
    
    public final int getPackedLightCoords(final T apx, final float float2) {
        final BlockPos fx4 = new BlockPos(apx.getLightProbePosition(float2));
        return LightTexture.pack(this.getBlockLightLevel(apx, fx4), this.getSkyLightLevel(apx, fx4));
    }
    
    protected int getSkyLightLevel(final T apx, final BlockPos fx) {
        return apx.level.getBrightness(LightLayer.SKY, fx);
    }
    
    protected int getBlockLightLevel(final T apx, final BlockPos fx) {
        if (apx.isOnFire()) {
            return 15;
        }
        return apx.level.getBrightness(LightLayer.BLOCK, fx);
    }
    
    public boolean shouldRender(final T apx, final Frustum ecr, final double double3, final double double4, final double double5) {
        if (!apx.shouldRender(double3, double4, double5)) {
            return false;
        }
        if (apx.noCulling) {
            return true;
        }
        AABB dcf10 = apx.getBoundingBoxForCulling().inflate(0.5);
        if (dcf10.hasNaN() || dcf10.getSize() == 0.0) {
            dcf10 = new AABB(apx.getX() - 2.0, apx.getY() - 2.0, apx.getZ() - 2.0, apx.getX() + 2.0, apx.getY() + 2.0, apx.getZ() + 2.0);
        }
        return ecr.isVisible(dcf10);
    }
    
    public Vec3 getRenderOffset(final T apx, final float float2) {
        return Vec3.ZERO;
    }
    
    public void render(final T apx, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (!this.shouldShowName(apx)) {
            return;
        }
        this.renderNameTag(apx, apx.getDisplayName(), dfj, dzy, integer);
    }
    
    protected boolean shouldShowName(final T apx) {
        return apx.shouldShowName() && apx.hasCustomName();
    }
    
    public abstract ResourceLocation getTextureLocation(final T apx);
    
    public Font getFont() {
        return this.entityRenderDispatcher.getFont();
    }
    
    protected void renderNameTag(final T apx, final Component nr, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final double double7 = this.entityRenderDispatcher.distanceToSqr(apx);
        if (double7 > 4096.0) {
            return;
        }
        final boolean boolean9 = !apx.isDiscrete();
        final float float10 = apx.getBbHeight() + 0.5f;
        final int integer2 = "deadmau5".equals(nr.getString()) ? -10 : 0;
        dfj.pushPose();
        dfj.translate(0.0, float10, 0.0);
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.scale(-0.025f, -0.025f, 0.025f);
        final Matrix4f b12 = dfj.last().pose();
        final float float11 = Minecraft.getInstance().options.getBackgroundOpacity(0.25f);
        final int integer3 = (int)(float11 * 255.0f) << 24;
        final Font dkr15 = this.getFont();
        final float float12 = (float)(-dkr15.width(nr) / 2);
        dkr15.drawInBatch(nr, float12, (float)integer2, 553648127, false, b12, dzy, boolean9, integer3, integer);
        if (boolean9) {
            dkr15.drawInBatch(nr, float12, (float)integer2, -1, false, b12, dzy, false, 0, integer);
        }
        dfj.popPose();
    }
    
    public EntityRenderDispatcher getDispatcher() {
        return this.entityRenderDispatcher;
    }
}
