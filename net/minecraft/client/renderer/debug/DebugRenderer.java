package net.minecraft.client.renderer.debug;

import net.minecraft.client.gui.Font;
import com.mojang.math.Transformation;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class DebugRenderer {
    public final PathfindingRenderer pathfindingRenderer;
    public final SimpleDebugRenderer waterDebugRenderer;
    public final SimpleDebugRenderer chunkBorderRenderer;
    public final SimpleDebugRenderer heightMapRenderer;
    public final SimpleDebugRenderer collisionBoxRenderer;
    public final SimpleDebugRenderer neighborsUpdateRenderer;
    public final CaveDebugRenderer caveRenderer;
    public final StructureRenderer structureRenderer;
    public final SimpleDebugRenderer lightDebugRenderer;
    public final SimpleDebugRenderer worldGenAttemptRenderer;
    public final SimpleDebugRenderer solidFaceRenderer;
    public final SimpleDebugRenderer chunkRenderer;
    public final BrainDebugRenderer brainDebugRenderer;
    public final VillageSectionsDebugRenderer villageSectionsDebugRenderer;
    public final BeeDebugRenderer beeDebugRenderer;
    public final RaidDebugRenderer raidDebugRenderer;
    public final GoalSelectorDebugRenderer goalSelectorRenderer;
    public final GameTestDebugRenderer gameTestDebugRenderer;
    private boolean renderChunkborder;
    
    public DebugRenderer(final Minecraft djw) {
        this.pathfindingRenderer = new PathfindingRenderer();
        this.waterDebugRenderer = new WaterDebugRenderer(djw);
        this.chunkBorderRenderer = new ChunkBorderRenderer(djw);
        this.heightMapRenderer = new HeightMapRenderer(djw);
        this.collisionBoxRenderer = new CollisionBoxRenderer(djw);
        this.neighborsUpdateRenderer = new NeighborsUpdateRenderer(djw);
        this.caveRenderer = new CaveDebugRenderer();
        this.structureRenderer = new StructureRenderer(djw);
        this.lightDebugRenderer = new LightDebugRenderer(djw);
        this.worldGenAttemptRenderer = new WorldGenAttemptRenderer();
        this.solidFaceRenderer = new SolidFaceRenderer(djw);
        this.chunkRenderer = new ChunkDebugRenderer(djw);
        this.brainDebugRenderer = new BrainDebugRenderer(djw);
        this.villageSectionsDebugRenderer = new VillageSectionsDebugRenderer();
        this.beeDebugRenderer = new BeeDebugRenderer(djw);
        this.raidDebugRenderer = new RaidDebugRenderer(djw);
        this.goalSelectorRenderer = new GoalSelectorDebugRenderer(djw);
        this.gameTestDebugRenderer = new GameTestDebugRenderer();
    }
    
    public void clear() {
        this.pathfindingRenderer.clear();
        this.waterDebugRenderer.clear();
        this.chunkBorderRenderer.clear();
        this.heightMapRenderer.clear();
        this.collisionBoxRenderer.clear();
        this.neighborsUpdateRenderer.clear();
        this.caveRenderer.clear();
        this.structureRenderer.clear();
        this.lightDebugRenderer.clear();
        this.worldGenAttemptRenderer.clear();
        this.solidFaceRenderer.clear();
        this.chunkRenderer.clear();
        this.brainDebugRenderer.clear();
        this.villageSectionsDebugRenderer.clear();
        this.beeDebugRenderer.clear();
        this.raidDebugRenderer.clear();
        this.goalSelectorRenderer.clear();
        this.gameTestDebugRenderer.clear();
    }
    
    public boolean switchRenderChunkborder() {
        return this.renderChunkborder = !this.renderChunkborder;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource.BufferSource a, final double double3, final double double4, final double double5) {
        if (this.renderChunkborder && !Minecraft.getInstance().showOnlyReducedInfo()) {
            this.chunkBorderRenderer.render(dfj, a, double3, double4, double5);
        }
        this.gameTestDebugRenderer.render(dfj, a, double3, double4, double5);
    }
    
    public static Optional<Entity> getTargetedEntity(@Nullable final Entity apx, final int integer) {
        if (apx == null) {
            return (Optional<Entity>)Optional.empty();
        }
        final Vec3 dck3 = apx.getEyePosition(1.0f);
        final Vec3 dck4 = apx.getViewVector(1.0f).scale(integer);
        final Vec3 dck5 = dck3.add(dck4);
        final AABB dcf6 = apx.getBoundingBox().expandTowards(dck4).inflate(1.0);
        final int integer2 = integer * integer;
        final Predicate<Entity> predicate8 = (Predicate<Entity>)(apx -> !apx.isSpectator() && apx.isPickable());
        final EntityHitResult dch9 = ProjectileUtil.getEntityHitResult(apx, dck3, dck5, dcf6, predicate8, integer2);
        if (dch9 == null) {
            return (Optional<Entity>)Optional.empty();
        }
        if (dck3.distanceToSqr(dch9.getLocation()) > integer2) {
            return (Optional<Entity>)Optional.empty();
        }
        return (Optional<Entity>)Optional.of(dch9.getEntity());
    }
    
    public static void renderFilledBox(final BlockPos fx1, final BlockPos fx2, final float float3, final float float4, final float float5, final float float6) {
        final Camera djh7 = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (!djh7.isInitialized()) {
            return;
        }
        final Vec3 dck8 = djh7.getPosition().reverse();
        final AABB dcf9 = new AABB(fx1, fx2).move(dck8);
        renderFilledBox(dcf9, float3, float4, float5, float6);
    }
    
    public static void renderFilledBox(final BlockPos fx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final Camera djh7 = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (!djh7.isInitialized()) {
            return;
        }
        final Vec3 dck8 = djh7.getPosition().reverse();
        final AABB dcf9 = new AABB(fx).move(dck8).inflate(float2);
        renderFilledBox(dcf9, float3, float4, float5, float6);
    }
    
    public static void renderFilledBox(final AABB dcf, final float float2, final float float3, final float float4, final float float5) {
        renderFilledBox(dcf.minX, dcf.minY, dcf.minZ, dcf.maxX, dcf.maxY, dcf.maxZ, float2, float3, float4, float5);
    }
    
    public static void renderFilledBox(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6, final float float7, final float float8, final float float9, final float float10) {
        final Tesselator dfl17 = Tesselator.getInstance();
        final BufferBuilder dfe18 = dfl17.getBuilder();
        dfe18.begin(5, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.addChainedFilledBoxVertices(dfe18, double1, double2, double3, double4, double5, double6, float7, float8, float9, float10);
        dfl17.end();
    }
    
    public static void renderFloatingText(final String string, final int integer2, final int integer3, final int integer4, final int integer5) {
        renderFloatingText(string, integer2 + 0.5, integer3 + 0.5, integer4 + 0.5, integer5);
    }
    
    public static void renderFloatingText(final String string, final double double2, final double double3, final double double4, final int integer) {
        renderFloatingText(string, double2, double3, double4, integer, 0.02f);
    }
    
    public static void renderFloatingText(final String string, final double double2, final double double3, final double double4, final int integer, final float float6) {
        renderFloatingText(string, double2, double3, double4, integer, float6, true, 0.0f, false);
    }
    
    public static void renderFloatingText(final String string, final double double2, final double double3, final double double4, final int integer, final float float6, final boolean boolean7, final float float8, final boolean boolean9) {
        final Minecraft djw13 = Minecraft.getInstance();
        final Camera djh14 = djw13.gameRenderer.getMainCamera();
        if (!djh14.isInitialized() || djw13.getEntityRenderDispatcher().options == null) {
            return;
        }
        final Font dkr15 = djw13.font;
        final double double5 = djh14.getPosition().x;
        final double double6 = djh14.getPosition().y;
        final double double7 = djh14.getPosition().z;
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)(double2 - double5), (float)(double3 - double6) + 0.07f, (float)(double4 - double7));
        RenderSystem.normal3f(0.0f, 1.0f, 0.0f);
        RenderSystem.multMatrix(new Matrix4f(djh14.rotation()));
        RenderSystem.scalef(float6, -float6, float6);
        RenderSystem.enableTexture();
        if (boolean9) {
            RenderSystem.disableDepthTest();
        }
        else {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask(true);
        RenderSystem.scalef(-1.0f, 1.0f, 1.0f);
        float float9 = boolean7 ? (-dkr15.width(string) / 2.0f) : 0.0f;
        float9 -= float8 / float6;
        RenderSystem.enableAlphaTest();
        final MultiBufferSource.BufferSource a23 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        dkr15.drawInBatch(string, float9, 0.0f, integer, false, Transformation.identity().getMatrix(), a23, boolean9, 0, 15728880);
        a23.endBatch();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableDepthTest();
        RenderSystem.popMatrix();
    }
    
    public interface SimpleDebugRenderer {
        void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5);
        
        default void clear() {
        }
    }
}
