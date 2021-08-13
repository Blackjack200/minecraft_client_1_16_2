package net.minecraft.client;

import net.minecraft.util.Mth;
import net.minecraft.client.gui.components.SliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ProgressOption extends Option {
    protected final float steps;
    protected final double minValue;
    protected double maxValue;
    private final Function<Options, Double> getter;
    private final BiConsumer<Options, Double> setter;
    private final BiFunction<Options, ProgressOption, Component> toString;
    
    public ProgressOption(final String string, final double double2, final double double3, final float float4, final Function<Options, Double> function, final BiConsumer<Options, Double> biConsumer, final BiFunction<Options, ProgressOption, Component> biFunction) {
        super(string);
        this.minValue = double2;
        this.maxValue = double3;
        this.steps = float4;
        this.getter = function;
        this.setter = biConsumer;
        this.toString = biFunction;
    }
    
    @Override
    public AbstractWidget createButton(final Options dka, final int integer2, final int integer3, final int integer4) {
        return new SliderButton(dka, integer2, integer3, integer4, 20, this);
    }
    
    public double toPct(final double double1) {
        return Mth.clamp((this.clamp(double1) - this.minValue) / (this.maxValue - this.minValue), 0.0, 1.0);
    }
    
    public double toValue(final double double1) {
        return this.clamp(Mth.lerp(Mth.clamp(double1, 0.0, 1.0), this.minValue, this.maxValue));
    }
    
    private double clamp(double double1) {
        if (this.steps > 0.0f) {
            double1 = this.steps * Math.round(double1 / this.steps);
        }
        return Mth.clamp(double1, this.minValue, this.maxValue);
    }
    
    public double getMinValue() {
        return this.minValue;
    }
    
    public double getMaxValue() {
        return this.maxValue;
    }
    
    public void setMaxValue(final float float1) {
        this.maxValue = float1;
    }
    
    public void set(final Options dka, final double double2) {
        this.setter.accept(dka, double2);
    }
    
    public double get(final Options dka) {
        return (double)this.getter.apply(dka);
    }
    
    public Component getMessage(final Options dka) {
        return (Component)this.toString.apply(dka, this);
    }
}
