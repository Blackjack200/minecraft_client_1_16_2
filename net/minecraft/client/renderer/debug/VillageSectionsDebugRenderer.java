package net.minecraft.client.renderer.debug;

import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Sets;
import net.minecraft.core.SectionPos;
import java.util.Set;

public class VillageSectionsDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Set<SectionPos> villageSections;
    
    VillageSectionsDebugRenderer() {
        this.villageSections = (Set<SectionPos>)Sets.newHashSet();
    }
    
    public void clear() {
        this.villageSections.clear();
    }
    
    public void setVillageSection(final SectionPos gp) {
        this.villageSections.add(gp);
    }
    
    public void setNotVillageSection(final SectionPos gp) {
        this.villageSections.remove(gp);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        this.doRender(double3, double4, double5);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
    
    private void doRender(final double double1, final double double2, final double double3) {
        final BlockPos fx8 = new BlockPos(double1, double2, double3);
        this.villageSections.forEach(gp -> {
            if (fx8.closerThan(gp.center(), 60.0)) {
                highlightVillageSection(gp);
            }
        });
    }
    
    private static void highlightVillageSection(final SectionPos gp) {
        final float float2 = 1.0f;
        final BlockPos fx3 = gp.center();
        final BlockPos fx4 = fx3.offset(-1.0, -1.0, -1.0);
        final BlockPos fx5 = fx3.offset(1.0, 1.0, 1.0);
        DebugRenderer.renderFilledBox(fx4, fx5, 0.2f, 1.0f, 0.2f, 0.15f);
    }
}
