package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dlerin.application.config.AWSConfig;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerMaterialImages;
import com.dlerin.application.entity.ResponseModel;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerMaterialImagesRepo;
import com.dlerin.application.service.DlerMaterialImagesService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class DlerMaterialImagesServiceImpl implements DlerMaterialImagesService {

	@Autowired
	DlerMaterialImagesRepo dlerMaterialImagesRepo;

	@Autowired
	private DlerBusinessLoginRepo dlerBusinessLoginRepo;

	@Autowired
	private AWSConfig awsConfig;

	@PersistenceContext
	private EntityManager entityManager;
//	@Override
//	public DlerMaterialImages addImages(DlerMaterialImages images) {
//
//		if (dlerMaterialImagesRepo.findByDlerIdMaterialId(images.getDlerIdMaterialId()) == null) {
//			return dlerMaterialImagesRepo.save(images);
//		}
//		return null;
//
//	}

//	public DlerMaterialImages updateImages(DlerMaterialImages imagesD) {
//		
//		Optional<DlerMaterialImages> idExists = Optional.ofNullable(dlerMaterialImagesRepo.findByDlerIdMaterialId(imagesD.getDlerIdMaterialId()));
//		
//		if(idExists.isPresent()) {
//			DlerMaterialImages imageDb= idExists.get();
//			imageDb.setImageId(imagesD.getImageId());
//			imageDb.setImageUrl(imagesD.getImageUrl());
//			
//			return dlerMaterialImagesRepo.save(imageDb);
//			
//		}
//		return null;
//		
//	}

//	@Override
//	public List<DlerMaterialImages> getImages(DlerMaterialImages images) {
//		return dlerMaterialImagesRepo.findByDlerIdMaterialId(images.getDlerIdMaterialId());
//	}

	@Override
	public DlerMaterialImages addDlerIdBusinessIdStoreIdImages(DlerMaterialImages images) {
		// Fetch the DlerBusinessLogin using the dlerId from images
		Optional<DlerBusinessLogin> dlerIdExists = Optional
				.of(dlerBusinessLoginRepo.findByDlerUserId(images.getDlerId()));

		// Check if dlerIdExists is present
		if (dlerIdExists.isPresent()) {
			// Save the images if the dlerId exists
			return dlerMaterialImagesRepo.save(images);
		} else {
			// Handle the case where the dlerId does not exist
			throw new IllegalArgumentException("Dler ID does not exist: " + images.getDlerId());
		}
	}

	@Override
	public ResponseEntity<?> uploadDlerIdMaterialId(String dlerIdMaterialId, MultipartFile imageUrl1,
			MultipartFile imageUrl2, MultipartFile imageUrl3) {
		DlerMaterialImages dmi = dlerMaterialImagesRepo.findById(dlerIdMaterialId).orElse(null);

		ResponseModel response = new ResponseModel();
		if (dmi == null) {
			response.setError("Store not found");
			response.setMsg("Invalid dlerIdStoreId provided.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
//        String directoryPath = "store_docs/" + dlerIdStoreId + "/";
			String imageUrl1DirectoryPath = "dler/" + dlerIdMaterialId + "/material_images" + "/";
			if (imageUrl1 != null) {
				String imageUrl1DocumentPath = imageUrl1DirectoryPath + imageUrl1.getOriginalFilename();
				String imageUrl1Link = awsConfig.uploadFileToS3Bucket(imageUrl1DocumentPath, imageUrl1);
				dmi.setImageUrl1(imageUrl1Link);
			}

			if (imageUrl2 != null) {
				String imageUrl2DocumentPath = imageUrl1DirectoryPath + imageUrl2.getOriginalFilename();
				String imageUrl2Link = awsConfig.uploadFileToS3Bucket(imageUrl2DocumentPath, imageUrl2);
				dmi.setImageUrl2(imageUrl2Link);
			}
			if (imageUrl3 != null) {
				String imageUrl3DocumentPath = imageUrl1DirectoryPath + imageUrl3.getOriginalFilename();
				String imageUrl3Link = awsConfig.uploadFileToS3Bucket(imageUrl3DocumentPath, imageUrl3);
				dmi.setImageUrl3(imageUrl3Link);
			}

			dlerMaterialImagesRepo.save(dmi);
			response.setError("No Error");// No error
			response.setMsg("Documents uploaded successfully.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setError("Document upload failed");
			response.setMsg("An error occurred while uploading the documents: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<DlerMaterialImages> getDlerMaterailDetails(String dlerIdMaterialId,String dlerId, String materialId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<DlerMaterialImages> query = cb.createQuery(DlerMaterialImages.class);
		Root<DlerMaterialImages> root = query.from(DlerMaterialImages.class);
		List<Predicate> predicates = new ArrayList<>();

		if (dlerIdMaterialId != null) {
			predicates.add(cb.equal(root.get("dlerIdMaterialId"), dlerIdMaterialId));
		}
		if (dlerId != null) {
			predicates.add(cb.equal(root.get("dlerId"), dlerId));
		}
		if (materialId != null) {
			predicates.add(cb.equal(root.get("materialId"), materialId));
		}

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

}
