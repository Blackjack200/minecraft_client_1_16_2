package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import java.util.Optional;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class AssignProfessionFromJobSite extends Behavior<Villager> {
    public AssignProfessionFromJobSite() {
        super((Map)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final BlockPos fx4 = ((GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos();
        return fx4.closerThan(bfg.position(), 2.0) || bfg.assignProfessionWhenSpawned();
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final GlobalPos gf6 = (GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
        bfg.getBrain().<GlobalPos>eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        bfg.getBrain().<GlobalPos>setMemory(MemoryModuleType.JOB_SITE, gf6);
        aag.broadcastEntityEvent(bfg, (byte)14);
        if (bfg.getVillagerData().getProfession() != VillagerProfession.NONE) {
            return;
        }
        final MinecraftServer minecraftServer7 = aag.getServer();
        Optional.ofNullable(minecraftServer7.getLevel(gf6.dimension())).flatMap(aag -> aag.getPoiManager().getType(gf6.pos())).flatMap(azo -> Registry.VILLAGER_PROFESSION.stream().filter(bfj -> bfj.getJobPoiType() == azo).findFirst()).ifPresent(bfj -> {
            bfg.setVillagerData(bfg.getVillagerData().setProfession(bfj));
            bfg.refreshBrain(aag);
        });
    }
}
