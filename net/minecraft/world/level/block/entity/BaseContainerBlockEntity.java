package net.minecraft.world.level.block.entity;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.Nameable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Container;

public abstract class BaseContainerBlockEntity extends BlockEntity implements Container, MenuProvider, Nameable {
    private LockCode lockKey;
    private Component name;
    
    protected BaseContainerBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.lockKey = LockCode.NO_LOCK;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.lockKey = LockCode.fromTag(md);
        if (md.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(md.getString("CustomName"));
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        this.lockKey.addToTag(md);
        if (this.name != null) {
            md.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return md;
    }
    
    public void setCustomName(final Component nr) {
        this.name = nr;
    }
    
    @Override
    public Component getName() {
        if (this.name != null) {
            return this.name;
        }
        return this.getDefaultName();
    }
    
    @Override
    public Component getDisplayName() {
        return this.getName();
    }
    
    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }
    
    protected abstract Component getDefaultName();
    
    public boolean canOpen(final Player bft) {
        return canUnlock(bft, this.lockKey, this.getDisplayName());
    }
    
    public static boolean canUnlock(final Player bft, final LockCode aot, final Component nr) {
        if (bft.isSpectator() || aot.unlocksWith(bft.getMainHandItem())) {
            return true;
        }
        bft.displayClientMessage(new TranslatableComponent("container.isLocked", new Object[] { nr }), true);
        bft.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0f, 1.0f);
        return false;
    }
    
    @Nullable
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
        if (this.canOpen(bft)) {
            return this.createMenu(integer, bfs);
        }
        return null;
    }
    
    protected abstract AbstractContainerMenu createMenu(final int integer, final Inventory bfs);
}
