package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;

public class ThrownTrident extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY;
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private ItemStack tridentItem;
    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;
    
    public ThrownTrident(final EntityType<? extends ThrownTrident> aqb, final Level bru) {
        super(aqb, bru);
        this.tridentItem = new ItemStack(Items.TRIDENT);
    }
    
    public ThrownTrident(final Level bru, final LivingEntity aqj, final ItemStack bly) {
        super(EntityType.TRIDENT, aqj, bru);
        this.tridentItem = new ItemStack(Items.TRIDENT);
        this.tridentItem = bly.copy();
        this.entityData.<Byte>set(ThrownTrident.ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(bly));
        this.entityData.<Boolean>set(ThrownTrident.ID_FOIL, bly.hasFoil());
    }
    
    public ThrownTrident(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.TRIDENT, double2, double3, double4, bru);
        this.tridentItem = new ItemStack(Items.TRIDENT);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(ThrownTrident.ID_LOYALTY, (Byte)0);
        this.entityData.<Boolean>define(ThrownTrident.ID_FOIL, false);
    }
    
    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        final Entity apx2 = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && apx2 != null) {
            final int integer3 = this.entityData.<Byte>get(ThrownTrident.ID_LOYALTY);
            if (integer3 > 0 && !this.isAcceptibleReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1f);
                }
                this.remove();
            }
            else if (integer3 > 0) {
                this.setNoPhysics(true);
                final Vec3 dck4 = new Vec3(apx2.getX() - this.getX(), apx2.getEyeY() - this.getY(), apx2.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + dck4.y * 0.015 * integer3, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }
                final double double5 = 0.05 * integer3;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(dck4.normalize().scale(double5)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0f, 1.0f);
                }
                ++this.clientSideReturnTridentTickCount;
            }
        }
        super.tick();
    }
    
    private boolean isAcceptibleReturnOwner() {
        final Entity apx2 = this.getOwner();
        return apx2 != null && apx2.isAlive() && (!(apx2 instanceof ServerPlayer) || !apx2.isSpectator());
    }
    
    @Override
    protected ItemStack getPickupItem() {
        return this.tridentItem.copy();
    }
    
    public boolean isFoil() {
        return this.entityData.<Boolean>get(ThrownTrident.ID_FOIL);
    }
    
    @Nullable
    @Override
    protected EntityHitResult findHitEntity(final Vec3 dck1, final Vec3 dck2) {
        if (this.dealtDamage) {
            return null;
        }
        return super.findHitEntity(dck1, dck2);
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        final Entity apx3 = dch.getEntity();
        float float4 = 8.0f;
        if (apx3 instanceof LivingEntity) {
            final LivingEntity aqj5 = (LivingEntity)apx3;
            float4 += EnchantmentHelper.getDamageBonus(this.tridentItem, aqj5.getMobType());
        }
        final Entity apx4 = this.getOwner();
        final DamageSource aph6 = DamageSource.trident(this, (apx4 == null) ? this : apx4);
        this.dealtDamage = true;
        SoundEvent adn7 = SoundEvents.TRIDENT_HIT;
        if (apx3.hurt(aph6, float4)) {
            if (apx3.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (apx3 instanceof LivingEntity) {
                final LivingEntity aqj6 = (LivingEntity)apx3;
                if (apx4 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(aqj6, apx4);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)apx4, aqj6);
                }
                this.doPostHurtEffects(aqj6);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        float float5 = 1.0f;
        if (this.level instanceof ServerLevel && this.level.isThundering() && EnchantmentHelper.hasChanneling(this.tridentItem)) {
            final BlockPos fx9 = apx3.blockPosition();
            if (this.level.canSeeSky(fx9)) {
                final LightningBolt aqi10 = EntityType.LIGHTNING_BOLT.create(this.level);
                aqi10.moveTo(Vec3.atBottomCenterOf(fx9));
                aqi10.setCause((apx4 instanceof ServerPlayer) ? ((ServerPlayer)apx4) : null);
                this.level.addFreshEntity(aqi10);
                adn7 = SoundEvents.TRIDENT_THUNDER;
                float5 = 5.0f;
            }
        }
        this.playSound(adn7, float5, 1.0f);
    }
    
    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }
    
    @Override
    public void playerTouch(final Player bft) {
        final Entity apx3 = this.getOwner();
        if (apx3 != null && apx3.getUUID() != bft.getUUID()) {
            return;
        }
        super.playerTouch(bft);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Trident", 10)) {
            this.tridentItem = ItemStack.of(md.getCompound("Trident"));
        }
        this.dealtDamage = md.getBoolean("DealtDamage");
        this.entityData.<Byte>set(ThrownTrident.ID_LOYALTY, (byte)EnchantmentHelper.getLoyalty(this.tridentItem));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.put("Trident", (Tag)this.tridentItem.save(new CompoundTag()));
        md.putBoolean("DealtDamage", this.dealtDamage);
    }
    
    public void tickDespawn() {
        final int integer2 = this.entityData.<Byte>get(ThrownTrident.ID_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || integer2 <= 0) {
            super.tickDespawn();
        }
    }
    
    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }
    
    @Override
    public boolean shouldRender(final double double1, final double double2, final double double3) {
        return true;
    }
    
    static {
        ID_LOYALTY = SynchedEntityData.<Byte>defineId(ThrownTrident.class, EntityDataSerializers.BYTE);
        ID_FOIL = SynchedEntityData.<Boolean>defineId(ThrownTrident.class, EntityDataSerializers.BOOLEAN);
    }
}
