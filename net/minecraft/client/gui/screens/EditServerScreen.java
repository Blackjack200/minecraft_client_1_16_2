package net.minecraft.client.gui.screens;

import java.net.IDN;
import net.minecraft.util.StringUtil;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import java.util.function.Consumer;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ServerData;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class EditServerScreen extends Screen {
    private static final Component NAME_LABEL;
    private static final Component IP_LABEL;
    private Button addButton;
    private final BooleanConsumer callback;
    private final ServerData serverData;
    private EditBox ipEdit;
    private EditBox nameEdit;
    private Button serverPackButton;
    private final Screen lastScreen;
    private final Predicate<String> addressFilter;
    
    public EditServerScreen(final Screen doq, final BooleanConsumer booleanConsumer, final ServerData dwr) {
        super(new TranslatableComponent("addServer.title"));
        this.addressFilter = (Predicate<String>)(string -> {
            if (StringUtil.isNullOrEmpty(string)) {
                return true;
            }
            final String[] arr2 = string.split(":");
            if (arr2.length == 0) {
                return true;
            }
            try {
                final String string2 = IDN.toASCII(arr2[0]);
                return true;
            }
            catch (IllegalArgumentException illegalArgumentException3) {
                return false;
            }
        });
        this.lastScreen = doq;
        this.callback = booleanConsumer;
        this.serverData = dwr;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.ipEdit.tick();
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        (this.nameEdit = new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, new TranslatableComponent("addServer.enterName"))).setFocus(true);
        this.nameEdit.setValue(this.serverData.name);
        this.nameEdit.setResponder((Consumer<String>)this::onEdited);
        this.children.add(this.nameEdit);
        (this.ipEdit = new EditBox(this.font, this.width / 2 - 100, 106, 200, 20, new TranslatableComponent("addServer.enterIp"))).setMaxLength(128);
        this.ipEdit.setValue(this.serverData.ip);
        this.ipEdit.setFilter(this.addressFilter);
        this.ipEdit.setResponder((Consumer<String>)this::onEdited);
        this.children.add(this.ipEdit);
        this.serverPackButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, createServerButtonText(this.serverData.getResourcePackStatus()), dlg -> {
            this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.values()[(this.serverData.getResourcePackStatus().ordinal() + 1) % ServerData.ServerPackStatus.values().length]);
            this.serverPackButton.setMessage(createServerButtonText(this.serverData.getResourcePackStatus()));
            return;
        }));
        this.addButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20, new TranslatableComponent("addServer.add"), dlg -> this.onAdd()));
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 18, 200, 20, CommonComponents.GUI_CANCEL, dlg -> this.callback.accept(false)));
        this.cleanUp();
    }
    
    private static Component createServerButtonText(final ServerData.ServerPackStatus a) {
        return new TranslatableComponent("addServer.resourcePack").append(": ").append(a.getName());
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.ipEdit.getValue();
        final String string6 = this.nameEdit.getValue();
        this.init(djw, integer2, integer3);
        this.ipEdit.setValue(string5);
        this.nameEdit.setValue(string6);
    }
    
    private void onEdited(final String string) {
        this.cleanUp();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void onAdd() {
        this.serverData.name = this.nameEdit.getValue();
        this.serverData.ip = this.ipEdit.getValue();
        this.callback.accept(true);
    }
    
    @Override
    public void onClose() {
        this.cleanUp();
        this.minecraft.setScreen(this.lastScreen);
    }
    
    private void cleanUp() {
        final String string2 = this.ipEdit.getValue();
        final boolean boolean3 = !string2.isEmpty() && string2.split(":").length > 0 && string2.indexOf(32) == -1;
        this.addButton.active = (boolean3 && !this.nameEdit.getValue().isEmpty());
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 17, 16777215);
        GuiComponent.drawString(dfj, this.font, EditServerScreen.NAME_LABEL, this.width / 2 - 100, 53, 10526880);
        GuiComponent.drawString(dfj, this.font, EditServerScreen.IP_LABEL, this.width / 2 - 100, 94, 10526880);
        this.nameEdit.render(dfj, integer2, integer3, float4);
        this.ipEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        NAME_LABEL = new TranslatableComponent("addServer.enterName");
        IP_LABEL = new TranslatableComponent("addServer.enterIp");
    }
}
