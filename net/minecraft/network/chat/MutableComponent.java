package net.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import java.util.function.UnaryOperator;

public interface MutableComponent extends Component {
    MutableComponent setStyle(final Style ob);
    
    default MutableComponent append(final String string) {
        return this.append(new TextComponent(string));
    }
    
    MutableComponent append(final Component nr);
    
    default MutableComponent withStyle(final UnaryOperator<Style> unaryOperator) {
        this.setStyle((Style)unaryOperator.apply(this.getStyle()));
        return this;
    }
    
    default MutableComponent withStyle(final Style ob) {
        this.setStyle(ob.applyTo(this.getStyle()));
        return this;
    }
    
    default MutableComponent withStyle(final ChatFormatting... arr) {
        this.setStyle(this.getStyle().applyFormats(arr));
        return this;
    }
    
    default MutableComponent withStyle(final ChatFormatting k) {
        this.setStyle(this.getStyle().applyFormat(k));
        return this;
    }
}
