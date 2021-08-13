package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class BackUpIfTooClose<E extends Mob> extends Behavior<E> {
    private final int tooCloseDistance;
    private final float strafeSpeed;
    
    public BackUpIfTooClose(final int integer, final float float2) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.tooCloseDistance = integer;
        this.strafeSpeed = float2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqk) {
        return this.isTargetVisible(aqk) && this.isTargetTooClose(aqk);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqk, final long long3) {
        aqk.getBrain().<EntityTracker>setMemory((MemoryModuleType<EntityTracker>)MemoryModuleType.LOOK_TARGET, new EntityTracker(this.getTarget(aqk), true));
        aqk.getMoveControl().strafe(-this.strafeSpeed, 0.0f);
        aqk.yRot = Mth.rotateIfNecessary(aqk.yRot, aqk.yHeadRot, 0.0f);
    }
    
    private boolean isTargetVisible(final E aqk) {
        return ((List)aqk.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).contains(this.getTarget(aqk));
    }
    
    private boolean isTargetTooClose(final E aqk) {
        return this.getTarget(aqk).closerThan(aqk, this.tooCloseDistance);
    }
    
    private LivingEntity getTarget(final E aqk) {
        return (LivingEntity)aqk.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
