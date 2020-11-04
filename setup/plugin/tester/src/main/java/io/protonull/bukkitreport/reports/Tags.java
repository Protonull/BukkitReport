package io.protonull.bukkitreport.reports;

import static io.protonull.bukkitreport.BukkitReport.Report;
import static io.protonull.bukkitreport.BukkitReport.ReportMaker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Material;
import org.bukkit.Tag;

@Report(outputFile = "tags.txt")
public class Tags extends ReportMaker {

    @Override
    @SuppressWarnings("unchecked")
    protected void makeReport() {
        final Material[] materials = Materials.getMaterials();
        Map<String, List<Material>> tags = Maps.newHashMap();
        Arrays.stream(Tag.class.getDeclaredFields())
                .forEachOrdered(field -> {
                    if (!Modifier.isPublic(field.getModifiers())) {
                        return;
                    }
                    if (!Modifier.isStatic(field.getModifiers())) {
                        return;
                    }
                    if (!Tag.class.isAssignableFrom(field.getType())) {
                        return;
                    }
                    Tag<Material> tag;
                    try {
                        tag = Objects.requireNonNull((Tag<Material>) field.get(null));
                    }
                    catch (Exception exception) {
                        exception.printStackTrace();
                        return;
                    }
                    List<Material> tagged = Lists.newArrayList();
                    for (Material material : materials) {
                        if (tag.isTagged(material)) {
                            tagged.add(material);
                        }
                    }
                    if (tagged.isEmpty()) {
                        return;
                    }
                    tags.put(field.getName(), tagged);
                });
        tags.keySet().stream()
                .sorted()
                .forEachOrdered(key -> {
                    addLine(key + ":");
                    for (Material material : tags.get(key)) {
                        addLine("\t" + material.name());
                    }
                });
    }

}
