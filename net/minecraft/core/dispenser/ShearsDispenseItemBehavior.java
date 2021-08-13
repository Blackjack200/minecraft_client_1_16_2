package net.minecraft.core.dispenser;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Shearable;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;

public class ShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
        final Level bru4 = fy.getLevel();
        if (!bru4.isClientSide()) {
            final BlockPos fx5 = fy.getPos().relative(fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
            this.setSuccess(tryShearBeehive((ServerLevel)bru4, fx5) || tryShearLivingEntity((ServerLevel)bru4, fx5));
            if (this.isSuccess() && bly.hurt(1, bru4.getRandom(), null)) {
                bly.setCount(0);
            }
        }
        return bly;
    }
    
    private static boolean tryShearBeehive(final ServerLevel aag, final BlockPos fx) {
        final BlockState cee3 = aag.getBlockState(fx);
        if (cee3.is(BlockTags.BEEHIVES)) {
            final int integer4 = cee3.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL);
            if (integer4 >= 5) {
                aag.playSound(null, fx, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0f, 1.0f);
                BeehiveBlock.dropHoneycomb(aag, fx);
                ((BeehiveBlock)cee3.getBlock()).releaseBeesAndResetHoneyLevel(aag, cee3, fx, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
                return true;
            }
        }
        return false;
    }
    
    private static boolean tryShearLivingEntity(final ServerLevel aag, final BlockPos fx) {
        final List<LivingEntity> list3 = aag.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, new AABB(fx), (java.util.function.Predicate<? super LivingEntity>)EntitySelector.NO_SPECTATORS);
        for (final LivingEntity aqj5 : list3) {
            if (aqj5 instanceof Shearable) {
                final Shearable aqy6 = (Shearable)aqj5;
                if (aqy6.readyForShearing()) {
                    aqy6.shear(SoundSource.BLOCKS);
                    return true;
                }
                continue;
            }
        }
        return false;
    }
}
