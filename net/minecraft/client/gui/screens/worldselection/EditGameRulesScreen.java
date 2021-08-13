package net.minecraft.client.gui.screens.worldselection;

import net.minecraft.client.gui.components.AbstractSelectionList;
import java.util.Comparator;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.FormattedText;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import com.google.common.collect.Sets;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import java.util.Set;
import net.minecraft.world.level.GameRules;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.Screen;

public class EditGameRulesScreen extends Screen {
    private final Consumer<Optional<GameRules>> exitCallback;
    private RuleList rules;
    private final Set<RuleEntry> invalidEntries;
    private Button doneButton;
    @Nullable
    private List<FormattedCharSequence> tooltip;
    private final GameRules gameRules;
    
    public EditGameRulesScreen(final GameRules brq, final Consumer<Optional<GameRules>> consumer) {
        super(new TranslatableComponent("editGamerule.title"));
        this.invalidEntries = (Set<RuleEntry>)Sets.newHashSet();
        this.gameRules = brq;
        this.exitCallback = consumer;
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        super.init();
        this.rules = new RuleList(this.gameRules);
        this.children.add(this.rules);
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.exitCallback.accept(Optional.empty())));
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, CommonComponents.GUI_DONE, dlg -> this.exitCallback.accept(Optional.of((Object)this.gameRules))));
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public void onClose() {
        this.exitCallback.accept(Optional.empty());
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.tooltip = null;
        this.rules.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(dfj, integer2, integer3, float4);
        if (this.tooltip != null) {
            this.renderTooltip(dfj, this.tooltip, integer2, integer3);
        }
    }
    
    private void setTooltip(@Nullable final List<FormattedCharSequence> list) {
        this.tooltip = list;
    }
    
    private void updateDoneButton() {
        this.doneButton.active = this.invalidEntries.isEmpty();
    }
    
    private void markInvalid(final RuleEntry f) {
        this.invalidEntries.add(f);
        this.updateDoneButton();
    }
    
    private void clearInvalid(final RuleEntry f) {
        this.invalidEntries.remove(f);
        this.updateDoneButton();
    }
    
    public abstract class RuleEntry extends ContainerObjectSelectionList.Entry<RuleEntry> {
        @Nullable
        private final List<FormattedCharSequence> tooltip;
        
        public RuleEntry(@Nullable final List<FormattedCharSequence> list) {
            this.tooltip = list;
        }
    }
    
    public class CategoryRuleEntry extends RuleEntry {
        private final Component label;
        
        public CategoryRuleEntry(final Component nr) {
            super(null);
            this.label = nr;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            GuiComponent.drawCenteredString(dfj, EditGameRulesScreen.this.minecraft.font, this.label, integer4 + integer5 / 2, integer3 + 5, 16777215);
        }
        
        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of();
        }
    }
    
    public abstract class GameRuleEntry extends RuleEntry {
        private final List<FormattedCharSequence> label;
        protected final List<GuiEventListener> children;
        
        public GameRuleEntry(@Nullable final List<FormattedCharSequence> list, final Component nr) {
            super(list);
            this.children = (List<GuiEventListener>)Lists.newArrayList();
            this.label = EditGameRulesScreen.this.minecraft.font.split(nr, 175);
        }
        
        @Override
        public List<? extends GuiEventListener> children() {
            return this.children;
        }
        
        protected void renderLabel(final PoseStack dfj, final int integer2, final int integer3) {
            if (this.label.size() == 1) {
                EditGameRulesScreen.this.minecraft.font.draw(dfj, (FormattedCharSequence)this.label.get(0), (float)integer3, (float)(integer2 + 5), 16777215);
            }
            else if (this.label.size() >= 2) {
                EditGameRulesScreen.this.minecraft.font.draw(dfj, (FormattedCharSequence)this.label.get(0), (float)integer3, (float)integer2, 16777215);
                EditGameRulesScreen.this.minecraft.font.draw(dfj, (FormattedCharSequence)this.label.get(1), (float)integer3, (float)(integer2 + 10), 16777215);
            }
        }
    }
    
    public class BooleanRuleEntry extends GameRuleEntry {
        private final Button checkbox;
        
        public BooleanRuleEntry(final Component nr, final List<FormattedCharSequence> list, final String string, final GameRules.BooleanValue a) {
            super(list, nr);
            final boolean boolean3;
            this.checkbox = new Button(10, 5, 44, 20, CommonComponents.optionStatus(a.get()), dlg -> {
                boolean3 = !a.get();
                a.set(boolean3, null);
                dlg.setMessage(CommonComponents.optionStatus(a.get()));
                return;
            }) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    return CommonComponents.optionStatus(nr, a.get()).append("\n").append(string);
                }
            };
            this.children.add(this.checkbox);
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderLabel(dfj, integer3, integer4);
            this.checkbox.x = integer4 + integer5 - 45;
            this.checkbox.y = integer3;
            this.checkbox.render(dfj, integer7, integer8, float10);
        }
    }
    
    public class IntegerRuleEntry extends GameRuleEntry {
        private final EditBox input;
        
        public IntegerRuleEntry(final Component nr, final List<FormattedCharSequence> list, final String string, final GameRules.IntegerValue d) {
            super(list, nr);
            (this.input = new EditBox(EditGameRulesScreen.this.minecraft.font, 10, 5, 42, 20, nr.copy().append("\n").append(string).append("\n"))).setValue(Integer.toString(d.get()));
            this.input.setResponder((Consumer<String>)(string -> {
                if (d.tryDeserialize(string)) {
                    this.input.setTextColor(14737632);
                    EditGameRulesScreen.this.clearInvalid(this);
                }
                else {
                    this.input.setTextColor(16711680);
                    EditGameRulesScreen.this.markInvalid(this);
                }
            }));
            this.children.add(this.input);
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderLabel(dfj, integer3, integer4);
            this.input.x = integer4 + integer5 - 44;
            this.input.y = integer3;
            this.input.render(dfj, integer7, integer8, float10);
        }
    }
    
    public class RuleList extends ContainerObjectSelectionList<RuleEntry> {
        public RuleList(final GameRules brq) {
            super(EditGameRulesScreen.this.minecraft, EditGameRulesScreen.this.width, EditGameRulesScreen.this.height, 43, EditGameRulesScreen.this.height - 32, 24);
            final Map<GameRules.Category, Map<GameRules.Key<?>, RuleEntry>> map4 = (Map<GameRules.Category, Map<GameRules.Key<?>, RuleEntry>>)Maps.newHashMap();
            GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                public void visitBoolean(final GameRules.Key<GameRules.BooleanValue> e, final GameRules.Type<GameRules.BooleanValue> f) {
                    this.<GameRules.BooleanValue>addEntry(e, (nr, list, string, a) -> new BooleanRuleEntry(nr, list, string, a));
                }
                
                public void visitInteger(final GameRules.Key<GameRules.IntegerValue> e, final GameRules.Type<GameRules.IntegerValue> f) {
                    this.<GameRules.IntegerValue>addEntry(e, (nr, list, string, d) -> new IntegerRuleEntry(nr, list, string, d));
                }
                
                private <T extends GameRules.Value<T>> void addEntry(final GameRules.Key<T> e, final EntryFactory<T> c) {
                    final Component nr4 = new TranslatableComponent(e.getDescriptionId());
                    final Component nr5 = new TextComponent(e.getId()).withStyle(ChatFormatting.YELLOW);
                    final T g6 = brq.<T>getRule(e);
                    final String string7 = g6.serialize();
                    final Component nr6 = new TranslatableComponent("editGamerule.default", new Object[] { new TextComponent(string7) }).withStyle(ChatFormatting.GRAY);
                    final String string8 = e.getDescriptionId() + ".description";
                    List<FormattedCharSequence> list10;
                    String string9;
                    if (I18n.exists(string8)) {
                        final ImmutableList.Builder<FormattedCharSequence> builder12 = (ImmutableList.Builder<FormattedCharSequence>)ImmutableList.builder().add(nr5.getVisualOrderText());
                        final Component nr7 = new TranslatableComponent(string8);
                        EditGameRulesScreen.this.font.split(nr7, 150).forEach(builder12::add);
                        list10 = (List<FormattedCharSequence>)builder12.add(nr6.getVisualOrderText()).build();
                        string9 = nr7.getString() + "\n" + nr6.getString();
                    }
                    else {
                        list10 = (List<FormattedCharSequence>)ImmutableList.of(nr5.getVisualOrderText(), nr6.getVisualOrderText());
                        string9 = nr6.getString();
                    }
                    ((Map)map4.computeIfAbsent(e.getCategory(), b -> Maps.newHashMap())).put(e, c.create(nr4, list10, string9, g6));
                }
            });
            map4.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                ((AbstractSelectionList<CategoryRuleEntry>)this).addEntry(new CategoryRuleEntry(new TranslatableComponent(((GameRules.Category)entry.getKey()).getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)));
                ((Map)entry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.comparing(GameRules.Key::getId))).forEach(entry -> this.addEntry((RuleEntry)entry.getValue()));
            });
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            super.render(dfj, integer2, integer3, float4);
            if (this.isMouseOver(integer2, integer3)) {
                final RuleEntry f6 = this.getEntryAtPosition(integer2, integer3);
                if (f6 != null) {
                    EditGameRulesScreen.this.setTooltip(f6.tooltip);
                }
            }
        }
    }
    
    @FunctionalInterface
    interface EntryFactory<T extends GameRules.Value<T>> {
        RuleEntry create(final Component nr, final List<FormattedCharSequence> list, final String string, final T g);
    }
}
