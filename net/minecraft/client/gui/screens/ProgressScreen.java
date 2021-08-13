package net.minecraft.client.gui.screens;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProgressListener;

public class ProgressScreen extends Screen implements ProgressListener {
    @Nullable
    private Component header;
    @Nullable
    private Component stage;
    private int progress;
    private boolean stop;
    
    public ProgressScreen() {
        super(NarratorChatListener.NO_TITLE);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public void progressStartNoAbort(final Component nr) {
        this.progressStart(nr);
    }
    
    @Override
    public void progressStart(final Component nr) {
        this.header = nr;
        this.progressStage(new TranslatableComponent("progress.working"));
    }
    
    @Override
    public void progressStage(final Component nr) {
        this.stage = nr;
        this.progressStagePercentage(0);
    }
    
    @Override
    public void progressStagePercentage(final int integer) {
        this.progress = integer;
    }
    
    @Override
    public void stop() {
        this.stop = true;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.stop) {
            if (!this.minecraft.isConnectedToRealms()) {
                this.minecraft.setScreen(null);
            }
            return;
        }
        this.renderBackground(dfj);
        if (this.header != null) {
            GuiComponent.drawCenteredString(dfj, this.font, this.header, this.width / 2, 70, 16777215);
        }
        if (this.stage != null && this.progress != 0) {
            GuiComponent.drawCenteredString(dfj, this.font, new TextComponent("").append(this.stage).append(new StringBuilder().append(" ").append(this.progress).append("%").toString()), this.width / 2, 90, 16777215);
        }
        super.render(dfj, integer2, integer3, float4);
    }
}
