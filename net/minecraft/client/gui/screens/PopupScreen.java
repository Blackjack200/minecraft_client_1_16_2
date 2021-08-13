package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.MultiLineLabel;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.FormattedText;

public class PopupScreen extends Screen {
    private final FormattedText message;
    private final ImmutableList<ButtonOption> buttonOptions;
    private MultiLineLabel messageLines;
    private int contentTop;
    private int buttonWidth;
    
    protected PopupScreen(final Component nr, final List<FormattedText> list, final ImmutableList<ButtonOption> immutableList) {
        super(nr);
        this.messageLines = MultiLineLabel.EMPTY;
        this.message = FormattedText.composite(list);
        this.buttonOptions = immutableList;
    }
    
    @Override
    public String getNarrationMessage() {
        return super.getNarrationMessage() + ". " + this.message.getString();
    }
    
    @Override
    public void init(final Minecraft djw, final int integer2, final int integer3) {
        super.init(djw, integer2, integer3);
        for (final ButtonOption a6 : this.buttonOptions) {
            this.buttonWidth = Math.max(this.buttonWidth, 20 + this.font.width(a6.message) + 20);
        }
        final int integer4 = 5 + this.buttonWidth + 5;
        final int integer5 = integer4 * this.buttonOptions.size();
        this.messageLines = MultiLineLabel.create(this.font, this.message, integer5);
        final int lineCount = this.messageLines.getLineCount();
        this.font.getClass();
        final int integer6 = lineCount * 9;
        this.contentTop = (int)(integer3 / 2.0 - integer6 / 2.0);
        final int n = this.contentTop + integer6;
        this.font.getClass();
        final int integer7 = n + 9 * 2;
        int integer8 = (int)(integer2 / 2.0 - integer5 / 2.0);
        for (final ButtonOption a7 : this.buttonOptions) {
            this.<Button>addButton(new Button(integer8, integer7, this.buttonWidth, 20, a7.message, a7.onPress));
            integer8 += integer4;
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderDirtBackground(0);
        final Font font = this.font;
        final Component title = this.title;
        final int integer4 = this.width / 2;
        final int contentTop = this.contentTop;
        this.font.getClass();
        GuiComponent.drawCenteredString(dfj, font, title, integer4, contentTop - 9 * 2, -1);
        this.messageLines.renderCentered(dfj, this.width / 2, this.contentTop);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    public static final class ButtonOption {
        private final Component message;
        private final Button.OnPress onPress;
        
        public ButtonOption(final Component nr, final Button.OnPress a) {
            this.message = nr;
            this.onPress = a;
        }
    }
}
