package de.cadentem.filtpick_additions.client.gui;

import de.cadentem.filtpick_additions.FA;
import de.cadentem.filtpick_additions.common.capability.CapabilityHandler;
import de.cadentem.filtpick_additions.common.capability.PlayerData;
import de.cadentem.filtpick_additions.common.capability.PlayerDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FiltPickClient {
    private static final ResourceLocation FILT_PICK_BUTTONS = FA.location("textures/gui/filtpick_buttons.png");

    public static ImageButton RARITY_FILTER_BUTTON;
    public static ImageButton QUALITY_FILTER_BUTTON;

    public static ImageButton createRarityFilterButton(int x, int y)  {
        return new ImageButton(x, y, 12, 12, 0, 0, FILT_PICK_BUTTONS, button -> {
            Consumer<Enum<?>> action = value -> PlayerDataProvider.getCapability(Minecraft.getInstance().player).ifPresent(innerData -> {
                innerData.rarityFilter = (PlayerData.RarityFilter) value;
                CapabilityHandler.syncPlayerData(Minecraft.getInstance().player);
            });

            Supplier<Enum<?>> getInitialValue = () -> {
                PlayerData innerData = PlayerDataProvider.getPlayerData(Minecraft.getInstance().player);
                return innerData != null ? innerData.rarityFilter : PlayerData.RarityFilter.DISABLED;
            };

            Minecraft.getInstance().setScreen(new SelectEnumScreen(Minecraft.getInstance().screen, PlayerData.RarityFilter.class, action, getInitialValue));
        });
    }

    public static ImageButton createQualityFilterButton(int x, int y) {
        return new ImageButton(x, y, 12, 12, 12, 0, FILT_PICK_BUTTONS, button -> {
            Consumer<Enum<?>> action = value -> PlayerDataProvider.getCapability(Minecraft.getInstance().player).ifPresent(innerData -> {
                innerData.qualityFilter = (PlayerData.Quality) value;
                CapabilityHandler.syncPlayerData(Minecraft.getInstance().player);
            });

            Supplier<Enum<?>> getInitialValue = () -> {
                PlayerData innerData = PlayerDataProvider.getPlayerData(Minecraft.getInstance().player);
                return innerData != null ? innerData.qualityFilter : PlayerData.Quality.DISABLED;
            };

            Minecraft.getInstance().setScreen(new SelectEnumScreen(Minecraft.getInstance().screen, PlayerData.Quality.class, action, getInitialValue));
        });
    }
}
