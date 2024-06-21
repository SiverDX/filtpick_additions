package de.cadentem.filtpick_additions.client;

import de.cadentem.filtpick_additions.common.capability.PlayerDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

public class ClientProxy {
    public static void handleSyncPlayerData(final CompoundTag tag) {
        PlayerDataProvider.getCapability(ClientProxy.getLocalPlayer()).ifPresent(data -> data.deserializeNBT(tag));
    }

    public static @Nullable Player getLocalPlayer() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return Minecraft.getInstance().player;
        }

        return null;
    }
}
