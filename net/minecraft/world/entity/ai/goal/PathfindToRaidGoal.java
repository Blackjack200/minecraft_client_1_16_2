package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.raid.Raids;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Collection;
import java.util.function.Predicate;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import java.util.EnumSet;
import net.minecraft.world.entity.raid.Raider;

public class PathfindToRaidGoal<T extends Raider> extends Goal {
    private final T mob;
    
    public PathfindToRaidGoal(final T bgz) {
        this.mob = bgz;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null && !this.mob.isVehicle() && this.mob.hasActiveRaid() && !this.mob.getCurrentRaid().isOver() && !((ServerLevel)this.mob.level).isVillage(this.mob.blockPosition());
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.mob.hasActiveRaid() && !this.mob.getCurrentRaid().isOver() && this.mob.level instanceof ServerLevel && !((ServerLevel)this.mob.level).isVillage(this.mob.blockPosition());
    }
    
    @Override
    public void tick() {
        if (this.mob.hasActiveRaid()) {
            final Raid bgy2 = this.mob.getCurrentRaid();
            if (this.mob.tickCount % 20 == 0) {
                this.recruitNearby(bgy2);
            }
            if (!this.mob.isPathFinding()) {
                final Vec3 dck3 = RandomPos.getPosTowards(this.mob, 15, 4, Vec3.atBottomCenterOf(bgy2.getCenter()));
                if (dck3 != null) {
                    this.mob.getNavigation().moveTo(dck3.x, dck3.y, dck3.z, 1.0);
                }
            }
        }
    }
    
    private void recruitNearby(final Raid bgy) {
        if (bgy.isActive()) {
            final Set<Raider> set3 = (Set<Raider>)Sets.newHashSet();
            final List<Raider> list4 = this.mob.level.<Raider>getEntitiesOfClass((java.lang.Class<? extends Raider>)Raider.class, this.mob.getBoundingBox().inflate(16.0), (java.util.function.Predicate<? super Raider>)(bgz -> !bgz.hasActiveRaid() && Raids.canJoinRaid(bgz, bgy)));
            set3.addAll((Collection)list4);
            for (final Raider bgz6 : set3) {
                bgy.joinRaid(bgy.getGroupsSpawned(), bgz6, null, true);
            }
        }
    }
}
