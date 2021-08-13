package net.minecraft.world.level.levelgen.surfacebuilders;

import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.ImmutableMap;

public abstract class NetherCappedSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    private long seed;
    private ImmutableMap<BlockState, PerlinNoise> floorNoises;
    private ImmutableMap<BlockState, PerlinNoise> ceilingNoises;
    private PerlinNoise patchNoise;
    
    public NetherCappedSurfaceBuilder(final Codec<SurfaceBuilderBaseConfiguration> codec) {
        super(codec);
        this.floorNoises = (ImmutableMap<BlockState, PerlinNoise>)ImmutableMap.of();
        this.ceilingNoises = (ImmutableMap<BlockState, PerlinNoise>)ImmutableMap.of();
    }
    
    @Override
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final SurfaceBuilderBaseConfiguration ctr) {
        final int integer11 = integer10 + 1;
        final int integer12 = integer4 & 0xF;
        final int integer13 = integer5 & 0xF;
        final int integer14 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final int integer15 = (int)(double7 / 3.0 + 3.0 + random.nextDouble() * 0.25);
        final double double8 = 0.03125;
        final boolean boolean23 = this.patchNoise.getValue(integer4 * 0.03125, 109.0, integer5 * 0.03125) * 75.0 + random.nextDouble() > 0.0;
        final BlockState cee10 = (BlockState)((Map.Entry)this.ceilingNoises.entrySet().stream().max(Comparator.comparing(entry -> ((PerlinNoise)entry.getValue()).getValue(integer4, integer10, integer5))).get()).getKey();
        final BlockState cee11 = (BlockState)((Map.Entry)this.floorNoises.entrySet().stream().max(Comparator.comparing(entry -> ((PerlinNoise)entry.getValue()).getValue(integer4, integer10, integer5))).get()).getKey();
        final BlockPos.MutableBlockPos a26 = new BlockPos.MutableBlockPos();
        BlockState cee12 = cft.getBlockState(a26.set(integer12, 128, integer13));
        for (int integer16 = 127; integer16 >= 0; --integer16) {
            a26.set(integer12, integer16, integer13);
            final BlockState cee13 = cft.getBlockState(a26);
            if (cee12.is(cee8.getBlock()) && (cee13.isAir() || cee13 == cee9)) {
                for (int integer17 = 0; integer17 < integer14; ++integer17) {
                    a26.move(Direction.UP);
                    if (!cft.getBlockState(a26).is(cee8.getBlock())) {
                        break;
                    }
                    cft.setBlockState(a26, cee10, false);
                }
                a26.set(integer12, integer16, integer13);
            }
            if ((cee12.isAir() || cee12 == cee9) && cee13.is(cee8.getBlock())) {
                for (int integer17 = 0; integer17 < integer15 && cft.getBlockState(a26).is(cee8.getBlock()); ++integer17) {
                    if (boolean23 && integer16 >= integer11 - 4 && integer16 <= integer11 + 1) {
                        cft.setBlockState(a26, this.getPatchBlockState(), false);
                    }
                    else {
                        cft.setBlockState(a26, cee11, false);
                    }
                    a26.move(Direction.DOWN);
                }
            }
            cee12 = cee13;
        }
    }
    
    @Override
    public void initNoise(final long long1) {
        if (this.seed != long1 || this.patchNoise == null || this.floorNoises.isEmpty() || this.ceilingNoises.isEmpty()) {
            this.floorNoises = initPerlinNoises(this.getFloorBlockStates(), long1);
            this.ceilingNoises = initPerlinNoises(this.getCeilingBlockStates(), long1 + this.floorNoises.size());
            this.patchNoise = new PerlinNoise(new WorldgenRandom(long1 + this.floorNoises.size() + this.ceilingNoises.size()), (List<Integer>)ImmutableList.of(0));
        }
        this.seed = long1;
    }
    
    private static ImmutableMap<BlockState, PerlinNoise> initPerlinNoises(final ImmutableList<BlockState> immutableList, long long2) {
        final ImmutableMap.Builder<BlockState, PerlinNoise> builder4 = (ImmutableMap.Builder<BlockState, PerlinNoise>)new ImmutableMap.Builder();
        for (final BlockState cee6 : immutableList) {
            builder4.put(cee6, new PerlinNoise(new WorldgenRandom(long2), (List<Integer>)ImmutableList.of((Object)(-4))));
            ++long2;
        }
        return (ImmutableMap<BlockState, PerlinNoise>)builder4.build();
    }
    
    protected abstract ImmutableList<BlockState> getFloorBlockStates();
    
    protected abstract ImmutableList<BlockState> getCeilingBlockStates();
    
    protected abstract BlockState getPatchBlockState();
}
