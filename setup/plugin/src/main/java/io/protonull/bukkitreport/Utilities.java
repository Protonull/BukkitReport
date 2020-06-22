package io.protonull.bukkitreport;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.bukkit.Material;

public class Utilities {

    @FunctionalInterface
    public interface BoolSupplier {
        boolean get();
    }


    public static <T> boolean isNullOrEmpty(T... entries) {
        return entries == null || entries.length <= 0;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() <= 0;
    }

    public static boolean hasRequiredClasses(String... classes) {
        if (isNullOrEmpty(classes)) {
            return true; // There's no classes to load, therefore all at loaded ;)
        }
        for (String classPath : classes) {
            if (isNullOrEmpty(classPath)) {
                continue;
            }
            try {
                Class.forName(classPath);
            }
            catch (ClassNotFoundException ignored) {
                return false;
            }
        }
        return true;
    }

}
