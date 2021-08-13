package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.projectile.ItemSupplier;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;

public class ThrownItemRenderer<T extends Entity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;
    
    public ThrownItemRenderer(final EntityRenderDispatcher eel, final ItemRenderer efg, final float float3, final boolean boolean4) {
        super(eel);
        this.itemRenderer = efg;
        this.scale = float3;
        this.fullBright = boolean4;
    }
    
    public ThrownItemRenderer(final EntityRenderDispatcher eel, final ItemRenderer efg) {
        this(eel, efg, 1.0f, false);
    }
    
    @Override
    protected int getBlockLightLevel(final T apx, final BlockPos fx) {
        return this.fullBright ? 15 : super.getBlockLightLevel(apx, fx);
    }
    
    @Override
    public void render(final T apx, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (((Entity)apx).tickCount < 2 && this.entityRenderDispatcher.camera.getEntity().distanceToSqr((Entity)apx) < 12.25) {
            return;
        }
        dfj.pushPose();
        dfj.scale(this.scale, this.scale, this.scale);
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        this.itemRenderer.renderStatic(((ItemSupplier)apx).getItem(), ItemTransforms.TransformType.GROUND, integer, OverlayTexture.NO_OVERLAY, dfj, dzy);
        dfj.popPose();
        super.render(apx, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Entity apx) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
