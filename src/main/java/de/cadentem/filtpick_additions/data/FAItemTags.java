package de.cadentem.filtpick_additions.data;

import de.cadentem.filtpick_additions.FA;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class FAItemTags extends ItemTagsProvider {
    public static final TagKey<Item> FILTER_BLACKLIST = TagKey.create(Registry.ITEM_REGISTRY, FA.location("blacklist"));

    public FAItemTags(final DataGenerator generator, final BlockTagsProvider blockTagsProvider, @Nullable final ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, FA.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(FILTER_BLACKLIST)
                .add(Items.GOLDEN_APPLE)
                .add(Items.ENCHANTED_GOLDEN_APPLE)
                .addOptional(new ResourceLocation("iceandfire", "fire_dragon_flesh"))
                .addOptional(new ResourceLocation("iceandfire", "ice_dragon_flesh"))
                .addOptional(new ResourceLocation("iceandfire", "lightning_dragon_flesh"))
        ;
    }
}
