package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.core.BlockPos;
import java.util.function.ToDoubleFunction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class GoToClosestVillage extends Behavior<Villager> {
    private final float speedModifier;
    private final int closeEnoughDistance;
    
    public GoToClosestVillage(final float float1, final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
        this.closeEnoughDistance = integer;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        return !aag.isVillage(bfg.blockPosition());
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final PoiManager azl6 = aag.getPoiManager();
        final int integer7 = azl6.sectionsToVillage(SectionPos.of(bfg.blockPosition()));
        Vec3 dck8 = null;
        for (int integer8 = 0; integer8 < 5; ++integer8) {
            final Vec3 dck9 = RandomPos.getLandPos(bfg, 15, 7, (ToDoubleFunction<BlockPos>)(fx -> -aag.sectionsToVillage(SectionPos.of(fx))));
            if (dck9 != null) {
                final int integer9 = azl6.sectionsToVillage(SectionPos.of(new BlockPos(dck9)));
                if (integer9 < integer7) {
                    dck8 = dck9;
                    break;
                }
                if (integer9 == integer7) {
                    dck8 = dck9;
                }
            }
        }
        if (dck8 != null) {
            bfg.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(dck8, this.speedModifier, this.closeEnoughDistance));
        }
    }
}
