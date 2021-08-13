package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class ResetProfession extends Behavior<Villager> {
    public ResetProfession() {
        super((Map)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final VillagerData bfh4 = bfg.getVillagerData();
        return bfh4.getProfession() != VillagerProfession.NONE && bfh4.getProfession() != VillagerProfession.NITWIT && bfg.getVillagerXp() == 0 && bfh4.getLevel() <= 1;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        bfg.setVillagerData(bfg.getVillagerData().setProfession(VillagerProfession.NONE));
        bfg.refreshBrain(aag);
    }
}
