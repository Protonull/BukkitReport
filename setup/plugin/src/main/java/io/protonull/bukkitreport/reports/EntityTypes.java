package io.protonull.bukkitreport.reports;

import static io.protonull.bukkitreport.BukkitReport.Report;
import static io.protonull.bukkitreport.BukkitReport.ReportMaker;
import org.bukkit.entity.EntityType;

@Report(outputFile = "entity-types.txt")
public class EntityTypes extends ReportMaker {

    @Override
    protected void makeReport() {
        addLine("EntityTypes:");
        for (EntityType type : EntityType.values()) {
            addLine("\t" + type.name());
        }
    }

}
