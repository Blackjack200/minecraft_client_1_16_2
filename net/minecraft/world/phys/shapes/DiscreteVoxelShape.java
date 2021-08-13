package net.minecraft.world.phys.shapes;

import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;

public abstract class DiscreteVoxelShape {
    private static final Direction.Axis[] AXIS_VALUES;
    protected final int xSize;
    protected final int ySize;
    protected final int zSize;
    
    protected DiscreteVoxelShape(final int integer1, final int integer2, final int integer3) {
        this.xSize = integer1;
        this.ySize = integer2;
        this.zSize = integer3;
    }
    
    public boolean isFullWide(final AxisCycle fv, final int integer2, final int integer3, final int integer4) {
        return this.isFullWide(fv.cycle(integer2, integer3, integer4, Direction.Axis.X), fv.cycle(integer2, integer3, integer4, Direction.Axis.Y), fv.cycle(integer2, integer3, integer4, Direction.Axis.Z));
    }
    
    public boolean isFullWide(final int integer1, final int integer2, final int integer3) {
        return integer1 >= 0 && integer2 >= 0 && integer3 >= 0 && integer1 < this.xSize && integer2 < this.ySize && integer3 < this.zSize && this.isFull(integer1, integer2, integer3);
    }
    
    public boolean isFull(final AxisCycle fv, final int integer2, final int integer3, final int integer4) {
        return this.isFull(fv.cycle(integer2, integer3, integer4, Direction.Axis.X), fv.cycle(integer2, integer3, integer4, Direction.Axis.Y), fv.cycle(integer2, integer3, integer4, Direction.Axis.Z));
    }
    
    public abstract boolean isFull(final int integer1, final int integer2, final int integer3);
    
    public abstract void setFull(final int integer1, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5);
    
    public boolean isEmpty() {
        for (final Direction.Axis a5 : DiscreteVoxelShape.AXIS_VALUES) {
            if (this.firstFull(a5) >= this.lastFull(a5)) {
                return true;
            }
        }
        return false;
    }
    
    public abstract int firstFull(final Direction.Axis a);
    
    public abstract int lastFull(final Direction.Axis a);
    
    public int lastFull(final Direction.Axis a, final int integer2, final int integer3) {
        if (integer2 < 0 || integer3 < 0) {
            return 0;
        }
        final Direction.Axis a2 = AxisCycle.FORWARD.cycle(a);
        final Direction.Axis a3 = AxisCycle.BACKWARD.cycle(a);
        if (integer2 >= this.getSize(a2) || integer3 >= this.getSize(a3)) {
            return 0;
        }
        final int integer4 = this.getSize(a);
        final AxisCycle fv8 = AxisCycle.between(Direction.Axis.X, a);
        for (int integer5 = integer4 - 1; integer5 >= 0; --integer5) {
            if (this.isFull(fv8, integer5, integer2, integer3)) {
                return integer5 + 1;
            }
        }
        return 0;
    }
    
    public int getSize(final Direction.Axis a) {
        return a.choose(this.xSize, this.ySize, this.zSize);
    }
    
    public int getXSize() {
        return this.getSize(Direction.Axis.X);
    }
    
    public int getYSize() {
        return this.getSize(Direction.Axis.Y);
    }
    
    public int getZSize() {
        return this.getSize(Direction.Axis.Z);
    }
    
    public void forAllEdges(final IntLineConsumer b, final boolean boolean2) {
        this.forAllAxisEdges(b, AxisCycle.NONE, boolean2);
        this.forAllAxisEdges(b, AxisCycle.FORWARD, boolean2);
        this.forAllAxisEdges(b, AxisCycle.BACKWARD, boolean2);
    }
    
    private void forAllAxisEdges(final IntLineConsumer b, final AxisCycle fv, final boolean boolean3) {
        final AxisCycle fv2 = fv.inverse();
        final int integer7 = this.getSize(fv2.cycle(Direction.Axis.X));
        final int integer8 = this.getSize(fv2.cycle(Direction.Axis.Y));
        final int integer9 = this.getSize(fv2.cycle(Direction.Axis.Z));
        for (int integer10 = 0; integer10 <= integer7; ++integer10) {
            for (int integer11 = 0; integer11 <= integer8; ++integer11) {
                int integer12 = -1;
                for (int integer13 = 0; integer13 <= integer9; ++integer13) {
                    int integer14 = 0;
                    int integer15 = 0;
                    for (int integer16 = 0; integer16 <= 1; ++integer16) {
                        for (int integer17 = 0; integer17 <= 1; ++integer17) {
                            if (this.isFullWide(fv2, integer10 + integer16 - 1, integer11 + integer17 - 1, integer13)) {
                                ++integer14;
                                integer15 ^= (integer16 ^ integer17);
                            }
                        }
                    }
                    if (integer14 == 1 || integer14 == 3 || (integer14 == 2 && (integer15 & 0x1) == 0x0)) {
                        if (boolean3) {
                            if (integer12 == -1) {
                                integer12 = integer13;
                            }
                        }
                        else {
                            b.consume(fv2.cycle(integer10, integer11, integer13, Direction.Axis.X), fv2.cycle(integer10, integer11, integer13, Direction.Axis.Y), fv2.cycle(integer10, integer11, integer13, Direction.Axis.Z), fv2.cycle(integer10, integer11, integer13 + 1, Direction.Axis.X), fv2.cycle(integer10, integer11, integer13 + 1, Direction.Axis.Y), fv2.cycle(integer10, integer11, integer13 + 1, Direction.Axis.Z));
                        }
                    }
                    else if (integer12 != -1) {
                        b.consume(fv2.cycle(integer10, integer11, integer12, Direction.Axis.X), fv2.cycle(integer10, integer11, integer12, Direction.Axis.Y), fv2.cycle(integer10, integer11, integer12, Direction.Axis.Z), fv2.cycle(integer10, integer11, integer13, Direction.Axis.X), fv2.cycle(integer10, integer11, integer13, Direction.Axis.Y), fv2.cycle(integer10, integer11, integer13, Direction.Axis.Z));
                        integer12 = -1;
                    }
                }
            }
        }
    }
    
