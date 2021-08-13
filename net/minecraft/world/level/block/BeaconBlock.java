package net.minecraft.world.level.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BeaconBlock extends BaseEntityBlock implements BeaconBeamBlock {
    public BeaconBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public DyeColor getColor() {
        return DyeColor.WHITE;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BeaconBlockEntity();
    }
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof BeaconBlockEntity) {
            bft.openMenu((MenuProvider)ccg8);
            bft.awardStat(Stats.INTERACT_WITH_BEACON);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof BeaconBlockEntity) {
                ((BeaconBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
}
