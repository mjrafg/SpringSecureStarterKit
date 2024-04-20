package com.mjrafg.springsecurestarterkit.app.base.file;

import com.mjrafg.springsecurestarterkit.base.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService extends BaseServiceImpl<FileEntity,String> {
    FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        super(fileRepository);
        this.fileRepository = fileRepository;
    }
}
