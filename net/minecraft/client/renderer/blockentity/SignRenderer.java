package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.Model;
import java.util.List;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class SignRenderer extends BlockEntityRenderer<SignBlockEntity> {
    private final SignModel signModel;
    
    public SignRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.signModel = new SignModel();
    }
    
    @Override
    public void render(final SignBlockEntity cdc, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final BlockState cee8 = cdc.getBlockState();
        dfj.pushPose();
        final float float3 = 0.6666667f;
        if (cee8.getBlock() instanceof StandingSignBlock) {
            dfj.translate(0.5, 0.5, 0.5);
            final float float4 = -(cee8.<Integer>getValue((Property<Integer>)StandingSignBlock.ROTATION) * 360 / 16.0f);
            dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
            this.signModel.stick.visible = true;
        }
        else {
            dfj.translate(0.5, 0.5, 0.5);
            final float float4 = -cee8.<Direction>getValue((Property<Direction>)WallSignBlock.FACING).toYRot();
            dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
            dfj.translate(0.0, -0.3125, -0.4375);
            this.signModel.stick.visible = false;
        }
        dfj.pushPose();
        dfj.scale(0.6666667f, -0.6666667f, -0.6666667f);
        final Material elj10 = getMaterial(cee8.getBlock());
        final VertexConsumer dfn11 = elj10.buffer(dzy, (Function<ResourceLocation, RenderType>)this.signModel::renderType);
        this.signModel.sign.render(dfj, dfn11, integer5, integer6);
        this.signModel.stick.render(dfj, dfn11, integer5, integer6);
        dfj.popPose();
        final Font dkr12 = this.renderer.getFont();
        final float float5 = 0.010416667f;
        dfj.translate(0.0, 0.3333333432674408, 0.046666666865348816);
        dfj.scale(0.010416667f, -0.010416667f, 0.010416667f);
        final int integer7 = cdc.getColor().getTextColor();
        final double double15 = 0.4;
        final int integer8 = (int)(NativeImage.getR(integer7) * 0.4);
        final int integer9 = (int)(NativeImage.getG(integer7) * 0.4);
        final int integer10 = (int)(NativeImage.getB(integer7) * 0.4);
        final int integer11 = NativeImage.combine(0, integer10, integer9, integer8);
        final int integer12 = 20;
        for (int integer13 = 0; integer13 < 4; ++integer13) {
            final FormattedCharSequence aex23 = cdc.getRenderMessage(integer13, (Function<Component, FormattedCharSequence>)(nr -> {
                final List<FormattedCharSequence> list3 = dkr12.split(nr, 90);
                return list3.isEmpty() ? FormattedCharSequence.EMPTY : list3.get(0);
            }));
            if (aex23 != null) {
                final float float6 = (float)(-dkr12.width(aex23) / 2);
                dkr12.drawInBatch(aex23, float6, (float)(integer13 * 10 - 20), integer11, false, dfj.last().pose(), dzy, false, 0, integer5);
            }
        }
        dfj.popPose();
    }
    
    public static Material getMaterial(final Block bul) {
        WoodType cfn2;
        if (bul instanceof SignBlock) {
            cfn2 = ((SignBlock)bul).type();
        }
        else {
            cfn2 = WoodType.OAK;
        }
        return Sheets.signTexture(cfn2);
    }
    
    public static final class SignModel extends Model {
        public final ModelPart sign;
        public final ModelPart stick;
        
        public SignModel() {
            super((Function<ResourceLocation, RenderType>)RenderType::entityCutoutNoCull);
            (this.sign = new ModelPart(64, 32, 0, 0)).addBox(-12.0f, -14.0f, -1.0f, 24.0f, 12.0f, 2.0f, 0.0f);
            (this.stick = new ModelPart(64, 32, 0, 14)).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 14.0f, 2.0f, 0.0f);
        }
        
        @Override
        public void renderToBuffer(final PoseStack dfj, final VertexConsumer dfn, final int integer3, final int integer4, final float float5, final float float6, final float float7, final float float8) {
            this.sign.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
            this.stick.render(dfj, dfn, integer3, integer4, float5, float6, float7, float8);
        }
    }
}
