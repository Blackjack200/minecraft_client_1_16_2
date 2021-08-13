package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;

public class FireworkEntityRenderer extends EntityRenderer<FireworkRocketEntity> {
    private final ItemRenderer itemRenderer;
    
    public FireworkEntityRenderer(final EntityRenderDispatcher eel, final ItemRenderer efg) {
        super(eel);
        this.itemRenderer = efg;
    }
    
    @Override
    public void render(final FireworkRocketEntity bge, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        if (bge.isShotAtAngle()) {
            dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
            dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        }
        this.itemRenderer.renderStatic(bge.getItem(), ItemTransforms.TransformType.GROUND, integer, OverlayTexture.NO_OVERLAY, dfj, dzy);
        dfj.popPose();
        super.render(bge, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final FireworkRocketEntity bge) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
