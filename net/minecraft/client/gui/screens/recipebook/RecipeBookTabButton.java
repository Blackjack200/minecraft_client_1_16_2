package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.components.StateSwitchingButton;

public class RecipeBookTabButton extends StateSwitchingButton {
    private final RecipeBookCategories category;
    private float animationTime;
    
    public RecipeBookTabButton(final RecipeBookCategories dkd) {
        super(0, 0, 35, 27, false);
        this.category = dkd;
        this.initTextureValues(153, 2, 35, 0, RecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    public void startAnimation(final Minecraft djw) {
        final ClientRecipeBook djj3 = djw.player.getRecipeBook();
        final List<RecipeCollection> list4 = djj3.getCollection(this.category);
        if (!(djw.player.containerMenu instanceof RecipeBookMenu)) {
            return;
        }
        for (final RecipeCollection drq6 : list4) {
            for (final Recipe<?> bon8 : drq6.getRecipes(djj3.isFiltering(djw.player.containerMenu))) {
                if (djj3.willHighlight(bon8)) {
                    this.animationTime = 15.0f;
                }
            }
        }
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.animationTime > 0.0f) {
            final float float5 = 1.0f + 0.1f * (float)Math.sin((double)(this.animationTime / 15.0f * 3.1415927f));
            RenderSystem.pushMatrix();
            RenderSystem.translatef((float)(this.x + 8), (float)(this.y + 12), 0.0f);
            RenderSystem.scalef(1.0f, float5, 1.0f);
            RenderSystem.translatef((float)(-(this.x + 8)), (float)(-(this.y + 12)), 0.0f);
        }
        final Minecraft djw6 = Minecraft.getInstance();
        djw6.getTextureManager().bind(this.resourceLocation);
        RenderSystem.disableDepthTest();
        int integer4 = this.xTexStart;
        int integer5 = this.yTexStart;
        if (this.isStateTriggered) {
            integer4 += this.xDiffTex;
        }
        if (this.isHovered()) {
            integer5 += this.yDiffTex;
        }
        int integer6 = this.x;
        if (this.isStateTriggered) {
            integer6 -= 2;
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.blit(dfj, integer6, this.y, integer4, integer5, this.width, this.height);
        RenderSystem.enableDepthTest();
        this.renderIcon(djw6.getItemRenderer());
        if (this.animationTime > 0.0f) {
            RenderSystem.popMatrix();
            this.animationTime -= float4;
        }
    }
    
    private void renderIcon(final ItemRenderer efg) {
        final List<ItemStack> list3 = this.category.getIconItems();
        final int integer4 = this.isStateTriggered ? -2 : 0;
        if (list3.size() == 1) {
            efg.renderAndDecorateFakeItem((ItemStack)list3.get(0), this.x + 9 + integer4, this.y + 5);
        }
        else if (list3.size() == 2) {
            efg.renderAndDecorateFakeItem((ItemStack)list3.get(0), this.x + 3 + integer4, this.y + 5);
            efg.renderAndDecorateFakeItem((ItemStack)list3.get(1), this.x + 14 + integer4, this.y + 5);
        }
    }
    
    public RecipeBookCategories getCategory() {
        return this.category;
    }
    
    public boolean updateVisibility(final ClientRecipeBook djj) {
        final List<RecipeCollection> list3 = djj.getCollection(this.category);
        this.visible = false;
        if (list3 != null) {
            for (final RecipeCollection drq5 : list3) {
                if (drq5.hasKnownRecipes() && drq5.hasFitting()) {
                    this.visible = true;
                    break;
                }
            }
        }
        return this.visible;
    }
}
