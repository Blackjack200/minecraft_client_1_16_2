package net.minecraft.client.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

public class Button extends AbstractButton {
    public static final OnTooltip NO_TOOLTIP;
    protected final OnPress onPress;
    protected final OnTooltip onTooltip;
    
    public Button(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr, final OnPress a) {
        this(integer1, integer2, integer3, integer4, nr, a, Button.NO_TOOLTIP);
    }
    
    public Button(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr, final OnPress a, final OnTooltip b) {
        super(integer1, integer2, integer3, integer4, nr);
        this.onPress = a;
        this.onTooltip = b;
    }
    
    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        super.renderButton(dfj, integer2, integer3, float4);
        if (this.isHovered()) {
            this.renderToolTip(dfj, integer2, integer3);
        }
    }
    
    @Override
    public void renderToolTip(final PoseStack dfj, final int integer2, final int integer3) {
        this.onTooltip.onTooltip(this, dfj, integer2, integer3);
    }
    
    static {
        NO_TOOLTIP = ((dlg, dfj, integer3, integer4) -> {});
    }
    
    public interface OnTooltip {
        void onTooltip(final Button dlg, final PoseStack dfj, final int integer3, final int integer4);
    }
    
    public interface OnPress {
        void onPress(final Button dlg);
    }
}
