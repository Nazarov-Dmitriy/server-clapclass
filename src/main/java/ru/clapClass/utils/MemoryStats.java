package ru.clapClass.utils;

import lombok.Data;

@Data
public class MemoryStats {

    public static void log() {
        System.out.println(size(Runtime.getRuntime().totalMemory()));
        System.out.println(size(Runtime.getRuntime().maxMemory()));
        System.out.println(size(Runtime.getRuntime().freeMemory()));
    }

    public static String size(long val) {
        return "Maximum Heap Size: " + (val / (1024 * 1024)) + " MB";
    }
}
