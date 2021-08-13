package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class SetWalkTargetFromLookTarget extends Behavior<LivingEntity> {
    private final float speedModifier;
    private final int closeEnoughDistance;
    
    public SetWalkTargetFromLookTarget(final float float1, final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT));
        this.speedModifier = float1;
        this.closeEnoughDistance = integer;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        final PositionTracker asy7 = (PositionTracker)arc6.<PositionTracker>getMemory(MemoryModuleType.LOOK_TARGET).get();
        arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(asy7, this.speedModifier, this.closeEnoughDistance));
    }
}
