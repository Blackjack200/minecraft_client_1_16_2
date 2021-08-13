package net.minecraft.client.gui.screens.recipebook;

import net.minecraft.ChatFormatting;
import net.minecraft.stats.RecipeBook;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundRecipeBookChangeSettingsPacket;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Locale;
import net.minecraft.client.searchtree.SearchRegistry;
import javax.annotation.Nullable;
import net.minecraft.world.inventory.Slot;
import java.util.Iterator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.client.gui.components.StateSwitchingButton;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.GuiComponent;

public class RecipeBookComponent extends GuiComponent implements Widget, GuiEventListener, RecipeShownListener, PlaceRecipe<Ingredient> {
    protected static final ResourceLocation RECIPE_BOOK_LOCATION;
    private static final Component SEARCH_HINT;
    private static final Component ONLY_CRAFTABLES_TOOLTIP;
    private static final Component ALL_RECIPES_TOOLTIP;
    private int xOffset;
    private int width;
    private int height;
    protected final GhostRecipe ghostRecipe;
    private final List<RecipeBookTabButton> tabButtons;
    private RecipeBookTabButton selectedTab;
    protected StateSwitchingButton filterButton;
    protected RecipeBookMenu<?> menu;
    protected Minecraft minecraft;
    private EditBox searchBox;
    private String lastSearch;
    private ClientRecipeBook book;
    private final RecipeBookPage recipeBookPage;
    private final StackedContents stackedContents;
    private int timesInventoryChanged;
    private boolean ignoreTextInput;
    
    public RecipeBookComponent() {
        this.ghostRecipe = new GhostRecipe();
        this.tabButtons = (List<RecipeBookTabButton>)Lists.newArrayList();
        this.lastSearch = "";
        this.recipeBookPage = new RecipeBookPage();
        this.stackedContents = new StackedContents();
    }
    
    public void init(final int integer1, final int integer2, final Minecraft djw, final boolean boolean4, final RecipeBookMenu<?> bjg) {
        this.minecraft = djw;
        this.width = integer1;
        this.height = integer2;
        this.menu = bjg;
        djw.player.containerMenu = bjg;
        this.book = djw.player.getRecipeBook();
        this.timesInventoryChanged = djw.player.inventory.getTimesChanged();
        if (this.isVisible()) {
            this.initVisuals(boolean4);
        }
        djw.keyboardHandler.setSendRepeatsToGui(true);
    }
    
    public void initVisuals(final boolean boolean1) {
        this.xOffset = (boolean1 ? 0 : 86);
        final int integer3 = (this.width - 147) / 2 - this.xOffset;
        final int integer4 = (this.height - 166) / 2;
        this.stackedContents.clear();
        this.minecraft.player.inventory.fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        final String string5 = (this.searchBox != null) ? this.searchBox.getValue() : "";
        final Font font = this.minecraft.font;
        final int integer5 = integer3 + 25;
        final int integer6 = integer4 + 14;
        final int integer7 = 80;
        this.minecraft.font.getClass();
        (this.searchBox = new EditBox(font, integer5, integer6, integer7, 9 + 5, new TranslatableComponent("itemGroup.search"))).setMaxLength(50);
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue(string5);
        this.recipeBookPage.init(this.minecraft, integer3, integer4);
        this.recipeBookPage.addListener(this);
        this.filterButton = new StateSwitchingButton(integer3 + 110, integer4 + 12, 26, 16, this.book.isFiltering(this.menu));
        this.initFilterButtonTextures();
        this.tabButtons.clear();
        for (final RecipeBookCategories dkd7 : RecipeBookCategories.getCategories(this.menu.getRecipeBookType())) {
            this.tabButtons.add(new RecipeBookTabButton(dkd7));
        }
        if (this.selectedTab != null) {
            this.selectedTab = (RecipeBookTabButton)this.tabButtons.stream().filter(dro -> dro.getCategory().equals(this.selectedTab.getCategory())).findFirst().orElse(null);
        }
        if (this.selectedTab == null) {
            this.selectedTab = (RecipeBookTabButton)this.tabButtons.get(0);
        }
        this.selectedTab.setStateTriggered(true);
        this.updateCollections(false);
        this.updateTabs();
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        return false;
    }
    
    protected void initFilterButtonTextures() {
        this.filterButton.initTextureValues(152, 41, 28, 18, RecipeBookComponent.RECIPE_BOOK_LOCATION);
    }
    
