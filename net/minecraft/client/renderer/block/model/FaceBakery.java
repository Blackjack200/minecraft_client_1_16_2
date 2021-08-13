package net.minecraft.client.renderer.block.model;

import net.minecraft.core.Vec3i;
import com.mojang.math.Quaternion;
import net.minecraft.client.renderer.FaceInfo;
import com.mojang.math.Matrix4f;
import com.mojang.math.Matrix3f;
import com.mojang.math.Vector4f;
import java.util.function.Supplier;
import net.minecraft.core.BlockMath;
import com.mojang.math.Transformation;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.math.Vector3f;

public class FaceBakery {
    private static final float RESCALE_22_5;
    private static final float RESCALE_45;
    
    public BakedQuad bakeQuad(final Vector3f g1, final Vector3f g2, final BlockElementFace eau, final TextureAtlasSprite eju, final Direction gc, final ModelState eln, @Nullable final BlockElementRotation eav, final boolean boolean8, final ResourceLocation vk) {
        BlockFaceUV eaw11 = eau.uv;
        if (eln.isUvLocked()) {
            eaw11 = recomputeUVs(eau.uv, gc, eln.getRotation(), vk);
        }
        final float[] arr12 = new float[eaw11.uvs.length];
        System.arraycopy(eaw11.uvs, 0, arr12, 0, arr12.length);
        final float float13 = eju.uvShrinkRatio();
        final float float14 = (eaw11.uvs[0] + eaw11.uvs[0] + eaw11.uvs[2] + eaw11.uvs[2]) / 4.0f;
        final float float15 = (eaw11.uvs[1] + eaw11.uvs[1] + eaw11.uvs[3] + eaw11.uvs[3]) / 4.0f;
        eaw11.uvs[0] = Mth.lerp(float13, eaw11.uvs[0], float14);
        eaw11.uvs[2] = Mth.lerp(float13, eaw11.uvs[2], float14);
        eaw11.uvs[1] = Mth.lerp(float13, eaw11.uvs[1], float15);
        eaw11.uvs[3] = Mth.lerp(float13, eaw11.uvs[3], float15);
        final int[] arr13 = this.makeVertices(eaw11, eju, gc, this.setupShape(g1, g2), eln.getRotation(), eav, boolean8);
        final Direction gc2 = calculateFacing(arr13);
        System.arraycopy(arr12, 0, eaw11.uvs, 0, arr12.length);
        if (eav == null) {
            this.recalculateWinding(arr13, gc2);
        }
        return new BakedQuad(arr13, eau.tintIndex, gc2, eju, boolean8);
    }
    
    public static BlockFaceUV recomputeUVs(final BlockFaceUV eaw, final Direction gc, final Transformation f, final ResourceLocation vk) {
        final Matrix4f b5 = BlockMath.getUVLockTransform(f, gc, (Supplier<String>)(() -> new StringBuilder().append("Unable to resolve UVLock for model: ").append(vk).toString())).getMatrix();
        final float float6 = eaw.getU(eaw.getReverseIndex(0));
        final float float7 = eaw.getV(eaw.getReverseIndex(0));
        final Vector4f h8 = new Vector4f(float6 / 16.0f, float7 / 16.0f, 0.0f, 1.0f);
        h8.transform(b5);
        final float float8 = 16.0f * h8.x();
        final float float9 = 16.0f * h8.y();
        final float float10 = eaw.getU(eaw.getReverseIndex(2));
        final float float11 = eaw.getV(eaw.getReverseIndex(2));
        final Vector4f h9 = new Vector4f(float10 / 16.0f, float11 / 16.0f, 0.0f, 1.0f);
        h9.transform(b5);
        final float float12 = 16.0f * h9.x();
        final float float13 = 16.0f * h9.y();
        float float14;
        float float15;
        if (Math.signum(float10 - float6) == Math.signum(float12 - float8)) {
            float14 = float8;
            float15 = float12;
        }
        else {
            float14 = float12;
            float15 = float8;
        }
        float float16;
        float float17;
        if (Math.signum(float11 - float7) == Math.signum(float13 - float9)) {
            float16 = float9;
            float17 = float13;
        }
        else {
            float16 = float13;
            float17 = float9;
        }
        final float float18 = (float)Math.toRadians((double)eaw.rotation);
        final Vector3f g21 = new Vector3f(Mth.cos(float18), Mth.sin(float18), 0.0f);
        final Matrix3f a22 = new Matrix3f(b5);
        g21.transform(a22);
        final int integer23 = Math.floorMod(-(int)Math.round(Math.toDegrees(Math.atan2((double)g21.y(), (double)g21.x())) / 90.0) * 90, 360);
        return new BlockFaceUV(new float[] { float14, float16, float15, float17 }, integer23);
    }
    
