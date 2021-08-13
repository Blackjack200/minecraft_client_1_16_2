package net.minecraft.world.level.border;

public enum BorderStatus {
    GROWING(4259712), 
    SHRINKING(16724016), 
    STATIONARY(2138367);
    
    private final int color;
    
    private BorderStatus(final int integer3) {
        this.color = integer3;
    }
    
    public int getColor() {
        return this.color;
    }
}
