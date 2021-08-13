package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class StriderGravityFix extends NamedEntityFix {
    public StriderGravityFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "StriderGravityFix", References.ENTITY, "minecraft:strider");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        if (dynamic.get("NoGravity").asBoolean(false)) {
            return dynamic.set("NoGravity", dynamic.createBoolean(false));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
}
