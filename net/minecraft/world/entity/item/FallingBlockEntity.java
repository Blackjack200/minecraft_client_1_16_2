package net.minecraft.world.entity.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.CrashReportCategory;
import net.minecraft.nbt.NbtUtils;
import java.util.List;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.BlockTags;
import com.google.common.collect.Lists;
import net.minecraft.util.Mth;
import java.util.Iterator;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;

public class FallingBlockEntity extends Entity {
    private BlockState blockState;
    public int time;
    public boolean dropItem;
    private boolean cancelDrop;
    private boolean hurtEntities;
    private int fallDamageMax;
    private float fallDamageAmount;
    public CompoundTag blockData;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS;
    
    public FallingBlockEntity(final EntityType<? extends FallingBlockEntity> aqb, final Level bru) {
        super(aqb, bru);
        this.blockState = Blocks.SAND.defaultBlockState();
        this.dropItem = true;
        this.fallDamageMax = 40;
        this.fallDamageAmount = 2.0f;
    }
    
    public FallingBlockEntity(final Level bru, final double double2, final double double3, final double double4, final BlockState cee) {
        this(EntityType.FALLING_BLOCK, bru);
        this.blockState = cee;
        this.blocksBuilding = true;
        this.setPos(double2, double3 + (1.0f - this.getBbHeight()) / 2.0f, double4);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = double2;
        this.yo = double3;
        this.zo = double4;
        this.setStartPos(this.blockPosition());
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    public void setStartPos(final BlockPos fx) {
        this.entityData.<BlockPos>set(FallingBlockEntity.DATA_START_POS, fx);
    }
    
    public BlockPos getStartPos() {
        return this.entityData.<BlockPos>get(FallingBlockEntity.DATA_START_POS);
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    protected void defineSynchedData() {
        this.entityData.<BlockPos>define(FallingBlockEntity.DATA_START_POS, BlockPos.ZERO);
    }
    
    @Override
    public boolean isPickable() {
        return !this.removed;
    }
    
    @Override
    public void tick() {
        if (this.blockState.isAir()) {
            this.remove();
            return;
        }
        final Block bul2 = this.blockState.getBlock();
        if (this.time++ == 0) {
            final BlockPos fx3 = this.blockPosition();
            if (this.level.getBlockState(fx3).is(bul2)) {
                this.level.removeBlock(fx3, false);
            }
            else if (!this.level.isClientSide) {
                this.remove();
                return;
            }
        }
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        if (!this.level.isClientSide) {
            BlockPos fx3 = this.blockPosition();
            final boolean boolean4 = this.blockState.getBlock() instanceof ConcretePowderBlock;
            boolean boolean5 = boolean4 && this.level.getFluidState(fx3).is(FluidTags.WATER);
            final double double6 = this.getDeltaMovement().lengthSqr();
            if (boolean4 && double6 > 1.0) {
                final BlockHitResult dcg8 = this.level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                if (dcg8.getType() != HitResult.Type.MISS && this.level.getFluidState(dcg8.getBlockPos()).is(FluidTags.WATER)) {
                    fx3 = dcg8.getBlockPos();
                    boolean5 = true;
                }
            }
            if (this.onGround || boolean5) {
                final BlockState cee8 = this.level.getBlockState(fx3);
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
                if (!cee8.is(Blocks.MOVING_PISTON)) {
                    this.remove();
                    if (!this.cancelDrop) {
                        final boolean boolean6 = cee8.canBeReplaced(new DirectionalPlaceContext(this.level, fx3, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                        final boolean boolean7 = FallingBlock.isFree(this.level.getBlockState(fx3.below())) && (!boolean4 || !boolean5);
                        final boolean boolean8 = this.blockState.canSurvive(this.level, fx3) && !boolean7;
                        if (boolean6 && boolean8) {
                            if (this.blockState.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.WATERLOGGED) && this.level.getFluidState(fx3).getType() == Fluids.WATER) {
                                this.blockState = ((StateHolder<O, BlockState>)this.blockState).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.WATERLOGGED, true);
                            }
                            if (this.level.setBlock(fx3, this.blockState, 3)) {
                                if (bul2 instanceof FallingBlock) {
                                    ((FallingBlock)bul2).onLand(this.level, fx3, this.blockState, cee8, this);
                                }
                                if (this.blockData != null && bul2 instanceof EntityBlock) {
                                    final BlockEntity ccg12 = this.level.getBlockEntity(fx3);
                                    if (ccg12 != null) {
                                        final CompoundTag md13 = ccg12.save(new CompoundTag());
                                        for (final String string15 : this.blockData.getAllKeys()) {
                                            final net.minecraft.nbt.Tag mt16 = this.blockData.get(string15);
                                            if (!"x".equals(string15) && !"y".equals(string15)) {
                                                if ("z".equals(string15)) {
                                                    continue;
                                                }
                                                md13.put(string15, mt16.copy());
                                            }
                                        }
                                        ccg12.load(this.blockState, md13);
                                        ccg12.setChanged();
                                    }
                                }
                            }
                            else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.spawnAtLocation(bul2);
                            }
                        }
                        else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(bul2);
                        }
                    }
                    else if (bul2 instanceof FallingBlock) {
                        ((FallingBlock)bul2).onBroken(this.level, fx3, this);
                    }
                }
            }
            else if (!this.level.isClientSide && ((this.time > 100 && (fx3.getY() < 1 || fx3.getY() > 256)) || this.time > 600)) {
                if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.spawnAtLocation(bul2);
                }
                this.remove();
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        if (this.hurtEntities) {
            final int integer4 = Mth.ceil(float1 - 1.0f);
            if (integer4 > 0) {
                final List<Entity> list5 = (List<Entity>)Lists.newArrayList((Iterable)this.level.getEntities(this, this.getBoundingBox()));
                final boolean boolean6 = this.blockState.is(BlockTags.ANVIL);
                final DamageSource aph7 = boolean6 ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                for (final Entity apx9 : list5) {
                    apx9.hurt(aph7, (float)Math.min(Mth.floor(integer4 * this.fallDamageAmount), this.fallDamageMax));
                }
                if (boolean6 && this.random.nextFloat() < 0.05000000074505806 + integer4 * 0.05) {
                    final BlockState cee8 = AnvilBlock.damage(this.blockState);
                    if (cee8 == null) {
                        this.cancelDrop = true;
                    }
                    else {
                        this.blockState = cee8;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.put("BlockState", (net.minecraft.nbt.Tag)NbtUtils.writeBlockState(this.blockState));
        md.putInt("Time", this.time);
        md.putBoolean("DropItem", this.dropItem);
        md.putBoolean("HurtEntities", this.hurtEntities);
        md.putFloat("FallHurtAmount", this.fallDamageAmount);
        md.putInt("FallHurtMax", this.fallDamageMax);
        if (this.blockData != null) {
            md.put("TileEntityData", (net.minecraft.nbt.Tag)this.blockData);
        }
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        this.blockState = NbtUtils.readBlockState(md.getCompound("BlockState"));
        this.time = md.getInt("Time");
        if (md.contains("HurtEntities", 99)) {
            this.hurtEntities = md.getBoolean("HurtEntities");
            this.fallDamageAmount = md.getFloat("FallHurtAmount");
            this.fallDamageMax = md.getInt("FallHurtMax");
        }
        else if (this.blockState.is(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }
        if (md.contains("DropItem", 99)) {
            this.dropItem = md.getBoolean("DropItem");
        }
        if (md.contains("TileEntityData", 10)) {
            this.blockData = md.getCompound("TileEntityData");
        }
        if (this.blockState.isAir()) {
            this.blockState = Blocks.SAND.defaultBlockState();
        }
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public void setHurtsEntities(final boolean boolean1) {
        this.hurtEntities = boolean1;
    }
    
    @Override
    public boolean displayFireAnimation() {
        return false;
    }
    
    @Override
    public void fillCrashReportCategory(final CrashReportCategory m) {
        super.fillCrashReportCategory(m);
        m.setDetail("Immitating BlockState", this.blockState.toString());
    }
    
    public BlockState getBlockState() {
        return this.blockState;
    }
    
    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockState()));
    }
    
    static {
        DATA_START_POS = SynchedEntityData.<BlockPos>defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
    }
}
