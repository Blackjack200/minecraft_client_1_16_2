package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.DataFixUtils;
import java.util.stream.Stream;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class PoiTypeRename extends DataFix {
    public PoiTypeRename(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = (Type<Pair<String, Dynamic<?>>>)DSL.named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.fixTypeEverywhere("POI rename", (Type)type2, dynamicOps -> pair -> pair.mapSecond(this::cap));
    }
    
    private <T> Dynamic<T> cap(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.update("Sections", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.update("Records", dynamic -> (Dynamic)DataFixUtils.orElse((Optional)this.renameRecords((com.mojang.serialization.Dynamic<Object>)dynamic), dynamic)))));
    }
    
    private <T> Optional<Dynamic<T>> renameRecords(final Dynamic<T> dynamic) {
        return (Optional<Dynamic<T>>)dynamic.asStreamOpt().map(stream -> dynamic.createList(stream.map(dynamic -> dynamic.update("type", dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asString().map(this::rename).map(dynamic::createString).result(), dynamic))))).result();
    }
    
    protected abstract String rename(final String string);
}
