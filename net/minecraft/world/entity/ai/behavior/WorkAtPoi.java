package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.Position;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class WorkAtPoi extends Behavior<Villager> {
    private long lastCheck;
    
    public WorkAtPoi() {
        super((Map)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        if (aag.getGameTime() - this.lastCheck < 300L) {
            return false;
        }
        if (aag.random.nextInt(2) != 0) {
            return false;
        }
        this.lastCheck = aag.getGameTime();
        final GlobalPos gf4 = (GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).get();
        return gf4.dimension() == aag.dimension() && gf4.pos().closerThan(bfg.position(), 1.73);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final Brain<Villager> arc6 = bfg.getBrain();
        arc6.<Long>setMemory(MemoryModuleType.LAST_WORKED_AT_POI, long3);
        arc6.<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).ifPresent(gf -> arc6.<BlockPosTracker>setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(gf.pos())));
        bfg.playWorkSound();
        this.useWorkstation(aag, bfg);
        if (bfg.shouldRestock()) {
            bfg.restock();
        }
    }
    
    protected void useWorkstation(final ServerLevel aag, final Villager bfg) {
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        final Optional<GlobalPos> optional6 = bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE);
        if (!optional6.isPresent()) {
            return false;
        }
        final GlobalPos gf7 = (GlobalPos)optional6.get();
        return gf7.dimension() == aag.dimension() && gf7.pos().closerThan(bfg.position(), 1.73);
    }
}
