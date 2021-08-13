package net.minecraft.world.entity.player;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.google.common.collect.Lists;
import java.util.BitSet;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

public class StackedContents {
    public final Int2IntMap contents;
    
    public StackedContents() {
        this.contents = (Int2IntMap)new Int2IntOpenHashMap();
    }
    
    public void accountSimpleStack(final ItemStack bly) {
        if (!bly.isDamaged() && !bly.isEnchanted() && !bly.hasCustomHoverName()) {
            this.accountStack(bly);
        }
    }
    
    public void accountStack(final ItemStack bly) {
        this.accountStack(bly, 64);
    }
    
    public void accountStack(final ItemStack bly, final int integer) {
        if (!bly.isEmpty()) {
            final int integer2 = getStackingIndex(bly);
            final int integer3 = Math.min(integer, bly.getCount());
            this.put(integer2, integer3);
        }
    }
    
    public static int getStackingIndex(final ItemStack bly) {
        return Registry.ITEM.getId(bly.getItem());
    }
    
    private boolean has(final int integer) {
        return this.contents.get(integer) > 0;
    }
    
    private int take(final int integer1, final int integer2) {
        final int integer3 = this.contents.get(integer1);
        if (integer3 >= integer2) {
            this.contents.put(integer1, integer3 - integer2);
            return integer1;
        }
        return 0;
    }
    
    private void put(final int integer1, final int integer2) {
        this.contents.put(integer1, this.contents.get(integer1) + integer2);
    }
    
    public boolean canCraft(final Recipe<?> bon, @Nullable final IntList intList) {
        return this.canCraft(bon, intList, 1);
    }
    
    public boolean canCraft(final Recipe<?> bon, @Nullable final IntList intList, final int integer) {
        return new RecipePicker(bon).tryPick(integer, intList);
    }
    
    public int getBiggestCraftableStack(final Recipe<?> bon, @Nullable final IntList intList) {
        return this.getBiggestCraftableStack(bon, Integer.MAX_VALUE, intList);
    }
    
    public int getBiggestCraftableStack(final Recipe<?> bon, final int integer, @Nullable final IntList intList) {
        return new RecipePicker(bon).tryPickAll(integer, intList);
    }
    
    public static ItemStack fromStackingIndex(final int integer) {
        if (integer == 0) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(Item.byId(integer));
    }
    
    public void clear() {
        this.contents.clear();
    }
    
    class RecipePicker {
        private final Recipe<?> recipe;
        private final List<Ingredient> ingredients;
        private final int ingredientCount;
        private final int[] items;
        private final int itemCount;
        private final BitSet data;
        private final IntList path;
        
        public RecipePicker(final Recipe<?> bon) {
            this.ingredients = (List<Ingredient>)Lists.newArrayList();
            this.path = (IntList)new IntArrayList();
            this.recipe = bon;
            this.ingredients.addAll((Collection)bon.getIngredients());
            this.ingredients.removeIf(Ingredient::isEmpty);
            this.ingredientCount = this.ingredients.size();
            this.items = this.getUniqueAvailableIngredientItems();
            this.itemCount = this.items.length;
            this.data = new BitSet(this.ingredientCount + this.itemCount + this.ingredientCount + this.ingredientCount * this.itemCount);
            for (int integer4 = 0; integer4 < this.ingredients.size(); ++integer4) {
                final IntList intList5 = ((Ingredient)this.ingredients.get(integer4)).getStackingIds();
                for (int integer5 = 0; integer5 < this.itemCount; ++integer5) {
                    if (intList5.contains(this.items[integer5])) {
                        this.data.set(this.getIndex(true, integer5, integer4));
                    }
                }
            }
        }
        
        public boolean tryPick(final int integer, @Nullable final IntList intList) {
            if (integer <= 0) {
                return true;
            }
            int integer2 = 0;
            while (this.dfs(integer)) {
                StackedContents.this.take(this.items[this.path.getInt(0)], integer);
                final int integer3 = this.path.size() - 1;
                this.setSatisfied(this.path.getInt(integer3));
                for (int integer4 = 0; integer4 < integer3; ++integer4) {
                    this.toggleResidual((integer4 & 0x1) == 0x0, this.path.get(integer4), this.path.get(integer4 + 1));
                }
                this.path.clear();
                this.data.clear(0, this.ingredientCount + this.itemCount);
                ++integer2;
            }
            final boolean boolean5 = integer2 == this.ingredientCount;
            final boolean boolean6 = boolean5 && intList != null;
            if (boolean6) {
                intList.clear();
            }
            this.data.clear(0, this.ingredientCount + this.itemCount + this.ingredientCount);
            int integer5 = 0;
            final List<Ingredient> list8 = (List<Ingredient>)this.recipe.getIngredients();
            for (int integer6 = 0; integer6 < list8.size(); ++integer6) {
                if (boolean6 && ((Ingredient)list8.get(integer6)).isEmpty()) {
                    intList.add(0);
                }
                else {
                    for (int integer7 = 0; integer7 < this.itemCount; ++integer7) {
                        if (this.hasResidual(false, integer5, integer7)) {
                            this.toggleResidual(true, integer7, integer5);
                            StackedContents.this.put(this.items[integer7], integer);
                            if (boolean6) {
                                intList.add(this.items[integer7]);
                            }
                        }
                    }
                    ++integer5;
                }
            }
            return boolean5;
        }
        
