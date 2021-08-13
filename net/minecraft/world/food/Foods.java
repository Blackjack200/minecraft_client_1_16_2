package net.minecraft.world.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Foods {
    public static final FoodProperties APPLE;
    public static final FoodProperties BAKED_POTATO;
    public static final FoodProperties BEEF;
    public static final FoodProperties BEETROOT;
    public static final FoodProperties BEETROOT_SOUP;
    public static final FoodProperties BREAD;
    public static final FoodProperties CARROT;
    public static final FoodProperties CHICKEN;
    public static final FoodProperties CHORUS_FRUIT;
    public static final FoodProperties COD;
    public static final FoodProperties COOKED_BEEF;
    public static final FoodProperties COOKED_CHICKEN;
    public static final FoodProperties COOKED_COD;
    public static final FoodProperties COOKED_MUTTON;
    public static final FoodProperties COOKED_PORKCHOP;
    public static final FoodProperties COOKED_RABBIT;
    public static final FoodProperties COOKED_SALMON;
    public static final FoodProperties COOKIE;
    public static final FoodProperties DRIED_KELP;
    public static final FoodProperties ENCHANTED_GOLDEN_APPLE;
    public static final FoodProperties GOLDEN_APPLE;
    public static final FoodProperties GOLDEN_CARROT;
    public static final FoodProperties HONEY_BOTTLE;
    public static final FoodProperties MELON_SLICE;
    public static final FoodProperties MUSHROOM_STEW;
    public static final FoodProperties MUTTON;
    public static final FoodProperties POISONOUS_POTATO;
    public static final FoodProperties PORKCHOP;
    public static final FoodProperties POTATO;
    public static final FoodProperties PUFFERFISH;
    public static final FoodProperties PUMPKIN_PIE;
    public static final FoodProperties RABBIT;
    public static final FoodProperties RABBIT_STEW;
    public static final FoodProperties ROTTEN_FLESH;
    public static final FoodProperties SALMON;
    public static final FoodProperties SPIDER_EYE;
    public static final FoodProperties SUSPICIOUS_STEW;
    public static final FoodProperties SWEET_BERRIES;
    public static final FoodProperties TROPICAL_FISH;
    
    private static FoodProperties stew(final int integer) {
        return new FoodProperties.Builder().nutrition(integer).saturationMod(0.6f).build();
    }
    
    static {
        APPLE = new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).build();
        BAKED_POTATO = new FoodProperties.Builder().nutrition(5).saturationMod(0.6f).build();
        BEEF = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).meat().build();
        BEETROOT = new FoodProperties.Builder().nutrition(1).saturationMod(0.6f).build();
        BEETROOT_SOUP = stew(6);
        BREAD = new FoodProperties.Builder().nutrition(5).saturationMod(0.6f).build();
        CARROT = new FoodProperties.Builder().nutrition(3).saturationMod(0.6f).build();
        CHICKEN = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3f).meat().build();
        CHORUS_FRUIT = new FoodProperties.Builder().nutrition(4).saturationMod(0.3f).alwaysEat().build();
        COD = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
        COOKED_BEEF = new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).meat().build();
        COOKED_CHICKEN = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).meat().build();
        COOKED_COD = new FoodProperties.Builder().nutrition(5).saturationMod(0.6f).build();
        COOKED_MUTTON = new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).meat().build();
        COOKED_PORKCHOP = new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).meat().build();
        COOKED_RABBIT = new FoodProperties.Builder().nutrition(5).saturationMod(0.6f).meat().build();
        COOKED_SALMON = new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build();
        COOKIE = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
        DRIED_KELP = new FoodProperties.Builder().nutrition(1).saturationMod(0.3f).fast().build();
        ENCHANTED_GOLDEN_APPLE = new FoodProperties.Builder().nutrition(4).saturationMod(1.2f).effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0f).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0f).effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0f).effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0f).alwaysEat().build();
        GOLDEN_APPLE = new FoodProperties.Builder().nutrition(4).saturationMod(1.2f).effect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0f).effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0f).alwaysEat().build();
        GOLDEN_CARROT = new FoodProperties.Builder().nutrition(6).saturationMod(1.2f).build();
        HONEY_BOTTLE = new FoodProperties.Builder().nutrition(6).saturationMod(0.1f).build();
        MELON_SLICE = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build();
        MUSHROOM_STEW = stew(6);
        MUTTON = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).meat().build();
        POISONOUS_POTATO = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.6f).build();
        PORKCHOP = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).meat().build();
        POTATO = new FoodProperties.Builder().nutrition(1).saturationMod(0.3f).build();
        PUFFERFISH = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.POISON, 1200, 3), 1.0f).effect(new MobEffectInstance(MobEffects.HUNGER, 300, 2), 1.0f).effect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0), 1.0f).build();
        PUMPKIN_PIE = new FoodProperties.Builder().nutrition(8).saturationMod(0.3f).build();
        RABBIT = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).meat().build();
        RABBIT_STEW = stew(10);
        ROTTEN_FLESH = new FoodProperties.Builder().nutrition(4).saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.8f).meat().build();
        SALMON = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
        SPIDER_EYE = new FoodProperties.Builder().nutrition(2).saturationMod(0.8f).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0f).build();
        SUSPICIOUS_STEW = stew(6);
        SWEET_BERRIES = new FoodProperties.Builder().nutrition(2).saturationMod(0.1f).build();
        TROPICAL_FISH = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build();
    }
}
