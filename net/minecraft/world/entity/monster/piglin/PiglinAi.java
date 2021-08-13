package net.minecraft.world.entity.monster.piglin;

import net.minecraft.util.TimeUtil;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.RandomPos;
import java.util.UUID;
import net.minecraft.world.entity.ai.behavior.RunSometimes;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import java.util.Random;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import java.util.Iterator;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import java.util.Collections;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.DismountOrSkipMounting;
import net.minecraft.world.entity.ai.behavior.Mount;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.GoToCelebrateLocation;
import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.RunIf;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.Behavior;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.BiPredicate;
import net.minecraft.world.entity.ai.behavior.StartCelebratingIfTargetDead;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.ai.Brain;
import java.util.Set;
import net.minecraft.util.IntRange;
import net.minecraft.world.item.Item;

public class PiglinAi {
    public static final Item BARTERING_ITEM;
    private static final IntRange TIME_BETWEEN_HUNTS;
    private static final IntRange RIDE_START_INTERVAL;
    private static final IntRange RIDE_DURATION;
    private static final IntRange RETREAT_DURATION;
    private static final IntRange AVOID_ZOMBIFIED_DURATION;
    private static final IntRange BABY_AVOID_NEMESIS_DURATION;
    private static final Set<Item> FOOD_ITEMS;
    
    protected static Brain<?> makeBrain(final Piglin bep, final Brain<Piglin> arc) {
        initCoreActivity(arc);
        initIdleActivity(arc);
        initAdmireItemActivity(arc);
        initFightActivity(bep, arc);
        initCelebrateActivity(arc);
        initRetreatActivity(arc);
        initRideHoglinActivity(arc);
        arc.setCoreActivities((Set<Activity>)ImmutableSet.of(Activity.CORE));
        arc.setDefaultActivity(Activity.IDLE);
        arc.useDefaultActivity();
        return arc;
    }
    
