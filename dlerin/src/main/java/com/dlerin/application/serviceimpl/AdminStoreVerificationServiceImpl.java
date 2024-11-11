package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.UpdateAdminStoreRequest;
import com.dlerin.application.entity.AdminStoreVerification;
import com.dlerin.application.entity.DlerBusinessLogin;
import com.dlerin.application.entity.DlerStoreDetails;
import com.dlerin.application.entity.PlanMembership;
import com.dlerin.application.entity.StoreMembership;
import com.dlerin.application.entity.VerificationStatus;
import com.dlerin.application.exception.DlerNotFoundException;
import com.dlerin.application.exception.ResourceNotFoundException;
import com.dlerin.application.repository.AdminStoreVerificationRepo;
import com.dlerin.application.repository.DlerBusinessLoginRepo;
import com.dlerin.application.repository.DlerStoreDetailsRepo;
import com.dlerin.application.repository.PlanMembershipRepo;
import com.dlerin.application.repository.StoreMembershipRepo;
import com.dlerin.application.service.AdminStoreVerificationService;

import jakarta.annotation.Resource;

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

	@Override
	public AdminStoreVerification addAdminStore(AdminStoreVerification adminstore) {
		DlerStoreDetails dlerIdExists = dlerStoreDetailsRepo.findByDlerIdAndStoreId(adminstore.getDlerId(),
				adminstore.getStoreId()).orElseThrow(() -> new ResourceNotFoundException("Dler store not found"+adminstore.getDlerId()+","+adminstore.getStoreId()));

		return adminStoreVerificationRepo.save(adminstore);
	}

	@Override
	public AdminStoreVerification updateAdminStoreVerification(UpdateAdminStoreRequest adminstore) {
		Optional<AdminStoreVerification> idExists = adminStoreVerificationRepo
				.findByAdminStatusUpdatedBy(adminstore.getAdminStatusUpdatedBy());

		PlanMembership planMembership=planMembershipRepo.findByPlanId(adminstore.getPlanId());
		if (idExists.isPresent()) {
			AdminStoreVerification Db = idExists.get();
			if (VerificationStatus.VERIFIED.equals(Db.getVerificationStatus())) {
				throw new RuntimeException("This store has already been activated and cannot be updated.");
			}

			Db.setDlerId(adminstore.getDlerId());
			Db.setStoreId(adminstore.getStoreId());
			Db.setVerifcationComment(adminstore.getVerifcationComment());
			Db.setVerificationStatus(adminstore.getVerificationStatus());
			if (VerificationStatus.VERIFIED.equals(Db.getVerificationStatus())) {
				sendAdminStoreToMail(Db);
			}

			Optional<DlerStoreDetails> dlerIdExists = 
					dlerStoreDetailsRepo.findByDlerIdAndStoreId(adminstore.getDlerId(), adminstore.getStoreId());

			if (dlerIdExists.isPresent()) {
				DlerStoreDetails storeDetails = dlerIdExists.get();

				StoreMembership membership = new StoreMembership();
				membership.setStoreId(storeDetails.getStoreId());
				membership.setUpdatedBy(storeDetails.getDlerId());
				membership.setVerificationStatus(Db.getVerificationStatus());
				membership.setStoreIdKey(Db.getStoreId());
				membership.setStoreCurrentPlan(planMembership.getPlanId());
				membership.setStoreExpiryDate(LocalDate.now().plusDays(planMembership.getNumberOfDays()).toString());
				storeMembershipRepo.save(membership); // Save updated DlerStoreDetails
			} else {
				System.out.println("DlerStoreDetails entry not found for provided storeId: " + adminstore.getStoreId());
				throw new RuntimeException("DlerStoreDetails entry not found for provided dlerId and storeId.");
			}

			return adminStoreVerificationRepo.save(Db);
		} else {
			throw new RuntimeException("Store ID not found.");
		}
	}

	private void sendAdminStoreToMail(AdminStoreVerification storeVerification) {

		Optional<DlerBusinessLogin> sendDlerId = businessLoginRepo.findById(storeVerification.getDlerId());
		if (sendDlerId.isPresent()) {
			String sendDlerIdByEmail = sendDlerId.get().getDlerEmailId();
			String subject = null;
			String message = null;
			emailServiceImpl.sendAdminStoreToMail(sendDlerIdByEmail, subject, message);
		}

	}

}
