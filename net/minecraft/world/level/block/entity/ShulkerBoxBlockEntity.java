package net.minecraft.world.level.block.entity;

import java.util.stream.IntStream;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.WorldlyContainer;

public class ShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, TickableBlockEntity {
    private static final int[] SLOTS;
    private NonNullList<ItemStack> itemStacks;
    private int openCount;
    private AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    @Nullable
    private DyeColor color;
    private boolean loadColorFromBlock;
    
    public ShulkerBoxBlockEntity(@Nullable final DyeColor bku) {
        super(BlockEntityType.SHULKER_BOX);
        this.itemStacks = NonNullList.<ItemStack>withSize(27, ItemStack.EMPTY);
        this.animationStatus = AnimationStatus.CLOSED;
        this.color = bku;
    }
    
    public ShulkerBoxBlockEntity() {
        this((DyeColor)null);
        this.loadColorFromBlock = true;
    }
    
    @Override
    public void tick() {
        this.updateAnimation();
        if (this.animationStatus == AnimationStatus.OPENING || this.animationStatus == AnimationStatus.CLOSING) {
            this.moveCollidedEntities();
        }
    }
    
    protected void updateAnimation() {
        this.progressOld = this.progress;
        switch (this.animationStatus) {
            case CLOSED: {
                this.progress = 0.0f;
                break;
            }
            case OPENING: {
                this.progress += 0.1f;
                if (this.progress >= 1.0f) {
                    this.moveCollidedEntities();
                    this.animationStatus = AnimationStatus.OPENED;
                    this.progress = 1.0f;
                    this.doNeighborUpdates();
                    break;
                }
                break;
            }
            case CLOSING: {
                this.progress -= 0.1f;
                if (this.progress <= 0.0f) {
                    this.animationStatus = AnimationStatus.CLOSED;
                    this.progress = 0.0f;
                    this.doNeighborUpdates();
                    break;
                }
                break;
            }
            case OPENED: {
                this.progress = 1.0f;
                break;
            }
        }
    }
    
    public AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }
    
    public AABB getBoundingBox(final BlockState cee) {
        return this.getBoundingBox(cee.<Direction>getValue(ShulkerBoxBlock.FACING));
    }
    
    public AABB getBoundingBox(final Direction gc) {
        final float float3 = this.getProgress(1.0f);
        return Shapes.block().bounds().expandTowards(0.5f * float3 * gc.getStepX(), 0.5f * float3 * gc.getStepY(), 0.5f * float3 * gc.getStepZ());
    }
    
    private AABB getTopBoundingBox(final Direction gc) {
        final Direction gc2 = gc.getOpposite();
        return this.getBoundingBox(gc).contract(gc2.getStepX(), gc2.getStepY(), gc2.getStepZ());
    }
    
    private void moveCollidedEntities() {
        final BlockState cee2 = this.level.getBlockState(this.getBlockPos());
        if (!(cee2.getBlock() instanceof ShulkerBoxBlock)) {
            return;
        }
        final Direction gc3 = cee2.<Direction>getValue(ShulkerBoxBlock.FACING);
        final AABB dcf4 = this.getTopBoundingBox(gc3).move(this.worldPosition);
        final List<Entity> list5 = this.level.getEntities(null, dcf4);
        if (list5.isEmpty()) {
            return;
        }
        for (int integer6 = 0; integer6 < list5.size(); ++integer6) {
            final Entity apx7 = (Entity)list5.get(integer6);
            if (apx7.getPistonPushReaction() != PushReaction.IGNORE) {
                double double8 = 0.0;
                double double9 = 0.0;
                double double10 = 0.0;
                final AABB dcf5 = apx7.getBoundingBox();
                switch (gc3.getAxis()) {
                    case X: {
                        if (gc3.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                            double8 = dcf4.maxX - dcf5.minX;
                        }
                        else {
                            double8 = dcf5.maxX - dcf4.minX;
                        }
                        double8 += 0.01;
                        break;
                    }
                    case Y: {
                        if (gc3.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                            double9 = dcf4.maxY - dcf5.minY;
                        }
                        else {
                            double9 = dcf5.maxY - dcf4.minY;
                        }
                        double9 += 0.01;
                        break;
                    }
                    case Z: {
                        if (gc3.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                            double10 = dcf4.maxZ - dcf5.minZ;
                        }
                        else {
                            double10 = dcf5.maxZ - dcf4.minZ;
                        }
                        double10 += 0.01;
                        break;
                    }
                }
                apx7.move(MoverType.SHULKER_BOX, new Vec3(double8 * gc3.getStepX(), double9 * gc3.getStepY(), double10 * gc3.getStepZ()));
            }
        }
    }
    
    public int getContainerSize() {
        return this.itemStacks.size();
    }
    
    public boolean triggerEvent(final int integer1, final int integer2) {
        if (integer1 == 1) {
            if ((this.openCount = integer2) == 0) {
                this.animationStatus = AnimationStatus.CLOSING;
                this.doNeighborUpdates();
            }
            if (integer2 == 1) {
                this.animationStatus = AnimationStatus.OPENING;
                this.doNeighborUpdates();
            }
            return true;
        }
        return super.triggerEvent(integer1, integer2);
    }
    
    private void doNeighborUpdates() {
        this.getBlockState().updateNeighbourShapes(this.getLevel(), this.getBlockPos(), 3);
    }
    
    public void startOpen(final Player bft) {
        if (!bft.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }
            ++this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount == 1) {
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    public void stopOpen(final Player bft) {
        if (!bft.isSpectator()) {
            --this.openCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.shulkerBox");
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.loadFromTag(md);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        return this.saveToTag(md);
    }
    
    public void loadFromTag(final CompoundTag md) {
        this.itemStacks = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(md) && md.contains("Items", 9)) {
            ContainerHelper.loadAllItems(md, this.itemStacks);
        }
    }
    
    public CompoundTag saveToTag(final CompoundTag md) {
        if (!this.trySaveLootTable(md)) {
            ContainerHelper.saveAllItems(md, this.itemStacks, false);
        }
        return md;
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }
    
    @Override
    protected void setItems(final NonNullList<ItemStack> gj) {
        this.itemStacks = gj;
    }
    
    @Override
    public int[] getSlotsForFace(final Direction gc) {
        return ShulkerBoxBlockEntity.SLOTS;
    }
    
    @Override
    public boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc) {
        return !(Block.byItem(bly.getItem()) instanceof ShulkerBoxBlock);
    }
    
    @Override
    public boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc) {
        return true;
    }
    
    public float getProgress(final float float1) {
        return Mth.lerp(float1, this.progressOld, this.progress);
    }
    
    @Nullable
    public DyeColor getColor() {
        if (this.loadColorFromBlock) {
            this.color = ShulkerBoxBlock.getColorFromBlock(this.getBlockState().getBlock());
            this.loadColorFromBlock = false;
        }
        return this.color;
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new ShulkerBoxMenu(integer, bfs, this);
    }
    
    public boolean isClosed() {
        return this.animationStatus == AnimationStatus.CLOSED;
    }
    
    static {
        SLOTS = IntStream.range(0, 27).toArray();
    }
    
    public enum AnimationStatus {
        CLOSED, 
        OPENING, 
        OPENED, 
        CLOSING;
    }
}
