package net.minecraft.world.entity.ai.memory;

import net.minecraft.core.SerializableUUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import java.util.UUID;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import java.util.List;
import net.minecraft.core.GlobalPos;

public class MemoryModuleType<U> {
    public static final MemoryModuleType<Void> DUMMY;
    public static final MemoryModuleType<GlobalPos> HOME;
    public static final MemoryModuleType<GlobalPos> JOB_SITE;
    public static final MemoryModuleType<GlobalPos> POTENTIAL_JOB_SITE;
    public static final MemoryModuleType<GlobalPos> MEETING_POINT;
    public static final MemoryModuleType<List<GlobalPos>> SECONDARY_JOB_SITE;
    public static final MemoryModuleType<List<LivingEntity>> LIVING_ENTITIES;
    public static final MemoryModuleType<List<LivingEntity>> VISIBLE_LIVING_ENTITIES;
    public static final MemoryModuleType<List<LivingEntity>> VISIBLE_VILLAGER_BABIES;
    public static final MemoryModuleType<List<Player>> NEAREST_PLAYERS;
    public static final MemoryModuleType<Player> NEAREST_VISIBLE_PLAYER;
    public static final MemoryModuleType<Player> NEAREST_VISIBLE_TARGETABLE_PLAYER;
    public static final MemoryModuleType<WalkTarget> WALK_TARGET;
    public static final MemoryModuleType<PositionTracker> LOOK_TARGET;
    public static final MemoryModuleType<LivingEntity> ATTACK_TARGET;
    public static final MemoryModuleType<Boolean> ATTACK_COOLING_DOWN;
    public static final MemoryModuleType<LivingEntity> INTERACTION_TARGET;
    public static final MemoryModuleType<AgableMob> BREED_TARGET;
    public static final MemoryModuleType<Entity> RIDE_TARGET;
    public static final MemoryModuleType<Path> PATH;
    public static final MemoryModuleType<List<GlobalPos>> INTERACTABLE_DOORS;
    public static final MemoryModuleType<Set<GlobalPos>> DOORS_TO_CLOSE;
    public static final MemoryModuleType<BlockPos> NEAREST_BED;
    public static final MemoryModuleType<DamageSource> HURT_BY;
    public static final MemoryModuleType<LivingEntity> HURT_BY_ENTITY;
    public static final MemoryModuleType<LivingEntity> AVOID_TARGET;
    public static final MemoryModuleType<LivingEntity> NEAREST_HOSTILE;
    public static final MemoryModuleType<GlobalPos> HIDING_PLACE;
    public static final MemoryModuleType<Long> HEARD_BELL_TIME;
    public static final MemoryModuleType<Long> CANT_REACH_WALK_TARGET_SINCE;
    public static final MemoryModuleType<Boolean> GOLEM_DETECTED_RECENTLY;
    public static final MemoryModuleType<Long> LAST_SLEPT;
    public static final MemoryModuleType<Long> LAST_WOKEN;
    public static final MemoryModuleType<Long> LAST_WORKED_AT_POI;
    public static final MemoryModuleType<AgableMob> NEAREST_VISIBLE_ADULT;
    public static final MemoryModuleType<ItemEntity> NEAREST_VISIBLE_WANTED_ITEM;
    public static final MemoryModuleType<Mob> NEAREST_VISIBLE_NEMESIS;
    public static final MemoryModuleType<UUID> ANGRY_AT;
    public static final MemoryModuleType<Boolean> UNIVERSAL_ANGER;
    public static final MemoryModuleType<Boolean> ADMIRING_ITEM;
    public static final MemoryModuleType<Integer> TIME_TRYING_TO_REACH_ADMIRE_ITEM;
    public static final MemoryModuleType<Boolean> DISABLE_WALK_TO_ADMIRE_ITEM;
    public static final MemoryModuleType<Boolean> ADMIRING_DISABLED;
    public static final MemoryModuleType<Boolean> HUNTED_RECENTLY;
    public static final MemoryModuleType<BlockPos> CELEBRATE_LOCATION;
    public static final MemoryModuleType<Boolean> DANCING;
    public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_HUNTABLE_HOGLIN;
    public static final MemoryModuleType<Hoglin> NEAREST_VISIBLE_BABY_HOGLIN;
    public static final MemoryModuleType<Player> NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD;
    public static final MemoryModuleType<List<AbstractPiglin>> NEARBY_ADULT_PIGLINS;
    public static final MemoryModuleType<List<AbstractPiglin>> NEAREST_VISIBLE_ADULT_PIGLINS;
    public static final MemoryModuleType<List<Hoglin>> NEAREST_VISIBLE_ADULT_HOGLINS;
    public static final MemoryModuleType<AbstractPiglin> NEAREST_VISIBLE_ADULT_PIGLIN;
    public static final MemoryModuleType<LivingEntity> NEAREST_VISIBLE_ZOMBIFIED;
    public static final MemoryModuleType<Integer> VISIBLE_ADULT_PIGLIN_COUNT;
    public static final MemoryModuleType<Integer> VISIBLE_ADULT_HOGLIN_COUNT;
    public static final MemoryModuleType<Player> NEAREST_PLAYER_HOLDING_WANTED_ITEM;
    public static final MemoryModuleType<Boolean> ATE_RECENTLY;
    public static final MemoryModuleType<BlockPos> NEAREST_REPELLENT;
    public static final MemoryModuleType<Boolean> PACIFIED;
    private final Optional<Codec<ExpirableValue<U>>> codec;
    
