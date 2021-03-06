package net.minecraft.world.level.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;

public class SoundType {
    public static final SoundType WOOD;
    public static final SoundType GRAVEL;
    public static final SoundType GRASS;
    public static final SoundType LILY_PAD;
    public static final SoundType STONE;
    public static final SoundType METAL;
    public static final SoundType GLASS;
    public static final SoundType WOOL;
    public static final SoundType SAND;
    public static final SoundType SNOW;
    public static final SoundType LADDER;
    public static final SoundType ANVIL;
    public static final SoundType SLIME_BLOCK;
    public static final SoundType HONEY_BLOCK;
    public static final SoundType WET_GRASS;
    public static final SoundType CORAL_BLOCK;
    public static final SoundType BAMBOO;
    public static final SoundType BAMBOO_SAPLING;
    public static final SoundType SCAFFOLDING;
    public static final SoundType SWEET_BERRY_BUSH;
    public static final SoundType CROP;
    public static final SoundType HARD_CROP;
    public static final SoundType VINE;
    public static final SoundType NETHER_WART;
    public static final SoundType LANTERN;
    public static final SoundType STEM;
    public static final SoundType NYLIUM;
    public static final SoundType FUNGUS;
    public static final SoundType ROOTS;
    public static final SoundType SHROOMLIGHT;
    public static final SoundType WEEPING_VINES;
    public static final SoundType TWISTING_VINES;
    public static final SoundType SOUL_SAND;
    public static final SoundType SOUL_SOIL;
    public static final SoundType BASALT;
    public static final SoundType WART_BLOCK;
    public static final SoundType NETHERRACK;
    public static final SoundType NETHER_BRICKS;
    public static final SoundType NETHER_SPROUTS;
    public static final SoundType NETHER_ORE;
    public static final SoundType BONE_BLOCK;
    public static final SoundType NETHERITE_BLOCK;
    public static final SoundType ANCIENT_DEBRIS;
    public static final SoundType LODESTONE;
    public static final SoundType CHAIN;
    public static final SoundType NETHER_GOLD_ORE;
    public static final SoundType GILDED_BLACKSTONE;
    public final float volume;
    public final float pitch;
    private final SoundEvent breakSound;
    private final SoundEvent stepSound;
    private final SoundEvent placeSound;
    private final SoundEvent hitSound;
    private final SoundEvent fallSound;
    
