package net.minecraft.world.entity.boss;

import net.minecraft.world.entity.Pose;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.Entity;

public class EnderDragonPart extends Entity {
    public final EnderDragon parentMob;
    public final String name;
    private final EntityDimensions size;
    
    public EnderDragonPart(final EnderDragon bbo, final String string, final float float3, final float float4) {
        super(bbo.getType(), bbo.level);
        this.size = EntityDimensions.scalable(float3, float4);
        this.refreshDimensions();
        this.parentMob = bbo;
        this.name = string;
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return !this.isInvulnerableTo(aph) && this.parentMob.hurt(this, aph, float2);
    }
    
    @Override
    public boolean is(final Entity apx) {
        return this == apx || this.parentMob == apx;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return this.size;
    }
}
