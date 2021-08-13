package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.UUID;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityStringUuidFix extends DataFix {
    public EntityStringUuidFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityStringUuidFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            final Optional<String> optional2 = (Optional<String>)dynamic.get("UUID").asString().result();
            if (optional2.isPresent()) {
                final UUID uUID3 = UUID.fromString((String)optional2.get());
                return dynamic.remove("UUID").set("UUIDMost", dynamic.createLong(uUID3.getMostSignificantBits())).set("UUIDLeast", dynamic.createLong(uUID3.getLeastSignificantBits()));
            }
            return dynamic;
        }));
    }
}
