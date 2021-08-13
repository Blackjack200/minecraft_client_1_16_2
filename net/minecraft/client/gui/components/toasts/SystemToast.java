package net.minecraft.client.gui.components.toasts;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.Minecraft;
import javax.annotation.Nullable;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.network.chat.Component;

public class SystemToast implements Toast {
    private final SystemToastIds id;
    private Component title;
    private List<FormattedCharSequence> messageLines;
    private long lastChanged;
    private boolean changed;
    private final int width;
    
    public SystemToast(final SystemToastIds a, final Component nr2, @Nullable final Component nr3) {
        this(a, nr2, (List<FormattedCharSequence>)nullToEmpty(nr3), 160);
    }
    
    public static SystemToast multiline(final Minecraft djw, final SystemToastIds a, final Component nr3, final Component nr4) {
        final Font dkr5 = djw.font;
        final List<FormattedCharSequence> list6 = dkr5.split(nr4, 200);
        final int integer7 = Math.max(200, list6.stream().mapToInt(dkr5::width).max().orElse(200));
        return new SystemToast(a, nr3, list6, integer7 + 30);
    }
    
    private SystemToast(final SystemToastIds a, final Component nr, final List<FormattedCharSequence> list, final int integer) {
        this.id = a;
        this.title = nr;
        this.messageLines = list;
        this.width = integer;
    }
    
    private static ImmutableList<FormattedCharSequence> nullToEmpty(@Nullable final Component nr) {
        return (ImmutableList<FormattedCharSequence>)((nr == null) ? ImmutableList.of() : ImmutableList.of(nr.getVisualOrderText()));
    }
    
    public int width() {
        return this.width;
    }
    
    public Visibility render(final PoseStack dfj, final ToastComponent dmo, final long long3) {
        if (this.changed) {
            this.lastChanged = long3;
            this.changed = false;
        }
        dmo.getMinecraft().getTextureManager().bind(SystemToast.TEXTURE);
        RenderSystem.color3f(1.0f, 1.0f, 1.0f);
        final int integer6 = this.width();
        final int integer7 = 12;
        if (integer6 == 160 && this.messageLines.size() <= 1) {
            dmo.blit(dfj, 0, 0, 0, 64, integer6, this.height());
        }
        else {
            final int integer8 = this.height() + Math.max(0, this.messageLines.size() - 1) * 12;
            final int integer9 = 28;
            final int integer10 = Math.min(4, integer8 - 28);
            this.renderBackgroundRow(dfj, dmo, integer6, 0, 0, 28);
            for (int integer11 = 28; integer11 < integer8 - integer10; integer11 += 10) {
                this.renderBackgroundRow(dfj, dmo, integer6, 16, integer11, Math.min(16, integer8 - integer11 - integer10));
            }
            this.renderBackgroundRow(dfj, dmo, integer6, 32 - integer10, integer8 - integer10, integer10);
        }
        if (this.messageLines == null) {
            dmo.getMinecraft().font.draw(dfj, this.title, 18.0f, 12.0f, -256);
        }
        else {
            dmo.getMinecraft().font.draw(dfj, this.title, 18.0f, 7.0f, -256);
            for (int integer8 = 0; integer8 < this.messageLines.size(); ++integer8) {
                dmo.getMinecraft().font.draw(dfj, (FormattedCharSequence)this.messageLines.get(integer8), 18.0f, (float)(18 + integer8 * 12), -1);
            }
        }
        return (long3 - this.lastChanged < 5000L) ? Visibility.SHOW : Visibility.HIDE;
    }
    
    private void renderBackgroundRow(final PoseStack dfj, final ToastComponent dmo, final int integer3, final int integer4, final int integer5, final int integer6) {
        final int integer7 = (integer4 == 0) ? 20 : 5;
        final int integer8 = Math.min(60, integer3 - integer7);
        dmo.blit(dfj, 0, integer5, 0, 64 + integer4, integer7, integer6);
        for (int integer9 = integer7; integer9 < integer3 - integer8; integer9 += 64) {
            dmo.blit(dfj, integer9, integer5, 32, 64 + integer4, Math.min(64, integer3 - integer9 - integer8), integer6);
        }
        dmo.blit(dfj, integer3 - integer8, integer5, 160 - integer8, 64 + integer4, integer8, integer6);
    }
    
    public void reset(final Component nr1, @Nullable final Component nr2) {
        this.title = nr1;
        this.messageLines = (List<FormattedCharSequence>)nullToEmpty(nr2);
        this.changed = true;
    }
    
    public SystemToastIds getToken() {
        return this.id;
    }
    
    public static void add(final ToastComponent dmo, final SystemToastIds a, final Component nr3, @Nullable final Component nr4) {
        dmo.addToast(new SystemToast(a, nr3, nr4));
    }
    
    public static void addOrUpdate(final ToastComponent dmo, final SystemToastIds a, final Component nr3, @Nullable final Component nr4) {
        final SystemToast dmm5 = dmo.<SystemToast>getToast((java.lang.Class<? extends SystemToast>)SystemToast.class, a);
        if (dmm5 == null) {
            add(dmo, a, nr3, nr4);
        }
        else {
            dmm5.reset(nr3, nr4);
        }
    }
    
    public static void onWorldAccessFailure(final Minecraft djw, final String string) {
        add(djw.getToasts(), SystemToastIds.WORLD_ACCESS_FAILURE, new TranslatableComponent("selectWorld.access_failure"), new TextComponent(string));
    }
    
    public static void onWorldDeleteFailure(final Minecraft djw, final String string) {
        add(djw.getToasts(), SystemToastIds.WORLD_ACCESS_FAILURE, new TranslatableComponent("selectWorld.delete_failure"), new TextComponent(string));
    }
    
    public static void onPackCopyFailure(final Minecraft djw, final String string) {
        add(djw.getToasts(), SystemToastIds.PACK_COPY_FAILURE, new TranslatableComponent("pack.copyFailure"), new TextComponent(string));
    }
    
    public enum SystemToastIds {
        TUTORIAL_HINT, 
        NARRATOR_TOGGLE, 
        WORLD_BACKUP, 
        WORLD_GEN_SETTINGS_TRANSFER, 
        PACK_LOAD_FAILURE, 
        WORLD_ACCESS_FAILURE, 
        PACK_COPY_FAILURE;
    }
}
