package net.minecraft.realms;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;

public class DisconnectedRealmsScreen extends RealmsScreen {
    private final Component title;
    private final Component reason;
    private MultiLineLabel message;
    private final Screen parent;
    private int textHeight;
    
    public DisconnectedRealmsScreen(final Screen doq, final Component nr2, final Component nr3) {
        this.message = MultiLineLabel.EMPTY;
        this.parent = doq;
        this.title = nr2;
        this.reason = nr3;
    }
    
    public void init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_1        /* djw2 */
        //     4: aload_1         /* djw2 */
        //     5: iconst_0       
        //     6: invokevirtual   net/minecraft/client/Minecraft.setConnectedToRealms:(Z)V
        //     9: aload_1         /* djw2 */
        //    10: invokevirtual   net/minecraft/client/Minecraft.getClientPackSource:()Lnet/minecraft/client/resources/ClientPackSource;
        //    13: invokevirtual   net/minecraft/client/resources/ClientPackSource.clearServerPack:()V
        //    16: new             Ljava/lang/StringBuilder;
        //    19: dup            
        //    20: invokespecial   java/lang/StringBuilder.<init>:()V
        //    23: aload_0         /* this */
        //    24: getfield        net/minecraft/realms/DisconnectedRealmsScreen.title:Lnet/minecraft/network/chat/Component;
        //    27: invokeinterface net/minecraft/network/chat/Component.getString:()Ljava/lang/String;
        //    32: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    35: ldc             ": "
        //    37: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    40: aload_0         /* this */
        //    41: getfield        net/minecraft/realms/DisconnectedRealmsScreen.reason:Lnet/minecraft/network/chat/Component;
        //    44: invokeinterface net/minecraft/network/chat/Component.getString:()Ljava/lang/String;
        //    49: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    52: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    55: invokestatic    net/minecraft/realms/NarrationHelper.now:(Ljava/lang/String;)V
        //    58: aload_0         /* this */
        //    59: aload_0         /* this */
        //    60: getfield        net/minecraft/realms/DisconnectedRealmsScreen.font:Lnet/minecraft/client/gui/Font;
        //    63: aload_0         /* this */
        //    64: getfield        net/minecraft/realms/DisconnectedRealmsScreen.reason:Lnet/minecraft/network/chat/Component;
        //    67: aload_0         /* this */
        //    68: getfield        net/minecraft/realms/DisconnectedRealmsScreen.width:I
        //    71: bipush          50
        //    73: isub           
        //    74: invokestatic    net/minecraft/client/gui/components/MultiLineLabel.create:(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/FormattedText;I)Lnet/minecraft/client/gui/components/MultiLineLabel;
        //    77: putfield        net/minecraft/realms/DisconnectedRealmsScreen.message:Lnet/minecraft/client/gui/components/MultiLineLabel;
        //    80: aload_0         /* this */
        //    81: aload_0         /* this */
        //    82: getfield        net/minecraft/realms/DisconnectedRealmsScreen.message:Lnet/minecraft/client/gui/components/MultiLineLabel;
        //    85: invokeinterface net/minecraft/client/gui/components/MultiLineLabel.getLineCount:()I
        //    90: aload_0         /* this */
        //    91: getfield        net/minecraft/realms/DisconnectedRealmsScreen.font:Lnet/minecraft/client/gui/Font;
        //    94: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    97: pop            
        //    98: bipush          9
        //   100: imul           
        //   101: putfield        net/minecraft/realms/DisconnectedRealmsScreen.textHeight:I
        //   104: aload_0         /* this */
        //   105: new             Lnet/minecraft/client/gui/components/Button;
        //   108: dup            
        //   109: aload_0         /* this */
        //   110: getfield        net/minecraft/realms/DisconnectedRealmsScreen.width:I
        //   113: iconst_2       
        //   114: idiv           
        //   115: bipush          100
        //   117: isub           
        //   118: aload_0         /* this */
        //   119: getfield        net/minecraft/realms/DisconnectedRealmsScreen.height:I
        //   122: iconst_2       
        //   123: idiv           
        //   124: aload_0         /* this */
        //   125: getfield        net/minecraft/realms/DisconnectedRealmsScreen.textHeight:I
        //   128: iconst_2       
        //   129: idiv           
        //   130: iadd           
        //   131: aload_0         /* this */
        //   132: getfield        net/minecraft/realms/DisconnectedRealmsScreen.font:Lnet/minecraft/client/gui/Font;
        //   135: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   138: pop            
        //   139: bipush          9
        //   141: iadd           
        //   142: sipush          200
        //   145: bipush          20
        //   147: getstatic       net/minecraft/network/chat/CommonComponents.GUI_BACK:Lnet/minecraft/network/chat/Component;
        //   150: aload_0         /* this */
        //   151: aload_1         /* djw2 */
        //   152: invokedynamic   BootstrapMethod #0, onPress:(Lnet/minecraft/realms/DisconnectedRealmsScreen;Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   157: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   160: invokevirtual   net/minecraft/realms/DisconnectedRealmsScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   163: pop            
        //   164: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 20
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
        //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
        //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
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
    
    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final Font font = this.font;
        final Component title = this.title;
        final int integer4 = this.width / 2;
        final int n = this.height / 2 - this.textHeight / 2;
        this.font.getClass();
        GuiComponent.drawCenteredString(dfj, font, title, integer4, n - 9 * 2, 11184810);
        this.message.renderCentered(dfj, this.width / 2, this.height / 2 - this.textHeight / 2);
        super.render(dfj, integer2, integer3, float4);
    }
}
