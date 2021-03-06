package org.greatfree.dsf.multicast.rp.child;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.multicast.message.HelloWorldAnycastNotification;

// Created: 10/22/2018, Bing Li
public class HelloWorldAnycastNotificationThreadCreator implements NotificationThreadCreatable<HelloWorldAnycastNotification, HelloWorldAnycastNotificationThread>
{

	@Override
	public HelloWorldAnycastNotificationThread createNotificationThreadInstance(int taskSize)
	{
		return new HelloWorldAnycastNotificationThread(taskSize);
	}

}
