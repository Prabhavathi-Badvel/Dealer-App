package com.dlerin.application.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dlerin.application.dto.ResponseDlerMaterialImagesAddDto;
import com.dlerin.application.dto.ResponseDlerMaterialImagesAddDto2;
import com.dlerin.application.dto.ResponseDlerMaterialImagesGetDto;
import com.dlerin.application.entity.DlerMaterialImages;
import com.dlerin.application.repository.DlerMaterialImagesRepo;
import com.dlerin.application.serviceimpl.DlerMaterialImagesServiceImpl;

@RestController
public class DlerMaterialImagesController {

	@Autowired
	DlerMaterialImagesServiceImpl dlerMaterialImagesServiceImpl;

	@Autowired
	DlerMaterialImagesRepo dlerMaterialImagesRepo;

//	@PostMapping("/dlerin-add-dlermaterialimages")
//	public ResponseEntity<?> addDlerImages(@RequestBody DlerMaterialImages dlerImages) {
//
//		DlerMaterialImages d = dlerMaterialImagesRepo.findByDlerIdMaterialId(dlerImages.getDlerIdMaterialId());
//		try {
//
//			ResponseDlerMaterialImagesAddDto rsImages = new ResponseDlerMaterialImagesAddDto();
//			rsImages.setMessage("Images information added successfully");
//			rsImages.setImageData(dlerMaterialImagesServiceImpl.addImages(dlerImages));
//			return new ResponseEntity<>(rsImages, HttpStatus.OK);
//
//		} catch (Exception e) {
//			e.getMessage();
//			return new ResponseEntity<>("Record alredy exits", HttpStatus.OK);
//
//		}
//
//	}
	@PostMapping("/dlerin-add-dlermaterialimages")
	public ResponseEntity<?> AddDealerStore(@RequestBody DlerMaterialImages images) {
		ResponseDlerMaterialImagesAddDto response = new ResponseDlerMaterialImagesAddDto();
		try {
			DlerMaterialImages dlerMaterialImages = dlerMaterialImagesServiceImpl
					.addDlerIdBusinessIdStoreIdImages(images);

			if (dlerMaterialImages != null) {
				response.setMessage("Dealer Store details added successfully");
				response.setMessage("Images information added successfully");
				response.setImageData(dlerMaterialImages);
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("Dealer not present");
				response.setMessage("Images information not added successfully");
				response.setImageData(dlerMaterialImages);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setMessage("Record already exist");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PostMapping("/uploadMultipleImage")
	public ResponseEntity<?> uploadDlerIdMaterialId(@RequestParam("dlerIdMaterialId") String dlerIdMaterialId,
			@RequestParam(value = "imageUrl1", required = false) MultipartFile imageUrl1,
			@RequestParam(value = "imageUrl2", required = false) MultipartFile imageUrl2,
			@RequestParam(value = "imageUrl3", required = false) MultipartFile imageUrl3) {
		return dlerMaterialImagesServiceImpl.uploadDlerIdMaterialId(dlerIdMaterialId, imageUrl1, imageUrl2, imageUrl3);
	}

	@GetMapping("/dler-material-details")
	public ResponseEntity<?> getDlerIdMaterialId(@RequestParam(required = false) String dlerId,
			@RequestParam(required = false) String materialId) {
		ResponseDlerMaterialImagesAddDto2 response1 = new ResponseDlerMaterialImagesAddDto2();

		try {
			List<DlerMaterialImages> getdlerId = dlerMaterialImagesServiceImpl.getDlerMaterailDetails(dlerId,
					materialId);
			if (getdlerId != null && !getdlerId.isEmpty()) {

				response1.setMessage("dealer material details");
				response1.setStatus(true);
				response1.setImageData(getdlerId);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			} else {
				response1.setMessage("No details found for provided parameters/check your parameters");
				response1.setStatus(false);
				response1.setImageData(getdlerId);
				return new ResponseEntity<>(response1, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}

//	@PutMapping("/dlerin-update-dlermaterialimages")
//	public ResponseEntity<?> updateDlerMaterialImages(@RequestBody DlerMaterialImages image) {
//
//		try {
//			Optional<DlerMaterialImages> exists = Optional
//					.ofNullable(dlerMaterialImagesRepo.findByDlerIdMaterialId(image.getDlerIdMaterialId()));
//			if (exists.isPresent()) {
//
//				DlerMaterialImages updateDMM = dlerMaterialImagesServiceImpl.updateImages(image);
//				return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>("Please check your dler id material id", HttpStatus.OK);
//			}
//
//		} catch (Exception e) {
//			e.getMessage();
//			return new ResponseEntity<>("invalid", HttpStatus.OK);
//			
//		}
//	}
//	
//	@GetMapping("/dlerin-get-dlermaterialimages")
//	public ResponseEntity<?> getDlerMaterialImages(@RequestBody DlerMaterialImages dImages) {
//
//		List<DlerMaterialImages> dl = dlerMaterialImagesServiceImpl.getImages(dImages);
//		ResponseDlerMaterialImagesGetDto rgImages = new ResponseDlerMaterialImagesGetDto();
//		rgImages.setMessage("images information");
//		rgImages.setGetData(dl);
//		return new ResponseEntity<>(rgImages, HttpStatus.OK);
//
//	}
}
