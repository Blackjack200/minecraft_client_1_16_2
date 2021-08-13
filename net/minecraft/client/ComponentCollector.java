package net.minecraft.client;

import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.FormattedText;
import java.util.List;

public class ComponentCollector {
    private final List<FormattedText> parts;
    
    public ComponentCollector() {
        this.parts = (List<FormattedText>)Lists.newArrayList();
    }
    
    public void append(final FormattedText nu) {
        this.parts.add(nu);
    }
    
    @Nullable
    public FormattedText getResult() {
        if (this.parts.isEmpty()) {
            return null;
        }
        if (this.parts.size() == 1) {
            return (FormattedText)this.parts.get(0);
        }
        return FormattedText.composite(this.parts);
    }
    
    public FormattedText getResultOrEmpty() {
        final FormattedText nu2 = this.getResult();
        return (nu2 != null) ? nu2 : FormattedText.EMPTY;
    }
}
