package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.BeaconMenu;

public class BeaconScreen extends AbstractContainerScreen<BeaconMenu> {
    private static final ResourceLocation BEACON_LOCATION;
    private static final Component PRIMARY_EFFECT_LABEL;
    private static final Component SECONDARY_EFFECT_LABEL;
    private BeaconConfirmButton confirmButton;
    private boolean initPowerButtons;
    private MobEffect primary;
    private MobEffect secondary;
    
    public BeaconScreen(final BeaconMenu bic, final Inventory bfs, final Component nr) {
        super(bic, bfs, nr);
        this.imageWidth = 230;
        this.imageHeight = 219;
        bic.addSlotListener(new ContainerListener() {
            public void refreshContainer(final AbstractContainerMenu bhz, final NonNullList<ItemStack> gj) {
            }
            
            public void slotChanged(final AbstractContainerMenu bhz, final int integer, final ItemStack bly) {
            }
            
            public void setContainerData(final AbstractContainerMenu bhz, final int integer2, final int integer3) {
                BeaconScreen.this.primary = bic.getPrimaryEffect();
                BeaconScreen.this.secondary = bic.getSecondaryEffect();
                BeaconScreen.this.initPowerButtons = true;
            }
        });
    }
    
    @Override
    protected void init() {
        super.init();
        this.confirmButton = this.<BeaconConfirmButton>addButton(new BeaconConfirmButton(this.leftPos + 164, this.topPos + 107));
        this.<BeaconCancelButton>addButton(new BeaconCancelButton(this.leftPos + 190, this.topPos + 107));
        this.initPowerButtons = true;
        this.confirmButton.active = false;
    }
    
    @Override
    public void tick() {
        super.tick();
        final int integer2 = ((BeaconMenu)this.menu).getLevels();
        if (this.initPowerButtons && integer2 >= 0) {
            this.initPowerButtons = false;
            for (int integer3 = 0; integer3 <= 2; ++integer3) {
                final int integer4 = BeaconBlockEntity.BEACON_EFFECTS[integer3].length;
                final int integer5 = integer4 * 22 + (integer4 - 1) * 2;
                for (int integer6 = 0; integer6 < integer4; ++integer6) {
                    final MobEffect app7 = BeaconBlockEntity.BEACON_EFFECTS[integer3][integer6];
                    final BeaconPowerButton c8 = new BeaconPowerButton(this.leftPos + 76 + integer6 * 24 - integer5 / 2, this.topPos + 22 + integer3 * 25, app7, true);
                    this.<BeaconPowerButton>addButton(c8);
                    if (integer3 >= integer2) {
                        c8.active = false;
                    }
                    else if (app7 == this.primary) {
                        c8.setSelected(true);
                    }
                }
            }
            int integer3 = 3;
            final int integer4 = BeaconBlockEntity.BEACON_EFFECTS[3].length + 1;
            final int integer5 = integer4 * 22 + (integer4 - 1) * 2;
            for (int integer6 = 0; integer6 < integer4 - 1; ++integer6) {
                final MobEffect app7 = BeaconBlockEntity.BEACON_EFFECTS[3][integer6];
                final BeaconPowerButton c8 = new BeaconPowerButton(this.leftPos + 167 + integer6 * 24 - integer5 / 2, this.topPos + 47, app7, false);
                this.<BeaconPowerButton>addButton(c8);
                if (3 >= integer2) {
                    c8.active = false;
                }
                else if (app7 == this.secondary) {
                    c8.setSelected(true);
                }
            }
            if (this.primary != null) {
                final BeaconPowerButton c9 = new BeaconPowerButton(this.leftPos + 167 + (integer4 - 1) * 24 - integer5 / 2, this.topPos + 47, this.primary, false);
                this.<BeaconPowerButton>addButton(c9);
                if (3 >= integer2) {
                    c9.active = false;
                }
                else if (this.primary == this.secondary) {
                    c9.setSelected(true);
                }
            }
        }
        this.confirmButton.active = (((BeaconMenu)this.menu).hasPayment() && this.primary != null);
    }
    
