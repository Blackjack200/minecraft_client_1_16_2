package net.minecraft.client.renderer;

import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.phys.Vec3;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public abstract class DimensionSpecialEffects {
    private static final Object2ObjectMap<ResourceLocation, DimensionSpecialEffects> EFFECTS;
    private final float[] sunriseCol;
    private final float cloudLevel;
    private final boolean hasGround;
    private final SkyType skyType;
    private final boolean forceBrightLightmap;
    private final boolean constantAmbientLight;
    
    public DimensionSpecialEffects(final float float1, final boolean boolean2, final SkyType d, final boolean boolean4, final boolean boolean5) {
        this.sunriseCol = new float[4];
        this.cloudLevel = float1;
        this.hasGround = boolean2;
        this.skyType = d;
        this.forceBrightLightmap = boolean4;
        this.constantAmbientLight = boolean5;
    }
    
    public static DimensionSpecialEffects forType(final DimensionType cha) {
        return (DimensionSpecialEffects)DimensionSpecialEffects.EFFECTS.get(cha.effectsLocation());
    }
    
    @Nullable
    public float[] getSunriseColor(final float float1, final float float2) {
        final float float3 = 0.4f;
        final float float4 = Mth.cos(float1 * 6.2831855f) - 0.0f;
        final float float5 = -0.0f;
        if (float4 >= -0.4f && float4 <= 0.4f) {
            final float float6 = (float4 + 0.0f) / 0.4f * 0.5f + 0.5f;
            float float7 = 1.0f - (1.0f - Mth.sin(float6 * 3.1415927f)) * 0.99f;
            float7 *= float7;
            this.sunriseCol[0] = float6 * 0.3f + 0.7f;
            this.sunriseCol[1] = float6 * float6 * 0.7f + 0.2f;
            this.sunriseCol[2] = float6 * float6 * 0.0f + 0.2f;
            this.sunriseCol[3] = float7;
            return this.sunriseCol;
        }
        return null;
    }
    
    public float getCloudHeight() {
        return this.cloudLevel;
    }
    
    public boolean hasGround() {
        return this.hasGround;
    }
    
    public abstract Vec3 getBrightnessDependentFogColor(final Vec3 dck, final float float2);
    
    public abstract boolean isFoggyAt(final int integer1, final int integer2);
    
    public SkyType skyType() {
        return this.skyType;
    }
    
    public boolean forceBrightLightmap() {
        return this.forceBrightLightmap;
    }
    
    public boolean constantAmbientLight() {
        return this.constantAmbientLight;
    }
    
    static {
        EFFECTS = Util.<Object2ObjectMap>make((Object2ObjectMap)new Object2ObjectArrayMap(), (java.util.function.Consumer<Object2ObjectMap>)(object2ObjectArrayMap -> {
            final OverworldEffects c2 = new OverworldEffects();
            object2ObjectArrayMap.defaultReturnValue(c2);
            object2ObjectArrayMap.put(DimensionType.OVERWORLD_EFFECTS, c2);
            object2ObjectArrayMap.put(DimensionType.NETHER_EFFECTS, new NetherEffects());
            object2ObjectArrayMap.put(DimensionType.END_EFFECTS, new EndEffects());
        }));
    }
    
    public enum SkyType {
        NONE, 
        NORMAL, 
        END;
    }
    
    public static class NetherEffects extends DimensionSpecialEffects {
        public NetherEffects() {
            super(Float.NaN, true, SkyType.NONE, false, true);
        }
        
        @Override
        public Vec3 getBrightnessDependentFogColor(final Vec3 dck, final float float2) {
            return dck;
        }
        
        @Override
        public boolean isFoggyAt(final int integer1, final int integer2) {
            return true;
        }
    }
    
    public static class OverworldEffects extends DimensionSpecialEffects {
        public OverworldEffects() {
            super(128.0f, true, SkyType.NORMAL, false, false);
        }
        
        @Override
        public Vec3 getBrightnessDependentFogColor(final Vec3 dck, final float float2) {
            return dck.multiply(float2 * 0.94f + 0.06f, float2 * 0.94f + 0.06f, float2 * 0.91f + 0.09f);
        }
        
        @Override
        public boolean isFoggyAt(final int integer1, final int integer2) {
            return false;
        }
    }
    
    public static class EndEffects extends DimensionSpecialEffects {
        public EndEffects() {
            super(Float.NaN, false, SkyType.END, true, false);
        }
        
        @Override
        public Vec3 getBrightnessDependentFogColor(final Vec3 dck, final float float2) {
            return dck.scale(0.15000000596046448);
        }
        
        @Override
        public boolean isFoggyAt(final int integer1, final int integer2) {
            return false;
        }
        
        @Nullable
        @Override
        public float[] getSunriseColor(final float float1, final float float2) {
            return null;
        }
    }
}
