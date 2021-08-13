package net.minecraft.client.gui.components.toasts;

import java.util.Iterator;
import java.util.List;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;

public class AdvancementToast implements Toast {
    private final Advancement advancement;
    private boolean playedSound;
    
    public AdvancementToast(final Advancement y) {
        this.advancement = y;
    }
    
    public Visibility render(final PoseStack dfj, final ToastComponent dmo, final long long3) {
        dmo.getMinecraft().getTextureManager().bind(AdvancementToast.TEXTURE);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        final DisplayInfo ah6 = this.advancement.getDisplay();
        dmo.blit(dfj, 0, 0, 0, 0, this.width(), this.height());
        if (ah6 != null) {
            final List<FormattedCharSequence> list7 = dmo.getMinecraft().font.split(ah6.getTitle(), 125);
            final int integer8 = (ah6.getFrame() == FrameType.CHALLENGE) ? 16746751 : 16776960;
            if (list7.size() == 1) {
                dmo.getMinecraft().font.draw(dfj, ah6.getFrame().getDisplayName(), 30.0f, 7.0f, integer8 | 0xFF000000);
                dmo.getMinecraft().font.draw(dfj, (FormattedCharSequence)list7.get(0), 30.0f, 18.0f, -1);
            }
            else {
                final int integer9 = 1500;
                final float float10 = 300.0f;
                if (long3 < 1500L) {
                    final int integer10 = Mth.floor(Mth.clamp((1500L - long3) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                    dmo.getMinecraft().font.draw(dfj, ah6.getFrame().getDisplayName(), 30.0f, 11.0f, integer8 | integer10);
                }
                else {
                    final int integer10 = Mth.floor(Mth.clamp((long3 - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                    final int n = this.height() / 2;
                    final int size = list7.size();
                    dmo.getMinecraft().font.getClass();
                    int integer11 = n - size * 9 / 2;
                    for (final FormattedCharSequence aex14 : list7) {
                        dmo.getMinecraft().font.draw(dfj, aex14, 30.0f, (float)integer11, 0xFFFFFF | integer10);
                        final int n2 = integer11;
                        dmo.getMinecraft().font.getClass();
                        integer11 = n2 + 9;
                    }
                }
            }
            if (!this.playedSound && long3 > 0L) {
                this.playedSound = true;
                if (ah6.getFrame() == FrameType.CHALLENGE) {
                    dmo.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f));
                }
            }
            dmo.getMinecraft().getItemRenderer().renderAndDecorateFakeItem(ah6.getIcon(), 8, 8);
            return (long3 >= 5000L) ? Visibility.HIDE : Visibility.SHOW;
        }
        return Visibility.HIDE;
    }
}