    private int[] makeVertices(final BlockFaceUV eaw, final TextureAtlasSprite eju, final Direction gc, final float[] arr, final Transformation f, @Nullable final BlockElementRotation eav, final boolean boolean7) {
        final int[] arr2 = new int[32];
        for (int integer10 = 0; integer10 < 4; ++integer10) {
            this.bakeVertex(arr2, integer10, gc, eaw, arr, eju, f, eav, boolean7);
        }
        return arr2;
    }
    
    private float[] setupShape(final Vector3f g1, final Vector3f g2) {
        final float[] arr4 = new float[Direction.values().length];
        arr4[FaceInfo.Constants.MIN_X] = g1.x() / 16.0f;
        arr4[FaceInfo.Constants.MIN_Y] = g1.y() / 16.0f;
        arr4[FaceInfo.Constants.MIN_Z] = g1.z() / 16.0f;
        arr4[FaceInfo.Constants.MAX_X] = g2.x() / 16.0f;
        arr4[FaceInfo.Constants.MAX_Y] = g2.y() / 16.0f;
        arr4[FaceInfo.Constants.MAX_Z] = g2.z() / 16.0f;
        return arr4;
    }
    
    private void bakeVertex(final int[] arr, final int integer, final Direction gc, final BlockFaceUV eaw, final float[] arr, final TextureAtlasSprite eju, final Transformation f, @Nullable final BlockElementRotation eav, final boolean boolean9) {
        final FaceInfo.VertexInfo b11 = FaceInfo.fromFacing(gc).getVertexInfo(integer);
        final Vector3f g12 = new Vector3f(arr[b11.xFace], arr[b11.yFace], arr[b11.zFace]);
        this.applyElementRotation(g12, eav);
        this.applyModelRotation(g12, f);
        this.fillVertex(arr, integer, g12, eju, eaw);
    }
    
    private void fillVertex(final int[] arr, final int integer, final Vector3f g, final TextureAtlasSprite eju, final BlockFaceUV eaw) {
        final int integer2 = integer * 8;
        arr[integer2] = Float.floatToRawIntBits(g.x());
        arr[integer2 + 1] = Float.floatToRawIntBits(g.y());
        arr[integer2 + 2] = Float.floatToRawIntBits(g.z());
        arr[integer2 + 3] = -1;
        arr[integer2 + 4] = Float.floatToRawIntBits(eju.getU(eaw.getU(integer)));
        arr[integer2 + 4 + 1] = Float.floatToRawIntBits(eju.getV(eaw.getV(integer)));
    }
    
    private void applyElementRotation(final Vector3f g, @Nullable final BlockElementRotation eav) {
        if (eav == null) {
            return;
        }
        Vector3f g2 = null;
        Vector3f g3 = null;
        switch (eav.axis) {
            case X: {
                g2 = new Vector3f(1.0f, 0.0f, 0.0f);
                g3 = new Vector3f(0.0f, 1.0f, 1.0f);
                break;
            }
            case Y: {
                g2 = new Vector3f(0.0f, 1.0f, 0.0f);
                g3 = new Vector3f(1.0f, 0.0f, 1.0f);
                break;
            }
            case Z: {
                g2 = new Vector3f(0.0f, 0.0f, 1.0f);
                g3 = new Vector3f(1.0f, 1.0f, 0.0f);
                break;
            }
            default: {
                throw new IllegalArgumentException("There are only 3 axes");
            }
        }
        final Quaternion d6 = new Quaternion(g2, eav.angle, true);
        if (eav.rescale) {
            if (Math.abs(eav.angle) == 22.5f) {
                g3.mul(FaceBakery.RESCALE_22_5);
            }
            else {
                g3.mul(FaceBakery.RESCALE_45);
            }
            g3.add(1.0f, 1.0f, 1.0f);
        }
        else {
            g3.set(1.0f, 1.0f, 1.0f);
        }
        this.rotateVertexBy(g, eav.origin.copy(), new Matrix4f(d6), g3);
    }
    
    public void applyModelRotation(final Vector3f g, final Transformation f) {
        if (f == Transformation.identity()) {
            return;
        }
        this.rotateVertexBy(g, new Vector3f(0.5f, 0.5f, 0.5f), f.getMatrix(), new Vector3f(1.0f, 1.0f, 1.0f));
    }
    
    private void rotateVertexBy(final Vector3f g1, final Vector3f g2, final Matrix4f b, final Vector3f g4) {
        final Vector4f h6 = new Vector4f(g1.x() - g2.x(), g1.y() - g2.y(), g1.z() - g2.z(), 1.0f);
        h6.transform(b);
        h6.mul(g4);
        g1.set(h6.x() + g2.x(), h6.y() + g2.y(), h6.z() + g2.z());
    }
    
