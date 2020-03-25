package org.greatfree.dip.cps.cache.message.front;

import java.util.List;

import org.greatfree.dip.cps.cache.data.MyCachePointing;
import org.greatfree.dip.cps.cache.message.TestCacheMessageType;
import org.greatfree.message.ServerMessage;

// Created: 08/03/2018, Bing Li
public class TopPointingsPrefetchListResponse extends ServerMessage
{
	private static final long serialVersionUID = -1964185842127458901L;
	
	private List<MyCachePointing> pointings;

	public TopPointingsPrefetchListResponse(List<MyCachePointing> pointings)
	{
		super(TestCacheMessageType.TOP_POINTINGS_PREFETCH_LIST_RESPONSE);
		this.pointings = pointings;
	}

	public List<MyCachePointing> getPointings()
	{
		return this.pointings;
	}
}