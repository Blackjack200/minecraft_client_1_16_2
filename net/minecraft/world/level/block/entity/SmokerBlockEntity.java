package net.minecraft.world.level.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SmokerBlockEntity extends AbstractFurnaceBlockEntity {
    public SmokerBlockEntity() {
        super(BlockEntityType.SMOKER, RecipeType.SMOKING);
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.smoker");
    }
    
    @Override
    protected int getBurnDuration(final ItemStack bly) {
        return super.getBurnDuration(bly) / 2;
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new SmokerMenu(integer, bfs, this, this.dataAccess);
    }
}
