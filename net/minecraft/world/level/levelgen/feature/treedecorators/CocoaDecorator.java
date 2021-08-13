package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Iterator;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public class CocoaDecorator extends TreeDecorator {
    public static final Codec<CocoaDecorator> CODEC;
    private final float probability;
    
    public CocoaDecorator(final float float1) {
        this.probability = float1;
    }
    
    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.COCOA;
    }
    
    @Override
    public void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx) {
        if (random.nextFloat() >= this.probability) {
            return;
        }
        final int integer8 = ((BlockPos)list3.get(0)).getY();
        list3.stream().filter(fx -> fx.getY() - integer8 <= 2).forEach(fx -> {
            for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() <= 0.25f) {
                    final Direction gc9 = gc8.getOpposite();
                    final BlockPos fx2 = fx.offset(gc9.getStepX(), 0, gc9.getStepZ());
                    if (!Feature.isAir(bso, fx2)) {
                        continue;
                    }
                    final BlockState cee11 = (((StateHolder<O, BlockState>)Blocks.COCOA.defaultBlockState()).setValue((Property<Comparable>)CocoaBlock.AGE, random.nextInt(3))).<Comparable, Direction>setValue((Property<Comparable>)CocoaBlock.FACING, gc8);
                    this.setBlock(bso, fx2, cee11, set, cqx);
                }
            }
        });
    }
    
    static {
        CODEC = Codec.floatRange(0.0f, 1.0f).fieldOf("probability").xmap(CocoaDecorator::new, col -> col.probability).codec();
    }
}
