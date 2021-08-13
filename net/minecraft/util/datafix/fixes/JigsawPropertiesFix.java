package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class JigsawPropertiesFix extends NamedEntityFix {
    public JigsawPropertiesFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "JigsawPropertiesFix", References.BLOCK_ENTITY, "minecraft:jigsaw");
    }
    
    private static Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final String string2 = dynamic.get("attachement_type").asString("minecraft:empty");
        final String string3 = dynamic.get("target_pool").asString("minecraft:empty");
        return dynamic.set("name", dynamic.createString(string2)).set("target", dynamic.createString(string2)).remove("attachement_type").set("pool", dynamic.createString(string3)).remove("target_pool");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), JigsawPropertiesFix::fixTag);
    }
}
