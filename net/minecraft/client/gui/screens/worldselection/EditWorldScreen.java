package net.minecraft.client.gui.screens.worldselection;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.io.FileUtils;
import net.minecraft.Util;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.LinkOption;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import com.google.gson.stream.JsonWriter;
import java.nio.file.Path;
import com.google.gson.JsonIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import net.minecraft.world.level.storage.LevelResource;
import com.google.gson.JsonElement;
import java.util.concurrent.ExecutionException;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import com.mojang.serialization.DynamicOps;
import net.minecraft.resources.RegistryWriteOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.datafixers.util.Function4;
import net.minecraft.world.level.DataPackConfig;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.TextComponent;
import java.io.IOException;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.client.gui.components.EditBox;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.Screen;

public class EditWorldScreen extends Screen {
    private static final Logger LOGGER;
    private static final Gson WORLD_GEN_SETTINGS_GSON;
    private static final Component NAME_LABEL;
    private Button renameButton;
    private final BooleanConsumer callback;
    private EditBox nameEdit;
    private final LevelStorageSource.LevelStorageAccess levelAccess;
    
    public EditWorldScreen(final BooleanConsumer booleanConsumer, final LevelStorageSource.LevelStorageAccess a) {
        super(new TranslatableComponent("selectWorld.edit.title"));
        this.callback = booleanConsumer;
        this.levelAccess = a;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
    }
    
