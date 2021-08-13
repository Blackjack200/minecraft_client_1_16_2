package net.minecraft.client.renderer;

import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;
import java.util.Objects;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.Util;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.Nullable;

public abstract class RenderStateShard {
    protected final String name;
    private final Runnable setupState;
    private final Runnable clearState;
    protected static final TransparencyStateShard NO_TRANSPARENCY;
    protected static final TransparencyStateShard ADDITIVE_TRANSPARENCY;
    protected static final TransparencyStateShard LIGHTNING_TRANSPARENCY;
    protected static final TransparencyStateShard GLINT_TRANSPARENCY;
    protected static final TransparencyStateShard CRUMBLING_TRANSPARENCY;
    protected static final TransparencyStateShard TRANSLUCENT_TRANSPARENCY;
    protected static final AlphaStateShard NO_ALPHA;
    protected static final AlphaStateShard DEFAULT_ALPHA;
    protected static final AlphaStateShard MIDWAY_ALPHA;
    protected static final ShadeModelStateShard FLAT_SHADE;
    protected static final ShadeModelStateShard SMOOTH_SHADE;
    protected static final TextureStateShard BLOCK_SHEET_MIPPED;
    protected static final TextureStateShard BLOCK_SHEET;
    protected static final TextureStateShard NO_TEXTURE;
    protected static final TexturingStateShard DEFAULT_TEXTURING;
    protected static final TexturingStateShard OUTLINE_TEXTURING;
    protected static final TexturingStateShard GLINT_TEXTURING;
    protected static final TexturingStateShard ENTITY_GLINT_TEXTURING;
    protected static final LightmapStateShard LIGHTMAP;
    protected static final LightmapStateShard NO_LIGHTMAP;
    protected static final OverlayStateShard OVERLAY;
    protected static final OverlayStateShard NO_OVERLAY;
    protected static final DiffuseLightingStateShard DIFFUSE_LIGHTING;
    protected static final DiffuseLightingStateShard NO_DIFFUSE_LIGHTING;
    protected static final CullStateShard CULL;
    protected static final CullStateShard NO_CULL;
    protected static final DepthTestStateShard NO_DEPTH_TEST;
    protected static final DepthTestStateShard EQUAL_DEPTH_TEST;
    protected static final DepthTestStateShard LEQUAL_DEPTH_TEST;
    protected static final WriteMaskStateShard COLOR_DEPTH_WRITE;
    protected static final WriteMaskStateShard COLOR_WRITE;
    protected static final WriteMaskStateShard DEPTH_WRITE;
    protected static final LayeringStateShard NO_LAYERING;
    protected static final LayeringStateShard POLYGON_OFFSET_LAYERING;
    protected static final LayeringStateShard VIEW_OFFSET_Z_LAYERING;
    protected static final FogStateShard NO_FOG;
    protected static final FogStateShard FOG;
    protected static final FogStateShard BLACK_FOG;
    protected static final OutputStateShard MAIN_TARGET;
    protected static final OutputStateShard OUTLINE_TARGET;
    protected static final OutputStateShard TRANSLUCENT_TARGET;
    protected static final OutputStateShard PARTICLES_TARGET;
    protected static final OutputStateShard WEATHER_TARGET;
    protected static final OutputStateShard CLOUDS_TARGET;
    protected static final OutputStateShard ITEM_ENTITY_TARGET;
    protected static final LineStateShard DEFAULT_LINE;
    
