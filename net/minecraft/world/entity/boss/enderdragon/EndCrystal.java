package net.minecraft.world.entity.boss.enderdragon;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import javax.annotation.Nullable;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;

public class EndCrystal extends Entity {
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_BEAM_TARGET;
    private static final EntityDataAccessor<Boolean> DATA_SHOW_BOTTOM;
    public int time;
    
    public EndCrystal(final EntityType<? extends EndCrystal> aqb, final Level bru) {
        super(aqb, bru);
        this.blocksBuilding = true;
        this.time = this.random.nextInt(100000);
    }
    
    public EndCrystal(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.END_CRYSTAL, bru);
        this.setPos(double2, double3, double4);
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<Optional<BlockPos>>define(EndCrystal.DATA_BEAM_TARGET, (Optional<BlockPos>)Optional.empty());
        this.getEntityData().<Boolean>define(EndCrystal.DATA_SHOW_BOTTOM, true);
    }
    
    @Override
    public void tick() {
        ++this.time;
        if (this.level instanceof ServerLevel) {
            final BlockPos fx2 = this.blockPosition();
            if (((ServerLevel)this.level).dragonFight() != null && this.level.getBlockState(fx2).isAir()) {
                this.level.setBlockAndUpdate(fx2, BaseFireBlock.getState(this.level, fx2));
            }
        }
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        if (this.getBeamTarget() != null) {
            md.put("BeamTarget", (Tag)NbtUtils.writeBlockPos(this.getBeamTarget()));
        }
        md.putBoolean("ShowBottom", this.showsBottom());
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        if (md.contains("BeamTarget", 10)) {
            this.setBeamTarget(NbtUtils.readBlockPos(md.getCompound("BeamTarget")));
        }
        if (md.contains("ShowBottom", 1)) {
            this.setShowBottom(md.getBoolean("ShowBottom"));
        }
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (aph.getEntity() instanceof EnderDragon) {
            return false;
        }
        if (!this.removed && !this.level.isClientSide) {
            this.remove();
            if (!aph.isExplosion()) {
                this.level.explode(null, this.getX(), this.getY(), this.getZ(), 6.0f, Explosion.BlockInteraction.DESTROY);
            }
            this.onDestroyedBy(aph);
        }
        return true;
    }
    
    @Override
    public void kill() {
        this.onDestroyedBy(DamageSource.GENERIC);
        super.kill();
    }
    
    private void onDestroyedBy(final DamageSource aph) {
        if (this.level instanceof ServerLevel) {
            final EndDragonFight chd3 = ((ServerLevel)this.level).dragonFight();
            if (chd3 != null) {
                chd3.onCrystalDestroyed(this, aph);
            }
        }
    }
    
    public void setBeamTarget(@Nullable final BlockPos fx) {
        this.getEntityData().<Optional<BlockPos>>set(EndCrystal.DATA_BEAM_TARGET, (Optional<BlockPos>)Optional.ofNullable(fx));
    }
    
    @Nullable
    public BlockPos getBeamTarget() {
        return (BlockPos)this.getEntityData().<Optional<BlockPos>>get(EndCrystal.DATA_BEAM_TARGET).orElse(null);
    }
    
    public void setShowBottom(final boolean boolean1) {
        this.getEntityData().<Boolean>set(EndCrystal.DATA_SHOW_BOTTOM, boolean1);
    }
    
    public boolean showsBottom() {
        return this.getEntityData().<Boolean>get(EndCrystal.DATA_SHOW_BOTTOM);
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        return super.shouldRenderAtSqrDistance(double1) || this.getBeamTarget() != null;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    static {
        DATA_BEAM_TARGET = SynchedEntityData.<Optional<BlockPos>>defineId(EndCrystal.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        DATA_SHOW_BOTTOM = SynchedEntityData.<Boolean>defineId(EndCrystal.class, EntityDataSerializers.BOOLEAN);
    }
}