        private int[] getUniqueAvailableIngredientItems() {
            final IntCollection intCollection2 = (IntCollection)new IntAVLTreeSet();
            for (final Ingredient bok4 : this.ingredients) {
                intCollection2.addAll((IntCollection)bok4.getStackingIds());
            }
            final IntIterator intIterator3 = intCollection2.iterator();
            while (intIterator3.hasNext()) {
                if (!StackedContents.this.has(intIterator3.nextInt())) {
                    intIterator3.remove();
                }
            }
            return intCollection2.toIntArray();
        }
        
        private boolean dfs(final int integer) {
            for (int integer2 = this.itemCount, integer3 = 0; integer3 < integer2; ++integer3) {
                if (StackedContents.this.contents.get(this.items[integer3]) >= integer) {
                    this.visit(false, integer3);
                    while (!this.path.isEmpty()) {
                        final int integer4 = this.path.size();
                        final boolean boolean6 = (integer4 & 0x1) == 0x1;
                        final int integer5 = this.path.getInt(integer4 - 1);
                        if (!boolean6 && !this.isSatisfied(integer5)) {
                            break;
                        }
                        for (int integer6 = boolean6 ? this.ingredientCount : integer2, integer7 = 0; integer7 < integer6; ++integer7) {
                            if (!this.hasVisited(boolean6, integer7) && this.hasConnection(boolean6, integer5, integer7) && this.hasResidual(boolean6, integer5, integer7)) {
                                this.visit(boolean6, integer7);
                                break;
                            }
                        }
                        int integer7 = this.path.size();
                        if (integer7 != integer4) {
                            continue;
                        }
                        this.path.removeInt(integer7 - 1);
                    }
                    if (!this.path.isEmpty()) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        private boolean isSatisfied(final int integer) {
            return this.data.get(this.getSatisfiedIndex(integer));
        }
        
        private void setSatisfied(final int integer) {
            this.data.set(this.getSatisfiedIndex(integer));
        }
        
        private int getSatisfiedIndex(final int integer) {
            return this.ingredientCount + this.itemCount + integer;
        }
        
        private boolean hasConnection(final boolean boolean1, final int integer2, final int integer3) {
            return this.data.get(this.getIndex(boolean1, integer2, integer3));
        }
        
        private boolean hasResidual(final boolean boolean1, final int integer2, final int integer3) {
            return boolean1 != this.data.get(1 + this.getIndex(boolean1, integer2, integer3));
        }
        
        private void toggleResidual(final boolean boolean1, final int integer2, final int integer3) {
            this.data.flip(1 + this.getIndex(boolean1, integer2, integer3));
        }
        
        private int getIndex(final boolean boolean1, final int integer2, final int integer3) {
            final int integer4 = boolean1 ? (integer2 * this.ingredientCount + integer3) : (integer3 * this.ingredientCount + integer2);
            return this.ingredientCount + this.itemCount + this.ingredientCount + 2 * integer4;
        }
        
        private void visit(final boolean boolean1, final int integer) {
            this.data.set(this.getVisitedIndex(boolean1, integer));
            this.path.add(integer);
        }
        
        private boolean hasVisited(final boolean boolean1, final int integer) {
            return this.data.get(this.getVisitedIndex(boolean1, integer));
        }
        
        private int getVisitedIndex(final boolean boolean1, final int integer) {
            return (boolean1 ? 0 : this.ingredientCount) + integer;
        }
        
        public int tryPickAll(final int integer, @Nullable final IntList intList) {
            int integer2 = 0;
            int integer3 = Math.min(integer, this.getMinIngredientCount()) + 1;
            int integer4;
            while (true) {
                integer4 = (integer2 + integer3) / 2;
                if (this.tryPick(integer4, null)) {
                    if (integer3 - integer2 <= 1) {
                        break;
                    }
                    integer2 = integer4;
                }
                else {
                    integer3 = integer4;
                }
            }
            if (integer4 > 0) {
                this.tryPick(integer4, intList);
            }
            return integer4;
        }
        
        private int getMinIngredientCount() {
            int integer2 = Integer.MAX_VALUE;
            for (final Ingredient bok4 : this.ingredients) {
                int integer3 = 0;
                for (final int integer4 : bok4.getStackingIds()) {
                    integer3 = Math.max(integer3, StackedContents.this.contents.get(integer4));
                }
                if (integer2 > 0) {
                    integer2 = Math.min(integer2, integer3);
                }
            }
            return integer2;
        }
    }
}
