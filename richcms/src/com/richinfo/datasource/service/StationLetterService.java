package com.richinfo.datasource.service;

import java.io.InputStream;
import java.util.List;

import com.richinfo.common.service.BaseService;
import com.richinfo.datasource.entity.StationLetter;

public interface StationLetterService extends BaseService<StationLetter, Integer> {
	
	public List<StationLetter> listByParam(String letter);
	
	public List<StationLetter> listByIds(String ids);
	
	public String importLetterFromExcel(InputStream is);
	
	public List<StationLetter> search(String[] params);
}
