package net.minecraft.core;

import java.util.stream.Stream;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.Util;
import java.util.Random;
import java.util.Locale;
import javax.annotation.Nullable;
import com.mojang.math.Vector3f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector4f;
import com.mojang.math.Matrix4f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.Map;
import net.minecraft.util.StringRepresentable;

public enum Direction implements StringRepresentable {
    DOWN(0, 1, -1, "down", AxisDirection.NEGATIVE, Axis.Y, new Vec3i(0, -1, 0)), 
    UP(1, 0, -1, "up", AxisDirection.POSITIVE, Axis.Y, new Vec3i(0, 1, 0)), 
    NORTH(2, 3, 2, "north", AxisDirection.NEGATIVE, Axis.Z, new Vec3i(0, 0, -1)), 
    SOUTH(3, 2, 0, "south", AxisDirection.POSITIVE, Axis.Z, new Vec3i(0, 0, 1)), 
    WEST(4, 5, 1, "west", AxisDirection.NEGATIVE, Axis.X, new Vec3i(-1, 0, 0)), 
    EAST(5, 4, 3, "east", AxisDirection.POSITIVE, Axis.X, new Vec3i(1, 0, 0));
    
    private final int data3d;
    private final int oppositeIndex;
    private final int data2d;
    private final String name;
    private final Axis axis;
    private final AxisDirection axisDirection;
    private final Vec3i normal;
    private static final Direction[] VALUES;
    private static final Map<String, Direction> BY_NAME;
    private static final Direction[] BY_3D_DATA;
    private static final Direction[] BY_2D_DATA;
    private static final Long2ObjectMap<Direction> BY_NORMAL;
    
    private Direction(final int integer3, final int integer4, final int integer5, final String string6, final AxisDirection b, final Axis a, final Vec3i gr) {
        this.data3d = integer3;
        this.data2d = integer5;
        this.oppositeIndex = integer4;
        this.name = string6;
        this.axis = a;
        this.axisDirection = b;
        this.normal = gr;
    }
    
    public static Direction[] orderedByNearest(final Entity apx) {
        final float float2 = apx.getViewXRot(1.0f) * 0.017453292f;
        final float float3 = -apx.getViewYRot(1.0f) * 0.017453292f;
        final float float4 = Mth.sin(float2);
        final float float5 = Mth.cos(float2);
        final float float6 = Mth.sin(float3);
        final float float7 = Mth.cos(float3);
        final boolean boolean8 = float6 > 0.0f;
        final boolean boolean9 = float4 < 0.0f;
        final boolean boolean10 = float7 > 0.0f;
        final float float8 = boolean8 ? float6 : (-float6);
        final float float9 = boolean9 ? (-float4) : float4;
        final float float10 = boolean10 ? float7 : (-float7);
        final float float11 = float8 * float5;
        final float float12 = float10 * float5;
        final Direction gc16 = boolean8 ? Direction.EAST : Direction.WEST;
        final Direction gc17 = boolean9 ? Direction.UP : Direction.DOWN;
        final Direction gc18 = boolean10 ? Direction.SOUTH : Direction.NORTH;
        if (float8 > float10) {
            if (float9 > float11) {
                return makeDirectionArray(gc17, gc16, gc18);
            }
            if (float12 > float9) {
                return makeDirectionArray(gc16, gc18, gc17);
            }
            return makeDirectionArray(gc16, gc17, gc18);
        }
        else {
            if (float9 > float12) {
                return makeDirectionArray(gc17, gc18, gc16);
            }
            if (float11 > float9) {
                return makeDirectionArray(gc18, gc16, gc17);
            }
            return makeDirectionArray(gc18, gc17, gc16);
        }
    }
    
    private static Direction[] makeDirectionArray(final Direction gc1, final Direction gc2, final Direction gc3) {
        return new Direction[] { gc1, gc2, gc3, gc3.getOpposite(), gc2.getOpposite(), gc1.getOpposite() };
    }
    
    public static Direction rotate(final Matrix4f b, final Direction gc) {
        final Vec3i gr3 = gc.getNormal();
        final Vector4f h4 = new Vector4f((float)gr3.getX(), (float)gr3.getY(), (float)gr3.getZ(), 0.0f);
        h4.transform(b);
        return getNearest(h4.x(), h4.y(), h4.z());
    }
    
    public Quaternion getRotation() {
        final Quaternion d2 = Vector3f.XP.rotationDegrees(90.0f);
        switch (this) {
            case DOWN: {
                return Vector3f.XP.rotationDegrees(180.0f);
            }
            case UP: {
                return Quaternion.ONE.copy();
            }
            case NORTH: {
                d2.mul(Vector3f.ZP.rotationDegrees(180.0f));
                return d2;
            }
            case SOUTH: {
                return d2;
            }
            case WEST: {
                d2.mul(Vector3f.ZP.rotationDegrees(90.0f));
                return d2;
            }
            default: {
                d2.mul(Vector3f.ZP.rotationDegrees(-90.0f));
                return d2;
            }
        }
    }
    
