# Bukkit Report

By downloading and running this, you are agreeing to [Minecraft's EULA](https://account.mojang.com/documents/minecraft_eula).

# Published Reports

[See here](reports/) for all of the reports that have been compiled for the versions listed in [this config file](setup/config.ts).

# Generating Reports

If you'd like to run this for yourself, you'll need:

 * [Java 8](https://www.java.com/en/download/)

 * [Maven](https://maven.apache.org/download.cgi) (Tedius install)

 * [NodeJS LTS](https://nodejs.org/en/)

Be mindful that this can only generate reports for versions that PaperMC provides downloads for, and that the generation process can take a while, particularly for a lot of versions. Additionally, if you're on Windows then at around/before 1.11, you'll start getting Jansi errors, but this shouldn't hinder report generation, it's just Minecraft complaining that it doesn't have the plugin to print coloured text in the console.

Then to compile the reports:

 * `cd` into the directory you downloaded this into

 * `npm install`

 * `npm start`