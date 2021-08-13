package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.TridentModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class ThrownTridentRenderer extends EntityRenderer<ThrownTrident> {
    public static final ResourceLocation TRIDENT_LOCATION;
    private final TridentModel model;
    
    public ThrownTridentRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new TridentModel();
    }
    
    @Override
    public void render(final ThrownTrident bgv, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(float3, bgv.yRotO, bgv.yRot) - 90.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(float3, bgv.xRotO, bgv.xRot) + 90.0f));
        final VertexConsumer dfn8 = ItemRenderer.getFoilBufferDirect(dzy, this.model.renderType(this.getTextureLocation(bgv)), false, bgv.isFoil());
        this.model.renderToBuffer(dfj, dfn8, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
        super.render(bgv, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ThrownTrident bgv) {
        return ThrownTridentRenderer.TRIDENT_LOCATION;
    }
    
    static {
        TRIDENT_LOCATION = new ResourceLocation("textures/entity/trident.png");
    }
}
