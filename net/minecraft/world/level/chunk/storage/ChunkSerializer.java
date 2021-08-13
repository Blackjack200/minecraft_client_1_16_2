package net.minecraft.world.level.chunk.storage;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.material.Fluids;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import net.minecraft.nbt.ShortTag;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Locale;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.TickList;
import java.util.BitSet;
import java.util.Iterator;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.Map;
import net.minecraft.world.level.ChunkTickList;
import net.minecraft.world.level.chunk.ProtoTickList;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.Collection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.LevelChunk;
import java.util.Arrays;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.Logger;

public class ChunkSerializer {
    private static final Logger LOGGER;
    
    public static ProtoChunk read(final ServerLevel aag, final StructureManager cst, final PoiManager azl, final ChunkPos bra, final CompoundTag md) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   net/minecraft/server/level/ServerLevel.getChunkSource:()Lnet/minecraft/server/level/ServerChunkCache;
        //     4: invokevirtual   net/minecraft/server/level/ServerChunkCache.getGenerator:()Lnet/minecraft/world/level/chunk/ChunkGenerator;
        //     7: astore          cfv6
        //     9: aload           cfv6
        //    11: invokevirtual   net/minecraft/world/level/chunk/ChunkGenerator.getBiomeSource:()Lnet/minecraft/world/level/biome/BiomeSource;
        //    14: astore          bsv7
        //    16: aload           md
        //    18: ldc             "Level"
        //    20: invokevirtual   net/minecraft/nbt/CompoundTag.getCompound:(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;
        //    23: astore          md8
        //    25: new             Lnet/minecraft/world/level/ChunkPos;
        //    28: dup            
        //    29: aload           md8
        //    31: ldc             "xPos"
        //    33: invokevirtual   net/minecraft/nbt/CompoundTag.getInt:(Ljava/lang/String;)I
        //    36: aload           md8
        //    38: ldc             "zPos"
        //    40: invokevirtual   net/minecraft/nbt/CompoundTag.getInt:(Ljava/lang/String;)I
        //    43: invokespecial   net/minecraft/world/level/ChunkPos.<init>:(II)V
        //    46: astore          bra9
        //    48: aload_3         /* bra */
        //    49: aload           bra9
        //    51: invokestatic    java/util/Objects.equals:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    54: ifne            71
        //    57: getstatic       net/minecraft/world/level/chunk/storage/ChunkSerializer.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    60: ldc             "Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})"
        //    62: aload_3         /* bra */
        //    63: aload_3         /* bra */
        //    64: aload           bra9
        //    66: invokeinterface org/apache/logging/log4j/Logger.error:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    71: new             Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;
        //    74: dup            
        //    75: aload_0         /* aag */
        //    76: invokevirtual   net/minecraft/server/level/ServerLevel.registryAccess:()Lnet/minecraft/core/RegistryAccess;
        //    79: getstatic       net/minecraft/core/Registry.BIOME_REGISTRY:Lnet/minecraft/resources/ResourceKey;
        //    82: invokevirtual   net/minecraft/core/RegistryAccess.registryOrThrow:(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/core/WritableRegistry;
        //    85: aload_3         /* bra */
        //    86: aload           bsv7
        //    88: aload           md8
        //    90: ldc             "Biomes"
        //    92: bipush          11
        //    94: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //    97: ifeq            110
        //   100: aload           md8
        //   102: ldc             "Biomes"
        //   104: invokevirtual   net/minecraft/nbt/CompoundTag.getIntArray:(Ljava/lang/String;)[I
        //   107: goto            111
        //   110: aconst_null    
        //   111: invokespecial   net/minecraft/world/level/chunk/ChunkBiomeContainer.<init>:(Lnet/minecraft/core/IdMap;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/biome/BiomeSource;[I)V
        //   114: astore          cfu10
        //   116: aload           md8
        //   118: ldc             "UpgradeData"
        //   120: bipush          10
        //   122: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   125: ifeq            145
        //   128: new             Lnet/minecraft/world/level/chunk/UpgradeData;
        //   131: dup            
        //   132: aload           md8
        //   134: ldc             "UpgradeData"
        //   136: invokevirtual   net/minecraft/nbt/CompoundTag.getCompound:(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;
        //   139: invokespecial   net/minecraft/world/level/chunk/UpgradeData.<init>:(Lnet/minecraft/nbt/CompoundTag;)V
        //   142: goto            148
        //   145: getstatic       net/minecraft/world/level/chunk/UpgradeData.EMPTY:Lnet/minecraft/world/level/chunk/UpgradeData;
        //   148: astore          cgo11
        //   150: new             Lnet/minecraft/world/level/chunk/ProtoTickList;
        //   153: dup            
        //   154: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //   159: aload_3         /* bra */
        //   160: aload           md8
        //   162: ldc             "ToBeTicked"
        //   164: bipush          9
        //   166: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   169: invokespecial   net/minecraft/world/level/chunk/ProtoTickList.<init>:(Ljava/util/function/Predicate;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/nbt/ListTag;)V
        //   172: astore          cgn12
        //   174: new             Lnet/minecraft/world/level/chunk/ProtoTickList;
        //   177: dup            
        //   178: invokedynamic   BootstrapMethod #1, test:()Ljava/util/function/Predicate;
        //   183: aload_3         /* bra */
        //   184: aload           md8
        //   186: ldc             "LiquidsToBeTicked"
        //   188: bipush          9
        //   190: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   193: invokespecial   net/minecraft/world/level/chunk/ProtoTickList.<init>:(Ljava/util/function/Predicate;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/nbt/ListTag;)V
        //   196: astore          cgn13
        //   198: aload           md8
        //   200: ldc             "isLightOn"
        //   202: invokevirtual   net/minecraft/nbt/CompoundTag.getBoolean:(Ljava/lang/String;)Z
        //   205: istore          boolean14
        //   207: aload           md8
        //   209: ldc             "Sections"
        //   211: bipush          10
        //   213: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   216: astore          mj15
        //   218: bipush          16
        //   220: istore          integer16
        //   222: bipush          16
        //   224: anewarray       Lnet/minecraft/world/level/chunk/LevelChunkSection;
        //   227: astore          arr17
        //   229: aload_0         /* aag */
        //   230: invokevirtual   net/minecraft/server/level/ServerLevel.dimensionType:()Lnet/minecraft/world/level/dimension/DimensionType;
        //   233: invokevirtual   net/minecraft/world/level/dimension/DimensionType.hasSkyLight:()Z
        //   236: istore          boolean18
        //   238: aload_0         /* aag */
        //   239: invokevirtual   net/minecraft/server/level/ServerLevel.getChunkSource:()Lnet/minecraft/server/level/ServerChunkCache;
        //   242: astore          cfw19
        //   244: aload           cfw19
        //   246: invokevirtual   net/minecraft/world/level/chunk/ChunkSource.getLightEngine:()Lnet/minecraft/world/level/lighting/LevelLightEngine;
        //   249: astore          cul20
        //   251: iload           boolean14
        //   253: ifeq            263
        //   256: aload           cul20
        //   258: aload_3         /* bra */
        //   259: iconst_1       
        //   260: invokevirtual   net/minecraft/world/level/lighting/LevelLightEngine.retainData:(Lnet/minecraft/world/level/ChunkPos;Z)V
        //   263: iconst_0       
        //   264: istore          integer21
        //   266: iload           integer21
        //   268: aload           mj15
        //   270: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //   273: if_icmpge       487
        //   276: aload           mj15
        //   278: iload           integer21
        //   280: invokevirtual   net/minecraft/nbt/ListTag.getCompound:(I)Lnet/minecraft/nbt/CompoundTag;
        //   283: astore          md22
        //   285: aload           md22
        //   287: ldc             "Y"
        //   289: invokevirtual   net/minecraft/nbt/CompoundTag.getByte:(Ljava/lang/String;)B
        //   292: istore          integer23
        //   294: aload           md22
        //   296: ldc             "Palette"
        //   298: bipush          9
        //   300: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   303: ifeq            382
        //   306: aload           md22
        //   308: ldc             "BlockStates"
        //   310: bipush          12
        //   312: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   315: ifeq            382
        //   318: new             Lnet/minecraft/world/level/chunk/LevelChunkSection;
        //   321: dup            
        //   322: iload           integer23
        //   324: iconst_4       
        //   325: ishl           
        //   326: invokespecial   net/minecraft/world/level/chunk/LevelChunkSection.<init>:(I)V
        //   329: astore          cgf24
        //   331: aload           cgf24
        //   333: invokevirtual   net/minecraft/world/level/chunk/LevelChunkSection.getStates:()Lnet/minecraft/world/level/chunk/PalettedContainer;
        //   336: aload           md22
        //   338: ldc             "Palette"
        //   340: bipush          10
        //   342: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   345: aload           md22
        //   347: ldc             "BlockStates"
        //   349: invokevirtual   net/minecraft/nbt/CompoundTag.getLongArray:(Ljava/lang/String;)[J
        //   352: invokevirtual   net/minecraft/world/level/chunk/PalettedContainer.read:(Lnet/minecraft/nbt/ListTag;[J)V
        //   355: aload           cgf24
        //   357: invokevirtual   net/minecraft/world/level/chunk/LevelChunkSection.recalcBlockCounts:()V
        //   360: aload           cgf24
        //   362: invokevirtual   net/minecraft/world/level/chunk/LevelChunkSection.isEmpty:()Z
        //   365: ifne            375
        //   368: aload           arr17
        //   370: iload           integer23
        //   372: aload           cgf24
        //   374: aastore        
        //   375: aload_2         /* azl */
        //   376: aload_3         /* bra */
        //   377: aload           cgf24
        //   379: invokevirtual   net/minecraft/world/entity/ai/village/poi/PoiManager.checkConsistencyWithBlocks:(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/LevelChunkSection;)V
        //   382: iload           boolean14
        //   384: ifne            390
        //   387: goto            481
        //   390: aload           md22
        //   392: ldc_w           "BlockLight"
        //   395: bipush          7
        //   397: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   400: ifeq            433
        //   403: aload           cul20
        //   405: getstatic       net/minecraft/world/level/LightLayer.BLOCK:Lnet/minecraft/world/level/LightLayer;
        //   408: aload_3         /* bra */
        //   409: iload           integer23
        //   411: invokestatic    net/minecraft/core/SectionPos.of:(Lnet/minecraft/world/level/ChunkPos;I)Lnet/minecraft/core/SectionPos;
        //   414: new             Lnet/minecraft/world/level/chunk/DataLayer;
        //   417: dup            
        //   418: aload           md22
        //   420: ldc_w           "BlockLight"
        //   423: invokevirtual   net/minecraft/nbt/CompoundTag.getByteArray:(Ljava/lang/String;)[B
        //   426: invokespecial   net/minecraft/world/level/chunk/DataLayer.<init>:([B)V
        //   429: iconst_1       
        //   430: invokevirtual   net/minecraft/world/level/lighting/LevelLightEngine.queueSectionData:(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/chunk/DataLayer;Z)V
        //   433: iload           boolean18
        //   435: ifeq            481
        //   438: aload           md22
        //   440: ldc_w           "SkyLight"
        //   443: bipush          7
        //   445: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   448: ifeq            481
        //   451: aload           cul20
        //   453: getstatic       net/minecraft/world/level/LightLayer.SKY:Lnet/minecraft/world/level/LightLayer;
        //   456: aload_3         /* bra */
        //   457: iload           integer23
        //   459: invokestatic    net/minecraft/core/SectionPos.of:(Lnet/minecraft/world/level/ChunkPos;I)Lnet/minecraft/core/SectionPos;
        //   462: new             Lnet/minecraft/world/level/chunk/DataLayer;
        //   465: dup            
        //   466: aload           md22
        //   468: ldc_w           "SkyLight"
        //   471: invokevirtual   net/minecraft/nbt/CompoundTag.getByteArray:(Ljava/lang/String;)[B
        //   474: invokespecial   net/minecraft/world/level/chunk/DataLayer.<init>:([B)V
        //   477: iconst_1       
        //   478: invokevirtual   net/minecraft/world/level/lighting/LevelLightEngine.queueSectionData:(Lnet/minecraft/world/level/LightLayer;Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/chunk/DataLayer;Z)V
        //   481: iinc            integer21, 1
        //   484: goto            266
        //   487: aload           md8
        //   489: ldc_w           "InhabitedTime"
        //   492: invokevirtual   net/minecraft/nbt/CompoundTag.getLong:(Ljava/lang/String;)J
        //   495: lstore          long21
        //   497: aload           md
        //   499: invokestatic    net/minecraft/world/level/chunk/storage/ChunkSerializer.getChunkTypeFromTag:(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/chunk/ChunkStatus$ChunkType;
        //   502: astore          a23
        //   504: aload           a23
        //   506: getstatic       net/minecraft/world/level/chunk/ChunkStatus$ChunkType.LEVELCHUNK:Lnet/minecraft/world/level/chunk/ChunkStatus$ChunkType;
        //   509: if_acmpne       650
        //   512: aload           md8
        //   514: ldc_w           "TileTicks"
        //   517: bipush          9
        //   519: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   522: ifeq            559
        //   525: aload           md8
        //   527: ldc_w           "TileTicks"
        //   530: bipush          10
        //   532: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   535: getstatic       net/minecraft/core/Registry.BLOCK:Lnet/minecraft/core/DefaultedRegistry;
        //   538: invokedynamic   BootstrapMethod #2, apply:(Lnet/minecraft/core/DefaultedRegistry;)Ljava/util/function/Function;
        //   543: getstatic       net/minecraft/core/Registry.BLOCK:Lnet/minecraft/core/DefaultedRegistry;
        //   546: invokedynamic   BootstrapMethod #3, apply:(Lnet/minecraft/core/DefaultedRegistry;)Ljava/util/function/Function;
        //   551: invokestatic    net/minecraft/world/level/ChunkTickList.create:(Lnet/minecraft/nbt/ListTag;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/world/level/ChunkTickList;
        //   554: astore          bsl25
        //   556: goto            563
        //   559: aload           cgn12
        //   561: astore          bsl25
        //   563: aload           md8
        //   565: ldc_w           "LiquidTicks"
        //   568: bipush          9
        //   570: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   573: ifeq            610
        //   576: aload           md8
        //   578: ldc_w           "LiquidTicks"
        //   581: bipush          10
        //   583: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //   586: getstatic       net/minecraft/core/Registry.FLUID:Lnet/minecraft/core/DefaultedRegistry;
        //   589: invokedynamic   BootstrapMethod #4, apply:(Lnet/minecraft/core/DefaultedRegistry;)Ljava/util/function/Function;
        //   594: getstatic       net/minecraft/core/Registry.FLUID:Lnet/minecraft/core/DefaultedRegistry;
        //   597: invokedynamic   BootstrapMethod #5, apply:(Lnet/minecraft/core/DefaultedRegistry;)Ljava/util/function/Function;
        //   602: invokestatic    net/minecraft/world/level/ChunkTickList.create:(Lnet/minecraft/nbt/ListTag;Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/world/level/ChunkTickList;
        //   605: astore          bsl26
        //   607: goto            614
        //   610: aload           cgn13
        //   612: astore          bsl26
        //   614: new             Lnet/minecraft/world/level/chunk/LevelChunk;
        //   617: dup            
        //   618: aload_0         /* aag */
        //   619: invokevirtual   net/minecraft/server/level/ServerLevel.getLevel:()Lnet/minecraft/server/level/ServerLevel;
        //   622: aload_3         /* bra */
        //   623: aload           cfu10
        //   625: aload           cgo11
        //   627: aload           bsl25
        //   629: aload           bsl26
        //   631: lload           long21
        //   633: aload           arr17
        //   635: aload           md8
        //   637: invokedynamic   BootstrapMethod #6, accept:(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/function/Consumer;
        //   642: invokespecial   net/minecraft/world/level/chunk/LevelChunk.<init>:(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;Lnet/minecraft/world/level/chunk/UpgradeData;Lnet/minecraft/world/level/TickList;Lnet/minecraft/world/level/TickList;J[Lnet/minecraft/world/level/chunk/LevelChunkSection;Ljava/util/function/Consumer;)V
        //   645: astore          cft24
        //   647: goto            825
        //   650: new             Lnet/minecraft/world/level/chunk/ProtoChunk;
        //   653: dup            
        //   654: aload_3         /* bra */
        //   655: aload           cgo11
        //   657: aload           arr17
        //   659: aload           cgn12
        //   661: aload           cgn13
        //   663: invokespecial   net/minecraft/world/level/chunk/ProtoChunk.<init>:(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/chunk/UpgradeData;[Lnet/minecraft/world/level/chunk/LevelChunkSection;Lnet/minecraft/world/level/chunk/ProtoTickList;Lnet/minecraft/world/level/chunk/ProtoTickList;)V
        //   666: astore          cgm25
        //   668: aload           cgm25
        //   670: aload           cfu10
        //   672: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.setBiomes:(Lnet/minecraft/world/level/chunk/ChunkBiomeContainer;)V
        //   675: aload           cgm25
        //   677: astore          cft24
        //   679: aload           cft24
        //   681: lload           long21
        //   683: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setInhabitedTime:(J)V
        //   688: aload           cgm25
        //   690: aload           md8
        //   692: ldc_w           "Status"
        //   695: invokevirtual   net/minecraft/nbt/CompoundTag.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   698: invokestatic    net/minecraft/world/level/chunk/ChunkStatus.byName:(Ljava/lang/String;)Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   701: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.setStatus:(Lnet/minecraft/world/level/chunk/ChunkStatus;)V
        //   704: aload           cft24
        //   706: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.getStatus:()Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   711: getstatic       net/minecraft/world/level/chunk/ChunkStatus.FEATURES:Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   714: invokevirtual   net/minecraft/world/level/chunk/ChunkStatus.isOrAfter:(Lnet/minecraft/world/level/chunk/ChunkStatus;)Z
        //   717: ifeq            727
        //   720: aload           cgm25
        //   722: aload           cul20
        //   724: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.setLightEngine:(Lnet/minecraft/world/level/lighting/LevelLightEngine;)V
        //   727: iload           boolean14
        //   729: ifne            825
        //   732: aload           cft24
        //   734: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.getStatus:()Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   739: getstatic       net/minecraft/world/level/chunk/ChunkStatus.LIGHT:Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   742: invokevirtual   net/minecraft/world/level/chunk/ChunkStatus.isOrAfter:(Lnet/minecraft/world/level/chunk/ChunkStatus;)Z
        //   745: ifeq            825
        //   748: aload_3         /* bra */
        //   749: invokevirtual   net/minecraft/world/level/ChunkPos.getMinBlockX:()I
        //   752: iconst_0       
        //   753: aload_3         /* bra */
        //   754: invokevirtual   net/minecraft/world/level/ChunkPos.getMinBlockZ:()I
        //   757: aload_3         /* bra */
        //   758: invokevirtual   net/minecraft/world/level/ChunkPos.getMaxBlockX:()I
        //   761: sipush          255
        //   764: aload_3         /* bra */
        //   765: invokevirtual   net/minecraft/world/level/ChunkPos.getMaxBlockZ:()I
        //   768: invokestatic    net/minecraft/core/BlockPos.betweenClosed:(IIIIII)Ljava/lang/Iterable;
        //   771: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   776: astore          25
        //   778: aload           25
        //   780: invokeinterface java/util/Iterator.hasNext:()Z
        //   785: ifeq            825
        //   788: aload           25
        //   790: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   795: checkcast       Lnet/minecraft/core/BlockPos;
        //   798: astore          fx27
        //   800: aload           cft24
        //   802: aload           fx27
        //   804: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.getBlockState:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
        //   809: invokevirtual   net/minecraft/world/level/block/state/BlockState.getLightEmission:()I
        //   812: ifeq            822
        //   815: aload           cgm25
        //   817: aload           fx27
        //   819: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.addLight:(Lnet/minecraft/core/BlockPos;)V
        //   822: goto            778
        //   825: aload           cft24
        //   827: iload           boolean14
        //   829: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setLightCorrect:(Z)V
        //   834: aload           md8
        //   836: ldc_w           "Heightmaps"
        //   839: invokevirtual   net/minecraft/nbt/CompoundTag.getCompound:(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;
        //   842: astore          md25
        //   844: ldc             Lnet/minecraft/world/level/levelgen/Heightmap$Types;.class
        //   846: invokestatic    java/util/EnumSet.noneOf:(Ljava/lang/Class;)Ljava/util/EnumSet;
        //   849: astore          enumSet26
        //   851: aload           cft24
        //   853: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.getStatus:()Lnet/minecraft/world/level/chunk/ChunkStatus;
        //   858: invokevirtual   net/minecraft/world/level/chunk/ChunkStatus.heightmapsAfter:()Ljava/util/EnumSet;
        //   861: invokevirtual   java/util/EnumSet.iterator:()Ljava/util/Iterator;
        //   864: astore          26
        //   866: aload           26
        //   868: invokeinterface java/util/Iterator.hasNext:()Z
        //   873: ifeq            937
        //   876: aload           26
        //   878: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   883: checkcast       Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //   886: astore          a28
        //   888: aload           a28
        //   890: invokevirtual   net/minecraft/world/level/levelgen/Heightmap$Types.getSerializationKey:()Ljava/lang/String;
        //   893: astore          string29
        //   895: aload           md25
        //   897: aload           string29
        //   899: bipush          12
        //   901: invokevirtual   net/minecraft/nbt/CompoundTag.contains:(Ljava/lang/String;I)Z
        //   904: ifeq            926
        //   907: aload           cft24
        //   909: aload           a28
        //   911: aload           md25
        //   913: aload           string29
        //   915: invokevirtual   net/minecraft/nbt/CompoundTag.getLongArray:(Ljava/lang/String;)[J
        //   918: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setHeightmap:(Lnet/minecraft/world/level/levelgen/Heightmap$Types;[J)V
        //   923: goto            934
        //   926: aload           enumSet26
        //   928: aload           a28
        //   930: invokevirtual   java/util/EnumSet.add:(Ljava/lang/Object;)Z
        //   933: pop            
        //   934: goto            866
        //   937: aload           cft24
        //   939: aload           enumSet26
        //   941: invokestatic    net/minecraft/world/level/levelgen/Heightmap.primeHeightmaps:(Lnet/minecraft/world/level/chunk/ChunkAccess;Ljava/util/Set;)V
        //   944: aload           md8
        //   946: ldc_w           "Structures"
        //   949: invokevirtual   net/minecraft/nbt/CompoundTag.getCompound:(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;
        //   952: astore          md27
        //   954: aload           cft24
        //   956: aload_1         /* cst */
        //   957: aload           md27
        //   959: aload_0         /* aag */
        //   960: invokevirtual   net/minecraft/server/level/ServerLevel.getSeed:()J
        //   963: invokestatic    net/minecraft/world/level/chunk/storage/ChunkSerializer.unpackStructureStart:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;Lnet/minecraft/nbt/CompoundTag;J)Ljava/util/Map;
        //   966: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setAllStarts:(Ljava/util/Map;)V
        //   971: aload           cft24
        //   973: aload_3         /* bra */
        //   974: aload           md27
        //   976: invokestatic    net/minecraft/world/level/chunk/storage/ChunkSerializer.unpackStructureReferences:(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/nbt/CompoundTag;)Ljava/util/Map;
        //   979: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setAllReferences:(Ljava/util/Map;)V
        //   984: aload           md8
        //   986: ldc_w           "shouldSave"
        //   989: invokevirtual   net/minecraft/nbt/CompoundTag.getBoolean:(Ljava/lang/String;)Z
        //   992: ifeq            1003
        //   995: aload           cft24
        //   997: iconst_1       
        //   998: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setUnsaved:(Z)V
        //  1003: aload           md8
        //  1005: ldc_w           "PostProcessing"
        //  1008: bipush          9
        //  1010: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //  1013: astore          mj28
        //  1015: iconst_0       
        //  1016: istore          integer29
        //  1018: iload           integer29
        //  1020: aload           mj28
        //  1022: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1025: if_icmpge       1078
        //  1028: aload           mj28
        //  1030: iload           integer29
        //  1032: invokevirtual   net/minecraft/nbt/ListTag.getList:(I)Lnet/minecraft/nbt/ListTag;
        //  1035: astore          mj30
        //  1037: iconst_0       
        //  1038: istore          integer31
        //  1040: iload           integer31
        //  1042: aload           mj30
        //  1044: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1047: if_icmpge       1072
        //  1050: aload           cft24
        //  1052: aload           mj30
        //  1054: iload           integer31
        //  1056: invokevirtual   net/minecraft/nbt/ListTag.getShort:(I)S
        //  1059: iload           integer29
        //  1061: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.addPackedPostProcess:(SI)V
        //  1066: iinc            integer31, 1
        //  1069: goto            1040
        //  1072: iinc            integer29, 1
        //  1075: goto            1018
        //  1078: aload           a23
        //  1080: getstatic       net/minecraft/world/level/chunk/ChunkStatus$ChunkType.LEVELCHUNK:Lnet/minecraft/world/level/chunk/ChunkStatus$ChunkType;
        //  1083: if_acmpne       1099
        //  1086: new             Lnet/minecraft/world/level/chunk/ImposterProtoChunk;
        //  1089: dup            
        //  1090: aload           cft24
        //  1092: checkcast       Lnet/minecraft/world/level/chunk/LevelChunk;
        //  1095: invokespecial   net/minecraft/world/level/chunk/ImposterProtoChunk.<init>:(Lnet/minecraft/world/level/chunk/LevelChunk;)V
        //  1098: areturn        
        //  1099: aload           cft24
        //  1101: checkcast       Lnet/minecraft/world/level/chunk/ProtoChunk;
        //  1104: astore          cgm29
        //  1106: aload           md8
        //  1108: ldc_w           "Entities"
        //  1111: bipush          10
        //  1113: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //  1116: astore          mj30
        //  1118: iconst_0       
        //  1119: istore          integer31
        //  1121: iload           integer31
        //  1123: aload           mj30
        //  1125: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1128: if_icmpge       1149
        //  1131: aload           cgm29
        //  1133: aload           mj30
        //  1135: iload           integer31
        //  1137: invokevirtual   net/minecraft/nbt/ListTag.getCompound:(I)Lnet/minecraft/nbt/CompoundTag;
        //  1140: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.addEntity:(Lnet/minecraft/nbt/CompoundTag;)V
        //  1143: iinc            integer31, 1
        //  1146: goto            1121
        //  1149: aload           md8
        //  1151: ldc_w           "TileEntities"
        //  1154: bipush          10
        //  1156: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //  1159: astore          mj31
        //  1161: iconst_0       
        //  1162: istore          integer32
        //  1164: iload           integer32
        //  1166: aload           mj31
        //  1168: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1171: if_icmpge       1198
        //  1174: aload           mj31
        //  1176: iload           integer32
        //  1178: invokevirtual   net/minecraft/nbt/ListTag.getCompound:(I)Lnet/minecraft/nbt/CompoundTag;
        //  1181: astore          md33
        //  1183: aload           cft24
        //  1185: aload           md33
        //  1187: invokeinterface net/minecraft/world/level/chunk/ChunkAccess.setBlockEntityNbt:(Lnet/minecraft/nbt/CompoundTag;)V
        //  1192: iinc            integer32, 1
        //  1195: goto            1164
        //  1198: aload           md8
        //  1200: ldc_w           "Lights"
        //  1203: bipush          9
        //  1205: invokevirtual   net/minecraft/nbt/CompoundTag.getList:(Ljava/lang/String;I)Lnet/minecraft/nbt/ListTag;
        //  1208: astore          mj32
        //  1210: iconst_0       
        //  1211: istore          integer33
        //  1213: iload           integer33
        //  1215: aload           mj32
        //  1217: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1220: if_icmpge       1271
        //  1223: aload           mj32
        //  1225: iload           integer33
        //  1227: invokevirtual   net/minecraft/nbt/ListTag.getList:(I)Lnet/minecraft/nbt/ListTag;
        //  1230: astore          mj34
        //  1232: iconst_0       
        //  1233: istore          integer35
        //  1235: iload           integer35
        //  1237: aload           mj34
        //  1239: invokevirtual   net/minecraft/nbt/ListTag.size:()I
        //  1242: if_icmpge       1265
        //  1245: aload           cgm29
        //  1247: aload           mj34
        //  1249: iload           integer35
        //  1251: invokevirtual   net/minecraft/nbt/ListTag.getShort:(I)S
        //  1254: iload           integer33
        //  1256: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.addLight:(SI)V
        //  1259: iinc            integer35, 1
        //  1262: goto            1235
        //  1265: iinc            integer33, 1
        //  1268: goto            1213
        //  1271: aload           md8
        //  1273: ldc_w           "CarvingMasks"
        //  1276: invokevirtual   net/minecraft/nbt/CompoundTag.getCompound:(Ljava/lang/String;)Lnet/minecraft/nbt/CompoundTag;
        //  1279: astore          md33
        //  1281: aload           md33
        //  1283: invokevirtual   net/minecraft/nbt/CompoundTag.getAllKeys:()Ljava/util/Set;
        //  1286: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  1291: astore          33
        //  1293: aload           33
        //  1295: invokeinterface java/util/Iterator.hasNext:()Z
        //  1300: ifeq            1342
        //  1303: aload           33
        //  1305: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1310: checkcast       Ljava/lang/String;
        //  1313: astore          string35
        //  1315: aload           string35
        //  1317: invokestatic    net/minecraft/world/level/levelgen/GenerationStep$Carving.valueOf:(Ljava/lang/String;)Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;
        //  1320: astore          a36
        //  1322: aload           cgm29
        //  1324: aload           a36
        //  1326: aload           md33
        //  1328: aload           string35
        //  1330: invokevirtual   net/minecraft/nbt/CompoundTag.getByteArray:(Ljava/lang/String;)[B
        //  1333: invokestatic    java/util/BitSet.valueOf:([B)Ljava/util/BitSet;
        //  1336: invokevirtual   net/minecraft/world/level/chunk/ProtoChunk.setCarvingMask:(Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;Ljava/util/BitSet;)V
        //  1339: goto            1293
        //  1342: aload           cgm29
        //  1344: areturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  aag   
        //  cst   
        //  azl   
        //  bra   
        //  md    
        //    StackMapTable: 00 2A FE 00 47 00 07 00 61 07 00 3D FF 00 26 00 08 07 00 29 07 00 7F 07 00 81 07 00 43 07 00 3D 00 00 07 00 3D 00 05 08 00 47 08 00 47 07 00 83 07 00 43 07 00 61 FF 00 00 00 08 07 00 29 07 00 7F 07 00 81 07 00 43 07 00 3D 00 00 07 00 3D 00 06 08 00 47 08 00 47 07 00 83 07 00 43 07 00 61 07 00 85 FD 00 21 00 07 00 63 42 07 00 8C FF 00 72 00 14 07 00 29 07 00 7F 07 00 81 07 00 43 07 00 3D 00 00 07 00 3D 00 07 00 63 07 00 8C 07 00 95 07 00 95 01 07 00 DC 00 07 00 DE 01 00 07 00 D6 00 00 FC 00 02 01 FE 00 6C 07 00 3D 01 07 00 C4 FA 00 06 07 2A F9 00 2F FF 00 05 00 14 07 00 29 07 00 7F 00 07 00 43 07 00 3D 00 00 07 00 3D 00 07 00 63 07 00 8C 07 00 95 07 00 95 01 00 00 07 00 DE 00 00 07 00 D6 00 00 FF 00 47 00 16 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 07 00 63 07 00 8C 07 00 95 07 00 95 01 00 00 07 00 DE 00 00 00 04 07 00 11 00 00 FF 00 03 00 18 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 07 00 63 07 00 8C 00 07 00 95 01 00 00 07 00 DE 00 00 00 04 07 00 11 00 07 01 5C 00 00 2E FF 00 03 00 19 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 07 00 63 07 00 8C 00 00 01 00 00 07 00 DE 00 00 00 04 07 00 11 00 07 01 5C 07 01 5C 00 00 FF 00 23 00 16 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 07 00 63 07 00 8C 07 00 95 07 00 95 01 00 00 07 00 DE 00 00 07 00 D6 04 07 00 11 00 00 FF 00 4C 00 19 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 00 00 00 00 01 00 00 00 00 00 00 00 00 07 00 11 07 01 7F 07 01 7F 00 00 FC 00 32 07 01 C7 2B FF 00 02 00 18 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 00 00 00 00 01 00 00 00 00 00 00 00 00 07 00 11 07 01 88 00 00 FF 00 28 00 1B 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 11 07 01 88 07 00 3D 07 01 E3 07 01 C7 00 00 FC 00 3B 07 00 07 FA 00 07 FF 00 02 00 1A 07 00 29 07 00 7F 00 07 00 43 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 11 07 01 88 00 07 01 E3 00 00 FF 00 41 00 18 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 11 07 01 88 00 00 FF 00 0E 00 1D 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 11 07 01 88 00 00 00 07 00 DC 01 00 00 FD 00 15 07 00 DC 01 F9 00 1F FF 00 05 00 18 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 00 11 07 01 88 00 00 FF 00 14 00 18 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 88 00 00 FF 00 15 00 1F 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 88 00 00 00 00 07 01 7F 07 00 DC 01 00 00 F9 00 1B FE 00 0E 00 07 00 DC 01 FF 00 21 00 1D 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 7F 00 00 FF 00 0E 00 21 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 7F 00 00 07 00 DC 01 00 00 FD 00 15 07 00 DC 01 F9 00 1D FF 00 05 00 1D 00 00 00 00 00 00 00 07 00 3D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 7F 00 00 FF 00 15 00 22 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 7F 00 00 00 07 00 3D 07 01 C7 00 00 FF 00 30 00 1D 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 7F 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 8
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:254)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    public static CompoundTag write(final ServerLevel aag, final ChunkAccess cft) {
        final ChunkPos bra3 = cft.getPos();
        final CompoundTag md4 = new CompoundTag();
        final CompoundTag md5 = new CompoundTag();
        md4.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        md4.put("Level", (Tag)md5);
        md5.putInt("xPos", bra3.x);
        md5.putInt("zPos", bra3.z);
        md5.putLong("LastUpdate", aag.getGameTime());
        md5.putLong("InhabitedTime", cft.getInhabitedTime());
        md5.putString("Status", cft.getStatus().getName());
        final UpgradeData cgo6 = cft.getUpgradeData();
        if (!cgo6.isEmpty()) {
            md5.put("UpgradeData", (Tag)cgo6.write());
        }
        final LevelChunkSection[] arr7 = cft.getSections();
        final ListTag mj8 = new ListTag();
        final LevelLightEngine cul9 = aag.getChunkSource().getLightEngine();
        final boolean boolean10 = cft.isLightCorrect();
        for (int integer11 = -1; integer11 < 17; ++integer11) {
            final int integer12 = integer11;
            final LevelChunkSection cgf13 = (LevelChunkSection)Arrays.stream((Object[])arr7).filter(cgf -> cgf != null && cgf.bottomBlockY() >> 4 == integer12).findFirst().orElse(LevelChunk.EMPTY_SECTION);
            final DataLayer cfy14 = cul9.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(bra3, integer12));
            final DataLayer cfy15 = cul9.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(bra3, integer12));
            if (cgf13 != LevelChunk.EMPTY_SECTION || cfy14 != null || cfy15 != null) {
                final CompoundTag md6 = new CompoundTag();
                md6.putByte("Y", (byte)(integer12 & 0xFF));
                if (cgf13 != LevelChunk.EMPTY_SECTION) {
                    cgf13.getStates().write(md6, "Palette", "BlockStates");
                }
                if (cfy14 != null && !cfy14.isEmpty()) {
                    md6.putByteArray("BlockLight", cfy14.getData());
                }
                if (cfy15 != null && !cfy15.isEmpty()) {
                    md6.putByteArray("SkyLight", cfy15.getData());
                }
                mj8.add(md6);
            }
        }
        md5.put("Sections", (Tag)mj8);
        if (boolean10) {
            md5.putBoolean("isLightOn", true);
        }
        final ChunkBiomeContainer cfu11 = cft.getBiomes();
        if (cfu11 != null) {
            md5.putIntArray("Biomes", cfu11.writeBiomes());
        }
        final ListTag mj9 = new ListTag();
        for (final BlockPos fx14 : cft.getBlockEntitiesPos()) {
            final CompoundTag md7 = cft.getBlockEntityNbtForSaving(fx14);
            if (md7 != null) {
                mj9.add(md7);
            }
        }
        md5.put("TileEntities", (Tag)mj9);
        final ListTag mj10 = new ListTag();
        if (cft.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
            final LevelChunk cge14 = (LevelChunk)cft;
            cge14.setLastSaveHadEntities(false);
            for (int integer13 = 0; integer13 < cge14.getEntitySections().length; ++integer13) {
                for (final Entity apx17 : cge14.getEntitySections()[integer13]) {
                    final CompoundTag md8 = new CompoundTag();
                    if (apx17.save(md8)) {
                        cge14.setLastSaveHadEntities(true);
                        mj10.add(md8);
                    }
                }
            }
        }
        else {
            final ProtoChunk cgm14 = (ProtoChunk)cft;
            mj10.addAll((Collection)cgm14.getEntities());
            md5.put("Lights", (Tag)packOffsets(cgm14.getPackedLights()));
            final CompoundTag md7 = new CompoundTag();
            for (final GenerationStep.Carving a19 : GenerationStep.Carving.values()) {
                final BitSet bitSet20 = cgm14.getCarvingMask(a19);
                if (bitSet20 != null) {
                    md7.putByteArray(a19.toString(), bitSet20.toByteArray());
                }
            }
            md5.put("CarvingMasks", (Tag)md7);
        }
        md5.put("Entities", (Tag)mj10);
        final TickList<Block> bsl14 = cft.getBlockTicks();
        if (bsl14 instanceof ProtoTickList) {
            md5.put("ToBeTicked", (Tag)((ProtoTickList)bsl14).save());
        }
        else if (bsl14 instanceof ChunkTickList) {
            md5.put("TileTicks", (Tag)((ChunkTickList)bsl14).save());
        }
        else {
            md5.put("TileTicks", (Tag)aag.getBlockTicks().save(bra3));
        }
        final TickList<Fluid> bsl15 = cft.getLiquidTicks();
        if (bsl15 instanceof ProtoTickList) {
            md5.put("LiquidsToBeTicked", (Tag)((ProtoTickList)bsl15).save());
        }
        else if (bsl15 instanceof ChunkTickList) {
            md5.put("LiquidTicks", (Tag)((ChunkTickList)bsl15).save());
        }
        else {
            md5.put("LiquidTicks", (Tag)aag.getLiquidTicks().save(bra3));
        }
        md5.put("PostProcessing", (Tag)packOffsets(cft.getPostProcessing()));
        final CompoundTag md6 = new CompoundTag();
        for (final Map.Entry<Heightmap.Types, Heightmap> entry18 : cft.getHeightmaps()) {
            if (cft.getStatus().heightmapsAfter().contains(entry18.getKey())) {
                md6.put(((Heightmap.Types)entry18.getKey()).getSerializationKey(), new LongArrayTag(((Heightmap)entry18.getValue()).getRawData()));
            }
        }
        md5.put("Heightmaps", (Tag)md6);
        md5.put("Structures", (Tag)packStructureData(bra3, cft.getAllStarts(), cft.getAllReferences()));
        return md4;
    }
    
    public static ChunkStatus.ChunkType getChunkTypeFromTag(@Nullable final CompoundTag md) {
        if (md != null) {
            final ChunkStatus cfx2 = ChunkStatus.byName(md.getCompound("Level").getString("Status"));
            if (cfx2 != null) {
                return cfx2.getChunkType();
            }
        }
        return ChunkStatus.ChunkType.PROTOCHUNK;
    }
    
    private static void postLoadChunk(final CompoundTag md, final LevelChunk cge) {
        final ListTag mj3 = md.getList("Entities", 10);
        final Level bru4 = cge.getLevel();
        for (int integer5 = 0; integer5 < mj3.size(); ++integer5) {
            final CompoundTag md2 = mj3.getCompound(integer5);
            EntityType.loadEntityRecursive(md2, bru4, (Function<Entity, Entity>)(apx -> {
                cge.addEntity(apx);
                return apx;
            }));
            cge.setLastSaveHadEntities(true);
        }
        final ListTag mj4 = md.getList("TileEntities", 10);
        for (int integer6 = 0; integer6 < mj4.size(); ++integer6) {
            final CompoundTag md3 = mj4.getCompound(integer6);
            final boolean boolean8 = md3.getBoolean("keepPacked");
            if (boolean8) {
                cge.setBlockEntityNbt(md3);
            }
            else {
                final BlockPos fx9 = new BlockPos(md3.getInt("x"), md3.getInt("y"), md3.getInt("z"));
                final BlockEntity ccg10 = BlockEntity.loadStatic(cge.getBlockState(fx9), md3);
                if (ccg10 != null) {
                    cge.addBlockEntity(ccg10);
                }
            }
        }
    }
    
    private static CompoundTag packStructureData(final ChunkPos bra, final Map<StructureFeature<?>, StructureStart<?>> map2, final Map<StructureFeature<?>, LongSet> map3) {
        final CompoundTag md4 = new CompoundTag();
        final CompoundTag md5 = new CompoundTag();
        for (final Map.Entry<StructureFeature<?>, StructureStart<?>> entry7 : map2.entrySet()) {
            md5.put(((StructureFeature)entry7.getKey()).getFeatureName(), ((StructureStart)entry7.getValue()).createTag(bra.x, bra.z));
        }
        md4.put("Starts", (Tag)md5);
        final CompoundTag md6 = new CompoundTag();
        for (final Map.Entry<StructureFeature<?>, LongSet> entry8 : map3.entrySet()) {
            md6.put(((StructureFeature)entry8.getKey()).getFeatureName(), new LongArrayTag((LongSet)entry8.getValue()));
        }
        md4.put("References", (Tag)md6);
        return md4;
    }
    
    private static Map<StructureFeature<?>, StructureStart<?>> unpackStructureStart(final StructureManager cst, final CompoundTag md, final long long3) {
        final Map<StructureFeature<?>, StructureStart<?>> map5 = (Map<StructureFeature<?>, StructureStart<?>>)Maps.newHashMap();
        final CompoundTag md2 = md.getCompound("Starts");
        for (final String string8 : md2.getAllKeys()) {
            final String string9 = string8.toLowerCase(Locale.ROOT);
            final StructureFeature<?> ckx10 = StructureFeature.STRUCTURES_REGISTRY.get(string9);
            if (ckx10 == null) {
                ChunkSerializer.LOGGER.error("Unknown structure start: {}", string9);
            }
            else {
                final StructureStart<?> crs11 = StructureFeature.loadStaticStart(cst, md2.getCompound(string8), long3);
                if (crs11 == null) {
                    continue;
                }
                map5.put(ckx10, crs11);
            }
        }
        return map5;
    }
    
    private static Map<StructureFeature<?>, LongSet> unpackStructureReferences(final ChunkPos bra, final CompoundTag md) {
        final Map<StructureFeature<?>, LongSet> map3 = (Map<StructureFeature<?>, LongSet>)Maps.newHashMap();
        final CompoundTag md2 = md.getCompound("References");
        for (final String string6 : md2.getAllKeys()) {
            map3.put(StructureFeature.STRUCTURES_REGISTRY.get(string6.toLowerCase(Locale.ROOT)), new LongOpenHashSet(Arrays.stream(md2.getLongArray(string6)).filter(long3 -> {
                final ChunkPos bra2 = new ChunkPos(long3);
                if (bra2.getChessboardDistance(bra) > 8) {
                    ChunkSerializer.LOGGER.warn("Found invalid structure reference [ {} @ {} ] for chunk {}.", (Object)string6, (Object)bra2, (Object)bra);
                    return false;
                }
                return true;
            }).toArray()));
        }
        return map3;
    }
    
    public static ListTag packOffsets(final ShortList[] arr) {
        final ListTag mj2 = new ListTag();
        for (final ShortList shortList6 : arr) {
            final ListTag mj3 = new ListTag();
            if (shortList6 != null) {
                for (final Short short9 : shortList6) {
                    mj3.add(ShortTag.valueOf(short9));
                }
            }
            mj2.add(mj3);
        }
        return mj2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
