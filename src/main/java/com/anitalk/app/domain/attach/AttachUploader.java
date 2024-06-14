package com.anitalk.app.domain.attach;

import org.springframework.web.multipart.MultipartFile;

public interface AttachUploader {
    String uploadAttach(String filename, MultipartFile file) throws Exception;
    boolean deleteAttach(String key) throws Exception;
}
