package net.minecraft.world.entity.monster.hoglin;

import net.minecraft.util.TimeUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.behavior.RunIf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.Mob;
import java.util.function.Function;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.BecomePassiveIfMemoryPresent;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.behavior.Behavior;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.util.IntRange;

public class HoglinAi {
    private static final IntRange RETREAT_DURATION;
    private static final IntRange ADULT_FOLLOW_RANGE;
    
    protected static Brain<?> makeBrain(final Brain<Hoglin> arc) {
        initCoreActivity(arc);
        initIdleActivity(arc);
        initFightActivity(arc);
        initRetreatActivity(arc);
        arc.setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
        arc.setDefaultActivity(Activity.IDLE);
        arc.useDefaultActivity();
        return arc;
    }
    
    private static void initCoreActivity(final Brain<Hoglin> arc) {
        arc.addActivity(Activity.CORE, 0, (com.google.common.collect.ImmutableList<? extends Behavior<? super Hoglin>>)ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }
    
    private static void initIdleActivity(final Brain<Hoglin> arc) {
        arc.addActivity(Activity.IDLE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Hoglin>>)ImmutableList.of(new BecomePassiveIfMemoryPresent(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6f), SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, true), new StartAttacking((java.util.function.Function<Mob, Optional<? extends LivingEntity>>)HoglinAi::findNearestValidAttackTarget), new RunIf((java.util.function.Predicate<LivingEntity>)Hoglin::isAdult, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4f, 8, false)), new RunSometimes(new SetEntityLookTarget(8.0f), IntRange.of(30, 60)), new BabyFollowAdult(HoglinAi.ADULT_FOLLOW_RANGE, 0.6f), createIdleMovementBehaviors()));
    }
    
    private static void initFightActivity(final Brain<Hoglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Hoglin>>)ImmutableList.of(new BecomePassiveIfMemoryPresent(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6f), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0f), new RunIf((java.util.function.Predicate<LivingEntity>)Hoglin::isAdult, new MeleeAttack(40)), new RunIf((java.util.function.Predicate<LivingEntity>)AgableMob::isBaby, new MeleeAttack(15)), new StopAttackingIfTargetInvalid(), new EraseMemoryIf((java.util.function.Predicate<LivingEntity>)HoglinAi::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
    }
    
    private static void initRetreatActivity(final Brain<Hoglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Hoglin>>)ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3f, 15, false), createIdleMovementBehaviors(), new RunSometimes(new SetEntityLookTarget(8.0f), IntRange.of(30, 60)), new EraseMemoryIf((java.util.function.Predicate<LivingEntity>)HoglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }
    
    private static RunOne<Hoglin> createIdleMovementBehaviors() {
        return new RunOne<Hoglin>((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super Hoglin>, Integer>>)ImmutableList.of(Pair.of((Object)new RandomStroll(0.4f), (Object)2), Pair.of((Object)new SetWalkTargetFromLookTarget(0.4f, 3), (Object)2), Pair.of((Object)new DoNothing(30, 60), (Object)1)));
    }
    
    protected static void updateActivity(final Hoglin bej) {
        final Brain<Hoglin> arc2 = bej.getBrain();
        final Activity bhc3 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        arc2.setActiveActivityToFirstValid((List<Activity>)ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        final Activity bhc4 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        if (bhc3 != bhc4) {
            getSoundForCurrentActivity(bej).ifPresent(bej::playSound);
        }
        bej.setAggressive(arc2.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
    
    protected static void onHitTarget(final Hoglin bej, final LivingEntity aqj) {
        if (bej.isBaby()) {
            return;
        }
        if (aqj.getType() == EntityType.PIGLIN && piglinsOutnumberHoglins(bej)) {
            setAvoidTarget(bej, aqj);
            broadcastRetreat(bej, aqj);
            return;
        }
        broadcastAttackTarget(bej, aqj);
    }
    
    private static void broadcastRetreat(final Hoglin bej, final LivingEntity aqj) {
        getVisibleAdultHoglins(bej).forEach(bej -> retreatFromNearestTarget(bej, aqj));
    }
    
    private static void retreatFromNearestTarget(final Hoglin bej, final LivingEntity aqj) {
        LivingEntity aqj2 = aqj;
        final Brain<Hoglin> arc4 = bej.getBrain();
        aqj2 = BehaviorUtils.getNearestTarget(bej, arc4.<LivingEntity>getMemory(MemoryModuleType.AVOID_TARGET), aqj2);
        aqj2 = BehaviorUtils.getNearestTarget(bej, arc4.<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET), aqj2);
        setAvoidTarget(bej, aqj2);
    }
    
    private static void setAvoidTarget(final Hoglin bej, final LivingEntity aqj) {
        bej.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.ATTACK_TARGET);
        bej.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        bej.getBrain().<LivingEntity>setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, aqj, HoglinAi.RETREAT_DURATION.randomValue(bej.level.random));
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(final Hoglin bej) {
        if (isPacified(bej) || isBreeding(bej)) {
            return Optional.empty();
        }
        return bej.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }
    
    static boolean isPosNearNearestRepellent(final Hoglin bej, final BlockPos fx) {
        final Optional<BlockPos> optional3 = bej.getBrain().<BlockPos>getMemory(MemoryModuleType.NEAREST_REPELLENT);
        return optional3.isPresent() && ((BlockPos)optional3.get()).closerThan(fx, 8.0);
    }
    
    private static boolean wantsToStopFleeing(final Hoglin bej) {
        return bej.isAdult() && !piglinsOutnumberHoglins(bej);
    }
    
    private static boolean piglinsOutnumberHoglins(final Hoglin bej) {
        if (bej.isBaby()) {
            return false;
        }
        final int integer2 = (int)bej.getBrain().<Integer>getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0);
        final int integer3 = (int)bej.getBrain().<Integer>getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0) + 1;
        return integer2 > integer3;
    }
    
    protected static void wasHurtBy(final Hoglin bej, final LivingEntity aqj) {
        final Brain<Hoglin> arc3 = bej.getBrain();
        arc3.<Boolean>eraseMemory(MemoryModuleType.PACIFIED);
        arc3.<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
        if (bej.isBaby()) {
            retreatFromNearestTarget(bej, aqj);
            return;
        }
        maybeRetaliate(bej, aqj);
    }
    
    private static void maybeRetaliate(final Hoglin bej, final LivingEntity aqj) {
        if (bej.getBrain().isActive(Activity.AVOID) && aqj.getType() == EntityType.PIGLIN) {
            return;
        }
        if (!EntitySelector.ATTACK_ALLOWED.test(aqj)) {
            return;
        }
        if (aqj.getType() == EntityType.HOGLIN) {
            return;
        }
        if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(bej, aqj, 4.0)) {
            return;
        }
        setAttackTarget(bej, aqj);
        broadcastAttackTarget(bej, aqj);
    }
    
    private static void setAttackTarget(final Hoglin bej, final LivingEntity aqj) {
        final Brain<Hoglin> arc3 = bej.getBrain();
        arc3.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        arc3.<AgableMob>eraseMemory(MemoryModuleType.BREED_TARGET);
        arc3.<LivingEntity>setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, aqj, 200L);
    }
    
    private static void broadcastAttackTarget(final Hoglin bej, final LivingEntity aqj) {
        getVisibleAdultHoglins(bej).forEach(bej -> setAttackTargetIfCloserThanCurrent(bej, aqj));
    }
    
    private static void setAttackTargetIfCloserThanCurrent(final Hoglin bej, final LivingEntity aqj) {
        if (isPacified(bej)) {
            return;
        }
        final Optional<LivingEntity> optional3 = bej.getBrain().<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET);
        final LivingEntity aqj2 = BehaviorUtils.getNearestTarget(bej, optional3, aqj);
        setAttackTarget(bej, aqj2);
    }
    
    public static Optional<SoundEvent> getSoundForCurrentActivity(final Hoglin bej) {
        return (Optional<SoundEvent>)bej.getBrain().getActiveNonCoreActivity().map(bhc -> getSoundForActivity(bej, bhc));
    }
    
    private static SoundEvent getSoundForActivity(final Hoglin bej, final Activity bhc) {
        if (bhc == Activity.AVOID || bej.isConverting()) {
            return SoundEvents.HOGLIN_RETREAT;
        }
        if (bhc == Activity.FIGHT) {
            return SoundEvents.HOGLIN_ANGRY;
        }
        if (isNearRepellent(bej)) {
            return SoundEvents.HOGLIN_RETREAT;
        }
        return SoundEvents.HOGLIN_AMBIENT;
    }
    
    private static List<Hoglin> getVisibleAdultHoglins(final Hoglin bej) {
        return (List<Hoglin>)bej.getBrain().<List<Hoglin>>getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS).orElse(ImmutableList.of());
    }
    
    private static boolean isNearRepellent(final Hoglin bej) {
        return bej.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }
    
    private static boolean isBreeding(final Hoglin bej) {
        return bej.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
    }
    
    protected static boolean isPacified(final Hoglin bej) {
        return bej.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
    }
    
    static {
        RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
        ADULT_FOLLOW_RANGE = IntRange.of(5, 16);
    }
}
