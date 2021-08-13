package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;

public class StopAttackingIfTargetInvalid<E extends Mob> extends Behavior<E> {
    private final Predicate<LivingEntity> stopAttackingWhen;
    
    public StopAttackingIfTargetInvalid(final Predicate<LivingEntity> predicate) {
        super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.stopAttackingWhen = predicate;
    }
    
    public StopAttackingIfTargetInvalid() {
        this(aqj -> false);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqk, final long long3) {
        if (StopAttackingIfTargetInvalid.<E>isTiredOfTryingToReachTarget(aqk)) {
            this.clearAttackTarget(aqk);
            return;
        }
        if (this.isCurrentTargetDeadOrRemoved(aqk)) {
            this.clearAttackTarget(aqk);
            return;
        }
        if (this.isCurrentTargetInDifferentLevel(aqk)) {
            this.clearAttackTarget(aqk);
            return;
        }
        if (!EntitySelector.ATTACK_ALLOWED.test(this.getAttackTarget(aqk))) {
            this.clearAttackTarget(aqk);
            return;
        }
        if (this.stopAttackingWhen.test(this.getAttackTarget(aqk))) {
            this.clearAttackTarget(aqk);
        }
    }
    
    private boolean isCurrentTargetInDifferentLevel(final E aqk) {
        return this.getAttackTarget(aqk).level != aqk.level;
    }
    
    private LivingEntity getAttackTarget(final E aqk) {
        return (LivingEntity)aqk.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
    
    private static <E extends LivingEntity> boolean isTiredOfTryingToReachTarget(final E aqj) {
        final Optional<Long> optional2 = aqj.getBrain().<Long>getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        return optional2.isPresent() && aqj.level.getGameTime() - (long)optional2.get() > 200L;
    }
    
    private boolean isCurrentTargetDeadOrRemoved(final E aqk) {
        final Optional<LivingEntity> optional3 = aqk.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET);
        return optional3.isPresent() && !((LivingEntity)optional3.get()).isAlive();
    }
    
    private void clearAttackTarget(final E aqk) {
        aqk.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
