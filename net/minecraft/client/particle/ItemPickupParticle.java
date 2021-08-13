package net.minecraft.client.particle;

import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.RenderBuffers;

public class ItemPickupParticle extends Particle {
    private final RenderBuffers renderBuffers;
    private final Entity itemEntity;
    private final Entity target;
    private int life;
    private final EntityRenderDispatcher entityRenderDispatcher;
    
    public ItemPickupParticle(final EntityRenderDispatcher eel, final RenderBuffers eae, final ClientLevel dwl, final Entity apx4, final Entity apx5) {
        this(eel, eae, dwl, apx4, apx5, apx4.getDeltaMovement());
    }
    
    private ItemPickupParticle(final EntityRenderDispatcher eel, final RenderBuffers eae, final ClientLevel dwl, final Entity apx4, final Entity apx5, final Vec3 dck) {
        super(dwl, apx4.getX(), apx4.getY(), apx4.getZ(), dck.x, dck.y, dck.z);
        this.renderBuffers = eae;
        this.itemEntity = this.getSafeCopy(apx4);
        this.target = apx5;
        this.entityRenderDispatcher = eel;
    }
    
    private Entity getSafeCopy(final Entity apx) {
        if (!(apx instanceof ItemEntity)) {
            return apx;
        }
        return ((ItemEntity)apx).copy();
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
    
    @Override
    public void render(final VertexConsumer dfn, final Camera djh, final float float3) {
        float float4 = (this.life + float3) / 3.0f;
        float4 *= float4;
        final double double6 = Mth.lerp(float3, this.target.xOld, this.target.getX());
        final double double7 = Mth.lerp(float3, this.target.yOld, this.target.getY()) + 0.5;
        final double double8 = Mth.lerp(float3, this.target.zOld, this.target.getZ());
        final double double9 = Mth.lerp(float4, this.itemEntity.getX(), double6);
        final double double10 = Mth.lerp(float4, this.itemEntity.getY(), double7);
        final double double11 = Mth.lerp(float4, this.itemEntity.getZ(), double8);
        final MultiBufferSource.BufferSource a18 = this.renderBuffers.bufferSource();
        final Vec3 dck19 = djh.getPosition();
        this.entityRenderDispatcher.<Entity>render(this.itemEntity, double9 - dck19.x(), double10 - dck19.y(), double11 - dck19.z(), this.itemEntity.yRot, float3, new PoseStack(), a18, this.entityRenderDispatcher.<Entity>getPackedLightCoords(this.itemEntity, float3));
        a18.endBatch();
    }
    
    @Override
    public void tick() {
        ++this.life;
        if (this.life == 3) {
            this.remove();
        }
    }
}
