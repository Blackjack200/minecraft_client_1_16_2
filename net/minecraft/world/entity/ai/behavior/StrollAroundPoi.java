package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.RandomPos;
import java.util.Optional;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class StrollAroundPoi extends Behavior<PathfinderMob> {
    private final MemoryModuleType<GlobalPos> memoryType;
    private long nextOkStartTime;
    private final int maxDistanceFromPoi;
    private float speedModifier;
    
    public StrollAroundPoi(final MemoryModuleType<GlobalPos> aya, final float float2, final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, aya, MemoryStatus.VALUE_PRESENT));
        this.memoryType = aya;
        this.speedModifier = float2;
        this.maxDistanceFromPoi = integer;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        final Optional<GlobalPos> optional4 = aqr.getBrain().<GlobalPos>getMemory(this.memoryType);
        return optional4.isPresent() && aag.dimension() == ((GlobalPos)optional4.get()).dimension() && ((GlobalPos)optional4.get()).pos().closerThan(aqr.position(), this.maxDistanceFromPoi);
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        if (long3 > this.nextOkStartTime) {
            final Optional<Vec3> optional6 = (Optional<Vec3>)Optional.ofNullable(RandomPos.getLandPos(aqr, 8, 6));
            aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, (java.util.Optional<? extends WalkTarget>)optional6.map(dck -> new WalkTarget(dck, this.speedModifier, 1)));
            this.nextOkStartTime = long3 + 180L;
        }
    }
}
