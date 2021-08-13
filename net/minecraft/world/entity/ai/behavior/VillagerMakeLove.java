package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.level.pathfinder.Path;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.EntityType;
import java.util.Optional;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class VillagerMakeLove extends Behavior<Villager> {
    private long birthTimestamp;
    
    public VillagerMakeLove() {
        super((Map)ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        return this.isBreedingPossible(bfg);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return long3 <= this.birthTimestamp && this.isBreedingPossible(bfg);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final AgableMob apv6 = (AgableMob)bfg.getBrain().<AgableMob>getMemory(MemoryModuleType.BREED_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(bfg, apv6, 0.5f);
        aag.broadcastEntityEvent(apv6, (byte)18);
        aag.broadcastEntityEvent(bfg, (byte)18);
        final int integer7 = 275 + bfg.getRandom().nextInt(50);
        this.birthTimestamp = long3 + integer7;
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        final Villager bfg2 = (Villager)bfg.getBrain().<AgableMob>getMemory(MemoryModuleType.BREED_TARGET).get();
        if (bfg.distanceToSqr(bfg2) > 5.0) {
            return;
        }
        BehaviorUtils.lockGazeAndWalkToEachOther(bfg, bfg2, 0.5f);
        if (long3 >= this.birthTimestamp) {
            bfg.eatAndDigestFood();
            bfg2.eatAndDigestFood();
            this.tryToGiveBirth(aag, bfg, bfg2);
        }
        else if (bfg.getRandom().nextInt(35) == 0) {
            aag.broadcastEntityEvent(bfg2, (byte)12);
            aag.broadcastEntityEvent(bfg, (byte)12);
        }
    }
    
    private void tryToGiveBirth(final ServerLevel aag, final Villager bfg2, final Villager bfg3) {
        final Optional<BlockPos> optional5 = this.takeVacantBed(aag, bfg2);
        if (!optional5.isPresent()) {
            aag.broadcastEntityEvent(bfg3, (byte)13);
            aag.broadcastEntityEvent(bfg2, (byte)13);
        }
        else {
            final Optional<Villager> optional6 = this.breed(aag, bfg2, bfg3);
            if (optional6.isPresent()) {
                this.giveBedToChild(aag, (Villager)optional6.get(), (BlockPos)optional5.get());
            }
            else {
                aag.getPoiManager().release((BlockPos)optional5.get());
                DebugPackets.sendPoiTicketCountPacket(aag, (BlockPos)optional5.get());
            }
        }
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Villager bfg, final long long3) {
        bfg.getBrain().<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
    }
    
    private boolean isBreedingPossible(final Villager bfg) {
        final Brain<Villager> arc3 = bfg.getBrain();
        final Optional<AgableMob> optional4 = (Optional<AgableMob>)arc3.<AgableMob>getMemory(MemoryModuleType.BREED_TARGET).filter(apv -> apv.getType() == EntityType.VILLAGER);
        return optional4.isPresent() && BehaviorUtils.targetIsValid(arc3, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && bfg.canBreed() && ((AgableMob)optional4.get()).canBreed();
    }
    
    private Optional<BlockPos> takeVacantBed(final ServerLevel aag, final Villager bfg) {
        return aag.getPoiManager().take(PoiType.HOME.getPredicate(), (Predicate<BlockPos>)(fx -> this.canReach(bfg, fx)), bfg.blockPosition(), 48);
    }
    
    private boolean canReach(final Villager bfg, final BlockPos fx) {
        final Path cxa4 = bfg.getNavigation().createPath(fx, PoiType.HOME.getValidRange());
        return cxa4 != null && cxa4.canReach();
    }
    
    private Optional<Villager> breed(final ServerLevel aag, final Villager bfg2, final Villager bfg3) {
        final Villager bfg4 = bfg2.getBreedOffspring(aag, bfg3);
        if (bfg4 == null) {
            return (Optional<Villager>)Optional.empty();
        }
        bfg2.setAge(6000);
        bfg3.setAge(6000);
        bfg4.setAge(-24000);
        bfg4.moveTo(bfg2.getX(), bfg2.getY(), bfg2.getZ(), 0.0f, 0.0f);
        aag.addFreshEntityWithPassengers(bfg4);
        aag.broadcastEntityEvent(bfg4, (byte)12);
        return (Optional<Villager>)Optional.of(bfg4);
    }
    
    private void giveBedToChild(final ServerLevel aag, final Villager bfg, final BlockPos fx) {
        final GlobalPos gf5 = GlobalPos.of(aag.dimension(), fx);
        bfg.getBrain().<GlobalPos>setMemory(MemoryModuleType.HOME, gf5);
    }
}
