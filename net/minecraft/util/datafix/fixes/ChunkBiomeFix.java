package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Arrays;
import java.util.stream.IntStream;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ChunkBiomeFix extends DataFix {
    public ChunkBiomeFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final OpticFinder<?> opticFinder3 = type2.findField("Level");
        return this.fixTypeEverywhereTyped("Leaves fix", (Type)type2, typed -> typed.updateTyped(opticFinder3, typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            final Optional<IntStream> optional2 = (Optional<IntStream>)dynamic.get("Biomes").asIntStreamOpt().result();
            if (!optional2.isPresent()) {
                return dynamic;
            }
            final int[] arr3 = ((IntStream)optional2.get()).toArray();
            final int[] arr4 = new int[1024];
            for (int integer5 = 0; integer5 < 4; ++integer5) {
                for (int integer6 = 0; integer6 < 4; ++integer6) {
                    final int integer7 = (integer6 << 2) + 2;
                    final int integer8 = (integer5 << 2) + 2;
                    final int integer9 = integer8 << 4 | integer7;
                    arr4[integer5 << 2 | integer6] = ((integer9 < arr3.length) ? arr3[integer9] : -1);
                }
            }
            for (int integer5 = 1; integer5 < 64; ++integer5) {
                System.arraycopy(arr4, 0, arr4, integer5 * 16, 16);
            }
            return dynamic.set("Biomes", dynamic.createIntList(Arrays.stream(arr4)));
        })));
    }
}
