package net.minecraft.util.datafix.fixes;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.HashMap;
import com.google.gson.JsonElement;
import java.util.Optional;
import net.minecraft.util.GsonHelper;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.DataResult;
import com.mojang.datafixers.Typed;
import java.util.Collections;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.util.stream.Collectors;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.Lists;
import java.util.Locale;
import com.google.common.collect.Maps;
import com.google.common.base.Splitter;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class LevelDataGeneratorOptionsFix extends DataFix {
    static final Map<String, String> MAP;
    
    public LevelDataGeneratorOptionsFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.LEVEL);
        return this.fixTypeEverywhereTyped("LevelDataGeneratorOptionsFix", this.getInputSchema().getType(References.LEVEL), (Type)type2, typed -> (Typed)typed.write().flatMap(dynamic -> {
            final Optional<String> optional3 = (Optional<String>)dynamic.get("generatorOptions").asString().result();
            Dynamic<?> dynamic2;
            if ("flat".equalsIgnoreCase(dynamic.get("generatorName").asString(""))) {
                final String string5 = (String)optional3.orElse("");
                dynamic2 = dynamic.set("generatorOptions", (Dynamic)LevelDataGeneratorOptionsFix.convert(string5, (com.mojang.serialization.DynamicOps<Object>)dynamic.getOps()));
            }
            else if ("buffet".equalsIgnoreCase(dynamic.get("generatorName").asString("")) && optional3.isPresent()) {
                final Dynamic<JsonElement> dynamic3 = (Dynamic<JsonElement>)new Dynamic((DynamicOps)JsonOps.INSTANCE, GsonHelper.parse((String)optional3.get(), true));
                dynamic2 = dynamic.set("generatorOptions", dynamic3.convert(dynamic.getOps()));
            }
            else {
                dynamic2 = dynamic;
            }
            return type2.readTyped((Dynamic)dynamic2);
        }).map(Pair::getFirst).result().orElseThrow(() -> new IllegalStateException("Could not read new level type.")));
    }
    
    private static <T> Dynamic<T> convert(final String string, final DynamicOps<T> dynamicOps) {
        final Iterator<String> iterator3 = (Iterator<String>)Splitter.on(';').split((CharSequence)string).iterator();
        String string2 = "minecraft:plains";
        final Map<String, Map<String, String>> map6 = (Map<String, Map<String, String>>)Maps.newHashMap();
        List<Pair<Integer, String>> list4;
        if (!string.isEmpty() && iterator3.hasNext()) {
            list4 = getLayersInfoFromString((String)iterator3.next());
            if (!list4.isEmpty()) {
                if (iterator3.hasNext()) {
                    string2 = (String)LevelDataGeneratorOptionsFix.MAP.getOrDefault(iterator3.next(), "minecraft:plains");
                }
                if (iterator3.hasNext()) {
                    final String[] split;
                    final String[] arr7 = split = ((String)iterator3.next()).toLowerCase(Locale.ROOT).split(",");
                    for (final String string3 : split) {
                        final String[] arr8 = string3.split("\\(", 2);
                        if (!arr8[0].isEmpty()) {
                            map6.put(arr8[0], Maps.newHashMap());
                            if (arr8.length > 1 && arr8[1].endsWith(")") && arr8[1].length() > 1) {
                                final String[] split2;
                                final String[] arr9 = split2 = arr8[1].substring(0, arr8[1].length() - 1).split(" ");
                                for (final String string4 : split2) {
                                    final String[] arr10 = string4.split("=", 2);
                                    if (arr10.length == 2) {
                                        ((Map)map6.get(arr8[0])).put(arr10[0], arr10[1]);
                                    }
                                }
                            }
                        }
                    }
                }
                else {
                    map6.put("village", Maps.newHashMap());
                }
            }
        }
        else {
            list4 = (List<Pair<Integer, String>>)Lists.newArrayList();
            list4.add(Pair.of((Object)1, "minecraft:bedrock"));
            list4.add(Pair.of((Object)2, "minecraft:dirt"));
            list4.add(Pair.of((Object)1, "minecraft:grass_block"));
            map6.put("village", Maps.newHashMap());
        }
        final T object7 = (T)dynamicOps.createList(list4.stream().map(pair -> dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps.createString("height"), dynamicOps.createInt((int)pair.getFirst()), dynamicOps.createString("block"), dynamicOps.createString((String)pair.getSecond())))));
        final T object8 = (T)dynamicOps.createMap((Map)map6.entrySet().stream().map(entry -> Pair.of(dynamicOps.createString(((String)entry.getKey()).toLowerCase(Locale.ROOT)), dynamicOps.createMap((Map)((Map)entry.getValue()).entrySet().stream().map(entry -> Pair.of(dynamicOps.createString((String)entry.getKey()), dynamicOps.createString((String)entry.getValue()))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond))))).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)));
        return (Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, dynamicOps.createMap((Map)ImmutableMap.of(dynamicOps.createString("layers"), object7, dynamicOps.createString("biome"), dynamicOps.createString(string2), dynamicOps.createString("structures"), object8)));
    }
    
    @Nullable
    private static Pair<Integer, String> getLayerInfoFromString(final String string) {
        final String[] arr2 = string.split("\\*", 2);
        int integer3 = 0;
        Label_0030: {
            if (arr2.length == 2) {
                try {
                    integer3 = Integer.parseInt(arr2[0]);
                    break Label_0030;
                }
                catch (NumberFormatException numberFormatException4) {
                    return null;
                }
            }
            integer3 = 1;
        }
        final String string2 = arr2[arr2.length - 1];
        return (Pair<Integer, String>)Pair.of(integer3, string2);
    }
    
    private static List<Pair<Integer, String>> getLayersInfoFromString(final String string) {
        final List<Pair<Integer, String>> list2 = (List<Pair<Integer, String>>)Lists.newArrayList();
        final String[] split;
        final String[] arr3 = split = string.split(",");
        for (final String string2 : split) {
            final Pair<Integer, String> pair8 = getLayerInfoFromString(string2);
            if (pair8 == null) {
                return (List<Pair<Integer, String>>)Collections.emptyList();
            }
            list2.add(pair8);
        }
        return list2;
    }
    
    static {
        MAP = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("0", "minecraft:ocean");
            hashMap.put("1", "minecraft:plains");
            hashMap.put("2", "minecraft:desert");
            hashMap.put("3", "minecraft:mountains");
            hashMap.put("4", "minecraft:forest");
            hashMap.put("5", "minecraft:taiga");
            hashMap.put("6", "minecraft:swamp");
            hashMap.put("7", "minecraft:river");
            hashMap.put("8", "minecraft:nether");
            hashMap.put("9", "minecraft:the_end");
            hashMap.put("10", "minecraft:frozen_ocean");
            hashMap.put("11", "minecraft:frozen_river");
            hashMap.put("12", "minecraft:snowy_tundra");
            hashMap.put("13", "minecraft:snowy_mountains");
            hashMap.put("14", "minecraft:mushroom_fields");
            hashMap.put("15", "minecraft:mushroom_field_shore");
            hashMap.put("16", "minecraft:beach");
            hashMap.put("17", "minecraft:desert_hills");
            hashMap.put("18", "minecraft:wooded_hills");
            hashMap.put("19", "minecraft:taiga_hills");
            hashMap.put("20", "minecraft:mountain_edge");
            hashMap.put("21", "minecraft:jungle");
            hashMap.put("22", "minecraft:jungle_hills");
            hashMap.put("23", "minecraft:jungle_edge");
            hashMap.put("24", "minecraft:deep_ocean");
            hashMap.put("25", "minecraft:stone_shore");
            hashMap.put("26", "minecraft:snowy_beach");
            hashMap.put("27", "minecraft:birch_forest");
            hashMap.put("28", "minecraft:birch_forest_hills");
            hashMap.put("29", "minecraft:dark_forest");
            hashMap.put("30", "minecraft:snowy_taiga");
            hashMap.put("31", "minecraft:snowy_taiga_hills");
            hashMap.put("32", "minecraft:giant_tree_taiga");
            hashMap.put("33", "minecraft:giant_tree_taiga_hills");
            hashMap.put("34", "minecraft:wooded_mountains");
            hashMap.put("35", "minecraft:savanna");
            hashMap.put("36", "minecraft:savanna_plateau");
            hashMap.put("37", "minecraft:badlands");
            hashMap.put("38", "minecraft:wooded_badlands_plateau");
            hashMap.put("39", "minecraft:badlands_plateau");
            hashMap.put("40", "minecraft:small_end_islands");
            hashMap.put("41", "minecraft:end_midlands");
            hashMap.put("42", "minecraft:end_highlands");
            hashMap.put("43", "minecraft:end_barrens");
            hashMap.put("44", "minecraft:warm_ocean");
            hashMap.put("45", "minecraft:lukewarm_ocean");
            hashMap.put("46", "minecraft:cold_ocean");
            hashMap.put("47", "minecraft:deep_warm_ocean");
            hashMap.put("48", "minecraft:deep_lukewarm_ocean");
            hashMap.put("49", "minecraft:deep_cold_ocean");
            hashMap.put("50", "minecraft:deep_frozen_ocean");
            hashMap.put("127", "minecraft:the_void");
            hashMap.put("129", "minecraft:sunflower_plains");
            hashMap.put("130", "minecraft:desert_lakes");
            hashMap.put("131", "minecraft:gravelly_mountains");
            hashMap.put("132", "minecraft:flower_forest");
            hashMap.put("133", "minecraft:taiga_mountains");
            hashMap.put("134", "minecraft:swamp_hills");
            hashMap.put("140", "minecraft:ice_spikes");
            hashMap.put("149", "minecraft:modified_jungle");
            hashMap.put("151", "minecraft:modified_jungle_edge");
            hashMap.put("155", "minecraft:tall_birch_forest");
            hashMap.put("156", "minecraft:tall_birch_hills");
            hashMap.put("157", "minecraft:dark_forest_hills");
            hashMap.put("158", "minecraft:snowy_taiga_mountains");
            hashMap.put("160", "minecraft:giant_spruce_taiga");
            hashMap.put("161", "minecraft:giant_spruce_taiga_hills");
            hashMap.put("162", "minecraft:modified_gravelly_mountains");
            hashMap.put("163", "minecraft:shattered_savanna");
            hashMap.put("164", "minecraft:shattered_savanna_plateau");
            hashMap.put("165", "minecraft:eroded_badlands");
            hashMap.put("166", "minecraft:modified_wooded_badlands_plateau");
            hashMap.put("167", "minecraft:modified_badlands_plateau");
        }));
    }
}
