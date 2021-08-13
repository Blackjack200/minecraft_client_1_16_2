package net.minecraft.world.level.levelgen.feature.treedecorators;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;

public class BeehiveDecorator extends TreeDecorator {
    public static final Codec<BeehiveDecorator> CODEC;
    private final float probability;
    
    public BeehiveDecorator(final float float1) {
        this.probability = float1;
    }
    
    @Override
    protected TreeDecoratorType<?> type() {
        return TreeDecoratorType.BEEHIVE;
    }
    
    @Override
    public void place(final WorldGenLevel bso, final Random random, final List<BlockPos> list3, final List<BlockPos> list4, final Set<BlockPos> set, final BoundingBox cqx) {
        if (random.nextFloat() >= this.probability) {
            return;
        }
        final Direction gc8 = BeehiveBlock.getRandomOffset(random);
        final int integer9 = list4.isEmpty() ? Math.min(((BlockPos)list3.get(0)).getY() + 1 + random.nextInt(3), ((BlockPos)list3.get(list3.size() - 1)).getY()) : Math.max(((BlockPos)list4.get(0)).getY() - 1, ((BlockPos)list3.get(0)).getY());
        final List<BlockPos> list5 = (List<BlockPos>)list3.stream().filter(fx -> fx.getY() == integer9).collect(Collectors.toList());
        if (list5.isEmpty()) {
            return;
        }
        final BlockPos fx11 = (BlockPos)list5.get(random.nextInt(list5.size()));
        final BlockPos fx12 = fx11.relative(gc8);
        if (!Feature.isAir(bso, fx12) || !Feature.isAir(bso, fx12.relative(Direction.SOUTH))) {
            return;
        }
        final BlockState cee13 = ((StateHolder<O, BlockState>)Blocks.BEE_NEST.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BeehiveBlock.FACING, Direction.SOUTH);
        this.setBlock(bso, fx12, cee13, set, cqx);
        final BlockEntity ccg14 = bso.getBlockEntity(fx12);
        if (ccg14 instanceof BeehiveBlockEntity) {
            final BeehiveBlockEntity ccd15 = (BeehiveBlockEntity)ccg14;
            for (int integer10 = 2 + random.nextInt(2), integer11 = 0; integer11 < integer10; ++integer11) {
                final Bee azx18 = new Bee(EntityType.BEE, bso.getLevel());
                ccd15.addOccupantWithPresetTicks(azx18, false, random.nextInt(599));
            }
        }
    }
    
    static {
        CODEC = Codec.floatRange(0.0f, 1.0f).fieldOf("probability").xmap(BeehiveDecorator::new, cok -> cok.probability).codec();
    }
}
