package com.yy.ent.platform.signcar.service.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yy.ent.platform.signcar.service.common.BaseService;

@Service
public class DataMockService extends BaseService{
    private List<String> uids;
    
    private List<String> chidList;

    public List<String> getChidList() {
        return chidList;
    }

    public DataMockService(){
        try {
            initChid();
            initUid();
        } catch (Exception e) {
            logger.error("init data service error!",e);
        }
    }
    
    public void initChid() throws Exception{
        chidList = new ArrayList<String>();
        ClassPathResource resource = new ClassPathResource("chid.ini");
        File f = resource.getFile();
        List<String> lines = FileUtils.readLines(f);
        for(String line:lines)
        {
            chidList.add(StringUtils.trimAllWhitespace(line));
            
        }
    }
    
    public void initUid() throws Exception{
        uids = new ArrayList<String>();
        ClassPathResource resource = new ClassPathResource("uid.ini");
        File f = resource.getFile();
        List<String> lines = FileUtils.readLines(f);
        for(String line:lines)
        {
            uids.add(StringUtils.trimAllWhitespace(line));
        }
    }
    
    public String getRandomUid(){
        Random rn = new Random();
        int index = rn.nextInt(uids.size()-1);
        return uids.get(index);
    }
    
    public List<String> getUidList(){
        return uids;
    }
    
    public int getRandomNum(int num){
        Random rn = new Random();
        return rn.nextInt(num);
    }
 
    
   
}
