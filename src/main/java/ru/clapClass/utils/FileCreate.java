package ru.clapClass.utils;

import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.domain.models.file.FileModel;
import ru.clapClass.exception.InternalServerError;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class FileCreate {
    public static FileModel addFile(MultipartFile file, StringBuilder path) {
        if (!file.isEmpty()) {
            try {
                long fileLength = file.getSize();
                String name = file.getOriginalFilename();

                FolderCreate.createFolder(String.valueOf(path));
                createFile(file, String.valueOf(path), name);

                var fullPath = path.append(name);

                return FileModel.builder().name(name)
                        .path(String.valueOf(fullPath)).size(fileLength).build();
            } catch (Exception e) {
                throw new InternalServerError(" Error getting file list");
            }
        }
        return null;
    }

    public static FileModel addFileS3(MultipartFile file, StringBuilder path) {
        try {
            long fileLength = file.getSize();
            String name = file.getOriginalFilename();
            return FileModel.builder().name(name)
                    .path(path.toString()).size(fileLength).build();
        } catch (Exception e) {
            throw new InternalServerError(" Error getting file list");
        }

    }


    public static void createFile(MultipartFile file, String path, String name) {
        MemoryStats.log();

        if (!file.isEmpty()) {
            try {
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(path + name));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                throw new InternalServerError(" Error getting file list");
            }
        }
    }
}
