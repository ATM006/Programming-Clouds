package org.greatfree.dsf.cps.cache.coordinator.front;

import java.io.IOException;

import org.greatfree.concurrency.reactive.RequestQueue;
import org.greatfree.data.ServerConfig;
import org.greatfree.dsf.cps.cache.coordinator.MySortedDistributedList;
import org.greatfree.dsf.cps.cache.message.front.LoadRangeMyPointingsRequest;
import org.greatfree.dsf.cps.cache.message.front.LoadRangeMyPointingsResponse;
import org.greatfree.dsf.cps.cache.message.front.LoadRangeMyPointingsStream;
import org.greatfree.exceptions.DistributedListFetchException;

// Created: 07/13/2018, Bing Li
public class LoadRangeMyPointingsThread extends RequestQueue<LoadRangeMyPointingsRequest, LoadRangeMyPointingsStream, LoadRangeMyPointingsResponse>
{

	public LoadRangeMyPointingsThread(int maxTaskSize)
	{
		super(maxTaskSize);
	}

	@Override
	public void run()
	{
		LoadRangeMyPointingsStream request;
		LoadRangeMyPointingsResponse response;
		while (!this.isShutdown())
		{
			while (!this.isEmpty())
			{
				request = this.getRequest();
				try
				{
					response = new LoadRangeMyPointingsResponse(MySortedDistributedList.MIDDLE().get(request.getMessage().getStartIndex(), request.getMessage().getEndIndex()));
					this.respond(request.getOutStream(), request.getLock(), response);
					this.disposeMessage(request, response);
				}
				catch (IOException | DistributedListFetchException e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				this.holdOn(ServerConfig.REQUEST_THREAD_WAIT_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
