package net.minecraft.util.datafix.schemas;

import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.schemas.Schema;

public class V100 extends Schema {
    public V100(final int integer, final Schema schema) {
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
        registerMob(schema, map3, "ArmorStand");
        registerMob(schema, map3, "Creeper");
        registerMob(schema, map3, "Skeleton");
        registerMob(schema, map3, "Spider");
        registerMob(schema, map3, "Giant");
        registerMob(schema, map3, "Zombie");
        registerMob(schema, map3, "Slime");
        registerMob(schema, map3, "Ghast");
        registerMob(schema, map3, "PigZombie");
        schema.register((Map)map3, "Enderman", string -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema), equipment(schema)));
        registerMob(schema, map3, "CaveSpider");
        registerMob(schema, map3, "Silverfish");
        registerMob(schema, map3, "Blaze");
        registerMob(schema, map3, "LavaSlime");
        registerMob(schema, map3, "EnderDragon");
        registerMob(schema, map3, "WitherBoss");
        registerMob(schema, map3, "Bat");
        registerMob(schema, map3, "Witch");
        registerMob(schema, map3, "Endermite");
        registerMob(schema, map3, "Guardian");
        registerMob(schema, map3, "Pig");
        registerMob(schema, map3, "Sheep");
        registerMob(schema, map3, "Cow");
        registerMob(schema, map3, "Chicken");
        registerMob(schema, map3, "Squid");
        registerMob(schema, map3, "Wolf");
        registerMob(schema, map3, "MushroomCow");
        registerMob(schema, map3, "SnowMan");
        registerMob(schema, map3, "Ozelot");
        registerMob(schema, map3, "VillagerGolem");
        schema.register((Map)map3, "EntityHorse", string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), equipment(schema)));
        registerMob(schema, map3, "Rabbit");
        schema.register((Map)map3, "Villager", string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), equipment(schema)));
        registerMob(schema, map3, "Shulker");
        schema.registerSimple((Map)map3, "AreaEffectCloud");
        schema.registerSimple((Map)map3, "ShulkerBullet");
        return map3;
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, (Map)map2, (Map)map3);
        schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
        schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
    }
}
