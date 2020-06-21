package io.protonull.bukkitreport.utilities;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.protonull.bukkitreport.BukkitReport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class ReportMaker {

	protected final BukkitReport plugin;

	private final File dataFolder;

	private final String file;

	protected final List<String> lines;

	public ReportMaker(BukkitReport plugin, String file) {
		Preconditions.checkArgument(plugin != null, "Bukkit Report plugins must not be null.");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(file), "Report file name must be valid!");
		this.plugin = plugin;
		this.dataFolder = plugin.getDataFolder();
		this.file = file;
		this.lines = Lists.newArrayList();
		if (!makeReport()) {
			plugin.warning("Could not make report: " + file);
		}
		else {
			saveReport();
			plugin.info("Report made: " + file);
		}
	}

	protected abstract boolean makeReport();

	protected void addLine(String line) {
		this.lines.add(line);
	}

	private void saveReport() {
		if (!this.dataFolder.exists() || !this.dataFolder.isDirectory()) {
			try {
				if (!this.dataFolder.mkdir()) {
					this.plugin.warning("Could not make data folder.");
				}
			}
			catch (SecurityException exception) {
				this.plugin.warning("An error occurred while making a data folder.", exception);
			}
		}
		try (FileWriter fw = new FileWriter(new File(this.dataFolder, this.file))) {
			for (String line : this.lines) {
				fw.write((Strings.isNullOrEmpty(line) ? "" : line) + "\r\n");
			}
			fw.write("\r\n");
		}
		catch (IOException exception) {
			this.plugin.warning("Could not write to report file.", exception);
		}
	}

}
