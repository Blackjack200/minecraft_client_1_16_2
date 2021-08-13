package net.minecraft.world.level.lighting;

import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.commons.lang3.mutable.MutableInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public final class BlockLightEngine extends LayerLightEngine<BlockLightSectionStorage.BlockDataLayerStorageMap, BlockLightSectionStorage> {
    private static final Direction[] DIRECTIONS;
    private final BlockPos.MutableBlockPos pos;
    
    public BlockLightEngine(final LightChunkGetter cgg) {
        super(cgg, LightLayer.BLOCK, new BlockLightSectionStorage(cgg));
        this.pos = new BlockPos.MutableBlockPos();
    }
    
    private int getLightEmission(final long long1) {
        final int integer4 = BlockPos.getX(long1);
        final int integer5 = BlockPos.getY(long1);
        final int integer6 = BlockPos.getZ(long1);
        final BlockGetter bqz7 = this.chunkSource.getChunkForLighting(integer4 >> 4, integer6 >> 4);
        if (bqz7 != null) {
            return bqz7.getLightEmission(this.pos.set(integer4, integer5, integer6));
        }
        return 0;
    }
    
    @Override
    protected int computeLevelFromNeighbor(final long long1, final long long2, final int integer) {
        if (long2 == Long.MAX_VALUE) {
            return 15;
        }
        if (long1 == Long.MAX_VALUE) {
            return integer + 15 - this.getLightEmission(long2);
        }
        if (integer >= 15) {
            return integer;
        }
        final int integer2 = Integer.signum(BlockPos.getX(long2) - BlockPos.getX(long1));
        final int integer3 = Integer.signum(BlockPos.getY(long2) - BlockPos.getY(long1));
        final int integer4 = Integer.signum(BlockPos.getZ(long2) - BlockPos.getZ(long1));
        final Direction gc10 = Direction.fromNormal(integer2, integer3, integer4);
        if (gc10 == null) {
            return 15;
        }
        final MutableInt mutableInt11 = new MutableInt();
        final BlockState cee12 = this.getStateAndOpacity(long2, mutableInt11);
        if (mutableInt11.getValue() >= 15) {
            return 15;
        }
        final BlockState cee13 = this.getStateAndOpacity(long1, null);
        final VoxelShape dde14 = this.getShape(cee13, long1, gc10);
        final VoxelShape dde15 = this.getShape(cee12, long2, gc10.getOpposite());
        if (Shapes.faceShapeOccludes(dde14, dde15)) {
            return 15;
        }
        return integer + Math.max(1, (int)mutableInt11.getValue());
    }
    
    @Override
    protected void checkNeighborsAfterUpdate(final long long1, final int integer, final boolean boolean3) {
        final long long2 = SectionPos.blockToSection(long1);
        for (final Direction gc11 : BlockLightEngine.DIRECTIONS) {
            final long long3 = BlockPos.offset(long1, gc11);
            final long long4 = SectionPos.blockToSection(long3);
            if (long2 == long4 || ((BlockLightSectionStorage)this.storage).storingLightForSection(long4)) {
                this.checkNeighbor(long1, long3, integer, boolean3);
            }
        }
    }
    
    @Override
    protected int getComputedLevel(final long long1, final long long2, final int integer) {
        int integer2 = integer;
        if (Long.MAX_VALUE != long2) {
            final int integer3 = this.computeLevelFromNeighbor(Long.MAX_VALUE, long1, 0);
            if (integer2 > integer3) {
                integer2 = integer3;
            }
            if (integer2 == 0) {
                return integer2;
            }
        }
        final long long3 = SectionPos.blockToSection(long1);
        final DataLayer cfy10 = ((BlockLightSectionStorage)this.storage).getDataLayer(long3, true);
        for (final Direction gc14 : BlockLightEngine.DIRECTIONS) {
            final long long4 = BlockPos.offset(long1, gc14);
            if (long4 != long2) {
                final long long5 = SectionPos.blockToSection(long4);
                DataLayer cfy11;
                if (long3 == long5) {
                    cfy11 = cfy10;
                }
                else {
                    cfy11 = ((BlockLightSectionStorage)this.storage).getDataLayer(long5, true);
                }
                if (cfy11 != null) {
                    final int integer4 = this.computeLevelFromNeighbor(long4, long1, this.getLevel(cfy11, long4));
                    if (integer2 > integer4) {
                        integer2 = integer4;
                    }
                    if (integer2 == 0) {
                        return integer2;
                    }
                }
            }
        }
        return integer2;
    }
    
    @Override
    public void onBlockEmissionIncrease(final BlockPos fx, final int integer) {
        ((BlockLightSectionStorage)this.storage).runAllUpdates();
        this.checkEdge(Long.MAX_VALUE, fx.asLong(), 15 - integer, true);
    }
    
    static {
        DIRECTIONS = Direction.values();
    }
}
