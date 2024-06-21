package de.cadentem.filtpick_additions.common.capability;

import de.cadentem.filtpick_additions.client.ClientProxy;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Map<Integer, LazyOptional<PlayerData>> SERVER_CACHE = new HashMap<>();
    public static final Map<Integer, LazyOptional<PlayerData>> CLIENT_CACHE = new HashMap<>();

    private final PlayerData data = new PlayerData();
    private final LazyOptional<PlayerData> instance = LazyOptional.of(() -> data);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull final Capability<T> capability, @Nullable final Direction side) {
        return capability == CapabilityHandler.PLAYER_DATA_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).serializeNBT();
    }

    @Override
    public void deserializeNBT(final CompoundTag tag) {
        instance.orElseThrow(() -> new IllegalArgumentException("Capability instance was not present")).deserializeNBT(tag);
    }

    public static LazyOptional<PlayerData> getCapability(final Entity entity) {
        if (entity instanceof Player) {
            Map<Integer, LazyOptional<PlayerData>> sidedCache = entity.getLevel().isClientSide() ? CLIENT_CACHE : SERVER_CACHE;
            LazyOptional<PlayerData> capability = sidedCache.get(entity.getId());

            if (capability == null) {
                capability = entity.getCapability(CapabilityHandler.PLAYER_DATA_CAPABILITY);
                capability.addListener(ignored -> sidedCache.remove(entity.getId()));

                if (capability.isPresent()) {
                    sidedCache.put(entity.getId(), capability);
                }
            }

            return capability;
        }

        return LazyOptional.empty();
    }

    public static @Nullable PlayerData getPlayerData(final Entity entity) {
        if (entity instanceof Player) {
            LazyOptional<PlayerData> capability = PlayerDataProvider.getCapability(entity);

            if (capability.isPresent()) {
                Optional<PlayerData> resolved = capability.resolve();

                if (resolved.isPresent()) {
                    return resolved.get();
                }
            }
        }

        return null;
    }

    public static void removeCachedEntry(final Entity entity) {
        if (entity.getLevel().isClientSide()) {
            if (entity == ClientProxy.getLocalPlayer()) {
                CLIENT_CACHE.clear();
            } else {
                CLIENT_CACHE.remove(entity.getId());
            }
        } else {
            SERVER_CACHE.remove(entity.getId());
        }
    }
}
