package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BlueIceFeature extends Feature<NoneFeatureConfiguration> {
    public BlueIceFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        if (fx.getY() > bso.getSeaLevel() - 1) {
            return false;
        }
        if (!bso.getBlockState(fx).is(Blocks.WATER) && !bso.getBlockState(fx.below()).is(Blocks.WATER)) {
            return false;
        }
        boolean boolean7 = false;
        for (final Direction gc11 : Direction.values()) {
            if (gc11 != Direction.DOWN) {
                if (bso.getBlockState(fx.relative(gc11)).is(Blocks.PACKED_ICE)) {
                    boolean7 = true;
                    break;
                }
            }
        }
        if (!boolean7) {
            return false;
        }
        bso.setBlock(fx, Blocks.BLUE_ICE.defaultBlockState(), 2);
        for (int integer8 = 0; integer8 < 200; ++integer8) {
            final int integer9 = random.nextInt(5) - random.nextInt(6);
            int integer10 = 3;
            if (integer9 < 2) {
                integer10 += integer9 / 2;
            }
            if (integer10 >= 1) {
                final BlockPos fx2 = fx.offset(random.nextInt(integer10) - random.nextInt(integer10), integer9, random.nextInt(integer10) - random.nextInt(integer10));
                final BlockState cee12 = bso.getBlockState(fx2);
                if (cee12.getMaterial() == Material.AIR || cee12.is(Blocks.WATER) || cee12.is(Blocks.PACKED_ICE) || cee12.is(Blocks.ICE)) {
                    for (final Direction gc12 : Direction.values()) {
                        final BlockState cee13 = bso.getBlockState(fx2.relative(gc12));
                        if (cee13.is(Blocks.BLUE_ICE)) {
                            bso.setBlock(fx2, Blocks.BLUE_ICE.defaultBlockState(), 2);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
}
