package net.minecraft.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public class CycleOption extends Option {
    private final BiConsumer<Options, Integer> setter;
    private final BiFunction<Options, CycleOption, Component> toString;
    
    public CycleOption(final String string, final BiConsumer<Options, Integer> biConsumer, final BiFunction<Options, CycleOption, Component> biFunction) {
        super(string);
        this.setter = biConsumer;
        this.toString = biFunction;
    }
    
    public void toggle(final Options dka, final int integer) {
        this.setter.accept(dka, integer);
        dka.save();
    }
    
    @Override
    public AbstractWidget createButton(final Options dka, final int integer2, final int integer3, final int integer4) {
        return new OptionButton(integer2, integer3, integer4, 20, this, this.getMessage(dka), dlg -> {
            this.toggle(dka, 1);
            dlg.setMessage(this.getMessage(dka));
        });
    }
    
    public Component getMessage(final Options dka) {
        return (Component)this.toString.apply(dka, this);
    }
}
