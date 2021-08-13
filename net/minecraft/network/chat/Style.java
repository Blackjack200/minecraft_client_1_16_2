package net.minecraft.network.chat;

import com.google.gson.JsonSerializationContext;
import net.minecraft.ResourceLocationException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class Style {
    public static final Style EMPTY;
    public static final ResourceLocation DEFAULT_FONT;
    @Nullable
    private final TextColor color;
    @Nullable
    private final Boolean bold;
    @Nullable
    private final Boolean italic;
    @Nullable
    private final Boolean underlined;
    @Nullable
    private final Boolean strikethrough;
    @Nullable
    private final Boolean obfuscated;
    @Nullable
    private final ClickEvent clickEvent;
    @Nullable
    private final HoverEvent hoverEvent;
    @Nullable
    private final String insertion;
    @Nullable
    private final ResourceLocation font;
    
    private Style(@Nullable final TextColor od, @Nullable final Boolean boolean2, @Nullable final Boolean boolean3, @Nullable final Boolean boolean4, @Nullable final Boolean boolean5, @Nullable final Boolean boolean6, @Nullable final ClickEvent np, @Nullable final HoverEvent nv, @Nullable final String string, @Nullable final ResourceLocation vk) {
        this.color = od;
        this.bold = boolean2;
        this.italic = boolean3;
        this.underlined = boolean4;
        this.strikethrough = boolean5;
        this.obfuscated = boolean6;
        this.clickEvent = np;
        this.hoverEvent = nv;
        this.insertion = string;
        this.font = vk;
    }
    
    @Nullable
    public TextColor getColor() {
        return this.color;
    }
    
    public boolean isBold() {
        return this.bold == Boolean.TRUE;
    }
    
    public boolean isItalic() {
        return this.italic == Boolean.TRUE;
    }
    
    public boolean isStrikethrough() {
        return this.strikethrough == Boolean.TRUE;
    }
    
    public boolean isUnderlined() {
        return this.underlined == Boolean.TRUE;
    }
    
    public boolean isObfuscated() {
        return this.obfuscated == Boolean.TRUE;
    }
    
    public boolean isEmpty() {
        return this == Style.EMPTY;
    }
    
    @Nullable
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }
    
    @Nullable
    public HoverEvent getHoverEvent() {
        return this.hoverEvent;
    }
    
    @Nullable
    public String getInsertion() {
        return this.insertion;
    }
    
    public ResourceLocation getFont() {
        return (this.font != null) ? this.font : Style.DEFAULT_FONT;
    }
    
    public Style withColor(@Nullable final TextColor od) {
        return new Style(od, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style withColor(@Nullable final ChatFormatting k) {
        return this.withColor((k != null) ? TextColor.fromLegacyFormat(k) : null);
    }
    
    public Style withBold(@Nullable final Boolean boolean1) {
        return new Style(this.color, boolean1, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style withItalic(@Nullable final Boolean boolean1) {
        return new Style(this.color, this.bold, boolean1, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style withUnderlined(@Nullable final Boolean boolean1) {
        return new Style(this.color, this.bold, this.italic, boolean1, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style withClickEvent(@Nullable final ClickEvent np) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, np, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style withHoverEvent(@Nullable final HoverEvent nv) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, nv, this.insertion, this.font);
    }
    
    public Style withInsertion(@Nullable final String string) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, string, this.font);
    }
    
    public Style withFont(@Nullable final ResourceLocation vk) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, vk);
    }
    
    public Style applyFormat(final ChatFormatting k) {
        TextColor od3 = this.color;
        Boolean boolean4 = this.bold;
        Boolean boolean5 = this.italic;
        Boolean boolean6 = this.strikethrough;
        Boolean boolean7 = this.underlined;
        Boolean boolean8 = this.obfuscated;
        switch (k) {
            case OBFUSCATED: {
                boolean8 = true;
                break;
            }
            case BOLD: {
                boolean4 = true;
                break;
            }
            case STRIKETHROUGH: {
                boolean6 = true;
                break;
            }
            case UNDERLINE: {
                boolean7 = true;
                break;
            }
            case ITALIC: {
                boolean5 = true;
                break;
            }
            case RESET: {
                return Style.EMPTY;
            }
            default: {
                od3 = TextColor.fromLegacyFormat(k);
                break;
            }
        }
        return new Style(od3, boolean4, boolean5, boolean7, boolean6, boolean8, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style applyLegacyFormat(final ChatFormatting k) {
        TextColor od3 = this.color;
        Boolean boolean4 = this.bold;
        Boolean boolean5 = this.italic;
        Boolean boolean6 = this.strikethrough;
        Boolean boolean7 = this.underlined;
        Boolean boolean8 = this.obfuscated;
        switch (k) {
            case OBFUSCATED: {
                boolean8 = true;
                break;
            }
            case BOLD: {
                boolean4 = true;
                break;
            }
            case STRIKETHROUGH: {
                boolean6 = true;
                break;
            }
            case UNDERLINE: {
                boolean7 = true;
                break;
            }
            case ITALIC: {
                boolean5 = true;
                break;
            }
            case RESET: {
                return Style.EMPTY;
            }
            default: {
                boolean8 = false;
                boolean4 = false;
                boolean6 = false;
                boolean7 = false;
                boolean5 = false;
                od3 = TextColor.fromLegacyFormat(k);
                break;
            }
        }
        return new Style(od3, boolean4, boolean5, boolean7, boolean6, boolean8, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style applyFormats(final ChatFormatting... arr) {
        TextColor od3 = this.color;
        Boolean boolean4 = this.bold;
        Boolean boolean5 = this.italic;
        Boolean boolean6 = this.strikethrough;
        Boolean boolean7 = this.underlined;
        Boolean boolean8 = this.obfuscated;
        for (final ChatFormatting k12 : arr) {
            switch (k12) {
                case OBFUSCATED: {
                    boolean8 = true;
                    break;
                }
                case BOLD: {
                    boolean4 = true;
                    break;
                }
                case STRIKETHROUGH: {
                    boolean6 = true;
                    break;
                }
                case UNDERLINE: {
                    boolean7 = true;
                    break;
                }
                case ITALIC: {
                    boolean5 = true;
                    break;
                }
                case RESET: {
                    return Style.EMPTY;
                }
                default: {
                    od3 = TextColor.fromLegacyFormat(k12);
                    break;
                }
            }
        }
        return new Style(od3, boolean4, boolean5, boolean7, boolean6, boolean8, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }
    
    public Style applyTo(final Style ob) {
        if (this == Style.EMPTY) {
            return ob;
        }
        if (ob == Style.EMPTY) {
            return this;
        }
        return new Style((this.color != null) ? this.color : ob.color, (this.bold != null) ? this.bold : ob.bold, (this.italic != null) ? this.italic : ob.italic, (this.underlined != null) ? this.underlined : ob.underlined, (this.strikethrough != null) ? this.strikethrough : ob.strikethrough, (this.obfuscated != null) ? this.obfuscated : ob.obfuscated, (this.clickEvent != null) ? this.clickEvent : ob.clickEvent, (this.hoverEvent != null) ? this.hoverEvent : ob.hoverEvent, (this.insertion != null) ? this.insertion : ob.insertion, (this.font != null) ? this.font : ob.font);
    }
    
    public String toString() {
        return new StringBuilder().append("Style{ color=").append(this.color).append(", bold=").append(this.bold).append(", italic=").append(this.italic).append(", underlined=").append(this.underlined).append(", strikethrough=").append(this.strikethrough).append(", obfuscated=").append(this.obfuscated).append(", clickEvent=").append(this.getClickEvent()).append(", hoverEvent=").append(this.getHoverEvent()).append(", insertion=").append(this.getInsertion()).append(", font=").append(this.getFont()).append('}').toString();
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof Style) {
            final Style ob3 = (Style)object;
            return this.isBold() == ob3.isBold() && Objects.equals(this.getColor(), ob3.getColor()) && this.isItalic() == ob3.isItalic() && this.isObfuscated() == ob3.isObfuscated() && this.isStrikethrough() == ob3.isStrikethrough() && this.isUnderlined() == ob3.isUnderlined() && Objects.equals(this.getClickEvent(), ob3.getClickEvent()) && Objects.equals(this.getHoverEvent(), ob3.getHoverEvent()) && Objects.equals(this.getInsertion(), ob3.getInsertion()) && Objects.equals(this.getFont(), ob3.getFont());
        }
        return false;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion });
    }
    
    static {
        EMPTY = new Style(null, null, null, null, null, null, null, null, null, null);
        DEFAULT_FONT = new ResourceLocation("minecraft", "default");
    }
    
    public static class Serializer implements JsonDeserializer<Style>, JsonSerializer<Style> {
        @Nullable
        public Style deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (!jsonElement.isJsonObject()) {
                return null;
            }
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            if (jsonObject5 == null) {
                return null;
            }
            final Boolean boolean6 = getOptionalFlag(jsonObject5, "bold");
            final Boolean boolean7 = getOptionalFlag(jsonObject5, "italic");
            final Boolean boolean8 = getOptionalFlag(jsonObject5, "underlined");
            final Boolean boolean9 = getOptionalFlag(jsonObject5, "strikethrough");
            final Boolean boolean10 = getOptionalFlag(jsonObject5, "obfuscated");
            final TextColor od11 = getTextColor(jsonObject5);
            final String string12 = getInsertion(jsonObject5);
            final ClickEvent np13 = getClickEvent(jsonObject5);
            final HoverEvent nv14 = getHoverEvent(jsonObject5);
            final ResourceLocation vk15 = getFont(jsonObject5);
            return new Style(od11, boolean6, boolean7, boolean8, boolean9, boolean10, np13, nv14, string12, vk15, null);
        }
        
        @Nullable
        private static ResourceLocation getFont(final JsonObject jsonObject) {
            if (jsonObject.has("font")) {
                final String string2 = GsonHelper.getAsString(jsonObject, "font");
                try {
                    return new ResourceLocation(string2);
                }
                catch (ResourceLocationException v3) {
                    throw new JsonSyntaxException("Invalid font name: " + string2);
                }
            }
            return null;
        }
        
        @Nullable
        private static HoverEvent getHoverEvent(final JsonObject jsonObject) {
            if (jsonObject.has("hoverEvent")) {
                final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "hoverEvent");
                final HoverEvent nv3 = HoverEvent.deserialize(jsonObject2);
                if (nv3 != null && nv3.getAction().isAllowedFromServer()) {
                    return nv3;
                }
            }
            return null;
        }
        
        @Nullable
        private static ClickEvent getClickEvent(final JsonObject jsonObject) {
            if (jsonObject.has("clickEvent")) {
                final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "clickEvent");
                final String string3 = GsonHelper.getAsString(jsonObject2, "action", (String)null);
                final ClickEvent.Action a4 = (string3 == null) ? null : ClickEvent.Action.getByName(string3);
                final String string4 = GsonHelper.getAsString(jsonObject2, "value", (String)null);
                if (a4 != null && string4 != null && a4.isAllowedFromServer()) {
                    return new ClickEvent(a4, string4);
                }
            }
            return null;
        }
        
        @Nullable
        private static String getInsertion(final JsonObject jsonObject) {
            return GsonHelper.getAsString(jsonObject, "insertion", (String)null);
        }
        
        @Nullable
        private static TextColor getTextColor(final JsonObject jsonObject) {
            if (jsonObject.has("color")) {
                final String string2 = GsonHelper.getAsString(jsonObject, "color");
                return TextColor.parseColor(string2);
            }
            return null;
        }
        
        @Nullable
        private static Boolean getOptionalFlag(final JsonObject jsonObject, final String string) {
            if (jsonObject.has(string)) {
                return jsonObject.get(string).getAsBoolean();
            }
            return null;
        }
        
        @Nullable
        public JsonElement serialize(final Style ob, final Type type, final JsonSerializationContext jsonSerializationContext) {
            if (ob.isEmpty()) {
                return null;
            }
            final JsonObject jsonObject5 = new JsonObject();
            if (ob.bold != null) {
                jsonObject5.addProperty("bold", ob.bold);
            }
            if (ob.italic != null) {
                jsonObject5.addProperty("italic", ob.italic);
            }
            if (ob.underlined != null) {
                jsonObject5.addProperty("underlined", ob.underlined);
            }
            if (ob.strikethrough != null) {
                jsonObject5.addProperty("strikethrough", ob.strikethrough);
            }
            if (ob.obfuscated != null) {
                jsonObject5.addProperty("obfuscated", ob.obfuscated);
            }
            if (ob.color != null) {
                jsonObject5.addProperty("color", ob.color.serialize());
            }
            if (ob.insertion != null) {
                jsonObject5.add("insertion", jsonSerializationContext.serialize(ob.insertion));
            }
            if (ob.clickEvent != null) {
                final JsonObject jsonObject6 = new JsonObject();
                jsonObject6.addProperty("action", ob.clickEvent.getAction().getName());
                jsonObject6.addProperty("value", ob.clickEvent.getValue());
                jsonObject5.add("clickEvent", (JsonElement)jsonObject6);
            }
            if (ob.hoverEvent != null) {
                jsonObject5.add("hoverEvent", (JsonElement)ob.hoverEvent.serialize());
            }
            if (ob.font != null) {
                jsonObject5.addProperty("font", ob.font.toString());
            }
            return (JsonElement)jsonObject5;
        }
    }
}
