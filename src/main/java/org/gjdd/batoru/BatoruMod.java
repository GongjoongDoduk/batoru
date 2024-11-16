package org.gjdd.batoru;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.gjdd.batoru.command.CastCommand;
import org.gjdd.batoru.command.JobCommand;
import org.gjdd.batoru.compatibility.BatoruPlaceholderApi;
import org.gjdd.batoru.compatibility.BatoruPolymerResourcePack;
import org.gjdd.batoru.effect.BatoruStatusEffects;
import org.gjdd.batoru.registry.BatoruRegistries;
import org.gjdd.batoru.registry.BatoruRegistryKeys;

public final class BatoruMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CastCommand.register(dispatcher);
            JobCommand.register(dispatcher);
        });
        BatoruStatusEffects.register();
        BatoruRegistries.register();
        BatoruRegistryKeys.register();

        if (FabricLoader.getInstance().isModLoaded("placeholder-api")) {
            BatoruPlaceholderApi.initialize();
        }

        if (FabricLoader.getInstance().isModLoaded("polymer-resource-pack")) {
            BatoruPolymerResourcePack.initialize();
        }
    }
}
