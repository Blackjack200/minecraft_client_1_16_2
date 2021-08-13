package net.minecraft.world.entity.projectile;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.effect.MobEffect;
import javax.annotation.Nullable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class ThrownPotion extends ThrowableItemProjectile implements ItemSupplier {
    public static final Predicate<LivingEntity> WATER_SENSITIVE;
    
    public ThrownPotion(final EntityType<? extends ThrownPotion> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ThrownPotion(final Level bru, final LivingEntity aqj) {
        super(EntityType.POTION, aqj, bru);
    }
    
    public ThrownPotion(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.POTION, double2, double3, double4, bru);
    }
    
    @Override
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }
    
    @Override
    protected float getGravity() {
        return 0.05f;
    }
    
    protected void onHitBlock(final BlockHitResult dcg) {
        super.onHitBlock(dcg);
        if (this.level.isClientSide) {
            return;
        }
        final ItemStack bly3 = this.getItem();
        final Potion bnq4 = PotionUtils.getPotion(bly3);
        final List<MobEffectInstance> list5 = PotionUtils.getMobEffects(bly3);
        final boolean boolean6 = bnq4 == Potions.WATER && list5.isEmpty();
        final Direction gc7 = dcg.getDirection();
        final BlockPos fx8 = dcg.getBlockPos();
        final BlockPos fx9 = fx8.relative(gc7);
        if (boolean6) {
            this.dowseFire(fx9, gc7);
            this.dowseFire(fx9.relative(gc7.getOpposite()), gc7);
            for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
                this.dowseFire(fx9.relative(gc8), gc8);
            }
        }
    }
    
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        if (this.level.isClientSide) {
            return;
        }
        final ItemStack bly3 = this.getItem();
        final Potion bnq4 = PotionUtils.getPotion(bly3);
        final List<MobEffectInstance> list5 = PotionUtils.getMobEffects(bly3);
        final boolean boolean6 = bnq4 == Potions.WATER && list5.isEmpty();
        if (boolean6) {
            this.applyWater();
        }
        else if (!list5.isEmpty()) {
            if (this.isLingering()) {
                this.makeAreaOfEffectCloud(bly3, bnq4);
            }
            else {
                this.applySplash(list5, (dci.getType() == HitResult.Type.ENTITY) ? ((EntityHitResult)dci).getEntity() : null);
            }
        }
        final int integer7 = bnq4.hasInstantEffects() ? 2007 : 2002;
        this.level.levelEvent(integer7, this.blockPosition(), PotionUtils.getColor(bly3));
        this.remove();
    }
    
    private void applyWater() {
        final AABB dcf2 = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        final List<LivingEntity> list3 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, dcf2, (java.util.function.Predicate<? super LivingEntity>)ThrownPotion.WATER_SENSITIVE);
        if (!list3.isEmpty()) {
            for (final LivingEntity aqj5 : list3) {
                final double double6 = this.distanceToSqr(aqj5);
                if (double6 < 16.0 && aqj5.isSensitiveToWater()) {
                    aqj5.hurt(DamageSource.indirectMagic(aqj5, this.getOwner()), 1.0f);
                }
            }
        }
    }
    
    private void applySplash(final List<MobEffectInstance> list, @Nullable final Entity apx) {
        final AABB dcf4 = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        final List<LivingEntity> list2 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, dcf4);
        if (!list2.isEmpty()) {
            for (final LivingEntity aqj7 : list2) {
                if (!aqj7.isAffectedByPotions()) {
                    continue;
                }
                final double double8 = this.distanceToSqr(aqj7);
                if (double8 >= 16.0) {
                    continue;
                }
                double double9 = 1.0 - Math.sqrt(double8) / 4.0;
                if (aqj7 == apx) {
                    double9 = 1.0;
                }
                for (final MobEffectInstance apr13 : list) {
                    final MobEffect app14 = apr13.getEffect();
                    if (app14.isInstantenous()) {
                        app14.applyInstantenousEffect(this, this.getOwner(), aqj7, apr13.getAmplifier(), double9);
                    }
                    else {
                        final int integer15 = (int)(double9 * apr13.getDuration() + 0.5);
                        if (integer15 <= 20) {
                            continue;
                        }
                        aqj7.addEffect(new MobEffectInstance(app14, integer15, apr13.getAmplifier(), apr13.isAmbient(), apr13.isVisible()));
                    }
                }
            }
        }
    }
    
    private void makeAreaOfEffectCloud(final ItemStack bly, final Potion bnq) {
        final AreaEffectCloud apw4 = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
        final Entity apx5 = this.getOwner();
        if (apx5 instanceof LivingEntity) {
            apw4.setOwner((LivingEntity)apx5);
        }
        apw4.setRadius(3.0f);
        apw4.setRadiusOnUse(-0.5f);
        apw4.setWaitTime(10);
        apw4.setRadiusPerTick(-apw4.getRadius() / apw4.getDuration());
        apw4.setPotion(bnq);
        for (final MobEffectInstance apr7 : PotionUtils.getCustomEffects(bly)) {
            apw4.addEffect(new MobEffectInstance(apr7));
        }
        final CompoundTag md6 = bly.getTag();
        if (md6 != null && md6.contains("CustomPotionColor", 99)) {
            apw4.setFixedColor(md6.getInt("CustomPotionColor"));
        }
        this.level.addFreshEntity(apw4);
    }
    
    private boolean isLingering() {
        return this.getItem().getItem() == Items.LINGERING_POTION;
    }
    
    private void dowseFire(final BlockPos fx, final Direction gc) {
        final BlockState cee4 = this.level.getBlockState(fx);
        if (cee4.is(BlockTags.FIRE)) {
            this.level.removeBlock(fx, false);
        }
        else if (CampfireBlock.isLitCampfire(cee4)) {
            this.level.levelEvent(null, 1009, fx, 0);
            CampfireBlock.dowse(this.level, fx, cee4);
            this.level.setBlockAndUpdate(fx, ((StateHolder<O, BlockState>)cee4).<Comparable, Boolean>setValue((Property<Comparable>)CampfireBlock.LIT, false));
        }
    }
    
    static {
        WATER_SENSITIVE = LivingEntity::isSensitiveToWater;
    }
}
