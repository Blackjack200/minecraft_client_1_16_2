package com.mojang.math;

import java.util.function.Supplier;
import net.minecraft.Util;
import java.util.Objects;
import org.apache.commons.lang3.tuple.Triple;
import com.mojang.datafixers.util.Pair;
import javax.annotation.Nullable;

public final class Transformation {
    private final Matrix4f matrix;
    private boolean decomposed;
    @Nullable
    private Vector3f translation;
    @Nullable
    private Quaternion leftRotation;
    @Nullable
    private Vector3f scale;
    @Nullable
    private Quaternion rightRotation;
    private static final Transformation IDENTITY;
    
    public Transformation(@Nullable final Matrix4f b) {
        if (b == null) {
            this.matrix = Transformation.IDENTITY.matrix;
        }
        else {
            this.matrix = b;
        }
    }
    
    public Transformation(@Nullable final Vector3f g1, @Nullable final Quaternion d2, @Nullable final Vector3f g3, @Nullable final Quaternion d4) {
        this.matrix = compose(g1, d2, g3, d4);
        this.translation = ((g1 != null) ? g1 : new Vector3f());
        this.leftRotation = ((d2 != null) ? d2 : Quaternion.ONE.copy());
        this.scale = ((g3 != null) ? g3 : new Vector3f(1.0f, 1.0f, 1.0f));
        this.rightRotation = ((d4 != null) ? d4 : Quaternion.ONE.copy());
        this.decomposed = true;
    }
    
    public static Transformation identity() {
        return Transformation.IDENTITY;
    }
    
    public Transformation compose(final Transformation f) {
        final Matrix4f b3 = this.getMatrix();
        b3.multiply(f.getMatrix());
        return new Transformation(b3);
    }
    
    @Nullable
    public Transformation inverse() {
        if (this == Transformation.IDENTITY) {
            return this;
        }
        final Matrix4f b2 = this.getMatrix();
        if (b2.invert()) {
            return new Transformation(b2);
        }
        return null;
    }
    
    private void ensureDecomposed() {
        if (!this.decomposed) {
            final Pair<Matrix3f, Vector3f> pair2 = toAffine(this.matrix);
            final Triple<Quaternion, Vector3f, Quaternion> triple3 = ((Matrix3f)pair2.getFirst()).svdDecompose();
            this.translation = (Vector3f)pair2.getSecond();
            this.leftRotation = (Quaternion)triple3.getLeft();
            this.scale = (Vector3f)triple3.getMiddle();
            this.rightRotation = (Quaternion)triple3.getRight();
            this.decomposed = true;
        }
    }
    
    private static Matrix4f compose(@Nullable final Vector3f g1, @Nullable final Quaternion d2, @Nullable final Vector3f g3, @Nullable final Quaternion d4) {
        final Matrix4f b5 = new Matrix4f();
        b5.setIdentity();
        if (d2 != null) {
            b5.multiply(new Matrix4f(d2));
        }
        if (g3 != null) {
            b5.multiply(Matrix4f.createScaleMatrix(g3.x(), g3.y(), g3.z()));
        }
        if (d4 != null) {
            b5.multiply(new Matrix4f(d4));
        }
        if (g1 != null) {
            b5.m03 = g1.x();
            b5.m13 = g1.y();
            b5.m23 = g1.z();
        }
        return b5;
    }
    
    public static Pair<Matrix3f, Vector3f> toAffine(final Matrix4f b) {
        b.multiply(1.0f / b.m33);
        final Vector3f g2 = new Vector3f(b.m03, b.m13, b.m23);
        final Matrix3f a3 = new Matrix3f(b);
        return (Pair<Matrix3f, Vector3f>)Pair.of(a3, g2);
    }
    
    public Matrix4f getMatrix() {
        return this.matrix.copy();
    }
    
    public Quaternion getLeftRotation() {
        this.ensureDecomposed();
        return this.leftRotation.copy();
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Transformation f3 = (Transformation)object;
        return Objects.equals(this.matrix, f3.matrix);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.matrix });
    }
    
    static {
        IDENTITY = Util.<Transformation>make((java.util.function.Supplier<Transformation>)(() -> {
            final Matrix4f b1 = new Matrix4f();
            b1.setIdentity();
            final Transformation f2 = new Transformation(b1);
            f2.getLeftRotation();
            return f2;
        }));
    }
}
