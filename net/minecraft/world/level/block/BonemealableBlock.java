package net.minecraft.world.level.block;

import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public interface BonemealableBlock {
    boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4);
    
    boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee);
    
    void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee);
}
