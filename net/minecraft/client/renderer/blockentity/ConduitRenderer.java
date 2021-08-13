package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;

public class ConduitRenderer extends BlockEntityRenderer<ConduitBlockEntity> {
    public static final Material SHELL_TEXTURE;
    public static final Material ACTIVE_SHELL_TEXTURE;
    public static final Material WIND_TEXTURE;
    public static final Material VERTICAL_WIND_TEXTURE;
    public static final Material OPEN_EYE_TEXTURE;
    public static final Material CLOSED_EYE_TEXTURE;
    private final ModelPart eye;
    private final ModelPart wind;
    private final ModelPart shell;
    private final ModelPart cage;
    
    public ConduitRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
        (this.eye = new ModelPart(16, 16, 0, 0)).addBox(-4.0f, -4.0f, 0.0f, 8.0f, 8.0f, 0.0f, 0.01f);
        (this.wind = new ModelPart(64, 32, 0, 0)).addBox(-8.0f, -8.0f, -8.0f, 16.0f, 16.0f, 16.0f);
        (this.shell = new ModelPart(32, 16, 0, 0)).addBox(-3.0f, -3.0f, -3.0f, 6.0f, 6.0f, 6.0f);
        (this.cage = new ModelPart(32, 16, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f);
    }
    
    @Override
    public void render(final ConduitBlockEntity ccn, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final float float3 = ccn.tickCount + float2;
        if (!ccn.isActive()) {
            final float float4 = ccn.getActiveRotation(0.0f);
            final VertexConsumer dfn10 = ConduitRenderer.SHELL_TEXTURE.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entitySolid);
            dfj.pushPose();
            dfj.translate(0.5, 0.5, 0.5);
            dfj.mulPose(Vector3f.YP.rotationDegrees(float4));
            this.shell.render(dfj, dfn10, integer5, integer6);
            dfj.popPose();
            return;
        }
        final float float4 = ccn.getActiveRotation(float2) * 57.295776f;
        float float5 = Mth.sin(float3 * 0.1f) / 2.0f + 0.5f;
        float5 += float5 * float5;
        dfj.pushPose();
        dfj.translate(0.5, 0.3f + float5 * 0.2f, 0.5);
        final Vector3f g11 = new Vector3f(0.5f, 1.0f, 0.5f);
        g11.normalize();
        dfj.mulPose(new Quaternion(g11, float4, true));
        this.cage.render(dfj, ConduitRenderer.ACTIVE_SHELL_TEXTURE.buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityCutoutNoCull), integer5, integer6);
        dfj.popPose();
        final int integer7 = ccn.tickCount / 66 % 3;
        dfj.pushPose();
        dfj.translate(0.5, 0.5, 0.5);
        if (integer7 == 1) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        }
        else if (integer7 == 2) {
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
        final VertexConsumer dfn11 = ((integer7 == 1) ? ConduitRenderer.VERTICAL_WIND_TEXTURE : ConduitRenderer.WIND_TEXTURE).buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityCutoutNoCull);
        this.wind.render(dfj, dfn11, integer5, integer6);
        dfj.popPose();
        dfj.pushPose();
        dfj.translate(0.5, 0.5, 0.5);
        dfj.scale(0.875f, 0.875f, 0.875f);
        dfj.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        this.wind.render(dfj, dfn11, integer5, integer6);
        dfj.popPose();
        final Camera djh14 = this.renderer.camera;
        dfj.pushPose();
        dfj.translate(0.5, 0.3f + float5 * 0.2f, 0.5);
        dfj.scale(0.5f, 0.5f, 0.5f);
        final float float6 = -djh14.getYRot();
        dfj.mulPose(Vector3f.YP.rotationDegrees(float6));
        dfj.mulPose(Vector3f.XP.rotationDegrees(djh14.getXRot()));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        final float float7 = 1.3333334f;
        dfj.scale(1.3333334f, 1.3333334f, 1.3333334f);
        this.eye.render(dfj, (ccn.isHunting() ? ConduitRenderer.OPEN_EYE_TEXTURE : ConduitRenderer.CLOSED_EYE_TEXTURE).buffer(dzy, (Function<ResourceLocation, RenderType>)RenderType::entityCutoutNoCull), integer5, integer6);
        dfj.popPose();
    }
    
    static {
        SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/base"));
        ACTIVE_SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/cage"));
        WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/wind"));
        VERTICAL_WIND_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/wind_vertical"));
        OPEN_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/open_eye"));
        CLOSED_EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/conduit/closed_eye"));
    }
}