    public int get3DDataValue() {
        return this.data3d;
    }
    
    public int get2DDataValue() {
        return this.data2d;
    }
    
    public AxisDirection getAxisDirection() {
        return this.axisDirection;
    }
    
    public Direction getOpposite() {
        return from3DDataValue(this.oppositeIndex);
    }
    
    public Direction getClockWise() {
        switch (this) {
            case NORTH: {
                return Direction.EAST;
            }
            case EAST: {
                return Direction.SOUTH;
            }
            case SOUTH: {
                return Direction.WEST;
            }
            case WEST: {
                return Direction.NORTH;
            }
            default: {
                throw new IllegalStateException(new StringBuilder().append("Unable to get Y-rotated facing of ").append(this).toString());
            }
        }
    }
    
    public Direction getCounterClockWise() {
        switch (this) {
            case NORTH: {
                return Direction.WEST;
            }
            case EAST: {
                return Direction.NORTH;
            }
            case SOUTH: {
                return Direction.EAST;
            }
            case WEST: {
                return Direction.SOUTH;
            }
            default: {
                throw new IllegalStateException(new StringBuilder().append("Unable to get CCW facing of ").append(this).toString());
            }
        }
    }
    
    public int getStepX() {
        return this.normal.getX();
    }
    
    public int getStepY() {
        return this.normal.getY();
    }
    
    public int getStepZ() {
        return this.normal.getZ();
    }
    
    public Vector3f step() {
        return new Vector3f((float)this.getStepX(), (float)this.getStepY(), (float)this.getStepZ());
    }
    
    public String getName() {
        return this.name;
    }
    
    public Axis getAxis() {
        return this.axis;
    }
    
    @Nullable
    public static Direction byName(@Nullable final String string) {
        if (string == null) {
            return null;
        }
        return (Direction)Direction.BY_NAME.get(string.toLowerCase(Locale.ROOT));
    }
    
    public static Direction from3DDataValue(final int integer) {
        return Direction.BY_3D_DATA[Mth.abs(integer % Direction.BY_3D_DATA.length)];
    }
    
    public static Direction from2DDataValue(final int integer) {
        return Direction.BY_2D_DATA[Mth.abs(integer % Direction.BY_2D_DATA.length)];
    }
    
    @Nullable
    public static Direction fromNormal(final int integer1, final int integer2, final int integer3) {
        return (Direction)Direction.BY_NORMAL.get(BlockPos.asLong(integer1, integer2, integer3));
    }
    
    public static Direction fromYRot(final double double1) {
        return from2DDataValue(Mth.floor(double1 / 90.0 + 0.5) & 0x3);
    }
    
    public static Direction fromAxisAndDirection(final Axis a, final AxisDirection b) {
        switch (a) {
            case X: {
                return (b == AxisDirection.POSITIVE) ? Direction.EAST : Direction.WEST;
            }
            case Y: {
                return (b == AxisDirection.POSITIVE) ? Direction.UP : Direction.DOWN;
            }
            default: {
                return (b == AxisDirection.POSITIVE) ? Direction.SOUTH : Direction.NORTH;
            }
        }
    }
    
    public float toYRot() {
        return (float)((this.data2d & 0x3) * 90);
    }
    
    public static Direction getRandom(final Random random) {
        return Util.<Direction>getRandom(Direction.VALUES, random);
    }
    
    public static Direction getNearest(final double double1, final double double2, final double double3) {
        return getNearest((float)double1, (float)double2, (float)double3);
    }
    
