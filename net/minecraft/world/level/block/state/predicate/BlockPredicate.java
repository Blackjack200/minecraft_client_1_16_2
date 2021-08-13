package net.minecraft.world.level.block.state.predicate;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class BlockPredicate implements Predicate<BlockState> {
    private final Block block;
    
    public BlockPredicate(final Block bul) {
        this.block = bul;
    }
    
    public static BlockPredicate forBlock(final Block bul) {
        return new BlockPredicate(bul);
    }
    
    public boolean test(@Nullable final BlockState cee) {
        return cee != null && cee.is(this.block);
    }
}
