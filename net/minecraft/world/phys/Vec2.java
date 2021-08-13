package net.minecraft.world.phys;

public class Vec2 {
    public static final Vec2 ZERO;
    public static final Vec2 ONE;
    public static final Vec2 UNIT_X;
    public static final Vec2 NEG_UNIT_X;
    public static final Vec2 UNIT_Y;
    public static final Vec2 NEG_UNIT_Y;
    public static final Vec2 MAX;
    public static final Vec2 MIN;
    public final float x;
    public final float y;
    
    public Vec2(final float float1, final float float2) {
        this.x = float1;
        this.y = float2;
    }
    
    public boolean equals(final Vec2 dcj) {
        return this.x == dcj.x && this.y == dcj.y;
    }
    
    static {
        ZERO = new Vec2(0.0f, 0.0f);
        ONE = new Vec2(1.0f, 1.0f);
        UNIT_X = new Vec2(1.0f, 0.0f);
        NEG_UNIT_X = new Vec2(-1.0f, 0.0f);
        UNIT_Y = new Vec2(0.0f, 1.0f);
        NEG_UNIT_Y = new Vec2(0.0f, -1.0f);
        MAX = new Vec2(Float.MAX_VALUE, Float.MAX_VALUE);
        MIN = new Vec2(Float.MIN_VALUE, Float.MIN_VALUE);
    }
}
