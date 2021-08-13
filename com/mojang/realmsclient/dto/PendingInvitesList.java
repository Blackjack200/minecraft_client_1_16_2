package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class PendingInvitesList extends ValueObject {
    private static final Logger LOGGER;
    public List<PendingInvite> pendingInvites;
    
    public PendingInvitesList() {
        this.pendingInvites = (List<PendingInvite>)Lists.newArrayList();
    }
    
    public static PendingInvitesList parse(final String string) {
        final PendingInvitesList dgi2 = new PendingInvitesList();
        try {
            final JsonParser jsonParser3 = new JsonParser();
            final JsonObject jsonObject4 = jsonParser3.parse(string).getAsJsonObject();
            if (jsonObject4.get("invites").isJsonArray()) {
                final Iterator<JsonElement> iterator5 = (Iterator<JsonElement>)jsonObject4.get("invites").getAsJsonArray().iterator();
                while (iterator5.hasNext()) {
                    dgi2.pendingInvites.add(PendingInvite.parse(((JsonElement)iterator5.next()).getAsJsonObject()));
                }
            }
        }
        catch (Exception exception3) {
            PendingInvitesList.LOGGER.error("Could not parse PendingInvitesList: " + exception3.getMessage());
        }
        return dgi2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
