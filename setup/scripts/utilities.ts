import { resolve } from "path";
import slash = require("slash");
import { sync as rimraf } from "rimraf";
import { mkdirSync, existsSync } from "fs";
import { copySync } from "fs-extra";
import { sprintf } from "sprintf-js";

export function path(...location: string[]) {
    return slash(resolve(...location));
}

export function rmdir(location: string) {
    rimraf(path(location));
}

export function mkdir(location: string) {
    mkdirSync(path(location), {
        recursive: true
    });
}

export function copy(from: string, to: string, overwrite: boolean) {
    copySync(path(from), path(to), {
        overwrite: overwrite
    });
}

export function exists(location: string) {
    return existsSync(path(location));
}

export function format(text: string, ...data): string {
    return sprintf(text, ...data);
}