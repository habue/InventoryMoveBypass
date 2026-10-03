/**

  All-encompassing Inventory Move Bypass by dinosaurwizard

  The important logic is in ClientPlayerInteractionManagerMixin.java

**/

package dinosaurwizard.inventorymove.modules;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.world.entity.player.Input;

public class InventoryMoveBypass extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> sprint = sgGeneral.add(new BoolSetting.Builder()
        .name("sprint")
        .description("Send additional packets to bypass Grim sprint checks. Enabling this can cause minor rubberbands in certain situations.")
        .defaultValue(true)
        .build()
    );

    public InventoryMoveBypass() {
        super(Categories.Misc, "inventory-move-bypass", "Encompassing bypass for Grim inventory-move anti-cheat.");
    }

    public boolean isPlayerMoving() {
        if (mc.player == null || mc.player.input == null) return false;

        Input input = mc.player.input.keyPresses;
        return input.forward() || input.backward() || input.left() || input.right() || input.jump();
    }
}
