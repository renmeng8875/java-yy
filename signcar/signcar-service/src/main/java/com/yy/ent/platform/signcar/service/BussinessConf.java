package com.yy.ent.platform.signcar.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BussinessConf implements InitializingBean{

    
    @Value("${cound.url}")
    private String coundUrl;
    
    @Value("${isDisplay}")
    private String isDisplay;
    

    

    public boolean isDisplay(){
        return Boolean.valueOf(isDisplay);
    }
   
    public String getCoundUrl() {
        return coundUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
       
        
    }

    
}
