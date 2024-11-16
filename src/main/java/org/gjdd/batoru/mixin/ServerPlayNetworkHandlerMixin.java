package org.gjdd.batoru.mixin;

import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.gjdd.batoru.job.event.entity.player.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerInput", at = @At(value = "HEAD"))
    private void batoru$injectOnPlayerInput(PlayerInputC2SPacket packet, CallbackInfo info) {
        var job = player.getJob();
        if (job == null) {
            return;
        }

        job.value()
                .getListenerDispatcher()
                .getListeners(PlayerInputListener.class)
                .forEach(listener ->
                        listener.onPlayerInput(
                                job,
                                player,
                                packet.input()
                        )
                );
    }

    @Inject(method = "onPlayerAction", at = @At(value = "HEAD"), cancellable = true)
    private void batoru$injectOnPlayerAction(PlayerActionC2SPacket packet, CallbackInfo info) {
        var job = player.getJob();
        if (job == null) {
            return;
        }

        var listenerDispatcher = job.value().getListenerDispatcher();
        var cancelled = switch (packet.getAction()) {
            case DROP_ITEM -> listenerDispatcher.getListeners(DropItemListener.class)
                    .stream()
                    .anyMatch(listener ->
                            listener.onDropItem(
                                    job,
                                    player,
                                    false
                            )
                    );
            case DROP_ALL_ITEMS -> listenerDispatcher.getListeners(DropItemListener.class)
                    .stream()
                    .anyMatch(listener ->
                            listener.onDropItem(
                                    job,
                                    player,
                                    true
                            )
                    );
            case SWAP_ITEM_WITH_OFFHAND -> listenerDispatcher.getListeners(SwapOffhandListener.class)
                    .stream()
                    .anyMatch(listener ->
                            listener.onSwapOffhand(
                                    job,
                                    player,
                                    player.getMainHandStack(),
                                    player.getOffHandStack()
                            )
                    );
            default -> false;
        };
        if (cancelled) {
            player.currentScreenHandler.syncState();
            info.cancel();
        }
    }

    @Inject(method = "onUpdateSelectedSlot", at = @At(value = "HEAD"), cancellable = true)
    private void batoru$injectOnUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo info) {
        var job = player.getJob();
        if (job == null) {
            return;
        }

        var cancelled = job.value()
                .getListenerDispatcher()
                .getListeners(UpdateSelectedSlotListener.class)
                .stream()
                .anyMatch(listener ->
                        listener.onUpdateSelectedSlot(
                                job,
                                player,
                                packet.getSelectedSlot()
                        )
                );
        if (cancelled) {
            player.networkHandler.sendPacket(new UpdateSelectedSlotS2CPacket(player.getInventory().selectedSlot));
            info.cancel();
        }
    }

    @Inject(method = "onHandSwing", at = @At(value = "HEAD"), cancellable = true)
    private void batoru$injectOnHandSwing(HandSwingC2SPacket packet, CallbackInfo info) {
        var job = player.getJob();
        if (job == null) {
            return;
        }

        var cancelled = job.value()
                .getListenerDispatcher()
                .getListeners(HandSwingListener.class)
                .stream()
                .anyMatch(listener ->
                        listener.onHandSwing(
                                job,
                                player,
                                packet.getHand()
                        )
                );
        if (cancelled) {
            info.cancel();
        }
    }
}
