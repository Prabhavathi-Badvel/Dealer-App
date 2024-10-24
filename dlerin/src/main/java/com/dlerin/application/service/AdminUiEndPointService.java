package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.AdminUiEndPoint;

public interface AdminUiEndPointService {
	public AdminUiEndPoint addAdminUiEndPoint(AdminUiEndPoint adminUiEndPoint, String name);

	public List<AdminUiEndPoint> getAdminUiEndPoint(String systemId, String ipUrlToUi, String updatedBy);

	public AdminUiEndPoint update(AdminUiEndPoint adminUiEndPoint);
}
