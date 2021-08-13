package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.util.Mth;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Ingredient;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.item.crafting.Recipe;

public class GhostRecipe {
    private Recipe<?> recipe;
    private final List<GhostIngredient> ingredients;
    private float time;
    
    public GhostRecipe() {
        this.ingredients = (List<GhostIngredient>)Lists.newArrayList();
    }
    
    public void clear() {
        this.recipe = null;
        this.ingredients.clear();
        this.time = 0.0f;
    }
    
    public void addIngredient(final Ingredient bok, final int integer2, final int integer3) {
        this.ingredients.add(new GhostIngredient(bok, integer2, integer3));
    }
    
    public GhostIngredient get(final int integer) {
        return (GhostIngredient)this.ingredients.get(integer);
    }
    
    public int size() {
        return this.ingredients.size();
    }
    
    @Nullable
    public Recipe<?> getRecipe() {
        return this.recipe;
    }
    
    public void setRecipe(final Recipe<?> bon) {
        this.recipe = bon;
    }
    
    public void render(final PoseStack dfj, final Minecraft djw, final int integer3, final int integer4, final boolean boolean5, final float float6) {
        if (!Screen.hasControlDown()) {
            this.time += float6;
        }
        for (int integer5 = 0; integer5 < this.ingredients.size(); ++integer5) {
            final GhostIngredient a9 = (GhostIngredient)this.ingredients.get(integer5);
            final int integer6 = a9.getX() + integer3;
            final int integer7 = a9.getY() + integer4;
            if (integer5 == 0 && boolean5) {
                GuiComponent.fill(dfj, integer6 - 4, integer7 - 4, integer6 + 20, integer7 + 20, 822018048);
            }
            else {
                GuiComponent.fill(dfj, integer6, integer7, integer6 + 16, integer7 + 16, 822018048);
            }
            final ItemStack bly12 = a9.getItem();
            final ItemRenderer efg13 = djw.getItemRenderer();
            efg13.renderAndDecorateFakeItem(bly12, integer6, integer7);
            RenderSystem.depthFunc(516);
            GuiComponent.fill(dfj, integer6, integer7, integer6 + 16, integer7 + 16, 822083583);
            RenderSystem.depthFunc(515);
            if (integer5 == 0) {
                efg13.renderGuiItemDecorations(djw.font, bly12, integer6, integer7);
            }
        }
    }
    
    public class GhostIngredient {
        private final Ingredient ingredient;
        private final int x;
        private final int y;
        
        public GhostIngredient(final Ingredient bok, final int integer3, final int integer4) {
            this.ingredient = bok;
            this.x = integer3;
            this.y = integer4;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
        
        public ItemStack getItem() {
            final ItemStack[] arr2 = this.ingredient.getItems();
            return arr2[Mth.floor(GhostRecipe.this.time / 30.0f) % arr2.length];
        }
    }
}
