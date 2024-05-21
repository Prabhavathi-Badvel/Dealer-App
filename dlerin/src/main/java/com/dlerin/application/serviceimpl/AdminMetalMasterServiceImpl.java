package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dlerin.application.dto.AdminMetalMasterDto;
import com.dlerin.application.entity.AdminMetalMaster;
import com.dlerin.application.entity.DlerMaterialMaster;
import com.dlerin.application.repository.AdminMetalMasterRepo;
import com.dlerin.application.service.AdminMetalMasterService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class AdminMetalMasterServiceImpl implements AdminMetalMasterService {

	@Autowired
	AdminMetalMasterRepo adminMetalMasterRepo;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public AdminMetalMaster addMaterial(AdminMetalMaster adminMaterial) {
		
        if(adminMetalMasterRepo.findByMaterialId(adminMaterial.getMaterialId())== null){
		  return adminMetalMasterRepo.save(adminMaterial);
		}
	
	
	return null;
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
	public List<AdminMetalMaster> getMaterial(AdminMetalMaster adminMetals) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminMetalMaster> query = cb.createQuery(AdminMetalMaster.class);
		Root<AdminMetalMaster> root = query.from(AdminMetalMaster.class);
		List<Predicate> predicates = new ArrayList<>();

		if (adminMetals.getMaterialId() != null) {
			predicates.add(cb.equal(root.get("materialId"),adminMetals.getMaterialId() ));
		}
		if (adminMetals.getMaterialType() != null) {
			predicates.add(cb.equal(root.get("materialType"),adminMetals.getMaterialType()));
		}
		if (adminMetals.getMaterialShape() != null) {
			predicates.add(cb.equal(root.get("materialShape"), adminMetals.getMaterialShape()));
		}
		

		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

}
