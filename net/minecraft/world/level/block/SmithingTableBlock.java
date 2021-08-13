package net.minecraft.world.level.block;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.network.chat.Component;

public class SmithingTableBlock extends CraftingTableBlock {
    private static final Component CONTAINER_TITLE;
    
    protected SmithingTableBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return new SimpleMenuProvider((integer, bfs, bft) -> new SmithingMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), SmithingTableBlock.CONTAINER_TITLE);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        bft.awardStat(Stats.INTERACT_WITH_SMITHING_TABLE);
        return InteractionResult.CONSUME;
    }
    
    static {
        CONTAINER_TITLE = new TranslatableComponent("container.upgrade");
    }
}
