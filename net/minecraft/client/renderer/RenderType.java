package net.minecraft.client.renderer;

import it.unimi.dsi.fastutil.Hash;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.Objects;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.entity.ItemRenderer;
import com.google.common.collect.ImmutableList;
import java.util.List;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;
import com.mojang.blaze3d.vertex.VertexFormat;

public abstract class RenderType extends RenderStateShard {
    private static final RenderType SOLID;
    private static final RenderType CUTOUT_MIPPED;
    private static final RenderType CUTOUT;
    private static final RenderType TRANSLUCENT;
    private static final RenderType TRANSLUCENT_MOVING_BLOCK;
    private static final RenderType TRANSLUCENT_NO_CRUMBLING;
    private static final RenderType LEASH;
    private static final RenderType WATER_MASK;
    private static final RenderType ARMOR_GLINT;
    private static final RenderType ARMOR_ENTITY_GLINT;
    private static final RenderType GLINT_TRANSLUCENT;
    private static final RenderType GLINT;
    private static final RenderType GLINT_DIRECT;
    private static final RenderType ENTITY_GLINT;
    private static final RenderType ENTITY_GLINT_DIRECT;
    private static final RenderType LIGHTNING;
    private static final RenderType TRIPWIRE;
    public static final CompositeRenderType LINES;
    private final VertexFormat format;
    private final int mode;
    private final int bufferSize;
    private final boolean affectsCrumbling;
    private final boolean sortOnUpload;
    private final Optional<RenderType> asOptional;
    
    public static RenderType solid() {
        return RenderType.SOLID;
    }
    
    public static RenderType cutoutMipped() {
        return RenderType.CUTOUT_MIPPED;
    }
    
    public static RenderType cutout() {
        return RenderType.CUTOUT;
    }
    
