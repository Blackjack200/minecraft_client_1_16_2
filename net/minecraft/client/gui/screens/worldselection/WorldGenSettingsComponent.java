package net.minecraft.client.gui.screens.worldselection;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import com.mojang.serialization.Lifecycle;
import java.io.BufferedReader;
import java.util.concurrent.CompletableFuture;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonIOException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.JsonParser;
import java.util.concurrent.ExecutionException;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.TextComponent;
import java.util.concurrent.Executor;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.serialization.DataResult;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.resources.RegistryReadOps;
import com.mojang.serialization.DynamicOps;
import com.google.gson.JsonElement;
import net.minecraft.resources.RegistryWriteOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.server.ServerResources;
import org.apache.commons.lang3.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import java.util.OptionalLong;
import java.util.Optional;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.core.RegistryAccess;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.TickableWidget;

public class WorldGenSettingsComponent implements TickableWidget, Widget {
    private static final Logger LOGGER;
    private static final Component CUSTOM_WORLD_DESCRIPTION;
    private static final Component AMPLIFIED_HELP_TEXT;
    private static final Component MAP_FEATURES_INFO;
    private MultiLineLabel amplifiedWorldInfo;
    private Font font;
    private int width;
    private EditBox seedEdit;
    private Button featuresButton;
    public Button bonusItemsButton;
    private Button typeButton;
    private Button customizeTypeButton;
    private Button importSettingsButton;
    private RegistryAccess.RegistryHolder registryHolder;
    private WorldGenSettings settings;
    private Optional<WorldPreset> preset;
    private OptionalLong seed;
    
    public WorldGenSettingsComponent(final RegistryAccess.RegistryHolder b, final WorldGenSettings cht, final Optional<WorldPreset> optional, final OptionalLong optionalLong) {
        this.amplifiedWorldInfo = MultiLineLabel.EMPTY;
        this.registryHolder = b;
        this.settings = cht;
        this.preset = optional;
        this.seed = optionalLong;
    }
    
