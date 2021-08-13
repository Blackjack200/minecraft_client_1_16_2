package net.minecraft.client.gui.components;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.gui.chat.NarratorChatListener;
import com.mojang.brigadier.Message;
import net.minecraft.world.phys.Vec2;
import net.minecraft.client.renderer.Rect2i;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.context.ParsedArgument;
import javax.annotation.Nullable;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.ComponentUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.regex.Matcher;
import com.google.common.base.Strings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.StringReader;
import java.util.Collection;
import java.util.Locale;
import java.util.Iterator;
import com.mojang.brigadier.suggestion.Suggestion;
import net.minecraft.util.Mth;
import java.util.function.BiFunction;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.ParseResults;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.network.chat.Style;
import java.util.regex.Pattern;

public class CommandSuggestions {
    private static final Pattern WHITESPACE_PATTERN;
    private static final Style UNPARSED_STYLE;
    private static final Style LITERAL_STYLE;
    private static final List<Style> ARGUMENT_STYLES;
    private final Minecraft minecraft;
    private final Screen screen;
    private final EditBox input;
    private final Font font;
    private final boolean commandsOnly;
    private final boolean onlyShowIfCursorPastError;
    private final int lineStartOffset;
    private final int suggestionLineLimit;
    private final boolean anchorToBottom;
    private final int fillColor;
    private final List<FormattedCharSequence> commandUsage;
    private int commandUsagePosition;
    private int commandUsageWidth;
    private ParseResults<SharedSuggestionProvider> currentParse;
    private CompletableFuture<Suggestions> pendingSuggestions;
    private SuggestionsList suggestions;
    private boolean allowSuggestions;
    private boolean keepSuggestions;
    
    public CommandSuggestions(final Minecraft djw, final Screen doq, final EditBox dln, final Font dkr, final boolean boolean5, final boolean boolean6, final int integer7, final int integer8, final boolean boolean9, final int integer10) {
        this.commandUsage = (List<FormattedCharSequence>)Lists.newArrayList();
        this.minecraft = djw;
        this.screen = doq;
        this.input = dln;
        this.font = dkr;
        this.commandsOnly = boolean5;
        this.onlyShowIfCursorPastError = boolean6;
        this.lineStartOffset = integer7;
        this.suggestionLineLimit = integer8;
        this.anchorToBottom = boolean9;
        this.fillColor = integer10;
        dln.setFormatter((BiFunction<String, Integer, FormattedCharSequence>)this::formatChat);
    }
    
