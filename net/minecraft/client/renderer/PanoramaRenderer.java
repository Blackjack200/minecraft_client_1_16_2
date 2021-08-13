package net.minecraft.client.renderer;

import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;

public class PanoramaRenderer {
    private final Minecraft minecraft;
    private final CubeMap cubeMap;
    private float time;
    
    public PanoramaRenderer(final CubeMap dzm) {
        this.cubeMap = dzm;
        this.minecraft = Minecraft.getInstance();
    }
    
    public void render(final float float1, final float float2) {
        this.time += float1;
        this.cubeMap.render(this.minecraft, Mth.sin(this.time * 0.001f) * 5.0f + 25.0f, -this.time * 0.1f, float2);
    }
}
