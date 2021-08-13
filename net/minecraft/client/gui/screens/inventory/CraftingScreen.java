package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.components.Button;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.CraftingMenu;

public class CraftingScreen extends AbstractContainerScreen<CraftingMenu> implements RecipeUpdateListener {
    private static final ResourceLocation CRAFTING_TABLE_LOCATION;
    private static final ResourceLocation RECIPE_BUTTON_LOCATION;
    private final RecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    
    public CraftingScreen(final CraftingMenu bim, final Inventory bfs, final Component nr) {
        super(bim, bfs, nr);
        this.recipeBookComponent = new RecipeBookComponent();
    }
    
    @Override
    protected void init() {
        super.init();
        this.widthTooNarrow = (this.width < 379);
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
        this.children.add(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.<ImageButton>addButton(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, CraftingScreen.RECIPE_BUTTON_LOCATION, dlg -> {
            this.recipeBookComponent.initVisuals(this.widthTooNarrow);
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
            dlg.setPosition(this.leftPos + 5, this.height / 2 - 49);
            return;
        }));
        this.titleLabelX = 29;
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
        this.minecraft.getTextureManager().bind(CraftingScreen.CRAFTING_TABLE_LOCATION);
        final int integer5 = this.leftPos;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
    }
    
    @Override
    protected boolean isHovering(final int integer1, final int integer2, final int integer3, final int integer4, final double double5, final double double6) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(integer1, integer2, integer3, integer4, double5, double6);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.recipeBookComponent.mouseClicked(double1, double2, integer)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        }
        return (this.widthTooNarrow && this.recipeBookComponent.isVisible()) || super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    protected boolean hasClickedOutside(final double double1, final double double2, final int integer3, final int integer4, final int integer5) {
        final boolean boolean9 = double1 < integer3 || double2 < integer4 || double1 >= integer3 + this.imageWidth || double2 >= integer4 + this.imageHeight;
        return this.recipeBookComponent.hasClickedOutside(double1, double2, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, integer5) && boolean9;
    }
    
    @Override
    protected void slotClicked(final Slot bjo, final int integer2, final int integer3, final ClickType bih) {
        super.slotClicked(bjo, integer2, integer3, bih);
        this.recipeBookComponent.slotClicked(bjo);
    }
    
    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }
    
    @Override
    public void removed() {
        this.recipeBookComponent.removed();
        super.removed();
    }
    
    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
    
    static {
        CRAFTING_TABLE_LOCATION = new ResourceLocation("textures/gui/container/crafting_table.png");
        RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    }
}
