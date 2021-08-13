package net.minecraft.client.gui.components.toasts;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import net.minecraft.world.item.crafting.Recipe;
import java.util.List;
import net.minecraft.network.chat.Component;

public class RecipeToast implements Toast {
    private static final Component TITLE_TEXT;
    private static final Component DESCRIPTION_TEXT;
    private final List<Recipe<?>> recipes;
    private long lastChanged;
    private boolean changed;
    
    public RecipeToast(final Recipe<?> bon) {
        (this.recipes = (List<Recipe<?>>)Lists.newArrayList()).add(bon);
    }
    
    public Visibility render(final PoseStack dfj, final ToastComponent dmo, final long long3) {
        if (this.changed) {
            this.lastChanged = long3;
            this.changed = false;
        }
        if (this.recipes.isEmpty()) {
            return Visibility.HIDE;
        }
        dmo.getMinecraft().getTextureManager().bind(RecipeToast.TEXTURE);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        dmo.blit(dfj, 0, 0, 0, 32, this.width(), this.height());
        dmo.getMinecraft().font.draw(dfj, RecipeToast.TITLE_TEXT, 30.0f, 7.0f, -11534256);
        dmo.getMinecraft().font.draw(dfj, RecipeToast.DESCRIPTION_TEXT, 30.0f, 18.0f, -16777216);
        final Recipe<?> bon6 = this.recipes.get((int)(long3 / Math.max(1L, 5000L / this.recipes.size()) % this.recipes.size()));
        final ItemStack bly7 = bon6.getToastSymbol();
        RenderSystem.pushMatrix();
        RenderSystem.scalef(0.6f, 0.6f, 1.0f);
        dmo.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(bly7, 3, 3);
        RenderSystem.popMatrix();
        dmo.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(bon6.getResultItem(), 8, 8);
        return (long3 - this.lastChanged >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
    }
    
    private void addItem(final Recipe<?> bon) {
        this.recipes.add(bon);
        this.changed = true;
    }
    
    public static void addOrUpdate(final ToastComponent dmo, final Recipe<?> bon) {
        final RecipeToast dml3 = dmo.<RecipeToast>getToast((java.lang.Class<? extends RecipeToast>)RecipeToast.class, RecipeToast.NO_TOKEN);
        if (dml3 == null) {
            dmo.addToast(new RecipeToast(bon));
        }
        else {
            dml3.addItem(bon);
        }
    }
    
    static {
        TITLE_TEXT = new TranslatableComponent("recipe.toast.title");
        DESCRIPTION_TEXT = new TranslatableComponent("recipe.toast.description");
    }
}
