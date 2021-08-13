package net.minecraft.client.gui.screens.advancements;

import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;

enum AdvancementTabType {
    ABOVE(0, 0, 28, 32, 8), 
    BELOW(84, 0, 28, 32, 8), 
    LEFT(0, 64, 32, 28, 5), 
    RIGHT(96, 64, 32, 28, 5);
    
    private final int textureX;
    private final int textureY;
    private final int width;
    private final int height;
    private final int max;
    
    private AdvancementTabType(final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        this.textureX = integer3;
        this.textureY = integer4;
        this.width = integer5;
        this.height = integer6;
        this.max = integer7;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public void draw(final PoseStack dfj, final GuiComponent dkt, final int integer3, final int integer4, final boolean boolean5, final int integer6) {
        int integer7 = this.textureX;
        if (integer6 > 0) {
            integer7 += this.width;
        }
        if (integer6 == this.max - 1) {
            integer7 += this.width;
        }
        final int integer8 = boolean5 ? (this.textureY + this.height) : this.textureY;
        dkt.blit(dfj, integer3 + this.getX(integer6), integer4 + this.getY(integer6), integer7, integer8, this.width, this.height);
    }
    
    public void drawIcon(final int integer1, final int integer2, final int integer3, final ItemRenderer efg, final ItemStack bly) {
        int integer4 = integer1 + this.getX(integer3);
        int integer5 = integer2 + this.getY(integer3);
        switch (this) {
            case ABOVE: {
                integer4 += 6;
                integer5 += 9;
                break;
            }
            case BELOW: {
                integer4 += 6;
                integer5 += 6;
                break;
            }
            case LEFT: {
                integer4 += 10;
                integer5 += 5;
                break;
            }
            case RIGHT: {
                integer4 += 6;
                integer5 += 5;
                break;
            }
        }
        efg.renderAndDecorateFakeItem(bly, integer4, integer5);
    }
    
    public int getX(final int integer) {
        switch (this) {
            case ABOVE: {
                return (this.width + 4) * integer;
            }
            case BELOW: {
                return (this.width + 4) * integer;
            }
            case LEFT: {
                return -this.width + 4;
            }
            case RIGHT: {
                return 248;
            }
            default: {
                throw new UnsupportedOperationException(new StringBuilder().append("Don't know what this tab type is!").append(this).toString());
            }
        }
    }
    
    public int getY(final int integer) {
        switch (this) {
            case ABOVE: {
                return -this.height + 4;
            }
            case BELOW: {
                return 136;
            }
            case LEFT: {
                return this.height * integer;
            }
            case RIGHT: {
                return this.height * integer;
            }
            default: {
                throw new UnsupportedOperationException(new StringBuilder().append("Don't know what this tab type is!").append(this).toString());
            }
        }
    }
    
    public boolean isMouseOver(final int integer1, final int integer2, final int integer3, final double double4, final double double5) {
        final int integer4 = integer1 + this.getX(integer3);
        final int integer5 = integer2 + this.getY(integer3);
        return double4 > integer4 && double4 < integer4 + this.width && double5 > integer5 && double5 < integer5 + this.height;
    }
}
