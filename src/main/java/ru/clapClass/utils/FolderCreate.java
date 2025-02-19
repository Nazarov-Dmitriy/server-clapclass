package ru.clapClass.utils;

import java.io.File;

public class FolderCreate {
    private final static String[] listFolder = {"files", "files/reviews"};

    static public void createFolder(String folderPath) {
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    static public void initialFolder() {
        for (var folder : listFolder) {
            createFolder(folder);
        }
    }
}