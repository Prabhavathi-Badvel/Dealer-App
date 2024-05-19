package com.dlerin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dlerin.application.entity.DlerMaterialImages;

@Repository
public interface DlerMaterialImagesRepo extends JpaRepository<DlerMaterialImages, String> {

	DlerMaterialImages findByDlerIdMaterialId(String dlerIdMaterialId);

	List<DlerMaterialImages> findByImageIdOrDlerIdMaterialId(String imageId, String dlerIdMaterialId);

}

