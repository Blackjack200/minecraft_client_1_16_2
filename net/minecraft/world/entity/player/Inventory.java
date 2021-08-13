package net.minecraft.world.entity.player;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.Tag;
import java.util.function.Consumer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.ContainerHelper;
import java.util.function.Predicate;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Nameable;
import net.minecraft.world.Container;

public class Inventory implements Container, Nameable {
    public final NonNullList<ItemStack> items;
    public final NonNullList<ItemStack> armor;
    public final NonNullList<ItemStack> offhand;
    private final List<NonNullList<ItemStack>> compartments;
    public int selected;
    public final Player player;
    private ItemStack carried;
    private int timesChanged;
    
    public Inventory(final Player bft) {
        this.items = NonNullList.<ItemStack>withSize(36, ItemStack.EMPTY);
        this.armor = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
        this.offhand = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
        this.compartments = (List<NonNullList<ItemStack>>)ImmutableList.of(this.items, this.armor, this.offhand);
        this.carried = ItemStack.EMPTY;
        this.player = bft;
    }
    
    public ItemStack getSelected() {
        if (isHotbarSlot(this.selected)) {
            return this.items.get(this.selected);
        }
        return ItemStack.EMPTY;
    }
    
    public static int getSelectionSize() {
        return 9;
    }
    
    private boolean hasRemainingSpaceForItem(final ItemStack bly1, final ItemStack bly2) {
        return !bly1.isEmpty() && this.isSameItem(bly1, bly2) && bly1.isStackable() && bly1.getCount() < bly1.getMaxStackSize() && bly1.getCount() < this.getMaxStackSize();
    }
    
    private boolean isSameItem(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && ItemStack.tagMatches(bly1, bly2);
    }
    
    public int getFreeSlot() {
        for (int integer2 = 0; integer2 < this.items.size(); ++integer2) {
            if (this.items.get(integer2).isEmpty()) {
                return integer2;
            }
        }
        return -1;
    }
    
    public void setPickedItem(final ItemStack bly) {
        final int integer3 = this.findSlotMatchingItem(bly);
        if (isHotbarSlot(integer3)) {
            this.selected = integer3;
            return;
        }
        if (integer3 == -1) {
            this.selected = this.getSuitableHotbarSlot();
            if (!this.items.get(this.selected).isEmpty()) {
                final int integer4 = this.getFreeSlot();
                if (integer4 != -1) {
                    this.items.set(integer4, this.items.get(this.selected));
                }
            }
            this.items.set(this.selected, bly);
        }
        else {
            this.pickSlot(integer3);
        }
    }
    
    public void pickSlot(final int integer) {
        this.selected = this.getSuitableHotbarSlot();
        final ItemStack bly3 = this.items.get(this.selected);
        this.items.set(this.selected, this.items.get(integer));
        this.items.set(integer, bly3);
    }
    
    public static boolean isHotbarSlot(final int integer) {
        return integer >= 0 && integer < 9;
    }
    
    public int findSlotMatchingItem(final ItemStack bly) {
        for (int integer3 = 0; integer3 < this.items.size(); ++integer3) {
            if (!this.items.get(integer3).isEmpty() && this.isSameItem(bly, this.items.get(integer3))) {
                return integer3;
            }
        }
        return -1;
    }
    
    public int findSlotMatchingUnusedItem(final ItemStack bly) {
        for (int integer3 = 0; integer3 < this.items.size(); ++integer3) {
            final ItemStack bly2 = this.items.get(integer3);
            if (!this.items.get(integer3).isEmpty() && this.isSameItem(bly, this.items.get(integer3)) && !this.items.get(integer3).isDamaged() && !bly2.isEnchanted() && !bly2.hasCustomHoverName()) {
                return integer3;
            }
        }
        return -1;
    }
    
    public int getSuitableHotbarSlot() {
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            final int integer3 = (this.selected + integer2) % 9;
            if (this.items.get(integer3).isEmpty()) {
                return integer3;
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            final int integer3 = (this.selected + integer2) % 9;
            if (!this.items.get(integer3).isEnchanted()) {
                return integer3;
            }
        }
        return this.selected;
    }
    
