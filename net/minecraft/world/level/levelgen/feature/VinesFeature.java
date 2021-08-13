package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class VinesFeature extends Feature<NoneFeatureConfiguration> {
    private static final Direction[] DIRECTIONS;
    
    public VinesFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final BlockPos.MutableBlockPos a7 = fx.mutable();
        for (int integer8 = 64; integer8 < 256; ++integer8) {
            a7.set(fx);
            a7.move(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
            a7.setY(integer8);
            if (bso.isEmptyBlock(a7)) {
                for (final Direction gc12 : VinesFeature.DIRECTIONS) {
                    if (gc12 != Direction.DOWN) {
                        if (VineBlock.isAcceptableNeighbour(bso, a7, gc12)) {
                            bso.setBlock(a7, ((StateHolder<O, BlockState>)Blocks.VINE.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)VineBlock.getPropertyForFace(gc12), true), 2);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    static {
        DIRECTIONS = Direction.values();
    }
}
