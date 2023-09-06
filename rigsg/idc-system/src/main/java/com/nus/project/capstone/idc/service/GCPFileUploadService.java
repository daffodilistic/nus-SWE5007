package com.nus.project.capstone.idc.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.nus.project.capstone.model.entity.base.GeneralMessageEntity;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class GCPFileUploadService {

    private static final Logger logger = LogManager.getLogger(GCPFileUploadService.class);
    private static final String PRIM = "Preliminary";
    public static final String PRIM_FILE_SUFFIX = ".pdf";
    private static final String PROMO = "Promotional";
    public static final String PROMO_FILE_SUFFIX = ".mp4";
    private final String gcpDirectoryName;
    private Bucket bucket;

    @Autowired
    public GCPFileUploadService(
            @Value("${spring.cloud.gcp.bucket.credential}") String gcpBucketCredential,
            @Value("${spring.cloud.gcp.project-id}") String gcpProjectId,
            @Value("${spring.cloud.gcp.bucket.id}") String gcpBucketId,
            @Value("${spring.cloud.gcp.bucket.dirName}") String gcpDirectoryName) {

        this.gcpDirectoryName = gcpDirectoryName;

        try (InputStream inputStream = new FileInputStream(ResourceUtils.getFile(gcpBucketCredential))) {
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            Storage storage = options.getService();
            bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());
        } catch (IOException ie) {
            logger.error("Unable to load GCP Bucket credential at: [{}]", gcpBucketCredential);
        }

    }

    public ResponseEntity<GeneralMessageEntity> uploadFileToGCP(MultipartFile file, String teamName){

        try{
            Path filePath = new File(file.getOriginalFilename()).toPath();
            byte[] fileData = FileUtils.readFileToByteArray(convertFile(file));
            String contentType = Files.probeContentType(filePath);
            String fileName = constructFileName(gcpDirectoryName, teamName, getRound(filePath.toString()));
            logger.info("Going to upload to [{}] with contentType [{}]", fileName, contentType);

            Blob blob = bucket.create(fileName, fileData, contentType);
            if(blob != null){
                return ResponseEntity.ok(GeneralMessageEntity.builder()
                        .data("File successfully uploaded to GCS").build());
            } else {
                return ResponseEntity.ok(GeneralMessageEntity.builder()
                        .data("Failed. Unknown error happened.").build());
            }
        } catch (Exception e){
            return ResponseEntity.ok(GeneralMessageEntity.builder()
                    .data(e.getMessage()).build());
        }
    }

    private String constructFileName(String gcpDirectoryName,
                                            String teamName,
                                            String round) throws Exception {
        String fileName = gcpDirectoryName + "/" + teamName + "-" + round + "-" + System.currentTimeMillis();
        if (round.equals(PRIM)){
            return fileName + PRIM_FILE_SUFFIX;
        } else if (round.equals(PROMO)){
            return fileName + PROMO_FILE_SUFFIX;
        } else {
            throw new Exception("Unknown round: " + round);
        }
    }

    private String getRound(String uploadFilePath) throws Exception {
        if (uploadFilePath.contains(PRIM_FILE_SUFFIX)){
            return PRIM;
        } else if (uploadFilePath.contains(PROMO_FILE_SUFFIX)){
            return PROMO;
        } else {
            throw new Exception("Invalid file type uploaded! " +
                    "Please upload only .pdf for preliminary round and .mp4 for promotional round");
        }
    }
    private File convertFile(MultipartFile file) throws Exception {
        try{
            File convertedFile = new File(file.getOriginalFilename());
            FileOutputStream outputStream = new FileOutputStream(convertedFile);
            outputStream.write(file.getBytes());
            outputStream.close();
            return convertedFile;
        }catch (Exception e){
            throw new Exception("An error has occurred while converting the file");
        }
    }

}
