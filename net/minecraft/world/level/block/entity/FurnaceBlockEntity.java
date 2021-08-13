package net.minecraft.world.level.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class FurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public FurnaceBlockEntity() {
        super(BlockEntityType.FURNACE, RecipeType.SMELTING);
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.furnace");
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new FurnaceMenu(integer, bfs, this, this.dataAccess);
    }
}
