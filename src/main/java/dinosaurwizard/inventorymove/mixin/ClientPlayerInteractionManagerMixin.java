/**

  All-encompassing Inventory Move Bypass by dinosaurwizard

**/

package dinosaurwizard.inventorymove.mixin;

import dinosaurwizard.inventorymove.modules.InventoryMoveBypass;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(MultiPlayerGameMode.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "handleContainerInput", at = @At("HEAD"))
    private void onClickSlotHead(int containerId, int slotId, int button, ContainerInput actionType, Player player, CallbackInfo ci) {
        Modules modules = Modules.get();
        if (modules == null || mc.player == null || mc.getConnection() == null) return;

        InventoryMoveBypass bypass = modules.get(InventoryMoveBypass.class);
        if (bypass != null && bypass.isActive() && bypass.isPlayerMoving()) {
            if (bypass.sprint.get() && mc.player.isSprinting()) {
                mc.getConnection().send(new ServerboundPlayerCommandPacket(
                    mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
            }

            Input stop = new Input(false, false, false, false, false, false, false);
            mc.getConnection().send(new ServerboundPlayerInputPacket(stop));
        }
    }

    @Inject(method = "handleContainerInput", at = @At("RETURN"))
    private void onClickSlotReturn(int containerId, int slotId, int button, ContainerInput actionType, Player player, CallbackInfo ci) {
        Modules modules = Modules.get();
        if (modules == null || mc.player == null || mc.getConnection() == null) return;

        InventoryMoveBypass bypass = modules.get(InventoryMoveBypass.class);
        if (bypass != null && bypass.isActive() && bypass.isPlayerMoving()) {
            if (bypass.sprint.get() && mc.player.isSprinting()) {
                mc.getConnection().send(new ServerboundPlayerCommandPacket(
                    mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
            }

            mc.getConnection().send(new ServerboundPlayerInputPacket(mc.player.input.keyPresses));
        }
    }
}
