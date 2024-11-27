package com.dlerin.application.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.dlerin.application.entity.DlerMaterialImages;

public interface DlerMaterialImagesService {

//	public DlerMaterialImages addImages(DlerMaterialImages images);
//	public List<DlerMaterialImages> getImages(DlerMaterialImages images);
	public DlerMaterialImages addDlerIdBusinessIdStoreIdImages(DlerMaterialImages images);
	public ResponseEntity<?> uploadDlerIdMaterialId(String materialIddlerIdBusinessIdStoreId,
			MultipartFile imageUrl1, MultipartFile imageUrl2, MultipartFile imageUrl3);
	public List<DlerMaterialImages> getDlerMaterailDetails(String dlerId,String materialId);
	}