    @Override
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        GuiComponent.drawCenteredString(dfj, this.font, BeaconScreen.PRIMARY_EFFECT_LABEL, 62, 10, 14737632);
        GuiComponent.drawCenteredString(dfj, this.font, BeaconScreen.SECONDARY_EFFECT_LABEL, 169, 10, 14737632);
        for (final AbstractWidget dle6 : this.buttons) {
            if (dle6.isHovered()) {
                dle6.renderToolTip(dfj, integer2 - this.leftPos, integer3 - this.topPos);
                break;
            }
        }
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(BeaconScreen.BEACON_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        this.itemRenderer.blitOffset = 100.0f;
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.NETHERITE_INGOT), integer5 + 20, integer6 + 109);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.EMERALD), integer5 + 41, integer6 + 109);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.DIAMOND), integer5 + 41 + 22, integer6 + 109);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.GOLD_INGOT), integer5 + 42 + 44, integer6 + 109);
        this.itemRenderer.renderAndDecorateItem(new ItemStack(Items.IRON_INGOT), integer5 + 42 + 66, integer6 + 109);
        this.itemRenderer.blitOffset = 0.0f;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    static {
        BEACON_LOCATION = new ResourceLocation("textures/gui/container/beacon.png");
        PRIMARY_EFFECT_LABEL = new TranslatableComponent("block.minecraft.beacon.primary");
        SECONDARY_EFFECT_LABEL = new TranslatableComponent("block.minecraft.beacon.secondary");
    }
    
    abstract static class BeaconScreenButton extends AbstractButton {
        private boolean selected;
        
        protected BeaconScreenButton(final int integer1, final int integer2) {
            super(integer1, integer2, 22, 22, TextComponent.EMPTY);
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            Minecraft.getInstance().getTextureManager().bind(BeaconScreen.BEACON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            final int integer4 = 219;
            int integer5 = 0;
            if (!this.active) {
                integer5 += this.width * 2;
            }
            else if (this.selected) {
                integer5 += this.width * 1;
            }
            else if (this.isHovered()) {
                integer5 += this.width * 3;
            }
            this.blit(dfj, this.x, this.y, integer5, 219, this.width, this.height);
            this.renderIcon(dfj);
        }
        
        protected abstract void renderIcon(final PoseStack dfj);
        
        public boolean isSelected() {
            return this.selected;
        }
        
        public void setSelected(final boolean boolean1) {
            this.selected = boolean1;
        }
    }
    
    class BeaconPowerButton extends BeaconScreenButton {
        private final MobEffect effect;
        private final TextureAtlasSprite sprite;
        private final boolean isPrimary;
        private final Component tooltip;
        
        public BeaconPowerButton(final int integer2, final int integer3, final MobEffect app, final boolean boolean5) {
            super(integer2, integer3);
            this.effect = app;
            this.sprite = Minecraft.getInstance().getMobEffectTextures().get(app);
            this.isPrimary = boolean5;
            this.tooltip = this.createTooltip(app, boolean5);
        }
        
        private Component createTooltip(final MobEffect app, final boolean boolean2) {
            final MutableComponent nx4 = new TranslatableComponent(app.getDescriptionId());
            if (!boolean2 && app != MobEffects.REGENERATION) {
                nx4.append(" II");
            }
            return nx4;
        }
        
        @Override
        public void onPress() {
            if (this.isSelected()) {
                return;
            }
            if (this.isPrimary) {
                BeaconScreen.this.primary = this.effect;
            }
            else {
                BeaconScreen.this.secondary = this.effect;
            }
            BeaconScreen.this.buttons.clear();
            BeaconScreen.this.children.clear();
            BeaconScreen.this.init();
            BeaconScreen.this.tick();
        }
        
        @Override
        public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
            BeaconScreen.this.renderTooltip(dfj, this.tooltip, integer2, integer3);
        }
        
        @Override
        protected void renderIcon(final PoseStack dfj) {
            Minecraft.getInstance().getTextureManager().bind(this.sprite.atlas().location());
            GuiComponent.blit(dfj, this.x + 2, this.y + 2, this.getBlitOffset(), 18, 18, this.sprite);
        }
    }
    
    abstract static class BeaconSpriteScreenButton extends BeaconScreenButton {
        private final int iconX;
        private final int iconY;
        
        protected BeaconSpriteScreenButton(final int integer1, final int integer2, final int integer3, final int integer4) {
            super(integer1, integer2);
            this.iconX = integer3;
            this.iconY = integer4;
        }
        
        @Override
        protected void renderIcon(final PoseStack dfj) {
            this.blit(dfj, this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
        }
    }
    
    class BeaconConfirmButton extends BeaconSpriteScreenButton {
        public BeaconConfirmButton(final int integer2, final int integer3) {
            super(integer2, integer3, 90, 220);
        }
        
        @Override
        public void onPress() {
            BeaconScreen.this.minecraft.getConnection().send(new ServerboundSetBeaconPacket(MobEffect.getId(BeaconScreen.this.primary), MobEffect.getId(BeaconScreen.this.secondary)));
            BeaconScreen.this.minecraft.player.connection.send(new ServerboundContainerClosePacket(BeaconScreen.this.minecraft.player.containerMenu.containerId));
            BeaconScreen.this.minecraft.setScreen(null);
        }
        
        @Override
        public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
            BeaconScreen.this.renderTooltip(dfj, CommonComponents.GUI_DONE, integer2, integer3);
        }
    }
    
    class BeaconCancelButton extends BeaconSpriteScreenButton {
        public BeaconCancelButton(final int integer2, final int integer3) {
            super(integer2, integer3, 112, 220);
        }
        
        @Override
        public void onPress() {
            BeaconScreen.this.minecraft.player.connection.send(new ServerboundContainerClosePacket(BeaconScreen.this.minecraft.player.containerMenu.containerId));
            BeaconScreen.this.minecraft.setScreen(null);
        }
        
        @Override
        public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
            BeaconScreen.this.renderTooltip(dfj, CommonComponents.GUI_CANCEL, integer2, integer3);
        }
    }
}
