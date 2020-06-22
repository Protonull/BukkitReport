import { path } from "./utilities";
import * as doDownload from "download";
import { writeFileSync } from "fs";

export default async function download(url: string, location: string) {
    writeFileSync(path(location), await doDownload(url));
}