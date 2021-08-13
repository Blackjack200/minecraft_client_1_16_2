package net.minecraft.world.level.block.entity;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Clearable;

public class JukeboxBlockEntity extends BlockEntity implements Clearable {
    private ItemStack record;
    
    public JukeboxBlockEntity() {
        super(BlockEntityType.JUKEBOX);
        this.record = ItemStack.EMPTY;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.contains("RecordItem", 10)) {
            this.setRecord(ItemStack.of(md.getCompound("RecordItem")));
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (!this.getRecord().isEmpty()) {
            md.put("RecordItem", (Tag)this.getRecord().save(new CompoundTag()));
        }
        return md;
    }
    
    public ItemStack getRecord() {
        return this.record;
    }
    
    public void setRecord(final ItemStack bly) {
        this.record = bly;
        this.setChanged();
    }
    
    @Override
    public void clearContent() {
        this.setRecord(ItemStack.EMPTY);
    }
}
