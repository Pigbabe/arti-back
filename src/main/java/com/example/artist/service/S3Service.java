
package com.example.artist.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.example.artist.domain.Artists;
import com.example.artist.repository.ArtistRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {
    private AmazonS3 s3Client;

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);


    @Autowired
    private ArtistRepository artistRepository;
    @Value("${aws.accessKeyId}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;



    private final String bucketName = "artist-capston";
    @PostConstruct
    public void init() {
        try {
            this.s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                    .withRegion("ap-northeast-2")
                    .build();
        } catch (Exception e) {
            logger.error("Error initializing S3 client: ", e);
            throw e;
        }
    }

    public void uploadFile(String foldername, MultipartFile file) {
        String key = foldername + "/images" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {
            s3Client.putObject(bucketName, key, file.getInputStream(),metadata);
            s3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public String uploadFileToS3(MultipartFile file, String folder, String artistName) throws IOException {
        String fileKey = folder + "/" + artistName + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            s3Client.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));
            String fileUrl = s3Client.getUrl(bucketName, fileKey).toString();
            logger.info("File uploaded to S3: {}", fileUrl);

            // DB에 파일 URL 저장
            Artists artist = new Artists();
            artist.setArtistName(artistName); // 실제 아티스트 이름을 설정
            artist.setImageUrl(fileUrl);
            artistRepository.save(artist);

            return fileUrl;
        } catch (Exception e) {
            logger.error("Error uploading file to S3: ", e);
            throw e;
        }
    }


    public void createFolder(String name) { //S3에 폴더 추가
        String folderKey = name + "/";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        ByteArrayInputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        PutObjectRequest putObjectRequest = new PutObjectRequest("artist-capston", folderKey, emptyContent, metadata);

        try {
            s3Client.putObject(putObjectRequest);
            logger.info("Folder created in S3: {}", folderKey);
        } catch (Exception e) {
            logger.error("Error creating folder in S3: ", e);
            throw e;
        }
    }

    public List<String> getTopLevelFolders() {
        List<String> topLevelFolders = new ArrayList<>();
        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withDelimiter("/");

        ListObjectsV2Result result = s3Client.listObjectsV2(listObjectsRequest);

        for (String prefix : result.getCommonPrefixes()) {
            topLevelFolders.add(prefix.replace("/", ""));
        }
        return topLevelFolders;
    }


    public List<String> getArtistImages(String artistName) {
        List<String> imageUrls = new ArrayList<>();
        String prefix = artistName + "/images/";

        ListObjectsV2Request listObjectsRequest = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        ListObjectsV2Result result = s3Client.listObjectsV2(listObjectsRequest);

        for (S3ObjectSummary summary : result.getObjectSummaries()) {
            if (summary.getKey().endsWith(".jpg") || summary.getKey().endsWith(".jpeg") || summary.getKey().endsWith(".png")) {
                imageUrls.add("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + summary.getKey());
            }
        }
        return imageUrls;
    }
}
