package net.minecraft.world.entity.ai.attributes;

import net.minecraft.core.Registry;

public class Attributes {
    public static final Attribute MAX_HEALTH;
    public static final Attribute FOLLOW_RANGE;
    public static final Attribute KNOCKBACK_RESISTANCE;
    public static final Attribute MOVEMENT_SPEED;
    public static final Attribute FLYING_SPEED;
    public static final Attribute ATTACK_DAMAGE;
    public static final Attribute ATTACK_KNOCKBACK;
    public static final Attribute ATTACK_SPEED;
    public static final Attribute ARMOR;
    public static final Attribute ARMOR_TOUGHNESS;
    public static final Attribute LUCK;
    public static final Attribute SPAWN_REINFORCEMENTS_CHANCE;
    public static final Attribute JUMP_STRENGTH;
    
    private static Attribute register(final String string, final Attribute ard) {
        return Registry.<Attribute>register(Registry.ATTRIBUTE, string, ard);
    }
    
    static {
        MAX_HEALTH = register("generic.max_health", new RangedAttribute("attribute.name.generic.max_health", 20.0, 1.0, 1024.0).setSyncable(true));
        FOLLOW_RANGE = register("generic.follow_range", (Attribute)new RangedAttribute("attribute.name.generic.follow_range", 32.0, 0.0, 2048.0));
        KNOCKBACK_RESISTANCE = register("generic.knockback_resistance", (Attribute)new RangedAttribute("attribute.name.generic.knockback_resistance", 0.0, 0.0, 1.0));
        MOVEMENT_SPEED = register("generic.movement_speed", new RangedAttribute("attribute.name.generic.movement_speed", 0.699999988079071, 0.0, 1024.0).setSyncable(true));
        FLYING_SPEED = register("generic.flying_speed", new RangedAttribute("attribute.name.generic.flying_speed", 0.4000000059604645, 0.0, 1024.0).setSyncable(true));
        ATTACK_DAMAGE = register("generic.attack_damage", (Attribute)new RangedAttribute("attribute.name.generic.attack_damage", 2.0, 0.0, 2048.0));
        ATTACK_KNOCKBACK = register("generic.attack_knockback", (Attribute)new RangedAttribute("attribute.name.generic.attack_knockback", 0.0, 0.0, 5.0));
        ATTACK_SPEED = register("generic.attack_speed", new RangedAttribute("attribute.name.generic.attack_speed", 4.0, 0.0, 1024.0).setSyncable(true));
        ARMOR = register("generic.armor", new RangedAttribute("attribute.name.generic.armor", 0.0, 0.0, 30.0).setSyncable(true));
        ARMOR_TOUGHNESS = register("generic.armor_toughness", new RangedAttribute("attribute.name.generic.armor_toughness", 0.0, 0.0, 20.0).setSyncable(true));
        LUCK = register("generic.luck", new RangedAttribute("attribute.name.generic.luck", 0.0, -1024.0, 1024.0).setSyncable(true));
        SPAWN_REINFORCEMENTS_CHANCE = register("zombie.spawn_reinforcements", (Attribute)new RangedAttribute("attribute.name.zombie.spawn_reinforcements", 0.0, 0.0, 1.0));
        JUMP_STRENGTH = register("horse.jump_strength", new RangedAttribute("attribute.name.horse.jump_strength", 0.7, 0.0, 2.0).setSyncable(true));
    }
}
