package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum ChestType implements StringRepresentable {
    SINGLE("single", 0), 
    LEFT("left", 2), 
    RIGHT("right", 1);
    
    public static final ChestType[] BY_ID;
    private final String name;
    private final int opposite;
    
    private ChestType(final String string3, final int integer4) {
        this.name = string3;
        this.opposite = integer4;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    public ChestType getOpposite() {
        return ChestType.BY_ID[this.opposite];
    }
    
    static {
        BY_ID = values();
    }
}
