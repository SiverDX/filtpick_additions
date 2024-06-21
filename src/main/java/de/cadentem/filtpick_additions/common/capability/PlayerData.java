package de.cadentem.filtpick_additions.common.capability;

import net.minecraft.nbt.CompoundTag;

public class PlayerData {
    public RarityFilter rarityFilter = RarityFilter.DISABLED;
    public Quality qualityFilter = Quality.DISABLED;

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("rarityFilter", rarityFilter.ordinal());
        tag.putInt("qualityFilter", qualityFilter.ordinal());

        return tag;
    }

    public void deserializeNBT(final CompoundTag tag) {
        rarityFilter = RarityFilter.get(tag.getInt("rarityFilter"));
        qualityFilter = Quality.values()[tag.getInt("qualityFilter")];
    }

    public enum Quality {
        DISABLED("Disabled", 0),
        NO_QUALITY("No Quality", 0),
        IRON("Iron", 1),
        GOLD("Gold", 2),
        DIAMOND("Diamond", 3);

        private final String name;
        private final int level;

        Quality(final String name, int level) {
            this.name = name;
            this.level = level;
        }

        public int level() {
            return level;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum RarityFilter {
        DISABLED("Disabled"),
        UNAFFIXED("Unaffixed"),
        COMMON("Common"),
        UNCOMMON("Uncommon"),
        RARE("Rare"),
        EPIC("Epic"),
        MYTHIC("Mythic"),
        ANCIENT("Ancient");

        private final String name;

        RarityFilter(final String name) {
            this.name = name;
        }

        public static RarityFilter get(int ordinal) {
            if (ordinal < 0 || ordinal > values().length - 1) {
                return DISABLED;
            }

            return values()[ordinal];
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
