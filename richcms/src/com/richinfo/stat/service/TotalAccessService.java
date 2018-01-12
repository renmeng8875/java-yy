package com.richinfo.stat.service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.jeecgframework.poi.excel.entity.result.ExcelImportResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.SystemProperties;
import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.pagination.Page;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.CommonUtil;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.privilege.entity.User;
import com.richinfo.stat.dao.TotalAccessDao;
import com.richinfo.stat.entity.TotalAccess;
import com.richinfo.stat.entity.TotalAccessExl;

@Service
public class TotalAccessService extends BaseServiceImpl<TotalAccess, Integer> {

	private final static String IMAGE_TOTALACCESSNUM_JPEG="totalaccessnum";
	private final static String IMAGE_DOWNLDNUM_JPEG="downldnum";
	private final static String IMAGE_AVGACCESSNUM_JPEG="avgaccessnum";
	private final static String IMAGE_UVTRENDNUM_JPEG="uvtrendnum";
	
	private TotalAccessDao totalAccessDao;

	@Autowired
	@Qualifier("totalAccessDao")
	@Override
	public void setBaseDao(BaseDao<TotalAccess, Integer> baseDao) {
		this.baseDao = (TotalAccessDao) baseDao;
		this.totalAccessDao = (TotalAccessDao) baseDao;
	}
	
