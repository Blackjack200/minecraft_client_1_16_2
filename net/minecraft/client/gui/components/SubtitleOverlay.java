package net.minecraft.client.gui.components;

import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import java.util.Iterator;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.Util;
import net.minecraft.world.phys.Vec3;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.gui.GuiComponent;

public class SubtitleOverlay extends GuiComponent implements SoundEventListener {
    private final Minecraft minecraft;
    private final List<Subtitle> subtitles;
    private boolean isListening;
    
    public SubtitleOverlay(final Minecraft djw) {
        this.subtitles = (List<Subtitle>)Lists.newArrayList();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj) {
        if (!this.isListening && this.minecraft.options.showSubtitles) {
            this.minecraft.getSoundManager().addListener(this);
            this.isListening = true;
        }
        else if (this.isListening && !this.minecraft.options.showSubtitles) {
            this.minecraft.getSoundManager().removeListener(this);
            this.isListening = false;
        }
        if (!this.isListening || this.subtitles.isEmpty()) {
            return;
        }
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Vec3 dck3 = new Vec3(this.minecraft.player.getX(), this.minecraft.player.getEyeY(), this.minecraft.player.getZ());
        final Vec3 dck4 = new Vec3(0.0, 0.0, -1.0).xRot(-this.minecraft.player.xRot * 0.017453292f).yRot(-this.minecraft.player.yRot * 0.017453292f);
        final Vec3 dck5 = new Vec3(0.0, 1.0, 0.0).xRot(-this.minecraft.player.xRot * 0.017453292f).yRot(-this.minecraft.player.yRot * 0.017453292f);
        final Vec3 dck6 = dck4.cross(dck5);
        int integer7 = 0;
        int integer8 = 0;
        final Iterator<Subtitle> iterator9 = (Iterator<Subtitle>)this.subtitles.iterator();
        while (iterator9.hasNext()) {
            final Subtitle a10 = (Subtitle)iterator9.next();
            if (a10.getTime() + 3000L <= Util.getMillis()) {
                iterator9.remove();
            }
            else {
                integer8 = Math.max(integer8, this.minecraft.font.width(a10.getText()));
            }
        }
        integer8 += this.minecraft.font.width("<") + this.minecraft.font.width(" ") + this.minecraft.font.width(">") + this.minecraft.font.width(" ");
        final Iterator iterator10 = this.subtitles.iterator();
        while (iterator10.hasNext()) {
            final Subtitle a10 = (Subtitle)iterator10.next();
            final int integer9 = 255;
            final Component nr12 = a10.getText();
            final Vec3 dck7 = a10.getLocation().subtract(dck3).normalize();
            final double double14 = -dck6.dot(dck7);
            final double double15 = -dck4.dot(dck7);
            final boolean boolean18 = double15 > 0.5;
            final int integer10 = integer8 / 2;
            this.minecraft.font.getClass();
            final int integer11 = 9;
            final int integer12 = integer11 / 2;
            final float float22 = 1.0f;
            final int integer13 = this.minecraft.font.width(nr12);
            final int integer14 = Mth.floor(Mth.clampedLerp(255.0, 75.0, (Util.getMillis() - a10.getTime()) / 3000.0f));
            final int integer15 = integer14 << 16 | integer14 << 8 | integer14;
            RenderSystem.pushMatrix();
            RenderSystem.translatef(this.minecraft.getWindow().getGuiScaledWidth() - integer10 * 1.0f - 2.0f, this.minecraft.getWindow().getGuiScaledHeight() - 30 - integer7 * (integer11 + 1) * 1.0f, 0.0f);
            RenderSystem.scalef(1.0f, 1.0f, 1.0f);
            GuiComponent.fill(dfj, -integer10 - 1, -integer12 - 1, integer10 + 1, integer12 + 1, this.minecraft.options.getBackgroundColor(0.8f));
            RenderSystem.enableBlend();
            if (!boolean18) {
                if (double14 > 0.0) {
                    this.minecraft.font.draw(dfj, ">", (float)(integer10 - this.minecraft.font.width(">")), (float)(-integer12), integer15 - 16777216);
                }
                else if (double14 < 0.0) {
                    this.minecraft.font.draw(dfj, "<", (float)(-integer10), (float)(-integer12), integer15 - 16777216);
                }
            }
            this.minecraft.font.draw(dfj, nr12, (float)(-integer13 / 2), (float)(-integer12), integer15 - 16777216);
            RenderSystem.popMatrix();
            ++integer7;
        }
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
    }
    
    @Override
    public void onPlaySound(final SoundInstance eml, final WeighedSoundEvents enn) {
        if (enn.getSubtitle() == null) {
            return;
        }
        final Component nr4 = enn.getSubtitle();
        if (!this.subtitles.isEmpty()) {
            for (final Subtitle a6 : this.subtitles) {
                if (a6.getText().equals(nr4)) {
                    a6.refresh(new Vec3(eml.getX(), eml.getY(), eml.getZ()));
                    return;
                }
            }
        }
        this.subtitles.add(new Subtitle(nr4, new Vec3(eml.getX(), eml.getY(), eml.getZ())));
    }
    
    public class Subtitle {
        private final Component text;
        private long time;
        private Vec3 location;
        
        public Subtitle(final Component nr, final Vec3 dck) {
            this.text = nr;
            this.location = dck;
            this.time = Util.getMillis();
        }
        
        public Component getText() {
            return this.text;
        }
        
        public long getTime() {
            return this.time;
        }
        
        public Vec3 getLocation() {
            return this.location;
        }
        
        public void refresh(final Vec3 dck) {
            this.location = dck;
            this.time = Util.getMillis();
        }
    }
}
