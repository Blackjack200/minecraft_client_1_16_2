package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public class HurtBySensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        final Brain<?> arc4 = aqj.getBrain();
        final DamageSource aph5 = aqj.getLastDamageSource();
        if (aph5 != null) {
            arc4.<DamageSource>setMemory(MemoryModuleType.HURT_BY, aqj.getLastDamageSource());
            final Entity apx6 = aph5.getEntity();
            if (apx6 instanceof LivingEntity) {
                arc4.<LivingEntity>setMemory(MemoryModuleType.HURT_BY_ENTITY, (LivingEntity)apx6);
            }
        }
        else {
            arc4.<DamageSource>eraseMemory(MemoryModuleType.HURT_BY);
        }
        arc4.<LivingEntity>getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(aqj -> {
            if (!aqj.isAlive() || aqj.level != aag) {
                arc4.<LivingEntity>eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            }
        });
    }
}
