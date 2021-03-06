package org.greatfree.dsf.multicast.rp.root;

import org.greatfree.concurrency.reactive.NotificationThreadCreatable;
import org.greatfree.dsf.multicast.message.HelloWorldBroadcastNotification;

// Created: 10/21/2018, Bing Li
public class ClientHelloWorldBroadcastNotificationThreadCreator implements NotificationThreadCreatable<HelloWorldBroadcastNotification, ClientHelloWorldBroadcastNotificationThread>
{

	@Override
	public ClientHelloWorldBroadcastNotificationThread createNotificationThreadInstance(int taskSize)
	{
		return new ClientHelloWorldBroadcastNotificationThread(taskSize);
	}

}
