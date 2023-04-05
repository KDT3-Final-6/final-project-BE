package com.travel.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.travel.image.entity.Image;
import com.travel.image.exception.ImageException;
import com.travel.image.exception.ImageExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class FileUploadService {
    private final AmazonS3 amazonS3;

    private static final int CAPACITY_LIMIT_BYTE = 1024 * 1024 * 10;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public List<Image> uploads(List<MultipartFile> multipartFiles) throws IOException {
        List<Image> images = new ArrayList<>();
        if (multipartFiles.isEmpty()) {
            return images;
        }
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                images.add(upload(multipartFile));
            }
        }
        return images;
    }

    public Image upload(MultipartFile multipartFile) throws IOException {

        if (multipartFile.isEmpty()) {
            return null;
        }

        int end = Math.min(multipartFile.getOriginalFilename().length(), 20);
        String originName = multipartFile.getOriginalFilename().substring(0, end);
        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        // s3에 저장되는 파일이름 중복안되게 하기

        String ext = extractExt(s3FileName);
        String contentType = "";

        //파일크기 용량제한 넘으면 예외 던지기
        if (multipartFile.getSize() > CAPACITY_LIMIT_BYTE) {
            throw new ImageException(ImageExceptionType.EXCEED_LIMIT_SIZE);
        }

        switch (ext) {
            case "jpeg":
                contentType = "image/jpeg";
                break;
            case "png":
                contentType = "image/png";
                break;
            case "txt":
                contentType = "text/plain";
                break;
            case "csv":
                contentType = "text/csv";
                break;
        }

        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            objMeta.setContentType(contentType);
            objMeta.setContentLength(multipartFile.getInputStream().available()); //파일의 사이즈 S3에 알려주기
            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta); //S3 API 메소드 이용해서 S3에 파일 업로드
            multipartFile.getInputStream().close();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return Image.builder()
                .imageName(s3FileName)
                .imageOriginalName(multipartFile.getOriginalFilename())
                .imageFormat(ext)
                .imagePath(amazonS3.getUrl(bucket, s3FileName).toString())
                .imageBytes(multipartFile.getSize())
                .build();
    }

    /*  사용자가 업로드한 파일에서 확장자를 추출한다.   */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}