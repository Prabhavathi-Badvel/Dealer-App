package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
import com.dlerin.application.dto.ResponseCombinedAdminStorAndStorMem;
import com.dlerin.application.dto.ResponseCombinedDealerBrandsDto;
import com.dlerin.application.dto.UpdateAdminStoreRequest;
import com.dlerin.application.entity.AdminBrandMaster;
import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.entity.DealerBrands;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerStoreDetails;
import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.entity.StoreMembership;
import com.dlerin.application.entity.VerificationStatus;

import com.dlerin.application.exception.ResourceNotFoundException;
import com.dlerin.application.repository.AdminStoreVerificationRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerStoreDetailsRepo;
import com.dlerin.application.repository.PlanMembershipRepo;
import com.dlerin.application.repository.StoreMembershipRepo;
import com.dlerin.application.service.AdminStoreVerificationService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Service
public class AdminStoreVerificationServiceImpl implements AdminStoreVerificationService {

	@Autowired
	private DlerStoreDetailsRepo dlerStoreDetailsRepo;

	@Autowired
	private AdminStoreVerificationRepo adminStoreVerificationRepo;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Autowired
	private DlerBusinessLoginRepo businessLoginRepo;

	@Autowired
	private StoreMembershipRepo storeMembershipRepo;

	@Autowired
	private PlanMembershipRepo planMembershipRepo;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public AdminStoreVerification addAdminStore(AdminStoreVerification adminstore) {
		DlerStoreDetails dlerIdExists = dlerStoreDetailsRepo.findByDlerIdAndStoreId(adminstore.getDlerId(),
				adminstore.getStoreId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Dler store not found" + adminstore.getDlerId() + "," + adminstore.getStoreId()));

		return adminStoreVerificationRepo.save(adminstore);
	}

	@Override
	public AdminStoreVerificationResponse updateAdminStoreVerification(UpdateAdminStoreRequest adminstore) {
		Optional<AdminStoreVerification> idExists = adminStoreVerificationRepo
				.findByAdminStoreVerificationId(adminstore.getAdminStoreVerificationId());

		AdminStoreVerification dbVerification;
		if (!idExists.isPresent()) {

			dbVerification = new AdminStoreVerification();
			dbVerification.setAdminStoreVerificationId(adminstore.getAdminStoreVerificationId());
			dbVerification.setCreatedDate(LocalDate.now());
		} else {
			dbVerification = idExists.get();

			if (VerificationStatus.VERIFIED.equals(dbVerification.getVerificationStatus())) {
				throw new RuntimeException("This store has already been activated and cannot be updated.");
			}

			dbVerification.setUpdatedDate(LocalDate.now());
		}

		dbVerification.setDlerId(adminstore.getDlerId());
		dbVerification.setStoreId(adminstore.getStoreId());
		dbVerification.setVerifcationComment(adminstore.getVerifcationComment());
		dbVerification.setVerificationStatus(adminstore.getVerificationStatus());
		dbVerification.setUpdatedBy(adminstore.getUpdatedBy());
		dbVerification.setUpdatedDate(LocalDate.now());

		String planId = null;
		String expiryDate = null;

		Optional<StoreMembership> storeMembershipOptional = storeMembershipRepo.findByStoreId(adminstore.getStoreId());

		if (!storeMembershipOptional.isPresent()) {
			throw new RuntimeException("StoreMembership not found for provided storeId: " + adminstore.getStoreId());
		}

		StoreMembership membership = storeMembershipOptional.get();
		membership.setUpdatedDate(LocalDate.now());

		if (VerificationStatus.PROCESSING.equals(adminstore.getVerificationStatus())
				|| VerificationStatus.PENDING.equals(adminstore.getVerificationStatus())) {
			membership.setVerificationStatus(adminstore.getVerificationStatus());
			membership.setVerificationComment(adminstore.getVerifcationComment());
		}

		if (VerificationStatus.VERIFIED.equals(adminstore.getVerificationStatus())) {
			sendAdminStoreToMail(dbVerification);

			List<PlanMembership> planMembershipList = planMembershipRepo.findByDefaultPlan("x");

			if (planMembershipList.isEmpty()) {
				throw new RuntimeException("No default plans found.");
			}

			PlanMembership selectedPlan = planMembershipList.get(0);

			membership.setVerificationStatus(adminstore.getVerificationStatus());
			membership.setStoreCurrentPlan(selectedPlan.getPlanId());
			expiryDate = LocalDate.now().plusDays(selectedPlan.getNumberOfDays()).toString();
			membership.setStoreExpiryDate(expiryDate);

			planId = selectedPlan.getPlanId();
		}

		storeMembershipRepo.save(membership);
		adminStoreVerificationRepo.save(dbVerification);

		AdminStoreVerificationResponse response = new AdminStoreVerificationResponse();
		BeanUtils.copyProperties(dbVerification, response);
		response.setPlanId(planId);
		response.setExpiryDate(expiryDate);

		return response;
	}
	private void sendAdminStoreToMail(AdminStoreVerification storeVerification) {
		Optional<DlerBusinessLogin> sendDlerId = businessLoginRepo.findById(storeVerification.getDlerId());
		if (sendDlerId.isPresent()) {
			String sendDlerIdByEmail = sendDlerId.get().getDlerEmailId();
			String subject = "Store Verification Status Updated";
			String message = "Your store verification status has been updated to: "
					+ storeVerification.getVerificationStatus();
			emailServiceImpl.sendAdminStoreToMail(sendDlerIdByEmail, subject, message);
		}
	}
	@Override
	public ResponseCombinedAdminStorAndStorMem getadminStoreVerificationService(String adminStoreVerificationId,
			String storeId, String dlerId, String verificationStatus) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AdminStoreVerification> query = cb.createQuery(AdminStoreVerification.class);
        Root<AdminStoreVerification> root = query.from(AdminStoreVerification.class);
        List<Predicate> predicates = new ArrayList<>();

        if (adminStoreVerificationId != null) {
            predicates.add(cb.equal(root.get("adminStoreVerificationId"), adminStoreVerificationId));
        }
        if (storeId != null) {
            predicates.add(cb.equal(root.get("storeId"), storeId));
        }
        if (dlerId != null) {
            predicates.add(cb.equal(root.get("dlerId"), dlerId));
        }
        query.where(predicates.toArray(new Predicate[0]));
        List<AdminStoreVerification> adminStoreVerification = entityManager.createQuery(query).getResultList();

        List<StoreMembership> storeMembership = new ArrayList<>();
        if (adminStoreVerificationId != null) {
        	storeMembership = storeMembershipRepo.findByStoreIdKey(adminStoreVerificationId);
        }

        ResponseCombinedAdminStorAndStorMem response = new ResponseCombinedAdminStorAndStorMem();
        if (!adminStoreVerification.isEmpty() || !storeMembership.isEmpty()) {
            response.setMessage("Dealer and Admin Brand details found.");
            response.setStatus(true);
            response.setDealerStore(adminStoreVerification);
            response.setDlerStoreMem(storeMembership);
        } else {
            response.setMessage("No records found for the provided parameters.");
            response.setStatus(false);
        }

        return response;
	}
}
