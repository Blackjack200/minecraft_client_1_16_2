package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public class LeaveVineDecorator extends TreeDecorator {
    public static final Codec<LeaveVineDecorator> CODEC;
    public static final LeaveVineDecorator INSTANCE;
    
    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.LEAVE_VINE;
    }
    
    @Override
    public void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx) {
        list4.forEach(fx -> {
            if (random.nextInt(4) == 0) {
                final BlockPos fx2 = fx.west();
                if (Feature.isAir(bso, fx2)) {
                    this.addHangingVine(bso, fx2, VineBlock.EAST, set, cqx);
                }
            }
            if (random.nextInt(4) == 0) {
                final BlockPos fx2 = fx.east();
                if (Feature.isAir(bso, fx2)) {
                    this.addHangingVine(bso, fx2, VineBlock.WEST, set, cqx);
                }
            }
            if (random.nextInt(4) == 0) {
                final BlockPos fx2 = fx.north();
                if (Feature.isAir(bso, fx2)) {
                    this.addHangingVine(bso, fx2, VineBlock.SOUTH, set, cqx);
                }
            }
            if (random.nextInt(4) == 0) {
                final BlockPos fx2 = fx.south();
                if (Feature.isAir(bso, fx2)) {
                    this.addHangingVine(bso, fx2, VineBlock.NORTH, set, cqx);
                }
            }
        });
    }
    
    private void addHangingVine(final LevelSimulatedRW bry, BlockPos fx, final BooleanProperty cev, final Set<BlockPos> set, final BoundingBox cqx) {
        this.placeVine(bry, fx, cev, set, cqx);
        int integer7;
        for (integer7 = 4, fx = fx.below(); Feature.isAir(bry, fx) && integer7 > 0; fx = fx.below(), --integer7) {
            this.placeVine(bry, fx, cev, set, cqx);
        }
    }
    
    static {
        CODEC = Codec.unit(() -> LeaveVineDecorator.INSTANCE);
        INSTANCE = new LeaveVineDecorator();
    }
}
