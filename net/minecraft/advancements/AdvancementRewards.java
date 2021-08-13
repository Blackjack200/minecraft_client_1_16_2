package net.minecraft.advancements;

import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.List;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import java.util.Arrays;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.Iterator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;

public class AdvancementRewards {
    public static final AdvancementRewards EMPTY;
    private final int experience;
    private final ResourceLocation[] loot;
    private final ResourceLocation[] recipes;
    private final CommandFunction.CacheableFunction function;
    
    public AdvancementRewards(final int integer, final ResourceLocation[] arr2, final ResourceLocation[] arr3, final CommandFunction.CacheableFunction a) {
        this.experience = integer;
        this.loot = arr2;
        this.recipes = arr3;
        this.function = a;
    }
    
    public void grant(final ServerPlayer aah) {
        aah.giveExperiencePoints(this.experience);
        final LootContext cys3 = new LootContext.Builder(aah.getLevel()).<Entity>withParameter(LootContextParams.THIS_ENTITY, aah).<Vec3>withParameter(LootContextParams.ORIGIN, aah.position()).withRandom(aah.getRandom()).create(LootContextParamSets.ADVANCEMENT_REWARD);
        boolean boolean4 = false;
        for (final ResourceLocation vk8 : this.loot) {
            for (final ItemStack bly10 : aah.server.getLootTables().get(vk8).getRandomItems(cys3)) {
                if (aah.addItem(bly10)) {
                    aah.level.playSound(null, aah.getX(), aah.getY(), aah.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2f, ((aah.getRandom().nextFloat() - aah.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    boolean4 = true;
                }
                else {
                    final ItemEntity bcs11 = aah.drop(bly10, false);
                    if (bcs11 == null) {
                        continue;
                    }
                    bcs11.setNoPickUpDelay();
                    bcs11.setOwner(aah.getUUID());
                }
            }
        }
        if (boolean4) {
            aah.inventoryMenu.broadcastChanges();
        }
        if (this.recipes.length > 0) {
            aah.awardRecipesByKey(this.recipes);
        }
        final MinecraftServer minecraftServer5 = aah.server;
        this.function.get(minecraftServer5.getFunctions()).ifPresent(cy -> minecraftServer5.getFunctions().execute(cy, aah.createCommandSourceStack().withSuppressedOutput().withPermission(2)));
    }
    
    public String toString() {
        return new StringBuilder().append("AdvancementRewards{experience=").append(this.experience).append(", loot=").append(Arrays.toString((Object[])this.loot)).append(", recipes=").append(Arrays.toString((Object[])this.recipes)).append(", function=").append(this.function).append('}').toString();
    }
    
    public JsonElement serializeToJson() {
        if (this == AdvancementRewards.EMPTY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.experience != 0) {
            jsonObject2.addProperty("experience", (Number)this.experience);
        }
        if (this.loot.length > 0) {
            final JsonArray jsonArray3 = new JsonArray();
            for (final ResourceLocation vk7 : this.loot) {
                jsonArray3.add(vk7.toString());
            }
            jsonObject2.add("loot", (JsonElement)jsonArray3);
        }
        if (this.recipes.length > 0) {
            final JsonArray jsonArray3 = new JsonArray();
            for (final ResourceLocation vk7 : this.recipes) {
                jsonArray3.add(vk7.toString());
            }
            jsonObject2.add("recipes", (JsonElement)jsonArray3);
        }
        if (this.function.getId() != null) {
            jsonObject2.addProperty("function", this.function.getId().toString());
        }
        return (JsonElement)jsonObject2;
    }
    
    public static AdvancementRewards deserialize(final JsonObject jsonObject) throws JsonParseException {
        final int integer2 = GsonHelper.getAsInt(jsonObject, "experience", 0);
        final JsonArray jsonArray3 = GsonHelper.getAsJsonArray(jsonObject, "loot", new JsonArray());
        final ResourceLocation[] arr4 = new ResourceLocation[jsonArray3.size()];
        for (int integer3 = 0; integer3 < arr4.length; ++integer3) {
            arr4[integer3] = new ResourceLocation(GsonHelper.convertToString(jsonArray3.get(integer3), new StringBuilder().append("loot[").append(integer3).append("]").toString()));
        }
        final JsonArray jsonArray4 = GsonHelper.getAsJsonArray(jsonObject, "recipes", new JsonArray());
        final ResourceLocation[] arr5 = new ResourceLocation[jsonArray4.size()];
        for (int integer4 = 0; integer4 < arr5.length; ++integer4) {
            arr5[integer4] = new ResourceLocation(GsonHelper.convertToString(jsonArray4.get(integer4), new StringBuilder().append("recipes[").append(integer4).append("]").toString()));
        }
        CommandFunction.CacheableFunction a7;
        if (jsonObject.has("function")) {
            a7 = new CommandFunction.CacheableFunction(new ResourceLocation(GsonHelper.getAsString(jsonObject, "function")));
        }
        else {
            a7 = CommandFunction.CacheableFunction.NONE;
        }
        return new AdvancementRewards(integer2, arr4, arr5, a7);
    }
    
    static {
        EMPTY = new AdvancementRewards(0, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE);
    }
    
    public static class Builder {
        private int experience;
        private final List<ResourceLocation> loot;
        private final List<ResourceLocation> recipes;
        @Nullable
        private ResourceLocation function;
        
        public Builder() {
            this.loot = (List<ResourceLocation>)Lists.newArrayList();
            this.recipes = (List<ResourceLocation>)Lists.newArrayList();
        }
        
        public static Builder experience(final int integer) {
            return new Builder().addExperience(integer);
        }
        
        public Builder addExperience(final int integer) {
            this.experience += integer;
            return this;
        }
        
        public static Builder recipe(final ResourceLocation vk) {
            return new Builder().addRecipe(vk);
        }
        
        public Builder addRecipe(final ResourceLocation vk) {
            this.recipes.add(vk);
            return this;
        }
        
        public AdvancementRewards build() {
            return new AdvancementRewards(this.experience, (ResourceLocation[])this.loot.toArray((Object[])new ResourceLocation[0]), (ResourceLocation[])this.recipes.toArray((Object[])new ResourceLocation[0]), (this.function == null) ? CommandFunction.CacheableFunction.NONE : new CommandFunction.CacheableFunction(this.function));
        }
    }
}
