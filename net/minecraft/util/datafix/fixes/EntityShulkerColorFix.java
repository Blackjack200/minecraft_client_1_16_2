package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityShulkerColorFix extends NamedEntityFix {
    public EntityShulkerColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityShulkerColorFix", References.ENTITY, "minecraft:shulker");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        if (!dynamic.get("Color").map(Dynamic::asNumber).result().isPresent()) {
            return dynamic.set("Color", dynamic.createByte((byte)10));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
}
