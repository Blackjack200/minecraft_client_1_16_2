package net.minecraft.client.gui.screens.recipebook;

import javax.annotation.Nullable;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.Minecraft;
import java.util.List;

public class RecipeBookPage {
    private final List<RecipeButton> buttons;
    private RecipeButton hoveredButton;
    private final OverlayRecipeComponent overlay;
    private Minecraft minecraft;
    private final List<RecipeShownListener> showListeners;
    private List<RecipeCollection> recipeCollections;
    private StateSwitchingButton forwardButton;
    private StateSwitchingButton backButton;
    private int totalPages;
    private int currentPage;
    private RecipeBook recipeBook;
    private Recipe<?> lastClickedRecipe;
    private RecipeCollection lastClickedRecipeCollection;
    
    public RecipeBookPage() {
        this.buttons = (List<RecipeButton>)Lists.newArrayListWithCapacity(20);
        this.overlay = new OverlayRecipeComponent();
        this.showListeners = (List<RecipeShownListener>)Lists.newArrayList();
        for (int integer2 = 0; integer2 < 20; ++integer2) {
            this.buttons.add(new RecipeButton());
        }
    }
    
    public void init(final Minecraft djw, final int integer2, final int integer3) {
        this.minecraft = djw;
        this.recipeBook = djw.player.getRecipeBook();
        for (int integer4 = 0; integer4 < this.buttons.size(); ++integer4) {
            ((RecipeButton)this.buttons.get(integer4)).setPosition(integer2 + 11 + 25 * (integer4 % 5), integer3 + 31 + 25 * (integer4 / 5));
        }
        (this.forwardButton = new StateSwitchingButton(integer2 + 93, integer3 + 137, 12, 17, false)).initTextureValues(1, 208, 13, 18, RecipeBookComponent.RECIPE_BOOK_LOCATION);
        (this.backButton = new StateSwitchingButton(integer2 + 38, integer3 + 137, 12, 17, true)).initTextureValues(1, 208, 13, 18, RecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    public void addListener(final RecipeBookComponent drm) {
        this.showListeners.remove(drm);
        this.showListeners.add(drm);
    }
    
    public void updateCollections(final List<RecipeCollection> list, final boolean boolean2) {
        this.recipeCollections = list;
        this.totalPages = (int)Math.ceil(list.size() / 20.0);
        if (this.totalPages <= this.currentPage || boolean2) {
            this.currentPage = 0;
        }
        this.updateButtonsForPage();
    }
    
    private void updateButtonsForPage() {
        final int integer2 = 20 * this.currentPage;
        for (int integer3 = 0; integer3 < this.buttons.size(); ++integer3) {
            final RecipeButton drp4 = (RecipeButton)this.buttons.get(integer3);
            if (integer2 + integer3 < this.recipeCollections.size()) {
                final RecipeCollection drq5 = (RecipeCollection)this.recipeCollections.get(integer2 + integer3);
                drp4.init(drq5, this);
                drp4.visible = true;
            }
            else {
                drp4.visible = false;
            }
        }
        this.updateArrowButtons();
    }
    
    private void updateArrowButtons() {
        this.forwardButton.visible = (this.totalPages > 1 && this.currentPage < this.totalPages - 1);
        this.backButton.visible = (this.totalPages > 1 && this.currentPage > 0);
    }
    
    public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final float float6) {
        if (this.totalPages > 1) {
            final String string8 = new StringBuilder().append(this.currentPage + 1).append("/").append(this.totalPages).toString();
            final int integer6 = this.minecraft.font.width(string8);
            this.minecraft.font.draw(dfj, string8, (float)(integer2 - integer6 / 2 + 73), (float)(integer3 + 141), -1);
        }
        this.hoveredButton = null;
        for (final RecipeButton drp9 : this.buttons) {
            drp9.render(dfj, integer4, integer5, float6);
            if (drp9.visible && drp9.isHovered()) {
                this.hoveredButton = drp9;
            }
        }
        this.backButton.render(dfj, integer4, integer5, float6);
        this.forwardButton.render(dfj, integer4, integer5, float6);
        this.overlay.render(dfj, integer4, integer5, float6);
    }
    
    public void renderTooltip(final PoseStack dfj, final int integer2, final int integer3) {
        if (this.minecraft.screen != null && this.hoveredButton != null && !this.overlay.isVisible()) {
            this.minecraft.screen.renderComponentTooltip(dfj, this.hoveredButton.getTooltipText(this.minecraft.screen), integer2, integer3);
        }
    }
    
    @Nullable
    public Recipe<?> getLastClickedRecipe() {
        return this.lastClickedRecipe;
    }
    
    @Nullable
    public RecipeCollection getLastClickedRecipeCollection() {
        return this.lastClickedRecipeCollection;
    }
    
    public void setInvisible() {
        this.overlay.setVisible(false);
    }
    
    public boolean mouseClicked(final double double1, final double double2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        this.lastClickedRecipe = null;
        this.lastClickedRecipeCollection = null;
        if (this.overlay.isVisible()) {
            if (this.overlay.mouseClicked(double1, double2, integer3)) {
                this.lastClickedRecipe = this.overlay.getLastRecipeClicked();
                this.lastClickedRecipeCollection = this.overlay.getRecipeCollection();
            }
            else {
                this.overlay.setVisible(false);
            }
            return true;
        }
        if (this.forwardButton.mouseClicked(double1, double2, integer3)) {
            ++this.currentPage;
            this.updateButtonsForPage();
            return true;
        }
        if (this.backButton.mouseClicked(double1, double2, integer3)) {
            --this.currentPage;
            this.updateButtonsForPage();
            return true;
        }
        for (final RecipeButton drp12 : this.buttons) {
            if (drp12.mouseClicked(double1, double2, integer3)) {
                if (integer3 == 0) {
                    this.lastClickedRecipe = drp12.getRecipe();
                    this.lastClickedRecipeCollection = drp12.getCollection();
                }
                else if (integer3 == 1 && !this.overlay.isVisible() && !drp12.isOnlyOption()) {
                    this.overlay.init(this.minecraft, drp12.getCollection(), drp12.x, drp12.y, integer4 + integer6 / 2, integer5 + 13 + integer7 / 2, (float)drp12.getWidth());
                }
                return true;
            }
        }
        return false;
    }
    
    public void recipesShown(final List<Recipe<?>> list) {
        for (final RecipeShownListener drr4 : this.showListeners) {
            drr4.recipesShown(list);
        }
    }
    
    public Minecraft getMinecraft() {
        return this.minecraft;
    }
    
    public RecipeBook getRecipeBook() {
        return this.recipeBook;
    }
}
