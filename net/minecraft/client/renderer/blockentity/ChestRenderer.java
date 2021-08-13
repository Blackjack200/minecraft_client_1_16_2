package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.state.StateHolder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Calendar;
import net.minecraft.client.model.geom.ModelPart;

public class ChestRenderer<T extends BlockEntity> extends BlockEntityRenderer<T> {
    private final ModelPart lid;
    private final ModelPart bottom;
    private final ModelPart lock;
    private final ModelPart doubleLeftLid;
    private final ModelPart doubleLeftBottom;
    private final ModelPart doubleLeftLock;
    private final ModelPart doubleRightLid;
    private final ModelPart doubleRightBottom;
    private final ModelPart doubleRightLock;
    private boolean xmasTextures;
    
    public ChestRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        final Calendar calendar3 = Calendar.getInstance();
        if (calendar3.get(2) + 1 == 12 && calendar3.get(5) >= 24 && calendar3.get(5) <= 26) {
            this.xmasTextures = true;
        }
        (this.bottom = new ModelPart(64, 64, 0, 19)).addBox(1.0f, 0.0f, 1.0f, 14.0f, 10.0f, 14.0f, 0.0f);
        (this.lid = new ModelPart(64, 64, 0, 0)).addBox(1.0f, 0.0f, 0.0f, 14.0f, 5.0f, 14.0f, 0.0f);
        this.lid.y = 9.0f;
        this.lid.z = 1.0f;
        (this.lock = new ModelPart(64, 64, 0, 0)).addBox(7.0f, -1.0f, 15.0f, 2.0f, 4.0f, 1.0f, 0.0f);
        this.lock.y = 8.0f;
        (this.doubleLeftBottom = new ModelPart(64, 64, 0, 19)).addBox(1.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f, 0.0f);
        (this.doubleLeftLid = new ModelPart(64, 64, 0, 0)).addBox(1.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f, 0.0f);
        this.doubleLeftLid.y = 9.0f;
        this.doubleLeftLid.z = 1.0f;
        (this.doubleLeftLock = new ModelPart(64, 64, 0, 0)).addBox(15.0f, -1.0f, 15.0f, 1.0f, 4.0f, 1.0f, 0.0f);
        this.doubleLeftLock.y = 8.0f;
        (this.doubleRightBottom = new ModelPart(64, 64, 0, 19)).addBox(0.0f, 0.0f, 1.0f, 15.0f, 10.0f, 14.0f, 0.0f);
        (this.doubleRightLid = new ModelPart(64, 64, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 15.0f, 5.0f, 14.0f, 0.0f);
        this.doubleRightLid.y = 9.0f;
        this.doubleRightLid.z = 1.0f;
        (this.doubleRightLock = new ModelPart(64, 64, 0, 0)).addBox(0.0f, -1.0f, 15.0f, 1.0f, 4.0f, 1.0f, 0.0f);
        this.doubleRightLock.y = 8.0f;
    }
    
    @Override
    public void render(final T ccg, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final Level bru8 = ((BlockEntity)ccg).getLevel();
        final boolean boolean9 = bru8 != null;
        final BlockState cee10 = boolean9 ? ((BlockEntity)ccg).getBlockState() : ((StateHolder<O, BlockState>)Blocks.CHEST.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, Direction.SOUTH);
        final ChestType cew11 = cee10.<ChestType>hasProperty(ChestBlock.TYPE) ? cee10.<ChestType>getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        final Block bul12 = cee10.getBlock();
        if (!(bul12 instanceof AbstractChestBlock)) {
            return;
        }
        final AbstractChestBlock<?> btk13 = bul12;
        final boolean boolean10 = cew11 != ChestType.SINGLE;
        dfj.pushPose();
        final float float3 = cee10.<Direction>getValue((Property<Direction>)ChestBlock.FACING).toYRot();
        dfj.translate(0.5, 0.5, 0.5);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-float3));
        dfj.translate(-0.5, -0.5, -0.5);
        DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> c16;
        if (boolean9) {
            c16 = btk13.combine(cee10, bru8, ((BlockEntity)ccg).getBlockPos(), true);
        }
        else {
            c16 = DoubleBlockCombiner.Combiner::acceptNone;
        }
        float float4 = c16.<Float2FloatFunction>apply(ChestBlock.opennessCombiner((LidBlockEntity)ccg)).get(float2);
        float4 = 1.0f - float4;
        float4 = 1.0f - float4 * float4 * float4;
        final int integer7 = c16.<Int2IntFunction>apply(new BrightnessCombiner<>()).applyAsInt(integer5);
        final Material elj19 = Sheets.chooseMaterial((BlockEntity)ccg, cew11, this.xmasTextures);
        final VertexConsumer dfn20 = elj19.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityCutout);
        if (boolean10) {
            if (cew11 == ChestType.LEFT) {
                this.render(dfj, dfn20, this.doubleRightLid, this.doubleRightLock, this.doubleRightBottom, float4, integer7, integer6);
            }
            else {
                this.render(dfj, dfn20, this.doubleLeftLid, this.doubleLeftLock, this.doubleLeftBottom, float4, integer7, integer6);
            }
        }
        else {
            this.render(dfj, dfn20, this.lid, this.lock, this.bottom, float4, integer7, integer6);
        }
        dfj.popPose();
    }
    
    private void render(final PoseStack dfj, final VertexConsumer dfn, final ModelPart dwf3, final ModelPart dwf4, final ModelPart dwf5, final float float6, final int integer7, final int integer8) {
        dwf3.xRot = -(float6 * 1.5707964f);
        dwf4.xRot = dwf3.xRot;
        dwf3.render(dfj, dfn, integer7, integer8);
        dwf4.render(dfj, dfn, integer7, integer8);
        dwf5.render(dfj, dfn, integer7, integer8);
    }
}
