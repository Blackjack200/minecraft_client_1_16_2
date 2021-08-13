package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class EyeOfEnder extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    private double tx;
    private double ty;
    private double tz;
    private int life;
    private boolean surviveAfterDeath;
    
    public EyeOfEnder(final EntityType<? extends EyeOfEnder> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public EyeOfEnder(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.EYE_OF_ENDER, bru);
        this.life = 0;
        this.setPos(double2, double3, double4);
    }
    
    public void setItem(final ItemStack bly) {
        if (bly.getItem() != Items.ENDER_EYE || bly.hasTag()) {
            this.getEntityData().<ItemStack>set(EyeOfEnder.DATA_ITEM_STACK, (ItemStack)Util.<T>make((T)bly.copy(), (java.util.function.Consumer<T>)(bly -> bly.setCount(1))));
        }
    }
    
    private ItemStack getItemRaw() {
        return this.getEntityData().<ItemStack>get(EyeOfEnder.DATA_ITEM_STACK);
    }
    
    @Override
    public ItemStack getItem() {
        final ItemStack bly2 = this.getItemRaw();
        return bly2.isEmpty() ? new ItemStack(Items.ENDER_EYE) : bly2;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<ItemStack>define(EyeOfEnder.DATA_ITEM_STACK, ItemStack.EMPTY);
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = this.getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(double2)) {
            double2 = 4.0;
        }
        double2 *= 64.0;
        return double1 < double2 * double2;
    }
    
    public void signalTo(final BlockPos fx) {
        final double double3 = fx.getX();
        final int integer5 = fx.getY();
        final double double4 = fx.getZ();
        final double double5 = double3 - this.getX();
        final double double6 = double4 - this.getZ();
        final float float12 = Mth.sqrt(double5 * double5 + double6 * double6);
        if (float12 > 12.0f) {
            this.tx = this.getX() + double5 / float12 * 12.0;
            this.tz = this.getZ() + double6 / float12 * 12.0;
            this.ty = this.getY() + 8.0;
        }
        else {
            this.tx = double3;
            this.ty = integer5;
            this.tz = double4;
        }
        this.life = 0;
        this.surviveAfterDeath = (this.random.nextInt(5) > 0);
    }
    
    @Override
    public void lerpMotion(final double double1, final double double2, final double double3) {
        this.setDeltaMovement(double1, double2, double3);
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            final float float8 = Mth.sqrt(double1 * double1 + double3 * double3);
            this.yRot = (float)(Mth.atan2(double1, double3) * 57.2957763671875);
            this.xRot = (float)(Mth.atan2(double2, float8) * 57.2957763671875);
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        Vec3 dck2 = this.getDeltaMovement();
        final double double3 = this.getX() + dck2.x;
        final double double4 = this.getY() + dck2.y;
        final double double5 = this.getZ() + dck2.z;
        final float float9 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck2));
        this.xRot = Projectile.lerpRotation(this.xRotO, (float)(Mth.atan2(dck2.y, float9) * 57.2957763671875));
        this.yRot = Projectile.lerpRotation(this.yRotO, (float)(Mth.atan2(dck2.x, dck2.z) * 57.2957763671875));
        if (!this.level.isClientSide) {
            final double double6 = this.tx - double3;
            final double double7 = this.tz - double5;
            final float float10 = (float)Math.sqrt(double6 * double6 + double7 * double7);
            final float float11 = (float)Mth.atan2(double7, double6);
            double double8 = Mth.lerp(0.0025, float9, float10);
            double double9 = dck2.y;
            if (float10 < 1.0f) {
                double8 *= 0.8;
                double9 *= 0.8;
            }
            final int integer20 = (this.getY() < this.ty) ? 1 : -1;
            dck2 = new Vec3(Math.cos((double)float11) * double8, double9 + (integer20 - double9) * 0.014999999664723873, Math.sin((double)float11) * double8);
            this.setDeltaMovement(dck2);
        }
        final float float12 = 0.25f;
        if (this.isInWater()) {
            for (int integer21 = 0; integer21 < 4; ++integer21) {
                this.level.addParticle(ParticleTypes.BUBBLE, double3 - dck2.x * 0.25, double4 - dck2.y * 0.25, double5 - dck2.z * 0.25, dck2.x, dck2.y, dck2.z);
            }
        }
        else {
            this.level.addParticle(ParticleTypes.PORTAL, double3 - dck2.x * 0.25 + this.random.nextDouble() * 0.6 - 0.3, double4 - dck2.y * 0.25 - 0.5, double5 - dck2.z * 0.25 + this.random.nextDouble() * 0.6 - 0.3, dck2.x, dck2.y, dck2.z);
        }
        if (!this.level.isClientSide) {
            this.setPos(double3, double4, double5);
            ++this.life;
            if (this.life > 80 && !this.level.isClientSide) {
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0f, 1.0f);
                this.remove();
                if (this.surviveAfterDeath) {
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), this.getItem()));
                }
                else {
                    this.level.levelEvent(2003, this.blockPosition(), 0);
                }
            }
        }
        else {
            this.setPosRaw(double3, double4, double5);
        }
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        final ItemStack bly3 = this.getItemRaw();
        if (!bly3.isEmpty()) {
            md.put("Item", (Tag)bly3.save(new CompoundTag()));
        }
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        final ItemStack bly3 = ItemStack.of(md.getCompound("Item"));
        this.setItem(bly3);
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    static {
        DATA_ITEM_STACK = SynchedEntityData.<ItemStack>defineId(EyeOfEnder.class, EntityDataSerializers.ITEM_STACK);
    }
}
