package com.mjrafg.springsecurestarterkit.app.base.file;


import com.mjrafg.springsecurestarterkit.base.controller.BaseController;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import com.mjrafg.springsecurestarterkit.utils.CommonUtils;
import com.mjrafg.springsecurestarterkit.utils.FileUtils;
import com.mjrafg.springsecurestarterkit.utils.MediaUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


import java.io.*;
import java.net.URLConnection;
import java.util.Optional;

@RestController
@RequestMapping("/api/file")
public class FileController extends BaseController<FileEntity, String> {


    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        super(fileService);
        this.fileService = fileService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> fileUploadPOST(String type, boolean dateBase, boolean isDraw, MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return MainResponse.error("File is empty");
        }
        try {
            FileEntity fileEntity = FileUtils.uploadFile(type, file.getOriginalFilename(), file.getBytes(), dateBase);
            fileEntity.setIsDraw(isDraw);
            FileEntity savedFile = fileService.save(fileEntity);
            return MainResponse.ok(savedFile);
        } catch (IOException e) {
            return MainResponse.error("Error uploading file: " + e.getMessage());
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        return fileService.findById(id)
                .map(file -> {
                    FileUtils.deleteFile(file.getSavedName(), file.getType());
                    return super.deleteById(id);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("type") String type,
                                          @RequestParam("fileName") String fileName) {
        if (CommonUtils.isEmpty(fileName)) {
            return MainResponse.error("File name is required");
        }

        String filePath = FileUtils.getFilePath(type) + "/" + fileName;
        FileSystemResource resource = new FileSystemResource(filePath);

        if (!resource.exists()) {
            return MainResponse.error("File not found");
        }

        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        mimeType = mimeType != null ? mimeType : "application/octet-stream";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.setContentType(MediaType.parseMediaType(mimeType));

        try {
            byte[] fileContent = IOUtils.toByteArray(new FileInputStream(resource.getFile()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileContent.length)
                    .body(fileContent);
        } catch (IOException e) {
            return MainResponse.error("Error downloading file: " + e.getMessage());
        }
    }

    @GetMapping("/display")
    public ResponseEntity<?> displayFile(@RequestParam("type") String type,
                                         @RequestParam("fileName") String fileName) {
        if (CommonUtils.isEmpty(fileName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String filePath = FileUtils.getFilePath(type) + "/" + fileName;
        FileSystemResource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
        MediaType mediaType = MediaUtils.getMediaType(formatName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType != null ? mediaType : MediaType.APPLICATION_OCTET_STREAM);

        try (FileInputStream in = new FileInputStream(resource.getFile())) {
            byte[] media = IOUtils.toByteArray(in);
            return new ResponseEntity<>(media, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
