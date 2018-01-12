/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.yy.ent.platform.core.web.controller;

import com.yy.ent.commons.base.dto.OrderMapProperty;
import com.yy.ent.commons.base.dto.Property;
import com.yy.ent.commons.base.valid.BlankUtil;
import com.yy.ent.external.service.WebdbHalbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/ent")
public class EntController extends BaseController {
    @Autowired
    private WebdbHalbService webdbHalbService;

    /**
     * 取登录信息
     *
     * @return
     * @throws Exception
     * @author suzhihua
     * @date 2015年3月19日 下午2:55:34
     */
    @RequestMapping(value = "/queryLoginYYInfos")
    @ResponseBody
    public String queryLoginYYInfos() throws Exception {
        String uid = getLoginUidStr();
        if (BlankUtil.isBlank(uid)) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        list.add(uid);

        OrderMapProperty omp = webdbHalbService.listUserInfoByUidsNew(list);
        Property prop = omp.get(uid);
        if (BlankUtil.isBlank(prop)) {
            prop = new Property();
            prop.put("uid", "");
            prop.put("yy_num", "");
        }

        return prop.toJSONString();
    }

}