    public void swapPaint(double double1) {
        if (double1 > 0.0) {
            double1 = 1.0;
        }
        if (double1 < 0.0) {
            double1 = -1.0;
        }
        this.selected -= (int)double1;
        while (this.selected < 0) {
            this.selected += 9;
        }
        while (this.selected >= 9) {
            this.selected -= 9;
        }
    }
    
    public int clearOrCountMatchingItems(final Predicate<ItemStack> predicate, final int integer, final Container aok) {
        int integer2 = 0;
        final boolean boolean6 = integer == 0;
        integer2 += ContainerHelper.clearOrCountMatchingItems(this, predicate, integer - integer2, boolean6);
        integer2 += ContainerHelper.clearOrCountMatchingItems(aok, predicate, integer - integer2, boolean6);
        integer2 += ContainerHelper.clearOrCountMatchingItems(this.carried, predicate, integer - integer2, boolean6);
        if (this.carried.isEmpty()) {
            this.carried = ItemStack.EMPTY;
        }
        return integer2;
    }
    
    private int addResource(final ItemStack bly) {
        int integer3 = this.getSlotWithRemainingSpace(bly);
        if (integer3 == -1) {
            integer3 = this.getFreeSlot();
        }
        if (integer3 == -1) {
            return bly.getCount();
        }
        return this.addResource(integer3, bly);
    }
    
    private int addResource(final int integer, final ItemStack bly) {
        final Item blu4 = bly.getItem();
        int integer2 = bly.getCount();
        ItemStack bly2 = this.getItem(integer);
        if (bly2.isEmpty()) {
            bly2 = new ItemStack(blu4, 0);
            if (bly.hasTag()) {
                bly2.setTag(bly.getTag().copy());
            }
            this.setItem(integer, bly2);
        }
        int integer3 = integer2;
        if (integer3 > bly2.getMaxStackSize() - bly2.getCount()) {
            integer3 = bly2.getMaxStackSize() - bly2.getCount();
        }
        if (integer3 > this.getMaxStackSize() - bly2.getCount()) {
            integer3 = this.getMaxStackSize() - bly2.getCount();
        }
        if (integer3 == 0) {
            return integer2;
        }
        integer2 -= integer3;
        bly2.grow(integer3);
        bly2.setPopTime(5);
        return integer2;
    }
    
    public int getSlotWithRemainingSpace(final ItemStack bly) {
        if (this.hasRemainingSpaceForItem(this.getItem(this.selected), bly)) {
            return this.selected;
        }
        if (this.hasRemainingSpaceForItem(this.getItem(40), bly)) {
            return 40;
        }
        for (int integer3 = 0; integer3 < this.items.size(); ++integer3) {
            if (this.hasRemainingSpaceForItem(this.items.get(integer3), bly)) {
                return integer3;
            }
        }
        return -1;
    }
    
    public void tick() {
        for (final NonNullList<ItemStack> gj3 : this.compartments) {
            for (int integer4 = 0; integer4 < gj3.size(); ++integer4) {
                if (!gj3.get(integer4).isEmpty()) {
                    gj3.get(integer4).inventoryTick(this.player.level, this.player, integer4, this.selected == integer4);
                }
            }
        }
    }
    
    public boolean add(final ItemStack bly) {
        return this.add(-1, bly);
    }
    
