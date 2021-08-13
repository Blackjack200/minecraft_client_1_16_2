package net.minecraft.world.level.biome;

import net.minecraft.resources.ResourceKey;

public abstract class Biomes {
    public static final ResourceKey<Biome> OCEAN;
    public static final ResourceKey<Biome> PLAINS;
    public static final ResourceKey<Biome> DESERT;
    public static final ResourceKey<Biome> MOUNTAINS;
    public static final ResourceKey<Biome> FOREST;
    public static final ResourceKey<Biome> TAIGA;
    public static final ResourceKey<Biome> SWAMP;
    public static final ResourceKey<Biome> RIVER;
    public static final ResourceKey<Biome> NETHER_WASTES;
    public static final ResourceKey<Biome> THE_END;
    public static final ResourceKey<Biome> FROZEN_OCEAN;
    public static final ResourceKey<Biome> FROZEN_RIVER;
    public static final ResourceKey<Biome> SNOWY_TUNDRA;
    public static final ResourceKey<Biome> SNOWY_MOUNTAINS;
    public static final ResourceKey<Biome> MUSHROOM_FIELDS;
    public static final ResourceKey<Biome> MUSHROOM_FIELD_SHORE;
    public static final ResourceKey<Biome> BEACH;
    public static final ResourceKey<Biome> DESERT_HILLS;
    public static final ResourceKey<Biome> WOODED_HILLS;
    public static final ResourceKey<Biome> TAIGA_HILLS;
    public static final ResourceKey<Biome> MOUNTAIN_EDGE;
    public static final ResourceKey<Biome> JUNGLE;
    public static final ResourceKey<Biome> JUNGLE_HILLS;
    public static final ResourceKey<Biome> JUNGLE_EDGE;
    public static final ResourceKey<Biome> DEEP_OCEAN;
    public static final ResourceKey<Biome> STONE_SHORE;
    public static final ResourceKey<Biome> SNOWY_BEACH;
    public static final ResourceKey<Biome> BIRCH_FOREST;
    public static final ResourceKey<Biome> BIRCH_FOREST_HILLS;
    public static final ResourceKey<Biome> DARK_FOREST;
    public static final ResourceKey<Biome> SNOWY_TAIGA;
    public static final ResourceKey<Biome> SNOWY_TAIGA_HILLS;
    public static final ResourceKey<Biome> GIANT_TREE_TAIGA;
    public static final ResourceKey<Biome> GIANT_TREE_TAIGA_HILLS;
    public static final ResourceKey<Biome> WOODED_MOUNTAINS;
    public static final ResourceKey<Biome> SAVANNA;
    public static final ResourceKey<Biome> SAVANNA_PLATEAU;
    public static final ResourceKey<Biome> BADLANDS;
    public static final ResourceKey<Biome> WOODED_BADLANDS_PLATEAU;
    public static final ResourceKey<Biome> BADLANDS_PLATEAU;
    public static final ResourceKey<Biome> SMALL_END_ISLANDS;
    public static final ResourceKey<Biome> END_MIDLANDS;
    public static final ResourceKey<Biome> END_HIGHLANDS;
    public static final ResourceKey<Biome> END_BARRENS;
    public static final ResourceKey<Biome> WARM_OCEAN;
    public static final ResourceKey<Biome> LUKEWARM_OCEAN;
    public static final ResourceKey<Biome> COLD_OCEAN;
    public static final ResourceKey<Biome> DEEP_WARM_OCEAN;
    public static final ResourceKey<Biome> DEEP_LUKEWARM_OCEAN;
    public static final ResourceKey<Biome> DEEP_COLD_OCEAN;
    public static final ResourceKey<Biome> DEEP_FROZEN_OCEAN;
    public static final ResourceKey<Biome> THE_VOID;
    public static final ResourceKey<Biome> SUNFLOWER_PLAINS;
    public static final ResourceKey<Biome> DESERT_LAKES;
    public static final ResourceKey<Biome> GRAVELLY_MOUNTAINS;
    public static final ResourceKey<Biome> FLOWER_FOREST;
    public static final ResourceKey<Biome> TAIGA_MOUNTAINS;
    public static final ResourceKey<Biome> SWAMP_HILLS;
    public static final ResourceKey<Biome> ICE_SPIKES;
    public static final ResourceKey<Biome> MODIFIED_JUNGLE;
    public static final ResourceKey<Biome> MODIFIED_JUNGLE_EDGE;
    public static final ResourceKey<Biome> TALL_BIRCH_FOREST;
    public static final ResourceKey<Biome> TALL_BIRCH_HILLS;
    public static final ResourceKey<Biome> DARK_FOREST_HILLS;
    public static final ResourceKey<Biome> SNOWY_TAIGA_MOUNTAINS;
    public static final ResourceKey<Biome> GIANT_SPRUCE_TAIGA;
    public static final ResourceKey<Biome> GIANT_SPRUCE_TAIGA_HILLS;
    public static final ResourceKey<Biome> MODIFIED_GRAVELLY_MOUNTAINS;
    public static final ResourceKey<Biome> SHATTERED_SAVANNA;
    public static final ResourceKey<Biome> SHATTERED_SAVANNA_PLATEAU;
    public static final ResourceKey<Biome> ERODED_BADLANDS;
    public static final ResourceKey<Biome> MODIFIED_WOODED_BADLANDS_PLATEAU;
    public static final ResourceKey<Biome> MODIFIED_BADLANDS_PLATEAU;
    public static final ResourceKey<Biome> BAMBOO_JUNGLE;
    public static final ResourceKey<Biome> BAMBOO_JUNGLE_HILLS;
    public static final ResourceKey<Biome> SOUL_SAND_VALLEY;
    public static final ResourceKey<Biome> CRIMSON_FOREST;
    public static final ResourceKey<Biome> WARPED_FOREST;
    public static final ResourceKey<Biome> BASALT_DELTAS;
    
