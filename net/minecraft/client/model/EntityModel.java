package net.minecraft.client.model;

import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public abstract class EntityModel<T extends Entity> extends Model {
    public float attackTime;
    public boolean riding;
    public boolean young;
    
    protected EntityModel() {
        this(RenderType::entityCutoutNoCull);
    }
    
    protected EntityModel(final Function<ResourceLocation, RenderType> function) {
        super(function);
        this.young = true;
    }
    
    public abstract void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6);
    
    public void prepareMobModel(final T apx, final float float2, final float float3, final float float4) {
    }
    
    public void copyPropertiesTo(final EntityModel<T> dtu) {
        dtu.attackTime = this.attackTime;
        dtu.riding = this.riding;
        dtu.young = this.young;
    }
}
