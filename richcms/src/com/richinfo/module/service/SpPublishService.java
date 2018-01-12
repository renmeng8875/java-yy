package com.richinfo.module.service;

import com.richinfo.common.service.BaseService;
import com.richinfo.module.entity.SpPublish;

public interface SpPublishService extends BaseService<SpPublish,Integer>{

	public int getSpPublishCountByCompanyId(int companyId);
}
