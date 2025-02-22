package ru.clapClass.service.s3;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
public class ServiceS3 {
    @Value("${BUCKET}")
    private String BUCKET;


    private final S3Client s3Client;

    public ServiceS3(@Value("${KEY_ID}") String KEY_ID, @Value("${SECRET_KEY}") String SECRET_KEY,
                     @Value("${REGION}") String REGION, @Value("${S3_ENDPOINT}")
                     String S3_ENDPOINT) {
        AwsCredentials credentials = AwsBasicCredentials.create(KEY_ID, SECRET_KEY);
        this.s3Client = S3Client.builder()
                .region(Region.of(REGION))
                .endpointOverride(URI.create(S3_ENDPOINT))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public boolean doesBucketExist(String bucketName) {
        HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                .bucket(BUCKET)
                .build();

        try {
            s3Client.headBucket(headBucketRequest);
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }

    //create a bucket
    public void createBucket(String bucketName) {
        CreateBucketRequest bucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();

        s3Client.createBucket(bucketRequest);
    }

    //delete a bucket
    public void deleteBucket(String bucketName) {
        try {
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.deleteBucket(deleteBucketRequest);
        } catch (S3Exception ignored) {
        }
    }

//

    public void putObject(String key, MultipartFile file) throws IOException {
        File scratchFile = File.createTempFile("prefix", "suffix");
        InputStream fileUpload= null;
        try {
            fileUpload = file.getInputStream();
            FileUtils.copyInputStreamToFile(file.getInputStream(), scratchFile);

            var body = RequestBody.fromInputStream(fileUpload, scratchFile.length());
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(putObjectRequest, body);
        } finally {
            if (scratchFile.exists()) {
                scratchFile.deleteOnExit();
            }
            assert fileUpload != null;
            fileUpload.close();
        }
    }

    public void deleteObject(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(BUCKET).key(key).build();

        s3Client.deleteObject(deleteObjectRequest);
    }
}
