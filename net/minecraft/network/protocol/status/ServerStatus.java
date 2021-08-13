package net.minecraft.network.protocol.status;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.UUID;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;

public class ServerStatus {
    private Component description;
    private Players players;
    private Version version;
    private String favicon;
    
    public Component getDescription() {
        return this.description;
    }
    
    public void setDescription(final Component nr) {
        this.description = nr;
    }
    
    public Players getPlayers() {
        return this.players;
    }
    
    public void setPlayers(final Players a) {
        this.players = a;
    }
    
    public Version getVersion() {
        return this.version;
    }
    
    public void setVersion(final Version c) {
        this.version = c;
    }
    
    public void setFavicon(final String string) {
        this.favicon = string;
    }
    
    public String getFavicon() {
        return this.favicon;
    }
    
    public static class Players {
        private final int maxPlayers;
        private final int numPlayers;
        private GameProfile[] sample;
        
        public Players(final int integer1, final int integer2) {
            this.maxPlayers = integer1;
            this.numPlayers = integer2;
        }
        
        public int getMaxPlayers() {
            return this.maxPlayers;
        }
        
        public int getNumPlayers() {
            return this.numPlayers;
        }
        
        public GameProfile[] getSample() {
            return this.sample;
        }
        
        public void setSample(final GameProfile[] arr) {
            this.sample = arr;
        }
        
        public static class Serializer implements JsonDeserializer<Players>, JsonSerializer<Players> {
            public Players deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "players");
                final Players a6 = new Players(GsonHelper.getAsInt(jsonObject5, "max"), GsonHelper.getAsInt(jsonObject5, "online"));
                if (GsonHelper.isArrayNode(jsonObject5, "sample")) {
                    final JsonArray jsonArray7 = GsonHelper.getAsJsonArray(jsonObject5, "sample");
                    if (jsonArray7.size() > 0) {
                        final GameProfile[] arr8 = new GameProfile[jsonArray7.size()];
                        for (int integer9 = 0; integer9 < arr8.length; ++integer9) {
                            final JsonObject jsonObject6 = GsonHelper.convertToJsonObject(jsonArray7.get(integer9), new StringBuilder().append("player[").append(integer9).append("]").toString());
                            final String string11 = GsonHelper.getAsString(jsonObject6, "id");
                            arr8[integer9] = new GameProfile(UUID.fromString(string11), GsonHelper.getAsString(jsonObject6, "name"));
                        }
                        a6.setSample(arr8);
                    }
                }
                return a6;
            }
            
            public JsonElement serialize(final Players a, final Type type, final JsonSerializationContext jsonSerializationContext) {
                final JsonObject jsonObject5 = new JsonObject();
                jsonObject5.addProperty("max", (Number)a.getMaxPlayers());
                jsonObject5.addProperty("online", (Number)a.getNumPlayers());
                if (a.getSample() != null && a.getSample().length > 0) {
                    final JsonArray jsonArray6 = new JsonArray();
                    for (int integer7 = 0; integer7 < a.getSample().length; ++integer7) {
                        final JsonObject jsonObject6 = new JsonObject();
                        final UUID uUID9 = a.getSample()[integer7].getId();
                        jsonObject6.addProperty("id", (uUID9 == null) ? "" : uUID9.toString());
                        jsonObject6.addProperty("name", a.getSample()[integer7].getName());
                        jsonArray6.add((JsonElement)jsonObject6);
                    }
                    jsonObject5.add("sample", (JsonElement)jsonArray6);
                }
                return (JsonElement)jsonObject5;
            }
        }
    }
    
    public static class Version {
        private final String name;
        private final int protocol;
        
        public Version(final String string, final int integer) {
            this.name = string;
            this.protocol = integer;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getProtocol() {
            return this.protocol;
        }
        
        public static class Serializer implements JsonDeserializer<Version>, JsonSerializer<Version> {
            public Version deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "version");
                return new Version(GsonHelper.getAsString(jsonObject5, "name"), GsonHelper.getAsInt(jsonObject5, "protocol"));
            }
            
            public JsonElement serialize(final Version c, final Type type, final JsonSerializationContext jsonSerializationContext) {
                final JsonObject jsonObject5 = new JsonObject();
                jsonObject5.addProperty("name", c.getName());
                jsonObject5.addProperty("protocol", (Number)c.getProtocol());
                return (JsonElement)jsonObject5;
            }
        }
    }
    
    public static class Serializer implements JsonDeserializer<ServerStatus>, JsonSerializer<ServerStatus> {
        public ServerStatus deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "status");
            final ServerStatus un6 = new ServerStatus();
            if (jsonObject5.has("description")) {
                un6.setDescription((Component)jsonDeserializationContext.deserialize(jsonObject5.get("description"), (Type)Component.class));
            }
            if (jsonObject5.has("players")) {
                un6.setPlayers((Players)jsonDeserializationContext.deserialize(jsonObject5.get("players"), (Type)Players.class));
            }
            if (jsonObject5.has("version")) {
                un6.setVersion((Version)jsonDeserializationContext.deserialize(jsonObject5.get("version"), (Type)Version.class));
            }
            if (jsonObject5.has("favicon")) {
                un6.setFavicon(GsonHelper.getAsString(jsonObject5, "favicon"));
            }
            return un6;
        }
        
        public JsonElement serialize(final ServerStatus un, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            if (un.getDescription() != null) {
                jsonObject5.add("description", jsonSerializationContext.serialize(un.getDescription()));
            }
            if (un.getPlayers() != null) {
                jsonObject5.add("players", jsonSerializationContext.serialize(un.getPlayers()));
            }
            if (un.getVersion() != null) {
                jsonObject5.add("version", jsonSerializationContext.serialize(un.getVersion()));
            }
            if (un.getFavicon() != null) {
                jsonObject5.addProperty("favicon", un.getFavicon());
            }
            return (JsonElement)jsonObject5;
        }
    }
}
