package com.aaronr92.fileserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@Slf4j
public class FileRepository {
    @Value("${application.file-directory}")
    private String sFileDir;

    /**
     * Find file by its name
     * @param fileName name of the file to find
     * @return file
     */
    public Resource findFile(String fileName) {
        String sPath = sFileDir + "\\" + fileName;

        if (!new File(sPath).exists())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        Resource resource = null;

        try {
            resource = new UrlResource(Paths.get(sPath).toUri());
        } catch (MalformedURLException e) {
            log.error("File read error");
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resource;
    }

    /**
     * Saves the file
     * @param file file to save
     * @return saved file name
     */
    public String save(MultipartFile file) {
        String filename = file.hashCode() + "." + StringUtils
                .getFilenameExtension(file.getOriginalFilename());
        Path path = Paths.get(sFileDir + "\\" + filename);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            log.error("File save error");
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return path.toString();
    }

    /**
     * Deletes file from server
     * @param fileName name of the file to delete
     */
    public void delete(String fileName) {
        try {
            Files.delete(Paths.get(sFileDir + "\\" + fileName));
        } catch (NoSuchFileException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            log.error("File delete error");
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
