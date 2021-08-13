package net.minecraft.world.entity.decoration;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.apache.logging.log4j.Logger;

public class ItemFrame extends HangingEntity {
    private static final Logger LOGGER;
    private static final EntityDataAccessor<ItemStack> DATA_ITEM;
    private static final EntityDataAccessor<Integer> DATA_ROTATION;
    private float dropChance;
    private boolean fixed;
    
    public ItemFrame(final EntityType<? extends ItemFrame> aqb, final Level bru) {
        super(aqb, bru);
        this.dropChance = 1.0f;
    }
    
    public ItemFrame(final Level bru, final BlockPos fx, final Direction gc) {
        super(EntityType.ITEM_FRAME, bru, fx);
        this.dropChance = 1.0f;
        this.setDirection(gc);
    }
    
    @Override
    protected float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return 0.0f;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<ItemStack>define(ItemFrame.DATA_ITEM, ItemStack.EMPTY);
        this.getEntityData().<Integer>define(ItemFrame.DATA_ROTATION, 0);
    }
    
    @Override
    protected void setDirection(final Direction gc) {
        Validate.notNull(gc);
        this.direction = gc;
        if (gc.getAxis().isHorizontal()) {
            this.xRot = 0.0f;
            this.yRot = (float)(this.direction.get2DDataValue() * 90);
        }
        else {
            this.xRot = (float)(-90 * gc.getAxisDirection().getStep());
            this.yRot = 0.0f;
        }
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
        this.recalculateBoundingBox();
    }
    
    @Override
    protected void recalculateBoundingBox() {
        if (this.direction == null) {
            return;
        }
        final double double2 = 0.46875;
        final double double3 = this.pos.getX() + 0.5 - this.direction.getStepX() * 0.46875;
        final double double4 = this.pos.getY() + 0.5 - this.direction.getStepY() * 0.46875;
        final double double5 = this.pos.getZ() + 0.5 - this.direction.getStepZ() * 0.46875;
        this.setPosRaw(double3, double4, double5);
        double double6 = this.getWidth();
        double double7 = this.getHeight();
        double double8 = this.getWidth();
        final Direction.Axis a16 = this.direction.getAxis();
        switch (a16) {
            case X: {
                double6 = 1.0;
                break;
            }
            case Y: {
                double7 = 1.0;
                break;
            }
            case Z: {
                double8 = 1.0;
                break;
            }
        }
        double6 /= 32.0;
        double7 /= 32.0;
        double8 /= 32.0;
        this.setBoundingBox(new AABB(double3 - double6, double4 - double7, double5 - double8, double3 + double6, double4 + double7, double5 + double8));
    }
    
    @Override
    public boolean survives() {
        if (this.fixed) {
            return true;
        }
        if (!this.level.noCollision(this)) {
            return false;
        }
        final BlockState cee2 = this.level.getBlockState(this.pos.relative(this.direction.getOpposite()));
        return (cee2.getMaterial().isSolid() || (this.direction.getAxis().isHorizontal() && DiodeBlock.isDiode(cee2))) && this.level.getEntities(this, this.getBoundingBox(), ItemFrame.HANGING_ENTITY).isEmpty();
    }
    
    @Override
    public void move(final MoverType aqo, final Vec3 dck) {
        if (!this.fixed) {
            super.move(aqo, dck);
        }
    }
    
    @Override
    public void push(final double double1, final double double2, final double double3) {
        if (!this.fixed) {
            super.push(double1, double2, double3);
        }
    }
    
    @Override
    public float getPickRadius() {
        return 0.0f;
    }
    
    @Override
    public void kill() {
        this.removeFramedMap(this.getItem());
        super.kill();
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.fixed) {
            return (aph == DamageSource.OUT_OF_WORLD || aph.isCreativePlayer()) && super.hurt(aph, float2);
        }
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (!aph.isExplosion() && !this.getItem().isEmpty()) {
            if (!this.level.isClientSide) {
                this.dropItem(aph.getEntity(), false);
                this.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0f, 1.0f);
            }
            return true;
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    public int getWidth() {
        return 12;
    }
    
    @Override
    public int getHeight() {
        return 12;
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = 16.0;
        double2 *= 64.0 * getViewScale();
        return double1 < double2 * double2;
    }
    
    @Override
    public void dropItem(@Nullable final Entity apx) {
        this.playSound(SoundEvents.ITEM_FRAME_BREAK, 1.0f, 1.0f);
        this.dropItem(apx, true);
    }
    
    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.ITEM_FRAME_PLACE, 1.0f, 1.0f);
    }
    
    private void dropItem(@Nullable final Entity apx, final boolean boolean2) {
        if (this.fixed) {
            return;
        }
        ItemStack bly4 = this.getItem();
        this.setItem(ItemStack.EMPTY);
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            if (apx == null) {
                this.removeFramedMap(bly4);
            }
            return;
        }
        if (apx instanceof Player) {
            final Player bft5 = (Player)apx;
            if (bft5.abilities.instabuild) {
                this.removeFramedMap(bly4);
                return;
            }
        }
        if (boolean2) {
            this.spawnAtLocation(Items.ITEM_FRAME);
        }
        if (!bly4.isEmpty()) {
            bly4 = bly4.copy();
            this.removeFramedMap(bly4);
            if (this.random.nextFloat() < this.dropChance) {
                this.spawnAtLocation(bly4);
            }
        }
    }
    
    private void removeFramedMap(final ItemStack bly) {
        if (bly.getItem() == Items.FILLED_MAP) {
            final MapItemSavedData cxu3 = MapItem.getOrCreateSavedData(bly, this.level);
            cxu3.removedFromFrame(this.pos, this.getId());
            cxu3.setDirty(true);
        }
        bly.setEntityRepresentation(null);
    }
    
    public ItemStack getItem() {
        return this.getEntityData().<ItemStack>get(ItemFrame.DATA_ITEM);
    }
    
    public void setItem(final ItemStack bly) {
        this.setItem(bly, true);
    }
    
    public void setItem(ItemStack bly, final boolean boolean2) {
        if (!bly.isEmpty()) {
            bly = bly.copy();
            bly.setCount(1);
            bly.setEntityRepresentation(this);
        }
        this.getEntityData().<ItemStack>set(ItemFrame.DATA_ITEM, bly);
        if (!bly.isEmpty()) {
            this.playSound(SoundEvents.ITEM_FRAME_ADD_ITEM, 1.0f, 1.0f);
        }
        if (boolean2 && this.pos != null) {
            this.level.updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
        }
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (integer == 0) {
            this.setItem(bly);
            return true;
        }
        return false;
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (us.equals(ItemFrame.DATA_ITEM)) {
            final ItemStack bly3 = this.getItem();
            if (!bly3.isEmpty() && bly3.getFrame() != this) {
                bly3.setEntityRepresentation(this);
            }
        }
    }
    
    public int getRotation() {
        return this.getEntityData().<Integer>get(ItemFrame.DATA_ROTATION);
    }
    
    public void setRotation(final int integer) {
        this.setRotation(integer, true);
    }
    
    private void setRotation(final int integer, final boolean boolean2) {
        this.getEntityData().<Integer>set(ItemFrame.DATA_ROTATION, integer % 8);
        if (boolean2 && this.pos != null) {
            this.level.updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (!this.getItem().isEmpty()) {
            md.put("Item", (Tag)this.getItem().save(new CompoundTag()));
            md.putByte("ItemRotation", (byte)this.getRotation());
            md.putFloat("ItemDropChance", this.dropChance);
        }
        md.putByte("Facing", (byte)this.direction.get3DDataValue());
        md.putBoolean("Invisible", this.isInvisible());
        md.putBoolean("Fixed", this.fixed);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        final CompoundTag md2 = md.getCompound("Item");
        if (md2 != null && !md2.isEmpty()) {
            final ItemStack bly4 = ItemStack.of(md2);
            if (bly4.isEmpty()) {
                ItemFrame.LOGGER.warn("Unable to load item from: {}", md2);
            }
            final ItemStack bly5 = this.getItem();
            if (!bly5.isEmpty() && !ItemStack.matches(bly4, bly5)) {
                this.removeFramedMap(bly5);
            }
            this.setItem(bly4, false);
            this.setRotation(md.getByte("ItemRotation"), false);
            if (md.contains("ItemDropChance", 99)) {
                this.dropChance = md.getFloat("ItemDropChance");
            }
        }
        this.setDirection(Direction.from3DDataValue(md.getByte("Facing")));
        this.setInvisible(md.getBoolean("Invisible"));
        this.fixed = md.getBoolean("Fixed");
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        final boolean boolean5 = !this.getItem().isEmpty();
        final boolean boolean6 = !bly4.isEmpty();
        if (this.fixed) {
            return InteractionResult.PASS;
        }
        if (this.level.isClientSide) {
            return (boolean5 || boolean6) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        if (!boolean5) {
            if (boolean6 && !this.removed) {
                this.setItem(bly4);
                if (!bft.abilities.instabuild) {
                    bly4.shrink(1);
                }
            }
        }
        else {
            this.playSound(SoundEvents.ITEM_FRAME_ROTATE_ITEM, 1.0f, 1.0f);
            this.setRotation(this.getRotation() + 1);
        }
        return InteractionResult.CONSUME;
    }
    
    public int getAnalogOutput() {
        if (this.getItem().isEmpty()) {
            return 0;
        }
        return this.getRotation() % 8 + 1;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.getType(), this.direction.get3DDataValue(), this.getPos());
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DATA_ITEM = SynchedEntityData.<ItemStack>defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);
        DATA_ROTATION = SynchedEntityData.<Integer>defineId(ItemFrame.class, EntityDataSerializers.INT);
    }
}
