package net.minecraft.client.gui.components.toasts;

import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class TutorialToast implements Toast {
    private final Icons icon;
    private final Component title;
    private final Component message;
    private Visibility visibility;
    private long lastProgressTime;
    private float lastProgress;
    private float progress;
    private final boolean progressable;
    
    public TutorialToast(final Icons a, final Component nr2, @Nullable final Component nr3, final boolean boolean4) {
        this.visibility = Visibility.SHOW;
        this.icon = a;
        this.title = nr2;
        this.message = nr3;
        this.progressable = boolean4;
    }
    
    public Visibility render(final PoseStack dfj, final ToastComponent dmo, final long long3) {
        dmo.getMinecraft().getTextureManager().bind(TutorialToast.TEXTURE);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        dmo.blit(dfj, 0, 0, 0, 96, this.width(), this.height());
        this.icon.render(dfj, dmo, 6, 6);
        if (this.message == null) {
            dmo.getMinecraft().font.draw(dfj, this.title, 30.0f, 12.0f, -11534256);
        }
        else {
            dmo.getMinecraft().font.draw(dfj, this.title, 30.0f, 7.0f, -11534256);
            dmo.getMinecraft().font.draw(dfj, this.message, 30.0f, 18.0f, -16777216);
        }
        if (this.progressable) {
            GuiComponent.fill(dfj, 3, 28, 157, 29, -1);
            final float float6 = (float)Mth.clampedLerp(this.lastProgress, this.progress, (long3 - this.lastProgressTime) / 100.0f);
            int integer7;
            if (this.progress >= this.lastProgress) {
                integer7 = -16755456;
            }
            else {
                integer7 = -11206656;
            }
            GuiComponent.fill(dfj, 3, 28, (int)(3.0f + 154.0f * float6), 29, integer7);
            this.lastProgress = float6;
            this.lastProgressTime = long3;
        }
        return this.visibility;
    }
    
    public void hide() {
        this.visibility = Visibility.HIDE;
    }
    
    public void updateProgress(final float float1) {
        this.progress = float1;
    }
    
    public enum Icons {
        MOVEMENT_KEYS(0, 0), 
        MOUSE(1, 0), 
        TREE(2, 0), 
        RECIPE_BOOK(0, 1), 
        WOODEN_PLANKS(1, 1);
        
        private final int x;
        private final int y;
        
        private Icons(final int integer3, final int integer4) {
            this.x = integer3;
            this.y = integer4;
        }
        
        public void render(final PoseStack dfj, final GuiComponent dkt, final int integer3, final int integer4) {
            RenderSystem.enableBlend();
            dkt.blit(dfj, integer3, integer4, 176 + this.x * 20, this.y * 20, 20, 20);
            RenderSystem.enableBlend();
        }
    }
}
