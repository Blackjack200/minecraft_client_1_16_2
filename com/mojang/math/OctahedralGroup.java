package com.mojang.math;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.core.FrontAndTop;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanList;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.util.StringRepresentable;

public enum OctahedralGroup implements StringRepresentable {
    IDENTITY("identity", SymmetricGroup3.P123, false, false, false), 
    ROT_180_FACE_XY("rot_180_face_xy", SymmetricGroup3.P123, true, true, false), 
    ROT_180_FACE_XZ("rot_180_face_xz", SymmetricGroup3.P123, true, false, true), 
    ROT_180_FACE_YZ("rot_180_face_yz", SymmetricGroup3.P123, false, true, true), 
    ROT_120_NNN("rot_120_nnn", SymmetricGroup3.P231, false, false, false), 
    ROT_120_NNP("rot_120_nnp", SymmetricGroup3.P312, true, false, true), 
    ROT_120_NPN("rot_120_npn", SymmetricGroup3.P312, false, true, true), 
    ROT_120_NPP("rot_120_npp", SymmetricGroup3.P231, true, false, true), 
    ROT_120_PNN("rot_120_pnn", SymmetricGroup3.P312, true, true, false), 
    ROT_120_PNP("rot_120_pnp", SymmetricGroup3.P231, true, true, false), 
    ROT_120_PPN("rot_120_ppn", SymmetricGroup3.P231, false, true, true), 
    ROT_120_PPP("rot_120_ppp", SymmetricGroup3.P312, false, false, false), 
    ROT_180_EDGE_XY_NEG("rot_180_edge_xy_neg", SymmetricGroup3.P213, true, true, true), 
    ROT_180_EDGE_XY_POS("rot_180_edge_xy_pos", SymmetricGroup3.P213, false, false, true), 
    ROT_180_EDGE_XZ_NEG("rot_180_edge_xz_neg", SymmetricGroup3.P321, true, true, true), 
    ROT_180_EDGE_XZ_POS("rot_180_edge_xz_pos", SymmetricGroup3.P321, false, true, false), 
    ROT_180_EDGE_YZ_NEG("rot_180_edge_yz_neg", SymmetricGroup3.P132, true, true, true), 
    ROT_180_EDGE_YZ_POS("rot_180_edge_yz_pos", SymmetricGroup3.P132, true, false, false), 
    ROT_90_X_NEG("rot_90_x_neg", SymmetricGroup3.P132, false, false, true), 
    ROT_90_X_POS("rot_90_x_pos", SymmetricGroup3.P132, false, true, false), 
    ROT_90_Y_NEG("rot_90_y_neg", SymmetricGroup3.P321, true, false, false), 
    ROT_90_Y_POS("rot_90_y_pos", SymmetricGroup3.P321, false, false, true), 
    ROT_90_Z_NEG("rot_90_z_neg", SymmetricGroup3.P213, false, true, false), 
    ROT_90_Z_POS("rot_90_z_pos", SymmetricGroup3.P213, true, false, false), 
    INVERSION("inversion", SymmetricGroup3.P123, true, true, true), 
    INVERT_X("invert_x", SymmetricGroup3.P123, true, false, false), 
    INVERT_Y("invert_y", SymmetricGroup3.P123, false, true, false), 
    INVERT_Z("invert_z", SymmetricGroup3.P123, false, false, true), 
    ROT_60_REF_NNN("rot_60_ref_nnn", SymmetricGroup3.P312, true, true, true), 
    ROT_60_REF_NNP("rot_60_ref_nnp", SymmetricGroup3.P231, true, false, false), 
    ROT_60_REF_NPN("rot_60_ref_npn", SymmetricGroup3.P231, false, false, true), 
    ROT_60_REF_NPP("rot_60_ref_npp", SymmetricGroup3.P312, false, false, true), 
    ROT_60_REF_PNN("rot_60_ref_pnn", SymmetricGroup3.P231, false, true, false), 
    ROT_60_REF_PNP("rot_60_ref_pnp", SymmetricGroup3.P312, true, false, false), 
    ROT_60_REF_PPN("rot_60_ref_ppn", SymmetricGroup3.P312, false, true, false), 
    ROT_60_REF_PPP("rot_60_ref_ppp", SymmetricGroup3.P231, true, true, true), 
    SWAP_XY("swap_xy", SymmetricGroup3.P213, false, false, false), 
    SWAP_YZ("swap_yz", SymmetricGroup3.P132, false, false, false), 
    SWAP_XZ("swap_xz", SymmetricGroup3.P321, false, false, false), 
    SWAP_NEG_XY("swap_neg_xy", SymmetricGroup3.P213, true, true, false), 
    SWAP_NEG_YZ("swap_neg_yz", SymmetricGroup3.P132, false, true, true), 
    SWAP_NEG_XZ("swap_neg_xz", SymmetricGroup3.P321, true, false, true), 
    ROT_90_REF_X_NEG("rot_90_ref_x_neg", SymmetricGroup3.P132, true, false, true), 
    ROT_90_REF_X_POS("rot_90_ref_x_pos", SymmetricGroup3.P132, true, true, false), 
    ROT_90_REF_Y_NEG("rot_90_ref_y_neg", SymmetricGroup3.P321, true, true, false), 
    ROT_90_REF_Y_POS("rot_90_ref_y_pos", SymmetricGroup3.P321, false, true, true), 
    ROT_90_REF_Z_NEG("rot_90_ref_z_neg", SymmetricGroup3.P213, false, true, true), 
    ROT_90_REF_Z_POS("rot_90_ref_z_pos", SymmetricGroup3.P213, true, false, true);
    
