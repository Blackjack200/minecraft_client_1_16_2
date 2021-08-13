package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.function.Consumer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class DirectJoinServerScreen extends Screen {
    private static final Component ENTER_IP_LABEL;
    private Button selectButton;
    private final ServerData serverData;
    private EditBox ipEdit;
    private final BooleanConsumer callback;
    private final Screen lastScreen;
    
    public DirectJoinServerScreen(final Screen doq, final BooleanConsumer booleanConsumer, final ServerData dwr) {
        super(new TranslatableComponent("selectServer.direct"));
        this.lastScreen = doq;
        this.serverData = dwr;
        this.callback = booleanConsumer;
    }
    
    @Override
    public void tick() {
        this.ipEdit.tick();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.getFocused() == this.ipEdit && (integer1 == 257 || integer1 == 335)) {
            this.onSelect();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.selectButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 12, 200, 20, new TranslatableComponent("selectServer.select"), dlg -> this.onSelect()));
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, CommonComponents.GUI_CANCEL, dlg -> this.callback.accept(false)));
        (this.ipEdit = new EditBox(this.font, this.width / 2 - 100, 116, 200, 20, new TranslatableComponent("addServer.enterIp"))).setMaxLength(128);
        this.ipEdit.setFocus(true);
        this.ipEdit.setValue(this.minecraft.options.lastMpIp);
        this.ipEdit.setResponder((Consumer<String>)(string -> this.updateSelectButtonStatus()));
        this.children.add(this.ipEdit);
        this.setInitialFocus(this.ipEdit);
        this.updateSelectButtonStatus();
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.ipEdit.getValue();
        this.init(djw, integer2, integer3);
        this.ipEdit.setValue(string5);
    }
    
    private void onSelect() {
        this.serverData.ip = this.ipEdit.getValue();
        this.callback.accept(true);
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        this.minecraft.options.lastMpIp = this.ipEdit.getValue();
        this.minecraft.options.save();
    }
    
    private void updateSelectButtonStatus() {
        final String string2 = this.ipEdit.getValue();
        this.selectButton.active = (!string2.isEmpty() && string2.split(":").length > 0 && string2.indexOf(32) == -1);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        GuiComponent.drawString(dfj, this.font, DirectJoinServerScreen.ENTER_IP_LABEL, this.width / 2 - 100, 100, 10526880);
        this.ipEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        ENTER_IP_LABEL = new TranslatableComponent("addServer.enterIp");
    }
}
