package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.level.pathfinder.Path;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import net.minecraft.world.entity.Mob;

public class NearestBedSensor extends Sensor<Mob> {
    private final Long2LongMap batchCache;
    private int triedCount;
    private long lastUpdate;
    
    public NearestBedSensor() {
        super(20);
        this.batchCache = (Long2LongMap)new Long2LongOpenHashMap();
    }
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_BED);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final Mob aqk) {
        if (!aqk.isBaby()) {
            return;
        }
        this.triedCount = 0;
        this.lastUpdate = aag.getGameTime() + aag.getRandom().nextInt(20);
        final PoiManager azl4 = aag.getPoiManager();
        final Predicate<BlockPos> predicate5 = (Predicate<BlockPos>)(fx -> {
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
        final Stream<BlockPos> stream6 = azl4.findAll(PoiType.HOME.getPredicate(), predicate5, aqk.blockPosition(), 48, PoiManager.Occupancy.ANY);
        final Path cxa7 = aqk.getNavigation().createPath(stream6, PoiType.HOME.getValidRange());
        if (cxa7 != null && cxa7.canReach()) {
            final BlockPos fx8 = cxa7.getTarget();
            final Optional<PoiType> optional9 = azl4.getType(fx8);
            if (optional9.isPresent()) {
                aqk.getBrain().<BlockPos>setMemory(MemoryModuleType.NEAREST_BED, fx8);
            }
        }
        else if (this.triedCount < 5) {
            this.batchCache.long2LongEntrySet().removeIf(entry -> entry.getLongValue() < this.lastUpdate);
        }
    }
}
