package io.protonull.bukkitreport.reports;

import static io.protonull.bukkitreport.BukkitReport.Report;
import static io.protonull.bukkitreport.BukkitReport.ReportMaker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import org.bukkit.Material;

@Report(outputFile = "materials.txt")
public class Materials extends ReportMaker {

    @Override
    protected void makeReport() {
        // Report All Materials
        addLine("Materials:");
        for (Material material : Material.values()) {
            addLine("\t" + material.name());
        }
        // Report all "is" method checkers
        Arrays.stream(Material.class.getMethods())
                .filter(method -> {
                    if (method.getParameterCount() > 0) {
                        return false;
                    }
                    if (!Modifier.isPublic(method.getModifiers())) {
                        return false;
                    }
                    if (Modifier.isAbstract(method.getModifiers())) {
                        return false;
                    }
                    if (Modifier.isStatic(method.getModifiers())) {
                        return false;
                    }
                    if (!boolean.class.equals(method.getReturnType())) {
                        return false;
                    }
                    return true;
                })
                .sorted(Comparator.comparing(Method::getName)).forEachOrdered(method -> {
                    addLine(method.getName() + "():");
                    for (Material material : Material.values()) {
                        boolean result = false;
                        try {
                            result = (boolean) method.invoke(material);
                        }
                        catch (IllegalAccessException | InvocationTargetException | ClassCastException exception) {
                            exception.printStackTrace();
                        }
                        if (!result) {
                            continue;
                        }
                        addLine("\t" + material.name());
                    }
                });
    }

}
