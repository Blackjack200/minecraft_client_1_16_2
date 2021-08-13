package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EvokerFangsModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.EvokerFangs;

public class EvokerFangsRenderer extends EntityRenderer<EvokerFangs> {
    private static final ResourceLocation TEXTURE_LOCATION;
    private final EvokerFangsModel<EvokerFangs> model;
    
    public EvokerFangsRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new EvokerFangsModel<EvokerFangs>();
    }
    
    @Override
    public void render(final EvokerFangs bgb, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final float float4 = bgb.getAnimationProgress(float3);
        if (float4 == 0.0f) {
            return;
        }
        float float5 = 2.0f;
        if (float4 > 0.9f) {
            float5 *= (float)((1.0 - float4) / 0.10000000149011612);
        }
        dfj.pushPose();
        dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f - bgb.yRot));
        dfj.scale(-float5, -float5, float5);
        final float float6 = 0.03125f;
        dfj.translate(0.0, -0.6259999871253967, 0.0);
        dfj.scale(0.5f, 0.5f, 0.5f);
        this.model.setupAnim(bgb, float4, 0.0f, 0.0f, bgb.yRot, bgb.xRot);
        final VertexConsumer dfn11 = dzy.getBuffer(this.model.renderType(EvokerFangsRenderer.TEXTURE_LOCATION));
        this.model.renderToBuffer(dfj, dfn11, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
        super.render(bgb, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final EvokerFangs bgb) {
        return EvokerFangsRenderer.TEXTURE_LOCATION;
    }
    
    static {
        TEXTURE_LOCATION = new ResourceLocation("textures/entity/illager/evoker_fangs.png");
    }
}
