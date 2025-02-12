package ru.clapClass.utils;

import java.io.File;

public class FileDelete {
    static public void deleteFile(String folderPath) {
        File directory = new File(folderPath);
        delete(directory, false);
    }

    static public void deleteFile(String folderPath, Boolean directoryDelete) {
        File directory = new File(folderPath);
        delete(directory, directoryDelete);

    }

    private static void delete(File directory, Boolean directoryDelete) {
        try {
            if (directory.exists()) {
                String[] entries = directory.list();
                assert entries != null;
                for (String s : entries) {
                    File currentFile = new File(directory.getPath(), s);
                    if (!currentFile.delete()) {
                        throw new RuntimeException("Failed to delete the file!");
                    }
                }

                if (directoryDelete) {
                    if (!directory.delete()) {
                        throw new RuntimeException("Failed to delete the file!");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}