    @Override
    protected void init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.minecraft:Lnet/minecraft/client/Minecraft;
        //     4: getfield        net/minecraft/client/Minecraft.keyboardHandler:Lnet/minecraft/client/KeyboardHandler;
        //     7: iconst_1       
        //     8: invokevirtual   net/minecraft/client/KeyboardHandler.setSendRepeatsToGui:(Z)V
        //    11: aload_0         /* this */
        //    12: new             Lnet/minecraft/client/gui/components/Button;
        //    15: dup            
        //    16: aload_0         /* this */
        //    17: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //    20: iconst_2       
        //    21: idiv           
        //    22: bipush          100
        //    24: isub           
        //    25: aload_0         /* this */
        //    26: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //    29: iconst_4       
        //    30: idiv           
        //    31: iconst_0       
        //    32: iadd           
        //    33: iconst_5       
        //    34: iadd           
        //    35: sipush          200
        //    38: bipush          20
        //    40: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    43: dup            
        //    44: ldc             "selectWorld.edit.resetIcon"
        //    46: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    49: aload_0         /* this */
        //    50: invokedynamic   BootstrapMethod #0, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //    55: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //    58: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //    61: checkcast       Lnet/minecraft/client/gui/components/Button;
        //    64: astore_1        /* dlg2 */
        //    65: aload_0         /* this */
        //    66: new             Lnet/minecraft/client/gui/components/Button;
        //    69: dup            
        //    70: aload_0         /* this */
        //    71: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //    74: iconst_2       
        //    75: idiv           
        //    76: bipush          100
        //    78: isub           
        //    79: aload_0         /* this */
        //    80: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //    83: iconst_4       
        //    84: idiv           
        //    85: bipush          24
        //    87: iadd           
        //    88: iconst_5       
        //    89: iadd           
        //    90: sipush          200
        //    93: bipush          20
        //    95: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    98: dup            
        //    99: ldc             "selectWorld.edit.openFolder"
        //   101: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   104: aload_0         /* this */
        //   105: invokedynamic   BootstrapMethod #1, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   110: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   113: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   116: pop            
        //   117: aload_0         /* this */
        //   118: new             Lnet/minecraft/client/gui/components/Button;
        //   121: dup            
        //   122: aload_0         /* this */
        //   123: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   126: iconst_2       
        //   127: idiv           
        //   128: bipush          100
        //   130: isub           
        //   131: aload_0         /* this */
        //   132: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   135: iconst_4       
        //   136: idiv           
        //   137: bipush          48
        //   139: iadd           
        //   140: iconst_5       
        //   141: iadd           
        //   142: sipush          200
        //   145: bipush          20
        //   147: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   150: dup            
        //   151: ldc             "selectWorld.edit.backup"
        //   153: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   156: aload_0         /* this */
        //   157: invokedynamic   BootstrapMethod #2, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   162: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   165: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   168: pop            
        //   169: aload_0         /* this */
        //   170: new             Lnet/minecraft/client/gui/components/Button;
        //   173: dup            
        //   174: aload_0         /* this */
        //   175: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   178: iconst_2       
        //   179: idiv           
        //   180: bipush          100
        //   182: isub           
        //   183: aload_0         /* this */
        //   184: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   187: iconst_4       
        //   188: idiv           
        //   189: bipush          72
        //   191: iadd           
        //   192: iconst_5       
        //   193: iadd           
        //   194: sipush          200
        //   197: bipush          20
        //   199: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   202: dup            
        //   203: ldc             "selectWorld.edit.backupFolder"
        //   205: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   208: aload_0         /* this */
        //   209: invokedynamic   BootstrapMethod #3, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   214: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   217: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   220: pop            
        //   221: aload_0         /* this */
        //   222: new             Lnet/minecraft/client/gui/components/Button;
        //   225: dup            
        //   226: aload_0         /* this */
        //   227: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   230: iconst_2       
        //   231: idiv           
        //   232: bipush          100
        //   234: isub           
        //   235: aload_0         /* this */
        //   236: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   239: iconst_4       
        //   240: idiv           
        //   241: bipush          96
        //   243: iadd           
        //   244: iconst_5       
        //   245: iadd           
        //   246: sipush          200
        //   249: bipush          20
        //   251: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   254: dup            
        //   255: ldc             "selectWorld.edit.optimize"
        //   257: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   260: aload_0         /* this */
        //   261: invokedynamic   BootstrapMethod #4, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   266: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   269: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   272: pop            
        //   273: aload_0         /* this */
        //   274: new             Lnet/minecraft/client/gui/components/Button;
        //   277: dup            
        //   278: aload_0         /* this */
        //   279: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   282: iconst_2       
        //   283: idiv           
        //   284: bipush          100
        //   286: isub           
        //   287: aload_0         /* this */
        //   288: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   291: iconst_4       
        //   292: idiv           
        //   293: bipush          120
        //   295: iadd           
        //   296: iconst_5       
        //   297: iadd           
        //   298: sipush          200
        //   301: bipush          20
        //   303: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   306: dup            
        //   307: ldc             "selectWorld.edit.export_worldgen_settings"
        //   309: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   312: aload_0         /* this */
        //   313: invokedynamic   BootstrapMethod #5, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   318: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   321: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   324: pop            
        //   325: aload_0         /* this */
        //   326: aload_0         /* this */
        //   327: new             Lnet/minecraft/client/gui/components/Button;
        //   330: dup            
        //   331: aload_0         /* this */
        //   332: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   335: iconst_2       
        //   336: idiv           
        //   337: bipush          100
        //   339: isub           
        //   340: aload_0         /* this */
        //   341: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   344: iconst_4       
        //   345: idiv           
        //   346: sipush          144
        //   349: iadd           
        //   350: iconst_5       
        //   351: iadd           
        //   352: bipush          98
        //   354: bipush          20
        //   356: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   359: dup            
        //   360: ldc             "selectWorld.edit.save"
        //   362: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   365: aload_0         /* this */
        //   366: invokedynamic   BootstrapMethod #6, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   371: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   374: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   377: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   380: putfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.renameButton:Lnet/minecraft/client/gui/components/Button;
        //   383: aload_0         /* this */
        //   384: new             Lnet/minecraft/client/gui/components/Button;
        //   387: dup            
        //   388: aload_0         /* this */
        //   389: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   392: iconst_2       
        //   393: idiv           
        //   394: iconst_2       
        //   395: iadd           
        //   396: aload_0         /* this */
        //   397: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.height:I
        //   400: iconst_4       
        //   401: idiv           
        //   402: sipush          144
        //   405: iadd           
        //   406: iconst_5       
        //   407: iadd           
        //   408: bipush          98
        //   410: bipush          20
        //   412: getstatic       net/minecraft/network/chat/CommonComponents.GUI_CANCEL:Lnet/minecraft/network/chat/Component;
        //   415: aload_0         /* this */
        //   416: invokedynamic   BootstrapMethod #7, onPress:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   421: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   424: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   427: pop            
        //   428: aload_1         /* dlg2 */
        //   429: aload_0         /* this */
        //   430: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.levelAccess:Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;
        //   433: invokevirtual   net/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess.getIconFile:()Ljava/io/File;
        //   436: invokevirtual   java/io/File.isFile:()Z
        //   439: putfield        net/minecraft/client/gui/components/Button.active:Z
        //   442: aload_0         /* this */
        //   443: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.levelAccess:Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;
        //   446: invokevirtual   net/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess.getSummary:()Lnet/minecraft/world/level/storage/LevelSummary;
        //   449: astore_2        /* cye3 */
        //   450: aload_2         /* cye3 */
        //   451: ifnonnull       459
        //   454: ldc             ""
        //   456: goto            463
        //   459: aload_2         /* cye3 */
        //   460: invokevirtual   net/minecraft/world/level/storage/LevelSummary.getLevelName:()Ljava/lang/String;
        //   463: astore_3        /* string4 */
        //   464: aload_0         /* this */
        //   465: new             Lnet/minecraft/client/gui/components/EditBox;
        //   468: dup            
        //   469: aload_0         /* this */
        //   470: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.font:Lnet/minecraft/client/gui/Font;
        //   473: aload_0         /* this */
        //   474: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.width:I
        //   477: iconst_2       
        //   478: idiv           
        //   479: bipush          100
        //   481: isub           
        //   482: bipush          38
        //   484: sipush          200
        //   487: bipush          20
        //   489: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   492: dup            
        //   493: ldc             "selectWorld.enterName"
        //   495: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   498: invokespecial   net/minecraft/client/gui/components/EditBox.<init>:(Lnet/minecraft/client/gui/Font;IIIILnet/minecraft/network/chat/Component;)V
        //   501: putfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.nameEdit:Lnet/minecraft/client/gui/components/EditBox;
        //   504: aload_0         /* this */
        //   505: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.nameEdit:Lnet/minecraft/client/gui/components/EditBox;
        //   508: aload_3         /* string4 */
        //   509: invokevirtual   net/minecraft/client/gui/components/EditBox.setValue:(Ljava/lang/String;)V
        //   512: aload_0         /* this */
        //   513: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.nameEdit:Lnet/minecraft/client/gui/components/EditBox;
        //   516: aload_0         /* this */
        //   517: invokedynamic   BootstrapMethod #8, accept:(Lnet/minecraft/client/gui/screens/worldselection/EditWorldScreen;)Ljava/util/function/Consumer;
        //   522: invokevirtual   net/minecraft/client/gui/components/EditBox.setResponder:(Ljava/util/function/Consumer;)V
        //   525: aload_0         /* this */
        //   526: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.children:Ljava/util/List;
        //   529: aload_0         /* this */
        //   530: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.nameEdit:Lnet/minecraft/client/gui/components/EditBox;
        //   533: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   538: pop            
        //   539: aload_0         /* this */
        //   540: aload_0         /* this */
        //   541: getfield        net/minecraft/client/gui/screens/worldselection/EditWorldScreen.nameEdit:Lnet/minecraft/client/gui/components/EditBox;
        //   544: invokevirtual   net/minecraft/client/gui/screens/worldselection/EditWorldScreen.setInitialFocus:(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V
        //   547: return         
        //    StackMapTable: 00 02 FD 01 CB 00 07 00 D8 FF 00 03 00 01 07 00 02 00 01 07 00 DE
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Could not infer any expression.
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:374)
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
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.nameEdit.getValue();
        this.init(djw, integer2, integer3);
        this.nameEdit.setValue(string5);
    }
    
    @Override
    public void onClose() {
        this.callback.accept(false);
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void onRename() {
        try {
            this.levelAccess.renameLevel(this.nameEdit.getValue().trim());
            this.callback.accept(true);
        }
        catch (IOException iOException2) {
            EditWorldScreen.LOGGER.error("Failed to access world '{}'", this.levelAccess.getLevelId(), iOException2);
            SystemToast.onWorldAccessFailure(this.minecraft, this.levelAccess.getLevelId());
            this.callback.accept(true);
        }
    }
    
    public static void makeBackupAndShowToast(final LevelStorageSource cyd, final String string) {
        boolean boolean3 = false;
        try (final LevelStorageSource.LevelStorageAccess a4 = cyd.createAccess(string)) {
            boolean3 = true;
            makeBackupAndShowToast(a4);
        }
        catch (IOException iOException4) {
            if (!boolean3) {
                SystemToast.onWorldAccessFailure(Minecraft.getInstance(), string);
            }
            EditWorldScreen.LOGGER.warn("Failed to create backup of level {}", string, iOException4);
        }
    }
    
    public static boolean makeBackupAndShowToast(final LevelStorageSource.LevelStorageAccess a) {
        long long2 = 0L;
        IOException iOException4 = null;
        try {
            long2 = a.makeWorldBackup();
        }
        catch (IOException iOException5) {
            iOException4 = iOException5;
        }
        if (iOException4 != null) {
            final Component nr5 = new TranslatableComponent("selectWorld.edit.backupFailed");
            final Component nr6 = new TextComponent(iOException4.getMessage());
            Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.WORLD_BACKUP, nr5, nr6));
            return false;
        }
        final Component nr5 = new TranslatableComponent("selectWorld.edit.backupCreated", new Object[] { a.getLevelId() });
        final Component nr6 = new TranslatableComponent("selectWorld.edit.backupSize", new Object[] { Mth.ceil(long2 / 1048576.0) });
        Minecraft.getInstance().getToasts().addToast(new SystemToast(SystemToast.SystemToastIds.WORLD_BACKUP, nr5, nr6));
        return true;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 15, 16777215);
        GuiComponent.drawString(dfj, this.font, EditWorldScreen.NAME_LABEL, this.width / 2 - 100, 24, 10526880);
        this.nameEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        WORLD_GEN_SETTINGS_GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
        NAME_LABEL = new TranslatableComponent("selectWorld.enterName");
    }
}
