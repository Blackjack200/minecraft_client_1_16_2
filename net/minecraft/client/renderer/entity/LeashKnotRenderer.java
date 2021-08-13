package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.LeashKnotModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;

public class LeashKnotRenderer extends EntityRenderer<LeashFenceKnotEntity> {
    private static final ResourceLocation KNOT_LOCATION;
    private final LeashKnotModel<LeashFenceKnotEntity> model;
    
    public LeashKnotRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new LeashKnotModel<LeashFenceKnotEntity>();
    }
    
    @Override
    public void render(final LeashFenceKnotEntity bcn, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.scale(-1.0f, -1.0f, 1.0f);
        this.model.setupAnim(bcn, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        final VertexConsumer dfn8 = dzy.getBuffer(this.model.renderType(LeashKnotRenderer.KNOT_LOCATION));
        this.model.renderToBuffer(dfj, dfn8, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
        super.render(bcn, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final LeashFenceKnotEntity bcn) {
        return LeashKnotRenderer.KNOT_LOCATION;
    }
    
    static {
        KNOT_LOCATION = new ResourceLocation("textures/entity/lead_knot.png");
    }
}
