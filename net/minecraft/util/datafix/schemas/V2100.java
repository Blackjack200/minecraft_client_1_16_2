package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V2100 extends NamespacedSchema {
    public V2100(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> V100.equipment(schema));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerEntities(schema);
        registerMob(schema, map3, "minecraft:bee");
        registerMob(schema, map3, "minecraft:bee_stinger");
        return map3;
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerBlockEntities(schema);
        schema.register((Map)map3, "minecraft:beehive", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "Bees", DSL.list(DSL.optionalFields("EntityData", References.ENTITY_TREE.in(schema)))));
        return map3;
    }
}
