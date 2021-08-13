package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import java.util.Optional;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class VillageBoundRandomStroll extends Behavior<PathfinderMob> {
    private final float speedModifier;
    private final int maxXyDist;
    private final int maxYDist;
    
    public VillageBoundRandomStroll(final float float1) {
        this(float1, 10, 7);
    }
    
    public VillageBoundRandomStroll(final float float1, final int integer2, final int integer3) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
        this.maxXyDist = integer2;
        this.maxYDist = integer3;
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        final BlockPos fx6 = aqr.blockPosition();
        if (aag.isVillage(fx6)) {
            this.setRandomPos(aqr);
        }
        else {
            final SectionPos gp7 = SectionPos.of(fx6);
            final SectionPos gp8 = BehaviorUtils.findSectionClosestToVillage(aag, gp7, 2);
            if (gp8 != gp7) {
                this.setTargetedPos(aqr, gp8);
            }
            else {
                this.setRandomPos(aqr);
            }
        }
    }
    
    private void setTargetedPos(final PathfinderMob aqr, final SectionPos gp) {
        final Optional<Vec3> optional4 = (Optional<Vec3>)Optional.ofNullable(RandomPos.getPosTowards(aqr, this.maxXyDist, this.maxYDist, Vec3.atBottomCenterOf(gp.center())));
        aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, (java.util.Optional<? extends WalkTarget>)optional4.map(dck -> new WalkTarget(dck, this.speedModifier, 0)));
    }
    
    private void setRandomPos(final PathfinderMob aqr) {
        final Optional<Vec3> optional3 = (Optional<Vec3>)Optional.ofNullable(RandomPos.getLandPos(aqr, this.maxXyDist, this.maxYDist));
        aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, (java.util.Optional<? extends WalkTarget>)optional3.map(dck -> new WalkTarget(dck, this.speedModifier, 0)));
    }
}
