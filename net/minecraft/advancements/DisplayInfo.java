package net.minecraft.advancements;

import net.minecraft.core.Registry;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public class DisplayInfo {
    private final Component title;
    private final Component description;
    private final ItemStack icon;
    private final ResourceLocation background;
    private final FrameType frame;
    private final boolean showToast;
    private final boolean announceChat;
    private final boolean hidden;
    private float x;
    private float y;
    
    public DisplayInfo(final ItemStack bly, final Component nr2, final Component nr3, @Nullable final ResourceLocation vk, final FrameType ai, final boolean boolean6, final boolean boolean7, final boolean boolean8) {
        this.title = nr2;
        this.description = nr3;
        this.icon = bly;
        this.background = vk;
        this.frame = ai;
        this.showToast = boolean6;
        this.announceChat = boolean7;
        this.hidden = boolean8;
    }
    
    public void setLocation(final float float1, final float float2) {
        this.x = float1;
        this.y = float2;
    }
    
    public Component getTitle() {
        return this.title;
    }
    
    public Component getDescription() {
        return this.description;
    }
    
    public ItemStack getIcon() {
        return this.icon;
    }
    
    @Nullable
    public ResourceLocation getBackground() {
        return this.background;
    }
    
    public FrameType getFrame() {
        return this.frame;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public boolean shouldShowToast() {
        return this.showToast;
    }
    
    public boolean shouldAnnounceChat() {
        return this.announceChat;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public static DisplayInfo fromJson(final JsonObject jsonObject) {
        final Component nr2 = Component.Serializer.fromJson(jsonObject.get("title"));
        final Component nr3 = Component.Serializer.fromJson(jsonObject.get("description"));
        if (nr2 == null || nr3 == null) {
            throw new JsonSyntaxException("Both title and description must be set");
        }
        final ItemStack bly4 = getIcon(GsonHelper.getAsJsonObject(jsonObject, "icon"));
        final ResourceLocation vk5 = jsonObject.has("background") ? new ResourceLocation(GsonHelper.getAsString(jsonObject, "background")) : null;
        final FrameType ai6 = jsonObject.has("frame") ? FrameType.byName(GsonHelper.getAsString(jsonObject, "frame")) : FrameType.TASK;
        final boolean boolean7 = GsonHelper.getAsBoolean(jsonObject, "show_toast", true);
        final boolean boolean8 = GsonHelper.getAsBoolean(jsonObject, "announce_to_chat", true);
        final boolean boolean9 = GsonHelper.getAsBoolean(jsonObject, "hidden", false);
        return new DisplayInfo(bly4, nr2, nr3, vk5, ai6, boolean7, boolean8, boolean9);
    }
    
    private static ItemStack getIcon(final JsonObject jsonObject) {
        if (!jsonObject.has("item")) {
            throw new JsonSyntaxException("Unsupported icon type, currently only items are supported (add 'item' key)");
        }
        final Item blu2 = GsonHelper.getAsItem(jsonObject, "item");
        if (jsonObject.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }
        final ItemStack bly3 = new ItemStack(blu2);
        if (jsonObject.has("nbt")) {
            try {
                final CompoundTag md4 = TagParser.parseTag(GsonHelper.convertToString(jsonObject.get("nbt"), "nbt"));
                bly3.setTag(md4);
            }
            catch (CommandSyntaxException commandSyntaxException4) {
                throw new JsonSyntaxException("Invalid nbt tag: " + commandSyntaxException4.getMessage());
            }
        }
        return bly3;
    }
    
    public void serializeToNetwork(final FriendlyByteBuf nf) {
        nf.writeComponent(this.title);
        nf.writeComponent(this.description);
        nf.writeItem(this.icon);
        nf.writeEnum(this.frame);
        int integer3 = 0;
        if (this.background != null) {
            integer3 |= 0x1;
        }
        if (this.showToast) {
            integer3 |= 0x2;
        }
        if (this.hidden) {
            integer3 |= 0x4;
        }
        nf.writeInt(integer3);
        if (this.background != null) {
            nf.writeResourceLocation(this.background);
        }
        nf.writeFloat(this.x);
        nf.writeFloat(this.y);
    }
    
    public static DisplayInfo fromNetwork(final FriendlyByteBuf nf) {
        final Component nr2 = nf.readComponent();
        final Component nr3 = nf.readComponent();
        final ItemStack bly4 = nf.readItem();
        final FrameType ai5 = nf.<FrameType>readEnum(FrameType.class);
        final int integer6 = nf.readInt();
        final ResourceLocation vk7 = ((integer6 & 0x1) != 0x0) ? nf.readResourceLocation() : null;
        final boolean boolean8 = (integer6 & 0x2) != 0x0;
        final boolean boolean9 = (integer6 & 0x4) != 0x0;
        final DisplayInfo ah10 = new DisplayInfo(bly4, nr2, nr3, vk7, ai5, boolean8, false, boolean9);
        ah10.setLocation(nf.readFloat(), nf.readFloat());
        return ah10;
    }
    
    public JsonElement serializeToJson() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("icon", (JsonElement)this.serializeIcon());
        jsonObject2.add("title", Component.Serializer.toJsonTree(this.title));
        jsonObject2.add("description", Component.Serializer.toJsonTree(this.description));
        jsonObject2.addProperty("frame", this.frame.getName());
        jsonObject2.addProperty("show_toast", Boolean.valueOf(this.showToast));
        jsonObject2.addProperty("announce_to_chat", Boolean.valueOf(this.announceChat));
        jsonObject2.addProperty("hidden", Boolean.valueOf(this.hidden));
        if (this.background != null) {
            jsonObject2.addProperty("background", this.background.toString());
        }
        return (JsonElement)jsonObject2;
    }
    
    private JsonObject serializeIcon() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("item", Registry.ITEM.getKey(this.icon.getItem()).toString());
        if (this.icon.hasTag()) {
            jsonObject2.addProperty("nbt", this.icon.getTag().toString());
        }
        return jsonObject2;
    }
}
