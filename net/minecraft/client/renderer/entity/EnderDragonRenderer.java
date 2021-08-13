package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.Model;
import javax.annotation.Nullable;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import java.util.Random;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class EnderDragonRenderer extends EntityRenderer<EnderDragon> {
    public static final ResourceLocation CRYSTAL_BEAM_LOCATION;
    private static final ResourceLocation DRAGON_EXPLODING_LOCATION;
    private static final ResourceLocation DRAGON_LOCATION;
    private static final ResourceLocation DRAGON_EYES_LOCATION;
    private static final RenderType RENDER_TYPE;
    private static final RenderType DECAL;
    private static final RenderType EYES;
    private static final RenderType BEAM;
    private static final float HALF_SQRT_3;
    private final DragonModel model;
    
    public EnderDragonRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new DragonModel();
        this.shadowRadius = 0.5f;
    }
    
    @Override
    public void render(final EnderDragon bbo, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        final float float4 = (float)bbo.getLatencyPos(7, float3)[0];
        final float float5 = (float)(bbo.getLatencyPos(5, float3)[1] - bbo.getLatencyPos(10, float3)[1]);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-float4));
        dfj.mulPose(Vector3f.XP.rotationDegrees(float5 * 10.0f));
        dfj.translate(0.0, 0.0, 1.0);
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.translate(0.0, -1.5010000467300415, 0.0);
        final boolean boolean10 = bbo.hurtTime > 0;
        this.model.prepareMobModel(bbo, 0.0f, 0.0f, float3);
        if (bbo.dragonDeathTime > 0) {
            final float float6 = bbo.dragonDeathTime / 200.0f;
            final VertexConsumer dfn12 = dzy.getBuffer(RenderType.dragonExplosionAlpha(EnderDragonRenderer.DRAGON_EXPLODING_LOCATION, float6));
            this.model.renderToBuffer(dfj, dfn12, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
            final VertexConsumer dfn13 = dzy.getBuffer(EnderDragonRenderer.DECAL);
            this.model.renderToBuffer(dfj, dfn13, integer, OverlayTexture.pack(0.0f, boolean10), 1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            final VertexConsumer dfn14 = dzy.getBuffer(EnderDragonRenderer.RENDER_TYPE);
            this.model.renderToBuffer(dfj, dfn14, integer, OverlayTexture.pack(0.0f, boolean10), 1.0f, 1.0f, 1.0f, 1.0f);
        }
        final VertexConsumer dfn14 = dzy.getBuffer(EnderDragonRenderer.EYES);
        this.model.renderToBuffer(dfj, dfn14, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        if (bbo.dragonDeathTime > 0) {
            final float float7 = (bbo.dragonDeathTime + float3) / 200.0f;
            final float float8 = Math.min((float7 > 0.8f) ? ((float7 - 0.8f) / 0.2f) : 0.0f, 1.0f);
            final Random random14 = new Random(432L);
            final VertexConsumer dfn15 = dzy.getBuffer(RenderType.lightning());
            dfj.pushPose();
            dfj.translate(0.0, -1.0, -2.0);
            for (int integer2 = 0; integer2 < (float7 + float7 * float7) / 2.0f * 60.0f; ++integer2) {
                dfj.mulPose(Vector3f.XP.rotationDegrees(random14.nextFloat() * 360.0f));
                dfj.mulPose(Vector3f.YP.rotationDegrees(random14.nextFloat() * 360.0f));
                dfj.mulPose(Vector3f.ZP.rotationDegrees(random14.nextFloat() * 360.0f));
                dfj.mulPose(Vector3f.XP.rotationDegrees(random14.nextFloat() * 360.0f));
                dfj.mulPose(Vector3f.YP.rotationDegrees(random14.nextFloat() * 360.0f));
                dfj.mulPose(Vector3f.ZP.rotationDegrees(random14.nextFloat() * 360.0f + float7 * 90.0f));
                final float float9 = random14.nextFloat() * 20.0f + 5.0f + float8 * 10.0f;
                final float float10 = random14.nextFloat() * 2.0f + 1.0f + float8 * 2.0f;
                final Matrix4f b19 = dfj.last().pose();
                final int integer3 = (int)(255.0f * (1.0f - float8));
                vertex01(dfn15, b19, integer3);
                vertex2(dfn15, b19, float9, float10);
                vertex3(dfn15, b19, float9, float10);
                vertex01(dfn15, b19, integer3);
                vertex3(dfn15, b19, float9, float10);
                vertex4(dfn15, b19, float9, float10);
                vertex01(dfn15, b19, integer3);
                vertex4(dfn15, b19, float9, float10);
                vertex2(dfn15, b19, float9, float10);
            }
            dfj.popPose();
        }
        dfj.popPose();
        if (bbo.nearestCrystal != null) {
            dfj.pushPose();
            final float float7 = (float)(bbo.nearestCrystal.getX() - Mth.lerp(float3, bbo.xo, bbo.getX()));
            final float float8 = (float)(bbo.nearestCrystal.getY() - Mth.lerp(float3, bbo.yo, bbo.getY()));
            final float float11 = (float)(bbo.nearestCrystal.getZ() - Mth.lerp(float3, bbo.zo, bbo.getZ()));
            renderCrystalBeams(float7, float8 + EndCrystalRenderer.getY(bbo.nearestCrystal, float3), float11, float3, bbo.tickCount, dfj, dzy, integer);
            dfj.popPose();
        }
        super.render(bbo, float2, float3, dfj, dzy, integer);
    }
    
    private static void vertex01(final VertexConsumer dfn, final Matrix4f b, final int integer) {
        dfn.vertex(b, 0.0f, 0.0f, 0.0f).color(255, 255, 255, integer).endVertex();
        dfn.vertex(b, 0.0f, 0.0f, 0.0f).color(255, 255, 255, integer).endVertex();
    }
    
    private static void vertex2(final VertexConsumer dfn, final Matrix4f b, final float float3, final float float4) {
        dfn.vertex(b, -EnderDragonRenderer.HALF_SQRT_3 * float4, float3, -0.5f * float4).color(255, 0, 255, 0).endVertex();
    }
    
    private static void vertex3(final VertexConsumer dfn, final Matrix4f b, final float float3, final float float4) {
        dfn.vertex(b, EnderDragonRenderer.HALF_SQRT_3 * float4, float3, -0.5f * float4).color(255, 0, 255, 0).endVertex();
    }
    
    private static void vertex4(final VertexConsumer dfn, final Matrix4f b, final float float3, final float float4) {
        dfn.vertex(b, 0.0f, float3, 1.0f * float4).color(255, 0, 255, 0).endVertex();
    }
    
    public static void renderCrystalBeams(final float float1, final float float2, final float float3, final float float4, final int integer5, final PoseStack dfj, final MultiBufferSource dzy, final int integer8) {
        final float float5 = Mth.sqrt(float1 * float1 + float3 * float3);
        final float float6 = Mth.sqrt(float1 * float1 + float2 * float2 + float3 * float3);
        dfj.pushPose();
        dfj.translate(0.0, 2.0, 0.0);
        dfj.mulPose(Vector3f.YP.rotation((float)(-Math.atan2((double)float3, (double)float1)) - 1.5707964f));
        dfj.mulPose(Vector3f.XP.rotation((float)(-Math.atan2((double)float5, (double)float2)) - 1.5707964f));
        final VertexConsumer dfn11 = dzy.getBuffer(EnderDragonRenderer.BEAM);
        final float float7 = 0.0f - (integer5 + float4) * 0.01f;
        final float float8 = Mth.sqrt(float1 * float1 + float2 * float2 + float3 * float3) / 32.0f - (integer5 + float4) * 0.01f;
        final int integer9 = 8;
        float float9 = 0.0f;
        float float10 = 0.75f;
        float float11 = 0.0f;
        final PoseStack.Pose a18 = dfj.last();
        final Matrix4f b19 = a18.pose();
        final Matrix3f a19 = a18.normal();
        for (int integer10 = 1; integer10 <= 8; ++integer10) {
            final float float12 = Mth.sin(integer10 * 6.2831855f / 8.0f) * 0.75f;
            final float float13 = Mth.cos(integer10 * 6.2831855f / 8.0f) * 0.75f;
            final float float14 = integer10 / 8.0f;
            dfn11.vertex(b19, float9 * 0.2f, float10 * 0.2f, 0.0f).color(0, 0, 0, 255).uv(float11, float7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer8).normal(a19, 0.0f, -1.0f, 0.0f).endVertex();
            dfn11.vertex(b19, float9, float10, float6).color(255, 255, 255, 255).uv(float11, float8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer8).normal(a19, 0.0f, -1.0f, 0.0f).endVertex();
            dfn11.vertex(b19, float12, float13, float6).color(255, 255, 255, 255).uv(float14, float8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer8).normal(a19, 0.0f, -1.0f, 0.0f).endVertex();
            dfn11.vertex(b19, float12 * 0.2f, float13 * 0.2f, 0.0f).color(0, 0, 0, 255).uv(float14, float7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer8).normal(a19, 0.0f, -1.0f, 0.0f).endVertex();
            float9 = float12;
            float10 = float13;
            float11 = float14;
        }
        dfj.popPose();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final EnderDragon bbo) {
        return EnderDragonRenderer.DRAGON_LOCATION;
    }
    
    static {
        CRYSTAL_BEAM_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
        DRAGON_EXPLODING_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_exploding.png");
        DRAGON_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon.png");
        DRAGON_EYES_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
        RENDER_TYPE = RenderType.entityCutoutNoCull(EnderDragonRenderer.DRAGON_LOCATION);
        DECAL = RenderType.entityDecal(EnderDragonRenderer.DRAGON_LOCATION);
        EYES = RenderType.eyes(EnderDragonRenderer.DRAGON_EYES_LOCATION);
        BEAM = RenderType.entitySmoothCutout(EnderDragonRenderer.CRYSTAL_BEAM_LOCATION);
        HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    }
    
    public static class DragonModel extends EntityModel<EnderDragon> {
        private final ModelPart head;
        private final ModelPart neck;
        private final ModelPart jaw;
        private final ModelPart body;
        private ModelPart leftWing;
        private ModelPart leftWingTip;
        private ModelPart leftFrontLeg;
        private ModelPart leftFrontLegTip;
        private ModelPart leftFrontFoot;
        private ModelPart leftRearLeg;
        private ModelPart leftRearLegTip;
        private ModelPart leftRearFoot;
        private ModelPart rightWing;
        private ModelPart rightWingTip;
        private ModelPart rightFrontLeg;
        private ModelPart rightFrontLegTip;
        private ModelPart rightFrontFoot;
        private ModelPart rightRearLeg;
        private ModelPart rightRearLegTip;
        private ModelPart rightRearFoot;
        @Nullable
        private EnderDragon entity;
        private float a;
        
        public DragonModel() {
            this.texWidth = 256;
            this.texHeight = 256;
            final float float2 = -16.0f;
            (this.head = new ModelPart(this)).addBox("upperlip", -6.0f, -1.0f, -24.0f, 12, 5, 16, 0.0f, 176, 44);
            this.head.addBox("upperhead", -8.0f, -8.0f, -10.0f, 16, 16, 16, 0.0f, 112, 30);
            this.head.mirror = true;
            this.head.addBox("scale", -5.0f, -12.0f, -4.0f, 2, 4, 6, 0.0f, 0, 0);
            this.head.addBox("nostril", -5.0f, -3.0f, -22.0f, 2, 2, 4, 0.0f, 112, 0);
            this.head.mirror = false;
            this.head.addBox("scale", 3.0f, -12.0f, -4.0f, 2, 4, 6, 0.0f, 0, 0);
            this.head.addBox("nostril", 3.0f, -3.0f, -22.0f, 2, 2, 4, 0.0f, 112, 0);
            (this.jaw = new ModelPart(this)).setPos(0.0f, 4.0f, -8.0f);
            this.jaw.addBox("jaw", -6.0f, 0.0f, -16.0f, 12, 4, 16, 0.0f, 176, 65);
            this.head.addChild(this.jaw);
            (this.neck = new ModelPart(this)).addBox("box", -5.0f, -5.0f, -5.0f, 10, 10, 10, 0.0f, 192, 104);
            this.neck.addBox("scale", -1.0f, -9.0f, -3.0f, 2, 4, 6, 0.0f, 48, 0);
            (this.body = new ModelPart(this)).setPos(0.0f, 4.0f, 8.0f);
            this.body.addBox("body", -12.0f, 0.0f, -16.0f, 24, 24, 64, 0.0f, 0, 0);
            this.body.addBox("scale", -1.0f, -6.0f, -10.0f, 2, 6, 12, 0.0f, 220, 53);
            this.body.addBox("scale", -1.0f, -6.0f, 10.0f, 2, 6, 12, 0.0f, 220, 53);
            this.body.addBox("scale", -1.0f, -6.0f, 30.0f, 2, 6, 12, 0.0f, 220, 53);
            this.leftWing = new ModelPart(this);
            this.leftWing.mirror = true;
            this.leftWing.setPos(12.0f, 5.0f, 2.0f);
            this.leftWing.addBox("bone", 0.0f, -4.0f, -4.0f, 56, 8, 8, 0.0f, 112, 88);
            this.leftWing.addBox("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 88);
            this.leftWingTip = new ModelPart(this);
            this.leftWingTip.mirror = true;
            this.leftWingTip.setPos(56.0f, 0.0f, 0.0f);
            this.leftWingTip.addBox("bone", 0.0f, -2.0f, -2.0f, 56, 4, 4, 0.0f, 112, 136);
            this.leftWingTip.addBox("skin", 0.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 144);
            this.leftWing.addChild(this.leftWingTip);
            (this.leftFrontLeg = new ModelPart(this)).setPos(12.0f, 20.0f, 2.0f);
            this.leftFrontLeg.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 0.0f, 112, 104);
            (this.leftFrontLegTip = new ModelPart(this)).setPos(0.0f, 20.0f, -1.0f);
            this.leftFrontLegTip.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 0.0f, 226, 138);
            this.leftFrontLeg.addChild(this.leftFrontLegTip);
            (this.leftFrontFoot = new ModelPart(this)).setPos(0.0f, 23.0f, 0.0f);
            this.leftFrontFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 0.0f, 144, 104);
            this.leftFrontLegTip.addChild(this.leftFrontFoot);
            (this.leftRearLeg = new ModelPart(this)).setPos(16.0f, 16.0f, 42.0f);
            this.leftRearLeg.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0.0f, 0, 0);
            (this.leftRearLegTip = new ModelPart(this)).setPos(0.0f, 32.0f, -4.0f);
            this.leftRearLegTip.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 0.0f, 196, 0);
            this.leftRearLeg.addChild(this.leftRearLegTip);
            (this.leftRearFoot = new ModelPart(this)).setPos(0.0f, 31.0f, 4.0f);
            this.leftRearFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 0.0f, 112, 0);
            this.leftRearLegTip.addChild(this.leftRearFoot);
            (this.rightWing = new ModelPart(this)).setPos(-12.0f, 5.0f, 2.0f);
            this.rightWing.addBox("bone", -56.0f, -4.0f, -4.0f, 56, 8, 8, 0.0f, 112, 88);
            this.rightWing.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 88);
            (this.rightWingTip = new ModelPart(this)).setPos(-56.0f, 0.0f, 0.0f);
            this.rightWingTip.addBox("bone", -56.0f, -2.0f, -2.0f, 56, 4, 4, 0.0f, 112, 136);
            this.rightWingTip.addBox("skin", -56.0f, 0.0f, 2.0f, 56, 0, 56, 0.0f, -56, 144);
            this.rightWing.addChild(this.rightWingTip);
            (this.rightFrontLeg = new ModelPart(this)).setPos(-12.0f, 20.0f, 2.0f);
            this.rightFrontLeg.addBox("main", -4.0f, -4.0f, -4.0f, 8, 24, 8, 0.0f, 112, 104);
            (this.rightFrontLegTip = new ModelPart(this)).setPos(0.0f, 20.0f, -1.0f);
            this.rightFrontLegTip.addBox("main", -3.0f, -1.0f, -3.0f, 6, 24, 6, 0.0f, 226, 138);
            this.rightFrontLeg.addChild(this.rightFrontLegTip);
            (this.rightFrontFoot = new ModelPart(this)).setPos(0.0f, 23.0f, 0.0f);
            this.rightFrontFoot.addBox("main", -4.0f, 0.0f, -12.0f, 8, 4, 16, 0.0f, 144, 104);
            this.rightFrontLegTip.addChild(this.rightFrontFoot);
            (this.rightRearLeg = new ModelPart(this)).setPos(-16.0f, 16.0f, 42.0f);
            this.rightRearLeg.addBox("main", -8.0f, -4.0f, -8.0f, 16, 32, 16, 0.0f, 0, 0);
            (this.rightRearLegTip = new ModelPart(this)).setPos(0.0f, 32.0f, -4.0f);
            this.rightRearLegTip.addBox("main", -6.0f, -2.0f, 0.0f, 12, 32, 12, 0.0f, 196, 0);
            this.rightRearLeg.addChild(this.rightRearLegTip);
            (this.rightRearFoot = new ModelPart(this)).setPos(0.0f, 31.0f, 4.0f);
            this.rightRearFoot.addBox("main", -9.0f, 0.0f, -20.0f, 18, 6, 24, 0.0f, 112, 0);
            this.rightRearLegTip.addChild(this.rightRearFoot);
        }
        
        @Override
        public void prepareMobModel(final EnderDragon bbo, final float float2, final float float3, final float float4) {
            this.entity = bbo;
            this.a = float4;
        }
        
        @Override
        public void setupAnim(final EnderDragon bbo, final float float2, final float float3, final float float4, final float float5, final float float6) {
        }
        
        @Override
        public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
            dfj.pushPose();
            final float float9 = Mth.lerp(this.a, this.entity.oFlapTime, this.entity.flapTime);
            this.jaw.xRot = (float)(Math.sin((double)(float9 * 6.2831855f)) + 1.0) * 0.2f;
            float float10 = (float)(Math.sin((double)(float9 * 6.2831855f - 1.0f)) + 1.0);
            float10 = (float10 * float10 + float10 * 2.0f) * 0.05f;
            dfj.translate(0.0, float10 - 2.0f, -3.0);
            dfj.mulPose(Vector3f.XP.rotationDegrees(float10 * 2.0f));
            float float11 = 0.0f;
            float float12 = 20.0f;
            float float13 = -12.0f;
            final float float14 = 1.5f;
            double[] arr16 = this.entity.getLatencyPos(6, this.a);
            final float float15 = Mth.rotWrap(this.entity.getLatencyPos(5, this.a)[0] - this.entity.getLatencyPos(10, this.a)[0]);
            final float float16 = Mth.rotWrap(this.entity.getLatencyPos(5, this.a)[0] + float15 / 2.0f);
            float float17 = float9 * 6.2831855f;
            for (int integer5 = 0; integer5 < 5; ++integer5) {
                final double[] arr17 = this.entity.getLatencyPos(5 - integer5, this.a);
                final float float18 = (float)Math.cos((double)(integer5 * 0.45f + float17)) * 0.15f;
                this.neck.yRot = Mth.rotWrap(arr17[0] - arr16[0]) * 0.017453292f * 1.5f;
                this.neck.xRot = float18 + this.entity.getHeadPartYOffset(integer5, arr16, arr17) * 0.017453292f * 1.5f * 5.0f;
                this.neck.zRot = -Mth.rotWrap(arr17[0] - float16) * 0.017453292f * 1.5f;
                this.neck.y = float12;
                this.neck.z = float13;
                this.neck.x = float11;
                float12 += (float)(Math.sin((double)this.neck.xRot) * 10.0);
                float13 -= (float)(Math.cos((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0);
                float11 -= (float)(Math.sin((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0);
                this.neck.render(dfj, dfn, integer3, integer4);
            }
            this.head.y = float12;
            this.head.z = float13;
            this.head.x = float11;
            double[] arr18 = this.entity.getLatencyPos(0, this.a);
            this.head.yRot = Mth.rotWrap(arr18[0] - arr16[0]) * 0.017453292f;
            this.head.xRot = Mth.rotWrap(this.entity.getHeadPartYOffset(6, arr16, arr18)) * 0.017453292f * 1.5f * 5.0f;
            this.head.zRot = -Mth.rotWrap(arr18[0] - float16) * 0.017453292f;
            this.head.render(dfj, dfn, integer3, integer4);
            dfj.pushPose();
            dfj.translate(0.0, 1.0, 0.0);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(-float15 * 1.5f));
            dfj.translate(0.0, -1.0, 0.0);
            this.body.zRot = 0.0f;
            this.body.render(dfj, dfn, integer3, integer4);
            final float float19 = float9 * 6.2831855f;
            this.leftWing.xRot = 0.125f - (float)Math.cos((double)float19) * 0.2f;
            this.leftWing.yRot = -0.25f;
            this.leftWing.zRot = -(float)(Math.sin((double)float19) + 0.125) * 0.8f;
            this.leftWingTip.zRot = (float)(Math.sin((double)(float19 + 2.0f)) + 0.5) * 0.75f;
            this.rightWing.xRot = this.leftWing.xRot;
            this.rightWing.yRot = -this.leftWing.yRot;
            this.rightWing.zRot = -this.leftWing.zRot;
            this.rightWingTip.zRot = -this.leftWingTip.zRot;
            this.renderSide(dfj, dfn, integer3, integer4, float10, this.leftWing, this.leftFrontLeg, this.leftFrontLegTip, this.leftFrontFoot, this.leftRearLeg, this.leftRearLegTip, this.leftRearFoot);
            this.renderSide(dfj, dfn, integer3, integer4, float10, this.rightWing, this.rightFrontLeg, this.rightFrontLegTip, this.rightFrontFoot, this.rightRearLeg, this.rightRearLegTip, this.rightRearFoot);
            dfj.popPose();
            float float18 = -(float)Math.sin((double)(float9 * 6.2831855f)) * 0.0f;
            float17 = float9 * 6.2831855f;
            float12 = 10.0f;
            float13 = 60.0f;
            float11 = 0.0f;
            arr16 = this.entity.getLatencyPos(11, this.a);
            for (int integer6 = 0; integer6 < 12; ++integer6) {
                arr18 = this.entity.getLatencyPos(12 + integer6, this.a);
                float18 += (float)(Math.sin((double)(integer6 * 0.45f + float17)) * 0.05000000074505806);
                this.neck.yRot = (Mth.rotWrap(arr18[0] - arr16[0]) * 1.5f + 180.0f) * 0.017453292f;
                this.neck.xRot = float18 + (float)(arr18[1] - arr16[1]) * 0.017453292f * 1.5f * 5.0f;
                this.neck.zRot = Mth.rotWrap(arr18[0] - float16) * 0.017453292f * 1.5f;
                this.neck.y = float12;
                this.neck.z = float13;
                this.neck.x = float11;
                float12 += (float)(Math.sin((double)this.neck.xRot) * 10.0);
                float13 -= (float)(Math.cos((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0);
                float11 -= (float)(Math.sin((double)this.neck.yRot) * Math.cos((double)this.neck.xRot) * 10.0);
                this.neck.render(dfj, dfn, integer3, integer4);
            }
            dfj.popPose();
        }
        
        private void renderSide(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final ModelPart dwf6, final ModelPart dwf7, final ModelPart dwf8, final ModelPart dwf9, final ModelPart dwf10, final ModelPart dwf11, final ModelPart dwf12) {
            dwf10.xRot = 1.0f + float5 * 0.1f;
            dwf11.xRot = 0.5f + float5 * 0.1f;
            dwf12.xRot = 0.75f + float5 * 0.1f;
            dwf7.xRot = 1.3f + float5 * 0.1f;
            dwf8.xRot = -0.5f - float5 * 0.1f;
            dwf9.xRot = 0.75f + float5 * 0.1f;
            dwf6.render(dfj, dfn, integer3, integer4);
            dwf7.render(dfj, dfn, integer3, integer4);
            dwf10.render(dfj, dfn, integer3, integer4);
        }
    }
}
