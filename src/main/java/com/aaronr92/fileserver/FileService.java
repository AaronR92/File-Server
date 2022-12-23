package com.aaronr92.fileserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collection;

@Service
@Slf4j
public class FileService {
    private final FileRepository repository;
    private Collection<String> allowedExtensions;

    public FileService(FileRepository repository, Environment environment) {
        this.repository = repository;
        allowedExtensions = Arrays.asList(environment.getProperty("application.allowed-extensions")
                .split(","));
    }

    /**
     * Returns a file by its name
     * @param fileName name of a file
     * @return file
     */
    public Resource getFile(String fileName) {
        return repository.findFile(fileName);
    }

    /**
     * Saves a file
     * @param file file to save
     * @return saved file name
     */
    public String saveFile(MultipartFile file) {
        log.info(file.getContentType());

        if (!allowedExtensions.contains(StringUtils.getFilenameExtension(file.getOriginalFilename()))) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                    "This file cannot be processed by the server");
        }

        return repository.save(file);
    }

    /**
     * Deletes file from server
     * @param fileName name of the file to delete
     */
    public void deleteFile(String fileName) {
        repository.delete(fileName);
    }
}
