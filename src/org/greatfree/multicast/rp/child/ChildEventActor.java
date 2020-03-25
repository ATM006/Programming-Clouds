package org.greatfree.multicast.rp.child;

import java.io.IOException;

import org.greatfree.concurrency.Async;
import org.greatfree.exceptions.DistributedNodeFailedException;
import org.greatfree.message.multicast.MulticastMessage;

// Created: 10/14/2018, Bing Li
class ChildEventActor extends Async<MulticastMessage>
{
	private ChildSyncMulticastor multicastor;
	
	public ChildEventActor(ChildSyncMulticastor multicastor)
	{
		this.multicastor = multicastor;
	}

	@Override
	public void perform(MulticastMessage notification)
	{
		try
		{
			this.multicastor.notify(notification);
		}
		catch (IOException | DistributedNodeFailedException e)
		{
			e.printStackTrace();
		}
	}

}