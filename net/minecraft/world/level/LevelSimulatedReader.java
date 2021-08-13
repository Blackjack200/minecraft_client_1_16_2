package net.minecraft.world.level;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public interface LevelSimulatedReader {
    boolean isStateAtPosition(final BlockPos fx, final Predicate<BlockState> predicate);
    
    BlockPos getHeightmapPos(final Heightmap.Types a, final BlockPos fx);
}
