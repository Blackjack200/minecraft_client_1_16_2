package net.minecraft.world.entity.ai.goal;

import java.util.List;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;

public class FollowFlockLeaderGoal extends Goal {
    private final AbstractSchoolingFish mob;
    private int timeToRecalcPath;
    private int nextStartTick;
    
    public FollowFlockLeaderGoal(final AbstractSchoolingFish azv) {
        this.mob = azv;
        this.nextStartTick = this.nextStartTick(azv);
    }
    
    protected int nextStartTick(final AbstractSchoolingFish azv) {
        return 200 + azv.getRandom().nextInt(200) % 20;
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        }
        if (this.mob.isFollower()) {
            return true;
        }
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        }
        this.nextStartTick = this.nextStartTick(this.mob);
        final Predicate<AbstractSchoolingFish> predicate2 = (Predicate<AbstractSchoolingFish>)(azv -> azv.canBeFollowed() || !azv.isFollower());
        final List<AbstractSchoolingFish> list3 = this.mob.level.<AbstractSchoolingFish>getEntitiesOfClass((java.lang.Class<? extends AbstractSchoolingFish>)this.mob.getClass(), this.mob.getBoundingBox().inflate(8.0, 8.0, 8.0), (java.util.function.Predicate<? super AbstractSchoolingFish>)predicate2);
        final AbstractSchoolingFish azv4 = (AbstractSchoolingFish)list3.stream().filter(AbstractSchoolingFish::canBeFollowed).findAny().orElse(this.mob);
        azv4.addFollowers((Stream<AbstractSchoolingFish>)list3.stream().filter(azv -> !azv.isFollower()));
        return this.mob.isFollower();
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.mob.isFollower() && this.mob.inRangeOfLeader();
    }
    
    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }
    
    @Override
    public void stop() {
        this.mob.stopFollowing();
    }
    
    @Override
    public void tick() {
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = 10;
        this.mob.pathToLeader();
    }
}
