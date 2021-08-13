package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class DesertVillagePools {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Lnet/minecraft/resources/ResourceLocation;
        //     7: dup            
        //     8: ldc             "village/desert/town_centers"
        //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    13: new             Lnet/minecraft/resources/ResourceLocation;
        //    16: dup            
        //    17: ldc             "empty"
        //    19: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    22: ldc             "village/desert/town_centers/desert_meeting_point_1"
        //    24: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    27: bipush          98
        //    29: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    32: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    35: ldc             "village/desert/town_centers/desert_meeting_point_2"
        //    37: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    40: bipush          98
        //    42: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    45: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    48: ldc             "village/desert/town_centers/desert_meeting_point_3"
        //    50: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    53: bipush          49
        //    55: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    58: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    61: ldc             "village/desert/zombie/town_centers/desert_meeting_point_1"
        //    63: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    66: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    69: iconst_2       
        //    70: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    73: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    76: ldc             "village/desert/zombie/town_centers/desert_meeting_point_2"
        //    78: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    81: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    84: iconst_2       
        //    85: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    88: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    91: ldc             "village/desert/zombie/town_centers/desert_meeting_point_3"
        //    93: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    96: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    99: iconst_1       
        //   100: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   103: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   106: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   109: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   112: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   115: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   118: putstatic       net/minecraft/data/worldgen/DesertVillagePools.START:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   121: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   124: dup            
        //   125: new             Lnet/minecraft/resources/ResourceLocation;
        //   128: dup            
        //   129: ldc             "village/desert/streets"
        //   131: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   134: new             Lnet/minecraft/resources/ResourceLocation;
        //   137: dup            
        //   138: ldc             "village/desert/terminators"
        //   140: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   143: ldc             "village/desert/streets/corner_01"
        //   145: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   148: iconst_3       
        //   149: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   152: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   155: ldc             "village/desert/streets/corner_02"
        //   157: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   160: iconst_3       
        //   161: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   164: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   167: ldc             "village/desert/streets/straight_01"
        //   169: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   172: iconst_4       
        //   173: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   176: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   179: ldc             "village/desert/streets/straight_02"
        //   181: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   184: iconst_4       
        //   185: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   188: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   191: ldc             "village/desert/streets/straight_03"
        //   193: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   196: iconst_3       
        //   197: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   200: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   203: ldc             "village/desert/streets/crossroad_01"
        //   205: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   208: iconst_3       
        //   209: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   212: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   215: ldc             "village/desert/streets/crossroad_02"
        //   217: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   220: iconst_3       
        //   221: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   224: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   227: ldc             "village/desert/streets/crossroad_03"
        //   229: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   232: iconst_3       
        //   233: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   236: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   239: ldc             "village/desert/streets/square_01"
        //   241: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   244: iconst_3       
        //   245: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   248: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   251: ldc             "village/desert/streets/square_02"
        //   253: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   256: iconst_3       
        //   257: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   260: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   263: ldc             "village/desert/streets/turn_01"
        //   265: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   268: iconst_3       
        //   269: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   272: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   275: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   278: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   281: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   284: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   287: pop            
        //   288: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   291: dup            
        //   292: new             Lnet/minecraft/resources/ResourceLocation;
        //   295: dup            
        //   296: ldc             "village/desert/zombie/streets"
        //   298: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   301: new             Lnet/minecraft/resources/ResourceLocation;
        //   304: dup            
        //   305: ldc             "village/desert/zombie/terminators"
        //   307: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   310: ldc             "village/desert/zombie/streets/corner_01"
        //   312: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   315: iconst_3       
        //   316: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   319: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   322: ldc             "village/desert/zombie/streets/corner_02"
        //   324: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   327: iconst_3       
        //   328: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   331: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   334: ldc             "village/desert/zombie/streets/straight_01"
        //   336: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   339: iconst_4       
        //   340: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   343: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   346: ldc             "village/desert/zombie/streets/straight_02"
        //   348: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   351: iconst_4       
        //   352: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   355: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   358: ldc             "village/desert/zombie/streets/straight_03"
        //   360: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   363: iconst_3       
        //   364: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   367: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   370: ldc             "village/desert/zombie/streets/crossroad_01"
        //   372: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   375: iconst_3       
        //   376: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   379: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   382: ldc             "village/desert/zombie/streets/crossroad_02"
        //   384: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   387: iconst_3       
        //   388: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   391: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   394: ldc             "village/desert/zombie/streets/crossroad_03"
        //   396: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   399: iconst_3       
        //   400: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   403: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   406: ldc             "village/desert/zombie/streets/square_01"
        //   408: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   411: iconst_3       
        //   412: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   415: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   418: ldc             "village/desert/zombie/streets/square_02"
        //   420: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   423: iconst_3       
        //   424: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   427: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   430: ldc             "village/desert/zombie/streets/turn_01"
        //   432: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   435: iconst_3       
        //   436: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   439: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   442: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   445: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   448: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   451: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   454: pop            
        //   455: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   458: dup            
        //   459: new             Lnet/minecraft/resources/ResourceLocation;
        //   462: dup            
        //   463: ldc             "village/desert/houses"
        //   465: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   468: new             Lnet/minecraft/resources/ResourceLocation;
        //   471: dup            
        //   472: ldc             "village/desert/terminators"
        //   474: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   477: ldc             "village/desert/houses/desert_small_house_1"
        //   479: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   482: iconst_2       
        //   483: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   486: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   489: ldc             "village/desert/houses/desert_small_house_2"
        //   491: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   494: iconst_2       
        //   495: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   498: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   501: ldc             "village/desert/houses/desert_small_house_3"
        //   503: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   506: iconst_2       
        //   507: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   510: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   513: ldc             "village/desert/houses/desert_small_house_4"
        //   515: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   518: iconst_2       
        //   519: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   522: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   525: ldc             "village/desert/houses/desert_small_house_5"
        //   527: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   530: iconst_2       
        //   531: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   534: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   537: ldc             "village/desert/houses/desert_small_house_6"
        //   539: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   542: iconst_1       
        //   543: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   546: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   549: ldc             "village/desert/houses/desert_small_house_7"
        //   551: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   554: iconst_2       
        //   555: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   558: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   561: ldc             "village/desert/houses/desert_small_house_8"
        //   563: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   566: iconst_2       
        //   567: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   570: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   573: ldc             "village/desert/houses/desert_medium_house_1"
        //   575: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   578: iconst_2       
        //   579: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   582: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   585: ldc             "village/desert/houses/desert_medium_house_2"
        //   587: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   590: iconst_2       
        //   591: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   594: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   597: ldc             "village/desert/houses/desert_butcher_shop_1"
        //   599: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   602: iconst_2       
        //   603: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   606: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   609: ldc             "village/desert/houses/desert_tool_smith_1"
        //   611: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   614: iconst_2       
        //   615: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   618: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   621: bipush          17
        //   623: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   626: dup            
        //   627: iconst_0       
        //   628: ldc             "village/desert/houses/desert_fletcher_house_1"
        //   630: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   633: iconst_2       
        //   634: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   637: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   640: aastore        
        //   641: dup            
        //   642: iconst_1       
        //   643: ldc             "village/desert/houses/desert_shepherd_house_1"
        //   645: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   648: iconst_2       
        //   649: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   652: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   655: aastore        
        //   656: dup            
        //   657: iconst_2       
        //   658: ldc             "village/desert/houses/desert_armorer_1"
        //   660: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   663: iconst_1       
        //   664: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   667: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   670: aastore        
        //   671: dup            
        //   672: iconst_3       
        //   673: ldc             "village/desert/houses/desert_fisher_1"
        //   675: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   678: iconst_2       
        //   679: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   682: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   685: aastore        
        //   686: dup            
        //   687: iconst_4       
        //   688: ldc             "village/desert/houses/desert_tannery_1"
        //   690: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   693: iconst_2       
        //   694: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   697: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   700: aastore        
        //   701: dup            
        //   702: iconst_5       
        //   703: ldc             "village/desert/houses/desert_cartographer_house_1"
        //   705: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   708: iconst_2       
        //   709: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   712: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   715: aastore        
        //   716: dup            
        //   717: bipush          6
        //   719: ldc             "village/desert/houses/desert_library_1"
        //   721: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   724: iconst_2       
        //   725: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   728: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   731: aastore        
        //   732: dup            
        //   733: bipush          7
        //   735: ldc             "village/desert/houses/desert_mason_1"
        //   737: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   740: iconst_2       
        //   741: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   744: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   747: aastore        
        //   748: dup            
        //   749: bipush          8
        //   751: ldc             "village/desert/houses/desert_weaponsmith_1"
        //   753: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   756: iconst_2       
        //   757: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   760: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   763: aastore        
        //   764: dup            
        //   765: bipush          9
        //   767: ldc             "village/desert/houses/desert_temple_1"
        //   769: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   772: iconst_2       
        //   773: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   776: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   779: aastore        
        //   780: dup            
        //   781: bipush          10
        //   783: ldc             "village/desert/houses/desert_temple_2"
        //   785: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   788: iconst_2       
        //   789: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   792: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   795: aastore        
        //   796: dup            
        //   797: bipush          11
        //   799: ldc             "village/desert/houses/desert_large_farm_1"
        //   801: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   804: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   807: bipush          11
        //   809: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   812: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   815: aastore        
        //   816: dup            
        //   817: bipush          12
        //   819: ldc             "village/desert/houses/desert_farm_1"
        //   821: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   824: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   827: iconst_4       
        //   828: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   831: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   834: aastore        
        //   835: dup            
        //   836: bipush          13
        //   838: ldc             "village/desert/houses/desert_farm_2"
        //   840: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   843: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   846: iconst_4       
        //   847: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   850: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   853: aastore        
        //   854: dup            
        //   855: bipush          14
        //   857: ldc             "village/desert/houses/desert_animal_pen_1"
        //   859: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   862: iconst_2       
        //   863: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   866: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   869: aastore        
        //   870: dup            
        //   871: bipush          15
        //   873: ldc             "village/desert/houses/desert_animal_pen_2"
        //   875: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   878: iconst_2       
        //   879: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   882: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   885: aastore        
        //   886: dup            
        //   887: bipush          16
        //   889: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //   892: iconst_5       
        //   893: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   896: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   899: aastore        
        //   900: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   903: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   906: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   909: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   912: pop            
        //   913: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   916: dup            
        //   917: new             Lnet/minecraft/resources/ResourceLocation;
        //   920: dup            
        //   921: ldc             "village/desert/zombie/houses"
        //   923: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   926: new             Lnet/minecraft/resources/ResourceLocation;
        //   929: dup            
        //   930: ldc             "village/desert/zombie/terminators"
        //   932: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   935: ldc             "village/desert/zombie/houses/desert_small_house_1"
        //   937: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   940: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   943: iconst_2       
        //   944: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   947: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   950: ldc             "village/desert/zombie/houses/desert_small_house_2"
        //   952: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   955: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   958: iconst_2       
        //   959: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   962: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   965: ldc             "village/desert/zombie/houses/desert_small_house_3"
        //   967: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   970: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   973: iconst_2       
        //   974: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   977: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   980: ldc             "village/desert/zombie/houses/desert_small_house_4"
        //   982: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   985: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   988: iconst_2       
        //   989: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   992: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   995: ldc             "village/desert/zombie/houses/desert_small_house_5"
        //   997: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1000: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1003: iconst_2       
        //  1004: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1007: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1010: ldc             "village/desert/zombie/houses/desert_small_house_6"
        //  1012: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1015: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1018: iconst_1       
        //  1019: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1022: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1025: ldc             "village/desert/zombie/houses/desert_small_house_7"
        //  1027: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1030: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1033: iconst_2       
        //  1034: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1037: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1040: ldc             "village/desert/zombie/houses/desert_small_house_8"
        //  1042: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1045: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1048: iconst_2       
        //  1049: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1052: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1055: ldc             "village/desert/zombie/houses/desert_medium_house_1"
        //  1057: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1060: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1063: iconst_2       
        //  1064: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1067: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1070: ldc             "village/desert/zombie/houses/desert_medium_house_2"
        //  1072: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1075: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1078: iconst_2       
        //  1079: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1082: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1085: ldc             "village/desert/houses/desert_butcher_shop_1"
        //  1087: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1090: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1093: iconst_2       
        //  1094: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1097: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1100: ldc             "village/desert/houses/desert_tool_smith_1"
        //  1102: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1105: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1108: iconst_2       
        //  1109: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1112: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1115: bipush          17
        //  1117: anewarray       Lcom/mojang/datafixers/util/Pair;
        //  1120: dup            
        //  1121: iconst_0       
        //  1122: ldc             "village/desert/houses/desert_fletcher_house_1"
        //  1124: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1127: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1130: iconst_2       
        //  1131: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1134: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1137: aastore        
        //  1138: dup            
        //  1139: iconst_1       
        //  1140: ldc             "village/desert/houses/desert_shepherd_house_1"
        //  1142: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1145: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1148: iconst_2       
        //  1149: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1152: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1155: aastore        
        //  1156: dup            
        //  1157: iconst_2       
        //  1158: ldc             "village/desert/houses/desert_armorer_1"
        //  1160: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1163: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1166: iconst_1       
        //  1167: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1170: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1173: aastore        
        //  1174: dup            
        //  1175: iconst_3       
        //  1176: ldc             "village/desert/houses/desert_fisher_1"
        //  1178: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1181: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1184: iconst_2       
        //  1185: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1188: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1191: aastore        
        //  1192: dup            
        //  1193: iconst_4       
        //  1194: ldc             "village/desert/houses/desert_tannery_1"
        //  1196: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1199: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1202: iconst_2       
        //  1203: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1206: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1209: aastore        
        //  1210: dup            
        //  1211: iconst_5       
        //  1212: ldc             "village/desert/houses/desert_cartographer_house_1"
        //  1214: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1217: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1220: iconst_2       
        //  1221: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1224: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1227: aastore        
        //  1228: dup            
        //  1229: bipush          6
        //  1231: ldc             "village/desert/houses/desert_library_1"
        //  1233: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1236: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1239: iconst_2       
        //  1240: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1243: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1246: aastore        
        //  1247: dup            
        //  1248: bipush          7
        //  1250: ldc             "village/desert/houses/desert_mason_1"
        //  1252: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1255: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1258: iconst_2       
        //  1259: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1262: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1265: aastore        
        //  1266: dup            
        //  1267: bipush          8
        //  1269: ldc             "village/desert/houses/desert_weaponsmith_1"
        //  1271: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1274: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1277: iconst_2       
        //  1278: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1281: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1284: aastore        
        //  1285: dup            
        //  1286: bipush          9
        //  1288: ldc             "village/desert/houses/desert_temple_1"
        //  1290: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1293: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1296: iconst_2       
        //  1297: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1300: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1303: aastore        
        //  1304: dup            
        //  1305: bipush          10
        //  1307: ldc             "village/desert/houses/desert_temple_2"
        //  1309: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1312: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1315: iconst_2       
        //  1316: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1319: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1322: aastore        
        //  1323: dup            
        //  1324: bipush          11
        //  1326: ldc             "village/desert/houses/desert_large_farm_1"
        //  1328: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1331: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1334: bipush          7
        //  1336: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1339: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1342: aastore        
        //  1343: dup            
        //  1344: bipush          12
        //  1346: ldc             "village/desert/houses/desert_farm_1"
        //  1348: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1351: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1354: iconst_4       
        //  1355: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1358: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1361: aastore        
        //  1362: dup            
        //  1363: bipush          13
        //  1365: ldc             "village/desert/houses/desert_farm_2"
        //  1367: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1370: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1373: iconst_4       
        //  1374: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1377: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1380: aastore        
        //  1381: dup            
        //  1382: bipush          14
        //  1384: ldc             "village/desert/houses/desert_animal_pen_1"
        //  1386: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1389: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1392: iconst_2       
        //  1393: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1396: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1399: aastore        
        //  1400: dup            
        //  1401: bipush          15
        //  1403: ldc             "village/desert/houses/desert_animal_pen_2"
        //  1405: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1408: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1411: iconst_2       
        //  1412: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1415: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1418: aastore        
        //  1419: dup            
        //  1420: bipush          16
        //  1422: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1425: iconst_5       
        //  1426: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1429: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1432: aastore        
        //  1433: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1436: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1439: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1442: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1445: pop            
        //  1446: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1449: dup            
        //  1450: new             Lnet/minecraft/resources/ResourceLocation;
        //  1453: dup            
        //  1454: ldc             "village/desert/terminators"
        //  1456: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1459: new             Lnet/minecraft/resources/ResourceLocation;
        //  1462: dup            
        //  1463: ldc             "empty"
        //  1465: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1468: ldc             "village/desert/terminators/terminator_01"
        //  1470: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1473: iconst_1       
        //  1474: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1477: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1480: ldc             "village/desert/terminators/terminator_02"
        //  1482: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1485: iconst_1       
        //  1486: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1489: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1492: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1495: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1498: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1501: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1504: pop            
        //  1505: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1508: dup            
        //  1509: new             Lnet/minecraft/resources/ResourceLocation;
        //  1512: dup            
        //  1513: ldc             "village/desert/zombie/terminators"
        //  1515: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1518: new             Lnet/minecraft/resources/ResourceLocation;
        //  1521: dup            
        //  1522: ldc             "empty"
        //  1524: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1527: ldc             "village/desert/terminators/terminator_01"
        //  1529: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1532: iconst_1       
        //  1533: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1536: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1539: ldc             "village/desert/zombie/terminators/terminator_02"
        //  1541: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1544: iconst_1       
        //  1545: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1548: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1551: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1554: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1557: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1560: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1563: pop            
        //  1564: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1567: dup            
        //  1568: new             Lnet/minecraft/resources/ResourceLocation;
        //  1571: dup            
        //  1572: ldc             "village/desert/decor"
        //  1574: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1577: new             Lnet/minecraft/resources/ResourceLocation;
        //  1580: dup            
        //  1581: ldc             "empty"
        //  1583: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1586: ldc             "village/desert/desert_lamp_1"
        //  1588: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1591: bipush          10
        //  1593: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1596: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1599: getstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1602: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  1605: iconst_4       
        //  1606: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1609: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1612: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1615: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  1618: iconst_4       
        //  1619: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1622: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1625: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1628: bipush          10
        //  1630: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1633: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1636: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1639: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1642: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1645: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1648: pop            
        //  1649: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1652: dup            
        //  1653: new             Lnet/minecraft/resources/ResourceLocation;
        //  1656: dup            
        //  1657: ldc_w           "village/desert/zombie/decor"
        //  1660: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1663: new             Lnet/minecraft/resources/ResourceLocation;
        //  1666: dup            
        //  1667: ldc             "empty"
        //  1669: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1672: ldc             "village/desert/desert_lamp_1"
        //  1674: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_DESERT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1677: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1680: bipush          10
        //  1682: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1685: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1688: getstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1691: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  1694: iconst_4       
        //  1695: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1698: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1701: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1704: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  1707: iconst_4       
        //  1708: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1711: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1714: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1717: bipush          10
        //  1719: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1722: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1725: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1728: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1731: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1734: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1737: pop            
        //  1738: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1741: dup            
        //  1742: new             Lnet/minecraft/resources/ResourceLocation;
        //  1745: dup            
        //  1746: ldc_w           "village/desert/villagers"
        //  1749: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1752: new             Lnet/minecraft/resources/ResourceLocation;
        //  1755: dup            
        //  1756: ldc             "empty"
        //  1758: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1761: ldc_w           "village/desert/villagers/nitwit"
        //  1764: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1767: iconst_1       
        //  1768: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1771: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1774: ldc_w           "village/desert/villagers/baby"
        //  1777: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1780: iconst_1       
        //  1781: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1784: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1787: ldc_w           "village/desert/villagers/unemployed"
        //  1790: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1793: bipush          10
        //  1795: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1798: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1801: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1804: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1807: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1810: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1813: pop            
        //  1814: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1817: dup            
        //  1818: new             Lnet/minecraft/resources/ResourceLocation;
        //  1821: dup            
        //  1822: ldc_w           "village/desert/zombie/villagers"
        //  1825: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1828: new             Lnet/minecraft/resources/ResourceLocation;
        //  1831: dup            
        //  1832: ldc             "empty"
        //  1834: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1837: ldc_w           "village/desert/zombie/villagers/nitwit"
        //  1840: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1843: iconst_1       
        //  1844: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1847: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1850: ldc_w           "village/desert/zombie/villagers/unemployed"
        //  1853: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1856: bipush          10
        //  1858: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1861: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1864: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1867: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1870: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1873: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1876: pop            
        //  1877: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
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
}
