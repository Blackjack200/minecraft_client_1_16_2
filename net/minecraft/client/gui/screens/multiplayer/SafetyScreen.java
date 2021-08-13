package net.minecraft.client.gui.screens.multiplayer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class SafetyScreen extends Screen {
    private final Screen previous;
    private static final Component TITLE;
    private static final Component CONTENT;
    private static final Component CHECK;
    private static final Component NARRATION;
    private Checkbox stopShowing;
    private MultiLineLabel message;
    
    public SafetyScreen(final Screen doq) {
        super(NarratorChatListener.NO_TITLE);
        this.message = MultiLineLabel.EMPTY;
        this.previous = doq;
    }
    
    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, SafetyScreen.CONTENT, this.width - 50);
        final int n = this.message.getLineCount() + 1;
        this.font.getClass();
        final int integer2 = n * 9;
        this.<Button>addButton(new Button(this.width / 2 - 155, 100 + integer2, 150, 20, CommonComponents.GUI_PROCEED, dlg -> {
            if (this.stopShowing.selected()) {
                this.minecraft.options.skipMultiplayerWarning = true;
                this.minecraft.options.save();
            }
            this.minecraft.setScreen(new JoinMultiplayerScreen(this.previous));
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, 100 + integer2, 150, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.previous)));
        this.<Checkbox>addButton(this.stopShowing = new Checkbox(this.width / 2 - 155 + 80, 76 + integer2, 150, 20, SafetyScreen.CHECK, false));
    }
    
    @Override
    public String getNarrationMessage() {
        return SafetyScreen.NARRATION.getString();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderDirtBackground(0);
        GuiComponent.drawCenteredString(dfj, this.font, SafetyScreen.TITLE, this.width / 2, 30, 16777215);
        this.message.renderCentered(dfj, this.width / 2, 70);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        TITLE = new TranslatableComponent("multiplayerWarning.header").withStyle(ChatFormatting.BOLD);
        CONTENT = new TranslatableComponent("multiplayerWarning.message");
        CHECK = new TranslatableComponent("multiplayerWarning.check");
        NARRATION = SafetyScreen.TITLE.copy().append("\n").append(SafetyScreen.CONTENT);
    }
}
