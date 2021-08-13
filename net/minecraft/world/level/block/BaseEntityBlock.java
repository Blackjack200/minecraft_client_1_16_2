package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class BaseEntityBlock extends Block implements EntityBlock {
    protected BaseEntityBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public boolean triggerEvent(final BlockState cee, final Level bru, final BlockPos fx, final int integer4, final int integer5) {
        super.triggerEvent(cee, bru, fx, integer4, integer5);
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        return ccg7 != null && ccg7.triggerEvent(integer4, integer5);
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        return (ccg5 instanceof MenuProvider) ? ccg5 : null;
    }
}
