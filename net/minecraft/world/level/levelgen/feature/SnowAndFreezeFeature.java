package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
    public SnowAndFreezeFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        for (int integer9 = 0; integer9 < 16; ++integer9) {
            for (int integer10 = 0; integer10 < 16; ++integer10) {
                final int integer11 = fx.getX() + integer9;
                final int integer12 = fx.getZ() + integer10;
                final int integer13 = bso.getHeight(Heightmap.Types.MOTION_BLOCKING, integer11, integer12);
                a7.set(integer11, integer13, integer12);
                a8.set(a7).move(Direction.DOWN, 1);
                final Biome bss14 = bso.getBiome(a7);
                if (bss14.shouldFreeze(bso, a8, false)) {
                    bso.setBlock(a8, Blocks.ICE.defaultBlockState(), 2);
                }
                if (bss14.shouldSnow(bso, a7)) {
                    bso.setBlock(a7, Blocks.SNOW.defaultBlockState(), 2);
                    final BlockState cee15 = bso.getBlockState(a8);
                    if (cee15.<Comparable>hasProperty((Property<Comparable>)SnowyDirtBlock.SNOWY)) {
                        bso.setBlock(a8, ((StateHolder<O, BlockState>)cee15).<Comparable, Boolean>setValue((Property<Comparable>)SnowyDirtBlock.SNOWY, true), 2);
                    }
                }
            }
        }
        return true;
    }
}
