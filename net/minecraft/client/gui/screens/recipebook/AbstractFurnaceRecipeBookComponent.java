package net.minecraft.client.gui.screens.recipebook;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.item.crafting.Recipe;
import javax.annotation.Nullable;
import net.minecraft.world.inventory.Slot;
import java.util.Set;
import net.minecraft.world.item.Item;
import java.util.Iterator;

public abstract class AbstractFurnaceRecipeBookComponent extends RecipeBookComponent {
    private Iterator<Item> iterator;
    private Set<Item> fuels;
    private Slot fuelSlot;
    private Item fuel;
    private float time;
    
    @Override
    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(152, 182, 28, 18, AbstractFurnaceRecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    @Override
    public void slotClicked(@Nullable final Slot bjo) {
        super.slotClicked(bjo);
        if (bjo != null && bjo.index < this.menu.getSize()) {
            this.fuelSlot = null;
        }
    }
    
    @Override
    public void setupGhostRecipe(final Recipe<?> bon, final List<Slot> list) {
        final ItemStack bly4 = bon.getResultItem();
        this.ghostRecipe.setRecipe(bon);
        this.ghostRecipe.addIngredient(Ingredient.of(bly4), ((Slot)list.get(2)).x, ((Slot)list.get(2)).y);
        final NonNullList<Ingredient> gj5 = bon.getIngredients();
        this.fuelSlot = (Slot)list.get(1);
        if (this.fuels == null) {
            this.fuels = this.getFuelItems();
        }
        this.iterator = (Iterator<Item>)this.fuels.iterator();
        this.fuel = null;
        final Iterator<Ingredient> iterator6 = (Iterator<Ingredient>)gj5.iterator();
        for (int integer7 = 0; integer7 < 2; ++integer7) {
            if (!iterator6.hasNext()) {
                return;
            }
            final Ingredient bok8 = (Ingredient)iterator6.next();
            if (!bok8.isEmpty()) {
                final Slot bjo9 = (Slot)list.get(integer7);
                this.ghostRecipe.addIngredient(bok8, bjo9.x, bjo9.y);
            }
        }
    }
    
    protected abstract Set<Item> getFuelItems();
    
    @Override
    public void renderGhostRecipe(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4, final float float5) {
        super.renderGhostRecipe(dfj, integer2, integer3, boolean4, float5);
        if (this.fuelSlot == null) {
            return;
        }
        if (!Screen.hasControlDown()) {
            this.time += float5;
        }
        final int integer4 = this.fuelSlot.x + integer2;
        final int integer5 = this.fuelSlot.y + integer3;
        GuiComponent.fill(dfj, integer4, integer5, integer4 + 16, integer5 + 16, 822018048);
        this.minecraft.getItemRenderer().renderAndDecorateItem(this.minecraft.player, this.getFuel().getDefaultInstance(), integer4, integer5);
        RenderSystem.depthFunc(516);
        GuiComponent.fill(dfj, integer4, integer5, integer4 + 16, integer5 + 16, 822083583);
        RenderSystem.depthFunc(515);
    }
    
    private Item getFuel() {
        if (this.fuel == null || this.time > 30.0f) {
            this.time = 0.0f;
            if (this.iterator == null || !this.iterator.hasNext()) {
                if (this.fuels == null) {
                    this.fuels = this.getFuelItems();
                }
                this.iterator = (Iterator<Item>)this.fuels.iterator();
            }
            this.fuel = (Item)this.iterator.next();
        }
        return this.fuel;
    }
}
