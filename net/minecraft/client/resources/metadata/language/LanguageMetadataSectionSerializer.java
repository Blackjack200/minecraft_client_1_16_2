package net.minecraft.client.resources.metadata.language;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.util.Map;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class LanguageMetadataSectionSerializer implements MetadataSectionSerializer<LanguageMetadataSection> {
    public LanguageMetadataSection fromJson(final JsonObject jsonObject) {
        final Set<LanguageInfo> set3 = (Set<LanguageInfo>)Sets.newHashSet();
        for (final Map.Entry<String, JsonElement> entry5 : jsonObject.entrySet()) {
            final String string6 = (String)entry5.getKey();
            if (string6.length() > 16) {
                throw new JsonParseException("Invalid language->'" + string6 + "': language code must not be more than " + 16 + " characters long");
            }
            final JsonObject jsonObject2 = GsonHelper.convertToJsonObject((JsonElement)entry5.getValue(), "language");
            final String string7 = GsonHelper.getAsString(jsonObject2, "region");
            final String string8 = GsonHelper.getAsString(jsonObject2, "name");
            final boolean boolean10 = GsonHelper.getAsBoolean(jsonObject2, "bidirectional", false);
            if (string7.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + string6 + "'->region: empty value");
            }
            if (string8.isEmpty()) {
                throw new JsonParseException("Invalid language->'" + string6 + "'->name: empty value");
            }
            if (!set3.add(new LanguageInfo(string6, string7, string8, boolean10))) {
                throw new JsonParseException("Duplicate language->'" + string6 + "' defined");
            }
        }
        return new LanguageMetadataSection((Collection<LanguageInfo>)set3);
    }
    
    public String getMetadataSectionName() {
        return "language";
    }
}
