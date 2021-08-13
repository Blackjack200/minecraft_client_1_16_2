package net.minecraft.world.entity.vehicle;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.item.Items;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.syncher.EntityDataAccessor;

public class MinecartFurnace extends AbstractMinecart {
    private static final EntityDataAccessor<Boolean> DATA_ID_FUEL;
    private int fuel;
    public double xPush;
    public double zPush;
    private static final Ingredient INGREDIENT;
    
    public MinecartFurnace(final EntityType<? extends MinecartFurnace> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public MinecartFurnace(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.FURNACE_MINECART, bru, double2, double3, double4);
    }
    
    @Override
    public Type getMinecartType() {
        return Type.FURNACE;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(MinecartFurnace.DATA_ID_FUEL, false);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide()) {
            if (this.fuel > 0) {
                --this.fuel;
            }
            if (this.fuel <= 0) {
                this.xPush = 0.0;
                this.zPush = 0.0;
            }
            this.setHasFuel(this.fuel > 0);
        }
        if (this.hasFuel() && this.random.nextInt(4) == 0) {
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.8, this.getZ(), 0.0, 0.0, 0.0);
        }
    }
    
    @Override
    protected double getMaxSpeed() {
        return 0.2;
    }
    
    @Override
    public void destroy(final DamageSource aph) {
        super.destroy(aph);
        if (!aph.isExplosion() && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.spawnAtLocation(Blocks.FURNACE);
        }
    }
    
    @Override
    protected void moveAlongTrack(final BlockPos fx, final BlockState cee) {
        final double double4 = 1.0E-4;
        final double double5 = 0.001;
        super.moveAlongTrack(fx, cee);
        final Vec3 dck8 = this.getDeltaMovement();
        final double double6 = Entity.getHorizontalDistanceSqr(dck8);
        final double double7 = this.xPush * this.xPush + this.zPush * this.zPush;
        if (double7 > 1.0E-4 && double6 > 0.001) {
            final double double8 = Mth.sqrt(double6);
            final double double9 = Mth.sqrt(double7);
            this.xPush = dck8.x / double8 * double9;
            this.zPush = dck8.z / double8 * double9;
        }
    }
    
    @Override
    protected void applyNaturalSlowdown() {
        double double2 = this.xPush * this.xPush + this.zPush * this.zPush;
        if (double2 > 1.0E-7) {
            double2 = Mth.sqrt(double2);
            this.xPush /= double2;
            this.zPush /= double2;
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.8, 0.0, 0.8).add(this.xPush, 0.0, this.zPush));
        }
        else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.98, 0.0, 0.98));
        }
        super.applyNaturalSlowdown();
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (MinecartFurnace.INGREDIENT.test(bly4) && this.fuel + 3600 <= 32000) {
            if (!bft.abilities.instabuild) {
                bly4.shrink(1);
            }
            this.fuel += 3600;
        }
        if (this.fuel > 0) {
            this.xPush = this.getX() - bft.getX();
            this.zPush = this.getZ() - bft.getZ();
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putDouble("PushX", this.xPush);
        md.putDouble("PushZ", this.zPush);
        md.putShort("Fuel", (short)this.fuel);
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.xPush = md.getDouble("PushX");
        this.zPush = md.getDouble("PushZ");
        this.fuel = md.getShort("Fuel");
    }
    
    protected boolean hasFuel() {
        return this.entityData.<Boolean>get(MinecartFurnace.DATA_ID_FUEL);
    }
    
    protected void setHasFuel(final boolean boolean1) {
        this.entityData.<Boolean>set(MinecartFurnace.DATA_ID_FUEL, boolean1);
    }
    
    @Override
    public BlockState getDefaultDisplayBlockState() {
        return (((StateHolder<O, BlockState>)Blocks.FURNACE.defaultBlockState()).setValue((Property<Comparable>)FurnaceBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)FurnaceBlock.LIT, this.hasFuel());
    }
    
    static {
        DATA_ID_FUEL = SynchedEntityData.<Boolean>defineId(MinecartFurnace.class, EntityDataSerializers.BOOLEAN);
        INGREDIENT = Ingredient.of(Items.COAL, Items.CHARCOAL);
    }
}
