package net.minecraft.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.gui.components.AbstractWidget;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class BooleanOption extends Option {
    private final Predicate<Options> getter;
    private final BiConsumer<Options, Boolean> setter;
    
    public BooleanOption(final String string, final Predicate<Options> predicate, final BiConsumer<Options, Boolean> biConsumer) {
        super(string);
        this.getter = predicate;
        this.setter = biConsumer;
    }
    
    public void set(final Options dka, final String string) {
        this.set(dka, "true".equals(string));
    }
    
    public void toggle(final Options dka) {
        this.set(dka, !this.get(dka));
        dka.save();
    }
    
    private void set(final Options dka, final boolean boolean2) {
        this.setter.accept(dka, boolean2);
    }
    
    public boolean get(final Options dka) {
        return this.getter.test(dka);
    }
    
    @Override
    public AbstractWidget createButton(final Options dka, final int integer2, final int integer3, final int integer4) {
        return new OptionButton(integer2, integer3, integer4, 20, this, this.getMessage(dka), dlg -> {
            this.toggle(dka);
            dlg.setMessage(this.getMessage(dka));
        });
    }
    
    public Component getMessage(final Options dka) {
        return CommonComponents.optionStatus(this.getCaption(), this.get(dka));
    }
}
