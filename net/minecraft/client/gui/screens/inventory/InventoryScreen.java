package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.components.Button;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.math.Quaternion;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.InventoryMenu;

public class InventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION;
    private float xMouse;
    private float yMouse;
    private final RecipeBookComponent recipeBookComponent;
    private boolean recipeBookComponentInitialized;
    private boolean widthTooNarrow;
    private boolean buttonClicked;
    
    public InventoryScreen(final Player bft) {
        super(bft.inventoryMenu, bft.inventory, new TranslatableComponent("container.crafting"));
        this.recipeBookComponent = new RecipeBookComponent();
        this.passEvents = true;
        this.titleLabelX = 97;
    }
    
    @Override
    public void tick() {
        if (this.minecraft.gameMode.hasInfiniteItems()) {
            this.minecraft.setScreen(new CreativeModeInventoryScreen(this.minecraft.player));
            return;
        }
        this.recipeBookComponent.tick();
    }
    
    @Override
    protected void init() {
        if (this.minecraft.gameMode.hasInfiniteItems()) {
            this.minecraft.setScreen(new CreativeModeInventoryScreen(this.minecraft.player));
            return;
        }
        super.init();
        this.widthTooNarrow = (this.width < 379);
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.recipeBookComponentInitialized = true;
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
        this.children.add(this.recipeBookComponent);
        this.setInitialFocus(this.recipeBookComponent);
        this.<ImageButton>addButton(new ImageButton(this.leftPos + 104, this.height / 2 - 22, 20, 18, 0, 0, 19, InventoryScreen.RECIPE_BUTTON_LOCATION, dlg -> {
            this.recipeBookComponent.initVisuals(this.widthTooNarrow);
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
            dlg.setPosition(this.leftPos + 104, this.height / 2 - 22);
            this.buttonClicked = true;
        }));
    }
    
    @Override
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        this.font.draw(dfj, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.doRenderEffects = !this.recipeBookComponent.isVisible();
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(dfj, float4, integer2, integer3);
            this.recipeBookComponent.render(dfj, integer2, integer3, float4);
        }
        else {
            this.recipeBookComponent.render(dfj, integer2, integer3, float4);
            super.render(dfj, integer2, integer3, float4);
            this.recipeBookComponent.renderGhostRecipe(dfj, this.leftPos, this.topPos, false, float4);
        }
        this.renderTooltip(dfj, integer2, integer3);
        this.recipeBookComponent.renderTooltip(dfj, this.leftPos, this.topPos, integer2, integer3);
        this.xMouse = (float)integer2;
        this.yMouse = (float)integer3;
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(InventoryScreen.INVENTORY_LOCATION);
        final int integer5 = this.leftPos;
        final int integer6 = this.topPos;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        renderEntityInInventory(integer5 + 51, integer6 + 75, 30, integer5 + 51 - this.xMouse, integer6 + 75 - 50 - this.yMouse, this.minecraft.player);
    }
    
    public static void renderEntityInInventory(final int integer1, final int integer2, final int integer3, final float float4, final float float5, final LivingEntity aqj) {
        final float float6 = (float)Math.atan((double)(float4 / 40.0f));
        final float float7 = (float)Math.atan((double)(float5 / 40.0f));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)integer1, (float)integer2, 1050.0f);
        RenderSystem.scalef(1.0f, 1.0f, -1.0f);
        final PoseStack dfj9 = new PoseStack();
        dfj9.translate(0.0, 0.0, 1000.0);
        dfj9.scale((float)integer3, (float)integer3, (float)integer3);
        final Quaternion d10 = Vector3f.ZP.rotationDegrees(180.0f);
        final Quaternion d11 = Vector3f.XP.rotationDegrees(float7 * 20.0f);
        d10.mul(d11);
        dfj9.mulPose(d10);
        final float float8 = aqj.yBodyRot;
        final float float9 = aqj.yRot;
        final float float10 = aqj.xRot;
        final float float11 = aqj.yHeadRotO;
        final float float12 = aqj.yHeadRot;
        aqj.yBodyRot = 180.0f + float6 * 20.0f;
        aqj.yRot = 180.0f + float6 * 40.0f;
        aqj.xRot = -float7 * 20.0f;
        aqj.yHeadRot = aqj.yRot;
        aqj.yHeadRotO = aqj.yRot;
        final EntityRenderDispatcher eel17 = Minecraft.getInstance().getEntityRenderDispatcher();
        d11.conj();
        eel17.overrideCameraOrientation(d11);
        eel17.setRenderShadow(false);
        final MultiBufferSource.BufferSource a18 = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> eel17.<LivingEntity>render(aqj, 0.0, 0.0, 0.0, 0.0f, 1.0f, dfj9, a18, 15728880));
        a18.endBatch();
        eel17.setRenderShadow(true);
        aqj.yBodyRot = float8;
        aqj.yRot = float9;
        aqj.xRot = float10;
        aqj.yHeadRotO = float11;
        aqj.yHeadRot = float12;
        RenderSystem.popMatrix();
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
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public boolean mouseReleased(final double double1, final double double2, final int integer) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        }
        return super.mouseReleased(double1, double2, integer);
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
        if (this.recipeBookComponentInitialized) {
            this.recipeBookComponent.removed();
        }
        super.removed();
    }
    
    @Override
    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
    
    static {
        RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    }
}