    public void init(final CreateWorldScreen drx, final Minecraft djw, final Font dkr) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_3         /* dkr */
        //     2: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.font:Lnet/minecraft/client/gui/Font;
        //     5: aload_0         /* this */
        //     6: aload_1         /* drx */
        //     7: getfield        net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.width:I
        //    10: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.width:I
        //    13: aload_0         /* this */
        //    14: new             Lnet/minecraft/client/gui/components/EditBox;
        //    17: dup            
        //    18: aload_0         /* this */
        //    19: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.font:Lnet/minecraft/client/gui/Font;
        //    22: aload_0         /* this */
        //    23: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.width:I
        //    26: iconst_2       
        //    27: idiv           
        //    28: bipush          100
        //    30: isub           
        //    31: bipush          60
        //    33: sipush          200
        //    36: bipush          20
        //    38: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    41: dup            
        //    42: ldc             "selectWorld.enterSeed"
        //    44: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    47: invokespecial   net/minecraft/client/gui/components/EditBox.<init>:(Lnet/minecraft/client/gui/Font;IIIILnet/minecraft/network/chat/Component;)V
        //    50: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.seedEdit:Lnet/minecraft/client/gui/components/EditBox;
        //    53: aload_0         /* this */
        //    54: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.seedEdit:Lnet/minecraft/client/gui/components/EditBox;
        //    57: aload_0         /* this */
        //    58: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.seed:Ljava/util/OptionalLong;
        //    61: invokestatic    net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.toString:(Ljava/util/OptionalLong;)Ljava/lang/String;
        //    64: invokevirtual   net/minecraft/client/gui/components/EditBox.setValue:(Ljava/lang/String;)V
        //    67: aload_0         /* this */
        //    68: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.seedEdit:Lnet/minecraft/client/gui/components/EditBox;
        //    71: aload_0         /* this */
        //    72: invokedynamic   BootstrapMethod #0, accept:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;)Ljava/util/function/Consumer;
        //    77: invokevirtual   net/minecraft/client/gui/components/EditBox.setResponder:(Ljava/util/function/Consumer;)V
        //    80: aload_1         /* drx */
        //    81: aload_0         /* this */
        //    82: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.seedEdit:Lnet/minecraft/client/gui/components/EditBox;
        //    85: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addWidget:(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;
        //    88: pop            
        //    89: aload_0         /* this */
        //    90: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.width:I
        //    93: iconst_2       
        //    94: idiv           
        //    95: sipush          155
        //    98: isub           
        //    99: istore          integer5
        //   101: aload_0         /* this */
        //   102: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.width:I
        //   105: iconst_2       
        //   106: idiv           
        //   107: iconst_5       
        //   108: iadd           
        //   109: istore          integer6
        //   111: aload_0         /* this */
        //   112: aload_1         /* drx */
        //   113: new             Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$1;
        //   116: dup            
        //   117: aload_0         /* this */
        //   118: iload           integer5
        //   120: bipush          100
        //   122: sipush          150
        //   125: bipush          20
        //   127: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   130: dup            
        //   131: ldc             "selectWorld.mapFeatures"
        //   133: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   136: aload_0         /* this */
        //   137: invokedynamic   BootstrapMethod #1, onPress:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   142: invokespecial   net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$1.<init>:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   145: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   148: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   151: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.featuresButton:Lnet/minecraft/client/gui/components/Button;
        //   154: aload_0         /* this */
        //   155: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.featuresButton:Lnet/minecraft/client/gui/components/Button;
        //   158: iconst_0       
        //   159: putfield        net/minecraft/client/gui/components/Button.visible:Z
        //   162: aload_0         /* this */
        //   163: aload_1         /* drx */
        //   164: new             Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$2;
        //   167: dup            
        //   168: aload_0         /* this */
        //   169: iload           integer6
        //   171: bipush          100
        //   173: sipush          150
        //   176: bipush          20
        //   178: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   181: dup            
        //   182: ldc             "selectWorld.mapType"
        //   184: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   187: aload_0         /* this */
        //   188: aload_1         /* drx */
        //   189: invokedynamic   BootstrapMethod #2, onPress:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   194: invokespecial   net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$2.<init>:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   197: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   200: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   203: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.typeButton:Lnet/minecraft/client/gui/components/Button;
        //   206: aload_0         /* this */
        //   207: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.typeButton:Lnet/minecraft/client/gui/components/Button;
        //   210: iconst_0       
        //   211: putfield        net/minecraft/client/gui/components/Button.visible:Z
        //   214: aload_0         /* this */
        //   215: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.typeButton:Lnet/minecraft/client/gui/components/Button;
        //   218: aload_0         /* this */
        //   219: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.preset:Ljava/util/Optional;
        //   222: invokevirtual   java/util/Optional.isPresent:()Z
        //   225: putfield        net/minecraft/client/gui/components/Button.active:Z
        //   228: aload_0         /* this */
        //   229: aload_1         /* drx */
        //   230: new             Lnet/minecraft/client/gui/components/Button;
        //   233: dup            
        //   234: iload           integer6
        //   236: bipush          120
        //   238: sipush          150
        //   241: bipush          20
        //   243: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   246: dup            
        //   247: ldc             "selectWorld.customizeType"
        //   249: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   252: aload_0         /* this */
        //   253: aload_2         /* djw */
        //   254: aload_1         /* drx */
        //   255: invokedynamic   BootstrapMethod #3, onPress:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   260: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   263: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   266: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   269: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.customizeTypeButton:Lnet/minecraft/client/gui/components/Button;
        //   272: aload_0         /* this */
        //   273: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.customizeTypeButton:Lnet/minecraft/client/gui/components/Button;
        //   276: iconst_0       
        //   277: putfield        net/minecraft/client/gui/components/Button.visible:Z
        //   280: aload_0         /* this */
        //   281: aload_1         /* drx */
        //   282: new             Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$3;
        //   285: dup            
        //   286: aload_0         /* this */
        //   287: iload           integer5
        //   289: sipush          151
        //   292: sipush          150
        //   295: bipush          20
        //   297: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   300: dup            
        //   301: ldc             "selectWorld.bonusItems"
        //   303: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   306: aload_0         /* this */
        //   307: invokedynamic   BootstrapMethod #4, onPress:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   312: aload_1         /* drx */
        //   313: invokespecial   net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent$3.<init>:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;)V
        //   316: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   319: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   322: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.bonusItemsButton:Lnet/minecraft/client/gui/components/Button;
        //   325: aload_0         /* this */
        //   326: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.bonusItemsButton:Lnet/minecraft/client/gui/components/Button;
        //   329: iconst_0       
        //   330: putfield        net/minecraft/client/gui/components/Button.visible:Z
        //   333: aload_0         /* this */
        //   334: aload_1         /* drx */
        //   335: new             Lnet/minecraft/client/gui/components/Button;
        //   338: dup            
        //   339: iload           integer5
        //   341: sipush          185
        //   344: sipush          150
        //   347: bipush          20
        //   349: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   352: dup            
        //   353: ldc             "selectWorld.import_worldgen_settings"
        //   355: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   358: aload_0         /* this */
        //   359: aload_1         /* drx */
        //   360: aload_2         /* djw */
        //   361: invokedynamic   BootstrapMethod #5, onPress:(Lnet/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent;Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/components/Button$OnPress;
        //   366: invokespecial   net/minecraft/client/gui/components/Button.<init>:(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)V
        //   369: invokevirtual   net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.addButton:(Lnet/minecraft/client/gui/components/AbstractWidget;)Lnet/minecraft/client/gui/components/AbstractWidget;
        //   372: checkcast       Lnet/minecraft/client/gui/components/Button;
        //   375: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.importSettingsButton:Lnet/minecraft/client/gui/components/Button;
        //   378: aload_0         /* this */
        //   379: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.importSettingsButton:Lnet/minecraft/client/gui/components/Button;
        //   382: iconst_0       
        //   383: putfield        net/minecraft/client/gui/components/Button.visible:Z
        //   386: aload_0         /* this */
        //   387: aload_3         /* dkr */
        //   388: getstatic       net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.AMPLIFIED_HELP_TEXT:Lnet/minecraft/network/chat/Component;
        //   391: aload_0         /* this */
        //   392: getfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.typeButton:Lnet/minecraft/client/gui/components/Button;
        //   395: invokevirtual   net/minecraft/client/gui/components/Button.getWidth:()I
        //   398: invokestatic    net/minecraft/client/gui/components/MultiLineLabel.create:(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/FormattedText;I)Lnet/minecraft/client/gui/components/MultiLineLabel;
        //   401: putfield        net/minecraft/client/gui/screens/worldselection/WorldGenSettingsComponent.amplifiedWorldInfo:Lnet/minecraft/client/gui/components/MultiLineLabel;
        //   404: return         
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  drx   
        //  djw   
        //  dkr   
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Unsupported node type: com.strobel.decompiler.ast.Lambda
        //     at com.strobel.decompiler.ast.Error.unsupportedNode(Error.java:32)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:612)
        //     at com.strobel.decompiler.ast.GotoRemoval.exit(GotoRemoval.java:586)
        //     at com.strobel.decompiler.ast.GotoRemoval.transformLeaveStatements(GotoRemoval.java:625)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotosCore(GotoRemoval.java:57)
        //     at com.strobel.decompiler.ast.GotoRemoval.removeGotos(GotoRemoval.java:53)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:276)
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
    
