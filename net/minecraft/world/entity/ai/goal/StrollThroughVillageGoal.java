package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.SectionPos;
import java.util.Random;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import java.util.function.ToDoubleFunction;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.server.level.ServerLevel;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;

public class StrollThroughVillageGoal extends Goal {
    private final PathfinderMob mob;
    private final int interval;
    @Nullable
    private BlockPos wantedPos;
    
    public StrollThroughVillageGoal(final PathfinderMob aqr, final int integer) {
        this.mob = aqr;
        this.interval = integer;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        }
        if (this.mob.level.isDay()) {
            return false;
        }
        if (this.mob.getRandom().nextInt(this.interval) != 0) {
            return false;
        }
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final BlockPos fx3 = this.mob.blockPosition();
        if (!aag2.isCloseToVillage(fx3, 6)) {
            return false;
        }
        final Vec3 dck4 = RandomPos.getLandPos(this.mob, 15, 7, (ToDoubleFunction<BlockPos>)(fx -> -aag2.sectionsToVillage(SectionPos.of(fx))));
        this.wantedPos = ((dck4 == null) ? null : new BlockPos(dck4));
        return this.wantedPos != null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.wantedPos != null && !this.mob.getNavigation().isDone() && this.mob.getNavigation().getTargetPos().equals(this.wantedPos);
    }
    
    @Override
    public void tick() {
        if (this.wantedPos == null) {
            return;
        }
        final PathNavigation ayg2 = this.mob.getNavigation();
        if (ayg2.isDone() && !this.wantedPos.closerThan(this.mob.position(), 10.0)) {
            Vec3 dck3 = Vec3.atBottomCenterOf(this.wantedPos);
            final Vec3 dck4 = this.mob.position();
            final Vec3 dck5 = dck4.subtract(dck3);
            dck3 = dck5.scale(0.4).add(dck3);
            final Vec3 dck6 = dck3.subtract(dck4).normalize().scale(10.0).add(dck4);
            BlockPos fx7 = new BlockPos(dck6);
            fx7 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, fx7);
            if (!ayg2.moveTo(fx7.getX(), fx7.getY(), fx7.getZ(), 1.0)) {
                this.moveRandomly();
            }
        }
    }
    
    private void moveRandomly() {
        final Random random2 = this.mob.getRandom();
        final BlockPos fx3 = this.mob.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.mob.blockPosition().offset(-8 + random2.nextInt(16), 0, -8 + random2.nextInt(16)));
        this.mob.getNavigation().moveTo(fx3.getX(), fx3.getY(), fx3.getZ(), 1.0);
    }
}
