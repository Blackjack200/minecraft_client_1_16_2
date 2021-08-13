package net.minecraft.world.level.lighting;

import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableInt;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.core.Direction;

public abstract class LayerLightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>> extends DynamicGraphMinFixedPoint implements LayerLightEventListener {
    private static final Direction[] DIRECTIONS;
    protected final LightChunkGetter chunkSource;
    protected final LightLayer layer;
    protected final S storage;
    private boolean runningLightUpdates;
    protected final BlockPos.MutableBlockPos pos;
    private final long[] lastChunkPos;
    private final BlockGetter[] lastChunk;
    
    public LayerLightEngine(final LightChunkGetter cgg, final LightLayer bsc, final S cuk) {
        super(16, 256, 8192);
        this.pos = new BlockPos.MutableBlockPos();
        this.lastChunkPos = new long[2];
        this.lastChunk = new BlockGetter[2];
        this.chunkSource = cgg;
        this.layer = bsc;
        this.storage = cuk;
        this.clearCache();
    }
    
    @Override
    protected void checkNode(final long long1) {
        this.storage.runAllUpdates();
        if (this.storage.storingLightForSection(SectionPos.blockToSection(long1))) {
            super.checkNode(long1);
        }
    }
    
    @Nullable
    private BlockGetter getChunk(final int integer1, final int integer2) {
        final long long4 = ChunkPos.asLong(integer1, integer2);
        for (int integer3 = 0; integer3 < 2; ++integer3) {
            if (long4 == this.lastChunkPos[integer3]) {
                return this.lastChunk[integer3];
            }
        }
        final BlockGetter bqz6 = this.chunkSource.getChunkForLighting(integer1, integer2);
        for (int integer4 = 1; integer4 > 0; --integer4) {
            this.lastChunkPos[integer4] = this.lastChunkPos[integer4 - 1];
            this.lastChunk[integer4] = this.lastChunk[integer4 - 1];
        }
        this.lastChunkPos[0] = long4;
        return this.lastChunk[0] = bqz6;
    }
    
    private void clearCache() {
        Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
        Arrays.fill((Object[])this.lastChunk, null);
    }
    
    protected BlockState getStateAndOpacity(final long long1, @Nullable final MutableInt mutableInt) {
        if (long1 == Long.MAX_VALUE) {
            if (mutableInt != null) {
                mutableInt.setValue(0);
            }
            return Blocks.AIR.defaultBlockState();
        }
        final int integer5 = SectionPos.blockToSectionCoord(BlockPos.getX(long1));
        final int integer6 = SectionPos.blockToSectionCoord(BlockPos.getZ(long1));
        final BlockGetter bqz7 = this.getChunk(integer5, integer6);
        if (bqz7 == null) {
            if (mutableInt != null) {
                mutableInt.setValue(16);
            }
            return Blocks.BEDROCK.defaultBlockState();
        }
        this.pos.set(long1);
        final BlockState cee8 = bqz7.getBlockState(this.pos);
        final boolean boolean9 = cee8.canOcclude() && cee8.useShapeForLightOcclusion();
        if (mutableInt != null) {
            mutableInt.setValue(cee8.getLightBlock(this.chunkSource.getLevel(), this.pos));
        }
        return boolean9 ? cee8 : Blocks.AIR.defaultBlockState();
    }
    
    protected VoxelShape getShape(final BlockState cee, final long long2, final Direction gc) {
        return cee.canOcclude() ? cee.getFaceOcclusionShape(this.chunkSource.getLevel(), this.pos.set(long2), gc) : Shapes.empty();
    }
    