    private final Matrix3f transformation;
    private final String name;
    @Nullable
    private Map<Direction, Direction> rotatedDirections;
    private final boolean invertX;
    private final boolean invertY;
    private final boolean invertZ;
    private final SymmetricGroup3 permutation;
    private static final OctahedralGroup[][] cayleyTable;
    private static final OctahedralGroup[] inverseTable;
    
    private OctahedralGroup(final String string3, final SymmetricGroup3 e, final boolean boolean5, final boolean boolean6, final boolean boolean7) {
        this.name = string3;
        this.invertX = boolean5;
        this.invertY = boolean6;
        this.invertZ = boolean7;
        this.permutation = e;
        this.transformation = new Matrix3f();
        this.transformation.m00 = (boolean5 ? -1.0f : 1.0f);
        this.transformation.m11 = (boolean6 ? -1.0f : 1.0f);
        this.transformation.m22 = (boolean7 ? -1.0f : 1.0f);
        this.transformation.mul(e.transformation());
    }
    
    private BooleanList packInversions() {
        return (BooleanList)new BooleanArrayList(new boolean[] { this.invertX, this.invertY, this.invertZ });
    }
    
    public OctahedralGroup compose(final OctahedralGroup c) {
        return OctahedralGroup.cayleyTable[this.ordinal()][c.ordinal()];
    }
    
    public String toString() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    public Direction rotate(final Direction gc) {
        if (this.rotatedDirections == null) {
            this.rotatedDirections = (Map<Direction, Direction>)Maps.newEnumMap((Class)Direction.class);
            for (final Direction gc2 : Direction.values()) {
                final Direction.Axis a7 = gc2.getAxis();
                final Direction.AxisDirection b8 = gc2.getAxisDirection();
                final Direction.Axis a8 = Direction.Axis.values()[this.permutation.permutation(a7.ordinal())];
                final Direction.AxisDirection b9 = this.inverts(a8) ? b8.opposite() : b8;
                final Direction gc3 = Direction.fromAxisAndDirection(a8, b9);
                this.rotatedDirections.put(gc2, gc3);
            }
        }
        return (Direction)this.rotatedDirections.get(gc);
    }
    
    public boolean inverts(final Direction.Axis a) {
        switch (a) {
            case X: {
                return this.invertX;
            }
            case Y: {
                return this.invertY;
            }
            default: {
                return this.invertZ;
            }
        }
    }
    
    public FrontAndTop rotate(final FrontAndTop ge) {
        return FrontAndTop.fromFrontAndTop(this.rotate(ge.front()), this.rotate(ge.top()));
    }
    
    static {
        cayleyTable = Util.<OctahedralGroup[][]>make(new OctahedralGroup[values().length][values().length], (java.util.function.Consumer<OctahedralGroup[][]>)(arr -> {
            final Map<Pair<SymmetricGroup3, BooleanList>, OctahedralGroup> map2 = (Map<Pair<SymmetricGroup3, BooleanList>, OctahedralGroup>)Arrays.stream((Object[])values()).collect(Collectors.toMap(c -> Pair.of(c.permutation, c.packInversions()), c -> c));
            for (final OctahedralGroup c6 : values()) {
                for (final OctahedralGroup c7 : values()) {
                    final BooleanList booleanList11 = c6.packInversions();
                    final BooleanList booleanList12 = c7.packInversions();
                    final SymmetricGroup3 e13 = c7.permutation.compose(c6.permutation);
                    final BooleanArrayList booleanArrayList14 = new BooleanArrayList(3);
                    for (int integer15 = 0; integer15 < 3; ++integer15) {
                        booleanArrayList14.add(booleanList11.getBoolean(integer15) ^ booleanList12.getBoolean(c6.permutation.permutation(integer15)));
                    }
                    arr[c6.ordinal()][c7.ordinal()] = (OctahedralGroup)map2.get(Pair.of((Object)e13, (Object)booleanArrayList14));
                }
            }
        }));
        inverseTable = (OctahedralGroup[])Arrays.stream((Object[])values()).map(c -> (OctahedralGroup)Arrays.stream((Object[])values()).filter(c2 -> c.compose(c2) == OctahedralGroup.IDENTITY).findAny().get()).toArray(OctahedralGroup[]::new);
    }
}
