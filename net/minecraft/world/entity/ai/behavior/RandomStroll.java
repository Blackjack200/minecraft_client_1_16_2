package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import java.util.Optional;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class RandomStroll extends Behavior<PathfinderMob> {
    private final float speedModifier;
    private final int maxHorizontalDistance;
    private final int maxVerticalDistance;
    
    public RandomStroll(final float float1) {
        this(float1, 10, 7);
    }
    
    public RandomStroll(final float float1, final int integer2, final int integer3) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
        this.maxHorizontalDistance = integer2;
        this.maxVerticalDistance = integer3;
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        final Optional<Vec3> optional6 = (Optional<Vec3>)Optional.ofNullable(RandomPos.getLandPos(aqr, this.maxHorizontalDistance, this.maxVerticalDistance));
        aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, (java.util.Optional<? extends WalkTarget>)optional6.map(dck -> new WalkTarget(dck, this.speedModifier, 0)));
    }
}
