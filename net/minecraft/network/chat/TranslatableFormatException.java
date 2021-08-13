package net.minecraft.network.chat;

public class TranslatableFormatException extends IllegalArgumentException {
    public TranslatableFormatException(final TranslatableComponent of, final String string) {
        super(String.format("Error parsing: %s: %s", new Object[] { of, string }));
    }
    
    public TranslatableFormatException(final TranslatableComponent of, final int integer) {
        super(String.format("Invalid index %d requested for %s", new Object[] { integer, of }));
    }
    
    public TranslatableFormatException(final TranslatableComponent of, final Throwable throwable) {
        super(String.format("Error while parsing: %s", new Object[] { of }), throwable);
    }
}
