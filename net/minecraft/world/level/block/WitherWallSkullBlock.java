package net.minecraft.world.level.block;

import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WitherWallSkullBlock extends WallSkullBlock {
    protected WitherWallSkullBlock(final Properties c) {
        super(SkullBlock.Types.WITHER_SKELETON, c);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        Blocks.WITHER_SKELETON_SKULL.setPlacedBy(bru, fx, cee, aqj, bly);
    }
}
