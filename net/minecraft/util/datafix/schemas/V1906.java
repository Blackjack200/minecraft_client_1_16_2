package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1906 extends NamespacedSchema {
    public V1906(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerBlockEntities(schema);
        registerInventory(schema, map3, "minecraft:barrel");
        registerInventory(schema, map3, "minecraft:smoker");
        registerInventory(schema, map3, "minecraft:blast_furnace");
        schema.register((Map)map3, "minecraft:lectern", string -> DSL.optionalFields("Book", References.ITEM_STACK.in(schema)));
        schema.registerSimple((Map)map3, "minecraft:bell");
        return map3;
    }
    
    protected static void registerInventory(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
    }
}
