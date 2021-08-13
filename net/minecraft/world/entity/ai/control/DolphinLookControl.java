package net.minecraft.world.entity.ai.control;

import net.minecraft.world.entity.Mob;

public class DolphinLookControl extends LookControl {
    private final int maxYRotFromCenter;
    
    public DolphinLookControl(final Mob aqk, final int integer) {
        super(aqk);
        this.maxYRotFromCenter = integer;
    }
    
    @Override
    public void tick() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.hasWanted:Z
        //     4: ifeq            73
        //     7: aload_0         /* this */
        //     8: iconst_0       
        //     9: putfield        net/minecraft/world/entity/ai/control/DolphinLookControl.hasWanted:Z
        //    12: aload_0         /* this */
        //    13: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    16: aload_0         /* this */
        //    17: aload_0         /* this */
        //    18: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    21: getfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //    24: aload_0         /* this */
        //    25: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.getYRotD:()F
        //    28: ldc             20.0
        //    30: fadd           
        //    31: aload_0         /* this */
        //    32: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.yMaxRotSpeed:F
        //    35: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.rotateTowards:(FFF)F
        //    38: putfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //    41: aload_0         /* this */
        //    42: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    45: aload_0         /* this */
        //    46: aload_0         /* this */
        //    47: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    50: getfield        net/minecraft/world/entity/Mob.xRot:F
        //    53: aload_0         /* this */
        //    54: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.getXRotD:()F
        //    57: ldc             10.0
        //    59: fadd           
        //    60: aload_0         /* this */
        //    61: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.xMaxRotAngle:F
        //    64: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.rotateTowards:(FFF)F
        //    67: putfield        net/minecraft/world/entity/Mob.xRot:F
        //    70: goto            136
        //    73: aload_0         /* this */
        //    74: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    77: invokevirtual   net/minecraft/world/entity/Mob.getNavigation:()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;
        //    80: invokevirtual   net/minecraft/world/entity/ai/navigation/PathNavigation.isDone:()Z
        //    83: ifeq            107
        //    86: aload_0         /* this */
        //    87: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    90: aload_0         /* this */
        //    91: aload_0         /* this */
        //    92: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //    95: getfield        net/minecraft/world/entity/Mob.xRot:F
        //    98: fconst_0       
        //    99: ldc             5.0
        //   101: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.rotateTowards:(FFF)F
        //   104: putfield        net/minecraft/world/entity/Mob.xRot:F
        //   107: aload_0         /* this */
        //   108: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   111: aload_0         /* this */
        //   112: aload_0         /* this */
        //   113: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   116: getfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //   119: aload_0         /* this */
        //   120: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   123: getfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   126: aload_0         /* this */
        //   127: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.yMaxRotSpeed:F
        //   130: invokevirtual   net/minecraft/world/entity/ai/control/DolphinLookControl.rotateTowards:(FFF)F
        //   133: putfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //   136: aload_0         /* this */
        //   137: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   140: getfield        net/minecraft/world/entity/Mob.yHeadRot:F
        //   143: aload_0         /* this */
        //   144: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   147: getfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   150: fsub           
        //   151: invokestatic    net/minecraft/util/Mth.wrapDegrees:(F)F
        //   154: fstore_1        /* float2 */
        //   155: fload_1         /* float2 */
        //   156: aload_0         /* this */
        //   157: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.maxYRotFromCenter:I
        //   160: ineg           
        //   161: i2f            
        //   162: fcmpg          
        //   163: ifge            183
        //   166: aload_0         /* this */
        //   167: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   170: dup            
        //   171: getfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   174: ldc             4.0
        //   176: fsub           
        //   177: putfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   180: goto            207
        //   183: fload_1         /* float2 */
        //   184: aload_0         /* this */
        //   185: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.maxYRotFromCenter:I
        //   188: i2f            
        //   189: fcmpl          
        //   190: ifle            207
        //   193: aload_0         /* this */
        //   194: getfield        net/minecraft/world/entity/ai/control/DolphinLookControl.mob:Lnet/minecraft/world/entity/Mob;
        //   197: dup            
        //   198: getfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   201: ldc             4.0
        //   203: fadd           
        //   204: putfield        net/minecraft/world/entity/Mob.yBodyRot:F
        //   207: return         
        //    StackMapTable: 00 05 FB 00 49 21 1C FC 00 2E 02 F9 00 17
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1047)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryArguments(TypeAnalysis.java:2800)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferBinaryExpression(TypeAnalysis.java:2195)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1119)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
