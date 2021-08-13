package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Position;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class SleepInBed extends Behavior<LivingEntity> {
    private long nextOkStartTime;
    
    public SleepInBed() {
        super((Map)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LAST_WOKEN, MemoryStatus.REGISTERED));
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        if (aqj.isPassenger()) {
            return false;
        }
        final Brain<?> arc4 = aqj.getBrain();
        final GlobalPos gf5 = (GlobalPos)arc4.<GlobalPos>getMemory(MemoryModuleType.HOME).get();
        if (aag.dimension() != gf5.dimension()) {
            return false;
        }
        final Optional<Long> optional6 = arc4.<Long>getMemory(MemoryModuleType.LAST_WOKEN);
        if (optional6.isPresent()) {
            final long long7 = aag.getGameTime() - (long)optional6.get();
            if (long7 > 0L && long7 < 100L) {
                return false;
            }
        }
        final BlockState cee7 = aag.getBlockState(gf5.pos());
        return gf5.pos().closerThan(aqj.position(), 2.0) && cee7.getBlock().is(BlockTags.BEDS) && !cee7.<Boolean>getValue((Property<Boolean>)BedBlock.OCCUPIED);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Optional<GlobalPos> optional6 = aqj.getBrain().<GlobalPos>getMemory(MemoryModuleType.HOME);
        if (!optional6.isPresent()) {
            return false;
        }
        final BlockPos fx7 = ((GlobalPos)optional6.get()).pos();
        return aqj.getBrain().isActive(Activity.REST) && aqj.getY() > fx7.getY() + 0.4 && fx7.closerThan(aqj.position(), 1.14);
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        if (long3 > this.nextOkStartTime) {
            InteractWithDoor.closeDoorsThatIHaveOpenedOrPassedThrough(aag, aqj, null, null);
            aqj.startSleeping(((GlobalPos)aqj.getBrain().<GlobalPos>getMemory(MemoryModuleType.HOME).get()).pos());
        }
    }
    
    @Override
    protected boolean timedOut(final long long1) {
        return false;
    }
    
    @Override
    protected void stop(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        if (aqj.isSleeping()) {
            aqj.stopSleeping();
            this.nextOkStartTime = long3 + 40L;
        }
    }
}