    public void removed() {
        this.searchBox = null;
        this.selectedTab = null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    public int updateScreenPosition(final boolean boolean1, final int integer2, final int integer3) {
        int integer4;
        if (this.isVisible() && !boolean1) {
            integer4 = 177 + (integer2 - integer3 - 200) / 2;
        }
        else {
            integer4 = (integer2 - integer3) / 2;
        }
        return integer4;
    }
    
    public void toggleVisibility() {
        this.setVisible(!this.isVisible());
    }
    
    public boolean isVisible() {
        return this.book.isOpen(this.menu.getRecipeBookType());
    }
    
    protected void setVisible(final boolean boolean1) {
        this.book.setOpen(this.menu.getRecipeBookType(), boolean1);
        if (!boolean1) {
            this.recipeBookPage.setInvisible();
        }
        this.sendUpdateSettings();
    }
    
    public void slotClicked(@Nullable final Slot bjo) {
        if (bjo != null && bjo.index < this.menu.getSize()) {
            this.ghostRecipe.clear();
            if (this.isVisible()) {
                this.updateStackedContents();
            }
        }
    }
    
    private void updateCollections(final boolean boolean1) {
        final List<RecipeCollection> list3 = this.book.getCollection(this.selectedTab.getCategory());
        list3.forEach(drq -> drq.canCraft(this.stackedContents, this.menu.getGridWidth(), this.menu.getGridHeight(), this.book));
        final List<RecipeCollection> list4 = (List<RecipeCollection>)Lists.newArrayList((Iterable)list3);
        list4.removeIf(drq -> !drq.hasKnownRecipes());
        list4.removeIf(drq -> !drq.hasFitting());
        final String string5 = this.searchBox.getValue();
        if (!string5.isEmpty()) {
            final ObjectSet<RecipeCollection> objectSet6 = (ObjectSet<RecipeCollection>)new ObjectLinkedOpenHashSet((Collection)this.minecraft.<RecipeCollection>getSearchTree(SearchRegistry.RECIPE_COLLECTIONS).search(string5.toLowerCase(Locale.ROOT)));
            list4.removeIf(drq -> !objectSet6.contains(drq));
        }
        if (this.book.isFiltering(this.menu)) {
            list4.removeIf(drq -> !drq.hasCraftable());
        }
        this.recipeBookPage.updateCollections(list4, boolean1);
    }
    
    private void updateTabs() {
        final int integer2 = (this.width - 147) / 2 - this.xOffset - 30;
        final int integer3 = (this.height - 166) / 2 + 3;
        final int integer4 = 27;
        int integer5 = 0;
        for (final RecipeBookTabButton dro7 : this.tabButtons) {
            final RecipeBookCategories dkd8 = dro7.getCategory();
            if (dkd8 == RecipeBookCategories.CRAFTING_SEARCH || dkd8 == RecipeBookCategories.FURNACE_SEARCH) {
                dro7.visible = true;
                dro7.setPosition(integer2, integer3 + 27 * integer5++);
            }
            else {
                if (!dro7.updateVisibility(this.book)) {
                    continue;
                }
                dro7.setPosition(integer2, integer3 + 27 * integer5++);
                dro7.startAnimation(this.minecraft);
            }
        }
    }
    
    public void tick() {
        if (!this.isVisible()) {
            return;
        }
        if (this.timesInventoryChanged != this.minecraft.player.inventory.getTimesChanged()) {
            this.updateStackedContents();
            this.timesInventoryChanged = this.minecraft.player.inventory.getTimesChanged();
        }
    }
    
    private void updateStackedContents() {
        this.stackedContents.clear();
        this.minecraft.player.inventory.fillStackedContents(this.stackedContents);
        this.menu.fillCraftSlotsStackedContents(this.stackedContents);
        this.updateCollections(false);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (!this.isVisible()) {
            return;
        }
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0f, 0.0f, 100.0f);
        this.minecraft.getTextureManager().bind(RecipeBookComponent.RECIPE_BOOK_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int integer4 = (this.width - 147) / 2 - this.xOffset;
        final int integer5 = (this.height - 166) / 2;
        this.blit(dfj, integer4, integer5, 1, 1, 147, 166);
        if (!this.searchBox.isFocused() && this.searchBox.getValue().isEmpty()) {
            GuiComponent.drawString(dfj, this.minecraft.font, RecipeBookComponent.SEARCH_HINT, integer4 + 25, integer5 + 14, -1);
        }
        else {
            this.searchBox.render(dfj, integer2, integer3, float4);
        }
        for (final RecipeBookTabButton dro9 : this.tabButtons) {
            dro9.render(dfj, integer2, integer3, float4);
        }
        this.filterButton.render(dfj, integer2, integer3, float4);
        this.recipeBookPage.render(dfj, integer4, integer5, integer2, integer3, float4);
        RenderSystem.popMatrix();
    }
    
    public void renderTooltip(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        if (!this.isVisible()) {
            return;
        }
        this.recipeBookPage.renderTooltip(dfj, integer4, integer5);
        if (this.filterButton.isHovered()) {
            final Component nr7 = this.getFilterButtonTooltip();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.renderTooltip(dfj, nr7, integer4, integer5);
            }
        }
        this.renderGhostRecipeTooltip(dfj, integer2, integer3, integer4, integer5);
    }
    
