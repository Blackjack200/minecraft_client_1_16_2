package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;

public class StartAttacking<E extends Mob> extends Behavior<E> {
    private final Predicate<E> canAttackPredicate;
    private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;
    
    public StartAttacking(final Predicate<E> predicate, final Function<E, Optional<? extends LivingEntity>> function) {
        super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.canAttackPredicate = predicate;
        this.targetFinderFunction = function;
    }
    
    public StartAttacking(final Function<E, Optional<? extends LivingEntity>> function) {
        this(aqk -> true, (Function)function);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqk) {
        if (!this.canAttackPredicate.test(aqk)) {
            return false;
        }
        final Optional<? extends LivingEntity> optional4 = this.targetFinderFunction.apply(aqk);
        return optional4.isPresent() && ((LivingEntity)optional4.get()).isAlive();
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqk, final long long3) {
        ((Optional)this.targetFinderFunction.apply(aqk)).ifPresent(aqj -> this.setAttackTarget(aqk, aqj));
    }
    
    private void setAttackTarget(final E aqk, final LivingEntity aqj) {
        aqk.getBrain().<LivingEntity>setMemory(MemoryModuleType.ATTACK_TARGET, aqj);
        aqk.getBrain().<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
