package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Quaternion;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BoatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatRenderer extends EntityRenderer<Boat> {
    private static final ResourceLocation[] BOAT_TEXTURE_LOCATIONS;
    protected final BoatModel model;
    
    public BoatRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new BoatModel();
        this.shadowRadius = 0.8f;
    }
    
    @Override
    public void render(final Boat bhk, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.translate(0.0, 0.375, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float2));
        final float float4 = bhk.getHurtTime() - float3;
        float float5 = bhk.getDamage() - float3;
        if (float5 < 0.0f) {
            float5 = 0.0f;
        }
        if (float4 > 0.0f) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(float4) * float4 * float5 / 10.0f * bhk.getHurtDir()));
        }
        final float float6 = bhk.getBubbleAngle(float3);
        if (!Mth.equal(float6, 0.0f)) {
            dfj.mulPose(new Quaternion(new Vector3f(1.0f, 0.0f, 1.0f), bhk.getBubbleAngle(float3), true));
        }
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        this.model.setupAnim(bhk, float3, 0.0f, -0.1f, 0.0f, 0.0f);
        final VertexConsumer dfn11 = dzy.getBuffer(this.model.renderType(this.getTextureLocation(bhk)));
        this.model.renderToBuffer(dfj, dfn11, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        if (!bhk.isUnderWater()) {
            final VertexConsumer dfn12 = dzy.getBuffer(RenderType.waterMask());
            this.model.waterPatch().render(dfj, dfn12, integer, OverlayTexture.NO_OVERLAY);
        }
        dfj.popPose();
        super.render(bhk, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Boat bhk) {
        return BoatRenderer.BOAT_TEXTURE_LOCATIONS[bhk.getBoatType().ordinal()];
    }
    
    static {
        BOAT_TEXTURE_LOCATIONS = new ResourceLocation[] { new ResourceLocation("textures/entity/boat/oak.png"), new ResourceLocation("textures/entity/boat/spruce.png"), new ResourceLocation("textures/entity/boat/birch.png"), new ResourceLocation("textures/entity/boat/jungle.png"), new ResourceLocation("textures/entity/boat/acacia.png"), new ResourceLocation("textures/entity/boat/dark_oak.png") };
    }
}
