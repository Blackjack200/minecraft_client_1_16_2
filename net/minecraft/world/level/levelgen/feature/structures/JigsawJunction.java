package net.minecraft.world.level.levelgen.feature.structures;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

public class JigsawJunction {
    private final int sourceX;
    private final int sourceGroundY;
    private final int sourceZ;
    private final int deltaY;
    private final StructureTemplatePool.Projection destProjection;
    
    public JigsawJunction(final int integer1, final int integer2, final int integer3, final int integer4, final StructureTemplatePool.Projection a) {
        this.sourceX = integer1;
        this.sourceGroundY = integer2;
        this.sourceZ = integer3;
        this.deltaY = integer4;
        this.destProjection = a;
    }
    
    public int getSourceX() {
        return this.sourceX;
    }
    
    public int getSourceGroundY() {
        return this.sourceGroundY;
    }
    
    public int getSourceZ() {
        return this.sourceZ;
    }
    
    public <T> Dynamic<T> serialize(final DynamicOps<T> dynamicOps) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_2        /* builder3 */
        //     4: aload_2         /* builder3 */
        //     5: aload_1         /* dynamicOps */
        //     6: ldc             "source_x"
        //     8: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //    13: aload_1         /* dynamicOps */
        //    14: aload_0         /* this */
        //    15: getfield        net/minecraft/world/level/levelgen/feature/structures/JigsawJunction.sourceX:I
        //    18: invokeinterface com/mojang/serialization/DynamicOps.createInt:(I)Ljava/lang/Object;
        //    23: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    26: aload_1         /* dynamicOps */
        //    27: ldc             "source_ground_y"
        //    29: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //    34: aload_1         /* dynamicOps */
        //    35: aload_0         /* this */
        //    36: getfield        net/minecraft/world/level/levelgen/feature/structures/JigsawJunction.sourceGroundY:I
        //    39: invokeinterface com/mojang/serialization/DynamicOps.createInt:(I)Ljava/lang/Object;
        //    44: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    47: aload_1         /* dynamicOps */
        //    48: ldc             "source_z"
        //    50: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //    55: aload_1         /* dynamicOps */
        //    56: aload_0         /* this */
        //    57: getfield        net/minecraft/world/level/levelgen/feature/structures/JigsawJunction.sourceZ:I
        //    60: invokeinterface com/mojang/serialization/DynamicOps.createInt:(I)Ljava/lang/Object;
        //    65: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    68: aload_1         /* dynamicOps */
        //    69: ldc             "delta_y"
        //    71: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //    76: aload_1         /* dynamicOps */
        //    77: aload_0         /* this */
        //    78: getfield        net/minecraft/world/level/levelgen/feature/structures/JigsawJunction.deltaY:I
        //    81: invokeinterface com/mojang/serialization/DynamicOps.createInt:(I)Ljava/lang/Object;
        //    86: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //    89: aload_1         /* dynamicOps */
        //    90: ldc             "dest_proj"
        //    92: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //    97: aload_1         /* dynamicOps */
        //    98: aload_0         /* this */
        //    99: getfield        net/minecraft/world/level/levelgen/feature/structures/JigsawJunction.destProjection:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   102: invokevirtual   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.getName:()Ljava/lang/String;
        //   105: invokeinterface com/mojang/serialization/DynamicOps.createString:(Ljava/lang/String;)Ljava/lang/Object;
        //   110: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
        //   113: pop            
        //   114: new             Lcom/mojang/serialization/Dynamic;
        //   117: dup            
        //   118: aload_1         /* dynamicOps */
        //   119: aload_1         /* dynamicOps */
        //   120: aload_2         /* builder3 */
        //   121: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
        //   124: invokeinterface com/mojang/serialization/DynamicOps.createMap:(Ljava/util/Map;)Ljava/lang/Object;
        //   129: invokespecial   com/mojang/serialization/Dynamic.<init>:(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)V
        //   132: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;>(Lcom/mojang/serialization/DynamicOps<TT;>;)Lcom/mojang/serialization/Dynamic<TT;>;
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  dynamicOps  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
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
    
    public static <T> JigsawJunction deserialize(final Dynamic<T> dynamic) {
        return new JigsawJunction(dynamic.get("source_x").asInt(0), dynamic.get("source_ground_y").asInt(0), dynamic.get("source_z").asInt(0), dynamic.get("delta_y").asInt(0), StructureTemplatePool.Projection.byName(dynamic.get("dest_proj").asString("")));
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final JigsawJunction coa3 = (JigsawJunction)object;
        return this.sourceX == coa3.sourceX && this.sourceZ == coa3.sourceZ && this.deltaY == coa3.deltaY && this.destProjection == coa3.destProjection;
    }
    
    public int hashCode() {
        int integer2 = this.sourceX;
        integer2 = 31 * integer2 + this.sourceGroundY;
        integer2 = 31 * integer2 + this.sourceZ;
        integer2 = 31 * integer2 + this.deltaY;
        integer2 = 31 * integer2 + this.destProjection.hashCode();
        return integer2;
    }
    
    public String toString() {
        return new StringBuilder().append("JigsawJunction{sourceX=").append(this.sourceX).append(", sourceGroundY=").append(this.sourceGroundY).append(", sourceZ=").append(this.sourceZ).append(", deltaY=").append(this.deltaY).append(", destProjection=").append(this.destProjection).append('}').toString();
    }
}
