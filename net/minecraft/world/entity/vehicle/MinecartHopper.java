package net.minecraft.world.entity.vehicle;

import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import java.util.List;
import net.minecraft.world.Container;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.Hopper;

public class MinecartHopper extends AbstractMinecartContainer implements Hopper {
    private boolean enabled;
    private int cooldownTime;
    private final BlockPos lastPosition;
    
    public MinecartHopper(final EntityType<? extends MinecartHopper> aqb, final Level bru) {
        super(aqb, bru);
        this.enabled = true;
        this.cooldownTime = -1;
        this.lastPosition = BlockPos.ZERO;
    }
    
    public MinecartHopper(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.HOPPER_MINECART, double2, double3, double4, bru);
        this.enabled = true;
        this.cooldownTime = -1;
        this.lastPosition = BlockPos.ZERO;
    }
    
    @Override
    public Type getMinecartType() {
        return Type.HOPPER;
    }
    
    @Override
    public BlockState getDefaultDisplayBlockState() {
        return Blocks.HOPPER.defaultBlockState();
    }
    
    @Override
    public int getDefaultDisplayOffset() {
        return 1;
    }
    
    @Override
    public int getContainerSize() {
        return 5;
    }
    
    @Override
    public void activateMinecart(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        final boolean boolean5 = !boolean4;
        if (boolean5 != this.isEnabled()) {
            this.setEnabled(boolean5);
        }
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean boolean1) {
        this.enabled = boolean1;
    }
    
    @Override
    public Level getLevel() {
        return this.level;
    }
    
    @Override
    public double getLevelX() {
        return this.getX();
    }
    
    @Override
    public double getLevelY() {
        return this.getY() + 0.5;
    }
    
    @Override
    public double getLevelZ() {
        return this.getZ();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.isAlive() && this.isEnabled()) {
            final BlockPos fx2 = this.blockPosition();
            if (fx2.equals(this.lastPosition)) {
                --this.cooldownTime;
            }
            else {
                this.setCooldown(0);
            }
            if (!this.isOnCooldown()) {
                this.setCooldown(0);
                if (this.suckInItems()) {
                    this.setCooldown(4);
                    this.setChanged();
                }
            }
        }
    }
    
    public boolean suckInItems() {
        if (HopperBlockEntity.suckInItems(this)) {
            return true;
        }
        final List<ItemEntity> list2 = this.level.<ItemEntity>getEntitiesOfClass((java.lang.Class<? extends ItemEntity>)ItemEntity.class, this.getBoundingBox().inflate(0.25, 0.0, 0.25), (java.util.function.Predicate<? super ItemEntity>)EntitySelector.ENTITY_STILL_ALIVE);
        if (!list2.isEmpty()) {
            HopperBlockEntity.addItem(this, (ItemEntity)list2.get(0));
        }
        return false;
    }
    
    @Override
    public void destroy(final DamageSource aph) {
        super.destroy(aph);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.spawnAtLocation(Blocks.HOPPER);
        }
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("TransferCooldown", this.cooldownTime);
        md.putBoolean("Enabled", this.enabled);
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.cooldownTime = md.getInt("TransferCooldown");
        this.enabled = (!md.contains("Enabled") || md.getBoolean("Enabled"));
    }
    
    public void setCooldown(final int integer) {
        this.cooldownTime = integer;
    }
    
    public boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }
    
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new HopperMenu(integer, bfs, this);
    }
}
