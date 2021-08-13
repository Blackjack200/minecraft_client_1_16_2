package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableList;
import java.util.stream.IntStream;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.core.Position;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;

public class TheEndPortalRenderer<T extends TheEndPortalBlockEntity> extends BlockEntityRenderer<T> {
    public static final ResourceLocation END_SKY_LOCATION;
    public static final ResourceLocation END_PORTAL_LOCATION;
    private static final Random RANDOM;
    private static final List<RenderType> RENDER_TYPES;
    
    public TheEndPortalRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final T cdi, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        TheEndPortalRenderer.RANDOM.setSeed(31100L);
        final double double8 = cdi.getBlockPos().distSqr(this.renderer.camera.getPosition(), true);
        final int integer7 = this.getPasses(double8);
        final float float3 = this.getOffset();
        final Matrix4f b12 = dfj.last().pose();
        this.renderCube(cdi, float3, 0.15f, b12, dzy.getBuffer((RenderType)TheEndPortalRenderer.RENDER_TYPES.get(0)));
        for (int integer8 = 1; integer8 < integer7; ++integer8) {
            this.renderCube(cdi, float3, 2.0f / (18 - integer8), b12, dzy.getBuffer((RenderType)TheEndPortalRenderer.RENDER_TYPES.get(integer8)));
        }
    }
    
    private void renderCube(final T cdi, final float float2, final float float3, final Matrix4f b, final VertexConsumer dfn) {
        final float float4 = (TheEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.1f) * float3;
        final float float5 = (TheEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.4f) * float3;
        final float float6 = (TheEndPortalRenderer.RANDOM.nextFloat() * 0.5f + 0.5f) * float3;
        this.renderFace(cdi, b, dfn, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, float4, float5, float6, Direction.SOUTH);
        this.renderFace(cdi, b, dfn, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, float4, float5, float6, Direction.NORTH);
        this.renderFace(cdi, b, dfn, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, float4, float5, float6, Direction.EAST);
        this.renderFace(cdi, b, dfn, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, float4, float5, float6, Direction.WEST);
        this.renderFace(cdi, b, dfn, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, float4, float5, float6, Direction.DOWN);
        this.renderFace(cdi, b, dfn, 0.0f, 1.0f, float2, float2, 1.0f, 1.0f, 0.0f, 0.0f, float4, float5, float6, Direction.UP);
    }
    
    private void renderFace(final T cdi, final Matrix4f b, final VertexConsumer dfn, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10, final float float11, final float float12, final float float13, final float float14, final Direction gc) {
        if (cdi.shouldRenderFace(gc)) {
            dfn.vertex(b, float4, float6, float8).color(float12, float13, float14, 1.0f).endVertex();
            dfn.vertex(b, float5, float6, float9).color(float12, float13, float14, 1.0f).endVertex();
            dfn.vertex(b, float5, float7, float10).color(float12, float13, float14, 1.0f).endVertex();
            dfn.vertex(b, float4, float7, float11).color(float12, float13, float14, 1.0f).endVertex();
        }
    }
    
    protected int getPasses(final double double1) {
        if (double1 > 36864.0) {
            return 1;
        }
        if (double1 > 25600.0) {
            return 3;
        }
        if (double1 > 16384.0) {
            return 5;
        }
        if (double1 > 9216.0) {
            return 7;
        }
        if (double1 > 4096.0) {
            return 9;
        }
        if (double1 > 1024.0) {
            return 11;
        }
        if (double1 > 576.0) {
            return 13;
        }
        if (double1 > 256.0) {
            return 14;
        }
        return 15;
    }
    
    protected float getOffset() {
        return 0.75f;
    }
    
    static {
        END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");
        END_PORTAL_LOCATION = new ResourceLocation("textures/entity/end_portal.png");
        RANDOM = new Random(31100L);
        RENDER_TYPES = (List)IntStream.range(0, 16).mapToObj(integer -> RenderType.endPortal(integer + 1)).collect(ImmutableList.toImmutableList());
    }
}
