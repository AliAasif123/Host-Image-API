package com.code.service.impl;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.code.Repository.ImageRepository;
import com.code.entity.Image;
import com.code.service.ImageService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String FREEIMAGE_HOST_API_URL = "https://freeimage.host/api/1/upload";

    @Value("${freeimage.host.api.key}")
    private String apiKey;

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", apiKey);
            body.add("action", "upload");
            body.add("source", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(FREEIMAGE_HOST_API_URL, requestEntity,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String imageUrl = getImageUrlFromResponse(response.getBody());
                Image image = new Image();
                image.setImageUrl(imageUrl);
                imageRepository.save(image);
                return "Image uploaded successfully. Image URL: " + imageUrl;
            } else {
                return "Failed to upload image. Status code: " + response.getStatusCodeValue();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload image";
        }
    }

    private String getImageUrlFromResponse(String responseBody) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        if (jsonObject.has("status_code") && jsonObject.get("status_code").getAsInt() == 200) {
            JsonObject imageData = jsonObject.getAsJsonObject("image");
            if (imageData != null && imageData.has("url")) {
                return imageData.get("url").getAsString();
            } else {
                return "Image URL not found in the response";
            }
        } else {
            return "Error: " + jsonObject.get("status_txt").getAsString();
        }
    }

	@Override
	public List<Image> GetAllImage() {
		return imageRepository.findAll();
	}
}
