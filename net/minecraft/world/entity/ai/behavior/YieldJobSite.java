package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import java.util.function.Predicate;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class YieldJobSite extends Behavior<Villager> {
    private final float speedModifier;
    
    public YieldJobSite(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        return !bfg.isBaby() && bfg.getVillagerData().getProfession() == VillagerProfession.NONE;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final BlockPos fx6 = ((GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos();
        final Optional<PoiType> optional7 = aag.getPoiManager().getType(fx6);
        if (!optional7.isPresent()) {
            return;
        }
        BehaviorUtils.getNearbyVillagersWithCondition(bfg, (Predicate<Villager>)(bfg -> this.nearbyWantsJobsite((PoiType)optional7.get(), bfg, fx6))).findFirst().ifPresent(bfg4 -> this.yieldJobSite(aag, bfg, bfg4, fx6, bfg4.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).isPresent()));
    }
    
    private boolean nearbyWantsJobsite(final PoiType azo, final Villager bfg, final BlockPos fx) {
        final boolean boolean5 = bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
        if (boolean5) {
            return false;
        }
        final Optional<GlobalPos> optional6 = bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE);
        final VillagerProfession bfj7 = bfg.getVillagerData().getProfession();
        if (bfg.getVillagerData().getProfession() == VillagerProfession.NONE || !bfj7.getJobPoiType().getPredicate().test(azo)) {
            return false;
        }
        if (!optional6.isPresent()) {
            return this.canReachPos(bfg, fx, azo);
        }
        return ((GlobalPos)optional6.get()).pos().equals(fx);
    }
    
    private void yieldJobSite(final ServerLevel aag, final Villager bfg2, final Villager bfg3, final BlockPos fx, final boolean boolean5) {
        this.eraseMemories(bfg2);
        if (!boolean5) {
            BehaviorUtils.setWalkAndLookTargetMemories(bfg3, fx, this.speedModifier, 1);
            bfg3.getBrain().<GlobalPos>setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(aag.dimension(), fx));
            DebugPackets.sendPoiTicketCountPacket(aag, fx);
        }
    }
    
    private boolean canReachPos(final Villager bfg, final BlockPos fx, final PoiType azo) {
        final Path cxa5 = bfg.getNavigation().createPath(fx, azo.getValidRange());
        return cxa5 != null && cxa5.canReach();
    }
    
    private void eraseMemories(final Villager bfg) {
        bfg.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        bfg.getBrain().<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
        bfg.getBrain().<GlobalPos>eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}
