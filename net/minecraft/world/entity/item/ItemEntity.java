package net.minecraft.world.entity.item;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Objects;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class ItemEntity extends Entity {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM;
    private int age;
    private int pickupDelay;
    private int health;
    private UUID thrower;
    private UUID owner;
    public final float bobOffs;
    
    public ItemEntity(final EntityType<? extends ItemEntity> aqb, final Level bru) {
        super(aqb, bru);
        this.health = 5;
        this.bobOffs = (float)(Math.random() * 3.141592653589793 * 2.0);
    }
    
    public ItemEntity(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.ITEM, bru);
        this.setPos(double2, double3, double4);
        this.yRot = this.random.nextFloat() * 360.0f;
        this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
    }
    
    public ItemEntity(final Level bru, final double double2, final double double3, final double double4, final ItemStack bly) {
        this(bru, double2, double3, double4);
        this.setItem(bly);
    }
    
    private ItemEntity(final ItemEntity bcs) {
        super(bcs.getType(), bcs.level);
        this.health = 5;
        this.setItem(bcs.getItem().copy());
        this.copyPosition(bcs);
        this.age = bcs.age;
        this.bobOffs = bcs.bobOffs;
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<ItemStack>define(ItemEntity.DATA_ITEM, ItemStack.EMPTY);
    }
    
    @Override
    public void tick() {
        if (this.getItem().isEmpty()) {
            this.remove();
            return;
        }
        super.tick();
        if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
            --this.pickupDelay;
        }
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        final Vec3 dck2 = this.getDeltaMovement();
        final float float3 = this.getEyeHeight() - 0.11111111f;
        if (this.isInWater() && this.getFluidHeight(FluidTags.WATER) > float3) {
            this.setUnderwaterMovement();
        }
        else if (this.isInLava() && this.getFluidHeight(FluidTags.LAVA) > float3) {
            this.setUnderLavaMovement();
        }
        else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        if (this.level.isClientSide) {
            this.noPhysics = false;
        }
        else {
            this.noPhysics = !this.level.noCollision(this);
            if (this.noPhysics) {
                this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
            }
        }
        if (!this.onGround || Entity.getHorizontalDistanceSqr(this.getDeltaMovement()) > 9.999999747378752E-6 || (this.tickCount + this.getId()) % 4 == 0) {
            this.move(MoverType.SELF, this.getDeltaMovement());
            float float4 = 0.98f;
            if (this.onGround) {
                float4 = this.level.getBlockState(new BlockPos(this.getX(), this.getY() - 1.0, this.getZ())).getBlock().getFriction() * 0.98f;
            }
            this.setDeltaMovement(this.getDeltaMovement().multiply(float4, 0.98, float4));
            if (this.onGround) {
                final Vec3 dck3 = this.getDeltaMovement();
                if (dck3.y < 0.0) {
                    this.setDeltaMovement(dck3.multiply(1.0, -0.5, 1.0));
                }
            }
        }
        final boolean boolean4 = Mth.floor(this.xo) != Mth.floor(this.getX()) || Mth.floor(this.yo) != Mth.floor(this.getY()) || Mth.floor(this.zo) != Mth.floor(this.getZ());
        final int integer5 = boolean4 ? 2 : 40;
        if (this.tickCount % integer5 == 0) {
            if (this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA) && !this.fireImmune()) {
                this.playSound(SoundEvents.GENERIC_BURN, 0.4f, 2.0f + this.random.nextFloat() * 0.4f);
            }
            if (!this.level.isClientSide && this.isMergable()) {
                this.mergeWithNeighbours();
            }
        }
        if (this.age != -32768) {
            ++this.age;
        }
        this.hasImpulse |= this.updateInWaterStateAndDoFluidPushing();
        if (!this.level.isClientSide) {
            final double double6 = this.getDeltaMovement().subtract(dck2).lengthSqr();
            if (double6 > 0.01) {
                this.hasImpulse = true;
            }
        }
        if (!this.level.isClientSide && this.age >= 6000) {
            this.remove();
        }
    }
    
    private void setUnderwaterMovement() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.x * 0.9900000095367432, dck2.y + ((dck2.y < 0.05999999865889549) ? 5.0E-4f : 0.0f), dck2.z * 0.9900000095367432);
    }
    
    private void setUnderLavaMovement() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.x * 0.949999988079071, dck2.y + ((dck2.y < 0.05999999865889549) ? 5.0E-4f : 0.0f), dck2.z * 0.949999988079071);
    }
    
    private void mergeWithNeighbours() {
        if (!this.isMergable()) {
            return;
        }
        final List<ItemEntity> list2 = this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, this.getBoundingBox().inflate(0.5, 0.0, 0.5), (java.util.function.Predicate<? super ItemEntity>)(bcs -> bcs != this && bcs.isMergable()));
        for (final ItemEntity bcs4 : list2) {
            if (bcs4.isMergable()) {
                this.tryToMerge(bcs4);
                if (this.removed) {
                    break;
                }
                continue;
            }
        }
    }
    
    private boolean isMergable() {
        final ItemStack bly2 = this.getItem();
        return this.isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && bly2.getCount() < bly2.getMaxStackSize();
    }
    
    private void tryToMerge(final ItemEntity bcs) {
        final ItemStack bly3 = this.getItem();
        final ItemStack bly4 = bcs.getItem();
        if (!Objects.equals(this.getOwner(), bcs.getOwner()) || !areMergable(bly3, bly4)) {
            return;
        }
        if (bly4.getCount() < bly3.getCount()) {
            merge(this, bly3, bcs, bly4);
        }
        else {
            merge(bcs, bly4, this, bly3);
        }
    }
    
    public static boolean areMergable(final ItemStack bly1, final ItemStack bly2) {
        return bly2.getItem() == bly1.getItem() && bly2.getCount() + bly1.getCount() <= bly2.getMaxStackSize() && !(bly2.hasTag() ^ bly1.hasTag()) && (!bly2.hasTag() || bly2.getTag().equals(bly1.getTag()));
    }
    
    public static ItemStack merge(final ItemStack bly1, final ItemStack bly2, final int integer) {
        final int integer2 = Math.min(Math.min(bly1.getMaxStackSize(), integer) - bly1.getCount(), bly2.getCount());
        final ItemStack bly3 = bly1.copy();
        bly3.grow(integer2);
        bly2.shrink(integer2);
        return bly3;
    }
    
    private static void merge(final ItemEntity bcs, final ItemStack bly2, final ItemStack bly3) {
        final ItemStack bly4 = merge(bly2, bly3, 64);
        bcs.setItem(bly4);
    }
    
    private static void merge(final ItemEntity bcs1, final ItemStack bly2, final ItemEntity bcs3, final ItemStack bly4) {
        merge(bcs1, bly2, bly4);
        bcs1.pickupDelay = Math.max(bcs1.pickupDelay, bcs3.pickupDelay);
        bcs1.age = Math.min(bcs1.age, bcs3.age);
        if (bly4.isEmpty()) {
            bcs3.remove();
        }
    }
    
    @Override
    public boolean fireImmune() {
        return this.getItem().getItem().isFireResistant() || super.fireImmune();
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (!this.getItem().isEmpty() && this.getItem().getItem() == Items.NETHER_STAR && aph.isExplosion()) {
            return false;
        }
        if (!this.getItem().getItem().canBeHurtBy(aph)) {
            return false;
        }
        this.markHurt();
        this.health -= (int)float2;
        if (this.health <= 0) {
            this.remove();
        }
        return false;
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        md.putShort("Health", (short)this.health);
        md.putShort("Age", (short)this.age);
        md.putShort("PickupDelay", (short)this.pickupDelay);
        if (this.getThrower() != null) {
            md.putUUID("Thrower", this.getThrower());
        }
        if (this.getOwner() != null) {
            md.putUUID("Owner", this.getOwner());
        }
        if (!this.getItem().isEmpty()) {
            md.put("Item", (net.minecraft.nbt.Tag)this.getItem().save(new CompoundTag()));
        }
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        this.health = md.getShort("Health");
        this.age = md.getShort("Age");
        if (md.contains("PickupDelay")) {
            this.pickupDelay = md.getShort("PickupDelay");
        }
        if (md.hasUUID("Owner")) {
            this.owner = md.getUUID("Owner");
        }
        if (md.hasUUID("Thrower")) {
            this.thrower = md.getUUID("Thrower");
        }
        final CompoundTag md2 = md.getCompound("Item");
        this.setItem(ItemStack.of(md2));
        if (this.getItem().isEmpty()) {
            this.remove();
        }
    }
    
    @Override
    public void playerTouch(final Player bft) {
        if (this.level.isClientSide) {
            return;
        }
        final ItemStack bly3 = this.getItem();
        final Item blu4 = bly3.getItem();
        final int integer5 = bly3.getCount();
        if (this.pickupDelay == 0 && (this.owner == null || this.owner.equals(bft.getUUID())) && bft.inventory.add(bly3)) {
            bft.take(this, integer5);
            if (bly3.isEmpty()) {
                this.remove();
                bly3.setCount(integer5);
            }
            bft.awardStat(Stats.ITEM_PICKED_UP.get(blu4), integer5);
            bft.onItemPickup(this);
        }
    }
    
    @Override
    public Component getName() {
        final Component nr2 = this.getCustomName();
        if (nr2 != null) {
            return nr2;
        }
        return new TranslatableComponent(this.getItem().getDescriptionId());
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    @Nullable
    @Override
    public Entity changeDimension(final ServerLevel aag) {
        final Entity apx3 = super.changeDimension(aag);
        if (!this.level.isClientSide && apx3 instanceof ItemEntity) {
            ((ItemEntity)apx3).mergeWithNeighbours();
        }
        return apx3;
    }
    
    public ItemStack getItem() {
        return this.getEntityData().<ItemStack>get(ItemEntity.DATA_ITEM);
    }
    
    public void setItem(final ItemStack bly) {
        this.getEntityData().<ItemStack>set(ItemEntity.DATA_ITEM, bly);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        super.onSyncedDataUpdated(us);
        if (ItemEntity.DATA_ITEM.equals(us)) {
            this.getItem().setEntityRepresentation(this);
        }
    }
    
    @Nullable
    public UUID getOwner() {
        return this.owner;
    }
    
    public void setOwner(@Nullable final UUID uUID) {
        this.owner = uUID;
    }
    
    @Nullable
    public UUID getThrower() {
        return this.thrower;
    }
    
    public void setThrower(@Nullable final UUID uUID) {
        this.thrower = uUID;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public void setDefaultPickUpDelay() {
        this.pickupDelay = 10;
    }
    
    public void setNoPickUpDelay() {
        this.pickupDelay = 0;
    }
    
    public void setNeverPickUp() {
        this.pickupDelay = 32767;
    }
    
    public void setPickUpDelay(final int integer) {
        this.pickupDelay = integer;
    }
    
    public boolean hasPickUpDelay() {
        return this.pickupDelay > 0;
    }
    
    public void setExtendedLifetime() {
        this.age = -6000;
    }
    
    public void makeFakeItem() {
        this.setNeverPickUp();
        this.age = 5999;
    }
    
    public float getSpin(final float float1) {
        return (this.getAge() + float1) / 20.0f + this.bobOffs;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    public ItemEntity copy() {
        return new ItemEntity(this);
    }
    
    static {
        DATA_ITEM = SynchedEntityData.<ItemStack>defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
    }
}
