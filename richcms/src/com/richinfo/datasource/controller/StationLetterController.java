package com.richinfo.datasource.controller;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.richinfo.common.Constants;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.datasource.entity.StationLetter;
import com.richinfo.datasource.service.StationLetterService;
import com.richinfo.privilege.entity.User;

@Controller
@RequestMapping(value = "/StationLetter")
public class StationLetterController {

	private StationLetterService stationLetterService;
	
	@Autowired
	@Qualifier("StationLetterService")
	public void setAnnounceService(StationLetterService stationLetterService) {
		this.stationLetterService = stationLetterService;
	}


	@RequestMapping(value = "/lists.do")
	public String lists(HttpServletRequest request, Model model) 
	{
		List<StationLetter> letterList = stationLetterService.listAll();
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"user"});
        model.addAttribute("letterList", JSONArray.fromObject(letterList,config));
		return "stationletter/lists";
	}

	@RequestMapping(value="/deleteLetter.do")
	@ResponseBody
	public Object deleteStationLetter(HttpServletRequest request, Model model)
	{
		Map<String,String> jsonMap = new HashMap<String, String>();
		String idstr = ServletRequestUtils.getStringParameter(request,"idstr","");
		String[] ids = idstr.split(",");
		for(String id:ids)
		{
			stationLetterService.delete(Integer.valueOf(id));
		}
		jsonMap.put("status", "0");
		return jsonMap;
	}
	
	@RequestMapping(value="/addLetter.do",method=RequestMethod.GET)
	public String getAddStationLetter(HttpServletRequest request, Model model)
	{
		return "stationletter/add";
	}
	
	@RequestMapping(value="/addLetter.do",method=RequestMethod.POST)
	public String addStationLetter(HttpServletRequest request, Model model)
	{
		StationLetter letter = new StationLetter();
		String title = ServletRequestUtils.getStringParameter(request, "letter", "");
		String content = ServletRequestUtils.getStringParameter(request, "content", "");
		int top = ServletRequestUtils.getIntParameter(request, "isTop", 0);
		User currentUser = (User)request.getSession().getAttribute(Constants.CURRENT_USER_ACCOUNT);
		letter.setLetter(title);
		letter.setContent(content);
		letter.setPublisher("管理员");
		letter.setUser(currentUser); // 后台显示的发送者
		letter.setUserNickName(currentUser.getNickName());
		letter.setPublishtime(DateTimeUtil.getTimeStamp());
		letter.setRecivers("1"); // 1表示“全站用户”
		letter.setAnnouncetype(1); // 1表示站内信，其他类型待添加（前期全部为1）
		letter.setTop(top);
		stationLetterService.save(letter);
		model.addAttribute("message", "添加站内信成功！");
		model.addAttribute("url", "/StationLetter/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/editLetter.do",method=RequestMethod.GET)
	public String getEditStationLetter(HttpServletRequest request, Model model)
	{
		int letterId = ServletRequestUtils.getIntParameter(request, "letterid", -1);
		StationLetter letter = stationLetterService.get(letterId);
		model.addAttribute("letter", letter);
		return "stationletter/edit";
	}
	
	@RequestMapping(value="/editLetter.do",method=RequestMethod.POST)
	public String editStationLetter(HttpServletRequest request, Model model)
	{
		int letterId = ServletRequestUtils.getIntParameter(request, "letterid", -1);
		String title = ServletRequestUtils.getStringParameter(request, "letter", "");
		String content = ServletRequestUtils.getStringParameter(request, "content", "");
		int top = ServletRequestUtils.getIntParameter(request, "isTop", 0);
		StationLetter letter = stationLetterService.get(letterId);
		letter.setLetter(title);
		letter.setContent(content);
		letter.setTop(top);
		stationLetterService.update(letter);
		model.addAttribute("message", "修改成功！");
		model.addAttribute("url", "/StationLetter/lists.do");
		return Constants.FORWARDURL;
	}
	
	@RequestMapping(value="/viewLetter.do",method=RequestMethod.GET)
	public String getViewStationLetter(HttpServletRequest request, Model model)
	{
		int letterId = ServletRequestUtils.getIntParameter(request, "letterid", -1);
		StationLetter letter = stationLetterService.get(letterId);
		model.addAttribute("letter", letter);
		return "stationletter/view";
	}
	
	@RequestMapping(value="/checkName.do")
	@ResponseBody
	public Object checkName(HttpServletRequest request, Model model)
	{
		String letter = ServletRequestUtils.getStringParameter(request, "param", "");
		List<StationLetter> list = stationLetterService.listByParam(letter);
		Map<String, String> jsonMap = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			jsonMap.put("info", "为避免混淆，请勿使用已存在的站内信标题！");
			jsonMap.put("status", "n");
			return jsonMap;
		}
		jsonMap.put("status", "y");
		return jsonMap;
	}

	/*
	 * 导出excel
	 */
	@RequestMapping(value="exportExcel.do", method=RequestMethod.GET)
	public void exportLetterToExcel(HttpServletRequest request, HttpServletResponse response){
		String idStr = ServletRequestUtils.getStringParameter(request, "idstr", "");
		List<StationLetter> letters = stationLetterService.listByIds(idStr);
		
		if (letters == null || letters.size() == 0) {
			return;
		}

		try {
			// 文件名需要处理编码，否则下载时无法显示
			String fileName = new String("站内信导出结果.xls".getBytes("GBK"),"ISO8859-1");
			String sheetName = "站内信";
			
			OutputStream os = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		
		    // 创建工作区  
		    WritableWorkbook workbook = Workbook.createWorkbook(os);
		    // 创建新的一页，sheet只能在工作簿中使用  
		    WritableSheet sheet = workbook.createSheet(sheetName, 0);  
		  
		    /*********样式初始化***********/
            //表头样式--表名
            WritableCellFormat tcellFormat1 = new WritableCellFormat();
            tcellFormat1.setAlignment(jxl.format.Alignment.CENTRE);//设置水平居中               
            tcellFormat1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//设置垂直居中             
            tcellFormat1.setWrap(true);//设置自动换行             
            tcellFormat1.setFont(new WritableFont(WritableFont.createFont("楷体_GB2312"),26,WritableFont.NO_BOLD,false,
                    UnderlineStyle.NO_UNDERLINE,Colour.BLACK)); //设置显示的字体样式，字体，字号，是否粗体，字体颜色 

            //表头样式--导出时间
            WritableCellFormat tcellFormat2 = new WritableCellFormat();
            tcellFormat2.setAlignment(jxl.format.Alignment.CENTRE);          
            tcellFormat2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);      
            tcellFormat2.setWrap(true);         
            tcellFormat2.setFont(new WritableFont(WritableFont.createFont("楷体_GB2312"),14,WritableFont.NO_BOLD,false,
                    UnderlineStyle.NO_UNDERLINE,Colour.BLACK)); 
            tcellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            
            //表头样式--标题栏
            WritableCellFormat tcellFormat3 = new WritableCellFormat();
            tcellFormat3.setAlignment(jxl.format.Alignment.CENTRE);          
            tcellFormat3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);      
            tcellFormat3.setWrap(true);         
            tcellFormat3.setFont(new WritableFont(WritableFont.createFont("楷体_GB2312"),14,WritableFont.NO_BOLD,false,
                    UnderlineStyle.NO_UNDERLINE,Colour.BLACK)); 
            tcellFormat3.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            tcellFormat3.setBackground(Colour.GRAY_25);
            
            //内容样式
            WritableCellFormat bcellFormat0 = new WritableCellFormat();
            bcellFormat0.setAlignment(jxl.format.Alignment.CENTRE);          
            bcellFormat0.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);      
            bcellFormat0.setWrap(true);         
            bcellFormat0.setFont(new WritableFont(WritableFont.createFont("宋体"),12,WritableFont.NO_BOLD,false,
                    UnderlineStyle.NO_UNDERLINE,Colour.BLACK)); 
            bcellFormat0.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            
            /*********内容填充，设置行列宽度***********/
            //表头--表名
            sheet.mergeCells(0, 0, 6, 0);//合并单元格
            sheet.setRowView(0, 1000);//设置行高
            sheet.addCell(new Label(0, 0, "Mobile Market移动应用商城 站内信", tcellFormat1));//要插入的单元格内容,第一个是列，第二个是行，第三个是值，第四个是样式
            
            //表头--导出时间
            sheet.mergeCells(0, 1, 3, 1);
            sheet.mergeCells(4, 1, 6, 1);
            sheet.setRowView(1, 650);
            sheet.addCell(new Label(4, 1, "时间：" + DateTimeUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"), tcellFormat2));
            
            //内容
            sheet.setRowView(2, 650); // 标题栏高度
            sheet.setColumnView(1, 10);// “发送人”列宽
            sheet.setColumnView(2, 18);// “站内信标题”列宽
            sheet.setColumnView(3, 50);// “内容”列宽
            sheet.setColumnView(4, 18);// “收件人”列宽
            sheet.setColumnView(5, 18);// “公告类型”列宽
            sheet.setColumnView(6, 25);// “公告类型”列宽
            
            
            sheet.addCell(new Label(0, 2, "编号", tcellFormat3));
            sheet.addCell(new Label(1, 2, "发送人", tcellFormat3));
            sheet.addCell(new Label(2, 2, "标题", tcellFormat3));
            sheet.addCell(new Label(3, 2, "内容", tcellFormat3));
            sheet.addCell(new Label(4, 2, "收件人", tcellFormat3));
            sheet.addCell(new Label(5, 2, "公告类型", tcellFormat3));
            sheet.addCell(new Label(6, 2, "发送时间", tcellFormat3));
            
            int index = 3;
            for (StationLetter letter : letters){
                sheet.setRowView(index, 650);//设置行高
                sheet.addCell(new Label(0, index, "" + letter.getLetterid(), bcellFormat0));
                sheet.addCell(new Label(1, index, "" + letter.getUserNickName(), bcellFormat0));
                sheet.addCell(new Label(2, index, "" + letter.getLetter(), bcellFormat0));
                sheet.addCell(new Label(3, index, "" + letter.getContent(), bcellFormat0));
                sheet.addCell(new Label(4, index, "" + letter.getReciversStr(), bcellFormat0));
                sheet.addCell(new Label(5, index, "" + letter.getAnnounceTypeStr(), bcellFormat0));
                sheet.addCell(new Label(6, index, "" + letter.getPublishTimeStr(), bcellFormat0));
                index++;  // 下一行
            }
            
		    // 将内容写到输出流中，然后关闭工作区，最后关闭输出流  
		    os.flush();
		    workbook.write();
		    workbook.close();
		    os.close(); 
		} catch (RowsExceededException e) {
			e.printStackTrace();
			return ;
		} catch (WriteException e) {
			e.printStackTrace();
			return ;
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}

		return;
	}
	
	@RequestMapping(value = "toImportExcel.do", method = RequestMethod.GET)
	public String toImport(HttpServletRequest request) {
		return "stationletter/import";
	}
	
	@RequestMapping(value = "importExcel.do")
	@ResponseBody
	public Object importExcel(HttpServletRequest request) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		
		String result = null;
		for(String key:fileMap.keySet()){
			 MultipartFile file = fileMap.get(key);
			 try {
				result = stationLetterService.importLetterFromExcel(file.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return "1";
			}
		}
		return result;
	}
	
	@RequestMapping(value = "search.do")
	public String search(HttpServletRequest request, Model model) {
		String paramsStr = ServletRequestUtils.getStringParameter(request, "paramsstr", "");
		String csrfToken = ServletRequestUtils.getStringParameter(request, "csrfToken", "");
		if (paramsStr == null) {
			return "stationletter/lists";
		}
		String[] params = paramsStr.split(",");
		if (params == null || params.length < 0){
			return "stationletter/lists";
		}
		
		List<StationLetter> list = stationLetterService.search(params);
		JsonConfig config = new JsonConfig();
		config.setExcludes(new String[]{"user"});
        model.addAttribute("letterList", JSONArray.fromObject(list,config));
        model.addAttribute("csrfToken", csrfToken);
		return "stationletter/lists";
	}
}
