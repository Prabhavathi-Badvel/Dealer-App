package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerMaterialImages;

public interface DlerMaterialImagesService {

	public DlerMaterialImages addImages(DlerMaterialImages images);
	public List<DlerMaterialImages> getImages(DlerMaterialImages images);
}
