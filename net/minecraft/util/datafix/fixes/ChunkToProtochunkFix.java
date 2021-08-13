package net.minecraft.util.datafix.fixes;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DataFixUtils;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;
import java.util.Arrays;
import java.nio.ByteBuffer;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ChunkToProtochunkFix extends DataFix {
    public ChunkToProtochunkFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type3 = this.getOutputSchema().getType(References.CHUNK);
        final Type<?> type4 = type2.findFieldType("Level");
        final Type<?> type5 = type3.findFieldType("Level");
        final Type<?> type6 = type4.findFieldType("TileTicks");
        final OpticFinder<?> opticFinder7 = DSL.fieldFinder("Level", (Type)type4);
        final OpticFinder<?> opticFinder8 = DSL.fieldFinder("TileTicks", (Type)type6);
        return TypeRewriteRule.seq(this.fixTypeEverywhereTyped("ChunkToProtoChunkFix", (Type)type2, this.getOutputSchema().getType(References.CHUNK), typed -> typed.updateTyped(opticFinder7, type5, typed -> {
            final Optional<? extends Stream<? extends Dynamic<?>>> optional4 = typed.getOptionalTyped(opticFinder8).flatMap(typed -> typed.write().result()).flatMap(dynamic -> dynamic.asStreamOpt().result());
            Dynamic<?> dynamic5 = typed.get(DSL.remainderFinder());
            final boolean boolean6 = dynamic5.get("TerrainPopulated").asBoolean(false) && (!dynamic5.get("LightPopulated").asNumber().result().isPresent() || dynamic5.get("LightPopulated").asBoolean(false));
            dynamic5 = dynamic5.set("Status", dynamic5.createString(boolean6 ? "mobs_spawned" : "empty"));
            dynamic5 = dynamic5.set("hasLegacyStructureData", dynamic5.createBoolean(true));
            Dynamic<?> dynamic7;
            if (boolean6) {
                final Optional<ByteBuffer> optional5 = (Optional<ByteBuffer>)dynamic5.get("Biomes").asByteBufferOpt().result();
                if (optional5.isPresent()) {
                    final ByteBuffer byteBuffer9 = (ByteBuffer)optional5.get();
                    final int[] arr10 = new int[256];
                    for (int integer11 = 0; integer11 < arr10.length; ++integer11) {
                        if (integer11 < byteBuffer9.capacity()) {
                            arr10[integer11] = (byteBuffer9.get(integer11) & 0xFF);
                        }
                    }
                    dynamic5 = dynamic5.set("Biomes", dynamic5.createIntList(Arrays.stream(arr10)));
                }
                final Dynamic<?> dynamic6 = dynamic5;
                final List<ShortList> list10 = (List<ShortList>)IntStream.range(0, 16).mapToObj(integer -> new ShortArrayList()).collect(Collectors.toList());
                if (optional4.isPresent()) {
                    ((Stream)optional4.get()).forEach(dynamic -> {
                        final int integer3 = dynamic.get("x").asInt(0);
                        final int integer4 = dynamic.get("y").asInt(0);
                        final int integer5 = dynamic.get("z").asInt(0);
                        final short short6 = packOffsetCoordinates(integer3, integer4, integer5);
                        ((ShortList)list10.get(integer4 >> 4)).add(short6);
                    });
                    dynamic5 = dynamic5.set("ToBeTicked", dynamic5.createList(list10.stream().map(shortList -> dynamic6.createList(shortList.stream().map(dynamic6::createShort)))));
                }
                dynamic7 = DataFixUtils.orElse(typed.set(DSL.remainderFinder(), dynamic5).write().result(), dynamic5);
            }
            else {
                dynamic7 = dynamic5;
            }
            return (Typed)((Pair)type5.readTyped((Dynamic)dynamic7).result().orElseThrow(() -> new IllegalStateException("Could not read the new chunk"))).getFirst();
        })), this.writeAndRead("Structure biome inject", this.getInputSchema().getType(References.STRUCTURE_FEATURE), this.getOutputSchema().getType(References.STRUCTURE_FEATURE)));
    }
    
    private static short packOffsetCoordinates(final int integer1, final int integer2, final int integer3) {
        return (short)((integer1 & 0xF) | (integer2 & 0xF) << 4 | (integer3 & 0xF) << 8);
    }
}
