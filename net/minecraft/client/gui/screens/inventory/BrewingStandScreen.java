package net.minecraft.client.gui.screens.inventory;

import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.BrewingStandMenu;

public class BrewingStandScreen extends AbstractContainerScreen<BrewingStandMenu> {
    private static final ResourceLocation BREWING_STAND_LOCATION;
    private static final int[] BUBBLELENGTHS;
    
    public BrewingStandScreen(final BrewingStandMenu bie, final Inventory bfs, final Component nr) {
        super(bie, bfs, nr);
    }
    
    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        this.renderTooltip(dfj, integer2, integer3);
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final float float2, final int integer3, final int integer4) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(BrewingStandScreen.BREWING_STAND_LOCATION);
        final int integer5 = (this.width - this.imageWidth) / 2;
        final int integer6 = (this.height - this.imageHeight) / 2;
        this.blit(dfj, integer5, integer6, 0, 0, this.imageWidth, this.imageHeight);
        final int integer7 = ((BrewingStandMenu)this.menu).getFuel();
        final int integer8 = Mth.clamp((18 * integer7 + 20 - 1) / 20, 0, 18);
        if (integer8 > 0) {
            this.blit(dfj, integer5 + 60, integer6 + 44, 176, 29, integer8, 4);
        }
        final int integer9 = ((BrewingStandMenu)this.menu).getBrewingTicks();
        if (integer9 > 0) {
            int integer10 = (int)(28.0f * (1.0f - integer9 / 400.0f));
            if (integer10 > 0) {
                this.blit(dfj, integer5 + 97, integer6 + 16, 176, 0, 9, integer10);
            }
            integer10 = BrewingStandScreen.BUBBLELENGTHS[integer9 / 2 % 7];
            if (integer10 > 0) {
                this.blit(dfj, integer5 + 63, integer6 + 14 + 29 - integer10, 185, 29 - integer10, 12, integer10);
            }
        }
    }
    
    static {
        BREWING_STAND_LOCATION = new ResourceLocation("textures/gui/container/brewing_stand.png");
        BUBBLELENGTHS = new int[] { 29, 24, 20, 16, 11, 6, 0 };
    }
}
