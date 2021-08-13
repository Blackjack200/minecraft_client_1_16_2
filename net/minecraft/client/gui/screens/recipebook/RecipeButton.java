package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.AbstractWidget;

public class RecipeButton extends AbstractWidget {
    private static final ResourceLocation RECIPE_BOOK_LOCATION;
    private static final Component MORE_RECIPES_TOOLTIP;
    private RecipeBookMenu<?> menu;
    private RecipeBook book;
    private RecipeCollection collection;
    private float time;
    private float animationTime;
    private int currentIndex;
    
    public RecipeButton() {
        super(0, 0, 25, 25, TextComponent.EMPTY);
    }
    
    public void init(final RecipeCollection drq, final RecipeBookPage drn) {
        this.collection = drq;
        this.menu = drn.getMinecraft().player.containerMenu;
        this.book = drn.getRecipeBook();
        final List<Recipe<?>> list4 = drq.getRecipes(this.book.isFiltering(this.menu));
        for (final Recipe<?> bon6 : list4) {
            if (this.book.willHighlight(bon6)) {
                drn.recipesShown(list4);
                this.animationTime = 15.0f;
                break;
            }
        }
    }
    
    public RecipeCollection getCollection() {
        return this.collection;
    }
    
    public void setPosition(final int integer1, final int integer2) {
        this.x = integer1;
        this.y = integer2;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (!Screen.hasControlDown()) {
            this.time += float4;
        }
        final Minecraft djw6 = Minecraft.getInstance();
        djw6.getTextureManager().bind(RecipeButton.RECIPE_BOOK_LOCATION);
        int integer4 = 29;
        if (!this.collection.hasCraftable()) {
            integer4 += 25;
        }
        int integer5 = 206;
        if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
            integer5 += 25;
        }
        final boolean boolean9 = this.animationTime > 0.0f;
        if (boolean9) {
            final float float5 = 1.0f + 0.1f * (float)Math.sin((double)(this.animationTime / 15.0f * 3.1415927f));
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(this.x + 8), (float)(this.y + 12), 0.0f);
            RenderSystem.scalef(float5, float5, 1.0f);
            RenderSystem.translatef((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0f);
            this.animationTime -= float4;
        }
        this.blit(dfj, this.x, this.y, integer4, integer5, this.width, this.height);
        final List<Recipe<?>> list10 = this.getOrderedRecipes();
        this.currentIndex = Mth.floor(this.time / 30.0f) % list10.size();
        final ItemStack bly11 = ((Recipe)list10.get(this.currentIndex)).getResultItem();
        int integer6 = 4;
        if (this.collection.hasSingleResultItem() && this.getOrderedRecipes().size() > 1) {
            djw6.getItemRenderer().renderAndDecorateItem(bly11, this.x + integer6 + 1, this.y + integer6 + 1);
            --integer6;
        }
        djw6.getItemRenderer().renderAndDecorateFakeItem(bly11, this.x + integer6, this.y + integer6);
        if (boolean9) {
            RenderSystem.popMatrix();
        }
    }
    
    private List<Recipe<?>> getOrderedRecipes() {
        final List<Recipe<?>> list2 = this.collection.getDisplayRecipes(true);
        if (!this.book.isFiltering(this.menu)) {
            list2.addAll((Collection)this.collection.getDisplayRecipes(false));
        }
        return list2;
    }
    
    public boolean isOnlyOption() {
        return this.getOrderedRecipes().size() == 1;
    }
    
    public Recipe<?> getRecipe() {
        final List<Recipe<?>> list2 = this.getOrderedRecipes();
        return list2.get(this.currentIndex);
    }
    
    public List<Component> getTooltipText(final Screen doq) {
        final ItemStack bly3 = ((Recipe)this.getOrderedRecipes().get(this.currentIndex)).getResultItem();
        final List<Component> list4 = (List<Component>)Lists.newArrayList((Iterable)doq.getTooltipFromItem(bly3));
        if (this.collection.getRecipes(this.book.isFiltering(this.menu)).size() > 1) {
            list4.add(RecipeButton.MORE_RECIPES_TOOLTIP);
        }
        return list4;
    }
    
    @Override
    public int getWidth() {
        return 25;
    }
    
    @Override
    protected boolean isValidClickButton(final int integer) {
        return integer == 0 || integer == 1;
    }
    
    static {
        RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
        MORE_RECIPES_TOOLTIP = new TranslatableComponent("gui.recipebook.moreRecipes");
    }
}
