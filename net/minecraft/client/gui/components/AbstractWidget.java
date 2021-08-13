package net.minecraft.client.gui.components;

import java.util.Objects;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.gui.Font;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.GuiComponent;

public abstract class AbstractWidget extends GuiComponent implements Widget, GuiEventListener {
    public static final ResourceLocation WIDGETS_LOCATION;
    protected int width;
    protected int height;
    public int x;
    public int y;
    private Component message;
    private boolean wasHovered;
    protected boolean isHovered;
    public boolean active;
    public boolean visible;
    protected float alpha;
    protected long nextNarration;
    private boolean focused;
    
    public AbstractWidget(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr) {
        this.active = true;
        this.visible = true;
        this.alpha = 1.0f;
        this.nextNarration = Long.MAX_VALUE;
        this.x = integer1;
        this.y = integer2;
        this.width = integer3;
        this.height = integer4;
        this.message = nr;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    protected int getYImage(final boolean boolean1) {
        int integer3 = 1;
        if (!this.active) {
            integer3 = 0;
        }
        else if (boolean1) {
            integer3 = 2;
        }
        return integer3;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (!this.visible) {
            return;
        }
        this.isHovered = (integer2 >= this.x && integer3 >= this.y && integer2 < this.x + this.width && integer3 < this.y + this.height);
        if (this.wasHovered != this.isHovered()) {
            if (this.isHovered()) {
                if (this.focused) {
                    this.queueNarration(200);
                }
                else {
                    this.queueNarration(750);
                }
            }
            else {
                this.nextNarration = Long.MAX_VALUE;
            }
        }
        if (this.visible) {
            this.renderButton(dfj, integer2, integer3, float4);
        }
        this.narrate();
        this.wasHovered = this.isHovered();
    }
    
    protected void narrate() {
        if (this.active && this.isHovered() && Util.getMillis() > this.nextNarration) {
            final String string2 = this.createNarrationMessage().getString();
            if (!string2.isEmpty()) {
                NarratorChatListener.INSTANCE.sayNow(string2);
                this.nextNarration = Long.MAX_VALUE;
            }
        }
    }
    
    protected MutableComponent createNarrationMessage() {
        return new TranslatableComponent("gui.narrate.button", new Object[] { this.getMessage() });
    }
    
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        final Minecraft djw6 = Minecraft.getInstance();
        final Font dkr7 = djw6.font;
        djw6.getTextureManager().bind(AbstractWidget.WIDGETS_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, this.alpha);
        final int integer4 = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(dfj, this.x, this.y, 0, 46 + integer4 * 20, this.width / 2, this.height);
        this.blit(dfj, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + integer4 * 20, this.width / 2, this.height);
        this.renderBg(dfj, djw6, integer2, integer3);
        final int integer5 = this.active ? 16777215 : 10526880;
        GuiComponent.drawCenteredString(dfj, dkr7, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, integer5 | Mth.ceil(this.alpha * 255.0f) << 24);
    }
    
    protected void renderBg(final PoseStack dfj, final Minecraft djw, final int integer3, final int integer4) {
    }
    
    public void onClick(final double double1, final double double2) {
    }
    
    public void onRelease(final double double1, final double double2) {
    }
    
    protected void onDrag(final double double1, final double double2, final double double3, final double double4) {
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (this.isValidClickButton(integer)) {
            final boolean boolean7 = this.clicked(double1, double2);
            if (boolean7) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.onClick(double1, double2);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean mouseReleased(final double double1, final double double2, final int integer) {
        if (this.isValidClickButton(integer)) {
            this.onRelease(double1, double2);
            return true;
        }
        return false;
    }
    
    protected boolean isValidClickButton(final int integer) {
        return integer == 0;
    }
    
    @Override
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        if (this.isValidClickButton(integer)) {
            this.onDrag(double1, double2, double4, double5);
            return true;
        }
        return false;
    }
    
    protected boolean clicked(final double double1, final double double2) {
        return this.active && this.visible && double1 >= this.x && double2 >= this.y && double1 < this.x + this.width && double2 < this.y + this.height;
    }
    
    public boolean isHovered() {
        return this.isHovered || this.focused;
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        if (!this.active || !this.visible) {
            return false;
        }
        this.onFocusedChanged(this.focused = !this.focused);
        return this.focused;
    }
    
    protected void onFocusedChanged(final boolean boolean1) {
    }
    
    @Override
    public boolean isMouseOver(final double double1, final double double2) {
        return this.active && this.visible && double1 >= this.x && double2 >= this.y && double1 < this.x + this.width && double2 < this.y + this.height;
    }
    
    public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
    }
    
    public void playDownSound(final SoundManager enm) {
        enm.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int integer) {
        this.width = integer;
    }
    
    public void setAlpha(final float float1) {
        this.alpha = float1;
    }
    
    public void setMessage(final Component nr) {
        if (!Objects.equals(nr, this.message)) {
            this.queueNarration(250);
        }
        this.message = nr;
    }
    
    public void queueNarration(final int integer) {
        this.nextNarration = Util.getMillis() + integer;
    }
    
    public Component getMessage() {
        return this.message;
    }
    
    public boolean isFocused() {
        return this.focused;
    }
    
    protected void setFocused(final boolean boolean1) {
        this.focused = boolean1;
    }
    
    static {
        WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    }
}
