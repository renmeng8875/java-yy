package com.richinfo.stat.service;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.richinfo.common.SystemProperties;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.privilege.entity.User;
import com.richinfo.stat.dao.CategoryAccessDao;
import com.richinfo.stat.entity.CategoryAccess;
import com.richinfo.stat.entity.CategoryAccessExl;

@Service
public class CategoryAccessService extends
		BaseServiceImpl<CategoryAccess, Integer> {
	
	private final static String IMAGE_ACCESSTOTALNUM_JPEG="accessTotalnum";
	private final static String IMAGE_ACCCLASSIFYTOTALNUM_JPEG="accClassifyTotalnum";
	private final static String IMAGE_DLTOTALNUM_JPEG="dltotalnum";
	private final static String IMAGE_CLASSIFYTOTALNUM_JPEG="classifyTotalnum";
	
	
	private CategoryAccessDao categoryAccessDao;
	
	@Autowired
    @Qualifier("categoryAccessDao")
	@Override
	public void setBaseDao(BaseDao<CategoryAccess, Integer> baseDao) 
	{
		this.baseDao = (CategoryAccessDao)baseDao;
		this.categoryAccessDao = (CategoryAccessDao)baseDao;
	}
	
	private String checkExcelBlock(int i,CategoryAccessExl c){
		if(c==null)
			return null;
		StringBuffer sf=new StringBuffer();
		
		//1检查日期
		if(StringUtils.isEmpty(c.getStatDate())||(!StringUtils.isEmpty(c.getStatDate())&&!DateTimeUtil.isValidDate(c.getStatDate(), "yyyy-MM-dd"))){
			sf.append("读取日期失败,统计日期为"+c.getStatDate()+";日期格式有误,日期格式应为yyyy-MM-dd;单元格格式必须为文本");
		}
		//2总访问次数
		if(!CommonUtil.isDecimal(c.getTotalAccessNum())){
			sf.append("总访问次数必须为数值;");
		}
		//3总IP
		if(!CommonUtil.isDecimal(c.getTotalIp())){
			sf.append("总IP必须为数值;");
		}
		//4总PV
		if(!CommonUtil.isDecimal(c.getTotalPv())){
			sf.append("总PV必须为数值;");
		}
		//5总UV
		if(!CommonUtil.isDecimal(c.getUv())){
			sf.append("总UV必须为数值;");
		}
		//6PV/IP比
		if(!CommonUtil.isDecimal(c.getPvIp())){
			sf.append("PV/IP比必须为数值;");
		}
		//7跳出次数
		if(!CommonUtil.isDecimal(c.getJumpNum())){
			sf.append("跳出次数必须为数值;");
		}
		//8跳出率
		if(!CommonUtil.isPercentages(c.getJumpRate())){
			sf.append("跳出率必须为百分比;");
		}
		//9尝试订购总次数
		if(!CommonUtil.isDecimal(c.getOrderTotalNum())){
			sf.append("尝试订购总次数必须为数值;");
		}
		//10尝试订购总用户数
		if(!CommonUtil.isDecimal(c.getOrderTotalUserNum())){
			sf.append("尝试订购总用户数必须为数值;");
		}
		//11登录下载次数
		if(!CommonUtil.isDecimal(c.getLoginDownloadNum())){
			sf.append("登录下载次数必须为数值;");
		}
		//12登录下载用户数
		if(!CommonUtil.isDecimal(c.getLoginDownloadUserNum())){
			sf.append("登录下载用户数必须为数值;");
		}
		//13免登录下载次数
		if(!CommonUtil.isDecimal(c.getNloginDownloadNum())){
			sf.append("免登录下载次数必须为数值;");
		}
		//14免登录下载用户数
		if(!CommonUtil.isDecimal(c.getNloginDownloadUserNum())){
			sf.append("免登录下载用户数必须为数值;");
		}
		//15免费下载次数
		if(!CommonUtil.isDecimal(c.getFreeDownloadNum())){
			sf.append("免费下载次数必须为数值;");
		}
		//16免费下载用户数
		if(!CommonUtil.isDecimal(c.getFreeDownloadUserNum())){
			sf.append("免费下载用户数必须为数值;");
		}
		//17付费下载次数
		if(!CommonUtil.isDecimal(c.getPayDownloadNum())){
			sf.append("付费下载次数必须为数值;");
		}
		//18 付费下载用户数
		if(!CommonUtil.isDecimal(c.getPayDownloadUserNum())){
			sf.append(" 付费下载用户数必须为数值;");
		}
		//19下载总次数
		if(!CommonUtil.isDecimal(c.getTotalDownloadNum())){
			sf.append("下载总次数必须为数值;");
		}
		//20下载用户总数
		if(!CommonUtil.isDecimal(c.getTotalAccessNum())){
			sf.append("下载用户总数必须为数值;");
		}
		
		//行号
		int index=i+2;
		String s=sf.toString();
		StringBuffer result=new StringBuffer();
		if(!StringUtils.isEmpty(s)){
			result.append("当前行").append(index).append(";").append(s);
		}
		return result.toString();
	}

	public Map<String,Object> importExcel(InputStream is){
		ImportParams params = new ImportParams();
		params.setHeadRows(1);
		List<CategoryAccessExl> list;
		List<String>blackList;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			list = ExcelImportUtil.importExcel(is, CategoryAccessExl.class, params);
			if(list!=null){
				int size=list.size();
				blackList=new ArrayList<String>();
				for(int i=0;i<size;i++){
					CategoryAccessExl categoryAccessExl=list.get(i);
					String blackStr=checkExcelBlock(i,categoryAccessExl);
					if(!StringUtils.isEmpty(blackStr)){
						blackList.add(blackStr);
						continue;
					}
					CategoryAccess oldCategoryAccess=categoryAccessDao.getCategoryAccessByPk(categoryAccessExl.getLevelone(),categoryAccessExl.getLeveltwo(), categoryAccessExl.getLevelthree(),DateTimeUtil.converToTimestamp(categoryAccessExl.getStatDate(), "yyyy-MM-dd"));
					if(null==oldCategoryAccess){
						CategoryAccess newCategoryAccess=new CategoryAccess();
						newCategoryAccess.copy(categoryAccessExl);
						categoryAccessDao.add(newCategoryAccess);
					}else{
						oldCategoryAccess.copy(categoryAccessExl);
						categoryAccessDao.merge(oldCategoryAccess);
					}
				}
				Object jObj = JSONArray.fromObject(blackList);
				json.put("blackList",jObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("status", "1");
			json.put("msg","读取文件失败,请检查再进行操作!");
			return json;
		}
		json.put("status", "0");
		return json;
	}
	
	/**
	* @desc:圈子下载访问数据
	* @param paramMap
	* @return 
	* @author wuzy
	* @date 2015-11-12
	 */
	public Map<String,Object> getCategoryAccessData(Map<String,Object> paramMap){
		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
		String ctone=ObjectUtils.toString(paramMap.get("ctone"));
		String cttwo=ObjectUtils.toString(paramMap.get("cttwo"));
		Page<Map<String,Object>> page=null;
		List<Map<String,Object>> list=null;
		if("singledata".equals(cttwo)){
			page=categoryAccessDao.getCategoryAccessList(beginTime,endTime);
			list=page.getItems();
			if(list!=null){
	        	int size=list.size();
	        	for(int i=0;i<size;i++){
	        		Map item=list.get(i);
	        		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
	        		item.put("STATDATESTR", statdate);
	        	}
	        }
		}else{
			page=categoryAccessDao.getCategoryAccessList(beginTime,endTime,cttwo);
			list=page.getItems();
		}
		
		long count=page.getTotal();
		Map<String,Object> resultMap =new HashMap<String,Object>();
		resultMap.put("count", count);
		String dataImg="";
		
		//处理统计图
		if("accessNum".equals(ctone)){
			if("daysum".equals(cttwo)){
				dataImg=getAccessImagePath(createAccessBarChart(list),paramMap,IMAGE_ACCESSTOTALNUM_JPEG);
			}
			if("classisum".equals(cttwo)){
				dataImg=getAccessImagePath(createAccessClassifyBarChart(list),paramMap,IMAGE_ACCCLASSIFYTOTALNUM_JPEG);
			}
		}else{
			if("daysum".equals(cttwo)){
				dataImg=getAccessImagePath(createBarChart(list),paramMap,IMAGE_DLTOTALNUM_JPEG);
			}
			if("classisum".equals(cttwo)){
				dataImg=getAccessImagePath(createClassifyBarChart(list),paramMap,IMAGE_CLASSIFYTOTALNUM_JPEG);
			}
		}
		resultMap.put("dataimg",dataImg);
		resultMap.put("list", list);
		return resultMap;
	}
	
	/**
	 * *******下载量-按日
	 */
	private DefaultCategoryDataset createDataset(List<Map<String,Object>> list) {  
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();  
        String rowKey1 = "下载总次数";  
        String rowKey2 = "下载总用户数"; 
        
        if(list!=null){
        	int size=list.size();
        	for(int i=0;i<size;i++){
        		Map item=list.get(i);
        		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
        		item.put("STATDATESTR", statdate);
        		Double totalDownloadNum=Double.valueOf(ObjectUtils.toString(item.get("DTOTALDLNUM")));
                Double totalDownloadUserNum=Double.valueOf(ObjectUtils.toString(item.get("DTDLUSERNUM")));
        		categoryDataset.setValue(totalDownloadNum, rowKey1, statdate);
                categoryDataset.setValue(totalDownloadUserNum, rowKey2, statdate);
        	}
        }
        return categoryDataset;  
    }  
  
	private JFreeChart createBarChart(List<Map<String,Object>> list){
    	DefaultCategoryDataset data=createDataset(list);
        JFreeChart barChart = ChartFactory.createBarChart("下载量","","",data,PlotOrientation.VERTICAL,true,true,false);
        Font font1 = new Font("SansSerif", 10, 20); // 设定字体、类型、字号
        barChart.getTitle().setFont(font1); // 标题
        
        Font font2 = new Font("SansSerif", 10, 12); // 设定字体、类型、字号
        barChart.getLegend().setItemFont(font2);// 最下方
        
        CategoryPlot categoryplot = barChart.getCategoryPlot(); // 获得
        CategoryAxis domainAxis = categoryplot.getDomainAxis();   
        domainAxis.setLabelFont(font2);// 轴标题
        domainAxis.setTickLabelFont(font2);// 轴数值  
        domainAxis.setTickLabelPaint(Color.black) ; // 字体颜色
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示
        
        categoryplot.setBackgroundPaint(Color.white); //设置网格背景颜色     
        categoryplot.setDomainGridlinePaint(Color.pink); //设置网格竖线颜色      
        categoryplot.setRangeGridlinePaint(Color.pink);//设置网格横线颜色      
        //显示每个柱的数值，并修改该数值的字体属性   
        BarRenderer renderer = new BarRenderer();
        renderer.setIncludeBaseInRange(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());   
        renderer.setBaseItemLabelsVisible(true);
        categoryplot.setRenderer(renderer);   

        return barChart;
    }
    
    /**
     * @desc:下载量-分类汇总
     * @param list
     * @return 
     * @author wuzy
     * @date 2015-11-11
     */
    private DefaultCategoryDataset createClassifyDataset(List<Map<String,Object>> list) {  
    	DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();  
    	String rowKey1 = "下载总次数";  
        String rowKey2 = "下载总用户数";   
    	
    	if(list!=null){
    		int size=list.size();
    		for(int i=0;i<size;i++){
    			Map item=list.get(i);
    			String levelThree=ObjectUtils.toString(item.get("LEVELTHREE"));
    			
    			Double totalDownloadNum=Double.valueOf(ObjectUtils.toString(item.get("DTOTALDLNUM")));
                Double totalDownloadUserNum=Double.valueOf(ObjectUtils.toString(item.get("DTDLUSERNUM")));
                
        		categoryDataset.setValue(totalDownloadNum, rowKey1, levelThree);
                categoryDataset.setValue(totalDownloadUserNum, rowKey2, levelThree);
    		}
    	}
    	return categoryDataset;  
    }
    
    private JFreeChart createClassifyBarChart(List<Map<String,Object>> list) {
    	DefaultCategoryDataset data=createClassifyDataset(list);
    	JFreeChart barChart = ChartFactory.createBarChart("","","",data,PlotOrientation.VERTICAL,true,true,false);
    	Font font1 = new Font("SansSerif", 10, 20); // 设定字体、类型、字号
    	barChart.getTitle().setFont(font1); // 标题
    	
    	Font font3 = new Font("SansSerif", 10, 12); // 设定字体、类型、字号
    	barChart.getLegend().setItemFont(font3);// 最下方
    	
    	CategoryPlot categoryplot = barChart.getCategoryPlot(); // 获得
    	Font font2 = new Font("SansSerif", 10, 16); // 设定字体、类型、字号
    	// X 轴
        CategoryAxis domainAxis = categoryplot.getDomainAxis();   
        domainAxis.setLabelFont(font2);// 轴标题
        domainAxis.setTickLabelFont(font2);// 轴数值  
        domainAxis.setTickLabelPaint(Color.black) ; // 字体颜色
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示 
           
        // Y 轴
        ValueAxis rangeAxis = categoryplot.getRangeAxis();   
        rangeAxis.setLabelFont(font2); 
        rangeAxis.setLabelPaint(Color.BLUE) ; // 字体颜色
        rangeAxis.setTickLabelFont(font2); 
        
        categoryplot.setBackgroundPaint(Color.white); //设置网格背景颜色     
        categoryplot.setDomainGridlinePaint(Color.pink); //设置网格竖线颜色      
        categoryplot.setRangeGridlinePaint(Color.pink);//设置网格横线颜色      
        //显示每个柱的数值，并修改该数值的字体属性   
        BarRenderer renderer = new BarRenderer();
        renderer.setIncludeBaseInRange(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());   
        renderer.setBaseItemLabelsVisible(true);
        categoryplot.setRenderer(renderer);  
    	
    	return barChart;
    }
    
    /**
    * @desc:访问量-按日  
    * @param list
    * @return 
    * @author wuzy
    * @date 2015-11-11
     */
    private DefaultCategoryDataset createAccessDataset(List<Map<String,Object>> list) {  
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();  
        String rowKey1 = "总IP";  
    	String rowKey2 = "总PV";  
    	String rowKey3 = "总UV";    
        
        if(list!=null){
        	int size=list.size();
        	for(int i=0;i<size;i++){
        		Map item=list.get(i);
        		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
        		item.put("STATDATESTR", statdate);
        		
        		Double totalIpNum=Double.valueOf(ObjectUtils.toString(item.get("DTIP")));
    			categoryDataset.setValue(totalIpNum, rowKey1, statdate);
    			
    			Double totalPvNum=Double.valueOf(ObjectUtils.toString(item.get("DTPV")));
    			categoryDataset.setValue(totalPvNum, rowKey2, statdate);
    			
    			Double totalUvNum=Double.valueOf(ObjectUtils.toString(item.get("DTUV")));
    			categoryDataset.setValue(totalUvNum, rowKey3, statdate);
        	}
        }
        return categoryDataset;  
    }
    
    private JFreeChart createAccessBarChart(List<Map<String,Object>> list) {
    	if(list==null){
    		return null;
    	}
    	DefaultCategoryDataset data=createAccessDataset(list);
        JFreeChart barChart = ChartFactory.createBarChart("","","",data,PlotOrientation.VERTICAL,true,true,false);
        Font font1 = new Font("SansSerif", 10, 20); // 设定字体、类型、字号
        barChart.getTitle().setFont(font1); // 标题
        
        Font font2 = new Font("SansSerif", 10, 12); // 设定字体、类型、字号
        barChart.getLegend().setItemFont(font2);// 最下方
        
        CategoryPlot categoryplot = barChart.getCategoryPlot(); // 获得
        CategoryAxis domainAxis = categoryplot.getDomainAxis();   
        domainAxis.setLabelFont(font2);// 轴标题
        domainAxis.setTickLabelFont(font2);// 轴数值  
        domainAxis.setTickLabelPaint(Color.black) ; // 字体颜色
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示
        
        categoryplot.setBackgroundPaint(Color.white); //设置网格背景颜色     
        categoryplot.setDomainGridlinePaint(Color.pink); //设置网格竖线颜色      
        categoryplot.setRangeGridlinePaint(Color.pink);//设置网格横线颜色      
        //显示每个柱的数值，并修改该数值的字体属性   
        BarRenderer renderer = new BarRenderer();
        renderer.setIncludeBaseInRange(true);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());   
        renderer.setBaseItemLabelsVisible(true);
        categoryplot.setRenderer(renderer);   
        return barChart;
    }
    
    private String getAccessImagePath(JFreeChart jFreeChart,Map paramMap,String imageName){
    	FileOutputStream fos=null;
    	String filePath="";
    	String fileName="";
    	String webFilePath=null;
		try {
			if(jFreeChart==null)
				return filePath;
			
			User user=(User)paramMap.get("user");
			String page=ObjectUtils.toString(paramMap.get("page"));
			//JFreeChart jFreeChart=createAccessBarChart(list);
			String savePath=SystemProperties.getInstance().getProperty("report.savePath");
			String parentDir=(user!=null?user.getUserId()+"":(DateTimeUtil.getFormatDateTime(new Date(), "yyyyMMdd")));
			filePath="datachat"+File.separator+parentDir+File.separator+page+File.separator;
			webFilePath=SystemProperties.getInstance().getProperty("report.prefix")+filePath;
			filePath=savePath+filePath;
			File f=new File(filePath);
			createFileDir(f);
			fileName=imageName+DateTimeUtil.getFormatDateTime(new Date(), "yyyyMMddHHmmssSSS")+".jpg";
			filePath=filePath+fileName;
			fos = new FileOutputStream(filePath);
			ChartUtilities.writeChartAsJPEG(fos,1,jFreeChart,900,600,null); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	return webFilePath+fileName;
    }
    
    
    /**
     * @desc:访问量-分类汇总
     * @param list
     * @return 
     * @author wuzy
     * @date 2015-11-11
      */
     private DefaultCategoryDataset createAccessClassifyDataset(List<Map<String,Object>> list) {  
         DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();  
         String rowKey1 = "总IP";  
     	 String rowKey2 = "总PV";  
     	 String rowKey3 = "总UV";
         
         if(list!=null){
         	int size=list.size();
         	for(int i=0;i<size;i++){
         		Map item=list.get(i);
         		String levelThree=ObjectUtils.toString(item.get("LEVELTHREE"));
         		Double totalIpNum=Double.valueOf(ObjectUtils.toString(item.get("DTIP")));
    			categoryDataset.setValue(totalIpNum, rowKey1, levelThree);
    			
    			Double totalPvNum=Double.valueOf(ObjectUtils.toString(item.get("DTPV")));
    			categoryDataset.setValue(totalPvNum, rowKey2, levelThree);
    			
    			Double totalUvNum=Double.valueOf(ObjectUtils.toString(item.get("DTUV")));
    			categoryDataset.setValue(totalUvNum, rowKey3, levelThree);
         	}
         }
         return categoryDataset;  
     }
     
     private JFreeChart createAccessClassifyBarChart(List<Map<String,Object>> list) {
     	DefaultCategoryDataset data=createAccessClassifyDataset(list);
         JFreeChart barChart = ChartFactory.createBarChart("","","",data,PlotOrientation.VERTICAL,true,true,false);
         Font font1 = new Font("SansSerif", 10, 20); // 设定字体、类型、字号
         barChart.getTitle().setFont(font1); // 标题
         
         Font font3 = new Font("SansSerif", 10, 12); // 设定字体、类型、字号
         barChart.getLegend().setItemFont(font3);// 最下方
         
         CategoryPlot categoryplot = barChart.getCategoryPlot(); // 获得
     	 Font font2 = new Font("SansSerif", 10, 16); // 设定字体、类型、字号
     	// X 轴
         CategoryAxis domainAxis = categoryplot.getDomainAxis();   
         domainAxis.setLabelFont(font2);// 轴标题
         domainAxis.setTickLabelFont(font2);// 轴数值  
         domainAxis.setTickLabelPaint(Color.BLUE) ; // 字体颜色
         domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示 
            
         // Y 轴
         ValueAxis rangeAxis = categoryplot.getRangeAxis();   
         rangeAxis.setLabelFont(font2); 
         rangeAxis.setLabelPaint(Color.BLUE) ; // 字体颜色
         rangeAxis.setTickLabelFont(font2);
         
         domainAxis.setLabelFont(font2);// 轴标题
         domainAxis.setTickLabelFont(font2);// 轴数值  
         domainAxis.setTickLabelPaint(Color.black) ; // 字体颜色
         domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的label斜显示
         
         categoryplot.setBackgroundPaint(Color.white); //设置网格背景颜色     
         categoryplot.setDomainGridlinePaint(Color.pink); //设置网格竖线颜色      
         categoryplot.setRangeGridlinePaint(Color.pink);//设置网格横线颜色      
         //显示每个柱的数值，并修改该数值的字体属性   
         BarRenderer renderer = new BarRenderer();
         renderer.setIncludeBaseInRange(true);
         renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());   
         renderer.setBaseItemLabelsVisible(true);
         categoryplot.setRenderer(renderer);   
         return barChart;
     }
     
     public Workbook getWorkbook(Map<String,Object> paramMap){
 		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
  		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
  		String cttwo=ObjectUtils.toString(paramMap.get("cttwo"));
  		String ctone=ObjectUtils.toString(paramMap.get("ctone"));
  		String template="static/template/catagerytemplate.xlsx";
  		
  		String codedFileName="圈子下载访问数据_";
  		if("accessNum".equals(ctone)){
  			codedFileName+="访问量";
  		}else{
  			codedFileName+="下载量";
  		}
  		
  		List<Map<String,Object>> list=null;
		//按日
		if("daysum".equals(cttwo)){
			    Page<Map<String,Object>> page=categoryAccessDao.getCategoryAccessList(beginTime,endTime,"");
			    list=page.getItems();
		 		if(list!=null){
		 			int size=list.size();
		 			for(int i=0;i<size;i++){
		 				Map<String,Object> item=list.get(i);
		 				String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy-MM-dd");
		        		item.put("STATDATESTR", statdate);
		 			}
		 		}
		 		template="static/template/catagerytemplate.xlsx";
		 		//"圈子下载访问数据";
		 		codedFileName+="_按日";
		}
		//分类
		if("classisum".equals(cttwo)){
			Page<Map<String,Object>> page=categoryAccessDao.getCategoryAccessList(beginTime,endTime,"classisum");
			list=page.getItems();
				template="static/template/catagerytemplate2.xlsx";
				codedFileName+="_分类";
		}
		
		//单独数据
		if("singledata".equals(cttwo)){
			Page<Map<String,Object>> page=categoryAccessDao.getCategoryAccessList(beginTime,endTime);
			list=page.getItems();
	  		if(list!=null){
	 			int size=list.size();
	 			for(int i=0;i<size;i++){
	 				Map<String,Object> item=list.get(i);
	 				String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy-MM-dd");
	        		item.put("STATDATESTR", statdate);
	 			}
	 		}
			template="static/template/catagerytemplate3.xlsx";
			codedFileName+="_单独数据";
		}
		paramMap.put("codedFileName", codedFileName);
		
 		Map<String,Object> map = new HashMap<String,Object>();  
 		map.put("list",list); 

 		Workbook workbook=null;
		try {
			TemplateExportParams params = new TemplateExportParams(template);
			workbook = ExcelExportUtil.exportExcel(params, map);
		} catch (Exception e) {
			e.printStackTrace();
		} 
 	 	return workbook;
 	}
     
	private void createFileDir(File f) throws IOException {
		try {
			if (f.exists()) {
				FileUtils.forceDelete(f);
			}
		} catch (Exception e) {
		}
		FileUtils.forceMkdir(f);
	}
    
     @Scheduled(cron="0 15 0 ? * WED")
     public void deleteImageFile(){
    	 try{
    		 String classPath=CategoryAccessService.class.getResource(File.separator).getPath();
        	 String webinfoPath=classPath.substring(0,classPath.indexOf("classes"));
        	 String dataChatPath=webinfoPath+"admin/report/datachat";
        	 File file=new File(dataChatPath);
        	 if(file.exists()){
        		 FileUtils.forceDelete(file);
        	 }
    	 }catch(Exception e){
    	 }
     }
    
}
