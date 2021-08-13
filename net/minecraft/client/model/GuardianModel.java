package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Minecraft;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Guardian;

public class GuardianModel extends ListModel<Guardian> {
    private static final float[] SPIKE_X_ROT;
    private static final float[] SPIKE_Y_ROT;
    private static final float[] SPIKE_Z_ROT;
    private static final float[] SPIKE_X;
    private static final float[] SPIKE_Y;
    private static final float[] SPIKE_Z;
    private final ModelPart head;
    private final ModelPart eye;
    private final ModelPart[] spikeParts;
    private final ModelPart[] tailParts;
    
    public GuardianModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.spikeParts = new ModelPart[12];
        this.head = new ModelPart(this);
        this.head.texOffs(0, 0).addBox(-6.0f, 10.0f, -8.0f, 12.0f, 12.0f, 16.0f);
        this.head.texOffs(0, 28).addBox(-8.0f, 10.0f, -6.0f, 2.0f, 12.0f, 12.0f);
        this.head.texOffs(0, 28).addBox(6.0f, 10.0f, -6.0f, 2.0f, 12.0f, 12.0f, true);
        this.head.texOffs(16, 40).addBox(-6.0f, 8.0f, -6.0f, 12.0f, 2.0f, 12.0f);
        this.head.texOffs(16, 40).addBox(-6.0f, 22.0f, -6.0f, 12.0f, 2.0f, 12.0f);
        for (int integer2 = 0; integer2 < this.spikeParts.length; ++integer2) {
            (this.spikeParts[integer2] = new ModelPart(this, 0, 0)).addBox(-1.0f, -4.5f, -1.0f, 2.0f, 9.0f, 2.0f);
            this.head.addChild(this.spikeParts[integer2]);
        }
        (this.eye = new ModelPart(this, 8, 0)).addBox(-1.0f, 15.0f, 0.0f, 2.0f, 2.0f, 1.0f);
        this.head.addChild(this.eye);
        this.tailParts = new ModelPart[3];
        (this.tailParts[0] = new ModelPart(this, 40, 0)).addBox(-2.0f, 14.0f, 7.0f, 4.0f, 4.0f, 8.0f);
        (this.tailParts[1] = new ModelPart(this, 0, 54)).addBox(0.0f, 14.0f, 0.0f, 3.0f, 3.0f, 7.0f);
        this.tailParts[2] = new ModelPart(this);
        this.tailParts[2].texOffs(41, 32).addBox(0.0f, 14.0f, 0.0f, 2.0f, 2.0f, 6.0f);
        this.tailParts[2].texOffs(25, 19).addBox(1.0f, 10.5f, 3.0f, 1.0f, 9.0f, 9.0f);
        this.head.addChild(this.tailParts[0]);
        this.tailParts[0].addChild(this.tailParts[1]);
        this.tailParts[1].addChild(this.tailParts[2]);
        this.setupSpikes(0.0f, 0.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head);
    }
    
    @Override
    public void setupAnim(final Guardian bdj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = float4 - bdj.tickCount;
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
        final float float8 = (1.0f - bdj.getSpikesAnimation(float7)) * 0.55f;
        this.setupSpikes(float4, float8);
        this.eye.z = -8.25f;
        Entity apx10 = Minecraft.getInstance().getCameraEntity();
        if (bdj.hasActiveAttackTarget()) {
            apx10 = bdj.getActiveAttackTarget();
        }
        if (apx10 != null) {
            final Vec3 dck11 = apx10.getEyePosition(0.0f);
            final Vec3 dck12 = bdj.getEyePosition(0.0f);
            final double double13 = dck11.y - dck12.y;
            if (double13 > 0.0) {
                this.eye.y = 0.0f;
            }
            else {
                this.eye.y = 1.0f;
            }
            Vec3 dck13 = bdj.getViewVector(0.0f);
            dck13 = new Vec3(dck13.x, 0.0, dck13.z);
            final Vec3 dck14 = new Vec3(dck12.x - dck11.x, 0.0, dck12.z - dck11.z).normalize().yRot(1.5707964f);
            final double double14 = dck13.dot(dck14);
            this.eye.x = Mth.sqrt((float)Math.abs(double14)) * 2.0f * (float)Math.signum(double14);
        }
        this.eye.visible = true;
        final float float9 = bdj.getTailAnimation(float7);
        this.tailParts[0].yRot = Mth.sin(float9) * 3.1415927f * 0.05f;
        this.tailParts[1].yRot = Mth.sin(float9) * 3.1415927f * 0.1f;
        this.tailParts[1].x = -1.5f;
        this.tailParts[1].y = 0.5f;
        this.tailParts[1].z = 14.0f;
        this.tailParts[2].yRot = Mth.sin(float9) * 3.1415927f * 0.15f;
        this.tailParts[2].x = 0.5f;
        this.tailParts[2].y = 0.5f;
        this.tailParts[2].z = 6.0f;
    }
    
    private void setupSpikes(final float float1, final float float2) {
        for (int integer4 = 0; integer4 < 12; ++integer4) {
            this.spikeParts[integer4].xRot = 3.1415927f * GuardianModel.SPIKE_X_ROT[integer4];
            this.spikeParts[integer4].yRot = 3.1415927f * GuardianModel.SPIKE_Y_ROT[integer4];
            this.spikeParts[integer4].zRot = 3.1415927f * GuardianModel.SPIKE_Z_ROT[integer4];
            this.spikeParts[integer4].x = GuardianModel.SPIKE_X[integer4] * (1.0f + Mth.cos(float1 * 1.5f + integer4) * 0.01f - float2);
            this.spikeParts[integer4].y = 16.0f + GuardianModel.SPIKE_Y[integer4] * (1.0f + Mth.cos(float1 * 1.5f + integer4) * 0.01f - float2);
            this.spikeParts[integer4].z = GuardianModel.SPIKE_Z[integer4] * (1.0f + Mth.cos(float1 * 1.5f + integer4) * 0.01f - float2);
        }
    }
    
    static {
        SPIKE_X_ROT = new float[] { 1.75f, 0.25f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f, 0.75f, 0.0f, 0.0f };
        SPIKE_Y_ROT = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.25f, 1.75f, 1.25f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f };
        SPIKE_Z_ROT = new float[] { 0.0f, 0.0f, 0.25f, 1.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.75f, 1.25f };
        SPIKE_X = new float[] { 0.0f, 0.0f, 8.0f, -8.0f, -8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f, 8.0f, -8.0f };
        SPIKE_Y = new float[] { -8.0f, -8.0f, -8.0f, -8.0f, 0.0f, 0.0f, 0.0f, 0.0f, 8.0f, 8.0f, 8.0f, 8.0f };
        SPIKE_Z = new float[] { 8.0f, -8.0f, 0.0f, 0.0f, -8.0f, -8.0f, 8.0f, 8.0f, 8.0f, -8.0f, 0.0f, 0.0f };
    }
}
