package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Date;
import org.apache.logging.log4j.Logger;

public class PendingInvite extends ValueObject {
    private static final Logger LOGGER;
    public String invitationId;
    public String worldName;
    public String worldOwnerName;
    public String worldOwnerUuid;
    public Date date;
    
    public static PendingInvite parse(final JsonObject jsonObject) {
        final PendingInvite dgh2 = new PendingInvite();
        try {
            dgh2.invitationId = JsonUtils.getStringOr("invitationId", jsonObject, "");
            dgh2.worldName = JsonUtils.getStringOr("worldName", jsonObject, "");
            dgh2.worldOwnerName = JsonUtils.getStringOr("worldOwnerName", jsonObject, "");
            dgh2.worldOwnerUuid = JsonUtils.getStringOr("worldOwnerUuid", jsonObject, "");
            dgh2.date = JsonUtils.getDateOr("date", jsonObject);
        }
        catch (Exception exception3) {
            PendingInvite.LOGGER.error("Could not parse PendingInvite: " + exception3.getMessage());
        }
        return dgh2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
