package net.minecraft.world.entity.npc;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class VillagerProfession {
    public static final VillagerProfession NONE;
    public static final VillagerProfession ARMORER;
    public static final VillagerProfession BUTCHER;
    public static final VillagerProfession CARTOGRAPHER;
    public static final VillagerProfession CLERIC;
    public static final VillagerProfession FARMER;
    public static final VillagerProfession FISHERMAN;
    public static final VillagerProfession FLETCHER;
    public static final VillagerProfession LEATHERWORKER;
    public static final VillagerProfession LIBRARIAN;
    public static final VillagerProfession MASON;
    public static final VillagerProfession NITWIT;
    public static final VillagerProfession SHEPHERD;
    public static final VillagerProfession TOOLSMITH;
    public static final VillagerProfession WEAPONSMITH;
    private final String name;
    private final PoiType jobPoiType;
    private final ImmutableSet<Item> requestedItems;
    private final ImmutableSet<Block> secondaryPoi;
    @Nullable
    private final SoundEvent workSound;
    
    private VillagerProfession(final String string, final PoiType azo, final ImmutableSet<Item> immutableSet3, final ImmutableSet<Block> immutableSet4, @Nullable final SoundEvent adn) {
        this.name = string;
        this.jobPoiType = azo;
        this.requestedItems = immutableSet3;
        this.secondaryPoi = immutableSet4;
        this.workSound = adn;
    }
    
    public PoiType getJobPoiType() {
        return this.jobPoiType;
    }
    
    public ImmutableSet<Item> getRequestedItems() {
        return this.requestedItems;
    }
    
    public ImmutableSet<Block> getSecondaryPoi() {
        return this.secondaryPoi;
    }
    
    @Nullable
    public SoundEvent getWorkSound() {
        return this.workSound;
    }
    
    public String toString() {
        return this.name;
    }
    
    static VillagerProfession register(final String string, final PoiType azo, @Nullable final SoundEvent adn) {
        return register(string, azo, (ImmutableSet<Item>)ImmutableSet.of(), (ImmutableSet<Block>)ImmutableSet.of(), adn);
    }
    
    static VillagerProfession register(final String string, final PoiType azo, final ImmutableSet<Item> immutableSet3, final ImmutableSet<Block> immutableSet4, @Nullable final SoundEvent adn) {
        return Registry.<VillagerProfession, VillagerProfession>register(Registry.VILLAGER_PROFESSION, new ResourceLocation(string), new VillagerProfession(string, azo, immutableSet3, immutableSet4, adn));
    }
    
    static {
        NONE = register("none", PoiType.UNEMPLOYED, (SoundEvent)null);
        ARMORER = register("armorer", PoiType.ARMORER, SoundEvents.VILLAGER_WORK_ARMORER);
        BUTCHER = register("butcher", PoiType.BUTCHER, SoundEvents.VILLAGER_WORK_BUTCHER);
        CARTOGRAPHER = register("cartographer", PoiType.CARTOGRAPHER, SoundEvents.VILLAGER_WORK_CARTOGRAPHER);
        CLERIC = register("cleric", PoiType.CLERIC, SoundEvents.VILLAGER_WORK_CLERIC);
        FARMER = register("farmer", PoiType.FARMER, (ImmutableSet<Item>)ImmutableSet.of(Items.WHEAT, Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.BONE_MEAL), (ImmutableSet<Block>)ImmutableSet.of(Blocks.FARMLAND), SoundEvents.VILLAGER_WORK_FARMER);
        FISHERMAN = register("fisherman", PoiType.FISHERMAN, SoundEvents.VILLAGER_WORK_FISHERMAN);
        FLETCHER = register("fletcher", PoiType.FLETCHER, SoundEvents.VILLAGER_WORK_FLETCHER);
        LEATHERWORKER = register("leatherworker", PoiType.LEATHERWORKER, SoundEvents.VILLAGER_WORK_LEATHERWORKER);
        LIBRARIAN = register("librarian", PoiType.LIBRARIAN, SoundEvents.VILLAGER_WORK_LIBRARIAN);
        MASON = register("mason", PoiType.MASON, SoundEvents.VILLAGER_WORK_MASON);
        NITWIT = register("nitwit", PoiType.NITWIT, (SoundEvent)null);
        SHEPHERD = register("shepherd", PoiType.SHEPHERD, SoundEvents.VILLAGER_WORK_SHEPHERD);
        TOOLSMITH = register("toolsmith", PoiType.TOOLSMITH, SoundEvents.VILLAGER_WORK_TOOLSMITH);
        WEAPONSMITH = register("weaponsmith", PoiType.WEAPONSMITH, SoundEvents.VILLAGER_WORK_WEAPONSMITH);
    }
}