    protected boolean isZStripFull(final int integer1, final int integer2, final int integer3, final int integer4) {
        for (int integer5 = integer1; integer5 < integer2; ++integer5) {
            if (!this.isFullWide(integer3, integer4, integer5)) {
                return false;
            }
        }
        return true;
    }
    
    protected void setZStrip(final int integer1, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
        for (int integer5 = integer1; integer5 < integer2; ++integer5) {
            this.setFull(integer3, integer4, integer5, false, boolean5);
        }
    }
    
    protected boolean isXZRectangleFull(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        for (int integer6 = integer1; integer6 < integer2; ++integer6) {
            if (!this.isZStripFull(integer3, integer4, integer6, integer5)) {
                return false;
            }
        }
        return true;
    }
    
    public void forAllBoxes(final IntLineConsumer b, final boolean boolean2) {
        final DiscreteVoxelShape dct4 = new BitSetDiscreteVoxelShape(this);
        for (int integer5 = 0; integer5 <= this.xSize; ++integer5) {
            for (int integer6 = 0; integer6 <= this.ySize; ++integer6) {
                int integer7 = -1;
                for (int integer8 = 0; integer8 <= this.zSize; ++integer8) {
                    if (dct4.isFullWide(integer5, integer6, integer8)) {
                        if (boolean2) {
                            if (integer7 == -1) {
                                integer7 = integer8;
                            }
                        }
                        else {
                            b.consume(integer5, integer6, integer8, integer5 + 1, integer6 + 1, integer8 + 1);
                        }
                    }
                    else if (integer7 != -1) {
                        int integer9 = integer5;
                        int integer10 = integer5;
                        int integer11 = integer6;
                        int integer12 = integer6;
                        dct4.setZStrip(integer7, integer8, integer5, integer6, false);
                        while (dct4.isZStripFull(integer7, integer8, integer9 - 1, integer11)) {
                            dct4.setZStrip(integer7, integer8, integer9 - 1, integer11, false);
                            --integer9;
                        }
                        while (dct4.isZStripFull(integer7, integer8, integer10 + 1, integer11)) {
                            dct4.setZStrip(integer7, integer8, integer10 + 1, integer11, false);
                            ++integer10;
                        }
                        while (dct4.isXZRectangleFull(integer9, integer10 + 1, integer7, integer8, integer11 - 1)) {
                            for (int integer13 = integer9; integer13 <= integer10; ++integer13) {
                                dct4.setZStrip(integer7, integer8, integer13, integer11 - 1, false);
                            }
                            --integer11;
                        }
                        while (dct4.isXZRectangleFull(integer9, integer10 + 1, integer7, integer8, integer12 + 1)) {
                            for (int integer13 = integer9; integer13 <= integer10; ++integer13) {
                                dct4.setZStrip(integer7, integer8, integer13, integer12 + 1, false);
                            }
                            ++integer12;
                        }
                        b.consume(integer9, integer11, integer7, integer10 + 1, integer12 + 1, integer8);
                        integer7 = -1;
                    }
                }
            }
        }
    }
    
    public void forAllFaces(final IntFaceConsumer a) {
        this.forAllAxisFaces(a, AxisCycle.NONE);
        this.forAllAxisFaces(a, AxisCycle.FORWARD);
        this.forAllAxisFaces(a, AxisCycle.BACKWARD);
    }
    
    private void forAllAxisFaces(final IntFaceConsumer a, final AxisCycle fv) {
        final AxisCycle fv2 = fv.inverse();
        final Direction.Axis a2 = fv2.cycle(Direction.Axis.Z);
        final int integer6 = this.getSize(fv2.cycle(Direction.Axis.X));
        final int integer7 = this.getSize(fv2.cycle(Direction.Axis.Y));
        final int integer8 = this.getSize(a2);
        final Direction gc9 = Direction.fromAxisAndDirection(a2, Direction.AxisDirection.NEGATIVE);
        final Direction gc10 = Direction.fromAxisAndDirection(a2, Direction.AxisDirection.POSITIVE);
        for (int integer9 = 0; integer9 < integer6; ++integer9) {
            for (int integer10 = 0; integer10 < integer7; ++integer10) {
                boolean boolean13 = false;
                for (int integer11 = 0; integer11 <= integer8; ++integer11) {
                    final boolean boolean14 = integer11 != integer8 && this.isFull(fv2, integer9, integer10, integer11);
                    if (!boolean13 && boolean14) {
                        a.consume(gc9, fv2.cycle(integer9, integer10, integer11, Direction.Axis.X), fv2.cycle(integer9, integer10, integer11, Direction.Axis.Y), fv2.cycle(integer9, integer10, integer11, Direction.Axis.Z));
                    }
                    if (boolean13 && !boolean14) {
                        a.consume(gc10, fv2.cycle(integer9, integer10, integer11 - 1, Direction.Axis.X), fv2.cycle(integer9, integer10, integer11 - 1, Direction.Axis.Y), fv2.cycle(integer9, integer10, integer11 - 1, Direction.Axis.Z));
                    }
                    boolean13 = boolean14;
                }
            }
        }
    }
    
    static {
        AXIS_VALUES = Direction.Axis.values();
    }
    
    public interface IntFaceConsumer {
        void consume(final Direction gc, final int integer2, final int integer3, final int integer4);
    }
    
    public interface IntLineConsumer {
        void consume(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6);
    }
}
