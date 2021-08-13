package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityCatSplitFix extends SimpleEntityRenameFix {
    public EntityCatSplitFix(final Schema schema, final boolean boolean2) {
        super("EntityCatSplitFix", schema, boolean2);
    }
    
    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(final String string, Dynamic<?> dynamic) {
        if (Objects.equals("minecraft:ocelot", string)) {
            final int integer4 = dynamic.get("CatType").asInt(0);
            if (integer4 == 0) {
                final String string2 = dynamic.get("Owner").asString("");
                final String string3 = dynamic.get("OwnerUUID").asString("");
                if (string2.length() > 0 || string3.length() > 0) {
                    dynamic.set("Trusting", dynamic.createBoolean(true));
                }
            }
            else if (integer4 > 0 && integer4 < 4) {
                dynamic = dynamic.set("CatType", dynamic.createInt(integer4));
                dynamic = dynamic.set("OwnerUUID", dynamic.createString(dynamic.get("OwnerUUID").asString("")));
                return (Pair<String, Dynamic<?>>)Pair.of("minecraft:cat", dynamic);
            }
        }
        return (Pair<String, Dynamic<?>>)Pair.of(string, dynamic);
    }
}
