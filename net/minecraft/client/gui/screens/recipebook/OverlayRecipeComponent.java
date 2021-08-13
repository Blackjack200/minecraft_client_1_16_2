package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import net.minecraft.util.Mth;
import java.util.Collections;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.GuiComponent;

public class OverlayRecipeComponent extends GuiComponent implements Widget, GuiEventListener {
    private static final ResourceLocation RECIPE_BOOK_LOCATION;
    private final List<OverlayRecipeButton> recipeButtons;
    private boolean isVisible;
    private int x;
    private int y;
    private Minecraft minecraft;
    private RecipeCollection collection;
    private Recipe<?> lastRecipeClicked;
    private float time;
    private boolean isFurnaceMenu;
    
    public OverlayRecipeComponent() {
        this.recipeButtons = (List<OverlayRecipeButton>)Lists.newArrayList();
    }
    
    public void init(final Minecraft djw, final RecipeCollection drq, final int integer3, final int integer4, final int integer5, final int integer6, final float float7) {
        this.minecraft = djw;
        this.collection = drq;
        if (djw.player.containerMenu instanceof AbstractFurnaceMenu) {
            this.isFurnaceMenu = true;
        }
        final boolean boolean9 = djw.player.getRecipeBook().isFiltering(djw.player.containerMenu);
        final List<Recipe<?>> list10 = drq.getDisplayRecipes(true);
        final List<Recipe<?>> list11 = (List<Recipe<?>>)(boolean9 ? Collections.emptyList() : drq.getDisplayRecipes(false));
        final int integer7 = list10.size();
        final int integer8 = integer7 + list11.size();
        final int integer9 = (integer8 <= 16) ? 4 : 5;
        final int integer10 = (int)Math.ceil((double)(integer8 / (float)integer9));
        this.x = integer3;
        this.y = integer4;
        final int integer11 = 25;
        final float float8 = (float)(this.x + Math.min(integer8, integer9) * 25);
        final float float9 = (float)(integer5 + 50);
        if (float8 > float9) {
            this.x -= (int)(float7 * (int)((float8 - float9) / float7));
        }
        final float float10 = (float)(this.y + integer10 * 25);
        final float float11 = (float)(integer6 + 50);
        if (float10 > float11) {
            this.y -= (int)(float7 * Mth.ceil((float10 - float11) / float7));
        }
        final float float12 = (float)this.y;
        final float float13 = (float)(integer6 - 100);
        if (float12 < float13) {
            this.y -= (int)(float7 * Mth.ceil((float12 - float13) / float7));
        }
        this.isVisible = true;
        this.recipeButtons.clear();
        for (int integer12 = 0; integer12 < integer8; ++integer12) {
            final boolean boolean10 = integer12 < integer7;
            final Recipe<?> bon25 = (boolean10 ? list10.get(integer12) : ((Recipe)list11.get(integer12 - integer7)));
            final int integer13 = this.x + 4 + 25 * (integer12 % integer9);
            final int integer14 = this.y + 5 + 25 * (integer12 / integer9);
            if (this.isFurnaceMenu) {
                this.recipeButtons.add(new OverlaySmeltingRecipeButton(integer13, integer14, bon25, boolean10));
            }
            else {
                this.recipeButtons.add(new OverlayRecipeButton(integer13, integer14, bon25, boolean10));
            }
        }
        this.lastRecipeClicked = null;
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        return false;
    }
    
    public RecipeCollection getRecipeCollection() {
        return this.collection;
    }
    
