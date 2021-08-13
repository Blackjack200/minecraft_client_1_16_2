package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Ravager;

public class RavagerModel extends ListModel<Ravager> {
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart body;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart neck;
    
    public RavagerModel() {
        this.texWidth = 128;
        this.texHeight = 128;
        final int integer2 = 16;
        final float float3 = 0.0f;
        (this.neck = new ModelPart(this)).setPos(0.0f, -7.0f, -1.5f);
        this.neck.texOffs(68, 73).addBox(-5.0f, -1.0f, -18.0f, 10.0f, 10.0f, 18.0f, 0.0f);
        (this.head = new ModelPart(this)).setPos(0.0f, 16.0f, -17.0f);
        this.head.texOffs(0, 0).addBox(-8.0f, -20.0f, -14.0f, 16.0f, 20.0f, 16.0f, 0.0f);
        this.head.texOffs(0, 0).addBox(-2.0f, -6.0f, -18.0f, 4.0f, 8.0f, 4.0f, 0.0f);
        final ModelPart dwf4 = new ModelPart(this);
        dwf4.setPos(-10.0f, -14.0f, -8.0f);
        dwf4.texOffs(74, 55).addBox(0.0f, -14.0f, -2.0f, 2.0f, 14.0f, 4.0f, 0.0f);
        dwf4.xRot = 1.0995574f;
        this.head.addChild(dwf4);
        final ModelPart dwf5 = new ModelPart(this);
        dwf5.mirror = true;
        dwf5.setPos(8.0f, -14.0f, -8.0f);
        dwf5.texOffs(74, 55).addBox(0.0f, -14.0f, -2.0f, 2.0f, 14.0f, 4.0f, 0.0f);
        dwf5.xRot = 1.0995574f;
        this.head.addChild(dwf5);
        (this.mouth = new ModelPart(this)).setPos(0.0f, -2.0f, 2.0f);
        this.mouth.texOffs(0, 36).addBox(-8.0f, 0.0f, -16.0f, 16.0f, 3.0f, 16.0f, 0.0f);
        this.head.addChild(this.mouth);
        this.neck.addChild(this.head);
        this.body = new ModelPart(this);
        this.body.texOffs(0, 55).addBox(-7.0f, -10.0f, -7.0f, 14.0f, 16.0f, 20.0f, 0.0f);
        this.body.texOffs(0, 91).addBox(-6.0f, 6.0f, -7.0f, 12.0f, 13.0f, 18.0f, 0.0f);
        this.body.setPos(0.0f, 1.0f, 2.0f);
        (this.leg0 = new ModelPart(this, 96, 0)).addBox(-4.0f, 0.0f, -4.0f, 8.0f, 37.0f, 8.0f, 0.0f);
        this.leg0.setPos(-8.0f, -13.0f, 18.0f);
        this.leg1 = new ModelPart(this, 96, 0);
        this.leg1.mirror = true;
        this.leg1.addBox(-4.0f, 0.0f, -4.0f, 8.0f, 37.0f, 8.0f, 0.0f);
        this.leg1.setPos(8.0f, -13.0f, 18.0f);
        (this.leg2 = new ModelPart(this, 64, 0)).addBox(-4.0f, 0.0f, -4.0f, 8.0f, 37.0f, 8.0f, 0.0f);
        this.leg2.setPos(-8.0f, -13.0f, -5.0f);
        this.leg3 = new ModelPart(this, 64, 0);
        this.leg3.mirror = true;
        this.leg3.addBox(-4.0f, 0.0f, -4.0f, 8.0f, 37.0f, 8.0f, 0.0f);
        this.leg3.setPos(8.0f, -13.0f, -5.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.neck, this.body, this.leg0, this.leg1, this.leg2, this.leg3);
    }
    
    @Override
    public void setupAnim(final Ravager bds, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = float5 * 0.017453292f;
        this.body.xRot = 1.5707964f;
        final float float7 = 0.4f * float3;
        this.leg0.xRot = Mth.cos(float2 * 0.6662f) * float7;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * float7;
        this.leg2.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * float7;
        this.leg3.xRot = Mth.cos(float2 * 0.6662f) * float7;
    }
    
    @Override
    public void prepareMobModel(final Ravager bds, final float float2, final float float3, final float float4) {
        super.prepareMobModel(bds, float2, float3, float4);
        final int integer6 = bds.getStunnedTick();
        final int integer7 = bds.getRoarTick();
        final int integer8 = 20;
        final int integer9 = bds.getAttackTick();
        final int integer10 = 10;
        if (integer9 > 0) {
            final float float5 = Mth.triangleWave(integer9 - float4, 10.0f);
            final float float6 = (1.0f + float5) * 0.5f;
            final float float7 = float6 * float6 * float6 * 12.0f;
            final float float8 = float7 * Mth.sin(this.neck.xRot);
            this.neck.z = -6.5f + float7;
            this.neck.y = -7.0f - float8;
            final float float9 = Mth.sin((integer9 - float4) / 10.0f * 3.1415927f * 0.25f);
            this.mouth.xRot = 1.5707964f * float9;
            if (integer9 > 5) {
                this.mouth.xRot = Mth.sin((-4 + integer9 - float4) / 4.0f) * 3.1415927f * 0.4f;
            }
            else {
                this.mouth.xRot = 0.15707964f * Mth.sin(3.1415927f * (integer9 - float4) / 10.0f);
            }
        }
        else {
            final float float5 = -1.0f;
            final float float6 = -1.0f * Mth.sin(this.neck.xRot);
            this.neck.x = 0.0f;
            this.neck.y = -7.0f - float6;
            this.neck.z = 5.5f;
            final boolean boolean13 = integer6 > 0;
            this.neck.xRot = (boolean13 ? 0.21991149f : 0.0f);
            this.mouth.xRot = 3.1415927f * (boolean13 ? 0.05f : 0.01f);
            if (boolean13) {
                final double double14 = integer6 / 40.0;
                this.neck.x = (float)Math.sin(double14 * 10.0) * 3.0f;
            }
            else if (integer7 > 0) {
                final float float8 = Mth.sin((20 - integer7 - float4) / 20.0f * 3.1415927f * 0.25f);
                this.mouth.xRot = 1.5707964f * float8;
            }
        }
    }
}
