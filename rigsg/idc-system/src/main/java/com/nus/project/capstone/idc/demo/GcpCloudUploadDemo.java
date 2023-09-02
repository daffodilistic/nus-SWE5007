package com.nus.project.capstone.idc.demo;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class GcpCloudUploadDemo {

    public static void main(String[] args) throws IOException {

//        String uploadFilePAth = "D:\\java2\\MC_Invoice.pdf";
        String uploadFilePAth = "D:\\java2\\cny.mp4";
        byte[] fileData = FileUtils.readFileToByteArray(new File(uploadFilePAth));
        String contentType = Files.probeContentType(Paths.get(uploadFilePAth));
        String gcpBucketCredential = "D:\\java2\\nus-SWE5007\\rigsg\\idc-system\\src\\main\\resources\\dev-bucket-credential.json";
        String gcpProjectId = "shaped-splicer-397009";
        String gcpBucketId = "idc-system-resources";
        String gcpDirectoryName = "participants";

        InputStream inputStream = new FileInputStream(gcpBucketCredential);
        StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
        Storage storage = options.getService();
        Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());
        RandomString id = new RandomString(6, ThreadLocalRandom.current());
//        Blob blob = bucket.create(gcpDirectoryName + "/" + "test-file" + "-" + id.nextString() + ".pdf", fileData, contentType);
        Blob blob = bucket.create(gcpDirectoryName + "/" + "test-file" + "-" + id.nextString() + ".mp4", fileData, contentType);

        if(blob != null){
            System.out.println("File successfully uploaded to GCS");
        }
    }
}
