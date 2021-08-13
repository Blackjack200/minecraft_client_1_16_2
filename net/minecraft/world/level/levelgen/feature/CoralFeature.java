package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class CoralFeature extends Feature<NoneFeatureConfiguration> {
    public CoralFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final BlockState cee7 = BlockTags.CORAL_BLOCKS.getRandomElement(random).defaultBlockState();
        return this.placeFeature(bso, random, fx, cee7);
    }
    
    protected abstract boolean placeFeature(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee);
    
    protected boolean placeCoralBlock(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   net/minecraft/core/BlockPos.above:()Lnet/minecraft/core/BlockPos;
        //     4: astore          fx6
        //     6: aload_1         /* brv */
        //     7: aload_3         /* fx */
        //     8: invokeinterface net/minecraft/world/level/LevelAccessor.getBlockState:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
        //    13: astore          cee7
        //    15: aload           cee7
        //    17: getstatic       net/minecraft/world/level/block/Blocks.WATER:Lnet/minecraft/world/level/block/Block;
        //    20: invokevirtual   net/minecraft/world/level/block/state/BlockState.is:(Lnet/minecraft/world/level/block/Block;)Z
        //    23: ifne            37
        //    26: aload           cee7
        //    28: getstatic       net/minecraft/tags/BlockTags.CORALS:Lnet/minecraft/tags/Tag$Named;
        //    31: invokevirtual   net/minecraft/world/level/block/state/BlockState.is:(Lnet/minecraft/tags/Tag;)Z
        //    34: ifeq            54
        //    37: aload_1         /* brv */
        //    38: aload           fx6
        //    40: invokeinterface net/minecraft/world/level/LevelAccessor.getBlockState:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
        //    45: getstatic       net/minecraft/world/level/block/Blocks.WATER:Lnet/minecraft/world/level/block/Block;
        //    48: invokevirtual   net/minecraft/world/level/block/state/BlockState.is:(Lnet/minecraft/world/level/block/Block;)Z
        //    51: ifne            56
        //    54: iconst_0       
        //    55: ireturn        
        //    56: aload_1         /* brv */
        //    57: aload_3         /* fx */
        //    58: aload           cee
        //    60: iconst_3       
        //    61: invokeinterface net/minecraft/world/level/LevelAccessor.setBlock:(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
        //    66: pop            
        //    67: aload_2         /* random */
        //    68: invokevirtual   java/util/Random.nextFloat:()F
        //    71: ldc             0.25
        //    73: fcmpg          
        //    74: ifge            105
        //    77: aload_1         /* brv */
        //    78: aload           fx6
        //    80: getstatic       net/minecraft/tags/BlockTags.CORALS:Lnet/minecraft/tags/Tag$Named;
        //    83: aload_2         /* random */
        //    84: invokeinterface net/minecraft/tags/Tag$Named.getRandomElement:(Ljava/util/Random;)Ljava/lang/Object;
        //    89: checkcast       Lnet/minecraft/world/level/block/Block;
        //    92: invokevirtual   net/minecraft/world/level/block/Block.defaultBlockState:()Lnet/minecraft/world/level/block/state/BlockState;
        //    95: iconst_2       
        //    96: invokeinterface net/minecraft/world/level/LevelAccessor.setBlock:(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
        //   101: pop            
        //   102: goto            150
        //   105: aload_2         /* random */
        //   106: invokevirtual   java/util/Random.nextFloat:()F
        //   109: ldc             0.05
        //   111: fcmpg          
        //   112: ifge            150
        //   115: aload_1         /* brv */
        //   116: aload           fx6
        //   118: getstatic       net/minecraft/world/level/block/Blocks.SEA_PICKLE:Lnet/minecraft/world/level/block/Block;
        //   121: invokevirtual   net/minecraft/world/level/block/Block.defaultBlockState:()Lnet/minecraft/world/level/block/state/BlockState;
        //   124: getstatic       net/minecraft/world/level/block/SeaPickleBlock.PICKLES:Lnet/minecraft/world/level/block/state/properties/IntegerProperty;
        //   127: aload_2         /* random */
        //   128: iconst_4       
        //   129: invokevirtual   java/util/Random.nextInt:(I)I
        //   132: iconst_1       
        //   133: iadd           
        //   134: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   137: invokevirtual   net/minecraft/world/level/block/state/BlockState.setValue:(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
        //   140: checkcast       Lnet/minecraft/world/level/block/state/BlockState;
        //   143: iconst_2       
        //   144: invokeinterface net/minecraft/world/level/LevelAccessor.setBlock:(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
        //   149: pop            
        //   150: getstatic       net/minecraft/core/Direction$Plane.HORIZONTAL:Lnet/minecraft/core/Direction$Plane;
        //   153: invokevirtual   net/minecraft/core/Direction$Plane.iterator:()Ljava/util/Iterator;
        //   156: astore          7
        //   158: aload           7
        //   160: invokeinterface java/util/Iterator.hasNext:()Z
        //   165: ifeq            258
        //   168: aload           7
        //   170: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   175: checkcast       Lnet/minecraft/core/Direction;
        //   178: astore          gc9
        //   180: aload_2         /* random */
        //   181: invokevirtual   java/util/Random.nextFloat:()F
        //   184: ldc             0.2
        //   186: fcmpg          
        //   187: ifge            255
        //   190: aload_3         /* fx */
        //   191: aload           gc9
        //   193: invokevirtual   net/minecraft/core/BlockPos.relative:(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;
        //   196: astore          fx10
        //   198: aload_1         /* brv */
        //   199: aload           fx10
        //   201: invokeinterface net/minecraft/world/level/LevelAccessor.getBlockState:(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
        //   206: getstatic       net/minecraft/world/level/block/Blocks.WATER:Lnet/minecraft/world/level/block/Block;
        //   209: invokevirtual   net/minecraft/world/level/block/state/BlockState.is:(Lnet/minecraft/world/level/block/Block;)Z
        //   212: ifeq            255
        //   215: getstatic       net/minecraft/tags/BlockTags.WALL_CORALS:Lnet/minecraft/tags/Tag$Named;
        //   218: aload_2         /* random */
        //   219: invokeinterface net/minecraft/tags/Tag$Named.getRandomElement:(Ljava/util/Random;)Ljava/lang/Object;
        //   224: checkcast       Lnet/minecraft/world/level/block/Block;
        //   227: invokevirtual   net/minecraft/world/level/block/Block.defaultBlockState:()Lnet/minecraft/world/level/block/state/BlockState;
        //   230: getstatic       net/minecraft/world/level/block/BaseCoralWallFanBlock.FACING:Lnet/minecraft/world/level/block/state/properties/DirectionProperty;
        //   233: aload           gc9
        //   235: invokevirtual   net/minecraft/world/level/block/state/BlockState.setValue:(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;
        //   238: checkcast       Lnet/minecraft/world/level/block/state/BlockState;
        //   241: astore          cee11
        //   243: aload_1         /* brv */
        //   244: aload           fx10
        //   246: aload           cee11
        //   248: iconst_2       
        //   249: invokeinterface net/minecraft/world/level/LevelAccessor.setBlock:(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z
        //   254: pop            
        //   255: goto            158
        //   258: iconst_1       
        //   259: ireturn        
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  brv     
        //  random  
        //  fx      
        //  cee     
        //    StackMapTable: 00 08 FF 00 25 00 06 00 07 00 47 07 00 5F 07 00 41 07 00 53 07 00 41 00 00 FF 00 10 00 00 00 00 FF 00 01 00 06 00 07 00 47 07 00 5F 07 00 41 07 00 53 07 00 41 00 00 FF 00 30 00 06 00 07 00 47 07 00 5F 07 00 41 00 07 00 41 00 00 F9 00 2C FF 00 07 00 08 00 07 00 47 07 00 5F 07 00 41 00 00 00 07 00 8A 00 00 FB 00 60 FF 00 02 00 00 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:265)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.findNestedAssignments(TypeAnalysis.java:272)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:158)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.createDependencyGraph(TypeAnalysis.java:185)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:93)
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
