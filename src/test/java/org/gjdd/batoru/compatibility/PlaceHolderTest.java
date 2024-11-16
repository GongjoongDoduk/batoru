package org.gjdd.batoru.compatibility;

import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.Placeholders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

public final class PlaceHolderTest implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(
                        CommandManager.literal("placeholdertest")
                                .executes(context -> {
                                    context.getSource().sendMessage(
                                            Placeholders.parseText(
                                                    Text.literal("%batoru:job%"),
                                                    PlaceholderContext.of(context.getSource())
                                            )
                                    );
                                    return 1;
                                })
                )
        );
    }
}
