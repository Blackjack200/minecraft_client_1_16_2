package net.minecraft.network.chat;

public class TextComponent extends BaseComponent {
    public static final Component EMPTY;
    private final String text;
    
    public TextComponent(final String string) {
        this.text = string;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public String getContents() {
        return this.text;
    }
    
    @Override
    public TextComponent plainCopy() {
        return new TextComponent(this.text);
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof TextComponent) {
            final TextComponent oe3 = (TextComponent)object;
            return this.text.equals(oe3.getText()) && super.equals(object);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "TextComponent{text='" + this.text + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    static {
        EMPTY = new TextComponent("");
    }
}
