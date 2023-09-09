package com.walk.aroundyou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.walk.aroundyou.domain.UploadImage;

@Repository
public interface UploadImageRepository 
			extends JpaRepository<UploadImage, Long>{

}
