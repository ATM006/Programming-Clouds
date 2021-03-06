package org.greatfree.dsf.multicast.rp.child;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.multicast.message.HelloWorldUnicastNotification;

// Created: 10/22/2018, Bing Li
public class HelloWorldUnicastNotificationThreadCreator implements NotificationThreadCreatable<HelloWorldUnicastNotification, HelloWorldUnicastNotificationThread>
{

	@Override
	public HelloWorldUnicastNotificationThread createNotificationThreadInstance(int taskSize)
	{
		return new HelloWorldUnicastNotificationThread(taskSize);
	}

}