    public static Direction getNearest(final float float1, final float float2, final float float3) {
        Direction gc4 = Direction.NORTH;
        float float4 = Float.MIN_VALUE;
        for (final Direction gc5 : Direction.VALUES) {
            final float float5 = float1 * gc5.normal.getX() + float2 * gc5.normal.getY() + float3 * gc5.normal.getZ();
            if (float5 > float4) {
                float4 = float5;
                gc4 = gc5;
            }
        }
        return gc4;
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    public static Direction get(final AxisDirection b, final Axis a) {
        for (final Direction gc6 : Direction.VALUES) {
            if (gc6.getAxisDirection() == b && gc6.getAxis() == a) {
                return gc6;
            }
        }
        throw new IllegalArgumentException(new StringBuilder().append("No such direction: ").append(b).append(" ").append(a).toString());
    }
    
    public Vec3i getNormal() {
        return this.normal;
    }
    
    public boolean isFacingAngle(final float float1) {
        final float float2 = float1 * 0.017453292f;
        final float float3 = -Mth.sin(float2);
        final float float4 = Mth.cos(float2);
        return this.normal.getX() * float3 + this.normal.getZ() * float4 > 0.0f;
    }
    
    static {
        VALUES = values();
        BY_NAME = (Map)Arrays.stream((Object[])Direction.VALUES).collect(Collectors.toMap(Direction::getName, gc -> gc));
        BY_3D_DATA = (Direction[])Arrays.stream((Object[])Direction.VALUES).sorted(Comparator.comparingInt(gc -> gc.data3d)).toArray(Direction[]::new);
        BY_2D_DATA = (Direction[])Arrays.stream((Object[])Direction.VALUES).filter(gc -> gc.getAxis().isHorizontal()).sorted(Comparator.comparingInt(gc -> gc.data2d)).toArray(Direction[]::new);
        BY_NORMAL = (Long2ObjectMap)Arrays.stream((Object[])Direction.VALUES).collect(Collectors.toMap(gc -> new BlockPos(gc.getNormal()).asLong(), gc -> gc, (gc1, gc2) -> {
            throw new IllegalArgumentException("Duplicate keys");
        }, Long2ObjectOpenHashMap::new));
    }
    
    public enum Axis implements StringRepresentable, Predicate<Direction> {
        X("x") {
            @Override
            public int choose(final int integer1, final int integer2, final int integer3) {
                return integer1;
            }
            
            @Override
            public double choose(final double double1, final double double2, final double double3) {
                return double1;
            }
        }, 
        Y("y") {
            @Override
            public int choose(final int integer1, final int integer2, final int integer3) {
                return integer2;
            }
            
            @Override
            public double choose(final double double1, final double double2, final double double3) {
                return double2;
            }
        }, 
        Z("z") {
            @Override
            public int choose(final int integer1, final int integer2, final int integer3) {
                return integer3;
            }
            
            @Override
            public double choose(final double double1, final double double2, final double double3) {
                return double3;
            }
        };
        
        private static final Axis[] VALUES;
        public static final Codec<Axis> CODEC;
        private static final Map<String, Axis> BY_NAME;
        private final String name;
        
        private Axis(final String string3) {
            this.name = string3;
        }
        
        @Nullable
        public static Axis byName(final String string) {
            return (Axis)Axis.BY_NAME.get(string.toLowerCase(Locale.ROOT));
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean isVertical() {
            return this == Axis.Y;
        }
        
        public boolean isHorizontal() {
            return this == Axis.X || this == Axis.Z;
        }
        
        public String toString() {
            return this.name;
        }
        
        public static Axis getRandom(final Random random) {
            return Util.<Axis>getRandom(Axis.VALUES, random);
        }
        
        public boolean test(@Nullable final Direction gc) {
            return gc != null && gc.getAxis() == this;
        }
        
        public Plane getPlane() {
            switch (this) {
                case X:
                case Z: {
                    return Plane.HORIZONTAL;
                }
                case Y: {
                    return Plane.VERTICAL;
                }
                default: {
                    throw new Error("Someone's been tampering with the universe!");
                }
            }
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        public abstract int choose(final int integer1, final int integer2, final int integer3);
        
        public abstract double choose(final double double1, final double double2, final double double3);
        
        static {
            VALUES = values();
            CODEC = StringRepresentable.<Axis>fromEnum((java.util.function.Supplier<Axis[]>)Axis::values, (java.util.function.Function<? super String, ? extends Axis>)Axis::byName);
            BY_NAME = (Map)Arrays.stream((Object[])Axis.VALUES).collect(Collectors.toMap(Axis::getName, a -> a));
        }
    }
    
    public enum AxisDirection {
        POSITIVE(1, "Towards positive"), 
        NEGATIVE(-1, "Towards negative");
        
        private final int step;
        private final String name;
        
        private AxisDirection(final int integer3, final String string4) {
            this.step = integer3;
            this.name = string4;
        }
        
        public int getStep() {
            return this.step;
        }
        
        public String toString() {
            return this.name;
        }
        
        public AxisDirection opposite() {
            return (this == AxisDirection.POSITIVE) ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE;
        }
    }
    
    public enum Plane implements Iterable<Direction>, Predicate<Direction> {
        HORIZONTAL(new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST }, new Axis[] { Axis.X, Axis.Z }), 
        VERTICAL(new Direction[] { Direction.UP, Direction.DOWN }, new Axis[] { Axis.Y });
        
        private final Direction[] faces;
        private final Axis[] axis;
        
        private Plane(final Direction[] arr, final Axis[] arr) {
            this.faces = arr;
            this.axis = arr;
        }
        
        public Direction getRandomDirection(final Random random) {
            return Util.<Direction>getRandom(this.faces, random);
        }
        
        public boolean test(@Nullable final Direction gc) {
            return gc != null && gc.getAxis().getPlane() == this;
        }
        
        public Iterator<Direction> iterator() {
            return (Iterator<Direction>)Iterators.forArray((Object[])this.faces);
        }
        
        public Stream<Direction> stream() {
            return (Stream<Direction>)Arrays.stream((Object[])this.faces);
        }
    }
}
