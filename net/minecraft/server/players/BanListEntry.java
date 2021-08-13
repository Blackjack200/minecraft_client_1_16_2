package net.minecraft.server.players;

import net.minecraft.network.chat.Component;
import java.text.ParseException;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class BanListEntry<T> extends StoredUserEntry<T> {
    public static final SimpleDateFormat DATE_FORMAT;
    protected final Date created;
    protected final String source;
    protected final Date expires;
    protected final String reason;
    
    public BanListEntry(final T object, @Nullable final Date date2, @Nullable final String string3, @Nullable final Date date4, @Nullable final String string5) {
        super(object);
        this.created = ((date2 == null) ? new Date() : date2);
        this.source = ((string3 == null) ? "(Unknown)" : string3);
        this.expires = date4;
        this.reason = ((string5 == null) ? "Banned by an operator." : string5);
    }
    
    protected BanListEntry(final T object, final JsonObject jsonObject) {
        super(object);
        Date date4;
        try {
            date4 = (jsonObject.has("created") ? BanListEntry.DATE_FORMAT.parse(jsonObject.get("created").getAsString()) : new Date());
        }
        catch (ParseException parseException5) {
            date4 = new Date();
        }
        this.created = date4;
        this.source = (jsonObject.has("source") ? jsonObject.get("source").getAsString() : "(Unknown)");
        Date date5;
        try {
            date5 = (jsonObject.has("expires") ? BanListEntry.DATE_FORMAT.parse(jsonObject.get("expires").getAsString()) : null);
        }
        catch (ParseException parseException6) {
            date5 = null;
        }
        this.expires = date5;
        this.reason = (jsonObject.has("reason") ? jsonObject.get("reason").getAsString() : "Banned by an operator.");
    }
    
    public String getSource() {
        return this.source;
    }
    
    public Date getExpires() {
        return this.expires;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public abstract Component getDisplayName();
    
    @Override
    boolean hasExpired() {
        return this.expires != null && this.expires.before(new Date());
    }
    
    @Override
    protected void serialize(final JsonObject jsonObject) {
        jsonObject.addProperty("created", BanListEntry.DATE_FORMAT.format(this.created));
        jsonObject.addProperty("source", this.source);
        jsonObject.addProperty("expires", (this.expires == null) ? "forever" : BanListEntry.DATE_FORMAT.format(this.expires));
        jsonObject.addProperty("reason", this.reason);
    }
    
    static {
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
}
