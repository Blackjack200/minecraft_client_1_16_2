package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;

public class FishingHookRenderer extends EntityRenderer<FishingHook> {
    private static final ResourceLocation TEXTURE_LOCATION;
    private static final RenderType RENDER_TYPE;
    
    public FishingHookRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public void render(final FishingHook bgf, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final Player bft8 = bgf.getPlayerOwner();
        if (bft8 == null) {
            return;
        }
        dfj.pushPose();
        dfj.pushPose();
        dfj.scale(0.5f, 0.5f, 0.5f);
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        final PoseStack.Pose a9 = dfj.last();
        final Matrix4f b10 = a9.pose();
        final Matrix3f a10 = a9.normal();
        final VertexConsumer dfn12 = dzy.getBuffer(FishingHookRenderer.RENDER_TYPE);
        vertex(dfn12, b10, a10, integer, 0.0f, 0, 0, 1);
        vertex(dfn12, b10, a10, integer, 1.0f, 0, 1, 1);
        vertex(dfn12, b10, a10, integer, 1.0f, 1, 1, 0);
        vertex(dfn12, b10, a10, integer, 0.0f, 1, 0, 0);
        dfj.popPose();
        int integer2 = (bft8.getMainArm() == HumanoidArm.RIGHT) ? 1 : -1;
        final ItemStack bly14 = bft8.getMainHandItem();
        if (bly14.getItem() != Items.FISHING_ROD) {
            integer2 = -integer2;
        }
        final float float4 = bft8.getAttackAnim(float3);
        final float float5 = Mth.sin(Mth.sqrt(float4) * 3.1415927f);
        final float float6 = Mth.lerp(float3, bft8.yBodyRotO, bft8.yBodyRot) * 0.017453292f;
        final double double18 = Mth.sin(float6);
        final double double19 = Mth.cos(float6);
        final double double20 = integer2 * 0.35;
        final double double21 = 0.8;
        double double22;
        double double23;
        double double24;
        float float7;
        if ((this.entityRenderDispatcher.options != null && !this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) || bft8 != Minecraft.getInstance().player) {
            double22 = Mth.lerp(float3, bft8.xo, bft8.getX()) - double19 * double20 - double18 * 0.8;
            double23 = bft8.yo + bft8.getEyeHeight() + (bft8.getY() - bft8.yo) * float3 - 0.45;
            double24 = Mth.lerp(float3, bft8.zo, bft8.getZ()) - double18 * double20 + double19 * 0.8;
            float7 = (bft8.isCrouching() ? -0.1875f : 0.0f);
        }
        else {
            double double25 = this.entityRenderDispatcher.options.fov;
            double25 /= 100.0;
            Vec3 dck35 = new Vec3(integer2 * -0.36 * double25, -0.045 * double25, 0.4);
            dck35 = dck35.xRot(-Mth.lerp(float3, bft8.xRotO, bft8.xRot) * 0.017453292f);
            dck35 = dck35.yRot(-Mth.lerp(float3, bft8.yRotO, bft8.yRot) * 0.017453292f);
            dck35 = dck35.yRot(float5 * 0.5f);
            dck35 = dck35.xRot(-float5 * 0.7f);
            double22 = Mth.lerp(float3, bft8.xo, bft8.getX()) + dck35.x;
            double23 = Mth.lerp(float3, bft8.yo, bft8.getY()) + dck35.y;
            double24 = Mth.lerp(float3, bft8.zo, bft8.getZ()) + dck35.z;
            float7 = bft8.getEyeHeight();
        }
        double double25 = Mth.lerp(float3, bgf.xo, bgf.getX());
        final double double26 = Mth.lerp(float3, bgf.yo, bgf.getY()) + 0.25;
        final double double27 = Mth.lerp(float3, bgf.zo, bgf.getZ());
        final float float8 = (float)(double22 - double25);
        final float float9 = (float)(double23 - double26) + float7;
        final float float10 = (float)(double24 - double27);
        final VertexConsumer dfn13 = dzy.getBuffer(RenderType.lines());
        final Matrix4f b11 = dfj.last().pose();
        final int integer3 = 16;
        for (int integer4 = 0; integer4 < 16; ++integer4) {
            stringVertex(float8, float9, float10, dfn13, b11, fraction(integer4, 16));
            stringVertex(float8, float9, float10, dfn13, b11, fraction(integer4 + 1, 16));
        }
        dfj.popPose();
        super.render(bgf, float2, float3, dfj, dzy, integer);
    }
    
    private static float fraction(final int integer1, final int integer2) {
        return integer1 / (float)integer2;
    }
    
    private static void vertex(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a, final int integer4, final float float5, final int integer6, final int integer7, final int integer8) {
        dfn.vertex(b, float5 - 0.5f, integer6 - 0.5f, 0.0f).color(255, 255, 255, 255).uv((float)integer7, (float)integer8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer4).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    private static void stringVertex(final float float1, final float float2, final float float3, final VertexConsumer dfn, final Matrix4f b, final float float6) {
        dfn.vertex(b, float1 * float6, float2 * (float6 * float6 + float6) * 0.5f + 0.25f, float3 * float6).color(0, 0, 0, 255).endVertex();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final FishingHook bgf) {
        return FishingHookRenderer.TEXTURE_LOCATION;
    }
    
    static {
        TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");
        RENDER_TYPE = RenderType.entityCutout(FishingHookRenderer.TEXTURE_LOCATION);
    }
}
