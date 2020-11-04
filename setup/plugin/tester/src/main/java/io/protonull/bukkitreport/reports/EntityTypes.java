package io.protonull.bukkitreport.reports;

import static io.protonull.bukkitreport.BukkitReport.Report;
import static io.protonull.bukkitreport.BukkitReport.ReportMaker;

import java.util.Arrays;
import java.util.Comparator;
import org.bukkit.entity.EntityType;

@Report(outputFile = "entity-types.txt")
public class EntityTypes extends ReportMaker {

    @Override
    protected void makeReport() {
        final EntityType[] entityTypes = EntityType.values();
        Arrays.sort(entityTypes, 0, entityTypes.length, Comparator.comparing(Enum::name));
        addLine("EntityTypes:");
        for (EntityType type : entityTypes) {
            addLine("\t" + type.name());
        }
    }

}
