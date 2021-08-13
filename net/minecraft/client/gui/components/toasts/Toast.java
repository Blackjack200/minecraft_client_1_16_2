package net.minecraft.client.gui.components.toasts;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public interface Toast {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/toasts.png");
    public static final Object NO_TOKEN = new Object();
    
    Visibility render(final PoseStack dfj, final ToastComponent dmo, final long long3);
    
    default Object getToken() {
        return Toast.NO_TOKEN;
    }
    
    default int width() {
        return 160;
    }
    
    default int height() {
        return 32;
    }
    
    public enum Visibility {
        SHOW(SoundEvents.UI_TOAST_IN), 
        HIDE(SoundEvents.UI_TOAST_OUT);
        
        private final SoundEvent soundEvent;
        
        private Visibility(final SoundEvent adn) {
            this.soundEvent = adn;
        }
        
        public void playSound(final SoundManager enm) {
            enm.play(SimpleSoundInstance.forUI(this.soundEvent, 1.0f, 1.0f));
        }
    }
}
