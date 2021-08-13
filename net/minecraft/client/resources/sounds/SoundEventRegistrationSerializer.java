package net.minecraft.client.resources.sounds;

import org.apache.commons.lang3.Validate;
import com.google.gson.JsonArray;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import java.util.List;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;

public class SoundEventRegistrationSerializer implements JsonDeserializer<SoundEventRegistration> {
    public SoundEventRegistration deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "entry");
        final boolean boolean6 = GsonHelper.getAsBoolean(jsonObject5, "replace", false);
        final String string7 = GsonHelper.getAsString(jsonObject5, "subtitle", (String)null);
        final List<Sound> list8 = this.getSounds(jsonObject5);
        return new SoundEventRegistration(list8, boolean6, string7);
    }
    
    private List<Sound> getSounds(final JsonObject jsonObject) {
        final List<Sound> list3 = (List<Sound>)Lists.newArrayList();
        if (jsonObject.has("sounds")) {
            final JsonArray jsonArray4 = GsonHelper.getAsJsonArray(jsonObject, "sounds");
            for (int integer5 = 0; integer5 < jsonArray4.size(); ++integer5) {
                final JsonElement jsonElement6 = jsonArray4.get(integer5);
                if (GsonHelper.isStringValue(jsonElement6)) {
                    final String string7 = GsonHelper.convertToString(jsonElement6, "sound");
                    list3.add(new Sound(string7, 1.0f, 1.0f, 1, Sound.Type.FILE, false, false, 16));
                }
                else {
                    list3.add(this.getSound(GsonHelper.convertToJsonObject(jsonElement6, "sound")));
                }
            }
        }
        return list3;
    }
    
    private Sound getSound(final JsonObject jsonObject) {
        final String string3 = GsonHelper.getAsString(jsonObject, "name");
        final Sound.Type a4 = this.getType(jsonObject, Sound.Type.FILE);
        final float float5 = GsonHelper.getAsFloat(jsonObject, "volume", 1.0f);
        Validate.isTrue(float5 > 0.0f, "Invalid volume", new Object[0]);
        final float float6 = GsonHelper.getAsFloat(jsonObject, "pitch", 1.0f);
        Validate.isTrue(float6 > 0.0f, "Invalid pitch", new Object[0]);
        final int integer7 = GsonHelper.getAsInt(jsonObject, "weight", 1);
        Validate.isTrue(integer7 > 0, "Invalid weight", new Object[0]);
        final boolean boolean8 = GsonHelper.getAsBoolean(jsonObject, "preload", false);
        final boolean boolean9 = GsonHelper.getAsBoolean(jsonObject, "stream", false);
        final int integer8 = GsonHelper.getAsInt(jsonObject, "attenuation_distance", 16);
        return new Sound(string3, float5, float6, integer7, a4, boolean9, boolean8, integer8);
    }
    
    private Sound.Type getType(final JsonObject jsonObject, final Sound.Type a) {
        Sound.Type a2 = a;
        if (jsonObject.has("type")) {
            a2 = Sound.Type.getByName(GsonHelper.getAsString(jsonObject, "type"));
            Validate.notNull(a2, "Invalid type", new Object[0]);
        }
        return a2;
    }
}
