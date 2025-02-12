package ru.clapClass.utils;

import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
public class File {
    private String name;
    private ByteBuffer content;

    public File(String name, ByteBuffer content) {
        this.name = name;
        this.content = content;
    }}

