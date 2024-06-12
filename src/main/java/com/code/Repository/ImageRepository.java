package com.code.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
