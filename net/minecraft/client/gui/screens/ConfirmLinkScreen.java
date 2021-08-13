package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.network.chat.Component;

public class ConfirmLinkScreen extends ConfirmScreen {
    private final Component warning;
    private final Component copyButton;
    private final String url;
    private final boolean showWarning;
    
    public ConfirmLinkScreen(final BooleanConsumer booleanConsumer, final String string, final boolean boolean3) {
        super(booleanConsumer, new TranslatableComponent(boolean3 ? "chat.link.confirmTrusted" : "chat.link.confirm"), new TextComponent(string));
        this.yesButton = (boolean3 ? new TranslatableComponent("chat.link.open") : CommonComponents.GUI_YES);
        this.noButton = (boolean3 ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO);
        this.copyButton = new TranslatableComponent("chat.copy");
        this.warning = new TranslatableComponent("chat.link.warning");
        this.showWarning = !boolean3;
        this.url = string;
    }
    
    @Override
    protected void init() {
        super.init();
        this.buttons.clear();
        this.children.clear();
        this.<Button>addButton(new Button(this.width / 2 - 50 - 105, this.height / 6 + 96, 100, 20, this.yesButton, dlg -> this.callback.accept(true)));
        this.<Button>addButton(new Button(this.width / 2 - 50, this.height / 6 + 96, 100, 20, this.copyButton, dlg -> {
            this.copyToClipboard();
            this.callback.accept(false);
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 50 + 105, this.height / 6 + 96, 100, 20, this.noButton, dlg -> this.callback.accept(false)));
    }
    
    public void copyToClipboard() {
        this.minecraft.keyboardHandler.setClipboard(this.url);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        super.render(dfj, integer2, integer3, float4);
        if (this.showWarning) {
            GuiComponent.drawCenteredString(dfj, this.font, this.warning, this.width / 2, 110, 16764108);
        }
    }
}
