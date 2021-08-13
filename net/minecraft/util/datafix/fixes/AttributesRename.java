package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.stream.Stream;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class AttributesRename extends DataFix {
    private static final Map<String, String> RENAMES;
    
    public AttributesRename(final Schema schema) {
        super(schema, false);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<?> opticFinder3 = type2.findField("tag");
        return TypeRewriteRule.seq(this.fixTypeEverywhereTyped("Rename ItemStack Attributes", (Type)type2, typed -> typed.updateTyped(opticFinder3, AttributesRename::fixItemStackTag)), new TypeRewriteRule[] { this.fixTypeEverywhereTyped("Rename Entity Attributes", this.getInputSchema().getType(References.ENTITY), AttributesRename::fixEntity), this.fixTypeEverywhereTyped("Rename Player Attributes", this.getInputSchema().getType(References.PLAYER), AttributesRename::fixEntity) });
    }
    
    private static Dynamic<?> fixName(final Dynamic<?> dynamic) {
        return DataFixUtils.orElse(dynamic.asString().result().map(string -> (String)AttributesRename.RENAMES.getOrDefault(string, string)).map(dynamic::createString), dynamic);
    }
    
    private static Typed<?> fixItemStackTag(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("AttributeModifiers", dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asStreamOpt().result().map(stream -> stream.map(dynamic -> dynamic.update("AttributeName", AttributesRename::fixName))).map(dynamic::createList), dynamic)));
    }
    
    private static Typed<?> fixEntity(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("Attributes", dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asStreamOpt().result().map(stream -> stream.map(dynamic -> dynamic.update("Name", AttributesRename::fixName))).map(dynamic::createList), dynamic)));
    }
    
    static {
        RENAMES = (Map)ImmutableMap.builder().put("generic.maxHealth", "generic.max_health").put("Max Health", "generic.max_health").put("zombie.spawnReinforcements", "zombie.spawn_reinforcements").put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements").put("horse.jumpStrength", "horse.jump_strength").put("Jump Strength", "horse.jump_strength").put("generic.followRange", "generic.follow_range").put("Follow Range", "generic.follow_range").put("generic.knockbackResistance", "generic.knockback_resistance").put("Knockback Resistance", "generic.knockback_resistance").put("generic.movementSpeed", "generic.movement_speed").put("Movement Speed", "generic.movement_speed").put("generic.flyingSpeed", "generic.flying_speed").put("Flying Speed", "generic.flying_speed").put("generic.attackDamage", "generic.attack_damage").put("generic.attackKnockback", "generic.attack_knockback").put("generic.attackSpeed", "generic.attack_speed").put("generic.armorToughness", "generic.armor_toughness").build();
    }
}
