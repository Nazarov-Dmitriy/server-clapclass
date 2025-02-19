package ru.clapClass.utils;

import java.nio.ByteBuffer;

public record File(String name, ByteBuffer content) {
}

