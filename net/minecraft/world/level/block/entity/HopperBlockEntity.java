package net.minecraft.world.level.block.entity;

import java.util.stream.Stream;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.Block;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.WorldlyContainer;
import java.util.stream.IntStream;
import net.minecraft.world.Container;
import net.minecraft.core.Direction;
import java.util.Iterator;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.HopperBlock;
import java.util.function.Supplier;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public class HopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper, TickableBlockEntity {
    private NonNullList<ItemStack> items;
    private int cooldownTime;
    private long tickedGameTime;
    
    public HopperBlockEntity() {
        super(BlockEntityType.HOPPER);
        this.items = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);
        this.cooldownTime = -1;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.items = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(md)) {
            ContainerHelper.loadAllItems(md, this.items);
        }
        this.cooldownTime = md.getInt("TransferCooldown");
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (!this.trySaveLootTable(md)) {
            ContainerHelper.saveAllItems(md, this.items);
        }
        md.putInt("TransferCooldown", this.cooldownTime);
        return md;
    }
    
    public int getContainerSize() {
        return this.items.size();
    }
    
    @Override
    public ItemStack removeItem(final int integer1, final int integer2) {
        this.unpackLootTable(null);
        return ContainerHelper.removeItem((List<ItemStack>)this.getItems(), integer1, integer2);
    }
    
    @Override
    public void setItem(final int integer, final ItemStack bly) {
        this.unpackLootTable(null);
        this.getItems().set(integer, bly);
        if (bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.hopper");
    }
    
    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide) {
            return;
        }
        --this.cooldownTime;
        this.tickedGameTime = this.level.getGameTime();
        if (!this.isOnCooldown()) {
            this.setCooldown(0);
            this.tryMoveItems((Supplier<Boolean>)(() -> suckInItems(this)));
        }
    }
    
    private boolean tryMoveItems(final Supplier<Boolean> supplier) {
        if (this.level == null || this.level.isClientSide) {
            return false;
        }
        if (!this.isOnCooldown() && this.getBlockState().<Boolean>getValue((Property<Boolean>)HopperBlock.ENABLED)) {
            boolean boolean3 = false;
            if (!this.isEmpty()) {
                boolean3 = this.ejectItems();
            }
            if (!this.inventoryFull()) {
                boolean3 |= (boolean)supplier.get();
            }
            if (boolean3) {
                this.setCooldown(8);
                this.setChanged();
                return true;
            }
        }
        return false;
    }
    
    private boolean inventoryFull() {
        for (final ItemStack bly3 : this.items) {
            if (bly3.isEmpty() || bly3.getCount() != bly3.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean ejectItems() {
        final Container aok2 = this.getAttachedContainer();
        if (aok2 == null) {
            return false;
        }
        final Direction gc3 = this.getBlockState().<Direction>getValue((Property<Direction>)HopperBlock.FACING).getOpposite();
        if (this.isFullContainer(aok2, gc3)) {
            return false;
        }
        for (int integer4 = 0; integer4 < this.getContainerSize(); ++integer4) {
            if (!this.getItem(integer4).isEmpty()) {
                final ItemStack bly5 = this.getItem(integer4).copy();
                final ItemStack bly6 = addItem(this, aok2, this.removeItem(integer4, 1), gc3);
                if (bly6.isEmpty()) {
                    aok2.setChanged();
                    return true;
                }
                this.setItem(integer4, bly5);
            }
        }
        return false;
    }
    
    private static IntStream getSlots(final Container aok, final Direction gc) {
        if (aok instanceof WorldlyContainer) {
            return IntStream.of(((WorldlyContainer)aok).getSlotsForFace(gc));
        }
        return IntStream.range(0, aok.getContainerSize());
    }
    
    private boolean isFullContainer(final Container aok, final Direction gc) {
        return getSlots(aok, gc).allMatch(integer -> {
            final ItemStack bly3 = aok.getItem(integer);
            return bly3.getCount() >= bly3.getMaxStackSize();
        });
    }
    
    private static boolean isEmptyContainer(final Container aok, final Direction gc) {
        return getSlots(aok, gc).allMatch(integer -> aok.getItem(integer).isEmpty());
    }
    
    public static boolean suckInItems(final Hopper ccu) {
        final Container aok2 = getSourceContainer(ccu);
        if (aok2 != null) {
            final Direction gc3 = Direction.DOWN;
            return !isEmptyContainer(aok2, gc3) && getSlots(aok2, gc3).anyMatch(integer -> tryTakeInItemFromSlot(ccu, aok2, integer, gc3));
        }
        for (final ItemEntity bcs4 : getItemsAtAndAbove(ccu)) {
            if (addItem(ccu, bcs4)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean tryTakeInItemFromSlot(final Hopper ccu, final Container aok, final int integer, final Direction gc) {
        final ItemStack bly5 = aok.getItem(integer);
        if (!bly5.isEmpty() && canTakeItemFromContainer(aok, bly5, integer, gc)) {
            final ItemStack bly6 = bly5.copy();
            final ItemStack bly7 = addItem(aok, ccu, aok.removeItem(integer, 1), null);
            if (bly7.isEmpty()) {
                aok.setChanged();
                return true;
            }
            aok.setItem(integer, bly6);
        }
        return false;
    }
    
    public static boolean addItem(final Container aok, final ItemEntity bcs) {
        boolean boolean3 = false;
        final ItemStack bly4 = bcs.getItem().copy();
        final ItemStack bly5 = addItem(null, aok, bly4, null);
        if (bly5.isEmpty()) {
            boolean3 = true;
            bcs.remove();
        }
        else {
            bcs.setItem(bly5);
        }
        return boolean3;
    }
    
    public static ItemStack addItem(@Nullable final Container aok1, final Container aok2, ItemStack bly, @Nullable final Direction gc) {
        if (aok2 instanceof WorldlyContainer && gc != null) {
            final WorldlyContainer apb5 = (WorldlyContainer)aok2;
            final int[] arr6 = apb5.getSlotsForFace(gc);
            for (int integer7 = 0; integer7 < arr6.length && !bly.isEmpty(); bly = tryMoveInItem(aok1, aok2, bly, arr6[integer7], gc), ++integer7) {}
        }
        else {
            for (int integer8 = aok2.getContainerSize(), integer9 = 0; integer9 < integer8 && !bly.isEmpty(); bly = tryMoveInItem(aok1, aok2, bly, integer9, gc), ++integer9) {}
        }
        return bly;
    }
    
    private static boolean canPlaceItemInContainer(final Container aok, final ItemStack bly, final int integer, @Nullable final Direction gc) {
        return aok.canPlaceItem(integer, bly) && (!(aok instanceof WorldlyContainer) || ((WorldlyContainer)aok).canPlaceItemThroughFace(integer, bly, gc));
    }
    
    private static boolean canTakeItemFromContainer(final Container aok, final ItemStack bly, final int integer, final Direction gc) {
        return !(aok instanceof WorldlyContainer) || ((WorldlyContainer)aok).canTakeItemThroughFace(integer, bly, gc);
    }
    
    private static ItemStack tryMoveInItem(@Nullable final Container aok1, final Container aok2, ItemStack bly, final int integer, @Nullable final Direction gc) {
        final ItemStack bly2 = aok2.getItem(integer);
        if (canPlaceItemInContainer(aok2, bly, integer, gc)) {
            boolean boolean7 = false;
            final boolean boolean8 = aok2.isEmpty();
            if (bly2.isEmpty()) {
                aok2.setItem(integer, bly);
                bly = ItemStack.EMPTY;
                boolean7 = true;
            }
            else if (canMergeItems(bly2, bly)) {
                final int integer2 = bly.getMaxStackSize() - bly2.getCount();
                final int integer3 = Math.min(bly.getCount(), integer2);
                bly.shrink(integer3);
                bly2.grow(integer3);
                boolean7 = (integer3 > 0);
            }
            if (boolean7) {
                if (boolean8 && aok2 instanceof HopperBlockEntity) {
                    final HopperBlockEntity ccv9 = (HopperBlockEntity)aok2;
                    if (!ccv9.isOnCustomCooldown()) {
                        int integer3 = 0;
                        if (aok1 instanceof HopperBlockEntity) {
                            final HopperBlockEntity ccv10 = (HopperBlockEntity)aok1;
                            if (ccv9.tickedGameTime >= ccv10.tickedGameTime) {
                                integer3 = 1;
                            }
                        }
                        ccv9.setCooldown(8 - integer3);
                    }
                }
                aok2.setChanged();
            }
        }
        return bly;
    }
    
    @Nullable
    private Container getAttachedContainer() {
        final Direction gc2 = this.getBlockState().<Direction>getValue((Property<Direction>)HopperBlock.FACING);
        return getContainerAt(this.getLevel(), this.worldPosition.relative(gc2));
    }
    
    @Nullable
    public static Container getSourceContainer(final Hopper ccu) {
        return getContainerAt(ccu.getLevel(), ccu.getLevelX(), ccu.getLevelY() + 1.0, ccu.getLevelZ());
    }
    
    public static List<ItemEntity> getItemsAtAndAbove(final Hopper ccu) {
        return (List<ItemEntity>)ccu.getSuckShape().toAabbs().stream().flatMap(dcf -> ccu.getLevel().<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)ItemEntity.class, dcf.move(ccu.getLevelX() - 0.5, ccu.getLevelY() - 0.5, ccu.getLevelZ() - 0.5), (java.util.function.Predicate<? super Entity>)EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }
    
    @Nullable
    public static Container getContainerAt(final Level bru, final BlockPos fx) {
        return getContainerAt(bru, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5);
    }
    
    @Nullable
    public static Container getContainerAt(final Level bru, final double double2, final double double3, final double double4) {
        Container aok8 = null;
        final BlockPos fx9 = new BlockPos(double2, double3, double4);
        final BlockState cee10 = bru.getBlockState(fx9);
        final Block bul11 = cee10.getBlock();
        if (bul11 instanceof WorldlyContainerHolder) {
            aok8 = ((WorldlyContainerHolder)bul11).getContainer(cee10, bru, fx9);
        }
        else if (bul11.isEntityBlock()) {
            final BlockEntity ccg12 = bru.getBlockEntity(fx9);
            if (ccg12 instanceof Container) {
                aok8 = (Container)ccg12;
                if (aok8 instanceof ChestBlockEntity && bul11 instanceof ChestBlock) {
                    aok8 = ChestBlock.getContainer((ChestBlock)bul11, cee10, bru, fx9, true);
                }
            }
        }
        if (aok8 == null) {
            final List<Entity> list12 = bru.getEntities(null, new AABB(double2 - 0.5, double3 - 0.5, double4 - 0.5, double2 + 0.5, double3 + 0.5, double4 + 0.5), EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!list12.isEmpty()) {
                aok8 = (Container)list12.get(bru.random.nextInt(list12.size()));
            }
        }
        return aok8;
    }
    
    private static boolean canMergeItems(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && bly1.getDamageValue() == bly2.getDamageValue() && bly1.getCount() <= bly1.getMaxStackSize() && ItemStack.tagMatches(bly1, bly2);
    }
    
    @Override
    public double getLevelX() {
        return this.worldPosition.getX() + 0.5;
    }
    
    @Override
    public double getLevelY() {
        return this.worldPosition.getY() + 0.5;
    }
    
    @Override
    public double getLevelZ() {
        return this.worldPosition.getZ() + 0.5;
    }
    
    private void setCooldown(final int integer) {
        this.cooldownTime = integer;
    }
    
    private boolean isOnCooldown() {
        return this.cooldownTime > 0;
    }
    
    private boolean isOnCustomCooldown() {
        return this.cooldownTime > 8;
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    protected void setItems(final NonNullList<ItemStack> gj) {
        this.items = gj;
    }
    
    public void entityInside(final Entity apx) {
        if (apx instanceof ItemEntity) {
            final BlockPos fx3 = this.getBlockPos();
            if (Shapes.joinIsNotEmpty(Shapes.create(apx.getBoundingBox().move(-fx3.getX(), -fx3.getY(), -fx3.getZ())), this.getSuckShape(), BooleanOp.AND)) {
                this.tryMoveItems((Supplier<Boolean>)(() -> addItem(this, (ItemEntity)apx)));
            }
        }
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new HopperMenu(integer, bfs, this);
    }
}
