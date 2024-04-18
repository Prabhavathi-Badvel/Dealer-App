package com.dlerin.application.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.repository.AdminMetalMasterRepo;
import com.dlerin.application.service.AdminMetalMasterService;

@Service
public class AdminMetalMasterServiceImpl implements AdminMetalMasterService {

	@Autowired
	AdminMetalMasterRepo adminMetalMasterRepo;

	@Override
	public AdminMetalMasterDto addMaterial(AdminMetalMasterDto adminMaterialDto) {
		
		AdminMetalMaster adminMaterial = new AdminMetalMaster();

		
		adminMaterial.setLengthInUnits(adminMaterialDto.getLengthInUnits());
		adminMaterial.setMaterialId(adminMaterialDto.getMaterialId());
		adminMaterial.setMaterialLength(adminMaterialDto.getMaterialLength());
		adminMaterial.setMaterialShape(adminMaterialDto.getMaterialShape());
		adminMaterial.setMaterialThickness(adminMaterialDto.getMaterialThickness());
		adminMaterial.setMaterialType(adminMaterialDto.getMaterialType());
		adminMaterial.setMaterialWidth(adminMaterialDto.getMaterialWidth());
		adminMaterial.setThicknessUnits(adminMaterialDto.getThicknessUnits());
		adminMaterial.setWidthInUnits(adminMaterialDto.getWidthInUnits());
		
		AdminMetalMaster savedMaterial = adminMetalMasterRepo.save(adminMaterial);

		
		adminMaterialDto.setLengthInUnits(savedMaterial.getLengthInUnits());
		adminMaterialDto.setMaterialId(savedMaterial.getMaterialId());
		adminMaterialDto.setMaterialLength(savedMaterial.getMaterialLength());
		adminMaterialDto.setMaterialShape(savedMaterial.getMaterialShape());
		adminMaterialDto.setMaterialType(savedMaterial.getMaterialType());
		adminMaterialDto.setMaterialWidth(savedMaterial.getMaterialWidth());
		adminMaterialDto.setThicknessUnits(savedMaterial.getThicknessUnits());
		adminMaterialDto.setWidthInUnits(savedMaterial.getWidthInUnits());
		adminMaterialDto.setMaterialThickness(savedMaterial.getMaterialThickness());
		
		return adminMaterialDto;
	}

	@Override
	public AdminMetalMaster updateMaterial(AdminMetalMaster adminMaterial) {
		Optional<AdminMetalMaster> materialOptional = adminMetalMasterRepo.findById(adminMaterial.getMaterialId());
		if (materialOptional.isPresent()) {
			AdminMetalMaster materialToUpdate = materialOptional.get();
			
			materialToUpdate.setMaterialLength(adminMaterial.getMaterialLength() + adminMaterial.getLengthInUnits());
			materialToUpdate.setMaterialShape(adminMaterial.getMaterialShape());
			materialToUpdate
					.setMaterialThickness(adminMaterial.getMaterialThickness() + adminMaterial.getThicknessUnits());
			materialToUpdate.setMaterialType(adminMaterial.getMaterialType());
			materialToUpdate.setMaterialWidth(adminMaterial.getMaterialWidth() + adminMaterial.getWidthInUnits());
			materialToUpdate.setLengthInUnits(adminMaterial.getLengthInUnits());
			materialToUpdate.setThicknessUnits(adminMaterial.getThicknessUnits());
			materialToUpdate.setWidthInUnits(adminMaterial.getWidthInUnits());
			
			return adminMetalMasterRepo.save(materialToUpdate);
		} else {
			
			throw new IllegalArgumentException("Material with ID " + adminMaterial.getMaterialId() + " not found");
		}

	}

	@Override
	public List<AdminMetalMaster> getMaterial(String materialId, String materialType, String materialShape) {

		if (materialId != null & materialType == null && materialShape == null) {
			List<AdminMetalMaster> material = adminMetalMasterRepo.findByMaterialId(materialId);
			return material;
		} else if (materialId == null && materialType != null && materialShape == null) {
			List<AdminMetalMaster> material = adminMetalMasterRepo.findByMaterialType(materialType);
			return material;
		} else if (materialId == null && materialType == null && materialShape != null) {
			List<AdminMetalMaster> material = adminMetalMasterRepo.findByMaterialShape(materialShape);
			return material;
		}
		return null;
	}

}
