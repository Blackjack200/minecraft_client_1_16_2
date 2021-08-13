package com.mojang.blaze3d.platform;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;

public class Lighting {
    private static final Vector3f DIFFUSE_LIGHT_0;
    private static final Vector3f DIFFUSE_LIGHT_1;
    private static final Vector3f NETHER_DIFFUSE_LIGHT_0;
    private static final Vector3f NETHER_DIFFUSE_LIGHT_1;
    
    public static void turnBackOn() {
        RenderSystem.enableLighting();
        RenderSystem.enableColorMaterial();
        RenderSystem.colorMaterial(1032, 5634);
    }
    
    public static void turnOff() {
        RenderSystem.disableLighting();
        RenderSystem.disableColorMaterial();
    }
    
    public static void setupNetherLevel(final Matrix4f b) {
        RenderSystem.setupLevelDiffuseLighting(Lighting.NETHER_DIFFUSE_LIGHT_0, Lighting.NETHER_DIFFUSE_LIGHT_1, b);
    }
    
    public static void setupLevel(final Matrix4f b) {
        RenderSystem.setupLevelDiffuseLighting(Lighting.DIFFUSE_LIGHT_0, Lighting.DIFFUSE_LIGHT_1, b);
    }
    
    public static void setupForFlatItems() {
        RenderSystem.setupGuiFlatDiffuseLighting(Lighting.DIFFUSE_LIGHT_0, Lighting.DIFFUSE_LIGHT_1);
    }
    
    public static void setupFor3DItems() {
        RenderSystem.setupGui3DDiffuseLighting(Lighting.DIFFUSE_LIGHT_0, Lighting.DIFFUSE_LIGHT_1);
    }
    
    static {
        DIFFUSE_LIGHT_0 = Util.<Vector3f>make(new Vector3f(0.2f, 1.0f, -0.7f), (java.util.function.Consumer<Vector3f>)Vector3f::normalize);
        DIFFUSE_LIGHT_1 = Util.<Vector3f>make(new Vector3f(-0.2f, 1.0f, 0.7f), (java.util.function.Consumer<Vector3f>)Vector3f::normalize);
        NETHER_DIFFUSE_LIGHT_0 = Util.<Vector3f>make(new Vector3f(0.2f, 1.0f, -0.7f), (java.util.function.Consumer<Vector3f>)Vector3f::normalize);
        NETHER_DIFFUSE_LIGHT_1 = Util.<Vector3f>make(new Vector3f(-0.2f, -1.0f, 0.7f), (java.util.function.Consumer<Vector3f>)Vector3f::normalize);
    }
}
