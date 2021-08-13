package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.LightTexture;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LightLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Mob;

public abstract class MobRenderer<T extends Mob, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    public MobRenderer(final EntityRenderDispatcher eel, final M dtu, final float float3) {
        super(eel, dtu, float3);
    }
    
    @Override
    protected boolean shouldShowName(final T aqk) {
        return super.shouldShowName(aqk) && (aqk.shouldShowName() || (aqk.hasCustomName() && aqk == this.entityRenderDispatcher.crosshairPickEntity));
    }
    
    public boolean shouldRender(final T aqk, final Frustum ecr, final double double3, final double double4, final double double5) {
        if (super.shouldRender((T)aqk, ecr, double3, double4, double5)) {
            return true;
        }
        final Entity apx10 = aqk.getLeashHolder();
        return apx10 != null && ecr.isVisible(apx10.getBoundingBoxForCulling());
    }
    
    @Override
    public void render(final T aqk, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        super.render(aqk, float2, float3, dfj, dzy, integer);
        final Entity apx8 = aqk.getLeashHolder();
        if (apx8 == null) {
            return;
        }
        this.<Entity>renderLeash(aqk, float3, dfj, dzy, apx8);
    }
    
    private <E extends Entity> void renderLeash(final T aqk, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final E apx) {
        dfj.pushPose();
        final Vec3 dck7 = apx.getRopeHoldPosition(float2);
        final double double8 = Mth.lerp(float2, aqk.yBodyRot, aqk.yBodyRotO) * 0.017453292f + 1.5707963267948966;
        final Vec3 dck8 = aqk.getLeashOffset();
        final double double9 = Math.cos(double8) * dck8.z + Math.sin(double8) * dck8.x;
        final double double10 = Math.sin(double8) * dck8.z - Math.cos(double8) * dck8.x;
        final double double11 = Mth.lerp(float2, aqk.xo, aqk.getX()) + double9;
        final double double12 = Mth.lerp(float2, aqk.yo, aqk.getY()) + dck8.y;
        final double double13 = Mth.lerp(float2, aqk.zo, aqk.getZ()) + double10;
        dfj.translate(double9, dck8.y, double10);
        final float float3 = (float)(dck7.x - double11);
        final float float4 = (float)(dck7.y - double12);
        final float float5 = (float)(dck7.z - double13);
        final float float6 = 0.025f;
        final VertexConsumer dfn25 = dzy.getBuffer(RenderType.leash());
        final Matrix4f b26 = dfj.last().pose();
        final float float7 = Mth.fastInvSqrt(float3 * float3 + float5 * float5) * 0.025f / 2.0f;
        final float float8 = float5 * float7;
        final float float9 = float3 * float7;
        final BlockPos fx30 = new BlockPos(aqk.getEyePosition(float2));
        final BlockPos fx31 = new BlockPos(apx.getEyePosition(float2));
        final int integer32 = this.getBlockLightLevel((T)aqk, fx30);
        final int integer33 = this.entityRenderDispatcher.<E>getRenderer(apx).getBlockLightLevel(apx, fx31);
        final int integer34 = aqk.level.getBrightness(LightLayer.SKY, fx30);
        final int integer35 = aqk.level.getBrightness(LightLayer.SKY, fx31);
        renderSide(dfn25, b26, float3, float4, float5, integer32, integer33, integer34, integer35, 0.025f, 0.025f, float8, float9);
        renderSide(dfn25, b26, float3, float4, float5, integer32, integer33, integer34, integer35, 0.025f, 0.0f, float8, float9);
        dfj.popPose();
    }
    
    public static void renderSide(final VertexConsumer dfn, final Matrix4f b, final float float3, final float float4, final float float5, final int integer6, final int integer7, final int integer8, final int integer9, final float float10, final float float11, final float float12, final float float13) {
        final int integer10 = 24;
        for (int integer11 = 0; integer11 < 24; ++integer11) {
            final float float14 = integer11 / 23.0f;
            final int integer12 = (int)Mth.lerp(float14, (float)integer6, (float)integer7);
            final int integer13 = (int)Mth.lerp(float14, (float)integer8, (float)integer9);
            final int integer14 = LightTexture.pack(integer12, integer13);
            addVertexPair(dfn, b, integer14, float3, float4, float5, float10, float11, 24, integer11, false, float12, float13);
            addVertexPair(dfn, b, integer14, float3, float4, float5, float10, float11, 24, integer11 + 1, true, float12, float13);
        }
    }
    
    public static void addVertexPair(final VertexConsumer dfn, final Matrix4f b, final int integer3, final float float4, final float float5, final float float6, final float float7, final float float8, final int integer9, final int integer10, final boolean boolean11, final float float12, final float float13) {
        float float14 = 0.5f;
        float float15 = 0.4f;
        float float16 = 0.3f;
        if (integer10 % 2 == 0) {
            float14 *= 0.7f;
            float15 *= 0.7f;
            float16 *= 0.7f;
        }
        final float float17 = integer10 / (float)integer9;
        final float float18 = float4 * float17;
        final float float19 = (float5 > 0.0f) ? (float5 * float17 * float17) : (float5 - float5 * (1.0f - float17) * (1.0f - float17));
        final float float20 = float6 * float17;
        if (!boolean11) {
            dfn.vertex(b, float18 + float12, float19 + float7 - float8, float20 - float13).color(float14, float15, float16, 1.0f).uv2(integer3).endVertex();
        }
        dfn.vertex(b, float18 - float12, float19 + float8, float20 + float13).color(float14, float15, float16, 1.0f).uv2(integer3).endVertex();
        if (boolean11) {
            dfn.vertex(b, float18 + float12, float19 + float7 - float8, float20 - float13).color(float14, float15, float16, 1.0f).uv2(integer3).endVertex();
        }
    }
}
