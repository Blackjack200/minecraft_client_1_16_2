package net.minecraft.world.entity.ai.behavior;

import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.LivingEntity;
import java.util.Iterator;
import net.minecraft.world.level.pathfinder.Path;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import java.util.stream.Collectors;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.Optional;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.PathfinderMob;

public class AcquirePoi extends Behavior<PathfinderMob> {
    private final PoiType poiType;
    private final MemoryModuleType<GlobalPos> memoryToAcquire;
    private final boolean onlyIfAdult;
    private final Optional<Byte> onPoiAcquisitionEvent;
    private long nextScheduledStart;
    private final Long2ObjectMap<JitteredLinearRetry> batchCache;
    
    public AcquirePoi(final PoiType azo, final MemoryModuleType<GlobalPos> aya2, final MemoryModuleType<GlobalPos> aya3, final boolean boolean4, final Optional<Byte> optional) {
        super((Map)constructEntryConditionMap(aya2, aya3));
        this.batchCache = (Long2ObjectMap<JitteredLinearRetry>)new Long2ObjectOpenHashMap();
        this.poiType = azo;
        this.memoryToAcquire = aya3;
        this.onlyIfAdult = boolean4;
        this.onPoiAcquisitionEvent = optional;
    }
    
    public AcquirePoi(final PoiType azo, final MemoryModuleType<GlobalPos> aya, final boolean boolean3, final Optional<Byte> optional) {
        this(azo, aya, aya, boolean3, optional);
    }
    
    private static ImmutableMap<MemoryModuleType<?>, MemoryStatus> constructEntryConditionMap(final MemoryModuleType<GlobalPos> aya1, final MemoryModuleType<GlobalPos> aya2) {
        final ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> builder3 = (ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.builder();
        builder3.put(aya1, MemoryStatus.VALUE_ABSENT);
        if (aya2 != aya1) {
            builder3.put(aya2, MemoryStatus.VALUE_ABSENT);
        }
        return (ImmutableMap<MemoryModuleType<?>, MemoryStatus>)builder3.build();
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        if (this.onlyIfAdult && aqr.isBaby()) {
            return false;
        }
        if (this.nextScheduledStart == 0L) {
            this.nextScheduledStart = aqr.level.getGameTime() + aag.random.nextInt(20);
            return false;
        }
        return aag.getGameTime() >= this.nextScheduledStart;
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        this.nextScheduledStart = long3 + 20L + aag.getRandom().nextInt(20);
        final PoiManager azl6 = aag.getPoiManager();
        this.batchCache.long2ObjectEntrySet().removeIf(entry -> !((JitteredLinearRetry)entry.getValue()).isStillValid(long3));
        final Predicate<BlockPos> predicate7 = (Predicate<BlockPos>)(fx -> {
            final JitteredLinearRetry a5 = (JitteredLinearRetry)this.batchCache.get(fx.asLong());
            if (a5 == null) {
                return true;
            }
            if (!a5.shouldRetry(long3)) {
                return false;
            }
            a5.markAttempt(long3);
            return true;
        });
        final Set<BlockPos> set8 = (Set<BlockPos>)azl6.findAllClosestFirst(this.poiType.getPredicate(), predicate7, aqr.blockPosition(), 48, PoiManager.Occupancy.HAS_SPACE).limit(5L).collect(Collectors.toSet());
        final Path cxa9 = aqr.getNavigation().createPath(set8, this.poiType.getValidRange());
        if (cxa9 != null && cxa9.canReach()) {
            final BlockPos fx10 = cxa9.getTarget();
            azl6.getType(fx10).ifPresent(azo -> {
                azl6.take(this.poiType.getPredicate(), (Predicate<BlockPos>)(fx2 -> fx2.equals(fx10)), fx10, 1);
                aqr.getBrain().<GlobalPos>setMemory(this.memoryToAcquire, GlobalPos.of(aag.dimension(), fx10));
                this.onPoiAcquisitionEvent.ifPresent(byte3 -> aag.broadcastEntityEvent(aqr, byte3));
                this.batchCache.clear();
                DebugPackets.sendPoiTicketCountPacket(aag, fx10);
            });
        }
        else {
            for (final BlockPos fx11 : set8) {
                this.batchCache.computeIfAbsent(fx11.asLong(), long3 -> new JitteredLinearRetry(aqr.level.random, long3));
            }
        }
    }
    
    static class JitteredLinearRetry {
        private final Random random;
        private long previousAttemptTimestamp;
        private long nextScheduledAttemptTimestamp;
        private int currentDelay;
        
        JitteredLinearRetry(final Random random, final long long2) {
            this.random = random;
            this.markAttempt(long2);
        }
        
        public void markAttempt(final long long1) {
            this.previousAttemptTimestamp = long1;
            final int integer4 = this.currentDelay + this.random.nextInt(40) + 40;
            this.currentDelay = Math.min(integer4, 400);
            this.nextScheduledAttemptTimestamp = long1 + this.currentDelay;
        }
        
        public boolean isStillValid(final long long1) {
            return long1 - this.previousAttemptTimestamp < 400L;
        }
        
        public boolean shouldRetry(final long long1) {
            return long1 >= this.nextScheduledAttemptTimestamp;
        }
        
        public String toString() {
            return new StringBuilder().append("RetryMarker{, previousAttemptAt=").append(this.previousAttemptTimestamp).append(", nextScheduledAttemptAt=").append(this.nextScheduledAttemptTimestamp).append(", currentDelay=").append(this.currentDelay).append('}').toString();
        }
    }
}
