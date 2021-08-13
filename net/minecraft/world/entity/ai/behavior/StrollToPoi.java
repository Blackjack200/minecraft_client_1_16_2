package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import java.util.Optional;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class StrollToPoi extends Behavior<PathfinderMob> {
    private final MemoryModuleType<GlobalPos> memoryType;
    private final int closeEnoughDist;
    private final int maxDistanceFromPoi;
    private final float speedModifier;
    private long nextOkStartTime;
    
    public StrollToPoi(final MemoryModuleType<GlobalPos> aya, final float float2, final int integer3, final int integer4) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, aya, MemoryStatus.VALUE_PRESENT));
        this.memoryType = aya;
        this.speedModifier = float2;
        this.closeEnoughDist = integer3;
        this.maxDistanceFromPoi = integer4;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        final Optional<GlobalPos> optional4 = aqr.getBrain().<GlobalPos>getMemory(this.memoryType);
        return optional4.isPresent() && aag.dimension() == ((GlobalPos)optional4.get()).dimension() && ((GlobalPos)optional4.get()).pos().closerThan(aqr.position(), this.maxDistanceFromPoi);
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        if (long3 > this.nextOkStartTime) {
            final Brain<?> arc6 = aqr.getBrain();
            final Optional<GlobalPos> optional7 = arc6.<GlobalPos>getMemory(this.memoryType);
            optional7.ifPresent(gf -> arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(gf.pos(), this.speedModifier, this.closeEnoughDist)));
            this.nextOkStartTime = long3 + 80L;
        }
    }
}
