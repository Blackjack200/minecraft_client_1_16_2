package net.minecraft.core;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;

public enum Direction8 {
    NORTH(new Direction[] { Direction.NORTH }), 
    NORTH_EAST(new Direction[] { Direction.NORTH, Direction.EAST }), 
    EAST(new Direction[] { Direction.EAST }), 
    SOUTH_EAST(new Direction[] { Direction.SOUTH, Direction.EAST }), 
    SOUTH(new Direction[] { Direction.SOUTH }), 
    SOUTH_WEST(new Direction[] { Direction.SOUTH, Direction.WEST }), 
    WEST(new Direction[] { Direction.WEST }), 
    NORTH_WEST(new Direction[] { Direction.NORTH, Direction.WEST });
    
    private static final int NORTH_WEST_MASK;
    private static final int WEST_MASK;
    private static final int SOUTH_WEST_MASK;
    private static final int SOUTH_MASK;
    private static final int SOUTH_EAST_MASK;
    private static final int EAST_MASK;
    private static final int NORTH_EAST_MASK;
    private static final int NORTH_MASK;
    private final Set<Direction> directions;
    
    private Direction8(final Direction[] arr) {
        this.directions = (Set<Direction>)Sets.immutableEnumSet((Iterable)Arrays.asList((Object[])arr));
    }
    
    public Set<Direction> getDirections() {
        return this.directions;
    }
    
    static {
        NORTH_WEST_MASK = 1 << Direction8.NORTH_WEST.ordinal();
        WEST_MASK = 1 << Direction8.WEST.ordinal();
        SOUTH_WEST_MASK = 1 << Direction8.SOUTH_WEST.ordinal();
        SOUTH_MASK = 1 << Direction8.SOUTH.ordinal();
        SOUTH_EAST_MASK = 1 << Direction8.SOUTH_EAST.ordinal();
        EAST_MASK = 1 << Direction8.EAST.ordinal();
        NORTH_EAST_MASK = 1 << Direction8.NORTH_EAST.ordinal();
        NORTH_MASK = 1 << Direction8.NORTH.ordinal();
    }
}
