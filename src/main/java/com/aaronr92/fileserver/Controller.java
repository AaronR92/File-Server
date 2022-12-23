package com.aaronr92.fileserver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/file")
public class Controller {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<Resource> getFile(@RequestParam("file_name") String fileName) {
        Resource resource = fileService.getFile(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<String> saveFile(@RequestParam MultipartFile file) {
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("api/v1/file")
                .toUriString());
        return ResponseEntity.created(uri).body(fileService.saveFile(file));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestParam("file_name") String fileName) {
        fileService.deleteFile(fileName);
        return ResponseEntity.noContent().build();
    }
}
