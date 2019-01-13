package com.trx0eth7.tool.scanner;

import com.trx0eth7.exception.FileScannerException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.toList;

public class YamlFileScanner implements FileScanner {
    @Override
    public List<String> scan(String pathDir) {
        try (Stream<Path> filePathStream = walk(get(pathDir))) {
            return filePathStream.filter(this::yamlExtensionFilter)
                    .map(Path::toString)
                    .collect(toList());
        } catch (IOException e) {
            throw new FileScannerException(e);
        }
    }

    //TODO check: getFileName instead toStrong
    private boolean yamlExtensionFilter(Path path) {
        return path.getFileName().endsWith(".yaml");
    }
}
