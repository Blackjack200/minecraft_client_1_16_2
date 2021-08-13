package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.Predicate;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class GoToPotentialJobSite extends Behavior<Villager> {
    final float speedModifier;
    
    public GoToPotentialJobSite(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), 1200);
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        return (boolean)bfg.getBrain().getActiveNonCoreActivity().map(bhc -> bhc == Activity.IDLE || bhc == Activity.WORK || bhc == Activity.PLAY).orElse(true);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return bfg.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        BehaviorUtils.setWalkAndLookTargetMemories(bfg, ((GlobalPos)bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos(), this.speedModifier, 1);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final Optional<GlobalPos> optional6 = bfg.getBrain().<GlobalPos>getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        optional6.ifPresent(gf -> {
            final BlockPos fx3 = gf.pos();
            final ServerLevel aag2 = aag.getServer().getLevel(gf.dimension());
            if (aag2 == null) {
                return;
            }
            final PoiManager azl5 = aag2.getPoiManager();
            if (azl5.exists(fx3, (Predicate<PoiType>)(azo -> true))) {
                azl5.release(fx3);
            }
            DebugPackets.sendPoiTicketCountPacket(aag, fx3);
        });
        bfg.getBrain().<GlobalPos>eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}
