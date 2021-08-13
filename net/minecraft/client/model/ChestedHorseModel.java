package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;

public class ChestedHorseModel<T extends AbstractChestedHorse> extends HorseModel<T> {
    private final ModelPart boxL;
    private final ModelPart boxR;
    
    public ChestedHorseModel(final float float1) {
        super(float1);
        (this.boxL = new ModelPart(this, 26, 21)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 8.0f, 3.0f);
        (this.boxR = new ModelPart(this, 26, 21)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 8.0f, 3.0f);
        this.boxL.yRot = -1.5707964f;
        this.boxR.yRot = 1.5707964f;
        this.boxL.setPos(6.0f, -8.0f, 0.0f);
        this.boxR.setPos(-6.0f, -8.0f, 0.0f);
        this.body.addChild(this.boxL);
        this.body.addChild(this.boxR);
    }
    
    @Override
    protected void addEarModels(final ModelPart dwf) {
        final ModelPart dwf2 = new ModelPart(this, 0, 12);
        dwf2.addBox(-1.0f, -7.0f, 0.0f, 2.0f, 7.0f, 1.0f);
        dwf2.setPos(1.25f, -10.0f, 4.0f);
        final ModelPart dwf3 = new ModelPart(this, 0, 12);
        dwf3.addBox(-1.0f, -7.0f, 0.0f, 2.0f, 7.0f, 1.0f);
        dwf3.setPos(-1.25f, -10.0f, 4.0f);
        dwf2.xRot = 0.2617994f;
        dwf2.zRot = 0.2617994f;
        dwf3.xRot = 0.2617994f;
        dwf3.zRot = -0.2617994f;
        dwf.addChild(dwf2);
        dwf.addChild(dwf3);
    }
    
    @Override
    public void setupAnim(final T bax, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bax, float2, float3, float4, float5, float6);
        if (bax.hasChest()) {
            this.boxL.visible = true;
            this.boxR.visible = true;
        }
        else {
            this.boxL.visible = false;
            this.boxR.visible = false;
        }
    }
}
