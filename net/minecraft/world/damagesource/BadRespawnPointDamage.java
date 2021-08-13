package net.minecraft.world.damagesource;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class BadRespawnPointDamage extends DamageSource {
    protected BadRespawnPointDamage() {
        super("badRespawnPoint");
        this.setScalesWithDifficulty();
        this.setExplosion();
    }
    
    @Override
    public Component getLocalizedDeathMessage(final LivingEntity aqj) {
        final Component nr3 = ComponentUtils.wrapInSquareBrackets(new TranslatableComponent("death.attack.badRespawnPoint.link")).withStyle((UnaryOperator<Style>)(ob -> ob.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://bugs.mojang.com/browse/MCPE-28723")).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent("MCPE-28723")))));
        return new TranslatableComponent("death.attack.badRespawnPoint.message", new Object[] { aqj.getDisplayName(), nr3 });
    }
}
