package net.minecraft.client.gui.screens.inventory;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.HorseInventoryMenu;

public class HorseInventoryScreen extends AbstractContainerScreen<HorseInventoryMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION;
    private final AbstractHorse horse;
    private float xMouse;
    private float yMouse;
    
    public HorseInventoryScreen(final HorseInventoryMenu biv, final Inventory bfs, final AbstractHorse bay) {
        super(biv, bfs, bay.getDisplayName());
        this.horse = bay;
        this.passEvents = false;
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(HorseInventoryScreen.HORSE_INVENTORY_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        if (this.horse instanceof AbstractChestedHorse) {
            final AbstractChestedHorse bax8 = (AbstractChestedHorse)this.horse;
            if (bax8.hasChest()) {
                this.blit(dfj, integer5 + 79, integer6 + 17, 0, this.imageHeight, bax8.getInventoryColumns() * 18, 54);
            }
        }
        if (this.horse.isSaddleable()) {
            this.blit(dfj, integer5 + 7, integer6 + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }
        if (this.horse.canWearArmor()) {
            if (this.horse instanceof Llama) {
                this.blit(dfj, integer5 + 7, integer6 + 35, 36, this.imageHeight + 54, 18, 18);
            }
            else {
                this.blit(dfj, integer5 + 7, integer6 + 35, 0, this.imageHeight + 54, 18, 18);
            }
        }
        InventoryScreen.renderEntityInInventory(integer5 + 51, integer6 + 60, 17, integer5 + 51 - this.xMouse, integer6 + 75 - 50 - this.yMouse, this.horse);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.xMouse = (float)integer2;
        this.yMouse = (float)integer3;
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    static {
        HORSE_INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/horse.png");
    }
}
