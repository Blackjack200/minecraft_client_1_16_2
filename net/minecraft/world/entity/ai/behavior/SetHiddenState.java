package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.core.Vec3i;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class SetHiddenState extends Behavior<LivingEntity> {
    private final int closeEnoughDist;
    private final int stayHiddenTicks;
    private int ticksHidden;
    
    public SetHiddenState(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(MemoryModuleType.HIDING_PLACE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HEARD_BELL_TIME, MemoryStatus.VALUE_PRESENT));
        this.stayHiddenTicks = integer1 * 20;
        this.ticksHidden = 0;
        this.closeEnoughDist = integer2;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        final Optional<Long> optional7 = arc6.<Long>getMemory(MemoryModuleType.HEARD_BELL_TIME);
        final boolean boolean8 = (long)optional7.get() + 300L <= long3;
        if (this.ticksHidden > this.stayHiddenTicks || boolean8) {
            arc6.<Long>eraseMemory(MemoryModuleType.HEARD_BELL_TIME);
            arc6.<GlobalPos>eraseMemory(MemoryModuleType.HIDING_PLACE);
            arc6.updateActivityFromSchedule(aag.getDayTime(), aag.getGameTime());
            this.ticksHidden = 0;
            return;
        }
        final BlockPos fx9 = ((GlobalPos)arc6.<GlobalPos>getMemory(MemoryModuleType.HIDING_PLACE).get()).pos();
        if (fx9.closerThan(aqj.blockPosition(), this.closeEnoughDist)) {
            ++this.ticksHidden;
        }
    }
}
