package net.minecraft.world.level.block.state.pattern;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Vec3i;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import com.google.common.cache.LoadingCache;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.function.Predicate;

public class BlockPattern {
    private final Predicate<BlockInWorld>[][][] pattern;
    private final int depth;
    private final int height;
    private final int width;
    
    public BlockPattern(final Predicate<BlockInWorld>[][][] arr) {
        this.pattern = arr;
        this.depth = arr.length;
        if (this.depth > 0) {
            this.height = arr[0].length;
            if (this.height > 0) {
                this.width = arr[0][0].length;
            }
            else {
                this.width = 0;
            }
        }
        else {
            this.height = 0;
            this.width = 0;
        }
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    @Nullable
    private BlockPatternMatch matches(final BlockPos fx, final Direction gc2, final Direction gc3, final LoadingCache<BlockPos, BlockInWorld> loadingCache) {
        for (int integer6 = 0; integer6 < this.width; ++integer6) {
            for (int integer7 = 0; integer7 < this.height; ++integer7) {
                for (int integer8 = 0; integer8 < this.depth; ++integer8) {
                    if (!this.pattern[integer8][integer7][integer6].test(loadingCache.getUnchecked(translateAndRotate(fx, gc2, gc3, integer6, integer7, integer8)))) {
                        return null;
                    }
                }
            }
        }
        return new BlockPatternMatch(fx, gc2, gc3, loadingCache, this.width, this.height, this.depth);
    }
    
    @Nullable
    public BlockPatternMatch find(final LevelReader brw, final BlockPos fx) {
        final LoadingCache<BlockPos, BlockInWorld> loadingCache4 = createLevelCache(brw, false);
        final int integer5 = Math.max(Math.max(this.width, this.height), this.depth);
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx, fx.offset(integer5 - 1, integer5 - 1, integer5 - 1))) {
            for (final Direction gc11 : Direction.values()) {
                for (final Direction gc12 : Direction.values()) {
                    if (gc12 != gc11) {
                        if (gc12 != gc11.getOpposite()) {
                            final BlockPatternMatch b16 = this.matches(fx2, gc11, gc12, loadingCache4);
                            if (b16 != null) {
                                return b16;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static LoadingCache<BlockPos, BlockInWorld> createLevelCache(final LevelReader brw, final boolean boolean2) {
        return (LoadingCache<BlockPos, BlockInWorld>)CacheBuilder.newBuilder().build((CacheLoader)new BlockCacheLoader(brw, boolean2));
    }
    
    protected static BlockPos translateAndRotate(final BlockPos fx, final Direction gc2, final Direction gc3, final int integer4, final int integer5, final int integer6) {
        if (gc2 == gc3 || gc2 == gc3.getOpposite()) {
            throw new IllegalArgumentException("Invalid forwards & up combination");
        }
        final Vec3i gr7 = new Vec3i(gc2.getStepX(), gc2.getStepY(), gc2.getStepZ());
        final Vec3i gr8 = new Vec3i(gc3.getStepX(), gc3.getStepY(), gc3.getStepZ());
        final Vec3i gr9 = gr7.cross(gr8);
        return fx.offset(gr8.getX() * -integer5 + gr9.getX() * integer4 + gr7.getX() * integer6, gr8.getY() * -integer5 + gr9.getY() * integer4 + gr7.getY() * integer6, gr8.getZ() * -integer5 + gr9.getZ() * integer4 + gr7.getZ() * integer6);
    }
    
    static class BlockCacheLoader extends CacheLoader<BlockPos, BlockInWorld> {
        private final LevelReader level;
        private final boolean loadChunks;
        
        public BlockCacheLoader(final LevelReader brw, final boolean boolean2) {
            this.level = brw;
            this.loadChunks = boolean2;
        }
        
        public BlockInWorld load(final BlockPos fx) throws Exception {
            return new BlockInWorld(this.level, fx, this.loadChunks);
        }
    }
    
    public static class BlockPatternMatch {
        private final BlockPos frontTopLeft;
        private final Direction forwards;
        private final Direction up;
        private final LoadingCache<BlockPos, BlockInWorld> cache;
        private final int width;
        private final int height;
        private final int depth;
        
        public BlockPatternMatch(final BlockPos fx, final Direction gc2, final Direction gc3, final LoadingCache<BlockPos, BlockInWorld> loadingCache, final int integer5, final int integer6, final int integer7) {
            this.frontTopLeft = fx;
            this.forwards = gc2;
            this.up = gc3;
            this.cache = loadingCache;
            this.width = integer5;
            this.height = integer6;
            this.depth = integer7;
        }
        
        public BlockPos getFrontTopLeft() {
            return this.frontTopLeft;
        }
        
        public Direction getForwards() {
            return this.forwards;
        }
        
        public Direction getUp() {
            return this.up;
        }
        
        public BlockInWorld getBlock(final int integer1, final int integer2, final int integer3) {
            return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, this.getForwards(), this.getUp(), integer1, integer2, integer3));
        }
        
        public String toString() {
            return MoreObjects.toStringHelper(this).add("up", this.up).add("forwards", this.forwards).add("frontTopLeft", this.frontTopLeft).toString();
        }
    }
}
