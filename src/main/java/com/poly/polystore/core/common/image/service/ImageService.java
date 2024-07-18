package com.poly.polystore.core.common.image.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.poly.polystore.core.common.image.model.response.ImageResponse;
import com.poly.polystore.repository.AnhRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AnhRepository anhRepository;
    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;
    @Value("${spring.cloud.azure.storage.blob.temp-container-name}")
    private String tempContainerName;
    @Value("${spring.cloud.azure.storage.connection-string}")
    private String connectionString;

    private BlobServiceClient blobServiceClient;

    @PostConstruct
    public void init() {
        System.out.println(connectionString);
        blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
    public List<ImageResponse> uploadTempImage(List<MultipartFile> images) throws IOException {
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(tempContainerName);
        List<ImageResponse> lstImageResponse = new ArrayList<>();
        for (MultipartFile image : images) {
            String fileName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
                BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
            blobClient.upload(image.getInputStream(), image.getSize(), true);
            var url=blobClient.getBlobUrl();
            lstImageResponse.add(new ImageResponse(fileName,url));
        }

        return lstImageResponse;
    }

    public void deleteTempImage(String fileName) {
        BlobClient blobClient = blobServiceClient
                .getBlobContainerClient(tempContainerName)
                .getBlobClient(fileName);

        blobClient.delete();
    }

    public String moveImageToPermanent(String fileName) {
        BlobClient tempBlobClient = blobServiceClient
                .getBlobContainerClient(tempContainerName)
                .getBlobClient(fileName);

        BlobClient permBlobClient = blobServiceClient
                .getBlobContainerClient(containerName)
                .getBlobClient(fileName);

        permBlobClient.beginCopy(tempBlobClient.getBlobUrl(), null);
//        tempBlobClient.delete();
        return permBlobClient.getBlobUrl();
    }

}
