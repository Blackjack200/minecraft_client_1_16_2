package net.minecraft.network.chat;

import java.util.Objects;
import java.util.Collection;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.locale.Language;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;

public abstract class BaseComponent implements MutableComponent {
    protected final List<Component> siblings;
    private FormattedCharSequence visualOrderText;
    @Nullable
    private Language decomposedWith;
    private Style style;
    
    public BaseComponent() {
        this.siblings = (List<Component>)Lists.newArrayList();
        this.visualOrderText = FormattedCharSequence.EMPTY;
        this.style = Style.EMPTY;
    }
    
    public MutableComponent append(final Component nr) {
        this.siblings.add(nr);
        return this;
    }
    
    public String getContents() {
        return "";
    }
    
    public List<Component> getSiblings() {
        return this.siblings;
    }
    
    public MutableComponent setStyle(final Style ob) {
        this.style = ob;
        return this;
    }
    
    public Style getStyle() {
        return this.style;
    }
    
    public abstract BaseComponent plainCopy();
    
    public final MutableComponent copy() {
        final BaseComponent nn2 = this.plainCopy();
        nn2.siblings.addAll((Collection)this.siblings);
        nn2.setStyle(this.style);
        return nn2;
    }
    
    public FormattedCharSequence getVisualOrderText() {
        final Language ly2 = Language.getInstance();
        if (this.decomposedWith != ly2) {
            this.visualOrderText = ly2.getVisualOrder(this);
            this.decomposedWith = ly2;
        }
        return this.visualOrderText;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof BaseComponent) {
            final BaseComponent nn3 = (BaseComponent)object;
            return this.siblings.equals(nn3.siblings) && Objects.equals(this.getStyle(), nn3.getStyle());
        }
        return false;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.getStyle(), this.siblings });
    }
    
    public String toString() {
        return new StringBuilder().append("BaseComponent{style=").append(this.style).append(", siblings=").append(this.siblings).append('}').toString();
    }
}
