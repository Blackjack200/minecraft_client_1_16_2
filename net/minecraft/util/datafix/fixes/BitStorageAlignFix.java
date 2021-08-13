package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.util.Mth;
import java.util.stream.LongStream;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class BitStorageAlignFix extends DataFix {
    public BitStorageAlignFix(final Schema schema) {
        super(schema, false);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final OpticFinder<?> opticFinder4 = DSL.fieldFinder("Level", (Type)type3);
        final OpticFinder<?> opticFinder5 = opticFinder4.type().findField("Sections");
        final Type<?> type4 = ((List.ListType)opticFinder5.type()).getElement();
        final OpticFinder<?> opticFinder6 = DSL.typeFinder((Type)type4);
        final Type<Pair<String, Dynamic<?>>> type5 = (Type<Pair<String, Dynamic<?>>>)DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
        final OpticFinder<java.util.List<Pair<String, Dynamic<?>>>> opticFinder7 = (OpticFinder<java.util.List<Pair<String, Dynamic<?>>>>)DSL.fieldFinder("Palette", (Type)DSL.list((Type)type5));
        return this.fixTypeEverywhereTyped("BitStorageAlignFix", (Type)type2, this.getOutputSchema().getType(References.CHUNK), typed -> typed.updateTyped(opticFinder4, typed -> this.updateHeightmaps(updateSections(opticFinder5, opticFinder6, opticFinder7, typed))));
    }
    
    private Typed<?> updateHeightmaps(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("Heightmaps", dynamic2 -> dynamic2.updateMapValues(pair -> pair.mapSecond(dynamic2 -> updateBitStorage(dynamic, dynamic2, 256, 9)))));
    }
    
    private static Typed<?> updateSections(final OpticFinder<?> opticFinder1, final OpticFinder<?> opticFinder2, final OpticFinder<java.util.List<Pair<String, Dynamic<?>>>> opticFinder3, final Typed<?> typed) {
        return typed.updateTyped((OpticFinder)opticFinder1, typed -> typed.updateTyped(opticFinder2, typed -> {
            final int integer3 = (int)typed.getOptional(opticFinder3).map(list -> Math.max(4, DataFixUtils.ceillog2(list.size()))).orElse(0);
            if (integer3 == 0 || Mth.isPowerOfTwo(integer3)) {
                return typed;
            }
            return typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("BlockStates", dynamic3 -> updateBitStorage(dynamic, dynamic3, 4096, integer3)));
        }));
    }
    
    private static Dynamic<?> updateBitStorage(final Dynamic<?> dynamic1, final Dynamic<?> dynamic2, final int integer3, final int integer4) {
        final long[] arr5 = dynamic2.asLongStream().toArray();
        final long[] arr6 = addPadding(integer3, integer4, arr5);
        return dynamic1.createLongList(LongStream.of(arr6));
    }
    
    public static long[] addPadding(final int integer1, final int integer2, final long[] arr) {
        final int integer3 = arr.length;
        if (integer3 == 0) {
            return arr;
        }
        final long long5 = (1L << integer2) - 1L;
        final int integer4 = 64 / integer2;
        final int integer5 = (integer1 + integer4 - 1) / integer4;
        final long[] arr2 = new long[integer5];
        int integer6 = 0;
        int integer7 = 0;
        long long6 = 0L;
        int integer8 = 0;
        long long7 = arr[0];
        long long8 = (integer3 > 1) ? arr[1] : 0L;
        for (int integer9 = 0; integer9 < integer1; ++integer9) {
            final int integer10 = integer9 * integer2;
            final int integer11 = integer10 >> 6;
            final int integer12 = (integer9 + 1) * integer2 - 1 >> 6;
            final int integer13 = integer10 ^ integer11 << 6;
            if (integer11 != integer8) {
                long7 = long8;
                long8 = ((integer11 + 1 < integer3) ? arr[integer11 + 1] : 0L);
                integer8 = integer11;
            }
            long long9;
            if (integer11 == integer12) {
                long9 = (long7 >>> integer13 & long5);
            }
            else {
                final int integer14 = 64 - integer13;
                long9 = ((long7 >>> integer13 | long8 << integer14) & long5);
            }
            final int integer14 = integer7 + integer2;
            if (integer14 >= 64) {
                arr2[integer6++] = long6;
                long6 = long9;
                integer7 = integer2;
            }
            else {
                long6 |= long9 << integer7;
                integer7 = integer14;
            }
        }
        if (long6 != 0L) {
            arr2[integer6] = long6;
        }
        return arr2;
    }
}
