package net.minecraft.client.gui.screens;

import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.OptionsList;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.Option;

public abstract class SimpleOptionsSubScreen extends OptionsSubScreen {
    private final Option[] smallOptions;
    @Nullable
    private AbstractWidget narratorButton;
    private OptionsList list;
    
    public SimpleOptionsSubScreen(final Screen doq, final Options dka, final Component nr, final Option[] arr) {
        super(doq, dka, nr);
        this.smallOptions = arr;
    }
    
    @Override
    protected void init() {
        (this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25)).addSmall(this.smallOptions);
        this.children.add(this.list);
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.narratorButton = this.list.findOption(Option.NARRATOR);
        if (this.narratorButton != null) {
            this.narratorButton.active = NarratorChatListener.INSTANCE.isActive();
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.list.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(dfj, integer2, integer3, float4);
        final List<FormattedCharSequence> list6 = OptionsSubScreen.tooltipAt(this.list, integer2, integer3);
        if (list6 != null) {
            this.renderTooltip(dfj, list6, integer2, integer3);
        }
    }
    
    public void updateNarratorButton() {
        if (this.narratorButton != null) {
            this.narratorButton.setMessage(Option.NARRATOR.getMessage(this.options));
        }
    }
}
