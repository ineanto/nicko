package net.artelnatif.nicko.utils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean isPresentOrCreate(File file) throws IOException {
        if (!file.exists()) {
            return file.createNewFile();
        }
        return true;
    }
}
