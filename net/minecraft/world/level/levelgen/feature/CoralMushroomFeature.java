package net.minecraft.world.level.levelgen.feature;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import com.mojang.serialization.Codec;

public class CoralMushroomFeature extends CoralFeature {
    public CoralMushroomFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean placeFeature(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee) {
        final int integer6 = random.nextInt(3) + 3;
        final int integer7 = random.nextInt(3) + 3;
        final int integer8 = random.nextInt(3) + 3;
        final int integer9 = random.nextInt(3) + 1;
        final BlockPos.MutableBlockPos a10 = fx.mutable();
        for (int integer10 = 0; integer10 <= integer7; ++integer10) {
            for (int integer11 = 0; integer11 <= integer6; ++integer11) {
                for (int integer12 = 0; integer12 <= integer8; ++integer12) {
                    a10.set(integer10 + fx.getX(), integer11 + fx.getY(), integer12 + fx.getZ());
                    a10.move(Direction.DOWN, integer9);
                    if (integer10 == 0 || integer10 == integer7) {
                        if (integer11 == 0) {
                            continue;
                        }
                        if (integer11 == integer6) {
                            continue;
                        }
                    }
                    if (integer12 == 0 || integer12 == integer8) {
                        if (integer11 == 0) {
                            continue;
                        }
                        if (integer11 == integer6) {
                            continue;
                        }
                    }
                    if (integer10 == 0 || integer10 == integer7) {
                        if (integer12 == 0) {
                            continue;
                        }
                        if (integer12 == integer8) {
                            continue;
                        }
                    }
                    if (integer10 == 0 || integer10 == integer7 || integer11 == 0 || integer11 == integer6 || integer12 == 0 || integer12 == integer8) {
                        if (random.nextFloat() >= 0.1f) {
                            if (!this.placeCoralBlock(brv, random, a10, cee)) {}
                        }
                    }
                }
            }
        }
        return true;
    }
}
