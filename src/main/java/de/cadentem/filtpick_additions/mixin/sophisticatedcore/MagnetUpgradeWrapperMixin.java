package de.cadentem.filtpick_additions.mixin.sophisticatedcore;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import de.cadentem.filtpick_additions.server.FilterHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Allow FiltPick to filter items picked up through the magnet upgrade */
@Mixin(value = MagnetUpgradeWrapper.class, remap = false)
public abstract class MagnetUpgradeWrapperMixin {
    @WrapOperation(method = "pickupItems", at = @At(value = "INVOKE", target = "Lnet/p3pp3rf1y/sophisticatedcore/upgrades/magnet/MagnetUpgradeWrapper;tryToInsertItem(Lnet/minecraft/world/entity/item/ItemEntity;)Z"))
    private boolean filtpick_additions$considerFilter(final MagnetUpgradeWrapper instance, final ItemEntity itemEntity, final Operation<Boolean> original, @Local(argsOnly = true) final LivingEntity livingEntity) {
        if (livingEntity instanceof Player player && FilterHandler.shouldFilter(player, itemEntity)) {
            return false;
        }

        return original.call(instance, itemEntity);
    }
}
