package net.minecraft.world.entity.ai.behavior;

import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.Villager;

public class PoiCompetitorScan extends Behavior<Villager> {
    final VillagerProfession profession;
    
    public PoiCompetitorScan(final VillagerProfession bfj) {
        super((Map)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.profession = bfj;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final GlobalPos gf6 = (GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).get();
        aag.getPoiManager().getType(gf6.pos()).ifPresent(azo -> BehaviorUtils.getNearbyVillagersWithCondition(bfg, (Predicate<Villager>)(bfg -> this.competesForSameJobsite(gf6, azo, bfg))).reduce(bfg, PoiCompetitorScan::selectWinner));
    }
    
    private static Villager selectWinner(final Villager bfg1, final Villager bfg2) {
        Villager bfg3;
        Villager bfg4;
        if (bfg1.getVillagerXp() > bfg2.getVillagerXp()) {
            bfg3 = bfg1;
            bfg4 = bfg2;
        }
        else {
            bfg3 = bfg2;
            bfg4 = bfg1;
        }
        bfg4.getBrain().<GlobalPos>eraseMemory(MemoryModuleType.JOB_SITE);
        return bfg3;
    }
    
    private boolean competesForSameJobsite(final GlobalPos gf, final PoiType azo, final Villager bfg) {
        return this.hasJobSite(bfg) && gf.equals(bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).get()) && this.hasMatchingProfession(azo, bfg.getVillagerData().getProfession());
    }
    
    private boolean hasMatchingProfession(final PoiType azo, final VillagerProfession bfj) {
        return bfj.getJobPoiType().getPredicate().test(azo);
    }
    
    private boolean hasJobSite(final Villager bfg) {
        return bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.JOB_SITE).isPresent();
    }
}
