package net.minecraft.client.gui.screens;

import javax.annotation.Nullable;
import net.minecraft.client.gui.components.AbstractWidget;
import java.util.Optional;
import net.minecraft.client.gui.components.TooltipAccessor;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Options;

public class OptionsSubScreen extends Screen {
    protected final Screen lastScreen;
    protected final Options options;
    
    public OptionsSubScreen(final Screen doq, final Options dka, final Component nr) {
        super(nr);
        this.lastScreen = doq;
        this.options = dka;
    }
    
    @Override
    public void removed() {
        this.minecraft.options.save();
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }
    
    @Nullable
    public static List<FormattedCharSequence> tooltipAt(final OptionsList dlu, final int integer2, final int integer3) {
        final Optional<AbstractWidget> optional4 = dlu.getMouseOver(integer2, integer3);
        if (optional4.isPresent() && optional4.get() instanceof TooltipAccessor) {
            final Optional<List<FormattedCharSequence>> optional5 = ((TooltipAccessor)optional4.get()).getTooltip();
            return (List<FormattedCharSequence>)optional5.orElse(null);
        }
        return null;
    }
}