    public RenderStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
        this.name = string;
        this.setupState = runnable2;
        this.clearState = runnable3;
    }
    
    public void setupRenderState() {
        this.setupState.run();
    }
    
    public void clearRenderState() {
        this.clearState.run();
    }
    
    public boolean equals(@Nullable final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final RenderStateShard eaf3 = (RenderStateShard)object;
        return this.name.equals(eaf3.name);
    }
    
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public String toString() {
        return this.name;
    }
    
    private static void setupGlintTexturing(final float float1) {
        RenderSystem.matrixMode(5890);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        final long long2 = Util.getMillis() * 8L;
        final float float2 = long2 % 110000L / 110000.0f;
        final float float3 = long2 % 30000L / 30000.0f;
        RenderSystem.translatef(-float2, float3, 0.0f);
        RenderSystem.rotatef(10.0f, 0.0f, 0.0f, 1.0f);
        RenderSystem.scalef(float1, float1, float1);
        RenderSystem.matrixMode(5888);
    }
    
    static {
        NO_TRANSPARENCY = new TransparencyStateShard("no_transparency", () -> RenderSystem.disableBlend(), () -> {});
        ADDITIVE_TRANSPARENCY = new TransparencyStateShard("additive_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        LIGHTNING_TRANSPARENCY = new TransparencyStateShard("lightning_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        GLINT_TRANSPARENCY = new TransparencyStateShard("glint_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        CRUMBLING_TRANSPARENCY = new TransparencyStateShard("crumbling_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        TRANSLUCENT_TRANSPARENCY = new TransparencyStateShard("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        NO_ALPHA = new AlphaStateShard(0.0f);
        DEFAULT_ALPHA = new AlphaStateShard(0.003921569f);
        MIDWAY_ALPHA = new AlphaStateShard(0.5f);
        FLAT_SHADE = new ShadeModelStateShard(false);
        SMOOTH_SHADE = new ShadeModelStateShard(true);
        BLOCK_SHEET_MIPPED = new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true);
        BLOCK_SHEET = new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false);
        NO_TEXTURE = new TextureStateShard();
        DEFAULT_TEXTURING = new TexturingStateShard("default_texturing", () -> {}, () -> {});
        OUTLINE_TEXTURING = new TexturingStateShard("outline_texturing", () -> RenderSystem.setupOutline(), () -> RenderSystem.teardownOutline());
        GLINT_TEXTURING = new TexturingStateShard("glint_texturing", () -> setupGlintTexturing(8.0f), () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
        });
        ENTITY_GLINT_TEXTURING = new TexturingStateShard("entity_glint_texturing", () -> setupGlintTexturing(0.16f), () -> {
            RenderSystem.matrixMode(5890);
            RenderSystem.popMatrix();
            RenderSystem.matrixMode(5888);
        });
        LIGHTMAP = new LightmapStateShard(true);
        NO_LIGHTMAP = new LightmapStateShard(false);
        OVERLAY = new OverlayStateShard(true);
        NO_OVERLAY = new OverlayStateShard(false);
        DIFFUSE_LIGHTING = new DiffuseLightingStateShard(true);
        NO_DIFFUSE_LIGHTING = new DiffuseLightingStateShard(false);
        CULL = new CullStateShard(true);
        NO_CULL = new CullStateShard(false);
        NO_DEPTH_TEST = new DepthTestStateShard("always", 519);
        EQUAL_DEPTH_TEST = new DepthTestStateShard("==", 514);
        LEQUAL_DEPTH_TEST = new DepthTestStateShard("<=", 515);
        COLOR_DEPTH_WRITE = new WriteMaskStateShard(true, true);
        COLOR_WRITE = new WriteMaskStateShard(true, false);
        DEPTH_WRITE = new WriteMaskStateShard(false, true);
        NO_LAYERING = new LayeringStateShard("no_layering", () -> {}, () -> {});
        POLYGON_OFFSET_LAYERING = new LayeringStateShard("polygon_offset_layering", () -> {
            RenderSystem.polygonOffset(-1.0f, -10.0f);
            RenderSystem.enablePolygonOffset();
        }, () -> {
            RenderSystem.polygonOffset(0.0f, 0.0f);
            RenderSystem.disablePolygonOffset();
        });
        VIEW_OFFSET_Z_LAYERING = new LayeringStateShard("view_offset_z_layering", () -> {
            RenderSystem.pushMatrix();
            RenderSystem.scalef(0.99975586f, 0.99975586f, 0.99975586f);
        }, RenderSystem::popMatrix);
        NO_FOG = new FogStateShard("no_fog", () -> {}, () -> {});
        FOG = new FogStateShard("fog", () -> {
            FogRenderer.levelFogColor();
            RenderSystem.enableFog();
        }, () -> RenderSystem.disableFog());
        BLACK_FOG = new FogStateShard("black_fog", () -> {
            RenderSystem.fog(2918, 0.0f, 0.0f, 0.0f, 1.0f);
            RenderSystem.enableFog();
        }, () -> {
            FogRenderer.levelFogColor();
            RenderSystem.disableFog();
        });
        MAIN_TARGET = new OutputStateShard("main_target", () -> {}, () -> {});
        OUTLINE_TARGET = new OutputStateShard("outline_target", () -> Minecraft.getInstance().levelRenderer.entityTarget().bindWrite(false), () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));
        TRANSLUCENT_TARGET = new OutputStateShard("translucent_target", () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().levelRenderer.getTranslucentTarget().bindWrite(false);
            }
        }, () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            }
        });
        PARTICLES_TARGET = new OutputStateShard("particles_target", () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().levelRenderer.getParticlesTarget().bindWrite(false);
            }
        }, () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            }
        });
        WEATHER_TARGET = new OutputStateShard("weather_target", () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().levelRenderer.getWeatherTarget().bindWrite(false);
            }
        }, () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            }
        });
        CLOUDS_TARGET = new OutputStateShard("clouds_target", () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().levelRenderer.getCloudsTarget().bindWrite(false);
            }
        }, () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            }
        });
        ITEM_ENTITY_TARGET = new OutputStateShard("item_entity_target", () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().levelRenderer.getItemEntityTarget().bindWrite(false);
            }
        }, () -> {
            if (Minecraft.useShaderTransparency()) {
                Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            }
        });
        DEFAULT_LINE = new LineStateShard(OptionalDouble.of(1.0));
    }
    
    public static class TransparencyStateShard extends RenderStateShard {
        public TransparencyStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
            super(string, runnable2, runnable3);
        }
    }
    
    public static class AlphaStateShard extends RenderStateShard {
        private final float cutoff;
        
        public AlphaStateShard(final float float1) {
            super("alpha", () -> {
                if (float1 > 0.0f) {
                    RenderSystem.enableAlphaTest();
                    RenderSystem.alphaFunc(516, float1);
                }
                else {
                    RenderSystem.disableAlphaTest();
                }
            }, () -> {
                RenderSystem.disableAlphaTest();
                RenderSystem.defaultAlphaFunc();
            });
            this.cutoff = float1;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this == object || (object != null && this.getClass() == object.getClass() && super.equals(object) && this.cutoff == ((AlphaStateShard)object).cutoff);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { super.hashCode(), this.cutoff });
        }
        
        @Override
        public String toString() {
            return this.name + '[' + this.cutoff + ']';
        }
    }
    
    public static class ShadeModelStateShard extends RenderStateShard {
        private final boolean smooth;
        
        public ShadeModelStateShard(final boolean boolean1) {
            super("shade_model", () -> RenderSystem.shadeModel(boolean1 ? 7425 : 7424), () -> RenderSystem.shadeModel(7424));
            this.smooth = boolean1;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final ShadeModelStateShard n3 = (ShadeModelStateShard)object;
            return this.smooth == n3.smooth;
        }
        
        @Override
        public int hashCode() {
            return Boolean.hashCode(this.smooth);
        }
        
        @Override
        public String toString() {
            return this.name + '[' + (this.smooth ? "smooth" : "flat") + ']';
        }
    }
    
    public static class TextureStateShard extends RenderStateShard {
        private final Optional<ResourceLocation> texture;
        private final boolean blur;
        private final boolean mipmap;
        
        public TextureStateShard(final ResourceLocation vk, final boolean boolean2, final boolean boolean3) {
            super("texture", () -> {
                RenderSystem.enableTexture();
                final TextureManager ejv4 = Minecraft.getInstance().getTextureManager();
                ejv4.bind(vk);
                ejv4.getTexture(vk).setFilter(boolean2, boolean3);
            }, () -> {});
            this.texture = (Optional<ResourceLocation>)Optional.of(vk);
            this.blur = boolean2;
            this.mipmap = boolean3;
        }
        
        public TextureStateShard() {
            super("texture", () -> RenderSystem.disableTexture(), () -> RenderSystem.enableTexture());
            this.texture = (Optional<ResourceLocation>)Optional.empty();
            this.blur = false;
            this.mipmap = false;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final TextureStateShard o3 = (TextureStateShard)object;
            return this.texture.equals(o3.texture) && this.blur == o3.blur && this.mipmap == o3.mipmap;
        }
        
        @Override
        public int hashCode() {
            return this.texture.hashCode();
        }
        
        @Override
        public String toString() {
            return this.name + '[' + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
        }
        
        protected Optional<ResourceLocation> texture() {
            return this.texture;
        }
    }
    
    public static class TexturingStateShard extends RenderStateShard {
        public TexturingStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
            super(string, runnable2, runnable3);
        }
    }
    
    public static final class OffsetTexturingStateShard extends TexturingStateShard {
        private final float uOffset;
        private final float vOffset;
        
        public OffsetTexturingStateShard(final float float1, final float float2) {
            super("offset_texturing", () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                RenderSystem.translatef(float1, float2, 0.0f);
                RenderSystem.matrixMode(5888);
            }, () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.popMatrix();
                RenderSystem.matrixMode(5888);
            });
            this.uOffset = float1;
            this.vOffset = float2;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final OffsetTexturingStateShard j3 = (OffsetTexturingStateShard)object;
            return Float.compare(j3.uOffset, this.uOffset) == 0 && Float.compare(j3.vOffset, this.vOffset) == 0;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { this.uOffset, this.vOffset });
        }
    }
    
    public static final class PortalTexturingStateShard extends TexturingStateShard {
        private final int iteration;
        
        public PortalTexturingStateShard(final int integer) {
            super("portal_texturing", () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                RenderSystem.translatef(0.5f, 0.5f, 0.0f);
                RenderSystem.scalef(0.5f, 0.5f, 1.0f);
                RenderSystem.translatef(17.0f / integer, (2.0f + integer / 1.5f) * (Util.getMillis() % 800000L / 800000.0f), 0.0f);
                RenderSystem.rotatef((integer * integer * 4321.0f + integer * 9.0f) * 2.0f, 0.0f, 0.0f, 1.0f);
                RenderSystem.scalef(4.5f - integer / 4.0f, 4.5f - integer / 4.0f, 1.0f);
                RenderSystem.mulTextureByProjModelView();
                RenderSystem.matrixMode(5888);
                RenderSystem.setupEndPortalTexGen();
            }, () -> {
                RenderSystem.matrixMode(5890);
                RenderSystem.popMatrix();
                RenderSystem.matrixMode(5888);
                RenderSystem.clearTexGen();
            });
            this.iteration = integer;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final PortalTexturingStateShard m3 = (PortalTexturingStateShard)object;
            return this.iteration == m3.iteration;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(this.iteration);
        }
    }
    
    static class BooleanStateShard extends RenderStateShard {
        private final boolean enabled;
        
        public BooleanStateShard(final String string, final Runnable runnable2, final Runnable runnable3, final boolean boolean4) {
            super(string, runnable2, runnable3);
            this.enabled = boolean4;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final BooleanStateShard b3 = (BooleanStateShard)object;
            return this.enabled == b3.enabled;
        }
        
        @Override
        public int hashCode() {
            return Boolean.hashCode(this.enabled);
        }
        
        @Override
        public String toString() {
            return this.name + '[' + this.enabled + ']';
        }
    }
    
    public static class LightmapStateShard extends BooleanStateShard {
        public LightmapStateShard(final boolean boolean1) {
            super("lightmap", () -> {
                if (boolean1) {
                    Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
                }
            }, () -> {
                if (boolean1) {
                    Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
                }
            }, boolean1);
        }
    }
    
    public static class OverlayStateShard extends BooleanStateShard {
        public OverlayStateShard(final boolean boolean1) {
            super("overlay", () -> {
                if (boolean1) {
                    Minecraft.getInstance().gameRenderer.overlayTexture().setupOverlayColor();
                }
            }, () -> {
                if (boolean1) {
                    Minecraft.getInstance().gameRenderer.overlayTexture().teardownOverlayColor();
                }
            }, boolean1);
        }
    }
    
    public static class DiffuseLightingStateShard extends BooleanStateShard {
        public DiffuseLightingStateShard(final boolean boolean1) {
            super("diffuse_lighting", () -> {
                if (boolean1) {
                    Lighting.turnBackOn();
                }
            }, () -> {
                if (boolean1) {
                    Lighting.turnOff();
                }
            }, boolean1);
        }
    }
    
    public static class CullStateShard extends BooleanStateShard {
        public CullStateShard(final boolean boolean1) {
            super("cull", () -> {
                if (!boolean1) {
                    RenderSystem.disableCull();
                }
            }, () -> {
                if (!boolean1) {
                    RenderSystem.enableCull();
                }
            }, boolean1);
        }
    }
    
    public static class DepthTestStateShard extends RenderStateShard {
        private final String functionName;
        private final int function;
        
        public DepthTestStateShard(final String string, final int integer) {
            super("depth_test", () -> {
                if (integer != 519) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.depthFunc(integer);
                }
            }, () -> {
                if (integer != 519) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthFunc(515);
                }
            });
            this.functionName = string;
            this.function = integer;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final DepthTestStateShard d3 = (DepthTestStateShard)object;
            return this.function == d3.function;
        }
        
        @Override
        public int hashCode() {
            return Integer.hashCode(this.function);
        }
        
        @Override
        public String toString() {
            return this.name + '[' + this.functionName + ']';
        }
    }
    
    public static class WriteMaskStateShard extends RenderStateShard {
        private final boolean writeColor;
        private final boolean writeDepth;
        
        public WriteMaskStateShard(final boolean boolean1, final boolean boolean2) {
            super("write_mask_state", () -> {
                if (!boolean2) {
                    RenderSystem.depthMask(boolean2);
                }
                if (!boolean1) {
                    RenderSystem.colorMask(boolean1, boolean1, boolean1, boolean1);
                }
            }, () -> {
                if (!boolean2) {
                    RenderSystem.depthMask(true);
                }
                if (!boolean1) {
                    RenderSystem.colorMask(true, true, true, true);
                }
            });
            this.writeColor = boolean1;
            this.writeDepth = boolean2;
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final WriteMaskStateShard r3 = (WriteMaskStateShard)object;
            return this.writeColor == r3.writeColor && this.writeDepth == r3.writeDepth;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { this.writeColor, this.writeDepth });
        }
        
        @Override
        public String toString() {
            return this.name + "[writeColor=" + this.writeColor + ", writeDepth=" + this.writeDepth + ']';
        }
    }
    
    public static class LayeringStateShard extends RenderStateShard {
        public LayeringStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
            super(string, runnable2, runnable3);
        }
    }
    
    public static class FogStateShard extends RenderStateShard {
        public FogStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
            super(string, runnable2, runnable3);
        }
    }
    
    public static class OutputStateShard extends RenderStateShard {
        public OutputStateShard(final String string, final Runnable runnable2, final Runnable runnable3) {
            super(string, runnable2, runnable3);
        }
    }
    
    public static class LineStateShard extends RenderStateShard {
        private final OptionalDouble width;
        
        public LineStateShard(final OptionalDouble optionalDouble) {
            super("line_width", () -> {
                if (!Objects.equals(optionalDouble, OptionalDouble.of(1.0))) {
                    if (optionalDouble.isPresent()) {
                        RenderSystem.lineWidth((float)optionalDouble.getAsDouble());
                    }
                    else {
                        RenderSystem.lineWidth(Math.max(2.5f, Minecraft.getInstance().getWindow().getWidth() / 1920.0f * 2.5f));
                    }
                }
            }, () -> {
                if (!Objects.equals(optionalDouble, OptionalDouble.of(1.0))) {
                    RenderSystem.lineWidth(1.0f);
                }
            });
            this.width = optionalDouble;
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            return this == object || (object != null && this.getClass() == object.getClass() && super.equals(object) && Objects.equals(this.width, ((LineStateShard)object).width));
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { super.hashCode(), this.width });
        }
        
        @Override
        public String toString() {
            return this.name + '[' + (this.width.isPresent() ? Double.valueOf(this.width.getAsDouble()) : "window_scale") + ']';
        }
    }
}
