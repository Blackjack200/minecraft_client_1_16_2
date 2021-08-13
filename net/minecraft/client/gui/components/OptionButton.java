package net.minecraft.client.gui.components;

import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.client.Option;

public class OptionButton extends Button implements TooltipAccessor {
    private final Option option;
    
    public OptionButton(final int integer1, final int integer2, final int integer3, final int integer4, final Option djz, final Component nr, final OnPress a) {
        super(integer1, integer2, integer3, integer4, nr, a);
        this.option = djz;
    }
    
    public Option getOption() {
        return this.option;
    }
    
    @Override
    public Optional<List<FormattedCharSequence>> getTooltip() {
        return this.option.getTooltip();
    }
}
