package net.minecraft.util.datafix.schemas;

import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.schemas.Schema;

public class V1928 extends NamespacedSchema {
    public V1928(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static TypeTemplate equipment(final Schema schema) {
        return DSL.optionalFields("ArmorItems", DSL.list(References.ITEM_STACK.in(schema)), "HandItems", DSL.list(References.ITEM_STACK.in(schema)));
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> equipment(schema));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerEntities(schema);
        map3.remove("minecraft:illager_beast");
        registerMob(schema, map3, "minecraft:ravager");
        return map3;
    }
}
