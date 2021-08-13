package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.level.levelgen.Heightmap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class MoveToSkySeeingSpot extends Behavior<LivingEntity> {
    private final float speedModifier;
    
    public MoveToSkySeeingSpot(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Optional<Vec3> optional6 = (Optional<Vec3>)Optional.ofNullable(this.getOutdoorPosition(aag, aqj));
        if (optional6.isPresent()) {
            aqj.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, (java.util.Optional<? extends WalkTarget>)optional6.map(dck -> new WalkTarget(dck, this.speedModifier, 0)));
        }
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return !aag.canSeeSky(aqj.blockPosition());
    }
    
    @Nullable
    private Vec3 getOutdoorPosition(final ServerLevel aag, final LivingEntity aqj) {
        final Random random4 = aqj.getRandom();
        final BlockPos fx5 = aqj.blockPosition();
        for (int integer6 = 0; integer6 < 10; ++integer6) {
            final BlockPos fx6 = fx5.offset(random4.nextInt(20) - 10, random4.nextInt(6) - 3, random4.nextInt(20) - 10);
            if (hasNoBlocksAbove(aag, aqj, fx6)) {
                return Vec3.atBottomCenterOf(fx6);
            }
        }
        return null;
    }
    
    public static boolean hasNoBlocksAbove(final ServerLevel aag, final LivingEntity aqj, final BlockPos fx) {
        return aag.canSeeSky(fx) && aag.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, fx).getY() <= aqj.getY();
    }
}
