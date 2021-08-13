package net.minecraft.world.inventory;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import java.util.Iterator;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiFunction;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import java.util.Set;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

public abstract class AbstractContainerMenu {
    private final NonNullList<ItemStack> lastSlots;
    public final List<Slot> slots;
    private final List<DataSlot> dataSlots;
    @Nullable
    private final MenuType<?> menuType;
    public final int containerId;
    private short changeUid;
    private int quickcraftType;
    private int quickcraftStatus;
    private final Set<Slot> quickcraftSlots;
    private final List<ContainerListener> containerListeners;
    private final Set<Player> unSynchedPlayers;
    
    protected AbstractContainerMenu(@Nullable final MenuType<?> bjb, final int integer) {
        this.lastSlots = NonNullList.<ItemStack>create();
        this.slots = (List<Slot>)Lists.newArrayList();
        this.dataSlots = (List<DataSlot>)Lists.newArrayList();
        this.quickcraftType = -1;
        this.quickcraftSlots = (Set<Slot>)Sets.newHashSet();
        this.containerListeners = (List<ContainerListener>)Lists.newArrayList();
        this.unSynchedPlayers = (Set<Player>)Sets.newHashSet();
        this.menuType = bjb;
        this.containerId = integer;
    }
    
    protected static boolean stillValid(final ContainerLevelAccess bij, final Player bft, final Block bul) {
        return bij.<Boolean>evaluate((java.util.function.BiFunction<Level, BlockPos, Boolean>)((bru, fx) -> {
            if (!bru.getBlockState(fx).is(bul)) {
                return false;
            }
            return bft.distanceToSqr(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5) <= 64.0;
        }), true);
    }
    
    public MenuType<?> getType() {
        if (this.menuType == null) {
            throw new UnsupportedOperationException("Unable to construct this menu by type");
        }
        return this.menuType;
    }
    
    protected static void checkContainerSize(final Container aok, final int integer) {
        final int integer2 = aok.getContainerSize();
        if (integer2 < integer) {
            throw new IllegalArgumentException(new StringBuilder().append("Container size ").append(integer2).append(" is smaller than expected ").append(integer).toString());
        }
    }
    
    protected static void checkContainerDataCount(final ContainerData bii, final int integer) {
        final int integer2 = bii.getCount();
        if (integer2 < integer) {
            throw new IllegalArgumentException(new StringBuilder().append("Container data count ").append(integer2).append(" is smaller than expected ").append(integer).toString());
        }
    }
    
    protected Slot addSlot(final Slot bjo) {
        bjo.index = this.slots.size();
        this.slots.add(bjo);
        this.lastSlots.add(ItemStack.EMPTY);
        return bjo;
    }
    
    protected DataSlot addDataSlot(final DataSlot bin) {
        this.dataSlots.add(bin);
        return bin;
    }
    
    protected void addDataSlots(final ContainerData bii) {
        for (int integer3 = 0; integer3 < bii.getCount(); ++integer3) {
            this.addDataSlot(DataSlot.forContainer(bii, integer3));
        }
    }
    
    public void addSlotListener(final ContainerListener bik) {
        if (this.containerListeners.contains(bik)) {
            return;
        }
        this.containerListeners.add(bik);
        bik.refreshContainer(this, this.getItems());
        this.broadcastChanges();
    }
    
    public void removeSlotListener(final ContainerListener bik) {
        this.containerListeners.remove(bik);
    }
    
    public NonNullList<ItemStack> getItems() {
        final NonNullList<ItemStack> gj2 = NonNullList.<ItemStack>create();
        for (int integer3 = 0; integer3 < this.slots.size(); ++integer3) {
            gj2.add(((Slot)this.slots.get(integer3)).getItem());
        }
        return gj2;
    }
    
