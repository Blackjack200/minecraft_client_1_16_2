package net.minecraft.client.gui.screens;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

public class DeathScreen extends Screen {
    private int delayTicker;
    private final Component causeOfDeath;
    private final boolean hardcore;
    private Component deathScore;
    
    public DeathScreen(@Nullable final Component nr, final boolean boolean2) {
        super(new TranslatableComponent(boolean2 ? "deathScreen.title.hardcore" : "deathScreen.title"));
        this.causeOfDeath = nr;
        this.hardcore = boolean2;
    }
    
    @Override
    protected void init() {
        this.delayTicker = 0;
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, this.hardcore ? new TranslatableComponent("deathScreen.spectate") : new TranslatableComponent("deathScreen.respawn"), dlg -> {
            this.minecraft.player.respawn();
            this.minecraft.setScreen(null);
            return;
        }));
        final ConfirmScreen confirmScreen;
        ConfirmScreen dnp3;
        final Button dlg2 = this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 96, 200, 20, new TranslatableComponent("deathScreen.titleScreen"), dlg -> {
            if (this.hardcore) {
                this.exitToTitleScreen();
                return;
            }
            else {
                new ConfirmScreen(this::confirmResult, new TranslatableComponent("deathScreen.quit.confirm"), TextComponent.EMPTY, new TranslatableComponent("deathScreen.titleScreen"), new TranslatableComponent("deathScreen.respawn"));
                dnp3 = confirmScreen;
                this.minecraft.setScreen(dnp3);
                dnp3.setDelay(20);
                return;
            }
        }));
        if (!this.hardcore && this.minecraft.getUser() == null) {
            dlg2.active = false;
        }
        for (final AbstractWidget dle4 : this.buttons) {
            dle4.active = false;
        }
        this.deathScore = new TranslatableComponent("deathScreen.score").append(": ").append(new TextComponent(Integer.toString(this.minecraft.player.getScore())).withStyle(ChatFormatting.YELLOW));
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    private void confirmResult(final boolean boolean1) {
        if (boolean1) {
            this.exitToTitleScreen();
        }
        else {
            this.minecraft.player.respawn();
            this.minecraft.setScreen(null);
        }
    }
    
    private void exitToTitleScreen() {
        if (this.minecraft.level != null) {
            this.minecraft.level.disconnect();
        }
        this.minecraft.clearLevel(new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel")));
        this.minecraft.setScreen(new TitleScreen());
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.fillGradient(dfj, 0, 0, this.width, this.height, 1615855616, -1602211792);
        RenderSystem.pushMatrix();
        RenderSystem.scalef(2.0f, 2.0f, 2.0f);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2 / 2, 30, 16777215);
        RenderSystem.popMatrix();
        if (this.causeOfDeath != null) {
            GuiComponent.drawCenteredString(dfj, this.font, this.causeOfDeath, this.width / 2, 85, 16777215);
        }
        GuiComponent.drawCenteredString(dfj, this.font, this.deathScore, this.width / 2, 100, 16777215);
        if (this.causeOfDeath != null && integer3 > 85) {
            final int n = 85;
            this.font.getClass();
            if (integer3 < n + 9) {
                final Style ob6 = this.getClickedComponentStyleAt(integer2);
                this.renderComponentHoverEffect(dfj, ob6, integer2, integer3);
            }
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Nullable
    private Style getClickedComponentStyleAt(final int integer) {
        if (this.causeOfDeath == null) {
            return null;
        }
        final int integer2 = this.minecraft.font.width(this.causeOfDeath);
        final int integer3 = this.width / 2 - integer2 / 2;
        final int integer4 = this.width / 2 + integer2 / 2;
        if (integer < integer3 || integer > integer4) {
            return null;
        }
        return this.minecraft.font.getSplitter().componentStyleAtWidth(this.causeOfDeath, integer - integer3);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.causeOfDeath != null && double2 > 85.0) {
            final int n = 85;
            this.font.getClass();
            if (double2 < n + 9) {
                final Style ob7 = this.getClickedComponentStyleAt((int)double1);
                if (ob7 != null && ob7.getClickEvent() != null && ob7.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                    this.handleComponentClicked(ob7);
                    return false;
                }
            }
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.delayTicker;
        if (this.delayTicker == 20) {
            for (final AbstractWidget dle3 : this.buttons) {
                dle3.active = true;
            }
        }
    }
}