    public void setAllowSuggestions(final boolean boolean1) {
        if (!(this.allowSuggestions = boolean1)) {
            this.suggestions = null;
        }
    }
    
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.suggestions != null && this.suggestions.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (this.screen.getFocused() == this.input && integer1 == 258) {
            this.showSuggestions(true);
            return true;
        }
        return false;
    }
    
    public boolean mouseScrolled(final double double1) {
        return this.suggestions != null && this.suggestions.mouseScrolled(Mth.clamp(double1, -1.0, 1.0));
    }
    
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        return this.suggestions != null && this.suggestions.mouseClicked((int)double1, (int)double2, integer);
    }
    
    public void showSuggestions(final boolean boolean1) {
        if (this.pendingSuggestions != null && this.pendingSuggestions.isDone()) {
            final Suggestions suggestions3 = (Suggestions)this.pendingSuggestions.join();
            if (!suggestions3.isEmpty()) {
                int integer4 = 0;
                for (final Suggestion suggestion6 : suggestions3.getList()) {
                    integer4 = Math.max(integer4, this.font.width(suggestion6.getText()));
                }
                final int integer5 = Mth.clamp(this.input.getScreenX(suggestions3.getRange().getStart()), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - integer4);
                final int integer6 = this.anchorToBottom ? (this.screen.height - 12) : 72;
                this.suggestions = new SuggestionsList(integer5, integer6, integer4, (List)this.sortSuggestions(suggestions3), boolean1);
            }
        }
    }
    
    private List<Suggestion> sortSuggestions(final Suggestions suggestions) {
        final String string3 = this.input.getValue().substring(0, this.input.getCursorPosition());
        final int integer4 = getLastWordIndex(string3);
        final String string4 = string3.substring(integer4).toLowerCase(Locale.ROOT);
        final List<Suggestion> list6 = (List<Suggestion>)Lists.newArrayList();
        final List<Suggestion> list7 = (List<Suggestion>)Lists.newArrayList();
        for (final Suggestion suggestion9 : suggestions.getList()) {
            if (suggestion9.getText().startsWith(string4) || suggestion9.getText().startsWith("minecraft:" + string4)) {
                list6.add(suggestion9);
            }
            else {
                list7.add(suggestion9);
            }
        }
        list6.addAll((Collection)list7);
        return list6;
    }
    
    public void updateCommandInfo() {
        final String string2 = this.input.getValue();
        if (this.currentParse != null && !this.currentParse.getReader().getString().equals(string2)) {
            this.currentParse = null;
        }
        if (!this.keepSuggestions) {
            this.input.setSuggestion(null);
            this.suggestions = null;
        }
        this.commandUsage.clear();
        final StringReader stringReader3 = new StringReader(string2);
        final boolean boolean4 = stringReader3.canRead() && stringReader3.peek() == '/';
        if (boolean4) {
            stringReader3.skip();
        }
        final boolean boolean5 = this.commandsOnly || boolean4;
        final int integer6 = this.input.getCursorPosition();
        if (boolean5) {
            final CommandDispatcher<SharedSuggestionProvider> commandDispatcher7 = this.minecraft.player.connection.getCommands();
            if (this.currentParse == null) {
                this.currentParse = (ParseResults<SharedSuggestionProvider>)commandDispatcher7.parse(stringReader3, this.minecraft.player.connection.getSuggestionsProvider());
            }
            final int integer7 = this.onlyShowIfCursorPastError ? stringReader3.getCursor() : 1;
            if (integer6 >= integer7 && (this.suggestions == null || !this.keepSuggestions)) {
                (this.pendingSuggestions = (CompletableFuture<Suggestions>)commandDispatcher7.getCompletionSuggestions((ParseResults)this.currentParse, integer6)).thenRun(() -> {
                    if (!this.pendingSuggestions.isDone()) {
                        return;
                    }
                    this.updateUsageInfo();
                });
            }
        }
        else {
            final String string3 = string2.substring(0, integer6);
            final int integer7 = getLastWordIndex(string3);
            final Collection<String> collection9 = this.minecraft.player.connection.getSuggestionsProvider().getOnlinePlayerNames();
            this.pendingSuggestions = SharedSuggestionProvider.suggest((Iterable<String>)collection9, new SuggestionsBuilder(string3, integer7));
        }
    }
    
    private static int getLastWordIndex(final String string) {
        if (Strings.isNullOrEmpty(string)) {
            return 0;
        }
        int integer2 = 0;
        final Matcher matcher3 = CommandSuggestions.WHITESPACE_PATTERN.matcher((CharSequence)string);
        while (matcher3.find()) {
            integer2 = matcher3.end();
        }
        return integer2;
    }
    
    private static FormattedCharSequence getExceptionMessage(final CommandSyntaxException commandSyntaxException) {
        final Component nr2 = ComponentUtils.fromMessage(commandSyntaxException.getRawMessage());
        final String string3 = commandSyntaxException.getContext();
        if (string3 == null) {
            return nr2.getVisualOrderText();
        }
        return new TranslatableComponent("command.context.parse_error", new Object[] { nr2, commandSyntaxException.getCursor(), string3 }).getVisualOrderText();
    }
    
    private void updateUsageInfo() {
        if (this.input.getCursorPosition() == this.input.getValue().length()) {
            if (((Suggestions)this.pendingSuggestions.join()).isEmpty() && !this.currentParse.getExceptions().isEmpty()) {
                int integer2 = 0;
                for (final Map.Entry<CommandNode<SharedSuggestionProvider>, CommandSyntaxException> entry4 : this.currentParse.getExceptions().entrySet()) {
                    final CommandSyntaxException commandSyntaxException5 = (CommandSyntaxException)entry4.getValue();
                    if (commandSyntaxException5.getType() == CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect()) {
                        ++integer2;
                    }
                    else {
                        this.commandUsage.add(getExceptionMessage(commandSyntaxException5));
                    }
                }
                if (integer2 > 0) {
                    this.commandUsage.add(getExceptionMessage(CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create()));
                }
            }
            else if (this.currentParse.getReader().canRead()) {
                this.commandUsage.add(getExceptionMessage(Commands.<SharedSuggestionProvider>getParseException(this.currentParse)));
            }
        }
        this.commandUsagePosition = 0;
        this.commandUsageWidth = this.screen.width;
        if (this.commandUsage.isEmpty()) {
            this.fillNodeUsage(ChatFormatting.GRAY);
        }
        this.suggestions = null;
        if (this.allowSuggestions && this.minecraft.options.autoSuggestions) {
            this.showSuggestions(false);
        }
    }
    
    private void fillNodeUsage(final ChatFormatting k) {
        final CommandContextBuilder<SharedSuggestionProvider> commandContextBuilder3 = (CommandContextBuilder<SharedSuggestionProvider>)this.currentParse.getContext();
        final SuggestionContext<SharedSuggestionProvider> suggestionContext4 = (SuggestionContext<SharedSuggestionProvider>)commandContextBuilder3.findSuggestionContext(this.input.getCursorPosition());
        final Map<CommandNode<SharedSuggestionProvider>, String> map5 = (Map<CommandNode<SharedSuggestionProvider>, String>)this.minecraft.player.connection.getCommands().getSmartUsage(suggestionContext4.parent, this.minecraft.player.connection.getSuggestionsProvider());
        final List<FormattedCharSequence> list6 = (List<FormattedCharSequence>)Lists.newArrayList();
        int integer7 = 0;
        final Style ob8 = Style.EMPTY.withColor(k);
        for (final Map.Entry<CommandNode<SharedSuggestionProvider>, String> entry10 : map5.entrySet()) {
            if (!(entry10.getKey() instanceof LiteralCommandNode)) {
                list6.add(FormattedCharSequence.forward((String)entry10.getValue(), ob8));
                integer7 = Math.max(integer7, this.font.width((String)entry10.getValue()));
            }
        }
        if (!list6.isEmpty()) {
            this.commandUsage.addAll((Collection)list6);
            this.commandUsagePosition = Mth.clamp(this.input.getScreenX(suggestionContext4.startPos), 0, this.input.getScreenX(0) + this.input.getInnerWidth() - integer7);
            this.commandUsageWidth = integer7;
        }
    }
    
    private FormattedCharSequence formatChat(final String string, final int integer) {
        if (this.currentParse != null) {
            return formatText(this.currentParse, string, integer);
        }
        return FormattedCharSequence.forward(string, Style.EMPTY);
    }
    
    @Nullable
    private static String calculateSuggestionSuffix(final String string1, final String string2) {
        if (string2.startsWith(string1)) {
            return string2.substring(string1.length());
        }
        return null;
    }
    
    private static FormattedCharSequence formatText(final ParseResults<SharedSuggestionProvider> parseResults, final String string, final int integer) {
        final List<FormattedCharSequence> list4 = (List<FormattedCharSequence>)Lists.newArrayList();
        int integer2 = 0;
        int integer3 = -1;
        final CommandContextBuilder<SharedSuggestionProvider> commandContextBuilder7 = (CommandContextBuilder<SharedSuggestionProvider>)parseResults.getContext().getLastChild();
        for (final ParsedArgument<SharedSuggestionProvider, ?> parsedArgument9 : commandContextBuilder7.getArguments().values()) {
            if (++integer3 >= CommandSuggestions.ARGUMENT_STYLES.size()) {
                integer3 = 0;
            }
            final int integer4 = Math.max(parsedArgument9.getRange().getStart() - integer, 0);
            if (integer4 >= string.length()) {
                break;
            }
            final int integer5 = Math.min(parsedArgument9.getRange().getEnd() - integer, string.length());
            if (integer5 <= 0) {
                continue;
            }
            list4.add(FormattedCharSequence.forward(string.substring(integer2, integer4), CommandSuggestions.LITERAL_STYLE));
            list4.add(FormattedCharSequence.forward(string.substring(integer4, integer5), (Style)CommandSuggestions.ARGUMENT_STYLES.get(integer3)));
            integer2 = integer5;
        }
        if (parseResults.getReader().canRead()) {
            final int integer6 = Math.max(parseResults.getReader().getCursor() - integer, 0);
            if (integer6 < string.length()) {
                final int integer7 = Math.min(integer6 + parseResults.getReader().getRemainingLength(), string.length());
                list4.add(FormattedCharSequence.forward(string.substring(integer2, integer6), CommandSuggestions.LITERAL_STYLE));
                list4.add(FormattedCharSequence.forward(string.substring(integer6, integer7), CommandSuggestions.UNPARSED_STYLE));
                integer2 = integer7;
            }
        }
        list4.add(FormattedCharSequence.forward(string.substring(integer2), CommandSuggestions.LITERAL_STYLE));
        return FormattedCharSequence.composite(list4);
    }
    
    public void render(final PoseStack dfj, final int integer2, final int integer3) {
        if (this.suggestions != null) {
            this.suggestions.render(dfj, integer2, integer3);
        }
        else {
            int integer4 = 0;
            for (final FormattedCharSequence aex7 : this.commandUsage) {
                final int integer5 = this.anchorToBottom ? (this.screen.height - 14 - 13 - 12 * integer4) : (72 + 12 * integer4);
                GuiComponent.fill(dfj, this.commandUsagePosition - 1, integer5, this.commandUsagePosition + this.commandUsageWidth + 1, integer5 + 12, this.fillColor);
                this.font.drawShadow(dfj, aex7, (float)this.commandUsagePosition, (float)(integer5 + 2), -1);
                ++integer4;
            }
        }
    }
    
    public String getNarrationMessage() {
        if (this.suggestions != null) {
            return "\n" + this.suggestions.getNarrationMessage();
        }
        return "";
    }
    
    static {
        WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
        UNPARSED_STYLE = Style.EMPTY.withColor(ChatFormatting.RED);
        LITERAL_STYLE = Style.EMPTY.withColor(ChatFormatting.GRAY);
        ARGUMENT_STYLES = (List)Stream.of((Object[])new ChatFormatting[] { ChatFormatting.AQUA, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD }).map(Style.EMPTY::withColor).collect(ImmutableList.toImmutableList());
    }
    
    public class SuggestionsList {
        private final Rect2i rect;
        private final String originalContents;
        private final List<Suggestion> suggestionList;
        private int offset;
        private int current;
        private Vec2 lastMouse;
        private boolean tabCycles;
        private int lastNarratedEntry;
        
        private SuggestionsList(final int integer2, final int integer3, final int integer4, final List<Suggestion> list, final boolean boolean6) {
            this.lastMouse = Vec2.ZERO;
            final int integer5 = integer2 - 1;
            final int integer6 = CommandSuggestions.this.anchorToBottom ? (integer3 - 3 - Math.min(list.size(), CommandSuggestions.this.suggestionLineLimit) * 12) : integer3;
            this.rect = new Rect2i(integer5, integer6, integer4 + 1, Math.min(list.size(), CommandSuggestions.this.suggestionLineLimit) * 12);
            this.originalContents = CommandSuggestions.this.input.getValue();
            this.lastNarratedEntry = (boolean6 ? -1 : 0);
            this.suggestionList = list;
            this.select(0);
        }
        
        public void render(final PoseStack dfj, final int integer2, final int integer3) {
            final int integer4 = Math.min(this.suggestionList.size(), CommandSuggestions.this.suggestionLineLimit);
            final int integer5 = -5592406;
            final boolean boolean7 = this.offset > 0;
            final boolean boolean8 = this.suggestionList.size() > this.offset + integer4;
            final boolean boolean9 = boolean7 || boolean8;
            final boolean boolean10 = this.lastMouse.x != integer2 || this.lastMouse.y != integer3;
            if (boolean10) {
                this.lastMouse = new Vec2((float)integer2, (float)integer3);
            }
            if (boolean9) {
                GuiComponent.fill(dfj, this.rect.getX(), this.rect.getY() - 1, this.rect.getX() + this.rect.getWidth(), this.rect.getY(), CommandSuggestions.this.fillColor);
                GuiComponent.fill(dfj, this.rect.getX(), this.rect.getY() + this.rect.getHeight(), this.rect.getX() + this.rect.getWidth(), this.rect.getY() + this.rect.getHeight() + 1, CommandSuggestions.this.fillColor);
                if (boolean7) {
                    for (int integer6 = 0; integer6 < this.rect.getWidth(); ++integer6) {
                        if (integer6 % 2 == 0) {
                            GuiComponent.fill(dfj, this.rect.getX() + integer6, this.rect.getY() - 1, this.rect.getX() + integer6 + 1, this.rect.getY(), -1);
                        }
                    }
                }
                if (boolean8) {
                    for (int integer6 = 0; integer6 < this.rect.getWidth(); ++integer6) {
                        if (integer6 % 2 == 0) {
                            GuiComponent.fill(dfj, this.rect.getX() + integer6, this.rect.getY() + this.rect.getHeight(), this.rect.getX() + integer6 + 1, this.rect.getY() + this.rect.getHeight() + 1, -1);
                        }
                    }
                }
            }
            boolean boolean11 = false;
            for (int integer7 = 0; integer7 < integer4; ++integer7) {
                final Suggestion suggestion13 = (Suggestion)this.suggestionList.get(integer7 + this.offset);
                GuiComponent.fill(dfj, this.rect.getX(), this.rect.getY() + 12 * integer7, this.rect.getX() + this.rect.getWidth(), this.rect.getY() + 12 * integer7 + 12, CommandSuggestions.this.fillColor);
                if (integer2 > this.rect.getX() && integer2 < this.rect.getX() + this.rect.getWidth() && integer3 > this.rect.getY() + 12 * integer7 && integer3 < this.rect.getY() + 12 * integer7 + 12) {
                    if (boolean10) {
                        this.select(integer7 + this.offset);
                    }
                    boolean11 = true;
                }
                CommandSuggestions.this.font.drawShadow(dfj, suggestion13.getText(), (float)(this.rect.getX() + 1), (float)(this.rect.getY() + 2 + 12 * integer7), (integer7 + this.offset == this.current) ? -256 : -5592406);
            }
            if (boolean11) {
                final Message message12 = ((Suggestion)this.suggestionList.get(this.current)).getTooltip();
                if (message12 != null) {
                    CommandSuggestions.this.screen.renderTooltip(dfj, ComponentUtils.fromMessage(message12), integer2, integer3);
                }
            }
        }
        
        public boolean mouseClicked(final int integer1, final int integer2, final int integer3) {
            if (!this.rect.contains(integer1, integer2)) {
                return false;
            }
            final int integer4 = (integer2 - this.rect.getY()) / 12 + this.offset;
            if (integer4 >= 0 && integer4 < this.suggestionList.size()) {
                this.select(integer4);
                this.useSuggestion();
            }
            return true;
        }
        
        public boolean mouseScrolled(final double double1) {
            final int integer4 = (int)(CommandSuggestions.this.minecraft.mouseHandler.xpos() * CommandSuggestions.this.minecraft.getWindow().getGuiScaledWidth() / CommandSuggestions.this.minecraft.getWindow().getScreenWidth());
            final int integer5 = (int)(CommandSuggestions.this.minecraft.mouseHandler.ypos() * CommandSuggestions.this.minecraft.getWindow().getGuiScaledHeight() / CommandSuggestions.this.minecraft.getWindow().getScreenHeight());
            if (this.rect.contains(integer4, integer5)) {
                this.offset = Mth.clamp((int)(this.offset - double1), 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
                return true;
            }
            return false;
        }
        
        public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
            if (integer1 == 265) {
                this.cycle(-1);
                this.tabCycles = false;
                return true;
            }
            if (integer1 == 264) {
                this.cycle(1);
                this.tabCycles = false;
                return true;
            }
            if (integer1 == 258) {
                if (this.tabCycles) {
                    this.cycle(Screen.hasShiftDown() ? -1 : 1);
                }
                this.useSuggestion();
                return true;
            }
            if (integer1 == 256) {
                this.hide();
                return true;
            }
            return false;
        }
        
        public void cycle(final int integer) {
            this.select(this.current + integer);
            final int integer2 = this.offset;
            final int integer3 = this.offset + CommandSuggestions.this.suggestionLineLimit - 1;
            if (this.current < integer2) {
                this.offset = Mth.clamp(this.current, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
            }
            else if (this.current > integer3) {
                this.offset = Mth.clamp(this.current + CommandSuggestions.this.lineStartOffset - CommandSuggestions.this.suggestionLineLimit, 0, Math.max(this.suggestionList.size() - CommandSuggestions.this.suggestionLineLimit, 0));
            }
        }
        
        public void select(final int integer) {
            this.current = integer;
            if (this.current < 0) {
                this.current += this.suggestionList.size();
            }
            if (this.current >= this.suggestionList.size()) {
                this.current -= this.suggestionList.size();
            }
            final Suggestion suggestion3 = (Suggestion)this.suggestionList.get(this.current);
            CommandSuggestions.this.input.setSuggestion(calculateSuggestionSuffix(CommandSuggestions.this.input.getValue(), suggestion3.apply(this.originalContents)));
            if (NarratorChatListener.INSTANCE.isActive() && this.lastNarratedEntry != this.current) {
                NarratorChatListener.INSTANCE.sayNow(this.getNarrationMessage());
            }
        }
        
        public void useSuggestion() {
            final Suggestion suggestion2 = (Suggestion)this.suggestionList.get(this.current);
            CommandSuggestions.this.keepSuggestions = true;
            CommandSuggestions.this.input.setValue(suggestion2.apply(this.originalContents));
            final int integer3 = suggestion2.getRange().getStart() + suggestion2.getText().length();
            CommandSuggestions.this.input.setCursorPosition(integer3);
            CommandSuggestions.this.input.setHighlightPos(integer3);
            this.select(this.current);
            CommandSuggestions.this.keepSuggestions = false;
            this.tabCycles = true;
        }
        
        private String getNarrationMessage() {
            this.lastNarratedEntry = this.current;
            final Suggestion suggestion2 = (Suggestion)this.suggestionList.get(this.current);
            final Message message3 = suggestion2.getTooltip();
            if (message3 != null) {
                return I18n.get("narration.suggestion.tooltip", this.current + 1, this.suggestionList.size(), suggestion2.getText(), message3.getString());
            }
            return I18n.get("narration.suggestion", this.current + 1, this.suggestionList.size(), suggestion2.getText());
        }
        
        public void hide() {
            CommandSuggestions.this.suggestions = null;
        }
    }
}
