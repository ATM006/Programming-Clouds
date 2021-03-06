package org.greatfree.dsf.cps.cache.terminal.replicating;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.cps.cache.message.replicate.PushMuchMyStoreDataNotification;

// Created: 08/09/2018, Bing Li
public class PushMuchMyStoreDataThreadCreator implements NotificationThreadCreatable<PushMuchMyStoreDataNotification, PushMuchMyStoreDataThread>
{

	@Override
	public PushMuchMyStoreDataThread createNotificationThreadInstance(int taskSize)
	{
		return new PushMuchMyStoreDataThread(taskSize);
	}

}
