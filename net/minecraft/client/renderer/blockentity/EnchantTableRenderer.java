package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;

public class EnchantTableRenderer extends BlockEntityRenderer<EnchantmentTableBlockEntity> {
    public static final Material BOOK_LOCATION;
    private final BookModel bookModel;
    
    public EnchantTableRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.bookModel = new BookModel();
    }
    
    @Override
    public void render(final EnchantmentTableBlockEntity ccr, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        dfj.pushPose();
        dfj.translate(0.5, 0.75, 0.5);
        final float float3 = ccr.time + float2;
        dfj.translate(0.0, 0.1f + Mth.sin(float3 * 0.1f) * 0.01f, 0.0);
        float float4;
        for (float4 = ccr.rot - ccr.oRot; float4 >= 3.1415927f; float4 -= 6.2831855f) {}
        while (float4 < -3.1415927f) {
            float4 += 6.2831855f;
        }
        final float float5 = ccr.oRot + float4 * float2;
        dfj.mulPose(Vector3f.YP.rotation(-float5));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(80.0f));
        final float float6 = Mth.lerp(float2, ccr.oFlip, ccr.flip);
        final float float7 = Mth.frac(float6 + 0.25f) * 1.6f - 0.3f;
        final float float8 = Mth.frac(float6 + 0.75f) * 1.6f - 0.3f;
        final float float9 = Mth.lerp(float2, ccr.oOpen, ccr.open);
        this.bookModel.setupAnim(float3, Mth.clamp(float7, 0.0f, 1.0f), Mth.clamp(float8, 0.0f, 1.0f), float9);
        final VertexConsumer dfn15 = EnchantTableRenderer.BOOK_LOCATION.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.bookModel.render(dfj, dfn15, integer5, integer6, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.popPose();
    }
    
    static {
        BOOK_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/enchanting_table_book"));
    }
}
