package net.minecraft.server.players;

import org.apache.logging.log4j.LogManager;
import java.text.ParseException;
import com.google.gson.JsonObject;
import java.util.Comparator;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import java.io.Writer;
import com.google.gson.JsonElement;
import java.io.Reader;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.FileNotFoundException;
import com.google.gson.JsonArray;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.concurrent.atomic.AtomicReference;
import com.mojang.authlib.GameProfile;
import java.util.Locale;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.common.collect.Maps;
import java.util.concurrent.atomic.AtomicLong;
import java.io.File;
import com.google.gson.Gson;
import com.mojang.authlib.GameProfileRepository;
import java.util.UUID;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class GameProfileCache {
    private static final Logger LOGGER;
    private static boolean usesAuthentication;
    private final Map<String, GameProfileInfo> profilesByName;
    private final Map<UUID, GameProfileInfo> profilesByUUID;
    private final GameProfileRepository profileRepository;
    private final Gson gson;
    private final File file;
    private final AtomicLong operationCount;
    
    public GameProfileCache(final GameProfileRepository gameProfileRepository, final File file) {
        this.profilesByName = (Map<String, GameProfileInfo>)Maps.newConcurrentMap();
        this.profilesByUUID = (Map<UUID, GameProfileInfo>)Maps.newConcurrentMap();
        this.gson = new GsonBuilder().create();
        this.operationCount = new AtomicLong();
        this.profileRepository = gameProfileRepository;
        this.file = file;
        Lists.reverse((List)this.load()).forEach(this::safeAdd);
    }
    
    private void safeAdd(final GameProfileInfo a) {
        final GameProfile gameProfile3 = a.getProfile();
        a.setLastAccess(this.getNextOperation());
        final String string4 = gameProfile3.getName();
        if (string4 != null) {
            this.profilesByName.put(string4.toLowerCase(Locale.ROOT), a);
        }
        final UUID uUID5 = gameProfile3.getId();
        if (uUID5 != null) {
            this.profilesByUUID.put(uUID5, a);
        }
    }
    
    @Nullable
    private static GameProfile lookupGameProfile(final GameProfileRepository gameProfileRepository, final String string) {
        final AtomicReference<GameProfile> atomicReference3 = (AtomicReference<GameProfile>)new AtomicReference();
        final ProfileLookupCallback profileLookupCallback4 = (ProfileLookupCallback)new ProfileLookupCallback() {
            public void onProfileLookupSucceeded(final GameProfile gameProfile) {
                atomicReference3.set(gameProfile);
            }
            
            public void onProfileLookupFailed(final GameProfile gameProfile, final Exception exception) {
                atomicReference3.set(null);
            }
        };
        gameProfileRepository.findProfilesByNames(new String[] { string }, Agent.MINECRAFT, profileLookupCallback4);
        GameProfile gameProfile5 = (GameProfile)atomicReference3.get();
        if (!usesAuthentication() && gameProfile5 == null) {
            final UUID uUID6 = Player.createPlayerUUID(new GameProfile((UUID)null, string));
            gameProfile5 = new GameProfile(uUID6, string);
        }
        return gameProfile5;
    }
    
    public static void setUsesAuthentication(final boolean boolean1) {
        GameProfileCache.usesAuthentication = boolean1;
    }
    
    private static boolean usesAuthentication() {
        return GameProfileCache.usesAuthentication;
    }
    
    public void add(final GameProfile gameProfile) {
        final Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(new Date());
        calendar3.add(2, 1);
        final Date date4 = calendar3.getTime();
        final GameProfileInfo a5 = new GameProfileInfo(gameProfile, date4);
        this.safeAdd(a5);
        this.save();
    }
    
    private long getNextOperation() {
        return this.operationCount.incrementAndGet();
    }
    
    @Nullable
    public GameProfile get(final String string) {
        final String string2 = string.toLowerCase(Locale.ROOT);
        GameProfileInfo a4 = (GameProfileInfo)this.profilesByName.get(string2);
        boolean boolean5 = false;
        if (a4 != null && new Date().getTime() >= a4.expirationDate.getTime()) {
            this.profilesByUUID.remove(a4.getProfile().getId());
            this.profilesByName.remove(a4.getProfile().getName().toLowerCase(Locale.ROOT));
            boolean5 = true;
            a4 = null;
        }
        GameProfile gameProfile6;
        if (a4 != null) {
            a4.setLastAccess(this.getNextOperation());
            gameProfile6 = a4.getProfile();
        }
        else {
            gameProfile6 = lookupGameProfile(this.profileRepository, string2);
            if (gameProfile6 != null) {
                this.add(gameProfile6);
                boolean5 = false;
            }
        }
        if (boolean5) {
            this.save();
        }
        return gameProfile6;
    }
    
    @Nullable
    public GameProfile get(final UUID uUID) {
        final GameProfileInfo a3 = (GameProfileInfo)this.profilesByUUID.get(uUID);
        if (a3 == null) {
            return null;
        }
        a3.setLastAccess(this.getNextOperation());
        return a3.getProfile();
    }
    
    private static DateFormat createDateFormat() {
        return (DateFormat)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }
    
    public List<GameProfileInfo> load() {
        final List<GameProfileInfo> list2 = (List<GameProfileInfo>)Lists.newArrayList();
        try (final Reader reader3 = (Reader)Files.newReader(this.file, StandardCharsets.UTF_8)) {
            final JsonArray jsonArray5 = (JsonArray)this.gson.fromJson(reader3, (Class)JsonArray.class);
            if (jsonArray5 == null) {
                return list2;
            }
            final DateFormat dateFormat6 = createDateFormat();
            jsonArray5.forEach(jsonElement -> {
                final GameProfileInfo a4 = readGameProfile(jsonElement, dateFormat6);
                if (a4 != null) {
                    list2.add(a4);
                }
            });
        }
        catch (FileNotFoundException ex2) {}
        catch (IOException | JsonParseException ex3) {
            final Exception ex;
            final Exception exception3 = ex;
            GameProfileCache.LOGGER.warn("Failed to load profile cache {}", this.file, exception3);
        }
        return list2;
    }
    
    public void save() {
        final JsonArray jsonArray2 = new JsonArray();
        final DateFormat dateFormat3 = createDateFormat();
        this.getTopMRUProfiles(1000).forEach(a -> jsonArray2.add(writeGameProfile(a, dateFormat3)));
        final String string4 = this.gson.toJson((JsonElement)jsonArray2);
        try (final Writer writer5 = (Writer)Files.newWriter(this.file, StandardCharsets.UTF_8)) {
            writer5.write(string4);
        }
        catch (IOException ex) {}
    }
    
    private Stream<GameProfileInfo> getTopMRUProfiles(final int integer) {
        return (Stream<GameProfileInfo>)ImmutableList.copyOf(this.profilesByUUID.values()).stream().sorted(Comparator.comparing(GameProfileInfo::getLastAccess).reversed()).limit((long)integer);
    }
    
    private static JsonElement writeGameProfile(final GameProfileInfo a, final DateFormat dateFormat) {
        final JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("name", a.getProfile().getName());
        final UUID uUID4 = a.getProfile().getId();
        jsonObject3.addProperty("uuid", (uUID4 == null) ? "" : uUID4.toString());
        jsonObject3.addProperty("expiresOn", dateFormat.format(a.getExpirationDate()));
        return (JsonElement)jsonObject3;
    }
    
    @Nullable
    private static GameProfileInfo readGameProfile(final JsonElement jsonElement, final DateFormat dateFormat) {
        if (!jsonElement.isJsonObject()) {
            return null;
        }
        final JsonObject jsonObject3 = jsonElement.getAsJsonObject();
        final JsonElement jsonElement2 = jsonObject3.get("name");
        final JsonElement jsonElement3 = jsonObject3.get("uuid");
        final JsonElement jsonElement4 = jsonObject3.get("expiresOn");
        if (jsonElement2 == null || jsonElement3 == null) {
            return null;
        }
        final String string7 = jsonElement3.getAsString();
        final String string8 = jsonElement2.getAsString();
        Date date9 = null;
        if (jsonElement4 != null) {
            try {
                date9 = dateFormat.parse(jsonElement4.getAsString());
            }
            catch (ParseException ex) {}
        }
        if (string8 == null || string7 == null || date9 == null) {
            return null;
        }
        UUID uUID10;
        try {
            uUID10 = UUID.fromString(string7);
        }
        catch (Throwable throwable11) {
            return null;
        }
        return new GameProfileInfo(new GameProfile(uUID10, string8), date9);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class GameProfileInfo {
        private final GameProfile profile;
        private final Date expirationDate;
        private volatile long lastAccess;
        
        private GameProfileInfo(final GameProfile gameProfile, final Date date) {
            this.profile = gameProfile;
            this.expirationDate = date;
        }
        
        public GameProfile getProfile() {
            return this.profile;
        }
        
        public Date getExpirationDate() {
            return this.expirationDate;
        }
        
        public void setLastAccess(final long long1) {
            this.lastAccess = long1;
        }
        
        public long getLastAccess() {
            return this.lastAccess;
        }
    }
}
