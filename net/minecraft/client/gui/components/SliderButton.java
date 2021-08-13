package net.minecraft.client.gui.components;

import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Options;
import net.minecraft.client.ProgressOption;

public class SliderButton extends AbstractOptionSliderButton implements TooltipAccessor {
    private final ProgressOption option;
    
    public SliderButton(final Options dka, final int integer2, final int integer3, final int integer4, final int integer5, final ProgressOption dkc) {
        super(dka, integer2, integer3, integer4, integer5, (float)dkc.toPct(dkc.get(dka)));
        this.option = dkc;
        this.updateMessage();
    }
    
    @Override
    protected void applyValue() {
        this.option.set(this.options, this.option.toValue(this.value));
        this.options.save();
    }
    
    @Override
    protected void updateMessage() {
        this.setMessage(this.option.getMessage(this.options));
    }
    
    @Override
    public Optional<List<FormattedCharSequence>> getTooltip() {
        return this.option.getTooltip();
    }
}
