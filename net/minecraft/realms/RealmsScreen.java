package net.minecraft.realms;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.gui.components.TickableWidget;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screens.Screen;

public abstract class RealmsScreen extends Screen {
    public RealmsScreen() {
        super(NarratorChatListener.NO_TITLE);
    }
    
    protected static int row(final int integer) {
        return 40 + integer * 13;
    }
    
    @Override
    public void tick() {
        for (final AbstractWidget dle3 : this.buttons) {
            if (dle3 instanceof TickableWidget) {
                ((TickableWidget)dle3).tick();
            }
        }
    }
    
    public void narrateLabels() {
        final List<String> list2 = (List<String>)this.children.stream().filter(RealmsLabel.class::isInstance).map(RealmsLabel.class::cast).map(RealmsLabel::getText).collect(Collectors.toList());
        NarrationHelper.now((Iterable<String>)list2);
    }
}
