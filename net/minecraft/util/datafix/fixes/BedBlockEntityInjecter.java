package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.DynamicOps;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.Map;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.Maps;
import java.util.function.Function;
import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class BedBlockEntityInjecter extends DataFix {
    public BedBlockEntityInjecter(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final Type<?> type4 = type3.findFieldType("TileEntities");
        if (!(type4 instanceof List.ListType)) {
            throw new IllegalStateException("Tile entity type is not a list type.");
        }
        final List.ListType<?> listType5 = type4;
        return this.cap(type3, listType5);
    }
    
    private <TE> TypeRewriteRule cap(final Type<?> type, final List.ListType<TE> listType) {
        final Type<TE> type2 = (Type<TE>)listType.getElement();
        final OpticFinder<?> opticFinder5 = DSL.fieldFinder("Level", (Type)type);
        final OpticFinder<java.util.List<TE>> opticFinder6 = (OpticFinder<java.util.List<TE>>)DSL.fieldFinder("TileEntities", (Type)listType);
        final int integer7 = 416;
        return TypeRewriteRule.seq(this.fixTypeEverywhere("InjectBedBlockEntityType", (Type)this.getInputSchema().findChoiceType(References.BLOCK_ENTITY), (Type)this.getOutputSchema().findChoiceType(References.BLOCK_ENTITY), dynamicOps -> pair -> pair), this.fixTypeEverywhereTyped("BedBlockEntityInjecter", this.getOutputSchema().getType(References.CHUNK), typed -> {
            final Typed<?> typed2 = typed.getTyped(opticFinder5);
            final Dynamic<?> dynamic6 = typed2.get(DSL.remainderFinder());
            final int integer7 = dynamic6.get("xPos").asInt(0);
            final int integer8 = dynamic6.get("zPos").asInt(0);
            final java.util.List<Object> list9 = (java.util.List<Object>)Lists.newArrayList((Iterable)typed2.getOrCreate(opticFinder6));
            final java.util.List<? extends Dynamic<?>> list10 = dynamic6.get("Sections").asList(Function.identity());
            for (int integer9 = 0; integer9 < list10.size(); ++integer9) {
                final Dynamic<?> dynamic7 = list10.get(integer9);
                final int integer10 = dynamic7.get("Y").asInt(0);
                final Stream<Integer> stream14 = (Stream<Integer>)dynamic7.get("Blocks").asStream().map(dynamic -> dynamic.asInt(0));
                int integer11 = 0;
                for (final int integer12 : stream14::iterator) {
                    if (416 == (integer12 & 0xFF) << 4) {
                        final int integer13 = integer11 & 0xF;
                        final int integer14 = integer11 >> 8 & 0xF;
                        final int integer15 = integer11 >> 4 & 0xF;
                        final Map<Dynamic<?>, Dynamic<?>> map21 = (Map<Dynamic<?>, Dynamic<?>>)Maps.newHashMap();
                        map21.put(dynamic7.createString("id"), dynamic7.createString("minecraft:bed"));
                        map21.put(dynamic7.createString("x"), dynamic7.createInt(integer13 + (integer7 << 4)));
                        map21.put(dynamic7.createString("y"), dynamic7.createInt(integer14 + (integer10 << 4)));
                        map21.put(dynamic7.createString("z"), dynamic7.createInt(integer15 + (integer8 << 4)));
                        map21.put(dynamic7.createString("color"), dynamic7.createShort((short)14));
                        list9.add(((Pair)type2.read(dynamic7.createMap((Map)map21)).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created bed block entity."))).getFirst());
                    }
                    ++integer11;
                }
            }
            if (!list9.isEmpty()) {
                return typed.set(opticFinder5, typed2.set(opticFinder6, list9));
            }
            return typed;
        }));
    }
}