    public void broadcastChanges() {
        for (int integer2 = 0; integer2 < this.slots.size(); ++integer2) {
            final ItemStack bly3 = ((Slot)this.slots.get(integer2)).getItem();
            final ItemStack bly4 = this.lastSlots.get(integer2);
            if (!ItemStack.matches(bly4, bly3)) {
                final ItemStack bly5 = bly3.copy();
                this.lastSlots.set(integer2, bly5);
                for (final ContainerListener bik7 : this.containerListeners) {
                    bik7.slotChanged(this, integer2, bly5);
                }
            }
        }
        for (int integer2 = 0; integer2 < this.dataSlots.size(); ++integer2) {
            final DataSlot bin3 = (DataSlot)this.dataSlots.get(integer2);
            if (bin3.checkAndClearUpdateFlag()) {
                for (final ContainerListener bik8 : this.containerListeners) {
                    bik8.setContainerData(this, integer2, bin3.get());
                }
            }
        }
    }
    
    public boolean clickMenuButton(final Player bft, final int integer) {
        return false;
    }
    
    public Slot getSlot(final int integer) {
        return (Slot)this.slots.get(integer);
    }
    
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        final Slot bjo4 = (Slot)this.slots.get(integer);
        if (bjo4 != null) {
            return bjo4.getItem();
        }
        return ItemStack.EMPTY;
    }
    
    public ItemStack clicked(final int integer1, final int integer2, final ClickType bih, final Player bft) {
        try {
            return this.doClick(integer1, integer2, bih, bft);
        }
        catch (Exception exception6) {
            final CrashReport l7 = CrashReport.forThrowable((Throwable)exception6, "Container click");
            final CrashReportCategory m8 = l7.addCategory("Click info");
            m8.setDetail("Menu Type", (CrashReportDetail<String>)(() -> (this.menuType != null) ? Registry.MENU.getKey(this.menuType).toString() : "<no type>"));
            m8.setDetail("Menu Class", (CrashReportDetail<String>)(() -> this.getClass().getCanonicalName()));
            m8.setDetail("Slot Count", this.slots.size());
            m8.setDetail("Slot", integer1);
            m8.setDetail("Button", integer2);
            m8.setDetail("Type", bih);
            throw new ReportedException(l7);
        }
    }
    
    private ItemStack doClick(final int integer1, final int integer2, final ClickType bih, final Player bft) {
        ItemStack bly6 = ItemStack.EMPTY;
        final Inventory bfs7 = bft.inventory;
        if (bih == ClickType.QUICK_CRAFT) {
            final int integer3 = this.quickcraftStatus;
            this.quickcraftStatus = getQuickcraftHeader(integer2);
            if ((integer3 != 1 || this.quickcraftStatus != 2) && integer3 != this.quickcraftStatus) {
                this.resetQuickCraft();
            }
            else if (bfs7.getCarried().isEmpty()) {
                this.resetQuickCraft();
            }
            else if (this.quickcraftStatus == 0) {
                this.quickcraftType = getQuickcraftType(integer2);
                if (isValidQuickcraftType(this.quickcraftType, bft)) {
                    this.quickcraftStatus = 1;
                    this.quickcraftSlots.clear();
                }
                else {
                    this.resetQuickCraft();
                }
            }
            else if (this.quickcraftStatus == 1) {
                final Slot bjo9 = (Slot)this.slots.get(integer1);
                final ItemStack bly7 = bfs7.getCarried();
                if (bjo9 != null && canItemQuickReplace(bjo9, bly7, true) && bjo9.mayPlace(bly7) && (this.quickcraftType == 2 || bly7.getCount() > this.quickcraftSlots.size()) && this.canDragTo(bjo9)) {
                    this.quickcraftSlots.add(bjo9);
                }
            }
            else if (this.quickcraftStatus == 2) {
                if (!this.quickcraftSlots.isEmpty()) {
                    final ItemStack bly8 = bfs7.getCarried().copy();
                    int integer4 = bfs7.getCarried().getCount();
                    for (final Slot bjo10 : this.quickcraftSlots) {
                        final ItemStack bly9 = bfs7.getCarried();
                        if (bjo10 != null && canItemQuickReplace(bjo10, bly9, true) && bjo10.mayPlace(bly9) && (this.quickcraftType == 2 || bly9.getCount() >= this.quickcraftSlots.size()) && this.canDragTo(bjo10)) {
                            final ItemStack bly10 = bly8.copy();
                            final int integer5 = bjo10.hasItem() ? bjo10.getItem().getCount() : 0;
                            getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, bly10, integer5);
                            final int integer6 = Math.min(bly10.getMaxStackSize(), bjo10.getMaxStackSize(bly10));
                            if (bly10.getCount() > integer6) {
                                bly10.setCount(integer6);
                            }
                            integer4 -= bly10.getCount() - integer5;
                            bjo10.set(bly10);
                        }
                    }
                    bly8.setCount(integer4);
                    bfs7.setCarried(bly8);
                }
                this.resetQuickCraft();
            }
            else {
                this.resetQuickCraft();
            }
        }
        else if (this.quickcraftStatus != 0) {
            this.resetQuickCraft();
        }
        else if ((bih == ClickType.PICKUP || bih == ClickType.QUICK_MOVE) && (integer2 == 0 || integer2 == 1)) {
            if (integer1 == -999) {
                if (!bfs7.getCarried().isEmpty()) {
                    if (integer2 == 0) {
                        bft.drop(bfs7.getCarried(), true);
                        bfs7.setCarried(ItemStack.EMPTY);
                    }
                    if (integer2 == 1) {
                        bft.drop(bfs7.getCarried().split(1), true);
                    }
                }
            }
            else if (bih == ClickType.QUICK_MOVE) {
                if (integer1 < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot bjo11 = (Slot)this.slots.get(integer1);
                if (bjo11 == null || !bjo11.mayPickup(bft)) {
                    return ItemStack.EMPTY;
                }
                for (ItemStack bly8 = this.quickMoveStack(bft, integer1); !bly8.isEmpty() && ItemStack.isSame(bjo11.getItem(), bly8); bly8 = this.quickMoveStack(bft, integer1)) {
                    bly6 = bly8.copy();
                }
            }
            else {
                if (integer1 < 0) {
                    return ItemStack.EMPTY;
                }
                final Slot bjo11 = (Slot)this.slots.get(integer1);
                if (bjo11 != null) {
                    ItemStack bly8 = bjo11.getItem();
                    final ItemStack bly7 = bfs7.getCarried();
                    if (!bly8.isEmpty()) {
                        bly6 = bly8.copy();
                    }
                    if (bly8.isEmpty()) {
                        if (!bly7.isEmpty() && bjo11.mayPlace(bly7)) {
                            int integer7 = (integer2 == 0) ? bly7.getCount() : 1;
                            if (integer7 > bjo11.getMaxStackSize(bly7)) {
                                integer7 = bjo11.getMaxStackSize(bly7);
                            }
                            bjo11.set(bly7.split(integer7));
                        }
                    }
                    else if (bjo11.mayPickup(bft)) {
                        if (bly7.isEmpty()) {
                            if (bly8.isEmpty()) {
                                bjo11.set(ItemStack.EMPTY);
                                bfs7.setCarried(ItemStack.EMPTY);
                            }
                            else {
                                final int integer7 = (integer2 == 0) ? bly8.getCount() : ((bly8.getCount() + 1) / 2);
                                bfs7.setCarried(bjo11.remove(integer7));
                                if (bly8.isEmpty()) {
                                    bjo11.set(ItemStack.EMPTY);
                                }
                                bjo11.onTake(bft, bfs7.getCarried());
                            }
                        }
                        else if (bjo11.mayPlace(bly7)) {
                            if (consideredTheSameItem(bly8, bly7)) {
                                int integer7 = (integer2 == 0) ? bly7.getCount() : 1;
                                if (integer7 > bjo11.getMaxStackSize(bly7) - bly8.getCount()) {
                                    integer7 = bjo11.getMaxStackSize(bly7) - bly8.getCount();
                                }
                                if (integer7 > bly7.getMaxStackSize() - bly8.getCount()) {
                                    integer7 = bly7.getMaxStackSize() - bly8.getCount();
                                }
                                bly7.shrink(integer7);
                                bly8.grow(integer7);
                            }
                            else if (bly7.getCount() <= bjo11.getMaxStackSize(bly7)) {
                                bjo11.set(bly7);
                                bfs7.setCarried(bly8);
                            }
                        }
                        else if (bly7.getMaxStackSize() > 1 && consideredTheSameItem(bly8, bly7) && !bly8.isEmpty()) {
                            final int integer7 = bly8.getCount();
                            if (integer7 + bly7.getCount() <= bly7.getMaxStackSize()) {
                                bly7.grow(integer7);
                                bly8 = bjo11.remove(integer7);
                                if (bly8.isEmpty()) {
                                    bjo11.set(ItemStack.EMPTY);
                                }
                                bjo11.onTake(bft, bfs7.getCarried());
                            }
                        }
                    }
                    bjo11.setChanged();
                }
            }
        }
        else if (bih == ClickType.SWAP) {
            final Slot bjo11 = (Slot)this.slots.get(integer1);
            final ItemStack bly8 = bfs7.getItem(integer2);
            final ItemStack bly7 = bjo11.getItem();
            if (!bly8.isEmpty() || !bly7.isEmpty()) {
                if (bly8.isEmpty()) {
                    if (bjo11.mayPickup(bft)) {
                        bfs7.setItem(integer2, bly7);
                        bjo11.onSwapCraft(bly7.getCount());
                        bjo11.set(ItemStack.EMPTY);
                        bjo11.onTake(bft, bly7);
                    }
                }
                else if (bly7.isEmpty()) {
                    if (bjo11.mayPlace(bly8)) {
                        final int integer7 = bjo11.getMaxStackSize(bly8);
                        if (bly8.getCount() > integer7) {
                            bjo11.set(bly8.split(integer7));
                        }
                        else {
                            bjo11.set(bly8);
                            bfs7.setItem(integer2, ItemStack.EMPTY);
                        }
                    }
                }
                else if (bjo11.mayPickup(bft) && bjo11.mayPlace(bly8)) {
                    final int integer7 = bjo11.getMaxStackSize(bly8);
                    if (bly8.getCount() > integer7) {
                        bjo11.set(bly8.split(integer7));
                        bjo11.onTake(bft, bly7);
                        if (!bfs7.add(bly7)) {
                            bft.drop(bly7, true);
                        }
                    }
                    else {
                        bjo11.set(bly8);
                        bfs7.setItem(integer2, bly7);
                        bjo11.onTake(bft, bly7);
                    }
                }
            }
        }
        else if (bih == ClickType.CLONE && bft.abilities.instabuild && bfs7.getCarried().isEmpty() && integer1 >= 0) {
            final Slot bjo11 = (Slot)this.slots.get(integer1);
            if (bjo11 != null && bjo11.hasItem()) {
                final ItemStack bly8 = bjo11.getItem().copy();
                bly8.setCount(bly8.getMaxStackSize());
                bfs7.setCarried(bly8);
            }
        }
        else if (bih == ClickType.THROW && bfs7.getCarried().isEmpty() && integer1 >= 0) {
            final Slot bjo11 = (Slot)this.slots.get(integer1);
            if (bjo11 != null && bjo11.hasItem() && bjo11.mayPickup(bft)) {
                final ItemStack bly8 = bjo11.remove((integer2 == 0) ? 1 : bjo11.getItem().getCount());
                bjo11.onTake(bft, bly8);
                bft.drop(bly8, true);
            }
        }
        else if (bih == ClickType.PICKUP_ALL && integer1 >= 0) {
            final Slot bjo11 = (Slot)this.slots.get(integer1);
            final ItemStack bly8 = bfs7.getCarried();
            if (!bly8.isEmpty() && (bjo11 == null || !bjo11.hasItem() || !bjo11.mayPickup(bft))) {
                final int integer4 = (integer2 == 0) ? 0 : (this.slots.size() - 1);
                final int integer7 = (integer2 == 0) ? 1 : -1;
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    for (int integer9 = integer4; integer9 >= 0 && integer9 < this.slots.size() && bly8.getCount() < bly8.getMaxStackSize(); integer9 += integer7) {
                        final Slot bjo12 = (Slot)this.slots.get(integer9);
                        if (bjo12.hasItem() && canItemQuickReplace(bjo12, bly8, true) && bjo12.mayPickup(bft) && this.canTakeItemForPickAll(bly8, bjo12)) {
                            final ItemStack bly11 = bjo12.getItem();
                            if (integer8 != 0 || bly11.getCount() != bly11.getMaxStackSize()) {
                                final int integer6 = Math.min(bly8.getMaxStackSize() - bly8.getCount(), bly11.getCount());
                                final ItemStack bly12 = bjo12.remove(integer6);
                                bly8.grow(integer6);
                                if (bly12.isEmpty()) {
                                    bjo12.set(ItemStack.EMPTY);
                                }
                                bjo12.onTake(bft, bly12);
                            }
                        }
                    }
                }
            }
            this.broadcastChanges();
        }
        return bly6;
    }
    
    public static boolean consideredTheSameItem(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && ItemStack.tagMatches(bly1, bly2);
    }
    
    public boolean canTakeItemForPickAll(final ItemStack bly, final Slot bjo) {
        return true;
    }
    
    public void removed(final Player bft) {
        final Inventory bfs3 = bft.inventory;
        if (!bfs3.getCarried().isEmpty()) {
            bft.drop(bfs3.getCarried(), false);
            bfs3.setCarried(ItemStack.EMPTY);
        }
    }
    
    protected void clearContainer(final Player bft, final Level bru, final Container aok) {
        if (!bft.isAlive() || (bft instanceof ServerPlayer && ((ServerPlayer)bft).hasDisconnected())) {
            for (int integer5 = 0; integer5 < aok.getContainerSize(); ++integer5) {
                bft.drop(aok.removeItemNoUpdate(integer5), false);
            }
            return;
        }
        for (int integer5 = 0; integer5 < aok.getContainerSize(); ++integer5) {
            bft.inventory.placeItemBackInInventory(bru, aok.removeItemNoUpdate(integer5));
        }
    }
    
    public void slotsChanged(final Container aok) {
        this.broadcastChanges();
    }
    
    public void setItem(final int integer, final ItemStack bly) {
        this.getSlot(integer).set(bly);
    }
    
    public void setAll(final List<ItemStack> list) {
        for (int integer3 = 0; integer3 < list.size(); ++integer3) {
            this.getSlot(integer3).set((ItemStack)list.get(integer3));
        }
    }
    
    public void setData(final int integer1, final int integer2) {
        ((DataSlot)this.dataSlots.get(integer1)).set(integer2);
    }
    
    public short backup(final Inventory bfs) {
        return (short)(++this.changeUid);
    }
    
    public boolean isSynched(final Player bft) {
        return !this.unSynchedPlayers.contains(bft);
    }
    
    public void setSynched(final Player bft, final boolean boolean2) {
        if (boolean2) {
            this.unSynchedPlayers.remove(bft);
        }
        else {
            this.unSynchedPlayers.add(bft);
        }
    }
    
    public abstract boolean stillValid(final Player bft);
    
    protected boolean moveItemStackTo(final ItemStack bly, final int integer2, final int integer3, final boolean boolean4) {
        boolean boolean5 = false;
        int integer4 = integer2;
        if (boolean4) {
            integer4 = integer3 - 1;
        }
        if (bly.isStackable()) {
            while (!bly.isEmpty()) {
                if (boolean4) {
                    if (integer4 < integer2) {
                        break;
                    }
                }
                else if (integer4 >= integer3) {
                    break;
                }
                final Slot bjo8 = (Slot)this.slots.get(integer4);
                final ItemStack bly2 = bjo8.getItem();
                if (!bly2.isEmpty() && consideredTheSameItem(bly, bly2)) {
                    final int integer5 = bly2.getCount() + bly.getCount();
                    if (integer5 <= bly.getMaxStackSize()) {
                        bly.setCount(0);
                        bly2.setCount(integer5);
                        bjo8.setChanged();
                        boolean5 = true;
                    }
                    else if (bly2.getCount() < bly.getMaxStackSize()) {
                        bly.shrink(bly.getMaxStackSize() - bly2.getCount());
                        bly2.setCount(bly.getMaxStackSize());
                        bjo8.setChanged();
                        boolean5 = true;
                    }
                }
                if (boolean4) {
                    --integer4;
                }
                else {
                    ++integer4;
                }
            }
        }
        if (!bly.isEmpty()) {
            if (boolean4) {
                integer4 = integer3 - 1;
            }
            else {
                integer4 = integer2;
            }
            while (true) {
                if (boolean4) {
                    if (integer4 < integer2) {
                        break;
                    }
                }
                else if (integer4 >= integer3) {
                    break;
                }
                final Slot bjo8 = (Slot)this.slots.get(integer4);
                final ItemStack bly2 = bjo8.getItem();
                if (bly2.isEmpty() && bjo8.mayPlace(bly)) {
                    if (bly.getCount() > bjo8.getMaxStackSize()) {
                        bjo8.set(bly.split(bjo8.getMaxStackSize()));
                    }
                    else {
                        bjo8.set(bly.split(bly.getCount()));
                    }
                    bjo8.setChanged();
                    boolean5 = true;
                    break;
                }
                if (boolean4) {
                    --integer4;
                }
                else {
                    ++integer4;
                }
            }
        }
        return boolean5;
    }
    
    public static int getQuickcraftType(final int integer) {
        return integer >> 2 & 0x3;
    }
    
    public static int getQuickcraftHeader(final int integer) {
        return integer & 0x3;
    }
    
    public static int getQuickcraftMask(final int integer1, final int integer2) {
        return (integer1 & 0x3) | (integer2 & 0x3) << 2;
    }
    
    public static boolean isValidQuickcraftType(final int integer, final Player bft) {
        return integer == 0 || integer == 1 || (integer == 2 && bft.abilities.instabuild);
    }
    
    protected void resetQuickCraft() {
        this.quickcraftStatus = 0;
        this.quickcraftSlots.clear();
    }
    
    public static boolean canItemQuickReplace(@Nullable final Slot bjo, final ItemStack bly, final boolean boolean3) {
        final boolean boolean4 = bjo == null || !bjo.hasItem();
        if (!boolean4 && bly.sameItem(bjo.getItem()) && ItemStack.tagMatches(bjo.getItem(), bly)) {
            return bjo.getItem().getCount() + (boolean3 ? 0 : bly.getCount()) <= bly.getMaxStackSize();
        }
        return boolean4;
    }
    
    public static void getQuickCraftSlotCount(final Set<Slot> set, final int integer2, final ItemStack bly, final int integer4) {
        switch (integer2) {
            case 0: {
                bly.setCount(Mth.floor(bly.getCount() / (float)set.size()));
                break;
            }
            case 1: {
                bly.setCount(1);
                break;
            }
            case 2: {
                bly.setCount(bly.getItem().getMaxStackSize());
                break;
            }
        }
        bly.grow(integer4);
    }
    
    public boolean canDragTo(final Slot bjo) {
        return true;
    }
    
    public static int getRedstoneSignalFromBlockEntity(@Nullable final BlockEntity ccg) {
        if (ccg instanceof Container) {
            return getRedstoneSignalFromContainer((Container)ccg);
        }
        return 0;
    }
    
    public static int getRedstoneSignalFromContainer(@Nullable final Container aok) {
        if (aok == null) {
            return 0;
        }
        int integer2 = 0;
        float float3 = 0.0f;
        for (int integer3 = 0; integer3 < aok.getContainerSize(); ++integer3) {
            final ItemStack bly5 = aok.getItem(integer3);
            if (!bly5.isEmpty()) {
                float3 += bly5.getCount() / (float)Math.min(aok.getMaxStackSize(), bly5.getMaxStackSize());
                ++integer2;
            }
        }
        float3 /= aok.getContainerSize();
        return Mth.floor(float3 * 14.0f) + ((integer2 > 0) ? 1 : 0);
    }
}
