package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;

public class BackupConfirmScreen extends Screen {
    @Nullable
    private final Screen lastScreen;
    protected final Listener listener;
    private final Component description;
    private final boolean promptForCacheErase;
    private MultiLineLabel message;
    private Checkbox eraseCache;
    
    public BackupConfirmScreen(@Nullable final Screen doq, final Listener a, final Component nr3, final Component nr4, final boolean boolean5) {
        super(nr3);
        this.message = MultiLineLabel.EMPTY;
        this.lastScreen = doq;
        this.listener = a;
        this.description = nr4;
        this.promptForCacheErase = boolean5;
    }
    
    @Override
    protected void init() {
        super.init();
        this.message = MultiLineLabel.create(this.font, this.description, this.width - 50);
        final int n = this.message.getLineCount() + 1;
        this.font.getClass();
        final int integer2 = n * 9;
        this.<Button>addButton(new Button(this.width / 2 - 155, 100 + integer2, 150, 20, new TranslatableComponent("selectWorld.backupJoinConfirmButton"), dlg -> this.listener.proceed(true, this.eraseCache.selected())));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, 100 + integer2, 150, 20, new TranslatableComponent("selectWorld.backupJoinSkipButton"), dlg -> this.listener.proceed(false, this.eraseCache.selected())));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 80, 124 + integer2, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.eraseCache = new Checkbox(this.width / 2 - 155 + 80, 76 + integer2, 150, 20, new TranslatableComponent("selectWorld.backupEraseCache"), false);
        if (this.promptForCacheErase) {
            this.<Checkbox>addButton(this.eraseCache);
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 50, 16777215);
        this.message.renderCentered(dfj, this.width / 2, 70);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    public interface Listener {
        void proceed(final boolean boolean1, final boolean boolean2);
    }
}
