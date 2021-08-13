package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import net.minecraft.resources.ResourceLocation;
import java.io.OutputStream;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.commands.Commands;
import net.minecraft.SharedConstants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import java.util.TreeSet;
import net.minecraft.world.level.GameRules;
import net.minecraft.locale.Language;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.tags.StaticTags;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.core.Registry;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class Bootstrap {
    public static final PrintStream STDOUT;
    private static boolean isBootstrapped;
    private static final Logger LOGGER;
    
    public static void bootStrap() {
        if (Bootstrap.isBootstrapped) {
            return;
        }
        Bootstrap.isBootstrapped = true;
        if (Registry.REGISTRY.keySet().isEmpty()) {
            throw new IllegalStateException("Unable to load registries");
        }
        FireBlock.bootStrap();
        ComposterBlock.bootStrap();
        if (EntityType.getKey(EntityType.PLAYER) == null) {
            throw new IllegalStateException("Failed loading EntityTypes");
        }
        PotionBrewing.bootStrap();
        EntitySelectorOptions.bootStrap();
        DispenseItemBehavior.bootStrap();
        ArgumentTypes.bootStrap();
        StaticTags.bootStrap();
        wrapStreams();
    }
    
    private static <T> void checkTranslations(final Iterable<T> iterable, final Function<T, String> function, final Set<String> set) {
        final Language ly4 = Language.getInstance();
        iterable.forEach(object -> {
            final String string5 = (String)function.apply(object);
            if (!ly4.has(string5)) {
                set.add(string5);
            }
        });
    }
    
    private static void checkGameruleTranslations(final Set<String> set) {
        final Language ly2 = Language.getInstance();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            public <T extends GameRules.Value<T>> void visit(final GameRules.Key<T> e, final GameRules.Type<T> f) {
                if (!ly2.has(e.getDescriptionId())) {
                    set.add(e.getId());
                }
            }
        });
    }
    
    public static Set<String> getMissingTranslations() {
        final Set<String> set1 = (Set<String>)new TreeSet();
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.ATTRIBUTE, (java.util.function.Function<Object, String>)Attribute::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.ENTITY_TYPE, (java.util.function.Function<Object, String>)EntityType::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.MOB_EFFECT, (java.util.function.Function<Object, String>)MobEffect::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.ITEM, (java.util.function.Function<Object, String>)Item::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.ENCHANTMENT, (java.util.function.Function<Object, String>)Enchantment::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.BLOCK, (java.util.function.Function<Object, String>)Block::getDescriptionId, set1);
        Bootstrap.checkTranslations((java.lang.Iterable<Object>)Registry.CUSTOM_STAT, (java.util.function.Function<Object, String>)(vk -> "stat." + vk.toString().replace(':', '.')), set1);
        checkGameruleTranslations(set1);
        return set1;
    }
    
    public static void validate() {
        if (!Bootstrap.isBootstrapped) {
            throw new IllegalArgumentException("Not bootstrapped");
        }
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            getMissingTranslations().forEach(string -> Bootstrap.LOGGER.error("Missing translations: " + string));
            Commands.validate();
        }
        DefaultAttributes.validate();
    }
    
    private static void wrapStreams() {
        if (Bootstrap.LOGGER.isDebugEnabled()) {
            System.setErr((PrintStream)new DebugLoggedPrintStream("STDERR", (OutputStream)System.err));
            System.setOut((PrintStream)new DebugLoggedPrintStream("STDOUT", (OutputStream)Bootstrap.STDOUT));
        }
        else {
            System.setErr((PrintStream)new LoggedPrintStream("STDERR", (OutputStream)System.err));
            System.setOut((PrintStream)new LoggedPrintStream("STDOUT", (OutputStream)Bootstrap.STDOUT));
        }
    }
    
    public static void realStdoutPrintln(final String string) {
        Bootstrap.STDOUT.println(string);
    }
    
    static {
        STDOUT = System.out;
        LOGGER = LogManager.getLogger();
    }
}
