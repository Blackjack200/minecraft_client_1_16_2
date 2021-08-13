package net.minecraft.world.level;

public enum TickPriority {
    EXTREMELY_HIGH(-3), 
    VERY_HIGH(-2), 
    HIGH(-1), 
    NORMAL(0), 
    LOW(1), 
    VERY_LOW(2), 
    EXTREMELY_LOW(3);
    
    private final int value;
    
    private TickPriority(final int integer3) {
        this.value = integer3;
    }
    
    public static TickPriority byValue(final int integer) {
        for (final TickPriority bsn5 : values()) {
            if (bsn5.value == integer) {
                return bsn5;
            }
        }
        if (integer < TickPriority.EXTREMELY_HIGH.value) {
            return TickPriority.EXTREMELY_HIGH;
        }
        return TickPriority.EXTREMELY_LOW;
    }
    
    public int getValue() {
        return this.value;
    }
}
