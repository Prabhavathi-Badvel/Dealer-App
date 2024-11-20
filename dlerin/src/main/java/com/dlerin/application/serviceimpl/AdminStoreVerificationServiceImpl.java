package com.dlerin.application.serviceimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dlerin.application.dto.AdminStoreVerificationResponse;
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
	public AdminStoreVerificationResponse updateAdminStoreVerification(UpdateAdminStoreRequest adminstore) {
	    Optional<AdminStoreVerification> idExists = adminStoreVerificationRepo
	            .findByAdminStoreVerificationId(adminstore.getAdminStoreVerificationId());

	    if (!idExists.isPresent()) {
	        throw new RuntimeException("AdminStoreVerification ID not found: " + adminstore.getAdminStoreVerificationId());
	    }

	    AdminStoreVerification dbVerification = idExists.get();

	    if (VerificationStatus.VERIFIED.equals(dbVerification.getVerificationStatus())) {
	        throw new RuntimeException("This store has already been activated and cannot be updated.");
	    }

	    dbVerification.setDlerId(adminstore.getDlerId());
	    dbVerification.setStoreId(adminstore.getStoreId());
	    dbVerification.setVerifcationComment(adminstore.getVerifcationComment());
	    dbVerification.setVerificationStatus(adminstore.getVerificationStatus());

	    String planId = null;
	    String expiryDate = null;

	    Optional<StoreMembership> storeMembershipOptional = storeMembershipRepo.findByStoreId(adminstore.getStoreId());

	    if (storeMembershipOptional.isPresent()) {
	        StoreMembership membership = storeMembershipOptional.get();

	        if (VerificationStatus.PROCESSING.equals(adminstore.getVerificationStatus())
	                || VerificationStatus.PENDING.equals(adminstore.getVerificationStatus())) {
	            membership.setVerificationStatus(adminstore.getVerificationStatus());
	            membership.setVerificationComment(adminstore.getVerifcationComment());	        }

	        if (VerificationStatus.VERIFIED.equals(adminstore.getVerificationStatus())) {
	            sendAdminStoreToMail(dbVerification);

	            // Fetch all default plans
	            List<PlanMembership> planMembershipList = planMembershipRepo.findByDefaultPlan("x");

	            if (planMembershipList.isEmpty()) {
	                throw new RuntimeException("No default plans found.");
	            }

	            // Pick the first plan from the list (or add your own logic to choose)
	            PlanMembership selectedPlan = planMembershipList.get(0);

	            membership.setVerificationStatus(adminstore.getVerificationStatus());
	            membership.setStoreCurrentPlan(selectedPlan.getPlanId());
	            expiryDate = LocalDate.now().plusDays(selectedPlan.getNumberOfDays()).toString();
	            membership.setStoreExpiryDate(expiryDate);

	            planId = selectedPlan.getPlanId();
	        }

	        storeMembershipRepo.save(membership);
	    } else {
	        throw new RuntimeException("StoreMembership not found for provided storeId: " + adminstore.getStoreId());
	    }

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


}
