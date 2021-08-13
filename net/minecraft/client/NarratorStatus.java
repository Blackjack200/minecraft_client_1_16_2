package net.minecraft.client;

import net.minecraft.util.Mth;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public enum NarratorStatus {
    public static final NarratorStatus OFF;
    public static final NarratorStatus ALL;
    public static final NarratorStatus CHAT;
    public static final NarratorStatus SYSTEM;
    private static final NarratorStatus[] BY_ID;
    private final int id;
    private final Component name;
    
    private NarratorStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.name = new TranslatableComponent(string4);
    }
    
    public int getId() {
        return this.id;
    }
    
    public Component getName() {
        return this.name;
    }
    
    public static NarratorStatus byId(final int integer) {
        return NarratorStatus.BY_ID[Mth.positiveModulo(integer, NarratorStatus.BY_ID.length)];
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc             "OFF"
        //     6: iconst_0       
        //     7: iconst_0       
        //     8: ldc             "options.narrator.off"
        //    10: invokespecial   net/minecraft/client/NarratorStatus.<init>:(Ljava/lang/String;IILjava/lang/String;)V
        //    13: putstatic       net/minecraft/client/NarratorStatus.OFF:Lnet/minecraft/client/NarratorStatus;
        //    16: new             Lnet/minecraft/client/NarratorStatus;
        //    19: dup            
        //    20: ldc             "ALL"
        //    22: iconst_1       
        //    23: iconst_1       
        //    24: ldc             "options.narrator.all"
        //    26: invokespecial   net/minecraft/client/NarratorStatus.<init>:(Ljava/lang/String;IILjava/lang/String;)V
        //    29: putstatic       net/minecraft/client/NarratorStatus.ALL:Lnet/minecraft/client/NarratorStatus;
        //    32: new             Lnet/minecraft/client/NarratorStatus;
        //    35: dup            
        //    36: ldc             "CHAT"
        //    38: iconst_2       
        //    39: iconst_2       
        //    40: ldc             "options.narrator.chat"
        //    42: invokespecial   net/minecraft/client/NarratorStatus.<init>:(Ljava/lang/String;IILjava/lang/String;)V
        //    45: putstatic       net/minecraft/client/NarratorStatus.CHAT:Lnet/minecraft/client/NarratorStatus;
        //    48: new             Lnet/minecraft/client/NarratorStatus;
        //    51: dup            
        //    52: ldc             "SYSTEM"
        //    54: iconst_3       
        //    55: iconst_3       
        //    56: ldc             "options.narrator.system"
        //    58: invokespecial   net/minecraft/client/NarratorStatus.<init>:(Ljava/lang/String;IILjava/lang/String;)V
        //    61: putstatic       net/minecraft/client/NarratorStatus.SYSTEM:Lnet/minecraft/client/NarratorStatus;
        //    64: iconst_4       
        //    65: anewarray       Lnet/minecraft/client/NarratorStatus;
        //    68: dup            
        //    69: iconst_0       
        //    70: getstatic       net/minecraft/client/NarratorStatus.OFF:Lnet/minecraft/client/NarratorStatus;
        //    73: aastore        
        //    74: dup            
        //    75: iconst_1       
        //    76: getstatic       net/minecraft/client/NarratorStatus.ALL:Lnet/minecraft/client/NarratorStatus;
        //    79: aastore        
        //    80: dup            
        //    81: iconst_2       
        //    82: getstatic       net/minecraft/client/NarratorStatus.CHAT:Lnet/minecraft/client/NarratorStatus;
        //    85: aastore        
        //    86: dup            
        //    87: iconst_3       
        //    88: getstatic       net/minecraft/client/NarratorStatus.SYSTEM:Lnet/minecraft/client/NarratorStatus;
        //    91: aastore        
        //    92: putstatic       net/minecraft/client/NarratorStatus.$VALUES:[Lnet/minecraft/client/NarratorStatus;
        //    95: invokestatic    net/minecraft/client/NarratorStatus.values:()[Lnet/minecraft/client/NarratorStatus;
        //    98: invokestatic    java/util/Arrays.stream:([Ljava/lang/Object;)Ljava/util/stream/Stream;
        //   101: invokedynamic   BootstrapMethod #0, applyAsInt:()Ljava/util/function/ToIntFunction;
        //   106: invokestatic    java/util/Comparator.comparingInt:(Ljava/util/function/ToIntFunction;)Ljava/util/Comparator;
        //   109: invokeinterface java/util/stream/Stream.sorted:(Ljava/util/Comparator;)Ljava/util/stream/Stream;
        //   114: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/IntFunction;
        //   119: invokeinterface java/util/stream/Stream.toArray:(Ljava/util/function/IntFunction;)[Ljava/lang/Object;
        //   124: checkcast       [Lnet/minecraft/client/NarratorStatus;
        //   127: putstatic       net/minecraft/client/NarratorStatus.BY_ID:[Lnet/minecraft/client/NarratorStatus;
        //   130: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
        //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:657)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
