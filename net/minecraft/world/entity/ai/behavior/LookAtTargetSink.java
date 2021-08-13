package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class LookAtTargetSink extends Behavior<Mob> {
    public LookAtTargetSink(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), integer1, integer2);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Mob aqk, final long long3) {
        return aqk.getBrain().<PositionTracker>getMemory(MemoryModuleType.LOOK_TARGET).filter(asy -> asy.isVisibleBy(aqk)).isPresent();
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Mob aqk, final long long3) {
        aqk.getBrain().<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Mob aqk, final long long3) {
        aqk.getBrain().<PositionTracker>getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(asy -> aqk.getLookControl().setLookAt(asy.currentPosition()));
    }
}
