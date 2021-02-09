package com.WbReader.Services;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class UploadDirFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!Files.isDirectory(file)) {
            Files.delete(file);
        }
        return FileVisitResult.CONTINUE;
    }
}
