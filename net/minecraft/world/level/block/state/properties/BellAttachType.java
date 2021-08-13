package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum BellAttachType implements StringRepresentable {
    FLOOR("floor"), 
    CEILING("ceiling"), 
    SINGLE_WALL("single_wall"), 
    DOUBLE_WALL("double_wall");
    
    private final String name;
    
    private BellAttachType(final String string3) {
        this.name = string3;
    }
    
    public String getSerializedName() {
        return this.name;
    }
}
