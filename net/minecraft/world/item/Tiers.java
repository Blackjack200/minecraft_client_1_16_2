package net.minecraft.world.item;

import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import java.util.function.Supplier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;

public enum Tiers implements Tier {
    WOOD(0, 59, 2.0f, 0.0f, 15, (Supplier<Ingredient>)(() -> Ingredient.of(ItemTags.PLANKS))), 
    STONE(1, 131, 4.0f, 1.0f, 5, (Supplier<Ingredient>)(() -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))), 
    IRON(2, 250, 6.0f, 2.0f, 14, (Supplier<Ingredient>)(() -> Ingredient.of(Items.IRON_INGOT))), 
    DIAMOND(3, 1561, 8.0f, 3.0f, 10, (Supplier<Ingredient>)(() -> Ingredient.of(Items.DIAMOND))), 
    GOLD(0, 32, 12.0f, 0.0f, 22, (Supplier<Ingredient>)(() -> Ingredient.of(Items.GOLD_INGOT))), 
    NETHERITE(4, 2031, 9.0f, 4.0f, 15, (Supplier<Ingredient>)(() -> Ingredient.of(Items.NETHERITE_INGOT)));
    
    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    
    private Tiers(final int integer3, final int integer4, final float float5, final float float6, final int integer7, final Supplier<Ingredient> supplier) {
        this.level = integer3;
        this.uses = integer4;
        this.speed = float5;
        this.damage = float6;
        this.enchantmentValue = integer7;
        this.repairIngredient = new LazyLoadedValue<Ingredient>(supplier);
    }
    
    public int getUses() {
        return this.uses;
    }
    
    public float getSpeed() {
        return this.speed;
    }
    
    public float getAttackDamageBonus() {
        return this.damage;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }
    
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
