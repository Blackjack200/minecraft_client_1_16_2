package net.minecraft.world.entity.ai.goal;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Stream;
import com.google.common.collect.Sets;
import java.util.EnumMap;
import java.util.EnumSet;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.function.Supplier;
import java.util.Set;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class GoalSelector {
    private static final Logger LOGGER;
    private static final WrappedGoal NO_GOAL;
    private final Map<Goal.Flag, WrappedGoal> lockedFlags;
    private final Set<WrappedGoal> availableGoals;
    private final Supplier<ProfilerFiller> profiler;
    private final EnumSet<Goal.Flag> disabledFlags;
    private int newGoalRate;
    
    public GoalSelector(final Supplier<ProfilerFiller> supplier) {
        this.lockedFlags = (Map<Goal.Flag, WrappedGoal>)new EnumMap((Class)Goal.Flag.class);
        this.availableGoals = (Set<WrappedGoal>)Sets.newLinkedHashSet();
        this.disabledFlags = (EnumSet<Goal.Flag>)EnumSet.noneOf((Class)Goal.Flag.class);
        this.newGoalRate = 3;
        this.profiler = supplier;
    }
    
    public void addGoal(final int integer, final Goal avs) {
        this.availableGoals.add(new WrappedGoal(integer, avs));
    }
    
    public void removeGoal(final Goal avs) {
        this.availableGoals.stream().filter(axi -> axi.getGoal() == avs).filter(WrappedGoal::isRunning).forEach(WrappedGoal::stop);
        this.availableGoals.removeIf(axi -> axi.getGoal() == avs);
    }
    
    public void tick() {
        final ProfilerFiller ant2 = (ProfilerFiller)this.profiler.get();
        ant2.push("goalCleanup");
        this.getRunningGoals().filter(axi -> !axi.isRunning() || axi.getFlags().stream().anyMatch(this.disabledFlags::contains) || !axi.canContinueToUse()).forEach(Goal::stop);
        this.lockedFlags.forEach((a, axi) -> {
            if (!axi.isRunning()) {
                this.lockedFlags.remove(a);
            }
        });
        ant2.pop();
        ant2.push("goalUpdate");
        this.availableGoals.stream().filter(axi -> !axi.isRunning()).filter(axi -> axi.getFlags().stream().noneMatch(this.disabledFlags::contains)).filter(axi -> axi.getFlags().stream().allMatch(a -> ((WrappedGoal)this.lockedFlags.getOrDefault(a, GoalSelector.NO_GOAL)).canBeReplacedBy(axi))).filter(WrappedGoal::canUse).forEach(axi -> {
            axi.getFlags().forEach(a -> {
                final WrappedGoal axi2 = (WrappedGoal)this.lockedFlags.getOrDefault(a, GoalSelector.NO_GOAL);
                axi2.stop();
                this.lockedFlags.put(a, axi);
            });
            axi.start();
        });
        ant2.pop();
        ant2.push("goalTick");
        this.getRunningGoals().forEach(WrappedGoal::tick);
        ant2.pop();
    }
    
    public Stream<WrappedGoal> getRunningGoals() {
        return (Stream<WrappedGoal>)this.availableGoals.stream().filter(WrappedGoal::isRunning);
    }
    
    public void disableControlFlag(final Goal.Flag a) {
        this.disabledFlags.add(a);
    }
    
    public void enableControlFlag(final Goal.Flag a) {
        this.disabledFlags.remove(a);
    }
    
    public void setControlFlag(final Goal.Flag a, final boolean boolean2) {
        if (boolean2) {
            this.enableControlFlag(a);
        }
        else {
            this.disableControlFlag(a);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        NO_GOAL = new WrappedGoal(Integer.MAX_VALUE, new Goal() {
            @Override
            public boolean canUse() {
                return false;
            }
        }) {
            @Override
            public boolean isRunning() {
                return false;
            }
        };
    }
}
