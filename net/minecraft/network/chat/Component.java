package net.minecraft.network.chat;

import java.util.function.Supplier;
import net.minecraft.Util;
import com.google.gson.TypeAdapterFactory;
import net.minecraft.util.LowerCaseEnumTypeAdapterFactory;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.StringReader;
import com.google.gson.JsonPrimitive;
import java.util.Map;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import java.lang.reflect.Field;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.Optional;
import com.mojang.brigadier.Message;

public interface Component extends Message, FormattedText {
    Style getStyle();
    
    String getContents();
    
    default String getString() {
        return super.getString();
    }
    
    default String getString(final int integer) {
        final StringBuilder stringBuilder3 = new StringBuilder();
        final StringBuilder sb;
        final int integer2;
        this.visit(string -> {
            integer2 = integer - sb.length();
            if (integer2 <= 0) {
                return (java.util.Optional<Object>)Component.STOP_ITERATION;
            }
            else {
                sb.append((string.length() <= integer2) ? string : string.substring(0, integer2));
                return (java.util.Optional<Object>)Optional.empty();
            }
        });
        return stringBuilder3.toString();
    }
    
    List<Component> getSiblings();
    
    MutableComponent plainCopy();
    
    MutableComponent copy();
    
    FormattedCharSequence getVisualOrderText();
    
    default <T> Optional<T> visit(final StyledContentConsumer<T> b, final Style ob) {
        final Style ob2 = this.getStyle().applyTo(ob);
        final Optional<T> optional5 = this.visitSelf((StyledContentConsumer<Object>)b, ob2);
        if (optional5.isPresent()) {
            return optional5;
        }
        for (final Component nr7 : this.getSiblings()) {
            final Optional<T> optional6 = nr7.visit((StyledContentConsumer<Object>)b, ob2);
            if (optional6.isPresent()) {
                return optional6;
            }
        }
        return (Optional<T>)Optional.empty();
    }
    
    default <T> Optional<T> visit(final ContentConsumer<T> a) {
        final Optional<T> optional3 = this.visitSelf((ContentConsumer<Object>)a);
        if (optional3.isPresent()) {
            return optional3;
        }
        for (final Component nr5 : this.getSiblings()) {
            final Optional<T> optional4 = nr5.visit((ContentConsumer<Object>)a);
            if (optional4.isPresent()) {
                return optional4;
            }
        }
        return (Optional<T>)Optional.empty();
    }
    
    default <T> Optional<T> visitSelf(final StyledContentConsumer<T> b, final Style ob) {
        return b.accept(ob, this.getContents());
    }
    
    default <T> Optional<T> visitSelf(final ContentConsumer<T> a) {
        return a.accept(this.getContents());
    }
    
    default Component nullToEmpty(@Nullable final String string) {
        return (string != null) ? new TextComponent(string) : TextComponent.EMPTY;
    }
    
    public static class Serializer implements JsonDeserializer<MutableComponent>, JsonSerializer<Component> {
        private static final Gson GSON;
        private static final Field JSON_READER_POS;
        private static final Field JSON_READER_LINESTART;
        
