package net.minecraft.server.players;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.function.Consumer;
import net.minecraft.Util;
import java.io.BufferedReader;
import net.minecraft.util.GsonHelper;
import java.io.Reader;
import java.io.BufferedWriter;
import com.google.gson.JsonElement;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonArray;
import java.util.Collection;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.io.IOException;
import com.google.common.collect.Maps;
import java.util.Map;
import java.io.File;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public abstract class StoredUserList<K, V extends StoredUserEntry<K>> {
    protected static final Logger LOGGER;
    private static final Gson GSON;
    private final File file;
    private final Map<String, V> map;
    
    public StoredUserList(final File file) {
        this.map = (Map<String, V>)Maps.newHashMap();
        this.file = file;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void add(final V acv) {
        this.map.put(this.getKeyForUser(acv.getUser()), acv);
        try {
            this.save();
        }
        catch (IOException iOException3) {
            StoredUserList.LOGGER.warn("Could not save the list after adding a user.", (Throwable)iOException3);
        }
    }
    
    @Nullable
    public V get(final K object) {
        this.removeExpired();
        return (V)this.map.get(this.getKeyForUser(object));
    }
    
    public void remove(final K object) {
        this.map.remove(this.getKeyForUser(object));
        try {
            this.save();
        }
        catch (IOException iOException3) {
            StoredUserList.LOGGER.warn("Could not save the list after removing a user.", (Throwable)iOException3);
        }
    }
    
    public void remove(final StoredUserEntry<K> acv) {
        this.remove(acv.getUser());
    }
    
    public String[] getUserList() {
        return (String[])this.map.keySet().toArray((Object[])new String[this.map.size()]);
    }
    
    public boolean isEmpty() {
        return this.map.size() < 1;
    }
    
    protected String getKeyForUser(final K object) {
        return object.toString();
    }
    
    protected boolean contains(final K object) {
        return this.map.containsKey(this.getKeyForUser(object));
    }
    
    private void removeExpired() {
        final List<K> list2 = (List<K>)Lists.newArrayList();
        for (final V acv4 : this.map.values()) {
            if (acv4.hasExpired()) {
                list2.add(((StoredUserEntry<Object>)acv4).getUser());
            }
        }
        for (final K object4 : list2) {
            this.map.remove(this.getKeyForUser(object4));
        }
    }
    
    protected abstract StoredUserEntry<K> createEntry(final JsonObject jsonObject);
    
    public Collection<V> getEntries() {
        return (Collection<V>)this.map.values();
    }
    
    public void save() throws IOException {
        final JsonArray jsonArray2 = new JsonArray();
        this.map.values().stream().map(acv -> Util.<JsonObject>make(new JsonObject(), (java.util.function.Consumer<JsonObject>)acv::serialize)).forEach(jsonArray2::add);
        try (final BufferedWriter bufferedWriter3 = Files.newWriter(this.file, StandardCharsets.UTF_8)) {
            StoredUserList.GSON.toJson((JsonElement)jsonArray2, (Appendable)bufferedWriter3);
        }
    }
    
    public void load() throws IOException {
        if (!this.file.exists()) {
            return;
        }
        try (final BufferedReader bufferedReader2 = Files.newReader(this.file, StandardCharsets.UTF_8)) {
            final JsonArray jsonArray4 = (JsonArray)StoredUserList.GSON.fromJson((Reader)bufferedReader2, (Class)JsonArray.class);
            this.map.clear();
            for (final JsonElement jsonElement6 : jsonArray4) {
                final JsonObject jsonObject7 = GsonHelper.convertToJsonObject(jsonElement6, "entry");
                final StoredUserEntry<K> acv8 = this.createEntry(jsonObject7);
                if (acv8.getUser() != null) {
                    this.map.put(this.getKeyForUser(acv8.getUser()), acv8);
                }
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().setPrettyPrinting().create();
    }
}
