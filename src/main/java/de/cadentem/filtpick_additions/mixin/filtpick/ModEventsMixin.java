package de.cadentem.filtpick_additions.mixin.filtpick;

import net.apeng.filtpick.event.ModEvents;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Disable event based filter logic */
@Mixin(value = ModEvents.class, remap = false)
public abstract class ModEventsMixin {
    @Inject(method = "itemsFilt", at = @At("HEAD"), cancellable = true)
    private static void filtpick_additions$disable(final EntityItemPickupEvent event, final CallbackInfo callback) {
        callback.cancel();
    }
}
