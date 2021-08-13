package net.minecraft.core;

public enum AxisCycle {
    NONE {
        @Override
        public int cycle(final int integer1, final int integer2, final int integer3, final Direction.Axis a) {
            return a.choose(integer1, integer2, integer3);
        }
        
        @Override
        public Direction.Axis cycle(final Direction.Axis a) {
            return a;
        }
        
        @Override
        public AxisCycle inverse() {
            return this;
        }
    }, 
    FORWARD {
        @Override
        public int cycle(final int integer1, final int integer2, final int integer3, final Direction.Axis a) {
            return a.choose(integer3, integer1, integer2);
        }
        
        @Override
        public Direction.Axis cycle(final Direction.Axis a) {
            return AxisCycle$2.AXIS_VALUES[Math.floorMod(a.ordinal() + 1, 3)];
        }
        
        @Override
        public AxisCycle inverse() {
            return AxisCycle$2.BACKWARD;
        }
    }, 
    BACKWARD {
        @Override
        public int cycle(final int integer1, final int integer2, final int integer3, final Direction.Axis a) {
            return a.choose(integer2, integer3, integer1);
        }
        
        @Override
        public Direction.Axis cycle(final Direction.Axis a) {
            return AxisCycle$3.AXIS_VALUES[Math.floorMod(a.ordinal() - 1, 3)];
        }
        
        @Override
        public AxisCycle inverse() {
            return AxisCycle$3.FORWARD;
        }
    };
    
    public static final Direction.Axis[] AXIS_VALUES;
    public static final AxisCycle[] VALUES;
    
    public abstract int cycle(final int integer1, final int integer2, final int integer3, final Direction.Axis a);
    
    public abstract Direction.Axis cycle(final Direction.Axis a);
    
    public abstract AxisCycle inverse();
    
    public static AxisCycle between(final Direction.Axis a1, final Direction.Axis a2) {
        return AxisCycle.VALUES[Math.floorMod(a2.ordinal() - a1.ordinal(), 3)];
    }
    
    static {
        AXIS_VALUES = Direction.Axis.values();
        VALUES = values();
    }
}
