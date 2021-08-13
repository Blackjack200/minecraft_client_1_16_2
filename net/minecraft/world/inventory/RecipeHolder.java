package net.minecraft.world.inventory;

import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Recipe;

public interface RecipeHolder {
    void setRecipeUsed(@Nullable final Recipe<?> bon);
    
    @Nullable
    Recipe<?> getRecipeUsed();
    
    default void awardUsedRecipes(final Player bft) {
        final Recipe<?> bon3 = this.getRecipeUsed();
        if (bon3 != null && !bon3.isSpecial()) {
            bft.awardRecipes((Collection<Recipe<?>>)Collections.singleton(bon3));
            this.setRecipeUsed(null);
        }
    }
    
    default boolean setRecipeUsed(final Level bru, final ServerPlayer aah, final Recipe<?> bon) {
        if (bon.isSpecial() || !bru.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) || aah.getRecipeBook().contains(bon)) {
            this.setRecipeUsed(bon);
            return true;
        }
        return false;
    }
}
