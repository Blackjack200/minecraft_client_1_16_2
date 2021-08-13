package net.minecraft.client.gui.screens.worldselection;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.apache.commons.lang3.mutable.MutableObject;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelResource;
import java.util.stream.Stream;
import java.util.Comparator;
import java.nio.file.FileVisitOption;
import java.util.concurrent.Executor;
import net.minecraft.server.ServerResources;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import java.io.File;
import java.io.IOException;
import net.minecraft.client.gui.components.toasts.SystemToast;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.GameType;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.FileUtil;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Optional;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import java.util.OptionalLong;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.GameRules;
import net.minecraft.client.gui.components.Button;
import net.minecraft.server.packs.repository.PackRepository;
import java.nio.file.Path;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.Difficulty;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.Screen;

public class CreateWorldScreen extends Screen {
    private static final Logger LOGGER;
    private static final Component GAME_MODEL_LABEL;
    private static final Component SEED_LABEL;
    private static final Component SEED_INFO;
    private static final Component NAME_LABEL;
    private static final Component OUTPUT_DIR_INFO;
    private static final Component COMMANDS_INFO;
    private final Screen lastScreen;
    private EditBox nameEdit;
    private String resultFolder;
    private SelectedGameMode gameMode;
    @Nullable
    private SelectedGameMode oldGameMode;
    private Difficulty selectedDifficulty;
    private Difficulty effectiveDifficulty;
    private boolean commands;
    private boolean commandsChanged;
    public boolean hardCore;
    protected DataPackConfig dataPacks;
    @Nullable
    private Path tempDataPackDir;
    @Nullable
    private PackRepository tempDataPackRepository;
    private boolean displayOptions;
    private Button createButton;
    private Button modeButton;
    private Button difficultyButton;
    private Button moreOptionsButton;
    private Button gameRulesButton;
    private Button dataPacksButton;
    private Button commandsButton;
    private Component gameModeHelp1;
    private Component gameModeHelp2;
    private String initName;
    private GameRules gameRules;
    public final WorldGenSettingsComponent worldGenSettingsComponent;
    
    public CreateWorldScreen(@Nullable final Screen doq, final LevelSettings brx, final WorldGenSettings cht, @Nullable final Path path, final DataPackConfig brh, final RegistryAccess.RegistryHolder b) {
        this(doq, brh, new WorldGenSettingsComponent(b, cht, WorldPreset.of(cht), OptionalLong.of(cht.seed())));
        this.initName = brx.levelName();
        this.commands = brx.allowCommands();
        this.commandsChanged = true;
        this.selectedDifficulty = brx.difficulty();
        this.effectiveDifficulty = this.selectedDifficulty;
        this.gameRules.assignFrom(brx.gameRules(), null);
        if (brx.hardcore()) {
            this.gameMode = SelectedGameMode.HARDCORE;
        }
        else if (brx.gameType().isSurvival()) {
            this.gameMode = SelectedGameMode.SURVIVAL;
        }
        else if (brx.gameType().isCreative()) {
            this.gameMode = SelectedGameMode.CREATIVE;
        }
        this.tempDataPackDir = path;
    }
    
