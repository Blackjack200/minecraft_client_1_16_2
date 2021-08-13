package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.BlockAndTintGetter;
import java.util.Random;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;

public class PistonHeadRenderer extends BlockEntityRenderer<PistonMovingBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;
    
    public PistonHeadRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }
    
    @Override
    public void render(final PistonMovingBlockEntity cea, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final Level bru8 = cea.getLevel();
        if (bru8 == null) {
            return;
        }
        final BlockPos fx9 = cea.getBlockPos().relative(cea.getMovementDirection().getOpposite());
        BlockState cee10 = cea.getMovedState();
        if (cee10.isAir()) {
            return;
        }
        ModelBlockRenderer.enableCaching();
        dfj.pushPose();
        dfj.translate(cea.getXOff(float2), cea.getYOff(float2), cea.getZOff(float2));
        if (cee10.is(Blocks.PISTON_HEAD) && cea.getProgress(float2) <= 4.0f) {
            cee10 = ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)PistonHeadBlock.SHORT, cea.getProgress(float2) <= 0.5f);
            this.renderBlock(fx9, cee10, dfj, dzy, bru8, false, integer6);
        }
        else if (cea.isSourcePiston() && !cea.isExtending()) {
            final PistonType cff11 = cee10.is(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT;
            BlockState cee11 = (((StateHolder<O, BlockState>)Blocks.PISTON_HEAD.defaultBlockState()).setValue(PistonHeadBlock.TYPE, cff11)).<Comparable, Comparable>setValue((Property<Comparable>)PistonHeadBlock.FACING, (Comparable)cee10.<V>getValue((Property<V>)PistonBaseBlock.FACING));
            cee11 = ((StateHolder<O, BlockState>)cee11).<Comparable, Boolean>setValue((Property<Comparable>)PistonHeadBlock.SHORT, cea.getProgress(float2) >= 0.5f);
            this.renderBlock(fx9, cee11, dfj, dzy, bru8, false, integer6);
            final BlockPos fx10 = fx9.relative(cea.getMovementDirection());
            dfj.popPose();
            dfj.pushPose();
            cee10 = ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, true);
            this.renderBlock(fx10, cee10, dfj, dzy, bru8, true, integer6);
        }
        else {
            this.renderBlock(fx9, cee10, dfj, dzy, bru8, false, integer6);
        }
        dfj.popPose();
        ModelBlockRenderer.clearCache();
    }
    
    private void renderBlock(final BlockPos fx, final BlockState cee, final PoseStack dfj, final MultiBufferSource dzy, final Level bru, final boolean boolean6, final int integer) {
        final RenderType eag9 = ItemBlockRenderTypes.getMovingBlockRenderType(cee);
        final VertexConsumer dfn10 = dzy.getBuffer(eag9);
        this.blockRenderer.getModelRenderer().tesselateBlock(bru, this.blockRenderer.getBlockModel(cee), cee, fx, dfj, dfn10, boolean6, new Random(), cee.getSeed(fx), integer);
    }
}
