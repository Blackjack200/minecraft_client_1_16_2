package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.GlobalPos;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;

public class SecondaryPoiSensor extends Sensor<Villager> {
    public SecondaryPoiSensor() {
        super(40);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final Villager bfg) {
        final ResourceKey<Level> vj4 = aag.dimension();
        final BlockPos fx5 = bfg.blockPosition();
        final List<GlobalPos> list6 = (List<GlobalPos>)Lists.newArrayList();
        final int integer7 = 4;
        for (int integer8 = -4; integer8 <= 4; ++integer8) {
            for (int integer9 = -2; integer9 <= 2; ++integer9) {
                for (int integer10 = -4; integer10 <= 4; ++integer10) {
                    final BlockPos fx6 = fx5.offset(integer8, integer9, integer10);
                    if (bfg.getVillagerData().getProfession().getSecondaryPoi().contains(aag.getBlockState(fx6).getBlock())) {
                        list6.add(GlobalPos.of(vj4, fx6));
                    }
                }
            }
        }
        final Brain<?> arc8 = bfg.getBrain();
        if (!list6.isEmpty()) {
            arc8.<List<GlobalPos>>setMemory(MemoryModuleType.SECONDARY_JOB_SITE, list6);
        }
        else {
            arc8.<List<GlobalPos>>eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
        }
    }
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
    }
}