    public static CreateWorldScreen create(@Nullable final Screen doq) {
        final RegistryAccess.RegistryHolder b2 = RegistryAccess.builtin();
        return new CreateWorldScreen(doq, DataPackConfig.DEFAULT, new WorldGenSettingsComponent(b2, WorldGenSettings.makeDefault(b2.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY), b2.registryOrThrow(Registry.BIOME_REGISTRY), b2.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY)), (Optional<WorldPreset>)Optional.of(WorldPreset.NORMAL), OptionalLong.empty()));
    }
    
    private CreateWorldScreen(@Nullable final Screen doq, final DataPackConfig brh, final WorldGenSettingsComponent dsc) {
        super(new TranslatableComponent("selectWorld.create"));
        this.gameMode = SelectedGameMode.SURVIVAL;
        this.selectedDifficulty = Difficulty.NORMAL;
        this.effectiveDifficulty = Difficulty.NORMAL;
        this.gameRules = new GameRules();
        this.lastScreen = doq;
        this.initName = I18n.get("selectWorld.newWorld");
        this.dataPacks = brh;
        this.worldGenSettingsComponent = dsc;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.worldGenSettingsComponent.tick();
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        (this.nameEdit = new EditBox(this.font, this.width / 2 - 100, 60, 200, 20, new TranslatableComponent("selectWorld.enterName")) {
            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(". ").append(new TranslatableComponent("selectWorld.resultFolder")).append(" ").append(CreateWorldScreen.this.resultFolder);
            }
        }).setValue(this.initName);
        this.nameEdit.setResponder((Consumer<String>)(string -> {
            this.initName = string;
            this.createButton.active = !this.nameEdit.getValue().isEmpty();
            this.updateResultFolder();
        }));
        this.children.add(this.nameEdit);
        final int integer2 = this.width / 2 - 155;
        final int integer3 = this.width / 2 + 5;
        this.modeButton = this.addButton(new Button(integer2, 100, 150, 20, TextComponent.EMPTY, dlg -> {
            switch (this.gameMode) {
                case SURVIVAL: {
                    this.setGameMode(SelectedGameMode.HARDCORE);
                    break;
                }
                case HARDCORE: {
                    this.setGameMode(SelectedGameMode.CREATIVE);
                    break;
                }
                case CREATIVE: {
                    this.setGameMode(SelectedGameMode.SURVIVAL);
                    break;
                }
            }
            dlg.queueNarration(250);
            return;
        }) {
            @Override
            public Component getMessage() {
                return new TranslatableComponent("options.generic_value", new Object[] { CreateWorldScreen.GAME_MODEL_LABEL, new TranslatableComponent("selectWorld.gameMode." + CreateWorldScreen.this.gameMode.name) });
            }
            
            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(". ").append(CreateWorldScreen.this.gameModeHelp1).append(" ").append(CreateWorldScreen.this.gameModeHelp2);
            }
        });
        this.difficultyButton = this.addButton(new Button(integer3, 100, 150, 20, new TranslatableComponent("options.difficulty"), dlg -> {
            this.selectedDifficulty = this.selectedDifficulty.nextById();
            this.effectiveDifficulty = this.selectedDifficulty;
            dlg.queueNarration(250);
            return;
        }) {
            @Override
            public Component getMessage() {
                return new TranslatableComponent("options.difficulty").append(": ").append(CreateWorldScreen.this.effectiveDifficulty.getDisplayName());
            }
        });
        this.commandsButton = this.addButton(new Button(integer2, 151, 150, 20, new TranslatableComponent("selectWorld.allowCommands"), dlg -> {
            this.commandsChanged = true;
            this.commands = !this.commands;
            dlg.queueNarration(250);
            return;
        }) {
            @Override
            public Component getMessage() {
                return CommonComponents.optionStatus(super.getMessage(), CreateWorldScreen.this.commands && !CreateWorldScreen.this.hardCore);
            }
            
            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(". ").append(new TranslatableComponent("selectWorld.allowCommands.info"));
            }
        });
        this.dataPacksButton = this.<Button>addButton(new Button(integer3, 151, 150, 20, new TranslatableComponent("selectWorld.dataPacks"), dlg -> this.openDataPackSelectionScreen()));
        this.gameRulesButton = this.<Button>addButton(new Button(integer2, 185, 150, 20, new TranslatableComponent("selectWorld.gameRules"), dlg -> this.minecraft.setScreen(new EditGameRulesScreen(this.gameRules.copy(), (Consumer<Optional<GameRules>>)(optional -> {
            this.minecraft.setScreen(this);
            optional.ifPresent(brq -> this.gameRules = brq);
        })))));
        this.worldGenSettingsComponent.init(this, this.minecraft, this.font);
        this.moreOptionsButton = this.<Button>addButton(new Button(integer3, 185, 150, 20, new TranslatableComponent("selectWorld.moreWorldOptions"), dlg -> this.toggleDisplayOptions()));
        this.createButton = this.<Button>addButton(new Button(integer2, this.height - 28, 150, 20, new TranslatableComponent("selectWorld.create"), dlg -> this.onCreate()));
        this.createButton.active = !this.initName.isEmpty();
        this.<Button>addButton(new Button(integer3, this.height - 28, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.popScreen()));
        this.updateDisplayOptions();
        this.setInitialFocus(this.nameEdit);
        this.setGameMode(this.gameMode);
        this.updateResultFolder();
    }
    
    private void updateGameModeHelp() {
        this.gameModeHelp1 = new TranslatableComponent("selectWorld.gameMode." + this.gameMode.name + ".line1");
        this.gameModeHelp2 = new TranslatableComponent("selectWorld.gameMode." + this.gameMode.name + ".line2");
    }
    
    private void updateResultFolder() {
        this.resultFolder = this.nameEdit.getValue().trim();
        if (this.resultFolder.isEmpty()) {
            this.resultFolder = "World";
        }
        try {
            this.resultFolder = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), this.resultFolder, "");
        }
        catch (Exception exception4) {
            this.resultFolder = "World";
            try {
                this.resultFolder = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), this.resultFolder, "");
            }
            catch (Exception exception3) {
                throw new RuntimeException("Could not create save folder", (Throwable)exception3);
            }
        }
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void onCreate() {
        this.minecraft.forceSetScreen(new GenericDirtMessageScreen(new TranslatableComponent("createWorld.preparing")));
        if (!this.copyTempDataPackDirToNewWorld()) {
            return;
        }
        this.cleanupTempResources();
        final WorldGenSettings cht2 = this.worldGenSettingsComponent.makeSettings(this.hardCore);
        LevelSettings brx3;
        if (cht2.isDebug()) {
            final GameRules brq4 = new GameRules();
            brq4.<GameRules.BooleanValue>getRule(GameRules.RULE_DAYLIGHT).set(false, null);
            brx3 = new LevelSettings(this.nameEdit.getValue().trim(), GameType.SPECTATOR, false, Difficulty.PEACEFUL, true, brq4, DataPackConfig.DEFAULT);
        }
        else {
            brx3 = new LevelSettings(this.nameEdit.getValue().trim(), this.gameMode.gameType, this.hardCore, this.effectiveDifficulty, this.commands && !this.hardCore, this.gameRules, this.dataPacks);
        }
        this.minecraft.createLevel(this.resultFolder, brx3, this.worldGenSettingsComponent.registryHolder(), cht2);
    }
    
    private void toggleDisplayOptions() {
        this.setDisplayOptions(!this.displayOptions);
    }
    
    private void setGameMode(final SelectedGameMode b) {
        if (!this.commandsChanged) {
            this.commands = (b == SelectedGameMode.CREATIVE);
        }
        if (b == SelectedGameMode.HARDCORE) {
            this.hardCore = true;
            this.commandsButton.active = false;
            this.worldGenSettingsComponent.bonusItemsButton.active = false;
            this.effectiveDifficulty = Difficulty.HARD;
            this.difficultyButton.active = false;
        }
        else {
            this.hardCore = false;
            this.commandsButton.active = true;
            this.worldGenSettingsComponent.bonusItemsButton.active = true;
            this.effectiveDifficulty = this.selectedDifficulty;
            this.difficultyButton.active = true;
        }
        this.gameMode = b;
        this.updateGameModeHelp();
    }
    
    public void updateDisplayOptions() {
        this.setDisplayOptions(this.displayOptions);
    }
    
    private void setDisplayOptions(final boolean boolean1) {
        this.displayOptions = boolean1;
        this.modeButton.visible = !this.displayOptions;
        this.difficultyButton.visible = !this.displayOptions;
        if (this.worldGenSettingsComponent.isDebug()) {
            this.dataPacksButton.visible = false;
            this.modeButton.active = false;
            if (this.oldGameMode == null) {
                this.oldGameMode = this.gameMode;
            }
            this.setGameMode(SelectedGameMode.DEBUG);
            this.commandsButton.visible = false;
        }
        else {
            this.modeButton.active = true;
            if (this.oldGameMode != null) {
                this.setGameMode(this.oldGameMode);
            }
            this.commandsButton.visible = !this.displayOptions;
            this.dataPacksButton.visible = !this.displayOptions;
        }
        this.worldGenSettingsComponent.setDisplayOptions(this.displayOptions);
        this.nameEdit.setVisible(!this.displayOptions);
        if (this.displayOptions) {
            this.moreOptionsButton.setMessage(CommonComponents.GUI_DONE);
        }
        else {
            this.moreOptionsButton.setMessage(new TranslatableComponent("selectWorld.moreWorldOptions"));
        }
        this.gameRulesButton.visible = !this.displayOptions;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 257 || integer1 == 335) {
            this.onCreate();
            return true;
        }
        return false;
    }
    
    @Override
    public void onClose() {
        if (this.displayOptions) {
            this.setDisplayOptions(false);
        }
        else {
            this.popScreen();
        }
    }
    
    public void popScreen() {
        this.minecraft.setScreen(this.lastScreen);
        this.cleanupTempResources();
    }
    
    private void cleanupTempResources() {
        if (this.tempDataPackRepository != null) {
            this.tempDataPackRepository.close();
        }
        this.removeTempDataPackDir();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, -1);
        if (this.displayOptions) {
            GuiComponent.drawString(dfj, this.font, CreateWorldScreen.SEED_LABEL, this.width / 2 - 100, 47, -6250336);
            GuiComponent.drawString(dfj, this.font, CreateWorldScreen.SEED_INFO, this.width / 2 - 100, 85, -6250336);
            this.worldGenSettingsComponent.render(dfj, integer2, integer3, float4);
        }
        else {
            GuiComponent.drawString(dfj, this.font, CreateWorldScreen.NAME_LABEL, this.width / 2 - 100, 47, -6250336);
            GuiComponent.drawString(dfj, this.font, new TextComponent("").append(CreateWorldScreen.OUTPUT_DIR_INFO).append(" ").append(this.resultFolder), this.width / 2 - 100, 85, -6250336);
            this.nameEdit.render(dfj, integer2, integer3, float4);
            GuiComponent.drawString(dfj, this.font, this.gameModeHelp1, this.width / 2 - 150, 122, -6250336);
            GuiComponent.drawString(dfj, this.font, this.gameModeHelp2, this.width / 2 - 150, 134, -6250336);
            if (this.commandsButton.visible) {
                GuiComponent.drawString(dfj, this.font, CreateWorldScreen.COMMANDS_INFO, this.width / 2 - 150, 172, -6250336);
            }
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    protected <T extends GuiEventListener> T addWidget(final T dmf) {
        return super.<T>addWidget(dmf);
    }
    
    @Override
    protected <T extends AbstractWidget> T addButton(final T dle) {
        return super.<T>addButton(dle);
    }
    
    @Nullable
    protected Path getTempDataPackDir() {
        if (this.tempDataPackDir == null) {
            try {
                this.tempDataPackDir = Files.createTempDirectory("mcworld-", new FileAttribute[0]);
            }
            catch (IOException iOException2) {
                CreateWorldScreen.LOGGER.warn("Failed to create temporary dir", (Throwable)iOException2);
                SystemToast.onPackCopyFailure(this.minecraft, this.resultFolder);
                this.popScreen();
            }
        }
        return this.tempDataPackDir;
    }
    
    private void openDataPackSelectionScreen() {
        final Pair<File, PackRepository> pair2 = this.getDataPackSelectionSettings();
        if (pair2 != null) {
            this.minecraft.setScreen(new PackSelectionScreen(this, (PackRepository)pair2.getSecond(), (Consumer<PackRepository>)this::tryApplyNewDataPacks, (File)pair2.getFirst(), new TranslatableComponent("dataPack.title")));
        }
    }
    
    private void tryApplyNewDataPacks(final PackRepository abu) {
        final List<String> list3 = (List<String>)ImmutableList.copyOf((Collection)abu.getSelectedIds());
        final List<String> list4 = (List<String>)abu.getAvailableIds().stream().filter(string -> !list3.contains(string)).collect(ImmutableList.toImmutableList());
        final DataPackConfig brh5 = new DataPackConfig(list3, list4);
        if (list3.equals(this.dataPacks.getEnabled())) {
            this.dataPacks = brh5;
            return;
        }
        this.minecraft.tell(() -> this.minecraft.setScreen(new GenericDirtMessageScreen(new TranslatableComponent("dataPack.validation.working"))));
        ServerResources.loadResources(abu.openAllSelected(), Commands.CommandSelection.INTEGRATED, 2, Util.backgroundExecutor(), (Executor)this.minecraft).handle((vz, throwable) -> {
            if (throwable != null) {
                CreateWorldScreen.LOGGER.warn("Failed to validate datapack", throwable);
                this.minecraft.tell(() -> this.minecraft.setScreen(new ConfirmScreen(boolean1 -> {
                    if (boolean1) {
                        this.openDataPackSelectionScreen();
                    }
                    else {
                        this.dataPacks = DataPackConfig.DEFAULT;
                        this.minecraft.setScreen(this);
                    }
                }, new TranslatableComponent("dataPack.validation.failed"), TextComponent.EMPTY, new TranslatableComponent("dataPack.validation.back"), new TranslatableComponent("dataPack.validation.reset"))));
            }
            else {
                this.minecraft.tell(() -> {
                    this.dataPacks = brh5;
                    this.worldGenSettingsComponent.updateDataPacks(vz);
                    vz.close();
                    this.minecraft.setScreen(this);
                });
            }
            return null;
        });
    }
    
    private void removeTempDataPackDir() {
        if (this.tempDataPackDir != null) {
            try (final Stream<Path> stream2 = (Stream<Path>)Files.walk(this.tempDataPackDir, new FileVisitOption[0])) {
                stream2.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    }
                    catch (IOException iOException2) {
                        CreateWorldScreen.LOGGER.warn("Failed to remove temporary file {}", path, iOException2);
                    }
                });
            }
            catch (IOException iOException2) {
                CreateWorldScreen.LOGGER.warn("Failed to list temporary dir {}", this.tempDataPackDir);
            }
            this.tempDataPackDir = null;
        }
    }
    
    private static void copyBetweenDirs(final Path path1, final Path path2, final Path path3) {
        try {
            Util.copyBetweenDirs(path1, path2, path3);
        }
        catch (IOException iOException4) {
            CreateWorldScreen.LOGGER.warn("Failed to copy datapack file from {} to {}", path3, path2);
            throw new OperationFailedException((Throwable)iOException4);
        }
    }
    
    private boolean copyTempDataPackDirToNewWorld() {
        if (this.tempDataPackDir != null) {
            try (final LevelStorageSource.LevelStorageAccess a2 = this.minecraft.getLevelSource().createAccess(this.resultFolder);
                 final Stream<Path> stream4 = (Stream<Path>)Files.walk(this.tempDataPackDir, new FileVisitOption[0])) {
                final Path path6 = a2.getLevelPath(LevelResource.DATAPACK_DIR);
                Files.createDirectories(path6, new FileAttribute[0]);
                stream4.filter(path -> !path.equals(this.tempDataPackDir)).forEach(path2 -> copyBetweenDirs(this.tempDataPackDir, path6, path2));
            }
            catch (IOException | OperationFailedException ex2) {
                final Exception ex;
                final Exception exception2 = ex;
                CreateWorldScreen.LOGGER.warn("Failed to copy datapacks to world {}", this.resultFolder, exception2);
                SystemToast.onPackCopyFailure(this.minecraft, this.resultFolder);
                this.popScreen();
                return false;
            }
        }
        return true;
    }
    
    @Nullable
    public static Path createTempDataPackDirFromExistingWorld(final Path path, final Minecraft djw) {
        final MutableObject<Path> mutableObject3 = (MutableObject<Path>)new MutableObject();
        try (final Stream<Path> stream4 = (Stream<Path>)Files.walk(path, new FileVisitOption[0])) {
            stream4.filter(path2 -> !path2.equals(path)).forEach(path3 -> {
                Path path4 = (Path)mutableObject3.getValue();
                if (path4 == null) {
                    try {
                        path4 = Files.createTempDirectory("mcworld-", new FileAttribute[0]);
                    }
                    catch (IOException iOException5) {
                        CreateWorldScreen.LOGGER.warn("Failed to create temporary dir");
                        throw new OperationFailedException((Throwable)iOException5);
                    }
                    mutableObject3.setValue(path4);
                }
                copyBetweenDirs(path, path4, path3);
            });
        }
        catch (IOException | OperationFailedException ex2) {
            final Exception ex;
            final Exception exception4 = ex;
            CreateWorldScreen.LOGGER.warn("Failed to copy datapacks from world {}", path, exception4);
            SystemToast.onPackCopyFailure(djw, path.toString());
            return null;
        }
        return (Path)mutableObject3.getValue();
    }
    
    @Nullable
    private Pair<File, PackRepository> getDataPackSelectionSettings() {
        final Path path2 = this.getTempDataPackDir();
        if (path2 != null) {
            final File file3 = path2.toFile();
            if (this.tempDataPackRepository == null) {
                (this.tempDataPackRepository = new PackRepository(new RepositorySource[] { new ServerPacksSource(), new FolderRepositorySource(file3, PackSource.DEFAULT) })).reload();
            }
            this.tempDataPackRepository.setSelected((Collection<String>)this.dataPacks.getEnabled());
            return (Pair<File, PackRepository>)Pair.of(file3, this.tempDataPackRepository);
        }
        return null;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GAME_MODEL_LABEL = new TranslatableComponent("selectWorld.gameMode");
        SEED_LABEL = new TranslatableComponent("selectWorld.enterSeed");
        SEED_INFO = new TranslatableComponent("selectWorld.seedInfo");
        NAME_LABEL = new TranslatableComponent("selectWorld.enterName");
        OUTPUT_DIR_INFO = new TranslatableComponent("selectWorld.resultFolder");
        COMMANDS_INFO = new TranslatableComponent("selectWorld.allowCommands.info");
    }
    
    enum SelectedGameMode {
        SURVIVAL("survival", GameType.SURVIVAL), 
        HARDCORE("hardcore", GameType.SURVIVAL), 
        CREATIVE("creative", GameType.CREATIVE), 
        DEBUG("spectator", GameType.SPECTATOR);
        
        private final String name;
        private final GameType gameType;
        
        private SelectedGameMode(final String string3, final GameType brr) {
            this.name = string3;
            this.gameType = brr;
        }
    }
    
    static class OperationFailedException extends RuntimeException {
        public OperationFailedException(final Throwable throwable) {
            super(throwable);
        }
    }
}
