package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.core.SectionPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;

public class MoveBackToVillageGoal extends RandomStrollGoal {
    public MoveBackToVillageGoal(final PathfinderMob aqr, final double double2, final boolean boolean3) {
        super(aqr, double2, 10, boolean3);
    }
    
    @Override
    public boolean canUse() {
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final BlockPos fx3 = this.mob.blockPosition();
        return !aag2.isVillage(fx3) && super.canUse();
    }
    
    @Nullable
    @Override
    protected Vec3 getPosition() {
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final BlockPos fx3 = this.mob.blockPosition();
        final SectionPos gp4 = SectionPos.of(fx3);
        final SectionPos gp5 = BehaviorUtils.findSectionClosestToVillage(aag2, gp4, 2);
        if (gp5 != gp4) {
            return RandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(gp5.center()));
        }
        return null;
    }
}
