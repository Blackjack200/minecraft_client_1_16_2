package net.minecraft;

public class ResourceLocationException extends RuntimeException {
    public ResourceLocationException(final String string) {
        super(string);
    }
    
    public ResourceLocationException(final String string, final Throwable throwable) {
        super(string, throwable);
    }
}
