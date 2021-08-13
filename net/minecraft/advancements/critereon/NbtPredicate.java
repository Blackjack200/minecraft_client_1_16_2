package net.minecraft.advancements.critereon;

import net.minecraft.world.entity.player.Player;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;

public class NbtPredicate {
    public static final NbtPredicate ANY;
    @Nullable
    private final CompoundTag tag;
    
    public NbtPredicate(@Nullable final CompoundTag md) {
        this.tag = md;
    }
    
    public boolean matches(final ItemStack bly) {
        return this == NbtPredicate.ANY || this.matches(bly.getTag());
    }
    
    public boolean matches(final Entity apx) {
        return this == NbtPredicate.ANY || this.matches(getEntityTagToCompare(apx));
    }
    
    public boolean matches(@Nullable final Tag mt) {
        if (mt == null) {
            return this == NbtPredicate.ANY;
        }
        return this.tag == null || NbtUtils.compareNbt(this.tag, mt, true);
    }
    
    public JsonElement serializeToJson() {
        if (this == NbtPredicate.ANY || this.tag == null) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        return (JsonElement)new JsonPrimitive(this.tag.toString());
    }
    
    public static NbtPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return NbtPredicate.ANY;
        }
        CompoundTag md2;
        try {
            md2 = TagParser.parseTag(GsonHelper.convertToString(jsonElement, "nbt"));
        }
        catch (CommandSyntaxException commandSyntaxException3) {
            throw new JsonSyntaxException("Invalid nbt tag: " + commandSyntaxException3.getMessage());
        }
        return new NbtPredicate(md2);
    }
    
    public static CompoundTag getEntityTagToCompare(final Entity apx) {
        final CompoundTag md2 = apx.saveWithoutId(new CompoundTag());
        if (apx instanceof Player) {
            final ItemStack bly3 = ((Player)apx).inventory.getSelected();
            if (!bly3.isEmpty()) {
                md2.put("SelectedItem", (Tag)bly3.save(new CompoundTag()));
            }
        }
        return md2;
    }
    
    static {
        ANY = new NbtPredicate(null);
    }
}
