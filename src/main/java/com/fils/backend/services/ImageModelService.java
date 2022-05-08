package com.fils.backend.services;

import com.fils.backend.domain.ImageModel;
import com.fils.backend.repositories.ImageModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageModelService {
    @Autowired
    ImageModelRepository imageModelRepository;

    public ImageModel uploadImage(MultipartFile file) throws IOException {
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        final ImageModel savedImage = imageModelRepository.save(img);
        return savedImage;
    }
}
