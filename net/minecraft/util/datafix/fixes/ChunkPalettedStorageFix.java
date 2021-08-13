package net.minecraft.util.datafix.fixes;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.Arrays;
import net.minecraft.util.datafix.PackedBitStorage;
import java.nio.ByteBuffer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import java.util.Set;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import com.mojang.datafixers.DataFixUtils;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Objects;
import java.util.HashMap;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;
import com.mojang.serialization.Dynamic;
import java.util.BitSet;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.DataFix;

public class ChunkPalettedStorageFix extends DataFix {
    private static final Logger LOGGER;
    private static final BitSet VIRTUAL;
    private static final BitSet FIX;
    private static final Dynamic<?> PUMPKIN;
    private static final Dynamic<?> SNOWY_PODZOL;
    private static final Dynamic<?> SNOWY_GRASS;
    private static final Dynamic<?> SNOWY_MYCELIUM;
    private static final Dynamic<?> UPPER_SUNFLOWER;
    private static final Dynamic<?> UPPER_LILAC;
    private static final Dynamic<?> UPPER_TALL_GRASS;
    private static final Dynamic<?> UPPER_LARGE_FERN;
    private static final Dynamic<?> UPPER_ROSE_BUSH;
    private static final Dynamic<?> UPPER_PEONY;
    private static final Map<String, Dynamic<?>> FLOWER_POT_MAP;
    private static final Map<String, Dynamic<?>> SKULL_MAP;
    private static final Map<String, Dynamic<?>> DOOR_MAP;
    private static final Map<String, Dynamic<?>> NOTE_BLOCK_MAP;
    private static final Int2ObjectMap<String> DYE_COLOR_MAP;
    private static final Map<String, Dynamic<?>> BED_BLOCK_MAP;
    private static final Map<String, Dynamic<?>> BANNER_BLOCK_MAP;
    private static final Dynamic<?> AIR;
    
    public ChunkPalettedStorageFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private static void mapSkull(final Map<String, Dynamic<?>> map, final int integer, final String string3, final String string4) {
        map.put(new StringBuilder().append(integer).append("north").toString(), BlockStateData.parse("{Name:'minecraft:" + string3 + "_wall_" + string4 + "',Properties:{facing:'north'}}"));
        map.put(new StringBuilder().append(integer).append("east").toString(), BlockStateData.parse("{Name:'minecraft:" + string3 + "_wall_" + string4 + "',Properties:{facing:'east'}}"));
        map.put(new StringBuilder().append(integer).append("south").toString(), BlockStateData.parse("{Name:'minecraft:" + string3 + "_wall_" + string4 + "',Properties:{facing:'south'}}"));
        map.put(new StringBuilder().append(integer).append("west").toString(), BlockStateData.parse("{Name:'minecraft:" + string3 + "_wall_" + string4 + "',Properties:{facing:'west'}}"));
        for (int integer2 = 0; integer2 < 16; ++integer2) {
            map.put(new StringBuilder().append(integer).append("").append(integer2).toString(), BlockStateData.parse("{Name:'minecraft:" + string3 + "_" + string4 + "',Properties:{rotation:'" + integer2 + "'}}"));
        }
    }
    
