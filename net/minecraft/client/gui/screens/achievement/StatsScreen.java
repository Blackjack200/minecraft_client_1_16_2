package net.minecraft.client.gui.screens.achievement;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.Set;
import net.minecraft.world.item.Items;
import net.minecraft.core.Registry;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.stats.StatType;
import java.util.List;
import net.minecraft.client.resources.language.I18n;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.stats.Stats;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.client.gui.Font;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.stats.StatsCounter;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class StatsScreen extends Screen implements StatsUpdateListener {
    private static final Component PENDING_TEXT;
    protected final Screen lastScreen;
    private GeneralStatisticsList statsList;
    private ItemStatisticsList itemStatsList;
    private MobsStatisticsList mobsStatsList;
    private final StatsCounter stats;
    @Nullable
    private ObjectSelectionList<?> activeList;
    private boolean isLoading;
    
    public StatsScreen(final Screen doq, final StatsCounter adz) {
        super(new TranslatableComponent("gui.stats"));
        this.isLoading = true;
        this.lastScreen = doq;
        this.stats = adz;
    }
    
    @Override
    protected void init() {
        this.isLoading = true;
        this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
    }
    
    public void initLists() {
        this.statsList = new GeneralStatisticsList(this.minecraft);
        this.itemStatsList = new ItemStatisticsList(this.minecraft);
        this.mobsStatsList = new MobsStatisticsList(this.minecraft);
    }
    
    public void initButtons() {
        this.<Button>addButton(new Button(this.width / 2 - 120, this.height - 52, 80, 20, new TranslatableComponent("stat.generalButton"), dlg -> this.setActiveList(this.statsList)));
        final Button dlg2 = this.<Button>addButton(new Button(this.width / 2 - 40, this.height - 52, 80, 20, new TranslatableComponent("stat.itemsButton"), dlg -> this.setActiveList(this.itemStatsList)));
        final Button dlg3 = this.<Button>addButton(new Button(this.width / 2 + 40, this.height - 52, 80, 20, new TranslatableComponent("stat.mobsButton"), dlg -> this.setActiveList(this.mobsStatsList)));
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 28, 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
        if (this.itemStatsList.children().isEmpty()) {
            dlg2.active = false;
        }
        if (this.mobsStatsList.children().isEmpty()) {
            dlg3.active = false;
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.isLoading) {
            this.renderBackground(dfj);
            GuiComponent.drawCenteredString(dfj, this.font, StatsScreen.PENDING_TEXT, this.width / 2, this.height / 2, 16777215);
            final Font font = this.font;
            final String string = StatsScreen.LOADING_SYMBOLS[(int)(Util.getMillis() / 150L % StatsScreen.LOADING_SYMBOLS.length)];
            final int integer4 = this.width / 2;
            final int n = this.height / 2;
            this.font.getClass();
            GuiComponent.drawCenteredString(dfj, font, string, integer4, n + 9 * 2, 16777215);
        }
        else {
            this.getActiveList().render(dfj, integer2, integer3, float4);
            GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
            super.render(dfj, integer2, integer3, float4);
        }
    }
    
    @Override
    public void onStatsUpdated() {
        if (this.isLoading) {
            this.initLists();
            this.initButtons();
            this.setActiveList(this.statsList);
            this.isLoading = false;
        }
    }
    
    @Override
    public boolean isPauseScreen() {
        return !this.isLoading;
    }
    
    @Nullable
    public ObjectSelectionList<?> getActiveList() {
        return this.activeList;
    }
    
    public void setActiveList(@Nullable final ObjectSelectionList<?> dls) {
        this.children.remove(this.statsList);
        this.children.remove(this.itemStatsList);
        this.children.remove(this.mobsStatsList);
        if (dls != null) {
            this.children.add(0, dls);
            this.activeList = dls;
        }
    }
    
    private static String getTranslationKey(final Stat<ResourceLocation> adv) {
        return "stat." + adv.getValue().toString().replace(':', '.');
    }
    
    private int getColumnX(final int integer) {
        return 115 + 40 * integer;
    }
    
    private void blitSlot(final PoseStack dfj, final int integer2, final int integer3, final Item blu) {
        this.blitSlotIcon(dfj, integer2 + 1, integer3 + 1, 0, 0);
        RenderSystem.enableRescaleNormal();
        this.itemRenderer.renderGuiItem(blu.getDefaultInstance(), integer2 + 2, integer3 + 2);
        RenderSystem.disableRescaleNormal();
    }
    
    private void blitSlotIcon(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(StatsScreen.STATS_ICON_LOCATION);
        GuiComponent.blit(dfj, integer2, integer3, this.getBlitOffset(), (float)integer4, (float)integer5, 18, 18, 128, 128);
    }
    
    static {
        PENDING_TEXT = new TranslatableComponent("multiplayer.downloadingStats");
    }
    
    class GeneralStatisticsList extends ObjectSelectionList<Entry> {
        public GeneralStatisticsList(final Minecraft djw) {
            super(djw, StatsScreen.this.width, StatsScreen.this.height, 32, StatsScreen.this.height - 64, 10);
            final ObjectArrayList<Stat<ResourceLocation>> objectArrayList4 = (ObjectArrayList<Stat<ResourceLocation>>)new ObjectArrayList((Iterator)Stats.CUSTOM.iterator());
            objectArrayList4.sort(Comparator.comparing(adv -> I18n.get(getTranslationKey(adv))));
            for (final Stat<ResourceLocation> adv6 : objectArrayList4) {
                this.addEntry(new Entry((Stat)adv6));
            }
        }
        
        @Override
        protected void renderBackground(final PoseStack dfj) {
            StatsScreen.this.renderBackground(dfj);
        }
        
        class Entry extends ObjectSelectionList.Entry<Entry> {
            private final Stat<ResourceLocation> stat;
            private final Component statDisplay;
            
            private Entry(final Stat<ResourceLocation> adv) {
                this.stat = adv;
                this.statDisplay = new TranslatableComponent(getTranslationKey(adv));
            }
            
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                GuiComponent.drawString(dfj, StatsScreen.this.font, this.statDisplay, integer4 + 2, integer3 + 1, (integer2 % 2 == 0) ? 16777215 : 9474192);
                final String string12 = this.stat.format(StatsScreen.this.stats.getValue(this.stat));
                GuiComponent.drawString(dfj, StatsScreen.this.font, string12, integer4 + 2 + 213 - StatsScreen.this.font.width(string12), integer3 + 1, (integer2 % 2 == 0) ? 16777215 : 9474192);
            }
        }
    }
    
    class ItemStatisticsList extends ObjectSelectionList<ItemRow> {
        protected final List<StatType<Block>> blockColumns;
        protected final List<StatType<Item>> itemColumns;
        private final int[] iconOffsets;
        protected int headerPressed;
        protected final List<Item> statItemList;
        protected final Comparator<Item> itemStatSorter;
        @Nullable
        protected StatType<?> sortColumn;
        protected int sortOrder;
        
        public ItemStatisticsList(final Minecraft djw) {
            super(djw, StatsScreen.this.width, StatsScreen.this.height, 32, StatsScreen.this.height - 64, 20);
            this.iconOffsets = new int[] { 3, 4, 1, 2, 5, 6 };
            this.headerPressed = -1;
            this.itemStatSorter = (Comparator<Item>)new ItemComparator();
            (this.blockColumns = (List<StatType<Block>>)Lists.newArrayList()).add(Stats.BLOCK_MINED);
            this.itemColumns = (List<StatType<Item>>)Lists.newArrayList((Object[])new StatType[] { Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED });
            this.setRenderHeader(true, 20);
            final Set<Item> set4 = (Set<Item>)Sets.newIdentityHashSet();
            for (final Item blu6 : Registry.ITEM) {
                boolean boolean7 = false;
                for (final StatType<Item> adx9 : this.itemColumns) {
                    if (adx9.contains(blu6) && StatsScreen.this.stats.getValue(adx9.get(blu6)) > 0) {
                        boolean7 = true;
                    }
                }
                if (boolean7) {
                    set4.add(blu6);
                }
            }
            for (final Block bul6 : Registry.BLOCK) {
                boolean boolean7 = false;
                for (final StatType<Block> adx10 : this.blockColumns) {
                    if (adx10.contains(bul6) && StatsScreen.this.stats.getValue(adx10.get(bul6)) > 0) {
                        boolean7 = true;
                    }
                }
                if (boolean7) {
                    set4.add(bul6.asItem());
                }
            }
            set4.remove(Items.AIR);
            this.statItemList = (List<Item>)Lists.newArrayList((Iterable)set4);
            for (int integer5 = 0; integer5 < this.statItemList.size(); ++integer5) {
                this.addEntry(new ItemRow());
            }
        }
        
        @Override
        protected void renderHeader(final PoseStack dfj, final int integer2, final int integer3, final Tesselator dfl) {
            if (!this.minecraft.mouseHandler.isLeftPressed()) {
                this.headerPressed = -1;
            }
            for (int integer4 = 0; integer4 < this.iconOffsets.length; ++integer4) {
                StatsScreen.this.blitSlotIcon(dfj, integer2 + StatsScreen.this.getColumnX(integer4) - 18, integer3 + 1, 0, (this.headerPressed == integer4) ? 0 : 18);
            }
            if (this.sortColumn != null) {
                final int integer4 = StatsScreen.this.getColumnX(this.getColumnIndex(this.sortColumn)) - 36;
                final int integer5 = (this.sortOrder == 1) ? 2 : 1;
                StatsScreen.this.blitSlotIcon(dfj, integer2 + integer4, integer3 + 1, 18 * integer5, 0);
            }
            for (int integer4 = 0; integer4 < this.iconOffsets.length; ++integer4) {
                final int integer5 = (this.headerPressed == integer4) ? 1 : 0;
                StatsScreen.this.blitSlotIcon(dfj, integer2 + StatsScreen.this.getColumnX(integer4) - 18 + integer5, integer3 + 1 + integer5, 18 * this.iconOffsets[integer4], 18);
            }
        }
        
        @Override
        public int getRowWidth() {
            return 375;
        }
        
        @Override
        protected int getScrollbarPosition() {
            return this.width / 2 + 140;
        }
        
        @Override
        protected void renderBackground(final PoseStack dfj) {
            StatsScreen.this.renderBackground(dfj);
        }
        
        @Override
        protected void clickedHeader(final int integer1, final int integer2) {
            this.headerPressed = -1;
            for (int integer3 = 0; integer3 < this.iconOffsets.length; ++integer3) {
                final int integer4 = integer1 - StatsScreen.this.getColumnX(integer3);
                if (integer4 >= -36 && integer4 <= 0) {
                    this.headerPressed = integer3;
                    break;
                }
            }
            if (this.headerPressed >= 0) {
                this.sortByColumn(this.getColumn(this.headerPressed));
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }
        
        private StatType<?> getColumn(final int integer) {
            return ((integer < this.blockColumns.size()) ? this.blockColumns.get(integer) : ((StatType)this.itemColumns.get(integer - this.blockColumns.size())));
        }
        
        private int getColumnIndex(final StatType<?> adx) {
            final int integer3 = this.blockColumns.indexOf(adx);
            if (integer3 >= 0) {
                return integer3;
            }
            final int integer4 = this.itemColumns.indexOf(adx);
            if (integer4 >= 0) {
                return integer4 + this.blockColumns.size();
            }
            return -1;
        }
        
        @Override
        protected void renderDecorations(final PoseStack dfj, final int integer2, final int integer3) {
            if (integer3 < this.y0 || integer3 > this.y1) {
                return;
            }
            final ItemRow b5 = this.getEntryAtPosition(integer2, integer3);
            final int integer4 = (this.width - this.getRowWidth()) / 2;
            if (b5 != null) {
                if (integer2 < integer4 + 40 || integer2 > integer4 + 40 + 20) {
                    return;
                }
                final Item blu7 = (Item)this.statItemList.get(this.children().indexOf(b5));
                this.renderMousehoverTooltip(dfj, this.getString(blu7), integer2, integer3);
            }
            else {
                Component nr7 = null;
                final int integer5 = integer2 - integer4;
                for (int integer6 = 0; integer6 < this.iconOffsets.length; ++integer6) {
                    final int integer7 = StatsScreen.this.getColumnX(integer6);
                    if (integer5 >= integer7 - 18 && integer5 <= integer7) {
                        nr7 = this.getColumn(integer6).getDisplayName();
                        break;
                    }
                }
                this.renderMousehoverTooltip(dfj, nr7, integer2, integer3);
            }
        }
        
        protected void renderMousehoverTooltip(final PoseStack dfj, @Nullable final Component nr, final int integer3, final int integer4) {
            if (nr == null) {
                return;
            }
            final int integer5 = integer3 + 12;
            final int integer6 = integer4 - 12;
            final int integer7 = StatsScreen.this.font.width(nr);
            this.fillGradient(dfj, integer5 - 3, integer6 - 3, integer5 + integer7 + 3, integer6 + 8 + 3, -1073741824, -1073741824);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0f, 0.0f, 400.0f);
            StatsScreen.this.font.drawShadow(dfj, nr, (float)integer5, (float)integer6, -1);
            RenderSystem.popMatrix();
        }
        
        protected Component getString(final Item blu) {
            return blu.getDescription();
        }
        
        protected void sortByColumn(final StatType<?> adx) {
            if (adx != this.sortColumn) {
                this.sortColumn = adx;
                this.sortOrder = -1;
            }
            else if (this.sortOrder == -1) {
                this.sortOrder = 1;
            }
            else {
                this.sortColumn = null;
                this.sortOrder = 0;
            }
            this.statItemList.sort((Comparator)this.itemStatSorter);
        }
        
        class ItemComparator implements Comparator<Item> {
            private ItemComparator() {
            }
            
            public int compare(final Item blu1, final Item blu2) {
                int integer4;
                int integer5;
                if (ItemStatisticsList.this.sortColumn == null) {
                    integer4 = 0;
                    integer5 = 0;
                }
                else if (ItemStatisticsList.this.blockColumns.contains(ItemStatisticsList.this.sortColumn)) {
                    final StatType<Block> adx6 = (StatType<Block>)ItemStatisticsList.this.sortColumn;
                    integer4 = ((blu1 instanceof BlockItem) ? StatsScreen.this.stats.<Block>getValue(adx6, ((BlockItem)blu1).getBlock()) : -1);
                    integer5 = ((blu2 instanceof BlockItem) ? StatsScreen.this.stats.<Block>getValue(adx6, ((BlockItem)blu2).getBlock()) : -1);
                }
                else {
                    final StatType<Item> adx7 = (StatType<Item>)ItemStatisticsList.this.sortColumn;
                    integer4 = StatsScreen.this.stats.<Item>getValue(adx7, blu1);
                    integer5 = StatsScreen.this.stats.<Item>getValue(adx7, blu2);
                }
                if (integer4 == integer5) {
                    return ItemStatisticsList.this.sortOrder * Integer.compare(Item.getId(blu1), Item.getId(blu2));
                }
                return ItemStatisticsList.this.sortOrder * Integer.compare(integer4, integer5);
            }
        }
        
        class ItemRow extends Entry<ItemRow> {
            private ItemRow() {
            }
            
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                final Item blu12 = (Item)StatsScreen.this.itemStatsList.statItemList.get(integer2);
                StatsScreen.this.blitSlot(dfj, integer4 + 40, integer3, blu12);
                for (int integer9 = 0; integer9 < StatsScreen.this.itemStatsList.blockColumns.size(); ++integer9) {
                    Stat<Block> adv14;
                    if (blu12 instanceof BlockItem) {
                        adv14 = ((StatType)StatsScreen.this.itemStatsList.blockColumns.get(integer9)).get(((BlockItem)blu12).getBlock());
                    }
                    else {
                        adv14 = null;
                    }
                    this.renderStat(dfj, adv14, integer4 + StatsScreen.this.getColumnX(integer9), integer3, integer2 % 2 == 0);
                }
                for (int integer9 = 0; integer9 < StatsScreen.this.itemStatsList.itemColumns.size(); ++integer9) {
                    this.renderStat(dfj, ((StatType)StatsScreen.this.itemStatsList.itemColumns.get(integer9)).get(blu12), integer4 + StatsScreen.this.getColumnX(integer9 + StatsScreen.this.itemStatsList.blockColumns.size()), integer3, integer2 % 2 == 0);
                }
            }
            
            protected void renderStat(final PoseStack dfj, @Nullable final Stat<?> adv, final int integer3, final int integer4, final boolean boolean5) {
                final String string7 = (adv == null) ? "-" : adv.format(StatsScreen.this.stats.getValue(adv));
                GuiComponent.drawString(dfj, StatsScreen.this.font, string7, integer3 - StatsScreen.this.font.width(string7), integer4 + 5, boolean5 ? 16777215 : 9474192);
            }
        }
    }
    
    class MobsStatisticsList extends ObjectSelectionList<MobRow> {
        final /* synthetic */ StatsScreen this$0;
        
        public MobsStatisticsList(final Minecraft djw) {
            final int width = StatsScreen.this.width;
            final int height = StatsScreen.this.height;
            final int integer4 = 32;
            final int integer5 = StatsScreen.this.height - 64;
            StatsScreen.this.font.getClass();
            super(djw, width, height, integer4, integer5, 9 * 4);
            for (final EntityType<?> aqb5 : Registry.ENTITY_TYPE) {
                if (StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED.get(aqb5)) > 0 || StatsScreen.this.stats.getValue(Stats.ENTITY_KILLED_BY.get(aqb5)) > 0) {
                    this.addEntry(new MobRow(aqb5));
                }
            }
        }
        
        @Override
        protected void renderBackground(final PoseStack dfj) {
            StatsScreen.this.renderBackground(dfj);
        }
        
        class MobRow extends Entry<MobRow> {
            private final EntityType<?> type;
            private final Component mobName;
            private final Component kills;
            private final boolean hasKills;
            private final Component killedBy;
            private final boolean wasKilledBy;
            
            public MobRow(final EntityType<?> aqb) {
                this.type = aqb;
                this.mobName = aqb.getDescription();
                final int integer4 = MobsStatisticsList.this.this$0.stats.getValue(Stats.ENTITY_KILLED.get(aqb));
                if (integer4 == 0) {
                    this.kills = new TranslatableComponent("stat_type.minecraft.killed.none", new Object[] { this.mobName });
                    this.hasKills = false;
                }
                else {
                    this.kills = new TranslatableComponent("stat_type.minecraft.killed", new Object[] { integer4, this.mobName });
                    this.hasKills = true;
                }
                final int integer5 = MobsStatisticsList.this.this$0.stats.getValue(Stats.ENTITY_KILLED_BY.get(aqb));
                if (integer5 == 0) {
                    this.killedBy = new TranslatableComponent("stat_type.minecraft.killed_by.none", new Object[] { this.mobName });
                    this.wasKilledBy = false;
                }
                else {
                    this.killedBy = new TranslatableComponent("stat_type.minecraft.killed_by", new Object[] { this.mobName, integer5 });
                    this.wasKilledBy = true;
                }
            }
            
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                GuiComponent.drawString(dfj, StatsScreen.this.font, this.mobName, integer4 + 2, integer3 + 1, 16777215);
                final Font access$1800 = StatsScreen.this.font;
                final Component kills = this.kills;
                final int integer9 = integer4 + 2 + 10;
                final int n = integer3 + 1;
                StatsScreen.this.font.getClass();
                GuiComponent.drawString(dfj, access$1800, kills, integer9, n + 9, this.hasKills ? 9474192 : 6316128);
                final Font access$1801 = StatsScreen.this.font;
                final Component killedBy = this.killedBy;
                final int integer10 = integer4 + 2 + 10;
                final int n2 = integer3 + 1;
                StatsScreen.this.font.getClass();
                GuiComponent.drawString(dfj, access$1801, killedBy, integer10, n2 + 9 * 2, this.wasKilledBy ? 9474192 : 6316128);
            }
        }
    }
}
