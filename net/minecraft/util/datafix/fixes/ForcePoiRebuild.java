package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ForcePoiRebuild extends DataFix {
    public ForcePoiRebuild(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = (Type<Pair<String, Dynamic<?>>>)DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.fixTypeEverywhere("POI rebuild", (Type)type2, dynamicOps -> pair -> pair.mapSecond(ForcePoiRebuild::cap));
    }
    
    private static <T> Dynamic<T> cap(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.update("Sections", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.remove("Valid"))));
    }
}
