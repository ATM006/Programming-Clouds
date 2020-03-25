package org.greatfree.dip.cps.cache.coordinator.prefetching;

import org.greatfree.concurrency.reactive.NotificationObjectThreadCreatable;
import org.greatfree.dip.cps.cache.message.FetchMyPointingListNotification;

// Created: 07/11/2018, Bing Li
public class PrefetchMyPointingListThreadCreator implements NotificationObjectThreadCreatable<FetchMyPointingListNotification, PrefetchMyPointingListThread>
{

	@Override
	public PrefetchMyPointingListThread createNotificationThreadInstance(int taskSize)
	{
		return new PrefetchMyPointingListThread(taskSize);
	}

}