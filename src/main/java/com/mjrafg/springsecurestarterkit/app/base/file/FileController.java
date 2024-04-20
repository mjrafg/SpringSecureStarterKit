package com.mjrafg.springsecurestarterkit.app.base.file;


import com.mjrafg.springsecurestarterkit.base.controller.BaseController;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import com.mjrafg.springsecurestarterkit.utils.CommonUtils;
import com.mjrafg.springsecurestarterkit.utils.FileUtils;
import com.mjrafg.springsecurestarterkit.utils.MediaUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // produces = "text/plain;charset=UTF-8" : 한국어를 정상적으로 전송하기 위한 설정
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> fileUploadPOST(String type,boolean dateBase,boolean isDraw , MultipartFile file) throws Exception {
        try {
            String originalName = file.getOriginalFilename();

            FileEntity fileEntity = FileUtils.uploadFile(type, originalName, file.getBytes(),dateBase);
            fileEntity.setIsDraw(isDraw);
            FileEntity savedFile = fileService.save(fileEntity);
            return MainResponse.ok(savedFile);
        } catch (Exception e) {
            e.printStackTrace();
            return MainResponse.error("Upload failed");
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable String id) {
        Optional<FileEntity> optionalFile = fileService.findById(id);
        if (optionalFile.isPresent()) {
            FileEntity file = optionalFile.get();
            FileUtils.deleteFile(file.getSavedName(), file.getType());
        }
        return super.deleteById(id);
    }

    @RequestMapping(value = "/download")
    public ResponseEntity<?> fileDownload(@RequestParam("type") String type,@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileSystemResource resource = null;
        try {
            String sFilePath = FileUtils.getFilePath(type);
            if (CommonUtils.isNotEmpty(fileName)) {
                resource = new FileSystemResource(sFilePath +"/"+ fileName);
                HttpHeaders headers = new HttpHeaders();
                headers.set("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("EUC-KR"), "ISO-8859-1") + "\"");
                return ResponseEntity.ok()
                        .contentLength((int) resource.getFile().length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else return MainResponse.error("fileName is empty!!!");
        } catch (Exception e) {
            e.printStackTrace();
            return MainResponse.error(e.getMessage());
        }
    }

    @GetMapping("/display")
    public ResponseEntity<byte[]> fileDisplay(@RequestParam("type") String type, @RequestParam("fileName") String fileName) {
        if (CommonUtils.isEmpty(fileName)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            String filePath = FileUtils.getFilePath(type)+"/" + fileName;
            String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);
            MediaType mediaType = MediaUtils.getMediaType(formatName);
            HttpHeaders headers = new HttpHeaders();

            FileSystemResource resource = new FileSystemResource(filePath);
            if (!resource.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (mediaType != null) {
                // If the file is an image or media type, serve it directly
                headers.setContentType(mediaType);
                try (FileInputStream in = new FileInputStream(resource.getFile())) {
                    byte[] media = IOUtils.toByteArray(in);
                    return new ResponseEntity<>(media, headers, HttpStatus.OK);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // For non-media files, trigger download
                String mimeType = URLConnection.guessContentTypeFromName(fileName);
                mimeType = mimeType != null ? mimeType : "application/octet-stream";
                headers.setContentType(MediaType.parseMediaType(mimeType));
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setContentLength(resource.contentLength());

                try (FileInputStream in = new FileInputStream(resource.getFile())) {
                    byte[] fileContent = IOUtils.toByteArray(in);
                    return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
