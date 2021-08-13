package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;

public class LocateHidingPlace extends Behavior<LivingEntity> {
    private final float speedModifier;
    private final int radius;
    private final int closeEnoughDist;
    private Optional<BlockPos> currentPos;
    
    public LocateHidingPlace(final int integer1, final float float2, final int integer3) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HOME, MemoryStatus.REGISTERED, MemoryModuleType.HIDING_PLACE, MemoryStatus.REGISTERED));
        this.currentPos = (Optional<BlockPos>)Optional.empty();
        this.radius = integer1;
        this.speedModifier = float2;
        this.closeEnoughDist = integer3;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final Optional<BlockPos> optional4 = aag.getPoiManager().find((Predicate<PoiType>)(azo -> azo == PoiType.HOME), (Predicate<BlockPos>)(fx -> true), aqj.blockPosition(), this.closeEnoughDist + 1, PoiManager.Occupancy.ANY);
        if (optional4.isPresent() && ((BlockPos)optional4.get()).closerThan(aqj.position(), this.closeEnoughDist)) {
            this.currentPos = optional4;
        }
        else {
            this.currentPos = (Optional<BlockPos>)Optional.empty();
        }
        return true;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        Optional<BlockPos> optional7 = this.currentPos;
        if (!optional7.isPresent()) {
            optional7 = aag.getPoiManager().getRandom((Predicate<PoiType>)(azo -> azo == PoiType.HOME), (Predicate<BlockPos>)(fx -> true), PoiManager.Occupancy.ANY, aqj.blockPosition(), this.radius, aqj.getRandom());
            if (!optional7.isPresent()) {
                final Optional<GlobalPos> optional8 = arc6.<GlobalPos>getMemory(MemoryModuleType.HOME);
                if (optional8.isPresent()) {
                    optional7 = (Optional<BlockPos>)Optional.of(((GlobalPos)optional8.get()).pos());
                }
            }
        }
        if (optional7.isPresent()) {
            arc6.<Path>eraseMemory(MemoryModuleType.PATH);
            arc6.<PositionTracker>eraseMemory(MemoryModuleType.LOOK_TARGET);
            arc6.<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
            arc6.<LivingEntity>eraseMemory(MemoryModuleType.INTERACTION_TARGET);
            arc6.<GlobalPos>setMemory(MemoryModuleType.HIDING_PLACE, GlobalPos.of(aag.dimension(), (BlockPos)optional7.get()));
            if (!((BlockPos)optional7.get()).closerThan(aqj.position(), this.closeEnoughDist)) {
                arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget((BlockPos)optional7.get(), this.speedModifier, this.closeEnoughDist));
            }
        }
    }
}
