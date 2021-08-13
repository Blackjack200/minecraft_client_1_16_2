package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PumpkinBlock extends StemGrownBlock {
    protected PumpkinBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (bly8.getItem() == Items.SHEARS) {
            if (!bru.isClientSide) {
                final Direction gc9 = dcg.getDirection();
                final Direction gc10 = (gc9.getAxis() == Direction.Axis.Y) ? bft.getDirection().getOpposite() : gc9;
                bru.playSound(null, fx, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0f, 1.0f);
                bru.setBlock(fx, ((StateHolder<O, BlockState>)Blocks.CARVED_PUMPKIN.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)CarvedPumpkinBlock.FACING, gc10), 11);
                final ItemEntity bcs11 = new ItemEntity(bru, fx.getX() + 0.5 + gc10.getStepX() * 0.65, fx.getY() + 0.1, fx.getZ() + 0.5 + gc10.getStepZ() * 0.65, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                bcs11.setDeltaMovement(0.05 * gc10.getStepX() + bru.random.nextDouble() * 0.02, 0.05, 0.05 * gc10.getStepZ() + bru.random.nextDouble() * 0.02);
                bru.addFreshEntity(bcs11);
                bly8.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return super.use(cee, bru, fx, bft, aoq, dcg);
    }
    
    @Override
    public StemBlock getStem() {
        return (StemBlock)Blocks.PUMPKIN_STEM;
    }
    
    @Override
    public AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock)Blocks.ATTACHED_PUMPKIN_STEM;
    }
}
