package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import java.util.List;
import net.minecraft.core.Position;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class SocializeAtBell extends Behavior<LivingEntity> {
    public SocializeAtBell() {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final Brain<?> arc4 = aqj.getBrain();
        final Optional<GlobalPos> optional5 = arc4.<GlobalPos>getMemory(MemoryModuleType.MEETING_POINT);
        return aag.getRandom().nextInt(100) == 0 && optional5.isPresent() && aag.dimension() == ((GlobalPos)optional5.get()).dimension() && ((GlobalPos)optional5.get()).pos().closerThan(aqj.position(), 4.0) && ((List)arc4.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream().anyMatch(aqj -> EntityType.VILLAGER.equals(aqj.getType()));
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        arc6.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(list -> list.stream().filter(aqj -> EntityType.VILLAGER.equals(aqj.getType())).filter(aqj2 -> aqj2.distanceToSqr(aqj) <= 32.0).findFirst().ifPresent(aqj -> {
            arc6.<LivingEntity>setMemory(MemoryModuleType.INTERACTION_TARGET, aqj);
            arc6.<EntityTracker>setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true));
            arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(aqj, false), 0.3f, 1));
        }));
    }
}