    private Component getFilterButtonTooltip() {
        return this.filterButton.isStateTriggered() ? this.getRecipeFilterName() : RecipeBookComponent.ALL_RECIPES_TOOLTIP;
    }
    
    protected Component getRecipeFilterName() {
        return RecipeBookComponent.ONLY_CRAFTABLES_TOOLTIP;
    }
    
    private void renderGhostRecipeTooltip(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        ItemStack bly7 = null;
        for (int integer6 = 0; integer6 < this.ghostRecipe.size(); ++integer6) {
            final GhostRecipe.GhostIngredient a9 = this.ghostRecipe.get(integer6);
            final int integer7 = a9.getX() + integer2;
            final int integer8 = a9.getY() + integer3;
            if (integer4 >= integer7 && integer5 >= integer8 && integer4 < integer7 + 16 && integer5 < integer8 + 16) {
                bly7 = a9.getItem();
            }
        }
        if (bly7 != null && this.minecraft.screen != null) {
            this.minecraft.screen.renderComponentTooltip(dfj, this.minecraft.screen.getTooltipFromItem(bly7), integer4, integer5);
        }
    }
    
    public void renderGhostRecipe(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4, final float float5) {
        this.ghostRecipe.render(dfj, this.minecraft, integer2, integer3, boolean4, float5);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (!this.isVisible() || this.minecraft.player.isSpectator()) {
            return false;
        }
        if (this.recipeBookPage.mouseClicked(double1, double2, integer, (this.width - 147) / 2 - this.xOffset, (this.height - 166) / 2, 147, 166)) {
            final Recipe<?> bon7 = this.recipeBookPage.getLastClickedRecipe();
            final RecipeCollection drq8 = this.recipeBookPage.getLastClickedRecipeCollection();
            if (bon7 != null && drq8 != null) {
                if (!drq8.isCraftable(bon7) && this.ghostRecipe.getRecipe() == bon7) {
                    return false;
                }
                this.ghostRecipe.clear();
                this.minecraft.gameMode.handlePlaceRecipe(this.minecraft.player.containerMenu.containerId, bon7, Screen.hasShiftDown());
                if (!this.isOffsetNextToMainGUI()) {
                    this.setVisible(false);
                }
            }
            return true;
        }
        if (this.searchBox.mouseClicked(double1, double2, integer)) {
            return true;
        }
        if (this.filterButton.mouseClicked(double1, double2, integer)) {
            final boolean boolean7 = this.toggleFiltering();
            this.filterButton.setStateTriggered(boolean7);
            this.sendUpdateSettings();
            this.updateCollections(false);
            return true;
        }
        for (final RecipeBookTabButton dro8 : this.tabButtons) {
            if (dro8.mouseClicked(double1, double2, integer)) {
                if (this.selectedTab != dro8) {
                    this.selectedTab.setStateTriggered(false);
                    (this.selectedTab = dro8).setStateTriggered(true);
                    this.updateCollections(true);
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean toggleFiltering() {
        final RecipeBookType bjh2 = this.menu.getRecipeBookType();
        final boolean boolean3 = !this.book.isFiltering(bjh2);
        this.book.setFiltering(bjh2, boolean3);
        return boolean3;
    }
    
    public boolean hasClickedOutside(final double double1, final double double2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        if (!this.isVisible()) {
            return true;
        }
        final boolean boolean11 = double1 < integer3 || double2 < integer4 || double1 >= integer3 + integer5 || double2 >= integer4 + integer6;
        final boolean boolean12 = integer3 - 147 < double1 && double1 < integer3 && integer4 < double2 && double2 < integer4 + integer6;
        return boolean11 && !boolean12 && !this.selectedTab.isHovered();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        this.ignoreTextInput = false;
        if (!this.isVisible() || this.minecraft.player.isSpectator()) {
            return false;
        }
        if (integer1 == 256 && !this.isOffsetNextToMainGUI()) {
            this.setVisible(false);
            return true;
        }
        if (this.searchBox.keyPressed(integer1, integer2, integer3)) {
            this.checkSearchStringUpdate();
            return true;
        }
        if (this.searchBox.isFocused() && this.searchBox.isVisible() && integer1 != 256) {
            return true;
        }
        if (this.minecraft.options.keyChat.matches(integer1, integer2) && !this.searchBox.isFocused()) {
            this.ignoreTextInput = true;
            this.searchBox.setFocus(true);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyReleased(final int integer1, final int integer2, final int integer3) {
        this.ignoreTextInput = false;
        return super.keyReleased(integer1, integer2, integer3);
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        if (this.ignoreTextInput) {
            return false;
        }
        if (!this.isVisible() || this.minecraft.player.isSpectator()) {
            return false;
        }
        if (this.searchBox.charTyped(character, integer)) {
            this.checkSearchStringUpdate();
            return true;
        }
        return super.charTyped(character, integer);
    }
    
    @Override
    public boolean isMouseOver(final double double1, final double double2) {
        return false;
    }
    
    private void checkSearchStringUpdate() {
        final String string2 = this.searchBox.getValue().toLowerCase(Locale.ROOT);
        this.pirateSpeechForThePeople(string2);
        if (!string2.equals(this.lastSearch)) {
            this.updateCollections(false);
            this.lastSearch = string2;
        }
    }
    
    private void pirateSpeechForThePeople(final String string) {
        if ("excitedze".equals(string)) {
            final LanguageManager ekr3 = this.minecraft.getLanguageManager();
            final LanguageInfo ekq4 = ekr3.getLanguage("en_pt");
            if (ekr3.getSelected().compareTo(ekq4) == 0) {
                return;
            }
            ekr3.setSelected(ekq4);
            this.minecraft.options.languageCode = ekq4.getCode();
            this.minecraft.reloadResourcePacks();
            this.minecraft.options.save();
        }
    }
    
    private boolean isOffsetNextToMainGUI() {
        return this.xOffset == 86;
    }
    
    public void recipesUpdated() {
        this.updateTabs();
        if (this.isVisible()) {
            this.updateCollections(false);
        }
    }
    
    @Override
    public void recipesShown(final List<Recipe<?>> list) {
        for (final Recipe<?> bon4 : list) {
            this.minecraft.player.removeRecipeHighlight(bon4);
        }
    }
    
    public void setupGhostRecipe(final Recipe<?> bon, final List<Slot> list) {
        final ItemStack bly4 = bon.getResultItem();
        this.ghostRecipe.setRecipe(bon);
        this.ghostRecipe.addIngredient(Ingredient.of(bly4), ((Slot)list.get(0)).x, ((Slot)list.get(0)).y);
        this.placeRecipe(this.menu.getGridWidth(), this.menu.getGridHeight(), this.menu.getResultSlotIndex(), bon, (java.util.Iterator<Ingredient>)bon.getIngredients().iterator(), 0);
    }
    
    @Override
    public void addItemToSlot(final Iterator<Ingredient> iterator, final int integer2, final int integer3, final int integer4, final int integer5) {
        final Ingredient bok7 = (Ingredient)iterator.next();
        if (!bok7.isEmpty()) {
            final Slot bjo8 = (Slot)this.menu.slots.get(integer2);
            this.ghostRecipe.addIngredient(bok7, bjo8.x, bjo8.y);
        }
    }
    
    protected void sendUpdateSettings() {
        if (this.minecraft.getConnection() != null) {
            final RecipeBookType bjh2 = this.menu.getRecipeBookType();
            final boolean boolean3 = this.book.getBookSettings().isOpen(bjh2);
            final boolean boolean4 = this.book.getBookSettings().isFiltering(bjh2);
            this.minecraft.getConnection().send(new ServerboundRecipeBookChangeSettingsPacket(bjh2, boolean3, boolean4));
        }
    }
    
    static {
        RECIPE_BOOK_LOCATION = new ResourceLocation("textures/gui/recipe_book.png");
        SEARCH_HINT = new TranslatableComponent("gui.recipebook.search_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
        ONLY_CRAFTABLES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.craftable");
        ALL_RECIPES_TOOLTIP = new TranslatableComponent("gui.recipebook.toggleRecipes.all");
    }
}
