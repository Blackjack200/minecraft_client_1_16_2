package net.minecraft.client.gui.components.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import java.util.Arrays;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Queues;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;

public class ToastComponent extends GuiComponent {
    private final Minecraft minecraft;
    private final ToastInstance<?>[] visible;
    private final Deque<Toast> queued;
    
    public ToastComponent(final Minecraft djw) {
        this.visible = new ToastInstance[5];
        this.queued = (Deque<Toast>)Queues.newArrayDeque();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj) {
        if (this.minecraft.options.hideGui) {
            return;
        }
        for (int integer3 = 0; integer3 < this.visible.length; ++integer3) {
            final ToastInstance<?> a4 = this.visible[integer3];
            if (a4 != null && a4.render(this.minecraft.getWindow().getGuiScaledWidth(), integer3, dfj)) {
                this.visible[integer3] = null;
            }
            if (this.visible[integer3] == null && !this.queued.isEmpty()) {
                this.visible[integer3] = new ToastInstance<>((Toast)this.queued.removeFirst());
            }
        }
    }
    
    @Nullable
    public <T extends Toast> T getToast(final Class<? extends T> class1, final Object object) {
        for (final ToastInstance<?> a7 : this.visible) {
            if (a7 != null && class1.isAssignableFrom((Class)a7.getToast().getClass()) && ((Toast)a7.getToast()).getToken().equals(object)) {
                return (T)a7.getToast();
            }
        }
        for (final Toast dmn5 : this.queued) {
            if (class1.isAssignableFrom(dmn5.getClass()) && dmn5.getToken().equals(object)) {
                return (T)dmn5;
            }
        }
        return null;
    }
    
    public void clear() {
        Arrays.fill((Object[])this.visible, null);
        this.queued.clear();
    }
    
    public void addToast(final Toast dmn) {
        this.queued.add(dmn);
    }
    
    public Minecraft getMinecraft() {
        return this.minecraft;
    }
    
    class ToastInstance<T extends Toast> {
        private final T toast;
        private long animationTime;
        private long visibleTime;
        private Toast.Visibility visibility;
        
        private ToastInstance(final T dmn) {
            this.animationTime = -1L;
            this.visibleTime = -1L;
            this.visibility = Toast.Visibility.SHOW;
            this.toast = dmn;
        }
        
        public T getToast() {
            return this.toast;
        }
        
        private float getVisibility(final long long1) {
            float float4 = Mth.clamp((long1 - this.animationTime) / 600.0f, 0.0f, 1.0f);
            float4 *= float4;
            if (this.visibility == Toast.Visibility.HIDE) {
                return 1.0f - float4;
            }
            return float4;
        }
        
        public boolean render(final int integer1, final int integer2, final PoseStack dfj) {
            final long long5 = Util.getMillis();
            if (this.animationTime == -1L) {
                this.animationTime = long5;
                this.visibility.playSound(ToastComponent.this.minecraft.getSoundManager());
            }
            if (this.visibility == Toast.Visibility.SHOW && long5 - this.animationTime <= 600L) {
                this.visibleTime = long5;
            }
            RenderSystem.pushMatrix();
            RenderSystem.translatef(integer1 - this.toast.width() * this.getVisibility(long5), (float)(integer2 * this.toast.height()), (float)(800 + integer2));
            final Toast.Visibility a7 = this.toast.render(dfj, ToastComponent.this, long5 - this.visibleTime);
            RenderSystem.popMatrix();
            if (a7 != this.visibility) {
                this.animationTime = long5 - (int)((1.0f - this.getVisibility(long5)) * 600.0f);
                (this.visibility = a7).playSound(ToastComponent.this.minecraft.getSoundManager());
            }
            return this.visibility == Toast.Visibility.HIDE && long5 - this.animationTime > 600L;
        }
    }
}
