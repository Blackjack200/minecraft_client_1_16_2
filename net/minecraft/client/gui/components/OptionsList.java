package net.minecraft.client.gui.components;

import net.minecraft.client.gui.components.events.GuiEventListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Options;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.client.Option;
import net.minecraft.client.Minecraft;

public class OptionsList extends ContainerObjectSelectionList<Entry> {
    public OptionsList(final Minecraft djw, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        super(djw, integer2, integer3, integer4, integer5, integer6);
        this.centerListVertically = false;
    }
    
    public int addBig(final Option djz) {
        return this.addEntry(Entry.big(this.minecraft.options, this.width, djz));
    }
    
    public void addSmall(final Option djz1, @Nullable final Option djz2) {
        this.addEntry(Entry.small(this.minecraft.options, this.width, djz1, djz2));
    }
    
    public void addSmall(final Option[] arr) {
        for (int integer3 = 0; integer3 < arr.length; integer3 += 2) {
            this.addSmall(arr[integer3], (integer3 < arr.length - 1) ? arr[integer3 + 1] : null);
        }
    }
    
    @Override
    public int getRowWidth() {
        return 400;
    }
    
    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }
    
    @Nullable
    public AbstractWidget findOption(final Option djz) {
        for (final Entry a4 : this.children()) {
            for (final AbstractWidget dle6 : a4.children) {
                if (dle6 instanceof OptionButton && ((OptionButton)dle6).getOption() == djz) {
                    return dle6;
                }
            }
        }
        return null;
    }
    
    public Optional<AbstractWidget> getMouseOver(final double double1, final double double2) {
        for (final Entry a7 : this.children()) {
            for (final AbstractWidget dle9 : a7.children) {
                if (dle9.isMouseOver(double1, double2)) {
                    return (Optional<AbstractWidget>)Optional.of(dle9);
                }
            }
        }
        return (Optional<AbstractWidget>)Optional.empty();
    }
    
    public static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        private final List<AbstractWidget> children;
        
        private Entry(final List<AbstractWidget> list) {
            this.children = list;
        }
        
        public static Entry big(final Options dka, final int integer, final Option djz) {
            return new Entry((List<AbstractWidget>)ImmutableList.of(djz.createButton(dka, integer / 2 - 155, 0, 310)));
        }
        
        public static Entry small(final Options dka, final int integer, final Option djz3, @Nullable final Option djz4) {
            final AbstractWidget dle5 = djz3.createButton(dka, integer / 2 - 155, 0, 150);
            if (djz4 == null) {
                return new Entry((List<AbstractWidget>)ImmutableList.of(dle5));
            }
            return new Entry((List<AbstractWidget>)ImmutableList.of(dle5, djz4.createButton(dka, integer / 2 - 155 + 160, 0, 150)));
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.children.forEach(dle -> {
                dle.y = integer3;
                dle.render(dfj, integer7, integer8, float10);
            });
        }
        
        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }
    }
}
