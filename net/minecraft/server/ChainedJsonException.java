package net.minecraft.server;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import com.google.common.collect.Lists;
import java.util.List;
import java.io.IOException;

public class ChainedJsonException extends IOException {
    private final List<Entry> entries;
    private final String message;
    
    public ChainedJsonException(final String string) {
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry());
        this.message = string;
    }
    
    public ChainedJsonException(final String string, final Throwable throwable) {
        super(throwable);
        (this.entries = (List<Entry>)Lists.newArrayList()).add(new Entry());
        this.message = string;
    }
    
    public void prependJsonKey(final String string) {
        ((Entry)this.entries.get(0)).addJsonKey(string);
    }
    
    public void setFilenameAndFlush(final String string) {
        ((Entry)this.entries.get(0)).filename = string;
        this.entries.add(0, new Entry());
    }
    
    public String getMessage() {
        return new StringBuilder().append("Invalid ").append(this.entries.get(this.entries.size() - 1)).append(": ").append(this.message).toString();
    }
    
    public static ChainedJsonException forException(final Exception exception) {
        if (exception instanceof ChainedJsonException) {
            return (ChainedJsonException)exception;
        }
        String string2 = exception.getMessage();
        if (exception instanceof FileNotFoundException) {
            string2 = "File not found";
        }
        return new ChainedJsonException(string2, (Throwable)exception);
    }
    
    public static class Entry {
        @Nullable
        private String filename;
        private final List<String> jsonKeys;
        
        private Entry() {
            this.jsonKeys = (List<String>)Lists.newArrayList();
        }
        
        private void addJsonKey(final String string) {
            this.jsonKeys.add(0, string);
        }
        
        public String getJsonKeys() {
            return StringUtils.join((Iterable)this.jsonKeys, "->");
        }
        
        public String toString() {
            if (this.filename != null) {
                if (this.jsonKeys.isEmpty()) {
                    return this.filename;
                }
                return this.filename + " " + this.getJsonKeys();
            }
            else {
                if (this.jsonKeys.isEmpty()) {
                    return "(Unknown file)";
                }
                return "(Unknown file) " + this.getJsonKeys();
            }
        }
    }
}