    private static CompositeState translucentState() {
        return CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET_MIPPED).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.TRANSLUCENT_TARGET).createCompositeState(true);
    }
    
    public static RenderType translucent() {
        return RenderType.TRANSLUCENT;
    }
    
    private static CompositeState translucentMovingBlockState() {
        return CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET_MIPPED).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.ITEM_ENTITY_TARGET).createCompositeState(true);
    }
    
    public static RenderType translucentMovingBlock() {
        return RenderType.TRANSLUCENT_MOVING_BLOCK;
    }
    
    public static RenderType translucentNoCrumbling() {
        return RenderType.TRANSLUCENT_NO_CRUMBLING;
    }
    
    public static RenderType armorCutoutNoCull(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.NO_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true);
        return create("armor_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, b2);
    }
    
    public static RenderType entitySolid(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.NO_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(true);
        return create("entity_solid", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, b2);
    }
    
    public static RenderType entityCutout(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.NO_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(true);
        return create("entity_cutout", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, b2);
    }
    
    public static RenderType entityCutoutNoCull(final ResourceLocation vk, final boolean boolean2) {
        final CompositeState b3 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.NO_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(boolean2);
        return create("entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, b3);
    }
    
    public static RenderType entityCutoutNoCull(final ResourceLocation vk) {
        return entityCutoutNoCull(vk, true);
    }
    
    public static RenderType entityCutoutNoCullZOffset(final ResourceLocation vk, final boolean boolean2) {
        final CompositeState b3 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.NO_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(boolean2);
        return create("entity_cutout_no_cull_z_offset", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, false, b3);
    }
    
    public static RenderType entityCutoutNoCullZOffset(final ResourceLocation vk) {
        return entityCutoutNoCullZOffset(vk, true);
    }
    
    public static RenderType itemEntityTranslucentCull(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.ITEM_ENTITY_TARGET).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).createCompositeState(true);
        return create("item_entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, true, b2);
    }
    
    public static RenderType entityTranslucentCull(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(true);
        return create("entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, true, b2);
    }
    
    public static RenderType entityTranslucent(final ResourceLocation vk, final boolean boolean2) {
        final CompositeState b3 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(boolean2);
        return create("entity_translucent", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, true, b3);
    }
    
    public static RenderType entityTranslucent(final ResourceLocation vk) {
        return entityTranslucent(vk, true);
    }
    
    public static RenderType entitySmoothCutout(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setAlphaState(RenderType.MIDWAY_ALPHA).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setShadeModelState(RenderType.SMOOTH_SHADE).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).createCompositeState(true);
        return create("entity_smooth_cutout", DefaultVertexFormat.NEW_ENTITY, 7, 256, b2);
    }
    
    public static RenderType beaconBeam(final ResourceLocation vk, final boolean boolean2) {
        final CompositeState b3 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(boolean2 ? RenderType.TRANSLUCENT_TRANSPARENCY : RenderType.NO_TRANSPARENCY).setWriteMaskState(boolean2 ? RenderType.COLOR_WRITE : RenderType.COLOR_DEPTH_WRITE).setFogState(RenderType.NO_FOG).createCompositeState(false);
        return create("beacon_beam", DefaultVertexFormat.BLOCK, 7, 256, false, true, b3);
    }
    
    public static RenderType entityDecal(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(false);
        return create("entity_decal", DefaultVertexFormat.NEW_ENTITY, 7, 256, b2);
    }
    
    public static RenderType entityNoOutline(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false);
        return create("entity_no_outline", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, true, b2);
    }
    
    public static RenderType entityShadow(final ResourceLocation vk) {
        final CompositeState b2 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setWriteMaskState(RenderType.COLOR_WRITE).setDepthTestState(RenderType.LEQUAL_DEPTH_TEST).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(false);
        return create("entity_shadow", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, false, b2);
    }
    
    public static RenderType dragonExplosionAlpha(final ResourceLocation vk, final float float2) {
        final CompositeState b3 = CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setAlphaState(new AlphaStateShard(float2)).setCullState(RenderType.NO_CULL).createCompositeState(true);
        return create("entity_alpha", DefaultVertexFormat.NEW_ENTITY, 7, 256, b3);
    }
    
    public static RenderType eyes(final ResourceLocation vk) {
        final TextureStateShard o2 = new TextureStateShard(vk, false, false);
        return create("eyes", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, true, CompositeState.builder().setTextureState(o2).setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_WRITE).setFogState(RenderType.BLACK_FOG).createCompositeState(false));
    }
    
    public static RenderType energySwirl(final ResourceLocation vk, final float float2, final float float3) {
        return create("energy_swirl", DefaultVertexFormat.NEW_ENTITY, 7, 256, false, true, CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setTexturingState(new OffsetTexturingStateShard(float2, float3)).setFogState(RenderType.BLACK_FOG).setTransparencyState(RenderType.ADDITIVE_TRANSPARENCY).setDiffuseLightingState(RenderType.DIFFUSE_LIGHTING).setAlphaState(RenderType.DEFAULT_ALPHA).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).createCompositeState(false));
    }
    
    public static RenderType leash() {
        return RenderType.LEASH;
    }
    
    public static RenderType waterMask() {
        return RenderType.WATER_MASK;
    }
    
    public static RenderType outline(final ResourceLocation vk) {
        return outline(vk, RenderType.NO_CULL);
    }
    
    public static RenderType outline(final ResourceLocation vk, final CullStateShard c) {
        return create("outline", DefaultVertexFormat.POSITION_COLOR_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setCullState(c).setDepthTestState(RenderType.NO_DEPTH_TEST).setAlphaState(RenderType.DEFAULT_ALPHA).setTexturingState(RenderType.OUTLINE_TEXTURING).setFogState(RenderType.NO_FOG).setOutputState(RenderType.OUTLINE_TARGET).createCompositeState(OutlineProperty.IS_OUTLINE));
    }
    
    public static RenderType armorGlint() {
        return RenderType.ARMOR_GLINT;
    }
    
    public static RenderType armorEntityGlint() {
        return RenderType.ARMOR_ENTITY_GLINT;
    }
    
    public static RenderType glintTranslucent() {
        return RenderType.GLINT_TRANSLUCENT;
    }
    
    public static RenderType glint() {
        return RenderType.GLINT;
    }
    
    public static RenderType glintDirect() {
        return RenderType.GLINT_DIRECT;
    }
    
    public static RenderType entityGlint() {
        return RenderType.ENTITY_GLINT;
    }
    
    public static RenderType entityGlintDirect() {
        return RenderType.ENTITY_GLINT_DIRECT;
    }
    
    public static RenderType crumbling(final ResourceLocation vk) {
        final TextureStateShard o2 = new TextureStateShard(vk, false, false);
        return create("crumbling", DefaultVertexFormat.BLOCK, 7, 256, false, true, CompositeState.builder().setTextureState(o2).setAlphaState(RenderType.DEFAULT_ALPHA).setTransparencyState(RenderType.CRUMBLING_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_WRITE).setLayeringState(RenderType.POLYGON_OFFSET_LAYERING).createCompositeState(false));
    }
    
    public static RenderType text(final ResourceLocation vk) {
        return create("text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setAlphaState(RenderType.DEFAULT_ALPHA).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).createCompositeState(false));
    }
    
    public static RenderType textSeeThrough(final ResourceLocation vk) {
        return create("text_see_through", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, 7, 256, false, true, CompositeState.builder().setTextureState(new TextureStateShard(vk, false, false)).setAlphaState(RenderType.DEFAULT_ALPHA).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setDepthTestState(RenderType.NO_DEPTH_TEST).setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false));
    }
    
    public static RenderType lightning() {
        return RenderType.LIGHTNING;
    }
    
    private static CompositeState tripwireState() {
        return CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET_MIPPED).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.WEATHER_TARGET).createCompositeState(true);
    }
    
    public static RenderType tripwire() {
        return RenderType.TRIPWIRE;
    }
    
    public static RenderType endPortal(final int integer) {
        TransparencyStateShard q2;
        TextureStateShard o3;
        if (integer <= 1) {
            q2 = RenderType.TRANSLUCENT_TRANSPARENCY;
            o3 = new TextureStateShard(TheEndPortalRenderer.END_SKY_LOCATION, false, false);
        }
        else {
            q2 = RenderType.ADDITIVE_TRANSPARENCY;
            o3 = new TextureStateShard(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false);
        }
        return create("end_portal", DefaultVertexFormat.POSITION_COLOR, 7, 256, false, true, CompositeState.builder().setTransparencyState(q2).setTextureState(o3).setTexturingState(new PortalTexturingStateShard(integer)).setFogState(RenderType.BLACK_FOG).createCompositeState(false));
    }
    
    public static RenderType lines() {
        return RenderType.LINES;
    }
    
    public RenderType(final String string, final VertexFormat dfo, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6, final Runnable runnable7, final Runnable runnable8) {
        super(string, runnable7, runnable8);
        this.format = dfo;
        this.mode = integer3;
        this.bufferSize = integer4;
        this.affectsCrumbling = boolean5;
        this.sortOnUpload = boolean6;
        this.asOptional = (Optional<RenderType>)Optional.of(this);
    }
    
    public static CompositeRenderType create(final String string, final VertexFormat dfo, final int integer3, final int integer4, final CompositeState b) {
        return create(string, dfo, integer3, integer4, false, false, b);
    }
    
    public static CompositeRenderType create(final String string, final VertexFormat dfo, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6, final CompositeState b) {
        return memoize(string, dfo, integer3, integer4, boolean5, boolean6, b);
    }
    
    public void end(final BufferBuilder dfe, final int integer2, final int integer3, final int integer4) {
        if (!dfe.building()) {
            return;
        }
        if (this.sortOnUpload) {
            dfe.sortQuads((float)integer2, (float)integer3, (float)integer4);
        }
        dfe.end();
        this.setupRenderState();
        BufferUploader.end(dfe);
        this.clearRenderState();
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public static List<RenderType> chunkBufferLayers() {
        return (List<RenderType>)ImmutableList.of(solid(), cutoutMipped(), cutout(), translucent(), tripwire());
    }
    
    public int bufferSize() {
        return this.bufferSize;
    }
    
    public VertexFormat format() {
        return this.format;
    }
    
    public int mode() {
        return this.mode;
    }
    
    public Optional<RenderType> outline() {
        return (Optional<RenderType>)Optional.empty();
    }
    
    public boolean isOutline() {
        return false;
    }
    
    public boolean affectsCrumbling() {
        return this.affectsCrumbling;
    }
    
    public Optional<RenderType> asOptional() {
        return this.asOptional;
    }
    
    static {
        SOLID = create("solid", DefaultVertexFormat.BLOCK, 7, 2097152, true, false, CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET_MIPPED).createCompositeState(true));
        CUTOUT_MIPPED = create("cutout_mipped", DefaultVertexFormat.BLOCK, 7, 131072, true, false, CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET_MIPPED).setAlphaState(RenderType.MIDWAY_ALPHA).createCompositeState(true));
        CUTOUT = create("cutout", DefaultVertexFormat.BLOCK, 7, 131072, true, false, CompositeState.builder().setShadeModelState(RenderType.SMOOTH_SHADE).setLightmapState(RenderType.LIGHTMAP).setTextureState(RenderType.BLOCK_SHEET).setAlphaState(RenderType.MIDWAY_ALPHA).createCompositeState(true));
        TRANSLUCENT = create("translucent", DefaultVertexFormat.BLOCK, 7, 262144, true, true, translucentState());
        TRANSLUCENT_MOVING_BLOCK = create("translucent_moving_block", DefaultVertexFormat.BLOCK, 7, 262144, false, true, translucentMovingBlockState());
        TRANSLUCENT_NO_CRUMBLING = create("translucent_no_crumbling", DefaultVertexFormat.BLOCK, 7, 262144, false, true, translucentState());
        LEASH = create("leash", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, 7, 256, CompositeState.builder().setTextureState(RenderType.NO_TEXTURE).setCullState(RenderType.NO_CULL).setLightmapState(RenderType.LIGHTMAP).createCompositeState(false));
        WATER_MASK = create("water_mask", DefaultVertexFormat.POSITION, 7, 256, CompositeState.builder().setTextureState(RenderType.NO_TEXTURE).setWriteMaskState(RenderType.DEPTH_WRITE).createCompositeState(false));
        ARMOR_GLINT = create("armor_glint", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.GLINT_TEXTURING).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(false));
        ARMOR_ENTITY_GLINT = create("armor_entity_glint", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.ENTITY_GLINT_TEXTURING).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(false));
        GLINT_TRANSLUCENT = create("glint_translucent", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.GLINT_TEXTURING).setOutputState(RenderType.ITEM_ENTITY_TARGET).createCompositeState(false));
        GLINT = create("glint", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.GLINT_TEXTURING).createCompositeState(false));
        GLINT_DIRECT = create("glint_direct", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.GLINT_TEXTURING).createCompositeState(false));
        ENTITY_GLINT = create("entity_glint", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setOutputState(RenderType.ITEM_ENTITY_TARGET).setTexturingState(RenderType.ENTITY_GLINT_TEXTURING).createCompositeState(false));
        ENTITY_GLINT_DIRECT = create("entity_glint_direct", DefaultVertexFormat.POSITION_TEX, 7, 256, CompositeState.builder().setTextureState(new TextureStateShard(ItemRenderer.ENCHANT_GLINT_LOCATION, true, false)).setWriteMaskState(RenderType.COLOR_WRITE).setCullState(RenderType.NO_CULL).setDepthTestState(RenderType.EQUAL_DEPTH_TEST).setTransparencyState(RenderType.GLINT_TRANSPARENCY).setTexturingState(RenderType.ENTITY_GLINT_TEXTURING).createCompositeState(false));
        LIGHTNING = create("lightning", DefaultVertexFormat.POSITION_COLOR, 7, 256, false, true, CompositeState.builder().setWriteMaskState(RenderType.COLOR_DEPTH_WRITE).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setOutputState(RenderType.WEATHER_TARGET).setShadeModelState(RenderType.SMOOTH_SHADE).createCompositeState(false));
        TRIPWIRE = create("tripwire", DefaultVertexFormat.BLOCK, 7, 262144, true, true, tripwireState());
        LINES = create("lines", DefaultVertexFormat.POSITION_COLOR, 1, 256, CompositeState.builder().setLineState(new LineStateShard(OptionalDouble.empty())).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.ITEM_ENTITY_TARGET).setWriteMaskState(RenderType.COLOR_DEPTH_WRITE).createCompositeState(false));
    }
    
    enum OutlineProperty {
        NONE("none"), 
        IS_OUTLINE("is_outline"), 
        AFFECTS_OUTLINE("affects_outline");
        
        private final String name;
        
        private OutlineProperty(final String string3) {
            this.name = string3;
        }
        
        public String toString() {
            return this.name;
        }
    }
    
    public static final class CompositeState {
        private final TextureStateShard textureState;
        private final TransparencyStateShard transparencyState;
        private final DiffuseLightingStateShard diffuseLightingState;
        private final ShadeModelStateShard shadeModelState;
        private final AlphaStateShard alphaState;
        private final DepthTestStateShard depthTestState;
        private final CullStateShard cullState;
        private final LightmapStateShard lightmapState;
        private final OverlayStateShard overlayState;
        private final FogStateShard fogState;
        private final LayeringStateShard layeringState;
        private final OutputStateShard outputState;
        private final TexturingStateShard texturingState;
        private final WriteMaskStateShard writeMaskState;
        private final LineStateShard lineState;
        private final OutlineProperty outlineProperty;
        private final ImmutableList<RenderStateShard> states;
        
        private CompositeState(final TextureStateShard o, final TransparencyStateShard q, final DiffuseLightingStateShard e, final ShadeModelStateShard n, final AlphaStateShard a, final DepthTestStateShard d, final CullStateShard c, final LightmapStateShard h, final OverlayStateShard l, final FogStateShard f, final LayeringStateShard g, final OutputStateShard k, final TexturingStateShard p, final WriteMaskStateShard r, final LineStateShard i, final OutlineProperty c) {
            this.textureState = o;
            this.transparencyState = q;
            this.diffuseLightingState = e;
            this.shadeModelState = n;
            this.alphaState = a;
            this.depthTestState = d;
            this.cullState = c;
            this.lightmapState = h;
            this.overlayState = l;
            this.fogState = f;
            this.layeringState = g;
            this.outputState = k;
            this.texturingState = p;
            this.writeMaskState = r;
            this.lineState = i;
            this.outlineProperty = c;
            this.states = (ImmutableList<RenderStateShard>)ImmutableList.of(this.textureState, this.transparencyState, this.diffuseLightingState, this.shadeModelState, this.alphaState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.fogState, this.layeringState, this.outputState, (Object[])new RenderStateShard[] { this.texturingState, this.writeMaskState, this.lineState });
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final CompositeState b3 = (CompositeState)object;
            return this.outlineProperty == b3.outlineProperty && this.states.equals(b3.states);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.states, this.outlineProperty });
        }
        
        public String toString() {
            return new StringBuilder().append("CompositeState[").append(this.states).append(", outlineProperty=").append(this.outlineProperty).append(']').toString();
        }
        
        public static CompositeStateBuilder builder() {
            return new CompositeStateBuilder();
        }
        
        public static class CompositeStateBuilder {
            private TextureStateShard textureState;
            private TransparencyStateShard transparencyState;
            private DiffuseLightingStateShard diffuseLightingState;
            private ShadeModelStateShard shadeModelState;
            private AlphaStateShard alphaState;
            private DepthTestStateShard depthTestState;
            private CullStateShard cullState;
            private LightmapStateShard lightmapState;
            private OverlayStateShard overlayState;
            private FogStateShard fogState;
            private LayeringStateShard layeringState;
            private OutputStateShard outputState;
            private TexturingStateShard texturingState;
            private WriteMaskStateShard writeMaskState;
            private LineStateShard lineState;
            
            private CompositeStateBuilder() {
                this.textureState = RenderStateShard.NO_TEXTURE;
                this.transparencyState = RenderStateShard.NO_TRANSPARENCY;
                this.diffuseLightingState = RenderStateShard.NO_DIFFUSE_LIGHTING;
                this.shadeModelState = RenderStateShard.FLAT_SHADE;
                this.alphaState = RenderStateShard.NO_ALPHA;
                this.depthTestState = RenderStateShard.LEQUAL_DEPTH_TEST;
                this.cullState = RenderStateShard.CULL;
                this.lightmapState = RenderStateShard.NO_LIGHTMAP;
                this.overlayState = RenderStateShard.NO_OVERLAY;
                this.fogState = RenderStateShard.FOG;
                this.layeringState = RenderStateShard.NO_LAYERING;
                this.outputState = RenderStateShard.MAIN_TARGET;
                this.texturingState = RenderStateShard.DEFAULT_TEXTURING;
                this.writeMaskState = RenderStateShard.COLOR_DEPTH_WRITE;
                this.lineState = RenderStateShard.DEFAULT_LINE;
            }
            
            public CompositeStateBuilder setTextureState(final TextureStateShard o) {
                this.textureState = o;
                return this;
            }
            
            public CompositeStateBuilder setTransparencyState(final TransparencyStateShard q) {
                this.transparencyState = q;
                return this;
            }
            
            public CompositeStateBuilder setDiffuseLightingState(final DiffuseLightingStateShard e) {
                this.diffuseLightingState = e;
                return this;
            }
            
            public CompositeStateBuilder setShadeModelState(final ShadeModelStateShard n) {
                this.shadeModelState = n;
                return this;
            }
            
            public CompositeStateBuilder setAlphaState(final AlphaStateShard a) {
                this.alphaState = a;
                return this;
            }
            
            public CompositeStateBuilder setDepthTestState(final DepthTestStateShard d) {
                this.depthTestState = d;
                return this;
            }
            
            public CompositeStateBuilder setCullState(final CullStateShard c) {
                this.cullState = c;
                return this;
            }
            
            public CompositeStateBuilder setLightmapState(final LightmapStateShard h) {
                this.lightmapState = h;
                return this;
            }
            
            public CompositeStateBuilder setOverlayState(final OverlayStateShard l) {
                this.overlayState = l;
                return this;
            }
            
            public CompositeStateBuilder setFogState(final FogStateShard f) {
                this.fogState = f;
                return this;
            }
            
            public CompositeStateBuilder setLayeringState(final LayeringStateShard g) {
                this.layeringState = g;
                return this;
            }
            
            public CompositeStateBuilder setOutputState(final OutputStateShard k) {
                this.outputState = k;
                return this;
            }
            
            public CompositeStateBuilder setTexturingState(final TexturingStateShard p) {
                this.texturingState = p;
                return this;
            }
            
            public CompositeStateBuilder setWriteMaskState(final WriteMaskStateShard r) {
                this.writeMaskState = r;
                return this;
            }
            
            public CompositeStateBuilder setLineState(final LineStateShard i) {
                this.lineState = i;
                return this;
            }
            
            public CompositeState createCompositeState(final boolean boolean1) {
                return this.createCompositeState(boolean1 ? OutlineProperty.AFFECTS_OUTLINE : OutlineProperty.NONE);
            }
            
            public CompositeState createCompositeState(final OutlineProperty c) {
                return new CompositeState(this.textureState, this.transparencyState, this.diffuseLightingState, this.shadeModelState, this.alphaState, this.depthTestState, this.cullState, this.lightmapState, this.overlayState, this.fogState, this.layeringState, this.outputState, this.texturingState, this.writeMaskState, this.lineState, c);
            }
        }
    }
    
    static final class CompositeRenderType extends RenderType {
        private static final ObjectOpenCustomHashSet<CompositeRenderType> INSTANCES;
        private final CompositeState state;
        private final int hashCode;
        private final Optional<RenderType> outline;
        private final boolean isOutline;
        
        private CompositeRenderType(final String string, final VertexFormat dfo, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6, final CompositeState b) {
            super(string, dfo, integer3, integer4, boolean5, boolean6, () -> b.states.forEach(RenderStateShard::setupRenderState), () -> b.states.forEach(RenderStateShard::clearRenderState));
            this.state = b;
            this.outline = (Optional<RenderType>)((b.outlineProperty == OutlineProperty.AFFECTS_OUTLINE) ? b.textureState.texture().map(vk -> RenderType.outline(vk, b.cullState)) : Optional.empty());
            this.isOutline = (b.outlineProperty == OutlineProperty.IS_OUTLINE);
            this.hashCode = Objects.hash(new Object[] { super.hashCode(), b });
        }
        
        private static CompositeRenderType memoize(final String string, final VertexFormat dfo, final int integer3, final int integer4, final boolean boolean5, final boolean boolean6, final CompositeState b) {
            return (CompositeRenderType)CompositeRenderType.INSTANCES.addOrGet(new CompositeRenderType(string, dfo, integer3, integer4, boolean5, boolean6, b));
        }
        
        @Override
        public Optional<RenderType> outline() {
            return this.outline;
        }
        
        @Override
        public boolean isOutline() {
            return this.isOutline;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this == object;
        }
        
        @Override
        public int hashCode() {
            return this.hashCode;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append("RenderType[").append(this.state).append(']').toString();
        }
        
        static {
            INSTANCES = new ObjectOpenCustomHashSet((Hash.Strategy)EqualsStrategy.INSTANCE);
        }
        
        enum EqualsStrategy implements Hash.Strategy<CompositeRenderType> {
            INSTANCE;
            
            public int hashCode(@Nullable final CompositeRenderType a) {
                if (a == null) {
                    return 0;
                }
                return a.hashCode;
            }
            
            public boolean equals(@Nullable final CompositeRenderType a1, @Nullable final CompositeRenderType a2) {
                return a1 == a2 || (a1 != null && a2 != null && Objects.equals(a1.state, a2.state));
            }
        }
    }
}
