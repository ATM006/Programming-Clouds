package org.greatfree.dsf.cps.cache.message.front;

import org.greatfree.dsf.cps.cache.data.MyPointing;
import org.greatfree.dsf.cps.cache.message.TestCacheMessageType;
import org.greatfree.message.ServerMessage;

// Created: 07/13/2018, Bing Li
public class LoadMyPointingResponse extends ServerMessage
{
	private static final long serialVersionUID = 126818371974397884L;
	
	private MyPointing pointing;

	public LoadMyPointingResponse(MyPointing pointing)
	{
		super(TestCacheMessageType.LOAD_MY_POINTING_RESPONSE);
		this.pointing = pointing;
	}

	public MyPointing getPointing()
	{
		return this.pointing;
	}
}
