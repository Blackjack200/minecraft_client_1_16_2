package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;

public abstract class ColorableListModel<E extends Entity> extends ListModel<E> {
    private float r;
    private float g;
    private float b;
    
    public ColorableListModel() {
        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
    }
    
    public void setColor(final float float1, final float float2, final float float3) {
        this.r = float1;
        this.g = float2;
        this.b = float3;
    }
    
    @Override
    public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
        super.renderToBuffer(dfj, dfn, integer3, integer4, this.r * float5, this.g * float6, this.b * float7, float8);
    }
}
