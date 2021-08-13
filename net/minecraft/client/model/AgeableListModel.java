package net.minecraft.client.model;

import net.minecraft.client.model.geom.ModelPart;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public abstract class AgeableListModel<E extends Entity> extends EntityModel<E> {
    private final boolean scaleHead;
    private final float yHeadOffset;
    private final float zHeadOffset;
    private final float babyHeadScale;
    private final float babyBodyScale;
    private final float bodyYOffset;
    
    protected AgeableListModel(final boolean boolean1, final float float2, final float float3) {
        this(boolean1, float2, float3, 2.0f, 2.0f, 24.0f);
    }
    
    protected AgeableListModel(final boolean boolean1, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this(RenderType::entityCutoutNoCull, boolean1, float2, float3, float4, float5, float6);
    }
    
    protected AgeableListModel(final Function<ResourceLocation, RenderType> function, final boolean boolean2, final float float3, final float float4, final float float5, final float float6, final float float7) {
        super(function);
        this.scaleHead = boolean2;
        this.yHeadOffset = float3;
        this.zHeadOffset = float4;
        this.babyHeadScale = float5;
        this.babyBodyScale = float6;
        this.bodyYOffset = float7;
    }
    
    protected AgeableListModel() {
        this(false, 5.0f, 2.0f);
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        if (this.young) {
            dfj.pushPose();
            if (this.scaleHead) {
                final float float9 = 1.5f / this.babyHeadScale;
                dfj.scale(float9, float9, float9);
            }
            dfj.translate(0.0, this.yHeadOffset / 16.0f, this.zHeadOffset / 16.0f);
            this.headParts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
            dfj.popPose();
            dfj.pushPose();
            final float float9 = 1.0f / this.babyBodyScale;
            dfj.scale(float9, float9, float9);
            dfj.translate(0.0, this.bodyYOffset / 16.0f, 0.0);
            this.bodyParts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
            dfj.popPose();
        }
        else {
            this.headParts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
            this.bodyParts().forEach(dwf -> dwf.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8));
        }
    }
    
    protected abstract Iterable<ModelPart> headParts();
    
    protected abstract Iterable<ModelPart> bodyParts();
}
