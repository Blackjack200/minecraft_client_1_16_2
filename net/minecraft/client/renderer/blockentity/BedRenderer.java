package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BedPart;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.core.BlockPos;
import java.util.function.BiPredicate;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Function;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.level.block.entity.BedBlockEntity;

public class BedRenderer extends BlockEntityRenderer<BedBlockEntity> {
    private final ModelPart headPiece;
    private final ModelPart footPiece;
    private final ModelPart[] legs;
    
    public BedRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.legs = new ModelPart[4];
        (this.headPiece = new ModelPart(64, 64, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 6.0f, 0.0f);
        (this.footPiece = new ModelPart(64, 64, 0, 22)).addBox(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 6.0f, 0.0f);
        this.legs[0] = new ModelPart(64, 64, 50, 0);
        this.legs[1] = new ModelPart(64, 64, 50, 6);
        this.legs[2] = new ModelPart(64, 64, 50, 12);
        this.legs[3] = new ModelPart(64, 64, 50, 18);
        this.legs[0].addBox(0.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f);
        this.legs[1].addBox(0.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f);
        this.legs[2].addBox(-16.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f);
        this.legs[3].addBox(-16.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f);
        this.legs[0].xRot = 1.5707964f;
        this.legs[1].xRot = 1.5707964f;
        this.legs[2].xRot = 1.5707964f;
        this.legs[3].xRot = 1.5707964f;
        this.legs[0].zRot = 0.0f;
        this.legs[1].zRot = 1.5707964f;
        this.legs[2].zRot = 4.712389f;
        this.legs[3].zRot = 3.1415927f;
    }
    
    @Override
    public void render(final BedBlockEntity ccc, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final Material elj8 = Sheets.BED_TEXTURES[ccc.getColor().getId()];
        final Level bru9 = ccc.getLevel();
        if (bru9 != null) {
            final BlockState cee10 = ccc.getBlockState();
            final DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> c11 = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, (Function<BlockState, DoubleBlockCombiner.BlockType>)BedBlock::getBlockType, (Function<BlockState, Direction>)BedBlock::getConnectedDirection, ChestBlock.FACING, cee10, bru9, ccc.getBlockPos(), (BiPredicate<LevelAccessor, BlockPos>)((brv, fx) -> false));
            final int integer7 = c11.<Int2IntFunction>apply(new BrightnessCombiner<>()).get(integer5);
            this.renderPiece(dfj, dzy, cee10.<BedPart>getValue(BedBlock.PART) == BedPart.HEAD, cee10.<Direction>getValue((Property<Direction>)BedBlock.FACING), elj8, integer7, integer6, false);
        }
        else {
            this.renderPiece(dfj, dzy, true, Direction.SOUTH, elj8, integer5, integer6, false);
            this.renderPiece(dfj, dzy, false, Direction.SOUTH, elj8, integer5, integer6, true);
        }
    }
    
    private void renderPiece(final PoseStack dfj, final MultiBufferSource dzy, final boolean boolean3, final Direction gc, final Material elj, final int integer6, final int integer7, final boolean boolean8) {
        this.headPiece.visible = boolean3;
        this.footPiece.visible = !boolean3;
        this.legs[0].visible = !boolean3;
        this.legs[1].visible = boolean3;
        this.legs[2].visible = !boolean3;
        this.legs[3].visible = boolean3;
        dfj.pushPose();
        dfj.translate(0.0, 0.5625, boolean8 ? -1.0 : 0.0);
        dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        dfj.translate(0.5, 0.5, 0.5);
        dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f + gc.toYRot()));
        dfj.translate(-0.5, -0.5, -0.5);
        final VertexConsumer dfn10 = elj.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.headPiece.render(dfj, dfn10, integer6, integer7);
        this.footPiece.render(dfj, dfn10, integer6, integer7);
        this.legs[0].render(dfj, dfn10, integer6, integer7);
        this.legs[1].render(dfj, dfn10, integer6, integer7);
        this.legs[2].render(dfj, dfn10, integer6, integer7);
        this.legs[3].render(dfj, dfn10, integer6, integer7);
        dfj.popPose();
    }
}
