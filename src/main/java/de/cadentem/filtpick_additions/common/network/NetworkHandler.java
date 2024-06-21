package de.cadentem.filtpick_additions.common.network;

import de.cadentem.filtpick_additions.FA;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    public static final String PROTOCOL_VERSION = "1.0.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(FA.location("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, SyncPlayerData.class, SyncPlayerData::encode, SyncPlayerData::decode, SyncPlayerData::handle);
    }
}
