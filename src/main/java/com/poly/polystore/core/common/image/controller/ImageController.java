package com.poly.polystore.core.common.image.controller;

import com.poly.polystore.core.common.image.model.response.ImageResponse;
import com.poly.polystore.core.common.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    @PostMapping("image/upload-temp")
    public List<ImageResponse> getUrl(@RequestParam("image") List<MultipartFile> imageName){
        try {
            return imageService.uploadTempImage(imageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
