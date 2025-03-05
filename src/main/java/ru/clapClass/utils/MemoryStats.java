package ru.clapClass.utils;

import lombok.Data;

@Data
public class MemoryStats {

    public static void log() {
        System.out.println(size(Runtime.getRuntime().totalMemory(), "totalMemory"));
        System.out.println(size(Runtime.getRuntime().maxMemory(), "maxMemory"));
        System.out.println(size(Runtime.getRuntime().freeMemory(), "freeMemory"));
    }

    public static void clear() {
        log();
    }


    public static String size(long val, String name) {
        return "Maximum Heap Size " + name+  ": " + (val / (1024 * 1024)) + " MB";
    }
}
