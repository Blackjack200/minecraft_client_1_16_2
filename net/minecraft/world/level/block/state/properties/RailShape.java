package net.minecraft.world.level.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum RailShape implements StringRepresentable {
    NORTH_SOUTH("north_south"), 
    EAST_WEST("east_west"), 
    ASCENDING_EAST("ascending_east"), 
    ASCENDING_WEST("ascending_west"), 
    ASCENDING_NORTH("ascending_north"), 
    ASCENDING_SOUTH("ascending_south"), 
    SOUTH_EAST("south_east"), 
    SOUTH_WEST("south_west"), 
    NORTH_WEST("north_west"), 
    NORTH_EAST("north_east");
    
    private final String name;
    
    private RailShape(final String string3) {
        this.name = string3;
    }
    
    public String toString() {
        return this.name;
    }
    
    public boolean isAscending() {
        return this == RailShape.ASCENDING_NORTH || this == RailShape.ASCENDING_EAST || this == RailShape.ASCENDING_SOUTH || this == RailShape.ASCENDING_WEST;
    }
    
    public String getSerializedName() {
        return this.name;
    }
}
