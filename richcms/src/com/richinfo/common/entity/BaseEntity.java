package com.richinfo.common.entity;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable
{

	private static final long serialVersionUID = -6653149534097851997L;
	
	public abstract String[] getExcludesAttributes(); 
	

}
