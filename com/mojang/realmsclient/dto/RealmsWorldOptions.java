package com.mojang.realmsclient.dto;

import java.util.Objects;
import net.minecraft.client.resources.language.I18n;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;

public class RealmsWorldOptions extends ValueObject {
    public Boolean pvp;
    public Boolean spawnAnimals;
    public Boolean spawnMonsters;
    public Boolean spawnNPCs;
    public Integer spawnProtection;
    public Boolean commandBlocks;
    public Boolean forceGameMode;
    public Integer difficulty;
    public Integer gameMode;
    public String slotName;
    public long templateId;
    public String templateImage;
    public boolean adventureMap;
    public boolean empty;
    private static final String DEFAULT_TEMPLATE_IMAGE;
    
    public RealmsWorldOptions(final Boolean boolean1, final Boolean boolean2, final Boolean boolean3, final Boolean boolean4, final Integer integer5, final Boolean boolean6, final Integer integer7, final Integer integer8, final Boolean boolean9, final String string) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   com/mojang/realmsclient/dto/ValueObject.<init>:()V
        //     4: aload_0         /* this */
        //     5: aload_1         /* boolean1 */
        //     6: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.pvp:Ljava/lang/Boolean;
        //     9: aload_0         /* this */
        //    10: aload_2         /* boolean2 */
        //    11: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.spawnAnimals:Ljava/lang/Boolean;
        //    14: aload_0         /* this */
        //    15: aload_3         /* boolean3 */
        //    16: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.spawnMonsters:Ljava/lang/Boolean;
        //    19: aload_0         /* this */
        //    20: aload           boolean4
        //    22: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.spawnNPCs:Ljava/lang/Boolean;
        //    25: aload_0         /* this */
        //    26: aload           integer5
        //    28: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.spawnProtection:Ljava/lang/Integer;
        //    31: aload_0         /* this */
        //    32: aload           boolean6
        //    34: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.commandBlocks:Ljava/lang/Boolean;
        //    37: aload_0         /* this */
        //    38: aload           integer7
        //    40: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.difficulty:Ljava/lang/Integer;
        //    43: aload_0         /* this */
        //    44: aload           integer8
        //    46: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.gameMode:Ljava/lang/Integer;
        //    49: aload_0         /* this */
        //    50: aload           boolean9
        //    52: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.forceGameMode:Ljava/lang/Boolean;
        //    55: aload_0         /* this */
        //    56: aload           string
        //    58: putfield        com/mojang/realmsclient/dto/RealmsWorldOptions.slotName:Ljava/lang/String;
        //    61: return         
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  boolean1  
        //  boolean2  
        //  boolean3  
        //  boolean4  
        //  integer5  
        //  boolean6  
        //  integer7  
        //  integer8  
        //  boolean9  
        //  string    
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 12
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.shouldInferVariableType(TypeAnalysis.java:613)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:918)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
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
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
    
    public static RealmsWorldOptions createDefaults() {
        return new RealmsWorldOptions(Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Boolean.valueOf(true), Integer.valueOf(0), Boolean.valueOf(false), Integer.valueOf(2), Integer.valueOf(0), Boolean.valueOf(false), "");
    }
    
    public static RealmsWorldOptions createEmptyDefaults() {
        final RealmsWorldOptions dgt1 = createDefaults();
        dgt1.setEmpty(true);
        return dgt1;
    }
    
    public void setEmpty(final boolean boolean1) {
        this.empty = boolean1;
    }
    
    public static RealmsWorldOptions parse(final JsonObject jsonObject) {
        final RealmsWorldOptions dgt2 = new RealmsWorldOptions(JsonUtils.getBooleanOr("pvp", jsonObject, true), JsonUtils.getBooleanOr("spawnAnimals", jsonObject, true), JsonUtils.getBooleanOr("spawnMonsters", jsonObject, true), JsonUtils.getBooleanOr("spawnNPCs", jsonObject, true), JsonUtils.getIntOr("spawnProtection", jsonObject, 0), JsonUtils.getBooleanOr("commandBlocks", jsonObject, false), JsonUtils.getIntOr("difficulty", jsonObject, 2), JsonUtils.getIntOr("gameMode", jsonObject, 0), JsonUtils.getBooleanOr("forceGameMode", jsonObject, false), JsonUtils.getStringOr("slotName", jsonObject, ""));
        dgt2.templateId = JsonUtils.getLongOr("worldTemplateId", jsonObject, -1L);
        dgt2.templateImage = JsonUtils.getStringOr("worldTemplateImage", jsonObject, RealmsWorldOptions.DEFAULT_TEMPLATE_IMAGE);
        dgt2.adventureMap = JsonUtils.getBooleanOr("adventureMap", jsonObject, false);
        return dgt2;
    }
    
    public String getSlotName(final int integer) {
        if (this.slotName != null && !this.slotName.isEmpty()) {
            return this.slotName;
        }
        if (this.empty) {
            return I18n.get("mco.configure.world.slot.empty");
        }
        return this.getDefaultSlotName(integer);
    }
    
    public String getDefaultSlotName(final int integer) {
        return I18n.get("mco.configure.world.slot", integer);
    }
    
    public String toJson() {
        final JsonObject jsonObject2 = new JsonObject();
        if (!this.pvp) {
            jsonObject2.addProperty("pvp", this.pvp);
        }
        if (!this.spawnAnimals) {
            jsonObject2.addProperty("spawnAnimals", this.spawnAnimals);
        }
        if (!this.spawnMonsters) {
            jsonObject2.addProperty("spawnMonsters", this.spawnMonsters);
        }
        if (!this.spawnNPCs) {
            jsonObject2.addProperty("spawnNPCs", this.spawnNPCs);
        }
        if (this.spawnProtection != 0) {
            jsonObject2.addProperty("spawnProtection", (Number)this.spawnProtection);
        }
        if (this.commandBlocks) {
            jsonObject2.addProperty("commandBlocks", this.commandBlocks);
        }
        if (this.difficulty != 2) {
            jsonObject2.addProperty("difficulty", (Number)this.difficulty);
        }
        if (this.gameMode != 0) {
            jsonObject2.addProperty("gameMode", (Number)this.gameMode);
        }
        if (this.forceGameMode) {
            jsonObject2.addProperty("forceGameMode", this.forceGameMode);
        }
        if (!Objects.equals(this.slotName, "")) {
            jsonObject2.addProperty("slotName", this.slotName);
        }
        return jsonObject2.toString();
    }
    
    public RealmsWorldOptions clone() {
        return new RealmsWorldOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficulty, this.gameMode, this.forceGameMode, this.slotName);
    }
    
    static {
        DEFAULT_TEMPLATE_IMAGE = null;
    }
}
