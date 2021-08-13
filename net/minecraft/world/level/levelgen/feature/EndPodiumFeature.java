package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class EndPodiumFeature extends Feature<NoneFeatureConfiguration> {
    public static final BlockPos END_PODIUM_LOCATION;
    private final boolean active;
    
    public EndPodiumFeature(final boolean boolean1) {
        super(NoneFeatureConfiguration.CODEC);
        this.active = boolean1;
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        for (final BlockPos fx2 : BlockPos.betweenClosed(new BlockPos(fx.getX() - 4, fx.getY() - 1, fx.getZ() - 4), new BlockPos(fx.getX() + 4, fx.getY() + 32, fx.getZ() + 4))) {
            final boolean boolean9 = fx2.closerThan(fx, 2.5);
            if (boolean9 || fx2.closerThan(fx, 3.5)) {
                if (fx2.getY() < fx.getY()) {
                    if (boolean9) {
                        this.setBlock(bso, fx2, Blocks.BEDROCK.defaultBlockState());
                    }
                    else {
                        if (fx2.getY() >= fx.getY()) {
                            continue;
                        }
                        this.setBlock(bso, fx2, Blocks.END_STONE.defaultBlockState());
                    }
                }
                else if (fx2.getY() > fx.getY()) {
                    this.setBlock(bso, fx2, Blocks.AIR.defaultBlockState());
                }
                else if (!boolean9) {
                    this.setBlock(bso, fx2, Blocks.BEDROCK.defaultBlockState());
                }
                else if (this.active) {
                    this.setBlock(bso, new BlockPos(fx2), Blocks.END_PORTAL.defaultBlockState());
                }
                else {
                    this.setBlock(bso, new BlockPos(fx2), Blocks.AIR.defaultBlockState());
                }
            }
        }
        for (int integer7 = 0; integer7 < 4; ++integer7) {
            this.setBlock(bso, fx.above(integer7), Blocks.BEDROCK.defaultBlockState());
        }
        final BlockPos fx3 = fx.above(2);
        for (final Direction gc9 : Direction.Plane.HORIZONTAL) {
            this.setBlock(bso, fx3.relative(gc9), ((StateHolder<O, BlockState>)Blocks.WALL_TORCH.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, gc9));
        }
        return true;
    }
    
    static {
        END_PODIUM_LOCATION = BlockPos.ZERO;
    }
}
