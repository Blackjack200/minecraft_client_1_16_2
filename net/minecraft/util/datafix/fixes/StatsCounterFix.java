package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import javax.annotation.Nullable;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import java.util.Set;
import com.mojang.datafixers.DataFix;

public class StatsCounterFix extends DataFix {
    private static final Set<String> SKIP;
    private static final Map<String, String> CUSTOM_MAP;
    private static final Map<String, String> ITEM_KEYS;
    private static final Map<String, String> ENTITY_KEYS;
    private static final Map<String, String> ENTITIES;
    
    public StatsCounterFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.STATS);
        return this.fixTypeEverywhereTyped("StatsCounterFix", this.getInputSchema().getType(References.STATS), (Type)type2, typed -> {
            final Dynamic<?> dynamic4 = typed.get(DSL.remainderFinder());
            final Map<Dynamic<?>, Dynamic<?>> map5 = (Map<Dynamic<?>, Dynamic<?>>)Maps.newHashMap();
            final Optional<? extends Map<? extends Dynamic<?>, ? extends Dynamic<?>>> optional6 = dynamic4.getMapValues().result();
            if (optional6.isPresent()) {
                for (final Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry8 : ((Map)optional6.get()).entrySet()) {
                    if (((Dynamic)entry8.getValue()).asNumber().result().isPresent()) {
                        final String string9 = ((Dynamic)entry8.getKey()).asString("");
                        if (StatsCounterFix.SKIP.contains(string9)) {
                            continue;
                        }
                        String string10;
                        String string11;
                        if (StatsCounterFix.CUSTOM_MAP.containsKey(string9)) {
                            string10 = "minecraft:custom";
                            string11 = (String)StatsCounterFix.CUSTOM_MAP.get(string9);
                        }
                        else {
                            final int integer12 = StringUtils.ordinalIndexOf((CharSequence)string9, ".", 2);
                            if (integer12 < 0) {
                                continue;
                            }
                            final String string12 = string9.substring(0, integer12);
                            if ("stat.mineBlock".equals(string12)) {
                                string10 = "minecraft:mined";
                                string11 = this.upgradeBlock(string9.substring(integer12 + 1).replace('.', ':'));
                            }
                            else if (StatsCounterFix.ITEM_KEYS.containsKey(string12)) {
                                string10 = (String)StatsCounterFix.ITEM_KEYS.get(string12);
                                final String string13 = string9.substring(integer12 + 1).replace('.', ':');
                                final String string14 = this.upgradeItem(string13);
                                string11 = ((string14 == null) ? string13 : string14);
                            }
                            else {
                                if (!StatsCounterFix.ENTITY_KEYS.containsKey(string12)) {
                                    continue;
                                }
                                string10 = (String)StatsCounterFix.ENTITY_KEYS.get(string12);
                                final String string13 = string9.substring(integer12 + 1).replace('.', ':');
                                string11 = (String)StatsCounterFix.ENTITIES.getOrDefault(string13, string13);
                            }
                        }
                        final Dynamic<?> dynamic5 = dynamic4.createString(string10);
                        final Dynamic<?> dynamic6 = map5.computeIfAbsent(dynamic5, dynamic2 -> dynamic4.emptyMap());
                        map5.put(dynamic5, dynamic6.set(string11, (Dynamic)entry8.getValue()));
                    }
                }
            }
            return (Typed)((Pair)type2.readTyped(dynamic4.emptyMap().set("stats", dynamic4.createMap((Map)map5))).result().orElseThrow(() -> new IllegalStateException("Could not parse new stats object."))).getFirst();
        });
    }
    
    @Nullable
    protected String upgradeItem(final String string) {
        return ItemStackTheFlatteningFix.updateItem(string, 0);
    }
    
    protected String upgradeBlock(final String string) {
        return BlockStateData.upgradeBlock(string);
    }
    
    static {
        SKIP = (Set)ImmutableSet.builder().add("stat.craftItem.minecraft.spawn_egg").add("stat.useItem.minecraft.spawn_egg").add("stat.breakItem.minecraft.spawn_egg").add("stat.pickup.minecraft.spawn_egg").add("stat.drop.minecraft.spawn_egg").build();
        CUSTOM_MAP = (Map)ImmutableMap.builder().put("stat.leaveGame", "minecraft:leave_game").put("stat.playOneMinute", "minecraft:play_one_minute").put("stat.timeSinceDeath", "minecraft:time_since_death").put("stat.sneakTime", "minecraft:sneak_time").put("stat.walkOneCm", "minecraft:walk_one_cm").put("stat.crouchOneCm", "minecraft:crouch_one_cm").put("stat.sprintOneCm", "minecraft:sprint_one_cm").put("stat.swimOneCm", "minecraft:swim_one_cm").put("stat.fallOneCm", "minecraft:fall_one_cm").put("stat.climbOneCm", "minecraft:climb_one_cm").put("stat.flyOneCm", "minecraft:fly_one_cm").put("stat.diveOneCm", "minecraft:dive_one_cm").put("stat.minecartOneCm", "minecraft:minecart_one_cm").put("stat.boatOneCm", "minecraft:boat_one_cm").put("stat.pigOneCm", "minecraft:pig_one_cm").put("stat.horseOneCm", "minecraft:horse_one_cm").put("stat.aviateOneCm", "minecraft:aviate_one_cm").put("stat.jump", "minecraft:jump").put("stat.drop", "minecraft:drop").put("stat.damageDealt", "minecraft:damage_dealt").put("stat.damageTaken", "minecraft:damage_taken").put("stat.deaths", "minecraft:deaths").put("stat.mobKills", "minecraft:mob_kills").put("stat.animalsBred", "minecraft:animals_bred").put("stat.playerKills", "minecraft:player_kills").put("stat.fishCaught", "minecraft:fish_caught").put("stat.talkedToVillager", "minecraft:talked_to_villager").put("stat.tradedWithVillager", "minecraft:traded_with_villager").put("stat.cakeSlicesEaten", "minecraft:eat_cake_slice").put("stat.cauldronFilled", "minecraft:fill_cauldron").put("stat.cauldronUsed", "minecraft:use_cauldron").put("stat.armorCleaned", "minecraft:clean_armor").put("stat.bannerCleaned", "minecraft:clean_banner").put("stat.brewingstandInteraction", "minecraft:interact_with_brewingstand").put("stat.beaconInteraction", "minecraft:interact_with_beacon").put("stat.dropperInspected", "minecraft:inspect_dropper").put("stat.hopperInspected", "minecraft:inspect_hopper").put("stat.dispenserInspected", "minecraft:inspect_dispenser").put("stat.noteblockPlayed", "minecraft:play_noteblock").put("stat.noteblockTuned", "minecraft:tune_noteblock").put("stat.flowerPotted", "minecraft:pot_flower").put("stat.trappedChestTriggered", "minecraft:trigger_trapped_chest").put("stat.enderchestOpened", "minecraft:open_enderchest").put("stat.itemEnchanted", "minecraft:enchant_item").put("stat.recordPlayed", "minecraft:play_record").put("stat.furnaceInteraction", "minecraft:interact_with_furnace").put("stat.craftingTableInteraction", "minecraft:interact_with_crafting_table").put("stat.chestOpened", "minecraft:open_chest").put("stat.sleepInBed", "minecraft:sleep_in_bed").put("stat.shulkerBoxOpened", "minecraft:open_shulker_box").build();
        ITEM_KEYS = (Map)ImmutableMap.builder().put("stat.craftItem", "minecraft:crafted").put("stat.useItem", "minecraft:used").put("stat.breakItem", "minecraft:broken").put("stat.pickup", "minecraft:picked_up").put("stat.drop", "minecraft:dropped").build();
        ENTITY_KEYS = (Map)ImmutableMap.builder().put("stat.entityKilledBy", "minecraft:killed_by").put("stat.killEntity", "minecraft:killed").build();
        ENTITIES = (Map)ImmutableMap.builder().put("Bat", "minecraft:bat").put("Blaze", "minecraft:blaze").put("CaveSpider", "minecraft:cave_spider").put("Chicken", "minecraft:chicken").put("Cow", "minecraft:cow").put("Creeper", "minecraft:creeper").put("Donkey", "minecraft:donkey").put("ElderGuardian", "minecraft:elder_guardian").put("Enderman", "minecraft:enderman").put("Endermite", "minecraft:endermite").put("EvocationIllager", "minecraft:evocation_illager").put("Ghast", "minecraft:ghast").put("Guardian", "minecraft:guardian").put("Horse", "minecraft:horse").put("Husk", "minecraft:husk").put("Llama", "minecraft:llama").put("LavaSlime", "minecraft:magma_cube").put("MushroomCow", "minecraft:mooshroom").put("Mule", "minecraft:mule").put("Ozelot", "minecraft:ocelot").put("Parrot", "minecraft:parrot").put("Pig", "minecraft:pig").put("PolarBear", "minecraft:polar_bear").put("Rabbit", "minecraft:rabbit").put("Sheep", "minecraft:sheep").put("Shulker", "minecraft:shulker").put("Silverfish", "minecraft:silverfish").put("SkeletonHorse", "minecraft:skeleton_horse").put("Skeleton", "minecraft:skeleton").put("Slime", "minecraft:slime").put("Spider", "minecraft:spider").put("Squid", "minecraft:squid").put("Stray", "minecraft:stray").put("Vex", "minecraft:vex").put("Villager", "minecraft:villager").put("VindicationIllager", "minecraft:vindication_illager").put("Witch", "minecraft:witch").put("WitherSkeleton", "minecraft:wither_skeleton").put("Wolf", "minecraft:wolf").put("ZombieHorse", "minecraft:zombie_horse").put("PigZombie", "minecraft:zombie_pigman").put("ZombieVillager", "minecraft:zombie_villager").put("Zombie", "minecraft:zombie").build();
    }
}
