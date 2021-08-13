package net.minecraft.world.entity.ai.goal;

import java.util.Iterator;
import java.util.Objects;
import net.minecraft.core.Position;
import net.minecraft.world.level.pathfinder.Node;
import java.util.Optional;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.ToDoubleFunction;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import java.util.EnumSet;
import com.google.common.collect.Lists;
import java.util.function.BooleanSupplier;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.PathfinderMob;

public class MoveThroughVillageGoal extends Goal {
    protected final PathfinderMob mob;
    private final double speedModifier;
    private Path path;
    private BlockPos poiPos;
    private final boolean onlyAtNight;
    private final List<BlockPos> visited;
    private final int distanceToPoi;
    private final BooleanSupplier canDealWithDoors;
    
    public MoveThroughVillageGoal(final PathfinderMob aqr, final double double2, final boolean boolean3, final int integer, final BooleanSupplier booleanSupplier) {
        this.visited = (List<BlockPos>)Lists.newArrayList();
        this.mob = aqr;
        this.speedModifier = double2;
        this.onlyAtNight = boolean3;
        this.distanceToPoi = integer;
        this.canDealWithDoors = booleanSupplier;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
        if (!GoalUtils.hasGroundPathNavigation(aqr)) {
            throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal");
        }
    }
    
    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        }
        this.updateVisited();
        if (this.onlyAtNight && this.mob.level.isDay()) {
            return false;
        }
        final ServerLevel aag2 = (ServerLevel)this.mob.level;
        final BlockPos fx3 = this.mob.blockPosition();
        if (!aag2.isCloseToVillage(fx3, 6)) {
            return false;
        }
        final Vec3 dck4 = RandomPos.getLandPos(this.mob, 15, 7, (ToDoubleFunction<BlockPos>)(fx3 -> {
            if (!aag2.isVillage(fx3)) {
                return Double.NEGATIVE_INFINITY;
            }
            final Optional<BlockPos> optional5 = aag2.getPoiManager().find(PoiType.ALL, (Predicate<BlockPos>)this::hasNotVisited, fx3, 10, PoiManager.Occupancy.IS_OCCUPIED);
            if (!optional5.isPresent()) {
                return Double.NEGATIVE_INFINITY;
            }
            return -((BlockPos)optional5.get()).distSqr(fx3);
        }));
        if (dck4 == null) {
            return false;
        }
        final Optional<BlockPos> optional5 = aag2.getPoiManager().find(PoiType.ALL, (Predicate<BlockPos>)this::hasNotVisited, new BlockPos(dck4), 10, PoiManager.Occupancy.IS_OCCUPIED);
        if (!optional5.isPresent()) {
            return false;
        }
        this.poiPos = ((BlockPos)optional5.get()).immutable();
        final GroundPathNavigation ayf6 = (GroundPathNavigation)this.mob.getNavigation();
        final boolean boolean7 = ayf6.canOpenDoors();
        ayf6.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
        this.path = ayf6.createPath(this.poiPos, 0);
        ayf6.setCanOpenDoors(boolean7);
        if (this.path == null) {
            final Vec3 dck5 = RandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(this.poiPos));
            if (dck5 == null) {
                return false;
            }
            ayf6.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
            this.path = this.mob.getNavigation().createPath(dck5.x, dck5.y, dck5.z, 0);
            ayf6.setCanOpenDoors(boolean7);
            if (this.path == null) {
                return false;
            }
        }
        for (int integer8 = 0; integer8 < this.path.getNodeCount(); ++integer8) {
            final Node cwy9 = this.path.getNode(integer8);
            final BlockPos fx4 = new BlockPos(cwy9.x, cwy9.y + 1, cwy9.z);
            if (DoorBlock.isWoodenDoor(this.mob.level, fx4)) {
                this.path = this.mob.getNavigation().createPath(cwy9.x, cwy9.y, cwy9.z, 0);
                break;
            }
        }
        return this.path != null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && !this.poiPos.closerThan(this.mob.position(), this.mob.getBbWidth() + this.distanceToPoi);
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
    }
    
    @Override
    public void stop() {
        if (this.mob.getNavigation().isDone() || this.poiPos.closerThan(this.mob.position(), this.distanceToPoi)) {
            this.visited.add(this.poiPos);
        }
    }
    
    private boolean hasNotVisited(final BlockPos fx) {
        for (final BlockPos fx2 : this.visited) {
            if (Objects.equals(fx, fx2)) {
                return false;
            }
        }
        return true;
    }
    
    private void updateVisited() {
        if (this.visited.size() > 15) {
            this.visited.remove(0);
        }
    }
}
