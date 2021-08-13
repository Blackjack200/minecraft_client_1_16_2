package net.minecraft.client.resources.model;

import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mojang.math.OctahedralGroup;
import com.mojang.math.Transformation;
import java.util.Map;

public enum BlockModelRotation implements ModelState {
    X0_Y0(0, 0), 
    X0_Y90(0, 90), 
    X0_Y180(0, 180), 
    X0_Y270(0, 270), 
    X90_Y0(90, 0), 
    X90_Y90(90, 90), 
    X90_Y180(90, 180), 
    X90_Y270(90, 270), 
    X180_Y0(180, 0), 
    X180_Y90(180, 90), 
    X180_Y180(180, 180), 
    X180_Y270(180, 270), 
    X270_Y0(270, 0), 
    X270_Y90(270, 90), 
    X270_Y180(270, 180), 
    X270_Y270(270, 270);
    
    private static final Map<Integer, BlockModelRotation> BY_INDEX;
    private final Transformation transformation;
    private final OctahedralGroup actualRotation;
    private final int index;
    
    private static int getIndex(final int integer1, final int integer2) {
        return integer1 * 360 + integer2;
    }
    
    private BlockModelRotation(final int integer3, final int integer4) {
        this.index = getIndex(integer3, integer4);
        final Quaternion d6 = new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)(-integer4), true);
        d6.mul(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), (float)(-integer3), true));
        OctahedralGroup c7 = OctahedralGroup.IDENTITY;
        for (int integer5 = 0; integer5 < integer4; integer5 += 90) {
            c7 = c7.compose(OctahedralGroup.ROT_90_Y_NEG);
        }
        for (int integer5 = 0; integer5 < integer3; integer5 += 90) {
            c7 = c7.compose(OctahedralGroup.ROT_90_X_NEG);
        }
        this.transformation = new Transformation(null, d6, null, null);
        this.actualRotation = c7;
    }
    
    public Transformation getRotation() {
        return this.transformation;
    }
    
    public static BlockModelRotation by(final int integer1, final int integer2) {
        return (BlockModelRotation)BlockModelRotation.BY_INDEX.get(getIndex(Mth.positiveModulo(integer1, 360), Mth.positiveModulo(integer2, 360)));
    }
    
    static {
        BY_INDEX = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(elh -> elh.index, elh -> elh));
    }
}
