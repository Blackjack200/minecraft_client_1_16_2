package net.minecraft.world.entity.ai.behavior;

import java.util.UUID;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class StopBeingAngryIfTargetDead<E extends Mob> extends Behavior<E> {
    public StopBeingAngryIfTargetDead() {
        super((Map)ImmutableMap.of(MemoryModuleType.ANGRY_AT, MemoryStatus.VALUE_PRESENT));
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqk, final long long3) {
        BehaviorUtils.getLivingEntityFromUUIDMemory(aqk, MemoryModuleType.ANGRY_AT).ifPresent(aqj -> {
            if (aqj.isDeadOrDying() && (aqj.getType() != EntityType.PLAYER || aag.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS))) {
                aqk.getBrain().<UUID>eraseMemory(MemoryModuleType.ANGRY_AT);
            }
        });
    }
}
