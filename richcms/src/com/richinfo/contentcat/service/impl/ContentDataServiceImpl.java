package com.richinfo.contentcat.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.json.JSONObject;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.Available;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
 
import com.richinfo.comm.Numeric;
import com.richinfo.common.annotation.RenmSelf;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.Image;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.dao.ContentAndroidDao;
import com.richinfo.contentcat.dao.ContentDataDao;
import com.richinfo.contentcat.dao.ContentIosDao;
import com.richinfo.contentcat.dao.ContentNewsDao;
import com.richinfo.contentcat.entity.ContentAndroid;
import com.richinfo.contentcat.entity.ContentData;
import com.richinfo.contentcat.entity.ContentIos;
import com.richinfo.contentcat.entity.ContentNews;
import com.richinfo.contentcat.service.ContentDataService;
import com.richinfo.privilege.dao.CategoryDao;
import com.richinfo.privilege.entity.Category;

@Service("ContentDataService")
public class ContentDataServiceImpl extends BaseServiceImpl<ContentData, Long>
implements ContentDataService {
	
	private final static Logger log = Logger.getLogger("exceptionLog");
	private ContentDataDao contentDataDao;
	private ContentIosDao contentIosDao;
	private ContentNewsDao contentNewsDao;
	private ContentAndroidDao contentAndroidDao;
	private CategoryDao categoryDao;

	@Autowired
	@Qualifier("ContentDataDao")
	public void setContentDataDao(ContentDataDao contentDataDao) {
		this.contentDataDao = contentDataDao;
	}
	
	@Autowired
	@Qualifier("ContentAndroidDao")
	public void setContentAndroidDao(ContentAndroidDao contentAndroidDao){
		this.contentAndroidDao=contentAndroidDao;
	}

	@Autowired
	@Qualifier("ContentIosDao")
	public void setContentIosDao(ContentIosDao contentIosDao) {
		this.contentIosDao = contentIosDao;
	}
	
	@Autowired
	@Qualifier("ContentNewsDao")
	public void setContentNewsDao(ContentNewsDao contentNewsDao) {
		this.contentNewsDao = contentNewsDao;
	}
	
	@Autowired
	@Qualifier("CategoryDao")
	public void setContentIosDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Autowired
	@Qualifier("ContentDataDao")
	@Override
	public void setBaseDao(BaseDao<ContentData, Long> baseDao) {
		this.baseDao=(ContentDataDao)baseDao;
	}
	
	public Page<ContentData> find(ContentData c){
		List<Object> paramList=null;
		String hql = "from ContentData c Where c.catId=? ";
		
		paramList=new ArrayList<Object>();
		paramList.add(c.getCatId());
		if(!StringUtils.isEmpty(c.getTxtHead())){
			hql+=" and c.txtHead like ?";
			paramList.add('%'+c.getTxtHead()+'%');
		}
		if("ctime".equals(c.getTimeType())){
			if(c.getStartTime()!=null&&c.getEndTime()!=null){
				hql+=" and c.createTime>=? and c.createTime<=?";
				paramList.add(c.getStartTime());
				paramList.add(c.getEndTime());
			}
			if(c.getStartTime()!=null&&c.getEndTime()==null){
				hql+=" and c.createTime>=? ";
				paramList.add(c.getStartTime());
			}
			if(c.getStartTime()==null&&c.getEndTime()!=null){
				hql+=" and c.createTime<=? ";
				paramList.add(c.getEndTime());
			}
		}
		if("utime".equals(c.getTimeType())){
			if(c.getStartTime()!=null&&c.getEndTime()!=null){
				hql+=" and c.updateTime>=? and c.updateTime<=?";
				paramList.add(c.getStartTime());
				paramList.add(c.getEndTime());
			}
			if(c.getStartTime()!=null&&c.getEndTime()==null){
				hql+=" and c.updateTime>=? ";
				paramList.add(c.getStartTime());
			}
			if(c.getStartTime()==null&&c.getEndTime()!=null){
				hql+=" and c.updateTime<=? ";
				paramList.add(c.getEndTime());
			}
		}
		if(c.getStatus()!=0){
			hql+=" and c.status=? ";
			paramList.add(c.getStatus());
		}
		
		hql+=" order by c.priority,c.updateTime desc,c.createTime desc";
		Page<ContentData> page= contentDataDao.find(hql, paramList.toArray(), null);
		paramList=null;
		return page;
	}
	
	@RenmSelf(methodDesc="更改排序权重值")
	public void updatePriority(Long pkid,int priority)
	{
		String hql = "update ContentData c set c.priority=? where c.contentDataId=?";
		contentDataDao.updateByHql(hql, new Object[]{priority,pkid});
	}
	
	
	public List<ContentAndroid> getContentAndroidlist(Map<String, Object> alias)
	{
		String hql="from ContentAndroid c where c.contentId in (:inList)";
		List<ContentAndroid>list=contentAndroidDao.list(hql, null, alias);
		return list;
	}
	
	public List<ContentIos> getContentIoslist(Map<String, Object> alias)
	{
		String hql="from ContentIos c where c.appId in (:inList)";
		List<ContentIos>list=contentIosDao.list(hql, null, alias);
		return list;
	}
	
	public List<ContentNews> getContentNewsList(Map<String, Object> alias){
		String hql="from ContentNews c where c.contentId in (:inList)";
		List<ContentNews>list=contentNewsDao.list(hql, null, alias);
		return list;
	}
	
	public String getDataTypeByCat(int catid){
		String androidSql = "select count(*) from (select catid ,catname from mm_content_cat start with catid=262 connect by prior catid=pid)a where a.catid = "+catid;
	    String iosSql = "select count(*) from (select catid ,catname from mm_content_cat start with catid in(283,634) connect by prior catid=pid)a where a.catid = "+catid;
	    int android = contentDataDao.getJdbcTemplate().queryForInt(androidSql);
	    if(android>0){
	    	return "android";
	    }else{
	    	int ios = contentDataDao.getJdbcTemplate().queryForInt(iosSql);
	    	if(ios>0){
	    		return "ios";
	    	}else{
	    		return "other";
	    	}
	    }
	}
	
	public ContentData getContentDataByParam(int dataid,int catId,String dataType){
		String hql = "from ContentData c where c.dataId=? and c.catId=? and c.dataType=?";
		List<ContentData> contentDataList=contentDataDao.list(hql, new Object[]{dataid,catId,dataType}, null);
		return contentDataList!=null&&contentDataList.size()>0?contentDataList.get(0):null;
	}
	
	private String grabAndroidLogo(ContentAndroid android){
		JSONObject logojson = JSONObject.fromObject(android.getLogo());
		Image img = new Image();
		JSONObject imgJson = new JSONObject();
		String path = JSONObject.fromObject(logojson.get("1")).get("path").toString();
		imgJson.put(1, img.downloadFile(path,"android/"+Numeric.hashNumber(Long.valueOf(android.getContentId())),Image.IMGSIZE.ShotScreen,false,android.getTitle(),1));
		
		if(imgJson.toString().length()>2){
			android.setPreLogo(imgJson.toString());
			android.setSfstatus("1");
			contentAndroidDao.update(android);
		}
    	return imgJson.toString().length()>2?imgJson.toString():"";
	}
	@RenmSelf(methodDesc="批量导入应用id")
	public String multImportApplication(Map<String,Object> paramMap){
		
		if(null==paramMap){
			return "-1";
		}
		//获取应用ID
		List<String> inList=null;
		String ids=ObjectUtils.toString(paramMap.get("ids"));
		String[] idOrderArray = ids.split(";");
		inList=new ArrayList<String>();
		for(int i=0;i<idOrderArray.length;i++)
		{
			String[] pair = idOrderArray[i].split(",");
			if(pair.length>1){
				inList.add(StringUtils.trim(pair[1]));
			}else{
				return "-1";
			}
		}
		Map<String,Object> idMap=new HashMap<String,Object>();
		idMap.put("inList", inList);
		//栏目ID
		int catId=Integer.valueOf(ObjectUtils.toString(paramMap.get("catId")));
		Category c = categoryDao.get(catId);
		int status=(c!=null && c.getWorkflowId()!=0)?1:99;
		
		//获取当前用户
		//User user=(User)paramMap.get("user");
		String nickname=ObjectUtils.toString(paramMap.get("nickname"));
		
		Map<String,String> imageMap =null;
		Map<String,Map<String,String>>treeMap=null;
		//根据应用类型及应用ID查询相关数据
		String dataType=ObjectUtils.toString(paramMap.get("datatype"));
		if("android".equals(dataType)){
 			List<ContentAndroid> list=getContentAndroidlist(idMap);
			if(list!=null && list.size()>0){
				JSONObject jsonObject=null;
				
				StringBuffer sf= null;
				sf=new StringBuffer();
				for(int i=0;i<list.size();i++){
					ContentAndroid contentAndroid=list.get(i);
					ContentData contentData=null;
					try{
						contentData=getContentDataByParam(contentAndroid.getDataId(),catId,dataType);
						if(contentData==null){
							contentData=new ContentData();
						}
						
						contentData.setAuthor(nickname);
						contentData.setDataType(dataType);
						contentData.setDataId(contentAndroid.getDataId());
						contentData.setCatId(Integer.valueOf(catId));
						contentData.setTxtHead(contentAndroid.getTitle());
						contentData.setContentId(contentAndroid.getContentId());
						for(int j=0;j<idOrderArray.length;j++)
						{
							String[] pair = idOrderArray[j].split(",");
							if(!StringUtils.isEmpty(contentAndroid.getContentId())&&contentAndroid.getContentId().equals(StringUtils.trim(pair[1]))){
								contentData.setPriority(Integer.valueOf(StringUtils.trim(pair[0])));
								break;
							}
						}
						String image=null;
						if(!StringUtils.isEmpty(contentAndroid.getPreImages())){
							image=contentAndroid.getPreImages();
						}else{
							image=contentAndroid.getImages();
						}
						String newImage=null;
						if(!StringUtils.isEmpty(image)){
							String strHtmlUnescape=HtmlUtils.htmlUnescape(image);
							jsonObject=JSONObject.fromObject(strHtmlUnescape);
							if(jsonObject!=null){
								String minKey=getJsonMinKey(jsonObject);
								JSONObject jo=jsonObject.getJSONObject(minKey);
								imageMap =new HashMap<String,String>();
								if(jo!=null){
									imageMap.put("alt", jo.getString("alt").replace("\\/",  "/"));
									imageMap.put("path", jo.getString("path").replace("\\/",  "/"));
									imageMap.put("size", jo.getString("size").replace("\\/",  "/"));
								}
								treeMap= new TreeMap<String,Map<String,String>>();
								treeMap.put("1", imageMap);
									
								jsonObject=JSONObject.fromObject(treeMap);
								newImage=jsonObject.toString();
							}
						}
						contentData.setImage(newImage);
						String logo=null;
//						if(!StringUtils.isEmpty(contentAndroid.getPreLogo())){
//							logo=ObjectUtils.toString(contentAndroid.getPreLogo());
//						}else{
							logo=ObjectUtils.toString(contentAndroid.getLogo());
//						}
						
						logo=stripslashes(logo);
						contentData.setLogo(logo);
						contentData.setPreLogo(contentAndroid.getPreLogo());
						String tempStr=null;
						if(!StringUtils.isEmpty(contentAndroid.getEditorLanguage())){
							tempStr=contentAndroid.getEditorLanguage();
						}else if(!StringUtils.isEmpty(contentAndroid.getPreIntroduce())){
							tempStr=contentAndroid.getPreIntroduce();
						}else if(!StringUtils.isEmpty(contentAndroid.getIntroduce())){
							tempStr=contentAndroid.getIntroduce();
						}
						if(!StringUtils.isEmpty(tempStr)&& tempStr.length()>100){
							tempStr=tempStr.substring(0, 97)+"...";
						}
						contentData.setEditorLanguage(tempStr);
						contentData.setStatus(status);
						Long contentDataId=contentData.getContentDataId();
						Calendar cal=Calendar.getInstance();
						Long currentTime=DateTimeUtil.converToTimestamp(DateTimeUtil.getFormatDateTime(cal.getTime(), "yyyy-MM-dd"),"yyyy-MM-dd");
						if("sfyy".equals(c.getCat())){
							contentData.setStime(DateTimeUtil.getTimeStamp()+"");
							Long etime = DateTimeUtil.converToTimestamp(DateTimeUtil.getDateAfterDay(new Date(), 7, "yyyy-MM-dd"),"yyyy-MM-dd");
							contentData.setEtime(etime+"");
							String plogo = grabAndroidLogo(contentAndroid);
							if("".equals(plogo)){
								sf.append(contentAndroid.getContentId()).append(";");
								continue;
							}
							contentData.setPreLogo(plogo);
							
							String sql = "update mm_content_data t set t.p_logo=? where t.contentid = ?";
							contentDataDao.getJdbcTemplate().update(sql, new Object[]{plogo,contentAndroid.getContentId()});
						}
						if(contentDataId!=null&&contentDataId.intValue()>0){
							contentData.setUpdateTime(currentTime);
							contentData.setContentDataId(contentDataId);
							contentDataDao.update(contentData);
						}else{
							contentData.setCreateTime(currentTime);
							contentData.setUpdateTime(currentTime);
							contentDataDao.add(contentData);
						}
					
					}catch(Exception e){
						log.error("[multImportApplication:]"+e);
						sf.append(contentAndroid.getContentId()).append(";");
						continue;
					}
				}
				
				if(inList!=null && inList.size()>0){
					for(int i=0;i<inList.size();i++){
						boolean isFlag=true;
						for(int j=0;j<list.size();j++){
							ContentAndroid contentAndroid=list.get(j);
							if(contentAndroid!=null && !StringUtils.isEmpty(contentAndroid.getContentId())
									&& contentAndroid.getContentId().equals(inList.get(i))){
								isFlag=false;
								break;
							}
						}
						if(isFlag){
							sf.append(inList.get(i)).append(";");
						}
					}
				}
				
				if(!"".equals(sf.toString())){
					return sf.toString();
				}
			}else{
				return "-1";
			}
		}else if("ios".equals(dataType)){
			List<ContentIos> list=getContentIoslist(idMap);
			if(list!=null && list.size()>0){
				JSONObject jsonObject=null;
				StringBuffer sf= null;
				
				sf=new StringBuffer();
				for(int i=0;i<list.size();i++){
					ContentIos contentIos=list.get(i);
					ContentData contentData=null;
					try{
						contentData=getContentDataByParam(contentIos.getDataId(),catId,dataType);
						if(contentData==null){
							contentData=new ContentData();
						}
						
						contentData.setAuthor(nickname);
						contentData.setDataType(dataType);
						contentData.setDataId(contentIos.getDataId());
						contentData.setCatId(Integer.valueOf(catId));
						
						if(!StringUtils.isEmpty(contentIos.getPreTitle())){
							contentData.setTxtHead(contentIos.getPreTitle());
						}else{
							contentData.setTxtHead(contentIos.getTitle());
						}
						contentData.setContentId(contentIos.getAppId());
						for(int j=0;j<idOrderArray.length;j++)
						{
							String[] pair = idOrderArray[j].split(",");
							if(!StringUtils.isEmpty(contentIos.getAppId())&&contentIos.getAppId().equals(StringUtils.trim(pair[1]))){
								contentData.setPriority(Integer.valueOf(StringUtils.trim(pair[0])));
								break;
							}
						}
						String image=null;
						if(!StringUtils.isEmpty(contentIos.getPreImages())){
							image=ObjectUtils.toString(contentIos.getPreImages());
						}else{
							image=ObjectUtils.toString(contentIos.getImages());
						}
						String newImage=null;
						if(!StringUtils.isEmpty(image)){
							String strHtmlUnescape=HtmlUtils.htmlUnescape(image);
							jsonObject=JSONObject.fromObject(strHtmlUnescape);
							if(jsonObject!=null){
									String minKey=getJsonMinKey(jsonObject);
									JSONObject jo=jsonObject.getJSONObject(minKey);
									imageMap =new HashMap<String,String>();
									if(jo!=null){
										imageMap.put("alt", jo.getString("alt").replace("\\/",  "/"));
										imageMap.put("path", jo.getString("path").replace("\\/",  "/"));
										imageMap.put("size", jo.getString("size").replace("\\/",  "/"));
									}
									treeMap= new TreeMap<String,Map<String,String>>();
									treeMap.put("1", imageMap);
									
									jsonObject=JSONObject.fromObject(treeMap);
									newImage=jsonObject.toString();
							}
						}
						contentData.setImage(newImage);
						String logo=null;
						if(!StringUtils.isEmpty(contentIos.getPreLogo())){
							logo=ObjectUtils.toString(contentIos.getPreLogo());
						}else{
							logo=ObjectUtils.toString(contentIos.getLogo());
						}
						logo=stripslashes(logo);
						contentData.setLogo(logo);
						
						String tempStr=null;
						if(!StringUtils.isEmpty(contentIos.getEditorLanguage())){
							tempStr=contentIos.getEditorLanguage();
						}else if(!StringUtils.isEmpty(contentIos.getPreIntroduce())){
							tempStr=contentIos.getPreIntroduce();
						}else if(!StringUtils.isEmpty(contentIos.getIntroduce())){
							tempStr=contentIos.getIntroduce();
						}
						if(!StringUtils.isEmpty(tempStr)&& tempStr.length()>100){
							tempStr=tempStr.substring(0, 97)+"...";
						}
						contentData.setEditorLanguage(tempStr);
						contentData.setStatus(status);
						
						Long contentDataId=contentData.getContentDataId();
						Calendar cal=Calendar.getInstance();
						Long currentTime=DateTimeUtil.converToTimestamp(DateTimeUtil.getFormatDateTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss");
						if(contentDataId!=null&&contentDataId.intValue()>0){
							contentData.setUpdateTime(currentTime);
							contentData.setContentDataId(contentDataId);
							contentDataDao.update(contentData);
						}else{
							contentData.setCreateTime(currentTime);
							contentData.setUpdateTime(contentData.getUpdateTime());
							contentDataDao.add(contentData);
						}
					
					}catch(Exception e){
						log.error("[multImportApplication:]"+e);
						sf.append(contentIos.getContentId()).append(";");
						continue;
					}
				}
				
				if(inList!=null && inList.size()>0){
					for(int i=0;i<inList.size();i++){
						boolean isFlag=true;
						for(int j=0;j<list.size();j++){
							ContentIos contentIos=list.get(j);
							if(contentIos!=null && !StringUtils.isEmpty(contentIos.getContentId())
									&&contentIos.getContentId().equals(inList.get(i))){
								isFlag=false;
								break;
							}
						}
						if(isFlag){
							sf.append(inList.get(i)).append(";");
						}
					}
				}
				
				if(!"".equals(sf.toString())){
					return sf.toString();
				}
			}else{
				return "-1";
			}
		}else{
			List<ContentNews> list=getContentNewsList(idMap);
			if(list!=null && list.size()>0){
				JSONObject jsonObject=null;
				StringBuffer sf= null;
				
				sf=new StringBuffer();
				for(int i=0;i<list.size();i++){
					ContentNews contentnews=list.get(i);
					ContentData contentData=null;
					try{
						contentData=getContentDataByParam(contentnews.getDataId(),catId,dataType);
						if(contentData==null){
							contentData=new ContentData();
						}
						
						contentData.setAuthor(nickname);
						contentData.setDataType(dataType);
						contentData.setDataId(contentnews.getDataId());
						contentData.setCatId(Integer.valueOf(catId));
						
						if(!StringUtils.isEmpty(contentnews.getPreTitle())){
							contentData.setTxtHead(contentnews.getPreTitle());
						}else{
							contentData.setTxtHead(contentnews.getTitle());
						}
						contentData.setContentId(contentnews.getContentId());
						for(int j=0;j<idOrderArray.length;j++)
						{
							String[] pair = idOrderArray[j].split(",");
							System.out.println("-----"+idOrderArray[j]);
							if(!StringUtils.isEmpty(contentnews.getContentId())&&contentnews.getContentId().equals(StringUtils.trim(pair[1]))){
								contentData.setPriority(Integer.valueOf(StringUtils.trim(pair[0])));
								break;
							}
						}
						String image=null;
						if(!StringUtils.isEmpty(contentnews.getPreImages())){
							image=ObjectUtils.toString(contentnews.getPreImages());
						}else{
							image=ObjectUtils.toString(contentnews.getImages());
						}
						String newImage=null;
						if(!StringUtils.isEmpty(image)){
							String strHtmlUnescape=HtmlUtils.htmlUnescape(image);
							jsonObject=JSONObject.fromObject(strHtmlUnescape);
							if(jsonObject!=null){
									String minKey=getJsonMinKey(jsonObject);
									JSONObject jo=jsonObject.getJSONObject(minKey);
									imageMap =new HashMap<String,String>();
									if(jo!=null){
										imageMap.put("alt", jo.getString("alt").replace("\\/",  "/"));
										imageMap.put("path", jo.getString("path").replace("\\/",  "/"));
										imageMap.put("size", jo.getString("size").replace("\\/",  "/"));
									}
									treeMap= new TreeMap<String,Map<String,String>>();
									treeMap.put("1", imageMap);
									
									jsonObject=JSONObject.fromObject(treeMap);
									newImage=jsonObject.toString();
							}
						}
						contentData.setImage(newImage);
						String logo=null;
						if(!StringUtils.isEmpty(contentnews.getPreLogo())){
							logo=ObjectUtils.toString(contentnews.getPreLogo());
						}else{
							logo=ObjectUtils.toString(contentnews.getLogo());
						}
						logo=stripslashes(logo);
						contentData.setLogo(logo);
						
						String tempStr=null;
						if(!StringUtils.isEmpty(contentnews.getEditorLanguage())){
							tempStr=contentnews.getEditorLanguage();
						}else if(!StringUtils.isEmpty(contentnews.getIntroduce())){
							tempStr=contentnews.getIntroduce();
						}
						if(!StringUtils.isEmpty(tempStr)&& tempStr.length()>100){
							tempStr=tempStr.substring(0, 97)+"...";
						}
						contentData.setEditorLanguage(tempStr);
						contentData.setStatus(status);
						
						Long contentDataId=contentData.getContentDataId();
						Calendar cal=Calendar.getInstance();
						Long currentTime=DateTimeUtil.converToTimestamp(DateTimeUtil.getFormatDateTime(cal.getTime(), "yyyy-MM-dd HH:mm:ss"),"yyyy-MM-dd HH:mm:ss");
						if(contentDataId!=null&&contentDataId.intValue()>0){
							contentData.setUpdateTime(contentnews.getUpdateTime());
							contentData.setContentDataId(contentDataId);
							contentDataDao.update(contentData);
						}else{
							contentData.setCreateTime(currentTime);
							contentData.setUpdateTime(contentnews.getUpdateTime());
							contentDataDao.add(contentData);
						}
					
					}catch(Exception e){
						log.error("[multImportApplication:]"+e);
						sf.append(contentnews.getContentId()).append(";");
						continue;
					}
				}
				
				if(inList!=null && inList.size()>0){
					for(int i=0;i<inList.size();i++){
						boolean isFlag=true;
						for(int j=0;j<list.size();j++){
							ContentNews contentnews=list.get(j);
							if(contentnews!=null && !StringUtils.isEmpty(contentnews.getContentId())
									&&contentnews.getContentId().equals(inList.get(i))){
								isFlag=false;
								break;
							}
						}
						if(isFlag){
							sf.append(inList.get(i)).append(";");
						}
					}
				}
				
				if(!"".equals(sf.toString())){
					return sf.toString();
				}
			}else{
				return "-1";
			}
		}
		
		return "1";
	}
	
	public List<Map<String,Object>> getCountByCatId(int catId){
		String sql="select c.status,count(c.DID) countNum from MM_CONTENT_DATA c where c.catId=? group by c.status";
		List<Map<String,Object>> list=contentDataDao.getJdbcTemplate().queryForList(sql, catId);
		return list;
	}
	
	@RenmSelf(methodDesc="更改状态")
	public void updateStatus(Long pkid,int status,String author,Long updateTime)
	{
		String hql = "update ContentData c set c.status=?,c.author=?,c.updateTime=? where c.contentDataId=?";
		contentDataDao.updateByHql(hql, new Object[]{status,author,updateTime,pkid});
	}

	/**
	 * 取消为'和\增加转移符 
	 */
	public static String stripslashes(String txt){        
		if (null != txt){            
			txt = txt.replace("\\\\", "\\");            
			txt = txt.replace("\'", "'") ;            
			txt = txt.replace("\\\"", "\"");
			txt = txt.replaceAll("\\/", "/");
		}
		return txt ;    
	}
	
	@SuppressWarnings("unchecked")
	public static String getJsonMinKey(JSONObject jsonObject){
		TreeSet<String> treeSet =null;
		if(jsonObject!=null){
			treeSet = new TreeSet<String>();
			Iterator<String> itr=jsonObject.keys();
			while(itr.hasNext()){
				treeSet.add(itr.next());
			}
			return treeSet.size()>0?ObjectUtils.toString(treeSet.pollFirst()):"";
		}
		return "";
	}

	@Override
	public void deleteContentData(Long contentDataId) {
		// TODO Auto-generated method stub
		ContentData contentData = contentDataDao.get(contentDataId);
		Category cat = categoryDao.get(contentData.getCatId());
		if("sfyy".equals(cat.getCat())){
			String sql = "update mm_content_android t set t.p_logo = null,t.sfstatus=0 where t.contentid = ?";
			contentAndroidDao.getJdbcTemplate().update(sql, new Object[]{contentData.getContentId()});
			sql = "update mm_content_data t set t.p_logo=null where t.contentid=? ";
			contentDataDao.getJdbcTemplate().update(sql, new Object[]{contentData.getContentId()});
			contentDataDao.delete(contentDataId);
		}else{
			contentDataDao.delete(contentDataId);
		}
	}
	
}
