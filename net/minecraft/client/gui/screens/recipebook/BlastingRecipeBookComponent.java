package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.item.Item;
import java.util.Set;
import net.minecraft.network.chat.Component;

public class BlastingRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    private static final Component FILTER_NAME;
    
    @Override
    protected Component getRecipeFilterName() {
        return BlastingRecipeBookComponent.FILTER_NAME;
    }
    
    @Override
    protected Set<Item> getFuelItems() {
        return (Set<Item>)AbstractFurnaceBlockEntity.getFuel().keySet();
    }
    
    static {
        FILTER_NAME = new TranslatableComponent("gui.recipebook.toggleRecipes.blastable");
    }
}
