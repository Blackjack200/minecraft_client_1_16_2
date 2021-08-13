package net.minecraft.client.gui.screens;

import java.util.Iterator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;

public class AlertScreen extends Screen {
    private final Runnable callback;
    protected final Component text;
    private MultiLineLabel message;
    protected final Component okButton;
    private int delayTicker;
    
    public AlertScreen(final Runnable runnable, final Component nr2, final Component nr3) {
        this(runnable, nr2, nr3, CommonComponents.GUI_BACK);
    }
    
    public AlertScreen(final Runnable runnable, final Component nr2, final Component nr3, final Component nr4) {
        super(nr2);
        this.message = MultiLineLabel.EMPTY;
        this.callback = runnable;
        this.text = nr3;
        this.okButton = nr4;
    }
    
    @Override
    protected void init() {
        super.init();
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, this.okButton, dlg -> this.callback.run()));
        this.message = MultiLineLabel.create(this.font, this.text, this.width - 50);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 70, 16777215);
        this.message.renderCentered(dfj, this.width / 2, 90);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public void tick() {
        super.tick();
        final int delayTicker = this.delayTicker - 1;
        this.delayTicker = delayTicker;
        if (delayTicker == 0) {
            for (final AbstractWidget dle3 : this.buttons) {
                dle3.active = true;
            }
        }
    }
}
