package org.greatfree.dsf.streaming.unicast.child;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.streaming.message.SubscribeNotification;

// Created: 03/23/2020, Bing Li
public class SubscribeNotificationThreadCreator implements NotificationThreadCreatable<SubscribeNotification, SubscribeNotificationThread>
{

	@Override
	public SubscribeNotificationThread createNotificationThreadInstance(int taskSize)
	{
		return new SubscribeNotificationThread(taskSize);
	}

}
