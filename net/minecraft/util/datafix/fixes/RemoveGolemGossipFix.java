package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class RemoveGolemGossipFix extends NamedEntityFix {
    public RemoveGolemGossipFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "Remove Golem Gossip Fix", References.ENTITY, "minecraft:villager");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), RemoveGolemGossipFix::fixValue);
    }
    
    private static Dynamic<?> fixValue(final Dynamic<?> dynamic) {
        return dynamic.update("Gossips", dynamic2 -> dynamic.createList(dynamic2.asStream().filter(dynamic -> !dynamic.get("Type").asString("").equals("golem"))));
    }
}
