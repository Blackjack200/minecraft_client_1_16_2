package net.minecraft.client.renderer.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.item.Items;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;

public class ItemFrameRenderer extends EntityRenderer<ItemFrame> {
    private static final ModelResourceLocation FRAME_LOCATION;
    private static final ModelResourceLocation MAP_FRAME_LOCATION;
    private final Minecraft minecraft;
    private final ItemRenderer itemRenderer;
    
    public ItemFrameRenderer(final EntityRenderDispatcher eel, final ItemRenderer efg) {
        super(eel);
        this.minecraft = Minecraft.getInstance();
        this.itemRenderer = efg;
    }
    
    @Override
    public void render(final ItemFrame bcm, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        super.render(bcm, float2, float3, dfj, dzy, integer);
        dfj.pushPose();
        final Direction gc8 = bcm.getDirection();
        final Vec3 dck9 = this.getRenderOffset(bcm, float3);
        dfj.translate(-dck9.x(), -dck9.y(), -dck9.z());
        final double double10 = 0.46875;
        dfj.translate(gc8.getStepX() * 0.46875, gc8.getStepY() * 0.46875, gc8.getStepZ() * 0.46875);
        dfj.mulPose(Vector3f.XP.rotationDegrees(bcm.xRot));
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - bcm.yRot));
        final boolean boolean12 = bcm.isInvisible();
        if (!boolean12) {
            final BlockRenderDispatcher eap13 = this.minecraft.getBlockRenderer();
            final ModelManager ell14 = eap13.getBlockModelShaper().getModelManager();
            final ModelResourceLocation elm15 = (bcm.getItem().getItem() == Items.FILLED_MAP) ? ItemFrameRenderer.MAP_FRAME_LOCATION : ItemFrameRenderer.FRAME_LOCATION;
            dfj.pushPose();
            dfj.translate(-0.5, -0.5, -0.5);
            eap13.getModelRenderer().renderModel(dfj.last(), dzy.getBuffer(Sheets.solidBlockSheet()), null, ell14.getModel(elm15), 1.0f, 1.0f, 1.0f, integer, OverlayTexture.NO_OVERLAY);
            dfj.popPose();
        }
        final ItemStack bly13 = bcm.getItem();
        if (!bly13.isEmpty()) {
            final boolean boolean13 = bly13.getItem() == Items.FILLED_MAP;
            if (boolean12) {
                dfj.translate(0.0, 0.0, 0.5);
            }
            else {
                dfj.translate(0.0, 0.0, 0.4375);
            }
            final int integer2 = boolean13 ? (bcm.getRotation() % 4 * 2) : bcm.getRotation();
            dfj.mulPose(Vector3f.ZP.rotationDegrees(integer2 * 360.0f / 8.0f));
            if (boolean13) {
                dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
                final float float4 = 0.0078125f;
                dfj.scale(0.0078125f, 0.0078125f, 0.0078125f);
                dfj.translate(-64.0, -64.0, 0.0);
                final MapItemSavedData cxu17 = MapItem.getOrCreateSavedData(bly13, bcm.level);
                dfj.translate(0.0, 0.0, -1.0);
                if (cxu17 != null) {
                    this.minecraft.gameRenderer.getMapRenderer().render(dfj, dzy, cxu17, true, integer);
                }
            }
            else {
                dfj.scale(0.5f, 0.5f, 0.5f);
                this.itemRenderer.renderStatic(bly13, ItemTransforms.TransformType.FIXED, integer, OverlayTexture.NO_OVERLAY, dfj, dzy);
            }
        }
        dfj.popPose();
    }
    
    @Override
    public Vec3 getRenderOffset(final ItemFrame bcm, final float float2) {
        return new Vec3(bcm.getDirection().getStepX() * 0.3f, -0.25, bcm.getDirection().getStepZ() * 0.3f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ItemFrame bcm) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
    
    @Override
    protected boolean shouldShowName(final ItemFrame bcm) {
        if (!Minecraft.renderNames() || bcm.getItem().isEmpty() || !bcm.getItem().hasCustomHoverName() || this.entityRenderDispatcher.crosshairPickEntity != bcm) {
            return false;
        }
        final double double3 = this.entityRenderDispatcher.distanceToSqr(bcm);
        final float float5 = bcm.isDiscrete() ? 32.0f : 64.0f;
        return double3 < float5 * float5;
    }
    
    @Override
    protected void renderNameTag(final ItemFrame bcm, final Component nr, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        super.renderNameTag(bcm, bcm.getItem().getHoverName(), dfj, dzy, integer);
    }
    
    static {
        FRAME_LOCATION = new ModelResourceLocation("item_frame", "map=false");
        MAP_FRAME_LOCATION = new ModelResourceLocation("item_frame", "map=true");
    }
}
