package de.cadentem.filtpick_additions.server;

import de.cadentem.filtpick_additions.FA;
import de.cadentem.filtpick_additions.common.capability.PlayerData;
import de.cadentem.filtpick_additions.common.capability.PlayerDataProvider;
import de.cadentem.filtpick_additions.compat.Compat;
import de.cadentem.filtpick_additions.data.FAItemTags;
import de.cadentem.quality_food.util.QualityUtils;
import de.cadentem.quality_food.util.Utils;
import net.apeng.filtpick.capability.FiltList;
import net.apeng.filtpick.capability.FiltListProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import shadows.apotheosis.adventure.affix.AffixHelper;
import shadows.apotheosis.adventure.loot.LootCategory;
import shadows.apotheosis.adventure.loot.LootRarity;
import top.theillusivec4.curios.api.CuriosApi;

public class FilterHandler {
    public static boolean shouldFilter(final Player player, final ItemEntity itemEntity) {
        if (player == null || itemEntity == null) {
            return false;
        }
        ItemStack stack = itemEntity.getItem();

        boolean doFilter = false;
        boolean isBlacklisted = stack.is(FAItemTags.FILTER_BLACKLIST);

        if (!isBlacklisted && FA.isModLoaded(Compat.APOTHEOSIS) && /* Apoth Curios */ !(FA.isModLoaded(Compat.CURIOS) && CuriosApi.getCuriosHelper().getCurio(stack).isPresent())) {
            PlayerData data = PlayerDataProvider.getPlayerData(player);

            if (data != null && data.rarityFilter != PlayerData.RarityFilter.DISABLED) {
                if (LootCategory.forItem(stack) != LootCategory.NONE) {
                    LootRarity rarity = AffixHelper.getRarity(stack);

                    if (rarity == null || rarity.ordinal() <= data.rarityFilter.ordinal() - /* Skip DISABLED and UNAFFIXED */ 2) {
                        doFilter = true;
                    }
                }
            }
        }

        if (!isBlacklisted && FA.isModLoaded(Compat.QUALITY_FOOD)) {
            PlayerData data = PlayerDataProvider.getPlayerData(player);

            if (data != null && data.qualityFilter != PlayerData.Quality.DISABLED && Utils.isValidItem(stack) && data.qualityFilter.level() >= QualityUtils.getQuality(stack).level()) {
                doFilter = true;
            }
        }

        LazyOptional<FiltList> capability = player.getCapability(FiltListProvider.FILT_LIST);

        if (capability.isPresent()) {
            FiltList filter = capability.resolve().orElse(null);

            if (filter != null) {
                if (!doFilter) {
                    Item item = stack.getItem();
                    boolean matched = hasMatchedItem(item, filter);

                    if (!filter.isWhitelistModeOn()) {
                        if (matched) {
                            doFilter = true;
                        }
                    } else if (!matched) {
                        doFilter = true;
                    }
                }

                if (doFilter) {
                    if (filter.isDestructionModeOn()) {
                        itemEntity.discard();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private static boolean hasMatchedItem(final Item pickedItem, final FiltList filtList) {
        boolean matched = false;

        for (int i = 0; i < filtList.getSlots(); ++i) {
            ItemStack itemStack = filtList.getStackInSlot(i);

            if (itemStack.getItem().equals(pickedItem)) {
                matched = true;
            }
        }

        return matched;
    }
}
