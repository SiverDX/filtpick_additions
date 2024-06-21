package de.cadentem.filtpick_additions.mixin.filtpick;

import de.cadentem.filtpick_additions.server.FilterHandler;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** More performant (and better compatible) filter call (i.e. before the forge event is being called) */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At("HEAD"), cancellable = true)
    private void filtpick_additions$filter(final Player player, final CallbackInfo callback) {
        if (player.getLevel().isClientSide()) {
            return;
        }

        if (FilterHandler.shouldFilter(player, (ItemEntity) (Object) this)) {
            callback.cancel();
        }
    }
}