package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.EquipmentSlot;
import java.util.function.Supplier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.sounds.SoundEvent;

public enum ArmorMaterials implements ArmorMaterial {
    LEATHER("leather", 5, new int[] { 1, 2, 3, 1 }, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.LEATHER))), 
    CHAIN("chainmail", 15, new int[] { 1, 4, 5, 2 }, 12, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.IRON_INGOT))), 
    IRON("iron", 15, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.IRON_INGOT))), 
    GOLD("gold", 7, new int[] { 1, 3, 5, 2 }, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.GOLD_INGOT))), 
    DIAMOND("diamond", 33, new int[] { 3, 6, 8, 3 }, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.DIAMOND))), 
    TURTLE("turtle", 25, new int[] { 2, 5, 6, 2 }, 9, SoundEvents.ARMOR_EQUIP_TURTLE, 0.0f, 0.0f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.SCUTE))), 
    NETHERITE("netherite", 37, new int[] { 3, 6, 8, 3 }, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f, (Supplier<Ingredient>)(() -> Ingredient.of(Items.NETHERITE_INGOT)));
    
    private static final int[] HEALTH_PER_SLOT;
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    
    private ArmorMaterials(final String string3, final int integer4, final int[] arr, final int integer6, final SoundEvent adn, final float float8, final float float9, final Supplier<Ingredient> supplier) {
        this.name = string3;
        this.durabilityMultiplier = integer4;
        this.slotProtections = arr;
        this.enchantmentValue = integer6;
        this.sound = adn;
        this.toughness = float8;
        this.knockbackResistance = float9;
        this.repairIngredient = new LazyLoadedValue<Ingredient>(supplier);
    }
    
    public int getDurabilityForSlot(final EquipmentSlot aqc) {
        return ArmorMaterials.HEALTH_PER_SLOT[aqc.getIndex()] * this.durabilityMultiplier;
    }
    
    public int getDefenseForSlot(final EquipmentSlot aqc) {
        return this.slotProtections[aqc.getIndex()];
    }
    
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }
    
    public SoundEvent getEquipSound() {
        return this.sound;
    }
    
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
    
    public String getName() {
        return this.name;
    }
    
    public float getToughness() {
        return this.toughness;
    }
    
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
    
    static {
        HEALTH_PER_SLOT = new int[] { 13, 15, 16, 11 };
    }
}
