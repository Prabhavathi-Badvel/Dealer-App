package com.dlerin.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dlerin.application.entity.DlerUrl;

public interface DlerUrlRepo extends JpaRepository<DlerUrl, String> {
	
	@Query("SELECT d FROM DlerUrl d WHERE d.uiUrl = :uiUrl")
	Optional<DlerUrl> findByDlerUrl(@Param("uiUrl") String uiUrl);
}
