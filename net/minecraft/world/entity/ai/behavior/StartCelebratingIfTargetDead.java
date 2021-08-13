package net.minecraft.world.entity.ai.behavior;

import java.util.UUID;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.LivingEntity;

public class StartCelebratingIfTargetDead extends Behavior<LivingEntity> {
    private final int celebrateDuration;
    private final BiPredicate<LivingEntity, LivingEntity> dancePredicate;
    
    public StartCelebratingIfTargetDead(final int integer, final BiPredicate<LivingEntity, LivingEntity> biPredicate) {
        super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryStatus.REGISTERED, MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DANCING, MemoryStatus.REGISTERED));
        this.celebrateDuration = integer;
        this.dancePredicate = biPredicate;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return this.getAttackTarget(aqj).isDeadOrDying();
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final LivingEntity aqj2 = this.getAttackTarget(aqj);
        if (this.dancePredicate.test(aqj, aqj2)) {
            aqj.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.DANCING, true, this.celebrateDuration);
        }
        aqj.getBrain().<BlockPos>setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, aqj2.blockPosition(), this.celebrateDuration);
        if (aqj2.getType() != EntityType.PLAYER || aag.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            aqj.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.ATTACK_TARGET);
            aqj.getBrain().<UUID>eraseMemory(MemoryModuleType.ANGRY_AT);
        }
    }
    
    private LivingEntity getAttackTarget(final LivingEntity aqj) {
        return (LivingEntity)aqj.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }
}
