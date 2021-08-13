package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.List;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityShulkerRotationFix extends NamedEntityFix {
    public EntityShulkerRotationFix(final Schema schema) {
        super(schema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final List<Double> list3 = (List<Double>)dynamic.get("Rotation").asList(dynamic -> dynamic.asDouble(180.0));
        if (!list3.isEmpty()) {
            list3.set(0, ((double)list3.get(0) - 180.0));
            return dynamic.set("Rotation", dynamic.createList(list3.stream().map(dynamic::createDouble)));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
}
