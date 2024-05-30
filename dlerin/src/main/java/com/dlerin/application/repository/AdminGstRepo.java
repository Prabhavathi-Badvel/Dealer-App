package com.dlerin.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dlerin.application.entity.AdminGst;

@Repository
public interface AdminGstRepo extends JpaRepository<AdminGst, String> {

	AdminGst findByGstCode(String gstCode);

	List<AdminGst> findByGstCodeOrGstPercentage(String gstCode, int gstPercentage);

	List<AdminGst> findByGstCodeAndGstPercentage(String gstCode, int gstPercentage);

}
