package io.protonull.bukkitreport.reports;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.protonull.bukkitreport.BukkitReport;
import io.protonull.bukkitreport.utilities.ReportMaker;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.Tag;
import vg.civcraft.mc.civmodcore.util.Iteration;

public class Tags extends ReportMaker {

	public Tags(BukkitReport plugin) {
		super(plugin, "tags.txt");
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean makeReport() {
		Map<String, List<Material>> tags = Maps.newHashMap();
		Arrays.stream(Tag.class.getDeclaredFields()).forEachOrdered(field -> {
			if (!Modifier.isPublic(field.getModifiers())) {
				return;
			}
			if (!Modifier.isStatic(field.getModifiers())) {
				return;
			}
			if (!Tag.class.isAssignableFrom(field.getType())) {
				return;
			}
			Tag<Material> tag = null;
			try {
				tag = (Tag<Material>) field.get(null);
			}
			catch (Exception exception) {
				this.plugin.warning("Failed: Tag." + field.getName(), exception);
				return;
			}
			if (tag == null) {
				this.plugin.warning("Empty: Tag." + field.getName());
				return;
			}
			List<Material> tagged = Lists.newArrayList();
			for (Material material : Material.values()) {
				if (tag.isTagged(material)) {
					tagged.add(material);
				}
			}
			if (tagged.isEmpty()) {
				return;
			}
			tags.put(field.getName(), tagged);
		});
		tags.keySet().stream().sorted().forEachOrdered(key -> {
			addLine(key + ":");
			List<Material> tagged = tags.get(key);
			if (!Iteration.isNullOrEmpty(tagged)) {
				for (Material material : tagged) {
					addLine("\t" + material.name());
				}
			}
			addLine(null);
		});
		return true;
	}

}
