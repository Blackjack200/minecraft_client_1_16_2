package net.minecraft.world.entity.ai.behavior;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.Mob;

public class GoToCelebrateLocation<E extends Mob> extends Behavior<E> {
    private final int closeEnoughDist;
    private final float speedModifier;
    
    public GoToCelebrateLocation(final int integer, final float float2) {
        super((Map)ImmutableMap.of(MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
        this.closeEnoughDist = integer;
        this.speedModifier = float2;
    }
    
    @Override
    protected void start(final ServerLevel aag, final Mob aqk, final long long3) {
        final BlockPos fx6 = getCelebrateLocation(aqk);
        final boolean boolean7 = fx6.closerThan(aqk.blockPosition(), this.closeEnoughDist);
        if (!boolean7) {
            BehaviorUtils.setWalkAndLookTargetMemories(aqk, getNearbyPos(aqk, fx6), this.speedModifier, this.closeEnoughDist);
        }
    }
    
    private static BlockPos getNearbyPos(final Mob aqk, final BlockPos fx) {
        final Random random3 = aqk.level.random;
        return fx.offset(getRandomOffset(random3), 0, getRandomOffset(random3));
    }
    
    private static int getRandomOffset(final Random random) {
        return random.nextInt(3) - 1;
    }
    
    private static BlockPos getCelebrateLocation(final Mob aqk) {
        return (BlockPos)aqk.getBrain().<BlockPos>getMemory(MemoryModuleType.CELEBRATE_LOCATION).get();
    }
}
