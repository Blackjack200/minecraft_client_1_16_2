package net.minecraft.world;

import java.util.function.Predicate;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class ContainerHelper {
    public static ItemStack removeItem(final List<ItemStack> list, final int integer2, final int integer3) {
        if (integer2 < 0 || integer2 >= list.size() || ((ItemStack)list.get(integer2)).isEmpty() || integer3 <= 0) {
            return ItemStack.EMPTY;
        }
        return ((ItemStack)list.get(integer2)).split(integer3);
    }
    
    public static ItemStack takeItem(final List<ItemStack> list, final int integer) {
        if (integer < 0 || integer >= list.size()) {
            return ItemStack.EMPTY;
        }
        return (ItemStack)list.set(integer, ItemStack.EMPTY);
    }
    
    public static CompoundTag saveAllItems(final CompoundTag md, final NonNullList<ItemStack> gj) {
        return saveAllItems(md, gj, true);
    }
    
    public static CompoundTag saveAllItems(final CompoundTag md, final NonNullList<ItemStack> gj, final boolean boolean3) {
        final ListTag mj4 = new ListTag();
        for (int integer5 = 0; integer5 < gj.size(); ++integer5) {
            final ItemStack bly6 = gj.get(integer5);
            if (!bly6.isEmpty()) {
                final CompoundTag md2 = new CompoundTag();
                md2.putByte("Slot", (byte)integer5);
                bly6.save(md2);
                mj4.add(md2);
            }
        }
        if (!mj4.isEmpty() || boolean3) {
            md.put("Items", (Tag)mj4);
        }
        return md;
    }
    
    public static void loadAllItems(final CompoundTag md, final NonNullList<ItemStack> gj) {
        final ListTag mj3 = md.getList("Items", 10);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            final CompoundTag md2 = mj3.getCompound(integer4);
            final int integer5 = md2.getByte("Slot") & 0xFF;
            if (integer5 >= 0 && integer5 < gj.size()) {
                gj.set(integer5, ItemStack.of(md2));
            }
        }
    }
    
    public static int clearOrCountMatchingItems(final Container aok, final Predicate<ItemStack> predicate, final int integer, final boolean boolean4) {
        int integer2 = 0;
        for (int integer3 = 0; integer3 < aok.getContainerSize(); ++integer3) {
            final ItemStack bly7 = aok.getItem(integer3);
            final int integer4 = clearOrCountMatchingItems(bly7, predicate, integer - integer2, boolean4);
            if (integer4 > 0 && !boolean4 && bly7.isEmpty()) {
                aok.setItem(integer3, ItemStack.EMPTY);
            }
            integer2 += integer4;
        }
        return integer2;
    }
    
    public static int clearOrCountMatchingItems(final ItemStack bly, final Predicate<ItemStack> predicate, final int integer, final boolean boolean4) {
        if (bly.isEmpty() || !predicate.test(bly)) {
            return 0;
        }
        if (boolean4) {
            return bly.getCount();
        }
        final int integer2 = (integer < 0) ? bly.getCount() : Math.min(integer, bly.getCount());
        bly.shrink(integer2);
        return integer2;
    }
}
