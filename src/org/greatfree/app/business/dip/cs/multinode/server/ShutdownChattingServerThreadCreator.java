package org.greatfree.app.business.dip.cs.multinode.server;

import org.greatfree.chat.message.ShutdownServerNotification;
import org.greatfree.concurrency.reactive.NotificationThreadCreatable;

// Created: 04/18/2017, Bing Li
public class ShutdownChattingServerThreadCreator implements NotificationThreadCreatable<ShutdownServerNotification, ShutdownChattingServerThread>
{

	@Override
	public ShutdownChattingServerThread createNotificationThreadInstance(int taskSize)
	{
		return new ShutdownChattingServerThread(taskSize);
	}

}