    private static void mapDoor(final Map<String, Dynamic<?>> map, final String string, final int integer) {
        map.put(("minecraft:" + string + "eastlowerleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "eastlowerleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "eastlowerlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "eastlowerlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "eastlowerrightfalsefalse"), BlockStateData.getTag(integer));
        map.put(("minecraft:" + string + "eastlowerrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "eastlowerrighttruefalse"), BlockStateData.getTag(integer + 4));
        map.put(("minecraft:" + string + "eastlowerrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "eastupperleftfalsefalse"), BlockStateData.getTag(integer + 8));
        map.put(("minecraft:" + string + "eastupperleftfalsetrue"), BlockStateData.getTag(integer + 10));
        map.put(("minecraft:" + string + "eastupperlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "eastupperlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "eastupperrightfalsefalse"), BlockStateData.getTag(integer + 9));
        map.put(("minecraft:" + string + "eastupperrightfalsetrue"), BlockStateData.getTag(integer + 11));
        map.put(("minecraft:" + string + "eastupperrighttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "eastupperrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'east',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "northlowerleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "northlowerleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "northlowerlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "northlowerlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "northlowerrightfalsefalse"), BlockStateData.getTag(integer + 3));
        map.put(("minecraft:" + string + "northlowerrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "northlowerrighttruefalse"), BlockStateData.getTag(integer + 7));
        map.put(("minecraft:" + string + "northlowerrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "northupperleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "northupperleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "northupperlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "northupperlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "northupperrightfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "northupperrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "northupperrighttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "northupperrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'north',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "southlowerleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "southlowerleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "southlowerlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "southlowerlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "southlowerrightfalsefalse"), BlockStateData.getTag(integer + 1));
        map.put(("minecraft:" + string + "southlowerrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "southlowerrighttruefalse"), BlockStateData.getTag(integer + 5));
        map.put(("minecraft:" + string + "southlowerrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "southupperleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "southupperleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "southupperlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "southupperlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "southupperrightfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "southupperrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "southupperrighttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "southupperrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'south',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "westlowerleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "westlowerleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "westlowerlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "westlowerlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "westlowerrightfalsefalse"), BlockStateData.getTag(integer + 2));
        map.put(("minecraft:" + string + "westlowerrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "westlowerrighttruefalse"), BlockStateData.getTag(integer + 6));
        map.put(("minecraft:" + string + "westlowerrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'lower',hinge:'right',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "westupperleftfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "westupperleftfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "westupperlefttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "westupperlefttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'left',open:'true',powered:'true'}}"));
        map.put(("minecraft:" + string + "westupperrightfalsefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'false'}}"));
        map.put(("minecraft:" + string + "westupperrightfalsetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'false',powered:'true'}}"));
        map.put(("minecraft:" + string + "westupperrighttruefalse"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'false'}}"));
        map.put(("minecraft:" + string + "westupperrighttruetrue"), BlockStateData.parse("{Name:'minecraft:" + string + "',Properties:{facing:'west',half:'upper',hinge:'right',open:'true',powered:'true'}}"));
    }
    
    private static void addBeds(final Map<String, Dynamic<?>> map, final int integer, final String string) {
        map.put(new StringBuilder().append("southfalsefoot").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'south',occupied:'false',part:'foot'}}"));
        map.put(new StringBuilder().append("westfalsefoot").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'west',occupied:'false',part:'foot'}}"));
        map.put(new StringBuilder().append("northfalsefoot").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'north',occupied:'false',part:'foot'}}"));
        map.put(new StringBuilder().append("eastfalsefoot").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'east',occupied:'false',part:'foot'}}"));
        map.put(new StringBuilder().append("southfalsehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'south',occupied:'false',part:'head'}}"));
        map.put(new StringBuilder().append("westfalsehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'west',occupied:'false',part:'head'}}"));
        map.put(new StringBuilder().append("northfalsehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'north',occupied:'false',part:'head'}}"));
        map.put(new StringBuilder().append("eastfalsehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'east',occupied:'false',part:'head'}}"));
        map.put(new StringBuilder().append("southtruehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'south',occupied:'true',part:'head'}}"));
        map.put(new StringBuilder().append("westtruehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'west',occupied:'true',part:'head'}}"));
        map.put(new StringBuilder().append("northtruehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'north',occupied:'true',part:'head'}}"));
        map.put(new StringBuilder().append("easttruehead").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_bed',Properties:{facing:'east',occupied:'true',part:'head'}}"));
    }
    
    private static void addBanners(final Map<String, Dynamic<?>> map, final int integer, final String string) {
        for (int integer2 = 0; integer2 < 16; ++integer2) {
            map.put(new StringBuilder().append("").append(integer2).append("_").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_banner',Properties:{rotation:'" + integer2 + "'}}"));
        }
        map.put(new StringBuilder().append("north_").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_wall_banner',Properties:{facing:'north'}}"));
        map.put(new StringBuilder().append("south_").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_wall_banner',Properties:{facing:'south'}}"));
        map.put(new StringBuilder().append("west_").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_wall_banner',Properties:{facing:'west'}}"));
        map.put(new StringBuilder().append("east_").append(integer).toString(), BlockStateData.parse("{Name:'minecraft:" + string + "_wall_banner',Properties:{facing:'east'}}"));
    }
    
    public static String getName(final Dynamic<?> dynamic) {
        return dynamic.get("Name").asString("");
    }
    
    public static String getProperty(final Dynamic<?> dynamic, final String string) {
        return dynamic.get("Properties").get(string).asString("");
    }
    
    public static int idFor(final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> aer, final Dynamic<?> dynamic) {
        int integer3 = aer.getId(dynamic);
        if (integer3 == -1) {
            integer3 = aer.add(dynamic);
        }
        return integer3;
    }
    
    private Dynamic<?> fix(final Dynamic<?> dynamic) {
        final Optional<? extends Dynamic<?>> optional3 = dynamic.get("Level").result();
        if (optional3.isPresent() && ((Dynamic)optional3.get()).get("Sections").asStreamOpt().result().isPresent()) {
            return dynamic.set("Level", (Dynamic)new UpgradeChunk(optional3.get()).write());
        }
        return dynamic;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type3 = this.getOutputSchema().getType(References.CHUNK);
        return this.writeFixAndRead("ChunkPalettedStorageFix", (Type)type2, (Type)type3, this::fix);
    }
    
    public static int getSideMask(final boolean boolean1, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        int integer5 = 0;
        if (boolean3) {
            if (boolean2) {
                integer5 |= 0x2;
            }
            else if (boolean1) {
                integer5 |= 0x80;
            }
            else {
                integer5 |= 0x1;
            }
        }
        else if (boolean4) {
            if (boolean1) {
                integer5 |= 0x20;
            }
            else if (boolean2) {
                integer5 |= 0x8;
            }
            else {
                integer5 |= 0x10;
            }
        }
        else if (boolean2) {
            integer5 |= 0x4;
        }
        else if (boolean1) {
            integer5 |= 0x40;
        }
        return integer5;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        VIRTUAL = new BitSet(256);
        FIX = new BitSet(256);
        PUMPKIN = BlockStateData.parse("{Name:'minecraft:pumpkin'}");
        SNOWY_PODZOL = BlockStateData.parse("{Name:'minecraft:podzol',Properties:{snowy:'true'}}");
        SNOWY_GRASS = BlockStateData.parse("{Name:'minecraft:grass_block',Properties:{snowy:'true'}}");
        SNOWY_MYCELIUM = BlockStateData.parse("{Name:'minecraft:mycelium',Properties:{snowy:'true'}}");
        UPPER_SUNFLOWER = BlockStateData.parse("{Name:'minecraft:sunflower',Properties:{half:'upper'}}");
        UPPER_LILAC = BlockStateData.parse("{Name:'minecraft:lilac',Properties:{half:'upper'}}");
        UPPER_TALL_GRASS = BlockStateData.parse("{Name:'minecraft:tall_grass',Properties:{half:'upper'}}");
        UPPER_LARGE_FERN = BlockStateData.parse("{Name:'minecraft:large_fern',Properties:{half:'upper'}}");
        UPPER_ROSE_BUSH = BlockStateData.parse("{Name:'minecraft:rose_bush',Properties:{half:'upper'}}");
        UPPER_PEONY = BlockStateData.parse("{Name:'minecraft:peony',Properties:{half:'upper'}}");
        FLOWER_POT_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            hashMap.put("minecraft:air0", BlockStateData.parse("{Name:'minecraft:flower_pot'}"));
            hashMap.put("minecraft:red_flower0", BlockStateData.parse("{Name:'minecraft:potted_poppy'}"));
            hashMap.put("minecraft:red_flower1", BlockStateData.parse("{Name:'minecraft:potted_blue_orchid'}"));
            hashMap.put("minecraft:red_flower2", BlockStateData.parse("{Name:'minecraft:potted_allium'}"));
            hashMap.put("minecraft:red_flower3", BlockStateData.parse("{Name:'minecraft:potted_azure_bluet'}"));
            hashMap.put("minecraft:red_flower4", BlockStateData.parse("{Name:'minecraft:potted_red_tulip'}"));
            hashMap.put("minecraft:red_flower5", BlockStateData.parse("{Name:'minecraft:potted_orange_tulip'}"));
            hashMap.put("minecraft:red_flower6", BlockStateData.parse("{Name:'minecraft:potted_white_tulip'}"));
            hashMap.put("minecraft:red_flower7", BlockStateData.parse("{Name:'minecraft:potted_pink_tulip'}"));
            hashMap.put("minecraft:red_flower8", BlockStateData.parse("{Name:'minecraft:potted_oxeye_daisy'}"));
            hashMap.put("minecraft:yellow_flower0", BlockStateData.parse("{Name:'minecraft:potted_dandelion'}"));
            hashMap.put("minecraft:sapling0", BlockStateData.parse("{Name:'minecraft:potted_oak_sapling'}"));
            hashMap.put("minecraft:sapling1", BlockStateData.parse("{Name:'minecraft:potted_spruce_sapling'}"));
            hashMap.put("minecraft:sapling2", BlockStateData.parse("{Name:'minecraft:potted_birch_sapling'}"));
            hashMap.put("minecraft:sapling3", BlockStateData.parse("{Name:'minecraft:potted_jungle_sapling'}"));
            hashMap.put("minecraft:sapling4", BlockStateData.parse("{Name:'minecraft:potted_acacia_sapling'}"));
            hashMap.put("minecraft:sapling5", BlockStateData.parse("{Name:'minecraft:potted_dark_oak_sapling'}"));
            hashMap.put("minecraft:red_mushroom0", BlockStateData.parse("{Name:'minecraft:potted_red_mushroom'}"));
            hashMap.put("minecraft:brown_mushroom0", BlockStateData.parse("{Name:'minecraft:potted_brown_mushroom'}"));
            hashMap.put("minecraft:deadbush0", BlockStateData.parse("{Name:'minecraft:potted_dead_bush'}"));
            hashMap.put("minecraft:tallgrass2", BlockStateData.parse("{Name:'minecraft:potted_fern'}"));
            hashMap.put("minecraft:cactus0", BlockStateData.getTag(2240));
        });
        SKULL_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            mapSkull((Map<String, Dynamic<?>>)hashMap, 0, "skeleton", "skull");
            mapSkull((Map<String, Dynamic<?>>)hashMap, 1, "wither_skeleton", "skull");
            mapSkull((Map<String, Dynamic<?>>)hashMap, 2, "zombie", "head");
            mapSkull((Map<String, Dynamic<?>>)hashMap, 3, "player", "head");
            mapSkull((Map<String, Dynamic<?>>)hashMap, 4, "creeper", "head");
            mapSkull((Map<String, Dynamic<?>>)hashMap, 5, "dragon", "head");
        });
        DOOR_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            mapDoor((Map<String, Dynamic<?>>)hashMap, "oak_door", 1024);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "iron_door", 1136);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "spruce_door", 3088);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "birch_door", 3104);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "jungle_door", 3120);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "acacia_door", 3136);
            mapDoor((Map<String, Dynamic<?>>)hashMap, "dark_oak_door", 3152);
        });
        NOTE_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            for (int integer2 = 0; integer2 < 26; ++integer2) {
                hashMap.put(new StringBuilder().append("true").append(integer2).toString(), BlockStateData.parse(new StringBuilder().append("{Name:'minecraft:note_block',Properties:{powered:'true',note:'").append(integer2).append("'}}").toString()));
                hashMap.put(new StringBuilder().append("false").append(integer2).toString(), BlockStateData.parse(new StringBuilder().append("{Name:'minecraft:note_block',Properties:{powered:'false',note:'").append(integer2).append("'}}").toString()));
            }
        });
        DYE_COLOR_MAP = (Int2ObjectMap)DataFixUtils.make(new Int2ObjectOpenHashMap(), int2ObjectOpenHashMap -> {
            int2ObjectOpenHashMap.put(0, "white");
            int2ObjectOpenHashMap.put(1, "orange");
            int2ObjectOpenHashMap.put(2, "magenta");
            int2ObjectOpenHashMap.put(3, "light_blue");
            int2ObjectOpenHashMap.put(4, "yellow");
            int2ObjectOpenHashMap.put(5, "lime");
            int2ObjectOpenHashMap.put(6, "pink");
            int2ObjectOpenHashMap.put(7, "gray");
            int2ObjectOpenHashMap.put(8, "light_gray");
            int2ObjectOpenHashMap.put(9, "cyan");
            int2ObjectOpenHashMap.put(10, "purple");
            int2ObjectOpenHashMap.put(11, "blue");
            int2ObjectOpenHashMap.put(12, "brown");
            int2ObjectOpenHashMap.put(13, "green");
            int2ObjectOpenHashMap.put(14, "red");
            int2ObjectOpenHashMap.put(15, "black");
        });
        BED_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            for (final Int2ObjectMap.Entry<String> entry3 : ChunkPalettedStorageFix.DYE_COLOR_MAP.int2ObjectEntrySet()) {
                if (!Objects.equals(entry3.getValue(), "red")) {
                    addBeds((Map<String, Dynamic<?>>)hashMap, entry3.getIntKey(), (String)entry3.getValue());
                }
            }
        });
        BANNER_BLOCK_MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            for (final Int2ObjectMap.Entry<String> entry3 : ChunkPalettedStorageFix.DYE_COLOR_MAP.int2ObjectEntrySet()) {
                if (!Objects.equals(entry3.getValue(), "white")) {
                    addBanners((Map<String, Dynamic<?>>)hashMap, 15 - entry3.getIntKey(), (String)entry3.getValue());
                }
            }
        });
        ChunkPalettedStorageFix.FIX.set(2);
        ChunkPalettedStorageFix.FIX.set(3);
        ChunkPalettedStorageFix.FIX.set(110);
        ChunkPalettedStorageFix.FIX.set(140);
        ChunkPalettedStorageFix.FIX.set(144);
        ChunkPalettedStorageFix.FIX.set(25);
        ChunkPalettedStorageFix.FIX.set(86);
        ChunkPalettedStorageFix.FIX.set(26);
        ChunkPalettedStorageFix.FIX.set(176);
        ChunkPalettedStorageFix.FIX.set(177);
        ChunkPalettedStorageFix.FIX.set(175);
        ChunkPalettedStorageFix.FIX.set(64);
        ChunkPalettedStorageFix.FIX.set(71);
        ChunkPalettedStorageFix.FIX.set(193);
        ChunkPalettedStorageFix.FIX.set(194);
        ChunkPalettedStorageFix.FIX.set(195);
        ChunkPalettedStorageFix.FIX.set(196);
        ChunkPalettedStorageFix.FIX.set(197);
        ChunkPalettedStorageFix.VIRTUAL.set(54);
        ChunkPalettedStorageFix.VIRTUAL.set(146);
        ChunkPalettedStorageFix.VIRTUAL.set(25);
        ChunkPalettedStorageFix.VIRTUAL.set(26);
        ChunkPalettedStorageFix.VIRTUAL.set(51);
        ChunkPalettedStorageFix.VIRTUAL.set(53);
        ChunkPalettedStorageFix.VIRTUAL.set(67);
        ChunkPalettedStorageFix.VIRTUAL.set(108);
        ChunkPalettedStorageFix.VIRTUAL.set(109);
        ChunkPalettedStorageFix.VIRTUAL.set(114);
        ChunkPalettedStorageFix.VIRTUAL.set(128);
        ChunkPalettedStorageFix.VIRTUAL.set(134);
        ChunkPalettedStorageFix.VIRTUAL.set(135);
        ChunkPalettedStorageFix.VIRTUAL.set(136);
        ChunkPalettedStorageFix.VIRTUAL.set(156);
        ChunkPalettedStorageFix.VIRTUAL.set(163);
        ChunkPalettedStorageFix.VIRTUAL.set(164);
        ChunkPalettedStorageFix.VIRTUAL.set(180);
        ChunkPalettedStorageFix.VIRTUAL.set(203);
        ChunkPalettedStorageFix.VIRTUAL.set(55);
        ChunkPalettedStorageFix.VIRTUAL.set(85);
        ChunkPalettedStorageFix.VIRTUAL.set(113);
        ChunkPalettedStorageFix.VIRTUAL.set(188);
        ChunkPalettedStorageFix.VIRTUAL.set(189);
        ChunkPalettedStorageFix.VIRTUAL.set(190);
        ChunkPalettedStorageFix.VIRTUAL.set(191);
        ChunkPalettedStorageFix.VIRTUAL.set(192);
        ChunkPalettedStorageFix.VIRTUAL.set(93);
        ChunkPalettedStorageFix.VIRTUAL.set(94);
        ChunkPalettedStorageFix.VIRTUAL.set(101);
        ChunkPalettedStorageFix.VIRTUAL.set(102);
        ChunkPalettedStorageFix.VIRTUAL.set(160);
        ChunkPalettedStorageFix.VIRTUAL.set(106);
        ChunkPalettedStorageFix.VIRTUAL.set(107);
        ChunkPalettedStorageFix.VIRTUAL.set(183);
        ChunkPalettedStorageFix.VIRTUAL.set(184);
        ChunkPalettedStorageFix.VIRTUAL.set(185);
        ChunkPalettedStorageFix.VIRTUAL.set(186);
        ChunkPalettedStorageFix.VIRTUAL.set(187);
        ChunkPalettedStorageFix.VIRTUAL.set(132);
        ChunkPalettedStorageFix.VIRTUAL.set(139);
        ChunkPalettedStorageFix.VIRTUAL.set(199);
        AIR = BlockStateData.getTag(0);
    }
    
    static class Section {
        private final CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>> palette;
        private final List<Dynamic<?>> listTag;
        private final Dynamic<?> section;
        private final boolean hasData;
        private final Int2ObjectMap<IntList> toFix;
        private final IntList update;
        public final int y;
        private final Set<Dynamic<?>> seen;
        private final int[] buffer;
        
        public Section(final Dynamic<?> dynamic) {
            this.palette = new CrudeIncrementalIntIdentityHashBiMap<Dynamic<?>>(32);
            this.toFix = (Int2ObjectMap<IntList>)new Int2ObjectLinkedOpenHashMap();
            this.update = (IntList)new IntArrayList();
            this.seen = (Set<Dynamic<?>>)Sets.newIdentityHashSet();
            this.buffer = new int[4096];
            this.listTag = (List<Dynamic<?>>)Lists.newArrayList();
            this.section = dynamic;
            this.y = dynamic.get("Y").asInt(0);
            this.hasData = dynamic.get("Blocks").result().isPresent();
        }
        
        public Dynamic<?> getBlock(final int integer) {
            if (integer < 0 || integer > 4095) {
                return ChunkPalettedStorageFix.AIR;
            }
            final Dynamic<?> dynamic3 = this.palette.byId(this.buffer[integer]);
            return (dynamic3 == null) ? ChunkPalettedStorageFix.AIR : dynamic3;
        }
        
        public void setBlock(final int integer, final Dynamic<?> dynamic) {
            if (this.seen.add(dynamic)) {
                this.listTag.add(("%%FILTER_ME%%".equals((Object)ChunkPalettedStorageFix.getName(dynamic)) ? ChunkPalettedStorageFix.AIR : dynamic));
            }
            this.buffer[integer] = ChunkPalettedStorageFix.idFor(this.palette, dynamic);
        }
        
        public int upgrade(int integer) {
            if (!this.hasData) {
                return integer;
            }
            final ByteBuffer byteBuffer3 = (ByteBuffer)this.section.get("Blocks").asByteBufferOpt().result().get();
            final DataLayer a4 = (DataLayer)this.section.get("Data").asByteBufferOpt().map(byteBuffer -> new DataLayer(DataFixUtils.toArray(byteBuffer))).result().orElseGet(DataLayer::new);
            final DataLayer a5 = (DataLayer)this.section.get("Add").asByteBufferOpt().map(byteBuffer -> new DataLayer(DataFixUtils.toArray(byteBuffer))).result().orElseGet(DataLayer::new);
            this.seen.add(ChunkPalettedStorageFix.AIR);
            ChunkPalettedStorageFix.idFor(this.palette, ChunkPalettedStorageFix.AIR);
            this.listTag.add(ChunkPalettedStorageFix.AIR);
            for (int integer2 = 0; integer2 < 4096; ++integer2) {
                final int integer3 = integer2 & 0xF;
                final int integer4 = integer2 >> 8 & 0xF;
                final int integer5 = integer2 >> 4 & 0xF;
                final int integer6 = a5.get(integer3, integer4, integer5) << 12 | (byteBuffer3.get(integer2) & 0xFF) << 4 | a4.get(integer3, integer4, integer5);
                if (ChunkPalettedStorageFix.FIX.get(integer6 >> 4)) {
                    this.addFix(integer6 >> 4, integer2);
                }
                if (ChunkPalettedStorageFix.VIRTUAL.get(integer6 >> 4)) {
                    final int integer7 = ChunkPalettedStorageFix.getSideMask(integer3 == 0, integer3 == 15, integer5 == 0, integer5 == 15);
                    if (integer7 == 0) {
                        this.update.add(integer2);
                    }
                    else {
                        integer |= integer7;
                    }
                }
                this.setBlock(integer2, BlockStateData.getTag(integer6));
            }
            return integer;
        }
        
        private void addFix(final int integer1, final int integer2) {
            IntList intList4 = (IntList)this.toFix.get(integer1);
            if (intList4 == null) {
                intList4 = (IntList)new IntArrayList();
                this.toFix.put(integer1, intList4);
            }
            intList4.add(integer2);
        }
        
        public Dynamic<?> write() {
            Dynamic<?> dynamic2 = this.section;
            if (!this.hasData) {
                return dynamic2;
            }
            dynamic2 = dynamic2.set("Palette", dynamic2.createList(this.listTag.stream()));
            final int integer3 = Math.max(4, DataFixUtils.ceillog2(this.seen.size()));
            final PackedBitStorage afz4 = new PackedBitStorage(integer3, 4096);
            for (int integer4 = 0; integer4 < this.buffer.length; ++integer4) {
                afz4.set(integer4, this.buffer[integer4]);
            }
            dynamic2 = dynamic2.set("BlockStates", dynamic2.createLongList(Arrays.stream(afz4.getRaw())));
            dynamic2 = dynamic2.remove("Blocks");
            dynamic2 = dynamic2.remove("Data");
            dynamic2 = dynamic2.remove("Add");
            return dynamic2;
        }
    }
    
    static final class UpgradeChunk {
        private int sides;
        private final Section[] sections;
        private final Dynamic<?> level;
        private final int x;
        private final int z;
        private final Int2ObjectMap<Dynamic<?>> blockEntities;
        
        public UpgradeChunk(final Dynamic<?> dynamic) {
            this.sections = new Section[16];
            this.blockEntities = (Int2ObjectMap<Dynamic<?>>)new Int2ObjectLinkedOpenHashMap(16);
            this.level = dynamic;
            this.x = dynamic.get("xPos").asInt(0) << 4;
            this.z = dynamic.get("zPos").asInt(0) << 4;
            dynamic.get("TileEntities").asStreamOpt().result().ifPresent(stream -> stream.forEach(dynamic -> {
                final int integer3 = dynamic.get("x").asInt(0) - this.x & 0xF;
                final int integer4 = dynamic.get("y").asInt(0);
                final int integer5 = dynamic.get("z").asInt(0) - this.z & 0xF;
                final int integer6 = integer4 << 8 | integer5 << 4 | integer3;
                if (this.blockEntities.put(integer6, dynamic) != null) {
                    ChunkPalettedStorageFix.LOGGER.warn("In chunk: {}x{} found a duplicate block entity at position: [{}, {}, {}]", this.x, this.z, integer3, integer4, integer5);
                }
            }));
            final boolean boolean3 = dynamic.get("convertedFromAlphaFormat").asBoolean(false);
            dynamic.get("Sections").asStreamOpt().result().ifPresent(stream -> stream.forEach(dynamic -> {
                final Section c3 = new Section(dynamic);
                this.sides = c3.upgrade(this.sides);
                this.sections[c3.y] = c3;
            }));
            for (final Section c7 : this.sections) {
                if (c7 != null) {
                    for (final Map.Entry<Integer, IntList> entry9 : c7.toFix.entrySet()) {
                        final int integer10 = c7.y << 12;
                        switch ((int)entry9.getKey()) {
                            case 2: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if ("minecraft:grass_block".equals(ChunkPalettedStorageFix.getName(dynamic2))) {
                                        final String string14 = ChunkPalettedStorageFix.getName(this.getBlock(relative(integer11, Direction.UP)));
                                        if (!"minecraft:snow".equals(string14) && !"minecraft:snow_layer".equals(string14)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.SNOWY_GRASS);
                                    }
                                }
                                continue;
                            }
                            case 3: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if ("minecraft:podzol".equals(ChunkPalettedStorageFix.getName(dynamic2))) {
                                        final String string14 = ChunkPalettedStorageFix.getName(this.getBlock(relative(integer11, Direction.UP)));
                                        if (!"minecraft:snow".equals(string14) && !"minecraft:snow_layer".equals(string14)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.SNOWY_PODZOL);
                                    }
                                }
                                continue;
                            }
                            case 110: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if ("minecraft:mycelium".equals(ChunkPalettedStorageFix.getName(dynamic2))) {
                                        final String string14 = ChunkPalettedStorageFix.getName(this.getBlock(relative(integer11, Direction.UP)));
                                        if (!"minecraft:snow".equals(string14) && !"minecraft:snow_layer".equals(string14)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.SNOWY_MYCELIUM);
                                    }
                                }
                                continue;
                            }
                            case 25: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.removeBlockEntity(integer11);
                                    if (dynamic2 != null) {
                                        final String string14 = Boolean.toString(dynamic2.get("powered").asBoolean(false)) + (int)(byte)Math.min(Math.max(dynamic2.get("note").asInt(0), 0), 24);
                                        this.setBlock(integer11, ChunkPalettedStorageFix.NOTE_BLOCK_MAP.getOrDefault(string14, ChunkPalettedStorageFix.NOTE_BLOCK_MAP.get("false0")));
                                    }
                                }
                                continue;
                            }
                            case 26: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlockEntity(integer11);
                                    final Dynamic<?> dynamic3 = this.getBlock(integer11);
                                    if (dynamic2 != null) {
                                        final int integer12 = dynamic2.get("color").asInt(0);
                                        if (integer12 == 14 || integer12 < 0 || integer12 >= 16) {
                                            continue;
                                        }
                                        final String string15 = ChunkPalettedStorageFix.getProperty(dynamic3, "facing") + ChunkPalettedStorageFix.getProperty(dynamic3, "occupied") + ChunkPalettedStorageFix.getProperty(dynamic3, "part") + integer12;
                                        if (!ChunkPalettedStorageFix.BED_BLOCK_MAP.containsKey(string15)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.BED_BLOCK_MAP.get(string15));
                                    }
                                }
                                continue;
                            }
                            case 176:
                            case 177: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlockEntity(integer11);
                                    final Dynamic<?> dynamic3 = this.getBlock(integer11);
                                    if (dynamic2 != null) {
                                        final int integer12 = dynamic2.get("Base").asInt(0);
                                        if (integer12 == 15 || integer12 < 0 || integer12 >= 16) {
                                            continue;
                                        }
                                        final String string15 = ChunkPalettedStorageFix.getProperty(dynamic3, ((int)entry9.getKey() == 176) ? "rotation" : "facing") + "_" + integer12;
                                        if (!ChunkPalettedStorageFix.BANNER_BLOCK_MAP.containsKey(string15)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.BANNER_BLOCK_MAP.get(string15));
                                    }
                                }
                                continue;
                            }
                            case 86: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if ("minecraft:carved_pumpkin".equals(ChunkPalettedStorageFix.getName(dynamic2))) {
                                        final String string14 = ChunkPalettedStorageFix.getName(this.getBlock(relative(integer11, Direction.DOWN)));
                                        if (!"minecraft:grass_block".equals(string14) && !"minecraft:dirt".equals(string14)) {
                                            continue;
                                        }
                                        this.setBlock(integer11, ChunkPalettedStorageFix.PUMPKIN);
                                    }
                                }
                                continue;
                            }
                            case 140: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.removeBlockEntity(integer11);
                                    if (dynamic2 != null) {
                                        final String string14 = dynamic2.get("Item").asString("") + dynamic2.get("Data").asInt(0);
                                        this.setBlock(integer11, ChunkPalettedStorageFix.FLOWER_POT_MAP.getOrDefault(string14, ChunkPalettedStorageFix.FLOWER_POT_MAP.get("minecraft:air0")));
                                    }
                                }
                                continue;
                            }
                            case 144: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlockEntity(integer11);
                                    if (dynamic2 != null) {
                                        final String string14 = String.valueOf(dynamic2.get("SkullType").asInt(0));
                                        final String string16 = ChunkPalettedStorageFix.getProperty(this.getBlock(integer11), "facing");
                                        String string15;
                                        if ("up".equals(string16) || "down".equals(string16)) {
                                            string15 = string14 + String.valueOf(dynamic2.get("Rot").asInt(0));
                                        }
                                        else {
                                            string15 = string14 + string16;
                                        }
                                        dynamic2.remove("SkullType");
                                        dynamic2.remove("facing");
                                        dynamic2.remove("Rot");
                                        this.setBlock(integer11, ChunkPalettedStorageFix.SKULL_MAP.getOrDefault(string15, ChunkPalettedStorageFix.SKULL_MAP.get("0north")));
                                    }
                                }
                                continue;
                            }
                            case 64:
                            case 71:
                            case 193:
                            case 194:
                            case 195:
                            case 196:
                            case 197: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if (ChunkPalettedStorageFix.getName(dynamic2).endsWith("_door")) {
                                        final Dynamic<?> dynamic3 = this.getBlock(integer11);
                                        if (!"lower".equals(ChunkPalettedStorageFix.getProperty(dynamic3, "half"))) {
                                            continue;
                                        }
                                        final int integer12 = relative(integer11, Direction.UP);
                                        final Dynamic<?> dynamic4 = this.getBlock(integer12);
                                        final String string17 = ChunkPalettedStorageFix.getName(dynamic3);
                                        if (!string17.equals(ChunkPalettedStorageFix.getName(dynamic4))) {
                                            continue;
                                        }
                                        final String string18 = ChunkPalettedStorageFix.getProperty(dynamic3, "facing");
                                        final String string19 = ChunkPalettedStorageFix.getProperty(dynamic3, "open");
                                        final String string20 = boolean3 ? "left" : ChunkPalettedStorageFix.getProperty(dynamic4, "hinge");
                                        final String string21 = boolean3 ? "false" : ChunkPalettedStorageFix.getProperty(dynamic4, "powered");
                                        this.setBlock(integer11, ChunkPalettedStorageFix.DOOR_MAP.get((string17 + string18 + "lower" + string20 + string19 + string21)));
                                        this.setBlock(integer12, ChunkPalettedStorageFix.DOOR_MAP.get((string17 + string18 + "upper" + string20 + string19 + string21)));
                                    }
                                }
                                continue;
                            }
                            case 175: {
                                for (int integer11 : (IntList)entry9.getValue()) {
                                    integer11 |= integer10;
                                    final Dynamic<?> dynamic2 = this.getBlock(integer11);
                                    if ("upper".equals(ChunkPalettedStorageFix.getProperty(dynamic2, "half"))) {
                                        final Dynamic<?> dynamic3 = this.getBlock(relative(integer11, Direction.DOWN));
                                        final String string16 = ChunkPalettedStorageFix.getName(dynamic3);
                                        if ("minecraft:sunflower".equals(string16)) {
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_SUNFLOWER);
                                        }
                                        else if ("minecraft:lilac".equals(string16)) {
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_LILAC);
                                        }
                                        else if ("minecraft:tall_grass".equals(string16)) {
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_TALL_GRASS);
                                        }
                                        else if ("minecraft:large_fern".equals(string16)) {
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_LARGE_FERN);
                                        }
                                        else if ("minecraft:rose_bush".equals(string16)) {
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_ROSE_BUSH);
                                        }
                                        else {
                                            if (!"minecraft:peony".equals(string16)) {
                                                continue;
                                            }
                                            this.setBlock(integer11, ChunkPalettedStorageFix.UPPER_PEONY);
                                        }
                                    }
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
        
        @Nullable
        private Dynamic<?> getBlockEntity(final int integer) {
            return this.blockEntities.get(integer);
        }
        
        @Nullable
        private Dynamic<?> removeBlockEntity(final int integer) {
            return this.blockEntities.remove(integer);
        }
        
        public static int relative(final int integer, final Direction b) {
            switch (b.getAxis()) {
                case X: {
                    final int integer2 = (integer & 0xF) + b.getAxisDirection().getStep();
                    return (integer2 < 0 || integer2 > 15) ? -1 : ((integer & 0xFFFFFFF0) | integer2);
                }
                case Y: {
                    final int integer3 = (integer >> 8) + b.getAxisDirection().getStep();
                    return (integer3 < 0 || integer3 > 255) ? -1 : ((integer & 0xFF) | integer3 << 8);
                }
                case Z: {
                    final int integer4 = (integer >> 4 & 0xF) + b.getAxisDirection().getStep();
                    return (integer4 < 0 || integer4 > 15) ? -1 : ((integer & 0xFFFFFF0F) | integer4 << 4);
                }
                default: {
                    return -1;
                }
            }
        }
        
        private void setBlock(final int integer, final Dynamic<?> dynamic) {
            if (integer < 0 || integer > 65535) {
                return;
            }
            final Section c4 = this.getSection(integer);
            if (c4 == null) {
                return;
            }
            c4.setBlock(integer & 0xFFF, dynamic);
        }
        
        @Nullable
        private Section getSection(final int integer) {
            final int integer2 = integer >> 12;
            return (integer2 < this.sections.length) ? this.sections[integer2] : null;
        }
        
        public Dynamic<?> getBlock(final int integer) {
            if (integer < 0 || integer > 65535) {
                return ChunkPalettedStorageFix.AIR;
            }
            final Section c3 = this.getSection(integer);
            if (c3 == null) {
                return ChunkPalettedStorageFix.AIR;
            }
            return c3.getBlock(integer & 0xFFF);
        }
        
        public Dynamic<?> write() {
            Dynamic<?> dynamic2 = this.level;
            if (this.blockEntities.isEmpty()) {
                dynamic2 = dynamic2.remove("TileEntities");
            }
            else {
                dynamic2 = dynamic2.set("TileEntities", dynamic2.createList(this.blockEntities.values().stream()));
            }
            Dynamic<?> dynamic3 = dynamic2.emptyMap();
            final List<Dynamic<?>> list4 = (List<Dynamic<?>>)Lists.newArrayList();
            for (final Section c8 : this.sections) {
                if (c8 != null) {
                    list4.add(c8.write());
                    dynamic3 = dynamic3.set(String.valueOf(c8.y), dynamic3.createIntList(Arrays.stream(c8.update.toIntArray())));
                }
            }
            Dynamic<?> dynamic4 = dynamic2.emptyMap();
            dynamic4 = dynamic4.set("Sides", dynamic4.createByte((byte)this.sides));
            dynamic4 = dynamic4.set("Indices", (Dynamic)dynamic3);
            return dynamic2.set("UpgradeData", (Dynamic)dynamic4).set("Sections", dynamic4.createList(list4.stream()));
        }
    }
    
    static class DataLayer {
        private final byte[] data;
        
        public DataLayer() {
            this.data = new byte[2048];
        }
        
        public DataLayer(final byte[] arr) {
            this.data = arr;
            if (arr.length != 2048) {
                throw new IllegalArgumentException(new StringBuilder().append("ChunkNibbleArrays should be 2048 bytes not: ").append(arr.length).toString());
            }
        }
        
        public int get(final int integer1, final int integer2, final int integer3) {
            final int integer4 = this.getPosition(integer2 << 8 | integer3 << 4 | integer1);
            if (this.isFirst(integer2 << 8 | integer3 << 4 | integer1)) {
                return this.data[integer4] & 0xF;
            }
            return this.data[integer4] >> 4 & 0xF;
        }
        
        private boolean isFirst(final int integer) {
            return (integer & 0x1) == 0x0;
        }
        
        private int getPosition(final int integer) {
            return integer >> 1;
        }
    }
    
    public enum Direction {
        DOWN(AxisDirection.NEGATIVE, Axis.Y), 
        UP(AxisDirection.POSITIVE, Axis.Y), 
        NORTH(AxisDirection.NEGATIVE, Axis.Z), 
        SOUTH(AxisDirection.POSITIVE, Axis.Z), 
        WEST(AxisDirection.NEGATIVE, Axis.X), 
        EAST(AxisDirection.POSITIVE, Axis.X);
        
        private final Axis axis;
        private final AxisDirection axisDirection;
        
        private Direction(final AxisDirection b, final Axis a) {
            this.axis = a;
            this.axisDirection = b;
        }
        
        public AxisDirection getAxisDirection() {
            return this.axisDirection;
        }
        
        public Axis getAxis() {
            return this.axis;
        }
        
        public enum Axis {
            X, 
            Y, 
            Z;
        }
        
        public enum AxisDirection {
            POSITIVE(1), 
            NEGATIVE(-1);
            
            private final int step;
            
            private AxisDirection(final int integer3) {
                this.step = integer3;
            }
            
            public int getStep() {
                return this.step;
            }
        }
    }
}
