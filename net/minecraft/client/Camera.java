package net.minecraft.client;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

public class Camera {
    private boolean initialized;
    private BlockGetter level;
    private Entity entity;
    private Vec3 position;
    private final BlockPos.MutableBlockPos blockPosition;
    private final Vector3f forwards;
    private final Vector3f up;
    private final Vector3f left;
    private float xRot;
    private float yRot;
    private final Quaternion rotation;
    private boolean detached;
    private boolean mirror;
    private float eyeHeight;
    private float eyeHeightOld;
    
    public Camera() {
        this.position = Vec3.ZERO;
        this.blockPosition = new BlockPos.MutableBlockPos();
        this.forwards = new Vector3f(0.0f, 0.0f, 1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.left = new Vector3f(1.0f, 0.0f, 0.0f);
        this.rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
    }
    
    public void setup(final BlockGetter bqz, final Entity apx, final boolean boolean3, final boolean boolean4, final float float5) {
        this.initialized = true;
        this.level = bqz;
        this.entity = apx;
        this.detached = boolean3;
        this.mirror = boolean4;
        this.setRotation(apx.getViewYRot(float5), apx.getViewXRot(float5));
        this.setPosition(Mth.lerp(float5, apx.xo, apx.getX()), Mth.lerp(float5, apx.yo, apx.getY()) + Mth.lerp(float5, this.eyeHeightOld, this.eyeHeight), Mth.lerp(float5, apx.zo, apx.getZ()));
        if (boolean3) {
            if (boolean4) {
                this.setRotation(this.yRot + 180.0f, -this.xRot);
            }
            this.move(-this.getMaxZoom(4.0), 0.0, 0.0);
        }
        else if (apx instanceof LivingEntity && ((LivingEntity)apx).isSleeping()) {
            final Direction gc7 = ((LivingEntity)apx).getBedOrientation();
            this.setRotation((gc7 != null) ? (gc7.toYRot() - 180.0f) : 0.0f, 0.0f);
            this.move(0.0, 0.3, 0.0);
        }
    }
    
    public void tick() {
        if (this.entity != null) {
            this.eyeHeightOld = this.eyeHeight;
            this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5f;
        }
    }
    
    private double getMaxZoom(double double1) {
        for (int integer4 = 0; integer4 < 8; ++integer4) {
            float float5 = (float)((integer4 & 0x1) * 2 - 1);
            float float6 = (float)((integer4 >> 1 & 0x1) * 2 - 1);
            float float7 = (float)((integer4 >> 2 & 0x1) * 2 - 1);
            float5 *= 0.1f;
            float6 *= 0.1f;
            float7 *= 0.1f;
            final Vec3 dck8 = this.position.add(float5, float6, float7);
            final Vec3 dck9 = new Vec3(this.position.x - this.forwards.x() * double1 + float5 + float7, this.position.y - this.forwards.y() * double1 + float6, this.position.z - this.forwards.z() * double1 + float7);
            final HitResult dci10 = this.level.clip(new ClipContext(dck8, dck9, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.entity));
            if (dci10.getType() != HitResult.Type.MISS) {
                final double double2 = dci10.getLocation().distanceTo(this.position);
                if (double2 < double1) {
                    double1 = double2;
                }
            }
        }
        return double1;
    }
    
    protected void move(final double double1, final double double2, final double double3) {
        final double double4 = this.forwards.x() * double1 + this.up.x() * double2 + this.left.x() * double3;
        final double double5 = this.forwards.y() * double1 + this.up.y() * double2 + this.left.y() * double3;
        final double double6 = this.forwards.z() * double1 + this.up.z() * double2 + this.left.z() * double3;
        this.setPosition(new Vec3(this.position.x + double4, this.position.y + double5, this.position.z + double6));
    }
    
    protected void setRotation(final float float1, final float float2) {
        this.xRot = float2;
        this.yRot = float1;
        this.rotation.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.rotation.mul(Vector3f.YP.rotationDegrees(-float1));
        this.rotation.mul(Vector3f.XP.rotationDegrees(float2));
        this.forwards.set(0.0f, 0.0f, 1.0f);
        this.forwards.transform(this.rotation);
        this.up.set(0.0f, 1.0f, 0.0f);
        this.up.transform(this.rotation);
        this.left.set(1.0f, 0.0f, 0.0f);
        this.left.transform(this.rotation);
    }
    
    protected void setPosition(final double double1, final double double2, final double double3) {
        this.setPosition(new Vec3(double1, double2, double3));
    }
    
    protected void setPosition(final Vec3 dck) {
        this.position = dck;
        this.blockPosition.set(dck.x, dck.y, dck.z);
    }
    
    public Vec3 getPosition() {
        return this.position;
    }
    
    public BlockPos getBlockPosition() {
        return this.blockPosition;
    }
    
    public float getXRot() {
        return this.xRot;
    }
    
    public float getYRot() {
        return this.yRot;
    }
    
    public Quaternion rotation() {
        return this.rotation;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public boolean isDetached() {
        return this.detached;
    }
    
    public FluidState getFluidInCamera() {
        if (!this.initialized) {
            return Fluids.EMPTY.defaultFluidState();
        }
        final FluidState cuu2 = this.level.getFluidState(this.blockPosition);
        if (!cuu2.isEmpty() && this.position.y >= this.blockPosition.getY() + cuu2.getHeight(this.level, this.blockPosition)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        return cuu2;
    }
    
    public final Vector3f getLookVector() {
        return this.forwards;
    }
    
    public final Vector3f getUpVector() {
        return this.up;
    }
    
    public void reset() {
        this.level = null;
        this.entity = null;
        this.initialized = false;
    }
}
