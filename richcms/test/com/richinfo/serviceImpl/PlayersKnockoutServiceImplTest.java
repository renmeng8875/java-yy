package com.richinfo.serviceImpl;

import junit.framework.Assert;

import org.junit.Test;

import com.richinfo.AbstractTestCase;
import com.richinfo.playersknockout.service.PlayersKnockoutService;

public class PlayersKnockoutServiceImplTest extends AbstractTestCase
{
	@Test
	public void testGetAppTitle1()
	{
		PlayersKnockoutService playersKnockoutService = (PlayersKnockoutService)this.getBean("PlayersKnockoutService");
		
		String contentid = "300002898296";
		String title = playersKnockoutService.getAppTitle("mm_content_android", contentid);
		Assert.assertEquals("YOO主题-得不到", title);
		
	}
	
	@Test
	public void testGetAppTitle2()
	{
		PlayersKnockoutService playersKnockoutService = (PlayersKnockoutService)this.getBean("PlayersKnockoutService");
		String title = playersKnockoutService.getAppTitle("mm_content_ios", "364709193");
		Assert.assertEquals("iBooks", title);
		
	}
}
