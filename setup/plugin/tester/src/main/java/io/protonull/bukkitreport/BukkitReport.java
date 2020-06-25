package io.protonull.bukkitreport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitReport extends JavaPlugin {

    @Override
    public void onEnable() {
        // ------------------------------------------------------------
        // Setup
        // ------------------------------------------------------------
        final Logger logger = getLogger();
        final File dataFolder = getDataFolder();
        if (!dataFolder.exists() || !dataFolder.isDirectory()) {
            try {
                if (!dataFolder.mkdir()) {
                    logger.warning("Could not make data folder.");
                }
            }
            catch (SecurityException exception) {
                logger.warning("An error occurred while making a data folder.");
                exception.printStackTrace();
                finished();
                return;
            }
        }
        // ------------------------------------------------------------
        // Reports
        // ------------------------------------------------------------
        arrangeReport("Materials", "io.protonull.bukkitreport.reports.Materials",
                // The Bukkit/Spigot classes it requires
                "org.bukkit.Material");
        arrangeReport("Tags", "io.protonull.bukkitreport.reports.Tags",
                // The Bukkit/Spigot classes it requires
                "org.bukkit.Tag",
                "org.bukkit.Material");
        arrangeReport("EntityTypes", "io.protonull.bukkitreport.reports.EntityTypes",
                // The Bukkit/Spigot classes it requires
                "org.bukkit.entity.EntityType");
        // ------------------------------------------------------------
        // Done
        // ------------------------------------------------------------
        finished();
    }

    private void finished() {
        final Logger logger = getLogger();
        logger.info("Done.");
        if (getConfig().getBoolean("shutdown", true)) {
            Bukkit.shutdown();
        }
    }

    @SuppressWarnings("unchecked")
    private void arrangeReport(String name, String reportClass, String... requiredClasses) {
        final Logger logger = getLogger();
        logger.info("Arranging report: " + name);
        if (!Utilities.hasRequiredClasses(requiredClasses)) {
            logger.warning("Cannot complete report \"" + name + "\" as its required classes are not loaded.");
            return;
        }
        Class<ReportMaker> reportClazz;
        try {
            reportClazz = (Class<ReportMaker>) Class.forName(reportClass);
        }
        catch (ClassNotFoundException ignored) {
            logger.warning("Cannot complete report \"" + name + "\" as the report class itself is missing.");
            return;
        }
        catch (ClassCastException ignored) {
            logger.warning("Cannot complete report \"" + name + "\" as the report class itself is invalid.");
            return;
        }
        Report report = reportClazz.getAnnotation(Report.class);
        if (report == null) {
            logger.warning("Cannot complete report \"" + name + "\" as the report class itself is not valid.");
            return;
        }
        Constructor<ReportMaker> constructor;
        try {
            constructor = reportClazz.getConstructor();
        }
        catch (NoSuchMethodException ignored) {
            logger.warning("Cannot complete report \"" + name + "\" as the report class does not have the " +
                    "appropriate constructor.");
            return;
        }
        ReportMaker instance;
        try {
            instance = constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ignored) {
            logger.warning("Cannot complete report \"" + name + "\" as the report class could not be constructed.");
            return;
        }
        try {
            instance.makeReport();
        }
        catch (Exception exception) {
            logger.warning("Cannot complete report \"" + name + "\" as an error occured while making the report.");
            exception.printStackTrace();
            return;
        }
        final File dataFolder = getDataFolder();
        try (FileWriter fw = new FileWriter(new File(dataFolder, report.outputFile()))) {
            Iterator<String> iterator = instance.lines.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (Utilities.isNullOrEmpty(line)) {
                    continue;
                }
                fw.write(line);
                if (iterator.hasNext()) {
                    fw.write("\r\n");
                }
            }
        }
        catch (IOException exception) {
            logger.warning("Could not write to report file.");
            exception.printStackTrace();
            return;
        }
        logger.info("Report for \"" + name + "\" completed.");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Report {
        String outputFile();
    }

    public static abstract class ReportMaker {

        private final List<String> lines;

        public ReportMaker() {
            this.lines = new ArrayList<>();
        }

        protected abstract void makeReport();

        protected final void addLine(String line) {
            this.lines.add(line);
        }

    }

}
