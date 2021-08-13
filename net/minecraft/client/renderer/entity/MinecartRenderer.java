package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.RenderShape;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public class MinecartRenderer<T extends AbstractMinecart> extends EntityRenderer<T> {
    private static final ResourceLocation MINECART_LOCATION;
    protected final EntityModel<T> model;
    
    public MinecartRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new MinecartModel<T>();
        this.shadowRadius = 0.7f;
    }
    
    @Override
    public void render(final T bhi, float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        super.render(bhi, float2, float3, dfj, dzy, integer);
        dfj.pushPose();
        long long8 = bhi.getId() * 493286711L;
        long8 = long8 * long8 * 4392167121L + long8 * 98761L;
        final float float4 = (((long8 >> 16 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float float5 = (((long8 >> 20 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float float6 = (((long8 >> 24 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        dfj.translate(float4, float5, float6);
        final double double13 = Mth.lerp(float3, bhi.xOld, bhi.getX());
        final double double14 = Mth.lerp(float3, bhi.yOld, bhi.getY());
        final double double15 = Mth.lerp(float3, bhi.zOld, bhi.getZ());
        final double double16 = 0.30000001192092896;
        final Vec3 dck21 = bhi.getPos(double13, double14, double15);
        float float7 = Mth.lerp(float3, bhi.xRotO, bhi.xRot);
        if (dck21 != null) {
            Vec3 dck22 = bhi.getPosOffs(double13, double14, double15, 0.30000001192092896);
            Vec3 dck23 = bhi.getPosOffs(double13, double14, double15, -0.30000001192092896);
            if (dck22 == null) {
                dck22 = dck21;
            }
            if (dck23 == null) {
                dck23 = dck21;
            }
            dfj.translate(dck21.x - double13, (dck22.y + dck23.y) / 2.0 - double14, dck21.z - double15);
            Vec3 dck24 = dck23.add(-dck22.x, -dck22.y, -dck22.z);
            if (dck24.length() != 0.0) {
                dck24 = dck24.normalize();
                float2 = (float)(Math.atan2(dck24.z, dck24.x) * 180.0 / 3.141592653589793);
                float7 = (float)(Math.atan(dck24.y) * 73.0);
            }
        }
        dfj.translate(0.0, 0.375, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float2));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(-float7));
        final float float8 = bhi.getHurtTime() - float3;
        float float9 = bhi.getDamage() - float3;
        if (float9 < 0.0f) {
            float9 = 0.0f;
        }
        if (float8 > 0.0f) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(float8) * float8 * float9 / 10.0f * bhi.getHurtDir()));
        }
        final int integer2 = bhi.getDisplayOffset();
        final BlockState cee26 = bhi.getDisplayBlockState();
        if (cee26.getRenderShape() != RenderShape.INVISIBLE) {
            dfj.pushPose();
            final float float10 = 0.75f;
            dfj.scale(0.75f, 0.75f, 0.75f);
            dfj.translate(-0.5, (integer2 - 8) / 16.0f, 0.5);
            dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f));
            this.renderMinecartContents(bhi, float3, cee26, dfj, dzy, integer);
            dfj.popPose();
        }
        dfj.scale(-1.0f, -1.0f, 1.0f);
        this.model.setupAnim(bhi, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f);
        final VertexConsumer dfn27 = dzy.getBuffer(this.model.renderType(this.getTextureLocation(bhi)));
        this.model.renderToBuffer(dfj, dfn27, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final T bhi) {
        return MinecartRenderer.MINECART_LOCATION;
    }
    
    protected void renderMinecartContents(final T bhi, final float float2, final BlockState cee, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(cee, dfj, dzy, integer, OverlayTexture.NO_OVERLAY);
    }
    
    static {
        MINECART_LOCATION = new ResourceLocation("textures/entity/minecart.png");
    }
}
