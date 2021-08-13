package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.level.pathfinder.Path;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.server.level.ServerLevel;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import net.minecraft.world.entity.LivingEntity;

public class SetClosestHomeAsWalkTarget extends Behavior<LivingEntity> {
    private final float speedModifier;
    private final Long2LongMap batchCache;
    private int triedCount;
    private long lastUpdate;
    
    public SetClosestHomeAsWalkTarget(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT));
        this.batchCache = (Long2LongMap)new Long2LongOpenHashMap();
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        if (aag.getGameTime() - this.lastUpdate < 20L) {
            return false;
        }
        final PathfinderMob aqr4 = (PathfinderMob)aqj;
        final PoiManager azl5 = aag.getPoiManager();
        final Optional<BlockPos> optional6 = azl5.findClosest(PoiType.HOME.getPredicate(), aqj.blockPosition(), 48, PoiManager.Occupancy.ANY);
        return optional6.isPresent() && ((BlockPos)optional6.get()).distSqr(aqr4.blockPosition()) > 4.0;
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        this.triedCount = 0;
        this.lastUpdate = aag.getGameTime() + aag.getRandom().nextInt(20);
        final PathfinderMob aqr6 = (PathfinderMob)aqj;
        final PoiManager azl7 = aag.getPoiManager();
        final Predicate<BlockPos> predicate8 = (Predicate<BlockPos>)(fx -> {
            final long long3 = fx.asLong();
            if (this.batchCache.containsKey(long3)) {
                return false;
            }
            if (++this.triedCount >= 5) {
                return false;
            }
            this.batchCache.put(long3, this.lastUpdate + 40L);
            return true;
        });
        final Stream<BlockPos> stream9 = azl7.findAll(PoiType.HOME.getPredicate(), predicate8, aqj.blockPosition(), 48, PoiManager.Occupancy.ANY);
        final Path cxa10 = aqr6.getNavigation().createPath(stream9, PoiType.HOME.getValidRange());
        if (cxa10 != null && cxa10.canReach()) {
            final BlockPos fx11 = cxa10.getTarget();
            final Optional<PoiType> optional12 = azl7.getType(fx11);
            if (optional12.isPresent()) {
                aqj.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fx11, this.speedModifier, 1));
                DebugPackets.sendPoiTicketCountPacket(aag, fx11);
            }
        }
        else if (this.triedCount < 5) {
            this.batchCache.long2LongEntrySet().removeIf(entry -> entry.getLongValue() < this.lastUpdate);
        }
    }
}