	private String checkExcelBlock(int i,TotalAccessExl c){
		if(c==null)
			return null;
		StringBuffer sf=new StringBuffer();
		
		//检查日期
		if(StringUtils.isEmpty(c.getStatDate())||(!StringUtils.isEmpty(c.getStatDate())&&!DateTimeUtil.isValidDate(c.getStatDate(), "yyyy-MM-dd"))){
			sf.append("读取日期失败,统计日期为"+c.getStatDate()+";日期格式有误,日期格式应为yyyy-MM-dd;单元格格式必须为文本");
		}
		
		//2IP
		if(!CommonUtil.isDecimal(c.getIp())){
			sf.append("IP必须为数值;");
		}
		
		//3UV
		if(!CommonUtil.isDecimal(c.getUv())){
			sf.append("UV必须为数值;");
		}
		
		//4PV
		if(!CommonUtil.isDecimal(c.getPv())){
			sf.append("PV必须为数值;");
		}
		
		//5总下载用户数
		if(!CommonUtil.isDecimal(c.getTotalUserNum())){
			sf.append("总下载用户数必须为数值;");
		}
		
		//6总下载量
		if(!CommonUtil.isDecimal(c.getTotalDownloadNum())){
			sf.append("总下载量必须为数值;");
		}
		
		//下载量（登录）
		if(!CommonUtil.isDecimal(c.getLoginDownloadNum())){
			sf.append("下载量（登录）必须为数值;");
		}
		
		//下载量（免登陆）
		if(!CommonUtil.isDecimal(c.getNloginDownloadNum())){
			sf.append("下载量（免登陆）必须为数值;");
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
		
		List<TotalAccessExl> list;
		List<String>blackList;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			list = ExcelImportUtil.importExcel(is, TotalAccessExl.class, params);
			if(list!=null){
				int size=list.size();
				blackList=new ArrayList<String>();
				for(int i=0;i<size;i++){
					TotalAccessExl totalAccessExl=list.get(i);
					String blackStr=checkExcelBlock(i,totalAccessExl);
					if(!StringUtils.isEmpty(blackStr)){
						blackList.add(blackStr);
						continue;
					}
					TotalAccess oldTotalAccess=totalAccessDao.getTotalAccessByPk(DateTimeUtil.converToTimestamp(totalAccessExl.getStatDate(), "yyyy-MM-dd"));
					if(null==oldTotalAccess){
						TotalAccess newTotalAccess=new TotalAccess();
						newTotalAccess.copy(totalAccessExl);
						totalAccessDao.add(newTotalAccess);
					}else{
						oldTotalAccess.copy(totalAccessExl);
						totalAccessDao.merge(oldTotalAccess);
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
 	* @desc:下载数据统计
 	* @param paramMap
 	* @return 
 	* @author wuzy
 	* @date 2015-11-12
 	 */
 	public Map<String,Object> getTotalAccessData(Map<String,Object> paramMap){
 		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
 		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
 		String ctone=ObjectUtils.toString(paramMap.get("ctone"));
 	
 		Page<Map<String,Object>> page=totalAccessDao.getTotalAccessList(beginTime,endTime);
 		List<Map<String,Object>> list=page.getItems();
 		
 		long count=page.getTotal();
 		Map<String,Object> resultMap =new HashMap<String,Object>();
 		resultMap.put("count", count);
 		String dataImg="";
 		
 		//处理统计图
		if ("taccessNum".equals(ctone)) {
			dataImg = getTotalAccessImagePath(createTotalAccessChart(list), paramMap,IMAGE_TOTALACCESSNUM_JPEG);
		}
		if ("downldNum".equals(ctone)) {
			dataImg = getTotalAccessImagePath(createDownloadNumChart(list), paramMap,IMAGE_DOWNLDNUM_JPEG);
		}

		if ("avgsum".equals(ctone)) {
			dataImg = getTotalAccessImagePath(createAvgAccessChart(list), paramMap,IMAGE_AVGACCESSNUM_JPEG);
		}
		if ("uvsum".equals(ctone)) {
			dataImg = getTotalAccessImagePath(createUvTrendChart(list), paramMap,IMAGE_UVTRENDNUM_JPEG);
		}
 		
 		resultMap.put("dataimg",dataImg);
 		resultMap.put("list", list);
 		return resultMap;
 	}
 	
 	/**
 	* @desc: 总体访问
 	* @param list
 	* @param paramMap
 	* @return 
 	* @author wuzy
 	* @date 2015-11-12
 	 */
 	private String getTotalAccessImagePath(JFreeChart jFreeChart,Map paramMap,String imageName){
    	FileOutputStream fos=null;
    	String filePath="";
    	String fileName="";
    	String webFilePath="";
		try {
			if(jFreeChart==null){
				return filePath;
			}
			
			User user=(User)paramMap.get("user");
			String page=ObjectUtils.toString(paramMap.get("page"));
			String savePath=SystemProperties.getInstance().getProperty("report.savePath");
			String parentDir=(user!=null?user.getUserId()+"":(DateTimeUtil.getFormatDateTime(new Date(), "yyyyMMdd")));
			filePath="datachat"+File.separator+parentDir+File.separator+page+File.separator;
			webFilePath=SystemProperties.getInstance().getProperty("report.prefix")+filePath;
			filePath=savePath+filePath;
			File f=new File(filePath);
			createFileDir(f);
			fileName=imageName+DateTimeUtil.getFormatDateTime(new Date(), "yyyyMMddHHmmssSSS")+".jpg";
			filePath=filePath+fileName;
			File dFile=new File(filePath);
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
 	
    public static JFreeChart createTotalAccessChart(List<Map<String,Object>>list) {
    	if(list==null){
    		return null;
    	}
    	
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();
    	String rowKey1="PV";
    	String rowKey2="IP";
    	String rowKey3="UV";
    	
    	int size=list.size();
    	Map item=null;
    	for(int i=0;i<size;i++){
    		item=list.get(i);
    		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
    		item.put("STATDATESTR", statdate);
    		Double tPv=Double.valueOf(ObjectUtils.toString(item.get("PV")));
            Double tIp=Double.valueOf(ObjectUtils.toString(item.get("IP")));
            Double tUv=Double.valueOf(ObjectUtils.toString(item.get("UV")));
            tPv=tPv/10000;
            tIp=tIp/10000;
            tUv=tUv/10000;
    		dataset1.addValue(tPv, rowKey1, statdate);
            dataset2.addValue(tIp,rowKey2, statdate);
            dataset3.addValue(tUv, rowKey3, statdate);

    	}
        CategoryItemLabelGenerator generator= new StandardCategoryItemLabelGenerator();
        
        BarRenderer renderer = new BarRenderer();
        renderer.setItemLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);
        renderer.setSeriesPaint(0, Color.decode("#9ACD32"));
        renderer.setMaximumBarWidth(0.1);
        renderer.setMinimumBarLength(0.1);
        
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
         
        plot.setDomainAxis(new CategoryAxis(""));
        plot.setRangeAxis(new NumberAxis("(单位:万)"));
 
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
       
 
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.decode("#E0FFFF"));
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
 
        ValueAxis rangeAxis2 = new NumberAxis("");
        plot.setRangeAxis(1, rangeAxis2);
        plot.setDataset(2, dataset3);
        
        CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        renderer3.setSeriesPaint(0, Color.decode("#6495ED"));
        plot.setRenderer(2, renderer3);
        plot.mapDatasetToRangeAxis(2, 1);
 
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("总体访问情况");
        return chart;
     
}
    
    public static JFreeChart createDownloadNumChart(List<Map<String,Object>>list) {
    	if(list==null){
    		return null;
    	}
    	
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();

    	String rowKey1="总下载量";
    	String rowKey2="下载量(登录)";
    	
    	int size=list.size();
    	Map item=null;
    	for(int i=0;i<size;i++){
    		item=list.get(i);
    		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
    		item.put("STATDATESTR", statdate);
    		Double totaldlNum=Double.valueOf(ObjectUtils.toString(item.get("TOTALDLNUM")));
            Double logindlNum=Double.valueOf(ObjectUtils.toString(item.get("LOGINDLNUM")));

            totaldlNum=totaldlNum/10000;
    		dataset1.addValue(totaldlNum, rowKey1, statdate);
            dataset3.addValue(logindlNum,rowKey2, statdate);
    	}
    	
        CategoryItemLabelGenerator generator= new StandardCategoryItemLabelGenerator();
        
        BarRenderer renderer = new BarRenderer();
        renderer.setItemLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);
        renderer.setMaximumBarWidth(0.1);
        renderer.setMinimumBarLength(0.1);
        renderer.setSeriesPaint(0, Color.decode("#6495ED"));
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
         
        plot.setDomainAxis(new CategoryAxis(""));
        plot.setRangeAxis(new NumberAxis("(单位:万)"));
 
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
 
        ValueAxis rangeAxis2 = new NumberAxis("");
        plot.setRangeAxis(1, rangeAxis2);
        plot.setDataset(2, dataset3);
        
        CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        renderer3.setSeriesPaint(0, Color.decode("#FFD700"));
        plot.setRenderer(2, renderer3);
        
        plot.mapDatasetToRangeAxis(2, 1);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("总体下载情况");
        return chart;
     
}
    
    public static JFreeChart createAvgAccessChart(List<Map<String,Object>>list) {
    	if(list==null){
    		return null;
    	}
    	
    	DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
    	DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();

    	String rowKey1="PV";
    	String rowKey2="人均浏览量";
    	
    	int size=list.size();
    	Map item=null;
    	for(int i=0;i<size;i++){
    		item=list.get(i);
    		String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy/MM/dd");
    		item.put("STATDATESTR", statdate);
    		Double pv=Double.valueOf(ObjectUtils.toString(item.get("PV")));
            Double pvUv=Double.valueOf(ObjectUtils.toString(item.get("PVUV")));
            pv=pv/10000;
            
    		dataset1.addValue(pv, rowKey1, statdate);
            dataset3.addValue(pvUv,rowKey2, statdate);
    	}
    	
        CategoryItemLabelGenerator generator= new StandardCategoryItemLabelGenerator();
        
        BarRenderer renderer = new BarRenderer();
        renderer.setItemLabelGenerator(generator);
        renderer.setItemLabelsVisible(true);
        renderer.setMaximumBarWidth(0.1);
        renderer.setMinimumBarLength(0.1);
        renderer.setSeriesPaint(0, Color.decode("#D19275"));
        CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);
         
        plot.setDomainAxis(new CategoryAxis(""));
        plot.setRangeAxis(new NumberAxis("(单位:万)"));
 
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
 
        ValueAxis rangeAxis2 = new NumberAxis("");
        plot.setRangeAxis(1, rangeAxis2);
        plot.setDataset(2, dataset3);
        
        CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        renderer3.setSeriesPaint(0, Color.decode("#C0C0C0"));
        plot.setRenderer(2, renderer3);
        
        plot.mapDatasetToRangeAxis(2, 1);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        JFreeChart chart = new JFreeChart(plot);
        chart.setTitle("MM门户PV与人均浏览量");
        return chart;
     
}
    private  JFreeChart createUvTrendChart(List<Map<String,Object>>list){
    	XYDataset dataset=createUvTrendDataset(list);
    	String str = "MM门户IP与UV趋势图";
    	JFreeChart jFreeChart = ChartFactory.createTimeSeriesChart(str, null, "",dataset, true, true, false);
        jFreeChart.getTitle().setFont(new Font(str, 1, 18));

        XYPlot plot = (XYPlot)jFreeChart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(false);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        
        plot.getDomainAxis().setLowerMargin(0.0D);
        plot.getDomainAxis().setLabelFont(new Font(str, 1, 10));
        plot.getDomainAxis().setTickLabelFont(new Font(str, 0, 10));
        DateAxis dateAxis=(DateAxis)plot.getDomainAxis();
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY,1));
        dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy/MM/dd"));
        plot.setDomainAxis(dateAxis);
        
        plot.setRangeAxis(new NumberAxis("(单位:万)"));
        plot.getRangeAxis().setLabelFont(new Font(str, 1, 14));
        plot.getRangeAxis().setTickLabelFont(new Font(str, 0, 10));
        
        jFreeChart.getLegend().setItemFont(new Font(str, 0, 14));
        jFreeChart.getLegend().setFrame(BlockBorder.NONE);
        jFreeChart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
       
        XYItemRenderer xyItemRenderer = plot.getRenderer();
        if ((xyItemRenderer instanceof XYLineAndShapeRenderer)){
          XYLineAndShapeRenderer xyLineAndShapeRenderer = (XYLineAndShapeRenderer)xyItemRenderer;
          xyLineAndShapeRenderer.setBaseShapesVisible(false);
          xyLineAndShapeRenderer.setDrawSeriesLineAsPath(true);
          xyLineAndShapeRenderer.setAutoPopulateSeriesStroke(false);
          xyLineAndShapeRenderer.setBaseStroke(new BasicStroke(3.0F, 1, 2), false);
          xyLineAndShapeRenderer.setSeriesPaint(0, Color.RED);
          xyLineAndShapeRenderer.setSeriesPaint(1, new Color(24, 123, 58));
          xyLineAndShapeRenderer.setSeriesPaint(2, new Color(149, 201, 136));
          xyLineAndShapeRenderer.setSeriesPaint(3, new Color(1, 62, 29));
          xyLineAndShapeRenderer.setSeriesPaint(4, new Color(81, 176, 86));
          xyLineAndShapeRenderer.setSeriesPaint(5, new Color(0, 55, 122));
          xyLineAndShapeRenderer.setSeriesPaint(6, new Color(0, 92, 165));
        }
        return jFreeChart;
    }

	private TimeSeriesCollection createUvTrendDataset(List<Map<String,Object>> list) {
		if (list == null) {
			return null;
		}
		// 时间曲线数据集合
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		// 访问量统计时间线
		TimeSeries timeSeriesA = new TimeSeries("IP", Day.class);
		TimeSeries timeSeriesB = new TimeSeries("UV", Day.class);

		int size = list.size();
		Map item = null;
		try{
			for (int i = 0; i < size; i++) {
				item = list.get(i);
				String statdate = DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))),"yyyy/MM/dd");
				item.put("STATDATESTR", statdate);

