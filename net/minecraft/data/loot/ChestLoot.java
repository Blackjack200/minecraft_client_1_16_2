package net.minecraft.data.loot;

import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChestLoot implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
    public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.ABANDONED_MINESHAFT:Lnet/minecraft/resources/ResourceLocation;
        //     4: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //     7: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    10: iconst_1       
        //    11: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //    14: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    17: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //    20: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    23: bipush          20
        //    25: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    28: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    31: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //    34: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    37: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    40: getstatic       net/minecraft/world/item/Items.NAME_TAG:Lnet/minecraft/world/item/Item;
        //    43: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    46: bipush          30
        //    48: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    51: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    54: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //    57: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    60: bipush          10
        //    62: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    65: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //    68: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    71: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    74: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //    77: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    80: iconst_5       
        //    81: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    84: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    87: invokestatic    net/minecraft/world/level/storage/loot/entries/EmptyLootItem.emptyItem:()Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    90: iconst_5       
        //    91: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //    94: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //    97: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   100: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   103: fconst_2       
        //   104: ldc             4.0
        //   106: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   109: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   112: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //   115: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   118: bipush          10
        //   120: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   123: fconst_1       
        //   124: ldc             5.0
        //   126: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   129: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   132: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   135: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   138: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //   141: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   144: iconst_5       
        //   145: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   148: fconst_1       
        //   149: ldc             3.0
        //   151: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   154: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   157: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   160: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   163: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        //   166: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   169: iconst_5       
        //   170: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   173: ldc             4.0
        //   175: ldc             9.0
        //   177: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   180: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   183: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   186: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   189: getstatic       net/minecraft/world/item/Items.LAPIS_LAZULI:Lnet/minecraft/world/item/Item;
        //   192: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   195: iconst_5       
        //   196: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   199: ldc             4.0
        //   201: ldc             9.0
        //   203: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   206: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   209: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   212: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   215: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //   218: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   221: iconst_3       
        //   222: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   225: fconst_1       
        //   226: fconst_2       
        //   227: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   230: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   233: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   236: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   239: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //   242: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   245: bipush          10
        //   247: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   250: ldc             3.0
        //   252: ldc             8.0
        //   254: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   257: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   260: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   263: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   266: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //   269: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   272: bipush          15
        //   274: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   277: fconst_1       
        //   278: ldc             3.0
        //   280: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   283: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   286: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   289: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   292: getstatic       net/minecraft/world/item/Items.MELON_SEEDS:Lnet/minecraft/world/item/Item;
        //   295: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   298: bipush          10
        //   300: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   303: fconst_2       
        //   304: ldc             4.0
        //   306: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   309: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   312: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   315: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   318: getstatic       net/minecraft/world/item/Items.PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;
        //   321: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   324: bipush          10
        //   326: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   329: fconst_2       
        //   330: ldc             4.0
        //   332: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   335: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   338: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   341: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   344: getstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        //   347: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   350: bipush          10
        //   352: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   355: fconst_2       
        //   356: ldc             4.0
        //   358: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   361: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   364: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   367: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   370: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   373: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   376: iconst_3       
        //   377: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   380: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   383: getstatic       net/minecraft/world/level/block/Blocks.RAIL:Lnet/minecraft/world/level/block/Block;
        //   386: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   389: bipush          20
        //   391: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   394: ldc             4.0
        //   396: ldc             8.0
        //   398: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   401: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   404: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   407: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   410: getstatic       net/minecraft/world/level/block/Blocks.POWERED_RAIL:Lnet/minecraft/world/level/block/Block;
        //   413: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   416: iconst_5       
        //   417: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   420: fconst_1       
        //   421: ldc             4.0
        //   423: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   426: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   429: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   432: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   435: getstatic       net/minecraft/world/level/block/Blocks.DETECTOR_RAIL:Lnet/minecraft/world/level/block/Block;
        //   438: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   441: iconst_5       
        //   442: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   445: fconst_1       
        //   446: ldc             4.0
        //   448: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   451: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   454: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   457: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   460: getstatic       net/minecraft/world/level/block/Blocks.ACTIVATOR_RAIL:Lnet/minecraft/world/level/block/Block;
        //   463: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   466: iconst_5       
        //   467: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   470: fconst_1       
        //   471: ldc             4.0
        //   473: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   476: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   479: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   482: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   485: getstatic       net/minecraft/world/level/block/Blocks.TORCH:Lnet/minecraft/world/level/block/Block;
        //   488: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   491: bipush          15
        //   493: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   496: fconst_1       
        //   497: ldc             16.0
        //   499: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   502: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   505: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   508: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   511: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   514: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //   519: aload_1         /* biConsumer */
        //   520: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.BASTION_BRIDGE:Lnet/minecraft/resources/ResourceLocation;
        //   523: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   526: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   529: iconst_1       
        //   530: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   533: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   536: getstatic       net/minecraft/world/level/block/Blocks.LODESTONE:Lnet/minecraft/world/level/block/Block;
        //   539: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   542: iconst_1       
        //   543: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   546: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   549: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   552: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   555: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   558: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   561: fconst_1       
        //   562: fconst_2       
        //   563: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   566: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   569: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
        //   572: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   575: ldc             0.1
        //   577: ldc             0.5
        //   579: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   582: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   585: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   588: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   591: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   594: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   597: getstatic       net/minecraft/world/item/Items.SPECTRAL_ARROW:Lnet/minecraft/world/item/Item;
        //   600: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   603: ldc             10.0
        //   605: ldc             28.0
        //   607: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   610: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   613: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   616: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   619: getstatic       net/minecraft/world/level/block/Blocks.GILDED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        //   622: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   625: ldc             8.0
        //   627: ldc             12.0
        //   629: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   632: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   635: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   638: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   641: getstatic       net/minecraft/world/level/block/Blocks.CRYING_OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //   644: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   647: ldc             3.0
        //   649: ldc             8.0
        //   651: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   654: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   657: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   660: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   663: getstatic       net/minecraft/world/level/block/Blocks.GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //   666: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   669: iconst_1       
        //   670: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   673: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   676: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   679: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   682: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //   685: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   688: ldc             4.0
        //   690: ldc             9.0
        //   692: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   695: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   698: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   701: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   704: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //   707: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   710: ldc             4.0
        //   712: ldc             9.0
        //   714: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   717: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   720: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   723: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   726: getstatic       net/minecraft/world/item/Items.GOLDEN_SWORD:Lnet/minecraft/world/item/Item;
        //   729: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   732: iconst_1       
        //   733: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   736: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   739: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   742: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   745: getstatic       net/minecraft/world/item/Items.GOLDEN_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //   748: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   751: iconst_1       
        //   752: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   755: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   758: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   761: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   764: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   767: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   770: getstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        //   773: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   776: iconst_1       
        //   777: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   780: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   783: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   786: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   789: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   792: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   795: getstatic       net/minecraft/world/item/Items.GOLDEN_LEGGINGS:Lnet/minecraft/world/item/Item;
        //   798: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   801: iconst_1       
        //   802: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   805: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   808: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   811: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   814: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   817: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   820: getstatic       net/minecraft/world/item/Items.GOLDEN_BOOTS:Lnet/minecraft/world/item/Item;
        //   823: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   826: iconst_1       
        //   827: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   830: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   833: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   836: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   839: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   842: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   845: getstatic       net/minecraft/world/item/Items.GOLDEN_AXE:Lnet/minecraft/world/item/Item;
        //   848: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   851: iconst_1       
        //   852: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //   855: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   858: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   861: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   864: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   867: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   870: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   873: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   876: fconst_2       
        //   877: ldc             4.0
        //   879: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   882: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   885: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //   888: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   891: fconst_1       
        //   892: ldc_w           6.0
        //   895: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   898: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   901: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   904: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   907: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //   910: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   913: fconst_1       
        //   914: ldc             3.0
        //   916: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   919: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   922: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   925: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   928: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //   931: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   934: ldc             5.0
        //   936: ldc_w           17.0
        //   939: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   942: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   945: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   948: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   951: getstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        //   954: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   957: fconst_2       
        //   958: ldc_w           6.0
        //   961: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   964: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   967: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   970: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   973: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //   976: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   979: fconst_2       
        //   980: ldc_w           6.0
        //   983: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //   986: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //   989: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //   992: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //   995: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //   998: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1003: aload_1         /* biConsumer */
        //  1004: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.BASTION_HOGLIN_STABLE:Lnet/minecraft/resources/ResourceLocation;
        //  1007: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1010: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1013: iconst_1       
        //  1014: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1017: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1020: getstatic       net/minecraft/world/item/Items.DIAMOND_SHOVEL:Lnet/minecraft/world/item/Item;
        //  1023: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1026: bipush          15
        //  1028: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1031: ldc_w           0.15
        //  1034: ldc_w           0.8
        //  1037: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1040: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1043: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1046: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1049: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1052: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1055: getstatic       net/minecraft/world/item/Items.DIAMOND_PICKAXE:Lnet/minecraft/world/item/Item;
        //  1058: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1061: bipush          12
        //  1063: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1066: ldc_w           0.15
        //  1069: ldc_w           0.95
        //  1072: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1075: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1078: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1081: iconst_1       
        //  1082: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1085: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1088: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1091: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1094: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1097: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1100: getstatic       net/minecraft/world/item/Items.NETHERITE_SCRAP:Lnet/minecraft/world/item/Item;
        //  1103: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1106: bipush          8
        //  1108: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1111: iconst_1       
        //  1112: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1115: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1118: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1121: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1124: getstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        //  1127: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1130: bipush          12
        //  1132: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1135: iconst_1       
        //  1136: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1139: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1142: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1145: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1148: getstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        //  1151: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1154: iconst_5       
        //  1155: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1158: iconst_2       
        //  1159: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1162: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1165: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1168: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1171: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  1174: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1177: bipush          12
        //  1179: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1182: iconst_1       
        //  1183: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1186: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1189: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1192: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1195: getstatic       net/minecraft/world/level/block/Blocks.GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  1198: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1201: bipush          16
        //  1203: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1206: fconst_2       
        //  1207: ldc             4.0
        //  1209: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1212: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1215: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1218: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1221: getstatic       net/minecraft/world/item/Items.GOLDEN_CARROT:Lnet/minecraft/world/item/Item;
        //  1224: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1227: bipush          10
        //  1229: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1232: ldc             8.0
        //  1234: ldc_w           17.0
        //  1237: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1240: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1243: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1246: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1249: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  1252: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1255: bipush          10
        //  1257: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1260: iconst_1       
        //  1261: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1264: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1267: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1270: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1273: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1276: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1279: ldc             3.0
        //  1281: ldc             4.0
        //  1283: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1286: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1289: getstatic       net/minecraft/world/item/Items.GOLDEN_AXE:Lnet/minecraft/world/item/Item;
        //  1292: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1295: iconst_1       
        //  1296: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1299: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1302: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1305: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1308: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1311: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1314: getstatic       net/minecraft/world/level/block/Blocks.CRYING_OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  1317: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1320: fconst_1       
        //  1321: ldc             5.0
        //  1323: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1326: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1329: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1332: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1335: getstatic       net/minecraft/world/level/block/Blocks.GLOWSTONE:Lnet/minecraft/world/level/block/Block;
        //  1338: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1341: ldc             3.0
        //  1343: ldc_w           6.0
        //  1346: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1349: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1352: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1355: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1358: getstatic       net/minecraft/world/level/block/Blocks.GILDED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        //  1361: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1364: fconst_2       
        //  1365: ldc             5.0
        //  1367: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1370: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1373: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1376: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1379: getstatic       net/minecraft/world/level/block/Blocks.SOUL_SAND:Lnet/minecraft/world/level/block/Block;
        //  1382: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1385: fconst_2       
        //  1386: ldc_w           7.0
        //  1389: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1392: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1395: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1398: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1401: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_NYLIUM:Lnet/minecraft/world/level/block/Block;
        //  1404: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1407: fconst_2       
        //  1408: ldc_w           7.0
        //  1411: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1414: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1417: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1420: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1423: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  1426: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1429: fconst_2       
        //  1430: ldc             8.0
        //  1432: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1435: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1438: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1441: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1444: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        //  1447: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1450: fconst_1       
        //  1451: ldc             3.0
        //  1453: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1456: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1459: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1462: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1465: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  1468: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1471: ldc             5.0
        //  1473: ldc_w           17.0
        //  1476: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1479: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1482: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1485: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1488: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  1491: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1494: ldc             3.0
        //  1496: ldc             8.0
        //  1498: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1501: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1504: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1507: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1510: getstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
        //  1513: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1516: fconst_2       
        //  1517: ldc             5.0
        //  1519: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1522: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1525: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1528: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1531: getstatic       net/minecraft/world/item/Items.COOKED_PORKCHOP:Lnet/minecraft/world/item/Item;
        //  1534: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1537: fconst_2       
        //  1538: ldc             5.0
        //  1540: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1543: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1546: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1549: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1552: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_FUNGUS:Lnet/minecraft/world/level/block/Block;
        //  1555: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1558: fconst_2       
        //  1559: ldc_w           7.0
        //  1562: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1565: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1568: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1571: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1574: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_ROOTS:Lnet/minecraft/world/level/block/Block;
        //  1577: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1580: fconst_2       
        //  1581: ldc_w           7.0
        //  1584: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1587: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1590: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1593: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1596: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1599: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  1604: aload_1         /* biConsumer */
        //  1605: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.BASTION_OTHER:Lnet/minecraft/resources/ResourceLocation;
        //  1608: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1611: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1614: iconst_1       
        //  1615: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1618: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1621: getstatic       net/minecraft/world/item/Items.DIAMOND_PICKAXE:Lnet/minecraft/world/item/Item;
        //  1624: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1627: bipush          6
        //  1629: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1632: iconst_1       
        //  1633: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1636: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1639: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1642: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1645: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1648: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1651: getstatic       net/minecraft/world/item/Items.DIAMOND_SHOVEL:Lnet/minecraft/world/item/Item;
        //  1654: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1657: bipush          6
        //  1659: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1662: iconst_1       
        //  1663: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1666: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1669: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1672: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1675: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
        //  1678: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1681: bipush          6
        //  1683: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1686: ldc             0.1
        //  1688: ldc_w           0.9
        //  1691: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1694: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1697: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1700: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1703: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1706: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1709: getstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        //  1712: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1715: bipush          12
        //  1717: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1720: iconst_1       
        //  1721: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1724: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1727: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1730: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1733: getstatic       net/minecraft/world/item/Items.NETHERITE_SCRAP:Lnet/minecraft/world/item/Item;
        //  1736: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1739: iconst_4       
        //  1740: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1743: iconst_1       
        //  1744: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1747: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1750: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1753: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1756: getstatic       net/minecraft/world/item/Items.SPECTRAL_ARROW:Lnet/minecraft/world/item/Item;
        //  1759: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1762: bipush          10
        //  1764: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1767: ldc             10.0
        //  1769: ldc_w           22.0
        //  1772: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1775: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1778: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1781: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1784: getstatic       net/minecraft/world/item/Items.PIGLIN_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        //  1787: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1790: bipush          9
        //  1792: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1795: iconst_1       
        //  1796: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1799: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1802: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1805: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1808: getstatic       net/minecraft/world/item/Items.MUSIC_DISC_PIGSTEP:Lnet/minecraft/world/item/Item;
        //  1811: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1814: iconst_5       
        //  1815: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1818: iconst_1       
        //  1819: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1822: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1825: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1828: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1831: getstatic       net/minecraft/world/item/Items.GOLDEN_CARROT:Lnet/minecraft/world/item/Item;
        //  1834: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1837: bipush          12
        //  1839: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1842: ldc_w           6.0
        //  1845: ldc_w           17.0
        //  1848: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1851: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1854: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1857: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1860: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  1863: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1866: bipush          9
        //  1868: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1871: iconst_1       
        //  1872: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1875: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1878: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1881: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1884: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  1887: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1890: bipush          10
        //  1892: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1895: new             Lnet/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder;
        //  1898: dup            
        //  1899: invokespecial   net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder.<init>:()V
        //  1902: getstatic       net/minecraft/world/item/enchantment/Enchantments.SOUL_SPEED:Lnet/minecraft/world/item/enchantment/Enchantment;
        //  1905: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder.withEnchantment:(Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder;
        //  1908: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1911: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1914: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  1917: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1920: iconst_2       
        //  1921: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1924: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1927: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  1930: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1933: iconst_2       
        //  1934: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1937: ldc             0.1
        //  1939: ldc_w           0.9
        //  1942: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  1945: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1948: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1951: iconst_1       
        //  1952: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1955: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1958: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1961: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1964: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1967: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1970: getstatic       net/minecraft/world/level/block/Blocks.IRON_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  1973: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1976: iconst_2       
        //  1977: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1980: iconst_1       
        //  1981: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  1984: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  1987: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1990: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  1993: getstatic       net/minecraft/world/item/Items.GOLDEN_BOOTS:Lnet/minecraft/world/item/Item;
        //  1996: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  1999: iconst_1       
        //  2000: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2003: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2006: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2009: new             Lnet/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder;
        //  2012: dup            
        //  2013: invokespecial   net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder.<init>:()V
        //  2016: getstatic       net/minecraft/world/item/enchantment/Enchantments.SOUL_SPEED:Lnet/minecraft/world/item/enchantment/Enchantment;
        //  2019: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder.withEnchantment:(Lnet/minecraft/world/item/enchantment/Enchantment;)Lnet/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction$Builder;
        //  2022: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2025: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2028: getstatic       net/minecraft/world/item/Items.GOLDEN_AXE:Lnet/minecraft/world/item/Item;
        //  2031: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2034: iconst_1       
        //  2035: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2038: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2041: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2044: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2047: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2050: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2053: getstatic       net/minecraft/world/level/block/Blocks.GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2056: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2059: iconst_2       
        //  2060: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2063: iconst_1       
        //  2064: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2067: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2070: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2073: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2076: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
        //  2079: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2082: iconst_1       
        //  2083: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2086: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2089: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2092: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2095: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  2098: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2101: iconst_2       
        //  2102: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2105: fconst_1       
        //  2106: ldc_w           6.0
        //  2109: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2112: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2115: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2118: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2121: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  2124: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2127: iconst_2       
        //  2128: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2131: fconst_1       
        //  2132: ldc_w           6.0
        //  2135: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2138: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2141: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2144: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2147: getstatic       net/minecraft/world/item/Items.GOLDEN_SWORD:Lnet/minecraft/world/item/Item;
        //  2150: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2153: iconst_1       
        //  2154: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2157: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2160: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2163: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2166: getstatic       net/minecraft/world/item/Items.GOLDEN_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  2169: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2172: iconst_1       
        //  2173: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2176: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2179: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2182: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2185: getstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        //  2188: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2191: iconst_1       
        //  2192: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2195: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2198: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2201: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2204: getstatic       net/minecraft/world/item/Items.GOLDEN_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  2207: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2210: iconst_1       
        //  2211: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2214: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2217: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2220: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2223: getstatic       net/minecraft/world/item/Items.GOLDEN_BOOTS:Lnet/minecraft/world/item/Item;
        //  2226: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2229: iconst_1       
        //  2230: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2233: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2236: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2239: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2242: getstatic       net/minecraft/world/level/block/Blocks.CRYING_OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  2245: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2248: iconst_2       
        //  2249: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2252: fconst_1       
        //  2253: ldc             5.0
        //  2255: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2258: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2261: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2264: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2267: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2270: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2273: ldc             3.0
        //  2275: ldc             4.0
        //  2277: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2280: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2283: getstatic       net/minecraft/world/level/block/Blocks.GILDED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        //  2286: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2289: iconst_2       
        //  2290: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2293: fconst_1       
        //  2294: ldc             5.0
        //  2296: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2299: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2302: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2305: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2308: getstatic       net/minecraft/world/level/block/Blocks.CHAIN:Lnet/minecraft/world/level/block/Block;
        //  2311: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2314: fconst_2       
        //  2315: ldc             10.0
        //  2317: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2320: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2323: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2326: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2329: getstatic       net/minecraft/world/item/Items.MAGMA_CREAM:Lnet/minecraft/world/item/Item;
        //  2332: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2335: iconst_2       
        //  2336: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2339: fconst_2       
        //  2340: ldc_w           6.0
        //  2343: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2346: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2349: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2352: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2355: getstatic       net/minecraft/world/level/block/Blocks.BONE_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2358: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2361: ldc             3.0
        //  2363: ldc_w           6.0
        //  2366: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2369: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2372: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2375: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2378: getstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        //  2381: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2384: fconst_2       
        //  2385: ldc             8.0
        //  2387: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2390: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2393: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2396: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2399: getstatic       net/minecraft/world/level/block/Blocks.OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  2402: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2405: ldc             4.0
        //  2407: ldc_w           6.0
        //  2410: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2413: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2416: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2419: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2422: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  2425: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2428: fconst_2       
        //  2429: ldc             8.0
        //  2431: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2434: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2437: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2440: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2443: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  2446: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2449: ldc             4.0
        //  2451: ldc_w           6.0
        //  2454: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2457: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2460: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2463: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2466: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  2469: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2472: iconst_2       
        //  2473: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2476: ldc             5.0
        //  2478: ldc_w           17.0
        //  2481: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2484: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2487: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2490: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2493: getstatic       net/minecraft/world/item/Items.COOKED_PORKCHOP:Lnet/minecraft/world/item/Item;
        //  2496: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2499: iconst_1       
        //  2500: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2503: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2506: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2509: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2512: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2515: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  2520: aload_1         /* biConsumer */
        //  2521: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.BASTION_TREASURE:Lnet/minecraft/resources/ResourceLocation;
        //  2524: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2527: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2530: iconst_3       
        //  2531: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2534: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2537: getstatic       net/minecraft/world/item/Items.NETHERITE_INGOT:Lnet/minecraft/world/item/Item;
        //  2540: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2543: bipush          15
        //  2545: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2548: iconst_1       
        //  2549: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2552: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2555: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2558: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2561: getstatic       net/minecraft/world/level/block/Blocks.ANCIENT_DEBRIS:Lnet/minecraft/world/level/block/Block;
        //  2564: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2567: bipush          10
        //  2569: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2572: iconst_1       
        //  2573: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2576: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2579: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2582: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2585: getstatic       net/minecraft/world/item/Items.NETHERITE_SCRAP:Lnet/minecraft/world/item/Item;
        //  2588: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2591: bipush          8
        //  2593: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2596: iconst_1       
        //  2597: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2600: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2603: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2606: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2609: getstatic       net/minecraft/world/level/block/Blocks.ANCIENT_DEBRIS:Lnet/minecraft/world/level/block/Block;
        //  2612: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2615: iconst_4       
        //  2616: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2619: iconst_2       
        //  2620: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2623: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2626: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2629: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2632: getstatic       net/minecraft/world/item/Items.DIAMOND_SWORD:Lnet/minecraft/world/item/Item;
        //  2635: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2638: bipush          6
        //  2640: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2643: ldc_w           0.8
        //  2646: fconst_1       
        //  2647: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2650: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2653: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2656: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2659: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2662: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2665: getstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  2668: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2671: bipush          6
        //  2673: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2676: ldc_w           0.8
        //  2679: fconst_1       
        //  2680: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2683: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2686: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2689: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2692: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2695: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2698: getstatic       net/minecraft/world/item/Items.DIAMOND_HELMET:Lnet/minecraft/world/item/Item;
        //  2701: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2704: bipush          6
        //  2706: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2709: ldc_w           0.8
        //  2712: fconst_1       
        //  2713: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2716: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2719: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2722: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2725: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2728: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2731: getstatic       net/minecraft/world/item/Items.DIAMOND_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  2734: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2737: bipush          6
        //  2739: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2742: ldc_w           0.8
        //  2745: fconst_1       
        //  2746: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2749: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2752: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2755: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2758: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2761: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2764: getstatic       net/minecraft/world/item/Items.DIAMOND_BOOTS:Lnet/minecraft/world/item/Item;
        //  2767: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2770: bipush          6
        //  2772: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2775: ldc_w           0.8
        //  2778: fconst_1       
        //  2779: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2782: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemDamageFunction.setDamage:(Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2785: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2788: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2791: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2794: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2797: getstatic       net/minecraft/world/item/Items.DIAMOND_SWORD:Lnet/minecraft/world/item/Item;
        //  2800: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2803: bipush          6
        //  2805: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2808: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2811: getstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  2814: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2817: iconst_5       
        //  2818: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2821: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2824: getstatic       net/minecraft/world/item/Items.DIAMOND_HELMET:Lnet/minecraft/world/item/Item;
        //  2827: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2830: iconst_5       
        //  2831: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2834: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2837: getstatic       net/minecraft/world/item/Items.DIAMOND_BOOTS:Lnet/minecraft/world/item/Item;
        //  2840: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2843: iconst_5       
        //  2844: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2847: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2850: getstatic       net/minecraft/world/item/Items.DIAMOND_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  2853: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2856: iconst_5       
        //  2857: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2860: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2863: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  2866: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2869: iconst_5       
        //  2870: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2873: fconst_2       
        //  2874: ldc_w           6.0
        //  2877: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2880: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2883: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2886: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2889: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  2892: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2895: iconst_2       
        //  2896: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2899: iconst_1       
        //  2900: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  2903: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2906: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2909: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2912: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  2915: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2918: ldc             3.0
        //  2920: ldc             4.0
        //  2922: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2925: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2928: getstatic       net/minecraft/world/item/Items.SPECTRAL_ARROW:Lnet/minecraft/world/item/Item;
        //  2931: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2934: ldc             12.0
        //  2936: ldc_w           25.0
        //  2939: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2942: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2945: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2948: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2951: getstatic       net/minecraft/world/level/block/Blocks.GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2954: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2957: fconst_2       
        //  2958: ldc             5.0
        //  2960: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2963: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2966: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2969: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2972: getstatic       net/minecraft/world/level/block/Blocks.IRON_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2975: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2978: fconst_2       
        //  2979: ldc             5.0
        //  2981: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  2984: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  2987: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2990: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  2993: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  2996: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  2999: ldc             3.0
        //  3001: ldc             9.0
        //  3003: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3006: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3009: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3012: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3015: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  3018: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3021: ldc             3.0
        //  3023: ldc             9.0
        //  3025: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3028: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3031: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3034: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3037: getstatic       net/minecraft/world/level/block/Blocks.CRYING_OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  3040: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3043: ldc             3.0
        //  3045: ldc             5.0
        //  3047: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3050: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3053: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3056: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3059: getstatic       net/minecraft/world/item/Items.QUARTZ:Lnet/minecraft/world/item/Item;
        //  3062: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3065: ldc             8.0
        //  3067: ldc_w           23.0
        //  3070: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3073: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3076: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3079: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3082: getstatic       net/minecraft/world/level/block/Blocks.GILDED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        //  3085: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3088: ldc             5.0
        //  3090: ldc_w           15.0
        //  3093: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3096: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3099: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3102: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3105: getstatic       net/minecraft/world/item/Items.MAGMA_CREAM:Lnet/minecraft/world/item/Item;
        //  3108: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3111: ldc             3.0
        //  3113: ldc             8.0
        //  3115: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3118: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3121: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3124: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3127: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3130: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3135: aload_1         /* biConsumer */
        //  3136: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.BURIED_TREASURE:Lnet/minecraft/resources/ResourceLocation;
        //  3139: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3142: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3145: iconst_1       
        //  3146: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3149: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3152: getstatic       net/minecraft/world/item/Items.HEART_OF_THE_SEA:Lnet/minecraft/world/item/Item;
        //  3155: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3158: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3161: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3164: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3167: ldc             5.0
        //  3169: ldc             8.0
        //  3171: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3174: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3177: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  3180: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3183: bipush          20
        //  3185: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3188: fconst_1       
        //  3189: ldc             4.0
        //  3191: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3194: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3197: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3200: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3203: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  3206: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3209: bipush          10
        //  3211: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3214: fconst_1       
        //  3215: ldc             4.0
        //  3217: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3220: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3223: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3226: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3229: getstatic       net/minecraft/world/level/block/Blocks.TNT:Lnet/minecraft/world/level/block/Block;
        //  3232: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3235: iconst_5       
        //  3236: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3239: fconst_1       
        //  3240: fconst_2       
        //  3241: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3244: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3247: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3250: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3253: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3256: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3259: fconst_1       
        //  3260: ldc             3.0
        //  3262: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3265: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3268: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  3271: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3274: iconst_5       
        //  3275: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3278: ldc             4.0
        //  3280: ldc             8.0
        //  3282: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3285: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3288: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3291: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3294: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  3297: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3300: iconst_5       
        //  3301: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3304: fconst_1       
        //  3305: fconst_2       
        //  3306: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3309: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3312: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3315: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3318: getstatic       net/minecraft/world/item/Items.PRISMARINE_CRYSTALS:Lnet/minecraft/world/item/Item;
        //  3321: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3324: iconst_5       
        //  3325: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3328: fconst_1       
        //  3329: ldc             5.0
        //  3331: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3334: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3337: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3340: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3343: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3346: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3349: fconst_0       
        //  3350: fconst_1       
        //  3351: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3354: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3357: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  3360: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3363: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3366: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  3369: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3372: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3375: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3378: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3381: iconst_2       
        //  3382: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3385: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3388: getstatic       net/minecraft/world/item/Items.COOKED_COD:Lnet/minecraft/world/item/Item;
        //  3391: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3394: fconst_2       
        //  3395: ldc             4.0
        //  3397: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3400: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3403: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3406: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3409: getstatic       net/minecraft/world/item/Items.COOKED_SALMON:Lnet/minecraft/world/item/Item;
        //  3412: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3415: fconst_2       
        //  3416: ldc             4.0
        //  3418: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3421: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3424: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3427: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3430: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3433: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3438: aload_1         /* biConsumer */
        //  3439: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.DESERT_PYRAMID:Lnet/minecraft/resources/ResourceLocation;
        //  3442: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3445: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3448: fconst_2       
        //  3449: ldc             4.0
        //  3451: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3454: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3457: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  3460: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3463: iconst_5       
        //  3464: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3467: fconst_1       
        //  3468: ldc             3.0
        //  3470: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3473: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3476: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3479: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3482: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  3485: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3488: bipush          15
        //  3490: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3493: fconst_1       
        //  3494: ldc             5.0
        //  3496: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3499: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3502: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3505: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3508: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  3511: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3514: bipush          15
        //  3516: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3519: fconst_2       
        //  3520: ldc_w           7.0
        //  3523: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3526: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3529: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3532: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3535: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  3538: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3541: bipush          15
        //  3543: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3546: fconst_1       
        //  3547: ldc             3.0
        //  3549: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3552: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3555: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3558: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3561: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  3564: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3567: bipush          25
        //  3569: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3572: ldc             4.0
        //  3574: ldc_w           6.0
        //  3577: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3580: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3583: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3586: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3589: getstatic       net/minecraft/world/item/Items.SPIDER_EYE:Lnet/minecraft/world/item/Item;
        //  3592: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3595: bipush          25
        //  3597: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3600: fconst_1       
        //  3601: ldc             3.0
        //  3603: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3606: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3609: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3612: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3615: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  3618: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3621: bipush          25
        //  3623: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3626: ldc             3.0
        //  3628: ldc_w           7.0
        //  3631: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3634: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3637: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3640: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3643: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  3646: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3649: bipush          20
        //  3651: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3654: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3657: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  3660: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3663: bipush          15
        //  3665: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3668: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3671: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  3674: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3677: bipush          10
        //  3679: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3682: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3685: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  3688: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3691: iconst_5       
        //  3692: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3695: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3698: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  3701: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3704: bipush          20
        //  3706: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3709: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3712: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3715: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3718: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  3721: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3724: bipush          20
        //  3726: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3729: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3732: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  3735: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3738: iconst_2       
        //  3739: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3742: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3745: invokestatic    net/minecraft/world/level/storage/loot/entries/EmptyLootItem.emptyItem:()Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3748: bipush          15
        //  3750: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3753: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3756: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3759: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3762: iconst_4       
        //  3763: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  3766: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3769: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  3772: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3775: bipush          10
        //  3777: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3780: fconst_1       
        //  3781: ldc             8.0
        //  3783: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3786: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3789: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3792: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3795: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  3798: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3801: bipush          10
        //  3803: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3806: fconst_1       
        //  3807: ldc             8.0
        //  3809: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3812: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3815: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3818: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3821: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  3824: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3827: bipush          10
        //  3829: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3832: fconst_1       
        //  3833: ldc             8.0
        //  3835: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3838: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3841: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3844: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3847: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  3850: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3853: bipush          10
        //  3855: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3858: fconst_1       
        //  3859: ldc             8.0
        //  3861: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3864: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3867: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3870: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3873: getstatic       net/minecraft/world/level/block/Blocks.SAND:Lnet/minecraft/world/level/block/Block;
        //  3876: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3879: bipush          10
        //  3881: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3884: fconst_1       
        //  3885: ldc             8.0
        //  3887: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3890: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3893: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3896: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3899: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3902: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  3907: aload_1         /* biConsumer */
        //  3908: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.END_CITY_TREASURE:Lnet/minecraft/resources/ResourceLocation;
        //  3911: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  3914: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3917: fconst_2       
        //  3918: ldc_w           6.0
        //  3921: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3924: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3927: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  3930: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3933: iconst_5       
        //  3934: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3937: fconst_2       
        //  3938: ldc_w           7.0
        //  3941: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3944: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3947: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3950: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3953: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  3956: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3959: bipush          10
        //  3961: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3964: ldc             4.0
        //  3966: ldc             8.0
        //  3968: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3971: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  3974: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3977: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  3980: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  3983: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3986: bipush          15
        //  3988: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  3991: fconst_2       
        //  3992: ldc_w           7.0
        //  3995: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  3998: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4001: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4004: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4007: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  4010: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4013: iconst_2       
        //  4014: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4017: fconst_2       
        //  4018: ldc_w           6.0
        //  4021: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4024: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4027: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4030: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4033: getstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        //  4036: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4039: iconst_5       
        //  4040: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4043: fconst_1       
        //  4044: ldc             10.0
        //  4046: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4049: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4052: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4055: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4058: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  4061: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4064: iconst_3       
        //  4065: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4068: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4071: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4074: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4077: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4080: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4083: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4086: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4089: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4092: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4095: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4098: getstatic       net/minecraft/world/item/Items.DIAMOND_SWORD:Lnet/minecraft/world/item/Item;
        //  4101: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4104: iconst_3       
        //  4105: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4108: ldc_w           20.0
        //  4111: ldc_w           39.0
        //  4114: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4117: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4120: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4123: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4126: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4129: getstatic       net/minecraft/world/item/Items.DIAMOND_BOOTS:Lnet/minecraft/world/item/Item;
        //  4132: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4135: iconst_3       
        //  4136: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4139: ldc_w           20.0
        //  4142: ldc_w           39.0
        //  4145: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4148: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4151: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4154: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4157: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4160: getstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  4163: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4166: iconst_3       
        //  4167: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4170: ldc_w           20.0
        //  4173: ldc_w           39.0
        //  4176: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4179: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4182: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4185: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4188: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4191: getstatic       net/minecraft/world/item/Items.DIAMOND_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  4194: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4197: iconst_3       
        //  4198: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4201: ldc_w           20.0
        //  4204: ldc_w           39.0
        //  4207: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4210: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4213: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4216: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4219: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4222: getstatic       net/minecraft/world/item/Items.DIAMOND_HELMET:Lnet/minecraft/world/item/Item;
        //  4225: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4228: iconst_3       
        //  4229: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4232: ldc_w           20.0
        //  4235: ldc_w           39.0
        //  4238: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4241: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4244: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4247: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4250: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4253: getstatic       net/minecraft/world/item/Items.DIAMOND_PICKAXE:Lnet/minecraft/world/item/Item;
        //  4256: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4259: iconst_3       
        //  4260: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4263: ldc_w           20.0
        //  4266: ldc_w           39.0
        //  4269: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4272: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4275: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4278: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4281: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4284: getstatic       net/minecraft/world/item/Items.DIAMOND_SHOVEL:Lnet/minecraft/world/item/Item;
        //  4287: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4290: iconst_3       
        //  4291: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4294: ldc_w           20.0
        //  4297: ldc_w           39.0
        //  4300: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4303: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4306: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4309: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4312: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4315: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  4318: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4321: iconst_3       
        //  4322: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4325: ldc_w           20.0
        //  4328: ldc_w           39.0
        //  4331: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4334: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4337: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4340: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4343: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4346: getstatic       net/minecraft/world/item/Items.IRON_BOOTS:Lnet/minecraft/world/item/Item;
        //  4349: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4352: iconst_3       
        //  4353: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4356: ldc_w           20.0
        //  4359: ldc_w           39.0
        //  4362: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4365: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4368: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4371: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4374: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4377: getstatic       net/minecraft/world/item/Items.IRON_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  4380: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4383: iconst_3       
        //  4384: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4387: ldc_w           20.0
        //  4390: ldc_w           39.0
        //  4393: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4396: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4399: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4402: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4405: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4408: getstatic       net/minecraft/world/item/Items.IRON_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  4411: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4414: iconst_3       
        //  4415: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4418: ldc_w           20.0
        //  4421: ldc_w           39.0
        //  4424: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4427: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4430: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4433: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4436: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4439: getstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
        //  4442: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4445: iconst_3       
        //  4446: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4449: ldc_w           20.0
        //  4452: ldc_w           39.0
        //  4455: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4458: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4461: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4464: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4467: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4470: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  4473: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4476: iconst_3       
        //  4477: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4480: ldc_w           20.0
        //  4483: ldc_w           39.0
        //  4486: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4489: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4492: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4495: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4498: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4501: getstatic       net/minecraft/world/item/Items.IRON_SHOVEL:Lnet/minecraft/world/item/Item;
        //  4504: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4507: iconst_3       
        //  4508: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4511: ldc_w           20.0
        //  4514: ldc_w           39.0
        //  4517: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4520: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4523: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4526: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4529: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4532: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4535: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  4540: aload_1         /* biConsumer */
        //  4541: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.IGLOO_CHEST:Lnet/minecraft/resources/ResourceLocation;
        //  4544: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4547: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4550: fconst_2       
        //  4551: ldc             8.0
        //  4553: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4556: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4559: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  4562: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4565: bipush          15
        //  4567: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4570: fconst_1       
        //  4571: ldc             3.0
        //  4573: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4576: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4579: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4582: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4585: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  4588: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4591: bipush          15
        //  4593: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4596: fconst_1       
        //  4597: ldc             4.0
        //  4599: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4602: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4605: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4608: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4611: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  4614: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4617: bipush          10
        //  4619: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4622: fconst_1       
        //  4623: ldc             3.0
        //  4625: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4628: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4631: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4634: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4637: getstatic       net/minecraft/world/item/Items.STONE_AXE:Lnet/minecraft/world/item/Item;
        //  4640: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4643: iconst_2       
        //  4644: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4647: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4650: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4653: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4656: bipush          10
        //  4658: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4661: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4664: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  4667: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4670: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4673: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  4676: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4679: bipush          10
        //  4681: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4684: fconst_2       
        //  4685: ldc             3.0
        //  4687: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4690: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4693: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4696: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4699: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4702: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4705: iconst_1       
        //  4706: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4709: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4712: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  4715: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4718: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4721: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4724: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  4729: aload_1         /* biConsumer */
        //  4730: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.JUNGLE_TEMPLE:Lnet/minecraft/resources/ResourceLocation;
        //  4733: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  4736: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4739: fconst_2       
        //  4740: ldc_w           6.0
        //  4743: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4746: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4749: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  4752: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4755: iconst_3       
        //  4756: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4759: fconst_1       
        //  4760: ldc             3.0
        //  4762: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4765: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4768: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4771: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4774: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  4777: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4780: bipush          10
        //  4782: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4785: fconst_1       
        //  4786: ldc             5.0
        //  4788: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4791: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4794: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4797: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4800: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  4803: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4806: bipush          15
        //  4808: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4811: fconst_2       
        //  4812: ldc_w           7.0
        //  4815: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4818: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4821: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4824: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4827: getstatic       net/minecraft/world/level/block/Blocks.BAMBOO:Lnet/minecraft/world/level/block/Block;
        //  4830: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4833: bipush          15
        //  4835: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4838: fconst_1       
        //  4839: ldc             3.0
        //  4841: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4844: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4847: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4850: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4853: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  4856: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4859: iconst_2       
        //  4860: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4863: fconst_1       
        //  4864: ldc             3.0
        //  4866: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4869: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4872: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4875: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4878: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  4881: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4884: bipush          20
        //  4886: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4889: ldc             4.0
        //  4891: ldc_w           6.0
        //  4894: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4897: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4900: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4903: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4906: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  4909: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4912: bipush          16
        //  4914: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4917: ldc             3.0
        //  4919: ldc_w           7.0
        //  4922: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  4925: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  4928: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4931: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4934: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  4937: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4940: iconst_3       
        //  4941: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4944: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4947: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4950: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4953: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4956: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4959: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4962: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4965: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  4968: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4971: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4974: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  4977: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4980: bipush          30
        //  4982: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  4985: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4988: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  4991: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  4994: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  4997: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5000: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5005: aload_1         /* biConsumer */
        //  5006: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER:Lnet/minecraft/resources/ResourceLocation;
        //  5009: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5012: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5015: fconst_1       
        //  5016: fconst_2       
        //  5017: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5020: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5023: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  5026: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5029: bipush          30
        //  5031: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5034: fconst_2       
        //  5035: ldc_w           7.0
        //  5038: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5041: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5044: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5047: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5050: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5053: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5058: aload_1         /* biConsumer */
        //  5059: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.NETHER_BRIDGE:Lnet/minecraft/resources/ResourceLocation;
        //  5062: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5065: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5068: fconst_2       
        //  5069: ldc             4.0
        //  5071: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5074: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5077: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  5080: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5083: iconst_5       
        //  5084: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5087: fconst_1       
        //  5088: ldc             3.0
        //  5090: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5093: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5096: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5099: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5102: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  5105: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5108: iconst_5       
        //  5109: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5112: fconst_1       
        //  5113: ldc             5.0
        //  5115: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5118: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5121: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5124: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5127: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  5130: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5133: bipush          15
        //  5135: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5138: fconst_1       
        //  5139: ldc             3.0
        //  5141: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5144: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5147: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5150: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5153: getstatic       net/minecraft/world/item/Items.GOLDEN_SWORD:Lnet/minecraft/world/item/Item;
        //  5156: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5159: iconst_5       
        //  5160: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5163: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5166: getstatic       net/minecraft/world/item/Items.GOLDEN_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  5169: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5172: iconst_5       
        //  5173: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5176: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5179: getstatic       net/minecraft/world/item/Items.FLINT_AND_STEEL:Lnet/minecraft/world/item/Item;
        //  5182: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5185: iconst_5       
        //  5186: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5189: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5192: getstatic       net/minecraft/world/item/Items.NETHER_WART:Lnet/minecraft/world/item/Item;
        //  5195: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5198: iconst_5       
        //  5199: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5202: ldc             3.0
        //  5204: ldc_w           7.0
        //  5207: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5210: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5213: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5216: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5219: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  5222: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5225: bipush          10
        //  5227: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5230: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5233: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  5236: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5239: bipush          8
        //  5241: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5244: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5247: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  5250: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5253: iconst_5       
        //  5254: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5257: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5260: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  5263: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5266: iconst_3       
        //  5267: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5270: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5273: getstatic       net/minecraft/world/level/block/Blocks.OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  5276: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5279: iconst_2       
        //  5280: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5283: fconst_2       
        //  5284: ldc             4.0
        //  5286: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5289: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5292: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5295: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5298: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5301: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5306: aload_1         /* biConsumer */
        //  5307: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.PILLAGER_OUTPOST:Lnet/minecraft/resources/ResourceLocation;
        //  5310: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5313: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5316: fconst_0       
        //  5317: fconst_1       
        //  5318: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5321: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5324: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
        //  5327: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5330: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5333: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5336: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5339: fconst_2       
        //  5340: ldc             3.0
        //  5342: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5345: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5348: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  5351: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5354: bipush          7
        //  5356: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5359: ldc             3.0
        //  5361: ldc             5.0
        //  5363: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5366: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5369: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5372: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5375: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        //  5378: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5381: iconst_5       
        //  5382: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5385: fconst_2       
        //  5386: ldc             5.0
        //  5388: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5391: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5394: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5397: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5400: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        //  5403: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5406: iconst_5       
        //  5407: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5410: ldc             3.0
        //  5412: ldc             5.0
        //  5414: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5417: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5420: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5423: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5426: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5429: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5432: fconst_1       
        //  5433: ldc             3.0
        //  5435: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5438: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5441: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //  5444: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5447: fconst_2       
        //  5448: ldc             3.0
        //  5450: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5453: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5456: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5459: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5462: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5465: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5468: fconst_2       
        //  5469: ldc             3.0
        //  5471: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5474: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5477: getstatic       net/minecraft/world/item/Items.EXPERIENCE_BOTTLE:Lnet/minecraft/world/item/Item;
        //  5480: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5483: bipush          7
        //  5485: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5488: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5491: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  5494: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5497: iconst_4       
        //  5498: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5501: fconst_1       
        //  5502: ldc_w           6.0
        //  5505: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5508: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5511: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5514: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5517: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  5520: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5523: iconst_4       
        //  5524: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5527: fconst_2       
        //  5528: ldc_w           7.0
        //  5531: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5534: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5537: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5540: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5543: getstatic       net/minecraft/world/item/Items.TRIPWIRE_HOOK:Lnet/minecraft/world/item/Item;
        //  5546: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5549: iconst_3       
        //  5550: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5553: fconst_1       
        //  5554: ldc             3.0
        //  5556: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5559: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5562: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5565: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5568: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  5571: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5574: iconst_3       
        //  5575: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5578: fconst_1       
        //  5579: ldc             3.0
        //  5581: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5584: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5587: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5590: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5593: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  5596: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5599: iconst_1       
        //  5600: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5603: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5606: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5609: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5612: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5615: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5620: aload_1         /* biConsumer */
        //  5621: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHIPWRECK_MAP:Lnet/minecraft/resources/ResourceLocation;
        //  5624: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5627: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5630: iconst_1       
        //  5631: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  5634: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5637: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  5640: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5643: invokestatic    net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction.makeExplorationMap:()Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  5646: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.BURIED_TREASURE:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
        //  5649: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setDestination:(Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  5652: getstatic       net/minecraft/world/level/saveddata/maps/MapDecoration$Type.RED_X:Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;
        //  5655: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setMapDecoration:(Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  5658: iconst_1       
        //  5659: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setZoom:(B)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  5662: iconst_0       
        //  5663: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setSkipKnownStructures:(Z)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  5666: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5669: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5672: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5675: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5678: iconst_3       
        //  5679: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  5682: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5685: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
        //  5688: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5691: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5694: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  5697: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5700: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5703: getstatic       net/minecraft/world/item/Items.CLOCK:Lnet/minecraft/world/item/Item;
        //  5706: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5709: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5712: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
        //  5715: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5718: bipush          20
        //  5720: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5723: fconst_1       
        //  5724: ldc             10.0
        //  5726: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5729: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5732: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5735: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5738: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        //  5741: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5744: bipush          10
        //  5746: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5749: fconst_1       
        //  5750: ldc             5.0
        //  5752: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5755: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5758: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5761: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5764: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  5767: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5770: iconst_5       
        //  5771: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5774: fconst_1       
        //  5775: ldc             5.0
        //  5777: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5780: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5783: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5786: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5789: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5792: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  5797: aload_1         /* biConsumer */
        //  5798: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHIPWRECK_SUPPLY:Lnet/minecraft/resources/ResourceLocation;
        //  5801: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  5804: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5807: ldc             3.0
        //  5809: ldc             10.0
        //  5811: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5814: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5817: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
        //  5820: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5823: bipush          8
        //  5825: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5828: fconst_1       
        //  5829: ldc             12.0
        //  5831: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5834: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5837: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5840: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5843: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        //  5846: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5849: bipush          7
        //  5851: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5854: fconst_2       
        //  5855: ldc_w           6.0
        //  5858: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5861: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5864: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5867: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5870: getstatic       net/minecraft/world/item/Items.POISONOUS_POTATO:Lnet/minecraft/world/item/Item;
        //  5873: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5876: bipush          7
        //  5878: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5881: fconst_2       
        //  5882: ldc_w           6.0
        //  5885: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5888: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5891: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5894: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5897: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        //  5900: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5903: bipush          7
        //  5905: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5908: ldc             4.0
        //  5910: ldc             8.0
        //  5912: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5915: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5918: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5921: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5924: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  5927: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5930: bipush          7
        //  5932: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5935: ldc             8.0
        //  5937: ldc_w           21.0
        //  5940: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5943: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  5946: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5949: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  5952: getstatic       net/minecraft/world/item/Items.SUSPICIOUS_STEW:Lnet/minecraft/world/item/Item;
        //  5955: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5958: bipush          10
        //  5960: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  5963: invokestatic    net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction.stewEffect:()Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  5966: getstatic       net/minecraft/world/effect/MobEffects.NIGHT_VISION:Lnet/minecraft/world/effect/MobEffect;
        //  5969: ldc_w           7.0
        //  5972: ldc             10.0
        //  5974: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5977: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  5980: getstatic       net/minecraft/world/effect/MobEffects.JUMP:Lnet/minecraft/world/effect/MobEffect;
        //  5983: ldc_w           7.0
        //  5986: ldc             10.0
        //  5988: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  5991: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  5994: getstatic       net/minecraft/world/effect/MobEffects.WEAKNESS:Lnet/minecraft/world/effect/MobEffect;
        //  5997: ldc_w           6.0
        //  6000: ldc             8.0
        //  6002: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6005: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  6008: getstatic       net/minecraft/world/effect/MobEffects.BLINDNESS:Lnet/minecraft/world/effect/MobEffect;
        //  6011: ldc             5.0
        //  6013: ldc_w           7.0
        //  6016: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6019: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  6022: getstatic       net/minecraft/world/effect/MobEffects.POISON:Lnet/minecraft/world/effect/MobEffect;
        //  6025: ldc             10.0
        //  6027: ldc_w           20.0
        //  6030: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6033: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  6036: getstatic       net/minecraft/world/effect/MobEffects.SATURATION:Lnet/minecraft/world/effect/MobEffect;
        //  6039: ldc_w           7.0
        //  6042: ldc             10.0
        //  6044: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6047: invokevirtual   net/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder.withEffect:(Lnet/minecraft/world/effect/MobEffect;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;)Lnet/minecraft/world/level/storage/loot/functions/SetStewEffectFunction$Builder;
        //  6050: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6053: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6056: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  6059: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6062: bipush          6
        //  6064: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6067: fconst_2       
        //  6068: ldc             8.0
        //  6070: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6073: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6076: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6079: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6082: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  6085: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6088: iconst_5       
        //  6089: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6092: ldc             5.0
        //  6094: ldc_w           24.0
        //  6097: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6100: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6103: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6106: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6109: getstatic       net/minecraft/world/level/block/Blocks.PUMPKIN:Lnet/minecraft/world/level/block/Block;
        //  6112: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6115: iconst_2       
        //  6116: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6119: fconst_1       
        //  6120: ldc             3.0
        //  6122: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6125: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6128: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6131: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6134: getstatic       net/minecraft/world/level/block/Blocks.BAMBOO:Lnet/minecraft/world/level/block/Block;
        //  6137: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6140: iconst_2       
        //  6141: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6144: fconst_1       
        //  6145: ldc             3.0
        //  6147: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6150: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6153: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6156: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6159: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  6162: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6165: iconst_3       
        //  6166: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6169: fconst_1       
        //  6170: ldc             5.0
        //  6172: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6175: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6178: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6181: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6184: getstatic       net/minecraft/world/level/block/Blocks.TNT:Lnet/minecraft/world/level/block/Block;
        //  6187: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6190: fconst_1       
        //  6191: fconst_2       
        //  6192: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6195: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6198: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6201: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6204: getstatic       net/minecraft/world/item/Items.LEATHER_HELMET:Lnet/minecraft/world/item/Item;
        //  6207: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6210: iconst_3       
        //  6211: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6214: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6217: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6220: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6223: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  6226: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6229: iconst_3       
        //  6230: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6233: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6236: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6239: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6242: getstatic       net/minecraft/world/item/Items.LEATHER_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  6245: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6248: iconst_3       
        //  6249: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6252: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6255: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6258: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6261: getstatic       net/minecraft/world/item/Items.LEATHER_BOOTS:Lnet/minecraft/world/item/Item;
        //  6264: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6267: iconst_3       
        //  6268: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6271: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6274: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6277: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6280: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6283: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  6288: aload_1         /* biConsumer */
        //  6289: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SHIPWRECK_TREASURE:Lnet/minecraft/resources/ResourceLocation;
        //  6292: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6295: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6298: ldc             3.0
        //  6300: ldc_w           6.0
        //  6303: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6306: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6309: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  6312: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6315: bipush          90
        //  6317: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6320: fconst_1       
        //  6321: ldc             5.0
        //  6323: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6326: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6329: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6332: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6335: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  6338: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6341: bipush          10
        //  6343: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6346: fconst_1       
        //  6347: ldc             5.0
        //  6349: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6352: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6355: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6358: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6361: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  6364: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6367: bipush          40
        //  6369: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6372: fconst_1       
        //  6373: ldc             5.0
        //  6375: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6378: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6381: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6384: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6387: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  6390: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6393: iconst_5       
        //  6394: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6397: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6400: getstatic       net/minecraft/world/item/Items.EXPERIENCE_BOTTLE:Lnet/minecraft/world/item/Item;
        //  6403: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6406: iconst_5       
        //  6407: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6410: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6413: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6416: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6419: fconst_2       
        //  6420: ldc             5.0
        //  6422: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6425: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6428: getstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        //  6431: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6434: bipush          50
        //  6436: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6439: fconst_1       
        //  6440: ldc             10.0
        //  6442: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6445: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6448: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6451: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6454: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  6457: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6460: bipush          10
        //  6462: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6465: fconst_1       
        //  6466: ldc             10.0
        //  6468: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6471: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6474: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6477: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6480: getstatic       net/minecraft/world/item/Items.LAPIS_LAZULI:Lnet/minecraft/world/item/Item;
        //  6483: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6486: bipush          20
        //  6488: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6491: fconst_1       
        //  6492: ldc             10.0
        //  6494: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6497: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6500: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6503: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6506: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6509: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  6514: aload_1         /* biConsumer */
        //  6515: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SIMPLE_DUNGEON:Lnet/minecraft/resources/ResourceLocation;
        //  6518: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6521: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6524: fconst_1       
        //  6525: ldc             3.0
        //  6527: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6530: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6533: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  6536: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6539: bipush          20
        //  6541: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6544: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6547: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  6550: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6553: bipush          15
        //  6555: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6558: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6561: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  6564: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6567: iconst_2       
        //  6568: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6571: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6574: getstatic       net/minecraft/world/item/Items.MUSIC_DISC_13:Lnet/minecraft/world/item/Item;
        //  6577: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6580: bipush          15
        //  6582: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6585: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6588: getstatic       net/minecraft/world/item/Items.MUSIC_DISC_CAT:Lnet/minecraft/world/item/Item;
        //  6591: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6594: bipush          15
        //  6596: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6599: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6602: getstatic       net/minecraft/world/item/Items.NAME_TAG:Lnet/minecraft/world/item/Item;
        //  6605: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6608: bipush          20
        //  6610: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6613: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6616: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  6619: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6622: bipush          10
        //  6624: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6627: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6630: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  6633: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6636: bipush          15
        //  6638: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6641: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6644: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  6647: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6650: iconst_5       
        //  6651: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6654: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6657: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  6660: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6663: bipush          10
        //  6665: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6668: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6671: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6674: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6677: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6680: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6683: fconst_1       
        //  6684: ldc             4.0
        //  6686: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6689: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6692: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  6695: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6698: bipush          10
        //  6700: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6703: fconst_1       
        //  6704: ldc             4.0
        //  6706: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6709: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6712: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6715: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6718: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  6721: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6724: iconst_5       
        //  6725: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6728: fconst_1       
        //  6729: ldc             4.0
        //  6731: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6734: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6737: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6740: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6743: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  6746: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6749: bipush          20
        //  6751: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6754: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6757: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  6760: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6763: bipush          20
        //  6765: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6768: fconst_1       
        //  6769: ldc             4.0
        //  6771: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6774: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6777: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6780: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6783: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        //  6786: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6789: bipush          10
        //  6791: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6794: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6797: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        //  6800: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6803: bipush          15
        //  6805: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6808: fconst_1       
        //  6809: ldc             4.0
        //  6811: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6814: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6817: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6820: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6823: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  6826: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6829: bipush          15
        //  6831: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6834: fconst_1       
        //  6835: ldc             4.0
        //  6837: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6840: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6843: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6846: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6849: getstatic       net/minecraft/world/item/Items.MELON_SEEDS:Lnet/minecraft/world/item/Item;
        //  6852: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6855: bipush          10
        //  6857: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6860: fconst_2       
        //  6861: ldc             4.0
        //  6863: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6866: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6869: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6872: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6875: getstatic       net/minecraft/world/item/Items.PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;
        //  6878: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6881: bipush          10
        //  6883: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6886: fconst_2       
        //  6887: ldc             4.0
        //  6889: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6892: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6895: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6898: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6901: getstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        //  6904: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6907: bipush          10
        //  6909: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6912: fconst_2       
        //  6913: ldc             4.0
        //  6915: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6918: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6921: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6924: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6927: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  6930: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6933: iconst_3       
        //  6934: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  6937: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6940: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        //  6943: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6946: bipush          10
        //  6948: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6951: fconst_1       
        //  6952: ldc             8.0
        //  6954: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6957: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6960: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6963: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6966: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  6969: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6972: bipush          10
        //  6974: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6977: fconst_1       
        //  6978: ldc             8.0
        //  6980: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  6983: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  6986: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6989: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  6992: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  6995: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  6998: bipush          10
        //  7000: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7003: fconst_1       
        //  7004: ldc             8.0
        //  7006: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7009: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7012: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7015: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7018: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  7021: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7024: bipush          10
        //  7026: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7029: fconst_1       
        //  7030: ldc             8.0
        //  7032: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7035: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7038: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7041: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7044: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7047: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  7052: aload_1         /* biConsumer */
        //  7053: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.SPAWN_BONUS_CHEST:Lnet/minecraft/resources/ResourceLocation;
        //  7056: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7059: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7062: iconst_1       
        //  7063: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7066: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7069: getstatic       net/minecraft/world/item/Items.STONE_AXE:Lnet/minecraft/world/item/Item;
        //  7072: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7075: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7078: getstatic       net/minecraft/world/item/Items.WOODEN_AXE:Lnet/minecraft/world/item/Item;
        //  7081: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7084: iconst_3       
        //  7085: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7088: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7091: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7094: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7097: iconst_1       
        //  7098: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7101: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7104: getstatic       net/minecraft/world/item/Items.STONE_PICKAXE:Lnet/minecraft/world/item/Item;
        //  7107: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7110: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7113: getstatic       net/minecraft/world/item/Items.WOODEN_PICKAXE:Lnet/minecraft/world/item/Item;
        //  7116: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7119: iconst_3       
        //  7120: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7123: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7126: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7129: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7132: iconst_3       
        //  7133: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7136: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7139: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  7142: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7145: iconst_5       
        //  7146: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7149: fconst_1       
        //  7150: fconst_2       
        //  7151: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7154: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7157: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7160: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7163: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  7166: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7169: iconst_3       
        //  7170: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7173: fconst_1       
        //  7174: fconst_2       
        //  7175: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7178: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7181: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7184: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7187: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
        //  7190: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7193: iconst_3       
        //  7194: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7197: fconst_1       
        //  7198: fconst_2       
        //  7199: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7202: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7205: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7208: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7211: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7214: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7217: iconst_4       
        //  7218: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7221: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7224: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  7227: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7230: bipush          10
        //  7232: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7235: fconst_1       
        //  7236: ldc             12.0
        //  7238: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7241: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7244: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7247: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7250: getstatic       net/minecraft/world/level/block/Blocks.OAK_PLANKS:Lnet/minecraft/world/level/block/Block;
        //  7253: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7256: bipush          10
        //  7258: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7261: fconst_1       
        //  7262: ldc             12.0
        //  7264: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7267: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7270: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7273: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7276: getstatic       net/minecraft/world/level/block/Blocks.OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //  7279: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7282: iconst_3       
        //  7283: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7286: fconst_1       
        //  7287: ldc             3.0
        //  7289: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7292: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7295: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7298: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7301: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_LOG:Lnet/minecraft/world/level/block/Block;
        //  7304: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7307: iconst_3       
        //  7308: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7311: fconst_1       
        //  7312: ldc             3.0
        //  7314: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7317: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7320: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7323: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7326: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_LOG:Lnet/minecraft/world/level/block/Block;
        //  7329: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7332: iconst_3       
        //  7333: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7336: fconst_1       
        //  7337: ldc             3.0
        //  7339: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7342: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7345: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7348: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7351: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_LOG:Lnet/minecraft/world/level/block/Block;
        //  7354: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7357: iconst_3       
        //  7358: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7361: fconst_1       
        //  7362: ldc             3.0
        //  7364: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7367: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7370: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7373: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7376: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_LOG:Lnet/minecraft/world/level/block/Block;
        //  7379: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7382: iconst_3       
        //  7383: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7386: fconst_1       
        //  7387: ldc             3.0
        //  7389: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7392: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7395: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7398: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7401: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //  7404: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7407: iconst_3       
        //  7408: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7411: fconst_1       
        //  7412: ldc             3.0
        //  7414: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7417: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7420: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7423: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7426: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7429: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  7434: aload_1         /* biConsumer */
        //  7435: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.STRONGHOLD_CORRIDOR:Lnet/minecraft/resources/ResourceLocation;
        //  7438: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7441: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7444: fconst_2       
        //  7445: ldc             3.0
        //  7447: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7450: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7453: getstatic       net/minecraft/world/item/Items.ENDER_PEARL:Lnet/minecraft/world/item/Item;
        //  7456: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7459: bipush          10
        //  7461: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7464: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7467: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  7470: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7473: iconst_3       
        //  7474: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7477: fconst_1       
        //  7478: ldc             3.0
        //  7480: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7483: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7486: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7489: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7492: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  7495: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7498: bipush          10
        //  7500: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7503: fconst_1       
        //  7504: ldc             5.0
        //  7506: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7509: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7512: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7515: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7518: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  7521: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7524: iconst_5       
        //  7525: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7528: fconst_1       
        //  7529: ldc             3.0
        //  7531: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7534: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7537: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7540: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7543: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        //  7546: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7549: iconst_5       
        //  7550: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7553: ldc             4.0
        //  7555: ldc             9.0
        //  7557: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7560: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7563: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7566: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7569: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  7572: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7575: bipush          15
        //  7577: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7580: fconst_1       
        //  7581: ldc             3.0
        //  7583: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7586: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7589: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7592: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7595: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  7598: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7601: bipush          15
        //  7603: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7606: fconst_1       
        //  7607: ldc             3.0
        //  7609: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7612: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7615: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7618: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7621: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  7624: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7627: iconst_5       
        //  7628: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7631: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7634: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  7637: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7640: iconst_5       
        //  7641: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7644: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7647: getstatic       net/minecraft/world/item/Items.IRON_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  7650: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7653: iconst_5       
        //  7654: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7657: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7660: getstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
        //  7663: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7666: iconst_5       
        //  7667: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7670: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7673: getstatic       net/minecraft/world/item/Items.IRON_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  7676: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7679: iconst_5       
        //  7680: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7683: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7686: getstatic       net/minecraft/world/item/Items.IRON_BOOTS:Lnet/minecraft/world/item/Item;
        //  7689: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7692: iconst_5       
        //  7693: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7696: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7699: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  7702: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7705: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7708: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  7711: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7714: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7717: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  7720: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7723: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7726: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  7729: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7732: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7735: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  7738: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7741: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7744: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  7747: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7750: bipush          30
        //  7752: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7755: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  7758: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  7761: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7764: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7767: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7770: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  7775: aload_1         /* biConsumer */
        //  7776: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.STRONGHOLD_CROSSING:Lnet/minecraft/resources/ResourceLocation;
        //  7779: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7782: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7785: fconst_1       
        //  7786: ldc             4.0
        //  7788: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7791: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7794: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  7797: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7800: bipush          10
        //  7802: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7805: fconst_1       
        //  7806: ldc             5.0
        //  7808: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7811: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7814: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7817: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7820: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  7823: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7826: iconst_5       
        //  7827: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7830: fconst_1       
        //  7831: ldc             3.0
        //  7833: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7836: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7839: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7842: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7845: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        //  7848: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7851: iconst_5       
        //  7852: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7855: ldc             4.0
        //  7857: ldc             9.0
        //  7859: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7862: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7865: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7868: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7871: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  7874: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7877: bipush          10
        //  7879: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7882: ldc             3.0
        //  7884: ldc             8.0
        //  7886: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7889: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7892: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7895: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7898: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  7901: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7904: bipush          15
        //  7906: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7909: fconst_1       
        //  7910: ldc             3.0
        //  7912: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7915: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7918: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7921: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7924: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  7927: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7930: bipush          15
        //  7932: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7935: fconst_1       
        //  7936: ldc             3.0
        //  7938: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  7941: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  7944: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7947: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7950: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  7953: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7956: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7959: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  7962: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7965: bipush          30
        //  7967: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  7970: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  7973: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  7976: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  7979: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  7982: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7985: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  7990: aload_1         /* biConsumer */
        //  7991: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.STRONGHOLD_LIBRARY:Lnet/minecraft/resources/ResourceLocation;
        //  7994: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  7997: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8000: fconst_2       
        //  8001: ldc             10.0
        //  8003: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8006: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8009: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  8012: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8015: bipush          20
        //  8017: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8020: fconst_1       
        //  8021: ldc             3.0
        //  8023: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8026: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8029: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8032: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8035: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
        //  8038: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8041: bipush          20
        //  8043: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8046: fconst_2       
        //  8047: ldc_w           7.0
        //  8050: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8053: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8056: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8059: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8062: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  8065: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8068: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8071: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
        //  8074: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8077: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8080: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  8083: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8086: bipush          10
        //  8088: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8091: bipush          30
        //  8093: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  8096: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction.enchantWithLevels:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  8099: invokevirtual   net/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder.allowTreasure:()Lnet/minecraft/world/level/storage/loot/functions/EnchantWithLevelsFunction$Builder;
        //  8102: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8105: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8108: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8111: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  8116: aload_1         /* biConsumer */
        //  8117: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.UNDERWATER_RUIN_BIG:Lnet/minecraft/resources/ResourceLocation;
        //  8120: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8123: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8126: fconst_2       
        //  8127: ldc             8.0
        //  8129: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8132: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8135: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  8138: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8141: bipush          10
        //  8143: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8146: fconst_1       
        //  8147: ldc             4.0
        //  8149: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8152: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8155: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8158: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8161: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        //  8164: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8167: bipush          10
        //  8169: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8172: fconst_1       
        //  8173: ldc             3.0
        //  8175: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8178: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8181: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8184: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8187: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  8190: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8193: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8196: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  8199: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8202: bipush          10
        //  8204: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8207: fconst_2       
        //  8208: ldc             3.0
        //  8210: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8213: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8216: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8219: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8222: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8225: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8228: iconst_1       
        //  8229: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  8232: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8235: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        //  8238: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8241: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8244: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        //  8247: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8250: iconst_5       
        //  8251: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8254: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8257: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8260: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8263: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  8266: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8269: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8272: getstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        //  8275: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8278: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8281: getstatic       net/minecraft/world/item/Items.FISHING_ROD:Lnet/minecraft/world/item/Item;
        //  8284: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8287: iconst_5       
        //  8288: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8291: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8294: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8297: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8300: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  8303: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8306: bipush          10
        //  8308: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8311: invokestatic    net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction.makeExplorationMap:()Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8314: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.BURIED_TREASURE:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
        //  8317: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setDestination:(Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8320: getstatic       net/minecraft/world/level/saveddata/maps/MapDecoration$Type.RED_X:Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;
        //  8323: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setMapDecoration:(Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8326: iconst_1       
        //  8327: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setZoom:(B)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8330: iconst_0       
        //  8331: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setSkipKnownStructures:(Z)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8334: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8337: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8340: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8343: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  8348: aload_1         /* biConsumer */
        //  8349: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.UNDERWATER_RUIN_SMALL:Lnet/minecraft/resources/ResourceLocation;
        //  8352: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8355: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8358: fconst_2       
        //  8359: ldc             8.0
        //  8361: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8364: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8367: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  8370: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8373: bipush          10
        //  8375: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8378: fconst_1       
        //  8379: ldc             4.0
        //  8381: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8384: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8387: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8390: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8393: getstatic       net/minecraft/world/item/Items.STONE_AXE:Lnet/minecraft/world/item/Item;
        //  8396: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8399: iconst_2       
        //  8400: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8403: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8406: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        //  8409: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8412: iconst_5       
        //  8413: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8416: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8419: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  8422: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8425: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8428: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  8431: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8434: bipush          10
        //  8436: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8439: fconst_2       
        //  8440: ldc             3.0
        //  8442: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8445: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8448: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8451: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8454: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8457: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8460: iconst_1       
        //  8461: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        //  8464: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8467: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  8470: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8473: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8476: getstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        //  8479: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8482: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8485: getstatic       net/minecraft/world/item/Items.FISHING_ROD:Lnet/minecraft/world/item/Item;
        //  8488: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8491: iconst_5       
        //  8492: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8495: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8498: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8501: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8504: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  8507: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8510: iconst_5       
        //  8511: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8514: invokestatic    net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction.makeExplorationMap:()Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8517: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.BURIED_TREASURE:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
        //  8520: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setDestination:(Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8523: getstatic       net/minecraft/world/level/saveddata/maps/MapDecoration$Type.RED_X:Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;
        //  8526: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setMapDecoration:(Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8529: iconst_1       
        //  8530: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setZoom:(B)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8533: iconst_0       
        //  8534: invokevirtual   net/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder.setSkipKnownStructures:(Z)Lnet/minecraft/world/level/storage/loot/functions/ExplorationMapFunction$Builder;
        //  8537: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8540: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8543: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8546: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  8551: aload_1         /* biConsumer */
        //  8552: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_WEAPONSMITH:Lnet/minecraft/resources/ResourceLocation;
        //  8555: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8558: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8561: ldc             3.0
        //  8563: ldc             8.0
        //  8565: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8568: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8571: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  8574: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8577: iconst_3       
        //  8578: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8581: fconst_1       
        //  8582: ldc             3.0
        //  8584: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8587: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8590: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8593: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8596: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  8599: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8602: bipush          10
        //  8604: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8607: fconst_1       
        //  8608: ldc             5.0
        //  8610: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8613: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8616: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8619: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8622: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  8625: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8628: iconst_5       
        //  8629: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8632: fconst_1       
        //  8633: ldc             3.0
        //  8635: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8638: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8641: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8644: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8647: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  8650: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8653: bipush          15
        //  8655: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8658: fconst_1       
        //  8659: ldc             3.0
        //  8661: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8664: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8667: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8670: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8673: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  8676: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8679: bipush          15
        //  8681: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8684: fconst_1       
        //  8685: ldc             3.0
        //  8687: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8690: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8693: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8696: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8699: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  8702: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8705: iconst_5       
        //  8706: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8709: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8712: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  8715: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8718: iconst_5       
        //  8719: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8722: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8725: getstatic       net/minecraft/world/item/Items.IRON_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  8728: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8731: iconst_5       
        //  8732: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8735: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8738: getstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
        //  8741: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8744: iconst_5       
        //  8745: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8748: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8751: getstatic       net/minecraft/world/item/Items.IRON_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  8754: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8757: iconst_5       
        //  8758: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8761: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8764: getstatic       net/minecraft/world/item/Items.IRON_BOOTS:Lnet/minecraft/world/item/Item;
        //  8767: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8770: iconst_5       
        //  8771: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8774: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8777: getstatic       net/minecraft/world/level/block/Blocks.OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  8780: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8783: iconst_5       
        //  8784: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8787: ldc             3.0
        //  8789: ldc_w           7.0
        //  8792: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8795: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8798: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8801: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8804: getstatic       net/minecraft/world/level/block/Blocks.OAK_SAPLING:Lnet/minecraft/world/level/block/Block;
        //  8807: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8810: iconst_5       
        //  8811: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8814: ldc             3.0
        //  8816: ldc_w           7.0
        //  8819: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8822: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8825: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8828: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8831: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        //  8834: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8837: iconst_3       
        //  8838: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8841: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8844: getstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  8847: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8850: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8853: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  8856: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8859: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8862: getstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        //  8865: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8868: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8871: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8874: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  8879: aload_1         /* biConsumer */
        //  8880: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_TOOLSMITH:Lnet/minecraft/resources/ResourceLocation;
        //  8883: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  8886: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8889: ldc             3.0
        //  8891: ldc             8.0
        //  8893: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8896: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8899: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  8902: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8905: iconst_1       
        //  8906: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8909: fconst_1       
        //  8910: ldc             3.0
        //  8912: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8915: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8918: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8921: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8924: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  8927: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8930: iconst_5       
        //  8931: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8934: fconst_1       
        //  8935: ldc             5.0
        //  8937: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8940: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8943: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8946: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8949: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  8952: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8955: iconst_1       
        //  8956: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8959: fconst_1       
        //  8960: ldc             3.0
        //  8962: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8965: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8968: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8971: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  8974: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  8977: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8980: bipush          15
        //  8982: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8985: fconst_1       
        //  8986: ldc             3.0
        //  8988: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  8991: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  8994: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  8997: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9000: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  9003: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9006: iconst_5       
        //  9007: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9010: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9013: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  9016: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9019: iconst_1       
        //  9020: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9023: fconst_1       
        //  9024: ldc             3.0
        //  9026: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9029: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9032: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9035: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9038: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  9041: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9044: bipush          20
        //  9046: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9049: fconst_1       
        //  9050: ldc             3.0
        //  9052: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9055: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9058: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9061: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9064: getstatic       net/minecraft/world/item/Items.IRON_SHOVEL:Lnet/minecraft/world/item/Item;
        //  9067: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9070: iconst_5       
        //  9071: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9074: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9077: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9080: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9085: aload_1         /* biConsumer */
        //  9086: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_CARTOGRAPHER:Lnet/minecraft/resources/ResourceLocation;
        //  9089: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9092: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9095: fconst_1       
        //  9096: ldc             5.0
        //  9098: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9101: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9104: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //  9107: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9110: bipush          10
        //  9112: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9115: fconst_1       
        //  9116: ldc             3.0
        //  9118: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9121: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9124: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9127: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9130: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
        //  9133: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9136: bipush          15
        //  9138: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9141: fconst_1       
        //  9142: ldc             5.0
        //  9144: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9147: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9150: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9153: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9156: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
        //  9159: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9162: iconst_5       
        //  9163: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9166: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9169: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  9172: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9175: bipush          15
        //  9177: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9180: fconst_1       
        //  9181: ldc             4.0
        //  9183: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9186: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9189: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9192: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9195: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  9198: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9201: iconst_5       
        //  9202: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9205: fconst_1       
        //  9206: fconst_2       
        //  9207: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9210: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9213: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9216: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9219: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9222: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9227: aload_1         /* biConsumer */
        //  9228: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_MASON:Lnet/minecraft/resources/ResourceLocation;
        //  9231: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9234: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9237: fconst_1       
        //  9238: ldc             5.0
        //  9240: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9243: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9246: getstatic       net/minecraft/world/item/Items.CLAY_BALL:Lnet/minecraft/world/item/Item;
        //  9249: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9252: iconst_1       
        //  9253: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9256: fconst_1       
        //  9257: ldc             3.0
        //  9259: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9262: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9265: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9268: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9271: getstatic       net/minecraft/world/item/Items.FLOWER_POT:Lnet/minecraft/world/item/Item;
        //  9274: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9277: iconst_1       
        //  9278: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9281: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9284: getstatic       net/minecraft/world/level/block/Blocks.STONE:Lnet/minecraft/world/level/block/Block;
        //  9287: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9290: iconst_2       
        //  9291: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9294: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9297: getstatic       net/minecraft/world/level/block/Blocks.STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  9300: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9303: iconst_2       
        //  9304: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9307: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9310: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  9313: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9316: iconst_4       
        //  9317: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9320: fconst_1       
        //  9321: ldc             4.0
        //  9323: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9326: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9329: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9332: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9335: getstatic       net/minecraft/world/item/Items.YELLOW_DYE:Lnet/minecraft/world/item/Item;
        //  9338: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9341: iconst_1       
        //  9342: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9345: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9348: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_STONE:Lnet/minecraft/world/level/block/Block;
        //  9351: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9354: iconst_1       
        //  9355: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9358: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9361: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  9364: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9367: iconst_1       
        //  9368: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9371: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9374: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9377: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9382: aload_1         /* biConsumer */
        //  9383: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_ARMORER:Lnet/minecraft/resources/ResourceLocation;
        //  9386: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9389: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9392: fconst_1       
        //  9393: ldc             5.0
        //  9395: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9398: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9401: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  9404: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9407: iconst_2       
        //  9408: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9411: fconst_1       
        //  9412: ldc             3.0
        //  9414: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9417: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9420: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9423: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9426: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  9429: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9432: iconst_4       
        //  9433: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9436: fconst_1       
        //  9437: ldc             4.0
        //  9439: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9442: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9445: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9448: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9451: getstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
        //  9454: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9457: iconst_1       
        //  9458: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9461: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9464: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  9467: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9470: iconst_1       
        //  9471: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9474: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9477: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9480: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9485: aload_1         /* biConsumer */
        //  9486: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_SHEPHERD:Lnet/minecraft/resources/ResourceLocation;
        //  9489: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9492: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9495: fconst_1       
        //  9496: ldc             5.0
        //  9498: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9501: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9504: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  9507: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9510: bipush          6
        //  9512: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9515: fconst_1       
        //  9516: ldc             8.0
        //  9518: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9521: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9524: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9527: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9530: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WOOL:Lnet/minecraft/world/level/block/Block;
        //  9533: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9536: iconst_3       
        //  9537: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9540: fconst_1       
        //  9541: ldc             3.0
        //  9543: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9546: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9549: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9552: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9555: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  9558: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9561: iconst_2       
        //  9562: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9565: fconst_1       
        //  9566: ldc             3.0
        //  9568: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9571: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9574: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9577: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9580: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  9583: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9586: iconst_2       
        //  9587: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9590: fconst_1       
        //  9591: ldc             3.0
        //  9593: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9596: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9599: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9602: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9605: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  9608: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9611: iconst_2       
        //  9612: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9615: fconst_1       
        //  9616: ldc             3.0
        //  9618: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9621: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9624: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9627: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9630: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  9633: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9636: iconst_1       
        //  9637: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9640: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9643: getstatic       net/minecraft/world/item/Items.SHEARS:Lnet/minecraft/world/item/Item;
        //  9646: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9649: iconst_1       
        //  9650: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9653: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9656: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  9659: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9662: bipush          6
        //  9664: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9667: fconst_1       
        //  9668: ldc_w           6.0
        //  9671: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9674: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9677: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9680: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9683: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9686: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9691: aload_1         /* biConsumer */
        //  9692: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_BUTCHER:Lnet/minecraft/resources/ResourceLocation;
        //  9695: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9698: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9701: fconst_1       
        //  9702: ldc             5.0
        //  9704: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9707: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9710: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  9713: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9716: iconst_1       
        //  9717: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9720: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9723: getstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
        //  9726: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9729: bipush          6
        //  9731: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9734: fconst_1       
        //  9735: ldc             3.0
        //  9737: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9740: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9743: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9746: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9749: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  9752: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9755: bipush          6
        //  9757: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9760: fconst_1       
        //  9761: ldc             3.0
        //  9763: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9766: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9769: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9772: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9775: getstatic       net/minecraft/world/item/Items.BEEF:Lnet/minecraft/world/item/Item;
        //  9778: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9781: bipush          6
        //  9783: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9786: fconst_1       
        //  9787: ldc             3.0
        //  9789: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9792: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9795: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9798: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9801: getstatic       net/minecraft/world/item/Items.MUTTON:Lnet/minecraft/world/item/Item;
        //  9804: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9807: bipush          6
        //  9809: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9812: fconst_1       
        //  9813: ldc             3.0
        //  9815: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9818: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9821: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9824: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9827: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  9830: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9833: iconst_3       
        //  9834: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9837: fconst_1       
        //  9838: ldc             3.0
        //  9840: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9843: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9846: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9849: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9852: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9855: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        //  9860: aload_1         /* biConsumer */
        //  9861: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_FLETCHER:Lnet/minecraft/resources/ResourceLocation;
        //  9864: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        //  9867: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9870: fconst_1       
        //  9871: ldc             5.0
        //  9873: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9876: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9879: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        //  9882: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9885: iconst_1       
        //  9886: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9889: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9892: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  9895: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9898: iconst_2       
        //  9899: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9902: fconst_1       
        //  9903: ldc             3.0
        //  9905: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9908: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9911: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9914: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9917: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        //  9920: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9923: bipush          6
        //  9925: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9928: fconst_1       
        //  9929: ldc             3.0
        //  9931: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9934: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9937: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9940: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9943: getstatic       net/minecraft/world/item/Items.EGG:Lnet/minecraft/world/item/Item;
        //  9946: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9949: iconst_2       
        //  9950: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9953: fconst_1       
        //  9954: ldc             3.0
        //  9956: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9959: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9962: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9965: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9968: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
        //  9971: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9974: bipush          6
        //  9976: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9979: fconst_1       
        //  9980: ldc             3.0
        //  9982: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        //  9985: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        //  9988: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        //  9991: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        //  9994: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  9997: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10000: bipush          6
        // 10002: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10005: fconst_1       
        // 10006: ldc             3.0
        // 10008: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10011: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10014: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10017: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10020: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10023: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 10028: aload_1         /* biConsumer */
        // 10029: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_FISHER:Lnet/minecraft/resources/ResourceLocation;
        // 10032: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10035: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10038: fconst_1       
        // 10039: ldc             5.0
        // 10041: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10044: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10047: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 10050: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10053: iconst_1       
        // 10054: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10057: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10060: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        // 10063: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10066: iconst_2       
        // 10067: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10070: fconst_1       
        // 10071: ldc             3.0
        // 10073: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10076: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10079: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10082: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10085: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
        // 10088: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10091: iconst_1       
        // 10092: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10095: fconst_1       
        // 10096: ldc             3.0
        // 10098: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10101: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10104: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10107: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10110: getstatic       net/minecraft/world/item/Items.WATER_BUCKET:Lnet/minecraft/world/item/Item;
        // 10113: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10116: iconst_1       
        // 10117: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10120: fconst_1       
        // 10121: ldc             3.0
        // 10123: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10126: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10129: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10132: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10135: getstatic       net/minecraft/world/item/Items.BARREL:Lnet/minecraft/world/item/Item;
        // 10138: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10141: iconst_1       
        // 10142: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10145: fconst_1       
        // 10146: ldc             3.0
        // 10148: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10151: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10154: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10157: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10160: getstatic       net/minecraft/world/item/Items.WHEAT_SEEDS:Lnet/minecraft/world/item/Item;
        // 10163: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10166: iconst_3       
        // 10167: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10170: fconst_1       
        // 10171: ldc             3.0
        // 10173: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10176: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10179: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10182: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10185: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        // 10188: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10191: iconst_2       
        // 10192: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10195: fconst_1       
        // 10196: ldc             3.0
        // 10198: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10201: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10204: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10207: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10210: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10213: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 10218: aload_1         /* biConsumer */
        // 10219: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_TANNERY:Lnet/minecraft/resources/ResourceLocation;
        // 10222: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10225: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10228: fconst_1       
        // 10229: ldc             5.0
        // 10231: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10234: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10237: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        // 10240: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10243: iconst_1       
        // 10244: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10247: fconst_1       
        // 10248: ldc             3.0
        // 10250: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10253: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10256: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10259: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10262: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 10265: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10268: iconst_2       
        // 10269: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10272: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10275: getstatic       net/minecraft/world/item/Items.LEATHER_BOOTS:Lnet/minecraft/world/item/Item;
        // 10278: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10281: iconst_2       
        // 10282: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10285: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10288: getstatic       net/minecraft/world/item/Items.LEATHER_HELMET:Lnet/minecraft/world/item/Item;
        // 10291: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10294: iconst_2       
        // 10295: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10298: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10301: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 10304: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10307: iconst_5       
        // 10308: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10311: fconst_1       
        // 10312: ldc             4.0
        // 10314: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10317: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10320: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10323: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10326: getstatic       net/minecraft/world/item/Items.LEATHER_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 10329: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10332: iconst_2       
        // 10333: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10336: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10339: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        // 10342: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10345: iconst_1       
        // 10346: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10349: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10352: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 10355: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10358: iconst_1       
        // 10359: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10362: fconst_1       
        // 10363: ldc             4.0
        // 10365: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10368: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10371: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10374: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10377: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10380: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 10385: aload_1         /* biConsumer */
        // 10386: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_TEMPLE:Lnet/minecraft/resources/ResourceLocation;
        // 10389: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10392: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10395: ldc             3.0
        // 10397: ldc             8.0
        // 10399: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10402: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10405: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        // 10408: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10411: iconst_2       
        // 10412: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10415: fconst_1       
        // 10416: ldc             4.0
        // 10418: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10421: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10424: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10427: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10430: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 10433: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10436: bipush          7
        // 10438: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10441: fconst_1       
        // 10442: ldc             4.0
        // 10444: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10447: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10450: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10453: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10456: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        // 10459: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10462: bipush          7
        // 10464: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10467: fconst_1       
        // 10468: ldc             4.0
        // 10470: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10473: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10476: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10479: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10482: getstatic       net/minecraft/world/item/Items.LAPIS_LAZULI:Lnet/minecraft/world/item/Item;
        // 10485: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10488: iconst_1       
        // 10489: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10492: fconst_1       
        // 10493: ldc             4.0
        // 10495: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10498: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10501: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10504: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10507: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        // 10510: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10513: iconst_1       
        // 10514: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10517: fconst_1       
        // 10518: ldc             4.0
        // 10520: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10523: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10526: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10529: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10532: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 10535: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10538: iconst_1       
        // 10539: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10542: fconst_1       
        // 10543: ldc             4.0
        // 10545: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10548: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10551: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10554: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10557: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10560: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 10565: aload_1         /* biConsumer */
        // 10566: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_PLAINS_HOUSE:Lnet/minecraft/resources/ResourceLocation;
        // 10569: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10572: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10575: ldc             3.0
        // 10577: ldc             8.0
        // 10579: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10582: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10585: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        // 10588: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10591: iconst_1       
        // 10592: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10595: fconst_1       
        // 10596: ldc             3.0
        // 10598: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10601: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10604: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10607: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10610: getstatic       net/minecraft/world/item/Items.DANDELION:Lnet/minecraft/world/item/Item;
        // 10613: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10616: iconst_2       
        // 10617: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10620: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10623: getstatic       net/minecraft/world/item/Items.POPPY:Lnet/minecraft/world/item/Item;
        // 10626: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10629: iconst_1       
        // 10630: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10633: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10636: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        // 10639: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10642: bipush          10
        // 10644: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10647: fconst_1       
        // 10648: ldc_w           7.0
        // 10651: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10654: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10657: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10660: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10663: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 10666: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10669: bipush          10
        // 10671: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10674: fconst_1       
        // 10675: ldc             4.0
        // 10677: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10680: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10683: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10686: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10689: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        // 10692: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10695: bipush          10
        // 10697: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10700: fconst_1       
        // 10701: ldc             5.0
        // 10703: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10706: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10709: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10712: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10715: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        // 10718: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10721: iconst_1       
        // 10722: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10725: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10728: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        // 10731: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10734: iconst_1       
        // 10735: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10738: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10741: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 10744: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10747: iconst_2       
        // 10748: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10751: fconst_1       
        // 10752: ldc             4.0
        // 10754: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10757: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10760: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10763: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10766: getstatic       net/minecraft/world/level/block/Blocks.OAK_SAPLING:Lnet/minecraft/world/level/block/Block;
        // 10769: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10772: iconst_5       
        // 10773: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10776: fconst_1       
        // 10777: fconst_2       
        // 10778: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10781: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10784: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10787: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10790: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10793: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 10798: aload_1         /* biConsumer */
        // 10799: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_TAIGA_HOUSE:Lnet/minecraft/resources/ResourceLocation;
        // 10802: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 10805: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10808: ldc             3.0
        // 10810: ldc             8.0
        // 10812: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10815: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10818: getstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        // 10821: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10824: iconst_1       
        // 10825: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10828: fconst_1       
        // 10829: ldc             5.0
        // 10831: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10834: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10837: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10840: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10843: getstatic       net/minecraft/world/item/Items.FERN:Lnet/minecraft/world/item/Item;
        // 10846: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10849: iconst_2       
        // 10850: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10853: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10856: getstatic       net/minecraft/world/item/Items.LARGE_FERN:Lnet/minecraft/world/item/Item;
        // 10859: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10862: iconst_2       
        // 10863: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10866: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10869: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        // 10872: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10875: bipush          10
        // 10877: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10880: fconst_1       
        // 10881: ldc_w           7.0
        // 10884: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10887: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10890: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10893: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10896: getstatic       net/minecraft/world/item/Items.SWEET_BERRIES:Lnet/minecraft/world/item/Item;
        // 10899: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10902: iconst_5       
        // 10903: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10906: fconst_1       
        // 10907: ldc_w           7.0
        // 10910: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10913: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10916: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10919: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10922: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 10925: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10928: bipush          10
        // 10930: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10933: fconst_1       
        // 10934: ldc             4.0
        // 10936: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10939: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10942: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10945: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10948: getstatic       net/minecraft/world/item/Items.PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;
        // 10951: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10954: iconst_5       
        // 10955: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10958: fconst_1       
        // 10959: ldc             5.0
        // 10961: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 10964: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 10967: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10970: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10973: getstatic       net/minecraft/world/item/Items.PUMPKIN_PIE:Lnet/minecraft/world/item/Item;
        // 10976: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10979: iconst_1       
        // 10980: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10983: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 10986: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 10989: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10992: iconst_2       
        // 10993: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 10996: fconst_1       
        // 10997: ldc             4.0
        // 10999: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11002: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11005: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11008: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11011: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_SAPLING:Lnet/minecraft/world/level/block/Block;
        // 11014: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11017: iconst_5       
        // 11018: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11021: fconst_1       
        // 11022: ldc             5.0
        // 11024: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11027: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11030: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11033: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11036: getstatic       net/minecraft/world/item/Items.SPRUCE_SIGN:Lnet/minecraft/world/item/Item;
        // 11039: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11042: iconst_1       
        // 11043: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11046: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11049: getstatic       net/minecraft/world/item/Items.SPRUCE_LOG:Lnet/minecraft/world/item/Item;
        // 11052: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11055: bipush          10
        // 11057: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11060: fconst_1       
        // 11061: ldc             5.0
        // 11063: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11066: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11069: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11072: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11075: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11078: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 11083: aload_1         /* biConsumer */
        // 11084: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_SAVANNA_HOUSE:Lnet/minecraft/resources/ResourceLocation;
        // 11087: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11090: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11093: ldc             3.0
        // 11095: ldc             8.0
        // 11097: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11100: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11103: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        // 11106: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11109: iconst_1       
        // 11110: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11113: fconst_1       
        // 11114: ldc             3.0
        // 11116: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11119: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11122: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11125: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11128: getstatic       net/minecraft/world/item/Items.GRASS:Lnet/minecraft/world/item/Item;
        // 11131: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11134: iconst_5       
        // 11135: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11138: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11141: getstatic       net/minecraft/world/item/Items.TALL_GRASS:Lnet/minecraft/world/item/Item;
        // 11144: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11147: iconst_5       
        // 11148: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11151: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11154: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 11157: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11160: bipush          10
        // 11162: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11165: fconst_1       
        // 11166: ldc             4.0
        // 11168: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11171: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11174: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11177: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11180: getstatic       net/minecraft/world/item/Items.WHEAT_SEEDS:Lnet/minecraft/world/item/Item;
        // 11183: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11186: bipush          10
        // 11188: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11191: fconst_1       
        // 11192: ldc             5.0
        // 11194: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11197: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11200: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11203: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11206: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 11209: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11212: iconst_2       
        // 11213: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11216: fconst_1       
        // 11217: ldc             4.0
        // 11219: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11222: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11225: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11228: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11231: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_SAPLING:Lnet/minecraft/world/level/block/Block;
        // 11234: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11237: bipush          10
        // 11239: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11242: fconst_1       
        // 11243: fconst_2       
        // 11244: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11247: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11250: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11253: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11256: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        // 11259: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11262: iconst_1       
        // 11263: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11266: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11269: getstatic       net/minecraft/world/level/block/Blocks.TORCH:Lnet/minecraft/world/level/block/Block;
        // 11272: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11275: iconst_1       
        // 11276: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11279: fconst_1       
        // 11280: fconst_2       
        // 11281: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11284: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11287: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11290: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11293: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 11296: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11299: iconst_1       
        // 11300: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11303: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11306: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11309: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 11314: aload_1         /* biConsumer */
        // 11315: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_SNOWY_HOUSE:Lnet/minecraft/resources/ResourceLocation;
        // 11318: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11321: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11324: ldc             3.0
        // 11326: ldc             8.0
        // 11328: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11331: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11334: getstatic       net/minecraft/world/level/block/Blocks.BLUE_ICE:Lnet/minecraft/world/level/block/Block;
        // 11337: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11340: iconst_1       
        // 11341: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11344: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11347: getstatic       net/minecraft/world/level/block/Blocks.SNOW_BLOCK:Lnet/minecraft/world/level/block/Block;
        // 11350: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11353: iconst_4       
        // 11354: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11357: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11360: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        // 11363: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11366: bipush          10
        // 11368: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11371: fconst_1       
        // 11372: ldc_w           7.0
        // 11375: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11378: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11381: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11384: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11387: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 11390: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11393: bipush          10
        // 11395: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11398: fconst_1       
        // 11399: ldc             4.0
        // 11401: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11404: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11407: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11410: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11413: getstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        // 11416: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11419: bipush          10
        // 11421: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11424: fconst_1       
        // 11425: ldc             5.0
        // 11427: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11430: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11433: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11436: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11439: getstatic       net/minecraft/world/item/Items.BEETROOT_SOUP:Lnet/minecraft/world/item/Item;
        // 11442: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11445: iconst_1       
        // 11446: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11449: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11452: getstatic       net/minecraft/world/item/Items.FURNACE:Lnet/minecraft/world/item/Item;
        // 11455: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11458: iconst_1       
        // 11459: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11462: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11465: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 11468: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11471: iconst_1       
        // 11472: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11475: fconst_1       
        // 11476: ldc             4.0
        // 11478: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11481: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11484: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11487: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11490: getstatic       net/minecraft/world/item/Items.SNOWBALL:Lnet/minecraft/world/item/Item;
        // 11493: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11496: bipush          10
        // 11498: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11501: fconst_1       
        // 11502: ldc_w           7.0
        // 11505: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11508: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11511: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11514: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11517: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        // 11520: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11523: iconst_5       
        // 11524: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11527: fconst_1       
        // 11528: ldc             4.0
        // 11530: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11533: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11536: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11539: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11542: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11545: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 11550: aload_1         /* biConsumer */
        // 11551: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.VILLAGE_DESERT_HOUSE:Lnet/minecraft/resources/ResourceLocation;
        // 11554: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11557: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11560: ldc             3.0
        // 11562: ldc             8.0
        // 11564: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11567: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11570: getstatic       net/minecraft/world/item/Items.CLAY_BALL:Lnet/minecraft/world/item/Item;
        // 11573: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11576: iconst_1       
        // 11577: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11580: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11583: getstatic       net/minecraft/world/item/Items.GREEN_DYE:Lnet/minecraft/world/item/Item;
        // 11586: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11589: iconst_1       
        // 11590: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11593: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11596: getstatic       net/minecraft/world/level/block/Blocks.CACTUS:Lnet/minecraft/world/level/block/Block;
        // 11599: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11602: bipush          10
        // 11604: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11607: fconst_1       
        // 11608: ldc             4.0
        // 11610: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11613: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11616: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11619: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11622: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        // 11625: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11628: bipush          10
        // 11630: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11633: fconst_1       
        // 11634: ldc_w           7.0
        // 11637: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11640: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11643: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11646: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11649: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 11652: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11655: bipush          10
        // 11657: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11660: fconst_1       
        // 11661: ldc             4.0
        // 11663: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11666: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11669: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11672: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11675: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        // 11678: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11681: iconst_1       
        // 11682: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11685: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11688: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUSH:Lnet/minecraft/world/level/block/Block;
        // 11691: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11694: iconst_2       
        // 11695: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11698: fconst_1       
        // 11699: ldc             3.0
        // 11701: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11704: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11707: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11710: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11713: getstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 11716: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11719: iconst_1       
        // 11720: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11723: fconst_1       
        // 11724: ldc             3.0
        // 11726: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11729: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11732: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11735: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11738: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11741: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 11746: aload_1         /* biConsumer */
        // 11747: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.WOODLAND_MANSION:Lnet/minecraft/resources/ResourceLocation;
        // 11750: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11753: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11756: fconst_1       
        // 11757: ldc             3.0
        // 11759: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11762: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11765: getstatic       net/minecraft/world/item/Items.LEAD:Lnet/minecraft/world/item/Item;
        // 11768: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11771: bipush          20
        // 11773: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11776: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11779: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 11782: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11785: bipush          15
        // 11787: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11790: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11793: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 11796: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11799: iconst_2       
        // 11800: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11803: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11806: getstatic       net/minecraft/world/item/Items.MUSIC_DISC_13:Lnet/minecraft/world/item/Item;
        // 11809: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11812: bipush          15
        // 11814: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11817: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11820: getstatic       net/minecraft/world/item/Items.MUSIC_DISC_CAT:Lnet/minecraft/world/item/Item;
        // 11823: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11826: bipush          15
        // 11828: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11831: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11834: getstatic       net/minecraft/world/item/Items.NAME_TAG:Lnet/minecraft/world/item/Item;
        // 11837: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11840: bipush          20
        // 11842: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11845: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11848: getstatic       net/minecraft/world/item/Items.CHAINMAIL_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 11851: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11854: bipush          10
        // 11856: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11859: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11862: getstatic       net/minecraft/world/item/Items.DIAMOND_HOE:Lnet/minecraft/world/item/Item;
        // 11865: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11868: bipush          15
        // 11870: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11873: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11876: getstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 11879: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11882: iconst_5       
        // 11883: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11886: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11889: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        // 11892: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11895: bipush          10
        // 11897: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11900: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11903: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11906: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11909: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 11912: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11915: fconst_1       
        // 11916: ldc             4.0
        // 11918: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11921: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11924: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        // 11927: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11930: bipush          10
        // 11932: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11935: fconst_1       
        // 11936: ldc             4.0
        // 11938: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11941: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11944: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11947: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11950: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        // 11953: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11956: iconst_5       
        // 11957: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11960: fconst_1       
        // 11961: ldc             4.0
        // 11963: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 11966: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 11969: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11972: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11975: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        // 11978: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11981: bipush          20
        // 11983: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11986: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 11989: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        // 11992: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 11995: bipush          20
        // 11997: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12000: fconst_1       
        // 12001: ldc             4.0
        // 12003: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12006: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12009: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12012: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12015: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 12018: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12021: bipush          10
        // 12023: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12026: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12029: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        // 12032: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12035: bipush          15
        // 12037: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12040: fconst_1       
        // 12041: ldc             4.0
        // 12043: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12046: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12049: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12052: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12055: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        // 12058: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12061: bipush          15
        // 12063: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12066: fconst_1       
        // 12067: ldc             4.0
        // 12069: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12072: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12075: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12078: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12081: getstatic       net/minecraft/world/item/Items.MELON_SEEDS:Lnet/minecraft/world/item/Item;
        // 12084: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12087: bipush          10
        // 12089: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12092: fconst_2       
        // 12093: ldc             4.0
        // 12095: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12098: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12101: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12104: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12107: getstatic       net/minecraft/world/item/Items.PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;
        // 12110: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12113: bipush          10
        // 12115: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12118: fconst_2       
        // 12119: ldc             4.0
        // 12121: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12124: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12127: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12130: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12133: getstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        // 12136: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12139: bipush          10
        // 12141: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12144: fconst_2       
        // 12145: ldc             4.0
        // 12147: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12150: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12153: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12156: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12159: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 12162: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12165: iconst_3       
        // 12166: invokestatic    net/minecraft/world/level/storage/loot/ConstantIntValue.exactly:(I)Lnet/minecraft/world/level/storage/loot/ConstantIntValue;
        // 12169: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12172: getstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        // 12175: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12178: bipush          10
        // 12180: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12183: fconst_1       
        // 12184: ldc             8.0
        // 12186: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12189: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12192: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12195: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12198: getstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        // 12201: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12204: bipush          10
        // 12206: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12209: fconst_1       
        // 12210: ldc             8.0
        // 12212: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12215: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12218: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12221: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12224: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        // 12227: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12230: bipush          10
        // 12232: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12235: fconst_1       
        // 12236: ldc             8.0
        // 12238: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12241: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12244: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12247: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12250: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        // 12253: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12256: bipush          10
        // 12258: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12261: fconst_1       
        // 12262: ldc             8.0
        // 12264: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12267: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12270: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12273: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12276: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 12279: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 12284: aload_1         /* biConsumer */
        // 12285: getstatic       net/minecraft/world/level/storage/loot/BuiltInLootTables.RUINED_PORTAL:Lnet/minecraft/resources/ResourceLocation;
        // 12288: invokestatic    net/minecraft/world/level/storage/loot/LootTable.lootTable:()Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 12291: invokestatic    net/minecraft/world/level/storage/loot/LootPool.lootPool:()Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12294: ldc             4.0
        // 12296: ldc             8.0
        // 12298: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12301: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.setRolls:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12304: getstatic       net/minecraft/world/item/Items.OBSIDIAN:Lnet/minecraft/world/item/Item;
        // 12307: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12310: bipush          40
        // 12312: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12315: fconst_1       
        // 12316: fconst_2       
        // 12317: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12320: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12323: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12326: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12329: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
        // 12332: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12335: bipush          40
        // 12337: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12340: fconst_1       
        // 12341: ldc             4.0
        // 12343: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12346: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12349: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12352: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12355: getstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        // 12358: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12361: bipush          40
        // 12363: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12366: ldc             9.0
        // 12368: ldc_w           18.0
        // 12371: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12374: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12377: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12380: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12383: getstatic       net/minecraft/world/item/Items.FLINT_AND_STEEL:Lnet/minecraft/world/item/Item;
        // 12386: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12389: bipush          40
        // 12391: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12394: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12397: getstatic       net/minecraft/world/item/Items.FIRE_CHARGE:Lnet/minecraft/world/item/Item;
        // 12400: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12403: bipush          40
        // 12405: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12408: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12411: getstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 12414: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12417: bipush          15
        // 12419: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12422: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12425: getstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        // 12428: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12431: bipush          15
        // 12433: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12436: ldc             4.0
        // 12438: ldc_w           24.0
        // 12441: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12444: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12447: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12450: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12453: getstatic       net/minecraft/world/item/Items.GOLDEN_SWORD:Lnet/minecraft/world/item/Item;
        // 12456: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12459: bipush          15
        // 12461: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12464: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12467: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12470: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12473: getstatic       net/minecraft/world/item/Items.GOLDEN_AXE:Lnet/minecraft/world/item/Item;
        // 12476: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12479: bipush          15
        // 12481: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12484: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12487: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12490: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12493: getstatic       net/minecraft/world/item/Items.GOLDEN_HOE:Lnet/minecraft/world/item/Item;
        // 12496: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12499: bipush          15
        // 12501: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12504: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12507: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12510: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12513: getstatic       net/minecraft/world/item/Items.GOLDEN_SHOVEL:Lnet/minecraft/world/item/Item;
        // 12516: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12519: bipush          15
        // 12521: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12524: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12527: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12530: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12533: getstatic       net/minecraft/world/item/Items.GOLDEN_PICKAXE:Lnet/minecraft/world/item/Item;
        // 12536: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12539: bipush          15
        // 12541: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12544: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12547: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12550: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12553: getstatic       net/minecraft/world/item/Items.GOLDEN_BOOTS:Lnet/minecraft/world/item/Item;
        // 12556: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12559: bipush          15
        // 12561: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12564: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12567: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12570: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12573: getstatic       net/minecraft/world/item/Items.GOLDEN_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 12576: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12579: bipush          15
        // 12581: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12584: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12587: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12590: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12593: getstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        // 12596: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12599: bipush          15
        // 12601: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12604: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12607: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12610: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12613: getstatic       net/minecraft/world/item/Items.GOLDEN_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 12616: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12619: bipush          15
        // 12621: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12624: invokestatic    net/minecraft/world/level/storage/loot/functions/EnchantRandomlyFunction.randomApplicableEnchantment:()Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12627: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12630: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12633: getstatic       net/minecraft/world/item/Items.GLISTERING_MELON_SLICE:Lnet/minecraft/world/item/Item;
        // 12636: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12639: iconst_5       
        // 12640: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12643: ldc             4.0
        // 12645: ldc             12.0
        // 12647: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12650: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12653: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12656: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12659: getstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        // 12662: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12665: iconst_5       
        // 12666: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12669: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12672: getstatic       net/minecraft/world/item/Items.LIGHT_WEIGHTED_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        // 12675: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12678: iconst_5       
        // 12679: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12682: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12685: getstatic       net/minecraft/world/item/Items.GOLDEN_CARROT:Lnet/minecraft/world/item/Item;
        // 12688: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12691: iconst_5       
        // 12692: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12695: ldc             4.0
        // 12697: ldc             12.0
        // 12699: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12702: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12705: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12708: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12711: getstatic       net/minecraft/world/item/Items.CLOCK:Lnet/minecraft/world/item/Item;
        // 12714: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12717: iconst_5       
        // 12718: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12721: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12724: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        // 12727: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12730: iconst_5       
        // 12731: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12734: fconst_2       
        // 12735: ldc             8.0
        // 12737: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12740: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12743: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12746: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12749: getstatic       net/minecraft/world/item/Items.BELL:Lnet/minecraft/world/item/Item;
        // 12752: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12755: iconst_1       
        // 12756: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12759: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12762: getstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 12765: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12768: iconst_1       
        // 12769: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12772: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12775: getstatic       net/minecraft/world/item/Items.GOLD_BLOCK:Lnet/minecraft/world/item/Item;
        // 12778: invokestatic    net/minecraft/world/level/storage/loot/entries/LootItem.lootTableItem:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12781: iconst_1       
        // 12782: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.setWeight:(I)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12785: fconst_1       
        // 12786: fconst_2       
        // 12787: invokestatic    net/minecraft/world/level/storage/loot/RandomValueBounds.between:(FF)Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
        // 12790: invokestatic    net/minecraft/world/level/storage/loot/functions/SetItemCountFunction.setCount:(Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;)Lnet/minecraft/world/level/storage/loot/functions/LootItemConditionalFunction$Builder;
        // 12793: invokevirtual   net/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder.apply:(Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction$Builder;)Lnet/minecraft/world/level/storage/loot/entries/LootPoolSingletonContainer$Builder;
        // 12796: invokevirtual   net/minecraft/world/level/storage/loot/LootPool$Builder.add:(Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer$Builder;)Lnet/minecraft/world/level/storage/loot/LootPool$Builder;
        // 12799: invokevirtual   net/minecraft/world/level/storage/loot/LootTable$Builder.withPool:(Lnet/minecraft/world/level/storage/loot/LootPool$Builder;)Lnet/minecraft/world/level/storage/loot/LootTable$Builder;
        // 12802: invokeinterface java/util/function/BiConsumer.accept:(Ljava/lang/Object;Ljava/lang/Object;)V
        // 12807: return         
        //    Signature:
        //  (Ljava/util/function/BiConsumer<Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/level/storage/loot/LootTable$Builder;>;)V
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  biConsumer  
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
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
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.execLocalTasks(ForkJoinPool.java:1040)
        //     at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:1058)
        //     at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1692)
        //     at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:157)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
