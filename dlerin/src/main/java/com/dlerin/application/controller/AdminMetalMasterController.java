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
import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.dto.ResponseAdminMetalMasterDto;
import com.dlerin.application.dto.ResponseAdminMaterialGetDto;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.repository.AdminMetalMasterRepo;
import com.dlerin.application.serviceimpl.AdminMetalMasterServiceImpl;

@RestController
public class AdminMetalMasterController {

	@Autowired
	AdminMetalMasterServiceImpl adminMetalMaterServiceImpl;

	@Autowired
	AdminMetalMasterRepo adminMetalMasterRepo;

	@PostMapping("/dlerin-add-adminmetalmaster")
	public ResponseEntity<?> addAdminMaterial(@RequestBody AdminMetalMasterDto admin) {

		try {
			ResponseAdminMetalMasterDto responseADto = new ResponseAdminMetalMasterDto();
			responseADto.setMessage("Material added successfully");
			responseADto.setMaterialData(adminMetalMaterServiceImpl.addMaterial(admin));
			return new ResponseEntity<>(responseADto, HttpStatus.OK);

		} catch (Exception e) {

			return new ResponseEntity<>("Record alredy exits", HttpStatus.OK);
		}
	}

	@PutMapping("/dlerin-update-adminmetalmaster")
	public ResponseEntity<String> updateAdminMaterial(@RequestBody AdminMetalMaster adminMaterial) {

		Optional<AdminMetalMaster> materialIdExists = adminMetalMasterRepo.findById(adminMaterial.getMaterialId());
		try {
			if (materialIdExists.isPresent()) {

				AdminMetalMaster updatedMaterial = adminMetalMaterServiceImpl.updateMaterial(adminMaterial);
				return new ResponseEntity<>("Updated successfully", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Please check your material id", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}

	@GetMapping("/dlerin-get-adminmetalmaster")
	public ResponseEntity<?> getAdminMaterial(@RequestBody AdminMetalMasterDto adMaterialDto) {

		String materialId = adMaterialDto.getMaterialId();
		String materialType = adMaterialDto.getMaterialType();
		String materialShape = adMaterialDto.getMaterialShape();
		List<AdminMetalMaster> materialIdExists = adminMetalMasterRepo.findByMaterialIdOrMaterialTypeOrMaterialShape(materialId,
				materialType, materialShape);
		try {
			if (materialIdExists != null) {
				List<AdminMetalMaster> materialDto = adminMetalMaterServiceImpl.getMaterial(materialId, materialType, materialShape);
				ResponseAdminMaterialGetDto getDto = new ResponseAdminMaterialGetDto();
				getDto.setMessage("Successfully received the data from table");
				getDto.setGetData(adminMetalMaterServiceImpl.getMaterial(materialId, materialType, materialShape));
				return new ResponseEntity<>(getDto, HttpStatus.OK);
			}
			return new ResponseEntity<>("invalid material id", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}

	}
}