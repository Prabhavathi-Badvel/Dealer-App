package com.dlerin.application.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.entity.AdminBusinessCategory;
import com.dlerin.application.entity.AdminLogin;
import com.dlerin.application.entity.AdminUiEndPoint;
import com.dlerin.application.repository.AdminLoginRepo;
import com.dlerin.application.repository.AdminUiEndPointRepo;
import com.dlerin.application.service.AdminUiEndPointService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public  class AdminUiEndPointServiceImpl implements AdminUiEndPointService {

	@Autowired
	private AdminLoginRepo adminLoginRepo;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AdminUiEndPointRepo adminUiEndPointRepo;
	
	@Override
	public AdminUiEndPoint addAdminUiEndPoint(AdminUiEndPoint adminUiEndPoint, String name) {
		Optional<AdminLogin> login = adminLoginRepo.findByName(name);

		if (login.isPresent()) {
			AdminLogin adminDb = login.get();
			AdminUiEndPoint existingAdminUiEndPoint = adminUiEndPointRepo
					.findByUpdatedBy(adminUiEndPoint.getUpdatedBy());
			if (existingAdminUiEndPoint == null) {
				adminUiEndPoint.setUpdatedBy(adminDb.getName());
				
				return adminUiEndPointRepo.save(adminUiEndPoint);
			}
		}
		return null;
	}

	@Override
	public List<AdminUiEndPoint> getAdminUiEndPoint(String systemId, String ipUrlToUi, String updatedBy) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminUiEndPoint> query = cb.createQuery(AdminUiEndPoint.class);
		Root<AdminUiEndPoint> root = query.from(AdminUiEndPoint.class);
		List<Predicate> predicates = new ArrayList<>();
		if(systemId!=null) {
			predicates.add(cb.equal(root.get("systemId"), systemId));

		}
		
		if (ipUrlToUi != null) {
			predicates.add(cb.equal(root.get("ipUrlToUi"), ipUrlToUi));
		}
		if (updatedBy != null) {
			predicates.add(cb.equal(root.get("updatedBy"), updatedBy));
		}
		query.where(predicates.toArray(new Predicate[0]));

		return entityManager.createQuery(query).getResultList();
	}

	@Override
	public AdminUiEndPoint update(AdminUiEndPoint adminUiEndPoint) {
		Optional<AdminUiEndPoint> adminExists = Optional.ofNullable(
				adminUiEndPointRepo.findBySystemId(adminUiEndPoint.getSystemId()));

		if (adminExists.isPresent()) {
			AdminUiEndPoint Db = adminExists.get();
			Db.setIpUrlToUi(adminUiEndPoint.getIpUrlToUi());
			Db.setSystemId(adminUiEndPoint.getSystemId());
//			Db.setUpdatedBy(adminUiEndPoint.getUpdatedBy());
			return adminUiEndPointRepo.save(Db);
		}
		return null;
	}

}
