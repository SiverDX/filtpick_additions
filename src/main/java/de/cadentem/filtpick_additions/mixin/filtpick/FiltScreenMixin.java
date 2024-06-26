package de.cadentem.filtpick_additions.mixin.filtpick;

import com.mojang.blaze3d.vertex.PoseStack;
import de.cadentem.filtpick_additions.FA;
import de.cadentem.filtpick_additions.client.gui.FiltPickClient;
import de.cadentem.filtpick_additions.common.capability.CapabilityHandler;
import de.cadentem.filtpick_additions.common.capability.PlayerData;
import de.cadentem.filtpick_additions.common.capability.PlayerDataProvider;
import de.cadentem.filtpick_additions.compat.Compat;
import net.apeng.filtpick.gui.FiltMenu;
import net.apeng.filtpick.gui.FiltScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/** Add custom buttons */
@Mixin(FiltScreen.class)
public abstract class FiltScreenMixin extends AbstractContainerScreen<FiltMenu> {
    public FiltScreenMixin(final FiltMenu menu, final Inventory playerInventory, final Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void filtpick_additions$addCustomButtons(final CallbackInfo callback) {
        int spacing = 1;
        int buttonWidth = 12;
        int offset = 0;

        int x = destructionModeButton.x + destructionModeButton.getWidth() + spacing;
        int y = destructionModeButton.y;

        if (FA.isModLoaded(Compat.APOTHEOSIS)) {
            FiltPickClient.RARITY_FILTER_BUTTON = addRenderableWidget(FiltPickClient.createRarityFilterButton(x, y));
            offset = buttonWidth + spacing;
        }

        if (FA.isModLoaded(Compat.QUALITY_FOOD)) {
            FiltPickClient.QUALITY_FILTER_BUTTON = addRenderableWidget(FiltPickClient.createQualityFilterButton(x + offset, y));
        }
    }

    @Inject(method = "lambda$initResetButton$0", at = @At("TAIL"), remap = false)
    private void filtpick_additions$resetConfig(final CallbackInfo callback) {
        PlayerDataProvider.getCapability(Minecraft.getInstance().player).ifPresent(data -> {
            data.qualityFilter = PlayerData.Quality.DISABLED;
            data.rarityFilter = PlayerData.RarityFilter.DISABLED;
            CapabilityHandler.syncPlayerData(Minecraft.getInstance().player);
        });
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/apeng/filtpick/gui/FiltScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;II)V", shift = At.Shift.BEFORE))
    private void filtpick_additions$renderTooltips(final PoseStack pose, int mouseX, int mouseY, float partialTick, CallbackInfo callback) {
        if (FiltPickClient.RARITY_FILTER_BUTTON != null && FiltPickClient.RARITY_FILTER_BUTTON.isHoveredOrFocused()) {
            PlayerDataProvider.getCapability(Minecraft.getInstance().player).ifPresent(data -> renderComponentTooltip(pose, List.of(Component.translatable("gui.filtpick_additions.rarity_filter_button", data.rarityFilter.toString()).withStyle(ChatFormatting.DARK_GRAY)), mouseX, mouseY));
        }

        if (FiltPickClient.QUALITY_FILTER_BUTTON != null && FiltPickClient.QUALITY_FILTER_BUTTON.isHoveredOrFocused()) {
            PlayerDataProvider.getCapability(Minecraft.getInstance().player).ifPresent(data -> renderComponentTooltip(pose, List.of(Component.translatable("gui.filtpick_additions.quality_filter_button", data.qualityFilter.toString()).withStyle(ChatFormatting.DARK_GRAY)), mouseX, mouseY));
        }
    }

    @Shadow(remap = false) private StateSwitchingButton destructionModeButton;
}
