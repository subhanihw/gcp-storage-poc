package org.nobita.googlestoragepoc.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SampleService
{
    private final Storage storage;
    private final String bucketName = "nobita_bucket";
    private final String localFilePath = "C:\\Users\\SAILS-DM273\\OneDrive - Sails Software Solutions Pvt Ltd\\Documents\\";

    public void saveFileToGC(MultipartFile file) throws IOException
    {
        if (file.isEmpty())
            throw new FileNotFoundException("File Not Found");

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name is null");
        }

        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        byte[] data = file.getBytes();
        storage.create(blobInfo, data);
    }

    public byte[] downloadFileFromGC(String fileName) throws IOException {
        Blob blob = storage.get(bucketName, fileName);

        if (blob == null) {
            throw new FileNotFoundException("File not found in Google Cloud Storage.");
        }

        String localPathFileName = localFilePath + fileName;
        try (FileOutputStream fileOutputStream = new FileOutputStream(localPathFileName)) {
            byte[] content = blob.getContent();
            fileOutputStream.write(content);
            System.out.println("File downloaded and saved to: " + localPathFileName);
        }
        return blob.getContent();
    }
}
