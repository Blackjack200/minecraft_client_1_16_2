package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import java.util.Optional;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import javax.annotation.Nullable;
import net.minecraft.core.GlobalPos;
import java.util.List;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class StrollToPoiList extends Behavior<Villager> {
    private final MemoryModuleType<List<GlobalPos>> strollToMemoryType;
    private final MemoryModuleType<GlobalPos> mustBeCloseToMemoryType;
    private final float speedModifier;
    private final int closeEnoughDist;
    private final int maxDistanceFromPoi;
    private long nextOkStartTime;
    @Nullable
    private GlobalPos targetPos;
    
    public StrollToPoiList(final MemoryModuleType<List<GlobalPos>> aya1, final float float2, final int integer3, final int integer4, final MemoryModuleType<GlobalPos> aya5) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, aya1, MemoryStatus.VALUE_PRESENT, aya5, MemoryStatus.VALUE_PRESENT));
        this.strollToMemoryType = aya1;
        this.speedModifier = float2;
        this.closeEnoughDist = integer3;
        this.maxDistanceFromPoi = integer4;
        this.mustBeCloseToMemoryType = aya5;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final Optional<List<GlobalPos>> optional4 = bfg.getBrain().<List<GlobalPos>>getMemory(this.strollToMemoryType);
        final Optional<GlobalPos> optional5 = bfg.getBrain().<GlobalPos>getMemory(this.mustBeCloseToMemoryType);
        if (optional4.isPresent() && optional5.isPresent()) {
            final List<GlobalPos> list6 = (List<GlobalPos>)optional4.get();
            if (!list6.isEmpty()) {
                this.targetPos = (GlobalPos)list6.get(aag.getRandom().nextInt(list6.size()));
                return this.targetPos != null && aag.dimension() == this.targetPos.dimension() && ((GlobalPos)optional5.get()).pos().closerThan(bfg.position(), this.maxDistanceFromPoi);
            }
        }
        return false;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        if (long3 > this.nextOkStartTime && this.targetPos != null) {
            bfg.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(this.targetPos.pos(), this.speedModifier, this.closeEnoughDist));
            this.nextOkStartTime = long3 + 100L;
        }
    }
}