    public static int getLightBlockInto(final BlockGetter bqz, final BlockState cee2, final BlockPos fx3, final BlockState cee4, final BlockPos fx5, final Direction gc, final int integer) {
        final boolean boolean8 = cee2.canOcclude() && cee2.useShapeForLightOcclusion();
        final boolean boolean9 = cee4.canOcclude() && cee4.useShapeForLightOcclusion();
        if (!boolean8 && !boolean9) {
            return integer;
        }
        final VoxelShape dde10 = boolean8 ? cee2.getOcclusionShape(bqz, fx3) : Shapes.empty();
        final VoxelShape dde11 = boolean9 ? cee4.getOcclusionShape(bqz, fx5) : Shapes.empty();
        if (Shapes.mergedFaceOccludes(dde10, dde11, gc)) {
            return 16;
        }
        return integer;
    }
    
    @Override
    protected boolean isSource(final long long1) {
        return long1 == Long.MAX_VALUE;
    }
    
    @Override
    protected int getComputedLevel(final long long1, final long long2, final int integer) {
        return 0;
    }
    
    @Override
    protected int getLevel(final long long1) {
        if (long1 == Long.MAX_VALUE) {
            return 0;
        }
        return 15 - this.storage.getStoredLevel(long1);
    }
    
    protected int getLevel(final DataLayer cfy, final long long2) {
        return 15 - cfy.get(SectionPos.sectionRelative(BlockPos.getX(long2)), SectionPos.sectionRelative(BlockPos.getY(long2)), SectionPos.sectionRelative(BlockPos.getZ(long2)));
    }
    
    @Override
    protected void setLevel(final long long1, final int integer) {
        this.storage.setStoredLevel(long1, Math.min(15, 15 - integer));
    }
    
    @Override
    protected int computeLevelFromNeighbor(final long long1, final long long2, final int integer) {
        return 0;
    }
    
    public boolean hasLightWork() {
        return this.hasWork() || this.storage.hasWork() || this.storage.hasInconsistencies();
    }
    
    public int runUpdates(int integer, final boolean boolean2, final boolean boolean3) {
        if (!this.runningLightUpdates) {
            if (this.storage.hasWork()) {
                integer = this.storage.runUpdates(integer);
                if (integer == 0) {
                    return integer;
                }
            }
            this.storage.markNewInconsistencies(this, boolean2, boolean3);
        }
        this.runningLightUpdates = true;
        if (this.hasWork()) {
            integer = this.runUpdates(integer);
            this.clearCache();
            if (integer == 0) {
                return integer;
            }
        }
        this.runningLightUpdates = false;
        this.storage.swapSectionMap();
        return integer;
    }
    
    protected void queueSectionData(final long long1, @Nullable final DataLayer cfy, final boolean boolean3) {
        this.storage.queueSectionData(long1, cfy, boolean3);
    }
    
    @Nullable
    @Override
    public DataLayer getDataLayerData(final SectionPos gp) {
        return this.storage.getDataLayerData(gp.asLong());
    }
    
    @Override
    public int getLightValue(final BlockPos fx) {
        return this.storage.getLightValue(fx.asLong());
    }
    
    public String getDebugData(final long long1) {
        return new StringBuilder().append("").append(this.storage.getLevel(long1)).toString();
    }
    
    public void checkBlock(final BlockPos fx) {
        final long long3 = fx.asLong();
        this.checkNode(long3);
        for (final Direction gc8 : LayerLightEngine.DIRECTIONS) {
            this.checkNode(BlockPos.offset(long3, gc8));
        }
    }
    
    public void onBlockEmissionIncrease(final BlockPos fx, final int integer) {
    }
    
    public void updateSectionStatus(final SectionPos gp, final boolean boolean2) {
        this.storage.updateSectionStatus(gp.asLong(), boolean2);
    }
    
    public void enableLightSources(final ChunkPos bra, final boolean boolean2) {
        final long long4 = SectionPos.getZeroNode(SectionPos.asLong(bra.x, 0, bra.z));
        this.storage.enableLightSources(long4, boolean2);
    }
    
    public void retainData(final ChunkPos bra, final boolean boolean2) {
        final long long4 = SectionPos.getZeroNode(SectionPos.asLong(bra.x, 0, bra.z));
        this.storage.retainData(long4, boolean2);
    }
    
    static {
        DIRECTIONS = Direction.values();
    }
}
