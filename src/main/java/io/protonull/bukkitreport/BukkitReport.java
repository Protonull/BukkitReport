package io.protonull.bukkitreport;

import io.protonull.bukkitreport.reports.Materials;
import io.protonull.bukkitreport.reports.Tags;
import vg.civcraft.mc.civmodcore.ACivMod;

public final class BukkitReport extends ACivMod {

    @Override
    public void onEnable() {
        new Materials(this);
        new Tags(this);
    }

}
