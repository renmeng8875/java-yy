package com.yy.ent.platform.signcar.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yy.ent.commons.protopack.marshal.BeanMarshal;
import com.yy.ent.commons.protopack.util.Uint;

/**
 * MarqueeHeartBatchInfo
 *
 * @author leijing
 * @date 2016/05/16.
 */
public class FansIntimacyBatchInfo extends BeanMarshal{	
	private List<Map<Uint,Uint>> IntimacyList = new ArrayList<Map<Uint,Uint>>();
	private String appData = "signCar";
	private Map<String, String > extend = new HashMap<String, String>();


	public void addMap(Map<Uint,Uint> map) {
		IntimacyList.add(map);
	}


	public Map<String, String> getExtend() {
		return extend;
	}


	public void setExtend(Map<String, String> extend) {
		this.extend = extend;
	}


	public List<Map<Uint, Uint>> getList() {
		return IntimacyList;
	}


	public void setList(List<Map<Uint, Uint>> list) {
		this.IntimacyList = list;
	}


	public String getAppData() {
		return appData;
	}


	public void setAppData(String appData) {
		this.appData = appData;
	}
	
	
}
