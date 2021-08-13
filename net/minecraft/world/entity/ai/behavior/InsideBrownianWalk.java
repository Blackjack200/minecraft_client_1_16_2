package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class InsideBrownianWalk extends Behavior<PathfinderMob> {
    private final float speedModifier;
    
    public InsideBrownianWalk(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        return !aag.canSeeSky(aqr.blockPosition());
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        final BlockPos fx6 = aqr.blockPosition();
        final List<BlockPos> list7 = (List<BlockPos>)BlockPos.betweenClosedStream(fx6.offset(-1, -1, -1), fx6.offset(1, 1, 1)).map(BlockPos::immutable).collect(Collectors.toList());
        Collections.shuffle((List)list7);
        final Optional<BlockPos> optional8 = (Optional<BlockPos>)list7.stream().filter(fx -> !aag.canSeeSky(fx)).filter(fx -> aag.loadedAndEntityCanStandOn(fx, aqr)).filter(fx -> aag.noCollision(aqr)).findFirst();
        optional8.ifPresent(fx -> aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fx, this.speedModifier, 0)));
    }
}
