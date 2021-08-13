package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.AbstractFurnaceMenu;

public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION;
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private final ResourceLocation texture;
    
    public AbstractFurnaceScreen(final T bia, final AbstractFurnaceRecipeBookComponent dri, final Inventory bfs, final Component nr, final ResourceLocation vk) {
        super(bia, bfs, nr);
        this.recipeBookComponent = dri;
        this.texture = vk;
    }
    
    public void init() {
        super.init();
        this.widthTooNarrow = (this.width < 379);
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
        this.<ImageButton>addButton(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, AbstractFurnaceScreen.RECIPE_BUTTON_LOCATION, dlg -> {
            this.recipeBookComponent.initVisuals(this.widthTooNarrow);
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
            dlg.setPosition(this.leftPos + 20, this.height / 2 - 49);
            return;
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.recipeBookComponent.tick();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(dfj, float4, integer2, integer3);
            this.recipeBookComponent.render(dfj, integer2, integer3, float4);
        }
        else {
            this.recipeBookComponent.render(dfj, integer2, integer3, float4);
            super.render(dfj, integer2, integer3, float4);
            this.recipeBookComponent.renderGhostRecipe(dfj, this.leftPos, this.topPos, true, float4);
        }
        this.renderTooltip(dfj, integer2, integer3);
        this.recipeBookComponent.renderTooltip(dfj, this.leftPos, this.topPos, integer2, integer3);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(this.texture);
        final int integer5 = this.leftPos;
        final int integer6 = this.topPos;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isLit()) {
            final int integer7 = this.menu.getLitProgress();
            this.blit(dfj, integer5 + 56, integer6 + 36 + 12 - integer7, 176, 12 - integer7, 14, integer7 + 1);
        }
        final int integer7 = this.menu.getBurnProgress();
        this.blit(dfj, integer5 + 79, integer6 + 34, 176, 14, integer7 + 1, 16);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        return this.recipeBookComponent.mouseClicked(double1, double2, integer) || (this.widthTooNarrow && this.recipeBookComponent.isVisible()) || super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    protected void slotClicked(final Slot bjo, final int integer2, final int integer3, final ClickType bih) {
        super.slotClicked(bjo, integer2, integer3, bih);
        this.recipeBookComponent.slotClicked(bjo);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        return !this.recipeBookComponent.keyPressed(integer1, integer2, integer3) && super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    protected boolean hasClickedOutside(final double double1, final double double2, final int integer3, final int integer4, final int integer5) {
        final boolean boolean9 = double1 < integer3 || double2 < integer4 || double1 >= integer3 + this.imageWidth || double2 >= integer4 + this.imageHeight;
        return this.recipeBookComponent.hasClickedOutside(double1, double2, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, integer5) && boolean9;
    }
    
    public boolean charTyped(final char character, final int integer) {
        return this.recipeBookComponent.charTyped(character, integer) || super.charTyped(character, integer);
    }
    
    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }
    
    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
    
    @Override
    public void removed() {
        this.recipeBookComponent.removed();
        super.removed();
    }
    
    static {
        RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    }
}
