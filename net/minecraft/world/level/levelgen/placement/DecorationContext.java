package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ProtoChunk;
import java.util.BitSet;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;

public class DecorationContext {
    private final WorldGenLevel level;
    private final ChunkGenerator generator;
    
    public DecorationContext(final WorldGenLevel bso, final ChunkGenerator cfv) {
        this.level = bso;
        this.generator = cfv;
    }
    
    public int getHeight(final Heightmap.Types a, final int integer2, final int integer3) {
        return this.level.getHeight(a, integer2, integer3);
    }
    
    public int getGenDepth() {
        return this.generator.getGenDepth();
    }
    
    public int getSeaLevel() {
        return this.generator.getSeaLevel();
    }
    
    public BitSet getCarvingMask(final ChunkPos bra, final GenerationStep.Carving a) {
        return ((ProtoChunk)this.level.getChunk(bra.x, bra.z)).getOrCreateCarvingMask(a);
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        return this.level.getBlockState(fx);
    }
}