				Date d = DateTimeUtil.getDateFromStr(statdate, "yyyy/MM/dd");
				
				Double ip = Double.valueOf(ObjectUtils.toString(item.get("IP")));
				Double uv = Double.valueOf(ObjectUtils.toString(item.get("UV")));
				ip=ip/10000;
				uv=uv/10000;
				Calendar c=Calendar.getInstance();
				c.setTime(d);
				int year = c.get(Calendar.YEAR);
	            int month = c.get(Calendar.MONTH) + 1;
	            int day = c.get(Calendar.DATE);

				// 构造数据集合
				timeSeriesA.add(new Day(day,month,year), ip);
				timeSeriesB.add(new Day(day,month,year), uv);

			}
			lineDataset.addSeries(timeSeriesA);
			lineDataset.addSeries(timeSeriesB);
		}catch(Exception e){
			e.printStackTrace();
		}
		return lineDataset;
	}

	public Workbook getWorkbook(Map<String,Object> paramMap){
 		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
 		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
 	
 		Page<Map<String,Object>> page=totalAccessDao.getTotalAccessList(beginTime,endTime);
 		List<Map<String,Object>> list=page.getItems();
 		if(list!=null){
 			int size=list.size();
 			for(int i=0;i<size;i++){
 				Map<String,Object> item=list.get(i);
 				String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy-MM-dd");
        		item.put("STATDATESTR", statdate);
 			}
 		}
 		String template="static/template/totaltemplate.xlsx";
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
}
