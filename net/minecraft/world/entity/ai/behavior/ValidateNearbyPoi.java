package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.Predicate;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;

public class ValidateNearbyPoi extends Behavior<LivingEntity> {
    private final MemoryModuleType<GlobalPos> memoryType;
    private final Predicate<PoiType> poiPredicate;
    
    public ValidateNearbyPoi(final PoiType azo, final MemoryModuleType<GlobalPos> aya) {
        super((Map)ImmutableMap.of(aya, MemoryStatus.VALUE_PRESENT));
        this.poiPredicate = azo.getPredicate();
        this.memoryType = aya;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        final GlobalPos gf4 = (GlobalPos)aqj.getBrain().<GlobalPos>getMemory(this.memoryType).get();
        return aag.dimension() == gf4.dimension() && gf4.pos().closerThan(aqj.position(), 16.0);
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        final GlobalPos gf7 = (GlobalPos)arc6.<GlobalPos>getMemory(this.memoryType).get();
        final BlockPos fx8 = gf7.pos();
        final ServerLevel aag2 = aag.getServer().getLevel(gf7.dimension());
        if (aag2 == null || this.poiDoesntExist(aag2, fx8)) {
            arc6.<GlobalPos>eraseMemory(this.memoryType);
        }
        else if (this.bedIsOccupied(aag2, fx8, aqj)) {
            arc6.<GlobalPos>eraseMemory(this.memoryType);
            aag.getPoiManager().release(fx8);
            DebugPackets.sendPoiTicketCountPacket(aag, fx8);
        }
    }
    
    private boolean bedIsOccupied(final ServerLevel aag, final BlockPos fx, final LivingEntity aqj) {
        final BlockState cee5 = aag.getBlockState(fx);
        return cee5.getBlock().is(BlockTags.BEDS) && cee5.<Boolean>getValue((Property<Boolean>)BedBlock.OCCUPIED) && !aqj.isSleeping();
    }
    
    private boolean poiDoesntExist(final ServerLevel aag, final BlockPos fx) {
        return !aag.getPoiManager().exists(fx, this.poiPredicate);
    }
}