    public Recipe<?> getLastRecipeClicked() {
        return this.lastRecipeClicked;
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (integer != 0) {
            return false;
        }
        for (final OverlayRecipeButton a8 : this.recipeButtons) {
            if (a8.mouseClicked(double1, double2, integer)) {
                this.lastRecipeClicked = a8.recipe;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isMouseOver(final double double1, final double double2) {
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (!this.isVisible) {
            return;
        }
        this.time += float4;
        RenderSystem.enableBlend();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(OverlayRecipeComponent.RECIPE_BOOK_LOCATION);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0f, 0.0f, 170.0f);
        final int integer4 = (this.recipeButtons.size() <= 16) ? 4 : 5;
        final int integer5 = Math.min(this.recipeButtons.size(), integer4);
        final int integer6 = Mth.ceil(this.recipeButtons.size() / (float)integer4);
        final int integer7 = 24;
        final int integer8 = 4;
        final int integer9 = 82;
        final int integer10 = 208;
        this.nineInchSprite(dfj, integer5, integer6, 24, 4, 82, 208);
        RenderSystem.disableBlend();
        for (final OverlayRecipeButton a14 : this.recipeButtons) {
            a14.render(dfj, integer2, integer3, float4);
        }
        RenderSystem.popMatrix();
    }
    
    private void nineInchSprite(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        this.blit(dfj, this.x, this.y, integer6, integer7, integer5, integer5);
        this.blit(dfj, this.x + integer5 * 2 + integer2 * integer4, this.y, integer6 + integer4 + integer5, integer7, integer5, integer5);
        this.blit(dfj, this.x, this.y + integer5 * 2 + integer3 * integer4, integer6, integer7 + integer4 + integer5, integer5, integer5);
        this.blit(dfj, this.x + integer5 * 2 + integer2 * integer4, this.y + integer5 * 2 + integer3 * integer4, integer6 + integer4 + integer5, integer7 + integer4 + integer5, integer5, integer5);
        for (int integer8 = 0; integer8 < integer2; ++integer8) {
            this.blit(dfj, this.x + integer5 + integer8 * integer4, this.y, integer6 + integer5, integer7, integer4, integer5);
            this.blit(dfj, this.x + integer5 + (integer8 + 1) * integer4, this.y, integer6 + integer5, integer7, integer5, integer5);
            for (int integer9 = 0; integer9 < integer3; ++integer9) {
                if (integer8 == 0) {
                    this.blit(dfj, this.x, this.y + integer5 + integer9 * integer4, integer6, integer7 + integer5, integer5, integer4);
                    this.blit(dfj, this.x, this.y + integer5 + (integer9 + 1) * integer4, integer6, integer7 + integer5, integer5, integer5);
                }
                this.blit(dfj, this.x + integer5 + integer8 * integer4, this.y + integer5 + integer9 * integer4, integer6 + integer5, integer7 + integer5, integer4, integer4);
                this.blit(dfj, this.x + integer5 + (integer8 + 1) * integer4, this.y + integer5 + integer9 * integer4, integer6 + integer5, integer7 + integer5, integer5, integer4);
                this.blit(dfj, this.x + integer5 + integer8 * integer4, this.y + integer5 + (integer9 + 1) * integer4, integer6 + integer5, integer7 + integer5, integer4, integer5);
                this.blit(dfj, this.x + integer5 + (integer8 + 1) * integer4 - 1, this.y + integer5 + (integer9 + 1) * integer4 - 1, integer6 + integer5, integer7 + integer5, integer5 + 1, integer5 + 1);
                if (integer8 == integer2 - 1) {
                    this.blit(dfj, this.x + integer5 * 2 + integer2 * integer4, this.y + integer5 + integer9 * integer4, integer6 + integer4 + integer5, integer7 + integer5, integer5, integer4);
                    this.blit(dfj, this.x + integer5 * 2 + integer2 * integer4, this.y + integer5 + (integer9 + 1) * integer4, integer6 + integer4 + integer5, integer7 + integer5, integer5, integer5);
                }
            }
            this.blit(dfj, this.x + integer5 + integer8 * integer4, this.y + integer5 * 2 + integer3 * integer4, integer6 + integer5, integer7 + integer4 + integer5, integer4, integer5);
            this.blit(dfj, this.x + integer5 + (integer8 + 1) * integer4, this.y + integer5 * 2 + integer3 * integer4, integer6 + integer5, integer7 + integer4 + integer5, integer5, integer5);
        }
    }
    
    public void setVisible(final boolean boolean1) {
        this.isVisible = boolean1;
    }
    
    public boolean isVisible() {
        return this.isVisible;
    }
    
    static {
        RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
    }
    
    class OverlaySmeltingRecipeButton extends OverlayRecipeButton {
        public OverlaySmeltingRecipeButton(final int integer2, final int integer3, final Recipe<?> bon, final boolean boolean5) {
            super(integer2, integer3, bon, boolean5);
        }
        
        @Override
        protected void calculateIngredientsPositions(final Recipe<?> bon) {
            final ItemStack[] arr3 = bon.getIngredients().get(0).getItems();
            this.ingredientPos.add(new Pos(10, 10, arr3));
        }
    }
    
    class OverlayRecipeButton extends AbstractWidget implements PlaceRecipe<Ingredient> {
        private final Recipe<?> recipe;
        private final boolean isCraftable;
        protected final List<Pos> ingredientPos;
        
        public OverlayRecipeButton(final int integer2, final int integer3, final Recipe<?> bon, final boolean boolean5) {
            super(integer2, integer3, 200, 20, TextComponent.EMPTY);
            this.ingredientPos = (List<Pos>)Lists.newArrayList();
            this.width = 24;
            this.height = 24;
            this.recipe = bon;
            this.isCraftable = boolean5;
            this.calculateIngredientsPositions(bon);
        }
        
        protected void calculateIngredientsPositions(final Recipe<?> bon) {
            this.placeRecipe(3, 3, -1, bon, (java.util.Iterator<Ingredient>)bon.getIngredients().iterator(), 0);
        }
        
        @Override
        public void addItemToSlot(final Iterator<Ingredient> iterator, final int integer2, final int integer3, final int integer4, final int integer5) {
            final ItemStack[] arr7 = ((Ingredient)iterator.next()).getItems();
            if (arr7.length != 0) {
                this.ingredientPos.add(new Pos(3 + integer5 * 7, 3 + integer4 * 7, arr7));
            }
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RenderSystem.enableAlphaTest();
            OverlayRecipeComponent.this.minecraft.getTextureManager().bind(OverlayRecipeComponent.RECIPE_BOOK_LOCATION);
            int integer4 = 152;
            if (!this.isCraftable) {
                integer4 += 26;
            }
            int integer5 = OverlayRecipeComponent.this.isFurnaceMenu ? 130 : 78;
            if (this.isHovered()) {
                integer5 += 26;
            }
            this.blit(dfj, this.x, this.y, integer4, integer5, this.width, this.height);
            for (final Pos a9 : this.ingredientPos) {
                RenderSystem.pushMatrix();
                final float float5 = 0.42f;
                final int integer6 = (int)((this.x + a9.x) / 0.42f - 3.0f);
                final int integer7 = (int)((this.y + a9.y) / 0.42f - 3.0f);
                RenderSystem.scalef(0.42f, 0.42f, 1.0f);
                OverlayRecipeComponent.this.minecraft.getItemRenderer().renderAndDecorateItem(a9.ingredients[Mth.floor(OverlayRecipeComponent.this.time / 30.0f) % a9.ingredients.length], integer6, integer7);
                RenderSystem.popMatrix();
            }
            RenderSystem.disableAlphaTest();
        }
        
        public class Pos {
            public final ItemStack[] ingredients;
            public final int x;
            public final int y;
            
            public Pos(final int integer2, final int integer3, final ItemStack[] arr) {
                this.x = integer2;
                this.y = integer3;
                this.ingredients = arr;
            }
        }
    }
}
