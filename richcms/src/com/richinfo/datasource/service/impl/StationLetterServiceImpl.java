package com.richinfo.datasource.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.richinfo.common.dao.BaseDao;
import com.richinfo.common.service.impl.BaseServiceImpl;
import com.richinfo.common.utils.dateUtil.DateTimeUtil;
import com.richinfo.datasource.dao.StationLetterDao;
import com.richinfo.datasource.entity.StationLetter;
import com.richinfo.datasource.service.StationLetterService;
import com.richinfo.stat.entity.ChannelAccess;
import com.richinfo.stat.entity.ChannelAccessExl;

@Service("StationLetterService")
public class StationLetterServiceImpl extends BaseServiceImpl<StationLetter, Integer> implements StationLetterService {

	private StationLetterDao stationLetterDao;
	
	@Autowired
	@Qualifier("StationLetterDao")
	public void setStationLetterDao(StationLetterDao stationLetterDao){
		this.stationLetterDao = stationLetterDao;
	}
	
	@Autowired
	@Qualifier("StationLetterDao")
	@Override
	public void setBaseDao(BaseDao<StationLetter, Integer> baseDao) {
		this.baseDao = (StationLetterDao)baseDao;
	}

	@Override
	public List<StationLetter> listByParam(String letter) {
		String hql = "from StationLetter s where s.letter=?";
		return stationLetterDao.list(hql, new String[]{letter}, null);
	}

	@Override
	public List<StationLetter> listByIds(String ids) {
		String hql = "from StationLetter s ";
		if (ids != null && !ids.equals("")) {
			hql = hql + "where s.letterid in (" + ids + ")";
		}
		return stationLetterDao.list(hql, new String[]{}, null);
	}
	
	
	
	@Override
	public String importLetterFromExcel(InputStream in) {
		try {
			Workbook book = Workbook.getWorkbook(in);
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows();
			
			// 从第3行开始读数据
			for (int row = 1; row < rowNum; row++){
				Cell[] cells = sheet.getRow(row);
				// 三条数据都要有
				if (cells.length == 3) {
					String title = cells[0].getContents();
					String content = cells[1].getContents();
					String time = cells[2].getContents();
					// 导入的数据至少有个标题
					if (title != null && !title.equals("")) {
					
						List<StationLetter> oldLetter = getLetterByTitle(title);
						if (oldLetter == null || oldLetter.size() == 0){
							StationLetter letter = new StationLetter();
							letter.setLetter(title);
							letter.setContent(content);
							letter.setPublisher("管理员");
							letter.setPublishtime(DateTimeUtil.converToTimestamp(time, "yyyy-MM-dd"));
							letter.setRecivers("1"); // 1表示“全站用户”
							letter.setAnnouncetype(1); // 1表示站内信，其他类型待添加（前期全部为1）
							letter.setTop(0); // 未置顶
							stationLetterDao.add(letter);
						}
					}
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
			return "1";
		} catch (IOException e) {
			e.printStackTrace();
			return "1";
		}
		return "0";
	}

	// 前端验证决定，title一般不会重复
	public List<StationLetter> getLetterByTitle(String title) {
		String hql = "from StationLetter s where s.letter=?";
		return stationLetterDao.list(hql, new Object[]{title}, null);
	}

	@Override
	public List<StationLetter> search(String[] params) {
		
		String hql = "from StationLetter s ";
		
		for (int i = 0; i < params.length; i++) {
			if (i == 0 ){
				hql += "where (s.letter like '%" + params[i] + "%' or s.content like '%" + params[i] + "%') ";
			} else {
				hql += " and (s.letter like '%" + params[i] + "%' or s.content like '%" + params[i] + "%') ";
			}
		}
		System.out.println("搜索hql:  " + hql);
		return stationLetterDao.list(hql, null, null);
	}
}
