package net.minecraft.world.entity.ai.sensing;

import java.util.Iterator;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public class PiglinBruteSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        final Brain<?> arc4 = aqj.getBrain();
        Optional<Mob> optional5 = (Optional<Mob>)Optional.empty();
        final List<AbstractPiglin> list6 = (List<AbstractPiglin>)Lists.newArrayList();
        final List<LivingEntity> list7 = (List<LivingEntity>)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of());
        for (final LivingEntity aqj2 : list7) {
            if (aqj2 instanceof WitherSkeleton || aqj2 instanceof WitherBoss) {
                optional5 = (Optional<Mob>)Optional.of(aqj2);
                break;
            }
        }
        final List<LivingEntity> list8 = (List<LivingEntity>)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of());
        for (final LivingEntity aqj3 : list8) {
            if (aqj3 instanceof AbstractPiglin && ((AbstractPiglin)aqj3).isAdult()) {
                list6.add(aqj3);
            }
        }
        arc4.<Mob>setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, optional5);
        arc4.<List<AbstractPiglin>>setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, list6);
    }
}