    protected static void initMemories(final Piglin bep) {
        final int integer2 = PiglinAi.TIME_BETWEEN_HUNTS.randomValue(bep.level.random);
        bep.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, integer2);
    }
    
    private static void initCoreActivity(final Brain<Piglin> arc) {
        arc.addActivity(Activity.CORE, 0, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new InteractWithDoor(), babyAvoidNemesis(), avoidZombified(), new StopHoldingItemIfNoLongerAdmiring(), new StartAdmiringItemIfSeen(120), new StartCelebratingIfTargetDead(300, (BiPredicate<LivingEntity, LivingEntity>)PiglinAi::wantsToDance), new StopBeingAngryIfTargetDead()));
    }
    
    private static void initIdleActivity(final Brain<Piglin> arc) {
        arc.addActivity(Activity.IDLE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(new SetEntityLookTarget((Predicate<LivingEntity>)PiglinAi::isPlayerHoldingLovedItem, 14.0f), new StartAttacking((java.util.function.Predicate<Mob>)AbstractPiglin::isAdult, (java.util.function.Function<Mob, Optional<? extends LivingEntity>>)PiglinAi::findNearestValidAttackTarget), new RunIf((java.util.function.Predicate<LivingEntity>)Piglin::canHunt, new StartHuntingHoglin<>()), avoidRepellent(), babySometimesRideBabyHoglin(), createIdleLookBehaviors(), createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
    }
    
    private static void initFightActivity(final Piglin bep, final Brain<Piglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(new StopAttackingIfTargetInvalid((Predicate<LivingEntity>)(aqj -> !isNearestValidAttackTarget(bep, aqj))), new RunIf((java.util.function.Predicate<LivingEntity>)PiglinAi::hasCrossbow, new BackUpIfTooClose<>(5, 0.75f)), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0f), new MeleeAttack(20), new CrossbowAttack(), new RememberIfHoglinWasKilled(), new EraseMemoryIf((java.util.function.Predicate<LivingEntity>)PiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
    }
    
    private static void initCelebrateActivity(final Brain<Piglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(avoidRepellent(), new SetEntityLookTarget((Predicate<LivingEntity>)PiglinAi::isPlayerHoldingLovedItem, 14.0f), new StartAttacking((java.util.function.Predicate<Mob>)AbstractPiglin::isAdult, (java.util.function.Function<Mob, Optional<? extends LivingEntity>>)PiglinAi::findNearestValidAttackTarget), new RunIf((java.util.function.Predicate<LivingEntity>)(bep -> !bep.isDancing()), new GoToCelebrateLocation<>(2, 1.0f)), new RunIf((java.util.function.Predicate<LivingEntity>)Piglin::isDancing, new GoToCelebrateLocation<>(4, 0.6f)), new RunOne((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super LivingEntity>, Integer>>)ImmutableList.of((Object)Pair.of((Object)new SetEntityLookTarget(EntityType.PIGLIN, 8.0f), (Object)1), (Object)Pair.of((Object)new RandomStroll(0.6f, 2, 1), (Object)1), (Object)Pair.of((Object)new DoNothing(10, 20), (Object)1)))), MemoryModuleType.CELEBRATE_LOCATION);
    }
    
    private static void initAdmireItemActivity(final Brain<Piglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(new GoToWantedItem((java.util.function.Predicate<LivingEntity>)PiglinAi::isNotHoldingLovedItemInOffHand, 1.0f, true, 9), new StopAdmiringIfItemTooFarAway(9), new StopAdmiringIfTiredOfTryingToReachItem(200, 200)), MemoryModuleType.ADMIRING_ITEM);
    }
    
    private static void initRetreatActivity(final Brain<Piglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0f, 12, true), createIdleLookBehaviors(), createIdleMovementBehaviors(), new EraseMemoryIf((java.util.function.Predicate<LivingEntity>)PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }
    
    private static void initRideHoglinActivity(final Brain<Piglin> arc) {
        arc.addActivityAndRemoveMemoryWhenStopped(Activity.RIDE, 10, (com.google.common.collect.ImmutableList<? extends Behavior<? super Piglin>>)ImmutableList.of(new Mount(0.8f), new SetEntityLookTarget((Predicate<LivingEntity>)PiglinAi::isPlayerHoldingLovedItem, 8.0f), new RunIf((java.util.function.Predicate<LivingEntity>)Entity::isPassenger, createIdleLookBehaviors()), new DismountOrSkipMounting(8, (java.util.function.BiPredicate<LivingEntity, Entity>)PiglinAi::wantsToStopRiding)), MemoryModuleType.RIDE_TARGET);
    }
    
    private static RunOne<Piglin> createIdleLookBehaviors() {
        return new RunOne<Piglin>((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super Piglin>, Integer>>)ImmutableList.of(Pair.of((Object)new SetEntityLookTarget(EntityType.PLAYER, 8.0f), (Object)1), Pair.of((Object)new SetEntityLookTarget(EntityType.PIGLIN, 8.0f), (Object)1), Pair.of((Object)new SetEntityLookTarget(8.0f), (Object)1), Pair.of((Object)new DoNothing(30, 60), (Object)1)));
    }
    
    private static RunOne<Piglin> createIdleMovementBehaviors() {
        return new RunOne<Piglin>((java.util.List<com.mojang.datafixers.util.Pair<Behavior<? super Piglin>, Integer>>)ImmutableList.of(Pair.of((Object)new RandomStroll(0.6f), (Object)2), Pair.of((Object)InteractWith.<LivingEntity>of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6f, 2), (Object)2), Pair.of((Object)new RunIf((java.util.function.Predicate<LivingEntity>)PiglinAi::doesntSeeAnyPlayerHoldingLovedItem, new SetWalkTargetFromLookTarget(0.6f, 3)), (Object)2), Pair.of((Object)new DoNothing(30, 60), (Object)1)));
    }
    
    private static SetWalkTargetAwayFrom<BlockPos> avoidRepellent() {
        return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0f, 8, false);
    }
    
    private static CopyMemoryWithExpiry<Piglin, LivingEntity> babyAvoidNemesis() {
        return new CopyMemoryWithExpiry<Piglin, LivingEntity>((java.util.function.Predicate<Piglin>)Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, PiglinAi.BABY_AVOID_NEMESIS_DURATION);
    }
    
    private static CopyMemoryWithExpiry<Piglin, LivingEntity> avoidZombified() {
        return new CopyMemoryWithExpiry<Piglin, LivingEntity>((java.util.function.Predicate<Piglin>)PiglinAi::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, PiglinAi.AVOID_ZOMBIFIED_DURATION);
    }
    
    protected static void updateActivity(final Piglin bep) {
        final Brain<Piglin> arc2 = bep.getBrain();
        final Activity bhc3 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        arc2.setActiveActivityToFirstValid((List<Activity>)ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
        final Activity bhc4 = (Activity)arc2.getActiveNonCoreActivity().orElse(null);
        if (bhc3 != bhc4) {
            getSoundForCurrentActivity(bep).ifPresent(bep::playSound);
        }
        bep.setAggressive(arc2.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
        if (!arc2.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isBabyRidingBaby(bep)) {
            bep.stopRiding();
        }
        if (!arc2.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION)) {
            arc2.<Boolean>eraseMemory(MemoryModuleType.DANCING);
        }
        bep.setDancing(arc2.hasMemoryValue(MemoryModuleType.DANCING));
    }
    
    private static boolean isBabyRidingBaby(final Piglin bep) {
        if (!bep.isBaby()) {
            return false;
        }
        final Entity apx2 = bep.getVehicle();
        return (apx2 instanceof Piglin && ((Piglin)apx2).isBaby()) || (apx2 instanceof Hoglin && ((Hoglin)apx2).isBaby());
    }
    
    protected static void pickUpItem(final Piglin bep, final ItemEntity bcs) {
        stopWalking(bep);
        ItemStack bly3;
        if (bcs.getItem().getItem() == Items.GOLD_NUGGET) {
            bep.take(bcs, bcs.getItem().getCount());
            bly3 = bcs.getItem();
            bcs.remove();
        }
        else {
            bep.take(bcs, 1);
            bly3 = removeOneItemFromItemEntity(bcs);
        }
        final Item blu4 = bly3.getItem();
        if (isLovedItem(blu4)) {
            bep.getBrain().<Integer>eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(bep, bly3);
            admireGoldItem(bep);
            return;
        }
        if (isFood(blu4) && !hasEatenRecently(bep)) {
            eat(bep);
            return;
        }
        final boolean boolean5 = bep.equipItemIfPossible(bly3);
        if (boolean5) {
            return;
        }
        putInInventory(bep, bly3);
    }
    
    private static void holdInOffhand(final Piglin bep, final ItemStack bly) {
        if (isHoldingItemInOffHand(bep)) {
            bep.spawnAtLocation(bep.getItemInHand(InteractionHand.OFF_HAND));
        }
        bep.holdInOffHand(bly);
    }
    
    private static ItemStack removeOneItemFromItemEntity(final ItemEntity bcs) {
        final ItemStack bly2 = bcs.getItem();
        final ItemStack bly3 = bly2.split(1);
        if (bly2.isEmpty()) {
            bcs.remove();
        }
        else {
            bcs.setItem(bly2);
        }
        return bly3;
    }
    
    protected static void stopHoldingOffHandItem(final Piglin bep, final boolean boolean2) {
        final ItemStack bly3 = bep.getItemInHand(InteractionHand.OFF_HAND);
        bep.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        if (bep.isAdult()) {
            final boolean boolean3 = isBarterCurrency(bly3.getItem());
            if (boolean2 && boolean3) {
                throwItems(bep, getBarterResponseItems(bep));
            }
            else if (!boolean3) {
                final boolean boolean4 = bep.equipItemIfPossible(bly3);
                if (!boolean4) {
                    putInInventory(bep, bly3);
                }
            }
        }
        else {
            final boolean boolean3 = bep.equipItemIfPossible(bly3);
            if (!boolean3) {
                final ItemStack bly4 = bep.getMainHandItem();
                if (isLovedItem(bly4.getItem())) {
                    putInInventory(bep, bly4);
                }
                else {
                    throwItems(bep, (List<ItemStack>)Collections.singletonList(bly4));
                }
                bep.holdInMainHand(bly3);
            }
        }
    }
    
    protected static void cancelAdmiring(final Piglin bep) {
        if (isAdmiringItem(bep) && !bep.getOffhandItem().isEmpty()) {
            bep.spawnAtLocation(bep.getOffhandItem());
            bep.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }
    
    private static void putInInventory(final Piglin bep, final ItemStack bly) {
        final ItemStack bly2 = bep.addToInventory(bly);
        throwItemsTowardRandomPos(bep, (List<ItemStack>)Collections.singletonList(bly2));
    }
    
    private static void throwItems(final Piglin bep, final List<ItemStack> list) {
        final Optional<Player> optional3 = bep.getBrain().<Player>getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional3.isPresent()) {
            throwItemsTowardPlayer(bep, (Player)optional3.get(), list);
        }
        else {
            throwItemsTowardRandomPos(bep, list);
        }
    }
    
    private static void throwItemsTowardRandomPos(final Piglin bep, final List<ItemStack> list) {
        throwItemsTowardPos(bep, list, getRandomNearbyPos(bep));
    }
    
    private static void throwItemsTowardPlayer(final Piglin bep, final Player bft, final List<ItemStack> list) {
        throwItemsTowardPos(bep, list, bft.position());
    }
    
    private static void throwItemsTowardPos(final Piglin bep, final List<ItemStack> list, final Vec3 dck) {
        if (!list.isEmpty()) {
            bep.swing(InteractionHand.OFF_HAND);
            for (final ItemStack bly5 : list) {
                BehaviorUtils.throwItem(bep, bly5, dck.add(0.0, 1.0, 0.0));
            }
        }
    }
    
    private static List<ItemStack> getBarterResponseItems(final Piglin bep) {
        final LootTable cyv2 = bep.level.getServer().getLootTables().get(BuiltInLootTables.PIGLIN_BARTERING);
        final List<ItemStack> list3 = cyv2.getRandomItems(new LootContext.Builder((ServerLevel)bep.level).<Entity>withParameter(LootContextParams.THIS_ENTITY, bep).withRandom(bep.level.random).create(LootContextParamSets.PIGLIN_BARTER));
        return list3;
    }
    
    private static boolean wantsToDance(final LivingEntity aqj1, final LivingEntity aqj2) {
        return aqj2.getType() == EntityType.HOGLIN && new Random(aqj1.level.getGameTime()).nextFloat() < 0.1f;
    }
    
    protected static boolean wantsToPickup(final Piglin bep, final ItemStack bly) {
        final Item blu3 = bly.getItem();
        if (blu3.is(ItemTags.PIGLIN_REPELLENTS)) {
            return false;
        }
        if (isAdmiringDisabled(bep) && bep.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        }
        if (isBarterCurrency(blu3)) {
            return isNotHoldingLovedItemInOffHand(bep);
        }
        final boolean boolean4 = bep.canAddToInventory(bly);
        if (blu3 == Items.GOLD_NUGGET) {
            return boolean4;
        }
        if (isFood(blu3)) {
            return !hasEatenRecently(bep) && boolean4;
        }
        if (isLovedItem(blu3)) {
            return isNotHoldingLovedItemInOffHand(bep) && boolean4;
        }
        return bep.canReplaceCurrentItem(bly);
    }
    
    protected static boolean isLovedItem(final Item blu) {
        return blu.is(ItemTags.PIGLIN_LOVED);
    }
    
    private static boolean wantsToStopRiding(final Piglin bep, final Entity apx) {
        if (apx instanceof Mob) {
            final Mob aqk3 = (Mob)apx;
            return !aqk3.isBaby() || !aqk3.isAlive() || wasHurtRecently(bep) || wasHurtRecently(aqk3) || (aqk3 instanceof Piglin && aqk3.getVehicle() == null);
        }
        return false;
    }
    
    private static boolean isNearestValidAttackTarget(final Piglin bep, final LivingEntity aqj) {
        return findNearestValidAttackTarget(bep).filter(aqj2 -> aqj2 == aqj).isPresent();
    }
    
    private static boolean isNearZombified(final Piglin bep) {
        final Brain<Piglin> arc2 = bep.getBrain();
        if (arc2.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
            final LivingEntity aqj3 = (LivingEntity)arc2.<LivingEntity>getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
            return bep.closerThan(aqj3, 6.0);
        }
        return false;
    }
    
    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(final Piglin bep) {
        final Brain<Piglin> arc2 = bep.getBrain();
        if (isNearZombified(bep)) {
            return Optional.empty();
        }
        final Optional<LivingEntity> optional3 = BehaviorUtils.getLivingEntityFromUUIDMemory(bep, MemoryModuleType.ANGRY_AT);
        if (optional3.isPresent() && isAttackAllowed((LivingEntity)optional3.get())) {
            return optional3;
        }
        if (arc2.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
            final Optional<Player> optional4 = arc2.<Player>getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
            if (optional4.isPresent()) {
                return optional4;
            }
        }
        final Optional<Mob> optional5 = arc2.<Mob>getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
        if (optional5.isPresent()) {
            return optional5;
        }
        final Optional<Player> optional6 = arc2.<Player>getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
        if (optional6.isPresent() && isAttackAllowed((LivingEntity)optional6.get())) {
            return optional6;
        }
        return Optional.empty();
    }
    
    public static void angerNearbyPiglins(final Player bft, final boolean boolean2) {
        final List<Piglin> list3 = bft.level.<Piglin>getEntitiesOfClass((java.lang.Class<? extends Piglin>)Piglin.class, bft.getBoundingBox().inflate(16.0));
        list3.stream().filter(PiglinAi::isIdle).filter(bep -> !boolean2 || BehaviorUtils.canSee(bep, bft)).forEach(bep -> {
            if (bep.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                setAngerTargetToNearestTargetablePlayerIfFound(bep, bft);
            }
            else {
                setAngerTarget(bep, bft);
            }
        });
    }
    
    public static InteractionResult mobInteract(final Piglin bep, final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (canAdmire(bep, bly4)) {
            final ItemStack bly5 = bly4.split(1);
            holdInOffhand(bep, bly5);
            admireGoldItem(bep);
            stopWalking(bep);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
    
    protected static boolean canAdmire(final Piglin bep, final ItemStack bly) {
        return !isAdmiringDisabled(bep) && !isAdmiringItem(bep) && bep.isAdult() && isBarterCurrency(bly.getItem());
    }
    
    protected static void wasHurtBy(final Piglin bep, final LivingEntity aqj) {
        if (aqj instanceof Piglin) {
            return;
        }
        if (isHoldingItemInOffHand(bep)) {
            stopHoldingOffHandItem(bep, false);
        }
        final Brain<Piglin> arc3 = bep.getBrain();
        arc3.<BlockPos>eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
        arc3.<Boolean>eraseMemory(MemoryModuleType.DANCING);
        arc3.<Boolean>eraseMemory(MemoryModuleType.ADMIRING_ITEM);
        if (aqj instanceof Player) {
            arc3.<Boolean>setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
        }
        getAvoidTarget(bep).ifPresent(aqj3 -> {
            if (aqj3.getType() != aqj.getType()) {
                arc3.<LivingEntity>eraseMemory(MemoryModuleType.AVOID_TARGET);
            }
        });
        if (bep.isBaby()) {
            arc3.<LivingEntity>setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, aqj, 100L);
            if (isAttackAllowed(aqj)) {
                broadcastAngerTarget(bep, aqj);
            }
            return;
        }
        if (aqj.getType() == EntityType.HOGLIN && hoglinsOutnumberPiglins(bep)) {
            setAvoidTargetAndDontHuntForAWhile(bep, aqj);
            broadcastRetreat(bep, aqj);
            return;
        }
        maybeRetaliate(bep, aqj);
    }
    
    protected static void maybeRetaliate(final AbstractPiglin beo, final LivingEntity aqj) {
        if (beo.getBrain().isActive(Activity.AVOID)) {
            return;
        }
        if (!isAttackAllowed(aqj)) {
            return;
        }
        if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(beo, aqj, 4.0)) {
            return;
        }
        if (aqj.getType() == EntityType.PLAYER && beo.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            setAngerTargetToNearestTargetablePlayerIfFound(beo, aqj);
            broadcastUniversalAnger(beo);
        }
        else {
            setAngerTarget(beo, aqj);
            broadcastAngerTarget(beo, aqj);
        }
    }
    
    public static Optional<SoundEvent> getSoundForCurrentActivity(final Piglin bep) {
        return (Optional<SoundEvent>)bep.getBrain().getActiveNonCoreActivity().map(bhc -> getSoundForActivity(bep, bhc));
    }
    
    private static SoundEvent getSoundForActivity(final Piglin bep, final Activity bhc) {
        if (bhc == Activity.FIGHT) {
            return SoundEvents.PIGLIN_ANGRY;
        }
        if (bep.isConverting()) {
            return SoundEvents.PIGLIN_RETREAT;
        }
        if (bhc == Activity.AVOID && isNearAvoidTarget(bep)) {
            return SoundEvents.PIGLIN_RETREAT;
        }
        if (bhc == Activity.ADMIRE_ITEM) {
            return SoundEvents.PIGLIN_ADMIRING_ITEM;
        }
        if (bhc == Activity.CELEBRATE) {
            return SoundEvents.PIGLIN_CELEBRATE;
        }
        if (seesPlayerHoldingLovedItem(bep)) {
            return SoundEvents.PIGLIN_JEALOUS;
        }
        if (isNearRepellent(bep)) {
            return SoundEvents.PIGLIN_RETREAT;
        }
        return SoundEvents.PIGLIN_AMBIENT;
    }
    
    private static boolean isNearAvoidTarget(final Piglin bep) {
        final Brain<Piglin> arc2 = bep.getBrain();
        return arc2.hasMemoryValue(MemoryModuleType.AVOID_TARGET) && ((LivingEntity)arc2.<LivingEntity>getMemory(MemoryModuleType.AVOID_TARGET).get()).closerThan(bep, 12.0);
    }
    
    protected static boolean hasAnyoneNearbyHuntedRecently(final Piglin bep) {
        return bep.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY) || getVisibleAdultPiglins(bep).stream().anyMatch(beo -> beo.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY));
    }
    
    private static List<AbstractPiglin> getVisibleAdultPiglins(final Piglin bep) {
        return (List<AbstractPiglin>)bep.getBrain().<List<AbstractPiglin>>getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
    }
    
    private static List<AbstractPiglin> getAdultPiglins(final AbstractPiglin beo) {
        return (List<AbstractPiglin>)beo.getBrain().<List<AbstractPiglin>>getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
    }
    
    public static boolean isWearingGold(final LivingEntity aqj) {
        final Iterable<ItemStack> iterable2 = aqj.getArmorSlots();
        for (final ItemStack bly4 : iterable2) {
            final Item blu5 = bly4.getItem();
            if (blu5 instanceof ArmorItem && ((ArmorItem)blu5).getMaterial() == ArmorMaterials.GOLD) {
                return true;
            }
        }
        return false;
    }
    
    private static void stopWalking(final Piglin bep) {
        bep.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        bep.getNavigation().stop();
    }
    
    private static RunSometimes<Piglin> babySometimesRideBabyHoglin() {
        return new RunSometimes<Piglin>(new CopyMemoryWithExpiry((java.util.function.Predicate<Mob>)Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, (MemoryModuleType<Object>)MemoryModuleType.RIDE_TARGET, PiglinAi.RIDE_DURATION), PiglinAi.RIDE_START_INTERVAL);
    }
    
    protected static void broadcastAngerTarget(final AbstractPiglin beo, final LivingEntity aqj) {
        getAdultPiglins(beo).forEach(beo -> {
            if (aqj.getType() == EntityType.HOGLIN && (!beo.canHunt() || !((Hoglin)aqj).canBeHunted())) {
                return;
            }
            setAngerTargetIfCloserThanCurrent(beo, aqj);
        });
    }
    
    protected static void broadcastUniversalAnger(final AbstractPiglin beo) {
        getAdultPiglins(beo).forEach(beo -> getNearestVisibleTargetablePlayer(beo).ifPresent(bft -> setAngerTarget(beo, bft)));
    }
    
    protected static void broadcastDontKillAnyMoreHoglinsForAWhile(final Piglin bep) {
        getVisibleAdultPiglins(bep).forEach(PiglinAi::dontKillAnyMoreHoglinsForAWhile);
    }
    
    protected static void setAngerTarget(final AbstractPiglin beo, final LivingEntity aqj) {
        if (!isAttackAllowed(aqj)) {
            return;
        }
        beo.getBrain().<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        beo.getBrain().<UUID>setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, aqj.getUUID(), 600L);
        if (aqj.getType() == EntityType.HOGLIN && beo.canHunt()) {
            dontKillAnyMoreHoglinsForAWhile(beo);
        }
        if (aqj.getType() == EntityType.PLAYER && beo.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            beo.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, true, 600L);
        }
    }
    
    private static void setAngerTargetToNearestTargetablePlayerIfFound(final AbstractPiglin beo, final LivingEntity aqj) {
        final Optional<Player> optional3 = getNearestVisibleTargetablePlayer(beo);
        if (optional3.isPresent()) {
            setAngerTarget(beo, (LivingEntity)optional3.get());
        }
        else {
            setAngerTarget(beo, aqj);
        }
    }
    
    private static void setAngerTargetIfCloserThanCurrent(final AbstractPiglin beo, final LivingEntity aqj) {
        final Optional<LivingEntity> optional3 = getAngerTarget(beo);
        final LivingEntity aqj2 = BehaviorUtils.getNearestTarget(beo, optional3, aqj);
        if (optional3.isPresent() && optional3.get() == aqj2) {
            return;
        }
        setAngerTarget(beo, aqj2);
    }
    
    private static Optional<LivingEntity> getAngerTarget(final AbstractPiglin beo) {
        return BehaviorUtils.getLivingEntityFromUUIDMemory(beo, MemoryModuleType.ANGRY_AT);
    }
    
    public static Optional<LivingEntity> getAvoidTarget(final Piglin bep) {
        if (bep.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return bep.getBrain().<LivingEntity>getMemory(MemoryModuleType.AVOID_TARGET);
        }
        return (Optional<LivingEntity>)Optional.empty();
    }
    
    public static Optional<Player> getNearestVisibleTargetablePlayer(final AbstractPiglin beo) {
        if (beo.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)) {
            return beo.getBrain().<Player>getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
        }
        return (Optional<Player>)Optional.empty();
    }
    
    private static void broadcastRetreat(final Piglin bep, final LivingEntity aqj) {
        getVisibleAdultPiglins(bep).stream().filter(beo -> beo instanceof Piglin).forEach(beo -> retreatFromNearestTarget((Piglin)beo, aqj));
    }
    
    private static void retreatFromNearestTarget(final Piglin bep, final LivingEntity aqj) {
        final Brain<Piglin> arc3 = bep.getBrain();
        LivingEntity aqj2 = aqj;
        aqj2 = BehaviorUtils.getNearestTarget(bep, arc3.<LivingEntity>getMemory(MemoryModuleType.AVOID_TARGET), aqj2);
        aqj2 = BehaviorUtils.getNearestTarget(bep, arc3.<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET), aqj2);
        setAvoidTargetAndDontHuntForAWhile(bep, aqj2);
    }
    
    private static boolean wantsToStopFleeing(final Piglin bep) {
        final Brain<Piglin> arc2 = bep.getBrain();
        if (!arc2.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        }
        final LivingEntity aqj3 = (LivingEntity)arc2.<LivingEntity>getMemory(MemoryModuleType.AVOID_TARGET).get();
        final EntityType<?> aqb4 = aqj3.getType();
        if (aqb4 == EntityType.HOGLIN) {
            return piglinsEqualOrOutnumberHoglins(bep);
        }
        return isZombified(aqb4) && !arc2.<LivingEntity>isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, aqj3);
    }
    
    private static boolean piglinsEqualOrOutnumberHoglins(final Piglin bep) {
        return !hoglinsOutnumberPiglins(bep);
    }
    
    private static boolean hoglinsOutnumberPiglins(final Piglin bep) {
        final int integer2 = (int)bep.getBrain().<Integer>getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(0) + 1;
        final int integer3 = (int)bep.getBrain().<Integer>getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(0);
        return integer3 > integer2;
    }
    
    private static void setAvoidTargetAndDontHuntForAWhile(final Piglin bep, final LivingEntity aqj) {
        bep.getBrain().<UUID>eraseMemory(MemoryModuleType.ANGRY_AT);
        bep.getBrain().<LivingEntity>eraseMemory(MemoryModuleType.ATTACK_TARGET);
        bep.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        bep.getBrain().<LivingEntity>setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, aqj, PiglinAi.RETREAT_DURATION.randomValue(bep.level.random));
        dontKillAnyMoreHoglinsForAWhile(bep);
    }
    
    protected static void dontKillAnyMoreHoglinsForAWhile(final AbstractPiglin beo) {
        beo.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, true, PiglinAi.TIME_BETWEEN_HUNTS.randomValue(beo.level.random));
    }
    
    private static void eat(final Piglin bep) {
        bep.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
    }
    
    private static Vec3 getRandomNearbyPos(final Piglin bep) {
        final Vec3 dck2 = RandomPos.getLandPos(bep, 4, 2);
        return (dck2 == null) ? bep.position() : dck2;
    }
    
    private static boolean hasEatenRecently(final Piglin bep) {
        return bep.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
    }
    
    protected static boolean isIdle(final AbstractPiglin beo) {
        return beo.getBrain().isActive(Activity.IDLE);
    }
    
    private static boolean hasCrossbow(final LivingEntity aqj) {
        return aqj.isHolding(Items.CROSSBOW);
    }
    
    private static void admireGoldItem(final LivingEntity aqj) {
        aqj.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }
    
    private static boolean isAdmiringItem(final Piglin bep) {
        return bep.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }
    
    private static boolean isBarterCurrency(final Item blu) {
        return blu == PiglinAi.BARTERING_ITEM;
    }
    
    private static boolean isFood(final Item blu) {
        return PiglinAi.FOOD_ITEMS.contains(blu);
    }
    
    private static boolean isAttackAllowed(final LivingEntity aqj) {
        return EntitySelector.ATTACK_ALLOWED.test(aqj);
    }
    
    private static boolean isNearRepellent(final Piglin bep) {
        return bep.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
    }
    
    private static boolean seesPlayerHoldingLovedItem(final LivingEntity aqj) {
        return aqj.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }
    
    private static boolean doesntSeeAnyPlayerHoldingLovedItem(final LivingEntity aqj) {
        return !seesPlayerHoldingLovedItem(aqj);
    }
    
    public static boolean isPlayerHoldingLovedItem(final LivingEntity aqj) {
        return aqj.getType() == EntityType.PLAYER && aqj.isHolding((Predicate<Item>)PiglinAi::isLovedItem);
    }
    
    private static boolean isAdmiringDisabled(final Piglin bep) {
        return bep.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }
    
    private static boolean wasHurtRecently(final LivingEntity aqj) {
        return aqj.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
    }
    
    private static boolean isHoldingItemInOffHand(final Piglin bep) {
        return !bep.getOffhandItem().isEmpty();
    }
    
    private static boolean isNotHoldingLovedItemInOffHand(final Piglin bep) {
        return bep.getOffhandItem().isEmpty() || !isLovedItem(bep.getOffhandItem().getItem());
    }
    
    public static boolean isZombified(final EntityType aqb) {
        return aqb == EntityType.ZOMBIFIED_PIGLIN || aqb == EntityType.ZOGLIN;
    }
    
    static {
        BARTERING_ITEM = Items.GOLD_INGOT;
        TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
        RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
        RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
        RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
        AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
        BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
        FOOD_ITEMS = (Set)ImmutableSet.of(Items.PORKCHOP, Items.COOKED_PORKCHOP);
    }
}