    public SoundType(final float float1, final float float2, final SoundEvent adn3, final SoundEvent adn4, final SoundEvent adn5, final SoundEvent adn6, final SoundEvent adn7) {
        this.volume = float1;
        this.pitch = float2;
        this.breakSound = adn3;
        this.stepSound = adn4;
        this.placeSound = adn5;
        this.hitSound = adn6;
        this.fallSound = adn7;
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public SoundEvent getBreakSound() {
        return this.breakSound;
    }
    
    public SoundEvent getStepSound() {
        return this.stepSound;
    }
    
    public SoundEvent getPlaceSound() {
        return this.placeSound;
    }
    
    public SoundEvent getHitSound() {
        return this.hitSound;
    }
    
    public SoundEvent getFallSound() {
        return this.fallSound;
    }
    
    static {
        WOOD = new SoundType(1.0f, 1.0f, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.WOOD_PLACE, SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL);
        GRAVEL = new SoundType(1.0f, 1.0f, SoundEvents.GRAVEL_BREAK, SoundEvents.GRAVEL_STEP, SoundEvents.GRAVEL_PLACE, SoundEvents.GRAVEL_HIT, SoundEvents.GRAVEL_FALL);
        GRASS = new SoundType(1.0f, 1.0f, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.GRASS_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
        LILY_PAD = new SoundType(1.0f, 1.0f, SoundEvents.GRASS_BREAK, SoundEvents.GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
        STONE = new SoundType(1.0f, 1.0f, SoundEvents.STONE_BREAK, SoundEvents.STONE_STEP, SoundEvents.STONE_PLACE, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
        METAL = new SoundType(1.0f, 1.5f, SoundEvents.METAL_BREAK, SoundEvents.METAL_STEP, SoundEvents.METAL_PLACE, SoundEvents.METAL_HIT, SoundEvents.METAL_FALL);
        GLASS = new SoundType(1.0f, 1.0f, SoundEvents.GLASS_BREAK, SoundEvents.GLASS_STEP, SoundEvents.GLASS_PLACE, SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL);
        WOOL = new SoundType(1.0f, 1.0f, SoundEvents.WOOL_BREAK, SoundEvents.WOOL_STEP, SoundEvents.WOOL_PLACE, SoundEvents.WOOL_HIT, SoundEvents.WOOL_FALL);
        SAND = new SoundType(1.0f, 1.0f, SoundEvents.SAND_BREAK, SoundEvents.SAND_STEP, SoundEvents.SAND_PLACE, SoundEvents.SAND_HIT, SoundEvents.SAND_FALL);
        SNOW = new SoundType(1.0f, 1.0f, SoundEvents.SNOW_BREAK, SoundEvents.SNOW_STEP, SoundEvents.SNOW_PLACE, SoundEvents.SNOW_HIT, SoundEvents.SNOW_FALL);
        LADDER = new SoundType(1.0f, 1.0f, SoundEvents.LADDER_BREAK, SoundEvents.LADDER_STEP, SoundEvents.LADDER_PLACE, SoundEvents.LADDER_HIT, SoundEvents.LADDER_FALL);
        ANVIL = new SoundType(0.3f, 1.0f, SoundEvents.ANVIL_BREAK, SoundEvents.ANVIL_STEP, SoundEvents.ANVIL_PLACE, SoundEvents.ANVIL_HIT, SoundEvents.ANVIL_FALL);
        SLIME_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.SLIME_BLOCK_BREAK, SoundEvents.SLIME_BLOCK_STEP, SoundEvents.SLIME_BLOCK_PLACE, SoundEvents.SLIME_BLOCK_HIT, SoundEvents.SLIME_BLOCK_FALL);
        HONEY_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.HONEY_BLOCK_BREAK, SoundEvents.HONEY_BLOCK_STEP, SoundEvents.HONEY_BLOCK_PLACE, SoundEvents.HONEY_BLOCK_HIT, SoundEvents.HONEY_BLOCK_FALL);
        WET_GRASS = new SoundType(1.0f, 1.0f, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.WET_GRASS_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
        CORAL_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.CORAL_BLOCK_BREAK, SoundEvents.CORAL_BLOCK_STEP, SoundEvents.CORAL_BLOCK_PLACE, SoundEvents.CORAL_BLOCK_HIT, SoundEvents.CORAL_BLOCK_FALL);
        BAMBOO = new SoundType(1.0f, 1.0f, SoundEvents.BAMBOO_BREAK, SoundEvents.BAMBOO_STEP, SoundEvents.BAMBOO_PLACE, SoundEvents.BAMBOO_HIT, SoundEvents.BAMBOO_FALL);
        BAMBOO_SAPLING = new SoundType(1.0f, 1.0f, SoundEvents.BAMBOO_SAPLING_BREAK, SoundEvents.BAMBOO_STEP, SoundEvents.BAMBOO_SAPLING_PLACE, SoundEvents.BAMBOO_SAPLING_HIT, SoundEvents.BAMBOO_FALL);
        SCAFFOLDING = new SoundType(1.0f, 1.0f, SoundEvents.SCAFFOLDING_BREAK, SoundEvents.SCAFFOLDING_STEP, SoundEvents.SCAFFOLDING_PLACE, SoundEvents.SCAFFOLDING_HIT, SoundEvents.SCAFFOLDING_FALL);
        SWEET_BERRY_BUSH = new SoundType(1.0f, 1.0f, SoundEvents.SWEET_BERRY_BUSH_BREAK, SoundEvents.GRASS_STEP, SoundEvents.SWEET_BERRY_BUSH_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
        CROP = new SoundType(1.0f, 1.0f, SoundEvents.CROP_BREAK, SoundEvents.GRASS_STEP, SoundEvents.CROP_PLANTED, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
        HARD_CROP = new SoundType(1.0f, 1.0f, SoundEvents.WOOD_BREAK, SoundEvents.WOOD_STEP, SoundEvents.CROP_PLANTED, SoundEvents.WOOD_HIT, SoundEvents.WOOD_FALL);
        VINE = new SoundType(1.0f, 1.0f, SoundEvents.GRASS_BREAK, SoundEvents.VINE_STEP, SoundEvents.GRASS_PLACE, SoundEvents.GRASS_HIT, SoundEvents.GRASS_FALL);
        NETHER_WART = new SoundType(1.0f, 1.0f, SoundEvents.NETHER_WART_BREAK, SoundEvents.STONE_STEP, SoundEvents.NETHER_WART_PLANTED, SoundEvents.STONE_HIT, SoundEvents.STONE_FALL);
        LANTERN = new SoundType(1.0f, 1.0f, SoundEvents.LANTERN_BREAK, SoundEvents.LANTERN_STEP, SoundEvents.LANTERN_PLACE, SoundEvents.LANTERN_HIT, SoundEvents.LANTERN_FALL);
        STEM = new SoundType(1.0f, 1.0f, SoundEvents.STEM_BREAK, SoundEvents.STEM_STEP, SoundEvents.STEM_PLACE, SoundEvents.STEM_HIT, SoundEvents.STEM_FALL);
        NYLIUM = new SoundType(1.0f, 1.0f, SoundEvents.NYLIUM_BREAK, SoundEvents.NYLIUM_STEP, SoundEvents.NYLIUM_PLACE, SoundEvents.NYLIUM_HIT, SoundEvents.NYLIUM_FALL);
        FUNGUS = new SoundType(1.0f, 1.0f, SoundEvents.FUNGUS_BREAK, SoundEvents.FUNGUS_STEP, SoundEvents.FUNGUS_PLACE, SoundEvents.FUNGUS_HIT, SoundEvents.FUNGUS_FALL);
        ROOTS = new SoundType(1.0f, 1.0f, SoundEvents.ROOTS_BREAK, SoundEvents.ROOTS_STEP, SoundEvents.ROOTS_PLACE, SoundEvents.ROOTS_HIT, SoundEvents.ROOTS_FALL);
        SHROOMLIGHT = new SoundType(1.0f, 1.0f, SoundEvents.SHROOMLIGHT_BREAK, SoundEvents.SHROOMLIGHT_STEP, SoundEvents.SHROOMLIGHT_PLACE, SoundEvents.SHROOMLIGHT_HIT, SoundEvents.SHROOMLIGHT_FALL);
        WEEPING_VINES = new SoundType(1.0f, 1.0f, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP, SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL);
        TWISTING_VINES = new SoundType(1.0f, 0.5f, SoundEvents.WEEPING_VINES_BREAK, SoundEvents.WEEPING_VINES_STEP, SoundEvents.WEEPING_VINES_PLACE, SoundEvents.WEEPING_VINES_HIT, SoundEvents.WEEPING_VINES_FALL);
        SOUL_SAND = new SoundType(1.0f, 1.0f, SoundEvents.SOUL_SAND_BREAK, SoundEvents.SOUL_SAND_STEP, SoundEvents.SOUL_SAND_PLACE, SoundEvents.SOUL_SAND_HIT, SoundEvents.SOUL_SAND_FALL);
        SOUL_SOIL = new SoundType(1.0f, 1.0f, SoundEvents.SOUL_SOIL_BREAK, SoundEvents.SOUL_SOIL_STEP, SoundEvents.SOUL_SOIL_PLACE, SoundEvents.SOUL_SOIL_HIT, SoundEvents.SOUL_SOIL_FALL);
        BASALT = new SoundType(1.0f, 1.0f, SoundEvents.BASALT_BREAK, SoundEvents.BASALT_STEP, SoundEvents.BASALT_PLACE, SoundEvents.BASALT_HIT, SoundEvents.BASALT_FALL);
        WART_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.WART_BLOCK_BREAK, SoundEvents.WART_BLOCK_STEP, SoundEvents.WART_BLOCK_PLACE, SoundEvents.WART_BLOCK_HIT, SoundEvents.WART_BLOCK_FALL);
        NETHERRACK = new SoundType(1.0f, 1.0f, SoundEvents.NETHERRACK_BREAK, SoundEvents.NETHERRACK_STEP, SoundEvents.NETHERRACK_PLACE, SoundEvents.NETHERRACK_HIT, SoundEvents.NETHERRACK_FALL);
        NETHER_BRICKS = new SoundType(1.0f, 1.0f, SoundEvents.NETHER_BRICKS_BREAK, SoundEvents.NETHER_BRICKS_STEP, SoundEvents.NETHER_BRICKS_PLACE, SoundEvents.NETHER_BRICKS_HIT, SoundEvents.NETHER_BRICKS_FALL);
        NETHER_SPROUTS = new SoundType(1.0f, 1.0f, SoundEvents.NETHER_SPROUTS_BREAK, SoundEvents.NETHER_SPROUTS_STEP, SoundEvents.NETHER_SPROUTS_PLACE, SoundEvents.NETHER_SPROUTS_HIT, SoundEvents.NETHER_SPROUTS_FALL);
        NETHER_ORE = new SoundType(1.0f, 1.0f, SoundEvents.NETHER_ORE_BREAK, SoundEvents.NETHER_ORE_STEP, SoundEvents.NETHER_ORE_PLACE, SoundEvents.NETHER_ORE_HIT, SoundEvents.NETHER_ORE_FALL);
        BONE_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.BONE_BLOCK_BREAK, SoundEvents.BONE_BLOCK_STEP, SoundEvents.BONE_BLOCK_PLACE, SoundEvents.BONE_BLOCK_HIT, SoundEvents.BONE_BLOCK_FALL);
        NETHERITE_BLOCK = new SoundType(1.0f, 1.0f, SoundEvents.NETHERITE_BLOCK_BREAK, SoundEvents.NETHERITE_BLOCK_STEP, SoundEvents.NETHERITE_BLOCK_PLACE, SoundEvents.NETHERITE_BLOCK_HIT, SoundEvents.NETHERITE_BLOCK_FALL);
        ANCIENT_DEBRIS = new SoundType(1.0f, 1.0f, SoundEvents.ANCIENT_DEBRIS_BREAK, SoundEvents.ANCIENT_DEBRIS_STEP, SoundEvents.ANCIENT_DEBRIS_PLACE, SoundEvents.ANCIENT_DEBRIS_HIT, SoundEvents.ANCIENT_DEBRIS_FALL);
        LODESTONE = new SoundType(1.0f, 1.0f, SoundEvents.LODESTONE_BREAK, SoundEvents.LODESTONE_STEP, SoundEvents.LODESTONE_PLACE, SoundEvents.LODESTONE_HIT, SoundEvents.LODESTONE_FALL);
        CHAIN = new SoundType(1.0f, 1.0f, SoundEvents.CHAIN_BREAK, SoundEvents.CHAIN_STEP, SoundEvents.CHAIN_PLACE, SoundEvents.CHAIN_HIT, SoundEvents.CHAIN_FALL);
        NETHER_GOLD_ORE = new SoundType(1.0f, 1.0f, SoundEvents.NETHER_GOLD_ORE_BREAK, SoundEvents.NETHER_GOLD_ORE_STEP, SoundEvents.NETHER_GOLD_ORE_PLACE, SoundEvents.NETHER_GOLD_ORE_HIT, SoundEvents.NETHER_GOLD_ORE_FALL);
        GILDED_BLACKSTONE = new SoundType(1.0f, 1.0f, SoundEvents.GILDED_BLACKSTONE_BREAK, SoundEvents.GILDED_BLACKSTONE_STEP, SoundEvents.GILDED_BLACKSTONE_PLACE, SoundEvents.GILDED_BLACKSTONE_HIT, SoundEvents.GILDED_BLACKSTONE_FALL);
    }
}
