package com.richinfo.datasource.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.richinfo.common.Constants;
import com.richinfo.common.SystemProperties;
import com.richinfo.common.utils.AntiXSSUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.contentcat.entity.Sysor;
import com.richinfo.contentcat.service.SysorService;
import com.richinfo.datasource.entity.DataSource;
import com.richinfo.datasource.entity.FormField;
import com.richinfo.datasource.entity.FormInfo;
import com.richinfo.datasource.entity.TableFieldValue;
import com.richinfo.datasource.service.DataSourceService;
import com.richinfo.privilege.entity.Category;
import com.richinfo.privilege.entity.User;
import com.richinfo.privilege.service.CategoryService;

@Controller
@RequestMapping(value = "/datasource")
public class DataSourceController {

	private DataSourceService dataSourceService;

	
	private CategoryService categoryService;
	
	private SysorService sysorService;
	
	@Autowired
	@Qualifier("CategoryService")
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@Autowired
	@Qualifier("DataSourceService")
	public void setDataSourceService(DataSourceService dataSourceService) {
		this.dataSourceService = dataSourceService;
	}
	
	@Autowired
	@Qualifier("SysorService")
	public void setSysorService(SysorService sysorService) {
		this.sysorService = sysorService;
	}
	
	
	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model){
		String module = ServletRequestUtils.getStringParameter(request, "module", "");
		String tablename = Constants.FORMPREFIX + module;;
	    FormInfo form = dataSourceService.getFormByName(tablename);
        int currentPage = ServletRequestUtils.getIntParameter(request, "pageno", 1);
        List<FormField> fieldlist = dataSourceService.getFormFieldByFormid(form.getFormid()+"","list");
        List<FormField> searchlist = dataSourceService.getFormFieldByFormid(form.getFormid()+"","search");
        Map<String,Object> search = new HashMap<String, Object>();
        if("POST".equals(request.getMethod())){
        	Enumeration<?> pNames=request.getParameterNames();
    		while(pNames.hasMoreElements()){
    		    String name=(String)pNames.nextElement();
    		    String value = "";
    		   
    		    if(name.startsWith("t_")){
    		    	String[] values = request.getParameterValues(name);
    		    	Map<String,Object> time = new HashMap<String, Object>();
    		    	Long t1 = DateTimeUtil.converToTimestamp(values[0], "yyyy-MM-dd HH:mm");
    		    	Long t2 = DateTimeUtil.converToTimestamp(values[1], "yyyy-MM-dd HH:mm");
    		    	if(t2>t1){
    		    		time.put("stime", t1+"");
    		    		time.put("etime", t2+"");
    		    	}else{
    		    		time.put("stime", t2+"");
    		    		time.put("etime", t1+"");
    		    	}
    		    	//name= name.substring(2);
    		    	search.put(name, time);
    		    }else if("ttype".equals(name)||"stime".equals(name)||"etime".equals(name)||"module".equals(name)||"csrfToken".equals(name)){
    		    	continue;
    		    }else{
    		    	value = request.getParameter(name);
    		    	search.put(name, value);
    		    }
//    		    System.out.println(name + "==="+ value);
    		}
    		if(!"".equals(request.getParameter("stime"))||!"".equals(request.getParameter("stime"))){
    			Map<String,String> t = new HashMap<String, String>();
    			t.put("stime", DateTimeUtil.converToTimestamp(request.getParameter("stime"), "yyyy-MM-dd HH:mm")+"");
    			t.put("etime", DateTimeUtil.converToTimestamp(request.getParameter("etime"), "yyyy-MM-dd HH:mm")+"");
    			search.put(request.getParameter("ttype"), t);
    		}
    		for(FormField f: searchlist){
    			if(f.getHtmltype()==6){
    				@SuppressWarnings("unchecked")
					Map<String,String> m = (Map<String, String>) search.get("t_"+f.getFieldname());
    				//System.out.println(m.get("stime"));
    				f.setFieldvalue( DateTimeUtil.getDataStrFromTimeStamp(m.get("stime"),"yyyy-MM-dd HH:mm")+","+DateTimeUtil.getDataStrFromTimeStamp(m.get("etime"),"yyyy-MM-dd HH:mm"));
    				//model.addAttribute("t_"+f.getFieldname(), DateTimeUtil.getDataStrFromTimeStamp(m.get("stime"),"yyyy-MM-dd HH:mm")+","+DateTimeUtil.getDataStrFromTimeStamp(m.get("etime"),"yyyy-MM-dd HH:mm"));
    			}else{
//    				System.out.println("----"+f.getFieldname() + "---"+search.get(f.getFieldname())==null?"":search.get(f.getFieldname()).toString());
    				if(search.get(f.getFieldname())==null){
    					f.setFieldvalue("");
    				}else{
    					f.setFieldvalue(HtmlUtils.htmlEscape(search.get(f.getFieldname()).toString()));
    				}
//    				f.setFieldvalue(search.get(f.getFieldname())==null?"":search.get(f.getFieldname()).toString());
    			}
    			
    		}
        	model.addAttribute("ttype", request.getParameter("ttype"));
        	model.addAttribute("stime", HtmlUtils.htmlEscape(request.getParameter("stime")));
        	model.addAttribute("etime", HtmlUtils.htmlEscape(request.getParameter("etime")));
        }
        List<TableFieldValue> listvalue = dataSourceService.getFormValueForPageList(tablename, fieldlist,search, currentPage,Constants.DEFAULT_PAGE_SIZE);
        int totalRows = dataSourceService.getTotalRows(tablename,search);
        int totalPages = 1;
        if (totalRows % Constants.DEFAULT_PAGE_SIZE == 0) {
            totalPages = totalRows / Constants.DEFAULT_PAGE_SIZE;
        } else {
            totalPages = (totalRows / Constants.DEFAULT_PAGE_SIZE) + 1;
        }
        model.addAttribute("fieldlist", fieldlist);
        model.addAttribute("listvalue", listvalue);
        model.addAttribute("searchlist", searchlist);
        model.addAttribute("count", totalPages);
        model.addAttribute("currentpage", currentPage);
        model.addAttribute("datatype", module);
        model.addAttribute("formid", form.getFormid());
		return "datasource/datalistsTpl";
	}
	 
    /**
     * 
     * @param request
     * @param model
     * @return
     */
	@RequestMapping(value = "/datainfo.do")
	public String datainfo(HttpServletRequest request, Model model) {
		String datatype = ServletRequestUtils.getStringParameter(request, "datatype", "");
		String dataid = ServletRequestUtils.getStringParameter(request, "dataid", "0");
		User user = (User) request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
        FormInfo form = dataSourceService.getFormByName(Constants.FORMPREFIX+datatype);
        List<FormField> fieldlist = dataSourceService.getFormFieldByFormid(form.getFormid()+"","info");
        TableFieldValue fieldvalue = dataSourceService.getFormValueForInfo(Constants.FORMPREFIX+datatype, fieldlist,dataid,user.getUserName());
        model.addAttribute("fieldvalue", fieldvalue);
        model.addAttribute("dataid", dataid);
        model.addAttribute("formid", form.getFormid());
        model.addAttribute("datatype", datatype);
        model.addAttribute("storageUrl", SystemProperties.getInstance().getProperty("fileupload.contextPath"));
		return "datasource/datainfoTpl";
	}
	
	@RequestMapping(value = "/add.do")
	public String add(HttpServletRequest request, Model model){
		Map<String,String> map = new HashMap<String, String>();
		String datatype = ServletRequestUtils.getStringParameter(request, "datatype", "");
		Enumeration<?> pNames=request.getParameterNames();
		while(pNames.hasMoreElements()){
		    String name=(String)pNames.nextElement();
		    String value = "";
		    if(name.endsWith("_CB")){
		    	String[] eget_CB = request.getParameterValues(name);  
		    	for(int i=0;i<eget_CB.length;i++){  
		    		value += eget_CB[i] +",";
	            }
		    	value = value.substring(0,value.lastIndexOf(","));
		    	name = name.substring(0, name.lastIndexOf("_CB"));
		    }else if(name.endsWith("_jsonimg")){
		    	String[] imgs = request.getParameterValues(name);
		    	if(imgs!=null&&imgs.length>1){
			    	Map<String, String> image = new HashMap<String, String>();
			    	JSONObject json = new JSONObject();
			    	int i=1;
			    	for(String img : imgs){
			    		if(StringUtils.isEmpty(img))continue;
			    		image.put("path", img);
						image.put("alt", "");
						image.put("size","240x320");
						json.put(i++, image);
			    	}
			    	value = json.toString();
		    	}else{
		    		value = null;
		    	}
		    	name = name.substring(0, name.lastIndexOf("_jsonimg"));
		    	
		    }else if("datatype".equals(name)||"csrfToken".equals(name)||"checkbox".equals(name)||name.startsWith("v_")){
		        continue;
		    }else{
		    	value=request.getParameter(name);
		    }
		    System.out.println(name +"    " + value);
		    map.put(name, value);
		}
		boolean bln = this.dataSourceService.saveForm(map);
		if(bln)
			model.addAttribute("message", "保存成功");
		else
			model.addAttribute("message", "保存失败！请重试");
		model.addAttribute("url","/datasource/lists.do?module="+datatype);
		return Constants.FORWARDURL;
		
	}
	/**
	 * 删除应用
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/delappresource.do")
	@ResponseBody
	public Map<String,String> delappresource(HttpServletRequest request, Model model){
		User user = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		Map<String,String> json = new HashMap<String,String>();
		int dataid = ServletRequestUtils.getIntParameter(request, "dataid", -1);
		String datatype = ServletRequestUtils.getStringParameter(request, "datatype","");
		Long count = 0L;
		String supper = SystemProperties.getInstance().getProperty("authentication.superManager");
		if(!supper.equals(user.getRole().getRoleName())){
		    count = sysorService.getTodayDelNum(user.getUserId());
		    int limit = Integer.parseInt(SystemProperties.getInstance().getProperty("delConfig.ulimit"));
		    if(count>=limit){
		    	json.put("status", "0");
				json.put("msg", "亲，您今天已经删除数据源20次了，要继续删除请联系管理员");
				return json;
		    }
		}
		boolean bln = this.dataSourceService.deleteInfoById(Constants.FORMPREFIX + datatype, datatype, dataid);
		if(bln){
			saveSysor(user,dataid+"",datatype);
			json.put("status", "1");
			json.put("msg", "应用删除成功");
		}else{
			json.put("status", "0");
			json.put("msg", "应用删除失败,请联系系统管理员");
		}
		return json;
	}
	
	/**
	 * 记录删除日志
	 * @param user
	 * @param dataid
	 * @param datatype
	 */
	private void saveSysor(User user,String dataid,String datatype){
		Sysor sysor = new Sysor();
		sysor.setUserId(user.getUserId());
		sysor.setOptype("delete");
		sysor.setOrid(dataid);
		sysor.setModule(datatype);
		sysor.setOpTime(DateTimeUtil.getTimeStamp());
		sysor.setCreateTime(DateTimeUtil.getTimeStamp());
		sysorService.save(sysor);
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/topublish.do")
	public String topublish(HttpServletRequest request, Model model){
		Category cate = categoryService.getRootCategory();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"parent","childId","seoTitle","seoDesc","seoKeyword"});
		Object json = JSONObject.fromObject(cate,config);
		System.out.println(json.toString());
		model.addAttribute("treeData",json.toString());
		String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
		String type = ServletRequestUtils.getStringParameter(request, "type", "");
		List<DataSource> dlist = this.dataSourceService.getPublishList(type, ids);
		model.addAttribute("dlist", dlist);
		model.addAttribute("datatype", type);
		return "datasource/publish";
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/publish.do")
	@ResponseBody
	public Map<String,String> publish(HttpServletRequest request, Model model){
		Map<String,String> json = new HashMap<String, String>();
		User user = (User) request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		String ctype = ServletRequestUtils.getStringParameter(request, "ctype", "");
		String type = ServletRequestUtils.getStringParameter(request, "type", "");
		String alldata = ServletRequestUtils.getStringParameter(request, "alldata", "");//id,title,ctid
	    String[] datas = alldata.split(";");
	    for(String data : datas){
	        String[] d = data.split(",");
	        //String dataid = d[0];
	        String title = d[1];
	        String contentid = d[2];
	        String[] catids = ctype.split(",");
	        for(String catid : catids){
	        	this.dataSourceService.publicData(type, catid, contentid,title,user.getNickName());
	        }
	    }
	    json.put("status", "1");
	    json.put("backurl", "datasource/lists.do?module="+type);
	    
	    return json;
	}
	@RequestMapping(value = "/test.do")
	public String test(HttpServletRequest request, Model model){
		return "datasource/test";
	}
	 
	public static void main(String[] args) {
		String tablename = "mm_content_news";
		String datatype = tablename.substring(Constants.FORMPREFIX.length(),tablename.length());
		System.out.println(datatype);
	}
	
}