package net.minecraft.world.phys.shapes;

import net.minecraft.core.Direction;
import java.util.BitSet;

public final class BitSetDiscreteVoxelShape extends DiscreteVoxelShape {
    private final BitSet storage;
    private int xMin;
    private int yMin;
    private int zMin;
    private int xMax;
    private int yMax;
    private int zMax;
    
    public BitSetDiscreteVoxelShape(final int integer1, final int integer2, final int integer3) {
        this(integer1, integer2, integer3, integer1, integer2, integer3, 0, 0, 0);
    }
    
    public BitSetDiscreteVoxelShape(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        super(integer1, integer2, integer3);
        this.storage = new BitSet(integer1 * integer2 * integer3);
        this.xMin = integer4;
        this.yMin = integer5;
        this.zMin = integer6;
        this.xMax = integer7;
        this.yMax = integer8;
        this.zMax = integer9;
    }
    
    public BitSetDiscreteVoxelShape(final DiscreteVoxelShape dct) {
        super(dct.xSize, dct.ySize, dct.zSize);
        if (dct instanceof BitSetDiscreteVoxelShape) {
            this.storage = (BitSet)((BitSetDiscreteVoxelShape)dct).storage.clone();
        }
        else {
            this.storage = new BitSet(this.xSize * this.ySize * this.zSize);
            for (int integer3 = 0; integer3 < this.xSize; ++integer3) {
                for (int integer4 = 0; integer4 < this.ySize; ++integer4) {
                    for (int integer5 = 0; integer5 < this.zSize; ++integer5) {
                        if (dct.isFull(integer3, integer4, integer5)) {
                            this.storage.set(this.getIndex(integer3, integer4, integer5));
                        }
                    }
                }
            }
        }
        this.xMin = dct.firstFull(Direction.Axis.X);
        this.yMin = dct.firstFull(Direction.Axis.Y);
        this.zMin = dct.firstFull(Direction.Axis.Z);
        this.xMax = dct.lastFull(Direction.Axis.X);
        this.yMax = dct.lastFull(Direction.Axis.Y);
        this.zMax = dct.lastFull(Direction.Axis.Z);
    }
    
    protected int getIndex(final int integer1, final int integer2, final int integer3) {
        return (integer1 * this.ySize + integer2) * this.zSize + integer3;
    }
    
    @Override
    public boolean isFull(final int integer1, final int integer2, final int integer3) {
        return this.storage.get(this.getIndex(integer1, integer2, integer3));
    }
    
    @Override
    public void setFull(final int integer1, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5) {
        this.storage.set(this.getIndex(integer1, integer2, integer3), boolean5);
        if (boolean4 && boolean5) {
            this.xMin = Math.min(this.xMin, integer1);
            this.yMin = Math.min(this.yMin, integer2);
            this.zMin = Math.min(this.zMin, integer3);
            this.xMax = Math.max(this.xMax, integer1 + 1);
            this.yMax = Math.max(this.yMax, integer2 + 1);
            this.zMax = Math.max(this.zMax, integer3 + 1);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.storage.isEmpty();
    }
    
    @Override
    public int firstFull(final Direction.Axis a) {
        return a.choose(this.xMin, this.yMin, this.zMin);
    }
    
    @Override
    public int lastFull(final Direction.Axis a) {
        return a.choose(this.xMax, this.yMax, this.zMax);
    }
    
    @Override
    protected boolean isZStripFull(final int integer1, final int integer2, final int integer3, final int integer4) {
        return integer3 >= 0 && integer4 >= 0 && integer1 >= 0 && integer3 < this.xSize && integer4 < this.ySize && integer2 <= this.zSize && this.storage.nextClearBit(this.getIndex(integer3, integer4, integer1)) >= this.getIndex(integer3, integer4, integer2);
    }
    
    @Override
    protected void setZStrip(final int integer1, final int integer2, final int integer3, final int integer4, final boolean boolean5) {
        this.storage.set(this.getIndex(integer3, integer4, integer1), this.getIndex(integer3, integer4, integer2), boolean5);
    }
    
    static BitSetDiscreteVoxelShape join(final DiscreteVoxelShape dct1, final DiscreteVoxelShape dct2, final IndexMerger dcw3, final IndexMerger dcw4, final IndexMerger dcw5, final BooleanOp dco) {
        final BitSetDiscreteVoxelShape dcn7 = new BitSetDiscreteVoxelShape(dcw3.getList().size() - 1, dcw4.getList().size() - 1, dcw5.getList().size() - 1);
        final int[] arr8 = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
        final boolean[] arr9;
        final boolean[] arr10;
        final boolean boolean14;
        final BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape;
        final Object o;
        final Object o2;
        final boolean boolean13;
        final Object o3;
        final Object o4;
        final boolean boolean12;
        final Object o5;
        dcw3.forMergedIndexes((integer8, integer9, integer10) -> {
            arr9 = new boolean[] { false };
            boolean12 = dcw4.forMergedIndexes((integer11, integer12, integer13) -> {
                arr10 = new boolean[] { false };
                boolean13 = dcw5.forMergedIndexes((integer13, integer14, integer15) -> {
                    boolean14 = dco.apply(dct1.isFullWide(integer8, integer11, integer13), dct2.isFullWide(integer9, integer12, integer14));
                    if (boolean14) {
                        bitSetDiscreteVoxelShape.storage.set(bitSetDiscreteVoxelShape.getIndex(integer10, integer13, integer15));
                        o[2] = Math.min(o[2], integer15);
                        o[5] = Math.max(o[5], integer15);
                        o2[0] = true;
                    }
                    return true;
                });
                if (arr10[0]) {
                    o3[1] = Math.min(o3[1], integer13);
                    o3[4] = Math.max(o3[4], integer13);
                    o4[0] = true;
                }
                return boolean13;
            });
            if (arr9[0]) {
                o5[0] = Math.min(o5[0], integer10);
                o5[3] = Math.max(o5[3], integer10);
            }
            return boolean12;
        });
        dcn7.xMin = arr8[0];
        dcn7.yMin = arr8[1];
        dcn7.zMin = arr8[2];
        dcn7.xMax = arr8[3] + 1;
        dcn7.yMax = arr8[4] + 1;
        dcn7.zMax = arr8[5] + 1;
        return dcn7;
    }
}