    private static ResourceKey<Biome> register(final String string) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: new             Lnet/minecraft/resources/ResourceLocation;
        //     6: dup            
        //     7: aload_0         /* string */
        //     8: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    11: invokestatic    net/minecraft/resources/ResourceKey.create:(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/resources/ResourceKey;
        //    14: areturn        
        //    Signature:
        //  (Ljava/lang/String;)Lnet/minecraft/resources/ResourceKey<Lnet/minecraft/world/level/biome/Biome;>;
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  string  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at com.strobel.assembler.metadata.WildcardType.hasExtendsBound(WildcardType.java:106)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitWildcard(TypeSubstitutionVisitor.java:101)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitWildcard(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameters(TypeSubstitutionVisitor.java:364)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:279)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2607)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
        //     at java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:401)
        //     at java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:734)
        //     at java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:160)
        //     at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:174)
        //     at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static {
        OCEAN = register("ocean");
        PLAINS = register("plains");
        DESERT = register("desert");
        MOUNTAINS = register("mountains");
        FOREST = register("forest");
        TAIGA = register("taiga");
        SWAMP = register("swamp");
        RIVER = register("river");
        NETHER_WASTES = register("nether_wastes");
        THE_END = register("the_end");
        FROZEN_OCEAN = register("frozen_ocean");
        FROZEN_RIVER = register("frozen_river");
        SNOWY_TUNDRA = register("snowy_tundra");
        SNOWY_MOUNTAINS = register("snowy_mountains");
        MUSHROOM_FIELDS = register("mushroom_fields");
        MUSHROOM_FIELD_SHORE = register("mushroom_field_shore");
        BEACH = register("beach");
        DESERT_HILLS = register("desert_hills");
        WOODED_HILLS = register("wooded_hills");
        TAIGA_HILLS = register("taiga_hills");
        MOUNTAIN_EDGE = register("mountain_edge");
        JUNGLE = register("jungle");
        JUNGLE_HILLS = register("jungle_hills");
        JUNGLE_EDGE = register("jungle_edge");
        DEEP_OCEAN = register("deep_ocean");
        STONE_SHORE = register("stone_shore");
        SNOWY_BEACH = register("snowy_beach");
        BIRCH_FOREST = register("birch_forest");
        BIRCH_FOREST_HILLS = register("birch_forest_hills");
        DARK_FOREST = register("dark_forest");
        SNOWY_TAIGA = register("snowy_taiga");
        SNOWY_TAIGA_HILLS = register("snowy_taiga_hills");
        GIANT_TREE_TAIGA = register("giant_tree_taiga");
        GIANT_TREE_TAIGA_HILLS = register("giant_tree_taiga_hills");
        WOODED_MOUNTAINS = register("wooded_mountains");
        SAVANNA = register("savanna");
        SAVANNA_PLATEAU = register("savanna_plateau");
        BADLANDS = register("badlands");
        WOODED_BADLANDS_PLATEAU = register("wooded_badlands_plateau");
        BADLANDS_PLATEAU = register("badlands_plateau");
        SMALL_END_ISLANDS = register("small_end_islands");
        END_MIDLANDS = register("end_midlands");
        END_HIGHLANDS = register("end_highlands");
        END_BARRENS = register("end_barrens");
        WARM_OCEAN = register("warm_ocean");
        LUKEWARM_OCEAN = register("lukewarm_ocean");
        COLD_OCEAN = register("cold_ocean");
        DEEP_WARM_OCEAN = register("deep_warm_ocean");
        DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean");
        DEEP_COLD_OCEAN = register("deep_cold_ocean");
        DEEP_FROZEN_OCEAN = register("deep_frozen_ocean");
        THE_VOID = register("the_void");
        SUNFLOWER_PLAINS = register("sunflower_plains");
        DESERT_LAKES = register("desert_lakes");
        GRAVELLY_MOUNTAINS = register("gravelly_mountains");
        FLOWER_FOREST = register("flower_forest");
        TAIGA_MOUNTAINS = register("taiga_mountains");
        SWAMP_HILLS = register("swamp_hills");
        ICE_SPIKES = register("ice_spikes");
        MODIFIED_JUNGLE = register("modified_jungle");
        MODIFIED_JUNGLE_EDGE = register("modified_jungle_edge");
        TALL_BIRCH_FOREST = register("tall_birch_forest");
        TALL_BIRCH_HILLS = register("tall_birch_hills");
        DARK_FOREST_HILLS = register("dark_forest_hills");
        SNOWY_TAIGA_MOUNTAINS = register("snowy_taiga_mountains");
        GIANT_SPRUCE_TAIGA = register("giant_spruce_taiga");
        GIANT_SPRUCE_TAIGA_HILLS = register("giant_spruce_taiga_hills");
        MODIFIED_GRAVELLY_MOUNTAINS = register("modified_gravelly_mountains");
        SHATTERED_SAVANNA = register("shattered_savanna");
        SHATTERED_SAVANNA_PLATEAU = register("shattered_savanna_plateau");
        ERODED_BADLANDS = register("eroded_badlands");
        MODIFIED_WOODED_BADLANDS_PLATEAU = register("modified_wooded_badlands_plateau");
        MODIFIED_BADLANDS_PLATEAU = register("modified_badlands_plateau");
        BAMBOO_JUNGLE = register("bamboo_jungle");
        BAMBOO_JUNGLE_HILLS = register("bamboo_jungle_hills");
        SOUL_SAND_VALLEY = register("soul_sand_valley");
        CRIMSON_FOREST = register("crimson_forest");
        WARPED_FOREST = register("warped_forest");
        BASALT_DELTAS = register("basalt_deltas");
    }
}
