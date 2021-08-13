package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.npc.Villager;

public class VillagerPanicTrigger extends Behavior<Villager> {
    public VillagerPanicTrigger() {
        super((Map)ImmutableMap.of());
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return isHurt(bfg) || hasHostile(bfg);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        if (isHurt(bfg) || hasHostile(bfg)) {
            final Brain<?> arc6 = bfg.getBrain();
            if (!arc6.isActive(Activity.PANIC)) {
                arc6.<Path>eraseMemory(MemoryModuleType.PATH);
                arc6.<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
                arc6.<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
                arc6.<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
                arc6.<LivingEntity>eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            }
            arc6.setActiveActivityIfPossible(Activity.PANIC);
        }
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        if (long3 % 100L == 0L) {
            bfg.spawnGolemIfNeeded(aag, long3, 3);
        }
    }
    
    public static boolean hasHostile(final LivingEntity aqj) {
        return aqj.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
    }
    
    public static boolean isHurt(final LivingEntity aqj) {
        return aqj.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
}
