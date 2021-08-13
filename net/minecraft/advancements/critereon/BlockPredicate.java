package net.minecraft.advancements.critereon;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.tags.SerializationTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;

public class BlockPredicate {
    public static final BlockPredicate ANY;
    @Nullable
    private final Tag<Block> tag;
    @Nullable
    private final Block block;
    private final StatePropertiesPredicate properties;
    private final NbtPredicate nbt;
    
    public BlockPredicate(@Nullable final Tag<Block> aej, @Nullable final Block bul, final StatePropertiesPredicate cm, final NbtPredicate cb) {
        this.tag = aej;
        this.block = bul;
        this.properties = cm;
        this.nbt = cb;
    }
    
    public boolean matches(final ServerLevel aag, final BlockPos fx) {
        if (this == BlockPredicate.ANY) {
            return true;
        }
        if (!aag.isLoaded(fx)) {
            return false;
        }
        final BlockState cee4 = aag.getBlockState(fx);
        final Block bul5 = cee4.getBlock();
        if (this.tag != null && !this.tag.contains(bul5)) {
            return false;
        }
        if (this.block != null && bul5 != this.block) {
            return false;
        }
        if (!this.properties.matches(cee4)) {
            return false;
        }
        if (this.nbt != NbtPredicate.ANY) {
            final BlockEntity ccg6 = aag.getBlockEntity(fx);
            if (ccg6 == null || !this.nbt.matches(ccg6.save(new CompoundTag()))) {
                return false;
            }
        }
        return true;
    }
    
    public static BlockPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return BlockPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "block");
        final NbtPredicate cb3 = NbtPredicate.fromJson(jsonObject2.get("nbt"));
        Block bul4 = null;
        if (jsonObject2.has("block")) {
            final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "block"));
            bul4 = Registry.BLOCK.get(vk5);
        }
        Tag<Block> aej5 = null;
        if (jsonObject2.has("tag")) {
            final ResourceLocation vk6 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "tag"));
            aej5 = SerializationTags.getInstance().getBlocks().getTag(vk6);
            if (aej5 == null) {
                throw new JsonSyntaxException(new StringBuilder().append("Unknown block tag '").append(vk6).append("'").toString());
            }
        }
        final StatePropertiesPredicate cm6 = StatePropertiesPredicate.fromJson(jsonObject2.get("state"));
        return new BlockPredicate(aej5, bul4, cm6, cb3);
    }
    
    public JsonElement serializeToJson() {
        if (this == BlockPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.block != null) {
            jsonObject2.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
        }
        if (this.tag != null) {
            jsonObject2.addProperty("tag", SerializationTags.getInstance().getBlocks().getIdOrThrow(this.tag).toString());
        }
        jsonObject2.add("nbt", this.nbt.serializeToJson());
        jsonObject2.add("state", this.properties.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new BlockPredicate(null, null, StatePropertiesPredicate.ANY, NbtPredicate.ANY);
    }
    
    public static class Builder {
        @Nullable
        private Block block;
        @Nullable
        private Tag<Block> blocks;
        private StatePropertiesPredicate properties;
        private NbtPredicate nbt;
        
        private Builder() {
            this.properties = StatePropertiesPredicate.ANY;
            this.nbt = NbtPredicate.ANY;
        }
        
        public static Builder block() {
            return new Builder();
        }
        
        public Builder of(final Block bul) {
            this.block = bul;
            return this;
        }
        
        public Builder of(final Tag<Block> aej) {
            this.blocks = aej;
            return this;
        }
        
        public Builder setProperties(final StatePropertiesPredicate cm) {
            this.properties = cm;
            return this;
        }
        
        public BlockPredicate build() {
            return new BlockPredicate(this.blocks, this.block, this.properties, this.nbt);
        }
    }
}
