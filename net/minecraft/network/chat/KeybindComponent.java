package net.minecraft.network.chat;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.Function;

public class KeybindComponent extends BaseComponent {
    private static Function<String, Supplier<Component>> keyResolver;
    private final String name;
    private Supplier<Component> nameResolver;
    
    public KeybindComponent(final String string) {
        this.name = string;
    }
    
    public static void setKeyResolver(final Function<String, Supplier<Component>> function) {
        KeybindComponent.keyResolver = function;
    }
    
    private Component getNestedComponent() {
        if (this.nameResolver == null) {
            this.nameResolver = (Supplier<Component>)KeybindComponent.keyResolver.apply(this.name);
        }
        return (Component)this.nameResolver.get();
    }
    
    public <T> Optional<T> visitSelf(final FormattedText.ContentConsumer<T> a) {
        return this.getNestedComponent().<T>visit(a);
    }
    
    public <T> Optional<T> visitSelf(final FormattedText.StyledContentConsumer<T> b, final Style ob) {
        return this.getNestedComponent().<T>visit(b, ob);
    }
    
    @Override
    public KeybindComponent plainCopy() {
        return new KeybindComponent(this.name);
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof KeybindComponent) {
            final KeybindComponent nw3 = (KeybindComponent)object;
            return this.name.equals(nw3.name) && super.equals(object);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "KeybindComponent{keybind='" + this.name + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        KeybindComponent.keyResolver = (Function<String, Supplier<Component>>)(string -> () -> new TextComponent(string));
    }
}
