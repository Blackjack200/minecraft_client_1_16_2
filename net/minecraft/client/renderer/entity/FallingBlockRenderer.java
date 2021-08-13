package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.util.Random;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.item.FallingBlockEntity;

public class FallingBlockRenderer extends EntityRenderer<FallingBlockEntity> {
    public FallingBlockRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.shadowRadius = 0.5f;
    }
    
    @Override
    public void render(final FallingBlockEntity bcr, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final BlockState cee8 = bcr.getBlockState();
        if (cee8.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        final Level bru9 = bcr.getLevel();
        if (cee8 == bru9.getBlockState(bcr.blockPosition()) || cee8.getRenderShape() == RenderShape.INVISIBLE) {
            return;
        }
        dfj.pushPose();
        final BlockPos fx10 = new BlockPos(bcr.getX(), bcr.getBoundingBox().maxY, bcr.getZ());
        dfj.translate(-0.5, 0.0, -0.5);
        final BlockRenderDispatcher eap11 = Minecraft.getInstance().getBlockRenderer();
        eap11.getModelRenderer().tesselateBlock(bru9, eap11.getBlockModel(cee8), cee8, fx10, dfj, dzy.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(cee8)), false, new Random(), cee8.getSeed(bcr.getStartPos()), OverlayTexture.NO_OVERLAY);
        dfj.popPose();
        super.render(bcr, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final FallingBlockEntity bcr) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
