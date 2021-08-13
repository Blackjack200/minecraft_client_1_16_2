package net.minecraft.world.item;

import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.function.Predicate;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BottleItem extends Item {
    public BottleItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final List<AreaEffectCloud> list5 = bru.<AreaEffectCloud>getEntitiesOfClass((java.lang.Class<? extends AreaEffectCloud>)AreaEffectCloud.class, bft.getBoundingBox().inflate(2.0), (java.util.function.Predicate<? super AreaEffectCloud>)(apw -> apw != null && apw.isAlive() && apw.getOwner() instanceof EnderDragon));
        final ItemStack bly6 = bft.getItemInHand(aoq);
        if (!list5.isEmpty()) {
            final AreaEffectCloud apw7 = (AreaEffectCloud)list5.get(0);
            apw7.setRadius(apw7.getRadius() - 0.5f);
            bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.BOTTLE_FILL_DRAGONBREATH, SoundSource.NEUTRAL, 1.0f, 1.0f);
            return InteractionResultHolder.<ItemStack>sidedSuccess(this.turnBottleIntoItem(bly6, bft, new ItemStack(Items.DRAGON_BREATH)), bru.isClientSide());
        }
        final HitResult dci7 = Item.getPlayerPOVHitResult(bru, bft, ClipContext.Fluid.SOURCE_ONLY);
        if (dci7.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.<ItemStack>pass(bly6);
        }
        if (dci7.getType() == HitResult.Type.BLOCK) {
            final BlockPos fx8 = ((BlockHitResult)dci7).getBlockPos();
            if (!bru.mayInteract(bft, fx8)) {
                return InteractionResultHolder.<ItemStack>pass(bly6);
            }
            if (bru.getFluidState(fx8).is(FluidTags.WATER)) {
                bru.playSound(bft, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0f, 1.0f);
                return InteractionResultHolder.<ItemStack>sidedSuccess(this.turnBottleIntoItem(bly6, bft, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)), bru.isClientSide());
            }
        }
        return InteractionResultHolder.<ItemStack>pass(bly6);
    }
    
    protected ItemStack turnBottleIntoItem(final ItemStack bly1, final Player bft, final ItemStack bly3) {
        bft.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(bly1, bft, bly3);
    }
}
