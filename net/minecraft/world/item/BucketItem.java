package net.minecraft.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FlowingFluid;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

public class BucketItem extends Item {
    private final Fluid content;
    
    public BucketItem(final Fluid cut, final Properties a) {
        super(a);
        this.content = cut;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final HitResult dci6 = Item.getPlayerPOVHitResult(bru, bft, (this.content == Fluids.EMPTY) ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (dci6.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        if (dci6.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        final BlockHitResult dcg7 = (BlockHitResult)dci6;
        final BlockPos fx8 = dcg7.getBlockPos();
        final Direction gc9 = dcg7.getDirection();
        final BlockPos fx9 = fx8.relative(gc9);
        if (!bru.mayInteract(bft, fx8) || !bft.mayUseItemAt(fx9, gc9, bly5)) {
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        if (this.content == Fluids.EMPTY) {
            final BlockState cee11 = bru.getBlockState(fx8);
            if (cee11.getBlock() instanceof BucketPickup) {
                final Fluid cut12 = ((BucketPickup)cee11.getBlock()).takeLiquid(bru, fx8, cee11);
                if (cut12 != Fluids.EMPTY) {
                    bft.awardStat(Stats.ITEM_USED.get(this));
                    bft.playSound(cut12.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
                    final ItemStack bly6 = ItemUtils.createFilledResult(bly5, bft, new ItemStack(cut12.getBucket()));
                    if (!bru.isClientSide) {
                        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)bft, new ItemStack(cut12.getBucket()));
                    }
                    return InteractionResultHolder.<ItemStack>sidedSuccess(bly6, bru.isClientSide());
                }
            }
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        final BlockState cee11 = bru.getBlockState(fx8);
        final BlockPos fx10 = (cee11.getBlock() instanceof LiquidBlockContainer && this.content == Fluids.WATER) ? fx8 : fx9;
        if (this.emptyBucket(bft, bru, fx10, dcg7)) {
            this.checkExtraContent(bru, bly5, fx10);
            if (bft instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)bft, fx10, bly5);
            }
            bft.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.<ItemStack>sidedSuccess(this.getEmptySuccessItem(bly5, bft), bru.isClientSide());
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
    
    protected ItemStack getEmptySuccessItem(final ItemStack bly, final Player bft) {
        if (!bft.abilities.instabuild) {
            return new ItemStack(Items.BUCKET);
        }
        return bly;
    }
    
    public void checkExtraContent(final Level bru, final ItemStack bly, final BlockPos fx) {
    }
    
    public boolean emptyBucket(@Nullable final Player bft, final Level bru, final BlockPos fx, @Nullable final BlockHitResult dcg) {
        if (!(this.content instanceof FlowingFluid)) {
            return false;
        }
        final BlockState cee6 = bru.getBlockState(fx);
        final Block bul7 = cee6.getBlock();
        final Material cux8 = cee6.getMaterial();
        final boolean boolean9 = cee6.canBeReplaced(this.content);
        final boolean boolean10 = cee6.isAir() || boolean9 || (bul7 instanceof LiquidBlockContainer && ((LiquidBlockContainer)bul7).canPlaceLiquid(bru, fx, cee6, this.content));
        if (!boolean10) {
            return dcg != null && this.emptyBucket(bft, bru, dcg.getBlockPos().relative(dcg.getDirection()), null);
        }
        if (bru.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER)) {
            final int integer11 = fx.getX();
            final int integer12 = fx.getY();
            final int integer13 = fx.getZ();
            bru.playSound(bft, fx, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (bru.random.nextFloat() - bru.random.nextFloat()) * 0.8f);
            for (int integer14 = 0; integer14 < 8; ++integer14) {
                bru.addParticle(ParticleTypes.LARGE_SMOKE, integer11 + Math.random(), integer12 + Math.random(), integer13 + Math.random(), 0.0, 0.0, 0.0);
            }
            return true;
        }
        if (bul7 instanceof LiquidBlockContainer && this.content == Fluids.WATER) {
            ((LiquidBlockContainer)bul7).placeLiquid(bru, fx, cee6, ((FlowingFluid)this.content).getSource(false));
            this.playEmptySound(bft, bru, fx);
            return true;
        }
        if (!bru.isClientSide && boolean9 && !cux8.isLiquid()) {
            bru.destroyBlock(fx, true);
        }
        if (bru.setBlock(fx, this.content.defaultFluidState().createLegacyBlock(), 11) || cee6.getFluidState().isSource()) {
            this.playEmptySound(bft, bru, fx);
            return true;
        }
        return false;
    }
    
    protected void playEmptySound(@Nullable final Player bft, final LevelAccessor brv, final BlockPos fx) {
        final SoundEvent adn5 = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        brv.playSound(bft, fx, adn5, SoundSource.BLOCKS, 1.0f, 1.0f);
    }
}
