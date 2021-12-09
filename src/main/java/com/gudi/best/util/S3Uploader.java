package com.gudi.best.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public HashMap<String, Object> upload(MultipartFile multipartFile) throws Exception {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));
        return upload(uploadFile);
    }

    // S3로 파일 업로드하기
    private HashMap<String, Object> upload(File uploadFile) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String fileName = UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String path = putS3(uploadFile, "imgs/" + fileName); // s3로 업로드
        removeNewFile(uploadFile);
        map.put("path", path);
        map.put("oriFileName", uploadFile.getName());
        map.put("newFileName", fileName);
        return map;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            logger.info("File delete success");
            return;
        }
        logger.info("File delete fail");
    }

    // 로컬에 파일 업로드 하기
    private Optional<File> convert(MultipartFile file) throws Exception {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // s3 파일 삭제하기
    public void delete(String newFileName) {
        boolean isExistObject = amazonS3Client.doesObjectExist(bucket, "imgs/" + newFileName);
        if (isExistObject == true) {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, "imgs/" + newFileName));
        }
    }
}
