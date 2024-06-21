package de.cadentem.filtpick_additions.common.capability;

import de.cadentem.filtpick_additions.FA;
import de.cadentem.filtpick_additions.common.network.NetworkHandler;
import de.cadentem.filtpick_additions.common.network.SyncPlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
public class CapabilityHandler {
    public static final Capability<PlayerData> PLAYER_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation PLAYER_DATA = FA.location("player_data");

    @SubscribeEvent
    public static void attachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (player instanceof FakePlayer) {
                return;
            }

            event.addCapability(PLAYER_DATA, new PlayerDataProvider());
        }
    }

    @SubscribeEvent
    public static void handleInitialSync(final EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof Player player) {
            syncPlayerData(player);
        }
    }

    @SubscribeEvent
    public static void handlePlayerDeath(final PlayerEvent.Clone event) {
        event.getOriginal().reviveCaps();
        PlayerDataProvider.getCapability(event.getEntity()).ifPresent(data -> PlayerDataProvider.getCapability(event.getOriginal()).ifPresent(oldData -> data.deserializeNBT(oldData.serializeNBT())));
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void removeCachedEntry(final EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            PlayerDataProvider.removeCachedEntry(entity);
        }
    }

    public static void syncPlayerData(final Player player) {
        PlayerDataProvider.getCapability(player).ifPresent(data -> syncPlayerData(player, data.serializeNBT()));
    }

    public static void syncPlayerData(final Player player, final CompoundTag tag) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncPlayerData(tag));
        } else {
            NetworkHandler.CHANNEL.sendToServer(new SyncPlayerData(tag));
        }
    }
}