    private void importSettings(final RegistryAccess.RegistryHolder b, final WorldGenSettings cht) {
        this.registryHolder = b;
        this.settings = cht;
        this.preset = WorldPreset.of(cht);
        this.seed = OptionalLong.of(cht.seed());
        this.seedEdit.setValue(toString(this.seed));
        this.typeButton.active = this.preset.isPresent();
    }
    
    public void tick() {
        this.seedEdit.tick();
    }
    
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.featuresButton.visible) {
            this.font.drawShadow(dfj, WorldGenSettingsComponent.MAP_FEATURES_INFO, (float)(this.width / 2 - 150), 122.0f, -6250336);
        }
        this.seedEdit.render(dfj, integer2, integer3, float4);
        if (this.preset.equals(Optional.of((Object)WorldPreset.AMPLIFIED))) {
            final MultiLineLabel amplifiedWorldInfo = this.amplifiedWorldInfo;
            final int integer4 = this.typeButton.x + 2;
            final int integer5 = this.typeButton.y + 22;
            this.font.getClass();
            amplifiedWorldInfo.renderLeftAligned(dfj, integer4, integer5, 9, 10526880);
        }
    }
    
    protected void updateSettings(final WorldGenSettings cht) {
        this.settings = cht;
    }
    
    private static String toString(final OptionalLong optionalLong) {
        if (optionalLong.isPresent()) {
            return Long.toString(optionalLong.getAsLong());
        }
        return "";
    }
    
    private static OptionalLong parseLong(final String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        }
        catch (NumberFormatException ex) {
            return OptionalLong.empty();
        }
    }
    
    public WorldGenSettings makeSettings(final boolean boolean1) {
        final OptionalLong optionalLong3 = this.parseSeed();
        return this.settings.withSeed(boolean1, optionalLong3);
    }
    
    private OptionalLong parseSeed() {
        final String string2 = this.seedEdit.getValue();
        OptionalLong optionalLong3;
        if (StringUtils.isEmpty((CharSequence)string2)) {
            optionalLong3 = OptionalLong.empty();
        }
        else {
            final OptionalLong optionalLong4 = parseLong(string2);
            if (optionalLong4.isPresent() && optionalLong4.getAsLong() != 0L) {
                optionalLong3 = optionalLong4;
            }
            else {
                optionalLong3 = OptionalLong.of((long)string2.hashCode());
            }
        }
        return optionalLong3;
    }
    
    public boolean isDebug() {
        return this.settings.isDebug();
    }
    
    public void setDisplayOptions(final boolean boolean1) {
        this.typeButton.visible = boolean1;
        if (this.settings.isDebug()) {
            this.featuresButton.visible = false;
            this.bonusItemsButton.visible = false;
            this.customizeTypeButton.visible = false;
            this.importSettingsButton.visible = false;
        }
        else {
            this.featuresButton.visible = boolean1;
            this.bonusItemsButton.visible = boolean1;
            this.customizeTypeButton.visible = (boolean1 && WorldPreset.EDITORS.containsKey(this.preset));
            this.importSettingsButton.visible = boolean1;
        }
        this.seedEdit.setVisible(boolean1);
    }
    
    public RegistryAccess.RegistryHolder registryHolder() {
        return this.registryHolder;
    }
    
    void updateDataPacks(final ServerResources vz) {
        final RegistryAccess.RegistryHolder b3 = RegistryAccess.builtin();
        final RegistryWriteOps<JsonElement> vi4 = RegistryWriteOps.<JsonElement>create((com.mojang.serialization.DynamicOps<JsonElement>)JsonOps.INSTANCE, this.registryHolder);
        final RegistryReadOps<JsonElement> vh5 = RegistryReadOps.<JsonElement>create((com.mojang.serialization.DynamicOps<JsonElement>)JsonOps.INSTANCE, vz.getResourceManager(), b3);
        final DataResult<WorldGenSettings> dataResult6 = (DataResult<WorldGenSettings>)WorldGenSettings.CODEC.encodeStart((DynamicOps)vi4, this.settings).flatMap(jsonElement -> WorldGenSettings.CODEC.parse((DynamicOps)vh5, jsonElement));
        dataResult6.resultOrPartial((Consumer)Util.prefix("Error parsing worldgen settings after loading data packs: ", (Consumer<String>)WorldGenSettingsComponent.LOGGER::error)).ifPresent(cht -> {
            this.settings = cht;
            this.registryHolder = b3;
        });
    }
    
    static {
        LOGGER = LogManager.getLogger();
        CUSTOM_WORLD_DESCRIPTION = new TranslatableComponent("generator.custom");
        AMPLIFIED_HELP_TEXT = new TranslatableComponent("generator.amplified.info");
        MAP_FEATURES_INFO = new TranslatableComponent("selectWorld.mapFeatures.info");
    }
}
