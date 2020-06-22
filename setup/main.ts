import config from "./config";
import * as chalk from "chalk";
import { path, exists, format } from "./scripts/utilities";

// Folders
const mainFolder = process.cwd();
const reportsFolder = path(mainFolder, config.folders.reports);
const setupFolder = path(mainFolder, config.folders.setup);
const cacheFolder = path(setupFolder, config.folders.config);
const projectFolder = path(setupFolder, config.folders.plugin);
const templateFolder = path(setupFolder, config.folders.template);
const pluginLocation = path(projectFolder, "target", config.plugin + ".jar");
const serverPath = (version) => path(cacheFolder, config.folders.server, version);
const pluginPath = (version) => path(serverPath(version), "plugins", config.plugin + ".jar");
const paperPath = (version) => path(serverPath(version), "paper.jar");
const generatedPath = (version) => path(serverPath(version), "plugins", config.plugin);
const reportsPath = (version) => path(reportsFolder, version);

// Tasks
import { rmdir, mkdir, copy } from "./scripts/utilities";
import compile from "./scripts/compile-plugin";
import download from "./scripts/download";
import run from "./scripts/run-paper";

(async function () {
     // Cleaning up reports
     console.log("Cleaning up reports.");
     rmdir(reportsFolder);
     // Ensure the cache folder exists
     console.log("Ensuring cache folder");
     mkdir(cacheFolder);
     // Compile the report plugin
     console.log("Compiling report plugin.");
     try {
          await compile(projectFolder);
          console.log(chalk.blue("Report plugin compiled!"));
     }
     catch (ignored) {
          console.error(chalk.red("Could not compile the report plugin. Stopping."));
          process.exit(1);
     }
     // Setup server folder for each version
     console.log("Setting up server environments.");
     for (const version of config.versions) {
          const serverFolder = serverPath(version);
          console.log(`Ensuring ${version} folder.`);
          mkdir(serverFolder);
          console.log("Applying template.");
          copy(templateFolder, serverFolder, true);
          console.log("Adding plugin.");
          copy(pluginLocation, pluginPath(version), true);
          console.log("Removing old reports.");
          rmdir(generatedPath(version));
          const paperLocation = paperPath(version);
          if (!exists(paperLocation)) {
               console.log(chalk.yellow(`Could not find paper.jar for ${version}, downloading now.`));
               try {
                    await download(format(config.paper, version), paperLocation);
                    console.log(chalk.blue(`PaperMC ${version} downloaded!`));
               }
               catch (ignored) {
                    console.error(chalk.red(`Could not download PaperMC ${version}, skipping.`));
                    continue;
               }
          }
          else {
               console.log(`Found paper.jar for ${version}`);
          }
          console.log("Compiling report for " + version);
          run(serverFolder, "paper.jar");
          console.log(chalk.blue("Report compiled."));
          console.log("Copying reports.");
          copy(generatedPath(version), reportsPath(version), true);
     }
     console.log(chalk.blue("Done."));
}())