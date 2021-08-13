package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.npc.Villager;

public class VillagerCalmDown extends Behavior<Villager> {
    public VillagerCalmDown() {
        super((Map)ImmutableMap.of());
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final boolean boolean6 = VillagerPanicTrigger.isHurt(bfg) || VillagerPanicTrigger.hasHostile(bfg) || isCloseToEntityThatHurtMe(bfg);
        if (!boolean6) {
            bfg.getBrain().<DamageSource>eraseMemory(MemoryModuleType.HURT_BY);
            bfg.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
            bfg.getBrain().updateActivityFromSchedule(aag.getDayTime(), aag.getGameTime());
        }
    }
    
    private static boolean isCloseToEntityThatHurtMe(final Villager bfg) {
        return bfg.getBrain().<LivingEntity>getMemory(MemoryModuleType.HURT_BY_ENTITY).filter(aqj -> aqj.distanceToSqr(bfg) <= 36.0).isPresent();
    }
}
