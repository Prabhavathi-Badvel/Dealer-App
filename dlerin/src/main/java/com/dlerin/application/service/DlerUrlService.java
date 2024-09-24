package com.dlerin.application.service;

import java.util.List;

import com.dlerin.application.entity.DlerUrl;

public interface DlerUrlService {

	public DlerUrl addDlerUrl(DlerUrl dlerUrl);

	public DlerUrl updateDlerUrl(DlerUrl dlerUrl);

	public List<DlerUrl> getDlerUrl(String uiUrl);


}
