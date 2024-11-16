package org.gjdd.batoru.job;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.gjdd.batoru.job.event.ListenerDispatcher;
import org.gjdd.batoru.job.event.entity.JobAssignedListener;
import org.gjdd.batoru.job.event.entity.JobUnassignedListener;
import org.gjdd.batoru.job.event.entity.player.*;
import org.gjdd.batoru.registry.BatoruRegistries;

public final class JobTest implements ModInitializer {
    private final Job testJob = Job.builder()
            .addDisabledHotbarSlots(0, 1, 2, 3)
            .setListenerDispatcher(
                    ListenerDispatcher.builder()
                            .addListener((DropItemListener) (job, player, entireStack) -> {
                                player.sendMessage(
                                        Text.literal(
                                                "onDropItem(" + entireStack + ")"
                                        )
                                );
                                return false;
                            })
                            .addListener((HandSwingListener) (job, player, hand) -> {
                                player.sendMessage(
                                        Text.literal(
                                                "onHandSwing(" + hand + ")"
                                        )
                                );
                                return false;
                            })
                            .addListener((PlayerInputListener) (job, player, input) ->
                                    player.sendMessage(
                                            Text.literal(
                                                    "onPlayerInput(" + input + ")"
                                            )
                                    ))
                            .addListener((SwapOffhandListener) (job, player, mainHandStack, offHandStack) -> {
                                player.sendMessage(
                                        Text.literal(
                                                "onSwapOffhand(" + mainHandStack + ", " + offHandStack + ")"
                                        )
                                );
                                return false;
                            })
                            .addListener((UpdateSelectedSlotListener) (job, player, selectedSlot) -> {
                                player.sendMessage(
                                        Text.literal(
                                                "onUpdateSelectedSlot(" + selectedSlot + ")"
                                        )
                                );
                                return false;
                            })
                            .addListener((JobAssignedListener) (job, entity) -> {
                                if (entity instanceof ServerPlayerEntity player) {
                                    player.sendMessage(
                                            Text.literal(
                                                    "onJobAssigned()"
                                            )
                                    );
                                }
                            })
                            .addListener((JobUnassignedListener) (job, entity) -> {
                                if (entity instanceof ServerPlayerEntity player) {
                                    player.sendMessage(
                                            Text.literal(
                                                    "onJobUnassigned()"
                                            )
                                    );
                                }
                            })
                            .build()
            )
            .build();

    @Override
    public void onInitialize() {
        Registry.register(BatoruRegistries.JOB, "batoru-test:test", testJob);
    }
}
