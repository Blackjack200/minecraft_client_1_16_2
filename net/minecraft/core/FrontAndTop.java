package net.minecraft.core;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.StringRepresentable;

public enum FrontAndTop implements StringRepresentable {
    DOWN_EAST("down_east", Direction.DOWN, Direction.EAST), 
    DOWN_NORTH("down_north", Direction.DOWN, Direction.NORTH), 
    DOWN_SOUTH("down_south", Direction.DOWN, Direction.SOUTH), 
    DOWN_WEST("down_west", Direction.DOWN, Direction.WEST), 
    UP_EAST("up_east", Direction.UP, Direction.EAST), 
    UP_NORTH("up_north", Direction.UP, Direction.NORTH), 
    UP_SOUTH("up_south", Direction.UP, Direction.SOUTH), 
    UP_WEST("up_west", Direction.UP, Direction.WEST), 
    WEST_UP("west_up", Direction.WEST, Direction.UP), 
    EAST_UP("east_up", Direction.EAST, Direction.UP), 
    NORTH_UP("north_up", Direction.NORTH, Direction.UP), 
    SOUTH_UP("south_up", Direction.SOUTH, Direction.UP);
    
    private static final Int2ObjectMap<FrontAndTop> LOOKUP_TOP_FRONT;
    private final String name;
    private final Direction top;
    private final Direction front;
    
    private static int lookupKey(final Direction gc1, final Direction gc2) {
        return gc1.ordinal() << 3 | gc2.ordinal();
    }
    
    private FrontAndTop(final String string3, final Direction gc4, final Direction gc5) {
        this.name = string3;
        this.front = gc4;
        this.top = gc5;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    public static FrontAndTop fromFrontAndTop(final Direction gc1, final Direction gc2) {
        final int integer3 = lookupKey(gc2, gc1);
        return (FrontAndTop)FrontAndTop.LOOKUP_TOP_FRONT.get(integer3);
    }
    
    public Direction front() {
        return this.front;
    }
    
    public Direction top() {
        return this.top;
    }
    
    static {
        LOOKUP_TOP_FRONT = (Int2ObjectMap)new Int2ObjectOpenHashMap(values().length);
        for (final FrontAndTop ge4 : values()) {
            FrontAndTop.LOOKUP_TOP_FRONT.put(lookupKey(ge4.top, ge4.front), ge4);
        }
    }
}
