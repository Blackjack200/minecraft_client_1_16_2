package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class Mount<E extends LivingEntity> extends Behavior<E> {
    private final float speedModifier;
    
    public Mount(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.RIDE_TARGET, MemoryStatus.VALUE_PRESENT));
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return !aqj.isPassenger();
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        if (this.isCloseEnoughToStartRiding(aqj)) {
            aqj.startRiding(this.getRidableEntity(aqj));
        }
        else {
            BehaviorUtils.setWalkAndLookTargetMemories(aqj, this.getRidableEntity(aqj), this.speedModifier, 1);
        }
    }
    
    private boolean isCloseEnoughToStartRiding(final E aqj) {
        return this.getRidableEntity(aqj).closerThan(aqj, 1.0);
    }
    
    private Entity getRidableEntity(final E aqj) {
        return (Entity)aqj.getBrain().<Entity>getMemory(MemoryModuleType.RIDE_TARGET).get();
    }
}
