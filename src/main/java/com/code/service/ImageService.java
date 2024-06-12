package com.code.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.code.entity.Image;

public interface ImageService {
	String uploadImage(MultipartFile file);

	 List<Image> GetAllImage();
}