        public MutableComponent deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonPrimitive()) {
                return new TextComponent(jsonElement.getAsString());
            }
            if (jsonElement.isJsonObject()) {
                final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
                MutableComponent nx6;
                if (jsonObject5.has("text")) {
                    nx6 = new TextComponent(GsonHelper.getAsString(jsonObject5, "text"));
                }
                else if (jsonObject5.has("translate")) {
                    final String string7 = GsonHelper.getAsString(jsonObject5, "translate");
                    if (jsonObject5.has("with")) {
                        final JsonArray jsonArray8 = GsonHelper.getAsJsonArray(jsonObject5, "with");
                        final Object[] arr9 = new Object[jsonArray8.size()];
                        for (int integer10 = 0; integer10 < arr9.length; ++integer10) {
                            arr9[integer10] = this.deserialize(jsonArray8.get(integer10), type, jsonDeserializationContext);
                            if (arr9[integer10] instanceof TextComponent) {
                                final TextComponent oe11 = (TextComponent)arr9[integer10];
                                if (oe11.getStyle().isEmpty() && oe11.getSiblings().isEmpty()) {
                                    arr9[integer10] = oe11.getText();
                                }
                            }
                        }
                        nx6 = new TranslatableComponent(string7, arr9);
                    }
                    else {
                        nx6 = new TranslatableComponent(string7);
                    }
                }
                else if (jsonObject5.has("score")) {
                    final JsonObject jsonObject6 = GsonHelper.getAsJsonObject(jsonObject5, "score");
                    if (!jsonObject6.has("name") || !jsonObject6.has("objective")) {
                        throw new JsonParseException("A score component needs a least a name and an objective");
                    }
                    nx6 = new ScoreComponent(GsonHelper.getAsString(jsonObject6, "name"), GsonHelper.getAsString(jsonObject6, "objective"));
                }
                else if (jsonObject5.has("selector")) {
                    nx6 = new SelectorComponent(GsonHelper.getAsString(jsonObject5, "selector"));
                }
                else if (jsonObject5.has("keybind")) {
                    nx6 = new KeybindComponent(GsonHelper.getAsString(jsonObject5, "keybind"));
                }
                else {
                    if (!jsonObject5.has("nbt")) {
                        throw new JsonParseException(new StringBuilder().append("Don't know how to turn ").append(jsonElement).append(" into a Component").toString());
                    }
                    final String string7 = GsonHelper.getAsString(jsonObject5, "nbt");
                    final boolean boolean8 = GsonHelper.getAsBoolean(jsonObject5, "interpret", false);
                    if (jsonObject5.has("block")) {
                        nx6 = new NbtComponent.BlockNbtComponent(string7, boolean8, GsonHelper.getAsString(jsonObject5, "block"));
                    }
                    else if (jsonObject5.has("entity")) {
                        nx6 = new NbtComponent.EntityNbtComponent(string7, boolean8, GsonHelper.getAsString(jsonObject5, "entity"));
                    }
                    else {
                        if (!jsonObject5.has("storage")) {
                            throw new JsonParseException(new StringBuilder().append("Don't know how to turn ").append(jsonElement).append(" into a Component").toString());
                        }
                        nx6 = new NbtComponent.StorageNbtComponent(string7, boolean8, new ResourceLocation(GsonHelper.getAsString(jsonObject5, "storage")));
                    }
                }
                if (jsonObject5.has("extra")) {
                    final JsonArray jsonArray9 = GsonHelper.getAsJsonArray(jsonObject5, "extra");
                    if (jsonArray9.size() <= 0) {
                        throw new JsonParseException("Unexpected empty array of components");
                    }
                    for (int integer11 = 0; integer11 < jsonArray9.size(); ++integer11) {
                        nx6.append(this.deserialize(jsonArray9.get(integer11), type, jsonDeserializationContext));
                    }
                }
                nx6.setStyle((Style)jsonDeserializationContext.deserialize(jsonElement, (Type)Style.class));
                return nx6;
            }
            if (jsonElement.isJsonArray()) {
                final JsonArray jsonArray10 = jsonElement.getAsJsonArray();
                MutableComponent nx6 = null;
                for (final JsonElement jsonElement2 : jsonArray10) {
                    final MutableComponent nx7 = this.deserialize(jsonElement2, (Type)jsonElement2.getClass(), jsonDeserializationContext);
                    if (nx6 == null) {
                        nx6 = nx7;
                    }
                    else {
                        nx6.append(nx7);
                    }
                }
                return nx6;
            }
            throw new JsonParseException(new StringBuilder().append("Don't know how to turn ").append(jsonElement).append(" into a Component").toString());
        }
        
        private void serializeStyle(final Style ob, final JsonObject jsonObject, final JsonSerializationContext jsonSerializationContext) {
            final JsonElement jsonElement5 = jsonSerializationContext.serialize(ob);
            if (jsonElement5.isJsonObject()) {
                final JsonObject jsonObject2 = (JsonObject)jsonElement5;
                for (final Map.Entry<String, JsonElement> entry8 : jsonObject2.entrySet()) {
                    jsonObject.add((String)entry8.getKey(), (JsonElement)entry8.getValue());
                }
            }
        }
        
        public JsonElement serialize(final Component nr, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            if (!nr.getStyle().isEmpty()) {
                this.serializeStyle(nr.getStyle(), jsonObject5, jsonSerializationContext);
            }
            if (!nr.getSiblings().isEmpty()) {
                final JsonArray jsonArray6 = new JsonArray();
                for (final Component nr2 : nr.getSiblings()) {
                    jsonArray6.add(this.serialize(nr2, (Type)nr2.getClass(), jsonSerializationContext));
                }
                jsonObject5.add("extra", (JsonElement)jsonArray6);
            }
            if (nr instanceof TextComponent) {
                jsonObject5.addProperty("text", ((TextComponent)nr).getText());
            }
            else if (nr instanceof TranslatableComponent) {
                final TranslatableComponent of6 = (TranslatableComponent)nr;
                jsonObject5.addProperty("translate", of6.getKey());
                if (of6.getArgs() != null && of6.getArgs().length > 0) {
                    final JsonArray jsonArray7 = new JsonArray();
                    for (final Object object11 : of6.getArgs()) {
                        if (object11 instanceof Component) {
                            jsonArray7.add(this.serialize((Component)object11, (Type)object11.getClass(), jsonSerializationContext));
                        }
                        else {
                            jsonArray7.add((JsonElement)new JsonPrimitive(String.valueOf(object11)));
                        }
                    }
                    jsonObject5.add("with", (JsonElement)jsonArray7);
                }
            }
            else if (nr instanceof ScoreComponent) {
                final ScoreComponent nz6 = (ScoreComponent)nr;
                final JsonObject jsonObject6 = new JsonObject();
                jsonObject6.addProperty("name", nz6.getName());
                jsonObject6.addProperty("objective", nz6.getObjective());
                jsonObject5.add("score", (JsonElement)jsonObject6);
            }
            else if (nr instanceof SelectorComponent) {
                final SelectorComponent oa6 = (SelectorComponent)nr;
                jsonObject5.addProperty("selector", oa6.getPattern());
            }
            else if (nr instanceof KeybindComponent) {
                final KeybindComponent nw6 = (KeybindComponent)nr;
                jsonObject5.addProperty("keybind", nw6.getName());
            }
            else {
                if (!(nr instanceof NbtComponent)) {
                    throw new IllegalArgumentException(new StringBuilder().append("Don't know how to serialize ").append(nr).append(" as a Component").toString());
                }
                final NbtComponent ny6 = (NbtComponent)nr;
                jsonObject5.addProperty("nbt", ny6.getNbtPath());
                jsonObject5.addProperty("interpret", Boolean.valueOf(ny6.isInterpreting()));
                if (nr instanceof NbtComponent.BlockNbtComponent) {
                    final NbtComponent.BlockNbtComponent a7 = (NbtComponent.BlockNbtComponent)nr;
                    jsonObject5.addProperty("block", a7.getPos());
                }
                else if (nr instanceof NbtComponent.EntityNbtComponent) {
                    final NbtComponent.EntityNbtComponent b7 = (NbtComponent.EntityNbtComponent)nr;
                    jsonObject5.addProperty("entity", b7.getSelector());
                }
                else {
                    if (!(nr instanceof NbtComponent.StorageNbtComponent)) {
                        throw new IllegalArgumentException(new StringBuilder().append("Don't know how to serialize ").append(nr).append(" as a Component").toString());
                    }
                    final NbtComponent.StorageNbtComponent c7 = (NbtComponent.StorageNbtComponent)nr;
                    jsonObject5.addProperty("storage", c7.getId().toString());
                }
            }
            return (JsonElement)jsonObject5;
        }
        
        public static String toJson(final Component nr) {
            return Serializer.GSON.toJson(nr);
        }
        
        public static JsonElement toJsonTree(final Component nr) {
            return Serializer.GSON.toJsonTree(nr);
        }
        
        @Nullable
        public static MutableComponent fromJson(final String string) {
            return GsonHelper.<MutableComponent>fromJson(Serializer.GSON, string, MutableComponent.class, false);
        }
        
        @Nullable
        public static MutableComponent fromJson(final JsonElement jsonElement) {
            return (MutableComponent)Serializer.GSON.fromJson(jsonElement, (Class)MutableComponent.class);
        }
        
        @Nullable
        public static MutableComponent fromJsonLenient(final String string) {
            return GsonHelper.<MutableComponent>fromJson(Serializer.GSON, string, MutableComponent.class, true);
        }
        
        public static MutableComponent fromJson(final StringReader stringReader) {
            try {
                final JsonReader jsonReader2 = new JsonReader((Reader)new java.io.StringReader(stringReader.getRemaining()));
                jsonReader2.setLenient(false);
                final MutableComponent nx3 = (MutableComponent)Serializer.GSON.getAdapter((Class)MutableComponent.class).read(jsonReader2);
                stringReader.setCursor(stringReader.getCursor() + getPos(jsonReader2));
                return nx3;
            }
            catch (IOException | StackOverflowError ex) {
                final Throwable t;
                final Throwable throwable2 = t;
                throw new JsonParseException(throwable2);
            }
        }
        
        private static int getPos(final JsonReader jsonReader) {
            try {
                return Serializer.JSON_READER_POS.getInt(jsonReader) - Serializer.JSON_READER_LINESTART.getInt(jsonReader) + 1;
            }
            catch (IllegalAccessException illegalAccessException2) {
                throw new IllegalStateException("Couldn't read position of JsonReader", (Throwable)illegalAccessException2);
            }
        }
        
        static {
            GSON = Util.<Gson>make((java.util.function.Supplier<Gson>)(() -> {
                final GsonBuilder gsonBuilder1 = new GsonBuilder();
                gsonBuilder1.disableHtmlEscaping();
                gsonBuilder1.registerTypeHierarchyAdapter((Class)Component.class, new Serializer());
                gsonBuilder1.registerTypeHierarchyAdapter((Class)Style.class, new Style.Serializer());
                gsonBuilder1.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
                return gsonBuilder1.create();
            }));
            JSON_READER_POS = Util.<Field>make((java.util.function.Supplier<Field>)(() -> {
                try {
                    final JsonReader jsonReader = new JsonReader((Reader)new java.io.StringReader(""));
                    final Field field1 = JsonReader.class.getDeclaredField("pos");
                    field1.setAccessible(true);
                    return field1;
                }
                catch (NoSuchFieldException noSuchFieldException1) {
                    throw new IllegalStateException("Couldn't get field 'pos' for JsonReader", (Throwable)noSuchFieldException1);
                }
            }));
            JSON_READER_LINESTART = Util.<Field>make((java.util.function.Supplier<Field>)(() -> {
                try {
                    final JsonReader jsonReader = new JsonReader((Reader)new java.io.StringReader(""));
                    final Field field1 = JsonReader.class.getDeclaredField("lineStart");
                    field1.setAccessible(true);
                    return field1;
                }
                catch (NoSuchFieldException noSuchFieldException1) {
                    throw new IllegalStateException("Couldn't get field 'lineStart' for JsonReader", (Throwable)noSuchFieldException1);
                }
            }));
        }
    }
}
