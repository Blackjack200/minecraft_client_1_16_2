package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1451_3 extends NamespacedSchema {
    public V1451_3(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerEntities(schema);
        schema.registerSimple((Map)map3, "minecraft:egg");
        schema.registerSimple((Map)map3, "minecraft:ender_pearl");
        schema.registerSimple((Map)map3, "minecraft:fireball");
        schema.register((Map)map3, "minecraft:potion", string -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema)));
        schema.registerSimple((Map)map3, "minecraft:small_fireball");
        schema.registerSimple((Map)map3, "minecraft:snowball");
        schema.registerSimple((Map)map3, "minecraft:wither_skull");
        schema.registerSimple((Map)map3, "minecraft:xp_bottle");
        schema.register((Map)map3, "minecraft:arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
        schema.register((Map)map3, "minecraft:enderman", () -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema), V100.equipment(schema)));
        schema.register((Map)map3, "minecraft:falling_block", () -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
        schema.register((Map)map3, "minecraft:spectral_arrow", () -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
        schema.register((Map)map3, "minecraft:chest_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        schema.register((Map)map3, "minecraft:commandblock_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        schema.register((Map)map3, "minecraft:furnace_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        schema.register((Map)map3, "minecraft:hopper_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        schema.register((Map)map3, "minecraft:minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        schema.register((Map)map3, "minecraft:spawner_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
        schema.register((Map)map3, "minecraft:tnt_minecart", () -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        return map3;
    }
}
