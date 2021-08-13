package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public abstract class SimpleEntityRenameFix extends EntityRenameFix {
    public SimpleEntityRenameFix(final String string, final Schema schema, final boolean boolean3) {
        super(string, schema, boolean3);
    }
    
    @Override
    protected Pair<String, Typed<?>> fix(final String string, final Typed<?> typed) {
        final Pair<String, Dynamic<?>> pair4 = this.getNewNameAndTag(string, typed.getOrCreate(DSL.remainderFinder()));
        return (Pair<String, Typed<?>>)Pair.of(pair4.getFirst(), typed.set(DSL.remainderFinder(), pair4.getSecond()));
    }
    
    protected abstract Pair<String, Dynamic<?>> getNewNameAndTag(final String string, final Dynamic<?> dynamic);
}
