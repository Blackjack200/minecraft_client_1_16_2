package com.mojang.realmsclient.dto;

import java.util.Comparator;
import org.apache.logging.log4j.LogManager;
import java.util.Locale;
import com.google.common.collect.ComparisonChain;
import org.apache.commons.lang3.builder.EqualsBuilder;
import java.util.Objects;
import com.google.gson.JsonParser;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Iterator;
import com.google.common.base.Joiner;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServer extends ValueObject {
    private static final Logger LOGGER;
    public long id;
    public String remoteSubscriptionId;
    public String name;
    public String motd;
    public State state;
    public String owner;
    public String ownerUUID;
    public List<PlayerInfo> players;
    public Map<Integer, RealmsWorldOptions> slots;
    public boolean expired;
    public boolean expiredTrial;
    public int daysLeft;
    public WorldType worldType;
    public int activeSlot;
    public String minigameName;
    public int minigameId;
    public String minigameImage;
    public RealmsServerPing serverPing;
    
    public RealmsServer() {
        this.serverPing = new RealmsServerPing();
    }
    
    public String getDescription() {
        return this.motd;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMinigameName() {
        return this.minigameName;
    }
    
    public void setName(final String string) {
        this.name = string;
    }
    
    public void setDescription(final String string) {
        this.motd = string;
    }
    
    public void updateServerPing(final RealmsServerPlayerList dgr) {
        final List<String> list3 = (List<String>)Lists.newArrayList();
        int integer4 = 0;
        for (final String string6 : dgr.players) {
            if (string6.equals(Minecraft.getInstance().getUser().getUuid())) {
                continue;
            }
            String string7 = "";
            try {
                string7 = RealmsUtil.uuidToName(string6);
            }
            catch (Exception exception8) {
                RealmsServer.LOGGER.error("Could not get name for " + string6, (Throwable)exception8);
                continue;
            }
            list3.add(string7);
            ++integer4;
        }
        this.serverPing.nrOfPlayers = String.valueOf(integer4);
        this.serverPing.playerList = Joiner.on('\n').join((Iterable)list3);
    }
    
    public static RealmsServer parse(final JsonObject jsonObject) {
        final RealmsServer dgn2 = new RealmsServer();
        try {
            dgn2.id = JsonUtils.getLongOr("id", jsonObject, -1L);
            dgn2.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", jsonObject, (String)null);
            dgn2.name = JsonUtils.getStringOr("name", jsonObject, (String)null);
            dgn2.motd = JsonUtils.getStringOr("motd", jsonObject, (String)null);
            dgn2.state = getState(JsonUtils.getStringOr("state", jsonObject, State.CLOSED.name()));
            dgn2.owner = JsonUtils.getStringOr("owner", jsonObject, (String)null);
            if (jsonObject.get("players") != null && jsonObject.get("players").isJsonArray()) {
                dgn2.players = parseInvited(jsonObject.get("players").getAsJsonArray());
                sortInvited(dgn2);
            }
            else {
                dgn2.players = (List<PlayerInfo>)Lists.newArrayList();
            }
            dgn2.daysLeft = JsonUtils.getIntOr("daysLeft", jsonObject, 0);
            dgn2.expired = JsonUtils.getBooleanOr("expired", jsonObject, false);
            dgn2.expiredTrial = JsonUtils.getBooleanOr("expiredTrial", jsonObject, false);
            dgn2.worldType = getWorldType(JsonUtils.getStringOr("worldType", jsonObject, WorldType.NORMAL.name()));
            dgn2.ownerUUID = JsonUtils.getStringOr("ownerUUID", jsonObject, "");
            if (jsonObject.get("slots") != null && jsonObject.get("slots").isJsonArray()) {
                dgn2.slots = parseSlots(jsonObject.get("slots").getAsJsonArray());
            }
            else {
                dgn2.slots = createEmptySlots();
            }
            dgn2.minigameName = JsonUtils.getStringOr("minigameName", jsonObject, (String)null);
            dgn2.activeSlot = JsonUtils.getIntOr("activeSlot", jsonObject, -1);
            dgn2.minigameId = JsonUtils.getIntOr("minigameId", jsonObject, -1);
            dgn2.minigameImage = JsonUtils.getStringOr("minigameImage", jsonObject, (String)null);
        }
        catch (Exception exception3) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + exception3.getMessage());
        }
        return dgn2;
    }
    
    private static void sortInvited(final RealmsServer dgn) {
        dgn.players.sort((dgk1, dgk2) -> ComparisonChain.start().compareFalseFirst(dgk2.getAccepted(), dgk1.getAccepted()).compare((Comparable)dgk1.getName().toLowerCase(Locale.ROOT), (Comparable)dgk2.getName().toLowerCase(Locale.ROOT)).result());
    }
    
    private static List<PlayerInfo> parseInvited(final JsonArray jsonArray) {
        final List<PlayerInfo> list2 = (List<PlayerInfo>)Lists.newArrayList();
        for (final JsonElement jsonElement4 : jsonArray) {
            try {
                final JsonObject jsonObject5 = jsonElement4.getAsJsonObject();
                final PlayerInfo dgk6 = new PlayerInfo();
                dgk6.setName(JsonUtils.getStringOr("name", jsonObject5, (String)null));
                dgk6.setUuid(JsonUtils.getStringOr("uuid", jsonObject5, (String)null));
                dgk6.setOperator(JsonUtils.getBooleanOr("operator", jsonObject5, false));
                dgk6.setAccepted(JsonUtils.getBooleanOr("accepted", jsonObject5, false));
                dgk6.setOnline(JsonUtils.getBooleanOr("online", jsonObject5, false));
                list2.add(dgk6);
            }
            catch (Exception ex) {}
        }
        return list2;
    }
    
    private static Map<Integer, RealmsWorldOptions> parseSlots(final JsonArray jsonArray) {
        final Map<Integer, RealmsWorldOptions> map2 = (Map<Integer, RealmsWorldOptions>)Maps.newHashMap();
        for (final JsonElement jsonElement4 : jsonArray) {
            try {
                final JsonObject jsonObject6 = jsonElement4.getAsJsonObject();
                final JsonParser jsonParser7 = new JsonParser();
                final JsonElement jsonElement5 = jsonParser7.parse(jsonObject6.get("options").getAsString());
                RealmsWorldOptions dgt5;
                if (jsonElement5 == null) {
                    dgt5 = RealmsWorldOptions.createDefaults();
                }
                else {
                    dgt5 = RealmsWorldOptions.parse(jsonElement5.getAsJsonObject());
                }
                final int integer9 = JsonUtils.getIntOr("slotId", jsonObject6, -1);
                map2.put(integer9, dgt5);
            }
            catch (Exception ex) {}
        }
        for (int integer10 = 1; integer10 <= 3; ++integer10) {
            if (!map2.containsKey(integer10)) {
                map2.put(integer10, RealmsWorldOptions.createEmptyDefaults());
            }
        }
        return map2;
    }
    
    private static Map<Integer, RealmsWorldOptions> createEmptySlots() {
        final Map<Integer, RealmsWorldOptions> map1 = (Map<Integer, RealmsWorldOptions>)Maps.newHashMap();
        map1.put(1, RealmsWorldOptions.createEmptyDefaults());
        map1.put(2, RealmsWorldOptions.createEmptyDefaults());
        map1.put(3, RealmsWorldOptions.createEmptyDefaults());
        return map1;
    }
    
    public static RealmsServer parse(final String string) {
        try {
            return parse(new JsonParser().parse(string).getAsJsonObject());
        }
        catch (Exception exception2) {
            RealmsServer.LOGGER.error("Could not parse McoServer: " + exception2.getMessage());
            return new RealmsServer();
        }
    }
    
    private static State getState(final String string) {
        try {
            return State.valueOf(string);
        }
        catch (Exception exception2) {
            return State.CLOSED;
        }
    }
    
    private static WorldType getWorldType(final String string) {
        try {
            return WorldType.valueOf(string);
        }
        catch (Exception exception2) {
            return WorldType.NORMAL;
        }
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.name, this.motd, this.state, this.owner, this.expired });
    }
    
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        final RealmsServer dgn3 = (RealmsServer)object;
        return new EqualsBuilder().append(this.id, dgn3.id).append(this.name, dgn3.name).append(this.motd, dgn3.motd).append(this.state, dgn3.state).append(this.owner, dgn3.owner).append(this.expired, dgn3.expired).append(this.worldType, this.worldType).isEquals();
    }
    
    public RealmsServer clone() {
        final RealmsServer dgn2 = new RealmsServer();
        dgn2.id = this.id;
        dgn2.remoteSubscriptionId = this.remoteSubscriptionId;
        dgn2.name = this.name;
        dgn2.motd = this.motd;
        dgn2.state = this.state;
        dgn2.owner = this.owner;
        dgn2.players = this.players;
        dgn2.slots = this.cloneSlots(this.slots);
        dgn2.expired = this.expired;
        dgn2.expiredTrial = this.expiredTrial;
        dgn2.daysLeft = this.daysLeft;
        dgn2.serverPing = new RealmsServerPing();
        dgn2.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
        dgn2.serverPing.playerList = this.serverPing.playerList;
        dgn2.worldType = this.worldType;
        dgn2.ownerUUID = this.ownerUUID;
        dgn2.minigameName = this.minigameName;
        dgn2.activeSlot = this.activeSlot;
        dgn2.minigameId = this.minigameId;
        dgn2.minigameImage = this.minigameImage;
        return dgn2;
    }
    
    public Map<Integer, RealmsWorldOptions> cloneSlots(final Map<Integer, RealmsWorldOptions> map) {
        final Map<Integer, RealmsWorldOptions> map2 = (Map<Integer, RealmsWorldOptions>)Maps.newHashMap();
        for (final Map.Entry<Integer, RealmsWorldOptions> entry5 : map.entrySet()) {
            map2.put(entry5.getKey(), ((RealmsWorldOptions)entry5.getValue()).clone());
        }
        return map2;
    }
    
    public String getWorldName(final int integer) {
        return this.name + " (" + ((RealmsWorldOptions)this.slots.get(integer)).getSlotName(integer) + ")";
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class McoServerComparator implements Comparator<RealmsServer> {
        private final String refOwner;
        
        public McoServerComparator(final String string) {
            this.refOwner = string;
        }
        
        public int compare(final RealmsServer dgn1, final RealmsServer dgn2) {
            return ComparisonChain.start().compareTrueFirst(dgn1.state == State.UNINITIALIZED, dgn2.state == State.UNINITIALIZED).compareTrueFirst(dgn1.expiredTrial, dgn2.expiredTrial).compareTrueFirst(dgn1.owner.equals(this.refOwner), dgn2.owner.equals(this.refOwner)).compareFalseFirst(dgn1.expired, dgn2.expired).compareTrueFirst(dgn1.state == State.OPEN, dgn2.state == State.OPEN).compare(dgn1.id, dgn2.id).result();
        }
    }
    
    public enum State {
        CLOSED, 
        OPEN, 
        UNINITIALIZED;
    }
    
    public enum WorldType {
        NORMAL, 
        MINIGAME, 
        ADVENTUREMAP, 
        EXPERIENCE, 
        INSPIRATION;
    }
}