    public static Direction calculateFacing(final int[] arr) {
        final Vector3f g2 = new Vector3f(Float.intBitsToFloat(arr[0]), Float.intBitsToFloat(arr[1]), Float.intBitsToFloat(arr[2]));
        final Vector3f g3 = new Vector3f(Float.intBitsToFloat(arr[8]), Float.intBitsToFloat(arr[9]), Float.intBitsToFloat(arr[10]));
        final Vector3f g4 = new Vector3f(Float.intBitsToFloat(arr[16]), Float.intBitsToFloat(arr[17]), Float.intBitsToFloat(arr[18]));
        final Vector3f g5 = g2.copy();
        g5.sub(g3);
        final Vector3f g6 = g4.copy();
        g6.sub(g3);
        final Vector3f g7 = g6.copy();
        g7.cross(g5);
        g7.normalize();
        Direction gc8 = null;
        float float9 = 0.0f;
        for (final Direction gc9 : Direction.values()) {
            final Vec3i gr14 = gc9.getNormal();
            final Vector3f g8 = new Vector3f((float)gr14.getX(), (float)gr14.getY(), (float)gr14.getZ());
            final float float10 = g7.dot(g8);
            if (float10 >= 0.0f && float10 > float9) {
                float9 = float10;
                gc8 = gc9;
            }
        }
        if (gc8 == null) {
            return Direction.UP;
        }
        return gc8;
    }
    
    private void recalculateWinding(final int[] arr, final Direction gc) {
        final int[] arr2 = new int[arr.length];
        System.arraycopy(arr, 0, arr2, 0, arr.length);
        final float[] arr3 = new float[Direction.values().length];
        arr3[FaceInfo.Constants.MIN_X] = 999.0f;
        arr3[FaceInfo.Constants.MIN_Y] = 999.0f;
        arr3[FaceInfo.Constants.MIN_Z] = 999.0f;
        arr3[FaceInfo.Constants.MAX_X] = -999.0f;
        arr3[FaceInfo.Constants.MAX_Y] = -999.0f;
        arr3[FaceInfo.Constants.MAX_Z] = -999.0f;
        for (int integer6 = 0; integer6 < 4; ++integer6) {
            final int integer7 = 8 * integer6;
            final float float8 = Float.intBitsToFloat(arr2[integer7]);
            final float float9 = Float.intBitsToFloat(arr2[integer7 + 1]);
            final float float10 = Float.intBitsToFloat(arr2[integer7 + 2]);
            if (float8 < arr3[FaceInfo.Constants.MIN_X]) {
                arr3[FaceInfo.Constants.MIN_X] = float8;
            }
            if (float9 < arr3[FaceInfo.Constants.MIN_Y]) {
                arr3[FaceInfo.Constants.MIN_Y] = float9;
            }
            if (float10 < arr3[FaceInfo.Constants.MIN_Z]) {
                arr3[FaceInfo.Constants.MIN_Z] = float10;
            }
            if (float8 > arr3[FaceInfo.Constants.MAX_X]) {
                arr3[FaceInfo.Constants.MAX_X] = float8;
            }
            if (float9 > arr3[FaceInfo.Constants.MAX_Y]) {
                arr3[FaceInfo.Constants.MAX_Y] = float9;
            }
            if (float10 > arr3[FaceInfo.Constants.MAX_Z]) {
                arr3[FaceInfo.Constants.MAX_Z] = float10;
            }
        }
        final FaceInfo dzp6 = FaceInfo.fromFacing(gc);
        for (int integer7 = 0; integer7 < 4; ++integer7) {
            final int integer8 = 8 * integer7;
            final FaceInfo.VertexInfo b9 = dzp6.getVertexInfo(integer7);
            final float float10 = arr3[b9.xFace];
            final float float11 = arr3[b9.yFace];
            final float float12 = arr3[b9.zFace];
            arr[integer8] = Float.floatToRawIntBits(float10);
            arr[integer8 + 1] = Float.floatToRawIntBits(float11);
            arr[integer8 + 2] = Float.floatToRawIntBits(float12);
            for (int integer9 = 0; integer9 < 4; ++integer9) {
                final int integer10 = 8 * integer9;
                final float float13 = Float.intBitsToFloat(arr2[integer10]);
                final float float14 = Float.intBitsToFloat(arr2[integer10 + 1]);
                final float float15 = Float.intBitsToFloat(arr2[integer10 + 2]);
                if (Mth.equal(float10, float13) && Mth.equal(float11, float14) && Mth.equal(float12, float15)) {
                    arr[integer8 + 4] = arr2[integer10 + 4];
                    arr[integer8 + 4 + 1] = arr2[integer10 + 4 + 1];
                }
            }
        }
    }
    
    static {
        RESCALE_22_5 = 1.0f / (float)Math.cos(0.39269909262657166) - 1.0f;
        RESCALE_45 = 1.0f / (float)Math.cos(0.7853981852531433) - 1.0f;
    }
}
