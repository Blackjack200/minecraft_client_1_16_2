package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1470 extends NamespacedSchema {
    public V1470(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> V100.equipment(schema));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerEntities(schema);
        registerMob(schema, map3, "minecraft:turtle");
        registerMob(schema, map3, "minecraft:cod_mob");
        registerMob(schema, map3, "minecraft:tropical_fish");
        registerMob(schema, map3, "minecraft:salmon_mob");
        registerMob(schema, map3, "minecraft:puffer_fish");
        registerMob(schema, map3, "minecraft:phantom");
        registerMob(schema, map3, "minecraft:dolphin");
        registerMob(schema, map3, "minecraft:drowned");
        schema.register((Map)map3, "minecraft:trident", string -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
        return map3;
    }
}
