package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;

public class ComparatorBlockEntity extends BlockEntity {
    private int output;
    
    public ComparatorBlockEntity() {
        super(BlockEntityType.COMPARATOR);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putInt("OutputSignal", this.output);
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.output = md.getInt("OutputSignal");
    }
    
    public int getOutputSignal() {
        return this.output;
    }
    
    public void setOutputSignal(final int integer) {
        this.output = integer;
    }
}
