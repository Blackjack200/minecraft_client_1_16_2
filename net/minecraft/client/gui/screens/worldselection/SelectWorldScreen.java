package net.minecraft.client.gui.screens.worldselection;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;

public class SelectWorldScreen extends Screen {
    protected final Screen lastScreen;
    private List<FormattedCharSequence> toolTip;
    private Button deleteButton;
    private Button selectButton;
    private Button renameButton;
    private Button copyButton;
    protected EditBox searchBox;
    private WorldSelectionList list;
    
    public SelectWorldScreen(final Screen doq) {
        super(new TranslatableComponent("selectWorld.title"));
        this.lastScreen = doq;
    }
    
    @Override
    public boolean mouseScrolled(final double double1, final double double2, final double double3) {
        return super.mouseScrolled(double1, double2, double3);
    }
    
    @Override
    public void tick() {
        this.searchBox.tick();
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        (this.searchBox = new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, new TranslatableComponent("selectWorld.search"))).setResponder((Consumer<String>)(string -> this.list.refreshList((Supplier<String>)(() -> string), false)));
        this.list = new WorldSelectionList(this, this.minecraft, this.width, this.height, 48, this.height - 64, 36, (Supplier<String>)(() -> this.searchBox.getValue()), this.list);
        this.children.add(this.searchBox);
        this.children.add(this.list);
        this.selectButton = this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 52, 150, 20, new TranslatableComponent("selectWorld.select"), dlg -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::joinWorld)));
        this.<Button>addButton(new Button(this.width / 2 + 4, this.height - 52, 150, 20, new TranslatableComponent("selectWorld.create"), dlg -> this.minecraft.setScreen(CreateWorldScreen.create(this))));
        this.renameButton = this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 28, 72, 20, new TranslatableComponent("selectWorld.edit"), dlg -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld)));
        this.deleteButton = this.<Button>addButton(new Button(this.width / 2 - 76, this.height - 28, 72, 20, new TranslatableComponent("selectWorld.delete"), dlg -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld)));
        this.copyButton = this.<Button>addButton(new Button(this.width / 2 + 4, this.height - 28, 72, 20, new TranslatableComponent("selectWorld.recreate"), dlg -> this.list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld)));
        this.<Button>addButton(new Button(this.width / 2 + 82, this.height - 28, 72, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.updateButtonStatus(false);
        this.setInitialFocus(this.searchBox);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        return super.keyPressed(integer1, integer2, integer3) || this.searchBox.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        return this.searchBox.charTyped(character, integer);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.list.render(dfj, integer2, integer3, float4);
        this.searchBox.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 8, 16777215);
        super.render(dfj, integer2, integer3, float4);
        if (this.toolTip != null) {
            this.renderTooltip(dfj, this.toolTip, integer2, integer3);
        }
    }
    
    public void setToolTip(final List<FormattedCharSequence> list) {
        this.toolTip = list;
    }
    
    public void updateButtonStatus(final boolean boolean1) {
        this.selectButton.active = boolean1;
        this.deleteButton.active = boolean1;
        this.renameButton.active = boolean1;
        this.copyButton.active = boolean1;
    }
    
    @Override
    public void removed() {
        if (this.list != null) {
            this.list.children().forEach(WorldSelectionList.WorldListEntry::close);
        }
    }
}
