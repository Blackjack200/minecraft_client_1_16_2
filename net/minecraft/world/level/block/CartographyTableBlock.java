package net.minecraft.world.level.block;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import javax.annotation.Nullable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.MenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.network.chat.Component;

public class CartographyTableBlock extends Block {
    private static final Component CONTAINER_TITLE;
    
    protected CartographyTableBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        bft.awardStat(Stats.INTERACT_WITH_CARTOGRAPHY_TABLE);
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return new SimpleMenuProvider((integer, bfs, bft) -> new CartographyTableMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), CartographyTableBlock.CONTAINER_TITLE);
    }
    
    static {
        CONTAINER_TITLE = new TranslatableComponent("container.cartography_table");
    }
}
