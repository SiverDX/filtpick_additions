package de.cadentem.filtpick_additions.mixin.bettertridents;

import de.cadentem.filtpick_additions.server.FilterHandler;
import fuzs.bettertridents.world.entity.item.LoyalItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Don't move filtered items */
@Mixin(LoyalItemEntity.class)
public abstract class LoyalItemEntityMixin extends ItemEntity {
    public LoyalItemEntityMixin(final EntityType<? extends ItemEntity> type, final Level level) {
        super(type, level);
    }

    @ModifyVariable(method = "tick", at = @At(value = "STORE"), index = 1)
    private Player filter(final Player owner) {
        if (FilterHandler.shouldFilter(owner, this)) {
            return null;
        }

        return owner;
    }
}
