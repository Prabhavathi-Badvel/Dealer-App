package com.dlerin.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dlerin.application.dto.ResponseDlerMaterialImagesAddDto;
import com.dlerin.application.dto.ResponseDlerMaterialImagesGetDto;
import com.dlerin.application.entity.DlerMaterialImages;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.repository.DlerMaterialImagesRepo;
import com.dlerin.application.serviceimpl.DlerMaterialImagesServiceImpl;

@RestController
public class DlerMaterialImagesController {

	@Autowired
	DlerMaterialImagesServiceImpl dlerMaterialImagesServiceImpl;

	@Autowired
	DlerMaterialImagesRepo dlerMaterialImagesRepo;

	@PostMapping("/dlerin-add-dlermaterialimages")
	public ResponseEntity<?> addDlerImages(@RequestBody DlerMaterialImages dlerImages) {

		DlerMaterialImages d = dlerMaterialImagesRepo.findByDlerIdMaterialId(dlerImages.getDlerIdMaterialId());
		try {

			ResponseDlerMaterialImagesAddDto rsImages = new ResponseDlerMaterialImagesAddDto();
			rsImages.setMessage("Images information added successfully");
			rsImages.setImageData(dlerMaterialImagesServiceImpl.addImages(dlerImages));
			return new ResponseEntity<>(rsImages, HttpStatus.OK);

		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("Record alredy exits", HttpStatus.OK);

		}

	}
	
	
	@PutMapping("/dlerin-update-dlermaterialimages")
	public ResponseEntity<?> updateDlerMaterialImages(@RequestBody DlerMaterialImages image) {

		try {
			Optional<DlerMaterialImages> exists = Optional
					.ofNullable(dlerMaterialImagesRepo.findByDlerIdMaterialId(image.getDlerIdMaterialId()));
			if (exists.isPresent()) {

				DlerMaterialImages updateDMM = dlerMaterialImagesServiceImpl.updateImages(image);
				return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Please check your dler id material id", HttpStatus.OK);
			}

		} catch (Exception e) {
			e.getMessage();
			return new ResponseEntity<>("invalid", HttpStatus.OK);
			
		}
	}
	
	@GetMapping("/dlerin-get-dlermaterialimages")
	public ResponseEntity<?> getDlerMaterialImages(@RequestBody DlerMaterialImages dImages){
		
	List<DlerMaterialImages> dl=dlerMaterialImagesServiceImpl.getImages(dImages);
		ResponseDlerMaterialImagesGetDto rgImages = new ResponseDlerMaterialImagesGetDto();
		rgImages.setMessage("images information");
		rgImages.setGetData(dl);
		return new ResponseEntity<>(rgImages, HttpStatus.OK);
		
	}
}

