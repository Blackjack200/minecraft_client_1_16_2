package net.minecraft.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import javax.annotation.concurrent.Immutable;

@Immutable
public class LockCode {
    public static final LockCode NO_LOCK;
    private final String key;
    
    public LockCode(final String string) {
        this.key = string;
    }
    
    public boolean unlocksWith(final ItemStack bly) {
        return this.key.isEmpty() || (!bly.isEmpty() && bly.hasCustomHoverName() && this.key.equals(bly.getHoverName().getString()));
    }
    
    public void addToTag(final CompoundTag md) {
        if (!this.key.isEmpty()) {
            md.putString("Lock", this.key);
        }
    }
    
    public static LockCode fromTag(final CompoundTag md) {
        if (md.contains("Lock", 8)) {
            return new LockCode(md.getString("Lock"));
        }
        return LockCode.NO_LOCK;
    }
    
    static {
        NO_LOCK = new LockCode("");
    }
}
