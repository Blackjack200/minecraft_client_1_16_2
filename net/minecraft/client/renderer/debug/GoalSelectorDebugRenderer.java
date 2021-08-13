package net.minecraft.client.renderer.debug;

import net.minecraft.core.Vec3i;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class GoalSelectorDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private final Map<Integer, List<DebugGoal>> goalSelectors;
    
    public void clear() {
        this.goalSelectors.clear();
    }
    
    public void addGoalSelector(final int integer, final List<DebugGoal> list) {
        this.goalSelectors.put(integer, list);
    }
    
    public GoalSelectorDebugRenderer(final Minecraft djw) {
        this.goalSelectors = (Map<Integer, List<DebugGoal>>)Maps.newHashMap();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        final Camera djh10 = this.minecraft.gameRenderer.getMainCamera();
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        final BlockPos fx11 = new BlockPos(djh10.getPosition().x, 0.0, djh10.getPosition().z);
        this.goalSelectors.forEach((integer, list) -> {
            for (int integer2 = 0; integer2 < list.size(); ++integer2) {
                final DebugGoal a5 = (DebugGoal)list.get(integer2);
                if (fx11.closerThan(a5.pos, 160.0)) {
                    final double double6 = a5.pos.getX() + 0.5;
                    final double double7 = a5.pos.getY() + 2.0 + integer2 * 0.25;
                    final double double8 = a5.pos.getZ() + 0.5;
                    final int integer3 = a5.isRunning ? -16711936 : -3355444;
                    DebugRenderer.renderFloatingText(a5.name, double6, double7, double8, integer3);
                }
            }
        });
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.popMatrix();
    }
    
    public static class DebugGoal {
        public final BlockPos pos;
        public final int priority;
        public final String name;
        public final boolean isRunning;
        
        public DebugGoal(final BlockPos fx, final int integer, final String string, final boolean boolean4) {
            this.pos = fx;
            this.priority = integer;
            this.name = string;
            this.isRunning = boolean4;
        }
    }
}
