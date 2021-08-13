package net.minecraft.client;

import net.minecraft.network.chat.Component;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LogaritmicProgressOption extends ProgressOption {
    public LogaritmicProgressOption(final String string, final double double2, final double double3, final float float4, final Function<Options, Double> function, final BiConsumer<Options, Double> biConsumer, final BiFunction<Options, ProgressOption, Component> biFunction) {
        super(string, double2, double3, float4, function, biConsumer, biFunction);
    }
    
    @Override
    public double toPct(final double double1) {
        return Math.log(double1 / this.minValue) / Math.log(this.maxValue / this.minValue);
    }
    
    @Override
    public double toValue(final double double1) {
        return this.minValue * Math.pow(2.718281828459045, Math.log(this.maxValue / this.minValue) * double1);
    }
}
