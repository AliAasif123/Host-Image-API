package com.code.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.code.entity.Image;
import com.code.service.ImageService;

@RestController
@RequestMapping("/api/image")
public class ImageUploadController {

	@Autowired
	private ImageService imageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadImage(@RequestParam("file") MultipartFile file) {
		return imageService.uploadImage(file);
	}

	@GetMapping
	public List<Image> getAllImages() {
		return imageService.GetAllImage();
	}
}