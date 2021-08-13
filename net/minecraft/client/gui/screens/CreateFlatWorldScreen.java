package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.world.item.Items;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import java.util.List;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;

public class CreateFlatWorldScreen extends Screen {
    protected final CreateWorldScreen parent;
    private final Consumer<FlatLevelGeneratorSettings> applySettings;
    private FlatLevelGeneratorSettings generator;
    private Component columnType;
    private Component columnHeight;
    private DetailsList list;
    private Button deleteLayerButton;
    
    public CreateFlatWorldScreen(final CreateWorldScreen drx, final Consumer<FlatLevelGeneratorSettings> consumer, final FlatLevelGeneratorSettings cpc) {
        super(new TranslatableComponent("createWorld.customize.flat.title"));
        this.parent = drx;
        this.applySettings = consumer;
        this.generator = cpc;
    }
    
    public FlatLevelGeneratorSettings settings() {
        return this.generator;
    }
    
    public void setConfig(final FlatLevelGeneratorSettings cpc) {
        this.generator = cpc;
    }
    
    @Override
    protected void init() {
        this.columnType = new TranslatableComponent("createWorld.customize.flat.tile");
        this.columnHeight = new TranslatableComponent("createWorld.customize.flat.height");
        this.list = new DetailsList();
        this.children.add(this.list);
        List<FlatLayerInfo> list3;
        int integer4;
        int integer5;
        this.deleteLayerButton = this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 52, 150, 20, new TranslatableComponent("createWorld.customize.flat.removeLayer"), dlg -> {
            if (!this.hasValidSelection()) {
                return;
            }
            else {
                list3 = this.generator.getLayersInfo();
                integer4 = this.list.children().indexOf(((AbstractSelectionList<Object>)this.list).getSelected());
                integer5 = list3.size() - integer4 - 1;
                list3.remove(integer5);
                this.list.setSelected(list3.isEmpty() ? null : ((DetailsList.Entry)this.list.children().get(Math.min(integer4, list3.size() - 1))));
                this.generator.updateLayers();
                this.list.resetRows();
                this.updateButtonValidity();
                return;
            }
        }));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height - 52, 150, 20, new TranslatableComponent("createWorld.customize.presets"), dlg -> {
            this.minecraft.setScreen(new PresetFlatWorldScreen(this));
            this.generator.updateLayers();
            this.updateButtonValidity();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, CommonComponents.GUI_DONE, dlg -> {
            this.applySettings.accept(this.generator);
            this.minecraft.setScreen(this.parent);
            this.generator.updateLayers();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, CommonComponents.GUI_CANCEL, dlg -> {
            this.minecraft.setScreen(this.parent);
            this.generator.updateLayers();
            return;
        }));
        this.generator.updateLayers();
        this.updateButtonValidity();
    }
    
    private void updateButtonValidity() {
        this.deleteLayerButton.active = this.hasValidSelection();
    }
    
    private boolean hasValidSelection() {
        return this.list.getSelected() != null;
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.list.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 8, 16777215);
        final int integer4 = this.width / 2 - 92 - 16;
        GuiComponent.drawString(dfj, this.font, this.columnType, integer4, 32, 16777215);
        GuiComponent.drawString(dfj, this.font, this.columnHeight, integer4 + 2 + 213 - this.font.width(this.columnHeight), 32, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    class DetailsList extends ObjectSelectionList<Entry> {
        public DetailsList() {
            super(CreateFlatWorldScreen.this.minecraft, CreateFlatWorldScreen.this.width, CreateFlatWorldScreen.this.height, 43, CreateFlatWorldScreen.this.height - 60, 24);
            for (int integer3 = 0; integer3 < CreateFlatWorldScreen.this.generator.getLayersInfo().size(); ++integer3) {
                this.addEntry(new Entry());
            }
        }
        
        @Override
        public void setSelected(@Nullable final Entry a) {
            super.setSelected(a);
            if (a != null) {
                final FlatLayerInfo cpb3 = (FlatLayerInfo)CreateFlatWorldScreen.this.generator.getLayersInfo().get(CreateFlatWorldScreen.this.generator.getLayersInfo().size() - this.children().indexOf(a) - 1);
                final Item blu4 = cpb3.getBlockState().getBlock().asItem();
                if (blu4 != Items.AIR) {
                    NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.select", new Object[] { blu4.getName(new ItemStack(blu4)) }).getString());
                }
            }
            CreateFlatWorldScreen.this.updateButtonValidity();
        }
        
        @Override
        protected boolean isFocused() {
            return CreateFlatWorldScreen.this.getFocused() == this;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.width - 70;
        }
        
        public void resetRows() {
            final int integer2 = this.children().indexOf(((AbstractSelectionList<Object>)this).getSelected());
            this.clearEntries();
            for (int integer3 = 0; integer3 < CreateFlatWorldScreen.this.generator.getLayersInfo().size(); ++integer3) {
                this.addEntry(new Entry());
            }
            final List<Entry> list3 = this.children();
            if (integer2 >= 0 && integer2 < list3.size()) {
                this.setSelected((Entry)list3.get(integer2));
            }
        }
        
        class Entry extends ObjectSelectionList.Entry<Entry> {
            private Entry() {
            }
            
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                final FlatLayerInfo cpb12 = (FlatLayerInfo)CreateFlatWorldScreen.this.generator.getLayersInfo().get(CreateFlatWorldScreen.this.generator.getLayersInfo().size() - integer2 - 1);
                final BlockState cee13 = cpb12.getBlockState();
                Item blu14 = cee13.getBlock().asItem();
                if (blu14 == Items.AIR) {
                    if (cee13.is(Blocks.WATER)) {
                        blu14 = Items.WATER_BUCKET;
                    }
                    else if (cee13.is(Blocks.LAVA)) {
                        blu14 = Items.LAVA_BUCKET;
                    }
                }
                final ItemStack bly15 = new ItemStack(blu14);
                this.blitSlot(dfj, integer4, integer3, bly15);
                CreateFlatWorldScreen.this.font.draw(dfj, blu14.getName(bly15), (float)(integer4 + 18 + 5), (float)(integer3 + 3), 16777215);
                String string16;
                if (integer2 == 0) {
                    string16 = I18n.get("createWorld.customize.flat.layer.top", cpb12.getHeight());
                }
                else if (integer2 == CreateFlatWorldScreen.this.generator.getLayersInfo().size() - 1) {
                    string16 = I18n.get("createWorld.customize.flat.layer.bottom", cpb12.getHeight());
                }
                else {
                    string16 = I18n.get("createWorld.customize.flat.layer", cpb12.getHeight());
                }
                CreateFlatWorldScreen.this.font.draw(dfj, string16, (float)(integer4 + 2 + 213 - CreateFlatWorldScreen.this.font.width(string16)), (float)(integer3 + 3), 16777215);
            }
            
            public boolean mouseClicked(final double double1, final double double2, final int integer) {
                if (integer == 0) {
                    DetailsList.this.setSelected(this);
                    return true;
                }
                return false;
            }
            
            private void blitSlot(final PoseStack dfj, final int integer2, final int integer3, final ItemStack bly) {
                this.blitSlotBg(dfj, integer2 + 1, integer3 + 1);
                RenderSystem.enableRescaleNormal();
                if (!bly.isEmpty()) {
                    CreateFlatWorldScreen.this.itemRenderer.renderGuiItem(bly, integer2 + 2, integer3 + 2);
                }
                RenderSystem.disableRescaleNormal();
            }
            
            private void blitSlotBg(final PoseStack dfj, final int integer2, final int integer3) {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                DetailsList.this.minecraft.getTextureManager().bind(GuiComponent.STATS_ICON_LOCATION);
                GuiComponent.blit(dfj, integer2, integer3, CreateFlatWorldScreen.this.getBlitOffset(), 0.0f, 0.0f, 18, 18, 128, 128);
            }
        }
    }
}
