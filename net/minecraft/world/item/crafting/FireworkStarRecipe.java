package net.minecraft.world.item.crafting;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import net.minecraft.world.Container;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import java.util.Map;

public class FireworkStarRecipe extends CustomRecipe {
    private static final Ingredient SHAPE_INGREDIENT;
    private static final Ingredient TRAIL_INGREDIENT;
    private static final Ingredient FLICKER_INGREDIENT;
    private static final Map<Item, FireworkRocketItem.Shape> SHAPE_BY_ITEM;
    private static final Ingredient GUNPOWDER_INGREDIENT;
    
    public FireworkStarRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        boolean boolean4 = false;
        boolean boolean5 = false;
        boolean boolean6 = false;
        boolean boolean7 = false;
        boolean boolean8 = false;
        for (int integer9 = 0; integer9 < bil.getContainerSize(); ++integer9) {
            final ItemStack bly10 = bil.getItem(integer9);
            if (!bly10.isEmpty()) {
                if (FireworkStarRecipe.SHAPE_INGREDIENT.test(bly10)) {
                    if (boolean6) {
                        return false;
                    }
                    boolean6 = true;
                }
                else if (FireworkStarRecipe.FLICKER_INGREDIENT.test(bly10)) {
                    if (boolean8) {
                        return false;
                    }
                    boolean8 = true;
                }
                else if (FireworkStarRecipe.TRAIL_INGREDIENT.test(bly10)) {
                    if (boolean7) {
                        return false;
                    }
                    boolean7 = true;
                }
                else if (FireworkStarRecipe.GUNPOWDER_INGREDIENT.test(bly10)) {
                    if (boolean4) {
                        return false;
                    }
                    boolean4 = true;
                }
                else {
                    if (!(bly10.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    boolean5 = true;
                }
            }
        }
        return boolean4 && boolean5;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final ItemStack bly3 = new ItemStack(Items.FIREWORK_STAR);
        final CompoundTag md4 = bly3.getOrCreateTagElement("Explosion");
        FireworkRocketItem.Shape a5 = FireworkRocketItem.Shape.SMALL_BALL;
        final List<Integer> list6 = (List<Integer>)Lists.newArrayList();
        for (int integer7 = 0; integer7 < bil.getContainerSize(); ++integer7) {
            final ItemStack bly4 = bil.getItem(integer7);
            if (!bly4.isEmpty()) {
                if (FireworkStarRecipe.SHAPE_INGREDIENT.test(bly4)) {
                    a5 = (FireworkRocketItem.Shape)FireworkStarRecipe.SHAPE_BY_ITEM.get(bly4.getItem());
                }
                else if (FireworkStarRecipe.FLICKER_INGREDIENT.test(bly4)) {
                    md4.putBoolean("Flicker", true);
                }
                else if (FireworkStarRecipe.TRAIL_INGREDIENT.test(bly4)) {
                    md4.putBoolean("Trail", true);
                }
                else if (bly4.getItem() instanceof DyeItem) {
                    list6.add(((DyeItem)bly4.getItem()).getDyeColor().getFireworkColor());
                }
            }
        }
        md4.putIntArray("Colors", list6);
        md4.putByte("Type", (byte)a5.getId());
        return bly3;
    }
    
    public boolean canCraftInDimensions(final int integer1, final int integer2) {
        return integer1 * integer2 >= 2;
    }
    
    @Override
    public ItemStack getResultItem() {
        return new ItemStack(Items.FIREWORK_STAR);
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.FIREWORK_STAR;
    }
    
    static {
        SHAPE_INGREDIENT = Ingredient.of(Items.FIRE_CHARGE, Items.FEATHER, Items.GOLD_NUGGET, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL, Items.CREEPER_HEAD, Items.PLAYER_HEAD, Items.DRAGON_HEAD, Items.ZOMBIE_HEAD);
        TRAIL_INGREDIENT = Ingredient.of(Items.DIAMOND);
        FLICKER_INGREDIENT = Ingredient.of(Items.GLOWSTONE_DUST);
        SHAPE_BY_ITEM = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(Items.FIRE_CHARGE, FireworkRocketItem.Shape.LARGE_BALL);
            hashMap.put(Items.FEATHER, FireworkRocketItem.Shape.BURST);
            hashMap.put(Items.GOLD_NUGGET, FireworkRocketItem.Shape.STAR);
            hashMap.put(Items.SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
            hashMap.put(Items.WITHER_SKELETON_SKULL, FireworkRocketItem.Shape.CREEPER);
            hashMap.put(Items.CREEPER_HEAD, FireworkRocketItem.Shape.CREEPER);
            hashMap.put(Items.PLAYER_HEAD, FireworkRocketItem.Shape.CREEPER);
            hashMap.put(Items.DRAGON_HEAD, FireworkRocketItem.Shape.CREEPER);
            hashMap.put(Items.ZOMBIE_HEAD, FireworkRocketItem.Shape.CREEPER);
        }));
        GUNPOWDER_INGREDIENT = Ingredient.of(Items.GUNPOWDER);
    }
}
