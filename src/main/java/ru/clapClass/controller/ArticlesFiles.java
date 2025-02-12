package ru.clapClass.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.clapClass.servise.s3.ServiceS3;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article-files")
public class ArticlesFiles {
    private final ServiceS3 serviceS3;

    @PostMapping(path = "/add")
    public boolean addFile(Long id, MultipartFile file) throws IOException {
        String key = "article/" + id + "/" + file.getOriginalFilename();

        var a = serviceS3.putObject(key, file);
        return true;
    }

    @PostMapping(path = "/delete")
    public boolean deleteFile(Long id, String file) throws IOException {
        String key = "article/" + id + "/" + file;

        var a = serviceS3.deleteObject(key, file);
        return true;
    }

}
