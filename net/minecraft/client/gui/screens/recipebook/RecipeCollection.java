package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.player.StackedContents;
import java.util.Iterator;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.item.crafting.Recipe;
import java.util.List;

public class RecipeCollection {
    private final List<Recipe<?>> recipes;
    private final boolean singleResultItem;
    private final Set<Recipe<?>> craftable;
    private final Set<Recipe<?>> fitsDimensions;
    private final Set<Recipe<?>> known;
    
    public RecipeCollection(final List<Recipe<?>> list) {
        this.craftable = (Set<Recipe<?>>)Sets.newHashSet();
        this.fitsDimensions = (Set<Recipe<?>>)Sets.newHashSet();
        this.known = (Set<Recipe<?>>)Sets.newHashSet();
        this.recipes = (List<Recipe<?>>)ImmutableList.copyOf((Collection)list);
        if (list.size() <= 1) {
            this.singleResultItem = true;
        }
        else {
            this.singleResultItem = allRecipesHaveSameResult(list);
        }
    }
    
    private static boolean allRecipesHaveSameResult(final List<Recipe<?>> list) {
        final int integer2 = list.size();
        final ItemStack bly3 = ((Recipe)list.get(0)).getResultItem();
        for (int integer3 = 1; integer3 < integer2; ++integer3) {
            final ItemStack bly4 = ((Recipe)list.get(integer3)).getResultItem();
            if (!ItemStack.isSame(bly3, bly4) || !ItemStack.tagMatches(bly3, bly4)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasKnownRecipes() {
        return !this.known.isEmpty();
    }
    
    public void updateKnownRecipes(final RecipeBook adr) {
        for (final Recipe<?> bon4 : this.recipes) {
            if (adr.contains(bon4)) {
                this.known.add(bon4);
            }
        }
    }
    
    public void canCraft(final StackedContents bfv, final int integer2, final int integer3, final RecipeBook adr) {
        for (final Recipe<?> bon7 : this.recipes) {
            final boolean boolean8 = bon7.canCraftInDimensions(integer2, integer3) && adr.contains(bon7);
            if (boolean8) {
                this.fitsDimensions.add(bon7);
            }
            else {
                this.fitsDimensions.remove(bon7);
            }
            if (boolean8 && bfv.canCraft(bon7, null)) {
                this.craftable.add(bon7);
            }
            else {
                this.craftable.remove(bon7);
            }
        }
    }
    
    public boolean isCraftable(final Recipe<?> bon) {
        return this.craftable.contains(bon);
    }
    
    public boolean hasCraftable() {
        return !this.craftable.isEmpty();
    }
    
    public boolean hasFitting() {
        return !this.fitsDimensions.isEmpty();
    }
    
    public List<Recipe<?>> getRecipes() {
        return this.recipes;
    }
    
    public List<Recipe<?>> getRecipes(final boolean boolean1) {
        final List<Recipe<?>> list3 = (List<Recipe<?>>)Lists.newArrayList();
        final Set<Recipe<?>> set4 = boolean1 ? this.craftable : this.fitsDimensions;
        for (final Recipe<?> bon6 : this.recipes) {
            if (set4.contains(bon6)) {
                list3.add(bon6);
            }
        }
        return list3;
    }
    
    public List<Recipe<?>> getDisplayRecipes(final boolean boolean1) {
        final List<Recipe<?>> list3 = (List<Recipe<?>>)Lists.newArrayList();
        for (final Recipe<?> bon5 : this.recipes) {
            if (this.fitsDimensions.contains(bon5) && this.craftable.contains(bon5) == boolean1) {
                list3.add(bon5);
            }
        }
        return list3;
    }
    
    public boolean hasSingleResultItem() {
        return this.singleResultItem;
    }
}
