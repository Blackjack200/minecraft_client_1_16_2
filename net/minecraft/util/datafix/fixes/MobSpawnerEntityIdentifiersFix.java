package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.DataResult;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import java.util.stream.Stream;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class MobSpawnerEntityIdentifiersFix extends DataFix {
    public MobSpawnerEntityIdentifiersFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private Dynamic<?> fix(Dynamic<?> dynamic) {
        if (!"MobSpawner".equals(dynamic.get("id").asString(""))) {
            return dynamic;
        }
        final Optional<String> optional3 = (Optional<String>)dynamic.get("EntityId").asString().result();
        if (optional3.isPresent()) {
            Dynamic<?> dynamic2 = DataFixUtils.orElse(dynamic.get("SpawnData").result(), dynamic.emptyMap());
            dynamic2 = dynamic2.set("id", dynamic2.createString(((String)optional3.get()).isEmpty() ? "Pig" : ((String)optional3.get())));
            dynamic = dynamic.set("SpawnData", (Dynamic)dynamic2);
            dynamic = dynamic.remove("EntityId");
        }
        final Optional<? extends Stream<? extends Dynamic<?>>> optional4 = dynamic.get("SpawnPotentials").asStreamOpt().result();
        if (optional4.isPresent()) {
            dynamic = dynamic.set("SpawnPotentials", dynamic.createList(((Stream)optional4.get()).map(dynamic -> {
                final Optional<String> optional2 = (Optional<String>)dynamic.get("Type").asString().result();
                if (optional2.isPresent()) {
                    final Dynamic<?> dynamic2 = ((Dynamic)DataFixUtils.orElse(dynamic.get("Properties").result(), dynamic.emptyMap())).set("id", dynamic.createString((String)optional2.get()));
                    return dynamic.set("Entity", (Dynamic)dynamic2).remove("Type").remove("Properties");
                }
                return dynamic;
            })));
        }
        return dynamic;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.UNTAGGED_SPAWNER);
        return this.fixTypeEverywhereTyped("MobSpawnerEntityIdentifiersFix", this.getInputSchema().getType(References.UNTAGGED_SPAWNER), (Type)type2, typed -> {
            Dynamic<?> dynamic4 = typed.get(DSL.remainderFinder());
            dynamic4 = dynamic4.set("id", dynamic4.createString("MobSpawner"));
            final DataResult<? extends Pair<? extends Typed<?>, ?>> dataResult5 = type2.readTyped((Dynamic)this.fix(dynamic4));
            if (!dataResult5.result().isPresent()) {
                return typed;
            }
            return (Typed)((Pair)dataResult5.result().get()).getFirst();
        });
    }
}
