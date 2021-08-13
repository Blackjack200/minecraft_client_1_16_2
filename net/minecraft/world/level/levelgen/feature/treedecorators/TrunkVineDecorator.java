package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public class TrunkVineDecorator extends TreeDecorator {
    public static final Codec<TrunkVineDecorator> CODEC;
    public static final TrunkVineDecorator INSTANCE;
    
    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.TRUNK_VINE;
    }
    
    @Override
    public void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx) {
        list3.forEach(fx -> {
            if (random.nextInt(3) > 0) {
                final BlockPos fx2 = fx.west();
                if (Feature.isAir(bso, fx2)) {
                    this.placeVine(bso, fx2, VineBlock.EAST, set, cqx);
                }
            }
            if (random.nextInt(3) > 0) {
                final BlockPos fx2 = fx.east();
                if (Feature.isAir(bso, fx2)) {
                    this.placeVine(bso, fx2, VineBlock.WEST, set, cqx);
                }
            }
            if (random.nextInt(3) > 0) {
                final BlockPos fx2 = fx.north();
                if (Feature.isAir(bso, fx2)) {
                    this.placeVine(bso, fx2, VineBlock.SOUTH, set, cqx);
                }
            }
            if (random.nextInt(3) > 0) {
                final BlockPos fx2 = fx.south();
                if (Feature.isAir(bso, fx2)) {
                    this.placeVine(bso, fx2, VineBlock.NORTH, set, cqx);
                }
            }
        });
    }
    
    static {
        CODEC = Codec.unit(() -> TrunkVineDecorator.INSTANCE);
        INSTANCE = new TrunkVineDecorator();
    }
}
