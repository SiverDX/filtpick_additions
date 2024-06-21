package de.cadentem.filtpick_additions;

import de.cadentem.filtpick_additions.common.capability.PlayerData;
import de.cadentem.filtpick_additions.common.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

@Mod(FA.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FA {
    public static final String MODID = "filtpick_additions";

    private static final Map<String, Boolean> MODS = new HashMap<>();

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent ignored) {
        NetworkHandler.register();
    }

    @SubscribeEvent
    public static void registerCapability(final RegisterCapabilitiesEvent event) {
        event.register(PlayerData.class);
    }

    public static ResourceLocation location(final String path) {
        return new ResourceLocation(MODID, path);
    }

    public static boolean isModLoaded(final String mod) {
        return MODS.computeIfAbsent(mod, key -> ModList.get().isLoaded(mod));
    }
}
