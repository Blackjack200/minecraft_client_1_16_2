package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityKeepPacked extends NamedEntityFix {
    public BlockEntityKeepPacked(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntityKeepPacked", References.BLOCK_ENTITY, "DUMMY");
    }
    
    private static Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        return dynamic.set("keepPacked", dynamic.createBoolean(true));
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), BlockEntityKeepPacked::fixTag);
    }
}
