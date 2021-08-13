package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.DataFixUtils;
import java.util.HashMap;
import com.google.common.collect.Maps;
import java.util.Objects;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import com.mojang.datafixers.types.templates.Hook;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V704 extends Schema {
    protected static final Map<String, String> ITEM_TO_BLOCKENTITY;
    protected static final Hook.HookFunction ADD_NAMES;
    
    public V704(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerInventory(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
    }
    
    public Type<?> getChoiceType(final DSL.TypeReference typeReference, final String string) {
        if (Objects.equals(typeReference.typeName(), References.BLOCK_ENTITY.typeName())) {
            return super.getChoiceType(typeReference, NamespacedSchema.ensureNamespaced(string));
        }
        return super.getChoiceType(typeReference, string);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)Maps.newHashMap();
        registerInventory(schema, map3, "minecraft:furnace");
        registerInventory(schema, map3, "minecraft:chest");
        schema.registerSimple((Map)map3, "minecraft:ender_chest");
        schema.register((Map)map3, "minecraft:jukebox", string -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
        registerInventory(schema, map3, "minecraft:dispenser");
        registerInventory(schema, map3, "minecraft:dropper");
        schema.registerSimple((Map)map3, "minecraft:sign");
        schema.register((Map)map3, "minecraft:mob_spawner", string -> References.UNTAGGED_SPAWNER.in(schema));
        schema.registerSimple((Map)map3, "minecraft:noteblock");
        schema.registerSimple((Map)map3, "minecraft:piston");
        registerInventory(schema, map3, "minecraft:brewing_stand");
        schema.registerSimple((Map)map3, "minecraft:enchanting_table");
        schema.registerSimple((Map)map3, "minecraft:end_portal");
        schema.registerSimple((Map)map3, "minecraft:beacon");
        schema.registerSimple((Map)map3, "minecraft:skull");
        schema.registerSimple((Map)map3, "minecraft:daylight_detector");
        registerInventory(schema, map3, "minecraft:hopper");
        schema.registerSimple((Map)map3, "minecraft:comparator");
        schema.register((Map)map3, "minecraft:flower_pot", string -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema))));
        schema.registerSimple((Map)map3, "minecraft:banner");
        schema.registerSimple((Map)map3, "minecraft:structure_block");
        schema.registerSimple((Map)map3, "minecraft:end_gateway");
        schema.registerSimple((Map)map3, "minecraft:command_block");
        return map3;
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, (Map)map2, (Map)map3);
        schema.registerType(false, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", (Type)NamespacedSchema.namespacedString(), map3));
        schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V704.ADD_NAMES, Hook.HookFunction.IDENTITY));
    }
    
    static {
        ITEM_TO_BLOCKENTITY = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            hashMap.put("minecraft:furnace", "minecraft:furnace");
            hashMap.put("minecraft:lit_furnace", "minecraft:furnace");
            hashMap.put("minecraft:chest", "minecraft:chest");
            hashMap.put("minecraft:trapped_chest", "minecraft:chest");
            hashMap.put("minecraft:ender_chest", "minecraft:ender_chest");
            hashMap.put("minecraft:jukebox", "minecraft:jukebox");
            hashMap.put("minecraft:dispenser", "minecraft:dispenser");
            hashMap.put("minecraft:dropper", "minecraft:dropper");
            hashMap.put("minecraft:sign", "minecraft:sign");
            hashMap.put("minecraft:mob_spawner", "minecraft:mob_spawner");
            hashMap.put("minecraft:noteblock", "minecraft:noteblock");
            hashMap.put("minecraft:brewing_stand", "minecraft:brewing_stand");
            hashMap.put("minecraft:enhanting_table", "minecraft:enchanting_table");
            hashMap.put("minecraft:command_block", "minecraft:command_block");
            hashMap.put("minecraft:beacon", "minecraft:beacon");
            hashMap.put("minecraft:skull", "minecraft:skull");
            hashMap.put("minecraft:daylight_detector", "minecraft:daylight_detector");
            hashMap.put("minecraft:hopper", "minecraft:hopper");
            hashMap.put("minecraft:banner", "minecraft:banner");
            hashMap.put("minecraft:flower_pot", "minecraft:flower_pot");
            hashMap.put("minecraft:repeating_command_block", "minecraft:command_block");
            hashMap.put("minecraft:chain_command_block", "minecraft:command_block");
            hashMap.put("minecraft:shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:white_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:orange_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:magenta_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:light_blue_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:yellow_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:lime_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:pink_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:gray_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:silver_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:cyan_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:purple_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:blue_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:brown_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:green_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:red_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:black_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:bed", "minecraft:bed");
            hashMap.put("minecraft:light_gray_shulker_box", "minecraft:shulker_box");
            hashMap.put("minecraft:banner", "minecraft:banner");
            hashMap.put("minecraft:white_banner", "minecraft:banner");
            hashMap.put("minecraft:orange_banner", "minecraft:banner");
            hashMap.put("minecraft:magenta_banner", "minecraft:banner");
            hashMap.put("minecraft:light_blue_banner", "minecraft:banner");
            hashMap.put("minecraft:yellow_banner", "minecraft:banner");
            hashMap.put("minecraft:lime_banner", "minecraft:banner");
            hashMap.put("minecraft:pink_banner", "minecraft:banner");
            hashMap.put("minecraft:gray_banner", "minecraft:banner");
            hashMap.put("minecraft:silver_banner", "minecraft:banner");
            hashMap.put("minecraft:cyan_banner", "minecraft:banner");
            hashMap.put("minecraft:purple_banner", "minecraft:banner");
            hashMap.put("minecraft:blue_banner", "minecraft:banner");
            hashMap.put("minecraft:brown_banner", "minecraft:banner");
            hashMap.put("minecraft:green_banner", "minecraft:banner");
            hashMap.put("minecraft:red_banner", "minecraft:banner");
            hashMap.put("minecraft:black_banner", "minecraft:banner");
            hashMap.put("minecraft:standing_sign", "minecraft:sign");
            hashMap.put("minecraft:wall_sign", "minecraft:sign");
            hashMap.put("minecraft:piston_head", "minecraft:piston");
            hashMap.put("minecraft:daylight_detector_inverted", "minecraft:daylight_detector");
            hashMap.put("minecraft:unpowered_comparator", "minecraft:comparator");
            hashMap.put("minecraft:powered_comparator", "minecraft:comparator");
            hashMap.put("minecraft:wall_banner", "minecraft:banner");
            hashMap.put("minecraft:standing_banner", "minecraft:banner");
            hashMap.put("minecraft:structure_block", "minecraft:structure_block");
            hashMap.put("minecraft:end_portal", "minecraft:end_portal");
            hashMap.put("minecraft:end_gateway", "minecraft:end_gateway");
            hashMap.put("minecraft:sign", "minecraft:sign");
            hashMap.put("minecraft:shield", "minecraft:banner");
        });
        ADD_NAMES = (Hook.HookFunction)new Hook.HookFunction() {
            public <T> T apply(final DynamicOps<T> dynamicOps, final T object) {
                return V99.<T>addNames((com.mojang.serialization.Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, object), V704.ITEM_TO_BLOCKENTITY, "ArmorStand");
            }
        };
    }
}
