package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Vec3i;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;

public class SetWalkTargetFromBlockMemory extends Behavior<Villager> {
    private final MemoryModuleType<GlobalPos> memoryType;
    private final float speedModifier;
    private final int closeEnoughDist;
    private final int tooFarDistance;
    private final int tooLongUnreachableDuration;
    
    public SetWalkTargetFromBlockMemory(final MemoryModuleType<GlobalPos> aya, final float float2, final int integer3, final int integer4, final int integer5) {
        super((Map)ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, aya, MemoryStatus.VALUE_PRESENT));
        this.memoryType = aya;
        this.speedModifier = float2;
        this.closeEnoughDist = integer3;
        this.tooFarDistance = integer4;
        this.tooLongUnreachableDuration = integer5;
    }
    
    private void dropPOI(final Villager bfg, final long long2) {
        final Brain<?> arc5 = bfg.getBrain();
        bfg.releasePoi(this.memoryType);
        arc5.<GlobalPos>eraseMemory(this.memoryType);
        arc5.<Long>setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, long2);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Villager bfg, final long long3) {
        final Brain<?> arc6 = bfg.getBrain();
        arc6.<GlobalPos>getMemory(this.memoryType).ifPresent(gf -> {
            if (this.wrongDimension(aag, gf) || this.tiredOfTryingToFindTarget(aag, bfg)) {
                this.dropPOI(bfg, long3);
            }
            else if (this.tooFar(bfg, gf)) {
                Vec3 dck8 = null;
                int integer9 = 0;
                final int integer10 = 1000;
                while (integer9 < 1000 && (dck8 == null || this.tooFar(bfg, GlobalPos.of(aag.dimension(), new BlockPos(dck8))))) {
                    dck8 = RandomPos.getPosTowards(bfg, 15, 7, Vec3.atBottomCenterOf(gf.pos()));
                    ++integer9;
                }
                if (integer9 == 1000) {
                    this.dropPOI(bfg, long3);
                    return;
                }
                arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(dck8, this.speedModifier, this.closeEnoughDist));
            }
            else if (!this.closeEnough(aag, bfg, gf)) {
                arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(gf.pos(), this.speedModifier, this.closeEnoughDist));
            }
        });
    }
    
    private boolean tiredOfTryingToFindTarget(final ServerLevel aag, final Villager bfg) {
        final Optional<Long> optional4 = bfg.getBrain().<Long>getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        return optional4.isPresent() && aag.getGameTime() - (long)optional4.get() > this.tooLongUnreachableDuration;
    }
    
    private boolean tooFar(final Villager bfg, final GlobalPos gf) {
        return gf.pos().distManhattan(bfg.blockPosition()) > this.tooFarDistance;
    }
    
    private boolean wrongDimension(final ServerLevel aag, final GlobalPos gf) {
        return gf.dimension() != aag.dimension();
    }
    
    private boolean closeEnough(final ServerLevel aag, final Villager bfg, final GlobalPos gf) {
        return gf.dimension() == aag.dimension() && gf.pos().distManhattan(bfg.blockPosition()) <= this.closeEnoughDist;
    }
}
