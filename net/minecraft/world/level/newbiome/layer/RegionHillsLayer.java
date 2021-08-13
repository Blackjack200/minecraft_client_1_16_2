package net.minecraft.world.level.newbiome.layer;

import java.util.function.Consumer;
import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.Context;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset1Transformer;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;

public enum RegionHillsLayer implements AreaTransformer2, DimensionOffset1Transformer {
    INSTANCE;
    
    private static final Logger LOGGER;
    private static final Int2IntMap MUTATIONS;
    
    public int applyPixel(final Context cvh, final Area cvc2, final Area cvc3, final int integer4, final int integer5) {
        final int integer6 = cvc2.get(this.getParentX(integer4 + 1), this.getParentY(integer5 + 1));
        final int integer7 = cvc3.get(this.getParentX(integer4 + 1), this.getParentY(integer5 + 1));
        if (integer6 > 255) {
            RegionHillsLayer.LOGGER.debug("old! {}", integer6);
        }
        final int integer8 = (integer7 - 2) % 29;
        if (!Layers.isShallowOcean(integer6) && integer7 >= 2 && integer8 == 1) {
            return RegionHillsLayer.MUTATIONS.getOrDefault(integer6, integer6);
        }
        if (cvh.nextRandom(3) == 0 || integer8 == 0) {
            int integer9;
            if ((integer9 = integer6) == 2) {
                integer9 = 17;
            }
            else if (integer6 == 4) {
                integer9 = 18;
            }
            else if (integer6 == 27) {
                integer9 = 28;
            }
            else if (integer6 == 29) {
                integer9 = 1;
            }
            else if (integer6 == 5) {
                integer9 = 19;
            }
            else if (integer6 == 32) {
                integer9 = 33;
            }
            else if (integer6 == 30) {
                integer9 = 31;
            }
            else if (integer6 == 1) {
                integer9 = ((cvh.nextRandom(3) == 0) ? 18 : 4);
            }
            else if (integer6 == 12) {
                integer9 = 13;
            }
            else if (integer6 == 21) {
                integer9 = 22;
            }
            else if (integer6 == 168) {
                integer9 = 169;
            }
            else if (integer6 == 0) {
                integer9 = 24;
            }
            else if (integer6 == 45) {
                integer9 = 48;
            }
            else if (integer6 == 46) {
                integer9 = 49;
            }
            else if (integer6 == 10) {
                integer9 = 50;
            }
            else if (integer6 == 3) {
                integer9 = 34;
            }
            else if (integer6 == 35) {
                integer9 = 36;
            }
            else if (Layers.isSame(integer6, 38)) {
                integer9 = 37;
            }
            else if ((integer6 == 24 || integer6 == 48 || integer6 == 49 || integer6 == 50) && cvh.nextRandom(3) == 0) {
                integer9 = ((cvh.nextRandom(2) == 0) ? 1 : 4);
            }
            if (integer8 == 0 && integer9 != integer6) {
                integer9 = RegionHillsLayer.MUTATIONS.getOrDefault(integer9, integer6);
            }
            if (integer9 != integer6) {
                int integer10 = 0;
                if (Layers.isSame(cvc2.get(this.getParentX(integer4 + 1), this.getParentY(integer5 + 0)), integer6)) {
                    ++integer10;
                }
                if (Layers.isSame(cvc2.get(this.getParentX(integer4 + 2), this.getParentY(integer5 + 1)), integer6)) {
                    ++integer10;
                }
                if (Layers.isSame(cvc2.get(this.getParentX(integer4 + 0), this.getParentY(integer5 + 1)), integer6)) {
                    ++integer10;
                }
                if (Layers.isSame(cvc2.get(this.getParentX(integer4 + 1), this.getParentY(integer5 + 2)), integer6)) {
                    ++integer10;
                }
                if (integer10 >= 3) {
                    return integer9;
                }
            }
        }
        return integer6;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MUTATIONS = Util.<Int2IntMap>make((Int2IntMap)new Int2IntOpenHashMap(), (java.util.function.Consumer<Int2IntMap>)(int2IntOpenHashMap -> {
            int2IntOpenHashMap.put(1, 129);
            int2IntOpenHashMap.put(2, 130);
            int2IntOpenHashMap.put(3, 131);
            int2IntOpenHashMap.put(4, 132);
            int2IntOpenHashMap.put(5, 133);
            int2IntOpenHashMap.put(6, 134);
            int2IntOpenHashMap.put(12, 140);
            int2IntOpenHashMap.put(21, 149);
            int2IntOpenHashMap.put(23, 151);
            int2IntOpenHashMap.put(27, 155);
            int2IntOpenHashMap.put(28, 156);
            int2IntOpenHashMap.put(29, 157);
            int2IntOpenHashMap.put(30, 158);
            int2IntOpenHashMap.put(32, 160);
            int2IntOpenHashMap.put(33, 161);
            int2IntOpenHashMap.put(34, 162);
            int2IntOpenHashMap.put(35, 163);
            int2IntOpenHashMap.put(36, 164);
            int2IntOpenHashMap.put(37, 165);
            int2IntOpenHashMap.put(38, 166);
            int2IntOpenHashMap.put(39, 167);
        }));
    }
}
