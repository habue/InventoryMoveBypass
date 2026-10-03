/**

  All-encompassing Inventory Move Bypass by dinosaurwizard

**/

package dinosaurwizard.inventorymove.mixin;

import dinosaurwizard.inventorymove.modules.InventoryMoveBypass;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.util.PlayerInput;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "clickSlot", at = @At("HEAD"))
    private void onClickSlotHead(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        Modules modules = Modules.get();
        if (modules == null) return;

        InventoryMoveBypass bypass = modules.get(InventoryMoveBypass.class);
        if (bypass != null && bypass.isActive() && bypass.isPlayerMoving()) {

            // Stop sprinting temporarily
            if (bypass.sprint.get() && mc.player.isSprinting()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            }

            // Set all inputs to false on server temporarily
            PlayerInput stop = new PlayerInput(false, false, false, false, false, false, false);
            mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(stop));
        }
    }

    @Inject(method = "clickSlot", at = @At("RETURN"))
    private void onClickSlotReturn(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        Modules modules = Modules.get();
        if (modules == null) return;

        InventoryMoveBypass bypass = modules.get(InventoryMoveBypass.class);
        if (bypass != null && bypass.isActive() && bypass.isPlayerMoving()) {

            // Return to sprinting if necessary
            if (bypass.sprint.get() && mc.player.isSprinting()) {
                mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            }

            // Return to normal inputs
            mc.getNetworkHandler().sendPacket(new PlayerInputC2SPacket(mc.player.input.playerInput));
        }
    }
}
