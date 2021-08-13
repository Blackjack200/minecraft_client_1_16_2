package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.common.annotations.VisibleForTesting;
import java.util.regex.Matcher;
import java.net.URISyntaxException;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import java.net.URI;
import javax.annotation.Nullable;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;

public class UploadInfo extends ValueObject {
    private static final Logger LOGGER;
    private static final Pattern URI_SCHEMA_PATTERN;
    private final boolean worldClosed;
    @Nullable
    private final String token;
    private final URI uploadEndpoint;
    
    private UploadInfo(final boolean boolean1, @Nullable final String string, final URI uRI) {
        this.worldClosed = boolean1;
        this.token = string;
        this.uploadEndpoint = uRI;
    }
    
    @Nullable
    public static UploadInfo parse(final String string) {
        try {
            final JsonParser jsonParser2 = new JsonParser();
            final JsonObject jsonObject3 = jsonParser2.parse(string).getAsJsonObject();
            final String string2 = JsonUtils.getStringOr("uploadEndpoint", jsonObject3, (String)null);
            if (string2 != null) {
                final int integer5 = JsonUtils.getIntOr("port", jsonObject3, -1);
                final URI uRI6 = assembleUri(string2, integer5);
                if (uRI6 != null) {
                    final boolean boolean7 = JsonUtils.getBooleanOr("worldClosed", jsonObject3, false);
                    final String string3 = JsonUtils.getStringOr("token", jsonObject3, (String)null);
                    return new UploadInfo(boolean7, string3, uRI6);
                }
            }
        }
        catch (Exception exception2) {
            UploadInfo.LOGGER.error("Could not parse UploadInfo: " + exception2.getMessage());
        }
        return null;
    }
    
    @Nullable
    @VisibleForTesting
    public static URI assembleUri(final String string, final int integer) {
        final Matcher matcher3 = UploadInfo.URI_SCHEMA_PATTERN.matcher((CharSequence)string);
        final String string2 = ensureEndpointSchema(string, matcher3);
        try {
            final URI uRI5 = new URI(string2);
            final int integer2 = selectPortOrDefault(integer, uRI5.getPort());
            if (integer2 != uRI5.getPort()) {
                return new URI(uRI5.getScheme(), uRI5.getUserInfo(), uRI5.getHost(), integer2, uRI5.getPath(), uRI5.getQuery(), uRI5.getFragment());
            }
            return uRI5;
        }
        catch (URISyntaxException uRISyntaxException5) {
            UploadInfo.LOGGER.warn("Failed to parse URI {}", string2, uRISyntaxException5);
            return null;
        }
    }
    
    private static int selectPortOrDefault(final int integer1, final int integer2) {
        if (integer1 != -1) {
            return integer1;
        }
        if (integer2 != -1) {
            return integer2;
        }
        return 8080;
    }
    
    private static String ensureEndpointSchema(final String string, final Matcher matcher) {
        if (matcher.find()) {
            return string;
        }
        return "http://" + string;
    }
    
    public static String createRequest(@Nullable final String string) {
        final JsonObject jsonObject2 = new JsonObject();
        if (string != null) {
            jsonObject2.addProperty("token", string);
        }
        return jsonObject2.toString();
    }
    
    @Nullable
    public String getToken() {
        return this.token;
    }
    
    public URI getUploadEndpoint() {
        return this.uploadEndpoint;
    }
    
    public boolean isWorldClosed() {
        return this.worldClosed;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        URI_SCHEMA_PATTERN = Pattern.compile("^[a-zA-Z][-a-zA-Z0-9+.]+:");
    }
}
