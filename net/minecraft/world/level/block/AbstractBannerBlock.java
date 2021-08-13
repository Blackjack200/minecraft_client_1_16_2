package net.minecraft.world.level.block;

import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.DyeColor;

public abstract class AbstractBannerBlock extends BaseEntityBlock {
    private final DyeColor color;
    
    protected AbstractBannerBlock(final DyeColor bku, final Properties c) {
        super(c);
        this.color = bku;
    }
    
    @Override
    public boolean isPossibleToRespawnInThis() {
        return true;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BannerBlockEntity(this.color);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof BannerBlockEntity) {
                ((BannerBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        final BlockEntity ccg5 = bqz.getBlockEntity(fx);
        if (ccg5 instanceof BannerBlockEntity) {
            return ((BannerBlockEntity)ccg5).getItem(cee);
        }
        return super.getCloneItemStack(bqz, fx, cee);
    }
    
    public DyeColor getColor() {
        return this.color;
    }
}
