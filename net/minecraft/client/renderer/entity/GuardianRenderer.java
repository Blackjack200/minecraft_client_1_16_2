package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.world.entity.monster.Guardian;

public class GuardianRenderer extends MobRenderer<Guardian, GuardianModel> {
    private static final ResourceLocation GUARDIAN_LOCATION;
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION;
    private static final RenderType BEAM_RENDER_TYPE;
    
    public GuardianRenderer(final EntityRenderDispatcher eel) {
        this(eel, 0.5f);
    }
    
    protected GuardianRenderer(final EntityRenderDispatcher eel, final float float2) {
        super(eel, new GuardianModel(), float2);
    }
    
    @Override
    public boolean shouldRender(final Guardian bdj, final Frustum ecr, final double double3, final double double4, final double double5) {
        if (super.shouldRender(bdj, ecr, double3, double4, double5)) {
            return true;
        }
        if (bdj.hasActiveAttackTarget()) {
            final LivingEntity aqj10 = bdj.getActiveAttackTarget();
            if (aqj10 != null) {
                final Vec3 dck11 = this.getPosition(aqj10, aqj10.getBbHeight() * 0.5, 1.0f);
                final Vec3 dck12 = this.getPosition(bdj, bdj.getEyeHeight(), 1.0f);
                return ecr.isVisible(new AABB(dck12.x, dck12.y, dck12.z, dck11.x, dck11.y, dck11.z));
            }
        }
        return false;
    }
    
    private Vec3 getPosition(final LivingEntity aqj, final double double2, final float float3) {
        final double double3 = Mth.lerp(float3, aqj.xOld, aqj.getX());
        final double double4 = Mth.lerp(float3, aqj.yOld, aqj.getY()) + double2;
        final double double5 = Mth.lerp(float3, aqj.zOld, aqj.getZ());
        return new Vec3(double3, double4, double5);
    }
    
    @Override
    public void render(final Guardian bdj, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        super.render(bdj, float2, float3, dfj, dzy, integer);
        final LivingEntity aqj8 = bdj.getActiveAttackTarget();
        if (aqj8 != null) {
            final float float4 = bdj.getAttackAnimationScale(float3);
            final float float5 = bdj.level.getGameTime() + float3;
            final float float6 = float5 * 0.5f % 1.0f;
            final float float7 = bdj.getEyeHeight();
            dfj.pushPose();
            dfj.translate(0.0, float7, 0.0);
            final Vec3 dck13 = this.getPosition(aqj8, aqj8.getBbHeight() * 0.5, float3);
            final Vec3 dck14 = this.getPosition(bdj, float7, float3);
            Vec3 dck15 = dck13.subtract(dck14);
            final float float8 = (float)(dck15.length() + 1.0);
            dck15 = dck15.normalize();
            final float float9 = (float)Math.acos(dck15.y);
            final float float10 = (float)Math.atan2(dck15.z, dck15.x);
            dfj.mulPose(Vector3f.YP.rotationDegrees((1.5707964f - float10) * 57.295776f));
            dfj.mulPose(Vector3f.XP.rotationDegrees(float9 * 57.295776f));
            final int integer2 = 1;
            final float float11 = float5 * 0.05f * -1.5f;
            final float float12 = float4 * float4;
            final int integer3 = 64 + (int)(float12 * 191.0f);
            final int integer4 = 32 + (int)(float12 * 191.0f);
            final int integer5 = 128 - (int)(float12 * 64.0f);
            final float float13 = 0.2f;
            final float float14 = 0.282f;
            final float float15 = Mth.cos(float11 + 2.3561945f) * 0.282f;
            final float float16 = Mth.sin(float11 + 2.3561945f) * 0.282f;
            final float float17 = Mth.cos(float11 + 0.7853982f) * 0.282f;
            final float float18 = Mth.sin(float11 + 0.7853982f) * 0.282f;
            final float float19 = Mth.cos(float11 + 3.926991f) * 0.282f;
            final float float20 = Mth.sin(float11 + 3.926991f) * 0.282f;
            final float float21 = Mth.cos(float11 + 5.4977875f) * 0.282f;
            final float float22 = Mth.sin(float11 + 5.4977875f) * 0.282f;
            final float float23 = Mth.cos(float11 + 3.1415927f) * 0.2f;
            final float float24 = Mth.sin(float11 + 3.1415927f) * 0.2f;
            final float float25 = Mth.cos(float11 + 0.0f) * 0.2f;
            final float float26 = Mth.sin(float11 + 0.0f) * 0.2f;
            final float float27 = Mth.cos(float11 + 1.5707964f) * 0.2f;
            final float float28 = Mth.sin(float11 + 1.5707964f) * 0.2f;
            final float float29 = Mth.cos(float11 + 4.712389f) * 0.2f;
            final float float30 = Mth.sin(float11 + 4.712389f) * 0.2f;
            final float float31 = float8;
            final float float32 = 0.0f;
            final float float33 = 0.4999f;
            final float float34 = -1.0f + float6;
            final float float35 = float8 * 2.5f + float34;
            final VertexConsumer dfn48 = dzy.getBuffer(GuardianRenderer.BEAM_RENDER_TYPE);
            final PoseStack.Pose a49 = dfj.last();
            final Matrix4f b50 = a49.pose();
            final Matrix3f a50 = a49.normal();
            vertex(dfn48, b50, a50, float23, float31, float24, integer3, integer4, integer5, 0.4999f, float35);
            vertex(dfn48, b50, a50, float23, 0.0f, float24, integer3, integer4, integer5, 0.4999f, float34);
            vertex(dfn48, b50, a50, float25, 0.0f, float26, integer3, integer4, integer5, 0.0f, float34);
            vertex(dfn48, b50, a50, float25, float31, float26, integer3, integer4, integer5, 0.0f, float35);
            vertex(dfn48, b50, a50, float27, float31, float28, integer3, integer4, integer5, 0.4999f, float35);
            vertex(dfn48, b50, a50, float27, 0.0f, float28, integer3, integer4, integer5, 0.4999f, float34);
            vertex(dfn48, b50, a50, float29, 0.0f, float30, integer3, integer4, integer5, 0.0f, float34);
            vertex(dfn48, b50, a50, float29, float31, float30, integer3, integer4, integer5, 0.0f, float35);
            float float36 = 0.0f;
            if (bdj.tickCount % 2 == 0) {
                float36 = 0.5f;
            }
            vertex(dfn48, b50, a50, float15, float31, float16, integer3, integer4, integer5, 0.5f, float36 + 0.5f);
            vertex(dfn48, b50, a50, float17, float31, float18, integer3, integer4, integer5, 1.0f, float36 + 0.5f);
            vertex(dfn48, b50, a50, float21, float31, float22, integer3, integer4, integer5, 1.0f, float36);
            vertex(dfn48, b50, a50, float19, float31, float20, integer3, integer4, integer5, 0.5f, float36);
            dfj.popPose();
        }
    }
    
    private static void vertex(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a, final float float4, final float float5, final float float6, final int integer7, final int integer8, final int integer9, final float float10, final float float11) {
        dfn.vertex(b, float4, float5, float6).color(integer7, integer8, integer9, 255).uv(float10, float11).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Guardian bdj) {
        return GuardianRenderer.GUARDIAN_LOCATION;
    }
    
    static {
        GUARDIAN_LOCATION = new ResourceLocation("textures/entity/guardian.png");
        GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");
        BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GuardianRenderer.GUARDIAN_BEAM_LOCATION);
    }
}
