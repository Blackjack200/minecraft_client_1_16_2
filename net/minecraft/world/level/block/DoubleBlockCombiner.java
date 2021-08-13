package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Function;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class DoubleBlockCombiner {
    public static <S extends BlockEntity> NeighborCombineResult<S> combineWithNeigbour(final BlockEntityType<S> cch, final Function<BlockState, BlockType> function2, final Function<BlockState, Direction> function3, final DirectionProperty cey, final BlockState cee, final LevelAccessor brv, final BlockPos fx, final BiPredicate<LevelAccessor, BlockPos> biPredicate) {
        final S ccg9 = cch.getBlockEntity(brv, fx);
        if (ccg9 == null) {
            return Combiner::acceptNone;
        }
        if (biPredicate.test(brv, fx)) {
            return Combiner::acceptNone;
        }
        final BlockType a10 = (BlockType)function2.apply(cee);
        final boolean boolean11 = a10 == BlockType.SINGLE;
        final boolean boolean12 = a10 == BlockType.FIRST;
        if (boolean11) {
            return new NeighborCombineResult.Single<S>(ccg9);
        }
        final BlockPos fx2 = fx.relative((Direction)function3.apply(cee));
        final BlockState cee2 = brv.getBlockState(fx2);
        if (cee2.is(cee.getBlock())) {
            final BlockType a11 = (BlockType)function2.apply(cee2);
            if (a11 != BlockType.SINGLE && a10 != a11 && cee2.<Comparable>getValue((Property<Comparable>)cey) == cee.<Comparable>getValue((Property<Comparable>)cey)) {
                if (biPredicate.test(brv, fx2)) {
                    return Combiner::acceptNone;
                }
                final S ccg10 = cch.getBlockEntity(brv, fx2);
                if (ccg10 != null) {
                    final S ccg11 = boolean12 ? ccg9 : ccg10;
                    final S ccg12 = boolean12 ? ccg10 : ccg9;
                    return new NeighborCombineResult.Double<S>(ccg11, ccg12);
                }
            }
        }
        return new NeighborCombineResult.Single<S>(ccg9);
    }
    
    public enum BlockType {
        SINGLE, 
        FIRST, 
        SECOND;
    }
    
    public interface NeighborCombineResult<S> {
         <T> T apply(final Combiner<? super S, T> b);
        
        public static final class Double<S> implements NeighborCombineResult<S> {
            private final S first;
            private final S second;
            
            public Double(final S object1, final S object2) {
                this.first = object1;
                this.second = object2;
            }
            
            public <T> T apply(final Combiner<? super S, T> b) {
                return b.acceptDouble(this.first, this.second);
            }
        }
        
        public static final class Single<S> implements NeighborCombineResult<S> {
            private final S single;
            
            public Single(final S object) {
                this.single = object;
            }
            
            public <T> T apply(final Combiner<? super S, T> b) {
                return b.acceptSingle(this.single);
            }
        }
    }
    
    public interface Combiner<S, T> {
        T acceptDouble(final S object1, final S object2);
        
        T acceptSingle(final S object);
        
        T acceptNone();
    }
}