    public boolean add(int integer, final ItemStack bly) {
        if (bly.isEmpty()) {
            return false;
        }
        try {
            if (!bly.isDamaged()) {
                int integer2;
                do {
                    integer2 = bly.getCount();
                    if (integer == -1) {
                        bly.setCount(this.addResource(bly));
                    }
                    else {
                        bly.setCount(this.addResource(integer, bly));
                    }
                } while (!bly.isEmpty() && bly.getCount() < integer2);
                if (bly.getCount() == integer2 && this.player.abilities.instabuild) {
                    bly.setCount(0);
                    return true;
                }
                return bly.getCount() < integer2;
            }
            else {
                if (integer == -1) {
                    integer = this.getFreeSlot();
                }
                if (integer >= 0) {
                    this.items.set(integer, bly.copy());
                    this.items.get(integer).setPopTime(5);
                    bly.setCount(0);
                    return true;
                }
                if (this.player.abilities.instabuild) {
                    bly.setCount(0);
                    return true;
                }
                return false;
            }
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, "Adding item to inventory");
            final CrashReportCategory m6 = l5.addCategory("Item being added");
            m6.setDetail("Item ID", Item.getId(bly.getItem()));
            m6.setDetail("Item data", bly.getDamageValue());
            m6.setDetail("Item name", (CrashReportDetail<String>)(() -> bly.getHoverName().getString()));
            throw new ReportedException(l5);
        }
    }
    
    public void placeItemBackInInventory(final Level bru, final ItemStack bly) {
        if (bru.isClientSide) {
            return;
        }
        while (!bly.isEmpty()) {
            int integer4 = this.getSlotWithRemainingSpace(bly);
            if (integer4 == -1) {
                integer4 = this.getFreeSlot();
            }
            if (integer4 == -1) {
                this.player.drop(bly, false);
                break;
            }
            final int integer5 = bly.getMaxStackSize() - this.getItem(integer4).getCount();
            if (!this.add(integer4, bly.split(integer5))) {
                continue;
            }
            ((ServerPlayer)this.player).connection.send(new ClientboundContainerSetSlotPacket(-2, integer4, this.getItem(integer4)));
        }
    }
    
    public ItemStack removeItem(int integer1, final int integer2) {
        List<ItemStack> list4 = null;
        for (final NonNullList<ItemStack> gj6 : this.compartments) {
            if (integer1 < gj6.size()) {
                list4 = (List<ItemStack>)gj6;
                break;
            }
            integer1 -= gj6.size();
        }
        if (list4 != null && !((ItemStack)list4.get(integer1)).isEmpty()) {
            return ContainerHelper.removeItem(list4, integer1, integer2);
        }
        return ItemStack.EMPTY;
    }
    
    public void removeItem(final ItemStack bly) {
        for (final NonNullList<ItemStack> gj4 : this.compartments) {
            for (int integer5 = 0; integer5 < gj4.size(); ++integer5) {
                if (gj4.get(integer5) == bly) {
                    gj4.set(integer5, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }
    
    public ItemStack removeItemNoUpdate(int integer) {
        NonNullList<ItemStack> gj3 = null;
        for (final NonNullList<ItemStack> gj4 : this.compartments) {
            if (integer < gj4.size()) {
                gj3 = gj4;
                break;
            }
            integer -= gj4.size();
        }
        if (gj3 != null && !gj3.get(integer).isEmpty()) {
            final ItemStack bly4 = gj3.get(integer);
            gj3.set(integer, ItemStack.EMPTY);
            return bly4;
        }
        return ItemStack.EMPTY;
    }
    
    public void setItem(int integer, final ItemStack bly) {
        NonNullList<ItemStack> gj4 = null;
        for (final NonNullList<ItemStack> gj5 : this.compartments) {
            if (integer < gj5.size()) {
                gj4 = gj5;
                break;
            }
            integer -= gj5.size();
        }
        if (gj4 != null) {
            gj4.set(integer, bly);
        }
    }
    
    public float getDestroySpeed(final BlockState cee) {
        return this.items.get(this.selected).getDestroySpeed(cee);
    }
    
    public ListTag save(final ListTag mj) {
        for (int integer3 = 0; integer3 < this.items.size(); ++integer3) {
            if (!this.items.get(integer3).isEmpty()) {
                final CompoundTag md4 = new CompoundTag();
                md4.putByte("Slot", (byte)integer3);
                this.items.get(integer3).save(md4);
                mj.add(md4);
            }
        }
        for (int integer3 = 0; integer3 < this.armor.size(); ++integer3) {
            if (!this.armor.get(integer3).isEmpty()) {
                final CompoundTag md4 = new CompoundTag();
                md4.putByte("Slot", (byte)(integer3 + 100));
                this.armor.get(integer3).save(md4);
                mj.add(md4);
            }
        }
        for (int integer3 = 0; integer3 < this.offhand.size(); ++integer3) {
            if (!this.offhand.get(integer3).isEmpty()) {
                final CompoundTag md4 = new CompoundTag();
                md4.putByte("Slot", (byte)(integer3 + 150));
                this.offhand.get(integer3).save(md4);
                mj.add(md4);
            }
        }
        return mj;
    }
    
    public void load(final ListTag mj) {
        this.items.clear();
        this.armor.clear();
        this.offhand.clear();
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            final int integer4 = md4.getByte("Slot") & 0xFF;
            final ItemStack bly6 = ItemStack.of(md4);
            if (!bly6.isEmpty()) {
                if (integer4 >= 0 && integer4 < this.items.size()) {
                    this.items.set(integer4, bly6);
                }
                else if (integer4 >= 100 && integer4 < this.armor.size() + 100) {
                    this.armor.set(integer4 - 100, bly6);
                }
                else if (integer4 >= 150 && integer4 < this.offhand.size() + 150) {
                    this.offhand.set(integer4 - 150, bly6);
                }
            }
        }
    }
    
    public int getContainerSize() {
        return this.items.size() + this.armor.size() + this.offhand.size();
    }
    
    public boolean isEmpty() {
        for (final ItemStack bly3 : this.items) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        for (final ItemStack bly3 : this.armor) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        for (final ItemStack bly3 : this.offhand) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public ItemStack getItem(int integer) {
        List<ItemStack> list3 = null;
        for (final NonNullList<ItemStack> gj5 : this.compartments) {
            if (integer < gj5.size()) {
                list3 = (List<ItemStack>)gj5;
                break;
            }
            integer -= gj5.size();
        }
        return (ItemStack)((list3 == null) ? ItemStack.EMPTY : list3.get(integer));
    }
    
    public Component getName() {
        return new TranslatableComponent("container.inventory");
    }
    
    public ItemStack getArmor(final int integer) {
        return this.armor.get(integer);
    }
    
    public void hurtArmor(final DamageSource aph, float float2) {
        if (float2 <= 0.0f) {
            return;
        }
        float2 /= 4.0f;
        if (float2 < 1.0f) {
            float2 = 1.0f;
        }
        for (int integer4 = 0; integer4 < this.armor.size(); ++integer4) {
            final ItemStack bly5 = this.armor.get(integer4);
            if (!aph.isFire() || !bly5.getItem().isFireResistant()) {
                if (bly5.getItem() instanceof ArmorItem) {
                    final int integer5 = integer4;
                    bly5.<Player>hurtAndBreak((int)float2, this.player, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, integer5))));
                }
            }
        }
    }
    
    public void dropAll() {
        for (final List<ItemStack> list3 : this.compartments) {
            for (int integer4 = 0; integer4 < list3.size(); ++integer4) {
                final ItemStack bly5 = (ItemStack)list3.get(integer4);
                if (!bly5.isEmpty()) {
                    this.player.drop(bly5, true, false);
                    list3.set(integer4, ItemStack.EMPTY);
                }
            }
        }
    }
    
    public void setChanged() {
        ++this.timesChanged;
    }
    
    public int getTimesChanged() {
        return this.timesChanged;
    }
    
    public void setCarried(final ItemStack bly) {
        this.carried = bly;
    }
    
    public ItemStack getCarried() {
        return this.carried;
    }
    
    public boolean stillValid(final Player bft) {
        return !this.player.removed && bft.distanceToSqr(this.player) <= 64.0;
    }
    
    public boolean contains(final ItemStack bly) {
        for (final List<ItemStack> list4 : this.compartments) {
            for (final ItemStack bly2 : list4) {
                if (!bly2.isEmpty() && bly2.sameItem(bly)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean contains(final Tag<Item> aej) {
        for (final List<ItemStack> list4 : this.compartments) {
            for (final ItemStack bly6 : list4) {
                if (!bly6.isEmpty() && aej.contains(bly6.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void replaceWith(final Inventory bfs) {
        for (int integer3 = 0; integer3 < this.getContainerSize(); ++integer3) {
            this.setItem(integer3, bfs.getItem(integer3));
        }
        this.selected = bfs.selected;
    }
    
    public void clearContent() {
        for (final List<ItemStack> list3 : this.compartments) {
            list3.clear();
        }
    }
    
    public void fillStackedContents(final StackedContents bfv) {
        for (final ItemStack bly4 : this.items) {
            bfv.accountSimpleStack(bly4);
        }
    }
}
