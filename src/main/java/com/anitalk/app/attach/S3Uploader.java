package com.anitalk.app.attach;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Uploader implements AttachUploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${aws.url}")
    private String awsUrl;

    @Override
    public String uploadAttach(String filename, MultipartFile file) throws Exception {
        try{
            String url = awsUrl + "/" + bucket + "/" + filename;

            ObjectMetadata data = new ObjectMetadata();
            data.setContentType(file.getContentType());
            data.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, filename, file.getInputStream(), data);

            return url;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new Exception("aws file upload fail");
        }
    }

    @Override
    public boolean deleteAttach(String key) throws Exception {
        try{
            amazonS3Client.deleteObject(bucket,key);
            return true;
        } catch (Exception e){
            log.error(e.getMessage());
            throw new Exception("aws file delete fail");
        }
    }
}
