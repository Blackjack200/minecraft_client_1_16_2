package net.minecraft.core.dispenser;

import net.minecraft.core.BlockSource;

public abstract class OptionalDispenseItemBehavior extends DefaultDispenseItemBehavior {
    private boolean success;
    
    public OptionalDispenseItemBehavior() {
        this.success = true;
    }
    
    public boolean isSuccess() {
        return this.success;
    }
    
    public void setSuccess(final boolean boolean1) {
        this.success = boolean1;
    }
    
    @Override
    protected void playSound(final BlockSource fy) {
        fy.getLevel().levelEvent(this.isSuccess() ? 1000 : 1001, fy.getPos(), 0);
    }
}
