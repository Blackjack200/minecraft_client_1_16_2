package net.minecraft.data.recipes;

import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.RecipeSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.function.Consumer;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.tags.Tag;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ItemLike;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.Map;
import java.util.List;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

public class ShapedRecipeBuilder {
    private static final Logger LOGGER;
    private final Item result;
    private final int count;
    private final List<String> rows;
    private final Map<Character, Ingredient> key;
    private final Advancement.Builder advancement;
    private String group;
    
    public ShapedRecipeBuilder(final ItemLike brt, final int integer) {
        this.rows = (List<String>)Lists.newArrayList();
        this.key = (Map<Character, Ingredient>)Maps.newLinkedHashMap();
        this.advancement = Advancement.Builder.advancement();
        this.result = brt.asItem();
        this.count = integer;
    }
    
    public static ShapedRecipeBuilder shaped(final ItemLike brt) {
        return shaped(brt, 1);
    }
    
    public static ShapedRecipeBuilder shaped(final ItemLike brt, final int integer) {
        return new ShapedRecipeBuilder(brt, integer);
    }
    
    public ShapedRecipeBuilder define(final Character character, final Tag<Item> aej) {
        return this.define(character, Ingredient.of(aej));
    }
    
    public ShapedRecipeBuilder define(final Character character, final ItemLike brt) {
        return this.define(character, Ingredient.of(brt));
    }
    
    public ShapedRecipeBuilder define(final Character character, final Ingredient bok) {
        if (this.key.containsKey(character)) {
            throw new IllegalArgumentException(new StringBuilder().append("Symbol '").append(character).append("' is already defined!").toString());
        }
        if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        }
        this.key.put(character, bok);
        return this;
    }
    
    public ShapedRecipeBuilder pattern(final String string) {
        if (!this.rows.isEmpty() && string.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        }
        this.rows.add(string);
        return this;
    }
    
    public ShapedRecipeBuilder unlockedBy(final String string, final CriterionTriggerInstance ag) {
        this.advancement.addCriterion(string, ag);
        return this;
    }
    
    public ShapedRecipeBuilder group(final String string) {
        this.group = string;
        return this;
    }
    
    public void save(final Consumer<FinishedRecipe> consumer) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* consumer */
        //     2: getstatic       net/minecraft/core/Registry.ITEM:Lnet/minecraft/core/DefaultedRegistry;
        //     5: aload_0         /* this */
        //     6: getfield        net/minecraft/data/recipes/ShapedRecipeBuilder.result:Lnet/minecraft/world/item/Item;
        //     9: invokevirtual   net/minecraft/core/DefaultedRegistry.getKey:(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;
        //    12: invokevirtual   net/minecraft/data/recipes/ShapedRecipeBuilder.save:(Ljava/util/function/Consumer;Lnet/minecraft/resources/ResourceLocation;)V
        //    15: return         
        //    Signature:
        //  (Ljava/util/function/Consumer<Lnet/minecraft/data/recipes/FinishedRecipe;>;)V
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  consumer  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitGenericParameter(TypeAnalysis.java:3167)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitGenericParameter(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2526)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1056)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final String string) {
        final ResourceLocation vk4 = Registry.ITEM.getKey(this.result);
        if (new ResourceLocation(string).equals(vk4)) {
            throw new IllegalStateException("Shaped Recipe " + string + " should remove its 'save' argument");
        }
        this.save(consumer, new ResourceLocation(string));
    }
    
    public void save(final Consumer<FinishedRecipe> consumer, final ResourceLocation vk) {
        this.ensureValid(vk);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", (CriterionTriggerInstance)RecipeUnlockedTrigger.unlocked(vk)).rewards(AdvancementRewards.Builder.recipe(vk)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(vk, this.result, this.count, (this.group == null) ? "" : this.group, this.rows, this.key, this.advancement, new ResourceLocation(vk.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + vk.getPath())));
    }
    
    private void ensureValid(final ResourceLocation vk) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("No pattern is defined for shaped recipe ").append(vk).append("!").toString());
        }
        final Set<Character> set3 = (Set<Character>)Sets.newHashSet((Iterable)this.key.keySet());
        set3.remove(' ');
        for (final String string5 : this.rows) {
            for (int integer6 = 0; integer6 < string5.length(); ++integer6) {
                final char character7 = string5.charAt(integer6);
                if (!this.key.containsKey(character7) && character7 != ' ') {
                    throw new IllegalStateException(new StringBuilder().append("Pattern in recipe ").append(vk).append(" uses undefined symbol '").append(character7).append("'").toString());
                }
                set3.remove(character7);
            }
        }
        if (!set3.isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("Ingredients are defined but not used in pattern for recipe ").append(vk).toString());
        }
        if (this.rows.size() == 1 && ((String)this.rows.get(0)).length() == 1) {
            throw new IllegalStateException(new StringBuilder().append("Shaped recipe ").append(vk).append(" only takes in a single item - should it be a shapeless recipe instead?").toString());
        }
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException(new StringBuilder().append("No way of obtaining recipe ").append(vk).toString());
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        
        public Result(final ResourceLocation vk2, final Item blu, final int integer, final String string, final List<String> list, final Map<Character, Ingredient> map, final Advancement.Builder a, final ResourceLocation vk9) {
            this.id = vk2;
            this.result = blu;
            this.count = integer;
            this.group = string;
            this.pattern = list;
            this.key = map;
            this.advancement = a;
            this.advancementId = vk9;
        }
        
        public void serializeRecipeData(final JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }
            final JsonArray jsonArray3 = new JsonArray();
            for (final String string5 : this.pattern) {
                jsonArray3.add(string5);
            }
            jsonObject.add("pattern", (JsonElement)jsonArray3);
            final JsonObject jsonObject2 = new JsonObject();
            for (final Map.Entry<Character, Ingredient> entry6 : this.key.entrySet()) {
                jsonObject2.add(String.valueOf(entry6.getKey()), ((Ingredient)entry6.getValue()).toJson());
            }
            jsonObject.add("key", (JsonElement)jsonObject2);
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                jsonObject3.addProperty("count", (Number)this.count);
            }
            jsonObject.add("result", (JsonElement)jsonObject3);
        }
        
        public RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPED_RECIPE;
        }
        
        public ResourceLocation getId() {
            return this.id;
        }
        
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }
        
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
