package net.minecraft.world.level.block;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SpawnerBlock extends BaseEntityBlock {
    protected SpawnerBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new SpawnerBlockEntity();
    }
    
    @Override
    public void spawnAfterBreak(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
        super.spawnAfterBreak(cee, aag, fx, bly);
        final int integer6 = 15 + aag.random.nextInt(15) + aag.random.nextInt(15);
        this.popExperience(aag, fx, integer6);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return ItemStack.EMPTY;
    }
}
