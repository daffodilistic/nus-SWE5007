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

        byte[] fileData = FileUtils.readFileToByteArray(new File("D:\\java2\\123.txt"));
        String contentType = Files.probeContentType(Paths.get("D:\\java2\\123.txt"));
        String gcpBucketCredential = "D:\\java2\\nus-SWE5007\\rigsg\\idc-system\\src\\main\\resources\\sapient-symbol-388402-c394d7904e67.json";
        String gcpProjectId = "sapient-symbol-388402";
        String gcpBucketId = "idc-resources";
        String gcpDirectoryName = "participants";

        InputStream inputStream = new FileInputStream(gcpBucketCredential);
        StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
        Storage storage = options.getService();
        Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());
        RandomString id = new RandomString(6, ThreadLocalRandom.current());
        Blob blob = bucket.create(gcpDirectoryName + "/" + "test-file" + "-" + id.nextString() + ".txt", fileData, contentType);

        if(blob != null){
            System.out.println("File successfully uploaded to GCS");
        }
    }
}
