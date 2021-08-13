package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class BannerRenderer extends BlockEntityRenderer<BannerBlockEntity> {
    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;
    
    public BannerRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        this.flag = makeFlag();
        (this.pole = new ModelPart(64, 64, 44, 0)).addBox(-1.0f, -30.0f, -1.0f, 2.0f, 42.0f, 2.0f, 0.0f);
        (this.bar = new ModelPart(64, 64, 0, 42)).addBox(-10.0f, -32.0f, -1.0f, 20.0f, 2.0f, 2.0f, 0.0f);
    }
    
    public static ModelPart makeFlag() {
        final ModelPart dwf1 = new ModelPart(64, 64, 0, 0);
        dwf1.addBox(-10.0f, 0.0f, -2.0f, 20.0f, 40.0f, 1.0f, 0.0f);
        return dwf1;
    }
    
    @Override
    public void render(final BannerBlockEntity cbx, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final List<Pair<BannerPattern, DyeColor>> list8 = cbx.getPatterns();
        if (list8 == null) {
            return;
        }
        final float float3 = 0.6666667f;
        final boolean boolean10 = cbx.getLevel() == null;
        dfj.pushPose();
        long long11;
        if (boolean10) {
            long11 = 0L;
            dfj.translate(0.5, 0.5, 0.5);
            this.pole.visible = true;
        }
        else {
            long11 = cbx.getLevel().getGameTime();
            final BlockState cee13 = cbx.getBlockState();
            if (cee13.getBlock() instanceof BannerBlock) {
                dfj.translate(0.5, 0.5, 0.5);
                final float float4 = -cee13.<Integer>getValue((Property<Integer>)BannerBlock.ROTATION) * 360 / 16.0f;
                dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
                this.pole.visible = true;
            }
            else {
                dfj.translate(0.5, -0.1666666716337204, 0.5);
                final float float4 = -cee13.<Direction>getValue((Property<Direction>)WallBannerBlock.FACING).toYRot();
                dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
                dfj.translate(0.0, -0.3125, -0.4375);
                this.pole.visible = false;
            }
        }
        dfj.pushPose();
        dfj.scale(0.6666667f, -0.6666667f, -0.6666667f);
        final VertexConsumer dfn13 = ModelBakery.BANNER_BASE.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
        this.pole.render(dfj, dfn13, integer5, integer6);
        this.bar.render(dfj, dfn13, integer5, integer6);
        final BlockPos fx14 = cbx.getBlockPos();
        final float float5 = (Math.floorMod(fx14.getX() * 7 + fx14.getY() * 9 + fx14.getZ() * 13 + long11, 100L) + float2) / 100.0f;
        this.flag.xRot = (-0.0125f + 0.01f * Mth.cos(6.2831855f * float5)) * 3.1415927f;
        this.flag.y = -32.0f;
        renderPatterns(dfj, dzy, integer5, integer6, this.flag, ModelBakery.BANNER_BASE, true, list8);
        dfj.popPose();
        dfj.popPose();
    }
    
    public static void renderPatterns(final PoseStack dfj, final MultiBufferSource dzy, final int integer3, final int integer4, final ModelPart dwf, final Material elj, final boolean boolean7, final List<Pair<BannerPattern, DyeColor>> list) {
        renderPatterns(dfj, dzy, integer3, integer4, dwf, elj, boolean7, list, false);
    }
    
    public static void renderPatterns(final PoseStack dfj, final MultiBufferSource dzy, final int integer3, final int integer4, final ModelPart dwf, final Material elj, final boolean boolean7, final List<Pair<BannerPattern, DyeColor>> list, final boolean boolean9) {
        dwf.render(dfj, elj.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid, boolean9), integer3, integer4);
        for (int integer5 = 0; integer5 < 17 && integer5 < list.size(); ++integer5) {
            final Pair<BannerPattern, DyeColor> pair11 = (Pair<BannerPattern, DyeColor>)list.get(integer5);
            final float[] arr12 = ((DyeColor)pair11.getSecond()).getTextureDiffuseColors();
            final Material elj2 = new Material(boolean7 ? Sheets.BANNER_SHEET : Sheets.SHIELD_SHEET, ((BannerPattern)pair11.getFirst()).location(boolean7));
            dwf.render(dfj, elj2.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityNoOutline), integer3, integer4, arr12[0], arr12[1], arr12[2], 1.0f);
        }
    }
}
