package ru.clapClass.domain.dto.file;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ru.clapClass.domain.models.file.FileModel}
 */
@Value
public class FileModelDto implements Serializable {
    long id;
    String name;
    String path;
    long size;
}