    private MemoryModuleType(final Optional<Codec<U>> optional) {
        this.codec = (Optional<Codec<ExpirableValue<U>>>)optional.map(ExpirableValue::codec);
    }
    
    public String toString() {
        return Registry.MEMORY_MODULE_TYPE.getKey(this).toString();
    }
    
    public Optional<Codec<ExpirableValue<U>>> getCodec() {
        return this.codec;
    }
    
    private static <U> MemoryModuleType<U> register(final String string, final Codec<U> codec) {
        return Registry.<MemoryModuleType<?>, MemoryModuleType<U>>register(Registry.MEMORY_MODULE_TYPE, new ResourceLocation(string), new MemoryModuleType<U>((java.util.Optional<com.mojang.serialization.Codec<U>>)Optional.of(codec)));
    }
    
    private static <U> MemoryModuleType<U> register(final String string) {
        return Registry.<MemoryModuleType<?>, MemoryModuleType<U>>register(Registry.MEMORY_MODULE_TYPE, new ResourceLocation(string), new MemoryModuleType<U>((java.util.Optional<com.mojang.serialization.Codec<U>>)Optional.empty()));
    }
    
    static {
        DUMMY = MemoryModuleType.<Void>register("dummy");
        HOME = MemoryModuleType.<GlobalPos>register("home", GlobalPos.CODEC);
        JOB_SITE = MemoryModuleType.<GlobalPos>register("job_site", GlobalPos.CODEC);
        POTENTIAL_JOB_SITE = MemoryModuleType.<GlobalPos>register("potential_job_site", GlobalPos.CODEC);
        MEETING_POINT = MemoryModuleType.<GlobalPos>register("meeting_point", GlobalPos.CODEC);
        SECONDARY_JOB_SITE = MemoryModuleType.<List<GlobalPos>>register("secondary_job_site");
        LIVING_ENTITIES = MemoryModuleType.<List<LivingEntity>>register("mobs");
        VISIBLE_LIVING_ENTITIES = MemoryModuleType.<List<LivingEntity>>register("visible_mobs");
        VISIBLE_VILLAGER_BABIES = MemoryModuleType.<List<LivingEntity>>register("visible_villager_babies");
        NEAREST_PLAYERS = MemoryModuleType.<List<Player>>register("nearest_players");
        NEAREST_VISIBLE_PLAYER = MemoryModuleType.<Player>register("nearest_visible_player");
        NEAREST_VISIBLE_TARGETABLE_PLAYER = MemoryModuleType.<Player>register("nearest_visible_targetable_player");
        WALK_TARGET = MemoryModuleType.<WalkTarget>register("walk_target");
        LOOK_TARGET = MemoryModuleType.<PositionTracker>register("look_target");
        ATTACK_TARGET = MemoryModuleType.<LivingEntity>register("attack_target");
        ATTACK_COOLING_DOWN = MemoryModuleType.<Boolean>register("attack_cooling_down");
        INTERACTION_TARGET = MemoryModuleType.<LivingEntity>register("interaction_target");
        BREED_TARGET = MemoryModuleType.<AgableMob>register("breed_target");
        RIDE_TARGET = MemoryModuleType.<Entity>register("ride_target");
        PATH = MemoryModuleType.<Path>register("path");
        INTERACTABLE_DOORS = MemoryModuleType.<List<GlobalPos>>register("interactable_doors");
        DOORS_TO_CLOSE = MemoryModuleType.<Set<GlobalPos>>register("doors_to_close");
        NEAREST_BED = MemoryModuleType.<BlockPos>register("nearest_bed");
        HURT_BY = MemoryModuleType.<DamageSource>register("hurt_by");
        HURT_BY_ENTITY = MemoryModuleType.<LivingEntity>register("hurt_by_entity");
        AVOID_TARGET = MemoryModuleType.<LivingEntity>register("avoid_target");
        NEAREST_HOSTILE = MemoryModuleType.<LivingEntity>register("nearest_hostile");
        HIDING_PLACE = MemoryModuleType.<GlobalPos>register("hiding_place");
        HEARD_BELL_TIME = MemoryModuleType.<Long>register("heard_bell_time");
        CANT_REACH_WALK_TARGET_SINCE = MemoryModuleType.<Long>register("cant_reach_walk_target_since");
        GOLEM_DETECTED_RECENTLY = MemoryModuleType.<Boolean>register("golem_detected_recently", (com.mojang.serialization.Codec<Boolean>)Codec.BOOL);
        LAST_SLEPT = MemoryModuleType.<Long>register("last_slept", (com.mojang.serialization.Codec<Long>)Codec.LONG);
        LAST_WOKEN = MemoryModuleType.<Long>register("last_woken", (com.mojang.serialization.Codec<Long>)Codec.LONG);
        LAST_WORKED_AT_POI = MemoryModuleType.<Long>register("last_worked_at_poi", (com.mojang.serialization.Codec<Long>)Codec.LONG);
        NEAREST_VISIBLE_ADULT = MemoryModuleType.<AgableMob>register("nearest_visible_adult");
        NEAREST_VISIBLE_WANTED_ITEM = MemoryModuleType.<ItemEntity>register("nearest_visible_wanted_item");
        NEAREST_VISIBLE_NEMESIS = MemoryModuleType.<Mob>register("nearest_visible_nemesis");
        ANGRY_AT = MemoryModuleType.<UUID>register("angry_at", SerializableUUID.CODEC);
        UNIVERSAL_ANGER = MemoryModuleType.<Boolean>register("universal_anger", (com.mojang.serialization.Codec<Boolean>)Codec.BOOL);
        ADMIRING_ITEM = MemoryModuleType.<Boolean>register("admiring_item", (com.mojang.serialization.Codec<Boolean>)Codec.BOOL);
        TIME_TRYING_TO_REACH_ADMIRE_ITEM = MemoryModuleType.<Integer>register("time_trying_to_reach_admire_item");
        DISABLE_WALK_TO_ADMIRE_ITEM = MemoryModuleType.<Boolean>register("disable_walk_to_admire_item");
        ADMIRING_DISABLED = MemoryModuleType.<Boolean>register("admiring_disabled", (com.mojang.serialization.Codec<Boolean>)Codec.BOOL);
        HUNTED_RECENTLY = MemoryModuleType.<Boolean>register("hunted_recently", (com.mojang.serialization.Codec<Boolean>)Codec.BOOL);
        CELEBRATE_LOCATION = MemoryModuleType.<BlockPos>register("celebrate_location");
        DANCING = MemoryModuleType.<Boolean>register("dancing");
        NEAREST_VISIBLE_HUNTABLE_HOGLIN = MemoryModuleType.<Hoglin>register("nearest_visible_huntable_hoglin");
        NEAREST_VISIBLE_BABY_HOGLIN = MemoryModuleType.<Hoglin>register("nearest_visible_baby_hoglin");
        NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD = MemoryModuleType.<Player>register("nearest_targetable_player_not_wearing_gold");
        NEARBY_ADULT_PIGLINS = MemoryModuleType.<List<AbstractPiglin>>register("nearby_adult_piglins");
        NEAREST_VISIBLE_ADULT_PIGLINS = MemoryModuleType.<List<AbstractPiglin>>register("nearest_visible_adult_piglins");
        NEAREST_VISIBLE_ADULT_HOGLINS = MemoryModuleType.<List<Hoglin>>register("nearest_visible_adult_hoglins");
        NEAREST_VISIBLE_ADULT_PIGLIN = MemoryModuleType.<AbstractPiglin>register("nearest_visible_adult_piglin");
        NEAREST_VISIBLE_ZOMBIFIED = MemoryModuleType.<LivingEntity>register("nearest_visible_zombified");
        VISIBLE_ADULT_PIGLIN_COUNT = MemoryModuleType.<Integer>register("visible_adult_piglin_count");
        VISIBLE_ADULT_HOGLIN_COUNT = MemoryModuleType.<Integer>register("visible_adult_hoglin_count");
        NEAREST_PLAYER_HOLDING_WANTED_ITEM = MemoryModuleType.<Player>register("nearest_player_holding_wanted_item");
        ATE_RECENTLY = MemoryModuleType.<Boolean>register("ate_recently");
        NEAREST_REPELLENT = MemoryModuleType.<BlockPos>register("nearest_repellent");
        PACIFIED = MemoryModuleType.<Boolean>register("pacified");
    }
}
