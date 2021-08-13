package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class RingBell extends Behavior<LivingEntity> {
    public RingBell() {
        super((Map)ImmutableMap.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return aag.random.nextFloat() > 0.95f;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        final BlockPos fx7 = ((GlobalPos)arc6.<GlobalPos>getMemory(MemoryModuleType.MEETING_POINT).get()).pos();
        if (fx7.closerThan(aqj.blockPosition(), 3.0)) {
            final BlockState cee8 = aag.getBlockState(fx7);
            if (cee8.is(Blocks.BELL)) {
                final BellBlock buj9 = (BellBlock)cee8.getBlock();
                buj9.attemptToRing(aag, fx7, null);
            }
        }
    }
}
