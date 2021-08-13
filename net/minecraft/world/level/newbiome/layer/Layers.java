package net.minecraft.world.level.newbiome.layer;

import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.area.Area;
import java.util.function.LongFunction;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

public class Layers {
    private static final Int2IntMap CATEGORIES;
    
    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> zoom(final long long1, final AreaTransformer1 cwj, final AreaFactory<T> cvd, final int integer, final LongFunction<C> longFunction) {
        AreaFactory<T> cvd2 = cvd;
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            cvd2 = cwj.<T>run((BigContext<T>)longFunction.apply(long1 + integer2), cvd2);
        }
        return cvd2;
    }
    
    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> getDefaultLayer(final boolean boolean1, final int integer2, final int integer3, final LongFunction<C> longFunction) {
        AreaFactory<T> cvd5 = IslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1L));
        cvd5 = ZoomLayer.FUZZY.<T>run((BigContext<T>)longFunction.apply(2000L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1L), cvd5);
        cvd5 = ZoomLayer.NORMAL.<T>run((BigContext<T>)longFunction.apply(2001L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(50L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(70L), cvd5);
        cvd5 = RemoveTooMuchOceanLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L), cvd5);
        AreaFactory<T> cvd6 = OceanLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L));
        cvd6 = Layers.<T, C>zoom(2001L, ZoomLayer.NORMAL, cvd6, 6, longFunction);
        cvd5 = AddSnowLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(3L), cvd5);
        cvd5 = AddEdgeLayer.CoolWarm.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L), cvd5);
        cvd5 = AddEdgeLayer.HeatIce.INSTANCE.<T>run((BigContext<T>)longFunction.apply(2L), cvd5);
        cvd5 = AddEdgeLayer.IntroduceSpecial.INSTANCE.<T>run((BigContext<T>)longFunction.apply(3L), cvd5);
        cvd5 = ZoomLayer.NORMAL.<T>run((BigContext<T>)longFunction.apply(2002L), cvd5);
        cvd5 = ZoomLayer.NORMAL.<T>run((BigContext<T>)longFunction.apply(2003L), cvd5);
        cvd5 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(4L), cvd5);
        cvd5 = AddMushroomIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(5L), cvd5);
        cvd5 = AddDeepOceanLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(4L), cvd5);
        AreaFactory<T> cvd7;
        cvd5 = (cvd7 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd5, 0, longFunction));
        cvd7 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd7, 0, longFunction);
        cvd7 = RiverInitLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(100L), cvd7);
        AreaFactory<T> cvd8 = cvd5;
        cvd8 = new BiomeInitLayer(boolean1).<T>run((BigContext<T>)longFunction.apply(200L), cvd8);
        cvd8 = RareBiomeLargeLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1001L), cvd8);
        cvd8 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd8, 2, longFunction);
        cvd8 = BiomeEdgeLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1000L), cvd8);
        AreaFactory<T> cvd9 = cvd7;
        cvd9 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd9, 2, longFunction);
        cvd8 = RegionHillsLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1000L), cvd8, cvd9);
        cvd7 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd7, 2, longFunction);
        cvd7 = Layers.<T, C>zoom(1000L, ZoomLayer.NORMAL, cvd7, integer3, longFunction);
        cvd7 = RiverLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1L), cvd7);
        cvd7 = SmoothLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1000L), cvd7);
        cvd8 = RareBiomeSpotLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1001L), cvd8);
        for (int integer4 = 0; integer4 < integer2; ++integer4) {
            cvd8 = ZoomLayer.NORMAL.<T>run((BigContext<T>)longFunction.apply((long)(1000 + integer4)), cvd8);
            if (integer4 == 0) {
                cvd8 = AddIslandLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(3L), cvd8);
            }
            if (integer4 == 1 || integer2 == 1) {
                cvd8 = ShoreLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1000L), cvd8);
            }
        }
        cvd8 = SmoothLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(1000L), cvd8);
        cvd8 = RiverMixerLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(100L), cvd8, cvd7);
        cvd8 = OceanMixerLayer.INSTANCE.<T>run((BigContext<T>)longFunction.apply(100L), cvd8, cvd6);
        return cvd8;
    }
    
    public static Layer getDefaultLayer(final long long1, final boolean boolean2, final int integer3, final int integer4) {
        final int integer5 = 25;
        final AreaFactory<LazyArea> cvd7 = Layers.<LazyArea, BigContext>getDefaultLayer(boolean2, integer3, integer4, (java.util.function.LongFunction<BigContext>)(long2 -> new LazyAreaContext(25, long1, long2)));
        return new Layer(cvd7);
    }
    
    public static boolean isSame(final int integer1, final int integer2) {
        return integer1 == integer2 || Layers.CATEGORIES.get(integer1) == Layers.CATEGORIES.get(integer2);
    }
    
    private static void register(final Int2IntOpenHashMap int2IntOpenHashMap, final Category a, final int integer) {
        int2IntOpenHashMap.put(integer, a.ordinal());
    }
    
    protected static boolean isOcean(final int integer) {
        return integer == 44 || integer == 45 || integer == 0 || integer == 46 || integer == 10 || integer == 47 || integer == 48 || integer == 24 || integer == 49 || integer == 50;
    }
    
    protected static boolean isShallowOcean(final int integer) {
        return integer == 44 || integer == 45 || integer == 0 || integer == 46 || integer == 10;
    }
    
    static {
        CATEGORIES = Util.<Int2IntMap>make((Int2IntMap)new Int2IntOpenHashMap(), (java.util.function.Consumer<Int2IntMap>)(int2IntOpenHashMap -> {
            register(int2IntOpenHashMap, Category.BEACH, 16);
            register(int2IntOpenHashMap, Category.BEACH, 26);
            register(int2IntOpenHashMap, Category.DESERT, 2);
            register(int2IntOpenHashMap, Category.DESERT, 17);
            register(int2IntOpenHashMap, Category.DESERT, 130);
            register(int2IntOpenHashMap, Category.EXTREME_HILLS, 131);
            register(int2IntOpenHashMap, Category.EXTREME_HILLS, 162);
            register(int2IntOpenHashMap, Category.EXTREME_HILLS, 20);
            register(int2IntOpenHashMap, Category.EXTREME_HILLS, 3);
            register(int2IntOpenHashMap, Category.EXTREME_HILLS, 34);
            register(int2IntOpenHashMap, Category.FOREST, 27);
            register(int2IntOpenHashMap, Category.FOREST, 28);
            register(int2IntOpenHashMap, Category.FOREST, 29);
            register(int2IntOpenHashMap, Category.FOREST, 157);
            register(int2IntOpenHashMap, Category.FOREST, 132);
            register(int2IntOpenHashMap, Category.FOREST, 4);
            register(int2IntOpenHashMap, Category.FOREST, 155);
            register(int2IntOpenHashMap, Category.FOREST, 156);
            register(int2IntOpenHashMap, Category.FOREST, 18);
            register(int2IntOpenHashMap, Category.ICY, 140);
            register(int2IntOpenHashMap, Category.ICY, 13);
            register(int2IntOpenHashMap, Category.ICY, 12);
            register(int2IntOpenHashMap, Category.JUNGLE, 168);
            register(int2IntOpenHashMap, Category.JUNGLE, 169);
            register(int2IntOpenHashMap, Category.JUNGLE, 21);
            register(int2IntOpenHashMap, Category.JUNGLE, 23);
            register(int2IntOpenHashMap, Category.JUNGLE, 22);
            register(int2IntOpenHashMap, Category.JUNGLE, 149);
            register(int2IntOpenHashMap, Category.JUNGLE, 151);
            register(int2IntOpenHashMap, Category.MESA, 37);
            register(int2IntOpenHashMap, Category.MESA, 165);
            register(int2IntOpenHashMap, Category.MESA, 167);
            register(int2IntOpenHashMap, Category.MESA, 166);
            register(int2IntOpenHashMap, Category.BADLANDS_PLATEAU, 39);
            register(int2IntOpenHashMap, Category.BADLANDS_PLATEAU, 38);
            register(int2IntOpenHashMap, Category.MUSHROOM, 14);
            register(int2IntOpenHashMap, Category.MUSHROOM, 15);
            register(int2IntOpenHashMap, Category.NONE, 25);
            register(int2IntOpenHashMap, Category.OCEAN, 46);
            register(int2IntOpenHashMap, Category.OCEAN, 49);
            register(int2IntOpenHashMap, Category.OCEAN, 50);
            register(int2IntOpenHashMap, Category.OCEAN, 48);
            register(int2IntOpenHashMap, Category.OCEAN, 24);
            register(int2IntOpenHashMap, Category.OCEAN, 47);
            register(int2IntOpenHashMap, Category.OCEAN, 10);
            register(int2IntOpenHashMap, Category.OCEAN, 45);
            register(int2IntOpenHashMap, Category.OCEAN, 0);
            register(int2IntOpenHashMap, Category.OCEAN, 44);
            register(int2IntOpenHashMap, Category.PLAINS, 1);
            register(int2IntOpenHashMap, Category.PLAINS, 129);
            register(int2IntOpenHashMap, Category.RIVER, 11);
            register(int2IntOpenHashMap, Category.RIVER, 7);
            register(int2IntOpenHashMap, Category.SAVANNA, 35);
            register(int2IntOpenHashMap, Category.SAVANNA, 36);
            register(int2IntOpenHashMap, Category.SAVANNA, 163);
            register(int2IntOpenHashMap, Category.SAVANNA, 164);
            register(int2IntOpenHashMap, Category.SWAMP, 6);
            register(int2IntOpenHashMap, Category.SWAMP, 134);
            register(int2IntOpenHashMap, Category.TAIGA, 160);
            register(int2IntOpenHashMap, Category.TAIGA, 161);
            register(int2IntOpenHashMap, Category.TAIGA, 32);
            register(int2IntOpenHashMap, Category.TAIGA, 33);
            register(int2IntOpenHashMap, Category.TAIGA, 30);
            register(int2IntOpenHashMap, Category.TAIGA, 31);
            register(int2IntOpenHashMap, Category.TAIGA, 158);
            register(int2IntOpenHashMap, Category.TAIGA, 5);
            register(int2IntOpenHashMap, Category.TAIGA, 19);
            register(int2IntOpenHashMap, Category.TAIGA, 133);
        }));
    }
    
    enum Category {
        NONE, 
        TAIGA, 
        EXTREME_HILLS, 
        JUNGLE, 
        MESA, 
        BADLANDS_PLATEAU, 
        PLAINS, 
        SAVANNA, 
        ICY, 
        BEACH, 
        FOREST, 
        OCEAN, 
        DESERT, 
        RIVER, 
        SWAMP, 
        MUSHROOM;
    }
}
