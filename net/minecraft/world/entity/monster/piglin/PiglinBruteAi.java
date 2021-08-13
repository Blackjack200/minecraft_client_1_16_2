package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
import net.minecraft.world.entity.ai.behavior.StrollToPoi;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.entity.Mob;
import java.util.function.Function;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.Behavior;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.GlobalPos;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.Brain;

public class PiglinBruteAi {
    protected static Brain<?> makeBrain(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        initCoreActivity(bes, arc);
        initIdleActivity(bes, arc);
        initFightActivity(bes, arc);
        arc.setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
        arc.setDefaultActivity(Activity.IDLE);
        arc.useDefaultActivity();
        return arc;
    }
    
    protected static void initMemories(final PiglinBrute bes) {
        final GlobalPos gf2 = GlobalPos.of(bes.level.dimension(), bes.blockPosition());
        bes.getBrain().<GlobalPos>setMemory(MemoryModuleType.HOME, gf2);
    }
    
    private static void initCoreActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivity(Activity.CORE, 0, (com.google.common.collect.ImmutableList<? extends Behavior<? super PiglinBrute>>)ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new InteractWithDoor(), new StopBeingAngryIfTargetDead()));
    }
    
    private static void initIdleActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivity(Activity.IDLE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super PiglinBrute>>)ImmutableList.of(new StartAttacking((java.util.function.Function<Mob, Optional<? extends LivingEntity>>)PiglinBruteAi::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
    }
    
    private static void initFightActivity(final PiglinBrute bes, final Brain<PiglinBrute> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super PiglinBrute>>)ImmutableList.of(new StopAttackingIfTargetInvalid((Predicate<LivingEntity>)(aqj -> !isNearestValidAttackTarget(bes, aqj))), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0f), new MeleeAttack(20)), MemoryModuleType.ATTACK_TARGET);
    }
    
    private static RunOne<PiglinBrute> createIdleLookBehaviors() {
        return new RunOne<PiglinBrute>((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super PiglinBrute>, Integer>>)ImmutableList.of(Pair.of((Object)new SetEntityLookTarget(EntityType.PLAYER, 8.0f), (Object)1), Pair.of((Object)new SetEntityLookTarget(EntityType.PIGLIN, 8.0f), (Object)1), Pair.of((Object)new SetEntityLookTarget(EntityType.PIGLIN_BRUTE, 8.0f), (Object)1), Pair.of((Object)new SetEntityLookTarget(8.0f), (Object)1), Pair.of((Object)new DoNothing(30, 60), (Object)1)));
    }
    
    private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
        return new RunOne<PiglinBrute>((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super PiglinBrute>, Integer>>)ImmutableList.of(Pair.of((Object)new RandomStroll(0.6f), (Object)2), Pair.of((Object)InteractWith.<LivingEntity>of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), (Object)2), Pair.of((Object)InteractWith.<LivingEntity>of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), (Object)2), Pair.of((Object)new StrollToPoi(MemoryModuleType.HOME, 0.6f, 2, 100), (Object)2), Pair.of((Object)new StrollAroundPoi(MemoryModuleType.HOME, 0.6f, 5), (Object)2), Pair.of((Object)new DoNothing(30, 60), (Object)1)));
    }
    
    protected static void updateActivity(final PiglinBrute bes) {
        final Brain<PiglinBrute> arc2 = bes.getBrain();
        final Activity bhc3 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        arc2.setActiveActivityToFirstValid((List<Activity>)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
        final Activity bhc4 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        if (bhc3 != bhc4) {
            playActivitySound(bes);
        }
        bes.setAggressive(arc2.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }
    
    private static boolean isNearestValidAttackTarget(final AbstractPiglin beo, final LivingEntity aqj) {
        return findNearestValidAttackTarget(beo).filter(aqj2 -> aqj2 == aqj).isPresent();
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(final AbstractPiglin beo) {
        final Optional<LivingEntity> optional2 = BehaviorUtils.getLivingEntityFromUUIDMemory(beo, MemoryModuleType.ANGRY_AT);
        if (optional2.isPresent() && isAttackAllowed((LivingEntity)optional2.get())) {
            return optional2;
        }
        final Optional<? extends LivingEntity> optional3 = getTargetIfWithinRange(beo, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        if (optional3.isPresent()) {
            return optional3;
        }
        return beo.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
    }
    
    private static boolean isAttackAllowed(final LivingEntity aqj) {
        return EntitySelector.ATTACK_ALLOWED.test(aqj);
    }
    
    private static Optional<? extends LivingEntity> getTargetIfWithinRange(final AbstractPiglin beo, final MemoryModuleType<? extends LivingEntity> aya) {
        return beo.getBrain().getMemory(aya).filter(aqj -> aqj.closerThan(beo, 12.0));
    }
    
    protected static void wasHurtBy(final PiglinBrute bes, final LivingEntity aqj) {
        if (aqj instanceof AbstractPiglin) {
            return;
        }
        PiglinAi.maybeRetaliate(bes, aqj);
    }
    
    protected static void maybePlayActivitySound(final PiglinBrute bes) {
        if (bes.level.random.nextFloat() < 0.0125) {
            playActivitySound(bes);
        }
    }
    
    private static void playActivitySound(final PiglinBrute bes) {
        bes.getBrain().getActiveNonCoreActivity().ifPresent(bhc -> {
            if (bhc == Activity.FIGHT) {
                bes.playAngrySound();
            }
        });
    }
}
