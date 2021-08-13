package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class VillagerFollowRangeFix extends NamedEntityFix {
    public VillagerFollowRangeFix(final Schema schema) {
        super(schema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue);
    }
    
    private static Dynamic<?> fixValue(final Dynamic<?> dynamic) {
        return dynamic.update("Attributes", dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> {
            if (!dynamic.get("Name").asString("").equals("generic.follow_range") || dynamic.get("Base").asDouble(0.0) != 16.0) {
                return dynamic;
            }
            return dynamic.set("Base", dynamic.createDouble(48.0));
        })));
    }
}
