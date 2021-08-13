package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.MultiLineLabel;

public class DatapackLoadFailureScreen extends Screen {
    private MultiLineLabel message;
    private final Runnable callback;
    
    public DatapackLoadFailureScreen(final Runnable runnable) {
        super(new TranslatableComponent("datapackFailure.title"));
        this.message = MultiLineLabel.EMPTY;
        this.callback = runnable;
    }
    
    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, this.getTitle(), this.width - 50);
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, new TranslatableComponent("datapackFailure.safeMode"), dlg -> this.callback.run()));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, new TranslatableComponent("gui.toTitle"), dlg -> this.minecraft.setScreen(null)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.message.renderCentered(dfj, this.width / 2, 70);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
