package dinosaurwizard.inventorymove;

import dinosaurwizard.inventorymove.modules.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class InventoryMoveAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing Inventory Move Bypass");

        // Modules
        Modules.get().add(new InventoryMoveBypass());
    }

    @Override
    public void onRegisterCategories() {
        // Custom category not defined
    }

    @Override
    public String getPackage() {
        return "dinosaurwizard.inventorymove";
    }
}
