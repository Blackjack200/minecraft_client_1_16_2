package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemStack;
import java.util.Random;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemEntityRenderer extends EntityRenderer<ItemEntity> {
    private final ItemRenderer itemRenderer;
    private final Random random;
    
    public ItemEntityRenderer(final EntityRenderDispatcher eel, final ItemRenderer efg) {
        super(eel);
        this.random = new Random();
        this.itemRenderer = efg;
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }
    
    private int getRenderAmount(final ItemStack bly) {
        int integer3 = 1;
        if (bly.getCount() > 48) {
            integer3 = 5;
        }
        else if (bly.getCount() > 32) {
            integer3 = 4;
        }
        else if (bly.getCount() > 16) {
            integer3 = 3;
        }
        else if (bly.getCount() > 1) {
            integer3 = 2;
        }
        return integer3;
    }
    
    @Override
    public void render(final ItemEntity bcs, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        final ItemStack bly8 = bcs.getItem();
        final int integer2 = bly8.isEmpty() ? 187 : (Item.getId(bly8.getItem()) + bly8.getDamageValue());
        this.random.setSeed((long)integer2);
        final BakedModel elg10 = this.itemRenderer.getModel(bly8, bcs.level, null);
        final boolean boolean11 = elg10.isGui3d();
        final int integer3 = this.getRenderAmount(bly8);
        final float float4 = 0.25f;
        final float float5 = Mth.sin((bcs.getAge() + float3) / 10.0f + bcs.bobOffs) * 0.1f + 0.1f;
        final float float6 = elg10.getTransforms().getTransform(ItemTransforms.TransformType.GROUND).scale.y();
        dfj.translate(0.0, float5 + 0.25f * float6, 0.0);
        final float float7 = bcs.getSpin(float3);
        dfj.mulPose(Vector3f.YP.rotation(float7));
        final float float8 = elg10.getTransforms().ground.scale.x();
        final float float9 = elg10.getTransforms().ground.scale.y();
        final float float10 = elg10.getTransforms().ground.scale.z();
        if (!boolean11) {
            final float float11 = -0.0f * (integer3 - 1) * 0.5f * float8;
            final float float12 = -0.0f * (integer3 - 1) * 0.5f * float9;
            final float float13 = -0.09375f * (integer3 - 1) * 0.5f * float10;
            dfj.translate(float11, float12, float13);
        }
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            dfj.pushPose();
            if (integer4 > 0) {
                if (boolean11) {
                    final float float12 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float float13 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float float14 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    dfj.translate(float12, float13, float14);
                }
                else {
                    final float float12 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    final float float13 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    dfj.translate(float12, float13, 0.0);
                }
            }
            this.itemRenderer.render(bly8, ItemTransforms.TransformType.GROUND, false, dfj, dzy, integer, OverlayTexture.NO_OVERLAY, elg10);
            dfj.popPose();
            if (!boolean11) {
                dfj.translate(0.0f * float8, 0.0f * float9, 0.09375f * float10);
            }
        }
        dfj.popPose();
        super.render(bcs, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ItemEntity bcs) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
