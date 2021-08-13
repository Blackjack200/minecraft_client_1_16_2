package net.minecraft.server;

import java.util.concurrent.Executor;
import net.minecraft.server.packs.PackResources;
import java.util.List;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagContainer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.commands.Commands;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import java.util.concurrent.CompletableFuture;

public class ServerResources implements AutoCloseable {
    private static final CompletableFuture<Unit> DATA_RELOAD_INITIAL_TASK;
    private final ReloadableResourceManager resources;
    private final Commands commands;
    private final RecipeManager recipes;
    private final TagManager tagManager;
    private final PredicateManager predicateManager;
    private final LootTables lootTables;
    private final ServerAdvancementManager advancements;
    private final ServerFunctionLibrary functionLibrary;
    
    public ServerResources(final Commands.CommandSelection a, final int integer) {
        this.resources = new SimpleReloadableResourceManager(PackType.SERVER_DATA);
        this.recipes = new RecipeManager();
        this.tagManager = new TagManager();
        this.predicateManager = new PredicateManager();
        this.lootTables = new LootTables(this.predicateManager);
        this.advancements = new ServerAdvancementManager(this.predicateManager);
        this.commands = new Commands(a);
        this.functionLibrary = new ServerFunctionLibrary(integer, this.commands.getDispatcher());
        this.resources.registerReloadListener(this.tagManager);
        this.resources.registerReloadListener(this.predicateManager);
        this.resources.registerReloadListener(this.recipes);
        this.resources.registerReloadListener(this.lootTables);
        this.resources.registerReloadListener(this.functionLibrary);
        this.resources.registerReloadListener(this.advancements);
    }
    
    public ServerFunctionLibrary getFunctionLibrary() {
        return this.functionLibrary;
    }
    
    public PredicateManager getPredicateManager() {
        return this.predicateManager;
    }
    
    public LootTables getLootTables() {
        return this.lootTables;
    }
    
    public TagContainer getTags() {
        return this.tagManager.getTags();
    }
    
    public RecipeManager getRecipeManager() {
        return this.recipes;
    }
    
    public Commands getCommands() {
        return this.commands;
    }
    
    public ServerAdvancementManager getAdvancements() {
        return this.advancements;
    }
    
    public ResourceManager getResourceManager() {
        return this.resources;
    }
    
    public static CompletableFuture<ServerResources> loadResources(final List<PackResources> list, final Commands.CommandSelection a, final int integer, final Executor executor4, final Executor executor5) {
        final ServerResources vz6 = new ServerResources(a, integer);
        final CompletableFuture<Unit> completableFuture7 = vz6.resources.reload(executor4, executor5, list, ServerResources.DATA_RELOAD_INITIAL_TASK);
        return (CompletableFuture<ServerResources>)completableFuture7.whenComplete((afu, throwable) -> {
            if (throwable != null) {
                vz6.close();
            }
        }).thenApply(afu -> vz6);
    }
    
    public void updateGlobals() {
        this.tagManager.getTags().bindToGlobal();
    }
    
    public void close() {
        this.resources.close();
    }
    
    static {
        DATA_RELOAD_INITIAL_TASK = CompletableFuture.completedFuture(Unit.INSTANCE);
    }
}
