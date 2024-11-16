package org.gjdd.batoru.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.entry.RegistryEntry;
import org.gjdd.batoru.job.Job;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(method = "getEmptySlot", at = @At(value = "HEAD"), cancellable = true)
    private void batoru$injectGetEmptySlot(CallbackInfoReturnable<Integer> info) {
        if (player.getWorld().isClient() || player.getJob() == null) {
            return;
        }

        var inventory = (PlayerInventory) (Object) this;
        for (int slot = 0; slot < inventory.main.size(); ++slot) {
            if (!batoru$isSlotDisabled(player.getJob(), slot) && inventory.main.get(slot).isEmpty()) {
                info.setReturnValue(slot);
                return;
            }
        }

        info.setReturnValue(-1);
    }

    @Inject(method = "updateItems", at = @At(value = "HEAD"))
    private void batoru$injectUpdateItems(CallbackInfo info) {
        if (player.getWorld().isClient() || player.getJob() == null) {
            return;
        }

        var inventory = (PlayerInventory) (Object) this;
        for (int slot = 0; slot < 9; slot++) {
            if (batoru$isSlotDisabled(player.getJob(), slot)) {
                inventory.offerOrDrop(inventory.removeStack(slot));
            }
        }
    }

    @Unique
    private boolean batoru$isSlotDisabled(RegistryEntry<Job> job, int slot) {
        return PlayerInventory.isValidHotbarIndex(slot) &&
                job.value().getDisabledHotbarSlots().contains(slot);
    }
}
