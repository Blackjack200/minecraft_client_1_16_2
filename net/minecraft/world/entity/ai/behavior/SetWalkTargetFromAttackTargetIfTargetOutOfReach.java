package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class SetWalkTargetFromAttackTargetIfTargetOutOfReach extends Behavior<Mob> {
    private final float speedModifier;
    
    public SetWalkTargetFromAttackTargetIfTargetOutOfReach(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED));
        this.speedModifier = float1;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Mob aqk, final long long3) {
        final LivingEntity aqj6 = (LivingEntity)aqk.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if (BehaviorUtils.canSee(aqk, aqj6) && BehaviorUtils.isWithinAttackRange(aqk, aqj6, 1)) {
            this.clearWalkTarget(aqk);
        }
        else {
            this.setWalkAndLookTarget(aqk, aqj6);
        }
    }
    
    private void setWalkAndLookTarget(final LivingEntity aqj1, final LivingEntity aqj2) {
        final Brain arc4 = aqj1.getBrain();
        arc4.<EntityTracker>setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj2, true));
        final WalkTarget ayc5 = new WalkTarget(new EntityTracker(aqj2, false), this.speedModifier, 0);
        arc4.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, ayc5);
    }
    
    private void clearWalkTarget(final LivingEntity aqj) {
        aqj.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
