package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.DlerMaterialImages;
import com.dlerin.application.repository.DlerMaterialImagesRepo;
import com.dlerin.application.service.DlerMaterialImagesService;

@Service
public class DlerMaterialImagesServiceImpl implements DlerMaterialImagesService {

	@Autowired
	DlerMaterialImagesRepo dlerMaterialImagesRepo;

	@Override
	public DlerMaterialImages addImages(DlerMaterialImages images) {

		if (dlerMaterialImagesRepo.findByDlerIdMaterialId(images.getDlerIdMaterialId()) == null) {
			return dlerMaterialImagesRepo.save(images);
		}
		return null;

	}

	
	public DlerMaterialImages updateImages(DlerMaterialImages imagesD) {
		
		Optional<DlerMaterialImages> idExists = Optional.ofNullable(dlerMaterialImagesRepo.findByDlerIdMaterialId(imagesD.getDlerIdMaterialId()));
		
		if(idExists.isPresent()) {
			DlerMaterialImages imageDb= idExists.get();
			imageDb.setImageId(imagesD.getImageId());
			imageDb.setImageUrl(imagesD.getImageUrl());
			
			return dlerMaterialImagesRepo.save(imageDb);
			
		}
		return null;
		
	}
	
	
	
	
	
	@Override
	public List<DlerMaterialImages> getImages(DlerMaterialImages images){
		return dlerMaterialImagesRepo.findByImageIdOrDlerIdMaterialId(images.getImageId(),images.getDlerIdMaterialId()) ;
		
	}
}
