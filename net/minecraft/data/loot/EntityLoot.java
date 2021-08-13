package net.minecraft.data.loot;

import com.google.common.collect.ImmutableSet;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.RandomIntGenerator;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.ItemLike;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import java.util.Set;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntityLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    private static final EntityPredicate.Builder ENTITY_ON_FIRE;
    private static final Set<EntityType<?>> SPECIAL_LOOT_TABLE_TYPES;
    private final Map<ResourceLocation, LootTable.Builder> map;
    
    public EntityLoot() {
        this.map = (Map<ResourceLocation, LootTable.Builder>)Maps.newHashMap();
    }
    
    private static LootTable.Builder createSheepTable(final ItemLike brt) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootItem.lootTableItem(brt))).withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1)).add(LootTableReference.lootTableReference(EntityType.SHEEP.getDefaultLootTable())));
    }
    
    public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       net/minecraft/world/entity/EntityType.ARMOR_STAND:Lnet/minecraft/world/entity/EntityType;
        //     4: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //     7: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //    10: aload_0         /* this */
        //    11: getstatic       net/minecraft/world/entity/EntityType.BAT:Lnet/minecraft/world/entity/EntityType;
        //    14: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //    17: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //    20: aload_0         /* this */
        //    21: getstatic       net/minecraft/world/entity/EntityType.BEE:Lnet/minecraft/world/entity/EntityType;
        //    24: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //    27: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //    30: aload_0         /* this */
        //    31: getstatic       net/minecraft/world/entity/EntityType.BLAZE:Lnet/minecraft/world/entity/EntityType;
        //    34: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //    37: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    40: iconst_1       
        //    41: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //    44: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    47: getstatic       net/minecraft/world/item/Items.BLAZE_ROD:Lnet/minecraft/world/item/Item;
        //    50: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    53: fconst_0       
        //    54: fconst_1       
        //    55: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //    58: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //    61: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    64: fconst_0       
        //    65: fconst_1       
        //    66: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //    69: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //    72: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    75: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    78: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //    81: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    84: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //    87: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //    90: aload_0         /* this */
        //    91: getstatic       net/minecraft/world/entity/EntityType.CAT:Lnet/minecraft/world/entity/EntityType;
        //    94: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //    97: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   100: iconst_1       
        //   101: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   104: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   107: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //   110: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   113: fconst_0       
        //   114: fconst_2       
        //   115: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   118: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   121: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   124: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   127: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   130: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   133: aload_0         /* this */
        //   134: getstatic       net/minecraft/world/entity/EntityType.CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;
        //   137: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   140: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   143: iconst_1       
        //   144: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   147: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   150: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //   153: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   156: fconst_0       
        //   157: fconst_2       
        //   158: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   161: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   164: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   167: fconst_0       
        //   168: fconst_1       
        //   169: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   172: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   175: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   178: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   181: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   184: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   187: iconst_1       
        //   188: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   191: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   194: getstatic       net/minecraft/world/item/Items.SPIDER_EYE:Lnet/minecraft/world/item/Item;
        //   197: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   200: ldc             -1.0
        //   202: fconst_1       
        //   203: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   206: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   209: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   212: fconst_0       
        //   213: fconst_1       
        //   214: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   217: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   220: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   223: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   226: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   229: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   232: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   235: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   238: aload_0         /* this */
        //   239: getstatic       net/minecraft/world/entity/EntityType.CHICKEN:Lnet/minecraft/world/entity/EntityType;
        //   242: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   245: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   248: iconst_1       
        //   249: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   252: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   255: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        //   258: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   261: fconst_0       
        //   262: fconst_2       
        //   263: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   266: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   269: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   272: fconst_0       
        //   273: fconst_1       
        //   274: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   277: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   280: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   283: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   286: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   289: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   292: iconst_1       
        //   293: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   296: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   299: getstatic       net/minecraft/world/item/Items.CHICKEN:Lnet/minecraft/world/item/Item;
        //   302: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   305: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   308: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   311: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   314: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   317: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   320: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   323: fconst_0       
        //   324: fconst_1       
        //   325: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   328: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   331: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   334: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   337: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   340: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   343: aload_0         /* this */
        //   344: getstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        //   347: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   350: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   353: iconst_1       
        //   354: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   357: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   360: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        //   363: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   366: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   369: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   372: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   375: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   378: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   381: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   384: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   387: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   390: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   393: iconst_1       
        //   394: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   397: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   400: getstatic       net/minecraft/world/item/Items.BONE_MEAL:Lnet/minecraft/world/item/Item;
        //   403: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   406: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   409: ldc             0.05
        //   411: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition.randomChance:(F)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   414: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   417: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   420: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   423: aload_0         /* this */
        //   424: getstatic       net/minecraft/world/entity/EntityType.COW:Lnet/minecraft/world/entity/EntityType;
        //   427: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   430: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   433: iconst_1       
        //   434: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   437: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   440: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //   443: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   446: fconst_0       
        //   447: fconst_2       
        //   448: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   451: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   454: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   457: fconst_0       
        //   458: fconst_1       
        //   459: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   462: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   465: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   468: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   471: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   474: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   477: iconst_1       
        //   478: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   481: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   484: getstatic       net/minecraft/world/item/Items.BEEF:Lnet/minecraft/world/item/Item;
        //   487: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   490: fconst_1       
        //   491: ldc_w           3.0
        //   494: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   497: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   500: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   503: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   506: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   509: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   512: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   515: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   518: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   521: fconst_0       
        //   522: fconst_1       
        //   523: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   526: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   529: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   532: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   535: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   538: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   541: aload_0         /* this */
        //   542: getstatic       net/minecraft/world/entity/EntityType.CREEPER:Lnet/minecraft/world/entity/EntityType;
        //   545: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   548: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   551: iconst_1       
        //   552: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   555: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   558: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //   561: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   564: fconst_0       
        //   565: fconst_2       
        //   566: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   569: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   572: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   575: fconst_0       
        //   576: fconst_1       
        //   577: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   580: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   583: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   586: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   589: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   592: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   595: getstatic       net/minecraft/tags/ItemTags.CREEPER_DROP_MUSIC_DISCS:Lnet/minecraft/tags/Tag$Named;
        //   598: invokestatic    net/minecraft/world/level/storage/loot/entries/TagEntry.expandTag:(Lnet/minecraft/tags/Tag;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   601: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   604: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.KILLER:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   607: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   610: getstatic       net/minecraft/tags/EntityTypeTags.SKELETONS:Lnet/minecraft/tags/Tag$Named;
        //   613: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/tags/Tag;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   616: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   619: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   622: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   625: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   628: aload_0         /* this */
        //   629: getstatic       net/minecraft/world/entity/EntityType.DOLPHIN:Lnet/minecraft/world/entity/EntityType;
        //   632: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   635: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   638: iconst_1       
        //   639: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   642: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   645: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        //   648: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   651: fconst_0       
        //   652: fconst_1       
        //   653: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   656: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   659: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   662: fconst_0       
        //   663: fconst_1       
        //   664: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   667: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   670: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   673: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   676: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   679: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   682: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   685: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   688: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   691: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   694: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   697: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   700: aload_0         /* this */
        //   701: getstatic       net/minecraft/world/entity/EntityType.DONKEY:Lnet/minecraft/world/entity/EntityType;
        //   704: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   707: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   710: iconst_1       
        //   711: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   714: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   717: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //   720: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   723: fconst_0       
        //   724: fconst_2       
        //   725: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   728: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   731: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   734: fconst_0       
        //   735: fconst_1       
        //   736: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   739: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   742: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   745: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   748: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   751: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   754: aload_0         /* this */
        //   755: getstatic       net/minecraft/world/entity/EntityType.DROWNED:Lnet/minecraft/world/entity/EntityType;
        //   758: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   761: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   764: iconst_1       
        //   765: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   768: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   771: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //   774: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   777: fconst_0       
        //   778: fconst_2       
        //   779: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   782: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   785: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   788: fconst_0       
        //   789: fconst_1       
        //   790: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   793: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   796: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   799: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   802: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   805: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   808: iconst_1       
        //   809: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   812: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   815: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //   818: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   821: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   824: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   827: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   830: ldc             0.05
        //   832: ldc_w           0.01
        //   835: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   838: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   841: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   844: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //   847: aload_0         /* this */
        //   848: getstatic       net/minecraft/world/entity/EntityType.ELDER_GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //   851: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   854: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   857: iconst_1       
        //   858: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   861: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   864: getstatic       net/minecraft/world/item/Items.PRISMARINE_SHARD:Lnet/minecraft/world/item/Item;
        //   867: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   870: fconst_0       
        //   871: fconst_2       
        //   872: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   875: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   878: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   881: fconst_0       
        //   882: fconst_1       
        //   883: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   886: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   889: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   892: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   895: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   898: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   901: iconst_1       
        //   902: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   905: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   908: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        //   911: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   914: iconst_3       
        //   915: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   918: fconst_0       
        //   919: fconst_1       
        //   920: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   923: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   926: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   929: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   932: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //   935: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   938: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //   941: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   944: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   947: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   950: getstatic       net/minecraft/world/item/Items.PRISMARINE_CRYSTALS:Lnet/minecraft/world/item/Item;
        //   953: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   956: iconst_2       
        //   957: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   960: fconst_0       
        //   961: fconst_1       
        //   962: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   965: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //   968: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   971: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   974: invokestatic    net/minecraft/world/level/storage/loot/entries/EmptyLootItem.emptyItem:()Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   977: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   980: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   983: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   986: iconst_1       
        //   987: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   990: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   993: getstatic       net/minecraft/world/level/block/Blocks.WET_SPONGE:Lnet/minecraft/world/level/block/Block;
        //   996: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   999: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1002: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1005: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1008: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1011: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1014: iconst_1       
        //  1015: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1018: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1021: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.FISHING_FISH:Lnet/minecraft/resources/ResourceLocation;
        //  1024: invokestatic    net/minecraft/world/level/storage/loot/entries/LootTableReference.lootTableReference:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1027: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1030: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1033: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1036: ldc_w           0.025
        //  1039: ldc_w           0.01
        //  1042: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1045: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1048: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1051: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1054: aload_0         /* this */
        //  1055: getstatic       net/minecraft/world/entity/EntityType.ENDER_DRAGON:Lnet/minecraft/world/entity/EntityType;
        //  1058: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1061: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1064: aload_0         /* this */
        //  1065: getstatic       net/minecraft/world/entity/EntityType.ENDERMAN:Lnet/minecraft/world/entity/EntityType;
        //  1068: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1071: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1074: iconst_1       
        //  1075: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1078: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1081: getstatic       net/minecraft/world/item/Items.ENDER_PEARL:Lnet/minecraft/world/item/Item;
        //  1084: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1087: fconst_0       
        //  1088: fconst_1       
        //  1089: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1092: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1095: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1098: fconst_0       
        //  1099: fconst_1       
        //  1100: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1103: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1106: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1109: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1112: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1115: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1118: aload_0         /* this */
        //  1119: getstatic       net/minecraft/world/entity/EntityType.ENDERMITE:Lnet/minecraft/world/entity/EntityType;
        //  1122: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1125: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1128: aload_0         /* this */
        //  1129: getstatic       net/minecraft/world/entity/EntityType.EVOKER:Lnet/minecraft/world/entity/EntityType;
        //  1132: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1135: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1138: iconst_1       
        //  1139: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1142: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1145: getstatic       net/minecraft/world/item/Items.TOTEM_OF_UNDYING:Lnet/minecraft/world/item/Item;
        //  1148: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1151: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1154: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1157: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1160: iconst_1       
        //  1161: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1164: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1167: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  1170: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1173: fconst_0       
        //  1174: fconst_1       
        //  1175: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1178: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1181: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1184: fconst_0       
        //  1185: fconst_1       
        //  1186: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1189: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1192: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1195: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1198: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1201: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1204: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1207: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1210: aload_0         /* this */
        //  1211: getstatic       net/minecraft/world/entity/EntityType.FOX:Lnet/minecraft/world/entity/EntityType;
        //  1214: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1217: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1220: aload_0         /* this */
        //  1221: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //  1224: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1227: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1230: iconst_1       
        //  1231: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1234: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1237: getstatic       net/minecraft/world/item/Items.GHAST_TEAR:Lnet/minecraft/world/item/Item;
        //  1240: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1243: fconst_0       
        //  1244: fconst_1       
        //  1245: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1248: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1251: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1254: fconst_0       
        //  1255: fconst_1       
        //  1256: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1259: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1262: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1265: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1268: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1271: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1274: iconst_1       
        //  1275: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1278: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1281: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  1284: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1287: fconst_0       
        //  1288: fconst_2       
        //  1289: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1292: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1295: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1298: fconst_0       
        //  1299: fconst_1       
        //  1300: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1303: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1306: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1309: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1312: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1315: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1318: aload_0         /* this */
        //  1319: getstatic       net/minecraft/world/entity/EntityType.GIANT:Lnet/minecraft/world/entity/EntityType;
        //  1322: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1325: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1328: aload_0         /* this */
        //  1329: getstatic       net/minecraft/world/entity/EntityType.GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        //  1332: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1335: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1338: iconst_1       
        //  1339: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1342: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1345: getstatic       net/minecraft/world/item/Items.PRISMARINE_SHARD:Lnet/minecraft/world/item/Item;
        //  1348: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1351: fconst_0       
        //  1352: fconst_2       
        //  1353: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1356: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1359: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1362: fconst_0       
        //  1363: fconst_1       
        //  1364: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1367: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1370: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1373: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1376: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1379: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1382: iconst_1       
        //  1383: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1386: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1389: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        //  1392: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1395: iconst_2       
        //  1396: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1399: fconst_0       
        //  1400: fconst_1       
        //  1401: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1404: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1407: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1410: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1413: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  1416: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1419: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1422: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1425: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1428: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1431: getstatic       net/minecraft/world/item/Items.PRISMARINE_CRYSTALS:Lnet/minecraft/world/item/Item;
        //  1434: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1437: iconst_2       
        //  1438: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1441: fconst_0       
        //  1442: fconst_1       
        //  1443: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1446: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1449: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1452: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1455: invokestatic    net/minecraft/world/level/storage/loot/entries/EmptyLootItem.emptyItem:()Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1458: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1461: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1464: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1467: iconst_1       
        //  1468: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1471: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1474: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.FISHING_FISH:Lnet/minecraft/resources/ResourceLocation;
        //  1477: invokestatic    net/minecraft/world/level/storage/loot/entries/LootTableReference.lootTableReference:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1480: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1483: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1486: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1489: ldc_w           0.025
        //  1492: ldc_w           0.01
        //  1495: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1498: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1501: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1504: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1507: aload_0         /* this */
        //  1508: getstatic       net/minecraft/world/entity/EntityType.HORSE:Lnet/minecraft/world/entity/EntityType;
        //  1511: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1514: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1517: iconst_1       
        //  1518: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1521: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1524: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  1527: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1530: fconst_0       
        //  1531: fconst_2       
        //  1532: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1535: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1538: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1541: fconst_0       
        //  1542: fconst_1       
        //  1543: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1546: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1549: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1552: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1555: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1558: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1561: aload_0         /* this */
        //  1562: getstatic       net/minecraft/world/entity/EntityType.HUSK:Lnet/minecraft/world/entity/EntityType;
        //  1565: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1568: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1571: iconst_1       
        //  1572: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1575: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1578: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  1581: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1584: fconst_0       
        //  1585: fconst_2       
        //  1586: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1589: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1592: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1595: fconst_0       
        //  1596: fconst_1       
        //  1597: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1600: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1603: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1606: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1609: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1612: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1615: iconst_1       
        //  1616: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1619: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1622: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  1625: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1628: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1631: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        //  1634: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1637: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1640: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        //  1643: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1646: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1649: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1652: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1655: ldc_w           0.025
        //  1658: ldc_w           0.01
        //  1661: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  1664: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1667: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1670: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1673: aload_0         /* this */
        //  1674: getstatic       net/minecraft/world/entity/EntityType.RAVAGER:Lnet/minecraft/world/entity/EntityType;
        //  1677: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1680: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1683: iconst_1       
        //  1684: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1687: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1690: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  1693: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1696: iconst_1       
        //  1697: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1700: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1703: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1706: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1709: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1712: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1715: aload_0         /* this */
        //  1716: getstatic       net/minecraft/world/entity/EntityType.ILLUSIONER:Lnet/minecraft/world/entity/EntityType;
        //  1719: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1722: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1725: aload_0         /* this */
        //  1726: getstatic       net/minecraft/world/entity/EntityType.IRON_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //  1729: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1732: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1735: iconst_1       
        //  1736: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1739: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1742: getstatic       net/minecraft/world/level/block/Blocks.POPPY:Lnet/minecraft/world/level/block/Block;
        //  1745: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1748: fconst_0       
        //  1749: fconst_2       
        //  1750: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1753: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1756: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1759: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1762: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1765: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1768: iconst_1       
        //  1769: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1772: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1775: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  1778: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1781: ldc_w           3.0
        //  1784: ldc_w           5.0
        //  1787: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1790: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1793: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1796: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1799: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1802: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1805: aload_0         /* this */
        //  1806: getstatic       net/minecraft/world/entity/EntityType.LLAMA:Lnet/minecraft/world/entity/EntityType;
        //  1809: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1812: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1815: iconst_1       
        //  1816: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1819: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1822: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  1825: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1828: fconst_0       
        //  1829: fconst_2       
        //  1830: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1833: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1836: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1839: fconst_0       
        //  1840: fconst_1       
        //  1841: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1844: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1847: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1850: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1853: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1856: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1859: aload_0         /* this */
        //  1860: getstatic       net/minecraft/world/entity/EntityType.MAGMA_CUBE:Lnet/minecraft/world/entity/EntityType;
        //  1863: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1866: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1869: iconst_1       
        //  1870: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1873: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1876: getstatic       net/minecraft/world/item/Items.MAGMA_CREAM:Lnet/minecraft/world/item/Item;
        //  1879: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1882: ldc_w           -2.0
        //  1885: fconst_1       
        //  1886: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1889: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1892: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1895: fconst_0       
        //  1896: fconst_1       
        //  1897: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1900: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1903: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1906: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1909: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1912: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1915: aload_0         /* this */
        //  1916: getstatic       net/minecraft/world/entity/EntityType.MULE:Lnet/minecraft/world/entity/EntityType;
        //  1919: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1922: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1925: iconst_1       
        //  1926: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1929: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1932: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  1935: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1938: fconst_0       
        //  1939: fconst_2       
        //  1940: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1943: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1946: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1949: fconst_0       
        //  1950: fconst_1       
        //  1951: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1954: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  1957: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1960: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1963: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1966: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  1969: aload_0         /* this */
        //  1970: getstatic       net/minecraft/world/entity/EntityType.MOOSHROOM:Lnet/minecraft/world/entity/EntityType;
        //  1973: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1976: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1979: iconst_1       
        //  1980: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1983: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1986: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  1989: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1992: fconst_0       
        //  1993: fconst_2       
        //  1994: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1997: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2000: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2003: fconst_0       
        //  2004: fconst_1       
        //  2005: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2008: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2011: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2014: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2017: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2020: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2023: iconst_1       
        //  2024: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2027: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2030: getstatic       net/minecraft/world/item/Items.BEEF:Lnet/minecraft/world/item/Item;
        //  2033: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2036: fconst_1       
        //  2037: ldc_w           3.0
        //  2040: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2043: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2046: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2049: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2052: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  2055: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2058: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2061: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2064: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2067: fconst_0       
        //  2068: fconst_1       
        //  2069: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2072: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2075: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2078: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2081: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2084: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2087: aload_0         /* this */
        //  2088: getstatic       net/minecraft/world/entity/EntityType.OCELOT:Lnet/minecraft/world/entity/EntityType;
        //  2091: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2094: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2097: aload_0         /* this */
        //  2098: getstatic       net/minecraft/world/entity/EntityType.PANDA:Lnet/minecraft/world/entity/EntityType;
        //  2101: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2104: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2107: iconst_1       
        //  2108: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2111: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2114: getstatic       net/minecraft/world/level/block/Blocks.BAMBOO:Lnet/minecraft/world/level/block/Block;
        //  2117: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2120: iconst_1       
        //  2121: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2124: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2127: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2130: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2133: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2136: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2139: aload_0         /* this */
        //  2140: getstatic       net/minecraft/world/entity/EntityType.PARROT:Lnet/minecraft/world/entity/EntityType;
        //  2143: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2146: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2149: iconst_1       
        //  2150: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2153: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2156: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        //  2159: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2162: fconst_1       
        //  2163: fconst_2       
        //  2164: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2167: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2170: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2173: fconst_0       
        //  2174: fconst_1       
        //  2175: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2178: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2181: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2184: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2187: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2190: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2193: aload_0         /* this */
        //  2194: getstatic       net/minecraft/world/entity/EntityType.PHANTOM:Lnet/minecraft/world/entity/EntityType;
        //  2197: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2200: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2203: iconst_1       
        //  2204: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2207: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2210: getstatic       net/minecraft/world/item/Items.PHANTOM_MEMBRANE:Lnet/minecraft/world/item/Item;
        //  2213: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2216: fconst_0       
        //  2217: fconst_1       
        //  2218: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2221: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2224: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2227: fconst_0       
        //  2228: fconst_1       
        //  2229: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2232: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2235: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2238: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2241: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2244: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2247: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2250: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2253: aload_0         /* this */
        //  2254: getstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        //  2257: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2260: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2263: iconst_1       
        //  2264: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2267: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2270: getstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
        //  2273: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2276: fconst_1       
        //  2277: ldc_w           3.0
        //  2280: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2283: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2286: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2289: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2292: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  2295: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2298: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2301: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2304: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2307: fconst_0       
        //  2308: fconst_1       
        //  2309: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2312: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2315: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2318: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2321: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2324: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2327: aload_0         /* this */
        //  2328: getstatic       net/minecraft/world/entity/EntityType.PILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  2331: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2334: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2337: aload_0         /* this */
        //  2338: getstatic       net/minecraft/world/entity/EntityType.PLAYER:Lnet/minecraft/world/entity/EntityType;
        //  2341: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2344: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2347: aload_0         /* this */
        //  2348: getstatic       net/minecraft/world/entity/EntityType.POLAR_BEAR:Lnet/minecraft/world/entity/EntityType;
        //  2351: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2354: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2357: iconst_1       
        //  2358: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2361: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2364: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        //  2367: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2370: iconst_3       
        //  2371: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2374: fconst_0       
        //  2375: fconst_2       
        //  2376: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2379: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2382: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2385: fconst_0       
        //  2386: fconst_1       
        //  2387: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2390: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2393: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2396: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2399: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
        //  2402: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2405: fconst_0       
        //  2406: fconst_2       
        //  2407: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2410: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2413: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2416: fconst_0       
        //  2417: fconst_1       
        //  2418: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2421: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2424: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2427: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2430: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2433: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2436: aload_0         /* this */
        //  2437: getstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        //  2440: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2443: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2446: iconst_1       
        //  2447: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2450: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2453: getstatic       net/minecraft/world/item/Items.PUFFERFISH:Lnet/minecraft/world/item/Item;
        //  2456: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2459: iconst_1       
        //  2460: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2463: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2466: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2469: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2472: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2475: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2478: iconst_1       
        //  2479: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2482: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2485: getstatic       net/minecraft/world/item/Items.BONE_MEAL:Lnet/minecraft/world/item/Item;
        //  2488: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2491: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2494: ldc             0.05
        //  2496: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition.randomChance:(F)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2499: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2502: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2505: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2508: aload_0         /* this */
        //  2509: getstatic       net/minecraft/world/entity/EntityType.RABBIT:Lnet/minecraft/world/entity/EntityType;
        //  2512: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2515: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2518: iconst_1       
        //  2519: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2522: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2525: getstatic       net/minecraft/world/item/Items.RABBIT_HIDE:Lnet/minecraft/world/item/Item;
        //  2528: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2531: fconst_0       
        //  2532: fconst_1       
        //  2533: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2536: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2539: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2542: fconst_0       
        //  2543: fconst_1       
        //  2544: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2547: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2550: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2553: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2556: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2559: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2562: iconst_1       
        //  2563: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2566: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2569: getstatic       net/minecraft/world/item/Items.RABBIT:Lnet/minecraft/world/item/Item;
        //  2572: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2575: fconst_0       
        //  2576: fconst_1       
        //  2577: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2580: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2583: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2586: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2589: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  2592: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2595: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2598: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2601: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2604: fconst_0       
        //  2605: fconst_1       
        //  2606: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2609: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2612: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2615: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2618: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2621: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2624: iconst_1       
        //  2625: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2628: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2631: getstatic       net/minecraft/world/item/Items.RABBIT_FOOT:Lnet/minecraft/world/item/Item;
        //  2634: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2637: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2640: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2643: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2646: ldc_w           0.1
        //  2649: ldc_w           0.03
        //  2652: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2655: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2658: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2661: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2664: aload_0         /* this */
        //  2665: getstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        //  2668: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2671: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2674: iconst_1       
        //  2675: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2678: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2681: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
        //  2684: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2687: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2690: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  2693: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2696: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2699: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2702: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2705: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2708: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2711: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2714: iconst_1       
        //  2715: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2718: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2721: getstatic       net/minecraft/world/item/Items.BONE_MEAL:Lnet/minecraft/world/item/Item;
        //  2724: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2727: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2730: ldc             0.05
        //  2732: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition.randomChance:(F)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2735: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2738: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2741: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2744: aload_0         /* this */
        //  2745: getstatic       net/minecraft/world/entity/EntityType.SHEEP:Lnet/minecraft/world/entity/EntityType;
        //  2748: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2751: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2754: iconst_1       
        //  2755: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2758: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2761: getstatic       net/minecraft/world/item/Items.MUTTON:Lnet/minecraft/world/item/Item;
        //  2764: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2767: fconst_1       
        //  2768: fconst_2       
        //  2769: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2772: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2775: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2778: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2781: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  2784: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2787: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  2790: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2793: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2796: fconst_0       
        //  2797: fconst_1       
        //  2798: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2801: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  2804: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2807: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2810: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2813: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2816: aload_0         /* this */
        //  2817: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_BLACK:Lnet/minecraft/resources/ResourceLocation;
        //  2820: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2823: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2826: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2829: aload_0         /* this */
        //  2830: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_BLUE:Lnet/minecraft/resources/ResourceLocation;
        //  2833: getstatic       net/minecraft/world/level/block/Blocks.BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2836: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2839: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2842: aload_0         /* this */
        //  2843: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_BROWN:Lnet/minecraft/resources/ResourceLocation;
        //  2846: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2849: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2852: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2855: aload_0         /* this */
        //  2856: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_CYAN:Lnet/minecraft/resources/ResourceLocation;
        //  2859: getstatic       net/minecraft/world/level/block/Blocks.CYAN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2862: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2865: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2868: aload_0         /* this */
        //  2869: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_GRAY:Lnet/minecraft/resources/ResourceLocation;
        //  2872: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2875: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2878: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2881: aload_0         /* this */
        //  2882: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_GREEN:Lnet/minecraft/resources/ResourceLocation;
        //  2885: getstatic       net/minecraft/world/level/block/Blocks.GREEN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2888: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2891: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2894: aload_0         /* this */
        //  2895: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_LIGHT_BLUE:Lnet/minecraft/resources/ResourceLocation;
        //  2898: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2901: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2904: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2907: aload_0         /* this */
        //  2908: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_LIGHT_GRAY:Lnet/minecraft/resources/ResourceLocation;
        //  2911: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2914: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2917: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2920: aload_0         /* this */
        //  2921: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_LIME:Lnet/minecraft/resources/ResourceLocation;
        //  2924: getstatic       net/minecraft/world/level/block/Blocks.LIME_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2927: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2930: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2933: aload_0         /* this */
        //  2934: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_MAGENTA:Lnet/minecraft/resources/ResourceLocation;
        //  2937: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2940: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2943: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2946: aload_0         /* this */
        //  2947: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_ORANGE:Lnet/minecraft/resources/ResourceLocation;
        //  2950: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2953: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2956: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2959: aload_0         /* this */
        //  2960: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_PINK:Lnet/minecraft/resources/ResourceLocation;
        //  2963: getstatic       net/minecraft/world/level/block/Blocks.PINK_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2966: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2969: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2972: aload_0         /* this */
        //  2973: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_PURPLE:Lnet/minecraft/resources/ResourceLocation;
        //  2976: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2979: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2982: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2985: aload_0         /* this */
        //  2986: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_RED:Lnet/minecraft/resources/ResourceLocation;
        //  2989: getstatic       net/minecraft/world/level/block/Blocks.RED_WOOL:Lnet/minecraft/world/level/block/Block;
        //  2992: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2995: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  2998: aload_0         /* this */
        //  2999: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_WHITE:Lnet/minecraft/resources/ResourceLocation;
        //  3002: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  3005: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3008: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3011: aload_0         /* this */
        //  3012: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHEEP_YELLOW:Lnet/minecraft/resources/ResourceLocation;
        //  3015: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_WOOL:Lnet/minecraft/world/level/block/Block;
        //  3018: invokestatic    net/minecraft/data/loot/EntityLoot.createSheepTable:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3021: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3024: aload_0         /* this */
        //  3025: getstatic       net/minecraft/world/entity/EntityType.SHULKER:Lnet/minecraft/world/entity/EntityType;
        //  3028: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3031: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3034: iconst_1       
        //  3035: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3038: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3041: getstatic       net/minecraft/world/item/Items.SHULKER_SHELL:Lnet/minecraft/world/item/Item;
        //  3044: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3047: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3050: ldc_w           0.5
        //  3053: ldc_w           0.0625
        //  3056: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  3059: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3062: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3065: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3068: aload_0         /* this */
        //  3069: getstatic       net/minecraft/world/entity/EntityType.SILVERFISH:Lnet/minecraft/world/entity/EntityType;
        //  3072: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3075: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3078: aload_0         /* this */
        //  3079: getstatic       net/minecraft/world/entity/EntityType.SKELETON:Lnet/minecraft/world/entity/EntityType;
        //  3082: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3085: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3088: iconst_1       
        //  3089: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3092: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3095: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  3098: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3101: fconst_0       
        //  3102: fconst_2       
        //  3103: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3106: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3109: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3112: fconst_0       
        //  3113: fconst_1       
        //  3114: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3117: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3120: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3123: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3126: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3129: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3132: iconst_1       
        //  3133: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3136: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3139: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  3142: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3145: fconst_0       
        //  3146: fconst_2       
        //  3147: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3150: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3153: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3156: fconst_0       
        //  3157: fconst_1       
        //  3158: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3161: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3164: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3167: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3170: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3173: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3176: aload_0         /* this */
        //  3177: getstatic       net/minecraft/world/entity/EntityType.SKELETON_HORSE:Lnet/minecraft/world/entity/EntityType;
        //  3180: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3183: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3186: iconst_1       
        //  3187: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3190: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3193: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  3196: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3199: fconst_0       
        //  3200: fconst_2       
        //  3201: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3204: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3207: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3210: fconst_0       
        //  3211: fconst_1       
        //  3212: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3215: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3218: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3221: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3224: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3227: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3230: aload_0         /* this */
        //  3231: getstatic       net/minecraft/world/entity/EntityType.SLIME:Lnet/minecraft/world/entity/EntityType;
        //  3234: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3237: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3240: iconst_1       
        //  3241: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3244: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3247: getstatic       net/minecraft/world/item/Items.SLIME_BALL:Lnet/minecraft/world/item/Item;
        //  3250: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3253: fconst_0       
        //  3254: fconst_2       
        //  3255: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3258: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3261: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3264: fconst_0       
        //  3265: fconst_1       
        //  3266: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3269: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3272: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3275: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3278: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3281: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3284: aload_0         /* this */
        //  3285: getstatic       net/minecraft/world/entity/EntityType.SNOW_GOLEM:Lnet/minecraft/world/entity/EntityType;
        //  3288: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3291: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3294: iconst_1       
        //  3295: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3298: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3301: getstatic       net/minecraft/world/item/Items.SNOWBALL:Lnet/minecraft/world/item/Item;
        //  3304: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3307: fconst_0       
        //  3308: ldc_w           15.0
        //  3311: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3314: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3317: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3320: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3323: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3326: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3329: aload_0         /* this */
        //  3330: getstatic       net/minecraft/world/entity/EntityType.SPIDER:Lnet/minecraft/world/entity/EntityType;
        //  3333: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3336: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3339: iconst_1       
        //  3340: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3343: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3346: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  3349: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3352: fconst_0       
        //  3353: fconst_2       
        //  3354: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3357: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3360: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3363: fconst_0       
        //  3364: fconst_1       
        //  3365: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3368: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3371: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3374: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3377: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3380: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3383: iconst_1       
        //  3384: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3387: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3390: getstatic       net/minecraft/world/item/Items.SPIDER_EYE:Lnet/minecraft/world/item/Item;
        //  3393: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3396: ldc             -1.0
        //  3398: fconst_1       
        //  3399: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3402: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3405: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3408: fconst_0       
        //  3409: fconst_1       
        //  3410: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3413: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3416: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3419: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3422: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  3425: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3428: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3431: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3434: aload_0         /* this */
        //  3435: getstatic       net/minecraft/world/entity/EntityType.SQUID:Lnet/minecraft/world/entity/EntityType;
        //  3438: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3441: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3444: iconst_1       
        //  3445: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3448: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3451: getstatic       net/minecraft/world/item/Items.INK_SAC:Lnet/minecraft/world/item/Item;
        //  3454: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3457: fconst_1       
        //  3458: ldc_w           3.0
        //  3461: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3464: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3467: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3470: fconst_0       
        //  3471: fconst_1       
        //  3472: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3475: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3478: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3481: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3484: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3487: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3490: aload_0         /* this */
        //  3491: getstatic       net/minecraft/world/entity/EntityType.STRAY:Lnet/minecraft/world/entity/EntityType;
        //  3494: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3497: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3500: iconst_1       
        //  3501: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3504: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3507: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  3510: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3513: fconst_0       
        //  3514: fconst_2       
        //  3515: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3518: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3521: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3524: fconst_0       
        //  3525: fconst_1       
        //  3526: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3529: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3532: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3535: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3538: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3541: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3544: iconst_1       
        //  3545: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3548: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3551: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  3554: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3557: fconst_0       
        //  3558: fconst_2       
        //  3559: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3562: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3565: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3568: fconst_0       
        //  3569: fconst_1       
        //  3570: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3573: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3576: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3579: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3582: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3585: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3588: iconst_1       
        //  3589: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3592: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3595: getstatic       net/minecraft/world/item/Items.TIPPED_ARROW:Lnet/minecraft/world/item/Item;
        //  3598: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3601: fconst_0       
        //  3602: fconst_1       
        //  3603: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3606: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3609: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3612: fconst_0       
        //  3613: fconst_1       
        //  3614: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3617: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3620: iconst_1       
        //  3621: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder.setLimit:(I)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3624: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3627: new             Lnet/minecraft/nbt/CompoundTag;
        //  3630: dup            
        //  3631: invokespecial   net/minecraft/nbt/CompoundTag.<init>:()V
        //  3634: invokedynamic   BootstrapMethod #0, accept:()Ljava/util/function/Consumer;
        //  3639: invokestatic    net/minecraft/Util.make:(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
        //  3642: checkcast       Lnet/minecraft/nbt/CompoundTag;
        //  3645: invokestatic    net/minecraft/world/level/storage/loot/functions/SetNbtFunction.setTag:(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3648: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3651: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3654: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  3657: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3660: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3663: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3666: aload_0         /* this */
        //  3667: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        //  3670: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3673: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3676: iconst_1       
        //  3677: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3680: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3683: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  3686: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3689: fconst_2       
        //  3690: ldc_w           5.0
        //  3693: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3696: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3699: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3702: fconst_0       
        //  3703: fconst_1       
        //  3704: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3707: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3710: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3713: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3716: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3719: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3722: aload_0         /* this */
        //  3723: getstatic       net/minecraft/world/entity/EntityType.TRADER_LLAMA:Lnet/minecraft/world/entity/EntityType;
        //  3726: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3729: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3732: iconst_1       
        //  3733: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3736: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3739: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  3742: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3745: fconst_0       
        //  3746: fconst_2       
        //  3747: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3750: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3753: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3756: fconst_0       
        //  3757: fconst_1       
        //  3758: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3761: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3764: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3767: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3770: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3773: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3776: aload_0         /* this */
        //  3777: getstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        //  3780: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3783: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3786: iconst_1       
        //  3787: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3790: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3793: getstatic       net/minecraft/world/item/Items.TROPICAL_FISH:Lnet/minecraft/world/item/Item;
        //  3796: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3799: iconst_1       
        //  3800: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3803: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3806: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3809: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3812: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3815: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3818: iconst_1       
        //  3819: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3822: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3825: getstatic       net/minecraft/world/item/Items.BONE_MEAL:Lnet/minecraft/world/item/Item;
        //  3828: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3831: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3834: ldc             0.05
        //  3836: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceCondition.randomChance:(F)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  3839: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3842: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3845: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3848: aload_0         /* this */
        //  3849: getstatic       net/minecraft/world/entity/EntityType.TURTLE:Lnet/minecraft/world/entity/EntityType;
        //  3852: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3855: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3858: iconst_1       
        //  3859: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3862: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3865: getstatic       net/minecraft/world/level/block/Blocks.SEAGRASS:Lnet/minecraft/world/level/block/Block;
        //  3868: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3871: iconst_3       
        //  3872: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3875: fconst_0       
        //  3876: fconst_2       
        //  3877: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3880: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3883: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3886: fconst_0       
        //  3887: fconst_1       
        //  3888: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3891: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  3894: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3897: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3900: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3903: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3906: iconst_1       
        //  3907: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3910: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3913: getstatic       net/minecraft/world/item/Items.BOWL:Lnet/minecraft/world/item/Item;
        //  3916: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3919: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3922: invokestatic    net/minecraft/advancements/critereon/DamageSourcePredicate$Builder.damageType:()Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;
        //  3925: iconst_1       
        //  3926: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  3929: invokevirtual   net/minecraft/advancements/critereon/DamageSourcePredicate$Builder.isLightning:(Ljava/lang/Boolean;)Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;
        //  3932: invokestatic    net/minecraft/world/level/storage/loot/predicates/DamageSourceCondition.hasDamageSource:(Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  3935: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3938: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3941: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3944: aload_0         /* this */
        //  3945: getstatic       net/minecraft/world/entity/EntityType.VEX:Lnet/minecraft/world/entity/EntityType;
        //  3948: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3951: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3954: aload_0         /* this */
        //  3955: getstatic       net/minecraft/world/entity/EntityType.VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  3958: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3961: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3964: aload_0         /* this */
        //  3965: getstatic       net/minecraft/world/entity/EntityType.WANDERING_TRADER:Lnet/minecraft/world/entity/EntityType;
        //  3968: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3971: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  3974: aload_0         /* this */
        //  3975: getstatic       net/minecraft/world/entity/EntityType.VINDICATOR:Lnet/minecraft/world/entity/EntityType;
        //  3978: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3981: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3984: iconst_1       
        //  3985: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3988: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3991: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  3994: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3997: fconst_0       
        //  3998: fconst_1       
        //  3999: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4002: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4005: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4008: fconst_0       
        //  4009: fconst_1       
        //  4010: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4013: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4016: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4019: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4022: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4025: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4028: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4031: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4034: aload_0         /* this */
        //  4035: getstatic       net/minecraft/world/entity/EntityType.WITCH:Lnet/minecraft/world/entity/EntityType;
        //  4038: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4041: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4044: fconst_1       
        //  4045: ldc_w           3.0
        //  4048: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4051: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4054: getstatic       net/minecraft/world/item/Items.GLOWSTONE_DUST:Lnet/minecraft/world/item/Item;
        //  4057: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4060: fconst_0       
        //  4061: fconst_2       
        //  4062: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4065: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4068: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4071: fconst_0       
        //  4072: fconst_1       
        //  4073: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4076: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4079: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4082: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4085: getstatic       net/minecraft/world/item/Items.SUGAR:Lnet/minecraft/world/item/Item;
        //  4088: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4091: fconst_0       
        //  4092: fconst_2       
        //  4093: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4096: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4099: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4102: fconst_0       
        //  4103: fconst_1       
        //  4104: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4107: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4110: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4113: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4116: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        //  4119: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4122: fconst_0       
        //  4123: fconst_2       
        //  4124: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4127: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4130: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4133: fconst_0       
        //  4134: fconst_1       
        //  4135: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4138: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4141: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4144: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4147: getstatic       net/minecraft/world/item/Items.SPIDER_EYE:Lnet/minecraft/world/item/Item;
        //  4150: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4153: fconst_0       
        //  4154: fconst_2       
        //  4155: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4158: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4161: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4164: fconst_0       
        //  4165: fconst_1       
        //  4166: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4169: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4172: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4175: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4178: getstatic       net/minecraft/world/item/Items.GLASS_BOTTLE:Lnet/minecraft/world/item/Item;
        //  4181: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4184: fconst_0       
        //  4185: fconst_2       
        //  4186: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4189: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4192: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4195: fconst_0       
        //  4196: fconst_1       
        //  4197: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4200: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4203: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4206: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4209: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  4212: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4215: fconst_0       
        //  4216: fconst_2       
        //  4217: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4220: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4223: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4226: fconst_0       
        //  4227: fconst_1       
        //  4228: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4231: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4234: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4237: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4240: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  4243: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4246: iconst_2       
        //  4247: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4250: fconst_0       
        //  4251: fconst_2       
        //  4252: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4255: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4258: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4261: fconst_0       
        //  4262: fconst_1       
        //  4263: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4266: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4269: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4272: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4275: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4278: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4281: aload_0         /* this */
        //  4282: getstatic       net/minecraft/world/entity/EntityType.WITHER:Lnet/minecraft/world/entity/EntityType;
        //  4285: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4288: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4291: aload_0         /* this */
        //  4292: getstatic       net/minecraft/world/entity/EntityType.WITHER_SKELETON:Lnet/minecraft/world/entity/EntityType;
        //  4295: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4298: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4301: iconst_1       
        //  4302: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4305: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4308: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  4311: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4314: ldc             -1.0
        //  4316: fconst_1       
        //  4317: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4320: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4323: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4326: fconst_0       
        //  4327: fconst_1       
        //  4328: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4331: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4334: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4337: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4340: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4343: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4346: iconst_1       
        //  4347: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4350: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4353: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  4356: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4359: fconst_0       
        //  4360: fconst_2       
        //  4361: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4364: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4367: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4370: fconst_0       
        //  4371: fconst_1       
        //  4372: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4375: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4378: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4381: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4384: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4387: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4390: iconst_1       
        //  4391: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4394: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4397: getstatic       net/minecraft/world/level/block/Blocks.WITHER_SKELETON_SKULL:Lnet/minecraft/world/level/block/Block;
        //  4400: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4403: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4406: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4409: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4412: ldc_w           0.025
        //  4415: ldc_w           0.01
        //  4418: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4421: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4424: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4427: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4430: aload_0         /* this */
        //  4431: getstatic       net/minecraft/world/entity/EntityType.WOLF:Lnet/minecraft/world/entity/EntityType;
        //  4434: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4437: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4440: aload_0         /* this */
        //  4441: getstatic       net/minecraft/world/entity/EntityType.ZOGLIN:Lnet/minecraft/world/entity/EntityType;
        //  4444: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4447: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4450: iconst_1       
        //  4451: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4454: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4457: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4460: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4463: fconst_1       
        //  4464: ldc_w           3.0
        //  4467: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4470: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4473: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4476: fconst_0       
        //  4477: fconst_1       
        //  4478: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4481: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4484: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4487: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4490: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4493: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4496: aload_0         /* this */
        //  4497: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE:Lnet/minecraft/world/entity/EntityType;
        //  4500: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4503: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4506: iconst_1       
        //  4507: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4510: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4513: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4516: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4519: fconst_0       
        //  4520: fconst_2       
        //  4521: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4524: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4527: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4530: fconst_0       
        //  4531: fconst_1       
        //  4532: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4535: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4538: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4541: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4544: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4547: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4550: iconst_1       
        //  4551: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4554: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4557: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  4560: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4563: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4566: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        //  4569: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4572: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4575: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        //  4578: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4581: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4584: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4587: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4590: ldc_w           0.025
        //  4593: ldc_w           0.01
        //  4596: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4599: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4602: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4605: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4608: aload_0         /* this */
        //  4609: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_HORSE:Lnet/minecraft/world/entity/EntityType;
        //  4612: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4615: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4618: iconst_1       
        //  4619: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4622: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4625: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4628: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4631: fconst_0       
        //  4632: fconst_2       
        //  4633: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4636: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4639: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4642: fconst_0       
        //  4643: fconst_1       
        //  4644: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4647: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4650: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4653: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4656: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4659: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4662: aload_0         /* this */
        //  4663: getstatic       net/minecraft/world/entity/EntityType.ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  4666: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4669: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4672: iconst_1       
        //  4673: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4676: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4679: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4682: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4685: fconst_0       
        //  4686: fconst_1       
        //  4687: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4690: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4693: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4696: fconst_0       
        //  4697: fconst_1       
        //  4698: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4701: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4704: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4707: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4710: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4713: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4716: iconst_1       
        //  4717: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4720: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4723: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  4726: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4729: fconst_0       
        //  4730: fconst_1       
        //  4731: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4734: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4737: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4740: fconst_0       
        //  4741: fconst_1       
        //  4742: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4745: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4748: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4751: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4754: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4757: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4760: iconst_1       
        //  4761: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4764: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4767: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  4770: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4773: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4776: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4779: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4782: ldc_w           0.025
        //  4785: ldc_w           0.01
        //  4788: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4791: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4794: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4797: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4800: aload_0         /* this */
        //  4801: getstatic       net/minecraft/world/entity/EntityType.HOGLIN:Lnet/minecraft/world/entity/EntityType;
        //  4804: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4807: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4810: iconst_1       
        //  4811: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4814: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4817: getstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
        //  4820: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4823: fconst_2       
        //  4824: ldc_w           4.0
        //  4827: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4830: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4833: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4836: invokestatic    net/minecraft/world/level/storage/loot/functions/SmeltItemFunction.smelted:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4839: getstatic       net/minecraft/world/level/storage/loot/LootContext$EntityTarget.THIS:Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;
        //  4842: getstatic       net/minecraft/data/loot/EntityLoot.ENTITY_ON_FIRE:Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  4845: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemEntityPropertyCondition.hasProperties:(Lnet/minecraft/world/level/storage/loot/LootContext$EntityTarget;Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  4848: invokevirtual   net/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4851: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4854: fconst_0       
        //  4855: fconst_1       
        //  4856: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4859: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4862: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4865: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4868: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4871: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4874: iconst_1       
        //  4875: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4878: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4881: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  4884: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4887: fconst_0       
        //  4888: fconst_1       
        //  4889: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4892: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4895: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4898: fconst_0       
        //  4899: fconst_1       
        //  4900: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4903: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4906: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4909: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4912: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4915: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4918: aload_0         /* this */
        //  4919: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  4922: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4925: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4928: aload_0         /* this */
        //  4929: getstatic       net/minecraft/world/entity/EntityType.PIGLIN_BRUTE:Lnet/minecraft/world/entity/EntityType;
        //  4932: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4935: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  4938: aload_0         /* this */
        //  4939: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_VILLAGER:Lnet/minecraft/world/entity/EntityType;
        //  4942: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4945: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4948: iconst_1       
        //  4949: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4952: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4955: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4958: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4961: fconst_0       
        //  4962: fconst_2       
        //  4963: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4966: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4969: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4972: fconst_0       
        //  4973: fconst_1       
        //  4974: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4977: invokestatic    net/minecraft/world/level/storage/loot/functions/LootingEnchantFunction.lootingMultiplier:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootingEnchantFunction$Builder;
        //  4980: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4983: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4986: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4989: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4992: iconst_1       
        //  4993: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4996: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4999: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  5002: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5005: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5008: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        //  5011: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5014: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5017: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        //  5020: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5023: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5026: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemKilledByPlayerCondition.killedByPlayer:()Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  5029: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5032: ldc_w           0.025
        //  5035: ldc_w           0.01
        //  5038: invokestatic    net/minecraft/world/level/storage/loot/predicates/LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost:(FF)Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;
        //  5041: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.when:(Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5044: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5047: invokespecial   net/minecraft/data/loot/EntityLoot.add:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;)V
        //  5050: invokestatic    com/google/common/collect/Sets.newHashSet:()Ljava/util/HashSet;
        //  5053: astore_2        /* set3 */
        //  5054: getstatic       net/minecraft/core/Registry.ENTITY_TYPE:Lnet/minecraft/core/DefaultedRegistry;
        //  5057: invokevirtual   net/minecraft/core/DefaultedRegistry.iterator:()Ljava/util/Iterator;
        //  5060: astore_3       
        //  5061: aload_3        
        //  5062: invokeinterface java/util/Iterator.hasNext:()Z
        //  5067: ifeq            5258
        //  5070: aload_3        
        //  5071: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  5076: checkcast       Lnet/minecraft/world/entity/EntityType;
        //  5079: astore          aqb5
        //  5081: aload           aqb5
        //  5083: invokevirtual   net/minecraft/world/entity/EntityType.getDefaultLootTable:()Lnet/minecraft/resources/ResourceLocation;
        //  5086: astore          vk6
        //  5088: getstatic       net/minecraft/data/loot/EntityLoot.SPECIAL_LOOT_TABLE_TYPES:Ljava/util/Set;
        //  5091: aload           aqb5
        //  5093: invokeinterface java/util/Set.contains:(Ljava/lang/Object;)Z
        //  5098: ifne            5112
        //  5101: aload           aqb5
        //  5103: invokevirtual   net/minecraft/world/entity/EntityType.getCategory:()Lnet/minecraft/world/entity/MobCategory;
        //  5106: getstatic       net/minecraft/world/entity/MobCategory.MISC:Lnet/minecraft/world/entity/MobCategory;
        //  5109: if_acmpeq       5199
        //  5112: aload           vk6
        //  5114: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.EMPTY:Lnet/minecraft/resources/ResourceLocation;
        //  5117: if_acmpeq       5255
        //  5120: aload_2         /* set3 */
        //  5121: aload           vk6
        //  5123: invokeinterface java/util/Set.add:(Ljava/lang/Object;)Z
        //  5128: ifeq            5255
        //  5131: aload_0         /* this */
        //  5132: getfield        net/minecraft/data/loot/EntityLoot.map:Ljava/util/Map;
        //  5135: aload           vk6
        //  5137: invokeinterface java/util/Map.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //  5142: checkcast       Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5145: astore          a7
        //  5147: aload           a7
        //  5149: ifnonnull       5186
        //  5152: new             Ljava/lang/IllegalStateException;
        //  5155: dup            
        //  5156: ldc_w           "Missing loottable '%s' for '%s'"
        //  5159: iconst_2       
        //  5160: anewarray       Ljava/lang/Object;
        //  5163: dup            
        //  5164: iconst_0       
        //  5165: aload           vk6
        //  5167: aastore        
        //  5168: dup            
        //  5169: iconst_1       
        //  5170: getstatic       net/minecraft/core/Registry.ENTITY_TYPE:Lnet/minecraft/core/DefaultedRegistry;
        //  5173: aload           aqb5
        //  5175: invokevirtual   net/minecraft/core/DefaultedRegistry.getKey:(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;
        //  5178: aastore        
        //  5179: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //  5182: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  5185: athrow         
        //  5186: aload_1         /* biConsumer */
        //  5187: aload           vk6
        //  5189: aload           a7
        //  5191: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5196: goto            5255
        //  5199: aload           vk6
        //  5201: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.EMPTY:Lnet/minecraft/resources/ResourceLocation;
        //  5204: if_acmpeq       5255
        //  5207: aload_0         /* this */
        //  5208: getfield        net/minecraft/data/loot/EntityLoot.map:Ljava/util/Map;
        //  5211: aload           vk6
        //  5213: invokeinterface java/util/Map.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //  5218: ifnull          5255
        //  5221: new             Ljava/lang/IllegalStateException;
        //  5224: dup            
        //  5225: ldc_w           "Weird loottable '%s' for '%s', not a LivingEntity so should not have loot"
        //  5228: iconst_2       
        //  5229: anewarray       Ljava/lang/Object;
        //  5232: dup            
        //  5233: iconst_0       
        //  5234: aload           vk6
        //  5236: aastore        
        //  5237: dup            
        //  5238: iconst_1       
        //  5239: getstatic       net/minecraft/core/Registry.ENTITY_TYPE:Lnet/minecraft/core/DefaultedRegistry;
        //  5242: aload           aqb5
        //  5244: invokevirtual   net/minecraft/core/DefaultedRegistry.getKey:(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;
        //  5247: aastore        
        //  5248: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //  5251: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  5254: athrow         
        //  5255: goto            5061
        //  5258: aload_0         /* this */
        //  5259: getfield        net/minecraft/data/loot/EntityLoot.map:Ljava/util/Map;
        //  5262: aload_1         /* biConsumer */
        //  5263: invokedynamic   BootstrapMethod #1, accept:(Ljava/util/function/BiConsumer;)Ljava/util/function/BiConsumer;
        //  5268: invokeinterface java/util/Map.forEach:(Ljava/util/function/BiConsumer;)V
        //  5273: return         
        //    Signature:
        //  (Ljava/util/function/BiConsumer<Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;>;)V
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  biConsumer  
        //    StackMapTable: 00 06 FD 13 C5 07 03 30 07 03 32 FD 00 32 07 00 7F 07 03 4E FF 00 49 00 07 07 00 02 07 03 6C 07 03 30 07 03 32 00 07 03 4E 07 00 0F 00 00 FF 00 0C 00 06 07 00 02 07 03 6C 07 03 30 07 03 32 07 00 7F 07 03 4E 00 00 F9 00 37 F9 00 02
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException: The requested operation is not supported.
        //     at com.strobel.util.ContractUtils.unsupported(ContractUtils.java:27)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:276)
        //     at com.strobel.assembler.metadata.TypeReference.getRawType(TypeReference.java:271)
        //     at com.strobel.assembler.metadata.TypeReference.makeGenericType(TypeReference.java:150)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:187)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:276)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2591)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
    
    private void add(final EntityType<?> aqb, final LootTable.Builder a) {
        this.add(aqb.getDefaultLootTable(), a);
    }
    
    private void add(final ResourceLocation vk, final LootTable.Builder a) {
        this.map.put(vk, a);
    }
    
    static {
        ENTITY_ON_FIRE = EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build());
        SPECIAL_LOOT_TABLE_TYPES = (Set)ImmutableSet.of(EntityType.PLAYER, EntityType.ARMOR_STAND, EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM, EntityType.VILLAGER);
    }
}
