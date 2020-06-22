import maven = require("maven");
import { path } from "./utilities";

export default async function compile(project: string) {
    // @ts-ignore
    const mvn = maven.create({
        cwd: path(project),
        quiet: true
    });
    const commands = ["clean", "package"];
    const options = {
        "skipTests": "true",
        "checkstyle.skip": "true"
    };
    return await mvn.execute(commands, options);
}