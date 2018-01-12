package com.richinfo.stat.service;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTick;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
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
import com.richinfo.stat.dao.ChannelAccessDao;
import com.richinfo.stat.entity.ChannelAccess;
import com.richinfo.stat.entity.ChannelAccessExl;

@Service
public class ChannelAccessService extends
		BaseServiceImpl<ChannelAccess, Integer> {

	private final static String IMAGE_CHANNELNUM_JPEG="channelnum";
	
	private ChannelAccessDao channelAccessDao;

	@Autowired
	@Qualifier("channelAccessDao")
	@Override
	public void setBaseDao(BaseDao<ChannelAccess, Integer> baseDao) {
		this.baseDao = (ChannelAccessDao) baseDao;
		this.channelAccessDao = (ChannelAccessDao) baseDao;
	}
	
	private String checkExcelBlock(int i,ChannelAccessExl c){
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
		
		//3pv
		if(!CommonUtil.isDecimal(c.getPv())){
			sf.append("pv必须为数值;");
		}
		
		//4uv
		if(!CommonUtil.isDecimal(c.getUv())){
			sf.append("uv必须为数值;");
		}
		
		//5访问数
		if(!CommonUtil.isDecimal(c.getAccessNum())){
			sf.append("访问数必须为数值;");
		}
		
		//6下载用户数
		if(!CommonUtil.isDecimal(c.getDownloadUserNum())){
			sf.append("下载用户数必须为数值;");
		}
		
		//7下载数
		if(!CommonUtil.isDecimal(c.getDownloadNum())){
			sf.append("下载数必须为数值;");
		}
		
		//行号
		int index=i+2;
		String s=sf.toString();
		StringBuffer result=new StringBuffer();
		if(!StringUtils.isEmpty(s)){
			result.append(c.getChannelNum()+"当前行").append(index).append(";").append(s);
		}
		return result.toString();
	}
	
	public Map<String,Object> importExcel(InputStream is){
		ImportParams params = new ImportParams();
		params.setHeadRows(1);
		List<ChannelAccessExl> list;
		List<String>blackList;
		Map<String,Object> json = new HashMap<String, Object>();
		try {
			list = ExcelImportUtil.importExcel(is, ChannelAccessExl.class, params);
			if(list!=null){
				int size=list.size();
				blackList=new ArrayList<String>();
				for(int i=0;i<size;i++){
					ChannelAccessExl channelAccessExl=list.get(i);
					String blackStr=checkExcelBlock(i,channelAccessExl);
					
					if(!StringUtils.isEmpty(blackStr)){
						blackList.add(blackStr);
						continue;
					}
					ChannelAccess oldChannelAccess=channelAccessDao.getChannelAccessByPk(channelAccessExl.getChannelNum(),DateTimeUtil.converToTimestamp(channelAccessExl.getStatDate(), "yyyy-MM-dd"));
					if(null==oldChannelAccess){
						ChannelAccess newChannelAccess=new ChannelAccess();
						newChannelAccess.copy(channelAccessExl);
						channelAccessDao.add(newChannelAccess);
					}else{
						oldChannelAccess.copy(channelAccessExl);
						channelAccessDao.merge(oldChannelAccess);
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
 	public Map<String,Object> getChannelAccessData(Map<String,Object> paramMap){
 		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
 		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
 		String ctone=ObjectUtils.toString(paramMap.get("ctone"));
 	
 		Page<Map<String,Object>> page=channelAccessDao.getChannelAccessList(beginTime,endTime);
 		List<Map<String,Object>> list=page.getItems();
 		
 		long count=page.getTotal();
 		Map<String,Object> resultMap =new HashMap<String,Object>();
 		resultMap.put("count", count);
 		String dataImg="";

		dataImg = getChannelImagePath(list, paramMap);
	
 		resultMap.put("dataimg",dataImg);
 		resultMap.put("list", list);
 		return resultMap;
 	}
 	
 	/**
 	* @desc: 渠道推广
 	* @param list
 	* @param paramMap
 	* @return 
 	* @author wuzy
 	* @date 2015-11-12
 	 */
 	private String getChannelImagePath(List<Map<String,Object>> list,Map paramMap){
    	FileOutputStream fos=null;
    	String filePath="";
    	String fileName="";
    	String webFilePath="";
		try {
			if(list==null){
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
			
			fileName=IMAGE_CHANNELNUM_JPEG+DateTimeUtil.getFormatDateTime(new Date(), "yyyyMMddHHmmssSSS")+".jpg";
			filePath=filePath+fileName;
			
			fos = new FileOutputStream(filePath);
			JFreeChart jFreeChart=createChannelChart(list);
			ChartUtilities.writeChartAsJPEG(fos,1,jFreeChart,900,650,null);
			
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
    
	private static JFreeChart createChannelChart(List<Map<String,Object>> list) throws ParseException {
		if (list == null) {
			return null;
		}
		
		TimeSeries tsPv = new TimeSeries("PV");
		TimeSeries tsAvg = new TimeSeries("人均浏览量");
		TimeSeries tsDl = new TimeSeries("下载量");

		int size = list.size();
		Map item = null;
		for (int i = 0; i < size; i++) {
			item = list.get(i);
			String statdate = DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))),"yyyy/MM/dd");
			item.put("STATDATESTR", statdate);
			
			Date d = DateTimeUtil.getDateFromStr(statdate, "yyyy/MM/dd");
			Double tPv = Double.valueOf(ObjectUtils.toString(item.get("PV")));
			Double pvuv = Double.valueOf(ObjectUtils.toString(item.get("PVUV")));
			Double dlNum = Double.valueOf(ObjectUtils.toString(item.get("DLNUM")));

			Calendar c=Calendar.getInstance();
			c.setTime(d);
			int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DATE);

			// 构造数据集合
			tsPv.add(new Day(day,month,year),tPv);
			tsAvg.add(new Day(day,month,year),pvuv);
			tsDl.add(new Day(day,month,year),dlNum);
		}
		
		TimeSeriesCollection tscPv = new TimeSeriesCollection(tsPv);
		TimeSeriesCollection tscAvg = new TimeSeriesCollection(tsAvg);
		tscAvg.setXPosition(TimePeriodAnchor.MIDDLE);
		TimeSeriesCollection tscDl = new TimeSeriesCollection(tsDl);
		tscDl.setXPosition(TimePeriodAnchor.MIDDLE);
		

		DateAxis dateAxis = new DateAxis(""){
		    @SuppressWarnings("unchecked")
		    protected List<DateTick> refreshTicksHorizontal(Graphics2D g2,
		            Rectangle2D dataArea, RectangleEdge edge) {
		        List ticks = super.refreshTicksHorizontal(g2, dataArea, edge);
		        List<DateTick> newTicks = new ArrayList<DateTick>();
		        for (Iterator it = ticks.iterator(); it.hasNext();) {
		            DateTick tick = (DateTick) it.next();
		            newTicks.add(new DateTick(tick.getDate(), tick.getText(),TextAnchor.TOP_RIGHT, TextAnchor.TOP_RIGHT,-Math.PI / 3));
		        }
		        return newTicks;
		    }
		};
		dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY,1));
		dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy/MM/dd"));
		dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
		
		IntervalXYDataset intervalXYDataset = tscPv;
		Double margin=size==1?0.9D:(size==2?0.8D:(size==3?0.7D:(size==4?0.6D:0.2D)));
		XYBarRenderer xYBarRenderer = new XYBarRenderer(margin);
		xYBarRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"),new DecimalFormat("0.00")));
		xYBarRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xYBarRenderer.setBaseItemLabelsVisible(true);
		Font font = new Font("SansSerif", 10, 12); // 设定字体、类型、字号
		xYBarRenderer.setBaseItemLabelFont(font);
		
		NumberAxis numberAxis1 = new NumberAxis("");
		XYPlot xYPlot = new XYPlot(intervalXYDataset,dateAxis,numberAxis1,xYBarRenderer);
		
		NumberAxis numberAxis2 = new NumberAxis("");
		xYPlot.setRangeAxis(1, numberAxis2);
		
		
		XYDataset dataset1 = tscAvg;
		StandardXYItemRenderer standardXYItemRenderer = new StandardXYItemRenderer();
		standardXYItemRenderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("d-MMM-yyyy"),new DecimalFormat("0.00")));
		xYPlot.setDataset(1, dataset1);
		xYPlot.setRenderer(1, standardXYItemRenderer);
		xYPlot.setOrientation(PlotOrientation.HORIZONTAL);

		XYDataset dataset2 = tscDl;
		xYPlot.setDataset(2, dataset2);
		xYPlot.setRenderer(2, new StandardXYItemRenderer());
		xYPlot.mapDatasetToRangeAxis(2, 1);
		xYPlot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
		xYPlot.setOrientation(PlotOrientation.VERTICAL);
		
	
		String title="渠道推广情况";
		JFreeChart jf = new JFreeChart(title,JFreeChart.DEFAULT_TITLE_FONT,xYPlot,true);
		jf.getLegend().setMargin(50, 2, 5, 2);
		//jf.getLegend().setPosition(RectangleEdge.RIGHT); 
		return jf;
	}
	
	public Workbook getWorkbook(Map<String,Object> paramMap){
 		String beginTime=ObjectUtils.toString(paramMap.get("beginTime"));
 		String endTime=ObjectUtils.toString(paramMap.get("endTime"));
 	
 		Page<Map<String,Object>> page=channelAccessDao.getChannelAccessList(beginTime,endTime);
 		List<Map<String,Object>> list=page.getItems();
 		if(list!=null){
 			int size=list.size();
 			for(int i=0;i<size;i++){
 				Map<String,Object> item=list.get(i);
 				String statdate=DateTimeUtil.getTimeStamp(Long.valueOf(ObjectUtils.toString(item.get("STATDATE"))), "yyyy-MM-dd");
        		item.put("STATDATESTR", statdate);
 			}
 		}
 		String template="static/template/channeltemplate.xlsx";
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
	
	public Workbook getDownloadTemplate(Map<String,Object> paramMap){
		String ct=ObjectUtils.toString(paramMap.get("ct"));
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();  
 		map.put("list",list);
		String template="static/template/3wdaily.xlsx";
		if("3wdaily".equals(ct)){
			template="static/template/3wdaily.xlsx";
			paramMap.put("codedFileName", "3wdaily");
		}
		if("catstat".equals(ct)){
			template="static/template/catstatreport_daily.xlsx";
			paramMap.put("codedFileName", "catstatreport_daily");
		}
		if("channel".equals(ct)){
			template="static/template/channel.xlsx";
			paramMap.put("codedFileName", "channel");
		}
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
