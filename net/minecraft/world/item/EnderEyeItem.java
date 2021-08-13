package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.EndPortalFrameBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class EnderEyeItem extends Item {
    public EnderEyeItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (!cee5.is(Blocks.END_PORTAL_FRAME) || cee5.<Boolean>getValue((Property<Boolean>)EndPortalFrameBlock.HAS_EYE)) {
            return InteractionResult.PASS;
        }
        if (bru3.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockState cee6 = ((StateHolder<O, BlockState>)cee5).<Comparable, Boolean>setValue((Property<Comparable>)EndPortalFrameBlock.HAS_EYE, true);
        Block.pushEntitiesUp(cee5, cee6, bru3, fx4);
        bru3.setBlock(fx4, cee6, 2);
        bru3.updateNeighbourForOutputSignal(fx4, Blocks.END_PORTAL_FRAME);
        bnx.getItemInHand().shrink(1);
        bru3.levelEvent(1503, fx4, 0);
        final BlockPattern.BlockPatternMatch b7 = EndPortalFrameBlock.getOrCreatePortalShape().find(bru3, fx4);
        if (b7 != null) {
            final BlockPos fx5 = b7.getFrontTopLeft().offset(-3, 0, -3);
            for (int integer9 = 0; integer9 < 3; ++integer9) {
                for (int integer10 = 0; integer10 < 3; ++integer10) {
                    bru3.setBlock(fx5.offset(integer9, 0, integer10), Blocks.END_PORTAL.defaultBlockState(), 2);
                }
            }
            bru3.globalLevelEvent(1038, fx5.offset(1, 0, 1), 0);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final HitResult dci6 = Item.getPlayerPOVHitResult(bru, bft, ClipContext.Fluid.NONE);
        if (dci6.getType() == HitResult.Type.BLOCK && bru.getBlockState(((BlockHitResult)dci6).getBlockPos()).is(Blocks.END_PORTAL_FRAME)) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        bft.startUsingItem(aoq);
        if (bru instanceof ServerLevel) {
            final BlockPos fx7 = ((ServerLevel)bru).getChunkSource().getGenerator().findNearestMapFeature((ServerLevel)bru, StructureFeature.STRONGHOLD, bft.blockPosition(), 100, false);
            if (fx7 != null) {
                final EyeOfEnder bgc8 = new EyeOfEnder(bru, bft.getX(), bft.getY(0.5), bft.getZ());
                bgc8.setItem(bly5);
                bgc8.signalTo(fx7);
                bru.addFreshEntity(bgc8);
                if (bft instanceof ServerPlayer) {
                    CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer)bft, fx7);
                }
                bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5f, 0.4f / (EnderEyeItem.random.nextFloat() * 0.4f + 0.8f));
                bru.levelEvent(null, 1003, bft.blockPosition(), 0);
                if (!bft.abilities.instabuild) {
                    bly5.shrink(1);
                }
                bft.awardStat(Stats.ITEM_USED.get(this));
                bft.swing(aoq, true);
                return InteractionResultHolder.<ItemStack>success(bly5);
            }
        }
        return InteractionResultHolder.<ItemStack>consume(bly5);
    }
}
