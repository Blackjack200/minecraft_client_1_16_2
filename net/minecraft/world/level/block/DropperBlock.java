package net.minecraft.world.level.block;

import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.Container;
import net.minecraft.core.BlockSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.dispenser.DispenseItemBehavior;

public class DropperBlock extends DispenserBlock {
    private static final DispenseItemBehavior DISPENSE_BEHAVIOUR;
    
    public DropperBlock(final Properties c) {
        super(c);
    }
    
    @Override
    protected DispenseItemBehavior getDispenseMethod(final ItemStack bly) {
        return DropperBlock.DISPENSE_BEHAVIOUR;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new DropperBlockEntity();
    }
    
    @Override
    protected void dispenseFrom(final ServerLevel aag, final BlockPos fx) {
        final BlockSourceImpl fz4 = new BlockSourceImpl(aag, fx);
        final DispenserBlockEntity ccp5 = fz4.<DispenserBlockEntity>getEntity();
        final int integer6 = ccp5.getRandomSlot();
        if (integer6 < 0) {
            aag.levelEvent(1001, fx, 0);
            return;
        }
        final ItemStack bly7 = ccp5.getItem(integer6);
        if (bly7.isEmpty()) {
            return;
        }
        final Direction gc8 = aag.getBlockState(fx).<Direction>getValue((Property<Direction>)DropperBlock.FACING);
        final Container aok9 = HopperBlockEntity.getContainerAt(aag, fx.relative(gc8));
        ItemStack bly8;
        if (aok9 == null) {
            bly8 = DropperBlock.DISPENSE_BEHAVIOUR.dispense(fz4, bly7);
        }
        else {
            bly8 = HopperBlockEntity.addItem(ccp5, aok9, bly7.copy().split(1), gc8.getOpposite());
            if (bly8.isEmpty()) {
                bly8 = bly7.copy();
                bly8.shrink(1);
            }
            else {
                bly8 = bly7.copy();
            }
        }
        ccp5.setItem(integer6, bly8);
    }
    
    static {
        DISPENSE_BEHAVIOUR = new DefaultDispenseItemBehavior();
    }
}
