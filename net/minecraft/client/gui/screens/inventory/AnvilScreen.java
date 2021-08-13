package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.function.Consumer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AnvilMenu;

public class AnvilScreen extends ItemCombinerScreen<AnvilMenu> {
    private static final ResourceLocation ANVIL_LOCATION;
    private static final Component TOO_EXPENSIVE_TEXT;
    private EditBox name;
    
    public AnvilScreen(final AnvilMenu bib, final Inventory bfs, final Component nr) {
        super(bib, bfs, nr, AnvilScreen.ANVIL_LOCATION);
        this.titleLabelX = 60;
    }
    
    @Override
    protected void subInit() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        final int integer2 = (this.width - this.imageWidth) / 2;
        final int integer3 = (this.height - this.imageHeight) / 2;
        (this.name = new EditBox(this.font, integer2 + 62, integer3 + 24, 103, 12, new TranslatableComponent("container.repair"))).setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(35);
        this.name.setResponder((Consumer<String>)this::onNameChanged);
        this.children.add(this.name);
        this.setInitialFocus(this.name);
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.name.getValue();
        this.init(djw, integer2, integer3);
        this.name.setValue(string5);
    }
    
    @Override
    public void removed() {
        super.removed();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.player.closeContainer();
        }
        return this.name.keyPressed(integer1, integer2, integer3) || this.name.canConsumeInput() || super.keyPressed(integer1, integer2, integer3);
    }
    
    private void onNameChanged(final String string) {
        if (string.isEmpty()) {
            return;
        }
        String string2 = string;
        final Slot bjo4 = ((AnvilMenu)this.menu).getSlot(0);
        if (bjo4 != null && bjo4.hasItem() && !bjo4.getItem().hasCustomHoverName() && string2.equals(bjo4.getItem().getHoverName().getString())) {
            string2 = "";
        }
        ((AnvilMenu)this.menu).setItemName(string2);
        this.minecraft.player.connection.send(new ServerboundRenameItemPacket(string2));
    }
    
    @Override
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        RenderSystem.disableBlend();
        super.renderLabels(dfj, integer2, integer3);
        final int integer4 = ((AnvilMenu)this.menu).getCost();
        if (integer4 > 0) {
            int integer5 = 8453920;
            Component nr7;
            if (integer4 >= 40 && !this.minecraft.player.abilities.instabuild) {
                nr7 = AnvilScreen.TOO_EXPENSIVE_TEXT;
                integer5 = 16736352;
            }
            else if (!((AnvilMenu)this.menu).getSlot(2).hasItem()) {
                nr7 = null;
            }
            else {
                nr7 = new TranslatableComponent("container.repair.cost", new Object[] { integer4 });
                if (!((AnvilMenu)this.menu).getSlot(2).mayPickup(this.inventory.player)) {
                    integer5 = 16736352;
                }
            }
            if (nr7 != null) {
                final int integer6 = this.imageWidth - 8 - this.font.width(nr7) - 2;
                final int integer7 = 69;
                GuiComponent.fill(dfj, integer6 - 2, 67, this.imageWidth - 8, 79, 1325400064);
                this.font.drawShadow(dfj, nr7, (float)integer6, 69.0f, integer5);
            }
        }
    }
    
    public void renderFg(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.name.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly) {
        if (integer == 0) {
            this.name.setValue(bly.isEmpty() ? "" : bly.getHoverName().getString());
            this.name.setEditable(!bly.isEmpty());
            this.setFocused(this.name);
        }
    }
    
    static {
        ANVIL_LOCATION = new ResourceLocation("textures/gui/container/anvil.png");
        TOO_EXPENSIVE_TEXT = new TranslatableComponent("container.repair.expensive");
    }
}
