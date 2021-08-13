package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;

public class FillLayerFeature extends Feature<LayerConfiguration> {
    public FillLayerFeature(final Codec<LayerConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final LayerConfiguration cma) {
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        for (int integer8 = 0; integer8 < 16; ++integer8) {
            for (int integer9 = 0; integer9 < 16; ++integer9) {
                final int integer10 = fx.getX() + integer8;
                final int integer11 = fx.getZ() + integer9;
                final int integer12 = cma.height;
                a7.set(integer10, integer12, integer11);
                if (bso.getBlockState(a7).isAir()) {
                    bso.setBlock(a7, cma.state, 2);
                }
            }
        }
        return true;
    }
}
