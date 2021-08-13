package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ReorganizePoi extends DataFix {
    public ReorganizePoi(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = (Type<Pair<String, Dynamic<?>>>)DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.fixTypeEverywhere("POI reorganization", (Type)type2, dynamicOps -> pair -> pair.mapSecond(ReorganizePoi::cap));
    }
    
    private static <T> Dynamic<T> cap(Dynamic<T> dynamic) {
        final Map<Dynamic<T>, Dynamic<T>> map2 = (Map<Dynamic<T>, Dynamic<T>>)Maps.newHashMap();
        for (int integer3 = 0; integer3 < 16; ++integer3) {
            final String string4 = String.valueOf(integer3);
            final Optional<Dynamic<T>> optional5 = (Optional<Dynamic<T>>)dynamic.get(string4).result();
            if (optional5.isPresent()) {
                final Dynamic<T> dynamic2 = (Dynamic<T>)optional5.get();
                final Dynamic<T> dynamic3 = (Dynamic<T>)dynamic.createMap((Map)ImmutableMap.of(dynamic.createString("Records"), dynamic2));
                map2.put(dynamic.createInt(integer3), dynamic3);
                dynamic = (Dynamic<T>)dynamic.remove(string4);
            }
        }
        return (Dynamic<T>)dynamic.set("Sections", dynamic.createMap((Map)map2));
    }
}
